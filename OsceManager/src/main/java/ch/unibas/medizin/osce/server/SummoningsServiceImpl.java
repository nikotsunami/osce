/**
 * 
 */
package ch.unibas.medizin.osce.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import ch.unibas.medizin.osce.client.SummoningsService;
import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.util.email.EmailService;
import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.domain.Doctor;
import ch.unibas.medizin.osce.domain.PatientInRole;
import ch.unibas.medizin.osce.domain.PatientInSemester;
import ch.unibas.medizin.osce.domain.StandardizedPatient;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.itextpdf.text.Document;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author rahul
 *
 */
@SuppressWarnings("serial")
public class SummoningsServiceImpl extends RemoteServiceServlet implements SummoningsService{

	private EmailService emailService;
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String generateSPMailPDF(Long semesterId,List<Long> spIds){
		
		StandardizedPatient patient = null;
		
		final Map templateVariables = new HashMap();
		
		List<String> toNames = null;
		List<String> fromNames = null;
		List<String> assignments = null;
		List<List<String>> assignmentList = null;
		
		SimpleDateFormat dateFormat = null;
		SimpleDateFormat timeFormat = null;
		
		try {
			
			toNames = new ArrayList<String>(0);
			fromNames = new ArrayList<String>(0);
			assignmentList = new ArrayList<List<String>>(0);
			
			dateFormat = new SimpleDateFormat("dd MMM yyyy");
			timeFormat = new SimpleDateFormat("hh:mm");
			
			for(Long id : spIds){
				
				patient = StandardizedPatient.findStandardizedPatient(id);
				Log.info("PatientInSemester : "+patient.getPatientInSemester());
				
				for(PatientInSemester patientInSemester : patient.getPatientInSemester()){
					
					Log.info("PatientInRole : "+patientInSemester.getPatientInRole());
					
					for(PatientInRole patientInRole : patientInSemester.getPatientInRole()){
						
						Log.info("Assignments : "+patientInRole.getAssignments());
						
						assignments = new ArrayList<String>(0);
						
						assignments.add( "Date"
								+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ "Start Time"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;"
								+ "End Time"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;"
								+ "Role<br/>");
						
						for(Assignment assignment : patientInRole.getAssignments()){
							
							Log.info("Assignment : "+assignment);
							
							assignments.add(dateFormat.format(assignment.getOsceDay().getOsceDate())
									+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
									+timeFormat.format(assignment.getOsceDay().getTimeStart())
									+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
									+timeFormat.format(assignment.getOsceDay().getTimeEnd())
									+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
									+assignment.getPatientInRole().getOscePost().getStandardizedRole().getShortName());
						}
					}
				}
				
				toNames.add(patient.getName()+" "+patient.getPreName());
				fromNames.add(OsMaConstant.FROM_NAME);
				
				assignmentList.add(assignments);
				assignments = null;
			}
			
			templateVariables.put("toNames",toNames);
			templateVariables.put("fromNames",fromNames);
			templateVariables.put("assignments",assignmentList);
			
			return this.generateMailPDFUsingTemplate("mail\\mailTemplate_SP"+semesterId.toString()+".txt", templateVariables);
			
		} catch (Exception e) {
			Log.error("ERROR : "+e.getMessage());
			e.printStackTrace();
			return null;
		}finally{
			patient = null;
			
			toNames = null;
			fromNames = null;
			assignments = null;
			assignmentList = null;
			
			dateFormat = null;
			timeFormat = null;
		}
		
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String generateExaminerMailPDF(Long semesterId,List<Long> examinerIds){
		
		Doctor examiner = null;
		
		final Map templateVariables = new HashMap();
		
		List<String> toNames = null;
		List<String> fromNames = null;
		List<String> assignments = null;
		List<List<String>> assignmentList = null;
		
		DateFormat dateFormat = null;
		DateFormat timeFormat = null;
		
		try {
			
			toNames = new ArrayList<String>(0);
			fromNames = new ArrayList<String>(0);
			assignmentList = new ArrayList<List<String>>(0);
			
			dateFormat = new SimpleDateFormat("dd MMM yyyy");
			timeFormat = new SimpleDateFormat("hh:mm");
			
			for(Long id : examinerIds){
				
				examiner = Doctor.findDoctor(id);
				Log.info("Assignments : "+examiner.getAssignments());
				
				assignments = new ArrayList<String>(0);
				
				assignments.add( "Date"
						+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
						+ "Start Time"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;"
						+ "End Time"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;"
						+ "Role"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
						+ "Room Number<br/>");
				
				for(Assignment assignment : examiner.getAssignments()){			
					Log.info("Assignment : "+assignment);
					
					assignments.add(dateFormat.format(assignment.getOsceDay().getOsceDate())
							+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
							+timeFormat.format(assignment.getOsceDay().getTimeStart())
							+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
							+timeFormat.format(assignment.getOsceDay().getTimeEnd())
							+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
							+assignment.getPatientInRole().getOscePost().getStandardizedRole().getShortName()
							+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
							+assignment.getOscePostRoom().getRoom().getRoomNumber());
				}
				
				toNames.add(examiner.getName()+" "+examiner.getPreName());
				fromNames.add(OsMaConstant.FROM_NAME);
				
				assignmentList.add(assignments);
				assignments = null;
			}
			
			templateVariables.put("toNames",toNames);
			templateVariables.put("fromNames",fromNames);
			templateVariables.put("assignments",assignmentList);
			
			return this.generateMailPDFUsingTemplate("mail\\mailTemplate_Ex"+semesterId.toString()+".txt", templateVariables);
			
		} catch (Exception e) {
			Log.error("ERROR : "+e.getMessage());
			e.printStackTrace();
			return null;
		}finally{
			examiner = null;
			
			toNames = null;
			fromNames = null;
			assignments = null;
			assignmentList = null;
			
			dateFormat = null;
			timeFormat = null;
			
		}
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String generateMailPDFUsingTemplate(String templateName,Map templateVariables){
		
		Document document = null;
		File file = null;
		PdfWriter writer =null;
		HTMLWorker htmlWorker = null;
		
		String fileContents = null;
		String mailMessage = null;
		String assignmentString = null;
		List<String> toNames = null;
		List<List<String>> assignments = null;
		List<String> fromNames = null;
		
		int index = 0;
		try {
			
			toNames = (List<String>) templateVariables.get("toNames");
			fromNames = (List<String>) templateVariables.get("fromNames");
			assignments = (List<List<String>>) templateVariables.get("assignments");
			
			assignmentString = "";
			document = new Document();
			file = new File("invitation.pdf");
			writer = PdfWriter.getInstance(document, new FileOutputStream(file));
			
			document.open();
			
			htmlWorker = new HTMLWorker(document);
			
			fileContents = this.getTemplateContent(templateName)[1];
			
			for(index=0; index < toNames.size(); index++){
				
				mailMessage = fileContents;
				
				mailMessage = mailMessage.replace("[toName]", toNames.get(index));
				
				assignmentString = "";
				
				for(String assignment : assignments.get(index)){
					assignmentString += assignment +"<br/>";
				}
				
				mailMessage = mailMessage.replace("[assignment]", assignmentString);
				mailMessage = mailMessage.replace("[fromName]", fromNames.get(index));
				
				htmlWorker.parse(new StringReader(mailMessage));
				document.newPage();
				
			}
			
			document.close();
			
			return "invitation.pdf";

		} catch (Exception e) {
			Log.error("in SummoningsServiceImpl.generateMailPDFUsingTemplate: "
					+ e.getMessage());
			e.printStackTrace();
			return null;
		}finally{
			document = null;
			file = null;
			writer =null;
			htmlWorker = null;
			fileContents = null;
		}
		
		
	}
	
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Boolean sendSPMail(Long semesterId,List<Long> spIds){
		
		StandardizedPatient patient = null;
		
		final Map templateVariables = new HashMap();
		
		List<String> toNames = null;
		List<String> fromNames = null;
		List<String> assignments = null;
		List<List<String>> assignmentList = null;
		
		List<String> toMailIds = null;
		String fromMailId = null;
		String subject = null;
		
		DateFormat dateFormat;
		DateFormat timeFormat;
		
		try {
			
			toNames = new ArrayList<String>(0);
			fromNames = new ArrayList<String>(0);
			assignmentList = new ArrayList<List<String>>(0);
			
			dateFormat = new SimpleDateFormat("dd MMM yyyy");
			timeFormat = new SimpleDateFormat("hh:mm");
			
			toMailIds = new ArrayList<String>(0);
			fromMailId = OsMaConstant.FROM_MAIL_ID;
			subject = OsMaConstant.MAIL_SUBJECT;
			
			for(Long id : spIds){
				
				patient = StandardizedPatient.findStandardizedPatient(id);
				Log.info("PatientInSemester : "+patient.getPatientInSemester());
				
				for(PatientInSemester patientInSemester : patient.getPatientInSemester()){
					
					Log.info("PatientInRole : "+patientInSemester.getPatientInRole());
					
					for(PatientInRole patientInRole : patientInSemester.getPatientInRole()){
						
						Log.info("Assignments : "+patientInRole.getAssignments());
						
						assignments = new ArrayList<String>(0);
						
						assignments.add( "Date"
								+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ "Start Time"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;"
								+ "End Time"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;"
								+ "Role<br/>");
						
						for(Assignment assignment : patientInRole.getAssignments()){
							
							Log.info("Assignment : "+assignment);
							
							assignments.add(dateFormat.format(assignment.getOsceDay().getOsceDate())
									+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
									+timeFormat.format(assignment.getOsceDay().getTimeStart())
									+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
									+timeFormat.format(assignment.getOsceDay().getTimeEnd())
									+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
									+assignment.getPatientInRole().getOscePost().getStandardizedRole().getShortName());
							
							
						}
					}
				}
				
				toNames.add(patient.getName()+" "+patient.getPreName());
				fromNames.add(OsMaConstant.FROM_NAME);
				
				assignmentList.add(assignments);
				toMailIds.add(patient.getEmail());
				
				assignments = null;
			}
			
			templateVariables.put("toNames",toNames);
			templateVariables.put("fromNames",fromNames);
			templateVariables.put("assignments",assignmentList);
			templateVariables.put("toMailIds",toMailIds);
			templateVariables.put("fromMailId",fromMailId);
			templateVariables.put("subject",subject);
			
			return this.sendMailUsingTemplate("email\\emailTemplate_SP"+semesterId.toString()+".txt", templateVariables);
			
		} catch (Exception e) {
			Log.error("ERROR : "+e.getMessage());
			e.printStackTrace();
			return null;
		}finally{
			patient = null;
			
			toNames = null;
			fromNames = null;
			assignments = null;
			assignmentList = null;
			
			toMailIds = null;
			fromMailId = null;
			subject = null;
			
			dateFormat = null;
			timeFormat = null;
		}
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Boolean sendExaminerMail(Long semesterId, List<Long> examinerIds){
		
		Doctor examiner = null;
		
		final Map templateVariables = new HashMap();
		
		List<String> toNames = null;
		List<String> fromNames = null;
		List<String> assignments = null;
		List<List<String>> assignmentList = null;
		
		DateFormat dateFormat;
		DateFormat timeFormat;
		
		List<String> toMailIds = null;
		String fromMailId = null;
		String subject = null;
		
		try {
			
			toNames = new ArrayList<String>(0);
			fromNames = new ArrayList<String>(0);
			assignmentList = new ArrayList<List<String>>(0);
			
			dateFormat = new SimpleDateFormat("dd MMM yyyy");
			timeFormat = new SimpleDateFormat("hh:mm");
			
			toMailIds = new ArrayList<String>(0);
			fromMailId = OsMaConstant.FROM_MAIL_ID;
			subject = OsMaConstant.MAIL_SUBJECT;
			
			for(Long id : examinerIds){
				
				examiner = Doctor.findDoctor(id);
				
				Log.info("Assignments : "+examiner.getAssignments());
						
				assignments = new ArrayList<String>(0);
				
				assignments.add( "Date"
						+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
						+ "Start Time"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;"
						+ "End Time"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;"
						+ "Role"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
						+ "Room Number<br/>");
				
				for(Assignment assignment : examiner.getAssignments()){
							
					Log.info("Assignment : "+assignment);
					
					assignments.add(dateFormat.format(assignment.getOsceDay().getOsceDate())
							+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
							+timeFormat.format(assignment.getOsceDay().getTimeStart())
							+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
							+timeFormat.format(assignment.getOsceDay().getTimeEnd())
							+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
							+assignment.getPatientInRole().getOscePost().getStandardizedRole().getShortName()
							+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
							+assignment.getOscePostRoom().getRoom().getRoomNumber());
							
				}
			
				
				toNames.add(examiner.getName()+" "+examiner.getPreName());
				fromNames.add(OsMaConstant.FROM_NAME);
				
				assignmentList.add(assignments);
				toMailIds.add(examiner.getEmail());
				
				assignments = null;
			}
			
			templateVariables.put("toNames",toNames);
			templateVariables.put("fromNames",fromNames);
			templateVariables.put("assignments",assignmentList);
			templateVariables.put("toMailIds",toMailIds);
			templateVariables.put("fromMailId",fromMailId);
			templateVariables.put("subject",subject);
			
			return this.sendMailUsingTemplate("email\\emailTemplate_Ex"+semesterId.toString()+".txt", templateVariables);
			
		} catch (Exception e) {
			Log.error("ERROR : "+e.getMessage());
			e.printStackTrace();
			return null;
		}finally{
			examiner = null;
			
			toNames = null;
			fromNames = null;
			assignments = null;
			assignmentList = null;
			
			dateFormat = null;
			timeFormat = null;
			
			toMailIds = null;
			fromMailId = null;
			subject = null;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Boolean sendMailUsingTemplate(String templateName,Map templateVariables){
		
		String fileContents = null;
		String mailMessage = null;
		List<String> toNames = null;
		List<String> fromNames = null;
		
		List<List<String>> assignments = null;
		String assignmentString = null;
		
		List<String> toMailIds = null;
		String fromMailId = null;
		String subject = null;
		
		int index = 0;
		try {
			toNames = (List<String>) templateVariables.get("toNames");
			toMailIds = (List<String>) templateVariables.get("toMailIds");
			fromNames = (List<String>) templateVariables.get("fromNames");
			
			fromMailId = (String) templateVariables.get("fromMailId");
			subject = (String) templateVariables.get("subject");
			
			assignments = (List<List<String>>) templateVariables.get("assignments");
			
			fileContents = this.getTemplateContent(templateName)[1];
			
			for(index=0; index < toNames.size(); index++){
				
				mailMessage = fileContents;
				
				mailMessage = mailMessage.replace("[toName]", toNames.get(index));
				
				assignmentString = "";
				
				for(String assignment : assignments.get(index)){
					assignmentString += assignment +"<br/>";
				}
				
				mailMessage = mailMessage.replace("[assignment]", assignmentString);
				mailMessage = mailMessage.replace("[fromName]", fromNames.get(index));
				
				Log.info("emailService = " + emailService);
				
				emailService.sendMail(new String[]{toMailIds.get(index)}, fromMailId, subject, mailMessage);
				
			}
			
			return true;
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
			return false;
		}finally{
			fileContents = null;
			mailMessage = null;
			toNames = null;
			fromNames = null;
			assignments = null;
			
			toMailIds = null;
			fromMailId = null;
			subject = null;
		}
	}
	
	@Override
	public String[] getTemplateContent(String templateName){
		
		File file = null;
		try {
			
			file = new File(OsMaConstant.DEFAULT_MAIL_TEMPLATE_PATH + templateName);
			
			if(file.isFile()){
				
				return new String[]{templateName,FileUtils.readFileToString(file)};
			}else{
				
				file = new File(OsMaConstant.DEFAULT_MAIL_TEMPLATE_PATH + OsMaConstant.DEFAULT_MAIL_TEMPLATE);
				
				if(file.isFile())
					return new String[]{OsMaConstant.DEFAULT_MAIL_TEMPLATE,FileUtils.readFileToString(file)};
				else
					return new String[]{"",""};
			}
				
			
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}finally{
			file = null;
		}
		
	}
	
	@Override
	public Boolean saveTemplate(String templateName,String templateContent){
		
		File file = null;
		try {
			
			file = new File(OsMaConstant.DEFAULT_MAIL_TEMPLATE_PATH + templateName);
			
			if(file.isFile())
				FileUtils.deleteQuietly(file);
			else
				Log.error("Template file does not exist. New File will be created.");
			
			FileUtils.touch(file);
			FileUtils.writeStringToFile(file, templateContent);
			return true;
			
			
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}finally{
			file = null;
		}
		
	}
	
	@Override
	public Boolean deleteTemplate(String templateName){
		
		File file = null;
		try {
			
			file = new File(OsMaConstant.DEFAULT_MAIL_TEMPLATE_PATH + templateName);
			
			if(file.isFile()){
				FileUtils.deleteQuietly(file);
				return true;
			}else{
				Log.error("Template file does not exist.");
				return false;
			}
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}finally{
			file = null;
		}
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}
	
}
