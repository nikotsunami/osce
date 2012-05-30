package ch.unibas.medizin.osce.server;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;
import org.springframework.roo.addon.test.RooIntegrationTest;
import ch.unibas.medizin.osce.domain.StandardizedPatient;
import ch.unibas.medizin.osce.domain.AnamnesisCheck;
import ch.unibas.medizin.osce.shared.Gender;


public class DMZSyncServiceImplTest {

    private MyDMZSyncServiceImpl instance  = null;
    private StandardizedPatient currentSP  = null;
    private String dataFromDMZ  = null;

    @Before
    public void setUp() throws Exception {
                   System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZzz " );
                   System.err.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" );
         instance = new MyDMZSyncServiceImpl();
         currentSP  =  new StandardizedPatient();
         setUpData(currentSP, 55);
    }

    @After
    public void tearDown() throws Exception {
        instance = null;
        currentSP  = null;
        dataFromDMZ  = null;
    }
/*
    @Test
    public void testPushToDMZ() {
        instance.pushToDMZ(22L);

        assertEquals((Long)22L, (Long)instance.spParam);
        assertNotNull(instance.sentData);

        checkSPData(instance.sentData, 55);

    }

*/
 /*
    @Test
    public void testPullFromDMZ() {
        dataFromDMZ  = "{\"class\":\"sp_portal.local.StandardizedPatient\",\"id\":5,\"anamnesisForm\":{\"class\":\"sp_portal.local.AnamnesisForm\",\"id\":5,\"anamnesisChecksValues\":[{\"class\":\"sp_portal.local.AnamnesisChecksValue\",\"id\":10,\"anamnesisCheck\":{\"class\":\"sp_portal.local.AnamnesisCheck\",\"id\":8,\"origId\":5,\"sortOrder\":6,\"text\":\"Leiden Sie unter Diabetes?\",\"title\":{\"class\":\"sp_portal.local.AnamnesisCheck\",\"id\":2,\"origId\":11,\"sortOrder\":5,\"text\":\"Disease history category\",\"title\":null,\"type\":4,\"userSpecifiedOrder\":null,\"value\":\"\"},\"type\":1,\"userSpecifiedOrder\":null,\"value\":\"\"},\"anamnesisChecksValue\":null,\"anamnesisForm\":{\"_ref\":\"../..\",\"class\":\"sp_portal.local.AnamnesisForm\"},\"comment\":null,\"origId\":15,\"truth\":false},{\"class\":\"sp_portal.local.AnamnesisChecksValue\",\"id\":9,\"anamnesisCheck\":{\"class\":\"sp_portal.local.AnamnesisCheck\",\"id\":4,\"origId\":1,\"sortOrder\":2,\"text\":\"Rauchen Sie?\",\"title\":{\"class\":\"sp_portal.local.AnamnesisCheck\",\"id\":1,\"origId\":10,\"sortOrder\":1,\"text\":\"Personal lifestyle category\",\"title\":null,\"type\":4,\"userSpecifiedOrder\":null,\"value\":\"\"},\"type\":1,\"userSpecifiedOrder\":null,\"value\":\"\"},\"anamnesisChecksValue\":null,\"anamnesisForm\":{\"_ref\":\"../..\",\"class\":\"sp_portal.local.AnamnesisForm\"},\"comment\":null,\"origId\":14,\"truth\":false}],\"createDate\":\"2009-09-18T16:00:00Z\",\"origId\":6,\"scars\":[],\"standardizedPatients\":[{\"_ref\":\"../..\",\"class\":\"sp_portal.local.StandardizedPatient\"}]},\"bankaccount\":{\"class\":\"sp_portal.local.Bankaccount\",\"id\":5,\"bankName\":\"KTS\",\"bic\":\"BENDSFF1JEV\",\"city\":null,\"iban\":\"CH78 5685 7565 4364 7\",\"origId\":31,\"ownerName\":null,\"postalCode\":null,\"standardizedPatients\":[{\"_ref\":\"../..\",\"class\":\"sp_portal.local.StandardizedPatient\"}]},\"birthday\":\"1965-09-23T16:00:00Z\",\"city\":\"Basel\",\"description\":null,\"email\":\"beddebu@hss.ch\",\"gender\":1,\"height\":182,\"immagePath\":null,\"maritalStatus\":null,\"mobile\":\"078 586 29 84\",\"name\":\"Buser\",\"nationality\":{\"class\":\"sp_portal.local.Nationality\",\"id\":2,\"nationality\":\"Deutschland\",\"origId\":6},\"origId\":23,\"postalCode\":4051,\"preName\":\"Bettina\",\"profession\":{\"class\":\"sp_portal.local.Profession\",\"id\":5,\"origId\":6,\"profession\":\"Florist/in\"},\"socialInsuranceNo\":null,\"street\":\"Rankenbergweg 1\",\"telephone\":null,\"telephone2\":null,\"videoPath\":null,\"weight\":82,\"workPermission\":null}";


        instance.pullFromDMZ(33L);



        assertEquals((Long)33L, (Long)instance.spParam);;
    }
*/
    
