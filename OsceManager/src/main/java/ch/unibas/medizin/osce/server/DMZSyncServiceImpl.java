package ch.unibas.medizin.osce.server;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Enumerated;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;

import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.DMZSyncException;
import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.DMZSyncService;
import ch.unibas.medizin.osce.domain.AnamnesisCheck;
import ch.unibas.medizin.osce.domain.AnamnesisChecksValue;
import ch.unibas.medizin.osce.domain.AnamnesisForm;
import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.domain.Bankaccount;
import ch.unibas.medizin.osce.domain.Description;
import ch.unibas.medizin.osce.domain.Nationality;
import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.domain.OsceDay;
import ch.unibas.medizin.osce.domain.OscePostBlueprint;
import ch.unibas.medizin.osce.domain.OsceSequence;
import ch.unibas.medizin.osce.domain.PatientInRole;
import ch.unibas.medizin.osce.domain.PatientInSemester;
import ch.unibas.medizin.osce.domain.Profession;
import ch.unibas.medizin.osce.domain.Semester;
import ch.unibas.medizin.osce.domain.StandardizedPatient;
import ch.unibas.medizin.osce.domain.StudentOsces;
import ch.unibas.medizin.osce.domain.Task;
import ch.unibas.medizin.osce.domain.Training;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;
import ch.unibas.medizin.osce.shared.DMZSyncExceptionType;
import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.MaritalStatus;
import ch.unibas.medizin.osce.shared.OSCESecurityStatus;
import ch.unibas.medizin.osce.shared.OsceSecurityType;
import ch.unibas.medizin.osce.shared.OsceStatus;
import ch.unibas.medizin.osce.shared.PatientAveragePerPost;
import ch.unibas.medizin.osce.shared.StudyYears;
import ch.unibas.medizin.osce.shared.TraitTypes;
import ch.unibas.medizin.osce.shared.WorkPermission;
import ch.unibas.medizin.osce.shared.StandardizedPatientStatus;



import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import flexjson.transformer.DateTransformer;
import ch.unibas.medizin.osce.shared.Semesters;


