package ch.unibas.medizin.osce.domain;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.Version;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import ch.unibas.medizin.osce.server.ttgen.TimetableGenerator;
import ch.unibas.medizin.osce.shared.BreakType;
import ch.unibas.medizin.osce.shared.RingtoneTypes;

import com.csvreader.CsvWriter;


@Configurable
@Entity
public class OsceDay {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	private static Logger Log = Logger.getLogger(OsceDay.class);
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date osceDate;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date timeStart;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date timeEnd;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date lunchBreakStart;
	
	private Integer lunchBreakAfterRotation;

	@ManyToOne
	private Osce osce;
	
	@ManyToOne
	private Room reserveSPRoom;
	
	private Integer value=0;
	
	
	private Boolean isTimeSlotShifted=false;

	
   @OneToMany(cascade = CascadeType.ALL, mappedBy = "osceDay")
   private Set<Assignment> assignments = new HashSet<Assignment>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "osceDay")
	@OrderBy("label")
	private List<OsceSequence> osceSequences = new ArrayList<OsceSequence>();

	@ManyToMany(cascade = CascadeType.ALL,mappedBy = "osceDays")
	private Set<PatientInSemester> patientInSemesters = new HashSet<PatientInSemester>();
	
	//ttGen Change By Spec
	private String breakByRotation;
	//ttGen Change By Spec
	
	@Column(nullable=false, columnDefinition = "int default 0")
	private Integer lunchBreakAdjustedTime = 0;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "osceDay")
	@OrderBy("timeStart")
	private List<OsceDayRotation> osceDayRotations = new ArrayList<OsceDayRotation>();
	
	private Integer studentCount;
	
	private Integer spCount;
	
	private Integer roomCount;
	
	/**
	 * Sum up number of rotations of sequences belonging to this OSCE day
	 * @return
	 */
	public int totalNumberRotations() {
		int nrRotations = 0;
		
		Iterator<OsceSequence> it = getOsceSequences().iterator();
		while (it.hasNext()) {
			OsceSequence osceSequence = (OsceSequence) it.next();
			nrRotations += osceSequence.getNumberRotation();
		}
		
		return nrRotations;
	}
	
	/**
	 * Set lunch_break after a specified number of rotations.
	 * flag is used to know that shift lunch break earlier or later
	 * value for flag is : 1 for Earlier and 2 For Later
	 */
	public static Boolean updateLunchBreak(Long osceDayId, Integer afterRotation, Integer flag) {
    	try {
    		OsceDay osceDay = OsceDay.findOsceDay(osceDayId);
    		TimetableGenerator optGen = TimetableGenerator.getOptimalSolution(osceDay.getOsce());
    		Boolean test = optGen.updateLunchBreakAfterRotation(osceDayId, afterRotation, flag);
    		return test;
    	} catch(Exception e) {
    		e.printStackTrace();
    		return false;
    	}
    	
    }
	
	public static Boolean updateTimesAfterRotationShift(Long osceDayIdFrom, Long osceDayIdTo) {
    	try {
    		OsceDay osceDay = OsceDay.findOsceDay(osceDayIdFrom);
    		TimetableGenerator optGen = TimetableGenerator.getOptimalSolution(osceDay.getOsce());
        	optGen.updateTimesAfterRotationShift(osceDayIdFrom, osceDayIdTo);
    	} catch(Exception e) {
    		e.printStackTrace();
    		return false;
    	}
    	return true;
    }

	public static OsceDay findOsceDayByOsceDate(Date osceDate){
		if(osceDate == null){
			return null;
		}

		Calendar calBegin = Calendar.getInstance();
		calBegin.setTime(osceDate);
		calBegin.add(Calendar.MINUTE,-1);
		Date minDate = calBegin.getTime();
		
		Calendar calEnd = Calendar.getInstance();

		calEnd.setTime(osceDate);
		calEnd.add(Calendar.MINUTE,1);
		Date maxDate = calEnd.getTime();

		EntityManager em = entityManager();
		TypedQuery<OsceDay> query = em.createQuery("SELECT o FROM OsceDay AS o WHERE o.osceDate < :maxDate and o.osceDate > :minDate", OsceDay.class);
		query.setParameter("maxDate", maxDate);
		query.setParameter("minDate", minDate);
        List<OsceDay> resultList = query.getResultList();
        if (resultList == null || resultList.size() == 0)
            return null;
        return resultList.get(0);
	}
	
	// Module 3 d {


	public static List <StandardizedRole> findRoleForSPInSemester(Long patientInSemesterId,Long osceDayId){

		Log.info("In dise findRoleForSPInSemester to Retrieve Data in OsceDay.java");
		OsceDay osceDay = OsceDay.findOsceDay(osceDayId);
//		Log.info("OsceDay is " + osceDay.getId());
		Semester semester;
		StandardizedPatient standardizedPatient;
		PatientInSemester patientInSemester;
		OsceSequence osceSequence;
		OscePost oscePost;
		StandardizedRole standardizedRole;

		patientInSemester = PatientInSemester.findPatientInSemester(patientInSemesterId);
		standardizedPatient=patientInSemester.getStandardizedPatient();
//		Log.info("Standardized Patient is :" + standardizedPatient.getId());
		semester=patientInSemester.getSemester();

//		Log.info("Semester is :" + semester.getId());

		List<PatientInSemester> listOfPatientInSemester = new ArrayList<PatientInSemester>();
		List<StandardizedRole> listStandardizedRole = new ArrayList<StandardizedRole>();
		List<OsceSequence> setOsceSequence = osceDay.getOsceSequences();

		try{
		if(setOsceSequence != null || setOsceSequence.size() > 0){


			
			Iterator<OsceSequence> itOsceSequence = setOsceSequence.iterator();

			while(itOsceSequence.hasNext()){
				osceSequence = itOsceSequence.next();
				Log.info("Osce Sequence is :" + osceSequence.getId());

				List<OscePost> listOscePost = osceSequence.getOscePosts();

				if(listOscePost != null && listOscePost.size() > 0){

					Iterator<OscePost> itOscePost = listOscePost.iterator();

					while(itOscePost.hasNext()){
						oscePost= itOscePost.next();
//						Log.info("Osce Post is : " + oscePost.getId());

						standardizedRole=oscePost.getStandardizedRole();
//						Log.info("Satandardized Role is :" +standardizedRole.getId());

						if(standardizedRole != null){

							Set<AdvancedSearchCriteria> setAdvanceSearchCriteria = standardizedRole.getAdvancedSearchCriteria();
							Log.info("~~setAdvanceSearchCriteria is :" +setAdvanceSearchCriteria.size());


							if(setAdvanceSearchCriteria == null || setAdvanceSearchCriteria.size() == 0 ){
								listStandardizedRole.add(standardizedRole);
//								continue;
							}
							else{
								ArrayList<AdvancedSearchCriteria> listAdvanceSearchCirteria = new ArrayList<AdvancedSearchCriteria>(setAdvanceSearchCriteria);

								Log.info("Search Criteria Size : " +listAdvanceSearchCirteria.size());


								listOfPatientInSemester = PatientInSemester.findPatientInSemesterByAdvancedCriteria(semester.getId(),listAdvanceSearchCirteria);

								Log.info("!listOfPatientInSemester : " +listOfPatientInSemester.size());

								if(listOfPatientInSemester != null || listOfPatientInSemester.size() > 0 ){

									Iterator<PatientInSemester> itPatientInSemester = listOfPatientInSemester.iterator(); 
									PatientInSemester patientInSemester2;

									while(itPatientInSemester.hasNext()){

										 patientInSemester2 = itPatientInSemester.next();
										
										Log.info("sp  "+patientInSemester2.getStandardizedPatient().getId());
										if(standardizedPatient.getId().longValue() == patientInSemester2.getStandardizedPatient().getId().longValue()){

											Log.info("St Role Satisfies Advance Search Criteria is :" + standardizedRole.getId());
											Log.info("St Role Satisfies Advance Search Criteria is :" + standardizedRole.getShortName());
											listStandardizedRole.add(standardizedRole);
										}
										else
											Log.info("ROLE " + standardizedRole.getId() + " not satisfies Criteria ");
									}
								}  
							}
						}
					}
				}
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}

		Log.info("listStandardizedRole Size is :" +listStandardizedRole.size());
		
		return listStandardizedRole;
	}


	public static boolean findRoleAssignedInOsceDay(Long standardizedRoleId,Long osceDayId){

		Log.info("In dise findRoleAssignedInOsceDay to Retrieve Data in OsceDay.java");
		EntityManager em = entityManager();
		String queryString = "SELECT COUNT(od) FROM OsceDay  as od JOIN od.osceSequences as os JOIN os.oscePosts AS op JOIN op.standardizedRole as sr WHERE sr.id = " + standardizedRoleId + " and  od.id = " + osceDayId;
		TypedQuery<Long> q = em.createQuery(queryString,Long.class);
		Long  count =q.getSingleResult();
		Log.info("Query Execution Successful");
		if(count > 0)
			return true;
		else
			return false; 

	}

	// Module 3 d }

	//Module : 6 START
	public static java.util.List<Assignment> findOsceDayByDoctorAssignment(Doctor proxy)
	{
		Log.info("~~Inside OSCEDAY Method");
		EntityManager em = entityManager();
		Log.info("~QUERY findDoctorWithRoleTopic()");
		String queryString="select a from Assignment a  where a.examiner.id="+proxy.getId()+")" +"order by a.osceDay.id and a.timeStart";
		//String queryString = "Select d from OsceDay d";
		Log.info("~QUERY String: " + queryString);
		TypedQuery<Assignment> q = em.createQuery(queryString, Assignment.class);
		java.util.List<Assignment> result = q.getResultList();
		Log.info("~QUERY Result : " + result);
		return result;

	}
	//Module : 6 END

	// Module : 15
	public static List<OsceDay> findAllOsceDaysOrderByDate() {
		return entityManager().createQuery(
				"SELECT o FROM OsceDay o order by o.osceDate asc",
				OsceDay.class).getResultList();
	}

	// Module : 15
	
	/*public static List<OsceDay> findOsceDayByOsce(Long id)
	{
		EntityManager em = entityManager();
		String query = "SELECT o FROM OsceDay o where osce = " + id;
		TypedQuery<OsceDay> q = em.createQuery(query, OsceDay.class);
		return q.getResultList();
	}*/
	//Module 5 Bug Report Solution
	public static String schedulePostpone(OsceDay osceDayProxy) 
	{
		Log.info("~~SchedulePostPone.");			
		Log.info("~~Osce Day Id: " + osceDayProxy.getId());
		
		Long osceId=OsceDay.findOsceIdByOsceDayId(osceDayProxy.getId());
		//Log.info("Osce Id: " + osceId);

		// Get Last OsceDay to Check weather add New Day or Update rotation in Existing Days
		OsceDay lastOsceDay=OsceDay.findLastOsceDayByOsce(osceId);
		if(osceDayProxy.equals(lastOsceDay))
		{
			Log.info("~~~~Last Day Clicked................Add New Osce Day");
			// If Schedule Postpone for Last Day then Add New Osce Day [Mean it will Scheduled to Next Day.]
			String response=createNewOsceDayOsceSequenceAndCourse(osceDayProxy);
			return response;
		}
		else
		{
			Log.info("~~~~ Update Rotation................");
			Log.info("Osce Day Proxy: " + osceDayProxy.getId());
			String response=updateNextRotation(osceDayProxy);
			return response;
		}		
	}
	
	private static String updateNextRotation(OsceDay osceDayProxy) 
	{
		Long osceId=OsceDay.findOsceIdByOsceDayId(osceDayProxy.getId());
		List<OsceDay> osceDays=OsceDay.findOsceDayByOsce(osceId);
		Log.info("~~Total "+ osceDays.size()+" OsceDay for Osce " + osceId);
		Iterator<OsceDay> osceDayIterator=osceDays.iterator();
		OsceDay nextOsceDay = null;
		boolean flagUpdateExistingNumberOfRotation=false;
		while(osceDayIterator.hasNext())
		{
			OsceDay osceDay=osceDayIterator.next();
			if(osceDay.equals(osceDayProxy))
			{
				Log.info("Update Rotation for OsceDay" + osceDay.getId());
				List<OsceSequence> osceSequences=OsceSequence.findOsceSequenceByOsceDay(osceDay.getId());
				if(osceSequences.size()>0)
				{				
					// Sequence Iterate only if the Existing Sequence Number of Rotation is not Updated (Minus 1)	
					if(flagUpdateExistingNumberOfRotation==false)
					{							
						Iterator<OsceSequence> osceSequenceIterator=osceSequences.iterator();
						OsceSequence osceSequence=null;		
						while(osceSequenceIterator.hasNext())
						{							
							osceSequence=osceSequenceIterator.next();																		
						}
						if(osceSequence.getNumberRotation()==1)
						{
							Log.info("No Postpone is Allowed");											
							return "Rotation1";
						}
						else
						{
							osceSequence.setNumberRotation((osceSequence.getNumberRotation()-1));
							osceSequence.persist();
							flagUpdateExistingNumberOfRotation=true;
						}
					}
				}
			}
			else if(flagUpdateExistingNumberOfRotation==true)
			{
				osceDay.getOsceSequences().get(0).setNumberRotation((osceDay.getOsceSequences().get(0).getNumberRotation()+1));
				osceDay.getOsceSequences().get(0).persist();
				nextOsceDay = osceDay;
				break;
			}
		}
		try{			
			if(nextOsceDay !=null )
				updateTimesAfterRotationShift(osceDayProxy.getId(),nextOsceDay.getId());
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return "UpdateSuccessful";
	}

	private static String createNewOsceDayOsceSequenceAndCourse(OsceDay osceDayProxy) 
	{
		//List<OsceDay> osceDays=OsceDay.findOsceDayByOsce(osceId);
		//Log.info("~~Total "+ osceDays.size()+" OsceDay for Osce " + osceId);
		//if(osceDays.size()>0)
		//{						
			//Iterator<OsceDay> osceDayIterator=osceDays.iterator();
			//while(osceDayIterator.hasNext())
			//{
				//OsceDay osceDay=osceDayIterator.next();
				List<OsceSequence> osceSequences=OsceSequence.findOsceSequenceByOsceDay(osceDayProxy.getId());
				Log.info("~~Total "+ osceSequences.size() + " Osce Sequence for Osce Day" + osceDayProxy.getId());
				OsceSequence newOsceSequence;
				OscePost newOscePost;
				Course newCourse;
				if(osceSequences.size()>0)
				{				
						Log.info("New Osce Day Add");						
						Iterator<OsceSequence> osceSequenceIterator=osceSequences.iterator();
						OsceSequence osceSequence=null;
						while(osceSequenceIterator.hasNext())
						{
							osceSequence=osceSequenceIterator.next();
						}
						if(osceSequence.getNumberRotation()==1)
						{
							Log.info("No Postpone is Allowed");							
							return "Rotation1";
						}
						osceSequence.setNumberRotation((osceSequence.getNumberRotation()-1));
						
						OsceDay newOsceday=new OsceDay();
						newOsceday.lunchBreakStart=osceDayProxy.getLunchBreakStart();
						newOsceday.osceDate=osceDayProxy.getOsceDate();
						newOsceday.timeEnd=osceDayProxy.getTimeEnd();
						newOsceday.timeStart=osceDayProxy.getTimeStart();
						newOsceday.value=osceDayProxy.getValue();
						newOsceday.osce=osceDayProxy.getOsce();
						newOsceday.lunchBreakAfterRotation=osceDayProxy.getLunchBreakAfterRotation();
						newOsceday.persist();
						
						newOsceSequence=new OsceSequence();
						newOsceSequence.setLabel(osceSequence.getLabel());
						newOsceSequence.setNumberRotation(1);
						newOsceSequence.setOsceDay(newOsceday);
						newOsceSequence.persist();
						
						List<OscePost> oscePosts=OscePost.findOscePostByOsceSequence(osceSequence.getId());
						if(oscePosts.size()>0)
						{
							Iterator<OscePost> oscePostIterator=oscePosts.iterator();
							while(oscePostIterator.hasNext())
							{
								OscePost oscePost=oscePostIterator.next();
								newOscePost=new OscePost();
								newOscePost.setSequenceNumber(oscePost.getSequenceNumber());
								newOscePost.setOscePostBlueprint(oscePost.getOscePostBlueprint());
								newOscePost.setOsceSequence(newOsceSequence);
								newOscePost.setStandardizedRole(oscePost.getStandardizedRole());
								newOscePost.persist();								
							}
						}
						
						List<Course> courses=Course.findCourseByOsceSequence(osceSequence.getId());
						if(courses.size()>0)
						{
							Iterator<Course> courseIterator=courses.iterator();
							while(courseIterator.hasNext())
							{
								Course course=courseIterator.next();
								newCourse=new Course();
								newCourse.setColor(course.getColor());
								newCourse.setOsce(course.getOsce());
								newCourse.setOsceSequence(newOsceSequence);
								newCourse.persist();								
							}
						}		
						
						// Add OscePostRoom
						List<OscePost> oldOscePosts=OscePost.findOscePostByOsceSequence(osceSequence.getId());
						List<Course> oldCourses=Course.findCourseByOsceSequence(osceSequence.getId());
						
						List<OscePost> oscePostsListforOscePostRoom=OscePost.findOscePostByOsceSequence(newOsceSequence.getId());
						List<Course> coursesforOscePostRoom=Course.findCourseByOsceSequence(newOsceSequence.getId());
						
						if(oscePostsListforOscePostRoom.size()>0 && coursesforOscePostRoom.size()>0)							
						{
							Iterator<Course> courseIterator=coursesforOscePostRoom.iterator();
							Iterator<Course> oldCourseIterator=oldCourses.iterator();
							
							while(courseIterator.hasNext() && oldCourseIterator.hasNext())
							{
								Iterator<OscePost> oscePostIterator=oscePostsListforOscePostRoom.iterator();
								Iterator<OscePost> oldOscePostIterator=oldOscePosts.iterator();
								
								Course course=courseIterator.next();
								Course oldCourse=oldCourseIterator.next();
								
								while(oscePostIterator.hasNext() && oldOscePostIterator.hasNext())
								{
									OscePost oscePost=oscePostIterator.next();
									OscePost oldOscePost=oldOscePostIterator.next();
									
									OscePostRoom opr=OscePostRoom.findOscePostRoomByOscePostAndCourse(oldCourse, oldOscePost);
																		
									System.out.println("Course: " + course + "Post: " + oscePost);
									OscePostRoom newOscePostRoom=new OscePostRoom();
									newOscePostRoom.setCourse(course);
									newOscePostRoom.setOscePost(oscePost);
									if(opr!=null)
									{
										newOscePostRoom.setRoom(opr.getRoom());
									}
									newOscePostRoom.persist();
								}
							}
						}
						
						try{					
							updateTimesAfterRotationShift(osceDayProxy.getId(),newOsceday.getId());
						}catch(Exception e)
						{
							e.printStackTrace();
				}					
				}
				
				
			//}
		//}
		return "CreateSuccessful";
	}

	public static java.util.List<OsceDay> findOsceDayByOsce(Long osce)
	{
		//Log.info("~~Inside findOsceDayByOsce Method");
		EntityManager em = entityManager();	
		String queryString="select d from OsceDay d where d.osce= "+osce;
		Log.info("~QUERY String: " + queryString);
		TypedQuery<OsceDay> q = em.createQuery(queryString, OsceDay.class);
		java.util.List<OsceDay> result = q.getResultList();
		//Log.info("~QUERY Result : " + result);
		return result;
	}
	public static Long findOsceIdByOsceDayId(Long osceDayId)
	{
		EntityManager em = entityManager();	
		String queryString="select d.osce.id from OsceDay d where d.id= "+osceDayId;
		Log.info("~QUERY String: " + queryString);
		TypedQuery<Long> q = em.createQuery(queryString, Long.class);
		Long result = q.getSingleResult();
		return result;
	}
	public static OsceDay findLastOsceDayByOsce(Long osceId)
	{		
		EntityManager em = entityManager();	
		String queryString="select d from OsceDay d where d.osce= "+osceId+ " order by d.id desc";
		Log.info("~QUERY String: " + queryString);
		TypedQuery<OsceDay> q = em.createQuery(queryString, OsceDay.class);
		List<OsceDay> result = q.getResultList();
		//Log.info("~QUERY Result : " + result);
		return result.get(0);
	}
	public static OsceDay findFirstOsceDayByOsce(Long osceId)
	{		
		EntityManager em = entityManager();	
		String queryString="select d from OsceDay d where d.osce= "+osceId;
		Log.info("~QUERY String: " + queryString);
		TypedQuery<OsceDay> q = em.createQuery(queryString, OsceDay.class);
		List<OsceDay> result = q.getResultList();
		//Log.info("~QUERY Result : " + result);
		return result.get(0);
	}
	public static java.util.List<OsceDay> findDescOsceDayByOsce(Long osce)
	{
		//Log.info("~~Inside findOsceDayByOsce Method");
		EntityManager em = entityManager();	
		String queryString="select d from OsceDay d where d.osce= "+osce+" order by d.id desc";
		Log.info("~QUERY String: " + queryString);
		TypedQuery<OsceDay> q = em.createQuery(queryString, OsceDay.class);
		java.util.List<OsceDay> result = q.getResultList();
		//Log.info("~QUERY Result : " + result);
		return result;
	}

	public static String scheduleEarlier(OsceDay osceDayProxy) 
	{
		Log.info("~~scheduleEarlier.");			
		Log.info("~~Osce Day Id: " + osceDayProxy.getId());
		
		Long osceId=OsceDay.findOsceIdByOsceDayId(osceDayProxy.getId());
		OsceDay firstOsceDay=OsceDay.findFirstOsceDayByOsce(osceId);
		// Get Last OsceDay to Check weather add New Day or Update rotation in Existing Days
		
		if(osceDayProxy.equals(firstOsceDay))
		{
			Log.info("~~~~First Day Clicked................Add New Osce Day");
			Log.info("No Schedule Earlier Possible");		
			return "FirstDay";			
		}
		else
		{
			Log.info("~~~~ Update Rotation................");
			Log.info("Osce Day Proxy: " + osceDayProxy.getId());
			String response=updatePreviousRotation(osceDayProxy);
			return response;
		}
	}
	private static String updatePreviousRotation(OsceDay osceDayProxy) 
	{	
		Long osceId=OsceDay.findOsceIdByOsceDayId(osceDayProxy.getId());
		List<OsceDay> osceDays=OsceDay.findDescOsceDayByOsce(osceId);
		
		Log.info("~~Total "+ osceDays.size()+" OsceDay for Osce " + osceId);
		boolean flagUpdateExistingNumberOfRotation=false;
		for(int i=0;i<osceDays.size();i++)
		{
			OsceDay osceDay=osceDays.get(i);	
			if(osceDay.equals(osceDayProxy))
			{
				List<OsceSequence> osceSequences=OsceSequence.findOsceSequenceByOsceDay(osceDay.getId());
				if(osceSequences.size()>0)
				{	
					if(osceSequences.get(0).getNumberRotation()==1)
					{
						if(osceSequences.size()>1)
						{
							// More Than one Osce So No Need To Remove  Day
							Log.info("More Than Two Sequence");
							return "Rotation1";
						}
						else
						{
							// Only One Sequence So Need to Remove Corse Osce_Post and Day
							Log.info("Only One Sequence");
							OsceDay previousOsceDay=osceDays.get(i+1);
							List<OsceSequence> previousDaySequences=OsceSequence.findOsceSequenceByOsceDay(previousOsceDay.getId());							
							if(previousDaySequences.size()>0)
							{	
								Iterator<OsceSequence> osceSeqIterator=previousDaySequences.iterator();
								OsceSequence updateOsceSequence=null;
								while(osceSeqIterator.hasNext())
								{
									updateOsceSequence=osceSeqIterator.next();
								}
								updateOsceSequence.setNumberRotation((updateOsceSequence.getNumberRotation()+1));
								String response=removeOsceSequenceCourseDay(osceSequences.get(0),osceDay,updateOsceSequence);							
								if(response.compareToIgnoreCase("SuccessfullyPreponeWithDelete")==0)
								{
									Log.info("Update Osce Sequence Rotation Successfully.");
									updateOsceSequence.persist();
									return response;
								}
								return "Rotation1";
								//break;
							}							
						}
					}
					else
					{
						Log.info("Go To Else.");
						osceSequences.get(0).setNumberRotation((osceSequences.get(0).getNumberRotation()-1));
						osceSequences.get(0).persist();
						flagUpdateExistingNumberOfRotation=true;						
					}
				}
			}
			else if(flagUpdateExistingNumberOfRotation==true)
			{
				List<OsceSequence> osceSequences=OsceSequence.findOsceSequenceByOsceDay(osceDay.getId());
				if(osceSequences.size()>0)
				{	
					Iterator<OsceSequence> osceSequenceIterator=osceSequences.iterator();
					OsceSequence osceSequence=null;
					while(osceSequenceIterator.hasNext())
					{						
						osceSequence=osceSequenceIterator.next();						
					}
					osceSequence.setNumberRotation((osceSequence.getNumberRotation()+1));
					osceSequence.persist();					
					return "SuccessfulPrepond";
					//break;
				}
			}
		}	
		return "SuccessfulPrepond";
	}
		
	private static String removeOsceSequenceCourseDay(OsceSequence osceSequence,OsceDay osceDay,OsceSequence updateOsceSequence) 
	{
		Log.info("Call removeOsceSequenceCourseDay for Sequence" +osceSequence.getId());			
		try{
			osceDay.remove();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	/*	List<OscePost> oscePosts=OscePost.findOscePostByOsceSequence(osceSequence.getId());
		if(oscePosts.size()>0)
		{
			Iterator<OscePost> oscePostIterator=oscePosts.iterator();
			//OscePost tempOscePost=null;
			while(oscePostIterator.hasNext())
			{
				Log.info("Go To Loop.");
				OscePost oscePost=oscePostIterator.next();		
				//tempOscePost=oscePost;
				Log.info("Osce Post to Remove: " + oscePost.getId());
				Log.info("1");
				List<OscePostRoom> oscePostRoom=OscePostRoom.findOscePostRoomsByOscePost(oscePost).getResultList();
				Log.info("2");
				if(oscePostRoom.size()>0)
				{
					Iterator<OscePostRoom> oscePostRoomIterator=oscePostRoom.iterator();
					while(oscePostRoomIterator.hasNext())
					{
						OscePostRoom removeOscePostRoom=oscePostRoomIterator.next();
						removeOscePostRoom.remove();
						Log.info("3");
					}										
				}
				
				Log.info("4");
				Log.info("Osce Post to Remove: " + oscePost.getId());
				//osceSequence.getOscePosts().remove(oscePost);
				Log.info("5");					
				oscePost.remove();						
				Log.info("6");
			}
		}
		Log.info("Remove Corse");
		List<Course> courses=Course.findCourseByOsceSequence(osceSequence.getId());
		if(courses.size()>0)
		{
			Iterator<Course> corseIterator=courses.iterator();
			while(corseIterator.hasNext())
			{
				Course removeCorse=corseIterator.next();
				osceSequence.getCourses().remove(removeCorse);
				removeCorse.remove();				
			}
		}
		osceSequence.remove();
		Log.info("Remove Osce Sequence");
		osceDay.remove();
		Log.info("Remove Osce Day");
	// Write Logic to Remove Osce Day and Osce Sequence	
	 */
		return "SuccessfullyPreponeWithDelete";
	}
	//E Module 5 Bug Report Solution

// TestCasePurpose Method start {
public static List<OsceDay> findOSceDaysForAnOsce(Long osceId){
	
			Log.info("Inside findOSceDaysForAnOsce with Osce Is : " + osceId);
			EntityManager em = entityManager();
			String query="select o from OsceDay as o where o.osce="+osceId;
			TypedQuery<OsceDay> q = em.createQuery(query, OsceDay.class);
			Log.info("Query String: " + query);
			return q.getResultList();
	}
	// TestCasePurpose Method start }


public static List<OsceDay> findOSceDaysForAnOsceId(Long osceId){
	
	Log.info("Inside findOSceDaysForAnOsce with Osce Is : " + osceId);
	EntityManager em = entityManager();
	String query="select o from OsceDay as o where o.osce="+osceId;
	TypedQuery<OsceDay> q = em.createQuery(query, OsceDay.class);
	Log.info("Query String: " + query);
	return q.getResultList();
}


public static Boolean updateRotation(Long osceDayId, Integer rotation) {
	try {
		
		OsceDay osceDay = OsceDay.findOsceDay(osceDayId);
		List<OsceSequence> listOsceSequence = osceDay.getOsceSequences();
		if(listOsceSequence!=null && listOsceSequence.size()==2)
		{
			OsceSequence firstOsceSequence = listOsceSequence.get(0);
			OsceSequence secondOsceSequence = listOsceSequence.get(1);
			firstOsceSequence.setNumberRotation(firstOsceSequence.getNumberRotation() - rotation);
			secondOsceSequence.setNumberRotation(secondOsceSequence.getNumberRotation() + rotation);
			
			firstOsceSequence.persist();
			secondOsceSequence.persist();
		}
		
	} catch(Exception e) {
		e.printStackTrace();
		return false;
	}
	return true;
}

	//spec[
	public int simpatAssignmentSlots() {
	    List<Assignment> assignments = Assignment.retrieveAssignmentsOfTypeSPByOsceDay(this);
	    if (assignments.get(0) != null) {
	        Assignment assignment = assignments.get(0);
	        long secs = (assignment.getTimeEnd().getTime() - assignment.getTimeStart().getTime()) / 1000;
	        int assignmentMinutes = (int) (secs / 60);
	        int assignmentSlots = assignmentMinutes / (this.osce.getPostLength() + 1);
	        return assignmentSlots;
	    }
	    return 0;
	}
	//Added for OMS-161.
	public int emptySimpatAssignmentSlots() {
	    List<Assignment> assignments = Assignment.retrieveEmptyAssignmentsOfTypeSPByOsceDay(this);
	    if (assignments.size()>0 && assignments.get(0) != null) {
	        Assignment assignment = assignments.get(0);
	        long secs = (assignment.getTimeEnd().getTime() - assignment.getTimeStart().getTime()) / 1000;
	        int assignmentMinutes = (int) (secs / 60);
	        int assignmentSlots = assignmentMinutes / (this.osce.getPostLength() + 1);
	        return assignmentSlots;
	    }
	    return 0;
	}
	//spec]
	
	public static List<OsceDay> findOsceDayBySemester(Long semesterId)
	{
		EntityManager em = entityManager();
		String sql = "SELECT od FROM OsceDay od WHERE od.osce.semester.id = " + semesterId + " ORDER BY od.osceDate";
		TypedQuery<OsceDay> query = em.createQuery(sql, OsceDay.class);
		return query.getResultList();
	}
	
	public static void exportCsvClicked(OutputStream os, Long osceDayId, int startTone, int endTone, int prePostEndTime, int prePostEndTone, int preBreakEndTime, int preBreakEndTone, int plusTime)
	{
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		try
		{
			OsceDay osceDay = OsceDay.findOsceDay(osceDayId);
			
			Integer longBreak = null;
			Integer lunchBreak = null;
			Integer middleBreak = null;
			if(osceDay.getOsce() != null) {
				longBreak = osceDay.getOsce().getLongBreak() == null ? null : osceDay.getOsce().getLongBreak().intValue();
				lunchBreak = osceDay.getOsce().getLunchBreak() == null ? null : osceDay.getOsce().getLunchBreak().intValue();
				middleBreak = osceDay.getOsce().getMiddleBreak() == null ? null : osceDay.getOsce().getMiddleBreak().intValue();
			}
			
			List<Assignment> assList = Assignment.findAssignmentByOsceDay(osceDayId);
			CsvWriter writer = new CsvWriter(os, ',', Charset.forName("ISO-8859-1"));

			writer.write("Time");
			writer.write("Tone");
			writer.write("isShow"); //always 1 its fix
			writer.write("Type");
			writer.endRecord();
			
			DateTime lastEndTime = null;
			Integer currentRoatation;
			Integer previousRoatation = 0;
			
			if (assList.size() == 0)
				return;
			
			if (assList.size() > 0)
			{
				lastEndTime = new DateTime(assList.get(0).getTimeEnd());
				previousRoatation = assList.get(0).getRotationNumber();
			}
			
			List<Schedule> scheduleList = new ArrayList<Schedule>();
			
			/* rotation map with break true*/
			Map<Integer,Boolean> rotationBreak = getRotationToBreakBoolean(osceDay.getBreakByRotation(),longBreak,lunchBreak,middleBreak);
			
			
			for (Assignment assignment : assList)
			{
				DateTime currentEndTime =  new DateTime(assignment.getTimeEnd());
				DateTime currentStartTime = new DateTime(assignment.getTimeStart());
				
				currentRoatation = assignment.getRotationNumber();
				
				/* for post begin */
				{
					Schedule schedule = new Schedule();
					schedule.setTime(currentStartTime.plusMinutes(plusTime));
					schedule.setTone(String.valueOf(startTone));
					schedule.setIsShow("1");
					schedule.setType(RingtoneTypes.START);
					scheduleList.add(schedule);
				}
				
				/* For before post end*/
				if (prePostEndTime > 0 && prePostEndTone > 0)
				{
					Schedule schedule = new Schedule();
					DateTime tempCurrEndTime = currentEndTime.plusMinutes(plusTime);
					schedule.setTime(tempCurrEndTime.minusMinutes(prePostEndTime));
					schedule.setTone(String.valueOf(prePostEndTone));
					schedule.setIsShow("1");
					schedule.setType(RingtoneTypes.PRE_END);
					scheduleList.add(schedule);
				}
				
				/* For before post break*/
				if (preBreakEndTime > 0 && preBreakEndTone > 0) 
				{
					if(isLunchOrLongOrMiddleBreak(lastEndTime,currentStartTime,lunchBreak,longBreak,middleBreak,previousRoatation,(!currentRoatation.equals(previousRoatation)),rotationBreak) == true)
					{
						Schedule schedule = new Schedule();
						DateTime tempCurrStTime = currentStartTime.plusMinutes(plusTime);
						schedule.setTime(tempCurrStTime.minusMinutes(preBreakEndTime));
						schedule.setTone(String.valueOf(preBreakEndTone));
						schedule.setIsShow("1");
						schedule.setType(RingtoneTypes.PRE_BREAK);
						scheduleList.add(schedule);
					}					
				}
				
				/* for post end */
				{
					Schedule schedule = new Schedule();
					DateTime endTime = currentEndTime.plusMinutes(plusTime);
					schedule.setTime(endTime);
					schedule.setTone(String.valueOf(endTone));
					schedule.setIsShow("1");
					schedule.setType(RingtoneTypes.END);
					scheduleList.add(schedule);
				}
				
				lastEndTime = new DateTime(assignment.getTimeEnd());
				previousRoatation = assignment.getRotationNumber();
			}			
			
			Collections.sort(scheduleList, new Comparator<Schedule>() {

				@Override
				public int compare(Schedule o1, Schedule o2) {
					return o1.getTime().compareTo(o2.getTime());
				}
			});
			
			for (Schedule schedule : scheduleList)
			{
				writer.write(dateFormat.format(schedule.getTime().toDate()));
				writer.write(schedule.getTone());
				writer.write(schedule.getIsShow());
				writer.write(String.valueOf(schedule.getType().ordinal()));
				writer.endRecord();		
			}
			
			writer.close();			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static boolean isLunchOrLongOrMiddleBreak(DateTime lastEndTime, DateTime currentStartTime, Integer lunchBreak, Integer longBreak, Integer middleBreak, Integer previousRotation, boolean isRotationChanged, Map<Integer, Boolean> rotationBreak) {
		
		if(lastEndTime == null || currentStartTime == null) {
			return false;
		}
		
		if(lunchBreak == null && longBreak == null /*&& middleBreak == null*/) {
			return false;
		}
		
		if(rotationBreak != null && rotationBreak.isEmpty() == false && isRotationChanged == true) {
			Boolean bool = rotationBreak.get(previousRotation);
			if(bool != null){
				return bool;
			}
		}
		
		Duration duration = new Duration(currentStartTime,lastEndTime);
		
		
		if(lunchBreak != null && lunchBreak == Math.abs(duration.getStandardSeconds()/60)) {
			return true;
		}
		
		if(longBreak != null && longBreak == Math.abs(duration.getStandardSeconds()/60)) {
			return true;
		}
		
		/*if(middleBreak != null && middleBreak == Math.abs(duration.getStandardMinutes()) && isRotationChanged == true) {
			return true;
		}*/
		
		/*if(middleBreak != null && Math.abs((duration.getStandardSeconds() / 60)) > middleBreak) {
			return true;
		}*/

		 
		return false;
	}
	
	private static Map<Integer, Boolean> getRotationToBreakBoolean(String breakByRotation, Integer longBreak, Integer lunchBreak, Integer middleBreak) {
		
		Map<Integer,Boolean> rotationBreak = new HashMap<Integer, Boolean>();
		
		if(StringUtils.isBlank(breakByRotation)) {
			return rotationBreak;
		}
		
		List<String> rotationList = Arrays.asList(breakByRotation.split("-"));
		
		for (String rotation : rotationList) {
			List<String> currentRotation = Arrays.asList(rotation.split(":"));
			
			if(currentRotation.size() == 2) {
				Integer rotationNumber = Integer.parseInt(currentRotation.get(0));
				Integer breakTime = Integer.parseInt(currentRotation.get(1));
				
				if(lunchBreak != null && breakTime == lunchBreak) {
					rotationBreak.put(rotationNumber, Boolean.TRUE);
				}
				
				if(longBreak != null && breakTime == longBreak) {
					rotationBreak.put(rotationNumber, Boolean.TRUE);
				}
				
				if(middleBreak != null && breakTime == middleBreak) {
					rotationBreak.put(rotationNumber, Boolean.FALSE);
				}
			}
		}
		
		return rotationBreak;
	}
	
	public static Osce createOsceDaySequenceCourseAndOscePost(Long osceId)
	{
		//create a new day with one sequence and one parcour and copy all osce post from above osce day.
		Osce osce = Osce.findOsce(osceId);
		
		List<OsceDay> osceDayList = osce.getOsce_days();
		OsceDay lastOsceDay = osceDayList.get(osceDayList.size() - 1);
		
		//create new osce day
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(lastOsceDay.getOsceDate());
		calendar.add(Calendar.DATE, 1);
		calendar.add(Calendar.HOUR, 0);
		calendar.add(Calendar.MINUTE, 0);
		calendar.add(Calendar.SECOND, 0);
		Date newOsceDate = calendar.getTime();
		
 		calendar.setTime(lastOsceDay.getTimeStart());
 		calendar.add(Calendar.DATE, 1);
 		Date newTimeStart = calendar.getTime(); 		
 		
		OsceDay osceDay = new OsceDay();
		osceDay.setOsceDate(newOsceDate);
		osceDay.setTimeStart(newTimeStart);
		osceDay.setTimeEnd(newTimeStart);
		osceDay.setOsce(osce);
		osceDay.persist();
		
		List<OsceSequence> osceSeqList = new ArrayList<OsceSequence>();		
		OsceSequence oldOsceSeq = lastOsceDay.getOsceSequences().get(0);
		
		//create new osce sequence		
		OsceSequence osceSequence = new OsceSequence();
		osceSequence.setLabel(oldOsceSeq.getLabel());
		osceSequence.setNumberRotation(1);
		osceSequence.setOsceDay(osceDay);
		osceSequence.persist();
		
		Map<Long, OscePost> oscePostMap = new HashMap<Long, OscePost>();
		for (OscePost oscePost : oldOsceSeq.getOscePosts())
 		{
 			OscePost newOscePost = new OscePost();
 			newOscePost.setSequenceNumber(oscePost.getSequenceNumber());
 			newOscePost.setValue(oscePost.getValue());
 			newOscePost.setOscePostBlueprint(oscePost.getOscePostBlueprint());
 			newOscePost.setOsceSequence(osceSequence);
 			newOscePost.setStandardizedRole(oscePost.getStandardizedRole());
 			newOscePost.persist();
 			oscePostMap.put(oscePost.getId(), newOscePost);
 		}
		
		//create new course
		List<Course> courseList = new ArrayList<Course>();
		Course oldCourse = oldOsceSeq.getCourses().get(0);
		
		Course course = new Course();
		course.setColor(oldCourse.getColor());
		course.setOsce(osce);
		course.setOsceSequence(osceSequence);
		course.persist();
		courseList.add(course);
		
		//create OscePostRoom
		for (OscePostRoom oscePostRoom : oldCourse.getOscePostRooms())
		{
			OscePostRoom newOscePostRoom = new OscePostRoom();
			newOscePostRoom.setCourse(course);
			newOscePostRoom.setOscePost(oscePostMap.get(oscePostRoom.getOscePost().getId()));
			newOscePostRoom.setRoom(oscePostRoom.getRoom());
			newOscePostRoom.persist();
		}
		
		osceSequence.setCourses(courseList);		
		osceSeqList.add(osceSequence);
		osceDay.setOsceSequences(osceSeqList);		
		osceDayList.add(osceDay);
		osce.setOsce_days(osceDayList);
		
		return osce;
	}
	
	public static void removeOsceDay(Long osceDayId)
	{
		OsceDay osceDay = OsceDay.findOsceDay(osceDayId);
		osceDay.remove();
	}
	
	public static void manualOsceAddRotation(Long osceSeqId)
	{
		OsceSequence osceSeq = OsceSequence.findOsceSequence(osceSeqId);
		OsceDay osceDay = osceSeq.getOsceDay();
		Osce osce = osceDay.getOsce();
		List<OsceDay> osceDayList = osce.getOsce_days();
		List<OsceSequence> osceSeqList = osceDay.getOsceSequences();
		
		if (osceSeqList.size() == 2)
		{
			OsceSequence firstOsceSeq = osceSeqList.get(0);
			OsceSequence secondOsceSeq = osceSeqList.get(1);
			
			if (firstOsceSeq.getId().equals(osceSeqId))
			{
				List<OsceDayRotation> firstOsceSeqRotationList = firstOsceSeq.getOsceDayRotations();
				List<OsceDayRotation> secondOsceSeqRotationList = secondOsceSeq.getOsceDayRotations();
				
				if (firstOsceSeqRotationList.isEmpty() == false && secondOsceSeqRotationList.isEmpty() == false)
				{
					OsceDayRotation firstSeqLastRot = firstOsceSeqRotationList.get((firstOsceSeqRotationList.size() - 1));
					OsceDayRotation secondSeqFirstRot = secondOsceSeqRotationList.get(0);
					
					Integer firstSeqRotBreak = firstSeqLastRot.getBreakDuration();
					Integer secondSeqRotBreak = secondSeqFirstRot.getBreakDuration();
					
					BreakType firstSeqRotBreakType = firstSeqLastRot.getBreakType();
					BreakType secondSeqRotBreakType = secondSeqFirstRot.getBreakType();
					
					Integer diff = firstSeqRotBreak - secondSeqRotBreak;
					
					Date rotTimeStart = dateSubtractMin(secondSeqFirstRot.getTimeStart(), diff);
					Date rotTimeEnd = dateSubtractMin(secondSeqFirstRot.getTimeEnd(), diff);
					
					secondSeqFirstRot.setRotationNumber((firstSeqLastRot.getRotationNumber() + 1));
					secondSeqFirstRot.setTimeStart(rotTimeStart);
					secondSeqFirstRot.setTimeEnd(rotTimeEnd);
					secondSeqFirstRot.setBreakType(firstSeqRotBreakType);
					secondSeqFirstRot.setBreakDuration(firstSeqRotBreak);
					secondSeqFirstRot.setOsceSequence(firstOsceSeq);
					secondSeqFirstRot.persist();
					
					for (int i=1; i<secondOsceSeqRotationList.size(); i++)
					{
						OsceDayRotation osceDayRotation = secondOsceSeqRotationList.get(i);
						if (i==1)
						{
							Date timeStartRot = dateAddMin(osceDayRotation.getTimeStart(), diff);
							Date timeEndRot = dateAddMin(osceDayRotation.getTimeEnd(), diff);
							
							osceDayRotation.setRotationNumber(i);
							osceDayRotation.setTimeStart(timeStartRot);
							osceDayRotation.setTimeEnd(timeEndRot);
							osceDayRotation.setBreakType(secondSeqRotBreakType);
							osceDayRotation.setBreakDuration(secondSeqRotBreak);
							osceDayRotation.persist();
						}
						else
						{
							osceDayRotation.setRotationNumber(i);
							osceDayRotation.persist();
						}
					}
					
					firstOsceSeq.setNumberRotation((firstOsceSeqRotationList.size() + 1));
					firstOsceSeq.persist();
					
					secondOsceSeq.setNumberRotation((secondOsceSeqRotationList.size() - 1));
					secondOsceSeq.persist();					
				}
			}
			else if (secondOsceSeq.getId().equals(osceSeqId))
			{
				int indexOfOsceDay = osceDayList.indexOf(osceDay);
				
				List<OsceDayRotation> secondOsceSeqRotationList = secondOsceSeq.getOsceDayRotations();
				
				OsceDayRotation lastRotation = secondOsceSeqRotationList.get((secondOsceSeqRotationList.size() - 1));
				OsceDayRotation secondLastRotation = secondOsceSeqRotationList.get((secondOsceSeqRotationList.size() - 2));
				
				String breakByRotation = osceDay.getBreakByRotation();
				String[] splitString = breakByRotation.split("-");
				String newString = "";
				
				for (int i=0; i<(splitString.length - 1); i++)
				{
					newString = newString + splitString[i] + "-";
				}
				
				secondOsceSeq.setNumberRotation((secondOsceSeqRotationList.size() - 1));
				secondOsceSeq.persist();
				
				osceDay.setTimeEnd(secondLastRotation.getTimeEnd());
				osceDay.setBreakByRotation(newString);
				osceDay.persist();
				
				if (indexOfOsceDay == (osceDayList.size() - 1))
				{
					Long newOsceSeqId = createOsceDayAndOsceSequence(osce, osceDay, secondOsceSeq);
					OsceSequence newOsceSeq = OsceSequence.findOsceSequence(newOsceSeqId);
					OsceDay newOsceDay = newOsceSeq.getOsceDay();
					
					Date rotTimeStart = lastRotation.getTimeStart();
					Date rotTimeEnd = lastRotation.getTimeEnd();
					
					int diff = differenceInMinutes(rotTimeStart, rotTimeEnd);
					
					Date dayTimeEnd = dateAddMin(newOsceDay.getTimeStart(), diff);
					String newOsceDayBreakStr = "0:" + lastRotation.getBreakDuration() + "-";
					
					newOsceDay.setTimeEnd(dayTimeEnd);
					newOsceDay.setBreakByRotation(newOsceDayBreakStr);
					newOsceDay.persist();
					
					OsceDayRotation osceDayRotation = new OsceDayRotation();
					osceDayRotation.setOsceSequence(newOsceSeq);
					osceDayRotation.setOsceDay(newOsceDay);
					osceDayRotation.setRotationNumber(1);
					osceDayRotation.setBreakDuration(lastRotation.getBreakDuration());
					osceDayRotation.setBreakType(lastRotation.getBreakType());
					osceDayRotation.setTimeStart(newOsceDay.getTimeStart());
					osceDayRotation.setTimeEnd(newOsceDay.getTimeEnd());
					osceDayRotation.persist();
					
					lastRotation.remove();
				}
				else
				{
					if ((indexOfOsceDay+1) >= 0 && (indexOfOsceDay+1) < osceDayList.size())
					{
						OsceDay nextOsceDay = osceDayList.get((indexOfOsceDay+1));
						List<OsceSequence> newOsceSeqList = nextOsceDay.getOsceSequences();
						OsceSequence newOsceSequence = null;
						
						if (newOsceSeqList.size() > 0)
							newOsceSequence = newOsceSeqList.get(0);
						
						if (newOsceSequence != null)
						{
							
						}						
					}
					lastRotation.remove();
				}
			}
		}
		else if (osceSeqList.size() == 1)
		{
			
		}
			
	}
	
	
	private static Long createOsceDayAndOsceSequence(Osce osce, OsceDay osceDay, OsceSequence osceSeq)
	{
		//create new osce day
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(osceDay.getOsceDate());
		calendar.add(Calendar.DATE, 1);
		Date newOsceDate = calendar.getTime();
		
 		calendar.setTime(osceDay.getTimeStart());
 		calendar.add(Calendar.DATE, 1);
 		Date newTimeStart = calendar.getTime(); 		
 		
 		calendar.setTime(osceDay.getTimeEnd());
 		calendar.add(Calendar.DATE, 1);
 		Date newTimeEnd = calendar.getTime();
 		
		OsceDay newOsceDay = new OsceDay();
		newOsceDay.setOsceDate(newOsceDate);
		newOsceDay.setTimeStart(newTimeStart);
		newOsceDay.setTimeEnd(newTimeEnd);
		newOsceDay.setOsce(osce);
		newOsceDay.persist();
		
		//create new osce sequence		
		OsceSequence newOsceSequence = new OsceSequence();
		newOsceSequence.setLabel("A");
		newOsceSequence.setNumberRotation(1);
		newOsceSequence.setOsceDay(newOsceDay);
		newOsceSequence.persist();
		
		Map<Long, OscePost> oscePostMap = new HashMap<Long, OscePost>();
		for (OscePost oscePost : osceSeq.getOscePosts())
 		{
 			OscePost newOscePost = new OscePost();
 			newOscePost.setSequenceNumber(oscePost.getSequenceNumber());
 			newOscePost.setValue(oscePost.getValue());
 			newOscePost.setOscePostBlueprint(oscePost.getOscePostBlueprint());
 			newOscePost.setOsceSequence(newOsceSequence);
 			newOscePost.setStandardizedRole(oscePost.getStandardizedRole());
 			newOscePost.persist();
 			oscePostMap.put(oscePost.getId(), newOscePost);
 		}
		
		for (Course course : osceSeq.getCourses())
		{
			Course newCourse = new Course();
			newCourse.setColor(course.getColor());
			newCourse.setOsce(osce);
			newCourse.setOsceSequence(newOsceSequence);
			newCourse.persist();
			
			//create OscePostRoom
			for (OscePostRoom oscePostRoom : course.getOscePostRooms())
			{
				OscePostRoom newOscePostRoom = new OscePostRoom();
				newOscePostRoom.setCourse(newCourse);
				newOscePostRoom.setOscePost(oscePostMap.get(oscePostRoom.getOscePost().getId()));
				newOscePostRoom.setRoom(oscePostRoom.getRoom());
				newOscePostRoom.persist();
			}
		}
	 
		return newOsceSequence.getId(); 
	}
	
	private static int differenceInMinutes(Date date1, Date date2)
	{
		long diff = date2.getTime() - date1.getTime();
		Long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
		return minutes.intValue();
	}
	
	private static Date dateAddMin(Date date, int minToAdd) {
		return new Date((long) (date.getTime() + minToAdd * 60 * 1000));
	}
	
	private static Date dateSubtractMin(Date date, int minToSubtract) {
		return new Date((long) (date.getTime() - minToSubtract * 60 * 1000));
	}
	
	public static OsceDay findOsceDayByOsceSequenceId(Long osceSeqId)
	{
		EntityManager em = entityManager();
		String sql = "select os.osceDay from OsceSequence os where os.id = " + osceSeqId;
		TypedQuery<OsceDay> query = em.createQuery(sql, OsceDay.class);
		if (query.getResultList() != null && query.getResultList().size() > 0)
			return query.getResultList().get(0);
		else
			return null;
	}

	public Date getOsceDate() {
        return this.osceDate;
    }

	public void setOsceDate(Date osceDate) {
        this.osceDate = osceDate;
    }

	public Date getTimeStart() {
        return this.timeStart;
    }

	public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

	public Date getTimeEnd() {
        return this.timeEnd;
    }

	public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

	public Date getLunchBreakStart() {
        return this.lunchBreakStart;
    }

	public void setLunchBreakStart(Date lunchBreakStart) {
        this.lunchBreakStart = lunchBreakStart;
    }

	public Integer getLunchBreakAfterRotation() {
        return this.lunchBreakAfterRotation;
    }

	public void setLunchBreakAfterRotation(Integer lunchBreakAfterRotation) {
        this.lunchBreakAfterRotation = lunchBreakAfterRotation;
    }

	public Osce getOsce() {
        return this.osce;
    }

	public void setOsce(Osce osce) {
        this.osce = osce;
    }

	public Integer getValue() {
        return this.value;
    }

	public void setValue(Integer value) {
        this.value = value;
    }

	public Boolean getIsTimeSlotShifted() {
        return this.isTimeSlotShifted;
    }

	public void setIsTimeSlotShifted(Boolean isTimeSlotShifted) {
        this.isTimeSlotShifted = isTimeSlotShifted;
    }

	public Set<Assignment> getAssignments() {
        return this.assignments;
    }

	public void setAssignments(Set<Assignment> assignments) {
        this.assignments = assignments;
    }

	public List<OsceSequence> getOsceSequences() {
        return this.osceSequences;
    }

	public void setOsceSequences(List<OsceSequence> osceSequences) {
        this.osceSequences = osceSequences;
    }

	public Set<PatientInSemester> getPatientInSemesters() {
        return this.patientInSemesters;
    }

	public void setPatientInSemesters(Set<PatientInSemester> patientInSemesters) {
        this.patientInSemesters = patientInSemesters;
    }

	public String getBreakByRotation() {
        return this.breakByRotation;
    }

	public void setBreakByRotation(String breakByRotation) {
        this.breakByRotation = breakByRotation;
    }

	public Integer getLunchBreakAdjustedTime() {
        return this.lunchBreakAdjustedTime;
    }

	public void setLunchBreakAdjustedTime(Integer lunchBreakAdjustedTime) {
        this.lunchBreakAdjustedTime = lunchBreakAdjustedTime;
    }

	public List<OsceDayRotation> getOsceDayRotations() {
        return this.osceDayRotations;
    }

	public void setOsceDayRotations(List<OsceDayRotation> osceDayRotations) {
        this.osceDayRotations = osceDayRotations;
    }

	public Integer getStudentCount() {
        return this.studentCount;
    }

	public void setStudentCount(Integer studentCount) {
        this.studentCount = studentCount;
    }

	public Integer getSpCount() {
        return this.spCount;
    }

	public void setSpCount(Integer spCount) {
        this.spCount = spCount;
    }

	public Integer getRoomCount() {
        return this.roomCount;
    }

	public void setRoomCount(Integer roomCount) {
        this.roomCount = roomCount;
    }

	public Room getReserveSPRoom() {
		return reserveSPRoom;
	}

	public void setReserveSPRoom(Room reserveSPRoom) {
		this.reserveSPRoom = reserveSPRoom;
	}

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Assignments: ").append(getAssignments() == null ? "null" : getAssignments().size()).append(", ");
        sb.append("BreakByRotation: ").append(getBreakByRotation()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("IsTimeSlotShifted: ").append(getIsTimeSlotShifted()).append(", ");
        sb.append("LunchBreakAdjustedTime: ").append(getLunchBreakAdjustedTime()).append(", ");
        sb.append("LunchBreakAfterRotation: ").append(getLunchBreakAfterRotation()).append(", ");
        sb.append("LunchBreakStart: ").append(getLunchBreakStart()).append(", ");
        sb.append("Osce: ").append(getOsce()).append(", ");
        sb.append("OsceDate: ").append(getOsceDate()).append(", ");
        sb.append("OsceDayRotations: ").append(getOsceDayRotations() == null ? "null" : getOsceDayRotations().size()).append(", ");
        sb.append("OsceSequences: ").append(getOsceSequences() == null ? "null" : getOsceSequences().size()).append(", ");
        sb.append("PatientInSemesters: ").append(getPatientInSemesters() == null ? "null" : getPatientInSemesters().size()).append(", ");
        sb.append("RoomCount: ").append(getRoomCount()).append(", ");
        sb.append("SpCount: ").append(getSpCount()).append(", ");
        sb.append("StudentCount: ").append(getStudentCount()).append(", ");
        sb.append("TimeEnd: ").append(getTimeEnd()).append(", ");
        sb.append("TimeStart: ").append(getTimeStart()).append(", ");
        sb.append("Value: ").append(getValue()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
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
            OsceDay attached = OsceDay.findOsceDay(this.id);
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
    public OsceDay merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        OsceDay merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new OsceDay().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countOsceDays() {
        return entityManager().createQuery("SELECT COUNT(o) FROM OsceDay o", Long.class).getSingleResult();
    }

	public static List<OsceDay> findAllOsceDays() {
        return entityManager().createQuery("SELECT o FROM OsceDay o", OsceDay.class).getResultList();
    }

	public static OsceDay findOsceDay(Long id) {
        if (id == null) return null;
        return entityManager().find(OsceDay.class, id);
    }

	public static List<OsceDay> findOsceDayEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM OsceDay o", OsceDay.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
	/**
	 * Assigning reserve room for day.
	 * @param osceDayId
	 * @param roomId
	 * @return
	 */
	public static Boolean assignReserveRoomForDay(Long osceDayId, Long roomId){
		Log.info("assigning reserve room to osce day");
		try{
			OsceDay osceDay = OsceDay.findOsceDay(osceDayId);
			Room reserveRoom = Room.findRoom(roomId);
			osceDay.setReserveSPRoom(reserveRoom);
			osceDay.persist();
			return true;	
		}catch(Exception e){
			Log.error("Error while assigning reserve room to osce day");
			return false;
		}
		
	}
}

class Schedule
{
	DateTime time;
	String tone;
	String isShow;
	RingtoneTypes type;
	
	
	public DateTime getTime() {
		return time;
	}
	public void setTime(DateTime time) {
		this.time = time;
	}
	public String getTone() {
		return tone;
	}
	public void setTone(String tone) {
		this.tone = tone;
	}
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	public RingtoneTypes getType() {
		return type;
	}
	public void setType(RingtoneTypes type) {
		this.type = type;
	}
	
}
