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
import javax.persistence.Version;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class EliminationCriterion {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
    private Boolean anamnesisCheckValue;

    @ManyToOne
    private StandardizedRole standardizedRole;

    @ManyToOne
    private Scar scar;

    @ManyToOne
    private AnamnesisCheck anamnesisCheck;

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
            EliminationCriterion attached = EliminationCriterion.findEliminationCriterion(this.id);
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
    public EliminationCriterion merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        EliminationCriterion merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new EliminationCriterion().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countEliminationCriterions() {
        return entityManager().createQuery("SELECT COUNT(o) FROM EliminationCriterion o", Long.class).getSingleResult();
    }

	public static List<EliminationCriterion> findAllEliminationCriterions() {
        return entityManager().createQuery("SELECT o FROM EliminationCriterion o", EliminationCriterion.class).getResultList();
    }

	public static EliminationCriterion findEliminationCriterion(Long id) {
        if (id == null) return null;
        return entityManager().find(EliminationCriterion.class, id);
    }

	public static List<EliminationCriterion> findEliminationCriterionEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM EliminationCriterion o", EliminationCriterion.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public Boolean getAnamnesisCheckValue() {
        return this.anamnesisCheckValue;
    }

	public void setAnamnesisCheckValue(Boolean anamnesisCheckValue) {
        this.anamnesisCheckValue = anamnesisCheckValue;
    }

	public StandardizedRole getStandardizedRole() {
        return this.standardizedRole;
    }

	public void setStandardizedRole(StandardizedRole standardizedRole) {
        this.standardizedRole = standardizedRole;
    }

	public Scar getScar() {
        return this.scar;
    }

	public void setScar(Scar scar) {
        this.scar = scar;
    }

	public AnamnesisCheck getAnamnesisCheck() {
        return this.anamnesisCheck;
    }

	public void setAnamnesisCheck(AnamnesisCheck anamnesisCheck) {
        this.anamnesisCheck = anamnesisCheck;
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AnamnesisCheck: ").append(getAnamnesisCheck()).append(", ");
        sb.append("AnamnesisCheckValue: ").append(getAnamnesisCheckValue()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Scar: ").append(getScar()).append(", ");
        sb.append("StandardizedRole: ").append(getStandardizedRole()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }
}