public class DMZSyncServiceImpl extends RemoteServiceServlet implements
		DMZSyncService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger Log = Logger.getLogger(DMZSyncServiceImpl.class);
	
	@Override
	public List<String> pushToDMZ(Long standardizedPatientId,String locale) throws DMZSyncException {
			StandardizedPatient patient = findPatient(standardizedPatientId);
			AnamnesisChecksValue.fillAnamnesisChecksValues(patient.getAnamnesisForm().getId());
			String json = "";
			String url = "";
			List<String> errorMessages = null;
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
				
					json = processSendJson(json,locale);
				}catch(Exception e){
					throw new DMZSyncException(DMZSyncExceptionType.SERIALIZING_EXCEPTION,e.getMessage());
				}
				url = getHostAddress() + "/DataImportExport/importSP";	
				String returnJson = sendData(json,url);
				errorMessages = getSendReturnErrorMessage(returnJson);
			}else{
				throw new DMZSyncException(DMZSyncExceptionType.PATIENT_EXIST_EXCEPTION,"");
			}
			
			return errorMessages;
	}
	
	/**
	 * process the send json data
	 */
	private String processSendJson(String json,String locale)
	{
		StringBuilder jsonStr = new StringBuilder();
		jsonStr.append("{");
		jsonStr.append("\"StandardizedPatient\":");
		jsonStr.append(json);
		jsonStr.append(",");
		jsonStr.append("\"languages\":{\"language\": \""+locale+"\"}");
		jsonStr.append("}");
		return jsonStr.toString();
	}
	
	/**
	 * get the error message which is send error field json data to DMZ
	 */
	protected List<String> getSendReturnErrorMessage(String json){
		List<String> returnStr =new ArrayList<String>();
		
		try{			
			JSONObject myjson = new JSONObject(json);
			if(myjson!=null && myjson.has("errors")){			
				
				JSONArray jsonArray = myjson.getJSONArray("errors");			
				for(int i =0;i<jsonArray.length();i++){
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					
					if(jsonObject.get("error")!=JSONObject.NULL){
					
						returnStr.add(jsonObject.get("error").toString());
					}
					
				}
			
			}
		}catch(JSONException e){
			Log.error("Error get return error Message: "+e.getMessage());
		}
				
		return returnStr;
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
				.use("status",new SPStatusTransformer())
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
	
	
    /**
     *Synchronise OsceDay,Training and PatientInSemester
     */
	@Override
	public String sync(String locale) throws DMZSyncException{
	
		String json = getSyncJsonData(locale);
		String url = getHostAddress() + "/OsceSync/syncJson";
		//Send OSCE data to DMZ and return DMZ data
		String returnJson = sendData(json,url);
		//System.out.println(">>>>>>>> return json: "+returnJson);
		String message = "";
		if(!json.equals("")){	
			try{
				JSONObject myjson = new JSONObject(returnJson);
				syncOsceDayAndTraining(myjson);
				message = getReturnMessage(myjson);
			}catch(DMZSyncException e){
				throw e;
			}catch(JSONException e){
				
				Log.error(e.getMessage());
				e.printStackTrace();
				
				throw new DMZSyncException(DMZSyncExceptionType.HTTP_EXCEPTION,e.getMessage());
				
			}catch(Exception e){
				
				Log.error(e.getMessage());
				e.printStackTrace();
				
				throw new DMZSyncException(DMZSyncExceptionType.HTTP_EXCEPTION,e.getMessage());
				
			}
			
		}
		return message;
	}
	
	/**
	 * get the return message	
	 */
	private String getReturnMessage(JSONObject myjson)throws JSONException{
		StringBuffer message = new StringBuffer();		
			if(myjson!=null){			
			
				JSONArray jsonArray = myjson.getJSONArray("message");			
				for(int i =0;i<jsonArray.length();i++){
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					
					if(jsonObject.get("key")!=JSONObject.NULL){

							message.append(jsonObject.get("key").toString());

						if(i != jsonArray.length()-1){

							message.append("#&");
						}
					}
					
				}
			
			}
				
		return message.toString();
	
	}
	
	
	
	private void syncOsceDayAndTraining(JSONObject myjson)throws JSONException,DMZSyncException{
			
		if(myjson!=null){
			try{
				syncOsceDay(myjson);
			}catch(Exception e){
				Log.error(e.getMessage());
				throw new DMZSyncException(DMZSyncExceptionType.SYNC_OSCEDAY_EXCEPTION,e.getMessage());
			}
			
			try{
				syncTraining(myjson);
			}catch(Exception e){
				Log.error(e.getMessage());
				throw new DMZSyncException(DMZSyncExceptionType.SYNC_TRAINING_EXCEPTION,e.getMessage());
			}
			
			try{
				syncPatientInSemester(myjson);
			}catch(Exception e){
				Log.error(e.getMessage());
				throw new DMZSyncException(DMZSyncExceptionType.SYNC_PATIENTINSEMESTER_EXCEPTION,e.getMessage());
			}

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
			   try {
	    	       osceDate = convertToDate(jsonObject.get("osceDate").toString());
				} catch (DMZSyncException e){
				   Log.error(e.getMessage());
				}
			}
	    	if(osceDate!=null){
		    	OsceDay osceDay = OsceDay.findOsceDayByOsceDate(osceDate);
		    	System.out.println("############osceDay: "+osceDay);
		    	if(osceDay == null){
		    		osceDay = new OsceDay();
		    	}

				osceDay.setOsceDate(osceDate);
				
				Date timeStart = null;
				if(jsonObject.get("timeStart")!=JSONObject.NULL){
					try {
						timeStart = convertToDate(jsonObject.get("timeStart").toString());
					} catch (DMZSyncException e){
					Log.error(e.getMessage());
					}
				}					
				osceDay.setTimeStart(timeStart);
				
				Date lunchBreakStart = null;
				if(jsonObject.get("lunchBreakStart")!=JSONObject.NULL){
					try {
						lunchBreakStart = convertToDate(jsonObject.get("lunchBreakStart").toString());
					} catch (DMZSyncException e){
					Log.error(e.getMessage());
					}
				}					
				osceDay.setLunchBreakStart(lunchBreakStart);
				
				Date timeEnd = null;
				if(jsonObject.get("timeEnd")!=JSONObject.NULL){
					try {
						timeEnd = convertToDate(jsonObject.get("timeEnd").toString());
					} catch (DMZSyncException e){
					Log.error(e.getMessage());
					}
				}					
				osceDay.setTimeEnd(timeEnd);
				
				if(jsonObject.get("value")!=JSONObject.NULL){
					osceDay.setValue(jsonObject.getInt("value"));
				}
				
				if(jsonObject.get("lunchBreakAfterRotation")!=JSONObject.NULL){
					osceDay.setLunchBreakAfterRotation(jsonObject.getInt("lunchBreakAfterRotation"));
				
				}
				
				System.out.println(">>>>>jsonObject.get(\"osce\"): "+jsonObject.get("osce"));
				if(jsonObject.get("osce")!=JSONObject.NULL){
					Osce osce = Osce.findOsce(jsonObject.getLong("osce"));
					osceDay.setOsce(osce);
				}
				
				osceDay.merge();
		    	
	    	}
	    	
	    }
	}
	
	/**
	 * sync the class of PatientInSemester
	 * */
	private void syncPatientInSemester(JSONObject myjson) throws JSONException,DMZSyncException{
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
				
				Semester inSemester=null;
				if(jsonObject.get("semester") != JSONObject.NULL){
					inSemester = Semester.findSemester(jsonObject.getLong("semester"));	
				}
				
				if(semester == null){
					semester = new PatientInSemester();	
				}
				
				semester.setStandardizedPatient(patient);
				semester.setAccepted(accepted);
				setOsceDays(jsonObject,semester);
				setTrainings(jsonObject,semester);	
									 
				if(inSemester != null){
					semester.setSemester(inSemester);	
				}
				semester.merge();
				semester.flush();
				
				//if(semester!=null){
				//	semester.setStandardizedPatient(patient);
				//	semester.setAccepted(accepted);
				//	setOsceDays(jsonObject,semester);
				//	setTrainings(jsonObject,semester);	
				//						 
				//	if(inSemester != null){
				//		semester.setSemester(inSemester);	
				//	}
				//	semester.merge();
				//	semester.flush();
				// 
				//}else {
				//	semester = new PatientInSemester();	
				//	semester.setStandardizedPatient(patient);
				//	semester.setAccepted(accepted);
				//	setOsceDays(jsonObject,semester);
				//	setTrainings(jsonObject,semester);
				//	
				//	if(inSemester != null){
				//		semester.setSemester(inSemester);	
				//	}
				//	
				//	semester.merge();
				//	semester.flush();
				//}
		 
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
				 try{
				 osceDay = OsceDay.findOsceDayByOsceDate(convertToDate(osceDate));		
				 } catch (DMZSyncException e){
				   Log.error(e.getMessage());
				 }
			}			
			
			 if(osceDay!=null){

				 jsonOsceDays.add(osceDay);
				 
			 }
			 
		 }
		 semester.setOsceDays(jsonOsceDays);
		 
		 
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
				trainingArray.getJSONObject(i).get("timeStart")!=JSONObject.NULL &&
				trainingArray.getJSONObject(i).get("name")!=JSONObject.NULL){
				String trainingDateStr = trainingArray.getJSONObject(i).get("trainingDate").toString();
				String timeStartStr = trainingArray.getJSONObject(i).get("timeStart").toString();
				String name = trainingArray.getJSONObject(i).get("name").toString();
				Date trainingDate = null;
				Date timeStart = null;
				try{
					trainingDate = convertToDate(trainingDateStr);
					timeStart = convertToDate(timeStartStr);
				} catch (DMZSyncException e){
				   Log.error(e.getMessage());
				}
				
				if(timeStart!=null){	
					training = Training.findTrainingByTrainingDateAndTimeStart(trainingDate,timeStart);
				}else if(timeStart==null && name!=null){
					training = Training.findTrainingByTrainingDateAndName(trainingDate,name);
				}
				 
			}
			if(training!=null){		
				jsonTrainings.add(training);
			}
		 }
		 semester.setTrainings(jsonTrainings);

		
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
				try{
	    	     trainingDate = convertToDate(jsonObject.get("trainingDate").toString());
	    	     timeStart = convertToDate(jsonObject.get("timeStart").toString());
				} catch (DMZSyncException e){
				   Log.error(e.getMessage());
				}
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
					try{
						training.setTimeEnd(convertToDate(jsonObject.get("timeEnd").toString()));
					} catch (DMZSyncException e){
						Log.error(e.getMessage());
					}
					
					if(jsonObject.get("semester") != JSONObject.NULL){
						Semester semester = Semester.findSemester(jsonObject.getLong("semester"));
						if(semester != null){
							training.setSemester(semester);	
						}
						
					}
		    		training.merge();
		    	}else {
		    		training.setName(jsonObject.get("name").toString());
					try{
						training.setTimeStart(convertToDate(jsonObject.get("timeStart").toString()));
						training.setTimeEnd(convertToDate(jsonObject.get("timeEnd").toString()));
					} catch (DMZSyncException e){
						Log.error(e.getMessage());
					}
		    		training.merge();
				}

	    	}	    		    	
	    }
	}
	

	
	/**
	 *get the json data which the sync method needed. 
	 **/
	protected String getSyncJsonData(String locale){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		
				
		List<Semester> semesters = Semester.findAllSemesters();
		sb.append("\"semesters\":[");
		
		int semesterCount = 0;
		for(Semester semester:semesters){
			semesterCount ++;
			sb.append(getSemesterJsonStr(semester));
			
			if(semesterCount != semesters.size()){
				sb.append(",");
			}
		}
		sb.append("],");
		
		List<Osce> osces = Osce.findAllOscesGroupByCopiedOsce();
		sb.append("\"osces\":[");
		int osceCount = 0;
		for(Osce osce : osces){
			osceCount ++;
			sb.append(getOscesJsonStr(osce));
			if(osceCount != osces.size()){
				sb.append(",");
			}
		}
		sb.append("],");

		
		List<OsceDay> osceDays = OsceDay.findAllOsceDays();
		
		sb.append("\"osceDay\":[");
		int i = 0; 
		for(OsceDay osceDay : osceDays){
			i++;
			sb.append(getOsceDayJsonStr(osceDay));
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
			sb.append(getTrainingJsonStr(training));
			if(j != trainings.size()){
				sb.append(",");
			}
		}
		sb.append("],");
		
		sb.append("\"language\":");
		sb.append("\""+locale+"\"");
		sb.append("}");

		return sb.toString();
	}
	


	
	/***
	 * get the json data of osceDay
	 * @param osceDay
	 * @return
	 */
	private String getOsceDayJsonStr(OsceDay osceDay){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		
		sb.append("\"osceDate\":");
		if(osceDay.getOsceDate() != null){
			sb.append("\""+convertToString(osceDay.getOsceDate())+"\"");
		}else{
			sb.append("null");
		}
		sb.append(",");
		sb.append("\"timeStart\":");
		if(osceDay.getTimeStart() != null){
			sb.append("\""+convertToString(osceDay.getTimeStart())+"\"");
		}else{
			sb.append("null");
		}
		sb.append(",");
		
		sb.append("\"timeEnd\":");
		if(osceDay.getTimeEnd() != null){
			sb.append("\""+convertToString(osceDay.getTimeEnd())+"\"");
		}else{
			sb.append("null");
		}
		sb.append(",");
		sb.append("\"lunchBreakStart\":");
		if(osceDay.getLunchBreakStart() != null){
			sb.append("\""+convertToString(osceDay.getLunchBreakStart())+"\"");
		}else{
			sb.append("null");
		}
		sb.append(",");
		sb.append("\"lunchBreakAfterRotation\":"+osceDay.getLunchBreakAfterRotation());
		sb.append(",");
		sb.append("\"osce\":");
		if(osceDay.getOsce() != null){
			sb.append(osceDay.getOsce().getId().toString());
		}else{
			sb.append("null");
		}
		sb.append(",");
		sb.append("\"value\": "+osceDay.getValue());
		
		sb.append("}");
		return sb.toString();
	}
	
	
	/***
	 * get the one json string of semester
	 */
	private String getSemesterJsonStr(Semester semester){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"id\":"+semester.getId());
		sb.append(",");
		sb.append("\"semester\":\""+semester.getSemester()+"\"");
		sb.append(",");
		sb.append("\"calYear\": "+semester.getCalYear());
		sb.append(",");
		sb.append("\"maximalYearEarnings\": "+semester.getMaximalYearEarnings());
		sb.append(",");
		sb.append("\"pricestatist\": "+semester.getPricestatist());
		sb.append(",");
		sb.append("\"priceStandardizedPartient\": "+semester.getPriceStandardizedPartient());
		sb.append(",");
		sb.append("\"preparationRing\": "+semester.getPreparationRing());
