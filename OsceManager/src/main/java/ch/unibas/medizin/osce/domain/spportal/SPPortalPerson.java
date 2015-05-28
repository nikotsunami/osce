package ch.unibas.medizin.osce.domain.spportal;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
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
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ch.unibas.medizin.osce.domain.AnamnesisCheck;
import ch.unibas.medizin.osce.domain.AnamnesisCheckTitle;
import ch.unibas.medizin.osce.domain.AnamnesisChecksValue;
import ch.unibas.medizin.osce.domain.AnamnesisForm;
import ch.unibas.medizin.osce.domain.Bankaccount;
import ch.unibas.medizin.osce.domain.Nationality;
import ch.unibas.medizin.osce.domain.Profession;
import ch.unibas.medizin.osce.domain.Scar;
import ch.unibas.medizin.osce.domain.StandardizedPatient;
import ch.unibas.medizin.osce.server.util.email.impl.EmailServiceImpl;
import ch.unibas.medizin.osce.shared.EditRequestState;
import ch.unibas.medizin.osce.shared.StandardizedPatientStatus;


@Entity
@Configurable
@PersistenceContext(unitName="spportalPersistenceUnit")
@Table(name="person")
public class SPPortalPerson {
	
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
	 
	 @OneToMany(cascade = CascadeType.ALL, mappedBy = "person")
	private Set<SpPatientInSemester> patientInSemester = new HashSet<SpPatientInSemester>();
	 
	 private static transient Logger log = Logger.getLogger(SPPortalPerson.class);
	 
	// This method is used to save standardized patient data in sp portal db.
	 	//@Transactional
		public static void insertStandardizedPatientDetailsInSPportal(Long standardizedPatinetId){
	 		
	 		log.info("insertStandardizedPatientDetailsInSPportal() called");
			SPPortalPerson spportalUser2 = new SPPortalPerson();
			log.info("taking sp based on given id from database");
			StandardizedPatient standardizedPatient = StandardizedPatient.findStandardizedPatient(standardizedPatinetId);
			
			String randomString = RandomStringUtils.randomAlphanumeric(64);
			
			spportalUser2.saveStandardizedPatientDetailsInSpPortal(standardizedPatient,randomString);
			
			SPPortalPerson spPortalUser=findSpPortalPersonBasedOnEmailAddress(standardizedPatient.getEmail());
			
			standardizedPatient.setStatus(StandardizedPatientStatus.EXPORTED);
			
			standardizedPatient.setSpPortalPersonId(spPortalUser.getId());
			
			
			standardizedPatient.setCreated(new Date());
			
			standardizedPatient.persist();
		}

		public EntityManager getEntityManager() {
			//return spportalEntityManagerFactory.createEntityManager();
			return entityManager;
			
		}
		
