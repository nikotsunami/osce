package ch.unibas.medizin.osce.domain;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;


@Configurable
@Entity
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
		 
		 //deleting past suggestions
		 //For native query we need transaction that is why we create method with @Transaction annotation and call it as shown below.
		 TrainingSuggestion ts = new TrainingSuggestion();
		 ts.deletePastStuggestionOfSemester(semesterId);
		 
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
	 
	 @Transactional
	 private void deletePastStuggestionOfSemester(Long semesterId) {
		 
		 List<TrainingDate> allTrainingDatesOfSemester = TrainingDate.findAllTrainingDatesOfSemester(semesterId);
		 
		 String trainingDateIdsOfSem = getIdOfTrainingDate(allTrainingDatesOfSemester);
		
		Log.info("Deleting all past suggestions");
		
		EntityManager em = Semester.entityManager();
				
		StringBuilder sql = new StringBuilder("DELETE FROM `training_suggestion` WHERE `training_date` IN ( "+trainingDateIdsOfSem + " )");
				
		Log.info("Query is : " + sql);
		
		javax.persistence.Query query =  em.createNativeQuery(sql.toString());
				
		int totalEntryDeleted =query.executeUpdate();
				
		Log.info(totalEntryDeleted +" training suggestions are deleted from osce");
				
		 
	}

	 private static String getIdOfTrainingDate(List<TrainingDate> trainingDateList) {

		 Log.info("extracting ids from list");
			if (trainingDateList == null|| trainingDateList.size() == 0) {
				Log.info("Return as null");
				return "''";
			}
			Iterator<TrainingDate> trainingDatelistIterator = trainingDateList.iterator();
			StringBuilder trainingDateIds = new StringBuilder();
			//trainingDateIds.append(",");
			while (trainingDatelistIterator.hasNext()) {
				
				TrainingDate trainingDate = trainingDatelistIterator.next();

				trainingDateIds.append("'"+trainingDate.getId()+"'");
				if (trainingDatelistIterator.hasNext()) {
					trainingDateIds.append(" ,");
				}
			}
			
			return trainingDateIds.toString();
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

	public static List<TrainingSuggestion> findTrainingSuggestionFromGivenDateToEndOfMonthForSem(Date startDate,Long semId) {

		 Log.info("finding training suggestion from training date :"+ startDate +" to end of month Not assigned to training");
		 
			try{
				
				DateTime trainingSuggestionDateTime = new DateTime(startDate);
				 
				DateTime endOfMonth = trainingSuggestionDateTime.dayOfMonth().withMaximumValue();
				 
				 EntityManager em = TrainingSuggestion.entityManager();
				 
				 String queryString="SELECT ts FROM TrainingSuggestion AS ts WHERE ts.trainingDate.trainingDate >= :startDate  AND ts.trainingDate.trainingDate <= :monthEndDate" +
				 		" AND ts.trainingDate.trainingBlock.semester.id="+semId  +" AND ts.training IS NULL ORDER BY ts.trainingDate.trainingDate ASC";
				
				 TypedQuery<TrainingSuggestion> query = em.createQuery(queryString, TrainingSuggestion.class);
					
				 query.setParameter("startDate", startDate);
				 
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

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("StandardizedRole: ").append(getStandardizedRole()).append(", ");
        sb.append("Training: ").append(getTraining()).append(", ");
        sb.append("TrainingDate: ").append(getTrainingDate()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public TrainingDate getTrainingDate() {
        return this.trainingDate;
    }

	public void setTrainingDate(TrainingDate trainingDate) {
        this.trainingDate = trainingDate;
    }

	public StandardizedRole getStandardizedRole() {
        return this.standardizedRole;
    }

	public void setStandardizedRole(StandardizedRole standardizedRole) {
        this.standardizedRole = standardizedRole;
    }

	public Training getTraining() {
        return this.training;
    }

	public void setTraining(Training training) {
        this.training = training;
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
            TrainingSuggestion attached = TrainingSuggestion.findTrainingSuggestion(this.id);
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
    public TrainingSuggestion merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        TrainingSuggestion merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new TrainingSuggestion().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countTrainingSuggestions() {
        return entityManager().createQuery("SELECT COUNT(o) FROM TrainingSuggestion o", Long.class).getSingleResult();
    }

	public static List<TrainingSuggestion> findAllTrainingSuggestions() {
        return entityManager().createQuery("SELECT o FROM TrainingSuggestion o", TrainingSuggestion.class).getResultList();
    }

	public static TrainingSuggestion findTrainingSuggestion(Long id) {
        if (id == null) return null;
        return entityManager().find(TrainingSuggestion.class, id);
    }

	public static List<TrainingSuggestion> findTrainingSuggestionEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM TrainingSuggestion o", TrainingSuggestion.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}


