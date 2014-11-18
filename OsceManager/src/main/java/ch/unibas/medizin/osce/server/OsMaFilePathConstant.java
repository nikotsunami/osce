package ch.unibas.medizin.osce.server;

import java.util.Properties;

public class OsMaFilePathConstant {

	 public static String getUploadBaseDIRPath() {
		  
		 String  UPLOAD_BASE_DIR_PATH = ""; 
	
		  try 
		  {
			  Properties properties =  new Properties();
			  properties.load(OsMaFilePathConstant.class.getResourceAsStream("/META-INF/spring/config.properties")); 
			  UPLOAD_BASE_DIR_PATH = properties.getProperty("baseDir");
		  }
		  catch (Exception e) 
		  {
			   e.printStackTrace();
		  }
		  
		  return UPLOAD_BASE_DIR_PATH;
	 }
	
	 public static String getQRCodeURL() {
		  
		 String  QR_CODE_URL = ""; 
		 
	
		  try 
		  {
			  Properties properties =  new Properties();
			  properties.load(OsMaFilePathConstant.class.getResourceAsStream("/META-INF/spring/config.properties")); 
			  QR_CODE_URL = properties.getProperty("url");
		  }
		  catch (Exception e) 
		  {
			   e.printStackTrace();
		  }
		  
		  return QR_CODE_URL;
	 }
	 
	 public static String getSymmetricKey() {
		  
		 String  SYMMETRIC_KEY = ""; 
		 
	
		  try 
		  {
			  Properties properties =  new Properties();
			  properties.load(OsMaFilePathConstant.class.getResourceAsStream("/META-INF/spring/config.properties")); 
			  SYMMETRIC_KEY = properties.getProperty("key");
		  }
		  catch (Exception e) 
		  {
			   e.printStackTrace();
		  }
		  
		  return SYMMETRIC_KEY;
	 }
	 
	 public static String getQRCodeWidth() {
		  
		 String  width = ""; 
		 
	
		  try 
		  {
			  Properties properties =  new Properties();
			  properties.load(OsMaFilePathConstant.class.getResourceAsStream("/META-INF/spring/config.properties")); 
			  width = properties.getProperty("qrCodeWidth");
		  }
		  catch (Exception e) 
		  {
			   e.printStackTrace();
		  }
		  
		  return width;
	 }
	 
	 public static String getQRCodeHeight() {
		  
		 String  height = ""; 
		 
	
		  try 
		  {
			  Properties properties =  new Properties();
			  properties.load(OsMaFilePathConstant.class.getResourceAsStream("/META-INF/spring/config.properties")); 
			  height = properties.getProperty("qrCodeHeight");
		  }
		  catch (Exception e) 
		  {
			   e.printStackTrace();
		  }
		  
		  return height;
	 }
	// Module 9 Start

	public static String DOWNLOAD_DIR_PATH = "Download";

	public static String FILENAME = "StandardizedPatientList.csv";
	public static String FILE_NAME_PDF_FORMAT = "StandardizedPatientDetails.pdf";
	public static String ROLE_FILE_NAME_PDF_FORMAT = "StandardizedRoleDetails.pdf";
	public static String ROLE_FILE_STUDENT_MANAGEMENT_PDF_FORMAT = "StudentRoleDetails.pdf";

	public static String PATIENT_FILE_NAME_PDF_FORMAT = "StandardizedPatient.pdf";
	public static String STUDENT_FILE_NAME_PDF_FORMAT = "Student.pdf";
	public static String EXAMINER_FILE_NAME_PDF_FORMAT = "Examiner.pdf";
	public static String INVITATION_FILE_NAME_PDF_FORMAT = "Invitation.pdf";
	public static String EXAMINER_QR_PDF_FORMAT="ExaminerQR.pdf";
	public static final String STUDENT_QR_PDF_FORMAT = "StudentQR.pdf";

	public static String TXT_EXTENTION = ".txt";

	public static String DEFAULT_SP_MAIL_TEMPLATE_PATH = "mail/mailTemplate_SP/";
	public static String DEFAULT_EXAMINER_MAIL_TEMPLATE_PATH = "mail/mailTemplate_Ex/";

	public static String DEFAULT_SP_EMAIL_TEMPLATE_PATH = "email/emailTemplate_SP/";
	public static String DEFAULT_EXAMINER_EMAIL_TEMPLATE_PATH = "email/emailTemplate_Ex/";

	public static String DEFAULT_MAIL_TEMPLATE = "osMaEntry/gwt/unibas/templates/defaultTemplate.txt";

	public static String TEMPLATE_PATH = "osMaEntry/gwt/unibas/templates/";
	public static String DEFAULT_TEMPLATE_PATH = "defaultTemplate";
	public static String UPDATED_TEMPLATE_PATH = "updatedTemplate";

	public static String UPDATED_TEMPLATE_EXAMINER = "UpdatedTemplateExaminer";
	public static String UPDATED_TEMPLATE_STUDENT = "UpdatedTemplateStudent";
	public static String UPDATED_TEMPLATE_SP = "UpdatedTemplateSP";

