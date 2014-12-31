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
import ch.unibas.medizin.osce.domain.spportal.SpProfession;

@Configurable
@Entity
public class Profession {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
    @Size(max = 60)
    private String profession;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profession")
    private Set<StandardizedPatient> standardizedpatients = new HashSet<StandardizedPatient>();
    
    private static Logger log = Logger.getLogger(Profession.class);
    
    public static Long countProfessionsByName(String name) {
    	EntityManager em = entityManager();
    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM Profession o WHERE o.profession LIKE :name", Long.class);
    	q.setParameter("name", "%" + name + "%");
    	
    	return q.getSingleResult();
    }
    
    public static List<Profession> findProfessionsByName(String name, int firstResult, int maxResults) {
        if (name == null) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = entityManager();
        TypedQuery<Profession> q = em.createQuery("SELECT o FROM Profession AS o WHERE o.profession LIKE :name", Profession.class);
        q.setParameter("name", "%" + name + "%");
        q.setFirstResult(firstResult);
        q.setMaxResults(maxResults);
        
        return q.getResultList();
    }
    
    public static Boolean saveNewProfessionInSpPortal(Profession profession){
    	try{
	    	SpProfession spProfession = new SpProfession();
	    	
	    	spProfession.setProfession(profession.getProfession());
	    	
	    	spProfession.persist();
	    	
	    	return true;
    	}catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
    }
    
    public static Boolean deleteProfessionInSpportal(Profession	 prof){
    	try{
    		SpProfession spProfession = SpProfession.findProfessionBasedOnProfessionText(prof.getProfession());
    		if(spProfession!=null){
    		
    			spProfession.remove();
    			
    			return true;
    		}else{
    			return false;
    		}
    	}catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
    }
    
    public static Boolean editProfessionInSpPortal(Profession prof, String value){
    	
    	try{
    		SpProfession spProfession = SpProfession.findProfessionBasedOnProfessionText(prof.getProfession());
    		if(spProfession!=null){
    			
    			spProfession.setProfession(value);
    			
    			spProfession.persist();
    			
    			return true;
    		}else{
    			return false;
    		}
    	}catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
    }
    
    public static Profession findProfessionByProfessionText(String profession){
    	
    	log.info("fininding profession of text : " + profession);
		
    	try{
			
			EntityManager em = Profession.entityManager();
			
			String sql="SELECT p FROM Profession as p where p.profession='"+profession + "'";
			
			TypedQuery<Profession> query = em.createQuery(sql,Profession.class);
			
			List<Profession> listOfProfession = query.getResultList();
			
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
            Profession attached = Profession.findProfession(this.id);
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
    public Profession merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Profession merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new Profession().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countProfessions() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Profession o", Long.class).getSingleResult();
    }

	public static List<Profession> findAllProfessions() {
        return entityManager().createQuery("SELECT o FROM Profession o", Profession.class).getResultList();
    }

	public static Profession findProfession(Long id) {
        if (id == null) return null;
        return entityManager().find(Profession.class, id);
    }

	public static List<Profession> findProfessionEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Profession o", Profession.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String getProfession() {
        return this.profession;
    }

	public void setProfession(String profession) {
        this.profession = profession;
    }

	public Set<StandardizedPatient> getStandardizedpatients() {
        return this.standardizedpatients;
    }

	public void setStandardizedpatients(Set<StandardizedPatient> standardizedpatients) {
        this.standardizedpatients = standardizedpatients;
    }
}
