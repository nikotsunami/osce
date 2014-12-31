package ch.unibas.medizin.osce.domain.spportal;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Table(name="anamnesis_form")
public class SpAnamnesisForm {

	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
	
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date createDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "anamnesisform")
    private Set<SpAnamnesisChecksValue> anamnesischecksvalues = new HashSet<SpAnamnesisChecksValue>();

    /*@ManyToMany(cascade = CascadeType.ALL,mappedBy="anamnesisForms")
    private Set<SpScar> scars = new HashSet<SpScar>();*/
    
    @ManyToMany
    @JoinTable(name="anamnesis_form_scars")
    private Set<SpScar> scars = new HashSet<SpScar>();
    
   

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Anamnesischecksvalues: ").append(getAnamnesischecksvalues() == null ? "null" : getAnamnesischecksvalues().size()).append(", ");
        sb.append("CreateDate: ").append(getCreateDate()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Scars: ").append(getScars() == null ? "null" : getScars().size()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public Date getCreateDate() {
        return this.createDate;
    }

	public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

	public Set<SpAnamnesisChecksValue> getAnamnesischecksvalues() {
        return this.anamnesischecksvalues;
    }

	public void setAnamnesischecksvalues(Set<SpAnamnesisChecksValue> anamnesischecksvalues) {
        this.anamnesischecksvalues = anamnesischecksvalues;
    }

	public Set<SpScar> getScars() {
        return this.scars;
    }

	public void setScars(Set<SpScar> scars) {
        this.scars = scars;
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
            SpAnamnesisForm attached = SpAnamnesisForm.findSpAnamnesisForm(this.id);
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
    public SpAnamnesisForm merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SpAnamnesisForm merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new SpAnamnesisForm().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSpAnamnesisForms() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SpAnamnesisForm o", Long.class).getSingleResult();
    }

	public static List<SpAnamnesisForm> findAllSpAnamnesisForms() {
        return entityManager().createQuery("SELECT o FROM SpAnamnesisForm o", SpAnamnesisForm.class).getResultList();
    }

	public static SpAnamnesisForm findSpAnamnesisForm(Long id) {
        if (id == null) return null;
        return entityManager().find(SpAnamnesisForm.class, id);
    }

	public static List<SpAnamnesisForm> findSpAnamnesisFormEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SpAnamnesisForm o", SpAnamnesisForm.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
