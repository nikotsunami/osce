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
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.Table;

@Configurable
@Entity
@Table(name="anamnesis_checks_value")
public class SpAnamnesisChecksValue{

	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
	
    private Boolean truth;

    @Size(max = 255)
    private String comment;

    @Size(max = 255)
    private String anamnesisChecksValue;

    @ManyToOne
    private SpAnamnesisForm anamnesisform;

    @ManyToOne
    private SpAnamnesisCheck anamnesischeck;
    

	public Boolean getTruth() {
        return this.truth;
    }

	public void setTruth(Boolean truth) {
        this.truth = truth;
    }

	public String getComment() {
        return this.comment;
    }

	public void setComment(String comment) {
        this.comment = comment;
    }

	public String getAnamnesisChecksValue() {
        return this.anamnesisChecksValue;
    }

	public void setAnamnesisChecksValue(String anamnesisChecksValue) {
        this.anamnesisChecksValue = anamnesisChecksValue;
    }

	public SpAnamnesisForm getAnamnesisform() {
        return this.anamnesisform;
    }

	public void setAnamnesisform(SpAnamnesisForm anamnesisform) {
        this.anamnesisform = anamnesisform;
    }

	public SpAnamnesisCheck getAnamnesischeck() {
        return this.anamnesischeck;
    }

	public void setAnamnesischeck(SpAnamnesisCheck anamnesischeck) {
        this.anamnesischeck = anamnesischeck;
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AnamnesisChecksValue: ").append(getAnamnesisChecksValue()).append(", ");
        sb.append("Anamnesischeck: ").append(getAnamnesischeck()).append(", ");
        sb.append("Anamnesisform: ").append(getAnamnesisform()).append(", ");
        sb.append("Comment: ").append(getComment()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Truth: ").append(getTruth()).append(", ");
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
            SpAnamnesisChecksValue attached = SpAnamnesisChecksValue.findSpAnamnesisChecksValue(this.id);
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
    public SpAnamnesisChecksValue merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SpAnamnesisChecksValue merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new SpAnamnesisChecksValue().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSpAnamnesisChecksValues() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SpAnamnesisChecksValue o", Long.class).getSingleResult();
    }

	public static List<SpAnamnesisChecksValue> findAllSpAnamnesisChecksValues() {
        return entityManager().createQuery("SELECT o FROM SpAnamnesisChecksValue o", SpAnamnesisChecksValue.class).getResultList();
    }

	public static SpAnamnesisChecksValue findSpAnamnesisChecksValue(Long id) {
        if (id == null) return null;
        return entityManager().find(SpAnamnesisChecksValue.class, id);
    }

	public static List<SpAnamnesisChecksValue> findSpAnamnesisChecksValueEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SpAnamnesisChecksValue o", SpAnamnesisChecksValue.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
