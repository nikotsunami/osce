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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Table(name="profession")
public class SpProfession {

	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
	
    @Size(max = 60)
    private String profession;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profession")
    private Set<SpStandardizedPatient> standardizedpatients = new HashSet<SpStandardizedPatient>();

    private static Logger log = Logger.getLogger(SpProfession.class);
    
	public static SpProfession findProfessionBasedOnProfessionText(String profession) {
		log.info("fininding profession of text : " + profession);
		String sql="";
		try{
			
			EntityManager em = SpProfession.entityManager();
			if(StringUtils.isNotBlank(profession)){
				sql="SELECT p FROM SpProfession as p where p.profession='"+profession + "'";
			}else{
				sql="SELECT p FROM SpProfession as p where p.profession=''";
			}
			
			TypedQuery<SpProfession> query = em.createQuery(sql,SpProfession.class);
			
			List<SpProfession> listOfProfession = query.getResultList();
			
			if(listOfProfession.size()==1){
				return listOfProfession.get(0);
			}else{
				return null;
			}
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
}
    

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Profession: ").append(getProfession()).append(", ");
        sb.append("Standardizedpatients: ").append(getStandardizedpatients() == null ? "null" : getStandardizedpatients().size()).append(", ");
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
            SpProfession attached = SpProfession.findSpProfession(this.id);
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
    public SpProfession merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SpProfession merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new SpProfession().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSpProfessions() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SpProfession o", Long.class).getSingleResult();
    }

	public static List<SpProfession> findAllSpProfessions() {
        return entityManager().createQuery("SELECT o FROM SpProfession o", SpProfession.class).getResultList();
    }

	public static SpProfession findSpProfession(Long id) {
        if (id == null) return null;
        return entityManager().find(SpProfession.class, id);
    }

	public static List<SpProfession> findSpProfessionEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SpProfession o", SpProfession.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String getProfession() {
        return this.profession;
    }

	public void setProfession(String profession) {
        this.profession = profession;
    }

	public Set<SpStandardizedPatient> getStandardizedpatients() {
        return this.standardizedpatients;
    }

	public void setStandardizedpatients(Set<SpStandardizedPatient> standardizedpatients) {
        this.standardizedpatients = standardizedpatients;
    }
}
