package ch.unibas.medizin.osce.domain.spportal;

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
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Size;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
@Table(name="nationality")
public class SpNationality {
	
	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
	
    @Size(max = 40)
    private String nationality;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nationality")
    private Set<SpStandardizedPatient> standardizedpatients = new HashSet<SpStandardizedPatient>();

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "country")
    private Set<SpStandardizedPatient> standardizedpatientsOfCountry = new HashSet<SpStandardizedPatient>();
    
    private static Logger log = Logger.getLogger(SpNationality.class);
    
	public static SpNationality findNationalityOnNationalityText(String nationality) {
		
		log.info("finding Nationality of text :" + nationality);
		
		try{
		
			EntityManager em = SpNationality.entityManager();
			
			String sql="SELECT n FROM SpNationality as n where n.nationality='"+nationality + "'";

			TypedQuery<SpNationality> query = em.createQuery(sql,SpNationality.class);
			
			List<SpNationality> listOfNationalities = query.getResultList();
			
			if(listOfNationalities.size()==1){
				return listOfNationalities.get(0);
			}else{
				return null;
			}
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
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
            SpNationality attached = SpNationality.findSpNationality(this.id);
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
    public SpNationality merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SpNationality merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new SpNationality().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSpNationalitys() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SpNationality o", Long.class).getSingleResult();
    }

	public static List<SpNationality> findAllSpNationalitys() {
        return entityManager().createQuery("SELECT o FROM SpNationality o", SpNationality.class).getResultList();
    }

	public static SpNationality findSpNationality(Long id) {
        if (id == null) return null;
        return entityManager().find(SpNationality.class, id);
    }

	public static List<SpNationality> findSpNationalityEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SpNationality o", SpNationality.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Nationality: ").append(getNationality()).append(", ");
        sb.append("Standardizedpatients: ").append(getStandardizedpatients() == null ? "null" : getStandardizedpatients().size()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public String getNationality() {
        return this.nationality;
    }

	public void setNationality(String nationality) {
        this.nationality = nationality;
    }

	public Set<SpStandardizedPatient> getStandardizedpatients() {
        return this.standardizedpatients;
    }

	public void setStandardizedpatients(Set<SpStandardizedPatient> standardizedpatients) {
        this.standardizedpatients = standardizedpatients;
    }

	
	public Set<SpStandardizedPatient> getStandardizedpatientsOfCountry() {
		return standardizedpatientsOfCountry;
	}

	public void setStandardizedpatientsOfCountry(Set<SpStandardizedPatient> standardizedpatientsOfCountry) {
		this.standardizedpatientsOfCountry = standardizedpatientsOfCountry;
	}
	
}
