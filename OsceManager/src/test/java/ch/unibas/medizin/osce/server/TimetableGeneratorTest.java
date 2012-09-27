package ch.unibas.medizin.osce.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.domain.Course;
import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.domain.OsceDay;
import ch.unibas.medizin.osce.domain.OscePostBlueprint;
import ch.unibas.medizin.osce.domain.OscePostRoom;
import ch.unibas.medizin.osce.domain.RoleTopic;
import ch.unibas.medizin.osce.domain.Semester;
import ch.unibas.medizin.osce.domain.Specialisation;
import ch.unibas.medizin.osce.server.ttgen.TimetableGenerator;
import ch.unibas.medizin.osce.shared.AssignmentTypes;
import ch.unibas.medizin.osce.shared.OsceStatus;
import ch.unibas.medizin.osce.shared.PostType;
import ch.unibas.medizin.osce.shared.StudyYears;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.SimpleEventBus;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/applicationContext.xml"})
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=false)
@Transactional(noRollbackFor=AssertionError.class)

public class TimetableGeneratorTest {
	

	SimpleEventBus eventBus = new SimpleEventBus();
	//OsMaRequestFactory requestFactory = RequestFactoryMagic.create(OsMaRequestFactory.class);
	public static Boolean isInitialization=true;
	
	Osce osce = null;
	SemesterProxy semester;	
	Boolean isBluePrintGenrated=false;
	private List<OsceDay> osceDayList=new ArrayList<OsceDay>();
	private List<OsceDay> osceDay_List = new ArrayList<OsceDay>();
	private List<OscePostBlueprint> oscePostBluePrintListPersist = new ArrayList<OscePostBlueprint>();
	private List<Assignment> assignmentList = new ArrayList<Assignment>();
	private List<Course> courseList= new ArrayList<Course>();
	private List<Integer> sequenceNumberList = new ArrayList<Integer>();
	OsceDay osceDay;
	OsceDay newOsceDay;
	List<OscePostBlueprint> oscePostBluePrintList;
	int oscePostBluePrint=0;
	private OscePostRoom oscePostRoom1;
	private OscePostRoom oscePostRoom2;
	private Map<Integer, Date> assignmentMap1 = new HashMap<Integer, Date>();
	private Map<Integer, Date> assignmentMap2 = new HashMap<Integer, Date>();
	private boolean result=false;
	
	// Specify Osce Values to create new Osce for TestCase.

	Short shortBreak=1;
	Short longBreak=15;
	Boolean isrepeatOSce=false;
	Boolean isValid=true;
	Short lunchBreak=45;
	int maxNumberStudent=130;
	Short middleBreak=5;
	String name="Testing";
	int numberCourses=0;
	int totalRooms=16;
	OsceStatus osceStatus=OsceStatus.OSCE_GENRATED;
	int postLenth=13;
	Short simpatChange=3;
	StudyYears studyYear= StudyYears.SJ4;
	Long semesterId=1l;
	
	// Specify OsceDay Values to create new OsceDay for TestCase.

