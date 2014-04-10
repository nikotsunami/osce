package ch.unibas.medizin.osce.domain.spportal;

import java.io.StringWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;



import ch.unibas.medizin.osce.domain.AnamnesisCheck;
import ch.unibas.medizin.osce.domain.AnamnesisCheckTitle;
import ch.unibas.medizin.osce.domain.AnamnesisChecksValue;
import ch.unibas.medizin.osce.domain.AnamnesisForm;
import ch.unibas.medizin.osce.domain.Bankaccount;
import ch.unibas.medizin.osce.domain.Nationality;
import ch.unibas.medizin.osce.domain.Person;
import ch.unibas.medizin.osce.domain.Profession;
import ch.unibas.medizin.osce.domain.Scar;
import ch.unibas.medizin.osce.domain.StandardizedPatient;
import ch.unibas.medizin.osce.server.util.email.impl.EmailServiceImpl;
import ch.unibas.medizin.osce.shared.EditRequestState;
import ch.unibas.medizin.osce.shared.OsMaConstant;


@RooJavaBean
@RooToString
@RooEntity
@PersistenceContext(unitName="spportalPersistenceUnit")
@Table(name="person")
public class SpPerson {
	
	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
	
	 @NotNull(message="emailMayNotBeNull")
	 @Column(unique = true)
	 @Size(min = 7, max = 50,message="emailMinMaxSize")
	 @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$",message="emailNotValid")
	 private String email;
	 
	 private String password;
	 
	 @Temporal(TemporalType.TIMESTAMP)
	 @DateTimeFormat(pattern="dd.MM.YYYY hh:mm:ss")
	 private Date expiration;
	 
	/* @OneToOne(cascade = CascadeType.ALL)
	 private SpStandardizedPatient standardizedPatient;*/
	
	 @NotNull(message="isFirstLoginMayNotBeNull")
	 private Boolean isFirstLogin;
	 
	 @NotNull(message="editRequestStateMayNotBeNull")
	 @Enumerated
	 private EditRequestState editRequestState;
	 
	 private String activationUrl;

	 private String token;
	 
	 private Boolean changed;
	 
	 private static transient Logger log = Logger.getLogger(SpPerson.class);
	 
	// This method is used to save standardized patient data in sp portal db.
	 	@Transactional
		public static void insertStandardizedPatientDetailsInSPportal(Long standardizedPatinetId){
	 		
	 		log.info("insertStandardizedPatientDetailsInSPportal() called");
			SpPerson spportalUser = new SpPerson();
			log.info("taking sp based on given id from database");
			StandardizedPatient standardizedPatient = StandardizedPatient.findStandardizedPatient(standardizedPatinetId);
			
			String randomString = RandomStringUtils.randomAlphanumeric(OsMaConstant.RANDOM_STRING_LENGTH);
			
			Person newSP = new Person();
			
			newSP.setActivationUrl(randomString);
			
			newSP.setEmail(standardizedPatient.getEmail());
			
			Date date = new Date();
			int currentHour =date.getHours();
			date.setHours(currentHour+4);
			
			newSP.setExpiration(date);
			
			newSP.setIsFirstLogin(true);
			
			newSP.setEditRequestState(EditRequestState.APPROVED);
			
			standardizedPatient.setPerson(newSP);
			
			standardizedPatient.persist();
			
			spportalUser.saveStandardizedPatientDetailsInSpPortal(standardizedPatient,randomString);
		}

		public EntityManager getEntityManager() {
			//return spportalEntityManagerFactory.createEntityManager();
			return entityManager;
			
		}

		/**
		 * This method is used to actually save all patient data in sp portal database
		 * @param standardizedPatient
		 */
		@Transactional("spportalTransactionManager")
		public void saveStandardizedPatientDetailsInSpPortal(StandardizedPatient standardizedPatient,String randomStr){
			
			try {	
				//creating entity factory based on persistent unit name for sp portal.
				EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");
				//creating entity manager from entity factory.
				EntityManager em = emFactory.createEntityManager();
				
				//EntityManager em =getEntityManager(); /*spportalUser.getEntityManager();*/
				
				log.info("sp portal em is : " + em);
				//Creating random string that will be stored in sp portal user table as valid url and also used when mail in sent to user.
				String randomString = randomStr;
				
				EntityTransaction transaction =	em.getTransaction();
				transaction.begin();
				
				SpStandardizedPatient spStandardizedPatient = initializeStandardizedPatientAndAnamnesisCheckValueDetails(em,standardizedPatient);
				
				// now actually saving all sp details in sp portal database.
				boolean isSaved=saveUserAndSpDetailInSpPortalDatabase(em,spStandardizedPatient,randomString);
				
				if(isSaved){
					//now sending email to sp so sp can now use sp portal.
					sendEmailToStandardizedPatient(standardizedPatient,randomString);
				}
				//committing transaction
				transaction.commit();
				
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Exception occured during persisting patient data in sp portal db." + e.getMessage(),e);
		}
		}

