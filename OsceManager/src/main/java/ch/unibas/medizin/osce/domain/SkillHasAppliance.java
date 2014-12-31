package ch.unibas.medizin.osce.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class SkillHasAppliance {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	
	@ManyToOne
	private Skill skill;
	
	@ManyToOne
	private Appliance appliance;
	
	public static List<SkillHasAppliance> findSkillHasApplianceBySkillAndAppliance(Skill val, Appliance appl)
	{
		EntityManager em = entityManager();
    	TypedQuery<SkillHasAppliance> q = em.createQuery("SELECT o FROM SkillHasAppliance o WHERE o.skill LIKE :val AND o.appliance LIKE :appl", SkillHasAppliance.class);
     	q.setParameter("val", val);
     	q.setParameter("appl", appl);
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
            SkillHasAppliance attached = SkillHasAppliance.findSkillHasAppliance(this.id);
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
    public SkillHasAppliance merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SkillHasAppliance merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new SkillHasAppliance().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSkillHasAppliances() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SkillHasAppliance o", Long.class).getSingleResult();
    }

	public static List<SkillHasAppliance> findAllSkillHasAppliances() {
        return entityManager().createQuery("SELECT o FROM SkillHasAppliance o", SkillHasAppliance.class).getResultList();
    }

	public static SkillHasAppliance findSkillHasAppliance(Long id) {
        if (id == null) return null;
        return entityManager().find(SkillHasAppliance.class, id);
    }

	public static List<SkillHasAppliance> findSkillHasApplianceEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SkillHasAppliance o", SkillHasAppliance.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Appliance: ").append(getAppliance()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Skill: ").append(getSkill()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public Skill getSkill() {
        return this.skill;
    }

	public void setSkill(Skill skill) {
        this.skill = skill;
    }

	public Appliance getAppliance() {
        return this.appliance;
    }

	public void setAppliance(Appliance appliance) {
        this.appliance = appliance;
    }
}
