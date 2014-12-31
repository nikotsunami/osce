package ch.unibas.medizin.osce.domain.spportal;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import ch.unibas.medizin.osce.shared.TraitTypes;



@Configurable
@Entity
@Table(name="scar")
public class SpScar {

	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
	
    @Size(max = 60)
    private String bodypart;
    
    @Enumerated
    @NotNull
    private TraitTypes traitType;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "scars")
    private Set<SpAnamnesisForm> anamnesisForms = new HashSet<SpAnamnesisForm>();
    
   

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

	public Set<SpAnamnesisForm> getAnamnesisForms() {
        return this.anamnesisForms;
    }

	public void setAnamnesisForms(Set<SpAnamnesisForm> anamnesisForms) {
        this.anamnesisForms = anamnesisForms;
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
            SpScar attached = SpScar.findSpScar(this.id);
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
    public SpScar merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SpScar merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new SpScar().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSpScars() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SpScar o", Long.class).getSingleResult();
    }

	public static List<SpScar> findAllSpScars() {
        return entityManager().createQuery("SELECT o FROM SpScar o", SpScar.class).getResultList();
    }

	public static SpScar findSpScar(Long id) {
        if (id == null) return null;
        return entityManager().find(SpScar.class, id);
    }

	public static List<SpScar> findSpScarEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SpScar o", SpScar.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
}
