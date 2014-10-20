package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class TrainingDate {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	 @Temporal(TemporalType.TIMESTAMP)
	 @DateTimeFormat(style = "M-")
	 private Date trainingDate;
	 
	 private Boolean isAfternoon;
	 
	 @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.PERSIST)
	 private TrainingBlock trainingBlock;
	 
	 @ManyToMany(cascade = CascadeType.ALL, mappedBy = "trainingDates")
	 private Set<PatientInSemester> patientInSemesters = new HashSet<PatientInSemester>();
	 
	 @OneToMany(cascade = CascadeType.ALL, mappedBy = "trainingDate")
	 private Set<Training> trainings = new HashSet<Training>();

	 @OneToMany(cascade = CascadeType.ALL, mappedBy = "trainingDate")
	 private Set<TrainingSuggestion> trainingSuggestions = new HashSet<TrainingSuggestion>();
	 
	 private static Logger Log = Logger.getLogger(TrainingDate.class);
	 
	 public static String findTrainingDateBasedOnDateAndSemester(Long semesterID,Date dateOnWidget){
			
		 String result="";
		 
		 List<TrainingDate> resultList = findTrainingDateBasedOnDateAndSemesterId(semesterID,dateOnWidget);
		 
	        if (resultList == null || resultList.size() == 0){
	        	result="NONE";
	        }else{
	        	result="TRAININGDATE";
	        }
	           
		 return result;
		 
	 }
	 
	 public static List<TrainingDate> findTrainingDateBasedOnDateAndSemesterId(Long semesterID,Date dateOnWidget){
		 
		 EntityManager em = TrainingDate.entityManager();
		 
		 String queryString="SELECT td FROM TrainingDate AS td WHERE td.trainingDate=:trainingdate  AND td.trainingBlock.semester.id="+semesterID ;
		
		TypedQuery<TrainingDate> query = em.createQuery(queryString, TrainingDate.class);
			
		 query.setParameter("trainingdate", dateOnWidget);

		 Log.info("Query : " + query.toString());
		 
		 List<TrainingDate> resultList = query.getResultList();
	       
		 return resultList;
	 }
	 public static TrainingBlock persistThisDateAsTrainingDate(Date currentlySelectedDate,Long semesterId){
		 Log.info("At persistThisDateAsTrainingDate() to create tarining date with semsterid" + semesterId  +" And date is :" + currentlySelectedDate) ;
		 boolean isFirstBlock= false;
		 
		 Semester semester =Semester.findSemester(semesterId);
		
		 Set<TrainingBlock> trainingBlocks = TrainingBlock.findTrainingBlockOfSemester(semester.getId()); /*semester.getTrainingBlocks()*/;
		 
		 DateTime trainingBlockDateTime = new DateTime(currentlySelectedDate);
		 DateTime firstDayOfWeek =trainingBlockDateTime.withDayOfWeek( DateTimeConstants.MONDAY );
		 
		 TrainingBlock trainingBlock=null;
		 
		 try{
		 
			 
				 if(trainingBlocks==null || trainingBlocks.size()==0){
						
					 trainingBlock = new TrainingBlock();
					 trainingBlock.setSemester(semester);
					 trainingBlock.setStartDate(firstDayOfWeek.toDate());
						 
					 trainingBlock.persist();
						 
					 persistTrainingDateForMorningAndAfternoon(currentlySelectedDate,trainingBlock);
					 isFirstBlock=true;
					 
				 }else if(trainingBlocks.size()==1){
					 
					 trainingBlock=trainingBlocks.iterator().next();
					 
					 persistTrainingDateForMorningAndAfternoon(currentlySelectedDate,trainingBlock);
					 
				 }else if(trainingBlocks.size() > 1 ){
					
					 @SuppressWarnings("unused")
					 TrainingBlock trainingBlock2 = TrainingBlock.findTrainingBlockForGivenDate(firstDayOfWeek.toDate(), semesterId);
					 //it may possible block is some week above from currently selected date.
					 if(trainingBlock2==null){
						 while(trainingBlock2==null){
							 //I am using this logic instead of finding Training block that is last block of semester because it may possible that user split block in middle 
							 //so in that case I get wrong block reference so current logic is well even it is worst.
							 firstDayOfWeek=firstDayOfWeek.minusDays(7);
							 
							 trainingBlock2= TrainingBlock.findTrainingBlockForGivenDate(firstDayOfWeek.toDate(), semesterId);
							 
							 /*if(firstDayOfWeek.getDayOfMonth()==1){
								 break;
							 }*/
						 }
					 }
					 
					 trainingBlock=trainingBlock2;
					 
					 persistTrainingDateForMorningAndAfternoon(currentlySelectedDate, trainingBlock2);
				 }
			 
		 }catch (Exception e) {
			 Log.error(e.getMessage(),e);
			 return null;
		}
		 return trainingBlock;
		 
	 }
	 public static TrainingBlock persistSelectedMultipleDatesAsTrainingDates(List<Date> currentlySelectedDatesList, Long semId){
		 Boolean result=null;
		 boolean returnResult=false;
		 
		 TrainingBlock trainingBlock=null;
		 
		 for(Date date : currentlySelectedDatesList){
		
			 trainingBlock=persistThisDateAsTrainingDate(date, semId);
			 
			 /*if(trainingBlock==null){
				 if(result==true && returnResult==false){
					 returnResult=true;
				 }
			 }*/
		 }
		 return trainingBlock;
	 }

	 public static void persistTrainingDateForMorningAndAfternoon(Date tainingDate,TrainingBlock trainingBlock)throws Exception{
		 Log.info("creating training dates with isAfternoon 0 and 1");
		 try{
			
			 TrainingDate trainingDateForMorning = new TrainingDate();
			 trainingDateForMorning.setIsAfternoon(false);
			 trainingDateForMorning.setTrainingBlock(trainingBlock);
			 trainingDateForMorning.setTrainingDate(tainingDate);
				 
			 trainingDateForMorning.persist();
				 
			 TrainingDate trainingDateForAfternoon = new TrainingDate();
			 trainingDateForAfternoon.setIsAfternoon(true);
			 trainingDateForAfternoon.setTrainingBlock(trainingBlock);
			 trainingDateForAfternoon.setTrainingDate(tainingDate);
				 
			 trainingDateForAfternoon.persist();
			 
		 }catch (Exception e) {
			Log.error(e.getMessage(), e);
			throw e;
		}
	 }
	 
	 public static Boolean removeTrainingDate(Date currentlySelectedDate, Long semesterId){
		
		 boolean isRemovedTrainingBlock=false;
		 
		 Log.info("Removing training dates");
	
		 Set<TrainingBlock> trainingBlocks= new HashSet<TrainingBlock>();
		 
		 try{
			 
			 List<TrainingDate> trainingDates = findTrainingDateBasedOnDateAndSemesterId(semesterId, currentlySelectedDate);
			
				 for (Iterator iterator = trainingDates.iterator(); iterator.hasNext();) {
					 TrainingDate trainingDate = (TrainingDate) iterator.next();
					 trainingBlocks.add(trainingDate.getTrainingBlock());
					 trainingDate.remove();
				 }
				 //Removing training block if now there is not training date refering that block.
				 for (Iterator iterator = trainingBlocks.iterator(); iterator.hasNext();) {
					TrainingBlock trainingBlock = (TrainingBlock) iterator.next();
					if(trainingBlock.getTrainingDates().size()==0){
						isRemovedTrainingBlock=true;
						trainingBlock.remove();
					}
				}
			 
		 }catch (Exception e) {
			Log.error(e.getMessage(),e);
			isRemovedTrainingBlock=false;
			return null;
		}
		 return isRemovedTrainingBlock;
	 }
	 
	 public static List<TrainingDate> findTrainingDatesFromGivenDateToEndOfMonth(Date date,Long semId){
 
		 DateTime dateTime = new DateTime(date);
		 
		 DateTime endOfMonth = dateTime.dayOfMonth().withMaximumValue();
				 
		 EntityManager em = TrainingDate.entityManager();
 
		String queryString="SELECT td FROM TrainingDate AS td WHERE td.trainingDate >= :trainingstartdate  AND td.trainingDate <= :monthEndDate AND td.trainingBlock.semester.id="+semId + 
				"AND td.isAfternoon=0 ORDER BY trainingDate";
		
		TypedQuery<TrainingDate> query = em.createQuery(queryString, TrainingDate.class);
			
		 query.setParameter("trainingstartdate", date);
		 
		 query.setParameter("monthEndDate", endOfMonth.toDate());

		 Log.info("Query : " + query.unwrap(Query.class).getQueryString());
		 
		 List<TrainingDate> resultList = query.getResultList();
	       
		 return resultList;
	 }

	public static List<TrainingDate> getTrainingDateBasedOnDateAndBlockID(Date blockStartDate,Long blockId) {
		
		Log.info("finding training date from given date and associated with block id");
		
		EntityManager em = TrainingBlock.entityManager();
		 
		 String queryString="SELECT td FROM TrainingDate AS td WHERE td.trainingDate >=:trainingblockdate  AND td.trainingBlock.id="+blockId;
		
		TypedQuery<TrainingDate> query = em.createQuery(queryString, TrainingDate.class);
			
		 query.setParameter("trainingblockdate", blockStartDate);

		 List<TrainingDate> resultList = query.getResultList();
	       
		 if (resultList == null || resultList.size() == 0){
			 return null;
	        }else{
	        	return resultList;
	       }
		
	}
	public static List<TrainingDate> findTrainingDateOfSemesterOrderByDateDesc(Long semId){
		Log.info("Finding all training dates of given sem :" + semId + " ORDER BY trainingDate DESC");
		try{
			EntityManager em = TrainingDate.entityManager();
			 
			 String queryString="SELECT td FROM TrainingDate AS td WHERE td.trainingBlock.semester.id="+semId + " ORDER BY td.trainingDate DESC";
			
			TypedQuery<TrainingDate> query = em.createQuery(queryString, TrainingDate.class);
				
			 Log.info("Query : " + query.toString());
			 
			 List<TrainingDate> resultList = query.getResultList();
		       
			 return resultList;
			 
		}catch (Exception e) {
			Log.error(e.getMessage(), e);
			return null;
		}
	}

	public static List<TrainingDate> findAllTrainingDatesOfSemester(Long semesterId) {
		Log.info("Finding all training dates of given sem :" + semesterId);
		
		try{
			EntityManager em = TrainingDate.entityManager();
			 
			 String queryString="SELECT td FROM TrainingDate AS td WHERE td.trainingBlock.semester.id="+semesterId;
			
			TypedQuery<TrainingDate> query = em.createQuery(queryString, TrainingDate.class);
				
			 Log.info("Query : " + query.toString());
			 
			 List<TrainingDate> resultList = query.getResultList();
		       
			 return resultList;
			 
		}catch (Exception e) {
			Log.error(e.getMessage(), e);
			return null;
		}
	}
	public static TrainingDate findTrainingDateBasedOnDateSemAndMorningValue(Date date,Long semId,int isAfterNoon){
		
		EntityManager em = TrainingDate.entityManager();
		 
		String queryString="SELECT td FROM TrainingDate AS td WHERE td.trainingDate=:trainingdate  AND td.trainingBlock.semester.id="+semId + " AND td.isAfternoon="+isAfterNoon ;
		
		TypedQuery<TrainingDate> query = em.createQuery(queryString, TrainingDate.class);
			
		 query.setParameter("trainingdate", date);

		 Log.info("Query : " + query.toString());
		 
		 List<TrainingDate> resultList = query.getResultList();
	       
		 if(resultList==null || resultList.size()==0){
			 return null;
		 }else{
		
			 return resultList.get(0);
		 }
		 
	}
	
	public static List<TrainingSuggestion> findTrainingSuggestionsOfDate(Date currentlySelectedDate, Long semId){
		
		Log.info("finding morning and afternoon suggested Roles");
		
		try{
			
			List<TrainingSuggestion> listOfTrainingSuggestion= TrainingSuggestion.findTrainingSuggestionOfTDNotAssignedToTraining(currentlySelectedDate,semId);
		
		return listOfTrainingSuggestion;
		
		}catch (Exception e) {
			Log.error(e.getMessage(), e);
			return null;
		}
	}
}
