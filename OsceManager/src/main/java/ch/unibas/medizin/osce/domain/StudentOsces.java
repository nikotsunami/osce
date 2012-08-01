package ch.unibas.medizin.osce.domain;

import java.util.List;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import ch.unibas.medizin.osce.domain.Osce;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import ch.unibas.medizin.osce.domain.Student;

@RooJavaBean
@RooToString
@RooEntity
public class StudentOsces {

    private Boolean isEnrolled;

    @ManyToOne
    @NotNull
    private Osce osce;

    @ManyToOne
    @NotNull
    private Student student;
    
    public static List<StudentOsces> findStudentOsceByOsce(Long id) {
    	EntityManager em = entityManager();
    	System.out.println("BEFORE CALL------->");
    	String queryString="SELECT o FROM StudentOsces as o  where o.osce="+id;
    	System.out.println("query--"+ queryString);
    	TypedQuery<StudentOsces> q = em.createQuery(queryString, StudentOsces.class);
    
    	System.out.println("success done with size");
    	
    	return q.getResultList();
    }
    
    public static Long findStudentByStudIdAndOsceId(Long studid, Long osceid)
    {
    	EntityManager em = entityManager();
    	String s = "SELECT COUNT(o) FROM StudentOsces o WHERE o.student =" + studid +" AND o.osce ="+ osceid;
    	TypedQuery<Long> q = em.createQuery(s, Long.class);
       	return q.getSingleResult();
    }
    
    public static Long countStudentByName(String name1,Long id) {
    	EntityManager em = entityManager();
    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM StudentOsces o WHERE o.student.name LIKE :name1 and o.osce.id LIKE :id", Long.class);
    	q.setParameter("name1", "%" + name1 + "%");
    	 q.setParameter("id", id);
    	return q.getSingleResult();
    }
    
    public static List<StudentOsces> findStudentEntriesByName(String name,Long id, int firstResult, int maxResults) {
        if (name == null) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = entityManager();
        TypedQuery<StudentOsces> q = em.createQuery("SELECT o FROM StudentOsces o WHERE  o.student.name LIKE :name and o.osce.id LIKE :id", StudentOsces.class);
        q.setParameter("name", "%" + name + "%");
        q.setParameter("id", id);
        q.setFirstResult(firstResult);
        q.setMaxResults(maxResults);
        
        return q.getResultList();
    }
}