	Date osceDate= new Date(2012-1900, 06, 18, 00, 00, 00);
	Date startTime = new Date(2012-1900, 06, 18, 9, 00, 00);
	Date endtTime = new Date(2012-1900, 06, 18, 19, 00, 00);
	
	
	
	
	@SuppressWarnings("deprecation")
	@Before
	public void setUp() throws Exception {	
		
		System.out.println("setup start");
		
		if(isInitialization){
			isInitialization=false;
			try {
				
				//requestFactory.initialize(eventBus,new InProcessRequestTransport(new SimpleRequestProcessor(ServiceLayer.create())));
				
				osce=createNewOsceForTest(shortBreak,longBreak,isrepeatOSce,isValid,lunchBreak,maxNumberStudent,middleBreak,name,numberCourses,totalRooms,osceStatus,postLenth,simpatChange,studyYear,semesterId);
				Log.info("Osce Is  Genrated With Id :" + osce.getId());
				
				osceDay=createOsceDayForTest(osce, osceDate, startTime, endtTime);
				Log.info("OsceDay Is  Genrated With Id :" + osceDay.getId());
				
				osceDayList.add(osceDay);
				
				osce.setOsce_days(osceDayList);
				//osce.persist();
				
				isBluePrintGenrated=createBluePrint(osce);
				Log.info(" Is Osce BluePrint Is  Genrated ? :" + isBluePrintGenrated);
				Log.info("OSce Post Blue print Size :" + oscePostBluePrintListPersist.size());
				
				osce.setOscePostBlueprints(oscePostBluePrintListPersist);
				//osce.persist();
				
				if(isBluePrintGenrated){
	
					TimetableGenerator optGen = TimetableGenerator.getOptimalSolution(osce);
		    		optGen.createScaffold();
		    		Log.info("Scaffold Genrated :");
					
		    		Set<Assignment> assignments = optGen.createAssignments();
			    	Log.info("number of assignments created: " + assignments.size());
				}
		    	System.out.println("setup end :");
	    		} catch(Exception e) {
	    			Log.info("Error Occured At setup() method :" + e.getMessage());
	    			e.printStackTrace();
	    		
	    	}
	 }
}

