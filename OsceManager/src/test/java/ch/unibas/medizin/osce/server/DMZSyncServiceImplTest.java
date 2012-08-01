package ch.unibas.medizin.osce.server;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;


import ch.unibas.medizin.osce.domain.Administrator;
import ch.unibas.medizin.osce.domain.AnamnesisChecksValue;
import ch.unibas.medizin.osce.domain.StandardizedPatient;
import ch.unibas.medizin.osce.domain.AnamnesisCheck;
import ch.unibas.medizin.osce.shared.Gender;
import java.util.*;
import java.text.*;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.ContextConfiguration;
import junit.framework.Assert;

import ch.unibas.medizin.osce.domain.OsceDay;
import ch.unibas.medizin.osce.domain.Training;
import ch.unibas.medizin.osce.domain.PatientInSemester;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.sql.DataSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

@ContextConfiguration(locations = { "/META-INF/spring/applicationContext2.xml" })
public class DMZSyncServiceImplTest extends AbstractJUnit4SpringContextTests  {

    private MyDMZSyncServiceImpl instance  = null;
    private StandardizedPatient currentSP  = null;
    private String dataFromDMZ  = null;
	private String expectedURL = null;
	private String locale = null;

    private StandardizedPatient testData = null;
	private StandardizedPatient patient1 = null;
	private StandardizedPatient patient2 = null;
	private String returnData = null;
	
    
    @Before
    public void setUp() throws Exception {
        instance = new MyDMZSyncServiceImpl();
        currentSP  =  new StandardizedPatient();			
		
		setUpData(currentSP, 55);
    }

    @After
    public void tearDown() throws Exception {
		instance = null;
        currentSP = null;
        dataFromDMZ  = null;
		locale = null;
		patient1 = null;
		patient2 = null;
		expectedURL = null;
		returnData = null;

		clearDB();
    }
    
