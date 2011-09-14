package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.Size;

import java.util.List;
import java.util.Set;
import ch.unibas.medizin.osce.domain.AnamnesisForm;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.CascadeType;
import javax.persistence.TypedQuery;

@RooJavaBean
@RooToString
@RooEntity
public class Scar {

    @Size(max = 60)
    private String bodypart;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<AnamnesisForm> anamnesisForms = new HashSet<AnamnesisForm>();
    
    public static Long countScarsByName(String name) {
    	EntityManager em = entityManager();
    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM Scar o WHERE o.bodypart LIKE :name", Long.class);
    	q.setParameter("name", "%" + name + "%");
    	
    	return q.getSingleResult();
    }
    
    public static List<Scar> findScarEntriesByName(String name, int firstResult, int maxResults) {
        if (name == null) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = entityManager();
        TypedQuery<Scar> q = em.createQuery("SELECT o FROM Scar AS o WHERE o.bodypart LIKE :name", Scar.class);
        q.setParameter("name", "%" + name + "%");
        q.setFirstResult(firstResult);
        q.setMaxResults(maxResults);
        
        return q.getResultList();
    }
}
