package ch.unibas.medizin.osce.domain.spportal;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import ch.unibas.medizin.osce.domain.AnamnesisChecksValue;
import ch.unibas.medizin.osce.domain.Bankaccount;
import ch.unibas.medizin.osce.domain.Nationality;
import ch.unibas.medizin.osce.domain.Profession;
import ch.unibas.medizin.osce.domain.StandardizedPatient;
import ch.unibas.medizin.osce.server.OsMaFilePathConstant;
import ch.unibas.medizin.osce.shared.EditRequestState;
import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.MaritalStatus;
import ch.unibas.medizin.osce.shared.StandardizedPatientStatus;
import ch.unibas.medizin.osce.shared.WorkPermission;



@Entity
@Configurable
@Table(name="standardized_patient")
public class SpStandardizedPatient {

	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
    
	 @Id
	 @Column(name = "id")
	 private Long id;
	    
	@Size(max = 40)
    private String preName;
	
    @Size(max = 40)
    private String name;
    
    @Size(max = 60)
    private String street;
    
    @Size(max = 15)
    private String postalCode;
    
    @Size(max = 30)
    private String city;
    
    @Size(max = 30)
    private String telephone;

    @Size(max = 30)
    private String telephone2;

    @Size(max = 30)
    private String mobile;
    
    @Size(max = 40)
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$")
    private String email;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date birthday;
    
    @Enumerated
    private Gender gender;

    private Integer height;	

    private Integer weight;

    @Size(max = 255)
    private String immagePath;
    
    @Size(max = 255)
    private String videoPath;
    
    @ManyToOne
    private SpProfession profession;
    
    @ManyToOne
    private SpNationality nationality;
    //Added as per OMS-157.
    @ManyToOne
    private SpNationality country;
    
    @Enumerated
    private WorkPermission workPermission;
    
    @Enumerated
    private StandardizedPatientStatus status;
    
    @Enumerated
    private MaritalStatus maritalStatus;
    
    @Size(max = 20)
    private String socialInsuranceNo;
    
    @OneToOne(cascade = CascadeType.ALL)
    private SpBankaccount bankAccount;

    @OneToOne(cascade = CascadeType.ALL)
    private SpAnamnesisForm anamnesisForm;

    @OneToOne(cascade = CascadeType.PERSIST)
	private SPPortalPerson person;
    
