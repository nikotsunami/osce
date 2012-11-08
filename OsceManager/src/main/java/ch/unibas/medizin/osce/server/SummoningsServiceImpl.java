/**
 * 
 */
package ch.unibas.medizin.osce.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ch.unibas.medizin.osce.client.SummoningsService;
import ch.unibas.medizin.osce.client.util.email.EmailService;
import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.domain.Doctor;
import ch.unibas.medizin.osce.domain.PatientInRole;
import ch.unibas.medizin.osce.domain.PatientInSemester;
import ch.unibas.medizin.osce.domain.StandardizedPatient;
import ch.unibas.medizin.osce.server.util.email.impl.EmailServiceImpl;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.itextpdf.text.Document;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author rahul
 *
 */
@SuppressWarnings("serial")
public class SummoningsServiceImpl extends RemoteServiceServlet implements SummoningsService{
	
	private static Logger log = Logger.getLogger(SummoningsServiceImpl.class);

	private EmailService emailService;
	
	
	public SummoningsServiceImpl() {
		super();
		this.emailService = new EmailServiceImpl();
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
		((EmailServiceImpl)emailService).setSender(applicationContext.getBean(JavaMailSenderImpl.class));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String generateSPMailPDF(Long semesterId,List<Long> spIds){
		
		StandardizedPatient patient = null;
		PatientInSemester patientInSemester = null;
		
		final Map templateVariables = new HashMap();
		
		List<String> toNames = null;
		List<String> fromNames = null;
		List<String> assignments = null;
		List<List<String>> assignmentList = null;
		List<Assignment> assignments2 = null;
		
		SimpleDateFormat dateFormat = null;
		SimpleDateFormat timeFormat = null;
		
		try {
			
			toNames = new ArrayList<String>(0);
			fromNames = new ArrayList<String>(0);
			assignmentList = new ArrayList<List<String>>(0);
			
			dateFormat = new SimpleDateFormat("dd MMM yyyy");
			timeFormat = new SimpleDateFormat("hh:mm a");
			
			for(Long id : spIds){
				
				patient = StandardizedPatient.findStandardizedPatient(id);
				log.info("StandardizedPatient ID : "+id);
				
				patientInSemester = PatientInSemester.findPisBySemesterSp(semesterId, id);
				
//				for(PatientInSemester patientInSemester : patient.getPatientInSemester()){
					
					assignments = new ArrayList<String>(0);
					
					if(patientInSemester.getPatientInRole() != null && patientInSemester.getPatientInRole().size() >0){
						
						assignments.add( "Date"
								+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ "Start Time"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ "End Time"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ "Role<br/>");
						
						for(PatientInRole patientInRole : patientInSemester.getPatientInRole()){
							
							log.info("PatientInRole  inner : "+patientInRole.getId());
							
							assignments2 = getSortedAssignmentList(patientInRole.getAssignments());
							
							for(Assignment assignment : assignments2){
								
								log.info("Assignment : "+assignment.getId());
								
								assignments.add(dateFormat.format(assignment.getOsceDay().getOsceDate())
										+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
										+timeFormat.format(assignment.getTimeStart())
										+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
										+timeFormat.format(assignment.getTimeEnd())
										+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
										+assignment.getPatientInRole().getOscePost().getStandardizedRole().getShortName());
							}
						}
					}
//				}
				
				toNames.add(patient.getName()+" "+patient.getPreName());
				fromNames.add(OsMaFilePathConstant.FROM_NAME);
				
				assignmentList.add(assignments);
				assignments = null;
			}
			
			templateVariables.put("toNames",toNames);
			templateVariables.put("fromNames",fromNames);
			templateVariables.put("assignments",assignmentList);
			//Feature : 154 
			return this.generateMailPDFUsingTemplate(semesterId.toString(),false, templateVariables);
			
		} catch (Exception e) {
			log.error("ERROR : "+e.getMessage());
			e.printStackTrace();
			return null;
		}finally{
			patient = null;
			
			toNames = null;
			fromNames = null;
			assignments = null;
			assignmentList = null;
			assignments2 = null;
			
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
		List<Assignment> assignments3 = null;
		
		DateFormat dateFormat = null;
		DateFormat timeFormat = null;
		
		try {
			
			toNames = new ArrayList<String>(0);
			fromNames = new ArrayList<String>(0);
			assignmentList = new ArrayList<List<String>>(0);
			
			dateFormat = new SimpleDateFormat("dd MMM yyyy");
			timeFormat = new SimpleDateFormat("hh:mm a");
			
			for(Long id : examinerIds){
				
				examiner = Doctor.findDoctor(id);
				
				assignments = new ArrayList<String>(0);
				
				assignments.add( "Date"
						+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
						+ "Start Time"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
						+ "End Time"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
						+ "Role"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
						+ "Room Number<br/>");
				
				assignments3 = getSortedAssignmentList(examiner.getAssignments());
				
				
				for(Assignment assignment : assignments3 ){			
					log.info("Assignment : "+assignment.getId());
					
					assignments.add(dateFormat.format(assignment.getOsceDay().getOsceDate())
							+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
							+timeFormat.format(assignment.getTimeStart())
							+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
							+timeFormat.format(assignment.getTimeEnd())
							+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
//Feature : 154 
							+((assignment.getOscePostRoom().getOscePost().getStandardizedRole().getShortName() == null && (assignment.getOscePostRoom().getOscePost().getStandardizedRole().getShortName().compareTo("")== 0 ))?"":assignment.getOscePostRoom().getOscePost().getStandardizedRole().getShortName())
//Feature : 154 
							+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
							+assignment.getOscePostRoom().getRoom().getRoomNumber());
				}
				
				toNames.add(examiner.getName()+" "+examiner.getPreName());
				fromNames.add(OsMaFilePathConstant.FROM_NAME);
				
				assignmentList.add(assignments);
				assignments = null;
			}
			
			templateVariables.put("toNames",toNames);
			templateVariables.put("fromNames",fromNames);
			templateVariables.put("assignments",assignmentList);
			
			return this.generateMailPDFUsingTemplate(semesterId.toString(),true, templateVariables);
			
		} catch (Exception e) {
			log.error("ERROR : "+e.getMessage());
			e.printStackTrace();
			return null;
		}finally{
			examiner = null;
			
			toNames = null;
			fromNames = null;
			assignments = null;
			assignmentList = null;
			assignments3 = null;
			
			dateFormat = null;
			timeFormat = null;
			
		}
	}

	private List<Assignment> getSortedAssignmentList(Set<Assignment> assignments) {
		List<Assignment> assignmentList = new ArrayList<Assignment>(assignments);
		Collections.sort(assignmentList, new Comparator<Assignment>() {

			@Override
			public int compare(Assignment assignment1, Assignment assignment2) {
				
				int result = assignment1.getOsceDay().getOsceDate().compareTo(assignment2.getOsceDay().getOsceDate());
				if(result != 0)
					return result;
				
				return assignment1.getTimeStart().compareTo(assignment2.getTimeStart());
										
			}
		});
		return assignmentList;
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String generateMailPDFUsingTemplate(String semesterId,Boolean isExaminer, Map templateVariables){
//Feature : 154 
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
		ByteArrayOutputStream os = null;
		
		int index = 0;
		try {
			
			toNames = (List<String>) templateVariables.get("toNames");
			fromNames = (List<String>) templateVariables.get("fromNames");
			assignments = (List<List<String>>) templateVariables.get("assignments");
			
			assignmentString = "";
			document = new Document();
//Feature : 154 
			//file = new File(fetchRealPath(true)+OsMaFilePathConstant.INVITATION_FILE_NAME_PDF_FORMAT);
			os = new ByteArrayOutputStream();
			writer = PdfWriter.getInstance(document, os);
			
			document.open();
			
			htmlWorker = new HTMLWorker(document);
			//Feature : 154 
			fileContents = this.getTemplateContent(semesterId, isExaminer, false)[1];
			
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

			saveFileToSession(OsMaFilePathConstant.INVITATION_FILE_NAME_PDF_FORMAT,os);
			//Feature : 154 
			//return fetchContextPath(true)+ OsMaFilePathConstant.INVITATION_FILE_NAME_PDF_FORMAT;
			return OsMaFilePathConstant.INVITATION_FILE_NAME_PDF_FORMAT;

		} catch (Exception e) {
			log.error("in SummoningsServiceImpl.generateMailPDFUsingTemplate: "
					+ e.getMessage());
			e.printStackTrace();
			return null;
		}finally{
			document = null;
			file = null;
			writer =null;
			htmlWorker = null;
			fileContents = null;
			os = null;
		}
		
		
	}
	
	
	private void saveFileToSession(String key, OutputStream os) {
		this.getThreadLocalRequest().getSession().setAttribute(key, os);
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Boolean sendSPMail(Long semesterId,List<Long> spIds){
		
		StandardizedPatient patient = null;
		
		PatientInSemester patientInSemester = null;
		
		final Map templateVariables = new HashMap();
		
		List<String> toNames = null;
		List<String> fromNames = null;
		List<String> assignments = null;
		List<List<String>> assignmentList = null;
		List<Assignment> assignments2 = null;
		
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
			timeFormat = new SimpleDateFormat("hh:mm a");
			
			toMailIds = new ArrayList<String>(0);
			fromMailId = OsMaFilePathConstant.FROM_MAIL_ID;
			subject = OsMaFilePathConstant.MAIL_SUBJECT;
			
			for(Long id : spIds){
				
				patient = StandardizedPatient.findStandardizedPatient(id);
				
				patientInSemester = PatientInSemester.findPisBySemesterSp(semesterId, id);
				
//				for(PatientInSemester patientInSemester : patient.getPatientInSemester()){
					
					assignments = new ArrayList<String>(0);
					
					if(patientInSemester.getPatientInRole() != null && patientInSemester.getPatientInRole().size() >0){
						
						assignments.add( "Date"
								+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ "Start Time"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ "End Time"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ "Role<br/>");
						
						
						for(PatientInRole patientInRole : patientInSemester.getPatientInRole()){
							
							assignments2 = getSortedAssignmentList(patientInRole.getAssignments());
							
							for(Assignment assignment : assignments2){
								
								log.info("Assignment : "+assignment.getId());
								
								assignments.add(dateFormat.format(assignment.getOsceDay().getOsceDate())
										+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
										+timeFormat.format(assignment.getTimeStart())
										+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
										+timeFormat.format(assignment.getTimeEnd())
										+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
										+assignment.getPatientInRole().getOscePost().getStandardizedRole().getShortName());
								
								
							}
						}
						
					}
					
					
					
//				}
				
				toNames.add(patient.getName()+" "+patient.getPreName());
				fromNames.add(OsMaFilePathConstant.FROM_NAME);
				
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
			
//Feature : 154 
			return this.sendMailUsingTemplate(semesterId.toString(),false, templateVariables);
			
		} catch (Exception e) {
			log.error("ERROR : "+e.getMessage());
			e.printStackTrace();
			return null;
		}finally{
			patient = null;
			patientInSemester = null;
			
			toNames = null;
			fromNames = null;
			assignments = null;
			assignmentList = null;
			assignments2 = null;
			
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
		List<Assignment> assignment2  = null;
		
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
			timeFormat = new SimpleDateFormat("hh:mm a");
			
			toMailIds = new ArrayList<String>(0);
			fromMailId = OsMaFilePathConstant.FROM_MAIL_ID;
			subject = OsMaFilePathConstant.MAIL_SUBJECT;
			
			for(Long id : examinerIds){
				
				examiner = Doctor.findDoctor(id);
						
				assignments = new ArrayList<String>(0);
				
				assignments.add( "Date"
						+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
						+ "Start Time"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
						+ "End Time"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
						+ "Role"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
						+ "Room Number<br/>");
				
				
				assignment2 = getSortedAssignmentList(examiner.getAssignments());
				
				for(Assignment assignment : assignment2){
							
					log.info("Assignment : "+assignment.getId());
					
					assignments.add(dateFormat.format(assignment.getOsceDay().getOsceDate())
							+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
							+timeFormat.format(assignment.getTimeStart())
							+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
							+timeFormat.format(assignment.getTimeEnd())
							+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
							//+assignment.getPatientInRole().getOscePost().getStandardizedRole().getShortName()
							+((assignment.getOscePostRoom().getOscePost().getStandardizedRole().getShortName() == null && (assignment.getOscePostRoom().getOscePost().getStandardizedRole().getShortName().compareTo("")== 0 ))?"":assignment.getOscePostRoom().getOscePost().getStandardizedRole().getShortName())
							+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
							+assignment.getOscePostRoom().getRoom().getRoomNumber());
							
				}
			
				
				toNames.add(examiner.getName()+" "+examiner.getPreName());
				fromNames.add(OsMaFilePathConstant.FROM_NAME);
				
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
			
//Feature : 154 
			return this.sendMailUsingTemplate(semesterId.toString(),true, templateVariables);
			
		} catch (Exception e) {
			log.error("ERROR : "+e.getMessage());
			e.printStackTrace();
			return null;
		}finally{
			examiner = null;
			
			toNames = null;
			fromNames = null;
			assignments = null;
			assignmentList = null;
			assignment2 = null;
			
			dateFormat = null;
			timeFormat = null;
			
			toMailIds = null;
			fromMailId = null;
			subject = null;
		}
	}
	
//	public  String fetchTemplateRealPath() {
//
//		String fileSeparator = System.getProperty("file.separator");
//		
//		try {
//
//			System.out.println("Finding servlet context");
//			
//				log.info("real Path for template is :" + getServletContext());
//		}catch(Exception e){
//				e.printStackTrace();
//			}
//			
//
//
//	}
	
	public  String fetchContextPath(boolean isDownload) {
//		fetchTemplateRealPath();
		String contextFileSeparator = "/";
//		RequestFactoryServlet.getThreadLocalRequest().getSession().
		return getServletContext().getContextPath() + ((isDownload) ? (contextFileSeparator + OsMaFilePathConstant.DOWNLOAD_DIR_PATH + contextFileSeparator) : contextFileSeparator);

	}

	public  String fetchRealPath(boolean isDownload) {
//		fetchTemplateRealPath();
		String fileSeparator = System.getProperty("file.separator");
		return getServletContext().getRealPath(fileSeparator) + ((isDownload) ? (OsMaFilePathConstant.DOWNLOAD_DIR_PATH + fileSeparator) : "");

	}
	
//	public  void fetchTemplateRealPath() {
//
//		String fileSeparator = System.getProperty("file.separator");
//		
//		try {
//
//			System.out.println("Finding servlet context");
//			try{
//				log.info("real Path for template is :" + getServletContext());
////				log.info("real Path for template is :" + RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext().getRealPath(fileSeparator) + fileSeparator);
////				return RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext().getRealPath(fileSeparator) + fileSeparator;
//				
////				log.info("real Path for template is :" + RequestFactoryServlet);
//						
//				log.info("@@@real Path for template is :" + getServletContext().getRealPath(fileSeparator));
//				log.info("real Path for template is :" + getThreadLocalRequest().getSession());
//				log.info("!!!!real Path for template is :" + getThreadLocalRequest().getSession().getServletContext().getRealPath(fileSeparator) + fileSeparator);
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//			
////			ServletContext servletContext = perThreadRequest.get().getSession().getServletContext();
////
////			if (servletContext != null) {
////				String checkRealPath = servletContext.getRealPath(fileSeparator);
////				log.info("checkRealPath is : " + checkRealPath);
////			} else {
////				log.info("checkRealPath is : null...");
////			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

//Feature : 154 
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Boolean sendMailUsingTemplate(String semesterId, Boolean isExaminer,Map templateVariables){
		
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
		
		// SPEC Change
		boolean success = false;
		
		try {
			toNames = (List<String>) templateVariables.get("toNames");
			toMailIds = (List<String>) templateVariables.get("toMailIds");
			fromNames = (List<String>) templateVariables.get("fromNames");
			
			fromMailId = (String) templateVariables.get("fromMailId");
			subject = (String) templateVariables.get("subject");
			
			assignments = (List<List<String>>) templateVariables.get("assignments");
			//Feature : 154 
			fileContents = this.getTemplateContent(semesterId, isExaminer, true)[1];
			
			// SPEC Change
			success = true;
			
			for(index=0; index < toNames.size(); index++){
				
				mailMessage = fileContents;
				
				mailMessage = mailMessage.replace("[toName]", toNames.get(index));
				
				assignmentString = "";
				
				for(String assignment : assignments.get(index)){
					assignmentString += assignment +"<br/>";
				}
				
				mailMessage = mailMessage.replace("[assignment]", assignmentString);
				mailMessage = mailMessage.replace("[fromName]", fromNames.get(index));
				
				log.info("emailService = " + emailService);
				
				if(!emailService.sendMail(new String[]{toMailIds.get(index)}, fromMailId, subject, mailMessage))
					success = false;
				
			}
			
			return success;
		} catch (Exception e) {
			log.error(e.getMessage());
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
	public String[] getTemplateContent(String semesterId, Boolean isExaminer, Boolean isEmail){
		//Feature : 154 
		File file = null;
		String filePath = null;
		try {						
			filePath = getPathName(semesterId, isExaminer, isEmail);
			
			file = new File(filePath);
			
			if(file.isFile()){
				
				return new String[]{ filePath ,FileUtils.readFileToString(file),"found"};
			}else{
				
				file = new File( fetchRealPath(false) +OsMaFilePathConstant.DEFAULT_MAIL_TEMPLATE);
				
				log.info("FILE PATH ==== " + file.getAbsolutePath());
				
				if(file.isFile())
					return new String[]{ fetchRealPath(false)+ OsMaFilePathConstant.DEFAULT_MAIL_TEMPLATE,FileUtils.readFileToString(file),"not_found"};
//Feature : 154 
				else
					return new String[]{"","",""};
			}
				
			
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}finally{
			file = null;
		}
		
	}
	
//Feature : 154 
	public String getPathName(String semesterId, Boolean isExaminer, Boolean isEmail) 
	{
		StringBuffer filePath = new StringBuffer(OsMaFilePathConstant.DEFAULT_MAIL_TEMPLATE_PATH);

		if (isEmail) {

			if (isExaminer)
				filePath.append(OsMaFilePathConstant.DEFAULT_EXAMINER_EMAIL_TEMPLATE_PATH);
			else
				filePath.append(OsMaFilePathConstant.DEFAULT_SP_EMAIL_TEMPLATE_PATH);
		} else {

			if (isExaminer)
				filePath.append(OsMaFilePathConstant.DEFAULT_EXAMINER_MAIL_TEMPLATE_PATH);
			else
				filePath.append(OsMaFilePathConstant.DEFAULT_SP_MAIL_TEMPLATE_PATH);
		}

		filePath.append(semesterId + OsMaFilePathConstant.TXT_EXTENTION);

		return filePath.toString();
	}
//Feature : 154 
	
	@Override
	public Boolean saveTemplate(String semesterId, Boolean isExaminer,Boolean isEmail, String templateContent){
		
		File file = null;
		
		try {
			
			if(OsMaFilePathConstant.DEFAULT_MAIL_TEMPLATE_PATH == null || OsMaFilePathConstant.DEFAULT_MAIL_TEMPLATE_PATH.equals(""))
				return false;
			
//Feature : 154 
			file = new File(getPathName(semesterId, isExaminer, isEmail));
		//Feature : 154 	
			if(file.isFile())
				FileUtils.deleteQuietly(file);
			else
				log.error("Template file does not exist. New File will be created.");
			
			FileUtils.touch(file);
			FileUtils.writeStringToFile(file, templateContent);
			return true;
			
			
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}finally{
			file = null;
		}
		
	}
	
	@Override
	public Boolean deleteTemplate(String semesterId, Boolean isExaminer,Boolean isEmail){
		
		File file = null;
		
		try {
			//Feature : 154 
			file = new File(getPathName(semesterId, isExaminer, isEmail));
			//Feature : 154 

			if(file.isFile())
				FileUtils.deleteQuietly(file);
			else
				log.error("Template file does not exist.");
			
			return true;
			
		} catch (Exception e) {
			log.error(e.getMessage());
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
