package ch.unibas.medizin.osce.domain;

//import groovy.transform.ToString;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ch.unibas.medizin.osce.server.i18n.GWTI18N;
import ch.unibas.medizin.osce.server.manualoscettgen.ManualOsceTimeTableCalculation;
import ch.unibas.medizin.osce.server.spalloc.SPAllocator;
import ch.unibas.medizin.osce.server.spalloc.SPFederalAllocationUpdator;
import ch.unibas.medizin.osce.server.spalloc.SPFederalAllocator;
import ch.unibas.medizin.osce.server.ttgen.TimetableGenerator;
import ch.unibas.medizin.osce.shared.AllocationType;
import ch.unibas.medizin.osce.shared.AssignmentTypes;
import ch.unibas.medizin.osce.shared.MapOsceRole;
import ch.unibas.medizin.osce.shared.OSCESecurityStatus;
import ch.unibas.medizin.osce.shared.OsceCreationType;
import ch.unibas.medizin.osce.shared.OsceSecurityType;
import ch.unibas.medizin.osce.shared.OsceStatus;
import ch.unibas.medizin.osce.shared.PatientAveragePerPost;
import ch.unibas.medizin.osce.shared.PostType;
import ch.unibas.medizin.osce.shared.RoleTypes;
import ch.unibas.medizin.osce.shared.StudyYears;
import ch.unibas.medizin.osce.shared.i18n.OsceConstantsWithLookup;

@Configurable
@Entity
public class Osce {
	
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
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
    
	private Boolean assignSPForHalfDay=false;
	
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
    private static Logger Log = Logger.getLogger(Osce.class);
    // dk, 2012-02-10: split up m to n relationship since students
    // need flag whether they are enrolled or not
    //
    // @ManyToMany(cascade = CascadeType.ALL, mappedBy = "osces")
    // private Set<Student>   students = new HashSet<Student>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "osce")
    private Set<StudentOsces> osceStudents = new HashSet<StudentOsces>();
    
