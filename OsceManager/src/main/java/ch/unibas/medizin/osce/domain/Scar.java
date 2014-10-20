package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.shared.TraitTypes;

@RooJavaBean
@RooToString
@RooEntity
public class Scar {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
    @Size(max = 60)
    private String bodypart;
    
    @Enumerated
    @NotNull
    private TraitTypes traitType;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "scars")
    private Set<AnamnesisForm> anamnesisForms = new HashSet<AnamnesisForm>();
    
    public static Long countScarsByAnamnesisForm(Long id) {
    	EntityManager em = entityManager();
    	AnamnesisForm anamnesisForm = AnamnesisForm.findAnamnesisForm(id);
    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM Scar o WHERE :anamnesisForm MEMBER OF o.anamnesisForms", Long.class);
    	q.setParameter("anamnesisForm", anamnesisForm);
    	
    	return q.getSingleResult();
    }
    
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
    
    public static List<Scar> findScarEntriesByAnamnesisForm(Long id, int firstResult, int maxResults) {
        if (id == null) throw new IllegalArgumentException("The id argument is required");
        EntityManager em = entityManager();
        AnamnesisForm anamnesisForm = AnamnesisForm.findAnamnesisForm(id);
        TypedQuery<Scar> q = em.createQuery("SELECT o FROM Scar AS o WHERE :anamnesisForm MEMBER OF o.anamnesisForms", Scar.class);
        q.setParameter("anamnesisForm", anamnesisForm);
        q.setFirstResult(firstResult);
        q.setMaxResults(maxResults);
        
        return q.getResultList();
    }
    
    public static List<Scar> findScarEntriesByNotAnamnesisForm(Long id) {
        if (id == null) throw new IllegalArgumentException("The id argument is required");
        EntityManager em = entityManager();
        AnamnesisForm anamnesisForm = AnamnesisForm.findAnamnesisForm(id);
        TypedQuery<Scar> q = em.createQuery("SELECT o FROM Scar AS o WHERE :anamnesisForm NOT MEMBER OF o.anamnesisForms", Scar.class);
        q.setParameter("anamnesisForm", anamnesisForm);
        
        return q.getResultList();
    }
}