		/**
		 * This method saves user details in sp portal db.
		 * @param em
		 * @param standardizedPatient
		 * @param randomString
		 */
		@Transactional("spportalTransactionManager")
		private boolean  saveUserAndSpDetailInSpPortalDatabase(EntityManager em,SpStandardizedPatient spStandardizedPatient,String randomString){
			log.info("saving sp portal user details");
			boolean isSaved=true;
			try{
				SpPerson spportalUser = new SpPerson();
				
				spportalUser.setActivationUrl(randomString);
				
				spportalUser.setEmail(spStandardizedPatient.getEmail());
				
				Date date = new Date();
				int currentHour =date.getHours();
				date.setHours(currentHour+4);
				
				spportalUser.setExpiration(date);
				
				spportalUser.setIsFirstLogin(true);
				
				spportalUser.setEditRequestState(EditRequestState.APPROVED);
				
				spStandardizedPatient.setPerson(spportalUser);
				//spportalUser.setStandardizedPatient(spStandardizedPatient);
				
				log.info("user detail that is saving in sp portal db is : " + spportalUser);
				
				em.persist(/*spportalUser*/spStandardizedPatient);
				
				log.info("user and patient details saved in sp portal database successfully");
				
			}catch(Exception e){
				isSaved=false;
				log.info("user and patient details saved failure in SP portal database");
				log.error(e.getMessage(),e);
				return isSaved;
			}
			return isSaved;
		}
		/**
		 * This method is used to send email to patient
		 * @param standardizedPatient
		 * @param randomString
		 */
		private void sendEmailToStandardizedPatient(StandardizedPatient standardizedPatient,String randomString) {
			
			@SuppressWarnings("deprecation")
			HttpServletRequest request = com.google.gwt.requestfactory.server.RequestFactoryServlet.getThreadLocalRequest();
			
			HttpSession session = request.getSession();
			
			ServletContext servletContex =session.getServletContext();
			
			WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContex);
			 
			
			EmailServiceImpl emailServiceImpl =applicationContext.getBean(EmailServiceImpl.class);
			 
			VelocityEngine velocityEngine = applicationContext.getBean(VelocityEngine.class);
			
			velocityEngine.init();
			
			VelocityContext velocityContext = new VelocityContext();
			
			velocityContext.put("randomString",randomString);
			
			Template template = velocityEngine.getTemplate("templates/emailTemplate.vm");
			
			StringWriter writer =new StringWriter();
			
			template.merge(velocityContext,writer);
			
			String emailContent = writer.toString(); //VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,"/templates/emailTemplate.vm", "UTF-8",null);
			log.info("email that is sening to sp is" + emailContent);
			
