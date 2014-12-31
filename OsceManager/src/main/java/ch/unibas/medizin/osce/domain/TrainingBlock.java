package ch.unibas.medizin.osce.domain;

import java.util.Date;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class TrainingBlock {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	 @Temporal(TemporalType.TIMESTAMP)
	 @DateTimeFormat(style = "M-")
	 private Date startDate;
	 
	 @ManyToOne
	 private Semester semester;

	 @OneToMany(/*cascade = CascadeType.PERSIST,*/ mappedBy = "trainingBlock")
	 private Set<TrainingDate> trainingDates = new HashSet<TrainingDate>();
	 
	 private static Logger Log = Logger.getLogger(TrainingBlock.class);
	 
	 public static TrainingBlock findTrainingBlockForGivenDate(Date trainingBlockDate,Long semesterId){
		 
		 Log.info("finding training block of given date : " + trainingBlockDate);
		 
		 try{
		 EntityManager em = TrainingBlock.entityManager();
		 
		 String queryString="SELECT tb FROM TrainingBlock AS tb WHERE tb.startDate=:trainingblockdate  AND tb.semester.id="+semesterId ;
		
		TypedQuery<TrainingBlock> query = em.createQuery(queryString, TrainingBlock.class);
			
		 query.setParameter("trainingblockdate", trainingBlockDate);

		 List<TrainingBlock> resultList = query.getResultList();
	       
		 if (resultList == null || resultList.size() == 0){
			 return null;
	        }else{
	        	return resultList.get(0);
	       }
		 }catch (Exception e) {
			Log.error(e.getMessage(),e);
			return null;
		}
	 }
	 
	 public static Set<TrainingBlock> findTrainingBlockOfSemester(Long semesterId){
		 
		 Log.info("finding training block of given semester Id : " + semesterId);
		 
		 EntityManager em = TrainingBlock.entityManager();
		 
		 String queryString="SELECT tb FROM TrainingBlock AS tb WHERE tb.semester.id="+semesterId ;
		
		TypedQuery<TrainingBlock> query = em.createQuery(queryString, TrainingBlock.class);

		 List<TrainingBlock> resultList = query.getResultList();
	       
		 if (resultList == null || resultList.size() == 0){
			 return null;
	        }else{
	        	return new HashSet<TrainingBlock>(resultList);
	       }
	 }
	 
	 public static List<TrainingDate> splitBlock(Date blockStartDate,Long blockId,Long semesterId){
		 Log.info("creating new TrainingBlock and assigning refrence of old block to it: " + blockStartDate);
		 
		 try{
			Semester semester =Semester.findSemester(semesterId);
			
			List<TrainingDate> trainingDateList=TrainingDate.getTrainingDateBasedOnDateAndBlockID(blockStartDate,blockId);
			
			TrainingBlock trainingBlock = new TrainingBlock();
			trainingBlock.setSemester(semester);
			trainingBlock.setStartDate(blockStartDate);
			trainingBlock.persist();
			
			for(TrainingDate trainingDate : trainingDateList){
				trainingDate.setTrainingBlock(trainingBlock);
				trainingDate.persist();
			}
		 
			return trainingDateList;
		 }catch (Exception e) {
			Log.error(e.getMessage(), e);
			return null;
		}
		
	 }
	 public static List<TrainingDate> joinBlock(Date blockStartDate, Long blockId,Long semesterId){
		 
		Log.info("Joining block ");
		 try{
		 List<TrainingDate> trainingDateList=TrainingDate.getTrainingDateBasedOnDateAndBlockID(blockStartDate,blockId);
		 
		 
		 
		 DateTime trainingBlockDateTime = new DateTime(blockStartDate);
		 
		 
		 TrainingBlock trainingBlock = findTrainingBlockForGivenDate(trainingBlockDateTime.minusDays(7).toDate(),semesterId);
		 
		 while(trainingBlock==null){
			 //I am using this logic instead of finding Training block that is last block of semester because it may possible that user split block in middle 
			 //so in that case I get wrong block reference so current logic is well even it is worst.
			 trainingBlockDateTime =trainingBlockDateTime.minusDays(7);
			 
			 trainingBlock= findTrainingBlockForGivenDate(trainingBlockDateTime.toDate(),semesterId);
			 
			 /*if(trainingBlockDateTime.getDayOfMonth()==1){
				 break;
			 }*/
		 }
		 
		 for(TrainingDate trainingDate : trainingDateList){
				trainingDate.setTrainingBlock(trainingBlock);
				trainingDate.persist();
		 }
		 
		 TrainingBlock oldTrainingBlock =TrainingBlock.findTrainingBlock(blockId);
		 oldTrainingBlock.remove();
		 
		 return trainingDateList;
		 
		 }catch (Exception e) {
			Log.error(e.getMessage(), e);
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
            TrainingBlock attached = TrainingBlock.findTrainingBlock(this.id);
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
    public TrainingBlock merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        TrainingBlock merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new TrainingBlock().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countTrainingBlocks() {
        return entityManager().createQuery("SELECT COUNT(o) FROM TrainingBlock o", Long.class).getSingleResult();
    }

	public static List<TrainingBlock> findAllTrainingBlocks() {
        return entityManager().createQuery("SELECT o FROM TrainingBlock o", TrainingBlock.class).getResultList();
    }

	public static TrainingBlock findTrainingBlock(Long id) {
        if (id == null) return null;
        return entityManager().find(TrainingBlock.class, id);
    }

	public static List<TrainingBlock> findTrainingBlockEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM TrainingBlock o", TrainingBlock.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public Date getStartDate() {
        return this.startDate;
    }

	public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

	public Semester getSemester() {
        return this.semester;
    }

	public void setSemester(Semester semester) {
        this.semester = semester;
    }

	public Set<TrainingDate> getTrainingDates() {
        return this.trainingDates;
    }

	public void setTrainingDates(Set<TrainingDate> trainingDates) {
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
}