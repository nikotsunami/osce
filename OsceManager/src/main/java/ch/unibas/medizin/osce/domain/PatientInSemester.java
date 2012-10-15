package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import com.allen_sauer.gwt.log.client.Log;
import ch.unibas.medizin.osce.domain.Semester;
import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;
import ch.unibas.medizin.osce.domain.StandardizedPatient;
import ch.unibas.medizin.osce.shared.StandardizedPatientStatus;

@RooJavaBean
@RooToString
@RooEntity(finders = { "findPatientInSemestersBySemester" })
public class PatientInSemester {

    @ManyToOne
    private Semester semester;

    @ManyToOne
    private StandardizedPatient standardizedPatient;

    private Boolean accepted;

    @ManyToMany
    @JoinTable(name = "accepted_osce_days")
    private Set<OsceDay> osceDays = new HashSet<OsceDay>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "patientInSemester")
    private Set<PatientInRole> patientInRole = new HashSet<PatientInRole>();

    @ManyToMany
    @JoinTable(name = "accepted_trainings")
    private Set<Training> trainings = new HashSet<Training>();

    private Integer value = 0;

    public static PatientInSemester findPatientInSemesterByStandardizedPatient(StandardizedPatient patient) {
        if (patient == null) return null;
        EntityManager em = entityManager();
        TypedQuery<PatientInSemester> query = em.createQuery("SELECT o FROM PatientInSemester AS o WHERE o.standardizedPatient = :standardizedPatient", PatientInSemester.class);
        query.setParameter("standardizedPatient", patient);
        List<PatientInSemester> resultList = query.getResultList();
        if (resultList == null || resultList.size() == 0) return null;
        return resultList.get(0);
    }
    
	public static List<PatientInSemester> findPatientInSemesterBySemester(Long semesterId) {
		if (semesterId == null)
			return new ArrayList<PatientInSemester>();

		EntityManager em = entityManager();
		TypedQuery<PatientInSemester> query = em.createQuery("SELECT o FROM PatientInSemester AS o WHERE o.semester.id = :semesterId", PatientInSemester.class);
		query.setParameter("semesterId", semesterId);

		List<PatientInSemester> resultList = query.getResultList();
		if (resultList == null || resultList.size() == 0)
			return new ArrayList<PatientInSemester>();

		return resultList;
	}
	
	
	public static List<StandardizedPatient> findAvailableSPBySemester(Long semesterId) {
		if (semesterId == null)
			return new ArrayList<StandardizedPatient>();

		EntityManager em = entityManager();
		String strQuery = "SELECT sp FROM StandardizedPatient AS sp WHERE sp.id not in ( SELECT tempPIS.standardizedPatient.id FROM PatientInSemester AS tempPIS where tempPIS.semester.id = " + semesterId +")";
		Log.info("Query is : "+ strQuery);
		TypedQuery<StandardizedPatient> query = em.createQuery(strQuery, StandardizedPatient.class);

		List<StandardizedPatient> resultList = query.getResultList();
		if (resultList == null || resultList.size() == 0)
			return new ArrayList<StandardizedPatient>();

		return resultList;
	}
	
	public static Boolean findAvailableSPActiveBySemester(Long semesterId) {
		if (semesterId == null){
			return false;
		}

		EntityManager em = entityManager();
		String strQuery = "SELECT sp FROM StandardizedPatient AS sp WHERE sp.status="+ StandardizedPatientStatus.ACTIVE.ordinal() +" and sp.id not in ( SELECT tempPIS.standardizedPatient.id FROM PatientInSemester AS tempPIS where tempPIS.semester.id = " + semesterId +")";
		Log.info("Query is : "+ strQuery);
		TypedQuery<StandardizedPatient> query = em.createQuery(strQuery, StandardizedPatient.class);

		List<StandardizedPatient> resultList = query.getResultList();
		if (resultList == null || resultList.size() == 0){
			return false;}
		
		else{
			
			PatientInSemester patientInSemester;
			Semester semester = Semester.findSemester(semesterId);
			
			for (StandardizedPatient standardizedPatient : resultList) {
				patientInSemester = new PatientInSemester();
				
				patientInSemester.setSemester(semester);
				patientInSemester.setStandardizedPatient(standardizedPatient);
				patientInSemester.setAccepted(false);
				patientInSemester.persist();
			}
			return true;
		}		
	}
	
    
 // Module10 Create plans	
 	public static List<PatientInSemester> findPatientInSemesterBySemesterPatient(
 			Long standardizedPatientId, Long semesterId) {
 		if (standardizedPatientId == null || semesterId == null)
 			return null;
 		EntityManager em = entityManager();
 		TypedQuery<PatientInSemester> query = em
 				.createQuery(
 						"SELECT o FROM PatientInSemester AS o WHERE o.standardizedPatient.id = :standardizedPatientId and o.semester.id = :semesterId",
 						PatientInSemester.class);
 		query.setParameter("standardizedPatientId", standardizedPatientId);
 		query.setParameter("semesterId", semesterId);

 		List<PatientInSemester> resultList = query.getResultList();
 		if (resultList == null || resultList.size() == 0)
 			return null;
 		return resultList;
 	}
 	// E Module10 Create plans