    @Test
    public void testSync(){
    	String returnJson = null;
		String excpetedReturn = "osce for date 2012-6-18 12:00 already in DMZ doing nothing#&";
			   excpetedReturn+="osce for date 12-6-20 12:00 already in DMZ doing nothing#&";	
			   excpetedReturn+="osce for date 12-6-20 12:00 already in DMZ doing nothing#&";	
			   excpetedReturn+="warning patient Daniel Kohler was not found in the DMZ. Please manually push#&";	
			   excpetedReturn+="warning patient Marianne Lamarie was not found in the DMZ. Please manually push#&";	
			   excpetedReturn+="warning patient Ferdinand Preussler was not found in the DMZ. Please manually push#&";	
			   excpetedReturn+="warning patient Karl Meyer was not found in the DMZ. Please manually push#&";	
			   excpetedReturn+="warning patient Bettina Buser was not found in the DMZ. Please manually push#&";	
			   excpetedReturn+="warning patient Carla Joanna Velazquez was not found in the DMZ. Please manually push#&";	
			   excpetedReturn+="warning patient Max Peter was not found in the DMZ. Please manually push#&";	
			   excpetedReturn+="warning patient Ruth Musyl was not found in the DMZ. Please manually push#&";	
			   excpetedReturn+="warning patient Ljiljana Ivanovicwas not found in the DMZ. Please manually push#&";	
			   excpetedReturn+="warning patient Delphine Landwerlin was not found in the DMZ. Please manually push";	
			   
		//Test sync create
	    patient1 = new StandardizedPatient();
		patient1.setPreName("perName1");
		patient1.setName("name1");
		patient1.persist();
		patient1.merge();
		patient2 = new StandardizedPatient();
		patient2.setPreName("perName2");
		patient2.setName("name2");
		patient2.persist();
		patient2.merge();
		
		PatientInSemester pSemester = PatientInSemester.findPatientInSemesterByStandardizedPatient(patient1);
		assertEquals(null,pSemester);
		
		List<StandardizedPatient> patients = StandardizedPatient.findAllStandardizedPatients();

		returnData = "{\"message\" : [{\"key\":\"osce for date 2012-6-18 12:00 already in DMZ doing nothing\"},{\"key\":\"osce for date 12-6-20 12:00 already in DMZ doing nothing\"},{\"key\":\"osce for date 12-6-20 12:00 already in DMZ doing nothing\"},{\"key\":\"warning patient Daniel Kohler was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Marianne Lamarie was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Ferdinand Preussler was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Karl Meyer was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Bettina Buser was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Carla Joanna Velazquez was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Max Peter was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Ruth Musyl was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Ljiljana Ivanovicwas not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Delphine Landwerlin was not found in the DMZ. Please manually push\"}],\"osceDay\" :[{\"osceDate\":\"2012-06-10T00:00:00Z\"},{\"osceDate\":\"2012-06-20T00:00:00Z\"},{\"osceDate\":\"2012-06-18T00:00:00Z\"},{\"osceDate\":\"2012-06-21T00:00:00Z\"}],\"trainings\" : [{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T11:00:02Z\",\"timeEnd\":\"2012-07-17T17:15:55Z\"},{\"name\":\"training2\",\"trainingDate\":\"2012-07-25T00:00:00Z\",\"timeStart\":\"2012-07-25T08:30:00Z\",\"timeEnd\":\"2012-07-25T15:30:00Z\"},{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T07:00:02Z\",\"timeEnd\":\"2012-07-17T14:15:55Z\"},{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T10:00:02Z\",\"timeEnd\":\"2012-07-17T14:15:55Z\"},{\"name\":\"tttttt\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T09:00:02Z\",\"timeEnd\":\"2012-07-17T19:00:00Z\"}],\"patientInSemester\" : [{\"standarizedPatientId\":"+patient2.getId()+",\"acceptedTrainings\":[{\"name\":\"training2\",\"trainingDate\":\"2012-07-25T00:00:00Z\",\"timeStart\":\"2012-07-25T08:30:00Z\",\"timeEnd\":\"2012-07-25T15:30:00Z\"}],\"acceptedOsce\":[{\"osceDate\":\"2012-06-21T00:00:00Z\"}],\"accepted\":false},{\"standarizedPatientId\":"+patient1.getId()+",\"acceptedTrainings\":[{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T07:00:02Z\",\"timeEnd\":\"2012-07-17T14:15:55Z\"},{\"name\":\"training2\",\"trainingDate\":\"2012-07-25T00:00:00Z\",\"timeStart\":\"2012-07-25T08:30:00Z\",\"timeEnd\":\"2012-07-25T15:30:00Z\"},{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T11:00:02Z\",\"timeEnd\":\"2012-07-17T17:15:55Z\"}],\"acceptedOsce\":[{\"osceDate\":\"2012-06-10T00:00:00Z\"},{\"osceDate\":\"2012-06-20T00:00:00Z\"}],\"accepted\":true}]}";
		
		try {
			locale = "en";
    
			returnJson = instance.sync(locale);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getCause().printStackTrace();
			Assert.fail("error occured " + e.getMessage());
		}
		
			
		//assertEquals(2,patients.size());
	
		List<OsceDay> osceDays = OsceDay.findAllOsceDays();
		assertEquals(4,osceDays.size());
		
		OsceDay osceDay1 = OsceDay.findOsceDayByOsceDate(convertToDate("2012-06-10T00:00:00Z"));
		assertNotNull(osceDay1);	
		OsceDay osceDay2 = OsceDay.findOsceDayByOsceDate(convertToDate("2012-06-20T00:00:00Z"));
		assertNotNull(osceDay2);
		OsceDay osceDay3 = OsceDay.findOsceDayByOsceDate(convertToDate("2012-06-18T00:00:00Z"));
		assertNotNull(osceDay3);
		OsceDay osceDay4 = OsceDay.findOsceDayByOsceDate(convertToDate("2012-06-21T00:00:00Z"));
		assertNotNull(osceDay4);
		
		
		List<Training> trainings = Training.findAllTrainings();
		assertEquals(5,trainings.size());
		
		Training training1 = Training.findTrainingByTrainingDateAndTimeStart(convertToDate("2012-07-17T00:00:00Z"),convertToDate("2012-07-17T11:00:02Z"));
		assertNotNull(training1);
		assertEquals("training1",training1.getName());
		assertEquals(convertToDate("2012-07-17T17:15:55Z"),training1.getTimeEnd());
		Training training2 = Training.findTrainingByTrainingDateAndTimeStart(convertToDate("2012-07-25T00:00:00Z"),convertToDate("2012-07-25T08:30:00Z"));
		assertNotNull(training2);
		assertEquals("training2",training2.getName());
		assertEquals(convertToDate("2012-07-25T15:30:00Z"),training2.getTimeEnd());
		Training training3 = Training.findTrainingByTrainingDateAndTimeStart(convertToDate("2012-07-17T00:00:00Z"),convertToDate("2012-07-17T07:00:02Z"));
		assertNotNull(training3);
		assertEquals("training1",training3.getName());
		assertEquals(convertToDate("2012-07-17T14:15:55Z"),training3.getTimeEnd());
		Training training4 = Training.findTrainingByTrainingDateAndTimeStart(convertToDate("2012-07-17T00:00:00Z"),convertToDate("2012-07-17T07:00:02Z"));
		assertNotNull(training4);
		assertEquals("training1",training4.getName());
		assertEquals(convertToDate("2012-07-17T14:15:55Z"),training4.getTimeEnd());
		Training training5 = Training.findTrainingByTrainingDateAndTimeStart(convertToDate("2012-07-17T00:00:00Z"),convertToDate("2012-07-17T09:00:02Z"));
		assertNotNull(training5);
		assertEquals("tttttt",training5.getName());
		assertEquals(convertToDate("2012-07-17T19:00:00Z"),training5.getTimeEnd());
		
		List<PatientInSemester> semesters = PatientInSemester.findAllPatientInSemesters();
		assertEquals(2,semesters.size());
		
		PatientInSemester semester = PatientInSemester.findPatientInSemesterByStandardizedPatient(patient1);
		assertTrue(semester.getAccepted());
		//assertEquals(3,semester.getTrainings().size());
		
		PatientInSemester semester2 = PatientInSemester.findPatientInSemesterByStandardizedPatient(patient2);
		assertFalse(semester2.getAccepted());
		
		assertEquals(excpetedReturn,returnJson);
		
		
		
		
		
		//Test sync update
		try {
			locale = "en";
			returnData = "{\"message\" : [{\"key\":\"osce for date 2012-6-18 12:00 already in DMZ doing nothing\"},{\"key\":\"osce for date 12-6-20 12:00 already in DMZ doing nothing\"},{\"key\":\"osce for date 12-6-20 12:00 already in DMZ doing nothing\"},{\"key\":\"warning patient Daniel Kohler was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Marianne Lamarie was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Ferdinand Preussler was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Karl Meyer was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Bettina Buser was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Carla Joanna Velazquez was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Max Peter was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Ruth Musyl was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Ljiljana Ivanovicwas not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Delphine Landwerlin was not found in the DMZ. Please manually push\"}],\"osceDay\" :[{\"osceDate\":\"2012-01-01T00:00:00Z\"},{\"osceDate\":\"2012-06-20T00:00:00Z\"},{\"osceDate\":\"2012-06-18T00:00:00Z\"},{\"osceDate\":\"2012-06-21T00:00:00Z\"}],\"trainings\" : [{\"name\":\"testTraining\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T11:00:02Z\",\"timeEnd\":\"2012-07-17T17:00:00Z\"},{\"name\":\"training2\",\"trainingDate\":\"2012-07-01T00:00:00Z\",\"timeStart\":\"2012-07-01T08:30:00Z\",\"timeEnd\":\"2012-07-01T15:30:00Z\"},{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T07:00:02Z\",\"timeEnd\":\"2012-07-17T14:15:55Z\"},{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T10:00:02Z\",\"timeEnd\":\"2012-07-17T14:15:55Z\"},{\"name\":\"tttttt\",\"trainingDate\":\"2012-07-17T00:00:59Z\",\"timeStart\":\"\",\"timeEnd\":\"\"}],\"patientInSemester\" : [{\"standarizedPatientId\":"+patient2.getId()+",\"acceptedTrainings\":[{\"name\":\"training2\",\"trainingDate\":\"2012-07-25T00:00:00Z\",\"timeStart\":\"2012-07-25T08:30:00Z\",\"timeEnd\":\"2012-07-25T15:30:00Z\"}],\"acceptedOsce\":[{\"osceDate\":\"2012-06-21T00:00:00Z\"}],\"accepted\":false},{\"standarizedPatientId\":"+patient1.getId()+",\"acceptedTrainings\":[{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T07:00:02Z\",\"timeEnd\":\"2012-07-17T14:15:55Z\"},{\"name\":\"training2\",\"trainingDate\":\"2012-07-25T00:00:00Z\",\"timeStart\":\"2012-07-25T08:30:00Z\",\"timeEnd\":\"2012-07-25T15:30:00Z\"},{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T11:00:02Z\",\"timeEnd\":\"2012-07-17T17:15:55Z\"}],\"acceptedOsce\":[{\"osceDate\":\"2012-06-10T00:00:00Z\"},{\"osceDate\":\"2012-06-20T00:00:00Z\"}],\"accepted\":true}]}";	
			//,\"timeStart\":\"2012-07-17T09:00:02Z\",\"timeEnd\":\"2012-07-17T19:00:00Z\"
			returnJson = instance.sync(locale);	
		} catch (Exception e) {
			e.printStackTrace();
			e.getCause().printStackTrace();
			Assert.fail("error occured " + e.getMessage());
		}
		
	
		osceDays = OsceDay.findAllOsceDays();
		assertEquals(5,osceDays.size());
		
		osceDay1 = OsceDay.findOsceDayByOsceDate(convertToDate("2012-06-10T00:00:00Z"));
		assertNotNull(osceDay1);	
	    osceDay2 = OsceDay.findOsceDayByOsceDate(convertToDate("2012-06-20T00:00:00Z"));
		assertNotNull(osceDay2);
		osceDay3 = OsceDay.findOsceDayByOsceDate(convertToDate("2012-06-18T00:00:00Z"));
		assertNotNull(osceDay3);
		osceDay4 = OsceDay.findOsceDayByOsceDate(convertToDate("2012-06-21T00:00:00Z"));
		assertNotNull(osceDay4);
		OsceDay osceDay5 = OsceDay.findOsceDayByOsceDate(convertToDate("2012-01-01T00:00:00Z"));
		assertNotNull(osceDay5);
		
		
		trainings = Training.findAllTrainings();
		assertEquals(6,trainings.size());
		
		training1 = Training.findTrainingByTrainingDateAndTimeStart(convertToDate("2012-07-17T00:00:00Z"),convertToDate("2012-07-17T11:00:02Z"));
		assertNotNull(training1);
		assertEquals("testTraining",training1.getName());
		assertEquals(convertToDate("2012-07-17T17:00:00Z"),training1.getTimeEnd());
		training2 = Training.findTrainingByTrainingDateAndTimeStart(convertToDate("2012-07-25T00:00:00Z"),convertToDate("2012-07-25T08:30:00Z"));
		assertNotNull(training2);
		assertEquals("training2",training2.getName());
		assertEquals(convertToDate("2012-07-25T15:30:00Z"),training2.getTimeEnd());
		training3 = Training.findTrainingByTrainingDateAndTimeStart(convertToDate("2012-07-17T00:00:00Z"),convertToDate("2012-07-17T07:00:02Z"));
		assertNotNull(training3);
		assertEquals("training1",training3.getName());
		assertEquals(convertToDate("2012-07-17T14:15:55Z"),training3.getTimeEnd());
		training4 = Training.findTrainingByTrainingDateAndTimeStart(convertToDate("2012-07-17T00:00:00Z"),convertToDate("2012-07-17T10:00:02Z"));
		assertNotNull(training4);
		assertEquals("training1",training4.getName());
		assertEquals(convertToDate("2012-07-17T14:15:55Z"),training4.getTimeEnd());
		training5 = Training.findTrainingByTrainingDateAndName(convertToDate("2012-07-17T00:00:59Z"),"tttttt");
		assertNotNull(training5);
		assertEquals("tttttt",training5.getName());
		assertEquals(null,training5.getTimeEnd());
		Training training6 = Training.findTrainingByTrainingDateAndTimeStart(convertToDate("2012-07-01T00:00:00Z"),convertToDate("2012-07-01T08:30:00Z"));
		assertNotNull(training6);
		assertEquals("training2",training6.getName());
		assertEquals(convertToDate("2012-07-01T15:30:00Z"),training6.getTimeEnd());
		
		
		semesters = PatientInSemester.findAllPatientInSemesters();
		assertEquals(2,semesters.size());
		
		semester = PatientInSemester.findPatientInSemesterByStandardizedPatient(patient1);
		assertTrue(semester.getAccepted());
		//assertEquals(3,semester.getTrainings().size());
		
		semester2 = PatientInSemester.findPatientInSemesterByStandardizedPatient(patient2);
		assertFalse(semester2.getAccepted());
	
	
		//Test sync when date has some second diffrent
		try {
			locale = "en";
			returnData = "{\"message\" : [{\"key\":\"osce for date 2012-6-18 12:00 already in DMZ doing nothing\"},{\"key\":\"osce for date 12-6-20 12:00 already in DMZ doing nothing\"},{\"key\":\"osce for date 12-6-20 12:00 already in DMZ doing nothing\"},{\"key\":\"warning patient Daniel Kohler was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Marianne Lamarie was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Ferdinand Preussler was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Karl Meyer was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Bettina Buser was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Carla Joanna Velazquez was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Max Peter was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Ruth Musyl was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Ljiljana Ivanovicwas not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Delphine Landwerlin was not found in the DMZ. Please manually push\"}],\"osceDay\" :[{\"osceDate\":\"2012-01-01T00:00:42Z\"},{\"osceDate\":\"2012-06-20T00:00:00Z\"},{\"osceDate\":\"2012-06-18T00:00:59Z\"},{\"osceDate\":\"2012-06-21T00:00:30Z\"}],\"trainings\" : [{\"name\":\"testTraining\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T11:00:02Z\",\"timeEnd\":\"2012-07-17T17:00:00Z\"},{\"name\":\"training2\",\"trainingDate\":\"2012-07-01T00:00:00Z\",\"timeStart\":\"2012-07-01T08:29:03Z\",\"timeEnd\":\"2012-07-01T15:29:01Z\"},{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:01:00Z\",\"timeStart\":\"2012-07-17T07:00:02Z\",\"timeEnd\":\"2012-07-17T14:15:59Z\"},{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:43Z\",\"timeStart\":\"2012-07-17T10:00:02Z\",\"timeEnd\":\"2012-07-17T14:15:55Z\"},{\"name\":\"tttttt\",\"trainingDate\":\"2012-07-17T00:00:59Z\",\"timeStart\":\"\",\"timeEnd\":\"\"}],\"patientInSemester\" : [{\"standarizedPatientId\":"+patient2.getId()+",\"acceptedTrainings\":[{\"name\":\"training2\",\"trainingDate\":\"2012-07-25T00:00:00Z\",\"timeStart\":\"2012-07-25T08:30:00Z\",\"timeEnd\":\"2012-07-25T15:30:00Z\"}],\"acceptedOsce\":[{\"osceDate\":\"2012-06-21T00:00:00Z\"}],\"accepted\":false},{\"standarizedPatientId\":"+patient1.getId()+",\"acceptedTrainings\":[{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T07:00:02Z\",\"timeEnd\":\"2012-07-17T14:15:55Z\"},{\"name\":\"training2\",\"trainingDate\":\"2012-07-25T00:00:00Z\",\"timeStart\":\"2012-07-25T08:30:00Z\",\"timeEnd\":\"2012-07-25T15:30:00Z\"},{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T11:00:02Z\",\"timeEnd\":\"2012-07-17T17:15:55Z\"}],\"acceptedOsce\":[{\"osceDate\":\"2012-06-10T00:00:00Z\"},{\"osceDate\":\"2012-06-20T00:00:00Z\"}],\"accepted\":true}]}";	
			returnJson = instance.sync(locale);	
		} catch (Exception e) {
			e.printStackTrace();
			e.getCause().printStackTrace();
			Assert.fail("error occured " + e.getMessage());
		}
		
	
		osceDays = OsceDay.findAllOsceDays();
		assertEquals(5,osceDays.size());
		
		trainings = Training.findAllTrainings();
		assertEquals(7,trainings.size());
    }
   
   
	@Test
    public void testSyncErroneousOsceData(){    
			   
		//Test sync create
	    patient1 = new StandardizedPatient();
		patient1.setPreName("perName1");
		patient1.setName("name1");
		patient1.persist();
		patient1.merge();
		patient2 = new StandardizedPatient();
		patient2.setPreName("perName2");
		patient2.setName("name2");
		patient2.persist();
		patient2.merge();
		
		PatientInSemester pSemester = PatientInSemester.findPatientInSemesterByStandardizedPatient(patient1);
		assertEquals(null,pSemester);
		
		returnData = "{\"message\" : [{\"key\":\"osce for date 2012-6-18 12:00 already in DMZ doing nothing\"},{\"key\":\"osce for date 12-6-20 12:00 already in DMZ doing nothing\"},{\"key\":\"osce for date 12-6-20 12:00 already in DMZ doing nothing\"},{\"key\":\"warning patient Daniel Kohler was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Marianne Lamarie was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Ferdinand Preussler was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Karl Meyer was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Bettina Buser was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Carla Joanna Velazquez was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Max Peter was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Ruth Musyl was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Ljiljana Ivanovicwas not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Delphine Landwerlin was not found in the DMZ. Please manually push\"}],\"osceDay\" :[{\"osceDate\":\"2012-13-1\"},{\"osceDate\":\"2012-06-20T00:00:00Z\"},{\"osceDate\":\"2012-06-18T00:00:00Z\"},{\"osceDate\":\"2012-06-21T00:00:00Z\"}],\"trainings\" : [{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T11:00:02Z\",\"timeEnd\":\"2012-07-17T17:15:55Z\"},{\"name\":\"training2\",\"trainingDate\":\"2012-07-25T00:00:00Z\",\"timeStart\":\"2012-07-25T08:30:00Z\",\"timeEnd\":\"2012-07-25T15:30:00Z\"},{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T07:00:02Z\",\"timeEnd\":\"2012-07-17T14:15:55Z\"},{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T10:00:02Z\",\"timeEnd\":\"2012-07-17T14:15:55Z\"},{\"name\":\"tttttt\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T09:00:02Z\",\"timeEnd\":\"2012-07-17T19:00:00Z\"}],\"patientInSemester\" : [{\"standarizedPatientId\":"+patient2.getId()+",\"acceptedTrainings\":[{\"name\":\"training2\",\"trainingDate\":\"2012-07-25T00:00:00Z\",\"timeStart\":\"2012-07-25T08:30:00Z\",\"timeEnd\":\"2012-07-25T15:30:00Z\"}],\"acceptedOsce\":[{\"osceDate\":\"2012-06-21T00:00:00Z\"}],\"accepted\":false},{\"standarizedPatientId\":"+patient1.getId()+",\"acceptedTrainings\":[{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T07:00:02Z\",\"timeEnd\":\"2012-07-17T14:15:55Z\"},{\"name\":\"training2\",\"trainingDate\":\"2012-07-25T00:00:00Z\",\"timeStart\":\"2012-07-25T08:30:00Z\",\"timeEnd\":\"2012-25-07\"},{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T11:00:02Z\",\"timeEnd\":\"2012-07-17T17:15:55Z\"}],\"acceptedOsce\":[{\"osceDate\":\"2012-06-10T00:00:00Z\"},{\"osceDate\":\"2012-06-20T00:00:00Z\"}],\"accepted\":true}]}";
		try {
			locale = "en";
    
			instance.sync(locale);
			
			
		} catch (Exception e) {
			//e.printStackTrace();
			//e.getCause().printStackTrace();
			Assert.fail("Expected Error never occured " );
		}
		
			
		//assertEquals(2,patients.size());
	
		List<OsceDay> osceDays = OsceDay.findAllOsceDays();
		assertEquals(3,osceDays.size());
		
		OsceDay osceDay1 = OsceDay.findOsceDayByOsceDate(convertToDate("2012-06-10T00:00:00Z"));
		assertNull(osceDay1);	
		OsceDay osceDay2 = OsceDay.findOsceDayByOsceDate(convertToDate("2012-06-20T00:00:00Z"));
		assertNotNull(osceDay2);
		OsceDay osceDay3 = OsceDay.findOsceDayByOsceDate(convertToDate("2012-06-18T00:00:00Z"));
		assertNotNull(osceDay3);
		OsceDay osceDay4 = OsceDay.findOsceDayByOsceDate(convertToDate("2012-06-21T00:00:00Z"));
		assertNotNull(osceDay4);		
		
    }   
	
