package ch.unibas.medizin.osce.domain;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.joda.time.DateTime;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;


@RooJavaBean
@RooToString
@RooEntity
public class TrainingSuggestion {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;

	 @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.PERSIST)
	 private TrainingDate trainingDate;
	 
	 @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.PERSIST)
	 private StandardizedRole standardizedRole;
	 
	 @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.PERSIST)
	 private Training training;
	 
	 
	 private static Logger Log = Logger.getLogger(Semester.class);
	 
	 public static Boolean createSuggestion(Long semesterId){
		
		 Log.info("Creating suggestion that is shown to user");
		 
		 try{
			 
			List<TrainingDate> allTrainingDateOfSem=TrainingDate.findAllTrainingDatesOfSemester(semesterId);
			
			if(allTrainingDateOfSem !=null && allTrainingDateOfSem.size()>0){
				
				for(TrainingDate td : allTrainingDateOfSem){
					
					Set<PatientInSemester> allPatientInSemWhoAcceptedThisTD=td.getPatientInSemesters();
					
					if(allPatientInSemWhoAcceptedThisTD!=null && allPatientInSemWhoAcceptedThisTD.size() > 0 ){
						
						List<PatientInRole> patientInRoleList = PatientInRole.findPatientInRoleBasedOnPatientInSemId(getIdOfPIS(allPatientInSemWhoAcceptedThisTD));
						
						if(patientInRoleList!=null && patientInRoleList.size() > 0){
							
							Log.info("creating traing suggestion");
							
							for(PatientInRole pir : patientInRoleList){
								TrainingSuggestion traingSuggestion =  new TrainingSuggestion();
								traingSuggestion.setStandardizedRole(pir.getOscePost().getStandardizedRole());
								traingSuggestion.setTrainingDate(td);
								traingSuggestion.persist();
							}
						}
					}
				}
			}
		
			return true;
			
		 }catch (Exception e) {
			 Log.error(e.getMessage(), e);
			 return null;
		}
		 
	 }
	 
	 private static String getIdOfPIS(Set<PatientInSemester> pisList) {

		/*	if (pisList == null|| pisList.size() == 0) {
				Log.info("Return as null");
				return "";
			}*/
			Iterator<PatientInSemester> splistIterator = pisList.iterator();
		
			StringBuilder spIds = new StringBuilder();
			//spIds.append(",");
			while (splistIterator.hasNext()) {
				
				PatientInSemester pis = splistIterator.next();

				spIds.append("'"+pis.getId()+"'");
				if (splistIterator.hasNext()) {
					spIds.append(" ,");
				}
			}
			
			return spIds.toString();
		}
	 
	 public static List<TrainingSuggestion> getSuggestionsFromGivenDate(Date date, Long semId){
		 
		 Log.info("finding suggestion that is shown to user");
		 
		try{
			
			 DateTime trainingSuggestionDateTime = new DateTime(date);
			 
			 DateTime endOfMonth = trainingSuggestionDateTime.dayOfMonth().withMaximumValue();
			 
			 EntityManager em = TrainingSuggestion.entityManager();
			 
			 String queryString="SELECT ts FROM TrainingSuggestion AS ts WHERE ts.trainingDate.trainingDate >= :startDate  AND ts.trainingDate.trainingDate <= :monthEndDate " +
			 		"AND ts.trainingDate.trainingBlock.semester.id="+semId + " ORDER BY ts.trainingDate.trainingDate,ts.standardizedRole.shortName";
			
			 TypedQuery<TrainingSuggestion> query = em.createQuery(queryString, TrainingSuggestion.class);
				
			 query.setParameter("startDate", date);
			 
			 query.setParameter("monthEndDate",endOfMonth.toDate());

			 Log.info("Query : " + query.unwrap(Query.class).getQueryString());
			 
			 List<TrainingSuggestion> resultList = query.getResultList();
		       
			 if (resultList == null || resultList.size() == 0){
				 return null;
		        }else{
		        	return resultList;
		       }
		}catch (Exception e) {
			Log.error(e.getMessage(), e);
			return null;
		}
	 }

	public static TrainingSuggestion findTrainingSueestionBasedOnTDAndRole(Long trainingDateId, Long standardizedRoleId) {

		 Log.info("finding TrainingSuggestion based on training date id :"+ trainingDateId +" and role id  : " + standardizedRoleId);
		 
			try{
				
				 EntityManager em = TrainingSuggestion.entityManager();
				 
				 String queryString="SELECT ts FROM TrainingSuggestion AS ts WHERE ts.trainingDate.id="+trainingDateId + " AND ts.standardizedRole.id="+standardizedRoleId;
				
				 TypedQuery<TrainingSuggestion> query = em.createQuery(queryString, TrainingSuggestion.class);
					
				 Log.info("Query : " + query.unwrap(Query.class).getQueryString());
				 
				 List<TrainingSuggestion> resultList = query.getResultList();
			       
				 if (resultList == null || resultList.size() == 0){
					 return null;
			        }else{
			        	return resultList.get(0);
			       }
			}catch (Exception e) {
				Log.error(e.getMessage(), e);
				return null;
			}
	}

	public static List<TrainingSuggestion> findTrainingSuggestionOfTDNotAssignedToTraining(Date trainingDate,Long semId) {

		 Log.info("finding training suggestion of training date :"+ trainingDate +" Not assigned to training");
		 
			try{
				
				 EntityManager em = TrainingSuggestion.entityManager();
				 
				 String queryString="SELECT ts FROM TrainingSuggestion AS ts WHERE ts.trainingDate.trainingDate = :trainingdate AND ts.trainingDate.trainingBlock.semester.id="+semId 
						 +" AND ts.training IS NULL";
				
				 TypedQuery<TrainingSuggestion> query = em.createQuery(queryString, TrainingSuggestion.class);
					
				 query.setParameter("trainingdate", trainingDate);
				 
				 Log.info("Query : " + query.unwrap(Query.class).getQueryString());
				 
				 List<TrainingSuggestion> resultList = query.getResultList();
			       
				 if (resultList == null || resultList.size() == 0){
					 return null;
			        }else{
			        	return resultList;
			       }
			}catch (Exception e) {
				Log.error(e.getMessage(), e);
				return null;
			}
	}

	public static TrainingSuggestion findTrainingSueestionBasedOnTrainingAndRoleId(Long trainingId,Long trainigDateId,Long roleId) {

		try{
			
			 EntityManager em = TrainingSuggestion.entityManager();
			 
			 String queryString="SELECT ts FROM TrainingSuggestion AS ts WHERE ts.trainingDate.id="+trainigDateId + " AND ts.training.id="+trainingId + " AND ts.standardizedRole.id="+roleId;
			
			 TypedQuery<TrainingSuggestion> query = em.createQuery(queryString, TrainingSuggestion.class);
				
			 Log.info("Query : " + query.unwrap(Query.class).getQueryString());
			 
			 List<TrainingSuggestion> resultList = query.getResultList();
		       
			 if (resultList == null || resultList.size() == 0){
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


