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
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;

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
}