	@Test
    public void testSyncErroneousTrainingDate(){
    
			   
		//Test sync create
	    patient1 = new StandardizedPatient();
		patient1.setPreName("perName1");
		patient1.setName("name1");
		patient1.persist();
		patient1.merge();
		patient2 = new StandardizedPatient();
		patient2.setPreName("perName2");
		patient2.setName("name2");
		patient2.persist();
		patient2.merge();
		
		PatientInSemester pSemester = PatientInSemester.findPatientInSemesterByStandardizedPatient(patient1);
		assertEquals(null,pSemester);
		
		returnData = "{\"message\" : [{\"key\":\"osce for date 2012-6-18 12:00 already in DMZ doing nothing\"},{\"key\":\"osce for date 12-6-20 12:00 already in DMZ doing nothing\"},{\"key\":\"osce for date 12-6-20 12:00 already in DMZ doing nothing\"},{\"key\":\"warning patient Daniel Kohler was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Marianne Lamarie was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Ferdinand Preussler was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Karl Meyer was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Bettina Buser was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Carla Joanna Velazquez was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Max Peter was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Ruth Musyl was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Ljiljana Ivanovicwas not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Delphine Landwerlin was not found in the DMZ. Please manually push\"}],\"osceDay\" :[{\"osceDate\":\"2012-06-10T00:00:00Z\"},{\"osceDate\":\"2012-06-20T00:00:00Z\"},{\"osceDate\":\"2012-06-18T00:00:00Z\"},{\"osceDate\":\"2012-06-21T00:00:00Z\"}],\"trainings\" : [{\"name\":\"training1\",\"trainingDate\":\"2012-16-\",\"timeStart\":\"2012-07-17T11:00:02Z\",\"timeEnd\":\"2012-07-17T17:15:55Z\"},{\"name\":\"training2\",\"trainingDate\":\"2012-07-25T00:00:00Z\",\"timeStart\":\"2012-07-25T08:30:00Z\",\"timeEnd\":\"2012-07-25T15:30:00Z\"},{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T07:00:02Z\",\"timeEnd\":\"2012-07-17T14:15:55Z\"},{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T10:00:02Z\",\"timeEnd\":\"2012-07-17T14:15:55Z\"},{\"name\":\"tttttt\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T09:00:02Z\",\"timeEnd\":\"2012-07-17T19:00:00Z\"}],\"patientInSemester\" : [{\"standarizedPatientId\":"+patient2.getId()+",\"acceptedTrainings\":[{\"name\":\"training2\",\"trainingDate\":\"2012-07-25T00:00:00Z\",\"timeStart\":\"2012-07-25T08:30:00Z\",\"timeEnd\":\"2012-07-25T15:30:00Z\"}],\"acceptedOsce\":[{\"osceDate\":\"2012-06-21T00:00:00Z\"}],\"accepted\":false},{\"standarizedPatientId\":"+patient1.getId()+",\"acceptedTrainings\":[{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T07:00:02Z\",\"timeEnd\":\"2012-07-17T14:15:55Z\"},{\"name\":\"training2\",\"trainingDate\":\"2012-07-25T00:00:00Z\",\"timeStart\":\"2012-07-25T08:30:00Z\",\"timeEnd\":\"2012-25-07\"},{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T11:00:02Z\",\"timeEnd\":\"2012-07-17T17:15:55Z\"}],\"acceptedOsce\":[{\"osceDate\":\"2012-06-10T00:00:00Z\"},{\"osceDate\":\"2012-06-20T00:00:00Z\"}],\"accepted\":true}]}";
		
		try {
			locale = "en";
    
			instance.sync(locale);
			
			
		} catch (Exception e) {
			//e.printStackTrace();
			//e.getCause().printStackTrace();
			Assert.fail("Expected Error never occured " );
		}
		
		List<Training> trainings = Training.findAllTrainings();
		assertEquals(4,trainings.size());
		
		//Wrong trainingDate, original trainingDate is 2012-07-17T00:00:00Z
		Training training1 = Training.findTrainingByTrainingDateAndTimeStart(convertToDate("2012-07-17T00:00:00Z"),convertToDate("2012-07-17T11:00:02Z"));
		assertNull(training1);
		
		Training training2 = Training.findTrainingByTrainingDateAndTimeStart(convertToDate("2012-07-25T00:00:00Z"),convertToDate("2012-07-25T08:30:00Z"));
		assertNotNull(training2);
		assertEquals("training2",training2.getName());
		assertEquals(convertToDate("2012-07-25T15:30:00Z"),training2.getTimeEnd());
		Training training3 = Training.findTrainingByTrainingDateAndTimeStart(convertToDate("2012-07-17T00:00:00Z"),convertToDate("2012-07-17T07:00:02Z"));
		assertNotNull(training3);
		assertEquals("training1",training3.getName());
		assertEquals(convertToDate("2012-07-17T14:15:55Z"),training3.getTimeEnd());
		Training training4 = Training.findTrainingByTrainingDateAndTimeStart(convertToDate("2012-07-17T00:00:00Z"),convertToDate("2012-07-17T10:00:02Z"));
		assertNotNull(training4);
		assertEquals("training1",training4.getName());
		assertEquals(convertToDate("2012-07-17T14:15:55Z"),training4.getTimeEnd());
		Training training5 = Training.findTrainingByTrainingDateAndTimeStart(convertToDate("2012-07-17T00:00:00Z"),convertToDate("2012-07-17T09:00:02Z"));
		assertNotNull(training5);
		assertEquals("tttttt",training5.getName());
		assertEquals(convertToDate("2012-07-17T19:00:00Z"),training5.getTimeEnd());	
				
		
    }
	
