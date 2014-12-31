package ch.unibas.medizin.osce.domain.spportal;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Version;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
@Table(name="elimination_criterion")
public class SpEliminationCriterion {

	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
	
    private Boolean anamnesisCheckValue;

    @ManyToOne
    private SpScar scar;

    @ManyToOne
    private SpAnamnesisCheck anamnesisCheck;

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
            SpEliminationCriterion attached = SpEliminationCriterion.findSpEliminationCriterion(this.id);
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
    public SpEliminationCriterion merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SpEliminationCriterion merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new SpEliminationCriterion().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSpEliminationCriterions() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SpEliminationCriterion o", Long.class).getSingleResult();
    }

	public static List<SpEliminationCriterion> findAllSpEliminationCriterions() {
        return entityManager().createQuery("SELECT o FROM SpEliminationCriterion o", SpEliminationCriterion.class).getResultList();
    }

	public static SpEliminationCriterion findSpEliminationCriterion(Long id) {
        if (id == null) return null;
        return entityManager().find(SpEliminationCriterion.class, id);
    }

	public static List<SpEliminationCriterion> findSpEliminationCriterionEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SpEliminationCriterion o", SpEliminationCriterion.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public Boolean getAnamnesisCheckValue() {
        return this.anamnesisCheckValue;
    }

	public void setAnamnesisCheckValue(Boolean anamnesisCheckValue) {
        this.anamnesisCheckValue = anamnesisCheckValue;
    }

	public SpScar getScar() {
        return this.scar;
    }

	public void setScar(SpScar scar) {
        this.scar = scar;
    }

	public SpAnamnesisCheck getAnamnesisCheck() {
        return this.anamnesisCheck;
    }

	public void setAnamnesisCheck(SpAnamnesisCheck anamnesisCheck) {
        this.anamnesisCheck = anamnesisCheck;
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AnamnesisCheck: ").append(getAnamnesisCheck()).append(", ");
        sb.append("AnamnesisCheckValue: ").append(getAnamnesisCheckValue()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Scar: ").append(getScar()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }
}
