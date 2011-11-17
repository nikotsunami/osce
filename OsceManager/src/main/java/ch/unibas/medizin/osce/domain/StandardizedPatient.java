package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import ch.unibas.medizin.osce.shared.Gender;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.Pattern;
import ch.unibas.medizin.osce.domain.Description;
import javax.persistence.OneToOne;
import ch.unibas.medizin.osce.domain.Bankaccount;
import ch.unibas.medizin.osce.domain.Nationality;
import javax.persistence.ManyToOne;
import ch.unibas.medizin.osce.domain.Profession;
import ch.unibas.medizin.osce.domain.AnamnesisForm;
import java.util.Set;
import ch.unibas.medizin.osce.domain.LangSkill;
import java.util.HashSet;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

@RooJavaBean
@RooToString
@RooEntity
public class StandardizedPatient {

    @Enumerated
    private Gender gender;

    @Size(max = 40)
    private String name;

    @Size(max = 40)
    private String preName;

    @Size(max = 60)
    private String street;

    @Size(max = 30)
    private String city;

    private Integer postalCode;

    @Size(max = 30)
    private String telephone;
    
    @Size(max = 30)
    private String telephone2;

    @Size(max = 30)
    private String mobile;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date birthday;

    @Size(max = 40)
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$")
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    private Description descriptions;

    @OneToOne(cascade = CascadeType.ALL)
    private Bankaccount bankAccount;

    @ManyToOne
    private Nationality nationality;

    @ManyToOne
    private Profession profession;

    @OneToOne(cascade = CascadeType.ALL)
    private AnamnesisForm anamnesisForm;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "standardizedpatient")
    private Set<LangSkill> langskills = new HashSet<LangSkill>();
    
    public static Long countPatientsBySearch(String q) {
    	EntityManager em = entityManager();
    	TypedQuery<Long> query = em.createQuery("SELECT COUNT(o) FROM StandardizedPatient o WHERE o.name LIKE :q OR o.preName LIKE :q OR o.email LIKE :q", Long.class);
    	query.setParameter("q", "%" + q + "%");
    	
    	return query.getSingleResult();
    }
    
    public static List<StandardizedPatient> findPatientsBySearch(String q, int firstResult, int maxResults) {
        if (q == null) throw new IllegalArgumentException("The q argument is required");
        EntityManager em = entityManager();
        TypedQuery<StandardizedPatient> query = em.createQuery("SELECT o FROM StandardizedPatient AS o WHERE o.name LIKE :q OR o.preName LIKE :q OR o.email LIKE :q", StandardizedPatient.class);
        query.setParameter("q", "%" + q + "%");
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        
        return query.getResultList();
    }
}
