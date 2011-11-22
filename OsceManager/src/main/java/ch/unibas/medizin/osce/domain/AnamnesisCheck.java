package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.Size;

import java.util.List;
import java.util.Set;
import ch.unibas.medizin.osce.domain.AnamnesisChecksValue;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;

import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.TypedQuery;

@RooJavaBean
@RooToString
@RooEntity
public class AnamnesisCheck {

    @Size(max = 255)
    private String text;
    
    @Size(max = 255)
    private String value;
    
    private Integer sort_order;
    
    @Enumerated
    private AnamnesisCheckTypes type;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "anamnesischeck")
    private Set<AnamnesisChecksValue> anamnesischecksvalues = new HashSet<AnamnesisChecksValue>();
    
    public static Long countAnamnesisChecksBySearch(String q) {
    	EntityManager em = entityManager();
    	TypedQuery<Long> query = em.createQuery("SELECT COUNT(o) FROM AnamnesisCheck o WHERE o.text LIKE :q", Long.class);
    	query.setParameter("q", "%" + q + "%");
    	
    	return query.getSingleResult();
    }
    
    public static List<AnamnesisCheck> findAnamnesisChecksBySearch(String q, int firstResult, int maxResults) {
        if (q == null) throw new IllegalArgumentException("The q argument is required");
        EntityManager em = entityManager();
        TypedQuery<AnamnesisCheck> query = em.createQuery("SELECT o FROM AnamnesisCheck AS o WHERE o.text LIKE :q", AnamnesisCheck.class);
        query.setParameter("q", "%" + q + "%");
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        
        return query.getResultList();
    }
}