    @Test
    public void testSendData() {
    	MyDMZSyncServiceImpl2 instance2 = new MyDMZSyncServiceImpl2();
    	
    	String data = "{\"class\":\"sp_portal.local.StandardizedPatient\",\"id\":23,\"anamnesisForm\":{\"class\":\"sp_portal.local.AnamnesisForm\",\"id\":5,\"createDate\":\"2009-09-18T16:00:00Z\",\"origId\":6,\"standardizedPatients\":[{\"_ref\":\"../..\",\"class\":\"sp_portal.local.StandardizedPatient\"}]},\"bankaccount\":{\"class\":\"sp_portal.local.Bankaccount\",\"id\":5,\"bankName\":\"KTS\",\"bic\":\"BENDSFF1JEV\",\"city\":null,\"iban\":\"CH78 5685 7565 4364 7\",\"origId\":31,\"ownerName\":null,\"postalCode\":null,\"standardizedPatients\":[{\"_ref\":\"../..\",\"class\":\"sp_portal.local.StandardizedPatient\"}]},\"birthday\":\"1965-09-23T16:00:00Z\",\"city\":\"PaulVille\",\"description\":null,\"email\":\"beddebu@hss.ch\",\"gender\":1,\"height\":182,\"immagePath\":null,\"maritalStatus\":null,\"mobile\":\"078 586 29 84\",\"name\":\"Buser\",\"nationality\":{\"class\":\"sp_portal.local.Nationality\",\"id\":2,\"nationality\":\"Deutschland\",\"origId\":6},\"origId\":23,\"postalCode\":4051,\"preName\":\"Bettina\",\"profession\":{\"class\":\"sp_portal.local.Profession\",\"id\":5,\"origId\":6,\"profession\":\"Florist/in\"},\"socialInsuranceNo\":null,\"street\":\"Rankenbergweg 1\",\"telephone\":\"9999999999\",\"telephone2\":null,\"videoPath\":null,\"weight\":82,\"workPermission\":null}";
        instance2.sendData(data);

    }

    @Test
    public void testGetDMZDataForPatient(){
    	MyDMZSyncServiceImpl2 instance2 = new MyDMZSyncServiceImpl2();
    	String ret = instance2.getDMZDataForPatient(23L);
    	
    	System.err.println("Data returned is " + ret );
    	
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

    }


    private class MyDMZSyncServiceImpl extends DMZSyncServiceImpl{
        public Long spParam = null;
        public String sentData = null;

        protected StandardizedPatient findPatient(Long objectId){
            spParam = objectId;
            return currentSP;
        }

       /**
        * Send data to the DMZ server
        */
       protected void sendData(String json){
            System.out.println(" >>> json is: "+ json);
            sentData = json;
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


}