    @ManyToOne(cascade = CascadeType.ALL)
	private Osce copiedOsce;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "osce")
    private List<ItemAnalysis> itemAnalysis = new ArrayList<ItemAnalysis>();
   
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "osce")
    private List<PostAnalysis> postAnalysis = new ArrayList<PostAnalysis>();
    
    private OsceCreationType osceCreationType;
    
    private Boolean isFormativeOsce;
    /**
	 * Get number of slots until a SP change is necessary (lowest number of consecutive slots
	 * is defined by the most difficult role)
	 * 
	 * @return number of slots until SP change for most difficult role topic
	 */
	public int slotsOfMostDifficultRole() 
	{
		int slotsUntilChange = Integer.MAX_VALUE;
		 
	
		/*Iterator<OscePostBlueprint> it = getOscePostBlueprints().iterator();
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
		}*/
		
		EntityManager em = entityManager();
		String sql = "select min(rt.slotsUntilChange) from RoleTopic rt, OscePostBlueprint opb, StandardizedRole sr"
						+ " where rt.id = opb.roleTopic.id"
						+ " and opb.roleTopic.id = sr.roleTopic.id"
						+ " and sr.roleType <> " + RoleTypes.Material.ordinal()
						+ " and opb.osce = " + this.getId();	
		TypedQuery<Integer> query = em.createQuery(sql, Integer.class);
		if (query.getResultList().size() > 0)
			slotsUntilChange = query.getResultList().get(0);
		
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
    		new Osce().removeOsceData(osceId);    		
    		
    		Osce osce = Osce.findOsce(osceId);
    		TimetableGenerator optGen = TimetableGenerator.getOptimalSolution(osce);

    		// persist scaffold (osce days and all cascading entities)
    		optGen.createScaffold();
    	} catch(Exception e) {
    		e.printStackTrace();
    		return false;
    	}
    	return true;
    }
    
    @Transactional
    private void removeOsceData(Long osceId)
    {
    	Osce osce = Osce.findOsce(osceId);
		
		if (osce.getOsce_days() != null && osce.getOsce_days().size() == 1)
		{
			OsceDay osceDay = osce.getOsce_days().get(0);
			List<OsceSequence> osceSequences = osceDay.getOsceSequences();
			osceDay.setOsceSequences(new ArrayList<OsceSequence>());
			osceDay.persist();
			
			if (osceSequences != null && osceSequences.size() == 1)
			{	
				OsceSequence osceSeq = osceSequences.get(0);
				osceSeq.remove();
			}
			else if (osceSequences != null && osceSequences.size() == 2)
			{
				OsceSequence osceSeqA = osceSequences.get(0);
				OsceSequence osceSeqB = osceSequences.get(1);
				osceSeqA.remove();
				osceSeqB.remove();
			}
		}
    }
    
    public static Boolean generateAssignments(Long osceId) {
    	try {
    		//spec bug sol
    		boolean test = OscePostRoom.insertRecordForDoublePost(osceId);
    		//spec bug sol
    		
    		if(test)
    		{
    			TimetableGenerator optGen = TimetableGenerator.getOptimalSolution(Osce.findOsce(osceId));
    	    	//System.out.println(optGen.toString());
    	    	
    	    	Log.info("calling createAssignments()...");
    	    	
    	    	Set<Assignment> assignments = optGen.createAssignments();
    	    	Log.info("number of assignments created: " + assignments.size());
    		}
	    	
    	} catch(Exception e) {
    		e.printStackTrace();
    		return false;
    	}
    	return true;
    }
    
    public static Boolean autoAssignPatientInRole(Long osceId) {
    	try {
    		/*SPAllocator spAlloc = new SPAllocator(Osce.findOsce(osceId));
    		spAlloc.getSolution();
    		spAlloc.printSolution();
    		spAlloc.saveSolution();*/
    		
    		//spec[
    		Osce osce = Osce.findOsce(osceId);
    		
    		if (osce.getOsceSecurityTypes().equals(OsceSecurityType.federal))
    		{
    			Iterator<OsceDay> itr = OsceDay.findOsceDayByOsce(osceId).iterator();
        		
        		while (itr.hasNext())
        		{
        			OsceDay osceDay = itr.next();
        			SPFederalAllocator spFederalAllocator = new SPFederalAllocator(osceDay);
        			spFederalAllocator.allocateSp();
        		} 
    		}
    		else if (osce.getOsceSecurityTypes().equals(OsceSecurityType.simple))
    		{
    			Iterator<OsceDay> itr = OsceDay.findOsceDayByOsce(osceId).iterator();
        		
        		while (itr.hasNext())
        		{
        			OsceDay osceDay = itr.next();
        			SPAllocator spAlloc = new SPAllocator(osceDay,AllocationType.INITIAL);    			
            		spAlloc.getSolution();
            		spAlloc.printSolution();
            		spAlloc.saveSolution();
        		} 
    		}
    		
    		   		
    		//spec]
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	return Boolean.TRUE;
    }
   //Added for OMS-161.
    /**
     * Updating SPs assignment.
     * @param osceId
     * @return
     */
    public static Boolean updateAutoAssignmentOfPatientInRole(Long osceId) {
    	try {
    		
    		Osce osce = Osce.findOsce(osceId);
    		
    		if (osce.getOsceSecurityTypes().equals(OsceSecurityType.federal))
    		{
    			Iterator<OsceDay> itr = OsceDay.findOsceDayByOsce(osceId).iterator();
        		
        		while (itr.hasNext())
        		{
        			OsceDay osceDay = itr.next();
        			SPFederalAllocationUpdator spFederalAllocationUpdator = new SPFederalAllocationUpdator(osceDay);
        			spFederalAllocationUpdator.allocateSp();
        		} 
    		}
    		else if (osce.getOsceSecurityTypes().equals(OsceSecurityType.simple))
    		{
    			Iterator<OsceDay> itr = OsceDay.findOsceDayByOsce(osceId).iterator();
        		
        		while (itr.hasNext())
        		{
        			OsceDay osceDay = itr.next();
        			SPAllocator spAlloc = new SPAllocator(osceDay,AllocationType.UPDATE);    			
            		spAlloc.getSolution();
            		spAlloc.printSolution();
            		spAlloc.saveSolution();
        		} 
    		}
    		
    		   		
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	return Boolean.TRUE;
    }
    //spec start
    //This method assigns students in Assignment Table.
   
    public static Boolean autoAssignStudent(Long osceId,Integer orderBy, boolean changeRequire)
    {
    	Osce osce = new Osce();
    	return osce.autoStudentAssignToOsce(osceId, orderBy, changeRequire);
    }
    
    @Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
    public Boolean autoStudentAssignToOsce(Long osceId,Integer orderBy, boolean changeRequire)
    {
    	Osce osce = Osce.findOsce(osceId);
    	Map<Long, Map<Long, Map<Integer, List<PatientInRole>>>> spMap = null;
    	List<Assignment> examinerAssList = null;
    			
    	if (changeRequire == true)
    	{
    		
    		int totalStudent = osce.getMaxNumberStudents();
    		Integer actualStudent = StudentOsces.countStudentByOsce(osceId);
    		osce.setMaxNumberStudents(actualStudent);
    		osce.persist();
    		
    		//Integer remainingStudent = osce.getMaxNumberStudents() - actualStudent;
    		spMap = storeSPDataBeforeRemove(osce);
    		examinerAssList = storeExaminerDataBeforeRemove(osce);
    		
    		changeOsceStructure(osce);
    		
    		osce.setMaxNumberStudents(totalStudent);
    		osce.persist();
    	}
    	
    	EntityManager em = entityManager();
    	
    	try{
    	
    	
    	//retrieve distinct sequence number of student
    		String seqQuery="";
    		if(orderBy==0)
    			seqQuery="select distinct sequenceNumber from Assignment where type=0 and osceDay in (select id from OsceDay where osce="+osceId+") order by sequenceNumber";
    		else
    			seqQuery="select distinct sequenceNumber from Assignment where type=0 and osceDay in (select id from OsceDay where osce="+osceId+") ";
    	
    	//retrieve studentOsces  
    	String studentQuery="";
    	if(orderBy==0)
    		studentQuery="SELECT student FROM StudentOsces s where s.isEnrolled=true and osce="+osceId +" order by s.student.name,s.student.preName";
    	else
    		studentQuery="SELECT student FROM StudentOsces s where s.isEnrolled=true and osce="+osceId  ;
    	
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
    		if (seqList.size() > i)
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
    	}
   // 	em.getTransaction().commit();
    	if (spMap != null && spMap.size() > 0)
    		allocateStandardizedPatient(osce, spMap);
    		
    	if (examinerAssList != null && examinerAssList.size() > 0)
    		allocateExaminer(osceId, examinerAssList);
    	
    	return true;
    	}
    	catch (Exception e) {
			e.printStackTrace();
		//	em.getTransaction().rollback();
			return false;
		}    
    }
    
    private void allocateExaminer(Long osceId, List<Assignment> examinerAssList) {
    	Map<Long, Map<Integer, Date>> rotWiseStartTimeMap = new HashMap<Long, Map<Integer,Date>>();
    	Map<Long, Map<Integer, Date>> rotWiseEndTimeMap = new HashMap<Long, Map<Integer,Date>>();
    	List<OscePostRoom> oscePostRoomList = Assignment.findDistinctOscePostRoomOfStudentAssignment(osceId);
    	
    	for (OscePostRoom opr : oscePostRoomList)
    	{
    		Map<Integer, Date> startTimeMap = Assignment.findTimeStartByOscePostRoom(osceId, opr);
    		rotWiseStartTimeMap.put(opr.getId(), startTimeMap);
    		Map<Integer, Date> endTimeMap = Assignment.findTimeEndByOscePostRoom(osceId, opr);
    		rotWiseEndTimeMap.put(opr.getId(), endTimeMap);
    	}
    	
    	for (Assignment ass : examinerAssList)
    	{
    		//System.out.println("EXAMINER ID : " + ass.getExaminer().getId() + " OPR ID : " + ass.getOscePostRoom().getId());
    		Date timeStart = ass.getTimeStart();
    		Date timeEnd = ass.getTimeEnd();
    		Long oprId = ass.getOscePostRoom().getId();
    		
    		Map<Integer, Date> startTimeMap = rotWiseStartTimeMap.get(oprId);
    		if (startTimeMap.size() > 0)
    		{
    			if (startTimeMap.containsValue(timeStart) == false)
    			{
    				int preRotNo = -1;
        			for (Integer rotNo : startTimeMap.keySet())
        			{
        				if (preRotNo == -1) preRotNo = rotNo;
        				
        				if (startTimeMap.get(rotNo).before(timeStart) == false)
        				{
        					timeStart = startTimeMap.get(preRotNo);
        					preRotNo = -1;
        					break;
        				}
        				preRotNo = rotNo;
        			}
        			
        			if (preRotNo != -1)
        			{
        				timeStart = startTimeMap.get(preRotNo);
        			}
    			}
    		}    		
    		
    		Map<Integer, Date> endTimeMap = rotWiseEndTimeMap.get(oprId);    		
    		if (endTimeMap.size() > 0)
    		{
    			if (endTimeMap.containsValue(timeEnd) == false)
    			{
    				int preRotNo = -1;
    				for (Integer rotNo : endTimeMap.keySet())
    				{
    					if (preRotNo == -1) preRotNo = rotNo;
    					
    					if (timeEnd.after(endTimeMap.get(rotNo)) == false)
    					{
    						timeEnd = endTimeMap.get(preRotNo);
    						preRotNo = -1;
    						break;
    					}
    					preRotNo = rotNo;
    				}
    				
    				if (preRotNo != -1)
    				{
    					timeEnd = endTimeMap.get(preRotNo);
    				}
    			}
    		}
    		
    		Assignment assignment = new Assignment();
    		assignment.setSequenceNumber(ass.getSequenceNumber());
    		assignment.setTimeStart(timeStart);
    		assignment.setTimeEnd(timeEnd);
    		assignment.setType(ass.getType());
    		assignment.setExaminer(ass.getExaminer());
    		assignment.setOsceDay(ass.getOsceDay());
    		assignment.setOscePostRoom(ass.getOscePostRoom());
    		assignment.persist();
    	}   	
	}

	private void allocateStandardizedPatient(Osce osce, Map<Long, Map<Long, Map<Integer, List<PatientInRole>>>> osceDaySpMap) {
    	Map<Integer, List<PatientInRole>> pirMap = null;    	
    
    	for (OsceDay osceDay : osce.getOsce_days())
    	{
    		Map<Long, Map<Integer, List<PatientInRole>>> spMap = osceDaySpMap.get(osceDay.getId());    		
    		for (Long oscePostRoomId : spMap.keySet())
        	{
        		Long oprId = oscePostRoomId == null ? null : oscePostRoomId;
        		pirMap = spMap.get(oprId);
        		
        		//if oscepostroom is null then enter new entry of pir for break in assignment
        		if (oprId == null)
        		{    			
        			for (Integer seqNumber : pirMap.keySet())
        			{
        				List<PatientInRole> pirList = pirMap.get(seqNumber);
        				Assignment assignment = Assignment.findSpTimeBySequenceNumberAndOsceDay(osceDay.getId(), seqNumber);
    					if (assignment != null)
    					{
    						for (PatientInRole pir : pirList)
        					{
        						Assignment ass = new Assignment();
        						ass.setType(AssignmentTypes.PATIENT);
        						ass.setTimeEnd(assignment.getTimeEnd());
        						ass.setTimeStart(assignment.getTimeStart());
        						ass.setOsceDay(assignment.getOsceDay());
        						ass.setPatientInRole(pir);
        						ass.setSequenceNumber(seqNumber);
        						ass.setOscePostRoom(null);
        						ass.persist();
        					}
    					}   	
        			}
        		}
        		else
        		{
        			for (Integer seqNumber : pirMap.keySet())
            		{
            			if (pirMap != null)
            			{
            				List<PatientInRole> pirList = pirMap.get(seqNumber);
            				List<Assignment> assList = Assignment.findSpAssignmentByOscePostRoomAndSequenceNumberByOsceDay(oprId, osceDay.getId(), seqNumber);	
            				if (pirList != null)
            				{
            					if (pirList.size() == assList.size())
                				{
                					for (int i=0; i<pirList.size(); i++)
                					{
                						Assignment ass = assList.get(i);
                						ass.setPatientInRole(pirList.get(i));
                						ass.persist();
                					}
                				}
            				}
            			}    			
            		}
        		}
        	}  
    	}    		
	}

    private List<Assignment> storeExaminerDataBeforeRemove(Osce osce)
    {
    	List<Assignment> examinerAssList = Assignment.findExaminerAssignmentByOsce(osce.getId());
    	return examinerAssList;
    }
    	
    private Map<Long, Map<Long, Map<Integer, List<PatientInRole>>>> storeSPDataBeforeRemove(Osce osce)
    {
    	Map<Long, Map<Long, Map<Integer, List<PatientInRole>>>> osceDaySpMap = new HashMap<Long, Map<Long,Map<Integer,List<PatientInRole>>>>();
    	
    	Map<Long, Map<Integer, List<PatientInRole>>> spMap;
    	Map<Integer, List<PatientInRole>> pirMap = null;
    	try
    	{	
    		for (OsceDay osceDay : osce.getOsce_days())
    		{
    			spMap = new HashMap<Long, Map<Integer,List<PatientInRole>>>();
    			List<OscePostRoom> oscePostRoomList = Assignment.findDistinctOscePostRoomAssignmentByOsceDay(osceDay.getId());
            	oscePostRoomList.add(null);
            	//System.out.println("oscePostRoomList size : " + oscePostRoomList.size());
            	
            	for (OscePostRoom oscePostRoom : oscePostRoomList)
            	{
            		pirMap = new HashMap<Integer, List<PatientInRole>>();
            		
            		Long oprId = oscePostRoom == null ? null : oscePostRoom.getId();
            		List<Integer> sequenceNumberList = Assignment.findDistinctSPSequenceNumberByOscePostRoomByOsceDay(oprId, osceDay.getId());
            		
            		for (Integer seqNumber : sequenceNumberList)
            		{
            			List<PatientInRole> pirList = Assignment.findSPByOscePostRoomAndSequenceNumberByOsceDay(oprId, osceDay.getId(), seqNumber);
            			pirMap.put(seqNumber, pirList);
            		}
            		
            		if (pirMap != null)
            			spMap.put(oprId, pirMap);
            	}    	
            	osceDaySpMap.put(osceDay.getId(), spMap);            	
    		}        	
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return osceDaySpMap;
    }
    
	private void changeOsceStructure(Osce osce) {
    	
    	try 
    	{
    		String sql = "delete from assignment where osce_day in (select id from osce_day where osce = " + osce.getId() + ")";
    		int deletedCount = entityManager().createNativeQuery(sql).executeUpdate();
    		TimetableGenerator optGen = TimetableGenerator.getOptimalSolution(osce);
			optGen.createAssignments();
    	} catch(Exception e) {
    		e.printStackTrace();    		
    	}    	    	
	}

    public static List<Osce> findAllOsceBySemster(Long id) {
    	EntityManager em = entityManager();
    	//System.out.println("BEFORE CALL------->");
    	String queryString="SELECT o FROM Osce as o  where o.semester.id="+id + " order by studyYear desc";
    //	System.out.println("query--"+ queryString);
    	TypedQuery<Osce> q = em.createQuery(queryString, Osce.class);
    //	TypedQuery<Osce> q = em.createQuery("SELECT o FROM Osce as o  where o.semester=1 " , Osce.class);
    //	System.out.println("success done with size");
    	
    	return q.getResultList();
    }
    
    
    
    public static Osce findMaxOsce() {
        EntityManager em = entityManager();
        TypedQuery<Osce> query = em.createQuery("SELECT o FROM Osce o  where o.id=(select max(o.id) from Osce o)  ", Osce.class);

        
        return query.getSingleResult();
        
    }
    
    public static List<Osce> findAllOsceOnSemesterId(Long semesterId){
		EntityManager em = entityManager();
		TypedQuery<Osce> q = em.createQuery("SELECT o FROM Osce AS o WHERE o.semester = " + semesterId ,Osce.class);
		return q.getResultList();
				
	}
    
    public static List<Osce> findAllOsceBySemesterIdAndCreationType(Long semesterId, OsceCreationType osceCreationType){
		EntityManager em = entityManager();
		String sql = "SELECT o FROM Osce AS o WHERE o.semester.id = " + semesterId + " AND o.osceCreationType = " + osceCreationType.ordinal();
		TypedQuery<Osce> q = em.createQuery(sql, Osce.class);
		return q.getResultList();				
	}

 
 public static List<Osce> findAllOsceSemester(Long roleId , Date startDate, Date endDate){
		
 	Log.info("Inside Osce class To retrive all Osce  and semster Based On roleId");
		EntityManager em = entityManager();
		Log.info("start date--"+startDate);
		Log.info("end date--"+endDate);
		String query="";
		TypedQuery<Osce> q;
		if(startDate==null && endDate==null)
		{
			Log.info("start and end date null");
			 query="select o from Osce o, OscePost op,  OscePostBlueprint opb where  o.id=opb.osce and opb.id=op.oscePostBlueprint and op.standardizedRole=" +roleId;
			 q = em.createQuery(query  ,Osce.class);
		}
		else
		{
			Log.info("start and end date not null");
			 query="select distinct o from Osce o, OscePost op, OsceDay od, OscePostBlueprint opb where o.id=od.osce and od.osceDate>= :startdate  and od.osceDate<=:enddate and  o.id=opb.osce and opb.id=op.oscePostBlueprint and op.standardizedRole=" +roleId;
			 q = em.createQuery(query  ,Osce.class);
			 q.setParameter("startdate", startDate, TemporalType.TIMESTAMP);
			 q.setParameter("enddate", endDate, TemporalType.TIMESTAMP);
		}
		//String query="select o from Osce o, OscePost op, OsceDay od, OscePostBlueprint opb where o.id=od.osce and od.osceDate>="+startDate +" and od.osceDate<="+ endDate +" o.id=opb.osce and opb.id=op.oscePostBlueprint and op.standardizedRole=" +roleId; 
		//System.out.println("Query: " +query);
		
		return q.getResultList();
				
	}

	//public static List<Osce> findAllOscesGroupByCopiedOsce() {
 //       return entityManager().createQuery("SELECT o FROM Osce o GROUP BY o.copiedOsce", Osce.class).getResultList();
 //   }
    
	public static Integer initOsceBySecurity() {

		List<Osce> osces = findAllOsces();
		int i =0;

		for (Iterator<Osce> iterator = osces.iterator(); iterator.hasNext();) {
			Osce osce = (Osce) iterator.next();
			//System.out.println("osce.security" + osce.security);

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
		/*String queryString="select pis from PatientInSemester as pis where pis.id not in (select patientInSemester from PatientInRole) " +
				"and pis.id in ('' " +getPatientInSemesterIDList(osceDay.getPatientInSemesters()) +") " +
				"and pis.accepted=1 and pis.semester="+semesterId;*/
					Log.info(queryString);
				TypedQuery<PatientInSemester> q = em.createQuery(queryString,PatientInSemester.class);
				 
				return q.getResultList();

	}
	public static List<PatientInSemester> getAcceptedPISAndNotAssignForThatDay(OsceDay osceDay,Long semesterID){
		
		EntityManager em = entityManager();
		
		Log.info("Inside getAcceptedPISAndNotAssignForThatDay () with day " + osceDay.getId() + " Sem id :" + semesterID);	
	
		String queryString="select ps from PatientInSemester as ps where ps.id in ('' " +getPatientInSemesterIDList(osceDay.getPatientInSemesters()) +") " +
		"and ps.id not in (select pr.patientInSemester from PatientInRole as pr, OscePost as op, OsceSequence as os, OsceDay  as od where od.id="+osceDay.getId() +" and os.osceDay= od.id "+
		" and op.osceSequence = os.id 	and pr.oscePost = op.id)";
		
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

	/*public static List<OscePost> findOscePostsinWhichSPFits(Long semesterId,List<OscePost>allOscePostOfThisDay,PatientInSemester sortedPatientInSemester2){
		
		Log.info("Semester Id AT findOscePostsinWhichSPFits() Is :" +semesterId );
		List<PatientInSemester> listOfPatientInSemesterSatisfyCriteria = new ArrayList<PatientInSemester>();
		Set<AdvancedSearchCriteria> setAdvanceSearchCriteria = new HashSet<AdvancedSearchCriteria>();
		List<AdvancedSearchCriteria> listAdvanceSearchCriteria = new ArrayList<AdvancedSearchCriteria>();
		
		List<OscePost> resultList =new ArrayList<OscePost>();
		
		OscePost oscePost;
		StandardizedRole standardizedRole;
		Iterator<OscePost> oscePostIterator = allOscePostOfThisDay.iterator();
		
		while(oscePostIterator.hasNext()){
			
			setAdvanceSearchCriteria.clear();
			listOfPatientInSemesterSatisfyCriteria.clear();
			listAdvanceSearchCriteria.clear();
			
			oscePost=oscePostIterator.next();
			
			standardizedRole=oscePost.getStandardizedRole();
			
			setAdvanceSearchCriteria=standardizedRole.getAdvancedSearchCriteria();
			
			Log.info("Search Criteria size :" + setAdvanceSearchCriteria.size());
			
			if(setAdvanceSearchCriteria==null || setAdvanceSearchCriteria.size() <=0 ){
				//continue;
				resultList.add(oscePost);
			}
			else{
			listAdvanceSearchCriteria.addAll(setAdvanceSearchCriteria);
			
			listOfPatientInSemesterSatisfyCriteria=PatientInSemester.findPatientInSemesterByAdvancedCriteria(semesterId,listAdvanceSearchCriteria);
			
			if(listOfPatientInSemesterSatisfyCriteria != null && listOfPatientInSemesterSatisfyCriteria.size() > 0){
				
				if(listOfPatientInSemesterSatisfyCriteria.contains(sortedPatientInSemester2)){
					resultList.add(oscePost);
				}
				
			}
		}
		}
		Log.info("Criteria Setisfy For Roles Is :"+resultList.size()+" For Patient :" +sortedPatientInSemester2.getId());
		
		return resultList;
	}*/
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
	
	private static String getOscePostIDList(
			List<OscePost> oscePostlist) {

		if (oscePostlist == null || oscePostlist.size() == 0) {
			Log.info("Return as null");
			return "";
		}
		Iterator<OscePost> oscePosstlistIterator = oscePostlist.iterator();
		StringBuilder oscePostId = new StringBuilder();
		oscePostId.append(",");
		while (oscePosstlistIterator.hasNext()) {
			OscePost oscePost = oscePosstlistIterator.next();

			oscePostId.append(oscePost.getId().toString());
			if (oscePosstlistIterator.hasNext()) {
				oscePostId.append(" ,");
			}
		}
		
		return oscePostId.toString();
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
		List<PatientInSemester> tempPatientInSemesters = PatientInSemester.findPatientInSemesterBySemester(semesterId,true,"");

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

	public static Long getCountOfSPAssigndAsBackups(List<OscePost> allOscePostOfThisDay){
		Log.info("Call getCountOfSPAssigndAsBackups");	
		EntityManager em = entityManager();		
		String queryString = "select count(*) from PatientInRole as pir where pir.oscePost in (' '" +getOscePostIDList(allOscePostOfThisDay) + " ) and pir.is_backup=1";
		Log.info("Query String: " + queryString);
		TypedQuery<Long> q = em.createQuery(queryString,Long.class);		
		Long result  = q.getSingleResult();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result);
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
				
				osceDay.setLunchBreakAdjustedTime(0);
				osceDay.persist();
				
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
					Log.info("assignment removed");
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

 		/* public static List<MapOsceRole> findAllOsceSemesterByStandardizedRole(Long roleId , Date startDate, Date endDate){
 			
 		 	Log.info("Inside Osce class To retrive all Osce  and semster Based On roleId");
 				EntityManager em = entityManager();
 				List<MapOsceRole> mapOsceRoleProxyList=new ArrayList<MapOsceRole>();
 				Log.info("start date--"+startDate);
 				Log.info("end date--"+endDate);
 				String query="";
 				TypedQuery<Osce> q;
 				if(startDate==null && endDate==null)
 				{
 					Log.info("start and end date null");
 					 query="select o from Osce o, OscePost op,  OscePostBlueprint opb where  o.id=opb.osce and opb.id=op.oscePostBlueprint and op.standardizedRole=" +roleId;
 					 q = em.createQuery(query  ,Osce.class);
 				}
 				else
 				{
 					Log.info("start and end date not null");
 					 query="select distinct o from Osce o, OscePost op, OsceDay od, OscePostBlueprint opb where o.id=od.osce and od.osceDate>= :startdate  and od.osceDate<=:enddate and  o.id=opb.osce and opb.id=op.oscePostBlueprint and op.standardizedRole=" +roleId;
 					 q = em.createQuery(query  ,Osce.class);
 					 q.setParameter("startdate", startDate, TemporalType.TIMESTAMP);
 					 q.setParameter("enddate", endDate, TemporalType.TIMESTAMP);
 				}
 				//String query="select o from Osce o, OscePost op, OsceDay od, OscePostBlueprint opb where o.id=od.osce and od.osceDate>="+startDate +" and od.osceDate<="+ endDate +" o.id=opb.osce and opb.id=op.oscePostBlueprint and op.standardizedRole=" +roleId; 
 				//System.out.println("Query: " +query);
 				
 				List lst = q.getResultList();
 		        

 		        for (int i = 0; i < lst.size(); i++) {
 		        	MapOsceRole mapOsceRoleProxy=new MapOsceRole();
  		            mapOsceRoleProxyList.add(mapOsceRoleProxy);
 		           
 		        }
 		        
 				return mapOsceRoleProxyList;
 						
 			}*/
 		 
 		public static List<MapOsceRole> findAllOsceSemesterByRole(List<Long>  standardizedRoleId, Date startDate, Date endDate){
 			List<MapOsceRole> mapOsceRoleProxyList=new ArrayList<MapOsceRole>();
 			try
 			{
 				OsceConstantsWithLookup enumConstants = enumConstants = GWTI18N.create(OsceConstantsWithLookup.class, "de");
 	 			
 	 		 	Log.info("Inside Osce class To retrive all Osce  and semster Based On roleId");
 				EntityManager em = entityManager();
 				
 				String query="";
 				Query q;
 				if(startDate==null && endDate==null)
 				{	
 					
 					query="select distinct o, op.standardizedRole,o.semester from Osce o, OscePost op,  OscePostBlueprint opb where  o.id=opb.osce and opb.id=op.oscePostBlueprint and op.standardizedRole in(" + StringUtils.join(standardizedRoleId, ",") +")";
 					// q = em.createQuery(query  ,Osce.class);
 					 q = em.createQuery(query);
 				}
 				else
 				{
 					 query="select distinct o,op.standardizedRole,o.semester from Osce o, OscePost op, OsceDay od, OscePostBlueprint opb where o.id=od.osce and od.osceDate>= :startdate  and od.osceDate<=:enddate and  o.id=opb.osce and opb.id=op.oscePostBlueprint and op.standardizedRole in(" + StringUtils.join(standardizedRoleId, ",") +")";
 					 // q = em.createQuery(query  ,Osce.class);
 					 q = em.createQuery(query);
 					 q.setParameter("startdate", startDate, TemporalType.TIMESTAMP);
 					 q.setParameter("enddate", endDate, TemporalType.TIMESTAMP);
 				}
 				//String query="select o from Osce o, OscePost op, OsceDay od, OscePostBlueprint opb where o.id=od.osce and od.osceDate>="+startDate +" and od.osceDate<="+ endDate +" o.id=opb.osce and opb.id=op.oscePostBlueprint and op.standardizedRole=" +roleId; 
 				List lst = q.getResultList();
 		     
 		        for (int i = 0; i < lst.size(); i++) {
 		        	MapOsceRole mapOsceRoleProxy=new MapOsceRole();
  		            
  		            StandardizedRole standardizedRole=(StandardizedRole)(((Object[]) lst.get(i))[1]);  		            
  		           
  		            Osce osce = (Osce)((Object[]) lst.get(i))[0];
  		            mapOsceRoleProxy.setOsce(osce.getStudyYear() == null ? "" : enumConstants.getString(osce.getStudyYear().toString()));
  		            mapOsceRoleProxy.setStandandarizeRoleVersion(standardizedRole.getMainVersion()+"."+standardizedRole.getSubVersion());
  		            Semester semester = (Semester)((Object[]) lst.get(i))[2];
  		            mapOsceRoleProxy.setSemester(semester == null ? "" : (semester.getSemester() + " " + semester.getCalYear()));
  		     
  		            mapOsceRoleProxyList.add(mapOsceRoleProxy);
 		           
 		        }
 		        
 				return mapOsceRoleProxyList;
 			}
 			catch (Exception e)
 			{
 				Log.error(e.getMessage(), e);
 			}
 			return mapOsceRoleProxyList;
 		}
 		
 		
 	public static void createOsceDaySequeceAndCourse(Long osceId)
 	{
 		Osce osce = Osce.findOsce(osceId);
 		
 		Calendar calendar = Calendar.getInstance();
 		calendar.setTime(new Date());
 		calendar.set(Calendar.HOUR_OF_DAY, 0);
 		calendar.set(Calendar.MINUTE, 0);
 		calendar.set(Calendar.SECOND, 0);
 		Date osceDate = calendar.getTime(); 		
 		
 		calendar.set(Calendar.HOUR_OF_DAY, 8);
 		calendar.set(Calendar.MINUTE, 0);
 		calendar.set(Calendar.SECOND, 0);
 		Date startTime = calendar.getTime();
 		
 		OsceDay osceDay = new OsceDay();
 		osceDay.setOsceDate(osceDate);
 		osceDay.setTimeStart(startTime);
 		osceDay.setTimeEnd(startTime);
 		osceDay.setOsce(osce);
 		osceDay.persist();
 		
 		OsceSequence osceSequence = new OsceSequence();
 		osceSequence.setLabel("A");
 		osceSequence.setNumberRotation(1);
 		osceSequence.setOsceDay(osceDay);
 		osceSequence.persist();
 		
 		Course course = new Course();
 		course.setColor("color_1");
 		course.setOsce(osce);
 		course.setOsceSequence(osceSequence);
 		course.persist();
 	}
 	
 	public static List<Osce> findAllOsceByStatusAndSemesterId(Long semesterId){
		EntityManager em = entityManager();
		TypedQuery<Osce> q = em.createQuery("SELECT o FROM Osce AS o WHERE o.semester = " + semesterId + " AND o.osceStatus != " + OsceStatus.OSCE_BLUEPRINT.ordinal(),Osce.class);
		return q.getResultList();
				
	}
 	
 	public static void calculateManualOsce(Long osceId)
 	{
 		try
 		{
 			Osce osce = Osce.findOsce(osceId);
 	 		
 	 		ManualOsceTimeTableCalculation manualOsceTimeTableCalculation = new ManualOsceTimeTableCalculation(osce);
 	 		manualOsceTimeTableCalculation.calculateTime2();
 	 		//manualOsceTimeTableCalculation.printResult();
 	 		manualOsceTimeTableCalculation.saveOsceData();
 		}
 		catch (Exception e) {
 			e.printStackTrace();
 		} 		
 	}
 	
 	public static String createAssignmentInManualOsce(Long osceId)
 	{
 		try
 		{
 			Osce osce = Osce.findOsce(osceId);
 	 		
 			List<OscePost> oscePostList = OscePost.findOscePostOfNullRoleByOsceId(osceId);
 			
 			if (oscePostList.isEmpty())
 			{
 				List<OscePostRoom> oscePostRoomList = OscePostRoom.findOscePostRoomOfNullRoomByOsceId(osceId);
 				
 				if (oscePostRoomList.isEmpty())
 				{
 					ManualOsceTimeTableCalculation manualOsceTimeTableCalculation = new ManualOsceTimeTableCalculation(osce);
 	 	 	 		manualOsceTimeTableCalculation.calculateTime2();
 	 	 	 		manualOsceTimeTableCalculation.createAssignment();
 	 	 	 		
 	 	 	 		osce.setOsceStatus(OsceStatus.OSCE_CLOSED);
 	 	 	 		osce.persist();
 	 	 	 		
 	 	 	 		return "";
 				}
 				
 				return "manualOsceClostRoomError";
 			}
 			
 	 		return "manualOsceClostRoleError";
 		}
 		catch (Exception e) {
 			Log.error(e.getMessage(), e);
 			return "manualOsceCloseError";
 		} 		
 	}
 	
 	public static Osce clearAllManualOsce(Long osceId)
 	{
 		try
 		{
 			Osce osce = Osce.findOsce(osceId);
 	 		
 	 		List<OsceDay> osceDayList = osce.getOsce_days();
 	 		
 	 		if (osceDayList.size() > 0)
 	 		{
 	 			OsceDay oldOsceDay = osceDayList.get(0);
 	 			Date timeStart = oldOsceDay.getTimeStart();
 	 			Date osceDate = oldOsceDay.getOsceDate();
 	 			
 	 			List<Long> removeOsceDayIdList = new ArrayList<Long>();
 	 			for (OsceDay osceDay : osceDayList)
 	 			{
 	 				removeOsceDayIdList.add(osceDay.getId());
 	 			}
 	 			
 	 			for (int i=0; i<removeOsceDayIdList.size(); i++)
 	 			{
 	 				OsceDay osceDay = OsceDay.findOsceDay(removeOsceDayIdList.get(i));
 	 				osce.getOsce_days().remove(osceDay);
 	 				if (osceDay != null)
 	 					osceDay.remove();
 	 			}
 	 			
 	 			List<OscePostBlueprint> oscePostBlueprintList = osce.getOscePostBlueprints();
 	 			List<Long> opbIdList = new ArrayList<Long>();
 	 			for (OscePostBlueprint oscePostBlueprint : oscePostBlueprintList)
 	 			{
 	 				opbIdList.add(oscePostBlueprint.getId());
 	 			}
 	 			
 	 			for (int i=0; i<opbIdList.size(); i++)
 	 			{
 	 				OscePostBlueprint oscePostBlueprint = OscePostBlueprint.findOscePostBlueprint(opbIdList.get(i));
 	 				osce.getOscePostBlueprints().remove(oscePostBlueprint);
 	 				
 	 				if (oscePostBlueprint != null)
 	 					oscePostBlueprint.remove();
 	 			}
 	 			
 	 			OsceDay osceDay = new OsceDay();
 	 	 		osceDay.setOsceDate(osceDate);
 	 	 		osceDay.setTimeStart(timeStart);
 	 	 		osceDay.setTimeEnd(timeStart);
 	 	 		osceDay.setOsce(osce);
 	 	 		osceDay.persist();
 	 	 		
 	 	 		OsceSequence osceSequence = new OsceSequence();
 	 	 		osceSequence.setLabel("A");
 	 	 		osceSequence.setNumberRotation(1);
 	 	 		osceSequence.setOsceDay(osceDay);
 	 	 		osceSequence.persist();
 	 	 		
 	 	 		Course course = new Course();
 	 	 		course.setColor("color_1");
 	 	 		course.setOsce(osce);
 	 	 		course.setOsceSequence(osceSequence);
 	 	 		course.persist();
 	 	 		
 	 	 		List<Course> courseList = new ArrayList<Course>();
 	 	 		courseList.add(course);
 	 	 		osceSequence.setCourses(courseList);
 	 	 		
 	 	 		List<OsceSequence> osceSequenceList = new ArrayList<OsceSequence>();
 	 	 		osceSequenceList.add(osceSequence);
 	 	 		osceDay.setOsceSequences(osceSequenceList);
 	 	 		
 	 	 		List<OsceDay> newOsceDayList = new ArrayList<OsceDay>();
 	 	 		newOsceDayList.add(osceDay);
 	 	 		osce.setOsce_days(newOsceDayList);
 	 		}
 	 		
 	 		osce.setOsceStatus(OsceStatus.OSCE_NEW);
 	 		osce.persist();
 	 		
 	 		return osce;
 		}
 		catch (Exception e) {
 			e.printStackTrace();
 		}
 		return null;	
 	}
 	
 	@Transactional
 	public void deleteOsceDayAndBlueprintByOsceId(Long osceId)
 	{
 		String sql = "delete from osce_day where osce = " + osceId;
		entityManager().createNativeQuery(sql).executeUpdate();
 	}

 	public static Osce changeOsceStatus(Long osceId, OsceStatus osceStatus)
 	{
 		Osce osce = Osce.findOsce(osceId);
 		boolean flag = true; 
 		
 		for (OsceDay osceDay : osce.getOsce_days())
 		{
 			if (osceDay.getOsceDayRotations().isEmpty())
 			{
 				flag = false;
 				break;
 			}
 		}
 		
 		if (flag)
 		{
 			osce.setOsceStatus(osceStatus);
 	 		osce.persist(); 	 		
 	 		return osce;
 		}
 		else
 		{
 			return null;
 		}
 		
 	}
 	
 	public static List<Osce> findOsceBySemesterId(Long semesterId) {
 		EntityManager em = entityManager();
 		String sql = "SELECT o FROM Osce o WHERE o.osceStatus = " + OsceStatus.OSCE_CLOSED.ordinal() + " AND o.semester.id = " + semesterId;
 		TypedQuery<Osce> query = em.createQuery(sql, Osce.class);
 		return query.getResultList();
 	}

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AssignSPForHalfDay: ").append(getAssignSPForHalfDay()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("IsFormativeOsce: ").append(getIsFormativeOsce()).append(", ");
        sb.append("IsRepeOsce: ").append(getIsRepeOsce()).append(", ");
        sb.append("IsValid: ").append(getIsValid()).append(", ");
        sb.append("ItemAnalysis: ").append(getItemAnalysis() == null ? "null" : getItemAnalysis().size()).append(", ");
        sb.append("LongBreak: ").append(getLongBreak()).append(", ");
        sb.append("LongBreakRequiredTime: ").append(getLongBreakRequiredTime()).append(", ");
        sb.append("LunchBreak: ").append(getLunchBreak()).append(", ");
        sb.append("LunchBreakRequiredTime: ").append(getLunchBreakRequiredTime()).append(", ");
        sb.append("MaxNumberStudents: ").append(getMaxNumberStudents()).append(", ");
        sb.append("MiddleBreak: ").append(getMiddleBreak()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("NumberCourses: ").append(getNumberCourses()).append(", ");
        sb.append("NumberRooms: ").append(getNumberRooms()).append(", ");
        sb.append("OsceCreationType: ").append(getOsceCreationType()).append(", ");
        sb.append("OscePostBlueprints: ").append(getOscePostBlueprints() == null ? "null" : getOscePostBlueprints().size()).append(", ");
        sb.append("OsceSecurityTypes: ").append(getOsceSecurityTypes()).append(", ");
        sb.append("OsceStatus: ").append(getOsceStatus()).append(", ");
        sb.append("OsceStudents: ").append(getOsceStudents() == null ? "null" : getOsceStudents().size()).append(", ");
        sb.append("Osce_days: ").append(getOsce_days() == null ? "null" : getOsce_days().size()).append(", ");
        sb.append("PatientAveragePerPost: ").append(getPatientAveragePerPost()).append(", ");
        sb.append("PostAnalysis: ").append(getPostAnalysis() == null ? "null" : getPostAnalysis().size()).append(", ");
        sb.append("PostLength: ").append(getPostLength()).append(", ");
        sb.append("Security: ").append(getSecurity()).append(", ");
        sb.append("Semester: ").append(getSemester()).append(", ");
        sb.append("ShortBreak: ").append(getShortBreak()).append(", ");
        sb.append("ShortBreakSimpatChange: ").append(getShortBreakSimpatChange()).append(", ");
        sb.append("SpStayInPost: ").append(getSpStayInPost()).append(", ");
        sb.append("StudyYear: ").append(getStudyYear()).append(", ");
        sb.append("Tasks: ").append(getTasks() == null ? "null" : getTasks().size()).append(", ");
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
            Osce attached = Osce.findOsce(this.id);
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
    public Osce merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Osce merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new Osce().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countOsces() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Osce o", Long.class).getSingleResult();
    }

	public static List<Osce> findAllOsces() {
        return entityManager().createQuery("SELECT o FROM Osce o", Osce.class).getResultList();
    }

	public static Osce findOsce(Long id) {
        if (id == null) return null;
        return entityManager().find(Osce.class, id);
    }

	public static List<Osce> findOsceEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Osce o", Osce.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public StudyYears getStudyYear() {
        return this.studyYear;
    }

	public void setStudyYear(StudyYears studyYear) {
        this.studyYear = studyYear;
    }

	public Integer getMaxNumberStudents() {
        return this.maxNumberStudents;
    }

	public void setMaxNumberStudents(Integer maxNumberStudents) {
        this.maxNumberStudents = maxNumberStudents;
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public Short getShortBreak() {
        return this.shortBreak;
    }

	public void setShortBreak(Short shortBreak) {
        this.shortBreak = shortBreak;
    }

	public Short getShortBreakSimpatChange() {
        return this.shortBreakSimpatChange;
    }

	public void setShortBreakSimpatChange(Short shortBreakSimpatChange) {
        this.shortBreakSimpatChange = shortBreakSimpatChange;
    }

	public Short getLongBreak() {
        return this.LongBreak;
    }

	public void setLongBreak(Short LongBreak) {
        this.LongBreak = LongBreak;
    }

	public Short getLunchBreak() {
        return this.lunchBreak;
    }

	public void setLunchBreak(Short lunchBreak) {
        this.lunchBreak = lunchBreak;
    }

	public Short getMiddleBreak() {
        return this.middleBreak;
    }

	public void setMiddleBreak(Short middleBreak) {
        this.middleBreak = middleBreak;
    }

	public Short getLunchBreakRequiredTime() {
        return this.lunchBreakRequiredTime;
    }

	public void setLunchBreakRequiredTime(Short lunchBreakRequiredTime) {
        this.lunchBreakRequiredTime = lunchBreakRequiredTime;
    }

	public Short getLongBreakRequiredTime() {
        return this.longBreakRequiredTime;
    }

	public void setLongBreakRequiredTime(Short longBreakRequiredTime) {
        this.longBreakRequiredTime = longBreakRequiredTime;
    }

	public Integer getNumberCourses() {
        return this.numberCourses;
    }

	public void setNumberCourses(Integer numberCourses) {
        this.numberCourses = numberCourses;
    }

	public Integer getPostLength() {
        return this.postLength;
    }

	public void setPostLength(Integer postLength) {
        this.postLength = postLength;
    }

	public Boolean getIsRepeOsce() {
        return this.isRepeOsce;
    }

	public void setIsRepeOsce(Boolean isRepeOsce) {
        this.isRepeOsce = isRepeOsce;
    }

	public Integer getNumberRooms() {
        return this.numberRooms;
    }

	public void setNumberRooms(Integer numberRooms) {
        this.numberRooms = numberRooms;
    }

	public Boolean getIsValid() {
        return this.isValid;
    }

	public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

	public OsceStatus getOsceStatus() {
        return this.osceStatus;
    }

	public void setOsceStatus(OsceStatus osceStatus) {
        this.osceStatus = osceStatus;
    }

	public OSCESecurityStatus getSecurity() {
        return this.security;
    }

	public void setSecurity(OSCESecurityStatus security) {
        this.security = security;
    }

	public OsceSecurityType getOsceSecurityTypes() {
        return this.osceSecurityTypes;
    }

	public void setOsceSecurityTypes(OsceSecurityType osceSecurityTypes) {
        this.osceSecurityTypes = osceSecurityTypes;
    }

	public PatientAveragePerPost getPatientAveragePerPost() {
        return this.patientAveragePerPost;
    }

	public void setPatientAveragePerPost(PatientAveragePerPost patientAveragePerPost) {
        this.patientAveragePerPost = patientAveragePerPost;
    }

	public Boolean getSpStayInPost() {
        return this.spStayInPost;
    }

	public void setSpStayInPost(Boolean spStayInPost) {
        this.spStayInPost = spStayInPost;
    }

	public Boolean getAssignSPForHalfDay() {
        return this.assignSPForHalfDay;
    }

	public void setAssignSPForHalfDay(Boolean assignSPForHalfDay) {
        this.assignSPForHalfDay = assignSPForHalfDay;
    }

	public Semester getSemester() {
        return this.semester;
    }

	public void setSemester(Semester semester) {
        this.semester = semester;
    }

	public List<OsceDay> getOsce_days() {
        return this.osce_days;
    }

	public void setOsce_days(List<OsceDay> osce_days) {
        this.osce_days = osce_days;
    }

	public List<OscePostBlueprint> getOscePostBlueprints() {
        return this.oscePostBlueprints;
    }

	public void setOscePostBlueprints(List<OscePostBlueprint> oscePostBlueprints) {
        this.oscePostBlueprints = oscePostBlueprints;
    }

	public Set<Task> getTasks() {
        return this.tasks;
    }

	public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

	public Set<StudentOsces> getOsceStudents() {
        return this.osceStudents;
    }

	public void setOsceStudents(Set<StudentOsces> osceStudents) {
        this.osceStudents = osceStudents;
    }

	public Osce getCopiedOsce() {
        return this.copiedOsce;
    }

	public void setCopiedOsce(Osce copiedOsce) {
        this.copiedOsce = copiedOsce;
    }

	public List<ItemAnalysis> getItemAnalysis() {
        return this.itemAnalysis;
    }

	public void setItemAnalysis(List<ItemAnalysis> itemAnalysis) {
        this.itemAnalysis = itemAnalysis;
    }

	public List<PostAnalysis> getPostAnalysis() {
        return this.postAnalysis;
    }

	public void setPostAnalysis(List<PostAnalysis> postAnalysis) {
        this.postAnalysis = postAnalysis;
    }

	public OsceCreationType getOsceCreationType() {
        return this.osceCreationType;
    }

	public void setOsceCreationType(OsceCreationType osceCreationType) {
        this.osceCreationType = osceCreationType;
    }

	public Boolean getIsFormativeOsce() {
        return this.isFormativeOsce;
    }

	public void setIsFormativeOsce(Boolean isFormativeOsce) {
        this.isFormativeOsce = isFormativeOsce;
    }
}
