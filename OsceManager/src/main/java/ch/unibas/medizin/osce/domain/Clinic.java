package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import javax.persistence.Column;
import javax.validation.constraints.Size;

import java.util.List;
import java.util.Set;
import ch.unibas.medizin.osce.domain.Doctor;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.TypedQuery;

@RooJavaBean
@RooToString
@RooEntity
public class Clinic {

    @NotNull
    @Column(unique = true)
    @Size(max = 60)
    private String name;

    @Size(max = 60)
    private String street;

    @Size(max = 30)
    private String city;

    private Integer postalCode;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clinic")
    private Set<Doctor> doctors = new HashSet<Doctor>();
    
    
    public static List<Clinic> findAllDoctorsId(Long id) {
    	EntityManager em = entityManager();
    	System.out.println("BEFORE CALL------->");
    	String queryString="SELECT o FROM Clinic as o  where o.id="+id;
    	System.out.println("query--"+ queryString);
    	TypedQuery<Clinic> q = em.createQuery(queryString, Clinic.class);
    	System.out.println("success done with size");
    	
    	return q.getResultList();
    }
    
    public static List<Clinic> findListOfDocsId(Long id) {
    	EntityManager em = entityManager();
    	System.out.println("BEFORE CALL------->");
    	String queryString="SELECT o FROM Clinic as o  where o.id="+id;
    	System.out.println("query--"+ queryString);
    	TypedQuery<Clinic> q = em.createQuery(queryString, Clinic.class);
    	System.out.println("success done with size");
    	
    	return q.getResultList();
    }
    
    public static Long countClinicsBySearch(String q) {
    	EntityManager em = entityManager();
    	TypedQuery<Long> query = em.createQuery("SELECT COUNT(o) FROM Clinic o WHERE o.name LIKE :q", Long.class);
    	query.setParameter("q", "%" + q + "%");
    	
    	return query.getSingleResult();
    }
    
    public static List<Clinic> findClinicsBySearch(String q, int firstResult, int maxResults) {
        if (q == null) throw new IllegalArgumentException("The q argument is required");
        EntityManager em = entityManager();
        TypedQuery<Clinic> query = em.createQuery("SELECT o FROM Clinic AS o WHERE o.name LIKE :q", Clinic.class);
        query.setParameter("q", "%" + q + "%");
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        
        return query.getResultList();
    }
    
   public static Long findSpecialist(){
    	EntityManager em = entityManager();
    	TypedQuery<Long> query = em.createQuery("SELECT d.email FROM Clinic AS o, Doctor AS d WHERE o.id = 3", Long.class);
    	return query.getSingleResult();

    	
    }
}
