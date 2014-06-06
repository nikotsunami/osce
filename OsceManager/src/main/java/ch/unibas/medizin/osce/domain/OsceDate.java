package ch.unibas.medizin.osce.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class OsceDate {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	 @Temporal(TemporalType.TIMESTAMP)
	 @DateTimeFormat(style = "M-")
	 private Date osceDate;
	 
	 
	 @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.PERSIST)
	 private Semester semester;

	 @ManyToMany(cascade = CascadeType.ALL, mappedBy = "osceDates")
	 private Set<PatientInSemester> patientInSemesters = new HashSet<PatientInSemester>();

	 private static Logger Log = Logger.getLogger(OsceDate.class);
	
	 public static String dateIsDefinedAsOSceOrTrainingDate(Long semesterID,Date dateOnWidget){
		
		 String result="";
		 
		 OsceDate osceDate=findOsceDateForGivenDate(dateOnWidget,semesterID);
		 
	        if (osceDate == null){
	        	result=TrainingDate.findTrainingDateBasedOnDateAndSemester(semesterID, dateOnWidget);
	        }else{
	        	result="OSCEDAY";
	        }
	           
		 return result;
		 
	 }
	 
	 public static Boolean persistThisDateAsOsceDate(Date currentlySelectedDate,Long semesterId){
		 boolean result=false;
		 try{
			 
			 Semester semester = Semester.findSemester(semesterId);
			 OsceDate osceDate = new OsceDate();
			 osceDate.setOsceDate(currentlySelectedDate);
			 osceDate.setSemester(semester);
			 osceDate.persist();
			 
			 result=true;
			 
		 }catch (Exception e) {
			Log.error(e.getMessage(), e);
			return null;
		}
		 return result;
	 }
	 public static Boolean removeOsceDateForGivenDate(Date currentlySelectedDate,Long semId){
		 Boolean result=false;
		 try{
			 OsceDate osceDate =findOsceDateForGivenDate(currentlySelectedDate,semId);
			 osceDate.remove();
			 result=true;
		 }catch (Exception e) {
			 Log.error(e.getMessage(), e);
			return null;
		}
		 return result;
	 }
	 
	 public static OsceDate findOsceDateForGivenDate(Date currentlySelectedDate,Long semId){
		 
		 EntityManager em = OsceDate.entityManager();
		 
		 String queryString="SELECT od FROM OsceDate AS od WHERE od.osceDate=:oscedate  AND od.semester.id="+semId ;
		
		 TypedQuery<OsceDate> query = em.createQuery(queryString, OsceDate.class);
			
		 query.setParameter("oscedate", currentlySelectedDate);
		 
		 Log.info("Query : " + query.unwrap(Query.class).getQueryString());
		 
		 List<OsceDate> resultList = query.getResultList();
		 
		 if (resultList == null || resultList.size() == 0){
			 return null;
		 }else{
			 return resultList.get(0);
		 }
	 }
	 
	 public static List<OsceDate> findOsceDatesFromGivenDateToEndOfMonth(Date date,Long semId){

		 DateTime dateTime = new DateTime(date);
		 
		 DateTime endOfMonth = dateTime.dayOfMonth().withMaximumValue();
				 
		 EntityManager em = TrainingDate.entityManager();
 
		String queryString="SELECT od FROM OsceDate AS od WHERE od.osceDate >= :startDate  AND od.osceDate <= :monthEndDate AND od.semester.id="+semId;
		
		TypedQuery<OsceDate> query = em.createQuery(queryString, OsceDate.class);
			
		 query.setParameter("startDate", date);
		 
		 query.setParameter("monthEndDate", endOfMonth.toDate());

		 Log.info("Query : " + query.unwrap(Query.class).getQueryString());
		 
		 List<OsceDate> resultList = query.getResultList();
	       
		 return resultList;
	 }
	 public static Boolean persistMultipleDateAsOsceDate(List<Date> currentlySelectedDatesList, Long semesterId){
		 Boolean result=null;
		 for(Date date : currentlySelectedDatesList){
			result=persistThisDateAsOsceDate(date, semesterId);
			if(result==null){
				return result;
				
			}
		 }
		 return result;
	 }
	 public static List<OsceDate> findOsceDateBasedOnSemOrderByDateDesc(Long semId){
		 try{
		 	EntityManager em = TrainingDate.entityManager();
		 
			String queryString="SELECT od FROM OsceDate AS od WHERE od.semester.id="+semId + " ORDER BY od.osceDate DESC";
			
			TypedQuery<OsceDate> query = em.createQuery(queryString, OsceDate.class);
				
			 Log.info("Query : " + query.unwrap(Query.class).getQueryString());
			 
			 List<OsceDate> resultList = query.getResultList();
		       
			 return resultList;
		 }catch (Exception e) {
			Log.error(e.getMessage(),e);
			return null;
		}
	 }
	 public static Boolean isThereAnyTrainingDateThatIsAfterOSceDate(Long semId){

		 try{
			 List<OsceDate> osceDateList = findOsceDateBasedOnSemOrderByDateDesc(semId);
			 
			 List<TrainingDate> trainingDateList = TrainingDate.findTrainingDateOfSemesterOrderByDateDesc(semId);
			 
			 if(osceDateList!=null && trainingDateList!=null){
				 TrainingDate tDate =trainingDateList.get(0);
				 OsceDate oscedate = osceDateList.get(0);
				 
				 if(tDate.getTrainingDate().after(oscedate.getOsceDate())){
					 return true;
				 }else{
					 return false;
				 }
			 }else{
				 return null;
			 }
		 }catch (Exception e) {
			 Log.error(e.getMessage(),e);
			 return null;
		 }
	}
}
