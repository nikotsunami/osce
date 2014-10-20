package ch.unibas.medizin.osce.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
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
}