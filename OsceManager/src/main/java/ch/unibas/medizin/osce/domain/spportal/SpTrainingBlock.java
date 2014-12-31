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
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

@Configurable
@Entity
@Table(name="training_block")
public class SpTrainingBlock {

	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
	
	@Id
	@Column(name = "id")
	private Long id;
	
	 @Temporal(TemporalType.TIMESTAMP)
	 @DateTimeFormat(style = "M-")
	 private Date startDate;
	 
	 @ManyToOne
	 private SpSemester semester;

	 @OneToMany(cascade = CascadeType.ALL, mappedBy = "trainingBlock")
	 private Set<SpTrainingDate> trainingDates = new HashSet<SpTrainingDate>();

	  private static transient Logger log = Logger.getLogger(SpTrainingBlock.class);
	  
	public static SpTrainingBlock findTrainingBlockBasedOnDateAndSemesterData(Date startDate2, Integer calYear, int ordinal) {
		
		log.info("finding training block based on year and semester data");
		 
		/* EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");

		 EntityManager em = emFactory.createEntityManager();*/
		 
		EntityManager em = entityManager();
		
		 String queryString ="select tb from SpTrainingBlock tb where tb.startDate= :startDate AND tb.semester.calYear="+calYear + " AND tb.semester.semester="+ordinal;
		 
		 TypedQuery<SpTrainingBlock> query =em.createQuery(queryString,SpTrainingBlock.class);
		 
		 query.setParameter("startDate",startDate2);
		 
		 List<SpTrainingBlock> spTrainingBlockList = query.getResultList();
		 
		 if(spTrainingBlockList==null || spTrainingBlockList.size()==0){
			 return null;
		 }else{
		
			 return spTrainingBlockList.get(0);
		 }
		 
   }
		

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Date getStartDate() {
        return this.startDate;
    }

	public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

	public SpSemester getSemester() {
        return this.semester;
    }

	public void setSemester(SpSemester semester) {
        this.semester = semester;
    }

	public Set<SpTrainingDate> getTrainingDates() {
        return this.trainingDates;
    }

	public void setTrainingDates(Set<SpTrainingDate> trainingDates) {
        this.trainingDates = trainingDates;
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Semester: ").append(getSemester()).append(", ");
        sb.append("StartDate: ").append(getStartDate()).append(", ");
        sb.append("TrainingDates: ").append(getTrainingDates() == null ? "null" : getTrainingDates().size()).append(", ");
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
            SpTrainingBlock attached = SpTrainingBlock.findSpTrainingBlock(this.id);
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
    public SpTrainingBlock merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SpTrainingBlock merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new SpTrainingBlock().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSpTrainingBlocks() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SpTrainingBlock o", Long.class).getSingleResult();
    }

	public static List<SpTrainingBlock> findAllSpTrainingBlocks() {
        return entityManager().createQuery("SELECT o FROM SpTrainingBlock o", SpTrainingBlock.class).getResultList();
    }

	public static SpTrainingBlock findSpTrainingBlock(Long id) {
        if (id == null) return null;
        return entityManager().find(SpTrainingBlock.class, id);
    }

	public static List<SpTrainingBlock> findSpTrainingBlockEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SpTrainingBlock o", SpTrainingBlock.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
