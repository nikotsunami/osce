package ch.unibas.medizin.osce.domain.spportal;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Persistence;
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
@Table(name="training_date")
public class SpTrainingDate {

	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
	
	@Id
	@Column(name = "id")
	private Long id;
	
	 @Temporal(TemporalType.TIMESTAMP)
	 @DateTimeFormat(style = "M-")
	 private Date trainingDate;
	 
	 private Boolean isAfternoon;
	 
	 @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.PERSIST)
	 private SpTrainingBlock trainingBlock;
	 
	 @ManyToMany(cascade = CascadeType.ALL, mappedBy = "trainingDates")
	 private Set<SpPatientInSemester> patientInSemesters = new HashSet<SpPatientInSemester>();

	 private static transient Logger log = Logger.getLogger(SpTrainingDate.class);
	  
	public static SpTrainingDate findTrainingDateBasedOnDateAndTrainingBlock(Date trainingDate2,int isAfternoon,Long trainingBlockId) {
		
		log.info("finding trainingDate based on date and block id");
		 
		/* EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");

		 EntityManager em = emFactory.createEntityManager();*/
		 
		 EntityManager em = entityManager();
		 
		 String queryString ="select td from SpTrainingDate td where td.trainingDate= :trainingdate AND td.isAfternoon="+isAfternoon +" AND td.trainingBlock.id="+trainingBlockId;
		 
		 TypedQuery<SpTrainingDate> query =em.createQuery(queryString,SpTrainingDate.class);
		 
		 query.setParameter("trainingdate", trainingDate2);
		 
		 List<SpTrainingDate> spTrainingDateList = query.getResultList();
		 
		 if(spTrainingDateList==null || spTrainingDateList.size()==0){
			 return null;
		 }else{
		
			 return spTrainingDateList.get(0);
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
            SpTrainingDate attached = SpTrainingDate.findSpTrainingDate(this.id);
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
    public SpTrainingDate merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SpTrainingDate merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new SpTrainingDate().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSpTrainingDates() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SpTrainingDate o", Long.class).getSingleResult();
    }

	public static List<SpTrainingDate> findAllSpTrainingDates() {
        return entityManager().createQuery("SELECT o FROM SpTrainingDate o", SpTrainingDate.class).getResultList();
    }

	public static SpTrainingDate findSpTrainingDate(Long id) {
        if (id == null) return null;
        return entityManager().find(SpTrainingDate.class, id);
    }

	public static List<SpTrainingDate> findSpTrainingDateEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SpTrainingDate o", SpTrainingDate.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("IsAfternoon: ").append(getIsAfternoon()).append(", ");
        sb.append("PatientInSemesters: ").append(getPatientInSemesters() == null ? "null" : getPatientInSemesters().size()).append(", ");
        sb.append("TrainingBlock: ").append(getTrainingBlock()).append(", ");
        sb.append("TrainingDate: ").append(getTrainingDate()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Date getTrainingDate() {
        return this.trainingDate;
    }

	public void setTrainingDate(Date trainingDate) {
        this.trainingDate = trainingDate;
    }

	public Boolean getIsAfternoon() {
        return this.isAfternoon;
    }

	public void setIsAfternoon(Boolean isAfternoon) {
        this.isAfternoon = isAfternoon;
    }

	public SpTrainingBlock getTrainingBlock() {
        return this.trainingBlock;
    }

	public void setTrainingBlock(SpTrainingBlock trainingBlock) {
        this.trainingBlock = trainingBlock;
    }

	public Set<SpPatientInSemester> getPatientInSemesters() {
        return this.patientInSemesters;
    }

	public void setPatientInSemesters(Set<SpPatientInSemester> patientInSemesters) {
        this.patientInSemesters = patientInSemesters;
    }
}
