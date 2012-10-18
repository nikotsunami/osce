package ch.unibas.medizin.osce.domain;

//import groovy.transform.ToString;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.i18n.client.Constants.DefaultBooleanValue;
import com.google.gwt.i18n.client.Constants.DefaultIntValue;
import com.google.gwt.user.client.AsyncProxy.DefaultValue;

import ch.unibas.medizin.osce.server.spalloc.SPAllocator;
import ch.unibas.medizin.osce.server.ttgen.TimetableGenerator;
import ch.unibas.medizin.osce.shared.OSCESecurityStatus;
import ch.unibas.medizin.osce.shared.OsceSecurityType;
import ch.unibas.medizin.osce.shared.OsceStatus;
import ch.unibas.medizin.osce.shared.PatientAveragePerPost;
import ch.unibas.medizin.osce.shared.PostType;
import ch.unibas.medizin.osce.shared.StudyYears;
import ch.unibas.medizin.osce.shared.util;

@RooJavaBean
@RooToString
@RooEntity
public class Osce {
	
    @Enumerated
    private StudyYears studyYear;

    private Integer maxNumberStudents;

    private String name;
    
    private Short shortBreak;
    
    private Short shortBreakSimpatChange;
    
    private Short LongBreak;
    
    private Short lunchBreak;
    
    private Short middleBreak;
    
    @NotNull
    private Short lunchBreakRequiredTime;
    
    @NotNull
    private Short longBreakRequiredTime;
    
   //s private Integer numberPosts;

    private Integer numberCourses;

    private Integer postLength;

    private Boolean isRepeOsce;

    @NotNull
    private Integer numberRooms;

    private Boolean isValid;
    

    @Enumerated
    private OsceStatus osceStatus;
    
    @Enumerated
    private OSCESecurityStatus security;
    
    @Enumerated
    private OsceSecurityType osceSecurityTypes;
    
    @Enumerated
    private PatientAveragePerPost patientAveragePerPost;
    
    
   @NotNull
    private Boolean spStayInPost=false;
    
