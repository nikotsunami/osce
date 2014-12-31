package ch.unibas.medizin.osce.domain.spportal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;




@Configurable
@Entity
@Table(name="anamnesis_check")
public class SpAnamnesisCheck {

	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
	
	 @Id
	 @Column(name = "id")
	 private Long id;
	 
    @Size(max = 999)
    private String text;

    @Size(max = 255)
    private String value;

    private Integer sort_order;

    @Enumerated
    private AnamnesisCheckTypes type;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "anamnesischeck")
    private Set<SpAnamnesisChecksValue> anamnesischecksvalues = new HashSet<SpAnamnesisChecksValue>();

    @ManyToOne
    private ch.unibas.medizin.osce.domain.spportal.SpAnamnesisCheck title;
    
    @ManyToOne
    private SpAnamnesisCheckTitle anamnesisCheckTitle;

    @NotNull
	@Value("false")
	@Column(columnDefinition="BIT", length = 1)
    private Boolean sendToDMZ;    
    

	@Version
    @Column(name = "version")
    private Integer version;

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
            SpAnamnesisCheck attached = SpAnamnesisCheck.findSpAnamnesisCheck(this.id);
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
    public SpAnamnesisCheck merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SpAnamnesisCheck merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new SpAnamnesisCheck().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSpAnamnesisChecks() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SpAnamnesisCheck o", Long.class).getSingleResult();
    }

	public static List<SpAnamnesisCheck> findAllSpAnamnesisChecks() {
        return entityManager().createQuery("SELECT o FROM SpAnamnesisCheck o", SpAnamnesisCheck.class).getResultList();
    }

	public static SpAnamnesisCheck findSpAnamnesisCheck(Long id) {
        if (id == null) return null;
        return entityManager().find(SpAnamnesisCheck.class, id);
    }

	public static List<SpAnamnesisCheck> findSpAnamnesisCheckEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SpAnamnesisCheck o", SpAnamnesisCheck.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public String getText() {
        return this.text;
    }

	public void setText(String text) {
        this.text = text;
    }

	public String getValue() {
        return this.value;
    }

	public void setValue(String value) {
        this.value = value;
    }

	public Integer getSort_order() {
        return this.sort_order;
    }

	public void setSort_order(Integer sort_order) {
        this.sort_order = sort_order;
    }

	public AnamnesisCheckTypes getType() {
        return this.type;
    }

	public void setType(AnamnesisCheckTypes type) {
        this.type = type;
    }

	public Set<SpAnamnesisChecksValue> getAnamnesischecksvalues() {
        return this.anamnesischecksvalues;
    }

	public void setAnamnesischecksvalues(Set<SpAnamnesisChecksValue> anamnesischecksvalues) {
        this.anamnesischecksvalues = anamnesischecksvalues;
    }

	public SpAnamnesisCheck getTitle() {
        return this.title;
    }

	public void setTitle(SpAnamnesisCheck title) {
        this.title = title;
    }

	public SpAnamnesisCheckTitle getAnamnesisCheckTitle() {
        return this.anamnesisCheckTitle;
    }

	public void setAnamnesisCheckTitle(SpAnamnesisCheckTitle anamnesisCheckTitle) {
        this.anamnesisCheckTitle = anamnesisCheckTitle;
    }

	public Boolean getSendToDMZ() {
        return this.sendToDMZ;
    }

	public void setSendToDMZ(Boolean sendToDMZ) {
        this.sendToDMZ = sendToDMZ;
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AnamnesisCheckTitle: ").append(getAnamnesisCheckTitle()).append(", ");
        sb.append("Anamnesischecksvalues: ").append(getAnamnesischecksvalues() == null ? "null" : getAnamnesischecksvalues().size()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("SendToDMZ: ").append(getSendToDMZ()).append(", ");
        sb.append("Sort_order: ").append(getSort_order()).append(", ");
        sb.append("Text: ").append(getText()).append(", ");
        sb.append("Type: ").append(getType()).append(", ");
        sb.append("Value: ").append(getValue()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }
}


