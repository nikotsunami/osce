package ch.unibas.medizin.osce.server;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.domain.Assignment;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.requestfactory.server.ServiceLayer;
import com.google.gwt.requestfactory.server.SimpleRequestProcessor;
import com.google.gwt.requestfactory.server.testing.InProcessRequestTransport;
import com.google.gwt.requestfactory.server.testing.RequestFactoryMagic;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.vm.RequestFactorySource;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(locations = { "/META-INF/spring/applicationContext.xml"})

public class TimetableGeneratorTest extends TestCase {
	OsMaRequestFactory requestFactory = RequestFactoryMagic.create(OsMaRequestFactory.class);

	SimpleEventBus eventBus = new SimpleEventBus();

	long osceId = 1l;

	OsceProxy osce = null;

	@SuppressWarnings("deprecation")
	@Before
	public void setUp() throws Exception {		
		System.out.println("setup start");				
		requestFactory.initialize(eventBus,new InProcessRequestTransport(new SimpleRequestProcessor(ServiceLayer.create())));
		requestFactory.osceRequestNonRoo().generateOsceScaffold(1l).fire(new Receiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				// TODO Auto-generated method stub
				requestFactory.osceRequestNonRoo().generateAssignments(1l).fire();
			}
			
		});
		System.out.println("setup end");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testCalculation() {
		try{
			System.out.println("request : " + requestFactory.osceRequest());
			requestFactory.osceRequest().findOsce(osceId).fire(new Receiver<OsceProxy>() {

				@Override
				public void onSuccess(OsceProxy response) {
					System.out.println("found : " + response.getName());
					osce = response;
					assertEquals(osceId, osce.getId().longValue());
					System.out.println("end");
					
					
					List<OsceDayProxy> osceDays = osce.getOsce_days();

					Iterator<OsceDayProxy> it = osceDays.iterator();
					OsceDayProxy prevDay = null;
					while (it.hasNext()) {
						OsceDayProxy osceDay = (OsceDayProxy) it.next();

						if(prevDay != null && osceDay != null) {
							// compare dates
						}

						prevDay = osceDay;
					}

					OsceDayProxy osceDay = osceDays.get(0);
					assertNotNull(osceDay);
					assertEquals(2, osceDay.getOsceSequences().size());
				}

			});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
//	@SuppressWarnings("deprecation")
//	@Test
//	public void testEndTimes() {
//		requestFactory.osceRequest().findOsce(osceId).fire(new Receiver<OsceProxy>() {
//
//			@Override
//			public void onSuccess(OsceProxy response) {
//				System.out.println("found : " + response.getName());
//				osce = response;
//				assertEquals(osceId, osce.getId().longValue());
//				System.out.println("end");
//				
//				
//				List<OsceDayProxy> osceDays = osce.getOsce_days();
//
//				Iterator<OsceDayProxy> it = osceDays.iterator();
//				while (it.hasNext()) {
//					OsceDayProxy osceDay = (OsceDayProxy) it.next();
//
//					if(osceDay != null) {
//						requestFactory.assignmentRequestNonRoo().findLastAssignmentsByOsceDay(osceDay).fire(new Receiver<AssignmentProxy>() {
//
//							@Override
//							public void onSuccess(AssignmentProxy lastAssignment) {
//								assertEquals(lastAssignment.getTimeEnd(), lastAssignment.getOsceDay().getTimeEnd());
//							}
//						});
//					}
//				}
//
//				OsceDayProxy osceDay = osceDays.get(0);
//				assertNotNull(osceDay);
//				assertEquals(2, osceDay.getOsceSequences().size());
//			}
//
//		});
//	}
}
