package ch.unibas.medizin.osce.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

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
    	
    	String queryString="SELECT o FROM StudentOsces as o  where o.osce="+id;
    	TypedQuery<StudentOsces> q = em.createQuery(queryString, StudentOsces.class);
      	return q.getResultList();
    }
    
    public static int findStudentByStudIdAndOsceId(Long studid, Long osceid)
    {
    	EntityManager em = entityManager();
    	String s = "SELECT o FROM StudentOsces o WHERE o.student =" + studid +" AND o.osce ="+ osceid;
    	TypedQuery<StudentOsces> q = em.createQuery(s, StudentOsces.class);
       	return q.getResultList().size();
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
    
    public static List<StudentOsces> findStudentEntriesByNameTest(String name,Long id) {
        if (name == null) throw new IllegalArgumentException("The name argument is required");
        
        EntityManager em = entityManager();
        TypedQuery<StudentOsces> q = em.createQuery("SELECT o FROM StudentOsces o WHERE  o.student.name LIKE :name and o.osce.id = " + id, StudentOsces.class);
        q.setParameter("name", "%" + name + "%");
        return q.getResultList();
    }
    
    public static List<StudentOsces> findStudentByRange(int start, int max, Long id, String name)
    {
    	EntityManager em = entityManager();
    	
    	String queryString = "";
    	if (name.equals(""))
    		queryString="SELECT o FROM StudentOsces as o where o.osce = " + id;
    	else
    		queryString="SELECT o FROM StudentOsces as o where o.student.name LIKE '%" + name + "%' AND o.osce = " + id;
    	
    	TypedQuery<StudentOsces> q = em.createQuery(queryString, StudentOsces.class);
    	q.setFirstResult(start);
    	q.setMaxResults(max);
    	return q.getResultList();
    }
    
    public static Integer countStudentByRange(Long id, String name)
    {
    	EntityManager em = entityManager();
    	String queryString = "";
    	if (name.equals(""))
    		queryString="SELECT o FROM StudentOsces as o where o.osce = " + id;
    	else
    		queryString="SELECT o FROM StudentOsces as o where o.student.name LIKE '%" + name + "%' AND o.osce = " + id;
    	
    	TypedQuery<StudentOsces> q = em.createQuery(queryString, StudentOsces.class);
    	return q.getResultList().size();
    }
    
    public static Integer countStudentByOsce(Long osceId)
    {
    	EntityManager em = entityManager();
    	String queryString = "SELECT o FROM StudentOsces as o where o.osce = " + osceId + " AND o.isEnrolled = true";
    	TypedQuery<StudentOsces> q = em.createQuery(queryString, StudentOsces.class);
    	return q.getResultList().size();
    }
}
