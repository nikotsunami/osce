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

@Entity
@Configurable
public class Appliance {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	@Size(max = 3)
	private String shortcut;
	
	public static List<Appliance> getAppliacneByShortcut(String val)
	{
		EntityManager em = entityManager();
    	TypedQuery<Appliance> q = em.createQuery("SELECT o FROM Appliance o WHERE o.shortcut LIKE :val", Appliance.class);
     	q.setParameter("val", "%" + val + "%");
     	return q.getResultList();
	}

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Shortcut: ").append(getShortcut()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public String getShortcut() {
        return this.shortcut;
    }

	public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
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
            Appliance attached = Appliance.findAppliance(this.id);
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
    public Appliance merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Appliance merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new Appliance().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countAppliances() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Appliance o", Long.class).getSingleResult();
    }

	public static List<Appliance> findAllAppliances() {
        return entityManager().createQuery("SELECT o FROM Appliance o", Appliance.class).getResultList();
    }

	public static Appliance findAppliance(Long id) {
        if (id == null) return null;
        return entityManager().find(Appliance.class, id);
    }

	public static List<Appliance> findApplianceEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Appliance o", Appliance.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