	@Test
    public void testSyncErroneousOsceDayJsonName(){
					   
		//Test sync error json data which name is the osceDay became a osceDays 
	    patient1 = new StandardizedPatient();
		patient1.setPreName("perName1");
		patient1.setName("name1");
		patient1.persist();
		patient1.merge();
		patient2 = new StandardizedPatient();
		patient2.setPreName("perName2");
		patient2.setName("name2");
		patient2.persist();
		patient2.merge();
		
		PatientInSemester pSemester = PatientInSemester.findPatientInSemesterByStandardizedPatient(patient1);
		assertEquals(null,pSemester);
		
		returnData = "{\"message\" : [{\"key\":\"osce for date 2012-6-18 12:00 already in DMZ doing nothing\"},{\"key\":\"osce for date 12-6-20 12:00 already in DMZ doing nothing\"},{\"key\":\"osce for date 12-6-20 12:00 already in DMZ doing nothing\"},{\"key\":\"warning patient Daniel Kohler was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Marianne Lamarie was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Ferdinand Preussler was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Karl Meyer was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Bettina Buser was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Carla Joanna Velazquez was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Max Peter was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Ruth Musyl was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Ljiljana Ivanovicwas not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Delphine Landwerlin was not found in the DMZ. Please manually push\"}],\"osceDays\" :[{\"osceDate\":\"2012-06-10T00:00:00Z\"},{\"osceDate\":\"2012-06-20T00:00:00Z\"},{\"osceDate\":\"2012-06-18T00:00:00Z\"},{\"osceDate\":\"2012-06-21T00:00:00Z\"}],\"trainings\" : [{\"name\":\"training1\",\"trainingDate\":\"2012-16-\",\"timeStart\":\"2012-07-17T11:00:02Z\",\"timeEnd\":\"2012-07-17T17:15:55Z\"},{\"name\":\"training2\",\"trainingDate\":\"2012-07-25T00:00:00Z\",\"timeStart\":\"2012-07-25T08:30:00Z\",\"timeEnd\":\"2012-07-25T15:30:00Z\"},{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T07:00:02Z\",\"timeEnd\":\"2012-07-17T14:15:55Z\"},{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T10:00:02Z\",\"timeEnd\":\"2012-07-17T14:15:55Z\"},{\"name\":\"tttttt\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T09:00:02Z\",\"timeEnd\":\"2012-07-17T19:00:00Z\"}],\"patientInSemester\" : [{\"standarizedPatientId\":"+patient2.getId()+",\"acceptedTrainings\":[{\"name\":\"training2\",\"trainingDate\":\"2012-07-25T00:00:00Z\",\"timeStart\":\"2012-07-25T08:30:00Z\",\"timeEnd\":\"2012-07-25T15:30:00Z\"}],\"acceptedOsce\":[{\"osceDate\":\"2012-06-21T00:00:00Z\"}],\"accepted\":false},{\"standarizedPatientId\":"+patient1.getId()+",\"acceptedTrainings\":[{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T07:00:02Z\",\"timeEnd\":\"2012-07-17T14:15:55Z\"},{\"name\":\"training2\",\"trainingDate\":\"2012-07-25T00:00:00Z\",\"timeStart\":\"2012-07-25T08:30:00Z\",\"timeEnd\":\"2012-25-07\"},{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T11:00:02Z\",\"timeEnd\":\"2012-07-17T17:15:55Z\"}],\"acceptedOsce\":[{\"osceDate\":\"2012-06-10T00:00:00Z\"},{\"osceDate\":\"2012-06-20T00:00:00Z\"}],\"accepted\":true}]}";
		
		try {
			locale = "en";
    
			instance.sync(locale);
			Assert.fail("excepted error did not happen");
			
		} catch (Exception e) {
			//e.printStackTrace();
		
			
		}
		
		List<Training> trainings = Training.findAllTrainings();
		assertEquals(0,trainings.size());
		
		List<OsceDay> osceDays = OsceDay.findAllOsceDays();
		assertEquals(0,osceDays.size());
    }
	
	
	@Test
    public void testSyncErroneousTrainingJsonName(){
    
			   
		//Test sync error json data which name is the trainings became a training 
	    patient1 = new StandardizedPatient();
		patient1.setPreName("perName1");
		patient1.setName("name1");
		patient1.persist();
		patient1.merge();
		patient2 = new StandardizedPatient();
		patient2.setPreName("perName2");
		patient2.setName("name2");
		patient2.persist();
		patient2.merge();
		
		PatientInSemester pSemester = PatientInSemester.findPatientInSemesterByStandardizedPatient(patient1);
		assertEquals(null,pSemester);
		
		returnData = "{\"message\" : [{\"key\":\"osce for date 2012-6-18 12:00 already in DMZ doing nothing\"},{\"key\":\"osce for date 12-6-20 12:00 already in DMZ doing nothing\"},{\"key\":\"osce for date 12-6-20 12:00 already in DMZ doing nothing\"},{\"key\":\"warning patient Daniel Kohler was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Marianne Lamarie was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Ferdinand Preussler was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Karl Meyer was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Bettina Buser was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Carla Joanna Velazquez was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Max Peter was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Ruth Musyl was not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Ljiljana Ivanovicwas not found in the DMZ. Please manually push\"},{\"key\":\"warning patient Delphine Landwerlin was not found in the DMZ. Please manually push\"}],\"osceDay\" :[{\"osceDate\":\"2012-06-10T00:00:00Z\"},{\"osceDate\":\"2012-06-20T00:00:00Z\"},{\"osceDate\":\"2012-06-18T00:00:00Z\"},{\"osceDate\":\"2012-06-21T00:00:00Z\"}],\"training\" : [{\"name\":\"training1\",\"trainingDate\":\"2012-16-\",\"timeStart\":\"2012-07-17T11:00:02Z\",\"timeEnd\":\"2012-07-17T17:15:55Z\"},{\"name\":\"training2\",\"trainingDate\":\"2012-07-25T00:00:00Z\",\"timeStart\":\"2012-07-25T08:30:00Z\",\"timeEnd\":\"2012-07-25T15:30:00Z\"},{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T07:00:02Z\",\"timeEnd\":\"2012-07-17T14:15:55Z\"},{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T10:00:02Z\",\"timeEnd\":\"2012-07-17T14:15:55Z\"},{\"name\":\"tttttt\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T09:00:02Z\",\"timeEnd\":\"2012-07-17T19:00:00Z\"}],\"patientInSemester\" : [{\"standarizedPatientId\":"+patient2.getId()+",\"acceptedTrainings\":[{\"name\":\"training2\",\"trainingDate\":\"2012-07-25T00:00:00Z\",\"timeStart\":\"2012-07-25T08:30:00Z\",\"timeEnd\":\"2012-07-25T15:30:00Z\"}],\"acceptedOsce\":[{\"osceDate\":\"2012-06-21T00:00:00Z\"}],\"accepted\":false},{\"standarizedPatientId\":"+patient1.getId()+",\"acceptedTrainings\":[{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T07:00:02Z\",\"timeEnd\":\"2012-07-17T14:15:55Z\"},{\"name\":\"training2\",\"trainingDate\":\"2012-07-25T00:00:00Z\",\"timeStart\":\"2012-07-25T08:30:00Z\",\"timeEnd\":\"2012-25-07\"},{\"name\":\"training1\",\"trainingDate\":\"2012-07-17T00:00:00Z\",\"timeStart\":\"2012-07-17T11:00:02Z\",\"timeEnd\":\"2012-07-17T17:15:55Z\"}],\"acceptedOsce\":[{\"osceDate\":\"2012-06-10T00:00:00Z\"},{\"osceDate\":\"2012-06-20T00:00:00Z\"}],\"accepted\":true}]}";
		
		try {
			locale = "en";
    
			instance.sync(locale);
			
			Assert.fail("Expected Error never occured " );
		} catch (Exception e) {
			//e.printStackTrace();

			
		}
		
		List<Training> trainings = Training.findAllTrainings();
		assertEquals(0,trainings.size());
		
		
		List<OsceDay> osceDays = OsceDay.findAllOsceDays();
		assertEquals(4,osceDays.size());		
		
    }
   
    
    private String getSendJsonData(){
    	
    	return "{\"language\": "+locale+",\"osceDay\":[{\"osceDate\": \"2012-06-18T00:00:00Z\"},{\"osceDate\": \"2012-06-20T00:00:00Z\"},{\"osceDate\": \"2012-06-20T00:00:00Z\"},{\"osceDate\": \"2012-06-10T00:00:00Z\"},{\"osceDate\": \"2012-06-21T00:00:00Z\"}],\"trainings\":[{\"name\" : \"training1\",\"trainingDate\" : \"2012-07-17T00:00:00Z\",\"timeStart\" : \"2012-07-17T11:00:02Z\",\"timeEnd\": \"2012-07-17T17:15:55Z\"},{\"name\" : \"training2\",\"trainingDate\" : \"2012-07-25T00:00:00Z\",\"timeStart\": \"2012-07-25T08:30:00Z\",\"timeEnd\": \"2012-07-25T15:30:00Z\"},{\"name\" : \"training1\",\"trainingDate\" : \"2012-07-17T00:00:00Z\",\"timeStart\" : \"2012-07-17T07:00:02Z\",\"timeEnd\": \"2012-07-17T14:15:55Z\"},{\"name\" : \"training1\",\"trainingDate\" : \"2012-07-17T00:00:00Z\",\"timeStart\" : \"2012-07-17T10:00:02Z\",\"timeEnd\": \"2012-07-17T14:15:55Z\"},{\"name\" : \"tttttt\",\"trainingDate\" : \"2012-07-17T00:00:00Z\",\"timeStart\" : \"2012-07-17T09:00:02Z\",\"timeEnd\": \"2012-07-17T19:00:00Z\"}],\"standardizedPatient\":[{\"id\": 19,\"preName\": \"Daniel\",\"name\": \"Kohler\"},{\"id\": 20,\"preName\": \"Marianne\",\"name\": \"Lamarie\"},{\"id\": 21,\"preName\": \"Ferdinand\",\"name\": \"Preussler\"},{\"id\": 22,\"preName\": \"Karl\",\"name\": \"Meyer\"},{\"id\": 23,\"preName\": \"Bettina\",\"name\": \"Buser\"},{\"id\": 24,\"preName\": \"Carla Joanna\",\"name\": \"Velazquez\"},{\"id\": 25,\"preName\": \"Max\",\"name\": \"Peter\"},{\"id\": 26,\"preName\": \"Ruth\",\"name\": \"Musyl\"},{\"id\": 27,\"preName\": \"Ljiljana\",\"name\": \"Ivanovic\"},{\"id\": 28,\"preName\": \"Benjamin\",\"name\": \"Adenauer\"},{\"id\": 29,\"preName\": \"Delphine\",\"name\": \"Landwerlin\"},{\"id\": 30,\"preName\": \"Carl\",\"name\": \"Collars\"}]}";
    }

