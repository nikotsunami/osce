package ch.unibas.medizin.osce.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class MainClassification {
	
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	@Size(max = 2)
	private String shortcut;
	
	private String description;
	
	public static List<MainClassification> findMainClassificationByShortCut(String val)
	{
		EntityManager em = entityManager();
    	TypedQuery<MainClassification> q = em.createQuery("SELECT o FROM MainClassification o WHERE o.shortcut LIKE :val", MainClassification.class);
     	q.setParameter("val", "%" + val + "%");
     	return q.getResultList();
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
            MainClassification attached = MainClassification.findMainClassification(this.id);
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
    public MainClassification merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        MainClassification merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new MainClassification().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countMainClassifications() {
        return entityManager().createQuery("SELECT COUNT(o) FROM MainClassification o", Long.class).getSingleResult();
    }

	public static List<MainClassification> findAllMainClassifications() {
        return entityManager().createQuery("SELECT o FROM MainClassification o", MainClassification.class).getResultList();
    }

	public static MainClassification findMainClassification(Long id) {
        if (id == null) return null;
        return entityManager().find(MainClassification.class, id);
    }

	public static List<MainClassification> findMainClassificationEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM MainClassification o", MainClassification.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String getShortcut() {
        return this.shortcut;
    }

	public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

	public String getDescription() {
        return this.description;
    }

	public void setDescription(String description) {
        this.description = description;
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Description: ").append(getDescription()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Shortcut: ").append(getShortcut()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }
}
