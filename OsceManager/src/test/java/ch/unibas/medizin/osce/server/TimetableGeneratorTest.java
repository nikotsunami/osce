package ch.unibas.medizin.osce.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.util.OSCEReceiverPopupViewImpl;
import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.shared.AssignmentTypes;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.requestfactory.server.ServiceLayer;
import com.google.gwt.requestfactory.server.SimpleRequestProcessor;
import com.google.gwt.requestfactory.server.testing.InProcessRequestTransport;
import com.google.gwt.requestfactory.server.testing.RequestFactoryMagic;
import com.google.gwt.requestfactory.shared.Receiver;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(locations = { "/META-INF/spring/applicationContext.xml"})

public class TimetableGeneratorTest extends TestCase {
	@SuppressWarnings("deprecation")
	OsMaRequestFactory requestFactory = RequestFactoryMagic.create(OsMaRequestFactory.class);

	SimpleEventBus eventBus = new SimpleEventBus();

	long osceId = 1l;
	OsceProxy osce = null;

	//Testing task {
	OsceProxy osceProxy;
	OsceDayProxy osceDayProxy;
	List<OscePostBlueprintProxy> oscePostBluePrintList;
	int oscePostBluePrint=0;
	private OscePostRoomProxy oscePostRoom1;
	private OscePostRoomProxy oscePostRoom2;
	private Map<Integer, Date> assignmentMap1 = new HashMap<Integer, Date>();
	private Map<Integer, Date> assignmentMap2 = new HashMap<Integer, Date>();
	private boolean result=false;
	//Testing task }

