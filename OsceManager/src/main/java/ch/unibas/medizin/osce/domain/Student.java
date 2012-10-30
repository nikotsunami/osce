package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.allen_sauer.gwt.log.client.Log;

import ch.unibas.medizin.osce.shared.Gender;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;

@RooJavaBean
@RooToString
@RooEntity
public class Student {

    @Enumerated
    private Gender gender;
    
    private String name;

    private String preName;

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$")
    private String email;
    
    private String studentId;
    
    private String street;
    
    private String city;
    
    // Module10 Create plans
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student")
    private Set<Assignment> assignments = new HashSet<Assignment>();
    // E Module10 Create plans
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student")
    private Set<StudentOsces> studentOsces = new HashSet<StudentOsces>();
    
    public static List<Student> findStudentByStudentIdAndByEmail(String studid, String email)
    {
    	EntityManager em = entityManager();
 
    	String sql = "";
    	
    	if (email.equals(""))
    		sql = "SELECT s FROM Student s WHERE s.studentId = '" + studid + "'";
    	else if (studid.equals(""))
    		sql = "SELECT s FROM Student s WHERE s.email = '" + email + "'";
    	else
    		sql = "SELECT s FROM Student s WHERE s.studentId = " + studid + " AND s.email = '" + email + "'";
    	
    	TypedQuery<Student> q = em.createQuery(sql, Student.class);
    	return q.getResultList();
    }
    
    public static List<Student> findStudentByEmail(String email)
    {
    	EntityManager em = entityManager();
    	TypedQuery<Student> q = em.createQuery("SELECT o FROM Student o WHERE o.email LIKE :studemail", Student.class);
     	q.setParameter("studemail", "%" + email + "%");
     	return q.getResultList();
    }
    
    public static Long countStudentByName(String name) {
    	EntityManager em = entityManager();
    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM Student o WHERE o.name LIKE :name", Long.class);
    	q.setParameter("name", "%" + name + "%");
    	
    	return q.getSingleResult();
    }
    
    public static List<Student> findStudentEntriesByName(String name, int firstResult, int maxResults) {
    	
        if (name == null) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = entityManager();
        TypedQuery<Student> q = em.createQuery("SELECT o FROM Student AS o WHERE o.name LIKE :name", Student.class);
        q.setParameter("name", "%" + name + "%");
        q.setFirstResult(firstResult);
        q.setMaxResults(maxResults);
        
        return q.getResultList();
    }

    // Module10 Create plans
    //Find Student by Osce Id
    public static List<Student> findStudentByOsceId(long osceId)
    {
		Log.info("Call findStudentByOsceId for id" + osceId);	
		EntityManager em = entityManager();
		//String queryString = "select distinct stud from Student as stud, OsceDay as od, Assignment as assi, Osce as o " +"where o.id=od.osce and od.id=assi.osceDay and assi.student=stud.id and o.id=" + osceId;
		
		// Fetch All The Student Which are in this OSCE
		//String queryString="select id from student where id in (select id  from student_osces where osce="+osceId+")";
		
		// Fetch All The Student which are in this OSCE and has a ASSIGNMENT
		String queryString="select distinct stud from Student as stud where stud.id in (select assi.student from Assignment as assi where assi.osceDay in(select id from OsceDay where osce=" + osceId + ") and assi.student is not null)";		
		
		Log.info("Query String: " + queryString);
		TypedQuery<Student> q = em.createQuery(queryString,Student.class);		
		List<Student> result  = q.getResultList();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result);
        return result;    	    
    }
 // E Module10 Create plans
   
}
