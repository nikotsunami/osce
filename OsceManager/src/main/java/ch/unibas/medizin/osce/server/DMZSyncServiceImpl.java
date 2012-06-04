package ch.unibas.medizin.osce.server;

import java.io.IOException;

import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.DMZSyncService;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import ch.unibas.medizin.osce.domain.*;
import flexjson.JSONSerializer;
import flexjson.JSONDeserializer;
import flexjson.transformer.DateTransformer;

import flexjson.ObjectFactory;
import flexjson.ObjectBinder;
import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.MaritalStatus;
import ch.unibas.medizin.osce.shared.WorkPermission;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class DMZSyncServiceImpl extends RemoteServiceServlet implements
		DMZSyncService {

	@Override
	public void pushToDMZ(Long standardizedPatientId) {

		StandardizedPatient patient = findPatient(standardizedPatientId);

		JSONSerializer serializer = new JSONSerializer();
		String json = serializer.exclude("*.class").serialize(patient);
		sendData(json);
	}
	
	

	@Override
	public void pullFromDMZ(Long standardizedPatientId) {
		System.err.println(" pullToDMZ(Integer standardizedPatientId) "
				+ standardizedPatientId);
		try {
			String data = getDMZDataForPatient(standardizedPatientId);

			data = preProcessData(data);


			JSONDeserializer deserializer = new JSONDeserializer()
					.use("anamnesisForm.createDate",
							new DateTransformer("yyyy-MM-dd'T'HH:mm:ss'Z'"))
					.use("birthday",
							new DateTransformer("yyyy-MM-dd'T'HH:mm:ss'Z'"))
					.use("gender", new GenderTransformer())
					.use("maritalStatus", new MaritalStatusTransformer())
					.use("workPermission", new WorkPermissionTransformer());

			StandardizedPatient newPatient = (StandardizedPatient) (deserializer
					.use(null, StandardizedPatient.class).deserialize(data));
			
				
			StandardizedPatient patient = StandardizedPatient.findStandardizedPatient(standardizedPatientId);
			if( patient != null){
				updatePatient(patient,newPatient);
			}else{
				savePatient(newPatient);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void updatePatient(StandardizedPatient patient,StandardizedPatient newPatient){
		patient.setName(newPatient.getName());
		patient.setPreName(newPatient.getPreName());
		patient.setStreet(newPatient.getStreet());
		patient.setCity(newPatient.getCity());
		patient.setPostalCode(newPatient.getPostalCode());
		patient.setTelephone(newPatient.getTelephone());
		patient.setTelephone2(newPatient.getTelephone2());
		patient.setMobile(newPatient.getMobile());
		patient.setHeight(newPatient.getHeight());
		patient.setWeight(newPatient.getWeight());
		
		patient.setImmagePath(newPatient.getImmagePath());
		patient.setVideoPath(newPatient.getVideoPath());
		patient.setBirthday(newPatient.getBirthday());
		patient.setEmail(newPatient.getEmail());
		patient.setSocialInsuranceNo(newPatient.getSocialInsuranceNo());
		
		patient.setGender(newPatient.getGender());
		patient.setMaritalStatus(newPatient.getMaritalStatus());
		patient.setWorkPermission(newPatient.getWorkPermission());
		
		if(newPatient.getNationality() != null){
			Nationality nationality = Nationality.findNationality(newPatient.getNationality().getId());
			if(nationality != null){
				patient.setNationality(nationality);
			}
		}
		if(newPatient.getProfession() !=null){
			Profession profession = Profession.findProfession(newPatient.getProfession().getId());
			if(profession != null){
				patient.setProfession(profession);
			}	
		}
		
		if(newPatient.getDescriptions() !=null){
			Description descriptions = Description.findDescription(newPatient.getDescriptions().getId());
			if(descriptions != null){
				patient.setDescriptions(descriptions);
			}	
		}	
		
		if(newPatient.getBankAccount() !=null){
			Bankaccount bankAccount = Bankaccount.findBankaccount(newPatient.getBankAccount().getId());
			if(bankAccount != null){
				patient.setBankAccount(bankAccount);
			}	
		}	
		
		if(newPatient.getAnamnesisForm() !=null){
			AnamnesisForm anamnesisForm = AnamnesisForm.findAnamnesisForm(newPatient.getAnamnesisForm().getId());
			if(anamnesisForm != null){
				patient.setAnamnesisForm(anamnesisForm);
			}	
		}
		

		patient.flush();
	}

	public String preProcessData(String data) {
		data = data.replaceAll("\"id\":[0-9]*,", "");
		data = data.replaceAll("origId", "id");
		data = data.replaceAll("bankaccount", "bankAccount");
		data = data.replaceAll("sp_portal\\.local",
				"ch.unibas.medizin.osce.domain");

		return data;

	}
	

	protected StandardizedPatient findPatient(Long objectId) {
		return StandardizedPatient.findStandardizedPatient(objectId);
	}

	private class GenderTransformer implements ObjectFactory {

		public Object instantiate(ObjectBinder context, Object value,
				Type targetType, Class targetClass) {
			Integer valueI = (Integer) value;
			if (valueI == 1) {
				return Gender.MALE;
			} else {
				return Gender.FEMALE;
			}
		}
	}

	private class MaritalStatusTransformer implements ObjectFactory {

		public Object instantiate(ObjectBinder context, Object value,
				Type targetType, Class targetClass) {
			Integer valueI = (Integer) value;
			if (valueI == 0) {
				return MaritalStatus.UNMARRIED;
			} else if(valueI == 1){
				return MaritalStatus.MARRIED;
			} else if(valueI == 2){
				return MaritalStatus.CIVIL_UNION;
			} else if(valueI == 3){
				return MaritalStatus.DIVORCED;
			} else if(valueI == 4){
				return MaritalStatus.SEPARATED;
			} else if(valueI == 5){
				return MaritalStatus.WIDOWED;
			}else {
				return null;
			}
			
		}
	}
	


	private class WorkPermissionTransformer implements ObjectFactory {

		public Object instantiate(ObjectBinder context, Object value,
				Type targetType, Class targetClass) {
			Integer valueI = (Integer) value;
			if (valueI == 0) {
				return WorkPermission.B;
			} else if(valueI == 1){
				return WorkPermission.L;
			} else if(valueI == 2){
				return WorkPermission.C;
			} else {
				return null;
			}
			
		}
	}
	
	/**
	 * Send data to the DMZ server
	 */
	protected void sendData(String json) {

		HttpClient httpClient = new HttpClient();

		String hostAddress = getHostAddress();

		String url = hostAddress + "/sp_portal/DataImportExport/importSP";

		PostMethod postMethod = new PostMethod(url);

		NameValuePair[] registerInform = { new NameValuePair("data", json),

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

		if (!(statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY)) {
			System.err.println("field.");
			return;
		}
	}

	/**
	 * returns the host address
	 */
	protected String getHostAddress() {

		String hostAddress = getThreadLocalRequest().getSession()
				.getServletContext().getInitParameter("DMZ_HOST_ADDRESS");
		return hostAddress;
	}

	/**
	 * Request data from the DMZ
	 */
	protected String getDMZDataForPatient(Long standardizedPatientId) {
		
		String ret = null;

		HttpClient httpClient = new HttpClient();

		String url = getHostAddress()
				+ "/sp_portal/DataImportExport/exportSP?id="
				+ standardizedPatientId;

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
	protected void savePatient(StandardizedPatient patient) {
		System.err.println("In Save Patient "  + patient.getBankAccount());
		if (patient.getDescriptions() != null ){
			
			Description description = patient.getDescriptions();
			if (description.getId() != null ){
				
				description = description.merge();	
			} else {			
				description.persist();
			}
			patient.setDescriptions(description);
		}
		
		if (patient.getBankAccount() != null ){
			
			if (patient.getBankAccount().getCountry() != null ){
				Nationality nationality = patient.getBankAccount().getCountry();
				if (nationality.getId() != null ){
					
					nationality = nationality.merge();	
				} else {			
					nationality.persist();
				}
				patient.getBankAccount().setCountry(nationality);
				
			}
			
			Bankaccount bankAccount = patient.getBankAccount();
			if (bankAccount.getId() != null ){
				bankAccount = bankAccount.merge();	
			} else {
				bankAccount.persist();

			}
		
			patient.setBankAccount(bankAccount);
		}
		

		if (patient.getNationality() != null) {

			Nationality nationality  = patient.getNationality();
			if (nationality.getId() != null) {
				nationality  = nationality.merge();
			} else {
				 nationality.persist();
			}
			patient.setNationality(nationality);
		}
		

		if (patient.getProfession() != null) {

			Profession profession = patient.getProfession();
			if (profession.getId() != null) {
				profession  = profession.merge();
			} else {
				profession.persist();
			}
			patient.setProfession(profession);
		}


		if (patient.getAnamnesisForm() != null){
			

			if (patient.getAnamnesisForm().getScars() != null){
				
			}
			
			
			
			if (patient.getAnamnesisForm().getAnamnesischecksvalues() != null){
				Set<AnamnesisChecksValue> anamnesischecksvalues = new HashSet<AnamnesisChecksValue>();
				
				for (AnamnesisChecksValue aCheckValue : patient.getAnamnesisForm().getAnamnesischecksvalues()){
					
					// TODO
					
					if (aCheckValue.getAnamnesischeck() != null) {

						AnamnesisCheck check = aCheckValue.getAnamnesischeck();
						if (check.getId() != null) {
							check  = check.merge();
						} 
						aCheckValue.setAnamnesischeck(check); 
					}
					
					if (aCheckValue.getId() != null){
						aCheckValue  = aCheckValue.merge();
					} else {
						aCheckValue.persist();
					}
					
					anamnesischecksvalues.add(aCheckValue);
				}
				
				patient.getAnamnesisForm().setAnamnesischecksvalues(anamnesischecksvalues); 
				
			}
			
			
			
		}

		
		patient.merge();
		patient.persist();
		
		
	}

}