		/**
		 * This method is used to actually save all patient data in sp portal database
		 * @param standardizedPatient
		 */
		//@Transactional("spportalTransactionManager")
		public void saveStandardizedPatientDetailsInSpPortal(StandardizedPatient standardizedPatient,String randomStr){
			
			try {	
				//creating entity factory based on persistent unit name for sp portal.
				/*EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");
				//creating entity manager from entity factory.
				EntityManager em = emFactory.createEntityManager();*/
				
				EntityManager em = entityManager;
				
				//EntityManager em =getEntityManager(); /*spportalUser.getEntityManager();*/
				
				log.info("sp portal em is : " + em);
				//Creating random string that will be stored in sp portal user table as valid url and also used when mail in sent to user.
				String randomString = randomStr;
				
				/*EntityTransaction transaction =	em.getTransaction();
				transaction.begin();*/
				
				SpStandardizedPatient spStandardizedPatient = initializeStandardizedPatientAndAnamnesisCheckValueDetails(/*em,*/standardizedPatient);
				
				// now actually saving all sp details in sp portal database.
				boolean isSaved=saveUserAndSpDetailInSpPortalDatabase(em,spStandardizedPatient,randomString,standardizedPatient);
				
				if(isSaved){
					//now sending email to sp so sp can now use sp portal.
					sendEmailToStandardizedPatient(standardizedPatient,randomString);
				}
				//committing transaction
				//transaction.commit();
				
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
		//@Transactional("spportalTransactionManager")
		private boolean  saveUserAndSpDetailInSpPortalDatabase(EntityManager em,SpStandardizedPatient spStandardizedPatient,String randomString,StandardizedPatient standardizedPatient){
			log.info("saving sp portal user details");
			boolean isSaved=true;
			try{
				SPPortalPerson spportalUser = new SPPortalPerson();
				
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
				
				//em.persist(/*spportalUser*/spStandardizedPatient);
				spStandardizedPatient.setStatus(StandardizedPatientStatus.EXPORTED);
				spStandardizedPatient.persist();

				//em.flush();
				
				//Now saving sp anamnesis check values in spportal.
				saveAnamnesisCheckValueinSpportal(spStandardizedPatient,standardizedPatient);
				
				log.info("user and patient details saved in sp portal database successfully");
				
			}catch(Exception e){
				isSaved=false;
				log.info("user and patient details saved failure in SP portal database");
				log.error(e.getMessage(),e);
				return isSaved;
			}
			return isSaved;
		}
		
		@Transactional
		private void saveAnamnesisCheckValueinSpportal(SpStandardizedPatient spStandardizedPatient, StandardizedPatient standardizedPatient) {
			
			spStandardizedPatient = SpStandardizedPatient.findSpStandardizedPatient(spStandardizedPatient.getId());
			
			Set<AnamnesisChecksValue> setOfAnamChecksValues = standardizedPatient.getAnamnesisForm().getAnamnesischecksvalues();
			
			EntityManager em = SPPortalPerson.entityManager();
		
			Long spFormId=  spStandardizedPatient.getAnamnesisForm().getId();
			
			StringBuilder sql = new StringBuilder("INSERT INTO `anamnesis_checks_value` (`anamnesis_checks_value`,`comment`,`truth`,`version`,`anamnesischeck`,`anamnesisform`) VALUES ");
			
			for(AnamnesisChecksValue anamnesisChecksValue : setOfAnamChecksValues){
				
				sql.append(" (");
				
				if(anamnesisChecksValue.getAnamnesisChecksValue()==null){
					sql.append(anamnesisChecksValue.getAnamnesisChecksValue());
				}else{
					sql.append("'").append(anamnesisChecksValue.getAnamnesisChecksValue()).append("'");
				}
				sql.append(", ");
				
				if(anamnesisChecksValue.getComment()==null){
					sql.append(anamnesisChecksValue.getComment());
				}else{
					sql.append("'").append(anamnesisChecksValue.getComment()).append("'");	
				}
				sql.append(", ").append(anamnesisChecksValue.getTruth()).append(", ").append("0, ")
				.append(anamnesisChecksValue.getAnamnesischeck().getId()).append(", ").append(spFormId).append("),");
			}
			String queryString = sql.toString().substring(0, sql.toString().length()-1);
	
			log.info("Query is :" + queryString);
		
			//em.getTransaction().begin();
			Query query =  em.createNativeQuery(queryString);
			//em.getTransaction().commit();
			
			int totalEntryCreated =query.executeUpdate();
			
			log.info("Total Anamnesis check value inserted in spportal is : " + totalEntryCreated);
	
		}
		/**
		 * This method is used to send email to patient
		 * @param standardizedPatient
		 * @param randomString
		 */
		private void sendEmailToStandardizedPatient(StandardizedPatient standardizedPatient,String randomString) {
			
			@SuppressWarnings("deprecation")
			HttpServletRequest request = com.google.web.bindery.requestfactory.server.RequestFactoryServlet.getThreadLocalRequest();
			
			HttpSession session = request.getSession();
			
			ServletContext servletContex =session.getServletContext();
			
			WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContex);
			 
			
			EmailServiceImpl emailServiceImpl =applicationContext.getBean(EmailServiceImpl.class);
			 
			VelocityEngine velocityEngine = applicationContext.getBean(VelocityEngine.class);
			
			velocityEngine.init();
			
			VelocityContext velocityContext = new VelocityContext();
			
			Properties prop = new Properties();
	    	
			Properties prop2 = new Properties();
			
			String serverPath=null;
			String isDemo=null;
			//load a properties file
			try {
				prop.load(applicationContext.getResource("classpath:META-INF/spring/serverInfo.properties").getInputStream());
				serverPath = prop.getProperty("serverAddress");
				
				prop2.load(applicationContext.getResource("classpath:META-INF/spring/smtp.properties").getInputStream());
				isDemo = prop2.getProperty("spportal.osce.demo");
				
			} catch (IOException e) {
				e.printStackTrace();
				log.error("Error in loading property file", e);
			}
			
			velocityContext.put("serverAddress",serverPath);
			
			velocityContext.put("randomString",randomString);
			
			Template template = velocityEngine.getTemplate("templates/spCreatedEmailTemplate.vm");
			
			StringWriter writer =new StringWriter();
			
			template.merge(velocityContext,writer);
			
			String emailContent = writer.toString(); //VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,"/templates/emailTemplate.vm", "UTF-8",null);
			log.info("email that is sening to sp is" + emailContent);
			
			if(isDemo!=null && isDemo.equals("true")){
				emailServiceImpl.sendMail( new String [] {prop2.getProperty("spportal.osce.demo.mail.id")},emailContent);
			}else{
				//sending email to user
				emailServiceImpl.sendMail( new String [] {standardizedPatient.getEmail()},emailContent);
			}
		}
		/**
		 * This method is used to save standardized patient data including anamnesis check value in sp portal database.
		 * @param em
		 * @param standardizedPatient
		 * @return
		 */
		//@Transactional("spportalTransactionManager")
		private SpStandardizedPatient initializeStandardizedPatientAndAnamnesisCheckValueDetails(/*EntityManager em,*/StandardizedPatient standardizedPatient){

			SpNationality spNationality =initializeNationalityDetailForSpPortal(standardizedPatient);
			
			SpBankaccount spBankaccount =initializeBankAccountDetailForSpPortal(standardizedPatient);
			
			SpProfession spProfession =  initializeProfessionDetailForSpPortal(standardizedPatient);
			
			SpAnamnesisForm spAnamnesisForm=initializeAnamnesisFormDetailForSpPortal(standardizedPatient);
			
			//The following code inside of if condition is commented to solve production entity not found error and separately saving AnamnesisCheckValues. Manish
			// In case if spAnamnesisForm is null we can not persist setSpAnamnesisChecksValues so making sure that we have anamnesis form and then persisting rest dependent data.
			/*if(spAnamnesisForm !=null){
				
				//If anamnesis form is not null saving dependent data (anamnesis_check_value data).
				//Set<SpScar> spScars = initializeScarDetailForSpPortal(standardizedPatient);
				
				//Commented By Manish to solve production error of no entity found
				//Set<SpAnamnesisChecksValue> setSpAnamnesisChecksValues = initializeAnamnesisCheckValuesForSpPortal(em,standardizedPatient,spAnamnesisForm);
	
				//spAnamnesisForm.setScars(spScars);
				
				//spAnamnesisForm.setAnamnesischecksvalues(setSpAnamnesisChecksValues);
			}*/
			//Added for OMS-157.
			SpNationality spCountry = initializeCountryDetailForSpPortal(standardizedPatient);
			
			SpStandardizedPatient spStandardizedPatient=initializeStandardizedPatientDetailForSpPortal(spNationality,spBankaccount,spProfession,standardizedPatient,spAnamnesisForm,spCountry);
			
			log.info("returning sp standardized patient");
			return spStandardizedPatient;
		}
		/**
		 * This method is used to copy country detail of standardized patient and initialize sp-poratal country detail
		 * @param standardizedPatient
		 * @return
		 */
		private SpNationality initializeCountryDetailForSpPortal(StandardizedPatient standardizedPatient) {
			log.info("initializing country details for sp portal");
			try{
					Nationality country =standardizedPatient.getCountry();
					
					SpNationality spCountry =null;
					
					if(country!=null){
					
						spCountry= SpNationality.findNationalityOnNationalityText(country.getNationality());
						 
						log.info("country detail that will be saved in sp portal db is : " + spCountry);
						
					}
					
					return spCountry;
			}catch(Exception e){
				log.info("Country detail initialization failure for SP portal database");
				log.error(e.getMessage(),e);
				return null;
			}
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
					
						spNationality= SpNationality.findNationalityOnNationalityText(nationality.getNationality());
						 
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
		private SpBankaccount initializeBankAccountDetailForSpPortal(StandardizedPatient standardizedPatient) {
			log.info("initializing bank account details for sp portal ");
			try{
					Bankaccount bankAccount =standardizedPatient.getBankAccount();
					
					SpBankaccount spBankAccount=null;
					
					if(bankAccount !=null ) {
			
						spBankAccount = new SpBankaccount();
						
						spBankAccount.setBankName(bankAccount.getBankName());
						spBankAccount.setBIC(bankAccount.getBIC());
						spBankAccount.setCity(bankAccount.getCity());
						
						Nationality nationality =bankAccount.getCountry();
						
						SpNationality spNationality =null;
						
						if(nationality!=null){
						
							spNationality= SpNationality.findNationalityOnNationalityText(nationality.getNationality());
							 
							log.info("Nationality detail that will be saved in sp portal db is : " + spNationality);
							
						}
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
			
						spProfession= SpProfession.findProfessionBasedOnProfessionText(profession.getProfession());
						
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
		 * @param spCountry 
		 * @return
		 */
		private SpStandardizedPatient initializeStandardizedPatientDetailForSpPortal(SpNationality spNationality, SpBankaccount spBankaccount,SpProfession spProfession,
				StandardizedPatient standardizedPatient,SpAnamnesisForm spAnamnesisForm, SpNationality spCountry) {
			
			log.info("initializing standardized patient details for sp portal ");
			try{
					
					SpStandardizedPatient spStandardizedPatient=null;
					
					if(standardizedPatient !=null ) {
			
						//following code is to get spportal "standardized patient" based on osce "standardizedPatient" id to solve issue SPP-10
						SpStandardizedPatient spportalPatient = getPatientFromSpportalBasedOnId(standardizedPatient.getId());
						if(spportalPatient==null){
						// "spportal standardized patient" id is null i.e there is no any data of sp so we are exporting all data of sp to spportal.
							spStandardizedPatient= new SpStandardizedPatient();
						}else{
							//There exist "standardized patient" data so we are copying remaining data (ana form and ana check values) of sp and exporting that to spportal.
							spStandardizedPatient=spportalPatient;
						}
						if(standardizedPatient.getCreated()==null){
							spStandardizedPatient.setCreated(new Date());
						}else{
							spStandardizedPatient.setCreated(standardizedPatient.getCreated());
						}
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
						spStandardizedPatient.setId(standardizedPatient.getId());
						spStandardizedPatient.setCountry(spCountry);
						System.out.println("stand patient id is" + standardizedPatient.getId());
						System.out.println("SP stand patient id is" + spStandardizedPatient.getId());
						log.info("standardized patient detail that will be saved in sp portal db is : " + spStandardizedPatient);
						
					}
					return spStandardizedPatient;
			}catch(Exception e){
				log.info("standardized patiemt initialization failure for SP portal database");
				log.error(e.getMessage(),e);
				return null;
			}
		}
		private SpStandardizedPatient getPatientFromSpportalBasedOnId(Long id) {
			
			log.info("finding spportal person based on standardized patient id :  " + id);
			
			EntityManager em = SpStandardizedPatient.entityManager();
			
			String queryString ="select sp from SpStandardizedPatient sp where sp.id="+id;
			 
			 TypedQuery<SpStandardizedPatient> query =em.createQuery(queryString,SpStandardizedPatient.class);
			 
			 List<SpStandardizedPatient> listOfSp = query.getResultList();
			 
			 if(listOfSp!=null && listOfSp.size()==1){
				 return listOfSp.get(0);
			 }else{
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
		//@Transactional("spportalTransactionManager")
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
							//spAnamnesisCheck.setUserSpecifiedOrder(anamnesisCheck.getUserSpecifiedOrder());
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
	
	/**
	 * This method finds count of all SpPerson who sent edit request.
	 * @return
	 */
	 public static Long findAllSpsCountWhoSentEditReq(){

		 log.info("fonding sp coung who sent edit request");
		 
		 List<SPPortalPerson> spPersons = allSPsWhoSentEditRequest();
		
		 return Long.parseLong(String.valueOf(spPersons.size()));
	 }
	 
	 /**
	  * This method return list of all sps who sent edit request.
	  * @return
	  */
	 public static List<SPPortalPerson> allSPsWhoSentEditRequest(){
		
		 log.info("finding count of sp who sent edit request");
		 
		 /*EntityManagerFactory emFactory=  Persistence.createEntityManagerFactory("spportalPersistenceUnit");

		 EntityManager em = emFactory.createEntityManager();*/
		 EntityManager em = entityManager();
		 
		 String queryString ="select sp from SPPortalPerson sp where sp.editRequestState="+EditRequestState.REQUEST_SEND.ordinal();
		 
		 TypedQuery<SPPortalPerson> query =em.createQuery(queryString,SPPortalPerson.class);
		 
		 List<SPPortalPerson> spPersons = query.getResultList();
		 
		 return spPersons;
		 
	 }
	 /**
	  * This method finds all SpStandardizedPatient who sent edit request.
	  * @return
	  */
	 public static List<StandardizedPatient> findAllSpsWhoSentEditRequest(int firstResult,int maxResults){
		
		 log.info("finding list of sp who sent edit request");
		 
		 EntityManager em = StandardizedPatient.entityManager();
		 
		// String queryString ="select sp from StandardizedPatient sp where sp.email in (''"+ getEmailOfSPs(allSPsWhoSentEditRequest()) +") ";
		 
		 String queryString ="select sp from StandardizedPatient sp where sp.spPortalPersonId in (''"+ getIdOfSPs(allSPsWhoSentEditRequest()) +") ";
		 
		 TypedQuery<StandardizedPatient> query =em.createQuery(queryString,StandardizedPatient.class);
		
		 query.setFirstResult(firstResult);
	
		 query.setMaxResults(maxResults);
		 
		 List<StandardizedPatient> standardizedPatientList = query.getResultList();
		
		return standardizedPatientList;
	 }
	 /**
	  * This method return all sps email as comma separated string.
	  * @param spPersonList
	  * @return
	  */
	 private static String getEmailOfSPs(List<SPPortalPerson> spPersonList) {

			if (spPersonList == null|| spPersonList.size() == 0) {
				log.info("Return as null");
				return "";
			}
			Iterator<SPPortalPerson> spPersonlistIterator = spPersonList.iterator();
			StringBuilder spEmailAddress = new StringBuilder();
			spEmailAddress.append(",");
			while (spPersonlistIterator.hasNext()) {
				
				SPPortalPerson spPerson = spPersonlistIterator.next();

				spEmailAddress.append("'"+spPerson.getEmail()+"'");
				if (spPersonlistIterator.hasNext()) {
					spEmailAddress.append(" ,");
				}
			}
			
			return spEmailAddress.toString();
		}
	 
	 private static String getIdOfSPs(List<SPPortalPerson> spPersonList) {

			if (spPersonList == null|| spPersonList.size() == 0) {
				log.info("Return as null");
				return "";
			}
			Iterator<SPPortalPerson> spPersonlistIterator = spPersonList.iterator();
			StringBuilder spIds = new StringBuilder();
			spIds.append(",");
			while (spPersonlistIterator.hasNext()) {
				
				SPPortalPerson spPerson = spPersonlistIterator.next();

				spIds.append("'"+spPerson.getId()+"'");
				if (spPersonlistIterator.hasNext()) {
					spIds.append(" ,");
				}
			}
			
			return spIds.toString();
		}
	 /**
	  * This method sent mail and reset edit request state.
	  */
	 public static void denyAllSpsEditRequest(){
		 log.info("sending mail and resetting flag in db as all sps edit req is denied");
		
		List<SPPortalPerson> spPersonsList =allSPsWhoSentEditRequest();
		 
		sendMailToSPAndUpdateStatusToDenyRequest(spPersonsList);
		
	 }

	 private static void sendMailToSPAndUpdateStatusToDenyRequest(List<SPPortalPerson> spPersonList){
		 
		 boolean isMailSendToSPs =sendEmailToAllSpsAsTheirEditRequestIsApprovedOrDenied(spPersonList,false);
			
			if(isMailSendToSPs){
				 
				 for(SPPortalPerson person : spPersonList){
					 person=SPPortalPerson.findSPPortalPerson(person.getId());
					 person.setEditRequestState(EditRequestState.NONE);
					 person.persist();
				 }
			 }
	 }
	 public static void allSpsEditRequestIsApproved(){
		 
		 log.info("sending mail and resetting flag in db as all sps edit req is approved");
		 try{
		 EntityManager em = StandardizedPatient.entityManager();
		 
		 //String queryString ="select sp from StandardizedPatient sp where sp.email in (''"+ getIdOfSPs(allSPsWhoSentEditRequest()) +") ";
		 
		 String queryString ="select sp from StandardizedPatient sp where sp.spPortalPersonId in (''"+ getIdOfSPs(allSPsWhoSentEditRequest()) +") ";
		 
		 TypedQuery<StandardizedPatient> query =em.createQuery(queryString,StandardizedPatient.class);
		
		 List<StandardizedPatient> standardizedPatientList = query.getResultList();
		 
		 List<SPPortalPerson> spPersonsList =allSPsWhoSentEditRequest();
		 
		 sendMailToSPAndUpdateStatusToApproveRequest(standardizedPatientList,spPersonsList);
		 }catch (Exception e) {
			e.printStackTrace();
		}

	 }
	 private static void sendMailToSPAndUpdateStatusToApproveRequest(List<StandardizedPatient> standardizedPatientList,List<SPPortalPerson> spPersonsList){
		 
		 //initializing and persisting standardized patient data in spPortal.
		 
		/* EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");

		 EntityManager emOfSPPortal = emFactory.createEntityManager();*/
		 
		 EntityManager emOfSPPortal  =entityManager();
		 
		 SPPortalPerson sppoPerson = new SPPortalPerson();
		 
		 for(StandardizedPatient standardizedPatient : standardizedPatientList){
		
			SpStandardizedPatient spStandardizedPatient= sppoPerson.initializeStandardizedPatientAndAnamnesisCheckValueDetails(/*emOfSPPortal,*/standardizedPatient);
			
			//SPPortalPerson spPerson = findSpPortalPersonBasedOnEmailAddress(standardizedPatient.getEmail());
			
			SPPortalPerson spPerson=SPPortalPerson.findSPPortalPerson(standardizedPatient.getSpPortalPersonId());
			
			spPerson.setChanged(false);
			
			//spPerson.persist();
			//spStandardizedPatient.getProfession().persist();
			
			spStandardizedPatient.setPerson(spPerson);
			
			//changing sp status to exported.
			standardizedPatient=StandardizedPatient.findStandardizedPatient(standardizedPatient.getId());
			
			//Following code is to solve issue SPP-10.(when survey is open we exported sp as he was in semester. Now sp makes edit request and admin approves it from osce)
			//first up all checking sp is exist in spportal
			SpStandardizedPatient patientFromSpportalBasedOnId = sppoPerson.getPatientFromSpportalBasedOnId(standardizedPatient.getId());
			
			//SP is null that means there is no any data of sp at spportal (Normal edit request is approved) so we are updating his status as exported. 
			if(patientFromSpportalBasedOnId==null){
				standardizedPatient.setStatus(StandardizedPatientStatus.EXPORTED);
				spStandardizedPatient.setStatus(StandardizedPatientStatus.EXPORTED);
			}else{
				//"standardized patient" data is already in spportal (sp is exported when survey is started) so we are updating his status as EXPORTED_AND_SURVEY as we are copying sps data to spportal.
				standardizedPatient.setStatus(StandardizedPatientStatus.EXPORTED_AND_SURVEY);
				spStandardizedPatient.setStatus(StandardizedPatientStatus.EXPORTED_AND_SURVEY);
			}
			standardizedPatient.persist();
			
			//pushing all sp data in sp portal.
			spStandardizedPatient.persist();
			
			//Now calling this method to save anamnesis check values in spportal this is to solve production error entity not found.
			sppoPerson.saveAnamnesisCheckValueinSpportal(spStandardizedPatient, standardizedPatient);
			
			log.info("user detail that is saving in sp portal db is : " + spPerson.getEmail());
			
		}
		 
		 //sending email to all sps as their request is approved
		boolean isMailSendToAllSPs =sendEmailToAllSpsAsTheirEditRequestIsApprovedOrDenied(spPersonsList,true);
			
			if(isMailSendToAllSPs){
				 
				 for(SPPortalPerson person : spPersonsList){
					 person=SPPortalPerson.findSPPortalPerson(person.getId());
					 //setting this flag once mail is sent so only sps that received mail can edit request. Because in spportal I allow enit if this flag is true on click of edit button.
					 //setting sp portal user flag to request approved.
					 person.setEditRequestState(EditRequestState.APPROVED);
					 person.persist();
				 }
			 }
	 }
	 
	 /**
	  * This method is used to send emil to all sps whose edit request is accepted/denied. 
	  * @param spPersonsList
	  * @return
	  */
	private static boolean sendEmailToAllSpsAsTheirEditRequestIsApprovedOrDenied(List<SPPortalPerson> spPersonsList,boolean isRequestIsApproved) {
		
		boolean isMailSendToAllSPs=false;
		try{
			@SuppressWarnings("deprecation")
			HttpServletRequest request = com.google.web.bindery.requestfactory.server.RequestFactoryServlet.getThreadLocalRequest();
			
			HttpSession session = request.getSession();
			
			ServletContext servletContex =session.getServletContext();
			
			WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContex);
			 
			
			EmailServiceImpl emailServiceImpl =applicationContext.getBean(EmailServiceImpl.class);
			 
			VelocityEngine velocityEngine = applicationContext.getBean(VelocityEngine.class);
			
			velocityEngine.init();
			
			Properties prop = new Properties();
			
			prop.load(applicationContext.getResource("classpath:META-INF/spring/smtp.properties").getInputStream());
			
			String emailSubject;
			
			if(isRequestIsApproved){
				emailSubject= prop.getProperty("spportal.editRequestApprovedEmail.subject");
			}else{
				emailSubject= prop.getProperty("spportal.editRequestDeniedEmail.subject");
			}
			
			String isDemo = prop.getProperty("spportal.osce.demo");
			
			Template template;
			
			for (SPPortalPerson person : spPersonsList) {
				
				VelocityContext velocityContext = new VelocityContext();
				
				if(isRequestIsApproved){
					template= velocityEngine.getTemplate("templates/editRequestApprovedEmailTemplate.vm");
				}else{
					template= velocityEngine.getTemplate("templates/editRequestDeniedEmailTemplate.vm");
				}
				StringWriter writer =new StringWriter();
				
				template.merge(velocityContext,writer);
				
				String emailContent = writer.toString(); //VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,"/templates/emailTemplate.vm", "UTF-8",null);
				
				if(isDemo!=null && isDemo.equals("true")){
					emailServiceImpl.sendMail( new String [] {prop.getProperty("spportal.osce.demo.mail.id")},emailSubject,emailContent);
				}else{
					//sending email to user
					emailServiceImpl.sendMail( new String [] {person.getEmail()},emailSubject,emailContent);
				}
			}
			
			isMailSendToAllSPs=true;
			
		}catch (Exception e) {
			log.error(e.getMessage(),e);
			isMailSendToAllSPs=false;
			return isMailSendToAllSPs;
		}
		return isMailSendToAllSPs;
	}
	public static SpStandardizedPatient findSpPortalSPBasedOnOsceSPID(Long osceSPId){
		
		log.info("finding sp portal sp based on osce sp id : " + osceSPId);
		
		StandardizedPatient standardizedPatient = StandardizedPatient.findStandardizedPatient(osceSPId);
		
		/* EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");

		 EntityManager em = emFactory.createEntityManager();*/
		 
		EntityManager em = entityManager();
		
		 String queryString ="select sp from SpStandardizedPatient sp where sp.person.id="+standardizedPatient.getSpPortalPersonId();
		 
		 TypedQuery<SpStandardizedPatient> query =em.createQuery(queryString,SpStandardizedPatient.class);
		 
		 List<SpStandardizedPatient> spportalSP = query.getResultList();
		 
		 if(spportalSP.size()==1){
			 return spportalSP.get(0);
		 }else{
			 return null;
		 }
		 
	}
	
public static SPPortalPerson findSpPortalPersonBasedOnEmailAddress(String emailAddress){
		
		log.info("finding sp portal person based on email address : " + emailAddress);
		
		/* EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");

		 EntityManager em = emFactory.createEntityManager();*/
		 
		EntityManager em  =entityManager();
		
		 String queryString ="select sp from SPPortalPerson sp where sp.email='"+emailAddress + "'";
		 
		 TypedQuery<SPPortalPerson> query =em.createQuery(queryString,SPPortalPerson.class);
		 
		 List<SPPortalPerson> spportalSP = query.getResultList();
		 
		 if(spportalSP.size()==1){
			 return spportalSP.get(0);
		 }else{
			 return null;
		 }
		 
	}

	
	public static List<StandardizedPatient> findAllStandardizedPAtientWhoesDataIsChangedAtSPPortal(List<SpStandardizedPatient> lisOfAllSPsWhoEditedData){
		
		EntityManager em = StandardizedPatient.entityManager();
		
		List<StandardizedPatient> allStandardizedPatientsList = new ArrayList<StandardizedPatient>();
		
		for(SpStandardizedPatient spStandardizedPatient : lisOfAllSPsWhoEditedData){
		 
			//String queryString ="select sp from StandardizedPatient sp where sp.email in (''"+ getEmailOfStandardizedPAtient(lisOfAllSPsWhoEditedData) +") ";
		 
			String queryString ="select sp from StandardizedPatient sp where sp.spPortalPersonId="+ spStandardizedPatient.getPerson().getId();
		 
			TypedQuery<StandardizedPatient> query =em.createQuery(queryString,StandardizedPatient.class);
		
		 
			allStandardizedPatientsList.add(query.getSingleResult());
		 
		}
		//return standardizedPatientList;
		return allStandardizedPatientsList;
	}
	
	 private static String getEmailOfStandardizedPAtient(List<SpStandardizedPatient> spStandardizedPatientList) {

			if (spStandardizedPatientList == null|| spStandardizedPatientList.size() == 0) {
				log.info("Return as null");
				return "";
			}
			Iterator<SpStandardizedPatient> spPatientlistIterator = spStandardizedPatientList.iterator();
			StringBuilder spEmailAddress = new StringBuilder();
			spEmailAddress.append(",");
			while (spPatientlistIterator.hasNext()) {
				
				SpStandardizedPatient sp = spPatientlistIterator.next();

				spEmailAddress.append("'"+sp.getEmail()+"'");
				if (spPatientlistIterator.hasNext()) {
					spEmailAddress.append(" ,");
				}
			}
			
			return spEmailAddress.toString();
		}
	 
	/**
	 * This method is used to get all sps who changed their personal data.
	 */
	
	/*public static List<SpStandardizedPatient> gatherSPDetailsOfDataChanged(int firstResult,int maxResults){
		log.info("finding SPs whose data is changed");
		//return SpStandardizedPatient.getAllSPWhoEditedDetails(firstResult, maxResults);
		return null;
	}*/
	
	public static SPPortalPerson findSPPersonToCheckWhetherHeHasSentEditReqOrChandedData(Long standardizedPatientId){
		
		log.info("finding sp that is used to check whetther he has sent edit request");
		
		StandardizedPatient standardizedPatient = StandardizedPatient.findStandardizedPatient(standardizedPatientId);
		
		/* EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");

		 EntityManager em = emFactory.createEntityManager();*/
		 
		EntityManager em  = entityManager();
		 String queryString ="select sp from SPPortalPerson sp where sp.id="+standardizedPatient.getSpPortalPersonId();
		 
		 TypedQuery<SPPortalPerson> query =em.createQuery(queryString,SPPortalPerson.class);
		 
		 List<SPPortalPerson> spportalSP = query.getResultList();
		 
		 if(spportalSP.size()==1){
			 return spportalSP.get(0);
		 }else{
			 return null;
		 }
		 
		
	}
	
	/**
	 * This method is used to send email and clear request flag as sps edit request is denied 
	 * @param standardizedPatientId
	 * @param spPersonId
	 */
	public static void denyEditRequestOfSP(Long spPersonId){

		SPPortalPerson spPerson = SPPortalPerson.findSPPortalPerson(spPersonId);

		List<SPPortalPerson> spPersonsList = new ArrayList<SPPortalPerson>();
		spPersonsList.add(spPerson);
		
		sendMailToSPAndUpdateStatusToDenyRequest(spPersonsList);
	}
	
	/**
	 * This method is used to send email and clear request flag as sps edit request is approved. 
	 * @param standardizedPatientId
	 * @param spPersonId
	 */
	public static void approveEditRequestOfSP(Long standardizedPatientId, Long spPersonId){
		
		StandardizedPatient standardizedPatient = StandardizedPatient.findStandardizedPatient(standardizedPatientId);
		
		SPPortalPerson spPerson = SPPortalPerson.findSPPortalPerson(spPersonId);
		
		List<StandardizedPatient> standardizedPatientsList = new ArrayList<StandardizedPatient>();
		standardizedPatientsList.add(standardizedPatient);
		
		List<SPPortalPerson> spPersonsList = new ArrayList<SPPortalPerson>();
		spPersonsList.add(spPerson);
		
		sendMailToSPAndUpdateStatusToApproveRequest(standardizedPatientsList,spPersonsList);
	}
	 
	/**
	 * This method is used to return all Anamnesis that is sent to DMZ
	 * 
	 */
		public static List<AnamnesisCheckTitle> findAllAnamnesisThatIsSendToDMZ(){
		
		log.info("getting all AnamnesisCheckTitle value");
		
		List<AnamnesisCheckTitle> allAnamnesisCheckTitles= AnamnesisCheckTitle.entityManager().createQuery("SELECT o FROM AnamnesisCheckTitle o ORDER BY sort_order", AnamnesisCheckTitle.class).getResultList();
		
		boolean isAllAnamnesisCheckIsSentToDMZ;
		
		List<AnamnesisCheckTitle> anamnesisCheckTitlesThatIsToShow = new ArrayList<AnamnesisCheckTitle>();
		
		for (AnamnesisCheckTitle anamnesisCheckTitle : allAnamnesisCheckTitles) {
			
			isAllAnamnesisCheckIsSentToDMZ=false;
			
			Set<AnamnesisCheck> setAnamnesisChecks = anamnesisCheckTitle.getAnamnesisChecks();
			
			for (Iterator iterator = setAnamnesisChecks.iterator(); iterator.hasNext();) {
				AnamnesisCheck anamnesisCheck = (AnamnesisCheck) iterator.next();
				if(anamnesisCheck.getSendToDMZ()){
					isAllAnamnesisCheckIsSentToDMZ=true;
					break;
				}
			}
			
			if(isAllAnamnesisCheckIsSentToDMZ){
				anamnesisCheckTitlesThatIsToShow.add(anamnesisCheckTitle);
			}
		}
		
			return anamnesisCheckTitlesThatIsToShow;
	}

	public String getEmail() {
        return this.email;
    }

	public void setEmail(String email) {
        this.email = email;
    }

	public String getPassword() {
        return this.password;
    }

	public void setPassword(String password) {
        this.password = password;
    }

	public Date getExpiration() {
        return this.expiration;
    }

	public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

	public Boolean getIsFirstLogin() {
        return this.isFirstLogin;
    }

	public void setIsFirstLogin(Boolean isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

	public EditRequestState getEditRequestState() {
        return this.editRequestState;
    }

	public void setEditRequestState(EditRequestState editRequestState) {
        this.editRequestState = editRequestState;
    }

	public String getActivationUrl() {
        return this.activationUrl;
    }

	public void setActivationUrl(String activationUrl) {
        this.activationUrl = activationUrl;
    }

	public String getToken() {
        return this.token;
    }

	public void setToken(String token) {
        this.token = token;
    }

	public Boolean getChanged() {
        return this.changed;
    }

	public void setChanged(Boolean changed) {
        this.changed = changed;
    }

	public Set<SpPatientInSemester> getPatientInSemester() {
        return this.patientInSemester;
    }

	public void setPatientInSemester(Set<SpPatientInSemester> patientInSemester) {
        this.patientInSemester = patientInSemester;
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            SPPortalPerson attached = SPPortalPerson.findSPPortalPerson(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public SPPortalPerson merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SPPortalPerson merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new SPPortalPerson().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSPPortalpeople() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SPPortalPerson o", Long.class).getSingleResult();
    }

	public static List<SPPortalPerson> findAllSPPortalpeople() {
        return entityManager().createQuery("SELECT o FROM SPPortalPerson o", SPPortalPerson.class).getResultList();
    }

	public static SPPortalPerson findSPPortalPerson(Long id) {
        if (id == null) return null;
        return entityManager().find(SPPortalPerson.class, id);
    }

	public static List<SPPortalPerson> findSPPortalPersonEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SPPortalPerson o", SPPortalPerson.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ActivationUrl: ").append(getActivationUrl()).append(", ");
        sb.append("Changed: ").append(getChanged()).append(", ");
        sb.append("EditRequestState: ").append(getEditRequestState()).append(", ");
        sb.append("Email: ").append(getEmail()).append(", ");
        sb.append("EntityManager: ").append(getEntityManager()).append(", ");
        sb.append("Expiration: ").append(getExpiration()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("IsFirstLogin: ").append(getIsFirstLogin()).append(", ");
        sb.append("Password: ").append(getPassword()).append(", ");
        sb.append("PatientInSemester: ").append(getPatientInSemester() == null ? "null" : getPatientInSemester().size()).append(", ");
        sb.append("Token: ").append(getToken()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }
}
