// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ch.unibas.medizin.osce.domain;

import ch.unibas.medizin.osce.domain.MediaContentDataOnDemand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect MediaContentIntegrationTest_Roo_IntegrationTest {
    
    declare @type: MediaContentIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: MediaContentIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml");
    
    declare @type: MediaContentIntegrationTest: @Transactional;
    
    @Autowired
    private MediaContentDataOnDemand MediaContentIntegrationTest.dod;
    
    @Test
    public void MediaContentIntegrationTest.testCountMediaContents() {
        org.junit.Assert.assertNotNull("Data on demand for 'MediaContent' failed to initialize correctly", dod.getRandomMediaContent());
        long count = ch.unibas.medizin.osce.domain.MediaContent.countMediaContents();
        org.junit.Assert.assertTrue("Counter for 'MediaContent' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void MediaContentIntegrationTest.testFindMediaContent() {
        ch.unibas.medizin.osce.domain.MediaContent obj = dod.getRandomMediaContent();
        org.junit.Assert.assertNotNull("Data on demand for 'MediaContent' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'MediaContent' failed to provide an identifier", id);
        obj = ch.unibas.medizin.osce.domain.MediaContent.findMediaContent(id);
        org.junit.Assert.assertNotNull("Find method for 'MediaContent' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'MediaContent' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void MediaContentIntegrationTest.testFindAllMediaContents() {
        org.junit.Assert.assertNotNull("Data on demand for 'MediaContent' failed to initialize correctly", dod.getRandomMediaContent());
        long count = ch.unibas.medizin.osce.domain.MediaContent.countMediaContents();
        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'MediaContent', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        java.util.List<ch.unibas.medizin.osce.domain.MediaContent> result = ch.unibas.medizin.osce.domain.MediaContent.findAllMediaContents();
        org.junit.Assert.assertNotNull("Find all method for 'MediaContent' illegally returned null", result);
        org.junit.Assert.assertTrue("Find all method for 'MediaContent' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void MediaContentIntegrationTest.testFindMediaContentEntries() {
        org.junit.Assert.assertNotNull("Data on demand for 'MediaContent' failed to initialize correctly", dod.getRandomMediaContent());
        long count = ch.unibas.medizin.osce.domain.MediaContent.countMediaContents();
        if (count > 20) count = 20;
        java.util.List<ch.unibas.medizin.osce.domain.MediaContent> result = ch.unibas.medizin.osce.domain.MediaContent.findMediaContentEntries(0, (int) count);
        org.junit.Assert.assertNotNull("Find entries method for 'MediaContent' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'MediaContent' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void MediaContentIntegrationTest.testFlush() {
        ch.unibas.medizin.osce.domain.MediaContent obj = dod.getRandomMediaContent();
        org.junit.Assert.assertNotNull("Data on demand for 'MediaContent' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'MediaContent' failed to provide an identifier", id);
        obj = ch.unibas.medizin.osce.domain.MediaContent.findMediaContent(id);
        org.junit.Assert.assertNotNull("Find method for 'MediaContent' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyMediaContent(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'MediaContent' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void MediaContentIntegrationTest.testMerge() {
        ch.unibas.medizin.osce.domain.MediaContent obj = dod.getRandomMediaContent();
        org.junit.Assert.assertNotNull("Data on demand for 'MediaContent' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'MediaContent' failed to provide an identifier", id);
        obj = ch.unibas.medizin.osce.domain.MediaContent.findMediaContent(id);
        boolean modified =  dod.modifyMediaContent(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        ch.unibas.medizin.osce.domain.MediaContent merged =  obj.merge();
        obj.flush();
        org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        org.junit.Assert.assertTrue("Version for 'MediaContent' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void MediaContentIntegrationTest.testPersist() {
        org.junit.Assert.assertNotNull("Data on demand for 'MediaContent' failed to initialize correctly", dod.getRandomMediaContent());
        ch.unibas.medizin.osce.domain.MediaContent obj = dod.getNewTransientMediaContent(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'MediaContent' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'MediaContent' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        org.junit.Assert.assertNotNull("Expected 'MediaContent' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void MediaContentIntegrationTest.testRemove() {
        ch.unibas.medizin.osce.domain.MediaContent obj = dod.getRandomMediaContent();
        org.junit.Assert.assertNotNull("Data on demand for 'MediaContent' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'MediaContent' failed to provide an identifier", id);
        obj = ch.unibas.medizin.osce.domain.MediaContent.findMediaContent(id);
        obj.remove();
        obj.flush();
        org.junit.Assert.assertNull("Failed to remove 'MediaContent' with identifier '" + id + "'", ch.unibas.medizin.osce.domain.MediaContent.findMediaContent(id));
    }
    
}
