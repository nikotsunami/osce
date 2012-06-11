// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ch.unibas.medizin.osce.domain;

import ch.unibas.medizin.osce.domain.StudentDataOnDemand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect StudentIntegrationTest_Roo_IntegrationTest {
    
    declare @type: StudentIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: StudentIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml");
    
    declare @type: StudentIntegrationTest: @Transactional;
    
    @Autowired
    private StudentDataOnDemand StudentIntegrationTest.dod;
    
    @Test
    public void StudentIntegrationTest.testCountStudents() {
        org.junit.Assert.assertNotNull("Data on demand for 'Student' failed to initialize correctly", dod.getRandomStudent());
        long count = ch.unibas.medizin.osce.domain.Student.countStudents();
        org.junit.Assert.assertTrue("Counter for 'Student' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void StudentIntegrationTest.testFindStudent() {
        ch.unibas.medizin.osce.domain.Student obj = dod.getRandomStudent();
        org.junit.Assert.assertNotNull("Data on demand for 'Student' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Student' failed to provide an identifier", id);
        obj = ch.unibas.medizin.osce.domain.Student.findStudent(id);
        org.junit.Assert.assertNotNull("Find method for 'Student' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'Student' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void StudentIntegrationTest.testFindAllStudents() {
        org.junit.Assert.assertNotNull("Data on demand for 'Student' failed to initialize correctly", dod.getRandomStudent());
        long count = ch.unibas.medizin.osce.domain.Student.countStudents();
        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'Student', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        java.util.List<ch.unibas.medizin.osce.domain.Student> result = ch.unibas.medizin.osce.domain.Student.findAllStudents();
        org.junit.Assert.assertNotNull("Find all method for 'Student' illegally returned null", result);
        org.junit.Assert.assertTrue("Find all method for 'Student' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void StudentIntegrationTest.testFindStudentEntries() {
        org.junit.Assert.assertNotNull("Data on demand for 'Student' failed to initialize correctly", dod.getRandomStudent());
        long count = ch.unibas.medizin.osce.domain.Student.countStudents();
        if (count > 20) count = 20;
        java.util.List<ch.unibas.medizin.osce.domain.Student> result = ch.unibas.medizin.osce.domain.Student.findStudentEntries(0, (int) count);
        org.junit.Assert.assertNotNull("Find entries method for 'Student' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'Student' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void StudentIntegrationTest.testFlush() {
        ch.unibas.medizin.osce.domain.Student obj = dod.getRandomStudent();
        org.junit.Assert.assertNotNull("Data on demand for 'Student' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Student' failed to provide an identifier", id);
        obj = ch.unibas.medizin.osce.domain.Student.findStudent(id);
        org.junit.Assert.assertNotNull("Find method for 'Student' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyStudent(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'Student' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void StudentIntegrationTest.testMerge() {
        ch.unibas.medizin.osce.domain.Student obj = dod.getRandomStudent();
        org.junit.Assert.assertNotNull("Data on demand for 'Student' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Student' failed to provide an identifier", id);
        obj = ch.unibas.medizin.osce.domain.Student.findStudent(id);
        boolean modified =  dod.modifyStudent(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        ch.unibas.medizin.osce.domain.Student merged =  obj.merge();
        obj.flush();
        org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        org.junit.Assert.assertTrue("Version for 'Student' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void StudentIntegrationTest.testPersist() {
        org.junit.Assert.assertNotNull("Data on demand for 'Student' failed to initialize correctly", dod.getRandomStudent());
        ch.unibas.medizin.osce.domain.Student obj = dod.getNewTransientStudent(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'Student' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'Student' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        org.junit.Assert.assertNotNull("Expected 'Student' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void StudentIntegrationTest.testRemove() {
        ch.unibas.medizin.osce.domain.Student obj = dod.getRandomStudent();
        org.junit.Assert.assertNotNull("Data on demand for 'Student' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Student' failed to provide an identifier", id);
        obj = ch.unibas.medizin.osce.domain.Student.findStudent(id);
        obj.remove();
        obj.flush();
        org.junit.Assert.assertNull("Failed to remove 'Student' with identifier '" + id + "'", ch.unibas.medizin.osce.domain.Student.findStudent(id));
    }
    
}
