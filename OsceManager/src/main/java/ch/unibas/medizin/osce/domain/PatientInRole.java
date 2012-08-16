package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.allen_sauer.gwt.log.client.Log;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;

@RooJavaBean
@RooToString
@RooEntity
public class PatientInRole {

    @ManyToOne
    private PatientInSemester patientInSemester;

    @ManyToOne
    private OscePost oscePost;

    Boolean fit_criteria;

    Boolean is_backup;

    Boolean stayInPost;
    
 // Module10 Create plans
 	@OneToMany(cascade = CascadeType.ALL, mappedBy = "patientInRole")
     private Set<Assignment> assignments = new HashSet<Assignment>();
 	// E Module10 Create plans

    private static List<PatientInRole> getPatientIRoleList(Long osceId) {
        List<PatientInRole> patientInRoleList = findAllPatientInRoles();
        for (Iterator<PatientInRole> iterator = patientInRoleList.iterator(); iterator.hasNext(); ) {
            PatientInRole patientInRole = (PatientInRole) iterator.next();
            if (patientInRole.getOscePost().getOsceSequence().getOsceDay().getOsce().getId().longValue() == osceId.longValue()) {
                patientInRole.remove();
            }
        }
        return null;
    }

    public static Integer removePatientInRoleByOSCE(Long osceId) {
        getPatientIRoleList(osceId);
        return 0;
    }
    
    public static TypedQuery<PatientInRole> findPatientInRolesByPatientInSemesterAndOscePostIsNull(PatientInSemester patientInSemester) {
        if (patientInSemester == null) throw new IllegalArgumentException("The patientInSemester argument is required");
        EntityManager em = PatientInRole.entityManager();
        TypedQuery<PatientInRole> q = em.createQuery("SELECT o FROM PatientInRole AS o WHERE o.patientInSemester = :patientInSemester AND o.oscePost IS NULL", PatientInRole.class);
        q.setParameter("patientInSemester", patientInSemester);
        return q;
    }
    
 // Module10 Create plans
    //Find  patient in ROle by SP Id and Semester Id
    public static List<PatientInRole> findPatientsInRoleForAssignmentBySPIdandSemesterId(long spId,long semId)
    {
		Log.info("Call findPatientsInRoleForAssignmentBySPIdandSemesterId for SP id" + spId + "for Semester" +semId);	
		EntityManager em = entityManager();
		/*select * from assignment where patient_in_role in (
	    		select patient_in_role.id from patient_in_role where patient_in_semester in (select patient_in_semester.id from patient_in_semester,standardized_patient 
	    		where  patient_in_semester.standardized_patient=standardized_patient.id
	    		       and standardized_patient.id=19
	    		       and patient_in_semester.semester=1));*/
		String queryString = "select distinct assi.patientInRole from Assignment assi where assi.patientInRole in (select pir.id from PatientInRole pir where pir.patientInSemester in " +
				"(select pis.id from PatientInSemester pis, StandardizedPatient sp where pis.standardizedPatient=sp.id and sp.id="+spId+" and pis.semester="+semId+"))";
		Log.info("Query String: " + queryString);
		TypedQuery<PatientInRole> q = em.createQuery(queryString,PatientInRole.class);		
		List<PatientInRole> result  = q.getResultList();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result.size());
        return result;    	    
    }
    
    public static List<Long> findPatientInRoleByOsceDayfromAssignment(long osceDayId)
    {
    	Log.info("Call findPatientInRoleByOsceDayfromAssignment for Day id" + osceDayId);	
		EntityManager em = entityManager();
		String queryString = "select distinct patientInRole.id from Assignment where osceDay=" + osceDayId;
		Log.info("Query String: " + queryString);
		TypedQuery<Long> q = em.createQuery(queryString,Long.class);		
		List<Long> result  = q.getResultList();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result.size());
        return result;    	    
    }
    // E Module10 Create plans    
}
