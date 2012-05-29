package ch.unibas.medizin.osce.server;

import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.DMZSyncService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import ch.unibas.medizin.osce.domain.StandardizedPatient;
import flexjson.JSONSerializer;
import flexjson.JSONDeserializer;
import flexjson.transformer.DateTransformer;
import org.apache.http.client.methods.HttpPost;

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




        data = data.replaceAll("sp_portal.local", "ch.unibas.medizin.osce.domain") ;
2009-09-18T16:00:00Z

        JSONDeserializer deserializer =  new JSONDeserializer().use("anamnesisForm.createDate", new DateTransformer("yyyy-MM-dd"));


        StandardizedPatient patient = (StandardizedPatient)(deserializer.use( null, StandardizedPatient.class ).deserialize( data ));


        savePatient(patient);

    }

    protected StandardizedPatient findPatient(Long objectId){
        return StandardizedPatient.findStandardizedPatient(objectId);
    }

    /**
     * Send data to the DMZ server
     */
    protected void sendData(String json){

       // HttpPost post = new HttpPost(String uri);

    }

    /**
     * Request data from the DMZ
     */
     protected String getDMZDataForPatient(Long standardizedPatientId){

            // Use HTTP client to get data.
            return "";
     }

    /**
     * Save a patient
     */
     protected void savePatient(StandardizedPatient patient){

     }


}