	public static String DEFAULT_TEMPLATE_STUDENT = "DefaultTemplateStudent";
	public static String DEFAULT_TEMPLATE_EXAMINER = "DefaultTemplateExaminer";
	public static String DEFAULT_TEMPLATE_SP = "DefaultTemplateSP";

	public static String FROM_MAIL_ID = "userId@gmail.com";
	public static String FROM_NAME = "Mail Sender";
	public static String MAIL_SUBJECT = "Invitaion from OSCE";

	// CSV File Upload Path
	public static String CSV_FILEPATH = "osMaEntry/gwt/unibas/role/images/";

	// EXCEL File Upload Path For Learning Objective
	public static String EXCEL_FILEPATH = "osMaEntry/gwt/unibas/role/images/";

	// video path under webapps
	public static String appVideoUploadDirectory = "osMaEntry/gwt/unibas/sp/videos";

	// image path under webapps
	public static String appImageUploadDirectory = "osMaEntry/gwt/unibas/sp/images";

	// Module 9 End
	public static String IMPORT_UNPROCESSED_EOSCE_PATH = getUploadBaseDIRPath() + "/eosce/import/unprocessed/"; //"/usr/oscemanager/eOSCE/import/";
	
	public static String IMPORT_PROCESSED_EOSCE_PATH = getUploadBaseDIRPath() + "/eosce/import/processed/";
	// public static String DEFAULT_IMPORT_EOSCE_PATH = "C:\\oscemanager\\eOSCE\\import\\";

	// Role Module
	public static String ROLE_IMAGE_FILEPATH = getUploadBaseDIRPath() + "/role/images/"; //"/usr/oscemanager/role/images/";
	// public static String ROLE_IMAGE_FILEPATH = "c:\\oscemanager\\role\\images\\";

	// Export OSCE File Path
	public static String EXPORT_OSCE_PROCESSED_FILEPATH = getUploadBaseDIRPath() + "/eosce/export/processed/"; //"/usr/oscemanager/eosce/export/processed/";
	//public static String EXPORT_OSCE_PROCESSED_FILEPATH = "c:\\oscemanager\\eosce\\export\\processed\\";
	
	public static String EXPORT_OSCE_UNPROCESSED_FILEPATH = getUploadBaseDIRPath() + "/eosce/export/unprocessed/"; //"/usr/oscemanager/eosce/export/unprocessed/";
	// public static String EXPORT_OSCE_UNPROCESSED_FILEPATH = "c:\\oscemanager\\eosce\\export\\unprocessed\\";

	public static String DEFAULT_MAIL_TEMPLATE_PATH = getUploadBaseDIRPath() + "/Templates/mailTemplates/"; //"/usr/oscemanager/Templates/mailTemplates/";
	// public static String DEFAULT_MAIL_TEMPLATE_PATH = "C:\\oscemanager\\Templates\\mailTemplates\\";

	public static String PRINT_SCHEDULE_TEMPLATE = getUploadBaseDIRPath() + "/Templates/"; //"/usr/oscemanager/Templates/";
	// public static final String PRINT_SCHEDULE_TEMPLATE = "C:\\oscemanager\\Templates\\";

	// Module 8 (Assessment Plan)[

	// path of outside of webapps for parmanent storage of images
	// linux
	public static String localImageUploadDirectory = getUploadBaseDIRPath() + "/sp/images/"; //"/usr/oscemanager/sp/images/";
	// windows
	// public static String localImageUploadDirectory = "c:\\oscemanager\\sp\\images\\";

	public static String realImagePath = "";
	// public static String imagesrcPath="/osMaEntry/gwt/unibas/sp/images/";

	// path of outside of webapps for parmanent storage of images
	// linux
	public static String localVideoUploadDirectory = getUploadBaseDIRPath() + "/sp/videos/"; //"/usr/oscemanager/sp/videos/";
	// windows
	// public static String localVideoUploadDirectory = "c://oscemanager//sp//videos//";

	public static String realVideoPath = "";//
	// public static String videosrcPath="/osMaEntry/gwt/unibas/sp/videos/";
	
	public static String assignmentXslPath="/osMaEntry/gwt/unibas/xsl/assignment.xsl";
	
	public static String assignmentHTML="/osMaEntry/gwt/unibas/";
	// Module 8]

	public static String localSpDataDirectory = getUploadBaseDIRPath() + "/sp/";
	
	public static final String appStandardizedPatientPaymentPDF = "/osMaEntry/gwt/unibas/payment/Honorarabrechnung_Form_2011_01.pdf";

	public static final String EXTRA_SPACE_QR="                                       ";
	
	public static final String EXPORT_EOSCE = "/eosce";
	
	public static final String EXPORT_IOSCE = "/iosce";
	
	public static final String EOSCE_FILE_EXTENSION = ".osceexchange";
	
	public static final String IOSCE_FILE_EXTENSION = ".iosce";
	
	public static final String EXAMINER_LIST_QR = "ExaminerQr";

	public static final String STUDENT_LIST_QR = "StudentQR";
}
