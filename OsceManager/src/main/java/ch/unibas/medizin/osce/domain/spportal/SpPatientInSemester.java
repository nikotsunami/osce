package ch.unibas.medizin.osce.domain.spportal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.domain.OsceDate;
import ch.unibas.medizin.osce.domain.PatientInSemester;

@RooJavaBean
@RooToString
@RooEntity
@Table(name="patient_in_semester")
public class SpPatientInSemester {

	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
	
	 @Id
	 @Column(name = "id")
	 private Long id;
	 
    @ManyToOne
    private SpSemester semester;

    @ManyToOne
    private SpStandardizedPatient standardizedPatient;

    private Boolean accepted;


    private Integer value = 0;

    @ManyToOne
    private SPPortalPerson person;
 
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "accepted_trainings")
    private Set<SpTrainingDate> trainingDates = new HashSet<SpTrainingDate>();
	
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "accepted_osce")
    private Set<SpOsceDate> osceDates = new HashSet<SpOsceDate>();

    private static transient Logger log = Logger.getLogger(SpPatientInSemester.class);
    
	public static List<SpPatientInSemester> findPatientInSemesterBasedOnSemesterId(Long semId) {
		
		log.info("finding patient in semester based on semester id : " + semId);
		 
		 /*EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");

		 EntityManager em = emFactory.createEntityManager();*/
		 
		EntityManager em  =entityManager();
		
		 String queryString ="select sp from SpPatientInSemester sp where sp.semester.id="+semId + " ORDER BY id DESC";
		 
		 TypedQuery<SpPatientInSemester> query =em.createQuery(queryString,SpPatientInSemester.class);
		 
		 List<SpPatientInSemester> spPatientInSemesterList = query.getResultList();
		 
		 if(spPatientInSemesterList==null || spPatientInSemesterList.size()==0){
			 return null;
		 }else{
		
			 return spPatientInSemesterList;
		 }
		
	}
	
	public static List<SpPatientInSemester> findPatientInSemesterBasedOnSemAndId(Long lastspPatientInSemId, Long semId) {
		try{
			log.info("finding patient in semester based on sem id : " + semId);
			 
			/* EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");

			 EntityManager em = emFactory.createEntityManager();*/
			 
			EntityManager em =entityManager();
			
			 String sql = "SELECT pis FROM SpPatientInSemester AS pis WHERE pis.id >" + lastspPatientInSemId + " AND pis.semester.id="+semId;
		     
			 TypedQuery<SpPatientInSemester> query = em.createQuery(sql, SpPatientInSemester.class);
		     
			 List<SpPatientInSemester> resultList = query.getResultList();
		     
			 if (resultList == null || resultList.size() == 0){ 
				 	return null;
			 }else{
				 return resultList;
			 }
		        
			}catch (Exception e) {
				log.error(e.getMessage(),e);
				return null;
			}
		
	}
}
