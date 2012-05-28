package ch.unibas.medizin.osce.server;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.roo.addon.test.RooIntegrationTest;

import ch.unibas.medizin.osce.domain.AnamnesisCheck;


@RooIntegrationTest(entity = AnamnesisCheck.class)
public class DMZSyncServiceImplTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPushToDMZ() {
		fail("Not yet implemented");
	}

	@Test
	public void testPullFromDMZ() {
		fail("Not yet implemented");
	}

}
