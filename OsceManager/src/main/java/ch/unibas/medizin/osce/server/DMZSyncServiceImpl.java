package ch.unibas.medizin.osce.server;

import java.io.IOException;

import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.DMZSyncService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import ch.unibas.medizin.osce.domain.StandardizedPatient;
import flexjson.JSONSerializer;
import flexjson.JSONDeserializer;
import flexjson.transformer.DateTransformer;

import flexjson.ObjectFactory;
import flexjson.ObjectBinder;
import ch.unibas.medizin.osce.shared.Gender;
import java.lang.reflect.Type;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;




public class DMZSyncServiceImpl extends RemoteServiceServlet implements DMZSyncService {


    @Override
    public void pushToDMZ(Long standardizedPatientId){
        System.err.println(" pushToDMZ(Integer standardizedPatientId) " + standardizedPatientId);
        StandardizedPatient patient = findPatient(standardizedPatientId);

        JSONSerializer serializer = new JSONSerializer();
        String json = serializer.serialize( patient );
        sendData(json);
    }

    @Override
    public void pullFromDMZ(Long standardizedPatientId){
        String data = getDMZDataForPatient( standardizedPatientId);
        data = preProcessData(data);

        JSONDeserializer deserializer =  new JSONDeserializer().
                                                use("anamnesisForm.createDate", new DateTransformer("yyyy-MM-dd'T'HH:mm:ss'Z'")).
                                                    use("birthday", new DateTransformer("yyyy-MM-dd'T'HH:mm:ss'Z'")).
                                                        use("gender", new GenderTransformer()); 


        StandardizedPatient patient = (StandardizedPatient)(deserializer.use( null, StandardizedPatient.class ).deserialize( data ));


        savePatient(patient);

    }

    public String preProcessData(String data){
    	data = data.replaceAll("\"id\":[0-9]*,", "");
    	data = data.replaceAll("origId", "id");
//    	data = data.replaceAll("\"class\":[^}]*?,", "");
 //   	data = data.replaceAll("\"\\{class\":[^}]*?,", "");
    	data = data.replaceAll("sp_portal\\.local", "ch.unibas.medizin.osce.domain");
System.out.println(">>> " + data);
    	
        return data;

    }

    protected StandardizedPatient findPatient(Long objectId){
        return StandardizedPatient.findStandardizedPatient(objectId);
    }


    private class GenderTransformer implements ObjectFactory{

       public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
            Integer valueI =  (Integer)value;
            if (valueI == 1){
                return Gender.MALE;
            } else {
                return Gender.FEMALE;
            }
        }
    }


    /**
     * Send data to the DMZ server
     */
    protected void sendData(String json){


          HttpClient httpClient = new HttpClient();

          String hostAddress = getHostAddress();


          String url = hostAddress + "/sp_portal/dataImportExport/importSP";


          PostMethod postMethod = new PostMethod(url);


          NameValuePair[] registerInform = {
            new NameValuePair("data", json),

          };

          postMethod.setRequestBody(registerInform);


          int statusCode = 0;
          try {
           statusCode = httpClient.executeMethod(postMethod);
          } catch (HttpException e) {
            e.printStackTrace();
          } catch (IOException e1) {
            e1.printStackTrace();
          }

          if (!(statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY))
          {
            System.err.println("field.");
            return;
          }
     }

    /**
     * returns the host address
     */
    protected String getHostAddress(){

           String hostAddress = getThreadLocalRequest().getSession().getServletContext().getInitParameter("DMZ_HOST_ADDRESS");
           return hostAddress;
    }

    /**
     * Request data from the DMZ
     */
     protected String getDMZDataForPatient(Long standardizedPatientId){
    	  System.err.println(" XXXXXXXXXXXXXXXX REAL REQUEST FOR DATA XXXXXXXXXXXXXXXXXXXXXXXX");
          String ret = null;

          HttpClient httpClient = new HttpClient();

          String url = getHostAddress() + "/sp_portal/dataImportExport/exportSP?id=" + standardizedPatientId;


          GetMethod getMethod = new GetMethod(url);

          getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
            new DefaultHttpMethodRetryHandler());
          try {

          int statusCode = httpClient.executeMethod(getMethod);
           if (statusCode != HttpStatus.SC_OK) {
            System.err.println("Method failed: "
              + getMethod.getStatusLine());
           }

           byte[] responseBody = getMethod.getResponseBody();

           ret = new String(responseBody);
          } catch (HttpException e) {

              e.printStackTrace();
          } catch (IOException e) {
           e.printStackTrace();
          } finally {
           getMethod.releaseConnection();
          }
            return ret;
     }

    /**
     * Save a patient
     */
     protected void savePatient(StandardizedPatient patient){

    	 patient.merge();
    	 patient.persist();
    	 
     }


}
