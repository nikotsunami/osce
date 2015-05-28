package ch.unibas.medizin.osce.domain;

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
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Size;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import ch.unibas.medizin.osce.domain.spportal.SpNationality;

@Entity
@Configurable
public class Nationality {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
    @Size(max = 40)
    private String nationality;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nationality")
    private Set<StandardizedPatient> standardizedpatients = new HashSet<StandardizedPatient>();
    
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "country")
    private Set<StandardizedPatient> standardizedpatientsOfCountry = new HashSet<StandardizedPatient>();
    
    private static Logger log = Logger.getLogger(Nationality.class);
    
    public static Long countNationalitiesByName(String name) {
    	EntityManager em = entityManager();
    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM Nationality o WHERE o.nationality LIKE :name", Long.class);
    	q.setParameter("name", "%" + name + "%");
    	
    	return q.getSingleResult();
    }
    
    public static List<Nationality> findNationalitiesByName(String name, int firstResult, int maxResults) {
        if (name == null) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = entityManager();
        TypedQuery<Nationality> q = em.createQuery("SELECT o FROM Nationality AS o WHERE o.nationality LIKE :name", Nationality.class);
        q.setParameter("name", "%" + name + "%");
        q.setFirstResult(firstResult);
        q.setMaxResults(maxResults);
        
        return q.getResultList();
    }
    
    public static Integer checkNationnality(String name)
    {
    	EntityManager em = entityManager();
    	String sql = "SELECT n FROM Nationality n WHERE n.nationality = '" + name + "'";
    	TypedQuery<Nationality> q = em.createQuery(sql, Nationality.class);
    	return q.getResultList().size();
    }
    
    public static Boolean saveNationalityInSpPortal(Nationality nation){
    	try{
	    		
    		SpNationality spNationality = new SpNationality();
    		
    		spNationality.setNationality(nation.getNationality());
    		
    		spNationality.persist();
	    		
    		return true;
    	}catch (Exception e) {
			
    		log.error(e.getMessage(), e);
			
    		return false;
		}
    }
    public static Boolean editNationalityInSpPortal(Nationality nation,String value){
    	try{
    		SpNationality spNationality = SpNationality.findNationalityOnNationalityText(nation.getNationality());
    		if(spNationality!=null){
    		
    			spNationality.setNationality(value);
    			
    			spNationality.persist();
    			
    			return true;
    		}else{
    			return false;
    		}
    	}catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
    }
    
    public static Boolean deleteNatinalityInSpPortal(Nationality nation){
    
    	try{
	    	SpNationality spNationality = SpNationality.findNationalityOnNationalityText(nation.getNationality());
			
	    	if(spNationality!=null){
			
				spNationality.remove();
				
				return true;
			}else{
				return false;
			}
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
    }

	public static Nationality findNationalityByName(String nationality) {
		
		log.info("finding Nationality of text :" + nationality);
		
		try{
		
			EntityManager em = Nationality.entityManager();
			
			String sql="SELECT n FROM Nationality as n where n.nationality='"+nationality + "'";

			TypedQuery<Nationality> query = em.createQuery(sql,Nationality.class);
			
			List<Nationality> listOfNationalities = query.getResultList();
			
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
            Nationality attached = Nationality.findNationality(this.id);
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
    public Nationality merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Nationality merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new Nationality().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countNationalitys() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Nationality o", Long.class).getSingleResult();
    }

	public static List<Nationality> findAllNationalitys() {
        return entityManager().createQuery("SELECT o FROM Nationality o", Nationality.class).getResultList();
    }

	public static Nationality findNationality(Long id) {
        if (id == null) return null;
        return entityManager().find(Nationality.class, id);
    }

	public static List<Nationality> findNationalityEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Nationality o", Nationality.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String getNationality() {
        return this.nationality;
    }

	public void setNationality(String nationality) {
        this.nationality = nationality;
    }

	public Set<StandardizedPatient> getStandardizedpatients() {
        return this.standardizedpatients;
    }

	public void setStandardizedpatients(Set<StandardizedPatient> standardizedpatients) {
        this.standardizedpatients = standardizedpatients;
    }
	

	public Set<StandardizedPatient> getStandardizedpatientsOfCountry() {
		return standardizedpatientsOfCountry;
	}

	public void setStandardizedpatientsOfCountry(
			Set<StandardizedPatient> standardizedpatientsOfCountry) {
		this.standardizedpatientsOfCountry = standardizedpatientsOfCountry;
	}

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Nationality: ").append(getNationality()).append(", ");
        sb.append("Standardizedpatients: ").append(getStandardizedpatients() == null ? "null" : getStandardizedpatients().size()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }
}
