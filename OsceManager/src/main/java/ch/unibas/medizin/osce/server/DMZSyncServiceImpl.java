package ch.unibas.medizin.osce.server;

import java.io.IOException;

import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.DMZSyncException;
import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.DMZSyncService;

import com.google.gwt.core.client.GWT;
import org.json.JSONObject;
import org.json.JSONArray;
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
import ch.unibas.medizin.osce.shared.Locale;
import ch.unibas.medizin.osce.shared.MaritalStatus;
import ch.unibas.medizin.osce.shared.TraitTypes;
import ch.unibas.medizin.osce.shared.WorkPermission;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.List;
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
import java.lang.String;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.google.gwt.i18n.client.LocaleInfo;
import org.json.JSONException;
import ch.unibas.medizin.osce.shared.Locale;
import com.allen_sauer.gwt.log.client.Log;
import java.text.ParseException;


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
			String url = "";
			if(patient !=null){
				try{
					//,"anamnesisForm.anamnesischecksvalues.anamnesischeck"
					JSONSerializer serializer = new JSONSerializer();
					json = serializer
					       .include("*.class","anamnesisForm","anamnesisForm.anamnesischecksvalues"
					    		   ,"anamnesisForm.scars","anamnesisForm.anamnesischecksvalues.values.anamnesisform"
					    		   ,"bankAccount.IBAN","bankAccount.BIC"
							       ,"description","profession","nationality","langskills")
						   .transform(new DateTransformer("yyyy-MM-dd'T'HH:mm:ss'Z'"), "birthday")
						   .transform(new DateTransformer("yyyy-MM-dd'T'HH:mm:ss'Z'"), "anamnesisForm.createDate")
						   .serialize(patient);
					
				}catch(Exception e){
					throw new DMZSyncException(DMZSyncExceptionType.SERIALIZING_EXCEPTION,e.getMessage());
				}
				url = getHostAddress() + "/sp_portal/DataImportExport/importSP";	
				sendData(json,url);
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
						new DateTransformer("yyyy-MM-dd"))
				.use("gender", new GenderTransformer())
				.use("maritalStatus", new MaritalStatusTransformer())
				.use("workPermission", new WorkPermissionTransformer())
				.use("anamnesisForm.anamnesischecksvalues.values.anamnesischeck.title.type", new AnamnesisChecksTypeTransformet())
				.use("anamnesisForm.scars.values.traitType", new TraitTypeTransformet())
				.use("anamnesisForm.anamnesischecksvalues.values.anamnesischeck.type", new AnamnesisChecksTypeTransformet());
		
		String unExpectedData=standardizedPatientId+"not found";
		StandardizedPatient newPatient = null;
		if(!data.equals(unExpectedData)){
			try{
				newPatient = (StandardizedPatient) (deserializer
						.use(null, StandardizedPatient.class).deserialize(data));
			}catch(flexjson.JSONException e){
				Log.error("Error deserialize json data: "+e.getMessage());
				throw new DMZSyncException(DMZSyncExceptionType.SYNC_PATIENT_EXCEPTION,"Error json data.");
			}
		}else{
			throw new DMZSyncException(DMZSyncExceptionType.PATIENT_EXIST_EXCEPTION,"");
		}

		
		
		StandardizedPatient patient = StandardizedPatient.findStandardizedPatient(standardizedPatientId);
	
		if( patient != null){
			if(newPatient!=null){
				updatePatient(patient,newPatient);
			}
		}else{
//			throw new DMZSyncException(DMZSyncExceptionType.PATIENT_EXIST_EXCEPTION,"");
//			savePatient(newPatient);
		}

	}
	
	@Override
	public String sync(String locale) throws DMZSyncException{
	
		String json = getSyncJsonData(locale);
		String url = getHostAddress() + "/sp_portal/OsceSync/syncJson";
		String returnJson = sendData(json,url);
		String message = "";
		if(!json.equals("")){	
			try{
				JSONObject myjson = new JSONObject(returnJson);
				syncOsceDayAndTraining(myjson);
				message = getReturnMessage(myjson);
			}catch(Exception e){
				Log.error(e.getMessage());
			}
		}

		return message;
	}
	
	/**
	 * get the return message	
	 */
	private String getReturnMessage(JSONObject myjson)throws JSONException{
		String message = "";
					
			if(myjson!=null){			
			
				JSONArray jsonArray = myjson.getJSONArray("message");			
				for(int i =0;i<jsonArray.length();i++){
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					
					if(jsonObject.get("key")!=JSONObject.NULL){
					//	if(jsonObject.get("key")!=JSONObject.NULL){
							message+=jsonObject.get("key").toString();
					//	}
						if(i != jsonArray.length()-1){
							message+="#&";
						}
					}
					
				}
			
			}
				
		return message;
	
	}
	
	
	
	private void syncOsceDayAndTraining(JSONObject myjson)throws JSONException{
			
		if(myjson!=null){
			syncOsceDay(myjson);
			
			syncTraining(myjson);
			
			syncPatientInSemester(myjson);
		}
			
		
		
	}
	
	/**
	 * sync the class of OsceDay
	 * */
	private void syncOsceDay(JSONObject myjson) throws JSONException{
		JSONArray jsonArray = myjson.getJSONArray("osceDay");			
	    for(int i =0;i<jsonArray.length();i++){
	    	JSONObject jsonObject = jsonArray.getJSONObject(i);
			Date osceDate = null;
			if(jsonObject.get("osceDate")!=JSONObject.NULL){
	    	   osceDate = convertToDate(jsonObject.get("osceDate").toString());
			}
	    	if(osceDate!=null){
		    	OsceDay osceDay = OsceDay.findOsceDayByOsceDate(osceDate);
		    	
		    	if(osceDay == null){
		    		osceDay = new OsceDay();
		    		osceDay.setOsceDate(osceDate);
		    		osceDay.merge();
		    	}
	    	}
	    	
	    }
	}
	
	/**
	 * sync the class of PatientInSemester
	 * */
	private void syncPatientInSemester(JSONObject myjson) throws JSONException{
	JSONArray jsonArray = myjson.getJSONArray("patientInSemester");			
    for(int i =0;i<jsonArray.length();i++){
    	JSONObject jsonObject = jsonArray.getJSONObject(i);
		Boolean accepted = null;
		if(jsonObject.get("accepted")!=JSONObject.NULL){
			accepted = jsonObject.getBoolean("accepted");
		}
		Long patientId = null;
		StandardizedPatient patient = null;
		if(jsonObject.get("standarizedPatientId")!=JSONObject.NULL){
	         patientId = jsonObject.getLong("standarizedPatientId");  
			 patient = StandardizedPatient.findStandardizedPatient(patientId);
		}
	    if(patient != null){
			 PatientInSemester semester =PatientInSemester.findPatientInSemesterByStandardizedPatient(patient);
			 
			 if(semester!=null){
				 semester.setStandardizedPatient(patient);
				 semester.setAccepted(accepted);
				 //semester.flush();
				 setOsceDays(jsonObject,semester);
				 setTrainings(jsonObject,semester);						 
				 semester.merge();
				 semester.flush();
				 
			 }else {
				 semester = new PatientInSemester();	
				 semester.setStandardizedPatient(patient);
				 semester.setAccepted(accepted);
				// semester.flush();
				 setOsceDays(jsonObject,semester);
				 setTrainings(jsonObject,semester);
				 semester.merge();
				 semester.flush();
			 }
		 
	    }

    	
    }
}
	
	/**
	 * save accepted osceDays
	 * */
	private void setOsceDays(JSONObject jsonObject,PatientInSemester semester)throws JSONException{
		 JSONArray osceDayArray = jsonObject.getJSONArray("acceptedOsce");
		 Set<OsceDay> jsonOsceDays = new HashSet<OsceDay>();
		 for(int j = 0; j<osceDayArray.length(); j++){
			OsceDay osceDay = null;
			if(osceDayArray.getJSONObject(j).get("osceDate")!=JSONObject.NULL){
				 String osceDate = osceDayArray.getJSONObject(j).get("osceDate").toString();	
				 osceDay = OsceDay.findOsceDayByOsceDate(convertToDate(osceDate));		
			}			
			
			 if(osceDay!=null){
//				 osceDay.getPatientInSemesters().add(semester);
//				 osceDay.persist();
				 jsonOsceDays.add(osceDay);
				 
			 }
			 
		 }
		 semester.setOsceDays(jsonOsceDays);
		// semester.persist();
//		 for(OsceDay osceDay : semester.getOsceDays()){
//			 if(!jsonOsceDays.contains(osceDay)){
//				 osceDay.getPatientInSemesters().remove(semester);
//				 osceDay.persist();
//			 }
//		 }
		 
		 
	}
	/**
	 * save accepted trainings
	 * */
	private void setTrainings(JSONObject jsonObject,PatientInSemester semester)throws JSONException{
		JSONArray trainingArray = jsonObject.getJSONArray("acceptedTrainings");
		 Set<Training> jsonTrainings = new HashSet<Training>();
		 for(int i = 0; i<trainingArray.length(); i++){
			Training training = null;
			if(trainingArray.getJSONObject(i).get("trainingDate")!=JSONObject.NULL && 
			trainingArray.getJSONObject(i).get("timeStart")!=JSONObject.NULL){
				 String trainingDate = trainingArray.getJSONObject(i).get("trainingDate").toString();
				 String timeStart = trainingArray.getJSONObject(i).get("timeStart").toString();
				 training = Training.findTrainingByTrainingDateAndTimeStart(convertToDate(trainingDate),convertToDate(timeStart));
			 }
			 if(training!=null){		
//				 training.getPatientInSemesters().add(semester);
//				 training.persist();
				 jsonTrainings.add(training);
			 }
		 }
		 semester.setTrainings(jsonTrainings);
	//	 semester.persist();
//		 for(Training training : semester.getTrainings()){
//			 if(!jsonTrainings.contains(training)){
//				 System.out.println("########### not contains");
//				 training.getPatientInSemesters().remove(semester);
//				 training.persist();
//			 }
//		 }
		
	}
	
	/**
	 * sync the class of Training
	 * */
	private void syncTraining(JSONObject myjson) throws JSONException{
		JSONArray jsonArray = myjson.getJSONArray("trainings");			
	    for(int i =0;i<jsonArray.length();i++){
	    	JSONObject jsonObject = jsonArray.getJSONObject(i);
			Date trainingDate = null;
			Date timeStart = null;
			String name = null;
			if(jsonObject.get("trainingDate") != JSONObject.NULL &&
			    jsonObject.get("timeStart") != JSONObject.NULL &&
				jsonObject.get("name") != JSONObject.NULL){
	    	     trainingDate = convertToDate(jsonObject.get("trainingDate").toString());
	    	     timeStart = convertToDate(jsonObject.get("timeStart").toString());
				 name = jsonObject.get("name").toString();
			}
	    	if(trainingDate!=null){
				Training training = null;
				if(timeStart!=null){
					training = Training.findTrainingByTrainingDateAndTimeStart(trainingDate,timeStart);
				}else if(timeStart==null && name!=null){
					training = Training.findTrainingByTrainingDateAndName(trainingDate,name);
				}
		    	if(training == null){
		    		training = new Training();
		    		training.setName(jsonObject.get("name").toString());
		    		training.setTrainingDate(trainingDate);
		    		training.setTimeStart(timeStart);
		    		training.setTimeEnd(convertToDate(jsonObject.get("timeEnd").toString()));
		    		training.merge();
		    	}else {
		    		training.setName(jsonObject.get("name").toString());
					training.setTimeStart(convertToDate(jsonObject.get("timeStart").toString()));
		    		training.setTimeEnd(convertToDate(jsonObject.get("timeEnd").toString()));
		    		training.merge();
				}

	    	}	    		    	
	    }
	}
	
	/**
	 *get the json data which the sync method needed. 
	 **/
	protected String getSyncJsonData(String locale){
		
		List<OsceDay> osceDays = OsceDay.findAllOsceDays();
		StringBuilder sb = new StringBuilder();
		sb.append("{").append("\"osceDay\":[");
		int i = 0; 
		for(OsceDay osceDay : osceDays){
			i++;
			sb.append("{\"osceDate\": \""+convertToString(osceDay.getOsceDate())+"\"}");
			if(i != osceDays.size()){
				sb.append(",");
			}
		}
		sb.append("],");
		
		List<Training> trainings = Training.findAllTrainings();
		sb.append("\"trainings\":[");
		int j = 0; 
		for(Training training : trainings){
			j++;
			sb.append("{");
			String name = "";
			if(training.getName() !=null ){
				name = training.getName();
			}
			sb.append("\"name\" : "+"\""+name+"\",");
			sb.append("\"trainingDate\" : \""+convertToString(training.getTrainingDate())+"\",");
			sb.append("\"timeStart\" : \""+convertToString(training.getTimeStart())+"\",");
			sb.append("\"timeEnd\": \""+convertToString(training.getTimeEnd())+"\"");
			sb.append("}");
			if(j != trainings.size()){
				sb.append(",");
			}
		}
		sb.append("],");
		
		List<StandardizedPatient> standardizedPatients = StandardizedPatient.findAllStandardizedPatients();
		sb.append("\"standardizedPatient\":[");
		int l = 0; 
		for(StandardizedPatient patient : standardizedPatients){
			l++;
			sb.append("{");
			sb.append("\"id\": "+patient.getId()+",");
			sb.append("\"preName\": "+"\""+patient.getPreName()+"\",");
			sb.append("\"name\": "+"\""+patient.getName()+"\"");
			sb.append("}");
			if(l != standardizedPatients.size()){
				sb.append(",");
			}
		}
		sb.append("],");
		sb.append("\"language\":");
		sb.append("\""+locale+"\"");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Time is converted to a string
	 **/
	private String convertToString(Date date){
		DateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

		String dateStr="";
		try {
			if(date !=null){
				dateStr = sdf.format(date);
			}
		} catch (Exception e) {
			Log.error(e.getMessage());
		}
		return dateStr;
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
		} catch (ParseException e) {
			Log.error("Date format in JSON string incorrect. Date string was " + dateStr ,e.getMessage());
			
			return null;
		}
		return date;
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
						nationality.setNationality(newPatient.getNationality().getNationality());
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
						profession.setProfession(newPatient.getProfession().getProfession()); 
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
						descriptions.setDescription(newPatient.getDescriptions().getDescription());
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
						bankAccount.setBankName(newPatient.getBankAccount().getBankName());
						bankAccount.setIBAN(newPatient.getBankAccount().getIBAN());
						bankAccount.setBIC(newPatient.getBankAccount().getBIC());
						bankAccount.setOwnerName(newPatient.getBankAccount().getOwnerName());
						bankAccount.setPostalCode(newPatient.getBankAccount().getPostalCode());
						bankAccount.setCity(newPatient.getBankAccount().getCity());
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
			
	
			//patient.flush();
			patient.merge();
			patient.flush();
		
		} catch (Exception e){
			if(dmzSyncExceptionType==null &&dmzSyncExceptionType.equals("")){
				setDMZSyncExceptionTypeAndErrorMsg(DMZSyncExceptionType.PATIENT_EXIST_EXCEPTION,e.getMessage());
			}
			
			
			throw new DMZSyncException(dmzSyncExceptionType,errorMsg,e);
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
		data = data.replaceAll("\"iban\"","\"IBAN\"");
		data = data.replaceAll("\"bic\"","\"BIC\"");
//		data = data.replaceAll("\"description\":{","\"descriptions\":{");

		return data;

	}
	

	protected StandardizedPatient findPatient(Long objectId) {
		return StandardizedPatient.findStandardizedPatient(objectId);
	}

	private class GenderTransformer implements ObjectFactory {

		public Object instantiate(ObjectBinder context, Object value,
				Type targetType, Class targetClass) {
			Integer valueI = (Integer) value;
			if (valueI == 0) {
				return Gender.MALE;
			} else{
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
				return AnamnesisCheckTypes.QUESTION_OPEN;
			} else if(valueI == 1){
				return AnamnesisCheckTypes.QUESTION_YES_NO;
			} else if(valueI == 2){
				return AnamnesisCheckTypes.QUESTION_MULT_S;
			} else if(valueI == 3){
				return AnamnesisCheckTypes.QUESTION_MULT_M;
			} else {
				return null;
			}			
		}
	}
	
	private class TraitTypeTransformet implements ObjectFactory {
		public Object instantiate(ObjectBinder context, Object value,
				Type targetType, Class targetClass) {
			Integer valueI = (Integer) value;
			if (valueI == 0) {
				return TraitTypes.SCAR;
			} else if(valueI == 1){
				return TraitTypes.TATTOO;
			} else if(valueI == 2){
				return TraitTypes.NOT_TO_EXAMINE;
			} else {
				return null;
			}
			
		}
	}

	

	/**
	 * Send data to the DMZ server
	 * @throws DMZSyncException 
	 */
	protected String sendData(String json,String url) throws DMZSyncException {

		HttpClient httpClient = new HttpClient();
		String ret = "";
		//String hostAddress = getHostAddress();

	    //String url = hostAddress + "/sp_portal/DataImportExport/importSP";
	
	    
		PostMethod postMethod = new PostMethod(url);

		NameValuePair[] registerInform = new NameValuePair[1];
		registerInform[0] = new NameValuePair("data", json);

		postMethod.setRequestBody(registerInform);

		int statusCode = 0;
		try {
			statusCode = httpClient.executeMethod(postMethod);
			//if(json.contains("osceDay")){
				byte[] responseBody = postMethod.getResponseBody();
			    ret = new String(responseBody);
			//}
		} catch (HttpException e) {
			throw new DMZSyncException(DMZSyncExceptionType.HTTP_EXCEPTION,url+": "+e.getMessage());
		} catch (IOException e1) {
			throw new DMZSyncException(DMZSyncExceptionType.CONNECT_HOST_ADDRESS_EXCEPTION,url+": "+e1.getMessage());
		}

		if (!(statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY)) {
			System.err.println("field.");
		}
		return ret;
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
	//	String url = getHostAddress() + "/dataImportExport/exportSP?id="	+ standardizedPatientId;
		GetMethod getMethod = new GetMethod(url);

		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		try {
			
			int statusCode = httpClient.executeMethod(getMethod);
			

			byte[] responseBody = getMethod.getResponseBody();

			ret = new String(responseBody);
			if (statusCode != HttpStatus.SC_OK) {
				throw new DMZSyncException(DMZSyncExceptionType.HTTP_EXCEPTION,ret);
			}
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
		return ret;
	}
	

	
	

}
