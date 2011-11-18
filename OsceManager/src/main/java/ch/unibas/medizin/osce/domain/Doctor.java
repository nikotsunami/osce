package ch.unibas.medizin.osce.domain;

import java.util.List;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import ch.unibas.medizin.osce.shared.Gender;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;
import ch.unibas.medizin.osce.domain.Clinic;
import javax.persistence.ManyToOne;
import ch.unibas.medizin.osce.domain.Office;
import javax.persistence.OneToOne;

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
}