	public Osce createNewOsceForTest(Short shortBreak,Short longBreak,Boolean isrepeatOSce,Boolean isValid,Short lunchBreak,int maxNumberStudent,Short middleBreak,String name,int numberCourses,int totalRooms,OsceStatus osceStatus,int postLenth,Short simpatChange,StudyYears studyYear,Long semesterId){
		Log.info("Inside createNewOsceForTest()");
		
		try
		{
			Osce osce =new Osce();
			
			Osce osce2=null;
			
			osce.setLongBreak(longBreak);
			osce.setIsRepeOsce(isrepeatOSce);
			osce.setIsValid(isValid);
			osce.setLunchBreak(lunchBreak);
			osce.setMaxNumberStudents(maxNumberStudent);
			osce.setMiddleBreak(middleBreak);
			osce.setName(name);
			osce.setNumberCourses(numberCourses);
			//osce.setNumberPosts(null);
			osce.setNumberRooms(totalRooms);
			osce.setOsceStatus(osceStatus);
			osce.setPostLength(postLenth);
			osce.setShortBreak(shortBreak);
			osce.setShortBreakSimpatChange(simpatChange);
			osce.setStudyYear(studyYear);
			osce.setSemester(Semester.findSemester(semesterId));
			osce.persist();

			if(Osce.findOsce(osce.getId()) !=null){
				osce2=osce;
			}
			Log.info(" Return from createNewOsceForTest()");
			Log.info("Returned value is :" + osce2);
			return osce2;
		}
		
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public OsceDay  createOsceDayForTest(Osce osce,Date osceDay,Date osceStartTime,Date osceEndTime){
		Log.info("Inside createOsceDayForTest with osce Is : " + osce.getId());
		
		try{
			OsceDay osceday = new OsceDay();
			OsceDay osceday2=null;
			osceday.setOsce(osce);
			osceday.setOsceDate(osceDay);
			osceday.setTimeStart(osceStartTime);
			osceday.setTimeEnd(osceEndTime);
			osceday.persist();
		
			if(OsceDay.findOsceDay(osceday.getId())!=null){
				osceday2=osceday; 
			}
			Log.info(" Return from createOsceDayForTest()");
			Log.info("Returned value is :" + osceday2);
			return osceday2;
			
			}
			catch(Exception e){
				e.printStackTrace();
				return null;
			}
	}
	
	public Boolean createBluePrint(Osce osce){
    	Log.info("In Side createBluePrint with OSce : " + osce.getId());
    	
		Boolean flag=false;
    	
		try{
			
			if(createPreparationPost(1,osce,Specialisation.getSpecialisationForId(2l),Specialisation.getSpecialisationForId(3l), RoleTopic.getRoleTopicForId(1l), RoleTopic.getRoleTopicForId(2l)))
			if(createNormalPost(3,osce,Specialisation.getSpecialisationForId(1l), RoleTopic.getRoleTopicForId(1l)));
	    	if(createNormalPost(4,osce,Specialisation.getSpecialisationForId(2l), RoleTopic.getRoleTopicForId(2l)));
			if(createAnamnesisTherapyPost(5,osce,Specialisation.getSpecialisationForId(4l),Specialisation.getSpecialisationForId(4l), RoleTopic.getRoleTopicForId(2l), RoleTopic.getRoleTopicForId(2l)));
			flag=true;
	    	
	    	Log.info("Return from createBluePrint()");
	    	return flag;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
    public Boolean createNormalPost(Integer sequenceNo,Osce osceId,Specialisation specialisation,RoleTopic roleTopic){
    	Log.info("In Side createNormalPost with OSce : " +osceId);
    	Boolean result=false;
    	
    	try{
			OscePostBlueprint blueprint =new OscePostBlueprint();
			blueprint.setOsce(osceId);
			blueprint.setPostType(PostType.NORMAL);
			blueprint.setSequenceNumber(sequenceNo);
			blueprint.setRoleTopic(roleTopic);
			blueprint.setSpecialisation(specialisation);
			blueprint.setIsFirstPart(false);
			blueprint.persist();
			oscePostBluePrintListPersist.add(blueprint);
			if(OscePostBlueprint.findOscePostBlueprint(blueprint.getId())!= null){
				result=true;
			}
			Log.info("Return from createNormalPost()");
			return result;
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
	}
	
	public Boolean createBreakPost(Integer sequenceNo,Osce osceId,Specialisation specialisation,RoleTopic roleTopic){
		Log.info("In Side createBreakPost with OSce : " + osceId);
		Boolean result=false;
		
		try{
			OscePostBlueprint breakPost =new OscePostBlueprint();
			breakPost.setOsce(osceId);
			breakPost.setPostType(PostType.BREAK);
			breakPost.setSequenceNumber(sequenceNo);
			breakPost.setRoleTopic(roleTopic);
			breakPost.setSpecialisation(specialisation);
			breakPost.setIsFirstPart(false);
			breakPost.persist();
			oscePostBluePrintListPersist.add(breakPost);
			if(OscePostBlueprint.findOscePostBlueprint(breakPost.getId())!= null){
				result=true;
			}
			Log.info("Return from createBreakPost()");
			return result;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public Boolean createAnamnesisTherapyPost(Integer sequenceNo,Osce osceId,Specialisation specialisationId,Specialisation specialisationId2,RoleTopic roleTopicId,RoleTopic roleTopicId2){
		Log.info("In Side createAnamnesisTherapyPost with OSce : " + osceId);
		Boolean result=false;
		
		try{
			OscePostBlueprint anamnesisTherapyPost =new OscePostBlueprint();
			anamnesisTherapyPost.setOsce(osceId);
			anamnesisTherapyPost.setPostType(PostType.ANAMNESIS_THERAPY);
			anamnesisTherapyPost.setSequenceNumber(sequenceNo);
			anamnesisTherapyPost.setRoleTopic(roleTopicId);
			anamnesisTherapyPost.setSpecialisation(specialisationId);
			anamnesisTherapyPost.setIsFirstPart(false);
			anamnesisTherapyPost.persist();
			oscePostBluePrintListPersist.add(anamnesisTherapyPost);
			
			OscePostBlueprint anamnesisTherapyPost2 =new OscePostBlueprint();
			anamnesisTherapyPost2.setOsce(osceId);
			anamnesisTherapyPost2.setPostType(PostType.ANAMNESIS_THERAPY);
			anamnesisTherapyPost2.setSequenceNumber((sequenceNo+1));
			anamnesisTherapyPost2.setRoleTopic(roleTopicId2);
			anamnesisTherapyPost2.setSpecialisation(specialisationId2);
			anamnesisTherapyPost2.setIsFirstPart(false);
			anamnesisTherapyPost2.persist();
			oscePostBluePrintListPersist.add(anamnesisTherapyPost2);
			
			if(OscePostBlueprint.findOscePostBlueprint(anamnesisTherapyPost.getId())!= null && OscePostBlueprint.findOscePostBlueprint(anamnesisTherapyPost2.getId())!= null){
				result=true;
			}
			Log.info("Return from createAnamnesisTherapyPost()");
			return result;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public Boolean createPreparationPost(Integer sequenceNo,Osce osceId,Specialisation specialisationId,Specialisation specialisationId2,RoleTopic roleTopicId,RoleTopic roleTopicId2){
		Log.info("In Side createPreparationPost with OSce : " + osceId);
		Boolean result=false;
		try{
			OscePostBlueprint preparationPost =new OscePostBlueprint();
			preparationPost.setOsce(osceId);
			preparationPost.setPostType(PostType.PREPARATION);
			preparationPost.setSequenceNumber(sequenceNo);
			preparationPost.setRoleTopic(roleTopicId);
			preparationPost.setSpecialisation(specialisationId);
			preparationPost.setIsFirstPart(false);
			preparationPost.persist();
			oscePostBluePrintListPersist.add(preparationPost);
			
			OscePostBlueprint preparationPost2 =new OscePostBlueprint();
			preparationPost2.setOsce(osceId);
			preparationPost2.setPostType(PostType.PREPARATION);
			preparationPost2.setSequenceNumber((sequenceNo+1));
			preparationPost2.setRoleTopic(roleTopicId2);
			preparationPost2.setSpecialisation(specialisationId2);
			preparationPost2.setIsFirstPart(false);
			preparationPost2.persist();
			oscePostBluePrintListPersist.add(preparationPost2);
			
			if(OscePostBlueprint.findOscePostBlueprint(preparationPost.getId())!= null && OscePostBlueprint.findOscePostBlueprint(preparationPost2.getId())!= null){
				result=true;
			}
			Log.info("Return from createPreparationPost()");
			return result;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	// Test Case 1
	@SuppressWarnings("deprecation")
	@Test
	public void testStudentSlotLength() {
		Log.info("Test Case : testStudentSlotLength Start");
		
		// Un comment the following line and Specify Osce Value if we want to test This Test-case for any existing osce
		
		//osce=Osce.findOsce(1l);
		
		assignmentList=null;
		assignmentList=Assignment.retrieveAssignmentsOfTypeStudent(osce.getId());
		junit.framework.TestCase.assertNotNull(assignmentList);
		
		Iterator<Assignment> it = assignmentList.iterator();
		while (it.hasNext()) {
			Assignment ass = (Assignment) it.next();
			
			junit.framework.TestCase.assertNotNull(ass);
			junit.framework.TestCase.assertNotNull(ass.getTimeEnd());
			junit.framework.TestCase.assertNotNull(ass.getTimeStart());

			int slotLength = (int) (ass.getTimeEnd().getTime() - ass.getTimeStart().getTime()) / (1000 * 60);
			
			junit.framework.TestCase.assertNotNull(osce);
			junit.framework.TestCase.assertNotNull(osce.getPostLength());

			junit.framework.TestCase.assertEquals(slotLength, osce.getPostLength().intValue());
		}
		Log.info("Test Case : testStudentSlotLength End");
}

	// Test Case 2
	@SuppressWarnings("deprecation")
	@Test
	public void testBreaksBetweenTimeSlots(){
		
		Log.info("Test Case : testBreaksBetweenTimeSlots Start");

		//Un comment the following line (osce=Osce.findOsce(1l)) and Specify Osce Value if we want to test This Test-case for any existing osce 
		//for that also comment the  osce=Osce.findLastOsceCreatedForTestPurpose(); line 

		//osce=Osce.findOsce(1l);
		
		osce=Osce.findLastOsceCreatedForTestPurpose();
	
		Log.info("Last Created Osce is :" + osce.getId());
		
		List<AssignmentTypes> type= new ArrayList<AssignmentTypes>();
			
		// Specify for which type we want to test this test-suite Comment All if we want to test for all types.

		type.add(AssignmentTypes.STUDENT);
		type.add(AssignmentTypes.PATIENT);
		type.add(AssignmentTypes.EXAMINER);
		
		osceDay_List=null;
		osceDay_List = OsceDay.findOSceDaysForAnOsce(osce.getId());
		junit.framework.TestCase.assertNotNull(osceDay_List);
		
		Log.info("OsceDay Size IS :" + osceDay_List.size());

		Iterator<OsceDay> osceDayIterator = osceDay_List.iterator();

		while(osceDayIterator.hasNext()){
		newOsceDay = osceDayIterator.next();
		
		assignmentList=null;
		assignmentList=Assignment.findAssignmentForTestBasedOnCriteria(newOsceDay.getId(), type);
		
		
		junit.framework.TestCase.assertNotNull(assignmentList);
		Log.info("Assignment List Size Is :" + assignmentList.size());
		
		Iterator<Assignment> assignmentIterator = assignmentList.iterator();
		
		Assignment assignmentPrivious =assignmentIterator.next();
		
		Log.info("Assignmnet privios is :" + assignmentPrivious.getId());
						
		while(assignmentIterator.hasNext()){

			AssignmentTypes assignmentType=assignmentPrivious.getType();
						
			Log.info("Osce Post room Is : :" + assignmentPrivious.getOscePostRoom());
			
			Long postRoomId=assignmentPrivious.getOscePostRoom().getId();
			
			Assignment assignmentNew=assignmentIterator.next();
			
			if(postRoomId==assignmentNew.getOscePostRoom().getId() && assignmentType==assignmentNew.getType()){
				
				Long diff=(assignmentNew.getTimeStart().getTime()-assignmentPrivious.getTimeEnd().getTime());
				Long minuteDiffranceIs = diff/(1000*60);
				Log.info("Diffrance between Time is :" + minuteDiffranceIs);
				boolean flag =false;
				if(minuteDiffranceIs==osce.getShortBreak().longValue()||minuteDiffranceIs== osce.getShortBreakSimpatChange().longValue() ||minuteDiffranceIs== osce.getMiddleBreak().longValue()||minuteDiffranceIs== osce.getLongBreak().longValue()||minuteDiffranceIs==osce.getLunchBreak().longValue() || minuteDiffranceIs==osce.getLongBreak().longValue()){
					flag=true;
				}
				
				junit.framework.TestCase.assertTrue(flag);
			}
			assignmentPrivious=assignmentNew;
		}
		
		
	}
		Log.info("Test Case : testBreaksBetweenTimeSlots End");
				
 } 
	
	
	// Test Case 3
	@SuppressWarnings("deprecation")
	@Test
	public void testTotalOsceStudentWithDiffSeqSlots(){
		
		Log.info("Test Case : testTotalOsceStudentWithDiffSeqSlots Start");
			
		//Un comment the following line (osce=Osce.findOsce(1l)) and Specify Osce Value if we want to test This Test-case for any existing osce 
		//for that also comment the  osce=Osce.findLastOsceCreatedForTestPurpose(); line		
		
		//osce=Osce.findOsce(1l);
		
		osce=Osce.findLastOsceCreatedForTestPurpose();
		
		Log.info("Last Created Osce is :" + osce.getId());
		
		Integer record = Assignment.findTotalStudentsBasedOnOsce(osce.getId());
		Log.info("testTotalOsceStudentWithDiffSeqSlots record :" + record);
		
		junit.framework.TestCase.assertEquals(record,osce.getMaxNumberStudents());
		
		Log.info("Test Case : testTotalOsceStudentWithDiffSeqSlots End");

	}
	
	// Test Case  4
	@SuppressWarnings("deprecation")
	@Test
	public void testStarTimeEndEndTimeOfSlotBasedOnOsceDayTimes(){

		Log.info("Test Case : testStarTimeEndEndTimeOfSlotBasedOnOsceDayTimes Start");
		
		//Un comment the following line (osce=Osce.findOsce(1l)) and Specify Osce Value if we want to test This Test-case for any existing osce 
		//for that also comment the  osce=Osce.findLastOsceCreatedForTestPurpose(); line
		
		//osce=Osce.findOsce(1l);
		
		osce=Osce.findLastOsceCreatedForTestPurpose();
		
		Log.info("Last Created Osce is :" + osce.getId());
		
		osceDay_List=null;
		osceDay_List = OsceDay.findOSceDaysForAnOsce(osce.getId());
		junit.framework.TestCase.assertNotNull(osceDay_List);
		Log.info("OsceDay IS :" + osceDay_List.size());

		Iterator<OsceDay> osceDayIterator = osceDay_List.iterator();

		while(osceDayIterator.hasNext()){
		newOsceDay=osceDayIterator.next();
		
		assignmentList=null;
		assignmentList=Assignment.findAssignmentBasedOnOsceDay(newOsceDay.getId());
		
		junit.framework.TestCase.assertNotNull(assignmentList);
		Log.info("List Size Is :" + assignmentList.size());
		
		Iterator<Assignment> assignmentIterator = assignmentList.iterator();
		
		Assignment assignmentprivious =assignmentIterator.next();
		
		Log.info("Assignmnet privios is :" + assignmentprivious.getId());
		
		junit.framework.TestCase.assertEquals(assignmentprivious.getTimeStart(),newOsceDay.getTimeStart());
		
		while(assignmentIterator.hasNext()){

			Long postRoomId=assignmentprivious.getOscePostRoom().getId();
			
			AssignmentTypes type=assignmentprivious.getType();
			
			Assignment assignmentNew=assignmentIterator.next();
			
			if(postRoomId!=assignmentNew.getOscePostRoom().getId() || type!=assignmentNew.getType()){
				junit.framework.TestCase.assertEquals(assignmentprivious.getTimeEnd(),newOsceDay.getTimeEnd());
				junit.framework.TestCase.assertEquals(assignmentNew.getTimeStart(), newOsceDay.getTimeStart());
			}
			assignmentprivious=assignmentNew;
		}
		
		Log.info("Test Case : testStarTimeEndEndTimeOfSlotBasedOnOsceDayTimes End");
	}

 }
		
		
	// Test Case 5
	@SuppressWarnings("deprecation")
	@Test

	public void testPrepqrationPostAlwaysStartOneTimeSlotEarlier(){
		
		Log.info("Test Case : testPrepqrationPostAlwaysStartOneTimeSlotEarlier Start");
	
		// Un comment the following line (osce=Osce.findOsce(1l)) and Specify Osce Value if we want to test This Test-case for any existing osce 
		//for that also comment the  osce=Osce.findLastOsceCreatedForTestPurpose(); line 
		
		//osce=Osce.findOsce(1l);
		
		osce=Osce.findLastOsceCreatedForTestPurpose();
		
		Log.info("Last Created Osce is :" + osce.getId());
		
		oscePostBluePrintList=null;
		oscePostBluePrintList=Assignment.findOscePostBluePrintForOsceWithTypePreparation(osce.getId());
				
		junit.framework.TestCase.assertNotNull(oscePostBluePrintList);
		Log.info("OscePostBluePrint Size is :" + oscePostBluePrintList.size());
		
		courseList=null;
		courseList=Assignment.findParcoursForOsce(osce.getId());
		
		junit.framework.TestCase.assertNotNull(courseList);
		Log.info("Total Parcour for osce is : " + courseList.size());
		
		Iterator<Course> parcoursIterator = courseList.iterator();
		
		while(parcoursIterator.hasNext()){
			
			final Course course = parcoursIterator.next();
			
			Iterator<OscePostBlueprint> bluePrintIterator = oscePostBluePrintList.iterator();
			
			while(bluePrintIterator.hasNext()){
				
				final OscePostBlueprint oscePostBluePrint1=bluePrintIterator.next();
				final OscePostBlueprint oscePostBluePrint2=bluePrintIterator.next();
				
				assignmentList=null;
				assignmentList=Assignment.findAssignmentBasedOnGivenCourseAndPost(course.getId(), oscePostBluePrint1.getId());
				junit.framework.TestCase.assertNotNull(assignmentList);
				Log.info("Assignment size for Blue Print: "+oscePostBluePrint1.getId() + " IS " + assignmentList.size());
				
				for (Iterator assignmentIterator = assignmentList.iterator(); assignmentIterator.hasNext();) {
					Assignment assignment = (Assignment) assignmentIterator.next();
					assignmentMap1.put(assignment.getSequenceNumber(),assignment.getTimeStart());
				}
				
				assignmentList=null;
				assignmentList=Assignment.findAssignmentBasedOnGivenCourseAndPost(course.getId(), oscePostBluePrint2.getId());
				
				junit.framework.TestCase.assertNotNull(assignmentList);
				Log.info("Assignment size for Blue Print: "+oscePostBluePrint2.getId() + " IS " + assignmentList.size());
				
				for (Iterator assignmentIterator = assignmentList.iterator(); assignmentIterator.hasNext();) {
					Assignment assignment = (Assignment) assignmentIterator.next();
					assignmentMap2.put(assignment.getSequenceNumber(),assignment.getTimeStart());
				}		
				
				junit.framework.TestCase.assertEquals(assignmentMap1.size(),assignmentMap2.size());
				
				for(Integer key : assignmentMap1.keySet()){
					result=false;
					Date value1 = assignmentMap1.get(key);
					Date value2 = assignmentMap2.get(key);
					if(value1.getTime() < value2.getTime()){
						result=true;
					}
					junit.framework.TestCase.assertTrue(result);
				}
						
											
			}
			
		 }
		Log.info("Test Case : testPrepqrationPostAlwaysStartOneTimeSlotEarlier End");
	}

			
	// Test Case 6
	@SuppressWarnings("deprecation")
	@Test
	public void testTwoConsucitiveAnamnesisTherapyPostHaveSamePostRooms(){
		
		Log.info("Test Case : testTwoConsucitiveAnamnesisTherapyPostHaveSamePostRooms Start");		
		
		//Un comment the following line (osce=Osce.findOsce(1l)) and Specify Osce Value if we want to test This Test-case for any existing osce 
		//for that also comment the  osce=Osce.findLastOsceCreatedForTestPurpose(); line
		
		//osce=Osce.findOsce(1l);
		
		osce=Osce.findLastOsceCreatedForTestPurpose();
		
		Log.info("Last Created Osce is :" + osce.getId());
		
		oscePostBluePrintList=null; 
		oscePostBluePrintList=Assignment.findOscePostBluePrintForOsce(osce.getId());
		
		junit.framework.TestCase.assertNotNull(oscePostBluePrintList);
		Log.info("OscePostBlue Print Size Is :" + oscePostBluePrintList.size());
		
		
		courseList=null;
		courseList=Assignment.findParcoursForOsce(osce.getId());
		
		junit.framework.TestCase.assertNotNull(courseList);
		Log.info("Total Parcour For Osce Is :" + courseList.size());
		
		Iterator<Course> parcoursIterator = courseList.iterator();
		
		while(parcoursIterator.hasNext()){
			
			final Course course = parcoursIterator.next();
			
			Iterator<OscePostBlueprint> bluePrintIterator = oscePostBluePrintList.iterator();
			
			while(bluePrintIterator.hasNext()){
				
				final OscePostBlueprint oscePostBluePrint1=bluePrintIterator.next();
				final OscePostBlueprint oscePostBluePrint2=bluePrintIterator.next();
				
				oscePostRoom1=Assignment.findRoomForCourseAndBluePrint(course.getId(),oscePostBluePrint1.getId());
				
				junit.framework.TestCase.assertNotNull(oscePostRoom1);
				Log.info("OScePostRoom 1 Is : " + oscePostRoom1.getId());
				
				oscePostRoom2=Assignment.findRoomForCourseAndBluePrint(course.getId(),oscePostBluePrint2.getId());
				
				junit.framework.TestCase.assertNotNull(oscePostRoom2);
				Log.info("OScePostRoom 2 IS " + oscePostRoom2.getId());
				
				junit.framework.TestCase.assertEquals(oscePostRoom1.getRoom().getId(),oscePostRoom2.getRoom().getId());
				
			}
			
		}

		Log.info("Test Case : testTwoConsucitiveAnamnesisTherapyPostHaveSamePostRooms End");
	}
	
 // Test Case 7
	@SuppressWarnings("deprecation")
	@Test
	public void testNoTimeSlotHasSameStudentWithSameSequenceNumber(){
		
		Log.info("Test Case : testNoTimeSlotHasSameStudentWithSameSequenceNumber Start");
		
		//Un comment the following line (osce=Osce.findOsce(1l)) and Specify Osce Value if we want to test This Test-case for any existing osce 
		//for that also comment the  osce=Osce.findLastOsceCreatedForTestPurpose(); line
		
		//osce=Osce.findOsce(1l);
		
		osce=Osce.findLastOsceCreatedForTestPurpose();
		
		Log.info("Last Created Osce is :" + osce.getId());
		
		osceDay_List=null;
		osceDay_List=OsceDay.findOSceDaysForAnOsce(osce.getId());
		
		junit.framework.TestCase.assertNotNull(osceDay_List);
		Log.info("OSce Day Size is :" + osceDay_List.size());
		
		Iterator<OsceDay> osceDayIterator = osceDay_List.iterator();
		
		while(osceDayIterator.hasNext()){
			newOsceDay=osceDayIterator.next();
		
		sequenceNumberList=null;
		sequenceNumberList=Assignment.findAllSequenceNumberForAssignment(newOsceDay.getId());
		junit.framework.TestCase.assertNotNull(sequenceNumberList);
		
		Iterator<Integer> sequenceNumberIterator = sequenceNumberList.iterator();
		while(sequenceNumberIterator.hasNext()){
			
		Integer studentSequenceNo = sequenceNumberIterator.next();
		
		assignmentList=null;
		assignmentList=Assignment.findAssignmtForOsceDayAndSeq(studentSequenceNo, newOsceDay.getId());
		
		
		junit.framework.TestCase.assertNotNull(assignmentList);
		Log.info("Assignment Size is :" + assignmentList.size());
		
		Iterator<Assignment> assignmentIterator = assignmentList.iterator();
		boolean flag=false;
		Assignment	priviousAssignment=assignmentIterator.next();
		
		while(assignmentIterator.hasNext()){
			Assignment currentAssignment = assignmentIterator.next();
			
			if(currentAssignment.getTimeStart().getTime() > priviousAssignment.getTimeEnd().getTime()){
				flag=true;
			}
			else{
				flag=false;
			}
			junit.framework.TestCase.assertTrue(flag);
			priviousAssignment=currentAssignment;
		 }
	  }
	}
		
		Log.info("Test Case : testNoTimeSlotHasSameStudentWithSameSequenceNumber End");

	} 
	
}

