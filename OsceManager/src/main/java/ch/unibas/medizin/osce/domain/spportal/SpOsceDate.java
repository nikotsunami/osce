package ch.unibas.medizin.osce.domain.spportal;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
@Table(name="osce_date")
public class SpOsceDate {

	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
	
	@Id
	@Column(name = "id")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date osceDate;
	 
	 
	@ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.PERSIST)
	private SpSemester semester;

	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "osceDates")
	private Set<SpPatientInSemester> patientInSemesters = new HashSet<SpPatientInSemester>();

	private static transient Logger log = Logger.getLogger(SpOsceDate.class);
	
	public static SpOsceDate findOsceDateBasedOnDateAndSemesterData(Date osceDate2, Integer calYear, int ordinal) {
		
		log.info("finding osce Date based on date and semster data");
		 
		/*EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");

		EntityManager em = emFactory.createEntityManager();*/
		 
		EntityManager em = entityManager();
		
		String queryString ="select od from SpOsceDate od where od.osceDate= :oscedate AND od.semester.calYear="+calYear +" AND od.semester.semester="+ordinal;
		 
		TypedQuery<SpOsceDate> query =em.createQuery(queryString,SpOsceDate.class);
		 
		query.setParameter("oscedate", osceDate2);
		 
		List<SpOsceDate> spOsceDateList = query.getResultList();
		 
		if(spOsceDateList==null || spOsceDateList.size()==0){
			 return null;
		 }else{
		
			 return spOsceDateList.get(0);
		 }
	}
	

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Date getOsceDate() {
        return this.osceDate;
    }

	public void setOsceDate(Date osceDate) {
        this.osceDate = osceDate;
    }

	public SpSemester getSemester() {
        return this.semester;
    }

	public void setSemester(SpSemester semester) {
        this.semester = semester;
    }

	public Set<SpPatientInSemester> getPatientInSemesters() {
        return this.patientInSemesters;
    }

	public void setPatientInSemesters(Set<SpPatientInSemester> patientInSemesters) {
        this.patientInSemesters = patientInSemesters;
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("OsceDate: ").append(getOsceDate()).append(", ");
        sb.append("PatientInSemesters: ").append(getPatientInSemesters() == null ? "null" : getPatientInSemesters().size()).append(", ");
        sb.append("Semester: ").append(getSemester()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	@Version
    @Column(name = "version")
    private Integer version;

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
            SpOsceDate attached = SpOsceDate.findSpOsceDate(this.id);
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
    public SpOsceDate merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SpOsceDate merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new SpOsceDate().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSpOsceDates() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SpOsceDate o", Long.class).getSingleResult();
    }

	public static List<SpOsceDate> findAllSpOsceDates() {
        return entityManager().createQuery("SELECT o FROM SpOsceDate o", SpOsceDate.class).getResultList();
    }

	public static SpOsceDate findSpOsceDate(Long id) {
        if (id == null) return null;
        return entityManager().find(SpOsceDate.class, id);
    }

	public static List<SpOsceDate> findSpOsceDateEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SpOsceDate o", SpOsceDate.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
