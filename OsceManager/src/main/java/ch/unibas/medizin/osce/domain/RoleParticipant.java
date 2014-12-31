package ch.unibas.medizin.osce.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import ch.unibas.medizin.osce.shared.RoleParticipantTypes;

@Configurable
@Entity
public class RoleParticipant {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	private static Logger Log = Logger.getLogger(RoleParticipant.class);
	
    @ManyToOne
    @NotNull
    private StandardizedRole standardizedRole;

    @ManyToOne
    @NotNull
    private Doctor doctor;

    @Enumerated
    private RoleParticipantTypes type;
    
 // SPEC START =
    
    public static java.util.List<RoleParticipant> findDoctorWithStandardizedRoleAndRoleTopic(Long standRole, Integer type,int start,int length)	 // Fill Auhtor/ Reviewer Table
	{
		EntityManager em1 = entityManager();
		Log.info("~QUERY findDoctorWithStandardizedRoleAndRoleTopic()");
		Log.info("~QUERY BEFOREE EXECUTION  Stand Role ID  : " + standRole + " Type : " + type );
		String queryString="SELECT rp  from  RoleParticipant rp JOIN rp.standardizedRole sr WHERE sr.id="+standRole+" AND rp.type="+type;
		Log.info("~QUERY STRING : " + queryString); 
		TypedQuery<RoleParticipant> q = em1.createQuery(queryString, RoleParticipant.class);
		//java.util.List<RoleParticipant> result = q.getResultList();
		//Log.info("~QUERY Result : " + result);
		
		q.setFirstResult(start);
    	q.setMaxResults(length);
		return q.getResultList();
		//return result;
		//String queryString="SELECT doc from Doctor doc JOIN doc.roleParticipants rp JOIN rp.standardizedRole sr WHERE sr.id="+standRole+"AND rp.type="+type;
	}
    
    public static long countDoctorWithStandardizedRoleAndRoleTopic(Long standRole, Integer type)	 // Fill Auhtor/ Reviewer Table
  	{
  		EntityManager em1 = entityManager();
  		Log.info("~QUERY findDoctorWithStandardizedRoleAndRoleTopic()");
  		Log.info("~QUERY BEFOREE EXECUTION  Stand Role ID  : " + standRole + " Type : " + type );
  		String queryString="SELECT rp  from  RoleParticipant rp JOIN rp.standardizedRole sr WHERE sr.id="+standRole+" AND rp.type="+type;
  		Log.info("~QUERY STRING : " + queryString); 
  		TypedQuery<RoleParticipant> q = em1.createQuery(queryString, RoleParticipant.class);
  		java.util.List<RoleParticipant> result = q.getResultList();
  		Log.info("~QUERY Result : " + result);
  		return result.size();
  		//String queryString="SELECT doc from Doctor doc JOIN doc.roleParticipants rp JOIN rp.standardizedRole sr WHERE sr.id="+standRole+"AND rp.type="+type;
  	}
	
    // SPEC END =
    
    public static java.util.List<RoleParticipant> findRoleParticipatentByDoctor(Doctor proxy)
    {
    	Log.info("~~Inside Server");
    	EntityManager em = entityManager();
    	String query = "SELECT r FROM RoleParticipant r WHERE r.doctor.id = " + proxy.getId();
    	Log.info("~~Query : " + query);
    	TypedQuery<RoleParticipant> q = em.createQuery(query, RoleParticipant.class);
    	Log.info("~~Result : " + q.getResultList().size());
    	return q.getResultList();
    }

	public StandardizedRole getStandardizedRole() {
        return this.standardizedRole;
    }

	public void setStandardizedRole(StandardizedRole standardizedRole) {
        this.standardizedRole = standardizedRole;
    }

	public Doctor getDoctor() {
        return this.doctor;
    }

	public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

	public RoleParticipantTypes getType() {
        return this.type;
    }

	public void setType(RoleParticipantTypes type) {
        this.type = type;
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
            RoleParticipant attached = RoleParticipant.findRoleParticipant(this.id);
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
    public RoleParticipant merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        RoleParticipant merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new RoleParticipant().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countRoleParticipants() {
        return entityManager().createQuery("SELECT COUNT(o) FROM RoleParticipant o", Long.class).getSingleResult();
    }

	public static List<RoleParticipant> findAllRoleParticipants() {
        return entityManager().createQuery("SELECT o FROM RoleParticipant o", RoleParticipant.class).getResultList();
    }

	public static RoleParticipant findRoleParticipant(Long id) {
        if (id == null) return null;
        return entityManager().find(RoleParticipant.class, id);
    }

	public static List<RoleParticipant> findRoleParticipantEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM RoleParticipant o", RoleParticipant.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Doctor: ").append(getDoctor()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("StandardizedRole: ").append(getStandardizedRole()).append(", ");
        sb.append("Type: ").append(getType()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }
}
