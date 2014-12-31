package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import ch.unibas.medizin.osce.shared.TraitTypes;

@Configurable
@Entity
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

	public String getBodypart() {
        return this.bodypart;
    }

	public void setBodypart(String bodypart) {
        this.bodypart = bodypart;
    }

	public TraitTypes getTraitType() {
        return this.traitType;
    }

	public void setTraitType(TraitTypes traitType) {
        this.traitType = traitType;
    }

	public Set<AnamnesisForm> getAnamnesisForms() {
        return this.anamnesisForms;
    }

	public void setAnamnesisForms(Set<AnamnesisForm> anamnesisForms) {
        this.anamnesisForms = anamnesisForms;
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AnamnesisForms: ").append(getAnamnesisForms() == null ? "null" : getAnamnesisForms().size()).append(", ");
        sb.append("Bodypart: ").append(getBodypart()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("TraitType: ").append(getTraitType()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Scar attached = Scar.findScar(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public Scar merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Scar merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new Scar().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countScars() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Scar o", Long.class).getSingleResult();
    }

	public static List<Scar> findAllScars() {
        return entityManager().createQuery("SELECT o FROM Scar o", Scar.class).getResultList();
    }

	public static Scar findScar(Long id) {
        if (id == null) return null;
        return entityManager().find(Scar.class, id);
    }

	public static List<Scar> findScarEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Scar o", Scar.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
