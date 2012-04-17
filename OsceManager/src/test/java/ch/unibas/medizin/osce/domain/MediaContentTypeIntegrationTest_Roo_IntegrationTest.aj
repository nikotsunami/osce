// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ch.unibas.medizin.osce.domain;

import ch.unibas.medizin.osce.domain.MediaContentTypeDataOnDemand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect MediaContentTypeIntegrationTest_Roo_IntegrationTest {
    
    declare @type: MediaContentTypeIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: MediaContentTypeIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml");
    
    declare @type: MediaContentTypeIntegrationTest: @Transactional;
    
    @Autowired
    private MediaContentTypeDataOnDemand MediaContentTypeIntegrationTest.dod;
    
    @Test
    public void MediaContentTypeIntegrationTest.testCountMediaContentTypes() {
        org.junit.Assert.assertNotNull("Data on demand for 'MediaContentType' failed to initialize correctly", dod.getRandomMediaContentType());
        long count = ch.unibas.medizin.osce.domain.MediaContentType.countMediaContentTypes();
        org.junit.Assert.assertTrue("Counter for 'MediaContentType' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void MediaContentTypeIntegrationTest.testFindMediaContentType() {
        ch.unibas.medizin.osce.domain.MediaContentType obj = dod.getRandomMediaContentType();
        org.junit.Assert.assertNotNull("Data on demand for 'MediaContentType' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'MediaContentType' failed to provide an identifier", id);
        obj = ch.unibas.medizin.osce.domain.MediaContentType.findMediaContentType(id);
        org.junit.Assert.assertNotNull("Find method for 'MediaContentType' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'MediaContentType' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void MediaContentTypeIntegrationTest.testFindAllMediaContentTypes() {
        org.junit.Assert.assertNotNull("Data on demand for 'MediaContentType' failed to initialize correctly", dod.getRandomMediaContentType());
        long count = ch.unibas.medizin.osce.domain.MediaContentType.countMediaContentTypes();
        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'MediaContentType', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        java.util.List<ch.unibas.medizin.osce.domain.MediaContentType> result = ch.unibas.medizin.osce.domain.MediaContentType.findAllMediaContentTypes();
        org.junit.Assert.assertNotNull("Find all method for 'MediaContentType' illegally returned null", result);
        org.junit.Assert.assertTrue("Find all method for 'MediaContentType' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void MediaContentTypeIntegrationTest.testFindMediaContentTypeEntries() {
        org.junit.Assert.assertNotNull("Data on demand for 'MediaContentType' failed to initialize correctly", dod.getRandomMediaContentType());
        long count = ch.unibas.medizin.osce.domain.MediaContentType.countMediaContentTypes();
        if (count > 20) count = 20;
        java.util.List<ch.unibas.medizin.osce.domain.MediaContentType> result = ch.unibas.medizin.osce.domain.MediaContentType.findMediaContentTypeEntries(0, (int) count);
        org.junit.Assert.assertNotNull("Find entries method for 'MediaContentType' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'MediaContentType' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void MediaContentTypeIntegrationTest.testFlush() {
        ch.unibas.medizin.osce.domain.MediaContentType obj = dod.getRandomMediaContentType();
        org.junit.Assert.assertNotNull("Data on demand for 'MediaContentType' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'MediaContentType' failed to provide an identifier", id);
        obj = ch.unibas.medizin.osce.domain.MediaContentType.findMediaContentType(id);
        org.junit.Assert.assertNotNull("Find method for 'MediaContentType' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyMediaContentType(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'MediaContentType' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void MediaContentTypeIntegrationTest.testMerge() {
        ch.unibas.medizin.osce.domain.MediaContentType obj = dod.getRandomMediaContentType();
        org.junit.Assert.assertNotNull("Data on demand for 'MediaContentType' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'MediaContentType' failed to provide an identifier", id);
        obj = ch.unibas.medizin.osce.domain.MediaContentType.findMediaContentType(id);
        boolean modified =  dod.modifyMediaContentType(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        ch.unibas.medizin.osce.domain.MediaContentType merged =  obj.merge();
        obj.flush();
        org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        org.junit.Assert.assertTrue("Version for 'MediaContentType' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void MediaContentTypeIntegrationTest.testPersist() {
        org.junit.Assert.assertNotNull("Data on demand for 'MediaContentType' failed to initialize correctly", dod.getRandomMediaContentType());
        ch.unibas.medizin.osce.domain.MediaContentType obj = dod.getNewTransientMediaContentType(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'MediaContentType' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'MediaContentType' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        org.junit.Assert.assertNotNull("Expected 'MediaContentType' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void MediaContentTypeIntegrationTest.testRemove() {
        ch.unibas.medizin.osce.domain.MediaContentType obj = dod.getRandomMediaContentType();
        org.junit.Assert.assertNotNull("Data on demand for 'MediaContentType' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'MediaContentType' failed to provide an identifier", id);
        obj = ch.unibas.medizin.osce.domain.MediaContentType.findMediaContentType(id);
        obj.remove();
        obj.flush();
        org.junit.Assert.assertNull("Failed to remove 'MediaContentType' with identifier '" + id + "'", ch.unibas.medizin.osce.domain.MediaContentType.findMediaContentType(id));
    }
    
}