	@SuppressWarnings("deprecation")
	@Before
	public void setUp() throws Exception {		
		System.out.println("setup start");				
		requestFactory.initialize(eventBus,new InProcessRequestTransport(new SimpleRequestProcessor(ServiceLayer.create())));
		/*requestFactory.osceRequestNonRoo().generateOsceScaffold(1l).fire(new Receiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				requestFactory.osceRequestNonRoo().generateAssignments(1l).fire();
			}
			
		});*/
		requestFactory.osceRequest().findOsce(osceId).fire(new Receiver<OsceProxy>() {

			@Override
			public void onSuccess(OsceProxy response) {
				osce = response;
			}
		});
		System.out.println("setup end");
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testStudentSlotLength() {
		requestFactory.assignmentRequestNonRoo().retrieveAssignmentsOfTypeStudent(osceId).fire(new Receiver<List<AssignmentProxy>>() {

			@Override
			public void onSuccess(List<AssignmentProxy> response) {
				Iterator<AssignmentProxy> it = response.iterator();
				while (it.hasNext()) {
					AssignmentProxy ass = (AssignmentProxy) it.next();
					
					assertNotNull(ass);
					assertNotNull(ass.getTimeEnd());
					assertNotNull(ass.getTimeStart());

					int slotLength = (int) (ass.getTimeEnd().getTime() - ass.getTimeStart().getTime()) / (1000 * 60);
					
					assertNotNull(osce);
					assertNotNull(osce.getPostLength());

					assertEquals(slotLength, osce.getPostLength().intValue());
				}
			}
		});
	}
	
	//Testing task {
	// Test Case 2
	@SuppressWarnings("deprecation")
	@Test
	public void testBreaksBetweenTimeSlots(){

		Long osceDayId=1l;
		final int responseSize=38;
		
		requestFactory.osceDayRequest().findOsceDay(osceDayId).with("osce").fire(new OSCEReceiver<OsceDayProxy>() {

			@Override
			public void onSuccess(OsceDayProxy response) {
				assertNotNull(response);
				osceProxy=response.getOsce();
				System.out.println("Osce For Osce Day 1 Is :" + osceProxy.getId());
			}
		});
		List<AssignmentTypes> type= new ArrayList<AssignmentTypes>();
		type.add(AssignmentTypes.STUDENT);
		type.add(AssignmentTypes.PATIENT);
		type.add(AssignmentTypes.EXAMINER);
		
		Long oscePostRoomId=1l;
		
		
		
		
		requestFactory.assignmentRequestNonRoo().findAssignmentForTestBasedOnCriteria(osceDayId,type,oscePostRoomId).with("oscePostRoom").fire(new OSCEReceiver<List<AssignmentProxy>>() {

			@Override
			public void onSuccess(List<AssignmentProxy> response) {
				System.out.println("List Size Is :" + response.size());
				assertEquals(response.size(),responseSize);
				
				Iterator<AssignmentProxy> assignmentIterator = response.iterator();
				
				AssignmentProxy assignmentProxyprivious =assignmentIterator.next();
				
				System.out.println("Assignmnet privios is :" + assignmentProxyprivious.getId());
								
				while(assignmentIterator.hasNext()){

					Long postRoomId=assignmentProxyprivious.getOscePostRoom().getId();
					
					AssignmentTypes type=assignmentProxyprivious.getType();
					
					AssignmentProxy assignmentProxyNew=assignmentIterator.next();
					
					if(postRoomId==assignmentProxyNew.getOscePostRoom().getId() && type==assignmentProxyNew.getType()){
						
						Long diff=(assignmentProxyNew.getTimeStart().getTime()-assignmentProxyprivious.getTimeEnd().getTime());
						Long minuteDiffranceIs = diff/(1000*60);
						Log.info("Diffrance between Time is :" + minuteDiffranceIs);
						boolean flag =false;
						if(minuteDiffranceIs==osceProxy.getShortBreak().longValue()||minuteDiffranceIs== osceProxy.getShortBreakSimpatChange().longValue() ||minuteDiffranceIs== osceProxy.getMiddleBreak().longValue()||minuteDiffranceIs== osceProxy.getLongBreak().longValue()||minuteDiffranceIs==osceProxy.getLunchBreak().longValue() || minuteDiffranceIs==osceProxy.getLongBreak().longValue()){
							flag=true;
						}
						
						assertTrue(flag);
					}
					assignmentProxyprivious=assignmentProxyNew;
				}
				
				
			}
		});
	}	
	// Test Case 3
	@SuppressWarnings("deprecation")
	@Test
	public void testTotalOsceStudentWithDiffSeqSlots(){
		
		Long osceId=1l;
		final Integer resultValue=530;
		requestFactory.osceRequest().findOsce(osceId).fire(new OSCEReceiver<OsceProxy>() {

			@Override
			public void onSuccess(OsceProxy response) {
				
				osceProxy=response;
			}
		});
		requestFactory.assignmentRequestNonRoo().findTotalStudentsBasedOnOsce(osceId).fire(new OSCEReceiver<Integer>() {

			@Override
			public void onSuccess(Integer response) {
				Log.info("Total Student Is :" + response);
				assertEquals(response,resultValue);
				
				assertEquals(osceProxy.getMaxNumberStudents(),response);
				
			}
		});
		
	}
	// Test Case  4
	@SuppressWarnings("deprecation")
	@Test
	public void testStarTimeEndEndTimeOfSlotBasedOnOsceDayTimes(){

		Long osceDayId=1l;
		final int responseSize=630;
		
		requestFactory.osceDayRequest().findOsceDay(osceDayId).fire(new OSCEReceiver<OsceDayProxy>() {

			@Override
			public void onSuccess(OsceDayProxy response) {
				assertNotNull(response);
				osceDayProxy=response;
				System.out.println("OsceDay  Is :" + osceDayProxy.getId());
			}
		});
		
		
		requestFactory.assignmentRequestNonRoo().findAssignmentBasedOnOsceDay(osceDayProxy.getId()).fire(new OSCEReceiver<List<AssignmentProxy>>() {

			@Override
			public void onSuccess(List<AssignmentProxy> response) {
				System.out.println("List Size Is :" + response.size());
				//assertEquals(response.size(),responseSize);
				
				Iterator<AssignmentProxy> assignmentIterator = response.iterator();
				
				AssignmentProxy assignmentProxyprivious =assignmentIterator.next();
				
				System.out.println("Assignmnet privios is :" + assignmentProxyprivious.getId());
				
				assertEquals(assignmentProxyprivious.getTimeStart(),osceDayProxy.getTimeStart());
				
				while(assignmentIterator.hasNext()){

					Long postRoomId=assignmentProxyprivious.getOscePostRoom().getId();
					
					AssignmentTypes type=assignmentProxyprivious.getType();
					
					AssignmentProxy assignmentProxyNew=assignmentIterator.next();
					
					if(postRoomId!=assignmentProxyNew.getOscePostRoom().getId() || type!=assignmentProxyNew.getType()){
						assertEquals(assignmentProxyprivious.getTimeEnd(),osceDayProxy.getTimeEnd());
						assertEquals(assignmentProxyNew.getTimeStart(), osceDayProxy.getTimeStart());
					}
					assignmentProxyprivious=assignmentProxyNew;
				}
				
			}
		});
	}	
		
	// Test Case 5
		@SuppressWarnings("deprecation")
		@Test
		public void testPrepqrationPostAlwaysStartOneTimeSlotEarlier(){
			
			Long osceId=1l;
			requestFactory.assignmentRequestNonRoo().findOscePostBluePrintForOsceWithTypePreparation(osceId).fire(new OSCEReceiver<List<OscePostBlueprintProxy>>() {

				@Override
				public void onSuccess(List<OscePostBlueprintProxy> response) {
					assertNotNull(response);
					oscePostBluePrintList=null;
					oscePostBluePrintList=response;
					Log.info("OscePostBluePrint Size Is " + response.size());
					
				}
			});
			
			requestFactory.assignmentRequestNonRoo().findParcoursForOsce(osceId).fire(new OSCEReceiver<List<CourseProxy>>() {

				@Override
				public void onSuccess(List<CourseProxy> response) {
					assertNotNull(response);
					Log.info("Total Parcour for osce is : " + response.size());
					
					Iterator<CourseProxy> parcoursIterator = response.iterator();
					
					while(parcoursIterator.hasNext()){
						
						final CourseProxy course = parcoursIterator.next();
						
						Iterator<OscePostBlueprintProxy> bluePrintIterator = oscePostBluePrintList.iterator();
						
						while(bluePrintIterator.hasNext()){
							
							final OscePostBlueprintProxy oscePostBluePrint1=bluePrintIterator.next();
							final OscePostBlueprintProxy oscePostBluePrint2=bluePrintIterator.next();
							
							requestFactory.assignmentRequestNonRoo().findAssignmentBasedOnGivenCourseAndPost(course.getId(),oscePostBluePrint1.getId()).fire(new OSCEReceiver<List<AssignmentProxy>>() {

								@Override
								public void onSuccess(List<AssignmentProxy> response) {
									assertNotNull(response);
									Log.info("Assignment size for Blue Print: "+oscePostBluePrint1.getId() + " IS " + response.size());
									for (Iterator assignmentIterator = response.iterator(); assignmentIterator.hasNext();) {
										AssignmentProxy assignmentProxy = (AssignmentProxy) assignmentIterator.next();
										assignmentMap1.put(assignmentProxy.getSequenceNumber(),assignmentProxy.getTimeStart());
									}
									
									
									requestFactory.assignmentRequestNonRoo().findAssignmentBasedOnGivenCourseAndPost(course.getId(),oscePostBluePrint2.getId()).fire(new OSCEReceiver<List<AssignmentProxy>>() {

										@Override
										public void onSuccess(List<AssignmentProxy> response) {
											assertNotNull(response);
											Log.info("Assignment size for Blue Print: "+oscePostBluePrint2.getId() + " IS " + response.size());
											for (Iterator assignmentIterator = response.iterator(); assignmentIterator.hasNext();) {
												AssignmentProxy assignmentProxy = (AssignmentProxy) assignmentIterator.next();
												assignmentMap2.put(assignmentProxy.getSequenceNumber(),assignmentProxy.getTimeStart());
											}
											
											assertEquals(assignmentMap1.size(),assignmentMap2.size());
											
											for(Integer key : assignmentMap1.keySet()){
												result=false;
												Date value1 = assignmentMap1.get(key);
												Date value2 = assignmentMap2.get(key);
												if(value1.getTime() < value2.getTime()){
													result=true;
												}
												assertEquals(result, true);
											}
										}
									});
								}
							});
							
						}
						
					 }

				}
				
		 });
}
		
			
	// Test Case 6
	@SuppressWarnings("deprecation")
	@Test
	public void testTwoConsucitiveAnamnesisTherapyPostHaveSamePostRooms(){
		Long osceId=1l;
		requestFactory.assignmentRequestNonRoo().findOscePostBluePrintForOsce(osceId).fire(new OSCEReceiver<List<OscePostBlueprintProxy>>() {

			@Override
			public void onSuccess(List<OscePostBlueprintProxy> response) {
				assertNotNull(response);
				oscePostBluePrintList=null;
				oscePostBluePrintList=response;
				Log.info("OscePostBlue Print Size Is :" + response.size());
			}
		});
		
		 
		
		requestFactory.assignmentRequestNonRoo().findParcoursForOsce(osceId).fire(new OSCEReceiver<List<CourseProxy>>() {

			@Override
			public void onSuccess(List<CourseProxy> response) {
				assertNotNull(response);
				Log.info("Total Parcour For Osce Is :" + response.size());
				
				Iterator<CourseProxy> parcoursIterator = response.iterator();
				
				while(parcoursIterator.hasNext()){
					
					final CourseProxy course = parcoursIterator.next();
					
					Iterator<OscePostBlueprintProxy> bluePrintIterator = oscePostBluePrintList.iterator();
					
					while(bluePrintIterator.hasNext()){
						
						final OscePostBlueprintProxy oscePostBluePrint1=bluePrintIterator.next();
						final OscePostBlueprintProxy oscePostBluePrint2=bluePrintIterator.next();
						
						requestFactory.assignmentRequestNonRoo().findRoomForCourseAndBluePrint(course.getId(),oscePostBluePrint1.getId()).with("room").fire(new OSCEReceiver<OscePostRoomProxy>() {

							@Override
							public void onSuccess(OscePostRoomProxy response) {
								assertNotNull(response);
								Log.info("OScePostRoom 1 Is : " + response.getId());
								oscePostRoom1=response;
								
								requestFactory.assignmentRequestNonRoo().findRoomForCourseAndBluePrint(course.getId(),oscePostBluePrint2.getId()).with("room").fire(new OSCEReceiver<OscePostRoomProxy>() {

									@Override
									public void onSuccess(OscePostRoomProxy response) {
										assertNotNull(response);
										Log.info("OScePostRoom 2 IS " + response.getId());
										oscePostRoom2=response;
									}
								});
							}
						});
						
						assertEquals(oscePostRoom1.getRoom().getId(),oscePostRoom2.getRoom().getId());
					}
					
				}
			}
		});
	}
	
 // Test Case 7
	@SuppressWarnings("deprecation")
	@Test
	public void testNoTimeSlotHasSameStudentWithSameSequenceNumber(){
		Long osceDayId= 1l;
		Integer studentSequenceNo=5;
		 
		requestFactory.assignmentRequestNonRoo().findAssignmtForOsceDayAndSeq(studentSequenceNo,osceDayId).fire(new OSCEReceiver<List<AssignmentProxy>>() {

			@Override
			public void onSuccess(List<AssignmentProxy> response) {
				assertNotNull(response);
				Log.info("Assignment Size is :" + response.size());
				Iterator<AssignmentProxy> assignmentProxyIterator = response.iterator();
				boolean flag=false;
				AssignmentProxy	priviousAssignment=assignmentProxyIterator.next();
				
				while(assignmentProxyIterator.hasNext()){
					AssignmentProxy currentAssignment = assignmentProxyIterator.next();
					
					if(currentAssignment.getTimeStart().getTime() > priviousAssignment.getTimeEnd().getTime()){
						flag=true;
					}
					else{
						flag=false;
					}
					assertEquals(flag, true);
					priviousAssignment=currentAssignment;
				}
				
			}
		});
	
	} 
		
	//Testing task }
}