    private Boolean ignoreSocialInsuranceNo;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date created;
    
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "standardizedPatient")
	private Set<SpPatientInSemester> patientInSemester = new HashSet<SpPatientInSemester>();

    
    private static transient Logger log = Logger.getLogger(SPPortalPerson.class);
    
    
    /**
	 * This method is used to get count of sps who changed their data.
	 * @return
	 */
	public static Long findAllSpsCountWhoEditedData(){
		
		List<SpStandardizedPatient> spStandardizedPatientsList =findAllSPWhoEditedData();
		
		 return Long.parseLong(String.valueOf(spStandardizedPatientsList.size()));
	}
	
    /**
	  * This method return list of all sps who edit their data.
	  * @return
	  */
	 public static List<SpStandardizedPatient> findAllSPWhoEditedData(){
		
		 log.info("finding All sps count who edited their data");
		 
		/* EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");

		 EntityManager em = emFactory.createEntityManager();*/
		 EntityManager em = entityManager();
		 String queryString ="select sp from SpStandardizedPatient sp where sp.person.changed=1";
		 
		 TypedQuery<SpStandardizedPatient> query =em.createQuery(queryString,SpStandardizedPatient.class);
		 
		 List<SpStandardizedPatient> spStandardizedPatientsList = query.getResultList();
		 
		 return spStandardizedPatientsList;
		 
	 }
	 
	 public static List<SpStandardizedPatient> findAllSPWhoEditedDetails(int firstResult,int maxResults){
			
		 log.info("finding All sps who edited their data");
		 
		/* EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");

		 EntityManager em = emFactory.createEntityManager();*/
		 
		 EntityManager em =entityManager();
		 
		 String queryString ="select sp from SpStandardizedPatient sp where sp.person.changed=1";
		 
		 TypedQuery<SpStandardizedPatient> query =em.createQuery(queryString,SpStandardizedPatient.class);
		 
		 query.setFirstResult(firstResult);
			
		 query.setMaxResults(maxResults);
		 
		 List<SpStandardizedPatient> spStandardizedPatientsList = query.getResultList();
		 
		 return spStandardizedPatientsList;
		 
	 }
	 
	 public static List<SpStandardizedPatient> findALlSPWhoEditedDetails(){
			
			List<SpStandardizedPatient> spStandardizedPatientsList =findAllSPWhoEditedData();
			
			return spStandardizedPatientsList;
			
		}
	 
	public static List<SpAnamnesisChecksValue> findAnamnesisChecksValuesByAnamnesisFormAndCheckTitleText(Long anamnesisFormId,String titleText){
		
		log.info("finding All AnamnesisChecksValues based on form id : " + anamnesisFormId);
		 
		/* EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");

		 EntityManager em = emFactory.createEntityManager();*/
		 
		 EntityManager em =entityManager();
		 
		 String queryString ="SELECT a FROM SpAnamnesisChecksValue a WHERE a.anamnesischeck.text in (" + titleText + ") AND a.anamnesisform.id = " + anamnesisFormId + " ORDER BY a.anamnesischeck.text";

		 TypedQuery<SpAnamnesisChecksValue> query = em.createQuery(queryString, SpAnamnesisChecksValue.class);  
		 
		 List<SpAnamnesisChecksValue> resultList = query.getResultList();
	    	
		 return resultList;
	}
	
	public static Boolean moveChangedDetailsOfSPFormSPPortal(Long standardizedPatientId, Long spStandardizedPatientId){
		
		Boolean isDataSavedInOsce =false;
		Boolean isDataRemovedFromSPPortal=false;
		boolean isStatusExportedAndSurvey=false;
		try{
				StandardizedPatient standardizedPatient = StandardizedPatient.findStandardizedPatient(standardizedPatientId);
				
				//Added following code to solve issue SPP-10. 
				/*Scenario is that survey is opened, sp still sent edit request, admin has approved it so we exports all sps data to spportal & 
				change sp's status to EXPORTED_AND_SURVEY. Now if sp updates its detail from spportal and admin approves changes from osce.
				Here we are considering two cases either survey is still open or closed. If survey is open then we have sp's status as EXPORTED_AND_SURVEY in this case
				We are removing all sp's data except its id is remain in spportal database and changing its status as INSURVEY (like we have sp in survey). 
				On the other hand if survey is closed then status of this sp is changed to EXPORTED, so we remove all data from spportal after upfating changes in osce. */
				
				if(standardizedPatient.getStatus().ordinal()==StandardizedPatientStatus.EXPORTED_AND_SURVEY.ordinal()){
					isStatusExportedAndSurvey=true;
				}
				SpStandardizedPatient spStandardizedPatient = SpStandardizedPatient.findSpStandardizedPatient(spStandardizedPatientId);
				
				SpBankaccount spBankaccount = spStandardizedPatient.getBankAccount();
				Bankaccount bankaccount = standardizedPatient.getBankAccount();
				
				bankaccount.setBankName(spBankaccount.getBankName());
				bankaccount.setBIC(spBankaccount.getBIC());
				bankaccount.setCity(spBankaccount.getCity());
				
				SpNationality spNationality = spBankaccount.getCountry();
				Nationality nationality=null;//=bankaccount.getCountry();
				
				if(spNationality!=null){
					
					nationality=Nationality.findNationalityByName(spNationality.getNationality());
					
				}
				bankaccount.setCountry(nationality);
				bankaccount.setIBAN(spBankaccount.getIBAN());
				bankaccount.setOwnerName(spBankaccount.getOwnerName());
				bankaccount.setPostalCode(spBankaccount.getPostalCode());
				
				standardizedPatient.setBankAccount(bankaccount);
				
				standardizedPatient.setBirthday(spStandardizedPatient.getBirthday());
				standardizedPatient.setCity(spStandardizedPatient.getCity());
				standardizedPatient.setEmail(spStandardizedPatient.getEmail());
				standardizedPatient.setGender(spStandardizedPatient.getGender());
				standardizedPatient.setHeight(spStandardizedPatient.getHeight());
				standardizedPatient.setIgnoreSocialInsuranceNo(spStandardizedPatient.getIgnoreSocialInsuranceNo());
				//Added as per OMS-157.
				SpNationality spCountry = spStandardizedPatient.getCountry();
				Nationality country=null;
				
				if(spCountry!=null){
					
					country=Nationality.findNationalityByName(spCountry.getNationality());
					
				}
				standardizedPatient.setCountry(country);
				
				if(spStandardizedPatient.getImmagePath()!=null){
					//i.e user has uploded image in spportal
					if(standardizedPatient.getImmagePath() !=null){
						//removing old image as it exist.
						String [] imagepath = standardizedPatient.getImmagePath().split("/");
						
						if(imagepath.length > 0){
							
							File file = new File(OsMaFilePathConstant.localImageUploadDirectory+ imagepath[imagepath.length-1]);
							
							if(file.exists()){
								file.delete();
							}
						}
					}
					
					standardizedPatient.setImmagePath(OsMaFilePathConstant.appImageUploadDirectory +"/" +spStandardizedPatient.getImmagePath());
				}
				
				standardizedPatient.setMaritalStatus(spStandardizedPatient.getMaritalStatus());
				standardizedPatient.setMobile(spStandardizedPatient.getMobile());
				standardizedPatient.setName(spStandardizedPatient.getName());
				
				SpNationality spNationality2 = spStandardizedPatient.getNationality();
				
				Nationality nationality2=null; //standardizedPatient.getNationality();
				
				if(spNationality2!=null){

					nationality2=Nationality.findNationalityByName(spNationality2.getNationality());
				}
				
				standardizedPatient.setNationality(nationality2);
				
				standardizedPatient.setPostalCode(spStandardizedPatient.getPostalCode());
				standardizedPatient.setPreName(spStandardizedPatient.getPreName());
				
				SpProfession spProfession = spStandardizedPatient.getProfession();
				
				Profession profession=null; //standardizedPatient.getProfession();
				
				if(spProfession!=null){
					
					profession =Profession.findProfessionByProfessionText(spProfession.getProfession());
					
					if(profession==null){
						profession = new Profession();
						profession.setProfession(spProfession.getProfession());
						profession.persist();
					}
					
				}
				
				standardizedPatient.setProfession(profession);
				
				standardizedPatient.setSocialInsuranceNo(spStandardizedPatient.getSocialInsuranceNo());
				//For issue SPP-10
				if(isStatusExportedAndSurvey){
					standardizedPatient.setStatus(StandardizedPatientStatus.INSURVEY);
				}else{
					standardizedPatient.setStatus(StandardizedPatientStatus.ACTIVE);
				}
				standardizedPatient.setStreet(spStandardizedPatient.getStreet());
				standardizedPatient.setTelephone(spStandardizedPatient.getTelephone());
				standardizedPatient.setTelephone2(spStandardizedPatient.getTelephone2());
				//standardizedPatient.setVideoPath(spStandardizedPatient.getVideoPath());
				standardizedPatient.setWeight(spStandardizedPatient.getWeight());
				standardizedPatient.setWorkPermission( spStandardizedPatient.getWorkPermission());
				
				log.info("Persisting standardized Patient new data ");
				standardizedPatient.persist();
				
				List<AnamnesisChecksValue> oldAnamnesisChecksValuesList =findOldAnamnesisCheckValuesBasedOnFormId(standardizedPatient.getAnamnesisForm().getId());
				
				List<SpAnamnesisChecksValue> newAnamnesisCheckValuesList = findNewAnamnesisCheckValuesBasedOnFormId(spStandardizedPatient.anamnesisForm.getId());

				for(int count=0;count < oldAnamnesisChecksValuesList.size();count++){
					AnamnesisChecksValue anamnesisChecksValue = oldAnamnesisChecksValuesList.get(count);
					SpAnamnesisChecksValue spAnamnesisChecksValue = newAnamnesisCheckValuesList.get(count);
					
					anamnesisChecksValue=AnamnesisChecksValue.findAnamnesisChecksValue(anamnesisChecksValue.getId());
					
					anamnesisChecksValue.setAnamnesisChecksValue(spAnamnesisChecksValue.getAnamnesisChecksValue());
					
					anamnesisChecksValue.setTruth(spAnamnesisChecksValue.getTruth());
					
					anamnesisChecksValue.persist();
					
				}
				
				isDataRemovedFromSPPortal=removeSPDataFromSPPortal(spStandardizedPatient,newAnamnesisCheckValuesList,false,isStatusExportedAndSurvey);
				
				isDataSavedInOsce=true;
				
		}catch (Exception e) {
			log.error(e.getMessage(),e);
			isDataSavedInOsce=false;
			return isDataSavedInOsce;
		}
		if(isDataSavedInOsce && isDataRemovedFromSPPortal){
			return true;
		}else{
			return false;	
		}
		
	}
	

	private static Boolean removeSPDataFromSPPortal(SpStandardizedPatient spStandardizedPatient,List<SpAnamnesisChecksValue> newAnamnesisCheckValuesList,boolean isDeleteNewImage,
			boolean isStatusExportedAndSurvey) {
		log.info("Removing sp old details and all associated data from sp portal");
		Boolean result=false;
		try{
			spStandardizedPatient = SpStandardizedPatient.findSpStandardizedPatient(spStandardizedPatient.getId());
		
			SPPortalPerson spperson = spStandardizedPatient.getPerson();
			
			spperson.setChanged(false);
			
			spperson.setEditRequestState(EditRequestState.NONE);
			
			spperson.persist();
			
			for(SpAnamnesisChecksValue spAnamnesisChecksValue : newAnamnesisCheckValuesList){
				spAnamnesisChecksValue=SpAnamnesisChecksValue.findSpAnamnesisChecksValue(spAnamnesisChecksValue.getId());
				spAnamnesisChecksValue.remove();
			}
			//Added this code to delete sps uploded new image in spportal and we downloded in osce.
			if(isDeleteNewImage){
				
				if(spStandardizedPatient.getImmagePath()!=null){
					
						//removing old image as it exist.
						String [] imagepath = spStandardizedPatient.getImmagePath().split("/");
						
						if(imagepath.length > 0){
							
							File file = new File(OsMaFilePathConstant.localImageUploadDirectory+ imagepath[imagepath.length-1]);
							
							if(file.exists()){
								file.delete();
							}
						}
					
					
				}
			}
			
			//Added following code to solve issue SPP-10. 
			/*Scenario is that survey is opened, sp still sent edit request, admin has approved it so we exports all sps data to spportal & 
			change sp's status to EXPORTED_AND_SURVEY. Now if sp updates its detail from spportal and admin approves changes from osce.
			Here we are considering two cases either survey is still open or closed. If survey is open then we have sp's status as EXPORTED_AND_SURVEY in this case
			We are removing all sp's data except its id is remain in spportal database and changing its status as INSURVEY (like we have sp in survey). 
			On the other hand if survey is closed then status of this sp is changed to EXPORTED, so we remove all data from spportal after upfating changes in osce. */
			if(isStatusExportedAndSurvey){
				
				Long spAnamnesisFromId = spStandardizedPatient.getAnamnesisForm().getId();
				Long spBankAccountId = spStandardizedPatient.getBankAccount().getId();
				
				//clearing all personal data.
				spStandardizedPatient.setAnamnesisForm(null);
				spStandardizedPatient.setBankAccount(null);
				spStandardizedPatient.setNationality(null);
				spStandardizedPatient.setProfession(null);
				spStandardizedPatient.setBirthday(null);
				spStandardizedPatient.setCity(null);
				spStandardizedPatient.setEmail(null);
				spStandardizedPatient.setGender(null);
				spStandardizedPatient.setHeight(null);
				spStandardizedPatient.setIgnoreSocialInsuranceNo(null);
				spStandardizedPatient.setImmagePath(null);
				spStandardizedPatient.setMaritalStatus(null);
				spStandardizedPatient.setMobile(null);
				spStandardizedPatient.setName(null);
				spStandardizedPatient.setPostalCode(null);
				spStandardizedPatient.setPreName(null);
				spStandardizedPatient.setSocialInsuranceNo(null);
				spStandardizedPatient.setStatus(null);
				spStandardizedPatient.setStreet(null);
				spStandardizedPatient.setTelephone(null);
				spStandardizedPatient.setTelephone2(null);
				spStandardizedPatient.setVideoPath(null);
				spStandardizedPatient.setWeight(null);
				spStandardizedPatient.setWorkPermission(null);
				spStandardizedPatient.setCreated(null);
				//Added as per OMS-157.
				spStandardizedPatient.setCountry(null);
				spStandardizedPatient.persist();
				
				
				//Removing sp's anamnesis form, bank account.
				spStandardizedPatient.deleteAnamnesisFormFromSpportal(spAnamnesisFromId);
				spStandardizedPatient.deleteBankAccountFromSpportal(spBankAccountId);
				
				
				
			}else{
				spStandardizedPatient.setAnamnesisForm(spStandardizedPatient.getAnamnesisForm());
				spStandardizedPatient.setBankAccount(spStandardizedPatient.getBankAccount());
				spStandardizedPatient.remove();
			}
			
			
		}catch (Exception e) {
			result=false;
			log.error(e.getMessage(), e);
			return result;
		}
		result=true;
		return result;
	}

	@Transactional
	private  void deleteAnamnesisFormFromSpportal(Long anamnesisFormId) {

		EntityManager em = SpAnamnesisForm.entityManager();
		
		StringBuilder sql = new StringBuilder("DELETE FROM `anamnesis_form` WHERE `id` ="+ anamnesisFormId);
		
		log.info("Query is" + sql.toString());
		
		Query query =  em.createNativeQuery(sql.toString());
		
		int totalEntryDeleted =query.executeUpdate();
		
		log.info(totalEntryDeleted +" Anamesis From deleted from spportal");
	}
	
	@Transactional
	private void deleteBankAccountFromSpportal(Long bankAccountId) {

		EntityManager em = SpAnamnesisForm.entityManager();
		
		StringBuilder sql = new StringBuilder("DELETE FROM `bankaccount` WHERE `id` ="+ bankAccountId);
		
		log.info("Query is" + sql.toString());
		
		Query query =  em.createNativeQuery(sql.toString());
		
		int totalEntryDeleted =query.executeUpdate();
		
		log.info(totalEntryDeleted +" bank account From deleted from spportal");
	}
	
	public static Boolean removeSPDetailsFromSPPortal(Long standardizedPatientId,Long spStandardizedPatientId,boolean isDeleteNewImage){
		
		log.info("Updating osce sp status to active and removing sp details from sp portal");
		
		boolean  isStatusExportedAndSurvey=false;
		
		StandardizedPatient standardizedPatient = StandardizedPatient.findStandardizedPatient(standardizedPatientId);
		
		//Added following code to solve issue SPP-10. 
		/*Scenario is that survey is opened, sp still sent edit request, admin has approved it so we exports all sps data to spportal & 
		change sp's status to EXPORTED_AND_SURVEY. Now if sp updates its detail from spportal and admin approves changes from osce.
		Here we are considering two cases either survey is still open or closed. If survey is open then we have sp's status as EXPORTED_AND_SURVEY in this case
		We are removing all sp's data except its id is remain in spportal database and changing its status as INSURVEY (like we have sp in survey). 
		On the other hand if survey is closed then status of this sp is changed to EXPORTED, so we remove all data from spportal after upfating changes in osce. */
		
		if(standardizedPatient.getStatus().ordinal()==StandardizedPatientStatus.EXPORTED_AND_SURVEY.ordinal()){
			isStatusExportedAndSurvey=true;
		}
		
		//For issue SPP-10
		if(isStatusExportedAndSurvey){
			standardizedPatient.setStatus(StandardizedPatientStatus.INSURVEY);
		}else{
			standardizedPatient.setStatus(StandardizedPatientStatus.ACTIVE);
		}
		standardizedPatient.persist();
		
		SpStandardizedPatient spStandardizedPatient = SpStandardizedPatient.findSpStandardizedPatient(spStandardizedPatientId);
		List<SpAnamnesisChecksValue> spAnamnesisChecksValuesList = findNewAnamnesisCheckValuesBasedOnFormId(null);
		
		Boolean isRemovedAllData = removeSPDataFromSPPortal(spStandardizedPatient, spAnamnesisChecksValuesList,isDeleteNewImage,isStatusExportedAndSurvey);
	
		return isRemovedAllData;
	}
	private static List<AnamnesisChecksValue> findOldAnamnesisCheckValuesBasedOnFormId(Long anamnesisFormId) {
		
		log.info("finding All OLD AnamnesisChecksValues based on form id : " + anamnesisFormId);
		 
		 EntityManager em = AnamnesisChecksValue.entityManager();
		 
		 String queryString ="SELECT a FROM AnamnesisChecksValue a WHERE a.anamnesisform.id = " + anamnesisFormId + " ORDER BY a.anamnesischeck.text";

		 TypedQuery<AnamnesisChecksValue> query = em.createQuery(queryString, AnamnesisChecksValue.class);  
		 
		 List<AnamnesisChecksValue> resultList = query.getResultList();
	    	
		 return resultList;
	}

	private static List<SpAnamnesisChecksValue> findNewAnamnesisCheckValuesBasedOnFormId(Long anamnesisFormId) {
		
		log.info("finding All NEW AnamnesisChecksValues based on form id : " + anamnesisFormId);
		 
		/* EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");

		 EntityManager em = emFactory.createEntityManager();*/
		 
		 EntityManager em= entityManager();
		 
		 String queryString ="SELECT a FROM SpAnamnesisChecksValue a WHERE a.anamnesisform.id = " + anamnesisFormId + " ORDER BY a.anamnesischeck.text";

		 TypedQuery<SpAnamnesisChecksValue> query = em.createQuery(queryString, SpAnamnesisChecksValue.class);  
		 
		 List<SpAnamnesisChecksValue> resultList = query.getResultList();
	    	
		 return resultList;
	}

	public static SpStandardizedPatient findSPWithId(Long id) {
		
		log.info("finding sp based on id : " + id);
		 
		/* EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");

		 EntityManager em = emFactory.createEntityManager();*/
		 
		 EntityManager em= entityManager();
		 
		 String queryString ="SELECT sp FROM SpStandardizedPatient sp WHERE sp.id="+id;

		 TypedQuery<SpStandardizedPatient> query = em.createQuery(queryString, SpStandardizedPatient.class);  
		 
		 List<SpStandardizedPatient> resultList = query.getResultList();
	    	
		 if(resultList==null || resultList.size()== 0){
			 return null;
		 }else{
			 return resultList.get(0);
		 }
	}

	@Version
    @Column(name = "version")
    private Integer version;

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
            SpStandardizedPatient attached = SpStandardizedPatient.findSpStandardizedPatient(this.id);
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
    public SpStandardizedPatient merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SpStandardizedPatient merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new SpStandardizedPatient().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSpStandardizedPatients() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SpStandardizedPatient o", Long.class).getSingleResult();
    }

	public static List<SpStandardizedPatient> findAllSpStandardizedPatients() {
        return entityManager().createQuery("SELECT o FROM SpStandardizedPatient o", SpStandardizedPatient.class).getResultList();
    }

	public static SpStandardizedPatient findSpStandardizedPatient(Long id) {
        if (id == null) return null;
        return entityManager().find(SpStandardizedPatient.class, id);
    }

	public static List<SpStandardizedPatient> findSpStandardizedPatientEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SpStandardizedPatient o", SpStandardizedPatient.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public String getPreName() {
        return this.preName;
    }

	public void setPreName(String preName) {
        this.preName = preName;
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public String getStreet() {
        return this.street;
    }

	public void setStreet(String street) {
        this.street = street;
    }

	public String getPostalCode() {
        return this.postalCode;
    }

	public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

	public String getCity() {
        return this.city;
    }

	public void setCity(String city) {
        this.city = city;
    }

	public String getTelephone() {
        return this.telephone;
    }

	public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

	public String getTelephone2() {
        return this.telephone2;
    }

	public void setTelephone2(String telephone2) {
        this.telephone2 = telephone2;
    }

	public String getMobile() {
        return this.mobile;
    }

	public void setMobile(String mobile) {
        this.mobile = mobile;
    }

	public String getEmail() {
        return this.email;
    }

	public void setEmail(String email) {
        this.email = email;
    }

	public Date getBirthday() {
        return this.birthday;
    }

	public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

	public Gender getGender() {
        return this.gender;
    }

	public void setGender(Gender gender) {
        this.gender = gender;
    }

	public Integer getHeight() {
        return this.height;
    }

	public void setHeight(Integer height) {
        this.height = height;
    }

	public Integer getWeight() {
        return this.weight;
    }

	public void setWeight(Integer weight) {
        this.weight = weight;
    }

	public String getImmagePath() {
        return this.immagePath;
    }

	public void setImmagePath(String immagePath) {
        this.immagePath = immagePath;
    }

	public String getVideoPath() {
        return this.videoPath;
    }

	public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

	public SpProfession getProfession() {
        return this.profession;
    }

	public void setProfession(SpProfession profession) {
        this.profession = profession;
    }

	public SpNationality getNationality() {
        return this.nationality;
    }

	public void setNationality(SpNationality nationality) {
        this.nationality = nationality;
    }

	public WorkPermission getWorkPermission() {
        return this.workPermission;
    }

	public void setWorkPermission(WorkPermission workPermission) {
        this.workPermission = workPermission;
    }

	public StandardizedPatientStatus getStatus() {
        return this.status;
    }

	public void setStatus(StandardizedPatientStatus status) {
        this.status = status;
    }

	public MaritalStatus getMaritalStatus() {
        return this.maritalStatus;
    }

	public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

	public String getSocialInsuranceNo() {
        return this.socialInsuranceNo;
    }

	public void setSocialInsuranceNo(String socialInsuranceNo) {
        this.socialInsuranceNo = socialInsuranceNo;
    }

	public SpBankaccount getBankAccount() {
        return this.bankAccount;
    }

	public void setBankAccount(SpBankaccount bankAccount) {
        this.bankAccount = bankAccount;
    }

	public SpAnamnesisForm getAnamnesisForm() {
        return this.anamnesisForm;
    }

	public void setAnamnesisForm(SpAnamnesisForm anamnesisForm) {
        this.anamnesisForm = anamnesisForm;
    }

	public SPPortalPerson getPerson() {
        return this.person;
    }

	public void setPerson(SPPortalPerson person) {
        this.person = person;
    }

	public Boolean getIgnoreSocialInsuranceNo() {
        return this.ignoreSocialInsuranceNo;
    }

	public void setIgnoreSocialInsuranceNo(Boolean ignoreSocialInsuranceNo) {
        this.ignoreSocialInsuranceNo = ignoreSocialInsuranceNo;
    }

	public Date getCreated() {
        return this.created;
    }

	public void setCreated(Date created) {
        this.created = created;
    }

	public Set<SpPatientInSemester> getPatientInSemester() {
        return this.patientInSemester;
    }

	public void setPatientInSemester(Set<SpPatientInSemester> patientInSemester) {
        this.patientInSemester = patientInSemester;
    }

	public SpNationality getCountry() {
		return country;
	}

	public void setCountry(SpNationality country) {
		this.country = country;
	}

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AnamnesisForm: ").append(getAnamnesisForm()).append(", ");
        sb.append("BankAccount: ").append(getBankAccount()).append(", ");
        sb.append("Birthday: ").append(getBirthday()).append(", ");
        sb.append("City: ").append(getCity()).append(", ");
        sb.append("Created: ").append(getCreated()).append(", ");
        sb.append("Email: ").append(getEmail()).append(", ");
        sb.append("Gender: ").append(getGender()).append(", ");
        sb.append("Height: ").append(getHeight()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("IgnoreSocialInsuranceNo: ").append(getIgnoreSocialInsuranceNo()).append(", ");
        sb.append("ImmagePath: ").append(getImmagePath()).append(", ");
        sb.append("MaritalStatus: ").append(getMaritalStatus()).append(", ");
        sb.append("Mobile: ").append(getMobile()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("Nationality: ").append(getNationality()).append(", ");
        sb.append("PatientInSemester: ").append(getPatientInSemester() == null ? "null" : getPatientInSemester().size()).append(", ");
        sb.append("Person: ").append(getPerson()).append(", ");
        sb.append("PostalCode: ").append(getPostalCode()).append(", ");
        sb.append("PreName: ").append(getPreName()).append(", ");
        sb.append("Profession: ").append(getProfession()).append(", ");
        sb.append("SocialInsuranceNo: ").append(getSocialInsuranceNo()).append(", ");
        sb.append("Status: ").append(getStatus()).append(", ");
        sb.append("Street: ").append(getStreet()).append(", ");
        sb.append("Telephone: ").append(getTelephone()).append(", ");
        sb.append("Telephone2: ").append(getTelephone2()).append(", ");
        sb.append("Version: ").append(getVersion()).append(", ");
        sb.append("VideoPath: ").append(getVideoPath()).append(", ");
        sb.append("Weight: ").append(getWeight()).append(", ");
        sb.append("WorkPermission: ").append(getWorkPermission());
        return sb.toString();
    }
}