    @Test 
    public void testPullFromDMZ() {
    	
    	testData =  new StandardizedPatient();
        testData.setName("patient1");
        testData.persist();
        testData.merge();
        
        dataFromDMZ  = "{\"class\":\"sp_portal.local.StandardizedPatient\",\"id\":"+testData.getId()+",\"anamnesisForm\":{\"class\":\"sp_portal.local.AnamnesisForm\",\"id\":5,\"anamnesisChecksValues\":[{\"class\":\"sp_portal.local.AnamnesisChecksValue\",\"id\":10,\"anamnesisCheck\":{\"class\":\"sp_portal.local.AnamnesisCheck\",\"id\":8,\"origId\":5,\"sortOrder\":6,\"text\":\"Leiden Sie unter Diabetes?\",\"title\":{\"class\":\"sp_portal.local.AnamnesisCheck\",\"id\":2,\"origId\":11,\"sortOrder\":5,\"text\":\"Disease history category\",\"title\":null,\"type\":4,\"userSpecifiedOrder\":null,\"value\":\"\"},\"type\":1,\"userSpecifiedOrder\":null,\"value\":\"\"},\"anamnesisChecksValue\":null,\"anamnesisForm\":{\"_ref\":\"../..\",\"class\":\"sp_portal.local.AnamnesisForm\"},\"comment\":null,\"origId\":15,\"truth\":false},{\"class\":\"sp_portal.local.AnamnesisChecksValue\",\"id\":9,\"anamnesisCheck\":{\"class\":\"sp_portal.local.AnamnesisCheck\",\"id\":4,\"origId\":1,\"sortOrder\":2,\"text\":\"Rauchen Sie?\",\"title\":{\"class\":\"sp_portal.local.AnamnesisCheck\",\"id\":1,\"origId\":10,\"sortOrder\":1,\"text\":\"Personal lifestyle category\",\"title\":null,\"type\":4,\"userSpecifiedOrder\":null,\"value\":\"\"},\"type\":1,\"userSpecifiedOrder\":null,\"value\":\"\"},\"anamnesisChecksValue\":null,\"anamnesisForm\":{\"_ref\":\"../..\",\"class\":\"sp_portal.local.AnamnesisForm\"},\"comment\":null,\"origId\":14,\"truth\":false}],\"createDate\":\"2009-09-18T16:00:00Z\",\"origId\":6,\"scars\":[],\"standardizedPatients\":[{\"_ref\":\"../..\",\"class\":\"sp_portal.local.StandardizedPatient\"}]},\"bankaccount\":{\"class\":\"sp_portal.local.Bankaccount\",\"id\":5,\"bankName\":\"KTS\",\"bic\":\"BENDSFF1JEV\",\"city\":null,\"iban\":\"CH78 5685 7565 4364 7\",\"origId\":31,\"ownerName\":null,\"postalCode\":null,\"standardizedPatients\":[{\"_ref\":\"../..\",\"class\":\"sp_portal.local.StandardizedPatient\"}]},\"birthday\":\"1965-09-23T16:00:00Z\",\"city\":\"Basel\",\"description\":null,\"email\":\"beddebu@hss.ch\",\"gender\":1,\"height\":182,\"immagePath\":null,\"maritalStatus\":null,\"mobile\":\"078 586 29 84\",\"name\":\"Buser\",\"nationality\":{\"class\":\"sp_portal.local.Nationality\",\"id\":2,\"nationality\":\"Deutschland\",\"origId\":6},\"origId\":23,\"postalCode\":4051,\"preName\":\"Bettina\",\"profession\":{\"class\":\"sp_portal.local.Profession\",\"id\":5,\"origId\":6,\"profession\":\"Florist/in\"},\"socialInsuranceNo\":null,\"street\":\"Rankenbergweg 1\",\"telephone\":null,\"telephone2\":null,\"videoPath\":null,\"weight\":82,\"workPermission\":null}";
        
        try {
			instance.pullFromDMZ(testData.getId());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			e1.getCause().printStackTrace();
			Assert.fail("error occured " + e1.getMessage());
		}

		
		StandardizedPatient patient = StandardizedPatient.findStandardizedPatient(testData.getId());

		assertEquals(testData.getId(), (Long)instance.spParam);
	   // assertEquals(Gender.FEMALE,patient.getGender());
	    assertEquals("Buser",patient.getName());
	
	    assertEquals("Bettina",patient.getPreName());
	
	    assertEquals("Rankenbergweg 1",patient.getStreet());
	
	    assertEquals("Basel",patient.getCity());
	
	    assertEquals((Integer)4051,patient.getPostalCode());
	
	    assertEquals(null,patient.getTelephone());
	
	    assertEquals(null,patient.getTelephone2());
	
	    assertEquals("078 586 29 84",patient.getMobile());
	
	    assertEquals((Integer)182,patient.getHeight());
	
	    assertEquals((Integer)82,patient.getWeight());
	
	    assertEquals(null,patient.getImmagePath());
	
	    assertEquals(null,patient.getVideoPath());
        
         Date expectedDate = null ;
         try {
             String str_date="2009-09-18T16:00:00Z";
             DateFormat formatter ;
 
             formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
             expectedDate = (Date)formatter.parse(str_date);
 
         } catch (ParseException e){
			 fail("format error");
 
         }



//         assertEquals(expectedDate,instance.updateData.getAnamnesisForm().getCreateDate());


    }