			//sending email to user
			emailServiceImpl.sendMail( new String [] {standardizedPatient.getEmail()},emailContent);
		}
		/**
		 * This method is used to save standardized patient data including anamnesis check value in sp portal database.
		 * @param em
		 * @param standardizedPatient
		 * @return
		 */
		@Transactional("spportalTransactionManager")
		private SpStandardizedPatient initializeStandardizedPatientAndAnamnesisCheckValueDetails(EntityManager em,StandardizedPatient standardizedPatient){

			SpNationality spNationality =initializeNationalityDetailForSpPortal(standardizedPatient);
			
			SpBankaccount spBankaccount =initializeBankAccountDetailForSpPortal(standardizedPatient,spNationality);
			
			SpProfession spProfession =  initializeProfessionDetailForSpPortal(standardizedPatient);
			
			SpAnamnesisForm spAnamnesisForm=initializeAnamnesisFormDetailForSpPortal(standardizedPatient);
			
			// In case if spAnamnesisForm is null we can not persist setSpAnamnesisChecksValues so making sure that we have anamnesis form and then persisting rest dependent data.
			if(spAnamnesisForm !=null){
				
				//If anamnesis form is not null saving dependent data (anamnesis_check_value data).
				Set<SpScar> spScars = initializeScarDetailForSpPortal(standardizedPatient);
				
				Set<SpAnamnesisChecksValue> setSpAnamnesisChecksValues = initializeAnamnesisCheckValuesForSpPortal(em,standardizedPatient,spAnamnesisForm);
	
				spAnamnesisForm.setScars(spScars);
				
				spAnamnesisForm.setAnamnesischecksvalues(setSpAnamnesisChecksValues);
			}
			SpStandardizedPatient spStandardizedPatient=initializeStandardizedPatientDetailForSpPortal(spNationality,spBankaccount,spProfession,standardizedPatient,spAnamnesisForm);
			
			log.info("returning sp standardized patient");
			return spStandardizedPatient;
		}

		/**
		 * This method is used to copy nationality detail of standardized patient and initialize sp-poratal nationality detail
		 * @param standardizedPatient
		 * @return
		 */
		private SpNationality initializeNationalityDetailForSpPortal(StandardizedPatient standardizedPatient) {
			log.info("initializing Nationality details for sp portal");
			try{
					Nationality nationality =standardizedPatient.getNationality();
					
					SpNationality spNationality =null;
					
					if(nationality!=null){
					
						spNationality= new SpNationality();
						 
						spNationality.setNationality(nationality.getNationality());
						
						log.info("Nationality detail that will be saved in sp portal db is : " + spNationality);
						
					}
					
					return spNationality;
			}catch(Exception e){
				log.info("Nationality detail initialization failure for SP portal database");
				log.error(e.getMessage(),e);
				return null;
			}
			
		}
		/**
		 * This method is used to copy BankAccount detail of standardized patient and initialize sp-poratal BankAccount detail
		 * @param standardizedPatient
		 * @param spNationality
		 * @return
		 */
		private SpBankaccount initializeBankAccountDetailForSpPortal(StandardizedPatient standardizedPatient,SpNationality spNationality) {
			log.info("initializing bank account details for sp portal ");
			try{
					Bankaccount bankAccount =standardizedPatient.getBankAccount();
					
					SpBankaccount spBankAccount=null;
					
					if(bankAccount !=null ) {
			
						spBankAccount = new SpBankaccount();
						
						spBankAccount.setBankName(bankAccount.getBankName());
						spBankAccount.setBIC(bankAccount.getBIC());
						spBankAccount.setCity(bankAccount.getCity());
						spBankAccount.setCountry(spNationality);
						spBankAccount.setIBAN(bankAccount.getIBAN());
						spBankAccount.setOwnerName(bankAccount.getOwnerName());
						spBankAccount.setPostalCode(bankAccount.getPostalCode());
						
						log.info("bank account detail that will be saved in sp portal db is :  " + spBankAccount);
						
					}
					return spBankAccount;
			}catch(Exception e){
				log.info("bank account detail initialization failure for SP portal database");
				log.error(e.getMessage(),e);
				return null;
			}
			
		}
		/**
		 * This method is used to copy profession detail of standardized patient and initialize sp-poratal profession detail
		 * @param standardizedPatient
		 * @return
		 */
		private SpProfession initializeProfessionDetailForSpPortal(StandardizedPatient standardizedPatient) {
			log.info("initializing profession details for sp portal ");
			try{
					Profession profession = standardizedPatient.getProfession();
					
					SpProfession spProfession=null;
					
					if(profession !=null ) {
			
						spProfession= new SpProfession();
						
						spProfession.setProfession(profession.getProfession());
						
						log.info("profession detail that will be saved in sp portal db is : " + spProfession);
						
					}
					return spProfession;
			}catch(Exception e){
				log.info("profession detail save failure in SP portal database");
				log.error(e.getMessage(),e);
				return null;
			}
			
		}
		/**
		 * This method is used to copy Anamnesis Form detail of standardized patient and initialize sp-poratal Anamnesis Form detail
		 * @param standardizedPatient
		 * @return
		 */
		private SpAnamnesisForm initializeAnamnesisFormDetailForSpPortal(StandardizedPatient standardizedPatient) {
			log.info("initializing anamnesis from details for sp portal ");
			try{
					AnamnesisForm anamnesisForm = standardizedPatient.getAnamnesisForm();
					
					SpAnamnesisForm spAnamnesisForm=null;
					
					// This is not possible that form is null still checking.
					if(anamnesisForm !=null ) {
			
						spAnamnesisForm= new SpAnamnesisForm();
						
						spAnamnesisForm.setCreateDate(anamnesisForm.getCreateDate());
						
						log.info("anamneis form detail that will ne saved in sp portal db is : " + spAnamnesisForm);
						
					}
					return spAnamnesisForm;
			}catch(Exception e){
				log.info("spAnamnesisForm detail initialization failure for SP portal database");
				log.error(e.getMessage(),e);
				return null;
			}
			
		}
		/**
		 * This method is used to copy scar detail of standardized patient and initialize sp-poratal scar detail.
		 * @param standardizedPatient
		 * @return
		 */
		private Set<SpScar> initializeScarDetailForSpPortal(StandardizedPatient standardizedPatient) {
			log.info("initializing anamnesis from details for sp portal ");
			try{
					Set<SpScar> spScars = new HashSet<SpScar>();
					
					if(standardizedPatient.getAnamnesisForm() !=null){
					
						Set<Scar> scars = standardizedPatient.getAnamnesisForm().getScars();
					
						for (Iterator iterator = scars.iterator(); iterator.hasNext();) {
						
							Scar scar = (Scar) iterator.next();
						
							if(scar !=null){
							
							SpScar spScare =new SpScar();
							spScare.setBodypart(scar.getBodypart());
							spScare.setTraitType(scar.getTraitType());

							spScars.add(spScare);
							
						}
						
					  }
					}
					log.info("returning set of scar");
					return spScars;
			}catch(Exception e){
				log.info("scar detail initialization failure for SP portal database");
				log.error(e.getMessage(),e);
				return null;
			}
			
		}
		/**
		 * This method is used to copy standardized patient detail of osce standardized patient and initialize sp-poratal standardized patient detail
		 * @param spNationality
		 * @param spBankaccount
		 * @param spProfession
		 * @param standardizedPatient
		 * @param spAnamnesisForm
		 * @return
		 */
		private SpStandardizedPatient initializeStandardizedPatientDetailForSpPortal(SpNationality spNationality, SpBankaccount spBankaccount,SpProfession spProfession,
				StandardizedPatient standardizedPatient,SpAnamnesisForm spAnamnesisForm) {
			
			log.info("initializing standardized patient details for sp portal ");
			try{
					
					SpStandardizedPatient spStandardizedPatient=null;
					
					if(standardizedPatient !=null ) {
			
						spStandardizedPatient= new SpStandardizedPatient();
						
						spStandardizedPatient.setAnamnesisForm(spAnamnesisForm);
						spStandardizedPatient.setBankAccount(spBankaccount);
						spStandardizedPatient.setBirthday(standardizedPatient.getBirthday());
						spStandardizedPatient.setCity(standardizedPatient.getCity());
						spStandardizedPatient.setEmail(standardizedPatient.getEmail());
						spStandardizedPatient.setGender(standardizedPatient.getGender());
						spStandardizedPatient.setHeight(standardizedPatient.getHeight());
						spStandardizedPatient.setMaritalStatus(standardizedPatient.getMaritalStatus());
						spStandardizedPatient.setMobile(standardizedPatient.getMobile());
						spStandardizedPatient.setName(standardizedPatient.getName());
						spStandardizedPatient.setNationality(spNationality);
						spStandardizedPatient.setPostalCode(standardizedPatient.getPostalCode());
						spStandardizedPatient.setPreName(standardizedPatient.getPreName());
						spStandardizedPatient.setProfession(spProfession);
						spStandardizedPatient.setSocialInsuranceNo(standardizedPatient.getSocialInsuranceNo());
						spStandardizedPatient.setStreet(standardizedPatient.getStreet());
						spStandardizedPatient.setTelephone(standardizedPatient.getTelephone());
						spStandardizedPatient.setTelephone2(standardizedPatient.getTelephone2());
						spStandardizedPatient.setWeight(standardizedPatient.getWeight());
						spStandardizedPatient.setWorkPermission(standardizedPatient.getWorkPermission());
						
						log.info("standardized patient detail that will be saved in sp portal db is : " + spStandardizedPatient);
						
					}
					return spStandardizedPatient;
			}catch(Exception e){
				log.info("standardized patiemt initialization failure for SP portal database");
				log.error(e.getMessage(),e);
				return null;
			}
		}
		/**
		 * This method is used to copy Anamnesis ChecksValue detail of standardized patient and initialize sp-poratal Anamnesis ChecksValue detail.
		 * @param em
		 * @param standardizedPatient
		 * @param spAnamnesisForm
		 * @return
		 */
		@Transactional("spportalTransactionManager")
		private Set<SpAnamnesisChecksValue> initializeAnamnesisCheckValuesForSpPortal(EntityManager em,StandardizedPatient standardizedPatient, SpAnamnesisForm spAnamnesisForm) {
			/**
			 * I have persisted data in SpAnamnesisCheck and  SpAnamnesisCheck using script but when we complete development we need that this data must be available in 
			 * sp poretal database before we persist anamnesis_check_value for sp <spec-india>.
			 */
			log.info("initializing anamnesis check value for sp portal ");
			try{
					Set<AnamnesisChecksValue> setAnamnesisChecksValues = standardizedPatient.getAnamnesisForm().getAnamnesischecksvalues();
					
					Set<SpAnamnesisChecksValue> setSpAnamnesisChecksValues = new HashSet<SpAnamnesisChecksValue>();
					
					for (Iterator iterator = setAnamnesisChecksValues.iterator(); iterator.hasNext();) {
						
						AnamnesisChecksValue anamnesisChecksValue = (AnamnesisChecksValue) iterator.next();
						
						if(anamnesisChecksValue !=null ) {
							
							AnamnesisCheck anamnesisCheck = anamnesisChecksValue.getAnamnesischeck();
							
							AnamnesisCheckTitle anamnesisCheckTitle = anamnesisCheck.getAnamnesisCheckTitle();
							
							//initializing anamnesisCheckTitle value.
							String queryStringForAnamnesisCheckTitle ="SELECT act FROM SpAnamnesisCheckTitle as act where act.text='"+anamnesisCheckTitle.getText()+"'" ;
						    
						    TypedQuery<SpAnamnesisCheckTitle> queryForAnamnesisCheckTitle = em.createQuery(queryStringForAnamnesisCheckTitle, SpAnamnesisCheckTitle.class);
						    
							SpAnamnesisCheckTitle spAnamnesisCheckTitle = queryForAnamnesisCheckTitle.getSingleResult();
							
							spAnamnesisCheckTitle.setSort_order(anamnesisCheckTitle.getSort_order());
							spAnamnesisCheckTitle.setText(anamnesisCheckTitle.getText());
							
							//initializing AnamnesisCheck value.	
							String queryStringForAnamnesisCheck="SELECT ac FROM SpAnamnesisCheck as ac where ac.text='"+anamnesisCheck.getText()+"' and ac.sort_order=" + anamnesisCheck.getSort_order();
						    
						    TypedQuery<SpAnamnesisCheck> queryForAnamnesisCheck = em.createQuery(queryStringForAnamnesisCheck, SpAnamnesisCheck.class);
						    
							SpAnamnesisCheck spAnamnesisCheck =queryForAnamnesisCheck.getSingleResult();
							
							spAnamnesisCheck.setAnamnesisCheckTitle(spAnamnesisCheckTitle);
							spAnamnesisCheck.setSendToDMZ(anamnesisCheck.getSendToDMZ());
							spAnamnesisCheck.setText(anamnesisCheck.getText());
							spAnamnesisCheck.setSort_order(anamnesisCheck.getSort_order());
							spAnamnesisCheck.setType(anamnesisCheck.getType());
							spAnamnesisCheck.setUserSpecifiedOrder(anamnesisCheck.getUserSpecifiedOrder());
							spAnamnesisCheck.setValue(anamnesisCheck.getValue());
							
							//initializing SpAnamnesisChecksValue value.	
							SpAnamnesisChecksValue spAnamnesisChecksValue = new SpAnamnesisChecksValue();
							spAnamnesisChecksValue.setAnamnesischeck(spAnamnesisCheck);
						    spAnamnesisChecksValue.setAnamnesisChecksValue(anamnesisChecksValue.getAnamnesisChecksValue());
							spAnamnesisChecksValue.setAnamnesisform(spAnamnesisForm);
							spAnamnesisChecksValue.setComment(anamnesisChecksValue.getComment());
							spAnamnesisChecksValue.setTruth(anamnesisChecksValue.getTruth());
							
							setSpAnamnesisChecksValues.add(spAnamnesisChecksValue);
						}
					}
					log.info("returning set of anamesis check value set");
					return setSpAnamnesisChecksValues;
			}catch(Exception e){
				log.info("sp anamnesis check value, anamnesis check title and anamnesis check detail initialization failure for SP portal database");
				log.error(e.getMessage(),e);
				return null;
			}
			
		}
}

