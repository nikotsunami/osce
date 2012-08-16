package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.allen_sauer.gwt.log.client.Log;

import ch.unibas.medizin.osce.shared.Gender;
import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;
import ch.unibas.medizin.osce.domain.Clinic;
import javax.persistence.ManyToOne;
import ch.unibas.medizin.osce.domain.Office;
import javax.persistence.OneToOne;
import ch.unibas.medizin.osce.domain.Specialisation;

@RooJavaBean
@RooToString
@RooEntity
public class Doctor {

    @Enumerated
    private Gender gender;

    @Size(max = 40)
    private String title;

    @Size(max = 40)
    private String name;

    @Size(max = 40)
    private String preName;

    @Size(max = 40)
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$")
    private String email;

    @Size(max = 30)
    private String telephone;

    @ManyToOne
    private Clinic clinic;

    @OneToOne(cascade = CascadeType.ALL)
    private Office office;

    private Boolean isActive;

    @ManyToOne
    private Specialisation specialisation;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "examiner")
	 private Set<Assignment> assignments = new HashSet<Assignment>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctor")
    private Set<RoleParticipant> roleParticipants = new HashSet<RoleParticipant>();

    public static Long countDoctorsBySearch(String q) {
        EntityManager em = entityManager();
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(o) FROM Doctor o WHERE o.name LIKE :q OR o.preName LIKE :q OR o.email LIKE :q", Long.class);
        query.setParameter("q", "%" + q + "%");
        return query.getSingleResult();
    }

    public static List<Doctor> findDoctorsBySearch(String q, int firstResult, int maxResults) {
        if (q == null) throw new IllegalArgumentException("The q argument is required");
        EntityManager em = entityManager();
        TypedQuery<Doctor> query = em.createQuery("SELECT o FROM Doctor AS o WHERE o.name LIKE :q OR o.preName LIKE :q OR o.email LIKE :q", Doctor.class);
        query.setParameter("q", "%" + q + "%");
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }
    
    
    // SPEC START =
   	public static java.util.List<Doctor> findDoctorWithRoleTopic(Long stadRoleid)  // Fill Doctor Name in Value List Box
   	{
   		EntityManager em = entityManager();
   		Log.info("~QUERY findDoctorWithRoleTopic()");
   		//Log.info("~QUERY BEFOREE EXECUTION role topic id : " + roleTopicid + " Standardized Role ID: " + stadRoleid);		
   		//String queryString="SELECT doc from Doctor doc";
   		//String queryString="select d from Doctor d where d.id not in(select r.doctor from RoleParticipant r join r.standardizedRole sr where sr.id="+stadRoleid+") and d.specialisation in (select s.id from Specialisation s join s.roleTopics rt join rt.standardizedRoles sr where sr.id="+stadRoleid+")";// WORKING		
   		//String queryString="select d from Doctor d where d.id not in(select r.doctor from RoleParticipant r join r.standardizedRole sr where sr.id="+stadRoleid+") and d.specialisation in (select s.id from Specialisation s join s.roleTopics rt join rt.standardizedRoles sr where s.id=rt.specialisation and rt.id=sr.roleTopic and sr.id="+stadRoleid+")";
   		String queryString="select d from Doctor d where d.id not in(select r.doctor from RoleParticipant r join r.standardizedRole sr where sr.id="+stadRoleid+")";
   		//select d.* from doctor d where d.id not in(select r.doctor from role_participant r join standardized_role sr where sr.id = 2 ) and d.specialisation in (select s.id from specialisation s join role_topic rt join standardized_role sr where s.id = rt.specialisation and rt.id = sr.role_topic and sr.id = 2 );
   		Log.info("~QUERY String: " + queryString);
   		TypedQuery<Doctor> q = em.createQuery(queryString, Doctor.class);
   		java.util.List<Doctor> result = q.getResultList();
   		Log.info("~QUERY Result : " + result);
   		return result;
   		//String queryString="SELECT doc from Doctor doc JOIN doc.specialisation sp JOIN sp.roleTopics rt JOIN rt.standardizedRoles sr WHERE rt.id = 1 and  sr.id <>"+id;
   		//String queryString="SELECT doc from Doctor doc JOIN RoleTopic rt with doc.specialisation = rt.specialisation JOIN rt.standardizedRoles sr WHERE sr.id <> " + id;		
   		//String queryString="SELECT sp.doctors from Specialisation sp join sp.roleTopics rt JOIN rt.standardizedRoles sr WHERE sr.id <> " + id;		
   		//String queryString="SELECT distinct doc from Doctor doc JOIN doc.specialisation.roleTopics rt JOIN rt.standardizedRoles sr WHERE rt.id = " + id + " and  sr.id <>"+id;
   	}
   	
   	// SPEC END =
    
   	//Module 6 Start
	public static java.util.List<Doctor> findDoctorByClinicID(Long clinicid)  
   	{
   		EntityManager em = entityManager();
   		String queryString="select d from Doctor d where d.clinic.id = " + clinicid;
   		TypedQuery<Doctor> q = em.createQuery(queryString, Doctor.class);
   		java.util.List<Doctor> result = q.getResultList();
   		return result;
   	}
   	
   	//Module 6 End
	
	// Module10 Create plans
    //Find Student by Osce Id
    public static List<Doctor> findDoctorByOsceId(Long osceId)
    {
		Log.info("Call findDoctorByOsceId for id" + osceId);	
		EntityManager em = entityManager();
		String queryString = "select distinct d from Doctor as d, OsceDay as od, Assignment as assi, Osce as o " +
				"where o.id=od.osce and od.id=assi.osceDay and assi.examiner=d.id and o.id=" + osceId;
		Log.info("Query String: " + queryString);
		TypedQuery<Doctor> q = em.createQuery(queryString,Doctor.class);		
		List<Doctor> result  = q.getResultList();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result);
        return result;    	    
    }
 // E Module10 Create plans
  
   	
    
}