    @Test 
    public void testPullFemaleFromDMZ() {
        testData =  new StandardizedPatient();
        testData.setName("patient2");
        testData.persist();
        testData.merge();

		dataFromDMZ  = "{\"class\":\"sp_portal.local.StandardizedPatient\",\"id\":"+testData.getId()+",\"anamnesisForm\":{\"class\":\"sp_portal.local.AnamnesisForm\",\"id\":5,\"anamnesisChecksValues\":[{\"class\":\"sp_portal.local.AnamnesisChecksValue\",\"id\":10,\"anamnesisCheck\":{\"class\":\"sp_portal.local.AnamnesisCheck\",\"id\":8,\"origId\":5,\"sortOrder\":6,\"text\":\"Leiden Sie unter Diabetes?\",\"title\":{\"class\":\"sp_portal.local.AnamnesisCheck\",\"id\":2,\"origId\":11,\"sortOrder\":5,\"text\":\"Disease history category\",\"title\":null,\"type\":4,\"userSpecifiedOrder\":null,\"value\":\"\"},\"type\":1,\"userSpecifiedOrder\":null,\"value\":\"\"},\"anamnesisChecksValue\":null,\"anamnesisForm\":{\"_ref\":\"../..\",\"class\":\"sp_portal.local.AnamnesisForm\"},\"comment\":null,\"origId\":15,\"truth\":false},{\"class\":\"sp_portal.local.AnamnesisChecksValue\",\"id\":9,\"anamnesisCheck\":{\"class\":\"sp_portal.local.AnamnesisCheck\",\"id\":4,\"origId\":1,\"sortOrder\":2,\"text\":\"Rauchen Sie?\",\"title\":{\"class\":\"sp_portal.local.AnamnesisCheck\",\"id\":1,\"origId\":10,\"sortOrder\":1,\"text\":\"Personal lifestyle category\",\"title\":null,\"type\":4,\"userSpecifiedOrder\":null,\"value\":\"\"},\"type\":1,\"userSpecifiedOrder\":null,\"value\":\"\"},\"anamnesisChecksValue\":null,\"anamnesisForm\":{\"_ref\":\"../..\",\"class\":\"sp_portal.local.AnamnesisForm\"},\"comment\":null,\"origId\":14,\"truth\":false}],\"createDate\":\"2009-09-18T16:00:00Z\",\"origId\":6,\"scars\":[],\"standardizedPatients\":[{\"_ref\":\"../..\",\"class\":\"sp_portal.local.StandardizedPatient\"}]},\"bankaccount\":{\"class\":\"sp_portal.local.Bankaccount\",\"id\":5,\"bankName\":\"KTS\",\"bic\":\"BENDSFF1JEV\",\"city\":null,\"iban\":\"CH78 5685 7565 4364 7\",\"origId\":31,\"ownerName\":null,\"postalCode\":null,\"standardizedPatients\":[{\"_ref\":\"../..\",\"class\":\"sp_portal.local.StandardizedPatient\"}]},\"birthday\":\"1965-09-23T16:00:00Z\",\"city\":\"Basel\",\"description\":null,\"email\":\"beddebu@hss.ch\",\"gender\":2,\"height\":182,\"immagePath\":null,\"maritalStatus\":null,\"mobile\":\"078 586 29 84\",\"name\":\"Buser\",\"nationality\":{\"class\":\"sp_portal.local.Nationality\",\"id\":2,\"nationality\":\"Deutschland\",\"origId\":6},\"origId\":23,\"postalCode\":4051,\"preName\":\"Bettina\",\"profession\":{\"class\":\"sp_portal.local.Profession\",\"id\":5,\"origId\":6,\"profession\":\"Florist/in\"},\"socialInsuranceNo\":null,\"street\":\"Rankenbergweg 1\",\"telephone\":null,\"telephone2\":null,\"videoPath\":null,\"weight\":82,\"workPermission\":null}";

                
        try {
			instance.pullFromDMZ(testData.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.getCause().printStackTrace();
			Assert.fail("error occured " + e.getMessage());
		}
		StandardizedPatient patient = StandardizedPatient.findStandardizedPatient(testData.getId());
	
        assertEquals(testData.getId(), (Long)instance.spParam);
        assertEquals(Gender.FEMALE,patient.getGender());
        assertEquals("Buser",patient.getName());

        assertEquals("Bettina",patient.getPreName());

        assertEquals("Rankenbergweg 1",patient.getStreet());

        assertEquals("Basel",patient.getCity());

        assertEquals((Integer)4051,patient.getPostalCode());

        assertEquals(null,patient.getTelephone());

        assertEquals(null,patient.getTelephone2());

        assertEquals("078 586 29 84",patient.getMobile());

        assertEquals((Integer)182,patient.getHeight());

        assertEquals((Integer)82,patient.getWeight());

        assertEquals(null,patient.getImmagePath());

        assertEquals(null,patient.getVideoPath());

    }
	
	
    @Test 
    public void testPullErrorJsonFromDMZ() {
        testData =  new StandardizedPatient();
        testData.setName("patient");
        testData.persist();
        testData.merge();

		dataFromDMZ  = "abc"; 
        try {
			instance.pullFromDMZ(testData.getId());
			Assert.fail("Pull StandardizedPatient from DMZ by error json data without throws Exception!");
		} catch (Exception e) {	
		}
		
    }	


    @Test
    public void testPushToDMZ() {
        try {
			instance.pushToDMZ(22L,"en");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.getCause().printStackTrace();
			Assert.fail("error occured " + e.getMessage());			
		}

        assertEquals((Long)22L, (Long)instance.spParam);
        assertNotNull(instance.sentData);

        checkSPData(instance.sentData, 55);

    }

   @Test 
    public void testPreProcessData() {
         String data  = "{\"class\":\"sp_portal.local.StandardizedPatient\",\"id\":5,\"anamnesisForm\":{\"class\":\"sp_portal.local.AnamnesisForm\",\"id\":5,\"anamnesisChecksValues\":[{\"class\":\"sp_portal.local.AnamnesisChecksValue\",\"id\":10,\"anamnesisCheck\":{\"class\":\"sp_portal.local.AnamnesisCheck\",\"id\":8,\"origId\":5,\"sortOrder\":6,\"text\":\"Leiden Sie unter Diabetes?\",\"title\":{\"class\":\"sp_portal.local.AnamnesisCheck\",\"id\":2,\"origId\":11,\"sortOrder\":5,\"text\":\"Disease history category\",\"title\":null,\"type\":4,\"userSpecifiedOrder\":null,\"value\":\"\"},\"type\":1,\"userSpecifiedOrder\":null,\"value\":\"\"},\"anamnesisChecksValue\":null,\"anamnesisForm\":{\"_ref\":\"../..\",\"class\":\"sp_portal.local.AnamnesisForm\"},\"comment\":null,\"origId\":15,\"truth\":false},{\"class\":\"sp_portal.local.AnamnesisChecksValue\",\"id\":9,\"anamnesisCheck\":{\"class\":\"sp_portal.local.AnamnesisCheck\",\"id\":4,\"origId\":1,\"sortOrder\":2,\"text\":\"Rauchen Sie?\",\"title\":{\"class\":\"sp_portal.local.AnamnesisCheck\",\"id\":1,\"origId\":10,\"sortOrder\":1,\"text\":\"Personal lifestyle category\",\"title\":null,\"type\":4,\"userSpecifiedOrder\":null,\"value\":\"\"},\"type\":1,\"userSpecifiedOrder\":null,\"value\":\"\"},\"anamnesisChecksValue\":null,\"anamnesisForm\":{\"_ref\":\"../..\",\"class\":\"sp_portal.local.AnamnesisForm\"},\"comment\":null,\"origId\":14,\"truth\":false}],\"createDate\":\"2009-09-18T16:00:00Z\",\"origId\":6,\"scars\":[],\"standardizedPatients\":[{\"_ref\":\"../..\",\"class\":\"sp_portal.local.StandardizedPatient\"}]},\"bankaccount\":{\"class\":\"sp_portal.local.Bankaccount\",\"id\":5,\"bankName\":\"KTS\",\"bic\":\"BENDSFF1JEV\",\"city\":null,\"iban\":\"CH78 5685 7565 4364 7\",\"origId\":31,\"ownerName\":null,\"postalCode\":null,\"standardizedPatients\":[{\"_ref\":\"../..\",\"class\":\"sp_portal.local.StandardizedPatient\"}]},\"birthday\":\"1965-09-23T16:00:00Z\",\"city\":\"Basel\",\"description\":null,\"email\":\"beddebu@hss.ch\",\"gender\":1,\"height\":182,\"immagePath\":null,\"maritalStatus\":null,\"mobile\":\"078 586 29 84\",\"name\":\"Buser\",\"nationality\":{\"class\":\"sp_portal.local.Nationality\",\"id\":2,\"nationality\":\"Deutschland\",\"origId\":6},\"origId\":23,\"postalCode\":4051,\"preName\":\"Bettina\",\"profession\":{\"class\":\"sp_portal.local.Profession\",\"id\":5,\"origId\":6,\"profession\":\"Florist/in\"},\"socialInsuranceNo\":null,\"street\":\"Rankenbergweg 1\",\"telephone\":null,\"telephone2\":null,\"videoPath\":null,\"weight\":82,\"workPermission\":null}";

         String expected  = "{\"class\":\"ch.unibas.medizin.osce.domain.StandardizedPatient\",\"anamnesisForm\":{\"class\":\"ch.unibas.medizin.osce.domain.AnamnesisForm\",\"anamnesischecksvalues\":[{\"class\":\"ch.unibas.medizin.osce.domain.AnamnesisChecksValue\",\"anamnesischeck\":{\"class\":\"ch.unibas.medizin.osce.domain.AnamnesisCheck\",\"id\":5,\"sortOrder\":6,\"text\":\"Leiden Sie unter Diabetes?\",\"title\":{\"class\":\"ch.unibas.medizin.osce.domain.AnamnesisCheck\",\"id\":11,\"sortOrder\":5,\"text\":\"Disease history category\",\"title\":null,\"type\":4,\"userSpecifiedOrder\":null,\"value\":\"\"},\"type\":1,\"userSpecifiedOrder\":null,\"value\":\"\"},\"anamnesisChecksValue\":null,\"anamnesisForm\":{\"ignoreMe\":\"../..\",\"class\":\"ch.unibas.medizin.osce.domain.AnamnesisForm\"},\"comment\":null,\"id\":15,\"truth\":false},{\"class\":\"ch.unibas.medizin.osce.domain.AnamnesisChecksValue\",\"anamnesischeck\":{\"class\":\"ch.unibas.medizin.osce.domain.AnamnesisCheck\",\"id\":1,\"sortOrder\":2,\"text\":\"Rauchen Sie?\",\"title\":{\"class\":\"ch.unibas.medizin.osce.domain.AnamnesisCheck\",\"id\":10,\"sortOrder\":1,\"text\":\"Personal lifestyle category\",\"title\":null,\"type\":4,\"userSpecifiedOrder\":null,\"value\":\"\"},\"type\":1,\"userSpecifiedOrder\":null,\"value\":\"\"},\"anamnesisChecksValue\":null,\"anamnesisForm\":{\"ignoreMe\":\"../..\",\"class\":\"ch.unibas.medizin.osce.domain.AnamnesisForm\"},\"comment\":null,\"id\":14,\"truth\":false}],\"createDate\":\"2009-09-18T16:00:00Z\",\"id\":6,\"scars\":[],\"standardizedPatients\":[{\"ignoreMe\":\"../..\",\"class\":\"ch.unibas.medizin.osce.domain.StandardizedPatient\"}]},\"bankAccount\":{\"class\":\"ch.unibas.medizin.osce.domain.Bankaccount\",\"bankName\":\"KTS\",\"BIC\":\"BENDSFF1JEV\",\"city\":null,\"IBAN\":\"CH78 5685 7565 4364 7\",\"id\":31,\"ownerName\":null,\"postalCode\":null,\"standardizedPatients\":[{\"ignoreMe\":\"../..\",\"class\":\"ch.unibas.medizin.osce.domain.StandardizedPatient\"}]},\"birthday\":\"1965-09-23T16:00:00Z\",\"city\":\"Basel\",\"description\":null,\"email\":\"beddebu@hss.ch\",\"gender\":1,\"height\":182,\"immagePath\":null,\"maritalStatus\":null,\"mobile\":\"078 586 29 84\",\"name\":\"Buser\",\"nationality\":{\"class\":\"ch.unibas.medizin.osce.domain.Nationality\",\"nationality\":\"Deutschland\",\"id\":6},\"id\":23,\"postalCode\":4051,\"preName\":\"Bettina\",\"profession\":{\"class\":\"ch.unibas.medizin.osce.domain.Profession\",\"id\":6,\"profession\":\"Florist/in\"},\"socialInsuranceNo\":null,\"street\":\"Rankenbergweg 1\",\"telephone\":null,\"telephone2\":null,\"videoPath\":null,\"weight\":82,\"workPermission\":null}";
         //                                                                                                        																																																								 					 
         					 												
         String ret = instance.preProcessData(data);
       
         assertEquals("preProcessData ",expected,ret);


    }



    @Test
    public void testSendData() {
        MyDMZSyncServiceImpl instance2 = new MyDMZSyncServiceImpl();

        returnData = "{\"class\":\"sp_portal.local.StandardizedPatient\",\"id\":23,\"anamnesisForm\":{\"class\":\"sp_portal.local.AnamnesisForm\",\"id\":5,\"createDate\":\"2009-09-18T16:00:00Z\",\"origId\":6,\"standardizedPatients\":[{\"_ref\":\"../..\",\"class\":\"sp_portal.local.StandardizedPatient\"}]},\"bankaccount\":{\"class\":\"sp_portal.local.Bankaccount\",\"id\":5,\"bankName\":\"KTS\",\"bic\":\"BENDSFF1JEV\",\"city\":null,\"iban\":\"CH78 5685 7565 4364 7\",\"origId\":31,\"ownerName\":null,\"postalCode\":null,\"standardizedPatients\":[{\"_ref\":\"../..\",\"class\":\"sp_portal.local.StandardizedPatient\"}]},\"birthday\":\"1965-09-23T16:00:00Z\",\"city\":\"PaulVille\",\"description\":null,\"email\":\"beddebu@hss.ch\",\"gender\":1,\"height\":182,\"immagePath\":null,\"maritalStatus\":null,\"mobile\":\"078 586 29 84\",\"name\":\"Buser\",\"nationality\":{\"class\":\"sp_portal.local.Nationality\",\"id\":2,\"nationality\":\"Deutschland\",\"origId\":6},\"origId\":23,\"postalCode\":4051,\"preName\":\"Bettina\",\"profession\":{\"class\":\"sp_portal.local.Profession\",\"id\":5,\"origId\":6,\"profession\":\"Florist/in\"},\"socialInsuranceNo\":null,\"street\":\"Rankenbergweg 1\",\"telephone\":\"9999999999\",\"telephone2\":null,\"videoPath\":null,\"weight\":82,\"workPermission\":null}";
		String excptedData = null;
		try {
		    String url = instance2.getHostAddress() + "/sp_portal/DataImportExport/importSP";
			excptedData = instance2.sendData(returnData,url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.getCause().printStackTrace();
			Assert.fail("error occured " + e.getMessage());			
		}

		assertEquals(excptedData,returnData);
    }

   // @Test
    public void testGetDMZDataForPatient(){
        MyDMZSyncServiceImpl2 instance2 = new MyDMZSyncServiceImpl2();
        String ret;
		try {
			ret = instance2.getDMZDataForPatient(23L);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.getCause().printStackTrace();
			Assert.fail("error occured " + e.getMessage());						
		}

    }



    ///////////////////////////////////////////////////////////////

//{"anamnesisForm":null,"bankAccount":null,"birthday":null,"city":"city55","class":"ch.unibas.medizin.osce.domain.StandardizedPatient","descriptions":null,"email":null,"gender":"MALE","height":55,"id":null,"immagePath":"immagePath55","maritalStatus":null,"mobile":"mobile55","name":"name55","nationality":null,"postalCode":55,"preName":"preName55","profession":null,"socialInsuranceNo":null,"street":"street55","telephone":"telephone55","telephone2":"telephone255","version":null,"videoPath":"videoPath55","weight":55,"workPermission":null}]>
    private void checkSPData(String data, Integer value){
        checkStrSPField(data,"gender","" +Gender.MALE);
        checkStrSPField(data,"name", "name" + value);
        checkStrSPField(data,"preName", "preName" + value);
        checkStrSPField(data,"street", "street" + value);
        checkStrSPField(data,"city" , "city" + value);
        checkNumSPField(data,"postalCode", ""+value);
        checkStrSPField(data,"telephone", "telephone" + value);
        checkStrSPField(data,"telephone2", "telephone2" + value);
        checkStrSPField(data,"mobile","mobile" + value);
        checkNumSPField(data,"height","" + value);
        checkNumSPField(data,"weight", "" +value);
        checkStrSPField(data,"immagePath", "immagePath" + value);
        checkStrSPField(data,"videoPath", "videoPath" + value);


    }

    private void checkStrSPField(String data, String name, String value){

        assertTrue("" + name + " with value + <<" + value +">> not found", data.contains("\"" + name +"\":\"" + value + "\""));

    }

    private void checkNumSPField(String data, String name, String value){

        assertTrue("" + name + " with value + <<" + value +">> not found", data.contains("\"" + name +"\":" + value + ""));

    }


    void setUpData(StandardizedPatient sp, Integer value){

        sp.setGender(Gender.MALE);
        sp.setName("name" + value);
        sp.setPreName("preName" + value);
        sp.setStreet("street" + value);
        sp.setCity("city" + value);
        sp.setPostalCode(value);
        sp.setTelephone("telephone" + value);
        sp.setTelephone2("telephone2" + value);
        sp.setMobile("mobile" + value);
        sp.setHeight(value);
        sp.setWeight(value);
        sp.setImmagePath("immagePath" + value);
        sp.setVideoPath("videoPath" + value);
        sp.setBirthday(new Date());
        //sp.persist();

    }
	
	/**
	 *The date of the format string into "yyyy-MM-dd 'T' HH: MM: ss 'Z'" format
	 */
	private Date convertToDate(String dateStr){
		DateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

		Date date=null;
		try {
			if(dateStr!=null && !dateStr.equals("")){
				date = sdf.parse(dateStr);
			}else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return date;
	}
	


    private class MyDMZSyncServiceImpl extends DMZSyncServiceImpl{
        public Long spParam = null;
        public String sentData = null;
        public StandardizedPatient savedData = null;
        public StandardizedPatient updateData = null;
        
        protected StandardizedPatient findPatient(Long objectId){
            spParam = objectId;
            return currentSP;
        }

       /**
        * Send data to the DMZ server
        */
       protected String sendData(String json,String url){
            sentData = json;
            expectedURL = url;
            return returnData;

       }
	   protected List<String> getSendReturnErrorMessage(String json){
			List<String> returnJstr = null;
			return returnJstr;
	   }

		protected String getSyncJsonData(String locale){
		
			return getSendJsonData();
		}

        /**
         * Request data from the DMZ
         */
         protected String getDMZDataForPatient(Long standardizedPatientId){
            spParam = standardizedPatientId;
            return dataFromDMZ;
         }

        /**
         * Save a patient
         */
         protected void savePatient(StandardizedPatient patient){
            savedData = patient;
         }

         protected String getHostAddress(){
             return "http://localhost:8090";
         }
    }

    private class MyDMZSyncServiceImpl2 extends DMZSyncServiceImpl{
        public Long spParam = null;
        public String sentData = null;

        protected StandardizedPatient findPatient(Long objectId){
            spParam = objectId;
            return currentSP;
        }


        /**
         * Save a patient
         */
         protected void savePatient(StandardizedPatient patient){

         }

         protected String getHostAddress(){
             return "http://localhost:8090";
         }
    }
	
	/**
	 * clear Database after each test 	
	 */
	private void clearDB(){
		
		try{
			
			List<PatientInSemester> semesters = PatientInSemester.findAllPatientInSemesters();
			for(PatientInSemester s : semesters){
				PatientInSemester semester = PatientInSemester.findPatientInSemester(s.getId());
				if(semester != null){
					semester.remove();
					semester.flush();
				}
			}
			
			List<Training> trainings = Training.findAllTrainings();
			for(Training t: trainings){
				Training training = Training.findTraining(t.getId());
				if(training !=null){
					training.remove();
					training.flush();
				}
			}

			List<OsceDay> osceDays = OsceDay.findAllOsceDays();
			for(OsceDay o: osceDays){
				OsceDay osceDay = OsceDay.findOsceDay(o.getId());
				if(osceDay!=null){
					osceDay.remove();
					osceDay.flush();
				}
			}
			
			List<StandardizedPatient> patients = StandardizedPatient.findAllStandardizedPatients();
			for(StandardizedPatient patient : patients){
				StandardizedPatient rPatient = StandardizedPatient.findStandardizedPatient(patient.getId());
				if(rPatient!=null){
					rPatient.remove();
					rPatient.flush();
				}
			}
									
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
