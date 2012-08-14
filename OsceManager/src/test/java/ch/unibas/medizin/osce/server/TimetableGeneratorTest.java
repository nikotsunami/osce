package ch.unibas.medizin.osce.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceRequest;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.domain.OsceDay;
import ch.unibas.medizin.osce.domain.OsceSequence;
import ch.unibas.medizin.osce.shared.AssignmentTypes;
import ch.unibas.medizin.osce.shared.RoleTypes;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.requestfactory.server.ServiceLayer;
import com.google.gwt.requestfactory.server.SimpleRequestProcessor;
import com.google.gwt.requestfactory.server.testing.InProcessRequestTransport;
import com.google.gwt.requestfactory.server.testing.RequestFactoryMagic;
import com.google.gwt.requestfactory.shared.BaseProxy;
import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.itextpdf.text.log.SysoLogger;
import com.allen_sauer.gwt.log.client.Log;

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
	//Testing task }

	@SuppressWarnings("deprecation")
	@Before
	public void setUp() throws Exception {		
		System.out.println("setup start");				
		requestFactory.initialize(eventBus,new InProcessRequestTransport(new SimpleRequestProcessor(ServiceLayer.create())));
		requestFactory.osceRequestNonRoo().generateOsceScaffold(1l).fire(new Receiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				requestFactory.osceRequestNonRoo().generateAssignments(1l).fire();
			}
			
		});
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
		
	//Testing task }
}