//		sb.append(",");
//		sb.append("\"osces\": [");
//		Set<Osce> osces = semester.getOsces();
//		int osceCount = 0;
//		for(Osce osce : osces){
//			osceCount++;
//			sb.append(getOscesJsonStr(osce));
//			if(osceCount != osces.size()){
//				sb.append(",");
//			}
//		}
//		sb.append("]");
		sb.append("}");
		return sb.toString();
	}
	
	
	/***
	 * get the one json string of training
	 * @param training
	 * @return
	 */
	private String getTrainingJsonStr(Training training){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		String name = "";
		if(training.getName() !=null ){
			name = training.getName();
		}
		sb.append("\"name\" : "+"\""+name+"\",");
		sb.append("\"trainingDate\" :");
		if(training.getTrainingDate() != null){
			sb.append("\""+convertToString(training.getTrainingDate())+"\"");
		}else{
			sb.append("null");
		}
		sb.append(",");
		
		sb.append("\"timeStart\":");
		if(training.getTimeStart() != null){
			sb.append("\""+convertToString(training.getTimeStart())+"\"");
		}else{
			sb.append("null");
		}
		sb.append(",");
		
		sb.append("\"timeEnd\":");
		if(training.getTimeEnd() != null){
			sb.append("\""+convertToString(training.getTimeEnd())+"\"");
		}else{
			sb.append("null");
		}
		sb.append(",");
		sb.append("\"semester\":");
		if(training.getSemester() !=null){
			sb.append(training.getSemester().getId().toString());
		}else{
			sb.append("null");
		}
		sb.append("}");
		return sb.toString();
	}
	
	/***
	 * get one json string of osce
	 * @param osce
	 * @return
	 */
	private String getOscesJsonStr(Osce osce){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"id\":"+osce.getId());
		sb.append(",");
		sb.append("\"studyYear\":");
		if(osce.getStudyYear()!=null){
			sb.append("\""+osce.getStudyYear()+"\"");
		}else{
			sb.append("null");
		}
		
		sb.append(",");
		sb.append("\"maxNumberStudents\":"+osce.getMaxNumberStudents());
		sb.append(",");
		sb.append("\"name\":\""+osce.getName()+"\"");
		sb.append(",");
		sb.append("\"shortBreak\":"+osce.getShortBreak());
		sb.append(",");
		sb.append("\"LongBreak\":"+osce.getLongBreak());
		sb.append(",");
		sb.append("\"lunchBreak\":"+osce.getLunchBreak());
		sb.append(",");
		sb.append("\"middleBreak\":"+osce.getMiddleBreak());
		sb.append(",");
		//sb.append("\"numberPosts\":"+osce.getNumberPosts());
		//sb.append(",");
		sb.append("\"numberCourses\":"+osce.getNumberCourses());
		sb.append(",");
		sb.append("\"postLength\":"+osce.getPostLength());
		sb.append(",");
		sb.append("\"isRepeOsce\":"+osce.getIsRepeOsce());
		sb.append(",");
		sb.append("\"numberRooms\":"+osce.getNumberRooms());
		sb.append(",");
		sb.append("\"isValid\":"+osce.getIsValid());
		sb.append(",");
		sb.append("\"osceStatus\":");
		if(osce.getOsceStatus()!=null){
			sb.append("\""+osce.getOsceStatus()+"\"");
		}else{
			sb.append("null");
		}
		sb.append(",");
		sb.append("\"security\":");
		if(osce.getSecurity()!=null){
			sb.append("\""+osce.getSecurity()+"\"");
		}else{
			sb.append("null");
		}
		sb.append(",");
		sb.append("\"osceSecurityTypes\":");
		if(osce.getOsceSecurityTypes()!=null){
			sb.append("\""+osce.getOsceSecurityTypes()+"\"");
		}else{
			sb.append("null");
		}
		sb.append(",");
		sb.append("\"patientAveragePerPost\":");
		if(osce.getPatientAveragePerPost()!=null){
			sb.append("\""+osce.getPatientAveragePerPost()+"\"");
		}else{
			sb.append("null");
		}
		sb.append(",");
		sb.append("\"semester\":"+osce.getSemester().getId());
		sb.append(",");
		sb.append("\"shortBreakSimpatChange\":"+osce.getShortBreakSimpatChange());
		sb.append(",");
		
		sb.append("\"copiedOsce\":");
		if(osce.getCopiedOsce() !=null){
			sb.append(osce.getCopiedOsce().getId());
		}else{
			sb.append("null");
		}
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
	private Date convertToDate(String dateStr)throws DMZSyncException{
		DateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

		Date date=null;
		try {
			if(dateStr!=null && !dateStr.equals("")){
				date = sdf.parse(dateStr);
			}else {
				return null;
			}
		} catch (ParseException e) {
			Log.error("Date format in JSON string incorrect. Date string was " + dateStr ,e);
				
			throw new DMZSyncException(DMZSyncExceptionType.SERIALIZING_EXCEPTION,e.getMessage());
			
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
	
	private class SPStatusTransformer implements ObjectFactory {

		public Object instantiate(ObjectBinder context, Object value,
				Type targetType, Class targetClass) {
			Integer valueI = (Integer) value;
			if (valueI == 0) {
				return StandardizedPatientStatus.INACTIVE;
			} else if(valueI == 1){
				return StandardizedPatientStatus.ACTIVE;
			} else if(valueI == 2){
				return StandardizedPatientStatus.EXPORTED;
			} else if(valueI == 3){
				return StandardizedPatientStatus.ANONYMIZED;
			} else {
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
	    
		PostMethod postMethod = new PostMethod(url);

		NameValuePair[] registerInform = new NameValuePair[1];
		registerInform[0] = new NameValuePair("data", json);

		postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		postMethod.setRequestBody(registerInform);

		int statusCode = 0;
		try {
			statusCode = httpClient.executeMethod(postMethod);
			
				ret = postMethod.getResponseBodyAsString();
				
				
		} catch (HttpException e) {
			throw new DMZSyncException(DMZSyncExceptionType.HTTP_EXCEPTION,url+": "+e.getMessage());
		} catch (IOException e1) {
			throw new DMZSyncException(DMZSyncExceptionType.CONNECT_HOST_ADDRESS_EXCEPTION,url+": "+e1.getMessage());
		}
		if(ret.equals("Data Error")){
			throw new DMZSyncException(DMZSyncExceptionType.RUN_TIME_EXCEPTION,": "+ret);
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
		String url = getHostAddress() + "/DataImportExport/exportSP?id="	+ standardizedPatientId;
		GetMethod getMethod = new GetMethod(url);

		// TODO: does this make sense?
		getMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		try {
			
			int statusCode = httpClient.executeMethod(getMethod);
			

			byte[] responseBody = getMethod.getResponseBody();

			ret = new String(responseBody);
			if (statusCode != HttpStatus.SC_OK) {
				Log.error("Method failed: " + getMethod.getStatusLine());
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
