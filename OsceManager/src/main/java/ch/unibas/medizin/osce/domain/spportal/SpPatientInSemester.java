package ch.unibas.medizin.osce.domain.spportal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import ch.unibas.medizin.osce.domain.OsceDate;
import ch.unibas.medizin.osce.domain.PatientInSemester;

@Entity
@Configurable
@Table(name="patient_in_semester")
public class SpPatientInSemester {

	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
	
	 @Id
	 @Column(name = "id")
	 private Long id;
	 
    @ManyToOne
    private SpSemester semester;

    @ManyToOne
    private SpStandardizedPatient standardizedPatient;

    private Boolean accepted;


    private Integer value = 0;

    @ManyToOne
    private SPPortalPerson person;
 
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "accepted_trainings")
    private Set<SpTrainingDate> trainingDates = new HashSet<SpTrainingDate>();
	
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "accepted_osce")
    private Set<SpOsceDate> osceDates = new HashSet<SpOsceDate>();

    private static transient Logger log = Logger.getLogger(SpPatientInSemester.class);
    
	public static List<SpPatientInSemester> findPatientInSemesterBasedOnSemesterId(Long semId) {
		
		log.info("finding patient in semester based on semester id : " + semId);
		 
		 /*EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");

		 EntityManager em = emFactory.createEntityManager();*/
		 
		EntityManager em  =entityManager();
		
		 String queryString ="select sp from SpPatientInSemester sp where sp.semester.id="+semId + " ORDER BY id DESC";
		 
		 TypedQuery<SpPatientInSemester> query =em.createQuery(queryString,SpPatientInSemester.class);
		 
		 List<SpPatientInSemester> spPatientInSemesterList = query.getResultList();
		 
		 if(spPatientInSemesterList==null || spPatientInSemesterList.size()==0){
			 return null;
		 }else{
		
			 return spPatientInSemesterList;
		 }
		
	}
	
	public static List<SpPatientInSemester> findPatientInSemesterBasedOnSemAndId(Long lastspPatientInSemId, Long semId) {
		try{
			log.info("finding patient in semester based on sem id : " + semId);
			 
			/* EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");

			 EntityManager em = emFactory.createEntityManager();*/
			 
			EntityManager em =entityManager();
			
			 String sql = "SELECT pis FROM SpPatientInSemester AS pis WHERE pis.id >" + lastspPatientInSemId + " AND pis.semester.id="+semId;
		     
			 TypedQuery<SpPatientInSemester> query = em.createQuery(sql, SpPatientInSemester.class);
		     
			 List<SpPatientInSemester> resultList = query.getResultList();
		     
			 if (resultList == null || resultList.size() == 0){ 
				 	return null;
			 }else{
				 return resultList;
			 }
		        
			}catch (Exception e) {
				log.error(e.getMessage(),e);
				return null;
			}
		
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
            SpPatientInSemester attached = SpPatientInSemester.findSpPatientInSemester(this.id);
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
    public SpPatientInSemester merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SpPatientInSemester merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new SpPatientInSemester().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSpPatientInSemesters() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SpPatientInSemester o", Long.class).getSingleResult();
    }

	public static List<SpPatientInSemester> findAllSpPatientInSemesters() {
        return entityManager().createQuery("SELECT o FROM SpPatientInSemester o", SpPatientInSemester.class).getResultList();
    }

	public static SpPatientInSemester findSpPatientInSemester(Long id) {
        if (id == null) return null;
        return entityManager().find(SpPatientInSemester.class, id);
    }

	public static List<SpPatientInSemester> findSpPatientInSemesterEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SpPatientInSemester o", SpPatientInSemester.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public SpSemester getSemester() {
        return this.semester;
    }

	public void setSemester(SpSemester semester) {
        this.semester = semester;
    }

	public SpStandardizedPatient getStandardizedPatient() {
        return this.standardizedPatient;
    }

	public void setStandardizedPatient(SpStandardizedPatient standardizedPatient) {
        this.standardizedPatient = standardizedPatient;
    }

	public Boolean getAccepted() {
        return this.accepted;
    }

	public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

	public Integer getValue() {
        return this.value;
    }

	public void setValue(Integer value) {
        this.value = value;
    }

	public SPPortalPerson getPerson() {
        return this.person;
    }

	public void setPerson(SPPortalPerson person) {
        this.person = person;
    }

	public Set<SpTrainingDate> getTrainingDates() {
        return this.trainingDates;
    }

	public void setTrainingDates(Set<SpTrainingDate> trainingDates) {
        this.trainingDates = trainingDates;
    }

	public Set<SpOsceDate> getOsceDates() {
        return this.osceDates;
    }

	public void setOsceDates(Set<SpOsceDate> osceDates) {
        this.osceDates = osceDates;
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Accepted: ").append(getAccepted()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("OsceDates: ").append(getOsceDates() == null ? "null" : getOsceDates().size()).append(", ");
        sb.append("Person: ").append(getPerson()).append(", ");
        sb.append("Semester: ").append(getSemester()).append(", ");
        sb.append("StandardizedPatient: ").append(getStandardizedPatient()).append(", ");
        sb.append("TrainingDates: ").append(getTrainingDates() == null ? "null" : getTrainingDates().size()).append(", ");
        sb.append("Value: ").append(getValue()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }
}
