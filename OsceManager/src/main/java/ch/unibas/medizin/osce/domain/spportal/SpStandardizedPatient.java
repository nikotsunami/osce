package ch.unibas.medizin.osce.domain.spportal;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.log4j.Logger;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.domain.AnamnesisChecksValue;
import ch.unibas.medizin.osce.domain.Bankaccount;
import ch.unibas.medizin.osce.domain.Nationality;
import ch.unibas.medizin.osce.domain.Profession;
import ch.unibas.medizin.osce.domain.StandardizedPatient;
import ch.unibas.medizin.osce.shared.EditRequestState;
import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.MaritalStatus;
import ch.unibas.medizin.osce.shared.StandardizedPatientStatus;
import ch.unibas.medizin.osce.shared.WorkPermission;



@RooJavaBean
@RooToString
@RooEntity
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
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "standardizedPatient")
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
		 
		 EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");

		 EntityManager em = emFactory.createEntityManager();
		 
		 String queryString ="select sp from SpStandardizedPatient sp where sp.person.changed=1";
		 
		 TypedQuery<SpStandardizedPatient> query =em.createQuery(queryString,SpStandardizedPatient.class);
		 
		 List<SpStandardizedPatient> spStandardizedPatientsList = query.getResultList();
		 
		 return spStandardizedPatientsList;
		 
	 }
	 
	 public static List<SpStandardizedPatient> findAllSPWhoEditedDetails(int firstResult,int maxResults){
			
		 log.info("finding All sps who edited their data");
		 
		 EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");

		 EntityManager em = emFactory.createEntityManager();
		 
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
		 
		 EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");

		 EntityManager em = emFactory.createEntityManager();
		 
		 String queryString ="SELECT a FROM SpAnamnesisChecksValue a WHERE a.anamnesischeck.text in (" + titleText + ") AND a.anamnesisform.id = " + anamnesisFormId + " ORDER BY a.anamnesischeck.text";

		 TypedQuery<SpAnamnesisChecksValue> query = em.createQuery(queryString, SpAnamnesisChecksValue.class);  
		 
		 List<SpAnamnesisChecksValue> resultList = query.getResultList();
	    	
		 return resultList;
	}
	
	public static Boolean moveChangedDetailsOfSPFormSPPortal(Long standardizedPatientId, Long spStandardizedPatientId){
		
		Boolean isDataSavedInOsce =false;
		Boolean isDataRemovedFromSPPortal=false;
		try{
				StandardizedPatient standardizedPatient = StandardizedPatient.findStandardizedPatient(standardizedPatientId);
				
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
				//standardizedPatient.setImmagePath(spStandardizedPatient.getImmagePath());
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
				standardizedPatient.setStatus(StandardizedPatientStatus.ACTIVE);
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
				
				isDataRemovedFromSPPortal=removeSPDataFromSPPortal(spStandardizedPatient,newAnamnesisCheckValuesList);
				
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
	

	private static Boolean removeSPDataFromSPPortal(SpStandardizedPatient spStandardizedPatient,List<SpAnamnesisChecksValue> newAnamnesisCheckValuesList) {
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
			
			spStandardizedPatient.setAnamnesisForm(spStandardizedPatient.getAnamnesisForm());
			spStandardizedPatient.setBankAccount(spStandardizedPatient.getBankAccount());
			spStandardizedPatient.setNationality(spStandardizedPatient.getNationality());
			spStandardizedPatient.setProfession(spStandardizedPatient.getProfession());
			spStandardizedPatient.remove();
			
			
		}catch (Exception e) {
			result=false;
			log.error(e.getMessage(), e);
			return result;
		}
		result=true;
		return result;
	}

	public static Boolean removeSPDetailsFromSPPortal(Long standardizedPatientId,Long spStandardizedPatientId){
		
		log.info("Updating osce sp status to active and removing sp details from sp portal");
		
		StandardizedPatient standardizedPatient = StandardizedPatient.findStandardizedPatient(standardizedPatientId);
		standardizedPatient.setStatus(StandardizedPatientStatus.ACTIVE);
		standardizedPatient.persist();
		
		SpStandardizedPatient spStandardizedPatient = SpStandardizedPatient.findSpStandardizedPatient(spStandardizedPatientId);
		List<SpAnamnesisChecksValue> spAnamnesisChecksValuesList = findNewAnamnesisCheckValuesBasedOnFormId(null);
		
		Boolean isRemovedAllData = removeSPDataFromSPPortal(spStandardizedPatient, spAnamnesisChecksValuesList);
	
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
		 
		 EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");

		 EntityManager em = emFactory.createEntityManager();
		 
		 String queryString ="SELECT a FROM SpAnamnesisChecksValue a WHERE a.anamnesisform.id = " + anamnesisFormId + " ORDER BY a.anamnesischeck.text";

		 TypedQuery<SpAnamnesisChecksValue> query = em.createQuery(queryString, SpAnamnesisChecksValue.class);  
		 
		 List<SpAnamnesisChecksValue> resultList = query.getResultList();
	    	
		 return resultList;
	}

	public static SpStandardizedPatient findSPWithId(Long id) {
		
		log.info("finding sp based on id : " + id);
		 
		 EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");

		 EntityManager em = emFactory.createEntityManager();
		 
		 String queryString ="SELECT sp FROM SpStandardizedPatient sp WHERE sp.id="+id;

		 TypedQuery<SpStandardizedPatient> query = em.createQuery(queryString, SpStandardizedPatient.class);  
		 
		 List<SpStandardizedPatient> resultList = query.getResultList();
	    	
		 if(resultList==null || resultList.size()== 0){
			 return null;
		 }else{
			 return resultList.get(0);
		 }
	}
}
