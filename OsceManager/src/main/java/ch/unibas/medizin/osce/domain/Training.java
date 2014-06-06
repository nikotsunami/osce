package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
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

import com.google.gwt.requestfactory.shared.Request;

@RooJavaBean
@RooToString
@RooEntity
public class Training {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	
	 @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.PERSIST)
	 private TrainingDate trainingDate;
	 
	 @Temporal(TemporalType.TIMESTAMP)
	 @DateTimeFormat(style = "M-")
	 private Date timeStart;

	 @Temporal(TemporalType.TIMESTAMP)
	 @DateTimeFormat(style = "M-")
	  private Date timeEnd;
	 
	 @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.PERSIST)
	 private StandardizedRole standardizedRole;
	
	 @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "training")
	 private Set<TrainingSuggestion> trainingSuggestions = new HashSet<TrainingSuggestion>();
	
	 
	 private static Logger Log = Logger.getLogger(Training.class);
		
	 public static Training createTraining(Date startTimeDate, Date endTimedate, Long roleId,Long semId,boolean isBindTrainingToSuggestion){
		 
		 Log.info("saving Training");
		 
		 try{
			 
			 StandardizedRole standardizedRole = StandardizedRole.findStandardizedRole(roleId);
			 
			 Date date = new Date(startTimeDate.getTime());
			 date.setHours(0);
			 date.setMinutes(0);
			 
			 Log.info("Training date is : " + date);
			 
			 int isAfterNoon;
			 if(startTimeDate.getHours() < 12){
				 isAfterNoon=0;
			 }else {
				 isAfterNoon=1;
			 }
			
			 TrainingDate trainingDate = TrainingDate.findTrainingDateBasedOnDateSemAndMorningValue(date, semId, isAfterNoon);
			 
			 if(trainingDate!=null){
				 Log.info("founded  training Date id is : " + trainingDate.getId());
					 Training training = new Training();
					 training.setStandardizedRole(standardizedRole);
					 training.setTimeEnd(endTimedate);
					 training.setTimeStart(startTimeDate);
					 training.setTrainingDate(trainingDate);
					 
					 if(isBindTrainingToSuggestion){
						
						 TrainingSuggestion trainingSuggestion = TrainingSuggestion.findTrainingSueestionBasedOnTDAndRole(trainingDate.getId(),standardizedRole.getId());
						 
						 if(trainingSuggestion!=null){
							
								 training.persist();
								 trainingSuggestion.setTraining(training);
								 trainingSuggestion.persist();
								 return training;
							 }else{
								 return null;
							 }
					 }else{
						 training.persist();
						 return training;
					 }
			 }
			 else{
					 Log.info("Training Date not found so creating new: ");
					 
					TrainingDate.persistThisDateAsTrainingDate(date, semId);
					
					TrainingDate newTrainingDate = TrainingDate.findTrainingDateBasedOnDateSemAndMorningValue(date, semId, isAfterNoon);
					
					 Training training = new Training();
					 training.setStandardizedRole(standardizedRole);
					 training.setTimeEnd(endTimedate);
					 training.setTimeStart(startTimeDate);
					 training.setTrainingDate(newTrainingDate);
					 //training.persist();
					 
					 if(isBindTrainingToSuggestion){
							
						 TrainingSuggestion trainingSuggestion = TrainingSuggestion.findTrainingSueestionBasedOnTDAndRole(newTrainingDate.getId(),standardizedRole.getId());
						 
						 if(trainingSuggestion!=null){
							
							 training.persist();
							 trainingSuggestion.setTraining(training);
							 trainingSuggestion.persist();
							 return training;
						 }else{
							 return null;
						 }
					 }else{
						 training.persist();
						 return training;
					 }
			 }
			 
		 }catch (Exception e) {
			Log.error(e.getMessage(), e);
			return null;
		}
	 }
	 
	 public static List<Training> findAllTrainingsByTimeAsc(Long semId){
		 try{
			 
			EntityManager em = Training.entityManager();
			 
			String queryString="SELECT t FROM Training AS t WHERE t.trainingDate.trainingBlock.semester.id="+semId + " ORDER BY t.trainingDate.trainingDate,t.timeStart";
			
			TypedQuery<Training> query = em.createQuery(queryString, Training.class);
				
			 Log.info("Query : " + query.unwrap(Query.class));
			 
			 List<Training> resultList = query.getResultList();
		       
			 if(resultList==null || resultList.size()==0){
				
				 return null;
			 }else{
				 
				 return resultList;
			 }
			 
		 }catch (Exception e) {
			 Log.error(e.getMessage(), e);
				return null;
		}
	 }
	 
	 public static List<Training> findTrainingsOfGivenDate(Date currentlySelectedDate,Long semId){

		 Log.info("finding training of given date");
		 
		 try{
		 
			EntityManager em = Training.entityManager();
			 
			String queryString="SELECT t FROM Training AS t WHERE t.trainingDate.trainingDate = :traingDate AND t.trainingDate.trainingBlock.semester.id="+semId + " ORDER BY t.timeStart";
			
			TypedQuery<Training> query = em.createQuery(queryString, Training.class);
				
			query.setParameter("traingDate",currentlySelectedDate);
			
			 Log.info("Query : " + query.unwrap(Query.class));
			 
			 List<Training> resultList = query.getResultList();
		       
			 if(resultList==null || resultList.size()==0){
					
				 return null;
			 }else{
				 
				 return resultList;
			 }
				 
			 }catch (Exception e) {
				 Log.error(e.getMessage(), e);
					return null;
			}
	 }
	 
	 public static Training updateTraining(Date startTimeDate, Date endTimedate,Long roleId, Long semId, Long trainingId){
		 
		 Log.info("Updating Training of id : " + trainingId);
		 
		 try{
			
			 Training training =Training.findTraining(trainingId);
			 
			 //TrainingSuggestion ts= TrainingSuggestion.findTrainingSueestionBasedOnTrainingAndRoleId(training.getId(),training.getTrainingDate().getId(),training.getStandardizedRole().getId());
			 
			 //boolean isUpdatedForSuggestion=false;
			 //When update is clicked I can not find out whether this update is of normal scheduled training or of training that is associated with training suggestion
			 //so here when result is null then it is  normal training or it is associated training.  
			 /*if(ts!=null){
				 isUpdatedForSuggestion=true;
				 ts.setTraining(null);
				 ts.persist();
			 }*/
			 
			 StandardizedRole standardizedRole = StandardizedRole.findStandardizedRole(roleId);
			 
			 Date date = new Date(startTimeDate.getTime());
			 date.setHours(0);
			 date.setMinutes(0);
			 
			 Log.info("Training date is : " + date);
			 
			 int isAfterNoon;
			 if(startTimeDate.getHours() < 12){
				 isAfterNoon=0;
			 }else {
				 isAfterNoon=1;
			 }
			
			 TrainingDate trainingDate = TrainingDate.findTrainingDateBasedOnDateSemAndMorningValue(date, semId, isAfterNoon);
			 
			 if(trainingDate!=null){
				 Log.info("founded  training Date id is : " + trainingDate.getId());
				 
					 training.setStandardizedRole(standardizedRole);
					 training.setTimeEnd(endTimedate);
					 training.setTimeStart(startTimeDate);
					 training.setTrainingDate(trainingDate);
					 training.persist();
					 
					 /*if(isUpdatedForSuggestion){
						 //find new Training Block with this training will be associated.
						 TrainingSuggestion trainingSuggestion = TrainingSuggestion.findTrainingSueestionBasedOnTDAndRole(trainingDate.getId(),standardizedRole.getId());
						 if(trainingSuggestion!=null){
							 training.persist();
							 trainingSuggestion.setTraining(training);
							 trainingSuggestion.persist();
							 return training;
						 }else{
							 return null;
						 }
					 }else{
						 training.persist();
					 }*/
			 }

			 return training;
			 
		 }catch (Exception e) {
			Log.error(e.getMessage(), e);
			return null;
		}
	 }
	 
	 public static List<StandardizedRole> findAllRolesAssignInBlock(Date currentlySelectedDate, Long semId){
		 Log.info("fining all roles allready assigned in block");
		 
		 List<StandardizedRole> stanRolesList = new ArrayList<StandardizedRole>();
		 
		 DateTime dateTime= new DateTime(currentlySelectedDate);
		 
		 DateTime firstDayOfWeek =dateTime.withDayOfWeek( DateTimeConstants.MONDAY );
		 
		 TrainingBlock tBlock= TrainingBlock.findTrainingBlockForGivenDate(firstDayOfWeek.toDate(),semId);
		 
		 while (tBlock==null){
			 
			//I am using this logic instead of finding Training block that is last block of semester because it may possible that user split block in middle 
			 //so in that case I get wrong block reference so current logic is well even it is worst.
			 
			 firstDayOfWeek=firstDayOfWeek.minusDays(7);
			 
			 tBlock= TrainingBlock.findTrainingBlockForGivenDate(firstDayOfWeek.toDate(),semId);
		 
		 	/*if(firstDayOfWeek.getDayOfMonth()==1){
		 		break;
		 	}*/
		 }
	 
	 if(tBlock !=null){
			 
		 Set<TrainingDate> setTrainingDates= tBlock.getTrainingDates();
		
		 for(TrainingDate td : setTrainingDates){
		
			 Set<Training> setTrainings= td.getTrainings();
			
			 if(setTrainings!=null){
	
				 for(Training training : setTrainings){
					 stanRolesList.add(training.getStandardizedRole());
					 }
				 }
			 }
		 }
		 return stanRolesList;
	 }
	 
	 public static Boolean deleteTrainingOfGivenId(Long trainingId){
		 try{
			 
			 Training training =Training.findTraining(trainingId);
			 Set<TrainingSuggestion> setTrainingSuggestion= training.getTrainingSuggestions();
			 if(setTrainingSuggestion==null || setTrainingSuggestion.size()==0){
				 training.remove();
				 return true;
			 }else{
				 TrainingSuggestion trainingsuggestion = TrainingSuggestion.findTrainingSueestionBasedOnTDAndRole(training.getTrainingDate().getId(),training.getStandardizedRole().getId());
				 trainingsuggestion.setTraining(null);
				 trainingsuggestion.persist();
				 training.remove();
				 return true;
			 }
			 
		 }catch (Exception e) {
			 return null;
		}
	 }
	 
	 public static Training findIsTrainingOverLapsWithAnyTraining(Date startTimeDate,Date endTimedate, Long semId,Long roleId){
 
		 Log.info("finding training of given time");
		 
		 try{
		 
			EntityManager em = Training.entityManager();
			 
			String queryString="SELECT t FROM Training AS t WHERE t.timeStart BETWEEN :startDateTime AND :endDateTime OR t.timeEnd BETWEEN :startDateTime AND :endDateTime" +
					" AND t.trainingDate.trainingBlock.semester.id="+semId;
			
			TypedQuery<Training> query = em.createQuery(queryString, Training.class);
				
			query.setParameter("startDateTime",startTimeDate);
			
			query.setParameter("endDateTime",endTimedate);
			
			 Log.info("Query : " + query.unwrap(Query.class));
			 
			 List<Training> resultList = query.getResultList();
		       
			 if(resultList==null || resultList.size()==0){
					
				 return null;
			 }else{
				 
				 return resultList.get(0);
			 }
				 
			 }catch (Exception e) {
				 Log.error(e.getMessage(), e);
					return null;
			}
	 }
}