// private static String queryBase = "FROM PatientInSemester AS o WHERE o.standardizedPatient.id In ( ";

    private static String selectBase = "SELECT o ";

    private static String selectCountBase = "SELECT COUNT(o) ";

    private static String queryBase = "FROM PatientInSemester AS o ";
   
    private static String joinBase = " JOIN o.osceDays oD ";

    private static String whereBase = "WHERE";
    
    private static String joinQueryBase = " oD.id = :oDayId and ";
    
    private static String patientBase = " o.standardizedPatient.id In ( ";

    private static String semesterCriteriaQuery = " ) and o.semester.id = :semesterId";

    public static List<PatientInSemester> findPatientInSemesterByAdvancedCriteria(Long semesterId, List<AdvancedSearchCriteria> searchCriteria) {
        EntityManager em = entityManager();
        String stanardizedPatientString = getStanardizedPatientIDList(searchCriteria);
        if (stanardizedPatientString == null) {
            Log.info("Return as null");
            return new ArrayList<PatientInSemester>();
        }
        TypedQuery<PatientInSemester> query = em.createQuery(selectBase + queryBase + whereBase + patientBase + stanardizedPatientString + semesterCriteriaQuery, PatientInSemester.class);
        query.setParameter("semesterId", semesterId);
        Log.info("!!!!! Query is : " + selectBase + queryBase + whereBase + patientBase + stanardizedPatientString + semesterCriteriaQuery + semesterId);
		
        List<PatientInSemester> resultList = new ArrayList<PatientInSemester>();
		
		resultList = query.getResultList();
		
//		if (resultList == null || resultList.size() == 0) {
//			Log.info("Size of PatientInSemester , for advanced search is : " + resultList.size());
//			//			return new ArrayList<PatientInSemester>();
//		}
		
        Log.info("Size of PatientInSemester , for advanced search is : " + resultList.size());
        return resultList;
    }
    
    public static Boolean checkAndSetFitCriteriaOfRole(OscePost post,Long semesterId, List<AdvancedSearchCriteria> searchCriteria)
    {
    	
    	try
    	{
    	List<PatientInSemester> patientInSemesters=findPatientInSemesterByAdvancedCriteria(semesterId,searchCriteria);
    	
    	 Iterator<PatientInRole> patientInRoleIterator=post.getPatientInRole().iterator();
    	
    	while(patientInRoleIterator.hasNext())
    	{
    		PatientInRole patientInRole=patientInRoleIterator.next();
    		
    		boolean isFirstAssigned=false;
    		
    		for(PatientInSemester p:patientInSemesters)
    		{
    			if(p.getId().equals(patientInRole.getPatientInSemester()))
    			{
    				isFirstAssigned=true;
    				break;
    			}
    			
    			
    		}
    		if(isFirstAssigned)
    			patientInRole.setFit_criteria(true);
    		else
    			patientInRole.setFit_criteria(false);
    		patientInRole.persist();
    	}
    	return true;
    	}
    	catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }
    
    public static List<PatientInSemester> findPatientInSemesterByOsceDayAdvancedCriteria(Long semesterId,Long osceDayId,Boolean useOsceDay, List<AdvancedSearchCriteria> searchCriteria) {
//        if(useOsceDay){
    	EntityManager em = entityManager();
        String stanardizedPatientString = getStanardizedPatientIDList(searchCriteria);
        if (stanardizedPatientString == null) {
            Log.info("Return as null");
            return new ArrayList<PatientInSemester>();
        }
		TypedQuery<PatientInSemester> query = em.createQuery(selectBase + queryBase + joinBase + whereBase + joinQueryBase + patientBase + stanardizedPatientString + semesterCriteriaQuery, PatientInSemester.class);
        query.setParameter("semesterId", semesterId);
        query.setParameter("oDayId", osceDayId);
        
        Log.info("!!!!! Query is : " +selectBase + queryBase + joinBase + whereBase + joinQueryBase + patientBase + stanardizedPatientString + semesterCriteriaQuery + semesterId);
		
        List<PatientInSemester> resultList = new ArrayList<PatientInSemester>();
		
		resultList = query.getResultList();
		
//		if (resultList == null || resultList.size() == 0) {
//			Log.info("Size of PatientInSemester , for advanced search is : " + resultList.size());
//			//			return new ArrayList<PatientInSemester>();
//		}
		
        Log.info("Size of PatientInSemester , for advanced search is : " + resultList.size());
        return resultList;
//        }        else{
//        	return findPatientInSemesterByAdvancedCriteria(semesterId, searchCriteria);
//        }
    }

    public static Long countPatientinSemesterByAdvancedCriteria(Long semesterId, List<AdvancedSearchCriteria> searchCriteria) {
        String stanardizedPatientString = getStanardizedPatientIDList(searchCriteria);
        if (stanardizedPatientString == null) {
            Log.info("Return as null");
            return 0L;
        }
        EntityManager em = entityManager();
        Log.info("!!!!! Query is : " +selectCountBase + queryBase + whereBase+ patientBase+ stanardizedPatientString + semesterCriteriaQuery + semesterId);
        TypedQuery<Long> query = em.createQuery(selectCountBase + queryBase + whereBase+ patientBase+ stanardizedPatientString + semesterCriteriaQuery, Long.class);
        query.setParameter("semesterId", semesterId);
        return query.getSingleResult();
    }

    private static String getStanardizedPatientIDList(List<AdvancedSearchCriteria> searchCriteria) {
        List<StandardizedPatient> standardizedPatientList = StandardizedPatient.findPatientsByAdvancedCriteria(searchCriteria);
        if (standardizedPatientList == null || standardizedPatientList.size() == 0) {
            Log.info("Return as null");
            return null;
        }
        Iterator<StandardizedPatient> standardizedPatientIterator = standardizedPatientList.iterator();
        StringBuilder standardizedPatientId = new StringBuilder();
        Log.info("Size of standardizedPatientList is : " + standardizedPatientList.size());
        while (standardizedPatientIterator.hasNext()) {
            StandardizedPatient standardizedPatient = (StandardizedPatient) standardizedPatientIterator.next();
            standardizedPatientId.append(standardizedPatient.getId().toString());
            if (standardizedPatientIterator.hasNext()) {
                standardizedPatientId.append(" ,");
            }
        }
        return standardizedPatientId.toString();
    }
}
