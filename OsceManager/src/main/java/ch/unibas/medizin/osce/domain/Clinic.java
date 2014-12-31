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
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class Clinic {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
    @NotNull
    @Column(unique = true)
    @Size(max = 60)
    private String name;

    @Size(max = 60)
    private String street;

    @Size(max = 30)
    private String city;

    private Integer postalCode;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clinic")
    private Set<Doctor> doctors = new HashSet<Doctor>();
    
    
    public static List<Clinic> findAllDoctorsId(Long id) {
    	EntityManager em = entityManager();
    	System.out.println("BEFORE CALL------->");
    	String queryString="SELECT o FROM Clinic as o  where o.id="+id;
    	System.out.println("query--"+ queryString);
    	TypedQuery<Clinic> q = em.createQuery(queryString, Clinic.class);
    	System.out.println("success done with size");
    	
    	return q.getResultList();
    }
    
    public static List<Clinic> findListOfDocsId(Long id) {
    	EntityManager em = entityManager();
    	System.out.println("BEFORE CALL------->");
    	String queryString="SELECT o FROM Clinic as o  where o.id="+id;
    	System.out.println("query--"+ queryString);
    	TypedQuery<Clinic> q = em.createQuery(queryString, Clinic.class);
    	System.out.println("success done with size");
    	
    	return q.getResultList();
    }
    
    public static Long countClinicsBySearch(String q) {
    	EntityManager em = entityManager();
    	TypedQuery<Long> query = em.createQuery("SELECT COUNT(o) FROM Clinic o WHERE o.name LIKE :q", Long.class);
    	query.setParameter("q", "%" + q + "%");
    	
    	return query.getSingleResult();
    }
    
    public static List<Clinic> findClinicsBySearch(String q, int firstResult, int maxResults) {
        if (q == null) throw new IllegalArgumentException("The q argument is required");
        EntityManager em = entityManager();
        TypedQuery<Clinic> query = em.createQuery("SELECT o FROM Clinic AS o WHERE o.name LIKE :q", Clinic.class);
        query.setParameter("q", "%" + q + "%");
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        
        return query.getResultList();
    }
    
   public static Long findSpecialist(){
    	EntityManager em = entityManager();
    	TypedQuery<Long> query = em.createQuery("SELECT d.email FROM Clinic AS o, Doctor AS d WHERE o.id = 3", Long.class);
    	return query.getSingleResult();

    	
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("City: ").append(getCity()).append(", ");
        sb.append("Doctors: ").append(getDoctors() == null ? "null" : getDoctors().size()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("PostalCode: ").append(getPostalCode()).append(", ");
        sb.append("Street: ").append(getStreet()).append(", ");
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
            Clinic attached = Clinic.findClinic(this.id);
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
    public Clinic merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Clinic merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new Clinic().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countClinics() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Clinic o", Long.class).getSingleResult();
    }

	public static List<Clinic> findAllClinics() {
        return entityManager().createQuery("SELECT o FROM Clinic o", Clinic.class).getResultList();
    }

	public static Clinic findClinic(Long id) {
        if (id == null) return null;
        return entityManager().find(Clinic.class, id);
    }

	public static List<Clinic> findClinicEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Clinic o", Clinic.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public String getStreet() {
        return this.street;
    }

	public void setStreet(String street) {
        this.street = street;
    }

	public String getCity() {
        return this.city;
    }

	public void setCity(String city) {
        this.city = city;
    }

	public Integer getPostalCode() {
        return this.postalCode;
    }

	public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

	public Set<Doctor> getDoctors() {
        return this.doctors;
    }

	public void setDoctors(Set<Doctor> doctors) {
        this.doctors = doctors;
    }
}
