package ch.unibas.medizin.osce.server;

import java.io.IOException;

import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.DMZSyncException;
import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.DMZSyncService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import ch.unibas.medizin.osce.domain.*;
import flexjson.JSONSerializer;
import flexjson.JSONDeserializer;
import flexjson.transformer.DateTransformer;

import flexjson.ObjectFactory;
import flexjson.ObjectBinder;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;
import ch.unibas.medizin.osce.shared.DMZSyncExceptionType;
import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.MaritalStatus;
import ch.unibas.medizin.osce.shared.WorkPermission;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Override
	public void pushToDMZ(Long standardizedPatientId) throws DMZSyncException {
			StandardizedPatient patient = findPatient(standardizedPatientId);
			String json = "";
			if(patient !=null){
				try{
					JSONSerializer serializer = new JSONSerializer();
					json = serializer.exclude("*.class").serialize(patient);	
				}catch(Exception e){
					throw new DMZSyncException(DMZSyncExceptionType.SERIALIZING_EXCEPTION,e.getMessage());
				}
				sendData(json);
			}else{
				throw new DMZSyncException(DMZSyncExceptionType.PATIENT_EXIST_EXCEPTION,"");
			}
	}
	


	@Override
	public void pullFromDMZ(Long standardizedPatientId)  throws DMZSyncException {
		String data = getDMZDataForPatient(standardizedPatientId);
		
		data =preProcessData(data);

		
		JSONDeserializer deserializer = new JSONDeserializer()
				.use("anamnesisForm.createDate",
						new DateTransformer("yyyy-MM-dd'T'HH:mm:ss'Z'"))
				.use("birthday",
						new DateTransformer("yyyy-MM-dd'T'HH:mm:ss'Z'"))
				.use("gender", new GenderTransformer())
				.use("maritalStatus", new MaritalStatusTransformer())
				.use("workPermission", new WorkPermissionTransformer())
				.use("anamnesisForm.anamnesischecksvalues.values.anamnesischeck.title.type", new AnamnesisChecksTypeTransformet())
				.use("anamnesisForm.anamnesischecksvalues.values.anamnesischeck.type", new AnamnesisChecksTypeTransformet());
		
		
		StandardizedPatient newPatient = (StandardizedPatient) (deserializer
				.use(null, StandardizedPatient.class).deserialize(data));
		

		
		
		StandardizedPatient patient = StandardizedPatient.findStandardizedPatient(standardizedPatientId);
	
		if( patient != null){
			updatePatient(patient,newPatient);
		}else{
//			throw new DMZSyncException(DMZSyncExceptionType.PATIENT_EXIST_EXCEPTION,"");
//			savePatient(newPatient);
		}

	}
	
	private static String dmzSyncExceptionType = "";
	private static String errorMsg = "";
	private void setDMZSyncExceptionTypeAndErrorMsg(String type,String msg){
		dmzSyncExceptionType = type;
		errorMsg = msg;
	}
 
	
	private void updatePatient(StandardizedPatient patient,StandardizedPatient newPatient) throws DMZSyncException{
		
		try {
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
			try{
				if(newPatient.getNationality() != null){
					Nationality nationality = Nationality.findNationality(newPatient.getNationality().getId());
					if(nationality != null){
						patient.setNationality(nationality);
					}
				}
			}catch(Exception e){
				setDMZSyncExceptionTypeAndErrorMsg(DMZSyncExceptionType.SYNC_NATIONALITY_EXCEPTION,e.getMessage());
				throw e;
			}
			try{
				if(newPatient.getProfession() !=null){
					Profession profession = Profession.findProfession(newPatient.getProfession().getId());
					if(profession != null){
						patient.setProfession(profession);
					}	
				}
			}catch(Exception e){
				setDMZSyncExceptionTypeAndErrorMsg(DMZSyncExceptionType.SYNC_PROFESSION_EXCEPTION,e.getMessage());
				throw e;
			}
			try{
				if(newPatient.getDescriptions() !=null){
					Description descriptions = Description.findDescription(newPatient.getDescriptions().getId());
					if(descriptions != null){
						patient.setDescriptions(descriptions);
					}	
				}	
			}catch(Exception e){
				setDMZSyncExceptionTypeAndErrorMsg(DMZSyncExceptionType.SYNC_DESCRIPTION_EXCEPTION,e.getMessage());
				throw e;
			}
			
			try{
				if(newPatient.getBankAccount() !=null){
					Bankaccount bankAccount = Bankaccount.findBankaccount(newPatient.getBankAccount().getId());
					if(bankAccount != null){
						patient.setBankAccount(bankAccount);
					}	
				}
			}catch(Exception e){
				setDMZSyncExceptionTypeAndErrorMsg(DMZSyncExceptionType.SYNC_BANKACCOUNT_EXCEPTION,e.getMessage());
				throw e;
			}
			try{
				if(newPatient.getAnamnesisForm() !=null){
	
				AnamnesisForm anamnesisForm = AnamnesisForm.findAnamnesisForm(newPatient.getAnamnesisForm().getId());
				
					 if(anamnesisForm != null){
							patient.setAnamnesisForm(anamnesisForm);
						}	
						Set<AnamnesisChecksValue> checksValueSet = newPatient.getAnamnesisForm().getAnamnesischecksvalues();
						if( checksValueSet != null){
							
							Iterator<AnamnesisChecksValue> iterator = checksValueSet.iterator();
							
							while(iterator.hasNext()){
								AnamnesisChecksValue newChecksValue = (AnamnesisChecksValue)iterator.next();
								
								if(newChecksValue.getId() != null){
									AnamnesisChecksValue checksValue =AnamnesisChecksValue.findAnamnesisChecksValue(newChecksValue.getId());
									if(checksValue!=null){
										updateChecksValue(checksValue, newChecksValue,anamnesisForm);
									}else{
										saveChecksValue(newChecksValue, anamnesisForm);
									}
									
								}else{
									AnamnesisChecksValue anamnesisChecksValues = AnamnesisChecksValue.findAnamnesisChecksValuesByAnamnesisFormAndAnamnesisCheck(anamnesisForm.getId(), newChecksValue.getAnamnesischeck().getId());
								
									if(anamnesisChecksValues.getId() != null){
										updateChecksValue(anamnesisChecksValues, newChecksValue,anamnesisForm);
										
									}else{
										saveChecksValue(newChecksValue, anamnesisForm);
									}
								}
								
									
							}
						}
				
				}
			}catch(Exception e){
				if(!dmzSyncExceptionType.equals(DMZSyncExceptionType.SYNC_ANAMNESISCHECKVALUES_EXCEPTION)){
					setDMZSyncExceptionTypeAndErrorMsg(DMZSyncExceptionType.SYNC_ANAMNESISFROM_EXCEPTION,e.getMessage());
				}
				throw e;
			}
			
	
			patient.flush();
		
		} catch (Exception e){
			if(dmzSyncExceptionType==null &&dmzSyncExceptionType.equals("")){
				setDMZSyncExceptionTypeAndErrorMsg(DMZSyncExceptionType.PATIENT_EXIST_EXCEPTION,e.getMessage());
			}
			throw new DMZSyncException(dmzSyncExceptionType,errorMsg);
		}
	}
	
	/**
	 *update the check values
	 **/
	private void updateChecksValue(AnamnesisChecksValue checksValue,AnamnesisChecksValue newChecksValue,AnamnesisForm anamnesisForm) throws Exception,DMZSyncException{
		try{
			checksValue.setTruth(newChecksValue.getTruth());
			checksValue.setComment(newChecksValue.getComment());
			checksValue.setAnamnesisChecksValue(newChecksValue.getAnamnesisChecksValue());
			checksValue.flush();
		}catch(Exception ex){
			setDMZSyncExceptionTypeAndErrorMsg(DMZSyncExceptionType.SYNC_ANAMNESISCHECKVALUES_EXCEPTION,ex.getMessage());
			throw ex;
		}
	}
	
	private void saveChecksValue(AnamnesisChecksValue newChecksValue,AnamnesisForm anamnesisForm) throws Exception,DMZSyncException{
		try{
			AnamnesisChecksValue checksValue = new AnamnesisChecksValue();
			checksValue.setTruth(newChecksValue.getTruth());
			checksValue.setComment(newChecksValue.getComment());
			
			checksValue.setAnamnesisChecksValue(newChecksValue.getAnamnesisChecksValue());
			
			checksValue.setAnamnesisform(anamnesisForm);
		
			AnamnesisCheck anamnesisCheck = AnamnesisCheck.findAnamnesisCheck(newChecksValue.getAnamnesischeck().getId());
					
			if(anamnesisCheck !=null){
			 	checksValue.setAnamnesischeck(anamnesisCheck);
			}
		
			
			checksValue.merge();
		}catch(Exception e){
			setDMZSyncExceptionTypeAndErrorMsg(DMZSyncExceptionType.SYNC_ANAMNESISCHECKVALUES_EXCEPTION,e.getMessage());
			throw e;
		}
	}

	public String preProcessData(String data) {
		data = data.replaceAll("\"id\":[0-9]*,", "");
		data = data.replaceAll("origId", "id");
		data = data.replaceAll("bankaccount", "bankAccount");
		data = data.replaceAll("sp_portal\\.local","ch.unibas.medizin.osce.domain");
		data = data.replaceAll("anamnesisChecksValues","anamnesischecksvalues");
		data = data.replaceAll("\"anamnesisCheck\"","\"anamnesischeck\"");
		data = data.replaceAll("\"_ref\"","\"ignoreMe\"");
		
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

	private class AnamnesisChecksTypeTransformet implements ObjectFactory {

		public Object instantiate(ObjectBinder context, Object value,
				Type targetType, Class targetClass) {
			Integer valueI = (Integer) value;
			if (valueI == 0) {
				return AnamnesisCheckTypes.QUESTION_MULT_M;
			} else if(valueI == 1){
				return AnamnesisCheckTypes.QUESTION_MULT_S;
			} else if(valueI == 2){
				return AnamnesisCheckTypes.QUESTION_OPEN;
			} else if(valueI == 3){
				return AnamnesisCheckTypes.QUESTION_TITLE;
			} else if(valueI == 4){
				return AnamnesisCheckTypes.QUESTION_YES_NO;
			}  else {
				return null;
			}
			
		}
	}
	

	/**
	 * Send data to the DMZ server
	 * @throws DMZSyncException 
	 */
	protected void sendData(String json) throws DMZSyncException {

		HttpClient httpClient = new HttpClient();

		String hostAddress = getHostAddress();

	    String url = hostAddress + "/sp_portal/DataImportExport/importSP";
	    
		PostMethod postMethod = new PostMethod(url);

		NameValuePair[] registerInform = new NameValuePair[1];
		registerInform[0] = new NameValuePair("data", json);

		postMethod.setRequestBody(registerInform);

		int statusCode = 0;
		try {
			statusCode = httpClient.executeMethod(postMethod);
		} catch (HttpException e) {
			throw new DMZSyncException(DMZSyncExceptionType.HTTP_EXCEPTION,url+": "+e.getMessage());
		} catch (IOException e1) {
			throw new DMZSyncException(DMZSyncExceptionType.CONNECT_HOST_ADDRESS_EXCEPTION,url+": "+e1.getMessage());
		}

		if (!(statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY)) {
			System.err.println("field.");
			return;
		}
	}

	/**
	 * returns the host address
	 * @throws DMZSyncException 
	 */
	protected String getHostAddress() throws DMZSyncException{

		String hostAddress = getRequest().getSession()
				.getServletContext().getInitParameter("DMZ_HOST_ADDRESS");
		if(hostAddress == null){
			throw new DMZSyncException(DMZSyncExceptionType.HOST_ADDRESS_EXCEPTION,"");
		}
		return hostAddress;
	}

	/**
	 * Request data from the DMZ
	 * @throws DMZSyncException 
	 */
	protected String getDMZDataForPatient(Long standardizedPatientId) throws DMZSyncException {
		String ret = null;

		HttpClient httpClient = new HttpClient();

		String url = getHostAddress() + "/sp_portal/DataImportExport/exportSP?id="	+ standardizedPatientId;
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
			throw new DMZSyncException(DMZSyncExceptionType.HTTP_EXCEPTION,url+": "+e.getMessage());
		} catch (IOException e) {
			throw new DMZSyncException(DMZSyncExceptionType.CONNECT_HOST_ADDRESS_EXCEPTION,url+": "+e.getMessage());
			
		} finally {
			getMethod.releaseConnection();
		}
		return ret;
	}

	/**
	 * Save a patient
	 */
	protected void savePatient(StandardizedPatient patient) {
		
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
	
	
	
	HttpServletRequest getRequest(){
		HttpServletRequest ret = getThreadLocalRequest();
		if (ret == null ){
			ret = GroovyDMZSyncServiceImpl.request;
		}
		return ret;
	}
	

	
	

}
