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
import javax.persistence.ManyToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class Keyword {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	private static Logger Log = Logger.getLogger(Keyword.class);
	
    @NotNull
    @Size(min = 2, max = 255)
    private String name;
    
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "keywords")
    private Set<StandardizedRole> standardizedRoles = new HashSet<StandardizedRole>();
    
 // SPEC START =
    
    public static long findKeywordByStandRoleCount(StandardizedRole standRole)
	{
		EntityManager em2 = entityManager();
		Log.info("~QUERY findKeywordByStandRole()");
		Log.info("~QUERY BEFOREE EXECUTION  Stand Role ID  : " + standRole.getId());
		String queryString="SELECT o from Keyword as o join o.standardizedRoles sr where sr.id ="+standRole.getId();
		Log.info("~QUERY STRING : " +  queryString); 
		TypedQuery<Keyword> q = em2.createQuery(queryString, Keyword.class);
		java.util.List<Keyword> result = q.getResultList();
		Log.info("~QUERY Result : " + result);
		return result.size();
		//String queryString="SELECT o from Keyword as o WHERE o.standardizedRoles.id="+standRole.getId();
		//String queryString="SELECT o FROM Keyword AS o WHERE o.standardizedRoles.id = :standRole";
		//String queryString="select name from keyword,standardized_role_keywords,standardized_role where keyword.id=standardized_role_keywords.keywords and standardized_role_keywords.standardized_roles=standardized_role.id and standardized_role.id="+standRole;
		//q.setParameter("standRole", standRole);
	}
    
    public static java.util.List<Keyword> findKeywordByStandRole(StandardizedRole standRole,int startrange,int length)
   	{
   		EntityManager em2 = entityManager();
   		Log.info("~QUERY findKeywordByStandRole()");
   		Log.info("~QUERY BEFOREE EXECUTION  Stand Role ID  : " + standRole.getId());
   		String queryString="SELECT o from Keyword as o join o.standardizedRoles sr where sr.id ="+standRole.getId();
   		Log.info("~QUERY STRING : " +  queryString); 
   		TypedQuery<Keyword> q = em2.createQuery(queryString, Keyword.class);
   		q.setFirstResult(startrange);
    	q.setMaxResults(length);
   		java.util.List<Keyword> result = q.getResultList();
   		Log.info("~QUERY Result : " + result);
		return result;
		//String queryString="SELECT o from Keyword as o WHERE o.standardizedRoles.id="+standRole.getId();
		//String queryString="SELECT o FROM Keyword AS o WHERE o.standardizedRoles.id = :standRole";
		//String queryString="select name from keyword,standardized_role_keywords,standardized_role where keyword.id=standardized_role_keywords.keywords and standardized_role_keywords.standardized_roles=standardized_role.id and standardized_role.id="+standRole;
		//q.setParameter("standRole", standRole);
	}
    
    public static List<Keyword> findKeywordByStandardizedRoleID(Long standardizedRoleID, int firstResult, int maxResults) 
	{
		if (standardizedRoleID == 0)
			throw new IllegalArgumentException("The name argument is required");
		EntityManager em = entityManager();
		TypedQuery<Keyword> q = em.createQuery("SELECT o from Keyword as o join o.standardizedRoles sr where sr.id ="+standardizedRoleID,Keyword.class);
		q.setParameter("standardizedRoleID", standardizedRoleID);
		Log.info("^standardizedRoleID: " + standardizedRoleID);
		Log.info("^ Size  : " + q.getMaxResults());
		
		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);
		
		return q.getResultList();
	}

    // SPEC END =

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public Set<StandardizedRole> getStandardizedRoles() {
        return this.standardizedRoles;
    }

	public void setStandardizedRoles(Set<StandardizedRole> standardizedRoles) {
        this.standardizedRoles = standardizedRoles;
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
            Keyword attached = Keyword.findKeyword(this.id);
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
    public Keyword merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Keyword merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new Keyword().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countKeywords() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Keyword o", Long.class).getSingleResult();
    }

	public static List<Keyword> findAllKeywords() {
        return entityManager().createQuery("SELECT o FROM Keyword o", Keyword.class).getResultList();
    }

	public static Keyword findKeyword(Long id) {
        if (id == null) return null;
        return entityManager().find(Keyword.class, id);
    }

	public static List<Keyword> findKeywordEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Keyword o", Keyword.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("StandardizedRoles: ").append(getStandardizedRoles() == null ? "null" : getStandardizedRoles().size()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }
}