    @NotNull
    @ManyToOne
    private Semester semester;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "osce")
    @OrderBy("osceDate")
    private List<OsceDay> osce_days = new ArrayList<OsceDay>();

    @OrderBy("sequenceNumber")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "osce")
    private List<OscePostBlueprint> oscePostBlueprints = new ArrayList<OscePostBlueprint>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "osce")
    private Set<Task> tasks = new HashSet<Task>();
    private static Logger log = Logger.getLogger(Osce.class);
    // dk, 2012-02-10: split up m to n relationship since students
    // need flag whether they are enrolled or not
    //
    // @ManyToMany(cascade = CascadeType.ALL, mappedBy = "osces")
    // private Set<Student>   students = new HashSet<Student>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "osce")
    private Set<StudentOsces> osceStudents = new HashSet<StudentOsces>();
    
    @ManyToOne(cascade = CascadeType.ALL)
	private Osce copiedOsce;
   
    /**
	 * Get number of slots until a SP change is necessary (lowest number of consecutive slots
	 * is defined by the most difficult role)
	 * 
	 * @return number of slots until SP change for most difficult role topic
	 */
	public int slotsOfMostDifficultRole() 
	{
		int slotsUntilChange = Integer.MAX_VALUE;
		 
	
		Iterator<OscePostBlueprint> it = getOscePostBlueprints().iterator();
		while (it.hasNext()) 
		{
			OscePostBlueprint oscePostBlueprint = (OscePostBlueprint) it.next();
			if(oscePostBlueprint.getPostType()==PostType.BREAK)
			{
				continue;	
			}			
			int slots = oscePostBlueprint.getRoleTopic().getSlotsUntilChange();
			if(slots > 0 && slots < slotsUntilChange) {
				slotsUntilChange = slots;
			}
		}
		
		return slotsUntilChange;
	}
	
	/**
     * Get the number of posts that need to have a room assigned (needed for calculation of number of parcours).
     * This number is basically defined by the number of posts that have post_type = BREAK
     * @return number of posts that need to have a room assigned
     */
    public int numberPostsWithRooms() {
        int numberPostsWithRooms = 0;
        Iterator<OscePostBlueprint> it = getOscePostBlueprints().iterator();
        while (it.hasNext()) {
        	OscePostBlueprint oscePostBlueprint = (OscePostBlueprint) it.next();
            if (!oscePostBlueprint.getPostType().equals(PostType.BREAK)) {
                numberPostsWithRooms++;
            }
        }
        return numberPostsWithRooms;
    }
    
    /**
     * Get the number of posts that are of post_type = BREAK.
     * NOTE: Only for manually defined break posts
     * @return number of break posts
     */
    public int numberManualBreakPosts() {
    	int numberManualBreakPosts = 0;
        Iterator<OscePostBlueprint> it = getOscePostBlueprints().iterator();
        while (it.hasNext()) {
        	OscePostBlueprint oscePost = (OscePostBlueprint) it.next();
            if (oscePost.getPostType().equals(PostType.BREAK)) {
                numberManualBreakPosts++;
            }
        }
        return numberManualBreakPosts;
    }
    
    /**
     * Get number of student-slots that are covered by one SimPat-slot
     * @return number of student-slots covered by one SimPat-slot
     */
    public int simpatAssignmentSlots() {
        List<Assignment> assignments = Assignment.retrieveAssignmentsOfTypeSP(this);
        if (assignments.get(0) != null) {
            Assignment assignment = assignments.get(0);
            long secs = (assignment.getTimeEnd().getTime() - assignment.getTimeStart().getTime()) / 1000;
            int assignmentMinutes = (int) (secs / 60);
            int assignmentSlots = assignmentMinutes / (this.getPostLength() + 1);
            return assignmentSlots;
        }
        return 0;
    }

    /**
     * Get all roles used in this OSCE
     * @return set containing roles
     */
    public Set<StandardizedRole> usedRoles() {
        Set<StandardizedRole> roles = new HashSet<StandardizedRole>();
        
        Iterator<OscePostBlueprint> it = getOscePostBlueprints().iterator();
        while (it.hasNext()) {
        	OscePostBlueprint oscePostBlueprint = (OscePostBlueprint) it.next();
            Iterator<OscePost> itPost = oscePostBlueprint.getOscePosts().iterator();
            while (itPost.hasNext()) {
				OscePost oscePost = (OscePost) itPost.next();
				roles.add(oscePost.getStandardizedRole());
			}
        }
        return roles;
    }
    
    public static Boolean generateOsceScaffold(Long osceId) {
    	try {
    		TimetableGenerator optGen = TimetableGenerator.getOptimalSolution(Osce.findOsce(osceId));

    		// persist scaffold (osce days and all cascading entities)
    		optGen.createScaffold();
    	} catch(Exception e) {
    		e.printStackTrace();
    		return false;
    	}
    	return true;
    }
    
    public static Boolean generateAssignments(Long osceId) {
    	try {
    		//spec bug sol
    		boolean test = OscePostRoom.insertRecordForDoublePost(osceId);
    		//spec bug sol
    		
    		if(test)
    		{
    			TimetableGenerator optGen = TimetableGenerator.getOptimalSolution(Osce.findOsce(osceId));
    	    	System.out.println(optGen.toString());
    	    	
    	    	log.info("calling createAssignments()...");
    	    	
    	    	Set<Assignment> assignments = optGen.createAssignments();
    	    	log.info("number of assignments created: " + assignments.size());
    		}
	    	
    	} catch(Exception e) {
    		e.printStackTrace();
    		return false;
    	}
    	return true;
    }
    
    public static Boolean autoAssignPatientInRole(Long osceId) {
    	try {
    		SPAllocator spAlloc = new SPAllocator(Osce.findOsce(osceId));
    		spAlloc.getSolution();
    		spAlloc.printSolution();
    		spAlloc.saveSolution();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	return Boolean.TRUE;
    }
   
    //spec start
    //This method assigns students in Assignment Table.
    public static Boolean autoAssignStudent(Long osceId)
    {
    	EntityManager em = entityManager();
    	
    	try{
    	
    	
    	//retrieve distinct sequence number of student
    	String seqQuery="select distinct sequenceNumber from Assignment where type=0 and osceDay in (select id from OsceDay where osce="+osceId+") order by sequenceNumber";
    	
    	//retrieve studentOsces    	
    	String studentQuery="SELECT student FROM StudentOsces s where s.isEnrolled=true and osce="+osceId +" order by s.student.name,s.student.preName";
    	
    	
    	TypedQuery<Integer> seqTypedQuery = em.createQuery(seqQuery, Integer.class);
    	List<Integer> seqList=seqTypedQuery.getResultList();
    	
    	TypedQuery<Student> studentTypedQuery = em.createQuery(studentQuery,Student.class);
    	
    	Log.info("Student Query : " + studentTypedQuery.toString());
    	
    	List<Student> studentList=studentTypedQuery.getResultList();
    	
    	String assignmentQueryString="select a from Assignment a  where sequenceNumber=:sequenceNumber and type=0 and osceDay in (select id from OsceDay where osce="+osceId+")";
    	TypedQuery<Assignment> assignmentTypedQuery = em.createQuery(assignmentQueryString,Assignment.class);
    
    	
   
    	
//    	em.getTransaction().begin();
    	for(int i=0;i<studentList.size();i++)
    	{
    			Integer sequence=seqList.get(i);
    		
    			assignmentTypedQuery.setParameter("sequenceNumber", sequence);
    			List<Assignment> assignmentList=assignmentTypedQuery.getResultList();
    			for(Assignment assignment:assignmentList)
    			{
    				assignment.setStudent(studentList.get(i));
    				assignment.persist();
    			}
    	
    	}
   // 	em.getTransaction().commit();
    	return true;
    	}
    	catch (Exception e) {
			e.printStackTrace();
		//	em.getTransaction().rollback();
			return false;
		}
    }
    
    public static List<Osce> findAllOsceBySemster(Long id) {
    	EntityManager em = entityManager();
    	System.out.println("BEFORE CALL------->");
    	String queryString="SELECT o FROM Osce as o  where o.semester.id="+id + " order by studyYear desc";
    	System.out.println("query--"+ queryString);
    	TypedQuery<Osce> q = em.createQuery(queryString, Osce.class);
    //	TypedQuery<Osce> q = em.createQuery("SELECT o FROM Osce as o  where o.semester=1 " , Osce.class);
    	System.out.println("success done with size");
    	
    	return q.getResultList();
    }
    
    
    
    public static Osce findMaxOsce() {
        EntityManager em = entityManager();
        TypedQuery<Osce> query = em.createQuery("SELECT o FROM Osce o  where o.id=(select max(o.id) from Osce o)  ", Osce.class);

        
        return query.getSingleResult();
        
    }
    
 public static List<Osce> findAllOsceOnSemesterId(Long semesterId){
		
    	log.info("Inside Osce class To retrive all Osce Based On semesterId");
		EntityManager em = entityManager();
		TypedQuery<Osce> q = em.createQuery("SELECT o FROM Osce AS o WHERE o.semester = " + semesterId ,Osce.class);
		return q.getResultList();
				
	}
	
	public static List<Osce> findAllOscesGroupByCopiedOsce() {
        return entityManager().createQuery("SELECT o FROM Osce o GROUP BY o.copiedOsce", Osce.class).getResultList();
    }
    
	public static Integer initOsceBySecurity() {

		List<Osce> osces = findAllOsces();
		int i =0;

		for (Iterator<Osce> iterator = osces.iterator(); iterator.hasNext();) {
			Osce osce = (Osce) iterator.next();
			System.out.println("osce.security" + osce.security);

			if (osce.security == null) {

				// System.out.println("\n\n Removed PIR is : "+patientInRole.getId());
				osce.security = OSCESecurityStatus.FEDERAL_EXAM;
				osce.persist();
				i++;

			}

		}
		return i;

	}
    
    //spec end
	 //module 3 f {

	  public static void autoAssignPatientInsemester(Long semesterId){
		  // Algorithm (Auto assignment of Patient ) is transfered at AutoAssignPatientInSemesterServiceImpl.java to support push event 
	
	  	}
	  
	  public static Integer getTotalRolesFroOscePost(Long oscePostId,Long patientInSemesterId){
		  Log.info("Inside getTotalRolesFroOscePost() where postIdis :" +oscePostId + " and SemId Is : " + patientInSemesterId );
		  EntityManager em = entityManager();
		  String query="select count(pir) from PatientInRole as pir where pir.oscePost=" + oscePostId + " and pir.patientInSemester=" +patientInSemesterId;
		  Log.info("Query Is :" + query);
		  TypedQuery<Long> q = em.createQuery(query, Long.class);
		  Integer result= q.getSingleResult() != null && q.getSingleResult() != 0 ? q.getSingleResult().intValue() : 0 ;
		  return result;
	  }
	  public static PatientInRole findPatientInRoleBasedOnOsceAndPatientInSem(Long oscePostId,Long patientInSemId){
		  Log.info("In Side findPatientInRoleBasedOnOsceAndPatientInSem() With PostId" + oscePostId +" and SemId" + patientInSemId);
		  EntityManager em = entityManager();
		  String query="select pir from PatientInRole as pir where pir.oscePost=" + oscePostId + " and pir.patientInSemester=" +patientInSemId;
		  Log.info("Query Is :" + query);
		  TypedQuery<PatientInRole> q = em.createQuery(query, PatientInRole.class);
		  return q.getSingleResult();
	  }
	  public static int findCountOfTimeSlot(List<Assignment> assignmentList,int breakCount){
		  
	  Assignment previousAssignment;
	  Assignment currentAssignment;
	  int timeSlotsBeforBrak=1;
		  for(int count=1;count<assignmentList.size();count++){

			previousAssignment=assignmentList.get(count-1);	
			currentAssignment=assignmentList.get(count);
		
			if(findCountOfTimeSlots(previousAssignment,currentAssignment,breakCount)){
				timeSlotsBeforBrak++;
			}
			else{
				break;
			}
			 
		  }
		  return timeSlotsBeforBrak;
	  }
	  private static boolean findCountOfTimeSlots(Assignment previosAssignment,Assignment currentAssignment,int breakTime){
		  Time previosEndDate= new Time(previosAssignment.getTimeEnd().getTime());
		  Time currentStartDate = new Time(currentAssignment.getTimeStart().getTime());

		  Log.info("Start Time is : " + currentStartDate.getTime());
		  Log.info("Start Time is : " + previosEndDate.getTime());
		  
		  if((Math.abs(currentStartDate.getTime()-previosEndDate.getTime()))!=breakTime){
			  Log.info("Deffrance is : "+Math.abs(currentStartDate.getTime()-previosEndDate.getTime()));
			  Log.info("Brak Time is :"+breakTime);
			  return true;
		  }
		  return false;
	  }
	  public static List<OscePost> findAllOscePostInSemester(Long semesterId){
		  
		  EntityManager em = entityManager();
		  String queryString="select op from Osce as o,OsceDay as od,OsceSequence as os,OscePost as op,StandardizedRole as sr,OscePostBlueprint as opb where o.semester="+semesterId +
		  " and o.osceStatus = " + OsceStatus.OSCE_CLOSED.ordinal() + " and od.osce=o.id and os.osceDay=od.id and op.osceSequence=os.id and opb.id=op.oscePostBlueprint and opb.postType NOT IN(1)" +
		  		" and sr.id=op.standardizedRole and sr.roleType NOT IN(2)";
		  Log.info(queryString);
		  TypedQuery<OscePost> q = em.createQuery(queryString,OscePost.class);
			return q.getResultList();
	  }
	  
	  public static List<OsceDay> findAllOsceDaysInSemster(Long semesterId){
		  
		  EntityManager em = entityManager();
		  String queryString="select od from Osce as o,OsceDay od where o.semester="+semesterId +" and o.osceStatus = " + OsceStatus.OSCE_CLOSED.ordinal() + " and od.osce=o.id";
		  TypedQuery<OsceDay> q = em.createQuery(queryString,OsceDay.class);
			return q.getResultList();
	  }
	  
	public static List<OsceDay> getAllOsceDaysSortByValueAsc(Long semesterId){
		
		  EntityManager em = entityManager();
		  String queryString="select od from Osce as o,OsceDay od where o.semester="+semesterId + " and o.osceStatus = " + OsceStatus.OSCE_CLOSED.ordinal() + " and od.osce=o.id ORDER BY od.value";
		  TypedQuery<OsceDay> q = em.createQuery(queryString,OsceDay.class);
		  return q.getResultList();
	  }
	public static List<OscePost> getSortedOscePost(Long osceDayId){
		
		EntityManager em = entityManager();
		Log.info("OsceDay id AT getSortedOscePost():" + osceDayId);
		//String queryString="select distinct sr from OsceDay as od, OsceSequence as os, OscePost as op, StandardizedRole sr where od.id = "+ osceDay.getId() +" and os.osceDay= od.id and op.osceSequence=os.id and op.standardizedRole=sr.id ORDER BY sr.value ASC";
		String queryString="select op from OsceSequence as os, OscePost as op,StandardizedRole as sr,OscePostBlueprint as opb where" +
		" os.osceDay= "+ osceDayId+" and op.osceSequence=os.id and sr.id=op.standardizedRole and opb.id=op.oscePostBlueprint and opb.postType NOT IN(1)" +
				" and sr.roleType NOT IN (2) ORDER BY op.value";
		TypedQuery<OscePost> q = em.createQuery(queryString,OscePost.class);
		return q.getResultList();
	}

	public static List<OscePost> getSortedOscePostByTypeAndComlexity(Long osceDayId){
		EntityManager em = entityManager();
		//IF(rt.slotsUntilChange IS NULL OR rt.slotsUntilChange='0',1,0) as slotdes
		String queryString="select op from OsceSequence as os, OscePost as op,RoleTopic as rt,StandardizedRole as sr,OscePostBlueprint as opb where" +
		" os.osceDay= "+ osceDayId+" and op.osceSequence=os.id and sr.id=op.standardizedRole and opb.id=op.oscePostBlueprint and opb.postType NOT IN(1)" +
				" and rt.id=sr.roleTopic and sr.roleType NOT IN (2)"+
		" ORDER BY sr.roleType DESC,rt.slotsUntilChange DESC NULLS FIRST";
		TypedQuery<OscePost> q = em.createQuery(queryString,OscePost.class);
		return q.getResultList();
	}

	public static List<PatientInSemester> getPatientAccptedInOsceDayByRoleCountAscAndValueASC(OsceDay osceDay,Long semesterId){
		EntityManager em = entityManager();
		
	Log.info("Size of PatientIn Sem at getPatientAccptedInOsceDayByRoleCountAscAndValueASC()" + osceDay.getPatientInSemesters().size());
	Log.info("OSce Day At getPatientAccptedInOsceDayByRoleCountAscAndValueASC():" +osceDay.getId());
	
	/*String queryString = "select pis from PatientInSemester as pis left join PatientInRole as pir "
			+ "where pis.id IN(''"+ getPatientInSemesterIDList(osceDay.getPatientInSemesters()) +") "
			+ "and pir.patientInSemester=pis.id  GROUP BY pir.patientInSemester ORDER BY pis.value,count(pir.patientInSemester)";*/
	/*String queryString = " Select pis from PatientInSemester pis left join PatientInRole pir  " +
			"where pis.id IN(''"+ getPatientInSemesterIDList(osceDay.getPatientInSemesters()) +") " +
			" GROUP BY pis.id " + 
			" ORDER BY pis.value,count(pir.patientInSemester.id) ";	*/	
	String queryString = " Select pis from PatientInSemester pis left join pis.patientInRole pir " +
			"where pis.id IN(''"+ getPatientInSemesterIDList(osceDay.getPatientInSemesters()) +") " +
			" and pis.semester="+semesterId +" and pis.accepted=1 GROUP BY pis.id " + 
			" ORDER BY pis.value , count(pir.patientInSemester) ";
	Log.info(queryString);
		TypedQuery<PatientInSemester> q = em.createQuery(queryString,PatientInSemester.class);
		 
		return q.getResultList();
	}


	public static List<PatientInSemester> getPatientAccptedInOsceDayByRoleCountAscAndValueDESC(OsceDay osceDay,Long semesterId){
		EntityManager em = entityManager();
			
		Log.info("Size of PatientIn Sem at getPatientAccptedInOsceDayByRoleCountAscAndValueDESC()" + osceDay.getPatientInSemesters().size());
		Log.info("OSceDay is At getPatientAccptedInOsceDayByRoleCountAscAndValueDESC () " + osceDay.getId());	
		String idList="";
		
		
			/*String queryString = "select pis from PatientInSemester as pis, PatientInRole as pir "
					+ "where pis.id IN(''"+ getPatientInSemesterIDList(osceDay.getPatientInSemesters()) +") "
					+ "and pir.patientInSemester=pis.id  GROUP BY pir.patientInSemester ORDER BY pis.value DESC,count(pir.patientInSemester)";*/
		/*String queryString = " Select pis from PatientInSemester pis left join PatientInRole pir  " +
				"where pis.id IN(''"+ getPatientInSemesterIDList(osceDay.getPatientInSemesters()) +") " +
				" GROUP BY pis.id " + 
				" ORDER BY pis.value DESC, count(pir.patientInSemester.id) ";*/
		String queryString = " Select pis from PatientInSemester pis left join pis.patientInRole pir " +
				"where pis.id IN(''"+ getPatientInSemesterIDList(osceDay.getPatientInSemesters()) +") " +
				" and pis.semester="+semesterId +" and pis.accepted=1 GROUP BY pis.id " + 
				" ORDER BY pis.value DESC , count(pir.patientInSemester) ";
					Log.info(queryString);
				TypedQuery<PatientInSemester> q = em.createQuery(queryString,PatientInSemester.class);
				 
				return q.getResultList();

	}

	/*public static List<Course>getAllParcoursForThisOsceDay(OsceDay osceDay){
		
		EntityManager em = entityManager();
		String queryString="select co from OsceSequence as os,Course as co where os.osceDay="+ osceDay.getId()+" and co.osceSequence=os.id";
		TypedQuery<Course> q = em.createQuery(queryString,Course.class);
		return q.getResultList();
	}*/
	public static List<Course>getAllParcoursForThisSequence(Long sequenceId){
		
		EntityManager em = entityManager();
		String queryString="select co from Course as co where co.osceSequence="+sequenceId;
		TypedQuery<Course> q = em.createQuery(queryString,Course.class);
		return q.getResultList();
	}
	public static List<OscePost>findAllOscePostOfDay(Long osceDayId){
		EntityManager em = entityManager();
		Log.info("OsceDay id AT findAllOscePostOfDay():" + osceDayId);
		String queryString="select op from OsceSequence as os, OscePost as op,StandardizedRole as sr,OscePostBlueprint as opb where"+
		" os.osceDay= "+osceDayId+" and op.osceSequence=os.id and opb.id=op.oscePostBlueprint and opb.postType NOT IN(1) and sr.id=op.standardizedRole and sr.roleType NOT IN (2)";
		TypedQuery<OscePost> q = em.createQuery(queryString,OscePost.class);
		return q.getResultList();
	}

	public static List<List<OscePost>>  findPersentageOfRoleFitsForDay(Long semesterId,List<OscePost>allOscePostOfThisDay,PatientInSemester sortedPatientInSemester2){
		
		Log.info("Semester Id AT findPersentageOfRoleFitsForDay() Is :" +semesterId );
		List<PatientInSemester> listOfPatientInSemesterSatisfyCriteria = new ArrayList<PatientInSemester>();
		Set<AdvancedSearchCriteria> setAdvanceSearchCriteria = new HashSet<AdvancedSearchCriteria>();
		List<AdvancedSearchCriteria> listAdvanceSearchCriteria = new ArrayList<AdvancedSearchCriteria>();
		
		List<OscePost> accePtedRoleInCriteriaList =new ArrayList<OscePost>();
		List<OscePost> unAccptedRoleInCriteriaList = new ArrayList<OscePost>();
		List<List<OscePost>> resultList= new ArrayList<List<OscePost>>();
		OscePost oscePost;
		StandardizedRole standardizedRole;
		Iterator<OscePost> oscePostIterator = allOscePostOfThisDay.iterator();
		
		while(oscePostIterator.hasNext()){
			
			setAdvanceSearchCriteria.clear();
			listOfPatientInSemesterSatisfyCriteria.clear();
			listAdvanceSearchCriteria.clear();
			
			oscePost=oscePostIterator.next();
			
			Log.info("Osce Post At findPersentageOfRoleFitsForDay()  is " +oscePost.getId() + " :: " + oscePost.getStandardizedRole().getRoleTopic().getOscePostBlueprints().size());
			standardizedRole=oscePost.getStandardizedRole();
			
			Log.info("StandarDized Role At findPersentageOfRoleFitsForDay() is " + standardizedRole.getId() + " : " + oscePost.getStandardizedRole().getAdvancedSearchCriteria().size());
			setAdvanceSearchCriteria=standardizedRole.getAdvancedSearchCriteria();
			
			Log.info("Search Criteria size :" + setAdvanceSearchCriteria.size());
			
			if(setAdvanceSearchCriteria==null || setAdvanceSearchCriteria.size() <=0 ){
				//continue;
				accePtedRoleInCriteriaList.add(oscePost);
			}
			else{
			listAdvanceSearchCriteria.addAll(setAdvanceSearchCriteria);
			
			listOfPatientInSemesterSatisfyCriteria= sortedPatientInSemester2.findPatientInSemesterByAdvancedCriteria(semesterId,listAdvanceSearchCriteria);
			
			if(listOfPatientInSemesterSatisfyCriteria != null || listOfPatientInSemesterSatisfyCriteria.size() > 0){
				
				if(listOfPatientInSemesterSatisfyCriteria.contains(sortedPatientInSemester2)){
					accePtedRoleInCriteriaList.add(oscePost);
				}
				else{
					unAccptedRoleInCriteriaList.add(oscePost);
				}
			}
		}
		}
		Log.info("Criteria Setisfy For Roles Is :"+accePtedRoleInCriteriaList.size()+" For Patient :" +sortedPatientInSemester2.getId());
		Log.info("Criteria NOT Setisfy For Roles Is :"+unAccptedRoleInCriteriaList.size()+" For Patient :" +sortedPatientInSemester2.getId());
		resultList.add(accePtedRoleInCriteriaList);
		resultList.add(unAccptedRoleInCriteriaList);
		
		return resultList;
	}
	public static Long getCountOfSPAssigndAsBackup(OsceDay osceDay){
		
		EntityManager em = entityManager();
		Log.info("Size of PatientIn Sem at getCountOfSPAssigndAsBackup()" + osceDay.getPatientInSemesters().size());
		
		/*String queryString ="select count(*) from OsceSequence as os,OscePost as op,PatientInRole as pir where os.osceDay="+osceDay.getId() +" and op.osceSequence=os.id and pir.oscePost=op.id"+
		" and pir.is_backup=1 GROUP BY pir.oscePost";*/
		String queryString ="select count(*) from OsceSequence as os,OscePost as op,PatientInRole as pir where os.osceDay="+osceDay.getId() +" and op.osceSequence=os.id and pir.oscePost=op.id"+
				" and pir.is_backup=1 GROUP BY pir.oscePost";
		Log.info(queryString);
		TypedQuery<Long> q = em.createQuery(queryString,Long.class);
		if(q.getResultList().size()==0)
			return 0l;
		else{
		Log.info("Result is :"+q.getSingleResult());
		return q.getSingleResult();
		}
		
	}
	public static List<PatientInSemester> findPatientInSemByCountOfAssignAsBackup(OsceDay osceDay){
		
		Log.info("OsceDay At findPatientInSemByCountOfAssignAsBackup() " +osceDay.getId());
		EntityManager em = entityManager();
		String queryString="select pis from PatientInSemester as pis, PatientInRole as pir "
					+ "where pis.id IN(''"+ getPatientInSemesterIDList(osceDay.getPatientInSemesters()) +") "
					+ " and pir.patientInSemester=pis.id and pir.is_backup=1";
		TypedQuery<PatientInSemester> q =em.createQuery(queryString,PatientInSemester.class);
		return q.getResultList();
		
	}
	public static List<Assignment> findAllAssignmentForOsceDayAndOscePost(Long osceDayId,OscePost oscePost){
		Log.info("oscePost At findAllAssignmentForOsceDayAndOscePost()" + oscePost.getId());
		EntityManager em = entityManager();
		Log.info("Osce Post Is :" + oscePost.getId());
		Log.info("Osce Day Id Is :" + osceDayId);
		String queryString="select a from Assignment as a where a.osceDay="+ osceDayId + " and a.oscePostRoom In(''" + getOscePostRoomIdList(oscePost.getOscePostRooms()) + ") ORDER BY a.timeStart";
		Log.info("findAllAssignmentForOsceDayAndOscePost() Query is :" + queryString);
		TypedQuery<Assignment> q =em.createQuery(queryString, Assignment.class);
		return q.getResultList();
	}
	private static String getPatientInSemesterIDList(
			Set<PatientInSemester> patientInsemesterlist) {

		if (patientInsemesterlist == null
				|| patientInsemesterlist.size() == 0) {
			Log.info("Return as null");
			return "";
		}
		Iterator<PatientInSemester> patientInsemesterlistIterator = patientInsemesterlist.iterator();
		StringBuilder patientInSemesterId = new StringBuilder();
		patientInSemesterId.append(",");
		while (patientInsemesterlistIterator.hasNext()) {
			PatientInSemester patientInSemester = patientInsemesterlistIterator.next();

			patientInSemesterId.append(patientInSemester.getId().toString());
			if (patientInsemesterlistIterator.hasNext()) {
				patientInSemesterId.append(" ,");
			}
		}
		
		return patientInSemesterId.toString();
	}
	private static String getOscePostRoomIdList(Set<OscePostRoom> oscePostRoomSet){
		
		if (oscePostRoomSet == null
				|| oscePostRoomSet.size() == 0) {
			Log.info("getOscePostRoomIdList Return as null");
			return "";
		}
		Iterator<OscePostRoom> oscePostRoomSetIterator = oscePostRoomSet.iterator();
		StringBuilder oscePostRoomId = new StringBuilder();
		oscePostRoomId.append(",");
		while (oscePostRoomSetIterator.hasNext()) {
			OscePostRoom oscePostRoom = oscePostRoomSetIterator.next();

			oscePostRoomId.append(oscePostRoom.getId().toString());
			if (oscePostRoomSetIterator.hasNext()) {
				oscePostRoomId.append(" ,");
			}
		}
		return oscePostRoomId.toString();
	}

	public static List<PatientInSemester> findAllPatientInSemesterBySemesterAndAcceptedDay(
			Long semesterId) {

		Log.info("In side With Semester Id Is :" + semesterId);

		// String
		// query="select pis from PatientInSemester as pis where pis.semester="+semesterId
		// + " and pis.id in (select psod from pis.osceDays psod)";
		List<PatientInSemester> tempPatientInSemesters = PatientInSemester.findPatientInSemesterBySemester(semesterId);

		Set<OsceDay> osceDays;
		
		List<PatientInSemester> patientInSemesters = new ArrayList<PatientInSemester>();

		for (Iterator iterator = tempPatientInSemesters.iterator(); iterator.hasNext();){
			
			PatientInSemester patientInSemester = (PatientInSemester) iterator.next();

			osceDays = patientInSemester.getOsceDays();
			if ((osceDays != null && osceDays.size() > 0) && (patientInSemester.getAccepted()) ) {
				patientInSemesters.add(patientInSemester);
			}
			
		}
		
		return patientInSemesters;
	}
	
	public static Long getTotalRoleAssignInPost(Long postId){
		
		Log.info("Call getTotalRoleAssignInPost With post id :" + postId);	
		EntityManager em = entityManager();		
		String queryString = "select count(*) from PatientInRole as pir where pir.oscePost="+postId;
		Log.info("Query String: " + queryString);
		TypedQuery<Long> q = em.createQuery(queryString,Long.class);		
		Long result  = q.getSingleResult();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result);
		return result; 
	}
	
	public static Long totalTimesPatientAssignInSequence(Long osceSequenceId,Long PISId){
		Log.info("Call isPatientAssignInSequence With seq id :" +osceSequenceId + " PatientIn sem Id " + PISId);	
		EntityManager em = entityManager();		
		String queryString = "select count(*) from PatientInRole as pir,OscePost as op where op.osceSequence="+osceSequenceId + " and pir.oscePost=op.id and pir.patientInSemester="+PISId +" and pir.is_backup=false";
		Log.info("Query String: " + queryString);
		TypedQuery<Long> q = em.createQuery(queryString,Long.class);		
		Long result  = q.getSingleResult();        
		Log.info("Total Role For this sequence is : "+result);
		return result;
				
	}

	//module 3 f }
	
	//Module10 Create plans

	 public static Long findOsceIdByOsceName(String osceName)
	 {
		 	Log.info("Call findOsceIdByOsceName for Name" + osceName);	
			EntityManager em = entityManager();		
			String queryString = "select o.id from Osce as o where name= '"+osceName +"'";
			Log.info("Query String: " + queryString);
			TypedQuery<Long> q = em.createQuery(queryString,Long.class);		
			Long result  = q.getSingleResult();        
			Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result);
			return result;   
	 }
	//E Module10 Create plans

