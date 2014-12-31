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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.log4j.Logger;
import org.hibernate.sql.Select;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import ch.unibas.medizin.osce.shared.Sorting;
import ch.unibas.medizin.osce.shared.StudyYears;



import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Configurable
@Entity
public class Specialisation {
	
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	
	private static Logger Log = Logger.getLogger(Specialisation.class);
	
    @NotNull
    @Size(min = 2, max = 255)
    private String name;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "specialisation")
    private Set<RoleTopic> roleTopics = new HashSet<RoleTopic>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "specialisation")
    private Set<Doctor> doctors = new HashSet<Doctor>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "specialisation")
    private Set<OscePostBlueprint> oscePostBlueprint = new HashSet<OscePostBlueprint>();
    
    public static Long countSpecializations(String name) {
    	Log.info("Inside to fire query to get total count:");
    	EntityManager em = entityManager();
    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM Specialisation o WHERE o.name LIKE :name1", Long.class);
    	q.setParameter("name1", "%" + name + "%");
    	
    	System.out.println(" :" + name);
    	return q.getSingleResult();
    }
    
    public static List<Specialisation> findAllSpecialisation(String sortname,Sorting order,String name, int firstResult, int maxResults) {
        Log.info("Inside to fire Query To get all Specialisation");
    	if (name == null) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = entityManager();
        String sqlQuery = "SELECT o FROM Specialisation AS o WHERE o.name LIKE :name1 order by " + sortname + " " + order;
        TypedQuery<Specialisation> q = em.createQuery(sqlQuery, Specialisation.class);
        q.setParameter("name1", "%" + name + "%");
        q.setFirstResult(firstResult);
        q.setMaxResults(maxResults);
        
        return q.getResultList();
    }
 // TestCasePurpose Method start {
    public static Specialisation getSpecialisationForId(Long id){
    	Log.info("Inside getSpecialisationForId() With Id " + id);
    	EntityManager em = entityManager();
        String sqlQuery = "SELECT o FROM Specialisation AS o WHERE o.id="+id;
        TypedQuery<Specialisation> q = em.createQuery(sqlQuery, Specialisation.class);
    	return q.getSingleResult();
    }
    
    /*public static java.util.List<Specialisation> findSpecialisationSortByName()
	{
		Log.info("~~Inside findSpecialisationSortByName Method");
		EntityManager em = entityManager();				
		String queryString="select sp from Specialisation as sp order by sp.name";			
		Log.info("~QUERY String: " + queryString);
		TypedQuery<Specialisation> q = em.createQuery(queryString, Specialisation.class);
		java.util.List<Specialisation> result = q.getResultList();
		Log.info("~QUERY Result : " + result);
		return result;
	}*/
    public static java.util.List<Specialisation> findSpecialisationSortByName(StudyYears studyYear)
	{
		Log.info("~~Inside findSpecialisationSortByName Method");
		EntityManager em = entityManager();				
		String queryString = "SELECT DISTINCT rt.specialisation FROM RoleTopic rt WHERE rt.studyYear = " + studyYear.ordinal() + " ORDER BY rt.specialisation.name";
		Log.info("~QUERY String: " + queryString);
		TypedQuery<Specialisation> q = em.createQuery(queryString, Specialisation.class);
		java.util.List<Specialisation> result = q.getResultList();
		Log.info("~QUERY Result : " + result);
		return result;
	}
 // TestCasePurpose Method End }

   public static java.util.List<Specialisation> findSpecialisations(){

    	EntityManager em = entityManager();
        String sqlQuery = "SELECT o FROM Specialisation AS o ";
        TypedQuery<Specialisation> q = em.createQuery(sqlQuery, Specialisation.class);
        
        return q.getResultList();
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public Set<RoleTopic> getRoleTopics() {
        return this.roleTopics;
    }

	public void setRoleTopics(Set<RoleTopic> roleTopics) {
        this.roleTopics = roleTopics;
    }

	public Set<Doctor> getDoctors() {
        return this.doctors;
    }

	public void setDoctors(Set<Doctor> doctors) {
        this.doctors = doctors;
    }

	public Set<OscePostBlueprint> getOscePostBlueprint() {
        return this.oscePostBlueprint;
    }

	public void setOscePostBlueprint(Set<OscePostBlueprint> oscePostBlueprint) {
        this.oscePostBlueprint = oscePostBlueprint;
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Doctors: ").append(getDoctors() == null ? "null" : getDoctors().size()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("OscePostBlueprint: ").append(getOscePostBlueprint() == null ? "null" : getOscePostBlueprint().size()).append(", ");
        sb.append("RoleTopics: ").append(getRoleTopics() == null ? "null" : getRoleTopics().size()).append(", ");
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
            Specialisation attached = Specialisation.findSpecialisation(this.id);
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
    public Specialisation merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Specialisation merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new Specialisation().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSpecialisations() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Specialisation o", Long.class).getSingleResult();
    }

	public static List<Specialisation> findAllSpecialisations() {
        return entityManager().createQuery("SELECT o FROM Specialisation o", Specialisation.class).getResultList();
    }

	public static Specialisation findSpecialisation(Long id) {
        if (id == null) return null;
        return entityManager().find(Specialisation.class, id);
    }

	public static List<Specialisation> findSpecialisationEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Specialisation o", Specialisation.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