// TestCase Purpose Method {
 public static Osce findLastOsceCreatedForTestPurpose(){
		 Log.info("Inside findLastOsceCreatedForTestPurpose() ");	
			EntityManager em = entityManager();		
			String queryString = "select o from Osce as o order by o.id desc";
			Log.info("Query String: " + queryString);
			TypedQuery<Osce> q = em.createQuery(queryString,Osce.class);		
			List<Osce> result  = q.getResultList();        
			Log.info("Required OSce is : "+result.get(0).getId());
			return result.get(0);
	 }
// TestCase Purpose Method }
 
	
	/*
	List<OsceDayProxy> osceDayProxyList = osceProxyforFixedStatus.getOsce_days();
	if(osceDayProxyList.size()>0)
	{
		Iterator<OsceDayProxy> osceDayproxyIterator=osceDayProxyList.iterator();
		while(osceDayproxyIterator.hasNext())
		{
			
			OsceDayProxy osceDayProxy=osceDayproxyIterator.next();
			Set<AssignmentProxy> assignmentProxiesList=osceDayProxy.getAssignments();
			if(assignmentProxiesList.size()>0)
			{
				Iterator<AssignmentProxy> assignmentIterator=assignmentProxiesList.iterator();
				while(assignmentIterator.hasNext())
				{
					final AssignmentProxy assignmentProxy=assignmentIterator.next();
					Log.info("Assignment " + assignmentProxy.getId() + "is going to remove");
					requests.assignmentRequest().remove().using(assignmentProxy).fire(new OSCEReceiver<Void>() 
					{
						@Override
						public void onSuccess(Void response) 
						{
							Log.info("Assignment is removed");														
						}
					});
				}
				
			}
		}
*/
 		public  static boolean removeassignment(Osce osce) {	
 			//Assignment assignmentProxynew;
			
			for(OsceDay osceDay: osce.getOsce_days()) {
				Set<Assignment> assignmentProxiesList=osceDay.getAssignments();
				ArrayList<Assignment> listRemoveAssignment = new ArrayList<Assignment>();
				Iterator<Assignment> assignmentIterator=assignmentProxiesList.iterator();
				while(assignmentIterator.hasNext()) 
				{				
					Assignment assignmentProxy=assignmentIterator.next();
					listRemoveAssignment.add(assignmentProxy);
				}
				
				osceDay.getAssignments().remove(listRemoveAssignment);
				for(Assignment assignment: listRemoveAssignment)
				{
					Log.info("Assignment " + assignment.getId() + "is going to remove");
					osceDay.getAssignments().remove(assignment);
					assignment.remove();
					log.info("assignment removed");
				}
				
				
				/*Log.info("osceday id-- " + osceDay.getId() + "is going to remove");
				Set<Assignment> assignmentProxiesList=osceDay.getAssignments();*/
				/*if(assignmentProxiesList.size()>0)
				{
					Iterator<Assignment> assignmentIterator=assignmentProxiesList.iterator();
					
					while(assignmentIterator.hasNext())
					{
						Assignment assignmentProxy=assignmentIterator.next();
						Log.info("Assignment " + assignmentProxy.getId() + "is going to remove");
						assignmentProxynew=assignmentProxy;
						assignmentProxynew.remove();
						//assignmentIterator.next();
					}
					
				}*/
				
			}
			
			//spec bug sol
 			boolean flag = OscePostRoom.removeOscePostRoomForDoublePost(osce.getId());
 			//spec bug sol
 			
 			boolean test = PatientInRole.removePatientInRoleByOsceID(osce.getId());
 			
 			if (flag && test)
 			{	
 				return true;
 			}
 			else
 			{
 				return false;
 			}
 			
 			
		}


}
