package ch.unibas.medizin.osce.server;

import static org.apache.commons.lang.StringUtils.defaultString;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

import javax.crypto.Cipher;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.eOSCESyncException;
import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.eOSCESyncService;
import ch.unibas.medizin.osce.domain.Answer;
import ch.unibas.medizin.osce.domain.AnswerCheckListCriteria;
import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.domain.BucketInformation;
import ch.unibas.medizin.osce.domain.CheckList;
import ch.unibas.medizin.osce.domain.ChecklistCriteria;
import ch.unibas.medizin.osce.domain.ChecklistItem;
import ch.unibas.medizin.osce.domain.ChecklistOption;
import ch.unibas.medizin.osce.domain.ChecklistQuestion;
import ch.unibas.medizin.osce.domain.ChecklistTopic;
import ch.unibas.medizin.osce.domain.Course;
import ch.unibas.medizin.osce.domain.Doctor;
import ch.unibas.medizin.osce.domain.KeyPair;
import ch.unibas.medizin.osce.domain.Notes;
import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.domain.OsceDay;
import ch.unibas.medizin.osce.domain.OscePost;
import ch.unibas.medizin.osce.domain.OscePostRoom;
import ch.unibas.medizin.osce.domain.OsceSequence;
import ch.unibas.medizin.osce.domain.Semester;
import ch.unibas.medizin.osce.domain.Signature;
import ch.unibas.medizin.osce.domain.StandardizedRole;
import ch.unibas.medizin.osce.domain.Student;
import ch.unibas.medizin.osce.server.bean.ObjectFactory;
import ch.unibas.medizin.osce.server.bean.Oscedata;
import ch.unibas.medizin.osce.server.bean.Oscedata.Candidates;
import ch.unibas.medizin.osce.server.bean.Oscedata.Candidates.Candidate;
import ch.unibas.medizin.osce.server.bean.Oscedata.Checklists;
import ch.unibas.medizin.osce.server.bean.Oscedata.Checklists.Checklist;
import ch.unibas.medizin.osce.server.bean.Oscedata.Checklists.Checklist.Checklisttopics;
import ch.unibas.medizin.osce.server.bean.Oscedata.Checklists.Checklist.Checklisttopics.Checklisttopic;
import ch.unibas.medizin.osce.server.bean.Oscedata.Checklists.Checklist.Checklisttopics.Checklisttopic.Checklistitems;
import ch.unibas.medizin.osce.server.bean.Oscedata.Checklists.Checklist.Checklisttopics.Checklisttopic.Checklistitems.Checklistitem;
import ch.unibas.medizin.osce.server.bean.Oscedata.Checklists.Checklist.Checklisttopics.Checklisttopic.Checklistitems.Checklistitem.Checklistcriteria;
import ch.unibas.medizin.osce.server.bean.Oscedata.Checklists.Checklist.Checklisttopics.Checklisttopic.Checklistitems.Checklistitem.Checklistcriteria.Checklistcriterion;
import ch.unibas.medizin.osce.server.bean.Oscedata.Checklists.Checklist.Checklisttopics.Checklisttopic.Checklistitems.Checklistitem.Checklistoptions;
import ch.unibas.medizin.osce.server.bean.Oscedata.Checklists.Checklist.Checklisttopics.Checklisttopic.Checklistitems.Checklistitem.Checklistoptions.Checklistoption;
import ch.unibas.medizin.osce.server.bean.Oscedata.Courses;
import ch.unibas.medizin.osce.server.bean.Oscedata.Credentials;
import ch.unibas.medizin.osce.server.bean.Oscedata.Credentials.Host;
import ch.unibas.medizin.osce.server.bean.Oscedata.Examiners;
import ch.unibas.medizin.osce.server.bean.Oscedata.Examiners.Examiner;
import ch.unibas.medizin.osce.server.bean.Oscedata.Rotations;
import ch.unibas.medizin.osce.server.bean.Oscedata.Rotations.Rotation;
import ch.unibas.medizin.osce.server.bean.Oscedata.Stations;
import ch.unibas.medizin.osce.server.bean.Oscedata.Stations.Station;
import ch.unibas.medizin.osce.server.i18n.GWTI18N;
import ch.unibas.medizin.osce.server.importanswer.AnswerOption;
import ch.unibas.medizin.osce.server.importanswer.AudioNote;
import ch.unibas.medizin.osce.server.importanswer.Criteria;
import ch.unibas.medizin.osce.server.importanswer.Examanswers;
import ch.unibas.medizin.osce.server.importanswer.StudentAnswer;
import ch.unibas.medizin.osce.shared.BucketInfoType;
import ch.unibas.medizin.osce.shared.EncryptionType;
import ch.unibas.medizin.osce.shared.ExportOsceData;
import ch.unibas.medizin.osce.shared.ExportOsceType;
import ch.unibas.medizin.osce.shared.NoteType;
import ch.unibas.medizin.osce.shared.i18n.OsceConstantsWithLookup;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.Region;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.dd.plist.NSData;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import com.dd.plist.NSString;
import com.dd.plist.PropertyListParser;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import flexjson.JSONDeserializer;

public class eOSCESyncServiceImpl extends RemoteServiceServlet implements eOSCESyncService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger Log = Logger.getLogger(eOSCESyncServiceImpl.class);
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final SimpleDateFormat jsonsdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); 
	private static final String IMPORT_EOSCE_FILE_EXTENSION = "crumble";
	private static final String IMPORT_IOSCE_FILE_EXTENSION = "iosceresult";
	private static final String AUDIO_FILE_EXTENSION = ".mp3";
	private String folderSeparatorLocal="\\";
	private String folderSeparatorProduction="/";
	private boolean isLocal=false; // To put folder separator for local test set true during development otherwise false

	private static final String IMPORT_ANSWER_PLIST_SHA_CODE = "DigitalSignature";
	private static final String IMPORT_ANSWER_PLIST_DATA = "EncryptedData";
	private static final String IMPORT_ANSWER_PLIST_SUBMIT = "submit";
	private static final String IMPORT_ANSWER_PLIST_ENCRYPTION = "encryption";
	private static final String IMPORT_ANSWER_PLIST_SIGN_MECHANISM = "signMechanism";
	private static final String IMPORT_ANSWER_EXAMINER_DATA = "examinerId";
	
	public List<String> processedFileList(ExportOsceType osceType, Long semesterID) throws eOSCESyncException
	{
		List<String> fileList = new ArrayList<String>();
		try
		{
			Semester semester = Semester.findSemester(semesterID);
			String path = OsMaFilePathConstant.IMPORT_PROCESSED_EOSCE_PATH + semester.getSemester() + semester.getCalYear();
	    	if (ExportOsceType.EOSCE.equals(osceType)) {
	    		path = path + OsMaFilePathConstant.EXPORT_EOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);	
	    	}
	    	else if (ExportOsceType.IOSCE.equals(osceType)) {
	    		path = path + OsMaFilePathConstant.EXPORT_IOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
	    	}
	    	
			String accessKey = "";
			String secretKey = "";
			String bucketName = "";
			
			BucketInformation bucketInformation = BucketInformation.findBucketInformationBySemesterForImport(semesterID);
			
			if (bucketInformation != null)
			{
				accessKey = bucketInformation.getAccessKey() == null ? "" : bucketInformation.getAccessKey();
				secretKey = bucketInformation.getSecretKey() == null ? "" : bucketInformation.getSecretKey();
				bucketName = bucketInformation.getBucketName() == null ? "" : bucketInformation.getBucketName();
			}
			
			AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
			AmazonS3Client client;
			
			Properties properties =  new Properties();
			properties.load(OsMaFilePathConstant.class.getResourceAsStream("/META-INF/spring/proxy.properties"));			
			String proxyHost = properties.getProperty("proxy.host");
			String proxyPort = properties.getProperty("proxy.port");
			
			if (StringUtils.isNotBlank(proxyHost) && StringUtils.isNotBlank(proxyPort))
			{
				ClientConfiguration configuration = new ClientConfiguration();
				configuration.setProtocol(Protocol.HTTP);
				configuration.setProxyHost(proxyHost);
				configuration.setProxyPort(Integer.parseInt(proxyPort));
				client = new AmazonS3Client(credentials, configuration);
			}
			else  
			{
				client = new AmazonS3Client(credentials);			
			}
			
			ObjectListing objectListing = client.listObjects(bucketName.toLowerCase());
			
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries())
			{
				String fileName = objectSummary.getKey();
				 
				/*if (fileName.substring(0, 1).matches("[0-9]"))
				{*/
					if (StringUtils.startsWith(fileName, "00") == false && FilenameUtils.getExtension(fileName).equals(IMPORT_EOSCE_FILE_EXTENSION))
					{	
						fileName = fileName.replaceAll(" ", "_");
						String fullFilename = path + fileName;
						if (new File(fullFilename).exists() == true)
						{
							fileList.add(objectSummary.getKey());					
						}
					}
				//}
				
			}
		}
		catch(AmazonServiceException ase)
		{
			Log.error(ase.getMessage());
			throw new eOSCESyncException(ase.getErrorType().toString(),ase.getMessage());
		}
		catch(AmazonClientException ace)
		{
			Log.error(ace.getMessage());
			throw new eOSCESyncException("",ace.getMessage());
		}
		catch(Exception e)
		{
			Log.error(e.getMessage());
			throw new eOSCESyncException("",e.getMessage());
		}
		
		return fileList;
	}
	
	public List<String> unprocessedFileList(ExportOsceType osceType, Long semesterID) throws eOSCESyncException
	{
		List<String> fileList = new ArrayList<String>();
		try
		{
			Semester semester = Semester.findSemester(semesterID);
			String path = OsMaFilePathConstant.IMPORT_PROCESSED_EOSCE_PATH + semester.getSemester() + semester.getCalYear();
	    	if (ExportOsceType.EOSCE.equals(osceType)) {
	    		path = path + OsMaFilePathConstant.EXPORT_EOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);	
	    	}
	    	else if (ExportOsceType.IOSCE.equals(osceType)) {
	    		path = path + OsMaFilePathConstant.EXPORT_IOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
	    	}
			
			String accessKey = "";
			String secretKey = "";
			String bucketName = "";
			
			BucketInformation bucketInformation = BucketInformation.findBucketInformationBySemesterForImport(semesterID);
			
			if (bucketInformation != null)
			{
				accessKey = bucketInformation.getAccessKey() == null ? "" : bucketInformation.getAccessKey();
				secretKey = bucketInformation.getSecretKey() == null ? "" : bucketInformation.getSecretKey();
				bucketName = bucketInformation.getBucketName() == null ? "" : bucketInformation.getBucketName();
			}
			
			AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
			AmazonS3Client client;
			
			Properties properties =  new Properties();
			properties.load(OsMaFilePathConstant.class.getResourceAsStream("/META-INF/spring/proxy.properties"));			
			String proxyHost = properties.getProperty("proxy.host");
			String proxyPort = properties.getProperty("proxy.port");
			
			if (StringUtils.isNotBlank(proxyHost) && StringUtils.isNotBlank(proxyPort))
			{
				ClientConfiguration configuration = new ClientConfiguration();
				configuration.setProtocol(Protocol.HTTP);
				configuration.setProxyHost(proxyHost);
				configuration.setProxyPort(Integer.parseInt(proxyPort));
				client = new AmazonS3Client(credentials, configuration);
			}
			else  
			{
				client = new AmazonS3Client(credentials);			
			}
			
			ObjectListing objectListing = client.listObjects(bucketName.toLowerCase());
			
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries())
			{
				String fileName = objectSummary.getKey();
				/*if (fileName.substring(0, 1).matches("[0-9]"))
				{*/
					if (StringUtils.startsWith(fileName, "00") == false && FilenameUtils.getExtension(fileName).equals(IMPORT_EOSCE_FILE_EXTENSION))
					{
						fileName = fileName.replaceAll(" ", "_");
						String fullFilename = path + fileName;
						if (new File(fullFilename).exists() == false)
						{
							fileList.add(objectSummary.getKey());					
						}
					}
				//}
			}
		}
		catch(AmazonServiceException ase)
		{
			Log.error(ase.getMessage());
			throw new eOSCESyncException(ase.getErrorType().toString(),ase.getMessage());
		}
		catch(AmazonClientException ace)
		{
			Log.error(ace.getMessage());
			throw new eOSCESyncException("",ace.getMessage());
		}
		catch(Exception e)
		{
			Log.error(e.getMessage());
			throw new eOSCESyncException("",e.getMessage());
		}
		
		//System.out.println("~~UNPROCESSED FILE SIZE : " + fileList.size());
		return fileList;
	}

	public void deleteAmzonS3Object(ExportOsceType osceType, Long semesterID, List<String> fileList, String bucketName, String accessKey, String secretKey) throws eOSCESyncException {
		try
		{
			Semester semester = Semester.findSemester(semesterID);
			String path = OsMaFilePathConstant.IMPORT_PROCESSED_EOSCE_PATH + semester.getSemester() + semester.getCalYear();
	    	if (ExportOsceType.EOSCE.equals(osceType)) {
	    		path = path + OsMaFilePathConstant.EXPORT_EOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);	
	    	}
	    	else if (ExportOsceType.IOSCE.equals(osceType)) {
	    		path = path + OsMaFilePathConstant.EXPORT_IOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
	    	}
	    	
			//write access and secret key
			AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
			AmazonS3Client client;
			
			Properties properties =  new Properties();
			properties.load(OsMaFilePathConstant.class.getResourceAsStream("/META-INF/spring/proxy.properties"));			
			String proxyHost = properties.getProperty("proxy.host");
			String proxyPort = properties.getProperty("proxy.port");
			
			if (StringUtils.isNotBlank(proxyHost) && StringUtils.isNotBlank(proxyPort))
			{
				ClientConfiguration configuration = new ClientConfiguration();
				configuration.setProtocol(Protocol.HTTP);
				configuration.setProxyHost(proxyHost);
				configuration.setProxyPort(Integer.parseInt(proxyPort));
				client = new AmazonS3Client(credentials, configuration);
			}
			else  
			{
				client = new AmazonS3Client(credentials);			
			}				
			
			for (int i=0; i<fileList.size(); i++)
			{
				String fileName = fileList.get(i);
				fileName = fileName.replaceAll(" ", "_");
				fileName = path + fileName;
				File file = new File(fileName);
				
				if (file.exists() == true)
				{
					file.delete();
				}
				//System.out.println("DELETED : " + fileName);
				//write bucket name
				client.deleteObject(bucketName, fileList.get(i));
			}
		}
		catch(AmazonServiceException ase)
		{
			Log.error(ase.getMessage());
			throw new eOSCESyncException(ase.getErrorType().toString(),ase.getMessage());
		}
		catch(AmazonClientException ace)
		{
			Log.error(ace.getMessage());
			throw new eOSCESyncException("",ace.getMessage());
		}
		catch(Exception e)
		{
			Log.error(e.getMessage());
			throw new eOSCESyncException("",e.getMessage());
		}
	}
	
	public boolean addFile(ExportOsceType osceType, String filePath, String filename, byte[] byteArray, String secretKey, String encryptionKey, Long semesterID) throws eOSCESyncException
	{
		String file_name = filename.replaceAll(" ", "_");
		
		filename = filePath + file_name;
		
		try
		{
			File file = new File(filename);
			FileUtils.touch(file);
			FileUtils.writeByteArrayToFile(file, byteArray);
			
			/*byte[] buffer = new byte[1024];
			BufferedInputStream bis = new BufferedInputStream(byteArray);					   
			OutputStream os = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(os);
			int readCount;
			
			while( (readCount = bis.read(buffer)) > 0) {				
				bos.write(buffer, 0, readCount);
			}
			
			bis.close();
			bos.close();*/
			
			if (ExportOsceType.EOSCE.equals(osceType)) {
				String symmetricKey = "";
				if (StringUtils.isNotBlank(encryptionKey) && encryptionKey.length() >= 16)
					symmetricKey = encryptionKey.substring(0, 16);
				else
					symmetricKey = secretKey.substring(0, 16);
				
				String decFileName = S3Decryptor.decrypt(symmetricKey, file_name, filePath);
				
				return importEOSCE(decFileName);
			}
			else if (ExportOsceType.IOSCE.equals(osceType)) {
				boolean importiOSCEFlag = importIOSCE(filename, semesterID);
				if (importiOSCEFlag == false) {
					File importFile = new File(filename);
					if (importFile.exists()) {
						importFile.delete();
					}
				}					
				return importiOSCEFlag;
			}
				
			return false;
		}
		catch(Exception e)
		{
			Log.error(e.getMessage());
				File importFile = new File(filename);
				if (importFile.exists()) {
					importFile.delete();
				}
			throw new eOSCESyncException("",e.getMessage());
		} 
		
	}
	
	@Transactional
	private boolean importIOSCE(String filename, Long semesterID) throws eOSCESyncException {
		ChannelSftp channelSftp = null;
		try {
			//decrypted code
			BucketInformation bucketInformation = BucketInformation.findBucketInformationBySemesterForImport(semesterID);
			byte[] bytes = FileUtils.readFileToByteArray(new File(filename));
			String SFTPWORKINGDIR = (bucketInformation == null || bucketInformation.getBasePath().isEmpty()) ? "" : bucketInformation.getBasePath();

			String dir = SFTPWORKINGDIR;
			
			if (dir.endsWith("/") == false) {
				dir = dir + "/";
			}
			
			NSDictionary rootDict = (NSDictionary) PropertyListParser.parse(bytes);
			NSObject encryption = rootDict.objectForKey(IMPORT_ANSWER_PLIST_ENCRYPTION);
			NSString encryptionData = (NSString) encryption;
				
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			
			if(encryptionData.getContent().equals("0")) {
				byteArrayOutputStream = decryptFileWithAdminPrivateKey(bytes, bucketInformation);
			}
			else if(encryptionData.getContent().equals("1")) {
				byteArrayOutputStream = decryptFileWithSymmetricKey(bytes, bucketInformation);
			}
		
			if (byteArrayOutputStream != null) {	
				JSONDeserializer<StudentAnswer> deserializer = new JSONDeserializer<StudentAnswer>().use("values", StudentAnswer.class);				
				StudentAnswer studentAnswer = new StudentAnswer();
				studentAnswer = deserializer.deserializeInto(new InputStreamReader(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())), studentAnswer);
				
				if (studentAnswer != null)
				{
					Examanswers examanswer = studentAnswer.getExamanswers();
					
					channelSftp = createSFTPConnection(bucketInformation);
					
					for (ch.unibas.medizin.osce.server.importanswer.Rotation rotation : examanswer.getRotations()) {
						
						if (StringUtils.isNotBlank(rotation.getStationId()) && StringUtils.isNotBlank(rotation.getExaminerId())) {
							Doctor doctor = Doctor.findDoctor(Long.parseLong(rotation.getExaminerId()));
							OscePostRoom oscePostRoom = OscePostRoom.findOscePostRoom(Long.parseLong(rotation.getStationId()));
							
							if (rotation.getSignature() != null) {
								new Signature().deleteSignature(doctor.getId(), oscePostRoom.getId());
								importiOSCESignature(rotation.getSignature(), doctor, oscePostRoom, bucketInformation, channelSftp, dir);
							}
							
							List<ch.unibas.medizin.osce.server.importanswer.Student> studentList = rotation.getStudents();
							
							for (ch.unibas.medizin.osce.server.importanswer.Student student : studentList) {
								String studentId = student.getId();
								if (StringUtils.isNotBlank(studentId)) {
									Student osceStudent = Student.findStudent(Long.parseLong(studentId));
									
									if (student.getNotes() != null) {
										new Answer().deleteTextualNote(doctor.getId(), oscePostRoom.getId(), osceStudent.getId());
										importTextualNotes(student.getNotes(), osceStudent, doctor, oscePostRoom);
									}
									
									if (student.getAudioNotes() != null) {
										if(oscePostRoom != null){
											new Answer().deleteAudioNote(doctor.getId(), oscePostRoom.getId(), osceStudent.getId());
											importAudioNotes(student.getAudioNotes(), osceStudent, doctor, oscePostRoom, semesterID, bucketInformation, channelSftp, dir);
										}
									}	
									
									List<ch.unibas.medizin.osce.server.importanswer.Answer> answerList = student.getAnswers();
									
									if (answerList.size() > 0) {
										if(oscePostRoom != null){
											new Answer().deleteAnswerAndCriteria(doctor.getId(), oscePostRoom.getId(), osceStudent.getId());
										}
									}
									
									for (ch.unibas.medizin.osce.server.importanswer.Answer jsonAnswer : answerList) {
										String questionId = jsonAnswer.getQuestionId();
										if (StringUtils.isNotBlank(questionId)) {
											ChecklistItem checklistItem = ChecklistItem.findChecklistItem(Long.parseLong(questionId));
											AnswerOption answerOption = jsonAnswer.getAnswerOption();
											String checklistOptionId = answerOption.getChecklistOptionId();
											if (StringUtils.isNotBlank(checklistOptionId)) {

												// Crude hack for iOSCE Int16 overflow
												Long maybeNegativeChecklistOptionId = Long.parseLong(checklistOptionId);

												if (maybeNegativeChecklistOptionId < 0) {
													maybeNegativeChecklistOptionId += 65536;
												}

												ChecklistOption checklistOption = ChecklistOption.findChecklistOption(maybeNegativeChecklistOptionId);

												Answer osceAnswer = new Answer();
												osceAnswer.setAnswer(answerOption.getChecklistOptionValue());
												osceAnswer.setChecklistOption(checklistOption);
												osceAnswer.setDoctor(doctor);
												osceAnswer.setOscePostRoom(oscePostRoom);
												osceAnswer.setChecklistItem(checklistItem);
												if (answerOption.getTimestamp() != null) {
													osceAnswer.setAnswerTimestamp(jsonsdf.parse(answerOption.getTimestamp()));
												}											
												osceAnswer.setStudent(osceStudent);
												osceAnswer.persist();
												
												if (jsonAnswer.getCriterias() != null && jsonAnswer.getCriterias().size() > 0) {
													importChecklistCriteria(osceAnswer, jsonAnswer.getCriterias());
												}
											}
										}
									}
								}
							}
						}
					}
					return true;
				}
			}
			
			return false;
		}
		catch (Exception e) {
			Log.error(e.getMessage(), e);
			throw new eOSCESyncException("",e.getMessage());
		}
		finally{
			try {
				if(channelSftp != null){
					if(channelSftp.getSession() != null){
						channelSftp.getSession().disconnect();
					}
					
					channelSftp.disconnect();
				}
			} catch (JSchException e) {
				Log.error(e.getMessage(), e);
				throw new eOSCESyncException("",e.getMessage());
			}
		}
	}
	

	private ByteArrayOutputStream decryptFileWithSymmetricKey(byte[] bytes,BucketInformation bucketInformation)  throws eOSCESyncException{

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		try{
			String symmetricKey =bucketInformation.getEncryptionKey();
			ByteArrayOutputStream generateDecryptedDataFile = null;
			if (StringUtils.isNotBlank(symmetricKey))
			{
				NSDictionary rootDict = (NSDictionary) PropertyListParser.parse(bytes);
				NSObject encryptedObject = rootDict.objectForKey(IMPORT_ANSWER_PLIST_DATA);
				NSData encryptedData = (NSData) encryptedObject;
				generateDecryptedDataFile = S3Decryptor.decryptFile(StringUtils.rightPad(symmetricKey, 16,'0'), encryptedData.bytes());
				
				NSObject signMechanismObject = rootDict.objectForKey(IMPORT_ANSWER_PLIST_SIGN_MECHANISM);
				NSString signMechanismData = (NSString) signMechanismObject;
				
				if (generateDecryptedDataFile != null && signMechanismData.getContent().equals(String.valueOf(EncryptionType.ASYM.ordinal()))) {
					
					//Need to discuss with team.
					/*NSObject shaHashObject = rootDict.objectForKey(IMPORT_ANSWER_PLIST_SHA_CODE);
					NSData shaHashValue = (NSData) shaHashObject;
					
					if (shaHashValue == null) {
						generateDecryptedDataFile = null;
					}
					else {
						JSONDeserializer<StudentAnswer> deserializer = new JSONDeserializer<StudentAnswer>().use("values", StudentAnswer.class);				
						StudentAnswer studentAnswer = new StudentAnswer();
						studentAnswer = deserializer.deserializeInto(new InputStreamReader(new ByteArrayInputStream(generateDecryptedDataFile.toByteArray())), studentAnswer);
						
						if (studentAnswer.getPublicKey() != null) {
							PublicKey publicKey = generatePublicKey(studentAnswer.getPublicKey());
							boolean isSigned = generateSignData(publicKey, shaHashValue.bytes(), generateDecryptedDataFile.toByteArray());
							if (isSigned == false)
								generateDecryptedDataFile = null;
						}
					}*/
				}
				else if (generateDecryptedDataFile != null && signMechanismData.getContent().equals(EncryptionType.HASH.ordinal())) {
					NSObject shaHashObject = rootDict.objectForKey(IMPORT_ANSWER_PLIST_SHA_CODE);
					NSString shaHashValue = (NSString) shaHashObject;
					
					byte[] calculatedHashValue = checkSignedWithSHA256(generateDecryptedDataFile.toByteArray());
					String base64CalculatedHashValue = Base64.encodeBase64String(calculatedHashValue);
					
					if (shaHashValue == null || base64CalculatedHashValue == null) {
						generateDecryptedDataFile = null;
					}
					else if (shaHashValue.getContent().equals(base64CalculatedHashValue) == false) {
						generateDecryptedDataFile = null;
					}
				}
			}
			return generateDecryptedDataFile;
		}
		catch (Exception e) {
			Log.error(e.getMessage(), e);
		}
		return bout;
	}

	private void importChecklistCriteria(Answer osceAnswer, List<Criteria> criteriaList) throws eOSCESyncException {
		try {
			for (Criteria criteria : criteriaList) {
				Long checklistCriteriaId = criteria.getId();
				
				if (checklistCriteriaId != null) {
					ChecklistCriteria checklistCriteria = ChecklistCriteria.findChecklistCriteria(checklistCriteriaId);
					
					AnswerCheckListCriteria answerCheckListCriteria = new AnswerCheckListCriteria();
					answerCheckListCriteria.setAnswer(osceAnswer);
					answerCheckListCriteria.setChecklistCriteria(checklistCriteria);
					if (criteria.getTimestamp() != null) {
						answerCheckListCriteria.setAnswerCriteriaTimestamp(jsonsdf.parse(criteria.getTimestamp()));
					}					
					answerCheckListCriteria.persist();
				}
			}
		}
		catch (Exception e) {
			Log.error(e.getMessage(), e);
			throw new eOSCESyncException("",e.getMessage());
		}		
	}

	public ChannelSftp createSFTPConnection(BucketInformation bucketInformation) throws eOSCESyncException {
		try {
			String SFTPHOST = bucketInformation == null ? "" : bucketInformation.getBucketName();
			int    SFTPPORT = 22;
			String SFTPUSER = (bucketInformation == null || bucketInformation.getAccessKey().isEmpty()) ? "" : bucketInformation.getAccessKey();
			String SFTPPASS = (bucketInformation == null || bucketInformation.getSecretKey().isEmpty()) ? "" : bucketInformation.getSecretKey();
			String SFTPWORKINGDIR = (bucketInformation == null || bucketInformation.getBasePath().isEmpty()) ? "" : bucketInformation.getBasePath();
			
			Session     session     = null;
			Channel     channel     = null;
			ChannelSftp channelSftp = null;
			
			JSch jsch = new JSch();
			session = jsch.getSession(SFTPUSER,SFTPHOST,SFTPPORT);
			session.setPassword(SFTPPASS);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp)channel;
			channelSftp.cd(SFTPWORKINGDIR);
			return channelSftp;
		}
		catch (SftpException e) {
			Log.error(e.getMessage(), e);
			throw new eOSCESyncException("",e.getMessage());
		}			
		catch(Exception ex){
			Log.error(ex.getMessage(), ex);
			throw new eOSCESyncException("",ex.getMessage());
		}
	}
	
	private void importAudioNotes(List<AudioNote> audioNotes, Student osceStudent, Doctor doctor, OscePostRoom oscePostRoom, Long semesterID, BucketInformation bucketInformation, ChannelSftp channelSftp, String dir) throws eOSCESyncException {
		try {
			
			Semester semester = Semester.findSemester(semesterID);
			String localPath = OsMaFilePathConstant.IMPORT_AUDIO_NOTE_PATH + semester.getSemester() + semester.getCalYear() + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
			
			for (AudioNote audioNote : audioNotes) {
				String fileName = "";
				if (BucketInfoType.S3.equals(bucketInformation.getType())) {
					fileName = copyAudioFileFromS3(bucketInformation, audioNote.getNotePath(), localPath);
				}
				else if (BucketInfoType.FTP.equals(bucketInformation.getType())) {
					System.out.println("Audio note" + audioNote.getNotePath());
					fileName = copyAudioFileFromSFTP(bucketInformation, audioNote.getNotePath(), localPath, doctor.getId(), channelSftp, dir);
				}
				
				if (StringUtils.isNotBlank(fileName)) {
					OsceDay osceDay = oscePostRoom.getOscePost().getOsceSequence().getOsceDay();
					
					Notes osceNotes = new Notes();
					osceNotes.setStudent(osceStudent);
					osceNotes.setDoctor(doctor);
					osceNotes.setOscePostRoom(oscePostRoom);
					osceNotes.setOsceDay(osceDay);
					if (StringUtils.isNotBlank(fileName)) {
						osceNotes.setComment(fileName);
					}
					else {
						osceNotes.setComment(audioNote.getNotePath());
					}
					osceNotes.setNoteType(NoteType.values()[Integer.parseInt(audioNote.getType())]);
					if (audioNote.getTimestamp() != null)
						osceNotes.setLastviewed(jsonsdf.parse(audioNote.getTimestamp()));
					
					osceNotes.persist();
				}
			}
		}
		catch (Exception e) {
			Log.error(e.getMessage(), e);
			throw new eOSCESyncException("",e.getMessage());
		}
	}

	private String copyAudioFileFromSFTP(BucketInformation bucketInformation, String notePath, String localPath, Long doctorId, ChannelSftp channelSftp, String dir) throws eOSCESyncException {
		
		try {	
			byte[] data = null;
			String fullAudioNotePath = dir + doctorId + "/" + notePath;
			InputStream inputStream = channelSftp.get(fullAudioNotePath);
			
			String filename = UUID.randomUUID().toString() + "_" + notePath;
			String fullFilePath = localPath + filename;
			File file = new File(fullFilePath);
			FileUtils.touch(file);
			data = IOUtils.toByteArray(inputStream);
			if (data != null && data.length > 0) {
				FileUtils.writeByteArrayToFile(file, data);
				return filename;
			}
			else {
				Log.error("No audio notes are found");
				return null;
			}
		}
		catch (SftpException e) {
			Log.error(e.getMessage(), e);
			throw new eOSCESyncException("",e.getMessage());
		}			
		catch(Exception ex){
			Log.error(ex.getMessage(), ex);
			throw new eOSCESyncException("",ex.getMessage());
		}
		
	}

	private String copyAudioFileFromS3(BucketInformation bucketInformation, String notePath, String localPath) throws eOSCESyncException {
		try {
			String accessKey = "";
			String secretKey = "";
			String bucketName = "";
			
			if (bucketInformation != null)
			{
				accessKey = bucketInformation.getAccessKey() == null ? "" : bucketInformation.getAccessKey();
				secretKey = bucketInformation.getSecretKey() == null ? "" : bucketInformation.getSecretKey();
				bucketName = bucketInformation.getBucketName() == null ? "" : bucketInformation.getBucketName();
			}
			
			AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
			AmazonS3Client client;
			
			Properties properties =  new Properties();
			properties.load(OsMaFilePathConstant.class.getResourceAsStream("/META-INF/spring/proxy.properties"));			
			String proxyHost = properties.getProperty("proxy.host");
			String proxyPort = properties.getProperty("proxy.port");
			
			if (StringUtils.isNotBlank(proxyHost) && StringUtils.isNotBlank(proxyPort))
			{
				ClientConfiguration configuration = new ClientConfiguration();
				configuration.setProtocol(Protocol.HTTP);
				configuration.setProxyHost(proxyHost);
				configuration.setProxyPort(Integer.parseInt(proxyPort));
				client = new AmazonS3Client(credentials, configuration);
			}
			else  
			{
				client = new AmazonS3Client(credentials);			
			}	
			
			S3Object object = null;			
			object = client.getObject(new GetObjectRequest(bucketName, notePath));
			InputStream inputStream = object.getObjectContent();
			
			String filename = FilenameUtils.getBaseName(notePath) + "_" + UUID.randomUUID().toString() + AUDIO_FILE_EXTENSION;
			String fullFilePath = localPath + filename;
			File file = new File(fullFilePath);
			FileUtils.touch(file);
			byte[] data = IOUtils.toByteArray(inputStream);
			FileUtils.writeByteArrayToFile(file, data);
			
			return filename;
		}
		catch (Exception e) {
			Log.error(e.getMessage());
			throw new eOSCESyncException("", e.getMessage());
		}	
	}

	private void importTextualNotes(ch.unibas.medizin.osce.server.importanswer.Notes notes, Student osceStudent, Doctor doctor, OscePostRoom oscePostRoom) throws eOSCESyncException {
		try
		{								
			OsceDay osceDay = oscePostRoom.getOscePost().getOsceSequence().getOsceDay();
			
			Notes osceNotes = new Notes();
			osceNotes.setStudent(osceStudent);
			osceNotes.setDoctor(doctor);
			osceNotes.setOscePostRoom(oscePostRoom);
			osceNotes.setOsceDay(osceDay);
			osceNotes.setComment(notes.getNoteText());
			osceNotes.setNoteType(NoteType.TEXTUAL);
			if (notes.getTimestamp() != null)
				osceNotes.setLastviewed(jsonsdf.parse(notes.getTimestamp()));
			
			osceNotes.persist();
		}
		catch (Exception e) {
			throw new eOSCESyncException("",e.getMessage());
		}
	}

	public void importiOSCESignature(ch.unibas.medizin.osce.server.importanswer.Signature signature, Doctor doctor, OscePostRoom oscePostRoom, BucketInformation bucketInformation, ChannelSftp channelSftp, String dir) throws eOSCESyncException {
		try
		{
			byte[] signatureImage = null;
			if (BucketInfoType.S3.equals(bucketInformation.getType())) {
				signatureImage = copySignatureFileFromS3(bucketInformation, signature.getSignaturePath());
			}
			else if (BucketInfoType.FTP.equals(bucketInformation.getType())) {
				signatureImage = copySignatureFileFromSFTP(channelSftp, signature.getSignaturePath(), doctor.getId(), dir);
			}
			
			if (signatureImage != null && signatureImage.length > 0) {
				Signature newSignature = new Signature();
				newSignature.setDoctor(doctor);
				newSignature.setOscePost(oscePostRoom.getOscePost());
				
				if (signature.getTimestamp() != null) {
					newSignature.setSignatureTimestamp(jsonsdf.parse(signature.getTimestamp()));
				}	
					
				if (oscePostRoom.getOscePost() != null && oscePostRoom.getOscePost().getOsceSequence() != null)
					newSignature.setOsceDay(oscePostRoom.getOscePost().getOsceSequence().getOsceDay());
				
				if (signatureImage != null) {
					newSignature.setSignatureImage(signatureImage);
				}
				
				newSignature.persist();
			}
			else {
				Log.error("Signature not found");
			}
				
		}
		catch(Exception e)
		{
			Log.error(e.getMessage());
			throw new eOSCESyncException("",e.getMessage());
		}
	}

	private byte[] copySignatureFileFromSFTP(ChannelSftp channelSftp, String signaturePath, Long doctorId,  String dir) throws eOSCESyncException {
		try {	
		
			String fullSingaturePath = dir + doctorId + "/" + signaturePath;
				
			InputStream inputStream = channelSftp.get(fullSingaturePath);
			return IOUtils.toByteArray(inputStream);
		}
		catch (SftpException e) {
			Log.error(e.getMessage(), e);
			throw new eOSCESyncException("",e.getMessage());
		}			
		catch(Exception ex){
			Log.error(ex.getMessage(), ex);
			throw new eOSCESyncException("",ex.getMessage());
		}
	}

	private byte[] copySignatureFileFromS3(BucketInformation bucketInformation, String signaturePath) throws eOSCESyncException {
		try {
			String accessKey = "";
			String secretKey = "";
			String bucketName = "";
			
			if (bucketInformation != null)
			{
				accessKey = bucketInformation.getAccessKey() == null ? "" : bucketInformation.getAccessKey();
				secretKey = bucketInformation.getSecretKey() == null ? "" : bucketInformation.getSecretKey();
				bucketName = bucketInformation.getBucketName() == null ? "" : bucketInformation.getBucketName();
			}
			
			AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
			AmazonS3Client client;
			
			Properties properties =  new Properties();
			properties.load(OsMaFilePathConstant.class.getResourceAsStream("/META-INF/spring/proxy.properties"));			
			String proxyHost = properties.getProperty("proxy.host");
			String proxyPort = properties.getProperty("proxy.port");
			
			if (StringUtils.isNotBlank(proxyHost) && StringUtils.isNotBlank(proxyPort))
			{
				ClientConfiguration configuration = new ClientConfiguration();
				configuration.setProtocol(Protocol.HTTP);
				configuration.setProxyHost(proxyHost);
				configuration.setProxyPort(Integer.parseInt(proxyPort));
				client = new AmazonS3Client(credentials, configuration);
			}
			else  
			{
				client = new AmazonS3Client(credentials);			
			}	
			
			S3Object object = null;			
			object = client.getObject(new GetObjectRequest(bucketName, signaturePath));
			InputStream inputStream = object.getObjectContent();
			return IOUtils.toByteArray(inputStream);
		}
		catch (Exception e) {
			Log.error(e.getMessage());
			throw new eOSCESyncException("", e.getMessage());
		}	
	}

	/*public Boolean fileExist(String fileName)
	{
		String cntxt = appUploadDirectory;
		cntxt = cntxt + fileName;
		
		File folder = new File(cntxt);
		
		return folder.exists();
	}*/
	
	public void importFileList(ExportOsceType osceType, Long semesterID, List<String> fileList, Boolean flag) throws eOSCESyncException
	{
		try
		{
			Semester semester = Semester.findSemester(semesterID);
			String path = OsMaFilePathConstant.IMPORT_PROCESSED_EOSCE_PATH + semester.getSemester() + semester.getCalYear();
	    	if (ExportOsceType.EOSCE.equals(osceType)) {
	    		path = path + OsMaFilePathConstant.EXPORT_EOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);	
	    	}
	    	else if (ExportOsceType.IOSCE.equals(osceType)) {
	    		path = path + OsMaFilePathConstant.EXPORT_IOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
	    	}
	    	
			String accessKey = "";
			String secretKey = "";
			String bucketName = "";
			String encryptionKey = "";
			
			BucketInformation bucketInformation = BucketInformation.findBucketInformationBySemesterForImport(semesterID);
			
			if (bucketInformation != null)
			{
				accessKey = bucketInformation.getAccessKey() == null ? "" : bucketInformation.getAccessKey();
				secretKey = bucketInformation.getSecretKey() == null ? "" : bucketInformation.getSecretKey();
				bucketName = bucketInformation.getBucketName() == null ? "" : bucketInformation.getBucketName();
				encryptionKey = bucketInformation.getEncryptionKey() == null ? "" : bucketInformation.getEncryptionKey();
			}
			
			AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
			AmazonS3Client client;
			
			Properties properties =  new Properties();
			properties.load(OsMaFilePathConstant.class.getResourceAsStream("/META-INF/spring/proxy.properties"));			
			String proxyHost = properties.getProperty("proxy.host");
			String proxyPort = properties.getProperty("proxy.port");
			
			if (StringUtils.isNotBlank(proxyHost) && StringUtils.isNotBlank(proxyPort))
			{
				ClientConfiguration configuration = new ClientConfiguration();
				configuration.setProtocol(Protocol.HTTP);
				configuration.setProxyHost(proxyHost);
				configuration.setProxyPort(Integer.parseInt(proxyPort));
				client = new AmazonS3Client(credentials, configuration);
			}
			else  
			{
				client = new AmazonS3Client(credentials);			
			}	
			
			S3Object object = null;			
			boolean deleteFlag = true;
			
			for (int i=0; i<fileList.size(); i++)
			{
				object = client.getObject(new GetObjectRequest(bucketName, fileList.get(i)));
				
				 if (FilenameUtils.getExtension(object.getKey()).equals(IMPORT_EOSCE_FILE_EXTENSION)) {
					 if (ExportOsceType.EOSCE.equals(osceType)) {
						//import in answer table is done from add file
						deleteFlag = deleteFlag & addFile(osceType, path, object.getKey(), IOUtils.toByteArray(object.getObjectContent()), secretKey, encryptionKey, semesterID);
					 }
					 else if (ExportOsceType.IOSCE.equals(osceType)) {
						 
					 }		
				 }		
			}
		
			if (flag == true && deleteFlag == true)
			{
				deleteAmzonS3Object(osceType, semesterID, fileList, bucketName, accessKey, secretKey);
			}
		}
		catch(AmazonServiceException ase)
		{
			Log.error(ase.getMessage(),ase);
			throw new eOSCESyncException(ase.getErrorType().toString(),ase.getMessage());
		}
		catch(AmazonClientException ace)
		{
			Log.error(ace.getMessage(),ace);
			throw new eOSCESyncException("",ace.getMessage());
		}
		catch(Exception e)
		{
			Log.error(e.getMessage(),e);
			throw new eOSCESyncException("",e.getMessage());
		}
	}
	
	/*public void importEOSCE(String filename)
	{
		//System.out.println("~~~!!!Import EOSCE");
		try
		{
			//"jdbc:sqlite:E:\\eosceTestData.oscexam"
			filename = "jdbc:sqlite:" + filename;
			Class.forName("org.sqlite.JDBC");
			Connection connection = DriverManager.getConnection(filename);  
			Statement statement = connection.createStatement();
			
			Statement st = connection.createStatement();
			
			//SELECT * FROM candidate WHERE present = 'TRUE'
			//SELECT a.*,c.osmaId FROM candidate c, answer a WHERE a.candidateId = c.id AND c.present = TRUE
			//SELECT * FROM answer WHERE candidateId = candResultSet.getInt(1)
			ResultSet resultset = statement.executeQuery("SELECT a.*,c.osmaId FROM candidate c, answer a WHERE a.candidateId = c.id AND c.present = 'TRUE'");
			
			ResultSet rs;
			
			long candidateId, criteriaId, optionId, questionId, osmaid, oscepostroomid = 0, examinerid = 0;
			long tempcandidateid, tempquestionid, tempexaminerid = 0;
			Answer answerTable;
			
			while (resultset.next())
			{				
				tempcandidateid = resultset.getInt("candidateId");
				rs = st.executeQuery("SELECT osmaId FROM candidate WHERE id = " + tempcandidateid);
				candidateId = rs.getInt("osmaId");
				//System.out.println("OSMAID OF CANDIDATE : " + candidateId);
			
				tempquestionid = resultset.getInt("questionId");
				rs = st.executeQuery("SELECT * FROM question WHERE id = " + tempquestionid);
				questionId = rs.getInt("osmaId");
				//System.out.println("OSMAID OF QUESTION : " + questionId);
			
				osmaid = resultset.getInt("optionId");
				rs = st.executeQuery("SELECT osmaId FROM option WHERE id = " + osmaid);
				optionId = rs.getInt("osmaId");
				//System.out.println("OSMAID OF OPTION : " + optionId);
				
				osmaid = resultset.getInt("criteriaId");
				rs = st.executeQuery("SELECT osmaId FROM criteria WHERE id = " + osmaid);
				if (rs.next())
					criteriaId = rs.getInt("osmaId");
				else
					criteriaId = 0;
				
				//System.out.println("OSMAID OF CRITERIA : " + criteriaId);
				rs = st.executeQuery("SELECT * FROM question WHERE id = " + tempquestionid);
				long temptopicid = rs.getInt("topicId");
				
				rs = st.executeQuery("SELECT * FROM topic WHERE id = " + temptopicid);
				long tempchecklistid = rs.getInt("checklistId");
				
				rs = st.executeQuery("SELECT * FROM examiner WHERE firstname = 'Rath'");
				tempexaminerid = rs.getInt("id");
				examinerid = rs.getInt("osmaId");
				//System.out.println("EXAMINER ID : " + tempexaminerid);				
				
				rs = st.executeQuery("SELECT osmaId FROM station WHERE candidateId = " + tempcandidateid + " AND checklistId = " + tempchecklistid);
				if (rs.next())
				{
					oscepostroomid = rs.getInt("osmaId");
					tempexaminerid = rs.getInt("examinerId");
					//System.out.println("OSMAID OF OSCEPOSTROOM : " + oscepostroomid);
				}
				
				rs = st.executeQuery("SELECT * FROM examiner WHERE id = " + tempexaminerid);
				examinerid = rs.getInt("osmaId");
				//System.out.println("OSMAID OF EXAMINER : " + examinerid);
							
				Student stud = Student.findStudent(candidateId);
				ChecklistQuestion checklistQuestion = ChecklistQuestion.findChecklistQuestion(questionId);
				ChecklistOption checklistOption = ChecklistOption.findChecklistOption(optionId);
								
				OscePostRoom oscePostRoom = OscePostRoom.findOscePostRoom(oscepostroomid);
				Doctor doctor = Doctor.findDoctor(examinerid);				
			
				answerTable = new Answer();
				answerTable.setAnswer(resultset.getString("answer"));
				answerTable.setStudent(stud);
				answerTable.setChecklistQuestion(checklistQuestion);
				answerTable.setChecklistOption(checklistOption);				
				if (criteriaId != 0)
				{
				    ChecklistCriteria checklistCriteria = ChecklistCriteria.findChecklistCriteria(criteriaId);
				    answerTable.setChecklistCriteria(checklistCriteria);
				}				
				answerTable.setOscePostRoom(oscePostRoom);
				answerTable.setDoctor(doctor);
				answerTable.persist();
				
				//System.out.println("RECORD INSERTED SUCCESSFULLY");		
			}
		}
		catch(Exception e)
		{
			Log.error(e.getMessage());
		}
		
	}*/
	
	@Transactional
	public boolean importEOSCE(String filename)
	{
		//System.out.println("~~~!!!Import EOSCE");
		try
		{
			//"jdbc:sqlite:E:\\eosceTestData.oscexam"
			filename = "jdbc:sqlite:" + filename;
			Class.forName("org.sqlite.JDBC");
			Connection connection = DriverManager.getConnection(filename);  
			Statement statement = connection.createStatement();
			//Statement statement2 = connection.createStatement();
			
			/*String sql = "SELECT  cand.zcandidateid, ex.zexaminerid, que.zquestionid, opt.zvalue, st.zstationid, ans.zanswerquestion, ans.ztimestamp, ans.zanswerassessment FROM zanswer ans , zassessment ass, zschedule sch, z_1answeroptions ansopt, zoption opt, zcandidate cand, zquestion que, zstation st, zexaminer ex"
					+ " WHERE ans.zanswerassessment = ass.z_pk"
					+ " and  ass.zschedule = sch.z_pk"
					+ " and ansopt.z_1optionanswers = ans.z_pk"
					+ " and opt.z_pk = ansopt.z_8answeroptions"
					+ " and sch.zcandidate = cand.z_pk"
					+ " and sch.zstation = st.z_pk"
					+ " and st.zexaminer = ex.z_pk"
					+ " and ans.zanswerquestion = que.z_pk"
					+ " order by sch.zcandidate asc";*/
			
			/*String sql = "SELECT cand.zcandidateid, ex.zexaminerid, que.zquestionid, opt.zvalue, st.zstationid, ans.zanswerquestion, ans.ztimestamp, ans.zanswerassessment FROM zanswer ans , zassessment ass, zschedule sch, zoption opt, zcandidate cand, zquestion que, zstation st, zexaminer ex"
					+ " WHERE ans.zanswerassessment = ass.z_pk"
					+ " and  ass.zschedule = sch.z_pk"
					+ " and opt.z_pk = ans.zansweroption"
					+ " and sch.zcandidate = cand.z_pk"
					+ " and sch.zstation = st.z_pk"
					+ " and st.zexaminer = ex.z_pk"
					+ " and ans.zanswerquestion = que.z_pk"
					+ " order by sch.zcandidate asc";*/
			
			Map<Long, Set<Long>> answerCriteriaMap = fetchAnswerCriteria(statement);
			
			String sql = "SELECT ans.z_pk, cand.zcandidateid, ex.zexaminerid, que.zquestionid, opt.zvalue, st.zstationid, ans.zanswerquestion, datetime(ztimestamp+978307200.0, 'unixepoch', 'localtime') as d, ans.zanswerassessment FROM zanswer ans, zassessment ass, zschedule sch, zoption opt, zcandidate cand, zquestion que, zstation st, zexaminer ex"
					+ " WHERE ans.zanswerassessment = ass.z_pk"
					+ " and ass.zschedule = sch.z_pk"
					+ " and opt.z_pk = ans.zansweroption"
					+ " and sch.zcandidate = cand.z_pk"
					+ " and sch.zstation = st.z_pk"
					+ " and st.zexaminer = ex.z_pk"
					+ " and ans.zanswerquestion = que.z_pk"
					+ " order by sch.zcandidate asc";
					
			ResultSet resultset = statement.executeQuery(sql);
			
			long candidateId, questionId, roomid = 0, examinerid = 0;
			String optionvalue;
			Answer answerTable = null;
			 
			while (resultset.next())
			{		
				long ansId = Long.parseLong(resultset.getString(1));
				
				candidateId = Long.parseLong(resultset.getString(2));
				examinerid = Long.parseLong(resultset.getString(3));
				questionId = Long.parseLong(resultset.getString(4));
				optionvalue = String.valueOf((int)resultset.getFloat(5));
				String oprId = StringUtils.substring(resultset.getString(6), 0, 4);
				roomid = Long.parseLong(oprId);
				
				new Answer().deleteAnswerAndCriteria(examinerid, roomid, candidateId);
				
				Student stud = Student.findStudent(candidateId);
				//ChecklistQuestion checklistQuestion = ChecklistQuestion.findChecklistQuestion(questionId);
				ChecklistItem checklistItem = ChecklistItem.findChecklistItem(questionId);
				
				//OscePostRoom oscePostRoom = OscePostRoom.findOscePostRoomByRoomAndStudent(candidateId, roomid);
				OscePostRoom oscePostRoom = OscePostRoom.findOscePostRoom(roomid);
				Doctor doctor = Doctor.findDoctor(examinerid);
				ChecklistOption checklistOption = ChecklistOption.findChecklistOptionByValueAndQuestion(questionId, String.valueOf(optionvalue));
				
				answerTable = new Answer();
				answerTable.setAnswer(String.valueOf(optionvalue));
				answerTable.setStudent(stud);
				//answerTable.setChecklistQuestion(checklistQuestion);
				answerTable.setChecklistItem(checklistItem);
				answerTable.setChecklistOption(checklistOption);							
				answerTable.setOscePostRoom(oscePostRoom);
				answerTable.setDoctor(doctor);
				answerTable.setAnswerTimestamp(sdf.parse(resultset.getString("d")));
				answerTable.persist();
				
				if (answerCriteriaMap.containsKey(ansId) == true)
				{
					Set<Long> criteriaIdList = answerCriteriaMap.get(ansId);
					
					for (Long criteriaId : criteriaIdList)
					{
						ChecklistCriteria checklistCriteria = ChecklistCriteria.findChecklistCriteria(criteriaId);
						if (checklistCriteria != null) {
							AnswerCheckListCriteria answerChecklistCriteria = new AnswerCheckListCriteria();
							answerChecklistCriteria.setChecklistCriteria(checklistCriteria);
							answerChecklistCriteria.setAnswer(answerTable);
							answerChecklistCriteria.persist();
						}
					}
				}
			}
			
			importNotes(statement);
			importSignature(statement);
			
			resultset.close();
			statement.close();
			connection.close();
			
			//FileUtils.deleteQuietly(new File(filename));
			File file = new File(filename);
			if (file.exists() == true) { 
				file.delete();
			}
			
			return true;
		}
		catch(Exception e)
		{
			Log.error(e.getMessage());
			return false;
		}
		
	}

	private Map<Long, Set<Long>> fetchAnswerCriteria(Statement statement) {
		Map<Long, Set<Long>> ansCriteriaMap = new HashMap<Long, Set<Long>>();
		
		try
		{
			String sql = "select ac.z_1criteriaanswers,c.zcriteriaid from z_1answercriterias ac, zcriteria c where ac.z_6answercriterias = c.z_pk";
			ResultSet resultSet = statement.executeQuery(sql);
			
			while (resultSet.next())
			{
				long key = Long.parseLong(resultSet.getString(1));
				long value = Long.parseLong(resultSet.getString(2));
				putToMap(key, value, ansCriteriaMap);				
			}
			
			return ansCriteriaMap;
		}
		catch(Exception e)
		{
			Log.error(e.getMessage(),e);
			return ansCriteriaMap;
		}
		
	}

	private void putToMap(long key, long value, Map<Long, Set<Long>> map) {
		if (map.containsKey(key) == true)
		{
			map.get(key).add(value);
		}
		else
		{
			Set<Long> valueSet = new HashSet<Long>();
			valueSet.add(value);
			map.put(key, valueSet);
		}
	}
	
	private void importNotes(Statement statement) {
		try
		{			
			String sql = "SELECT c.zcandidateid, st.zstationid, ex.zexaminerid, datetime(a.zlastviewed+978307200.0, 'unixepoch', 'localtime') as d , a.znotes from zassessment a, zschedule sc, zcandidate c, zstation st, zexaminer ex"
					+ " WHERE sc.zassessment = a.z_pk AND" 
					+ " c.z_pk = sc.zcandidate AND"
					+ " sc.zstation = st.z_pk AND"
					+ " st.zexaminer = ex.z_pk AND"						
					+ " c.zfirstname <> '%BREAK%'"
					+ " ORDER BY c.zcandidateid";
					
			ResultSet resultset = statement.executeQuery(sql);
			
			long candidateId = 0, examinerid = 0, roomid = 0;
			String comment = "", stationId;
			 
			while (resultset.next())
			{	
				if (resultset.getString(1) != null && (resultset.getString(1).isEmpty() == false || resultset.getString(1).equals("") == false))
				{
					candidateId = Long.parseLong(resultset.getString(1));
					stationId = resultset.getString(2);
					examinerid = Long.parseLong(resultset.getString(3));
					comment = resultset.getString(5);
					String[] str = StringUtils.split(stationId, "-");
					
					new Answer().deleteTextualNote(examinerid, roomid, candidateId);
					new Answer().deleteAudioNote(examinerid, roomid, candidateId);
					
					if (str.length == 0)
						roomid = Long.parseLong(stationId);
					else if (str.length > 0)
						roomid = Long.parseLong(str[0]);
					else
						throw new IllegalArgumentException("No station id found");
					
					Student student = Student.findStudent(candidateId);
					Doctor doctor = Doctor.findDoctor(examinerid);
					OscePostRoom oscePostRoom =  OscePostRoom.findOscePostRoom(roomid); //OscePostRoom.findOscePostRoomByRoomAndStudent(candidateId, roomid); //OscePostRoom.findOscePostRoom(roomid);
										
					OsceDay osceDay = oscePostRoom.getOscePost().getOsceSequence().getOsceDay();
					
					Notes notes = new Notes();
					notes.setStudent(student);
					notes.setDoctor(doctor);
					notes.setOscePostRoom(oscePostRoom);
					notes.setOsceDay(osceDay);
					notes.setComment(comment);
					notes.setNoteType(NoteType.TEXTUAL);
					
					if (resultset.getString("d") != null)
						notes.setLastviewed(sdf.parse(resultset.getString("d")));
					
					notes.persist();
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void importSignature(Statement statement) {
		try
		{
			String sql = "select ex.zexaminerid, sig.zimage, st.zstationid from zsignature sig, zstation st, zexaminer ex" 
					+ " where sig.zexaminer = st.zexaminer"
					+ " and ex.z_pk = sig.zexaminer";

			ResultSet resultSet = statement.executeQuery(sql);
			
			Long examinerId, oscePostRoomId;
			
			while(resultSet.next())
			{
				examinerId = Long.parseLong(resultSet.getString(1));
				String oprId = StringUtils.substring(resultSet.getString(3), 0, 4);
				oscePostRoomId = Long.parseLong(oprId);
				
				new Signature().deleteSignature(examinerId, oscePostRoomId);
				
				OscePostRoom oscePostRoom = OscePostRoom.findOscePostRoom(oscePostRoomId);
				Doctor examiner = Doctor.findDoctor(examinerId);
				
				Signature signature = new Signature();
				signature.setDoctor(examiner);
				signature.setOscePost(oscePostRoom.getOscePost());
				
				if (oscePostRoom.getOscePost() != null && oscePostRoom.getOscePost().getOsceSequence() != null)
					signature.setOsceDay(oscePostRoom.getOscePost().getOsceSequence().getOsceDay());
				
				signature.setSignatureImage(resultSet.getBytes(2));
				signature.persist();
			}
		}
		catch(Exception e)
		{
			Log.error(e.getMessage());
		}
		
	}

	//export
	
	public void exportOsceFile1(Long semesterID)
	{
		//System.out.println("~~SEMESTER ID : " + semesterID);
		String fileName = "";
		int timeslot = 0;
		String xml;
		int parcourCount = 1;
		
		try
		{
			OsceConstantsWithLookup constants = GWTI18N.create(OsceConstantsWithLookup.class);
			
			List<Osce> osceList = Osce.findAllOsceBySemster(semesterID);
					
			for (int i=0; i<osceList.size(); i++)
			{
				parcourCount = 1;
				
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
				Document doc = docBuilder.newDocument();
				
				Element osceDataElement = doc.createElement("oscedata");
				//osceDataElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
				osceDataElement.setAttribute("version", "1.1");
				doc.appendChild(osceDataElement);
				
				
				Element examElement = doc.createElement("exam");
				osceDataElement.appendChild(examElement);
				
				//examElement.setAttribute("id", osceList.get(i).getId().toString());
				//examElement.setAttribute("name", osceList.get(i).getName() == null ? "" : osceList.get(i).getName());
				
				BucketInformation bucketInformation = BucketInformation.findBucketInformationBySemesterForExport(osceList.get(i).getSemester().getId());
				
				
				Element cloudElement = doc.createElement("credentials");
				examElement.appendChild(cloudElement);
				if(bucketInformation == null || bucketInformation.getType() == null) {
					cloudElement.setAttribute("type", BucketInfoType.S3.getStringValue());
				}else {
					cloudElement.setAttribute("type", bucketInformation.getType().getStringValue());	
				}
				
				if(bucketInformation != null && BucketInfoType.FTP.equals(bucketInformation.getType())) {
					cloudElement.setAttribute("basePath", bucketInformation.getBasePath());
				}
				
				Element bucketElement = doc.createElement("host");
				bucketElement.appendChild(doc.createTextNode(bucketInformation == null ? "" : (bucketInformation.getBucketName() == null ? "" : bucketInformation.getBucketName().toString())));
				cloudElement.appendChild(bucketElement);
				
				Element accKeyElement = doc.createElement("user");
				accKeyElement.appendChild(doc.createTextNode(bucketInformation == null ? "" : (bucketInformation.getAccessKey() == null ? "" : bucketInformation.getAccessKey().toString())));
				cloudElement.appendChild(accKeyElement);
				
				Element secretKeyElement = doc.createElement("password");
				secretKeyElement.appendChild(doc.createTextNode(bucketInformation == null ? "" : (bucketInformation.getSecretKey() == null ? "" : bucketInformation.getSecretKey().toString())));
				cloudElement.appendChild(secretKeyElement);
				
				Element encryptionKeyElement = doc.createElement("encryptionKey");
				encryptionKeyElement.appendChild(doc.createTextNode(bucketInformation == null ? "" : (bucketInformation.getEncryptionKey() == null ? "" : bucketInformation.getEncryptionKey().toString())));
				cloudElement.appendChild(encryptionKeyElement);
				
				checkList(osceList.get(i).getId(),doc,examElement);
				examinors(osceList.get(i).getId(),doc,examElement);
				students(osceList.get(i).getId(),doc,examElement);
				
				timeslot = osceList.get(i).getOscePostBlueprints().size();
				
				List<OsceDay> osceDayList = OsceDay.findOsceDayByOsce(osceList.get(i).getId());
				int startrotation = 0;
				int totalrotation = 0;
				
				//System.out.println("OSCEDAY LIST : " + osceDayList.size());
				
				/*Element daysElement = doc.createElement("days");
				examElement.appendChild(daysElement);*/
				
				for (int j=0; j<osceDayList.size(); j++)
				{
					/*Element dayElement = doc.createElement("day");
					daysElement.appendChild(dayElement);
					dayElement.setAttribute("id", osceDayList.get(j).getId().toString());
					
					Element parcoursElement = doc.createElement("parcours");
					dayElement.appendChild(parcoursElement);*/
					
					//System.out.println("DAY ID : " + osceDayList.get(i));
					List<OsceSequence> sequenceList = OsceSequence.findOsceSequenceByOsceDay(osceDayList.get(j).getId());
					//System.out.println("SEQUENCE LIST : " + sequenceList.size());
				
					/*if (sequenceList.size() == 1){
						totalrotation = sequenceList.get(0).getNumberRotation();
					}
					else if (sequenceList.size() == 2){
						totalrotation = sequenceList.get(0).getNumberRotation() + sequenceList.get(1).getNumberRotation();
					}*/
					
					for (int k=0; k<sequenceList.size(); k++)
					{
						startrotation = totalrotation;
						totalrotation = totalrotation + sequenceList.get(k).getNumberRotation();
						
						List<Course> courseList = sequenceList.get(k).getCourses();
						
						for (Course course : courseList)
						{
							/*Element parcourElement = doc.createElement("parcour");
							parcoursElement.appendChild(parcourElement);
							
							parcourElement.setAttribute("id", course.getId().toString());
							parcourElement.setAttribute("color", constants.getString(course.getColor()));
							parcourElement.setAttribute("sequence", String.format("%02d", parcourCount));*/
							
							parcourCount += 1;
							
							Element rotationsElement = doc.createElement("rotations");
							//parcourElement.appendChild(rotationsElement);
							examElement.appendChild(rotationsElement);
							
							for (int l=startrotation; l<totalrotation; l++)
							{	
								Element rotationElement = doc.createElement("rotation");
								rotationsElement.appendChild(rotationElement);
								
								rotationElement.setAttribute("sequence", String.format("%02d", (l+1)));
								rotationElement.setAttribute("id", ("r" + String.format("%02d", (l+1)) + "p" + course.getId()));
								rotationElement.setAttribute("title", ("Rotation " + String.format("%02d", (l+1)) + " " + constants.getString(course.getColor())));
								
								Element oscePostRoomsEle = doc.createElement("stations");
								rotationElement.appendChild(oscePostRoomsEle);
								
								List<OscePostRoom> oscePostRoomList = OscePostRoom.findOscePostRoomByCourseID(course.getId());
								
								for (OscePostRoom oscePostRoom : oscePostRoomList)
								{
									Element oscePostRoomElement = doc.createElement("station");
									oscePostRoomsEle.appendChild(oscePostRoomElement);
									
									oscePostRoomElement.setAttribute("id", oscePostRoom.getId().toString());
									oscePostRoomElement.setAttribute("title", ""); //TODO need to ask
									
									if (oscePostRoom.getRoom() == null)
										oscePostRoomElement.setAttribute("isBreakStation", "yes");
									else 
										oscePostRoomElement.setAttribute("isBreakStation", "no");
									
									/*Element roomElement = doc.createElement("room");
									oscePostRoomElement.appendChild(roomElement);
									
									Element roomIdElement = doc.createElement("id");
									roomIdElement.appendChild(doc.createTextNode(oscePostRoom.getRoom() == null ? "" : oscePostRoom.getRoom().getId().toString()));
									roomElement.appendChild(roomIdElement);
									
									Element roomNumElement = doc.createElement("number");
									roomNumElement.appendChild(doc.createCDATASection(oscePostRoom.getRoom() == null ? "" : (oscePostRoom.getRoom().getRoomNumber() == null ? "" : oscePostRoom.getRoom().getRoomNumber())));
									roomElement.appendChild(roomNumElement);*/
									
									List<Assignment> assignmentlist = Assignment.findAssignmentByOscePostRoom(oscePostRoom.getId(), osceList.get(i).getId(), l);
									
									List<Assignment> examinerAssList = new ArrayList<Assignment>();
									
									if (assignmentlist.size() > 0)
									{
										Date timestart = assignmentlist.get(0).getTimeStart();
										Date timeend = assignmentlist.get(assignmentlist.size()-1).getTimeEnd();
										examinerAssList = Assignment.findAssignmentExamnierByOscePostRoom(oscePostRoom.getId(), osceList.get(i).getId(), timestart, timeend);
									}
									
									/*Element examinersElement = doc.createElement("examiners");
									oscePostRoomElement.appendChild(examinersElement);*/
									
									for (Assignment examinerAss : examinerAssList)
									{
										if (examinerAss.getExaminer() != null)
										{
											Element examinerEle = doc.createElement("examiner");
											oscePostRoomElement.appendChild(examinerEle);
											
											examinerEle.setAttribute("id", examinerAss.getExaminer().getId().toString());
											break;
											/*Element examinerIdElement = doc.createElement("id");
											examinerIdElement.appendChild(doc.createTextNode(examinerAss.getExaminer().getId().toString()));
											examinerEle.appendChild(examinerIdElement);*/
											
											
											
											/*Element examinerFirstnameElement = doc.createElement("firstname");
											examinerFirstnameElement.appendChild(doc.createCDATASection(examinerAss.getExaminer().getPreName() == null ? "" : examinerAss.getExaminer().getPreName()));
											examinerEle.appendChild(examinerFirstnameElement);
											
											Element examinerlastnameElement = doc.createElement("lastname");
											examinerlastnameElement.appendChild(doc.createCDATASection(examinerAss.getExaminer().getName() == null ? "" : examinerAss.getExaminer().getName()));
											examinerEle.appendChild(examinerlastnameElement);
											
											Element examinerPhoneElement = doc.createElement("phone");
											examinerPhoneElement.appendChild(doc.createCDATASection(examinerAss.getExaminer().getTelephone() == null ? "" : examinerAss.getExaminer().getTelephone()));
											examinerEle.appendChild(examinerPhoneElement);*/
											
										}
									}
									
									OscePost oscepost = OscePost.findOscePost(oscePostRoom.getOscePost().getId());
									
									if (oscepost.getStandardizedRole() != null)
									{
										StandardizedRole standardizedRole = StandardizedRole.findStandardizedRole(oscepost.getStandardizedRole().getId());
										CheckList checklist = CheckList.findCheckList(standardizedRole.getCheckList().getId());
										
										Element checkListEle = doc.createElement("checklist");
										oscePostRoomElement.appendChild(checkListEle);
										checkListEle.setAttribute("id", checklist.getId().toString());
										
										/*Element checkListIdElement = doc.createElement("id");
										checkListIdElement.appendChild(doc.createTextNode(checklist.getId().toString()));
										checkListEle.appendChild(checkListIdElement);*/
										
										/*Element checkListTitleEle = doc.createElement("title");
										checkListTitleEle.appendChild(doc.createCDATASection(checklist.getTitle() == null ? "" : checklist.getTitle()));
										checkListEle.appendChild(checkListTitleEle);
										
										Element checkListTopicsEle = doc.createElement("checklisttopics");
										checkListEle.appendChild(checkListTopicsEle);
										
										List<ChecklistTopic> checklistTopicList = checklist.getCheckListTopics();
										
										int ctr = 1;
										
										for (ChecklistTopic checklistTopic : checklistTopicList)
										{
											Element checkListTopicElement = doc.createElement("checklisttopic");
											checkListTopicsEle.appendChild(checkListTopicElement);
											
											Element checkListTopicIdElement = doc.createElement("id");
											checkListTopicIdElement.appendChild(doc.createTextNode(checklistTopic.getId().toString()));
											checkListTopicElement.appendChild(checkListTopicIdElement);
											
											Element checkListTopicTitleEle = doc.createElement("title");
											checkListTopicTitleEle.appendChild(doc.createCDATASection(checklistTopic.getTitle() == null ? "" : checklistTopic.getTitle()));
											checkListTopicElement.appendChild(checkListTopicTitleEle);
											
											Element checkListTopicDescEle = doc.createElement("description");
											checkListTopicDescEle.appendChild(doc.createCDATASection(checklistTopic.getDescription() == null ? "" : checklistTopic.getDescription()));
											checkListTopicElement.appendChild(checkListTopicDescEle);
											
											Element checkListQuestionElement = doc.createElement("checklistquestions");
											checkListTopicElement.appendChild(checkListQuestionElement);
											
											List<ChecklistQuestion> checklistQuestionsList = checklistTopic.getCheckListQuestions();
											
											for (ChecklistQuestion checklistQuestion : checklistQuestionsList)
											{
												Element checkListQueElement = doc.createElement("checklistquestion");
												checkListQuestionElement.appendChild(checkListQueElement);
												
												Element checkListQueIdElement = doc.createElement("id");
												checkListQueIdElement.appendChild(doc.createTextNode(checklistQuestion.getId().toString()));
												checkListQueElement.appendChild(checkListQueIdElement);
												
												Element checkListQuestEle = doc.createElement("question");
												checkListQuestEle.appendChild(doc.createCDATASection(checklistQuestion.getQuestion() == null ? "" : checklistQuestion.getQuestion()));
												checkListQueElement.appendChild(checkListQuestEle);
												
												Element checkListQueInstEle = doc.createElement("instruction");
												checkListQueInstEle.appendChild(doc.createCDATASection(checklistQuestion.getInstruction() == null ? "" : checklistQuestion.getInstruction()));
												checkListQueElement.appendChild(checkListQueInstEle);
												
												Element checkListQueIsOverall = doc.createElement("key");
												checkListQueIsOverall.appendChild(doc.createTextNode("isOverallQuestion"));
												checkListQueElement.appendChild(checkListQueIsOverall);
												Element checkListQueIsOverallVal = doc.createElement((checklistQuestion.getIsOveralQuestion() == null ? "false" : checklistQuestion.getIsOveralQuestion().toString()));					
												checkListQueElement.appendChild(checkListQueIsOverallVal);
												
												Element checkListQueSeqNoElement = doc.createElement("sequencenumber");
												checkListQueSeqNoElement.appendChild(doc.createTextNode(String.valueOf(ctr)));
												checkListQueElement.appendChild(checkListQueSeqNoElement);
												
												ctr++;
												
												Element checkListCriteriaElement = doc.createElement("checklistcriterias");
												checkListQueElement.appendChild(checkListCriteriaElement);
												
												Iterator<ChecklistCriteria> criiterator = checklistQuestion.getCheckListCriterias().iterator();
												
												while (criiterator.hasNext())
												{
													ChecklistCriteria criteria = criiterator.next();
												
													Element criteriaElement = doc.createElement("checklistcriteria");
													checkListCriteriaElement.appendChild(criteriaElement);
													
													Element criteriaIdElement = doc.createElement("id");
													criteriaIdElement.appendChild(doc.createTextNode(criteria.getId().toString()));
													criteriaElement.appendChild(criteriaIdElement);
													
													Element criteriaTitleEle = doc.createElement("title");
													criteriaTitleEle.appendChild(doc.createCDATASection(criteria.getCriteria() == null ? "" : criteria.getCriteria()));
													criteriaElement.appendChild(criteriaTitleEle);
													
													Element criteriaSeqNoEle = doc.createElement("sequencenumber");
													criteriaSeqNoEle.appendChild(doc.createTextNode(criteria.getSequenceNumber() == null ? "" : criteria.getSequenceNumber().toString()));
													criteriaElement.appendChild(criteriaSeqNoEle);
												}
												
												Element checkListOptionElement = doc.createElement("checklistoptions");
												checkListQueElement.appendChild(checkListOptionElement);
												
												Iterator<ChecklistOption> opitr = checklistQuestion.getCheckListOptions().iterator();
												
												while (opitr.hasNext())
												{
													ChecklistOption option = opitr.next();
													
													Element optionElement = doc.createElement("checklistoption");
													checkListOptionElement.appendChild(optionElement);
													
													Element optionIdElement = doc.createElement("id");
													optionIdElement.appendChild(doc.createTextNode(option.getId().toString()));
													optionElement.appendChild(optionIdElement);
													
													Element optionTitleEle = doc.createElement("title");
													optionTitleEle.appendChild(doc.createCDATASection(option.getOptionName() == null ? "" : option.getOptionName()));
													optionElement.appendChild(optionTitleEle);
													
													Element optionValElement = doc.createElement("value");
													optionValElement.appendChild(doc.createTextNode(option.getValue() == null ? "" : option.getValue().toString()));
													optionElement.appendChild(optionValElement);
													
													Element instructionElement = doc.createElement("instruction");
													instructionElement.appendChild(doc.createCDATASection(option.getInstruction() == null ? "" : option.getInstruction()));
													optionElement.appendChild(instructionElement);
													
													Element optionSeqNoElement = doc.createElement("sequencenumber");
													optionSeqNoElement.appendChild(doc.createTextNode(option.getSequenceNumber() == null ? "" : option.getSequenceNumber().toString()));
													optionElement.appendChild(optionSeqNoElement);
													
													Element optionCriteriaCountElement = doc.createElement("criteriacount");
													optionCriteriaCountElement.appendChild(doc.createTextNode((option.getCriteriaCount() == null ? "0" : option.getCriteriaCount().toString())));
													optionElement.appendChild(optionCriteriaCountElement);
												}
											}
										}*/
									}
									
									Element studentElement = doc.createElement("students");
									oscePostRoomElement.appendChild(studentElement);
									
									for (Assignment studAss : assignmentlist)
									{	
										/*if (studAss.getStudent() != null)
										{*/
										Element studElement = doc.createElement("student");
										studentElement.appendChild(studElement);
										
										if (studAss.getStudent() == null)
										{
											studElement.setAttribute("isBreakCandidate", "yes");
										}
										else
										{
											if (oscePostRoom.getRoom() == null)
												studElement.setAttribute("isBreakCandidate", "yes");
											else
												studElement.setAttribute("isBreakCandidate", "no");
										}
										
										String studId = studAss.getStudent() == null ? "" : studAss.getStudent().getId().toString();
										studElement.setAttribute("id", studId);
										
										/*Element studIdElement = doc.createElement("id");
										String studId = studAss.getStudent() == null ? "" : studAss.getStudent().getId().toString();
										studIdElement.appendChild(doc.createTextNode(studId));
										studElement.appendChild(studIdElement);*/
										
										/*Element studFirstNameEle = doc.createElement("firstname");
										String firstName = studAss.getStudent() == null ? ("S" + String.format("%03d", studAss.getSequenceNumber())) : (studAss.getStudent().getPreName() == null ? "" : studAss.getStudent().getPreName()); 
										studFirstNameEle.appendChild(doc.createCDATASection(firstName));
										studElement.appendChild(studFirstNameEle);
										
										Element studlastNameEle = doc.createElement("lastname");
										String lastName = studAss.getStudent() == null ? ("S" + String.format("%03d", studAss.getSequenceNumber())) : (studAss.getStudent().getName() == null ? "" : studAss.getStudent().getName());
										studlastNameEle.appendChild(doc.createCDATASection(lastName));
										studElement.appendChild(studlastNameEle);
										
										Element studEmailEle = doc.createElement("email");
										String email = studAss.getStudent() == null ? "" : (studAss.getStudent().getEmail() == null ? "" : studAss.getStudent().getEmail());
										studEmailEle.appendChild(doc.createCDATASection(email));
										studElement.appendChild(studEmailEle);
										
										Element studStTimeElement = doc.createElement("starttime");
										studStTimeElement.appendChild(doc.createTextNode(studAss.getTimeStart() == null ? "" :studAss.getTimeStart().toString()));
										studElement.appendChild(studStTimeElement);
										
										Element studEndTimeElement = doc.createElement("endtime");
										studEndTimeElement.appendChild(doc.createTextNode(studAss.getTimeEnd() == null ? "" : studAss.getTimeEnd().toString()));
										studElement.appendChild(studEndTimeElement);*/
										//}
									}
								}
								
								List<Assignment> logicalBreakAssignment = Assignment.findAssignmentOfLogicalBreakPostPerRotation(osceDayList.get(j).getId(), course.getId(), l);
								
								if (logicalBreakAssignment != null && logicalBreakAssignment.size() > 0)
								{
									Element logicalOPRElement = doc.createElement("station");
									oscePostRoomsEle.appendChild(logicalOPRElement);
									logicalOPRElement.setAttribute("isBreakStation", "yes");
									
									Element logicalStudentElement = doc.createElement("students");
									logicalOPRElement.appendChild(logicalStudentElement);
									
									for (Assignment assignment : logicalBreakAssignment)
									{
										/*if (assignment.getStudent() != null)
										{*/
										Element studElement = doc.createElement("student");
										logicalStudentElement.appendChild(studElement);
										
										studElement.setAttribute("isBreakCandidate", "yes");
										
										String id = assignment.getStudent() == null ? ("S" + String.format("%03d", assignment.getSequenceNumber())) : assignment.getStudent().getId().toString();							
										studElement.setAttribute("id", id);
										/*Element studIdElement = doc.createElement("id");
										String id = assignment.getStudent() == null ? ("S" + String.format("%03d", assignment.getSequenceNumber())) : assignment.getStudent().getId().toString(); 
										studIdElement.appendChild(doc.createTextNode(id));
										studElement.appendChild(studIdElement);*/
										
										/*Element studFirstNameEle = doc.createElement("firstname");
										String firstName = assignment.getStudent() == null ? ("S" + String.format("%03d", assignment.getSequenceNumber())) : assignment.getStudent().getPreName() == null ? "" : assignment.getStudent().getPreName();
										studFirstNameEle.appendChild(doc.createCDATASection(firstName));
										studElement.appendChild(studFirstNameEle);
										
										Element studlastNameEle = doc.createElement("lastname");
										String lastName = assignment.getStudent() == null ? "" : assignment.getStudent().getName() == null ? "" : assignment.getStudent().getName();
										studlastNameEle.appendChild(doc.createCDATASection(lastName));
										studElement.appendChild(studlastNameEle);
										
										Element studEmailEle = doc.createElement("email");
										String email = assignment.getStudent() == null ? "" : assignment.getStudent().getEmail() == null ? "" : assignment.getStudent().getEmail();
										studEmailEle.appendChild(doc.createCDATASection(email));
										studElement.appendChild(studEmailEle);
										
										Element studStTimeElement = doc.createElement("starttime");
										studStTimeElement.appendChild(doc.createTextNode(assignment.getTimeStart() == null ? "" :assignment.getTimeStart().toString()));
										studElement.appendChild(studStTimeElement);
										
										Element studEndTimeElement = doc.createElement("endtime");
										studEndTimeElement.appendChild(doc.createTextNode(assignment.getTimeEnd() == null ? "" : assignment.getTimeEnd().toString()));
										studElement.appendChild(studEndTimeElement);*/
										//}
									}
								}
							}
						}
					}
				}
				fileName = osceList.get(i).getSemester().getSemester().toString() 
						+ osceList.get(i).getSemester().getCalYear().toString().substring(2, osceList.get(i).getSemester().getCalYear().toString().length()) 
						+ "-" + (constants.getString(osceList.get(i).getStudyYear().toString()).replace(".", "")); 
										
				fileName = fileName + ".osceexchange";
				Semester semester = Semester.findSemester(semesterID);
				String processedFileName = OsMaFilePathConstant.EXPORT_OSCE_PROCESSED_FILEPATH + semester.getSemester() + semester.getCalYear() + 
																													(isLocal==true ? folderSeparatorLocal : folderSeparatorProduction) + fileName;
				File processfile = new File(processedFileName);
				Boolean processCheck = processfile.exists();	
				
				if (!processCheck)
				{
					fileName = fileName.replaceAll(" ", "_");
					fileName = OsMaFilePathConstant.EXPORT_OSCE_UNPROCESSED_FILEPATH + semester.getSemester() + semester.getCalYear() + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction)
								+ fileName;
					File file = new File(fileName);
					Boolean check = file.exists();
					
					if (check)
					{
						file.delete();
					}
					
					FileUtils.touch(file);
					
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					//transformerFactory.setAttribute("indent-number", new Integer(5));
					Transformer transformer = transformerFactory.newTransformer();
					transformer.setOutputProperty(OutputKeys.INDENT, "yes");
					transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
					//transformer.setOutputProperty("indent-amount", "3");
					DOMSource source = new DOMSource(doc);
					StreamResult result = new StreamResult(file);
					transformer.transform(source, result);
					
					//System.out.println("* * *" + file.getName() + " IS CREATED * * *");								
				}
				xml = "";
				fileName = "";
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.error(e.getMessage());
		}
	
	}
	
	private void students(Long osceId, Document doc, Element parcourElement) {
		Element studentElement = doc.createElement("students");
		parcourElement.appendChild(studentElement);
		Set<String> done = new HashSet<String>();
		Set<Assignment> assignmentlist = new HashSet<Assignment>();
		assignmentlist.addAll(Assignment.findAssignmentStudentsByOsce(osceId));
		assignmentlist.addAll(Assignment.findAssignmentOfLogicalBreakPost(osceId));
		
		for (Assignment studAss : assignmentlist)
		{	
			String studIdComp = studAss.getStudent() == null ? "" : studAss.getStudent().getId().toString();
			
			if(done.contains(studIdComp) == false) {
				done.add(studIdComp);
				/*if (studAss.getStudent() != null)
				{*/
				Element studElement = doc.createElement("student");
				studentElement.appendChild(studElement);
				
				String studId = studAss.getStudent() == null ? "" : studAss.getStudent().getId().toString();
				studElement.setAttribute("id", studId);
				
				/*Element studIdElement = doc.createElement("id");
				String studId = studAss.getStudent() == null ? "" : studAss.getStudent().getId().toString();
				studIdElement.appendChild(doc.createTextNode(studId));
				studElement.appendChild(studIdElement);*/
				
				Element studFirstNameEle = doc.createElement("firstname");
				String firstName = studAss.getStudent() == null ? ("S" + String.format("%03d", studAss.getSequenceNumber())) : (studAss.getStudent().getPreName() == null ? "" : studAss.getStudent().getPreName()); 
				studFirstNameEle.appendChild(doc.createCDATASection(firstName));
				studElement.appendChild(studFirstNameEle);
				
				Element studlastNameEle = doc.createElement("lastname");
				String lastName = studAss.getStudent() == null ? ("S" + String.format("%03d", studAss.getSequenceNumber())) : (studAss.getStudent().getName() == null ? "" : studAss.getStudent().getName());
				studlastNameEle.appendChild(doc.createCDATASection(lastName));
				studElement.appendChild(studlastNameEle);
				
				Element studEmailEle = doc.createElement("email");
				String email = studAss.getStudent() == null ? "" : (studAss.getStudent().getEmail() == null ? "" : studAss.getStudent().getEmail());
				studEmailEle.appendChild(doc.createCDATASection(email));
				studElement.appendChild(studEmailEle);
				
				/*Element studStTimeElement = doc.createElement("starttime");
				studStTimeElement.appendChild(doc.createTextNode(studAss.getTimeStart() == null ? "" :studAss.getTimeStart().toString()));
				studElement.appendChild(studStTimeElement);
				
				Element studEndTimeElement = doc.createElement("endtime");
				studEndTimeElement.appendChild(doc.createTextNode(studAss.getTimeEnd() == null ? "" : studAss.getTimeEnd().toString()));
				studElement.appendChild(studEndTimeElement);*/
				//}
			}
		}
	}

	private void examinors(Long osceId, Document doc, Element parcourElement) {
		Element examinersElement = doc.createElement("examiners");
		parcourElement.appendChild(examinersElement);
		
		Set<Doctor> examinerAssList = new HashSet<Doctor>(Assignment.findAssignmentExamnierByOsce(osceId));
		
		for (Doctor examinerAss : examinerAssList)
		{
			
			Element examinerEle = doc.createElement("examiner");
			examinersElement.appendChild(examinerEle);
			
			examinerEle.setAttribute("id", examinerAss.getId().toString());
			/*Element examinerIdElement = doc.createElement("id");
			examinerIdElement.appendChild(doc.createTextNode(examinerAss.getId().toString()));
			examinerEle.appendChild(examinerIdElement);*/
			
			Element examinerSalutationElement = doc.createElement("salutation");
			examinerSalutationElement.appendChild(doc.createCDATASection(examinerAss.getTitle() == null ? "" : examinerAss.getTitle()));
			examinerEle.appendChild(examinerSalutationElement);
			
			Element examinerFirstnameElement = doc.createElement("firstname");
			examinerFirstnameElement.appendChild(doc.createCDATASection(examinerAss.getPreName() == null ? "" : examinerAss.getPreName()));
			examinerEle.appendChild(examinerFirstnameElement);
			
			Element examinerlastnameElement = doc.createElement("lastname");
			examinerlastnameElement.appendChild(doc.createCDATASection(examinerAss.getName() == null ? "" : examinerAss.getName()));
			examinerEle.appendChild(examinerlastnameElement);
			
			Element examinerPhoneElement = doc.createElement("phone");
			examinerPhoneElement.appendChild(doc.createCDATASection(examinerAss.getTelephone() == null ? "" : examinerAss.getTelephone()));
			examinerEle.appendChild(examinerPhoneElement);
				
			
		}		
	}

	private void checkList(Long osceId, Document doc, Element examElement) {
		List<Course> courses = Course.findCourseByOsce(osceId);
		Element checkListsEle = doc.createElement("checklists");
		examElement.appendChild(checkListsEle);
		Set<CheckList> checkLists= new HashSet<CheckList>();
		
		for (Course course : courses) {
			List<OscePostRoom> oscePostRoomList = OscePostRoom.findOscePostRoomByCourseID(course.getId());
			
			for (OscePostRoom oscePostRoom : oscePostRoomList) {
				OscePost oscepost = OscePost.findOscePost(oscePostRoom.getOscePost().getId());
				if (oscepost.getStandardizedRole() != null)
				{
					StandardizedRole standardizedRole = StandardizedRole.findStandardizedRole(oscepost.getStandardizedRole().getId());
					CheckList checklist = CheckList.findCheckList(standardizedRole.getCheckList().getId());
					checkLists.add(checklist);
				}
			}
		}
		
		for (CheckList checklist : checkLists) {
			
				Element checkListEle = doc.createElement("checklist");
				checkListsEle.appendChild(checkListEle);
				
				checkListEle.setAttribute("id", checklist.getId().toString());
				checkListEle.setAttribute("title", checklist.getTitle() == null ? "" : checklist.getTitle());
				
				/*Element checkListIdElement = doc.createElement("id");
				checkListIdElement.appendChild(doc.createTextNode(checklist.getId().toString()));
				checkListEle.appendChild(checkListIdElement);
				
				Element checkListTitleEle = doc.createElement("title");
				checkListTitleEle.appendChild(doc.createCDATASection(checklist.getTitle() == null ? "" : checklist.getTitle()));
				checkListEle.appendChild(checkListTitleEle);*/
				
				Element checkListTopicsEle = doc.createElement("checklisttopics");
				checkListEle.appendChild(checkListTopicsEle);
				
				List<ChecklistTopic> checklistTopicList = checklist.getCheckListTopics();
				
				int ctr = 1;
				
				for (ChecklistTopic checklistTopic : checklistTopicList)
				{
					Element checkListTopicElement = doc.createElement("checklisttopic");
					checkListTopicsEle.appendChild(checkListTopicElement);
					
					checkListTopicElement.setAttribute("id", checklistTopic.getId().toString());
					checkListTopicElement.setAttribute("title", checklistTopic.getTitle() == null ? "" : checklistTopic.getTitle());
					/*Element checkListTopicIdElement = doc.createElement("id");
					checkListTopicIdElement.appendChild(doc.createTextNode(checklistTopic.getId().toString()));
					checkListTopicElement.appendChild(checkListTopicIdElement);
					
					Element checkListTopicTitleEle = doc.createElement("title");
					checkListTopicTitleEle.appendChild(doc.createCDATASection(checklistTopic.getTitle() == null ? "" : checklistTopic.getTitle()));
					checkListTopicElement.appendChild(checkListTopicTitleEle);*/
					
					Element checkListTopicDescEle = doc.createElement("instruction");
					checkListTopicDescEle.appendChild(doc.createCDATASection(checklistTopic.getDescription() == null ? "" : checklistTopic.getDescription()));
					checkListTopicElement.appendChild(checkListTopicDescEle);
					
					Element checkListQuestionElement = doc.createElement("checklistitems");
					checkListTopicElement.appendChild(checkListQuestionElement);
					
					List<ChecklistQuestion> checklistQuestionsList = checklistTopic.getCheckListQuestions();
					
					for (ChecklistQuestion checklistQuestion : checklistQuestionsList)
					{
						Element checkListQueElement = doc.createElement("checklistitem");
						checkListQuestionElement.appendChild(checkListQueElement);
						
						checkListQueElement.setAttribute("id", checklistQuestion.getId().toString());
						checkListQueElement.setAttribute("affectsOverallRating", (checklistQuestion.getIsOveralQuestion() == null ? "no" : checklistQuestion.getIsOveralQuestion() == true ? "yes" : "no"));
						checkListQueElement.setAttribute("title",checklistQuestion.getQuestion() == null ? "" : checklistQuestion.getQuestion());
						
						/*Element checkListQueIdElement = doc.createElement("id");
						checkListQueIdElement.appendChild(doc.createTextNode(checklistQuestion.getId().toString()));
						checkListQueElement.appendChild(checkListQueIdElement);
						
						Element checkListQuestEle = doc.createElement("question");
						checkListQuestEle.appendChild(doc.createCDATASection(checklistQuestion.getQuestion() == null ? "" : checklistQuestion.getQuestion()));
						checkListQueElement.appendChild(checkListQuestEle);*/
						
						Element checkListQueInstEle = doc.createElement("instruction");
						checkListQueInstEle.appendChild(doc.createCDATASection(checklistQuestion.getInstruction() == null ? "" : checklistQuestion.getInstruction()));
						checkListQueElement.appendChild(checkListQueInstEle);
						
						/*Element checkListQueIsOverall = doc.createElement("key");
						checkListQueIsOverall.appendChild(doc.createTextNode("isOverallQuestion"));
						checkListQueElement.appendChild(checkListQueIsOverall);
						Element checkListQueIsOverallVal = doc.createElement((checklistQuestion.getIsOveralQuestion() == null ? "false" : checklistQuestion.getIsOveralQuestion().toString()));					
						checkListQueElement.appendChild(checkListQueIsOverallVal);*/
						
						/*Element checkListQueSeqNoElement = doc.createElement("sequencenumber");
						checkListQueSeqNoElement.appendChild(doc.createTextNode(String.valueOf(ctr)));
						checkListQueElement.appendChild(checkListQueSeqNoElement);*/
						
						ctr++;
						
						Element checkListCriteriaElement = doc.createElement("checklistcriteria");
						checkListQueElement.appendChild(checkListCriteriaElement);
						
						Iterator<ChecklistCriteria> criiterator = checklistQuestion.getCheckListCriterias().iterator();
						
						while (criiterator.hasNext())
						{
							ChecklistCriteria criteria = criiterator.next();
						
							Element criteriaElement = doc.createElement("checklistcriterion");
							checkListCriteriaElement.appendChild(criteriaElement);
							
							criteriaElement.setAttribute("id", criteria.getId().toString());
							criteriaElement.setAttribute("title", criteria.getCriteria() == null ? "" : criteria.getCriteria());
							/*Element criteriaIdElement = doc.createElement("id");
							criteriaIdElement.appendChild(doc.createTextNode(criteria.getId().toString()));
							criteriaElement.appendChild(criteriaIdElement);
							
							Element criteriaTitleEle = doc.createElement("title");
							criteriaTitleEle.appendChild(doc.createCDATASection(criteria.getCriteria() == null ? "" : criteria.getCriteria()));
							criteriaElement.appendChild(criteriaTitleEle);
							
							Element criteriaSeqNoEle = doc.createElement("sequencenumber");
							criteriaSeqNoEle.appendChild(doc.createTextNode(criteria.getSequenceNumber() == null ? "" : criteria.getSequenceNumber().toString()));
							criteriaElement.appendChild(criteriaSeqNoEle);*/
						}
						
						Element checkListOptionElement = doc.createElement("checklistoptions");
						checkListQueElement.appendChild(checkListOptionElement);
						
						Iterator<ChecklistOption> opitr = checklistQuestion.getCheckListOptions().iterator();
						
						while (opitr.hasNext())
						{
							ChecklistOption option = opitr.next();
							
							Element optionElement = doc.createElement("checklistoption");
							checkListOptionElement.appendChild(optionElement);
							
							optionElement.setAttribute("id", option.getId().toString());
							optionElement.setAttribute("title", option.getOptionName() == null ? "" : option.getOptionName());
							//optionElement.setAttribute("subtitle", option.getInstruction() == null ? "" : option.getInstruction());
							optionElement.setAttribute("value", option.getValue() == null ? "" : option.getValue().toString());
							optionElement.setAttribute("criteriacount", option.getCriteriaCount() == null ? "0" : option.getCriteriaCount().toString());
							
							/*Element optionIdElement = doc.createElement("id");
							optionIdElement.appendChild(doc.createTextNode(option.getId().toString()));
							optionElement.appendChild(optionIdElement);
							
							Element optionTitleEle = doc.createElement("title");
							optionTitleEle.appendChild(doc.createCDATASection(option.getOptionName() == null ? "" : option.getOptionName()));
							optionElement.appendChild(optionTitleEle);
							
							Element optionValElement = doc.createElement("value");
							optionValElement.appendChild(doc.createTextNode(option.getValue() == null ? "" : option.getValue().toString()));
							optionElement.appendChild(optionValElement);
							
							Element instructionElement = doc.createElement("instruction");
							instructionElement.appendChild(doc.createCDATASection(option.getInstruction() == null ? "" : option.getInstruction()));
							optionElement.appendChild(instructionElement);
							
							Element optionSeqNoElement = doc.createElement("sequencenumber");
							optionSeqNoElement.appendChild(doc.createTextNode(option.getSequenceNumber() == null ? "" : option.getSequenceNumber().toString()));
							optionElement.appendChild(optionSeqNoElement);
							
							Element optionCriteriaCountElement = doc.createElement("criteriacount");
							optionCriteriaCountElement.appendChild(doc.createTextNode((option.getCriteriaCount() == null ? "0" : option.getCriteriaCount().toString())));
							optionElement.appendChild(optionCriteriaCountElement);*/
						}
					}
				}
		}
	}

	/*public void exportOsceFile(Long semesterID)
	{
		//System.out.println("~~SEMESTER ID : " + semesterID);
		String fileName = "";
		int timeslot = 0;
		String xml;
		
		try
		{
			OsceConstantsWithLookup constants = GWTI18N.create(OsceConstantsWithLookup.class);
			
			List<Osce> osceList = Osce.findAllOsceBySemster(semesterID);
					
			for (int i=0; i<osceList.size(); i++)
			{
				//System.out.println("OSCE : " + i);		
				
				timeslot = osceList.get(i).getOscePostBlueprints().size();
				
				List<OsceDay> osceDayList = OsceDay.findOsceDayByOsce(osceList.get(i).getId());
				int startrotation = 0;
				int totalrotation = 0;
				
				//System.out.println("OSCEDAY LIST : " + osceDayList.size());
				
				for (int j=0; j<osceDayList.size(); j++)
				{
					//System.out.println("DAY ID : " + osceDayList.get(i));
					List<OsceSequence> sequenceList = OsceSequence.findOsceSequenceByOsceDay(osceDayList.get(j).getId());
					//System.out.println("SEQUENCE LIST : " + sequenceList.size());
					
					for (int k=0; k<sequenceList.size(); k++)
					{
						startrotation = totalrotation;
						totalrotation = totalrotation + sequenceList.get(k).getNumberRotation();
						
						List<Course> courseList = sequenceList.get(k).getCourses();
						
						int rotationoffset = 0;
						int rotationStart = startrotation;
						for (int l=startrotation; l<totalrotation; l++)
						{	
							for (Course course : courseList)
							{
								DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
								DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
						
								Document doc = docBuilder.newDocument();
								
								Element rotationElement = doc.createElement("rotation");
								doc.appendChild(rotationElement);							
								
								rotationElement.setAttribute("version", "1.0");
								
								Element rootElement = doc.createElement("oscepostrooms");
								rotationElement.appendChild(rootElement);
								
								List<OscePostRoom> oscePostRoomList = OscePostRoom.findOscePostRoomByCourseID(course.getId());
								
								for (OscePostRoom oscePostRoom : oscePostRoomList)
								{
									Element oscePostRoomElement = doc.createElement("oscepostroom");
									rootElement.appendChild(oscePostRoomElement);
									
									Element roomElement = doc.createElement("room");
									oscePostRoomElement.appendChild(roomElement);
									
									Element roomIdElement = doc.createElement("id");
									roomIdElement.appendChild(doc.createTextNode(oscePostRoom.getRoom() == null ? "" : oscePostRoom.getRoom().getId().toString()));
									roomElement.appendChild(roomIdElement);
									
									Element roomNumElement = doc.createElement("number");
									roomNumElement.appendChild(doc.createCDATASection(oscePostRoom.getRoom() == null ? "" : (oscePostRoom.getRoom().getRoomNumber() == null ? "" : oscePostRoom.getRoom().getRoomNumber())));
									roomElement.appendChild(roomNumElement);
									
									List<Assignment> assignmentlist = Assignment.findAssignmentByOscePostRoom(oscePostRoom.getId(), osceList.get(i).getId(), l);
									
									List<Assignment> examinerAssList = new ArrayList<Assignment>();
									
									if (assignmentlist.size() > 0)
									{
										Date timestart = assignmentlist.get(0).getTimeStart();
										Date timeend = assignmentlist.get(assignmentlist.size()-1).getTimeEnd();
										examinerAssList = Assignment.findAssignmentExamnierByOscePostRoom(oscePostRoom.getId(), osceList.get(i).getId(), timestart, timeend);
									}
									
									Element examinersElement = doc.createElement("examiners");
									oscePostRoomElement.appendChild(examinersElement);
									
									for (Assignment examinerAss : examinerAssList)
									{
										if (examinerAss.getExaminer() != null)
										{
											Element examinerEle = doc.createElement("examiner");
											examinersElement.appendChild(examinerEle);
											
											Element examinerIdElement = doc.createElement("id");
											examinerIdElement.appendChild(doc.createTextNode(examinerAss.getExaminer().getId().toString()));
											examinerEle.appendChild(examinerIdElement);
											
											Element examinerFirstnameElement = doc.createElement("firstname");
											examinerFirstnameElement.appendChild(doc.createCDATASection(examinerAss.getExaminer().getPreName() == null ? "" : examinerAss.getExaminer().getPreName()));
											examinerEle.appendChild(examinerFirstnameElement);
											
											Element examinerlastnameElement = doc.createElement("lastname");
											examinerlastnameElement.appendChild(doc.createCDATASection(examinerAss.getExaminer().getName() == null ? "" : examinerAss.getExaminer().getName()));
											examinerEle.appendChild(examinerlastnameElement);
											
											Element examinerPhoneElement = doc.createElement("phone");
											examinerPhoneElement.appendChild(doc.createCDATASection(examinerAss.getExaminer().getTelephone() == null ? "" : examinerAss.getExaminer().getTelephone()));
											examinerEle.appendChild(examinerPhoneElement);
											
										}
									}
									
									OscePost oscepost = OscePost.findOscePost(oscePostRoom.getOscePost().getId());
									
									if (oscepost.getStandardizedRole() != null)
									{
										StandardizedRole standardizedRole = StandardizedRole.findStandardizedRole(oscepost.getStandardizedRole().getId());
										CheckList checklist = CheckList.findCheckList(standardizedRole.getCheckList().getId());
										
										Element checkListEle = doc.createElement("checklist");
										oscePostRoomElement.appendChild(checkListEle);
										
										Element checkListIdElement = doc.createElement("id");
										checkListIdElement.appendChild(doc.createTextNode(checklist.getId().toString()));
										checkListEle.appendChild(checkListIdElement);
										
										Element checkListTitleEle = doc.createElement("title");
										checkListTitleEle.appendChild(doc.createCDATASection(checklist.getTitle() == null ? "" : checklist.getTitle()));
										checkListEle.appendChild(checkListTitleEle);
										
										Element checkListTopicsEle = doc.createElement("checklisttopics");
										checkListEle.appendChild(checkListTopicsEle);
										
										List<ChecklistTopic> checklistTopicList = checklist.getCheckListTopics();
										
										int ctr = 1;
										
										for (ChecklistTopic checklistTopic : checklistTopicList)
										{
											Element checkListTopicElement = doc.createElement("checklisttopic");
											checkListTopicsEle.appendChild(checkListTopicElement);
											
											Element checkListTopicIdElement = doc.createElement("id");
											checkListTopicIdElement.appendChild(doc.createTextNode(checklistTopic.getId().toString()));
											checkListTopicElement.appendChild(checkListTopicIdElement);
											
											Element checkListTopicTitleEle = doc.createElement("title");
											checkListTopicTitleEle.appendChild(doc.createCDATASection(checklistTopic.getTitle() == null ? "" : checklistTopic.getTitle()));
											checkListTopicElement.appendChild(checkListTopicTitleEle);
											
											Element checkListTopicDescEle = doc.createElement("description");
											checkListTopicDescEle.appendChild(doc.createCDATASection(checklistTopic.getDescription() == null ? "" : checklistTopic.getDescription()));
											checkListTopicElement.appendChild(checkListTopicDescEle);
											
											Element checkListQuestionElement = doc.createElement("checklistquestions");
											checkListTopicElement.appendChild(checkListQuestionElement);
											
											List<ChecklistQuestion> checklistQuestionsList = checklistTopic.getCheckListQuestions();
											
											for (ChecklistQuestion checklistQuestion : checklistQuestionsList)
											{
												Element checkListQueElement = doc.createElement("checklistquestion");
												checkListQuestionElement.appendChild(checkListQueElement);
												
												Element checkListQueIdElement = doc.createElement("id");
												checkListQueIdElement.appendChild(doc.createTextNode(checklistQuestion.getId().toString()));
												checkListQueElement.appendChild(checkListQueIdElement);
												
												Element checkListQuestEle = doc.createElement("question");
												checkListQuestEle.appendChild(doc.createCDATASection(checklistQuestion.getQuestion() == null ? "" : checklistQuestion.getQuestion()));
												checkListQueElement.appendChild(checkListQuestEle);
												
												Element checkListQueInstEle = doc.createElement("instruction");
												checkListQueInstEle.appendChild(doc.createCDATASection(checklistQuestion.getInstruction() == null ? "" : checklistQuestion.getInstruction()));
												checkListQueElement.appendChild(checkListQueInstEle);
												
												Element checkListQueIsOverall = doc.createElement("key");
												checkListQueIsOverall.appendChild(doc.createTextNode("isOverallQuestion"));
												checkListQueElement.appendChild(checkListQueIsOverall);
												Element checkListQueIsOverallVal = doc.createElement((checklistQuestion.getIsOveralQuestion() == null ? "false" : checklistQuestion.getIsOveralQuestion().toString()));					
												checkListQueElement.appendChild(checkListQueIsOverallVal);
												
												Element checkListQueSeqNoElement = doc.createElement("sequencenumber");
												checkListQueSeqNoElement.appendChild(doc.createTextNode(String.valueOf(ctr)));
												checkListQueElement.appendChild(checkListQueSeqNoElement);
												
												ctr++;
												
												Element checkListCriteriaElement = doc.createElement("checklistcriterias");
												checkListQueElement.appendChild(checkListCriteriaElement);
												
												Iterator<ChecklistCriteria> criiterator = checklistQuestion.getCheckListCriterias().iterator();
												
												while (criiterator.hasNext())
												{
													ChecklistCriteria criteria = criiterator.next();
												
													Element criteriaElement = doc.createElement("checklistcriteria");
													checkListCriteriaElement.appendChild(criteriaElement);
													
													Element criteriaIdElement = doc.createElement("id");
													criteriaIdElement.appendChild(doc.createTextNode(criteria.getId().toString()));
													criteriaElement.appendChild(criteriaIdElement);
													
													Element criteriaTitleEle = doc.createElement("title");
													criteriaTitleEle.appendChild(doc.createCDATASection(criteria.getCriteria() == null ? "" : criteria.getCriteria()));
													criteriaElement.appendChild(criteriaTitleEle);
													
													Element criteriaSeqNoEle = doc.createElement("sequencenumber");
													criteriaSeqNoEle.appendChild(doc.createTextNode(criteria.getSequenceNumber() == null ? "" : criteria.getSequenceNumber().toString()));
													criteriaElement.appendChild(criteriaSeqNoEle);
												}
												
												Element checkListOptionElement = doc.createElement("checklistoptions");
												checkListQueElement.appendChild(checkListOptionElement);
												
												Iterator<ChecklistOption> opitr = checklistQuestion.getCheckListOptions().iterator();
												
												while (opitr.hasNext())
												{
													ChecklistOption option = opitr.next();
													
													Element optionElement = doc.createElement("checklistoption");
													checkListOptionElement.appendChild(optionElement);
													
													Element optionIdElement = doc.createElement("id");
													optionIdElement.appendChild(doc.createTextNode(option.getId().toString()));
													optionElement.appendChild(optionIdElement);
													
													Element optionTitleEle = doc.createElement("title");
													optionTitleEle.appendChild(doc.createCDATASection(option.getOptionName() == null ? "" : option.getOptionName()));
													optionElement.appendChild(optionTitleEle);
													
													Element optionValElement = doc.createElement("value");
													optionValElement.appendChild(doc.createTextNode(option.getValue() == null ? "" : option.getValue().toString()));
													optionElement.appendChild(optionValElement);
													
													Element optionSeqNoElement = doc.createElement("sequencenumber");
													optionSeqNoElement.appendChild(doc.createTextNode(option.getSequenceNumber() == null ? "" : option.getSequenceNumber().toString()));
													optionElement.appendChild(optionSeqNoElement);
													
													Element optionCriteriaCountElement = doc.createElement("criteriacount");
													optionCriteriaCountElement.appendChild(doc.createTextNode("[" + (option.getCriteriaCount() == null ? "" : option.getCriteriaCount()) + "]"));
													optionElement.appendChild(optionCriteriaCountElement);
												}
											}
										}
									}
									
									Element studentElement = doc.createElement("students");
									oscePostRoomElement.appendChild(studentElement);
									
									for (Assignment studAss : assignmentlist)
									{	
										if (studAss.getStudent() != null)
										{
											Element studElement = doc.createElement("student");
											studentElement.appendChild(studElement);
											
											Element studIdElement = doc.createElement("id");
											studIdElement.appendChild(doc.createTextNode(studAss.getStudent().getId().toString()));
											studElement.appendChild(studIdElement);
											
											Element studFirstNameEle = doc.createElement("firstname");
											studFirstNameEle.appendChild(doc.createCDATASection(studAss.getStudent().getPreName() == null ? "" : studAss.getStudent().getPreName()));
											studElement.appendChild(studFirstNameEle);
											
											Element studlastNameEle = doc.createElement("lastname");
											studlastNameEle.appendChild(doc.createCDATASection(studAss.getStudent().getName() == null ? "" : studAss.getStudent().getName()));
											studElement.appendChild(studlastNameEle);
											
											Element studEmailEle = doc.createElement("email");
											studEmailEle.appendChild(doc.createCDATASection(studAss.getStudent().getEmail() == null ? "" : studAss.getStudent().getEmail()));
											studElement.appendChild(studEmailEle);
											
											Element studStTimeElement = doc.createElement("starttime");
											studStTimeElement.appendChild(doc.createTextNode(studAss.getTimeStart() == null ? "" :studAss.getTimeStart().toString()));
											studElement.appendChild(studStTimeElement);
											
											Element studEndTimeElement = doc.createElement("endtime");
											studEndTimeElement.appendChild(doc.createTextNode(studAss.getTimeEnd() == null ? "" : studAss.getTimeEnd().toString()));
											studElement.appendChild(studEndTimeElement);
										}
									}
									
									String rotNum = String.format("%02d", (l+1));
									
									String semesterValue = osceList.get(i).getSemester().getSemester().toString();
									
									String calyear = osceList.get(i).getSemester().getCalYear().toString();
									calyear = calyear.substring(2, calyear.length());
									
									EnumRenderer<Enum<StudyYears>> enumStudyYear = new EnumRenderer<Enum<StudyYears>>();
									
									String studyYear = enumStudyYear.render(osceList.get(i).getStudyYear());
									
									studyYear.replace(".", "");
									
									EnumRenderer<Enum<ColorPicker>> enumColor = new EnumRenderer<Enum<ColorPicker>>();
									String rotationString = enumColor.render(ColorPicker.valueOf(course.getColor()));
									
									
									
									fileName = osceList.get(i).getSemester().getSemester().toString() 
											+ osceList.get(i).getSemester().getCalYear().toString().substring(2, osceList.get(i).getSemester().getCalYear().toString().length()) 
											+ "-" + (constants.getString(osceList.get(i).getStudyYear().toString()).replace(".", "")) 
											+ "-D" + (j + 1) + "-" + String.format("%02d", (l+1)) + "-" 
											+ (constants.getString(course.getColor()));
									
									//fileName = String.valueOf(osceList.get(i).getSemester().getSemester()) +  + osceList.get(i).getName() + rotNum + course.getColor() + ".xml";
									
									fileName = fileName + ".oscexchange";
									
									//System.out.println("File Name : " + fileName);
									
									String processedFileName = OsMaFilePathConstant.EXPORT_OSCE_PROCESSED_FILEPATH + fileName;
									File processfile = new File(processedFileName);
									Boolean processCheck = processfile.exists();	
									
									if (!processCheck)
									{
										fileName = fileName.replaceAll(" ", "_");
										fileName = OsMaFilePathConstant.EXPORT_OSCE_UNPROCESSED_FILEPATH + fileName;
										File file = new File(fileName);
										Boolean check = file.exists();
										
										if (check)
										{
											file.delete();
										}
										
										FileUtils.touch(file);
										
										TransformerFactory transformerFactory = TransformerFactory.newInstance();
										//transformerFactory.setAttribute("indent-number", new Integer(5));
										Transformer transformer = transformerFactory.newTransformer();
										transformer.setOutputProperty(OutputKeys.INDENT, "yes");
										transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
										//transformer.setOutputProperty("indent-amount", "3");
										DOMSource source = new DOMSource(doc);
										StreamResult result = new StreamResult(file);
										transformer.transform(source, result);
										
										//System.out.println("* * *" + file.getName() + " IS CREATED * * *");								
									}
									xml = "";
									fileName = "";
									}
									
							}
							
							rotationoffset += timeslot;
							
							
						}
						
						startrotation = totalrotation;
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.error(e.getMessage());
		}
	
	}*/
	
	public List<ExportOsceData> exportProcessedFileList(ExportOsceType osceType, Long semesterId)
	{
		List<ExportOsceData> exportOsceDataList = new ArrayList<ExportOsceData>();
		List<String> processedList = new ArrayList<String>();
		List<String> processedOsceIdList = new ArrayList<String>();
		try {
			OsceConstantsWithLookup constants = GWTI18N.create(OsceConstantsWithLookup.class);
			Semester semester = Semester.findSemester(semesterId);
			
			String folderPath = OsMaFilePathConstant.EXPORT_OSCE_PROCESSED_FILEPATH + semester.getSemester() + semester.getCalYear();
			if (ExportOsceType.EOSCE.equals(osceType)) {
				folderPath = folderPath + OsMaFilePathConstant.EXPORT_EOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
			} 
			else if (ExportOsceType.IOSCE.equals(osceType)) {
				folderPath = folderPath + OsMaFilePathConstant.EXPORT_IOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
			}
			
			File folder = new File(folderPath);
			
			if (folder.exists())
			{
				File[] listOfFiles = folder.listFiles();
				for (int i=0; i<listOfFiles.length; i++)
				{
					processedList.add(listOfFiles[i].getName());
				}
			}
			
			//List<Osce> osceList = Osce.findAllOsceBySemster(semesterId);
			List<Osce> osceList = Osce.findOsceBySemesterId(semesterId);
			
			for (Osce osce : osceList)
			{
				if ((ExportOsceType.EOSCE.equals(osceType) && osce.getIsFormativeOsce() != null && osce.getIsFormativeOsce()) == false) {
					ExportOsceData exportOsceData = new ExportOsceData();
					
					String fileName = osce.getSemester().getSemester().toString() 
							+ osce.getSemester().getCalYear().toString().substring(2, osce.getSemester().getCalYear().toString().length()) 
							+ "-" + (constants.getString(osce.getStudyYear().toString()).replace(".", "")); 
					
					if (ExportOsceType.EOSCE.equals(osceType)) {
						fileName = fileName + OsMaFilePathConstant.EOSCE_FILE_EXTENSION;
					} 
					else if (ExportOsceType.IOSCE.equals(osceType)) {
						fileName = fileName + OsMaFilePathConstant.IOSCE_FILE_EXTENSION;
					}
					
					if (processedList.contains(fileName)) {
						processedOsceIdList.add(osce.getId().toString());
						exportOsceData.setOsceId(osce.getId());
						exportOsceData.setFilename(fileName);
						
						exportOsceDataList.add(exportOsceData);
					}
				}
			}
		} 
		catch (Exception e) {
			Log.error(e.getMessage(), e);
		}
		
		return exportOsceDataList;
	}
	
	public List<ExportOsceData> exportUnprocessedFileList(ExportOsceType osceType, Long semesterId)
	{
		List<ExportOsceData> exportOsceDataList = new ArrayList<ExportOsceData>();
		List<String> unprocessedOsceIdList = new ArrayList<String>();
		List<String> processedList = new ArrayList<String>();
		try {
			OsceConstantsWithLookup constants = GWTI18N.create(OsceConstantsWithLookup.class);
			Semester semester = Semester.findSemester(semesterId);
			
			String folderPath = OsMaFilePathConstant.EXPORT_OSCE_PROCESSED_FILEPATH + semester.getSemester() + semester.getCalYear();
			if (ExportOsceType.EOSCE.equals(osceType)) {
				folderPath = folderPath + OsMaFilePathConstant.EXPORT_EOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
			} 
			else if (ExportOsceType.IOSCE.equals(osceType)) {
				folderPath = folderPath + OsMaFilePathConstant.EXPORT_IOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
			}
			
			File folder = new File(folderPath);
			if (folder.exists())
			{
				File[] listOfFiles = folder.listFiles();
				for (int i=0; i<listOfFiles.length; i++)
				{
					processedList.add(listOfFiles[i].getName());
				}
			}
			
			//List<Osce> osceList = Osce.findAllOsceBySemster(semesterId);
			List<Osce> osceList = Osce.findOsceBySemesterId(semesterId);
			
			for (Osce osce : osceList)
			{
				if ((ExportOsceType.EOSCE.equals(osceType) && osce.getIsFormativeOsce() != null && osce.getIsFormativeOsce()) == false) {
					ExportOsceData exportOsceData = new ExportOsceData();
					String fileName = osce.getSemester().getSemester().toString() 
							+ osce.getSemester().getCalYear().toString().substring(2, osce.getSemester().getCalYear().toString().length()) 
							+ "-" + (constants.getString(osce.getStudyYear().toString()).replace(".", "")); 
											
					if (ExportOsceType.EOSCE.equals(osceType)) {
						fileName = fileName + OsMaFilePathConstant.EOSCE_FILE_EXTENSION;
					} 
					else if (ExportOsceType.IOSCE.equals(osceType)) {
						fileName = fileName + OsMaFilePathConstant.IOSCE_FILE_EXTENSION;
					}
					
					if (processedList.contains(fileName) == false) {
						unprocessedOsceIdList.add(osce.getId().toString());
						exportOsceData.setOsceId(osce.getId());
						exportOsceData.setFilename(fileName);
						
						exportOsceDataList.add(exportOsceData);
					}
				}
			}
		}
		catch (Exception e) {
			Log.error(e.getMessage(), e);
		}
		
		return exportOsceDataList;
	}
	
	public void putAmazonS3Object(ExportOsceType osceType, Long semesterId,String bucketName, String accessKey, String secretKey, List<String> osceIdList, Boolean flag) throws eOSCESyncException
	{	
		//file is put in bucketname as key.
		
		try
		{
			Semester semester = Semester.findSemester(semesterId);
			OsceConstantsWithLookup constants = GWTI18N.create(OsceConstantsWithLookup.class);
			bucketName = bucketName.toLowerCase();
			
			//System.out.println("BUCKET NAME : " + bucketName);
			AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
			//AmazonS3Client client = new AmazonS3Client(new PropertiesCredentials(eOSCESyncServiceImpl.class.getResourceAsStream("AwsCredentials.properties")));
			
			AmazonS3Client client;
			
			Properties properties =  new Properties();
			properties.load(OsMaFilePathConstant.class.getResourceAsStream("/META-INF/spring/proxy.properties"));			
			String proxyHost = properties.getProperty("proxy.host");
			String proxyPort = properties.getProperty("proxy.port");
			
			if (StringUtils.isNotBlank(proxyHost) && StringUtils.isNotBlank(proxyPort))
			{
				ClientConfiguration configuration = new ClientConfiguration();
				configuration.setProtocol(Protocol.HTTP);
				configuration.setProxyHost(proxyHost);
				configuration.setProxyPort(Integer.parseInt(proxyPort));
				client = new AmazonS3Client(credentials, configuration);
			}
			else  
			{
				client = new AmazonS3Client(credentials);			
			}
			
			List<Bucket> bucketList = client.listBuckets();
			Boolean bucketExist = false;
			
			for (Bucket bucket : bucketList)
			{
				//System.out.println("BUCKET ON S3 : " + bucket.getName());
				if (bucketName.equals(bucket.getName()))
				{
					bucketExist = true;
					break;
				}
				else
				{
					bucketExist = false;
				}
			}
			
			if (!bucketExist)
			{	
				client.createBucket(bucketName,Region.EU_Ireland);
			}
			
			if (flag)
			{
				String path = OsMaFilePathConstant.EXPORT_OSCE_UNPROCESSED_FILEPATH + semester.getSemester() + semester.getCalYear();
            	if (ExportOsceType.EOSCE.equals(osceType)) {
            		path = path + OsMaFilePathConstant.EXPORT_EOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);	
            	}
            	else if (ExportOsceType.IOSCE.equals(osceType)) {
            		path = path + OsMaFilePathConstant.EXPORT_IOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
            	}
				
				for (String osceId : osceIdList)
				{
					Osce osce = Osce.findOsce(Long.parseLong(osceId));
					if ((ExportOsceType.EOSCE.equals(osceType) && osce.getIsFormativeOsce() != null && osce.getIsFormativeOsce()) == false) {
						String fileName = osce.getSemester().getSemester().toString() 
								+ osce.getSemester().getCalYear().toString().substring(2, osce.getSemester().getCalYear().toString().length()) 
								+ "-" + (constants.getString(osce.getStudyYear().toString()).replace(".", ""));
						
						if (ExportOsceType.EOSCE.equals(osceType)) {
							fileName = path + fileName + OsMaFilePathConstant.EOSCE_FILE_EXTENSION;	
		            	}
		            	else if (ExportOsceType.IOSCE.equals(osceType)) {
		            		fileName = path + fileName + OsMaFilePathConstant.IOSCE_FILE_EXTENSION;;
		            	}
						
						File file = new File(fileName);
						
						//if (file.exists() == false) {
						if (ExportOsceType.EOSCE.equals(osceType)) {
							byte[] bytes = new ExporteOSCEXml().generateXmlFileByOsceId(Long.parseLong(osceId), osce);
							FileUtils.touch(file);
							FileUtils.writeByteArrayToFile(file, bytes);
						} 
						else if (ExportOsceType.IOSCE.equals(osceType)) {
							byte[] bytes = new ExportiOSCEXml().generateXmlFileByOsceId(Long.parseLong(osceId), osce);
							FileUtils.touch(file);
							FileUtils.writeByteArrayToFile(file, bytes);
						}
						//}
						
						client.putObject(bucketName, file.getName(), file);
					
						//move file to processed		
						String processedFilePath = OsMaFilePathConstant.EXPORT_OSCE_PROCESSED_FILEPATH + semester.getSemester() + semester.getCalYear() + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
						if (ExportOsceType.EOSCE.equals(osceType)) {
							processedFilePath = processedFilePath + OsMaFilePathConstant.EXPORT_EOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);	
		            	}
		            	else if (ExportOsceType.IOSCE.equals(osceType)) {
		            		processedFilePath = processedFilePath + OsMaFilePathConstant.EXPORT_IOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
		            	}
						
						File dir = new File(processedFilePath);
						if (dir.exists())
						{
							//file.renameTo(new File(dir, file.getName()));
							FileUtils.copyFile(file, new File(dir, file.getName()));
							file.delete();
						}
						else
						{	
							dir.mkdirs();
							//file.renameTo(new File(dir, file.getName()));
							FileUtils.copyFile(file, new File(dir, file.getName()));
							file.delete();
						}
					}
				}	
			}			
			else
			{
				String path = OsMaFilePathConstant.EXPORT_OSCE_PROCESSED_FILEPATH + semester.getSemester() + semester.getCalYear() + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
				if (ExportOsceType.EOSCE.equals(osceType)) {
            		path = path + OsMaFilePathConstant.EXPORT_EOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);	
            	}
            	else if (ExportOsceType.IOSCE.equals(osceType)) {
            		path = path + OsMaFilePathConstant.EXPORT_IOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
            	}
				
				for (String osceId : osceIdList)
				{
					Osce osce = Osce.findOsce(Long.parseLong(osceId));
					String fileName = osce.getSemester().getSemester().toString() 
							+ osce.getSemester().getCalYear().toString().substring(2, osce.getSemester().getCalYear().toString().length()) 
							+ "-" + (constants.getString(osce.getStudyYear().toString()).replace(".", ""));
					
					if (ExportOsceType.EOSCE.equals(osceType)) {
						fileName = path + fileName + OsMaFilePathConstant.EOSCE_FILE_EXTENSION;	
	            	}
	            	else if (ExportOsceType.IOSCE.equals(osceType)) {
	            		fileName = path + fileName + OsMaFilePathConstant.IOSCE_FILE_EXTENSION;;
	            	}
					
					File file = new File(path);
					//if (file.exists() == false) {
					if (ExportOsceType.EOSCE.equals(osceType)) {
						byte[] bytes = new ExporteOSCEXml().generateXmlFileByOsceId(Long.parseLong(osceId), osce);
						FileUtils.touch(file);
						FileUtils.writeByteArrayToFile(file, bytes);
					} 
					else if (ExportOsceType.IOSCE.equals(osceType)) {
						byte[] bytes = new ExportiOSCEXml().generateXmlFileByOsceId(Long.parseLong(osceId), osce);
						FileUtils.touch(file);
						FileUtils.writeByteArrayToFile(file, bytes);
					}
					//}
					
					client.putObject(bucketName, file.getName(), file);
				}
			}
		}
		catch(AmazonServiceException ase)
		{
			Log.error(ase.getMessage());
			throw new eOSCESyncException(ase.getErrorType().toString(),ase.getMessage());
			//check that bucketname is already exist or not
			/*if (ase.getStatusCode() == 409)
			{
				bucketName = bucketName + "1";
				putAmazonS3Object(bucketName, fileList, flag);
			}
			else
			{
				throw new eOSCESyncException(ase.getErrorType().toString(),ase.getMessage());
			}*/
		}
		catch(AmazonClientException ace)
		{
			Log.error(ace.getMessage());
			throw new eOSCESyncException("",ace.getMessage());
		}
		catch(Exception e)
		{
			Log.error(e.getMessage());
			throw new eOSCESyncException("",e.getMessage());
		}
		
		//System.out.println("File is Put Successfully");
	}
	
	public void putFTP(ExportOsceType osceType, Long semesterId,String bucketName, String accessKey, String secretKey, String basePath, List<String> osceIdList, Boolean flag) throws eOSCESyncException {
		String SFTPHOST = bucketName;
		int    SFTPPORT = 22;
		String SFTPUSER = accessKey;
		String SFTPPASS = secretKey;
		String SFTPWORKINGDIR = basePath;
		Session     session     = null;
		Channel     channel     = null;
		ChannelSftp channelSftp = null;
        try {
        	OsceConstantsWithLookup constants = GWTI18N.create(OsceConstantsWithLookup.class);
        	Semester semester = Semester.findSemester(semesterId);
             
            JSch jsch = new JSch();
			session = jsch.getSession(SFTPUSER,SFTPHOST,SFTPPORT);
			session.setPassword(SFTPPASS);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp)channel;
			channelSftp.cd(SFTPWORKINGDIR);
			
            if (flag)
			{
            	String path = OsMaFilePathConstant.EXPORT_OSCE_UNPROCESSED_FILEPATH + semester.getSemester() + semester.getCalYear();
            	if (ExportOsceType.EOSCE.equals(osceType)) {
            		path = path + OsMaFilePathConstant.EXPORT_EOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);	
            	}
            	else if (ExportOsceType.IOSCE.equals(osceType)) {
            		path = path + OsMaFilePathConstant.EXPORT_IOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
            	}
            	
            	for (String osceId : osceIdList)
				{
            		Osce osce = Osce.findOsce(Long.parseLong(osceId));
            		if ((ExportOsceType.EOSCE.equals(osceType) && osce.getIsFormativeOsce() != null && osce.getIsFormativeOsce()) == false) {
            			String fileName = osce.getSemester().getSemester().toString() 
								+ osce.getSemester().getCalYear().toString().substring(2, osce.getSemester().getCalYear().toString().length()) 
								+ "-" + (constants.getString(osce.getStudyYear().toString()).replace(".", ""));
						
						if (ExportOsceType.EOSCE.equals(osceType)) {
							fileName = path + fileName + OsMaFilePathConstant.EOSCE_FILE_EXTENSION;	
		            	}
		            	else if (ExportOsceType.IOSCE.equals(osceType)) {
		            		fileName = path + fileName + OsMaFilePathConstant.IOSCE_FILE_EXTENSION;;
		            	}
						
						File file = new File(fileName);
						
						//if (file.exists() == false) {
						if (ExportOsceType.EOSCE.equals(osceType)) {
							byte[] bytes = new ExporteOSCEXml().generateXmlFileByOsceId(Long.parseLong(osceId), osce);
							FileUtils.touch(file);
							FileUtils.writeByteArrayToFile(file, bytes);
						} 
						else if (ExportOsceType.IOSCE.equals(osceType)) {
							byte[] bytes = new ExportiOSCEXml().generateXmlFileByOsceId(Long.parseLong(osceId), osce);
							FileUtils.touch(file);
							FileUtils.writeByteArrayToFile(file, bytes);
						}
						//}
						
						//fis = new FileInputStream(file);
						//client.storeFile(basePath + fileName, fis);
						channelSftp.put(new FileInputStream(file), file.getName());
					
						//move file to processed
						String processedFilePath = OsMaFilePathConstant.EXPORT_OSCE_PROCESSED_FILEPATH + semester.getSemester() + semester.getCalYear();
						if (ExportOsceType.EOSCE.equals(osceType)) {
							processedFilePath = processedFilePath + OsMaFilePathConstant.EXPORT_EOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);	
		            	}
		            	else if (ExportOsceType.IOSCE.equals(osceType)) {
		            		processedFilePath = processedFilePath + OsMaFilePathConstant.EXPORT_IOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
		            	}
						File dir = new File(processedFilePath);
						if (dir.exists())
						{
							//file.renameTo(new File(dir, file.getName()));
							FileUtils.copyFile(file, new File(dir, file.getName()));
							//fis.close();
							file.delete();
						}
						else
						{	
							dir.mkdirs();
							//file.renameTo(new File(dir, file.getName()));
							FileUtils.copyFile(file, new File(dir, file.getName()));
							//fis.close();
							file.delete();
						}
            		}
				}	
			}			
			else
			{
				String path = OsMaFilePathConstant.EXPORT_OSCE_PROCESSED_FILEPATH + semester.getSemester() + semester.getCalYear();
				
				if (ExportOsceType.EOSCE.equals(osceType)) {
            		path = path + OsMaFilePathConstant.EXPORT_EOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);	
            	}
            	else if (ExportOsceType.IOSCE.equals(osceType)) {
            		path = path + OsMaFilePathConstant.EXPORT_IOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
            	}
				
				for (String osceId : osceIdList)
				{
					Osce osce = Osce.findOsce(Long.parseLong(osceId));
					String fileName = osce.getSemester().getSemester().toString() 
							+ osce.getSemester().getCalYear().toString().substring(2, osce.getSemester().getCalYear().toString().length()) 
							+ "-" + (constants.getString(osce.getStudyYear().toString()).replace(".", "")); 
					
					if (ExportOsceType.EOSCE.equals(osceType)) {
						fileName = path + fileName + OsMaFilePathConstant.EOSCE_FILE_EXTENSION;	
	            	}
	            	else if (ExportOsceType.IOSCE.equals(osceType)) {
	            		fileName = path + fileName + OsMaFilePathConstant.IOSCE_FILE_EXTENSION;;
	            	}
					
					File file = new File(fileName);
					
					//if (file.exists() == false) {
					if (ExportOsceType.EOSCE.equals(osceType)) {
						byte[] bytes = new ExporteOSCEXml().generateXmlFileByOsceId(Long.parseLong(osceId), osce);
						FileUtils.touch(file);
						FileUtils.writeByteArrayToFile(file, bytes);
					} 
					else if (ExportOsceType.IOSCE.equals(osceType)) {
						byte[] bytes = new ExportiOSCEXml().generateXmlFileByOsceId(Long.parseLong(osceId), osce);
						FileUtils.touch(file);
						FileUtils.writeByteArrayToFile(file, bytes);
					}
					//}
					channelSftp.put(new FileInputStream(file), file.getName());
					//fis = new FileInputStream(file);
					//client.storeFile(basePath + fileName, fis);
				}
			}
        }
        catch(SftpException e){
       		e.printStackTrace();
			Log.error(e.getMessage());
			throw new eOSCESyncException(e.getMessage(),"SFTP_EXCEPTION");
		}
        catch(java.net.UnknownHostException e){
       		e.printStackTrace();
			Log.error(e.getMessage());
			throw new eOSCESyncException(e.getMessage(),"UNKNOWN_HOST");
		}
        catch(JSchException e){
       		e.printStackTrace();
			Log.error(e.getMessage());
			throw new eOSCESyncException(e.getMessage(),"JSCH_EXCEPTION");
		}
        catch(IOException e){
       		e.printStackTrace();
			Log.error(e.getMessage());
			throw new eOSCESyncException(e.getMessage(),"IO_EXCEPTION");
		}
        catch(Exception e){
        	e.printStackTrace();
			Log.error(e.getMessage());
			throw new eOSCESyncException(e.getMessage(),"EXCEPTION");
		}
        finally {
			if (channel != null)
				channel.disconnect();
			
			if (session != null)
				session.disconnect();
        }
           
	}
	
	/**
	* utility to create an arbitrary directory hierarchy on the remote ftp server 
	* @param client
	* @param dirTree  the directory tree only delimited with / chars.  No file name!
	* @throws Exception
	*/
	private static void ftpCreateDirectoryTree( FTPClient client, String dirTree ) throws IOException {

	  boolean dirExists = true;

	  //tokenize the string and attempt to change into each directory level.  If you cannot, then start creating.
	  String[] directories = dirTree.split("/");
	  for (String dir : directories ) {
	    if (!dir.isEmpty() ) {
	      if (dirExists) {
	        dirExists = client.changeWorkingDirectory(dir);
	      }
	      if (!dirExists) {
	        if (!client.makeDirectory(dir)) {
	          throw new IOException("Unable to create remote directory '" + dir + "'.  error='" + client.getReplyString()+"'");
	        }
	        if (!client.changeWorkingDirectory(dir)) {
	          throw new IOException("Unable to change into newly created remote directory '" + dir + "'.  error='" + client.getReplyString()+"'");
	        }
	      }
	    }
	  }     
	}
	
	public void exportOsceFile(Long semesterID){

		String fileName = "";
		try
		{
			OsceConstantsWithLookup constants = GWTI18N.create(OsceConstantsWithLookup.class);
			
			List<Osce> osceList = Osce.findAllOsceBySemster(semesterID);
					
			for (int i=0; i<osceList.size(); i++)
			{
				ObjectFactory factory = new ObjectFactory();
				Oscedata oscedata = factory.createOscedata();
				oscedata.setVersion(1.1f);
				
				BucketInformation bucketInformation = BucketInformation.findBucketInformationBySemesterForExport(osceList.get(i).getSemester().getId());
				
				
				Credentials credentials = factory.createOscedataCredentials();
				oscedata.setCredentials(credentials);
				
				if(bucketInformation == null || bucketInformation.getType() == null) {
					credentials.setType(BucketInfoType.S3.getStringValue());
				}else {
					credentials.setType(bucketInformation.getType().getStringValue());	
				}
				
				Host host = factory.createOscedataCredentialsHost();
				credentials.setHost(host);
				if(bucketInformation != null && BucketInfoType.FTP.equals(bucketInformation.getType())) {
					host.setBasepath(bucketInformation.getBasePath());
				}
				
				host.setValue(bucketInformation == null ? "" : defaultString(bucketInformation.getBucketName()));
				credentials.setUser(bucketInformation == null ? "" : defaultString(bucketInformation.getAccessKey()));
				credentials.setPassword(bucketInformation == null ? "" : defaultString(bucketInformation.getSecretKey()));
				credentials.setEncryptionKey(bucketInformation == null ? "" : defaultString(bucketInformation.getEncryptionKey()));
				
				//checkList(osceList.get(i).getId(),factory,oscedata);
				checkListFromChecklistItem(osceList.get(i).getId(),factory,oscedata);
				examiners(osceList.get(i).getId(),factory,oscedata);
				candidates(osceList.get(i).getId(),factory,oscedata);
				stations(osceList.get(i).getId(),factory,oscedata);
				courses(osceList.get(i).getId(),factory,oscedata,constants);
				rotations(osceList.get(i).getId(),factory,oscedata,constants);
				
				
				fileName = osceList.get(i).getSemester().getSemester().toString() 
						+ osceList.get(i).getSemester().getCalYear().toString().substring(2, osceList.get(i).getSemester().getCalYear().toString().length()) 
						+ "-" + (constants.getString(osceList.get(i).getStudyYear().toString()).replace(".", "")); 
										
				fileName = fileName + ".osceexchange";
				Semester semester = Semester.findSemester(semesterID);
				String processedFileName = OsMaFilePathConstant.EXPORT_OSCE_PROCESSED_FILEPATH + semester.getSemester() + semester.getCalYear() + 
																													(isLocal==true ? folderSeparatorLocal : folderSeparatorProduction) + fileName;
				File processfile = new File(processedFileName);
				Boolean processCheck = processfile.exists();	
				
				if (!processCheck)
				{
					fileName = fileName.replaceAll(" ", "_");
					fileName = OsMaFilePathConstant.EXPORT_OSCE_UNPROCESSED_FILEPATH + semester.getSemester() + semester.getCalYear() + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction)
								+ fileName;
					File file = new File(fileName);
					Boolean check = file.exists();
					
					if (check)
					{
						file.delete();
					}
					
					FileUtils.touch(file);
					
					JAXBContext jaxbContext = JAXBContext.newInstance(Oscedata.class);
					Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			 
					// output pretty printed
					jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
					jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			 
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					jaxbMarshaller.marshal(oscedata, stream);
					
					String data = new String(stream.toByteArray(),"UTF-8");
					
					data = data.replaceAll("xsi:oscedata", "oscedata");
					FileUtils.writeStringToFile(file, data);
					
					// get an Apache XMLSerializer configured to generate CDATA
			        /*XMLSerializer serializer = getXMLSerializer(file);

			        // marshal using the Apache XMLSerializer
			        jaxbMarshaller.marshal(oscedata, serializer.asContentHandler());*/
					 
					/*TransformerFactory transformerFactory = TransformerFactory.newInstance();
					//transformerFactory.setAttribute("indent-number", new Integer(5));
					Transformer transformer = transformerFactory.newTransformer();
					transformer.setOutputProperty(OutputKeys.INDENT, "yes");
					transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
					//transformer.setOutputProperty("indent-amount", "3");
					DOMSource source = new DOMSource(doc);
					StreamResult result = new StreamResult(file);
					transformer.transform(source, result);*/
					
					//System.out.println("* * *" + file.getName() + " IS CREATED * * *");								
				}
				//xml = "";
				fileName = "";
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.error(e.getMessage());
		}
	}
	
	private void checkList(Long osceId,ObjectFactory factory, Oscedata oscedata) {
		//List<Course> courses = Course.findCourseByOsce(osceId);
		Checklists checklistsBean = factory.createOscedataChecklists();
		oscedata.setChecklists(checklistsBean);
		
		List<CheckList> checkLists =  CheckList.findAllCheckListforOsce(osceId);
		//Set<CheckList> checkLists= new HashSet<CheckList>();
		
		/*for (Course course : courses) {
			List<OscePostRoom> oscePostRoomList = OscePostRoom.findOscePostRoomByCourseID(course.getId());
			
			for (OscePostRoom oscePostRoom : oscePostRoomList) {
				OscePost oscepost = OscePost.findOscePost(oscePostRoom.getOscePost().getId());
				if (oscepost.getStandardizedRole() != null)
				{
					StandardizedRole standardizedRole = StandardizedRole.findStandardizedRole(oscepost.getStandardizedRole().getId());
					CheckList checklist = CheckList.findCheckList(standardizedRole.getCheckList().getId());
					checkLists.add(checklist);
				}
			}
		}*/
		
		for (CheckList checklist : checkLists) {
			
			Checklist checklistBean = factory.createOscedataChecklistsChecklist();
			checklistsBean.getChecklist().add(checklistBean);
			
			checklistBean.setId(checklist.getId());
			checklistBean.setTitle(defaultString(checklist.getTitle()));
			
			Checklisttopics checklisttopicsBean = factory.createOscedataChecklistsChecklistChecklisttopics();
			checklistBean.setChecklisttopics(checklisttopicsBean);
			
			List<ChecklistTopic> checklistTopicList = checklist.getCheckListTopics();
			
			for (ChecklistTopic checklistTopic : checklistTopicList)
			{
				Checklisttopic checklisttopicBean = factory.createOscedataChecklistsChecklistChecklisttopicsChecklisttopic();
				checklisttopicsBean.getChecklisttopic().add(checklisttopicBean);
				
				checklisttopicBean.setId(checklistTopic.getId());
				checklisttopicBean.setTitle(defaultString(checklistTopic.getTitle()));
				checklisttopicBean.setInstruction(defaultString(checklistTopic.getDescription()));
				
				Checklistitems checklistitemsBean = factory.createOscedataChecklistsChecklistChecklisttopicsChecklisttopicChecklistitems();
				checklisttopicBean.setChecklistitems(checklistitemsBean);
				
				List<ChecklistQuestion> checklistQuestionsList = checklistTopic.getCheckListQuestions();
				
				for (ChecklistQuestion checklistQuestion : checklistQuestionsList)
				{
					Checklistitem checklistitemBean = factory.createOscedataChecklistsChecklistChecklisttopicsChecklisttopicChecklistitemsChecklistitem();
					checklistitemsBean.getChecklistitem().add(checklistitemBean);
					
					checklistitemBean.setId(checklistQuestion.getId());
					checklistitemBean.setAffectsOverallRating((checklistQuestion.getIsOveralQuestion() == null ? "no" : checklistQuestion.getIsOveralQuestion() == true ? "yes" : "no"));
					checklistitemBean.setTitle(defaultString(checklistQuestion.getQuestion()));
					checklistitemBean.setInstruction(defaultString(checklistQuestion.getInstruction()));
					
					Checklistcriteria checklistcriteriaBean = factory.createOscedataChecklistsChecklistChecklisttopicsChecklisttopicChecklistitemsChecklistitemChecklistcriteria();
					checklistitemBean.setChecklistcriteria(checklistcriteriaBean);
					
					Iterator<ChecklistCriteria> criiterator = checklistQuestion.getCheckListCriterias().iterator();
					
					while (criiterator.hasNext())
					{
						ChecklistCriteria criteria = criiterator.next();
					
						Checklistcriterion checklistcriterionBean = factory.createOscedataChecklistsChecklistChecklisttopicsChecklisttopicChecklistitemsChecklistitemChecklistcriteriaChecklistcriterion();
						checklistcriteriaBean.getChecklistcriterion().add(checklistcriterionBean);
						checklistcriterionBean.setId(criteria.getId());
						checklistcriterionBean.setTitle(defaultString(criteria.getCriteria()));
					}
					
					Checklistoptions checklistoptionsBean = factory.createOscedataChecklistsChecklistChecklisttopicsChecklisttopicChecklistitemsChecklistitemChecklistoptions();
					checklistitemBean.setChecklistoptions(checklistoptionsBean);
					
					Iterator<ChecklistOption> opitr = checklistQuestion.getCheckListOptions().iterator();
					
					while (opitr.hasNext())
					{
						ChecklistOption option = opitr.next();
						
						Checklistoption checklistoptionBean = factory.createOscedataChecklistsChecklistChecklisttopicsChecklisttopicChecklistitemsChecklistitemChecklistoptionsChecklistoption();
						checklistoptionsBean.getChecklistoption().add(checklistoptionBean);
						
						checklistoptionBean.setId(option.getId());
						checklistoptionBean.setTitle(defaultString(option.getOptionName()));
						checklistoptionBean.setSubtitle(defaultString(option.getDescription()));
						checklistoptionBean.setVal(defaultString(option.getValue()));
						
						if(option.getCriteriaCount() != null) {
							checklistoptionBean.setCriteriacount(option.getCriteriaCount());	
						} else {
							checklistoptionBean.setCriteriacount(0);
						}
					}
				}
			}
		}
	}
	
	private void checkListFromChecklistItem(Long osceId,ObjectFactory factory, Oscedata oscedata) {
		Checklists checklistsBean = factory.createOscedataChecklists();
		oscedata.setChecklists(checklistsBean);
		
		List<CheckList> checkLists =  CheckList.findAllCheckListforOsce(osceId);
		
		for (CheckList checklist : checkLists) {
			
			Checklist checklistBean = factory.createOscedataChecklistsChecklist();
			checklistsBean.getChecklist().add(checklistBean);
			
			checklistBean.setId(checklist.getId());
			checklistBean.setTitle(defaultString(checklist.getTitle()));
			
			Checklisttopics checklisttopicsBean = factory.createOscedataChecklistsChecklistChecklisttopics();
			checklistBean.setChecklisttopics(checklisttopicsBean);
			
			List<ChecklistItem> checklistItemTopicList = ChecklistItem.findChecklistTopicByChecklistId(checklist.getId()); 
			
			for (ChecklistItem checklistItem : checklistItemTopicList)
			{
				Checklisttopic checklisttopicBean = factory.createOscedataChecklistsChecklistChecklisttopicsChecklisttopic();
				checklisttopicsBean.getChecklisttopic().add(checklisttopicBean);
				
				checklisttopicBean.setId(checklistItem.getId());
				checklisttopicBean.setTitle(defaultString(checklistItem.getName()));
				checklisttopicBean.setInstruction(defaultString(checklistItem.getDescription()));
				
				Checklistitems checklistitemsBean = factory.createOscedataChecklistsChecklistChecklisttopicsChecklisttopicChecklistitems();
				checklisttopicBean.setChecklistitems(checklistitemsBean);
				
				List<ChecklistItem> checklistItemQuestionId = ChecklistItem.findChecklistQuestionByChecklistTopicId(checklistItem.getId());
				
				for (ChecklistItem checklistQuestion : checklistItemQuestionId)
				{
					Checklistitem checklistitemBean = factory.createOscedataChecklistsChecklistChecklisttopicsChecklisttopicChecklistitemsChecklistitem();
					checklistitemsBean.getChecklistitem().add(checklistitemBean);
					
					checklistitemBean.setId(checklistQuestion.getId());
					checklistitemBean.setAffectsOverallRating((checklistQuestion.getIsRegressionItem() == null ? "no" : checklistQuestion.getIsRegressionItem() == true ? "yes" : "no"));
					checklistitemBean.setTitle(defaultString(checklistQuestion.getName()));
					checklistitemBean.setInstruction(defaultString(checklistQuestion.getDescription()));
					
					Checklistcriteria checklistcriteriaBean = factory.createOscedataChecklistsChecklistChecklisttopicsChecklisttopicChecklistitemsChecklistitemChecklistcriteria();
					checklistitemBean.setChecklistcriteria(checklistcriteriaBean);
					
					List<ChecklistCriteria> checkListCriteriaList = checklistQuestion.getCheckListCriterias();
					for (ChecklistCriteria criteria : checkListCriteriaList)
					{
						Checklistcriterion checklistcriterionBean = factory.createOscedataChecklistsChecklistChecklisttopicsChecklisttopicChecklistitemsChecklistitemChecklistcriteriaChecklistcriterion();
						checklistcriteriaBean.getChecklistcriterion().add(checklistcriterionBean);
						checklistcriterionBean.setId(criteria.getId());
						checklistcriterionBean.setTitle(defaultString(criteria.getCriteria()));
					}
					
					Checklistoptions checklistoptionsBean = factory.createOscedataChecklistsChecklistChecklisttopicsChecklisttopicChecklistitemsChecklistitemChecklistoptions();
					checklistitemBean.setChecklistoptions(checklistoptionsBean);
					
					Iterator<ChecklistOption> opitr = checklistQuestion.getCheckListOptions().iterator();
					while (opitr.hasNext())
					{
						ChecklistOption option = opitr.next();
						
						Checklistoption checklistoptionBean = factory.createOscedataChecklistsChecklistChecklisttopicsChecklisttopicChecklistitemsChecklistitemChecklistoptionsChecklistoption();
						checklistoptionsBean.getChecklistoption().add(checklistoptionBean);
						
						checklistoptionBean.setId(option.getId());
						checklistoptionBean.setTitle(defaultString(option.getOptionName()));
						checklistoptionBean.setSubtitle(defaultString(option.getDescription()));
						checklistoptionBean.setVal(defaultString(option.getValue()));
						
						if(option.getCriteriaCount() != null) {
							checklistoptionBean.setCriteriacount(option.getCriteriaCount());	
						} else {
							checklistoptionBean.setCriteriacount(0);
						}
					}
				}
			}
		}
	}

	private void examiners(Long osceId, ObjectFactory factory, Oscedata oscedata) {

		Examiners examinersBean = factory.createOscedataExaminers();
		oscedata.setExaminers(examinersBean);
		
		List<Doctor> examinerAssList = Assignment.findAssignmentExamnierByOsce(osceId);
		
		for (Doctor examinerAss : examinerAssList)
		{
			Examiner examinerBean = factory.createOscedataExaminersExaminer();
			examinersBean.getExaminer().add(examinerBean);
			
			examinerBean.setId(examinerAss.getId().intValue());
			examinerBean.setSalutation(defaultString(examinerAss.getTitle()));
			examinerBean.setFirstname(defaultString(examinerAss.getPreName()));
			examinerBean.setLastname(defaultString(examinerAss.getName()));
			if(StringUtils.isNotBlank(examinerAss.getTelephone())) {
				examinerBean.setPhone(getPhoneNumber(examinerAss.getTelephone()));	
			}
		}		
	}
	
	private Long getPhoneNumber(String telephone) {
		telephone = telephone.replaceAll("\\+", "");
		telephone = telephone.replaceAll(" ", "");
		try {
			return Long.parseLong(telephone,10);	
		}catch (Exception e) {
			Log.error(e);
		}
		
		return null;
	}

	private void candidates(Long osceId, ObjectFactory factory, Oscedata oscedata) {
		Candidates candidatesBean = factory.createOscedataCandidates();
		oscedata.setCandidates(candidatesBean);
		
		Set<String> done = new HashSet<String>();
		Set<Assignment> assignmentlist = new HashSet<Assignment>();
		assignmentlist.addAll(Assignment.findAssignmentStudentsByOsce(osceId));
		assignmentlist.addAll(Assignment.findAssignmentOfLogicalBreakPost(osceId));
		List<Assignment> assList = new ArrayList<Assignment>(assignmentlist);
		Collections.sort(assList, new Comparator<Assignment>() {

			@Override
			public int compare(Assignment o1, Assignment o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});
		
		for (Assignment studAss : assList)
		{	
			String studIdComp = studAss.getStudent() == null ? "" : studAss.getStudent().getId().toString();
			
			if(done.contains(studIdComp) == false) {
				done.add(studIdComp);
				
				Candidate candidateBean = factory.createOscedataCandidatesCandidate();
				candidatesBean.getCandidate().add(candidateBean);
				
				if(studAss.getStudent() != null) {
					candidateBean.setId(studAss.getStudent().getId());	
				} else {
					candidateBean.setId(0l);
				}
				
				String firstName = studAss.getStudent() == null ? ("S" + String.format("%03d", studAss.getSequenceNumber())) : (studAss.getStudent().getPreName() == null ? "" : studAss.getStudent().getPreName());
				candidateBean.setFirstname(firstName);
				String lastName = studAss.getStudent() == null ? ("S" + String.format("%03d", studAss.getSequenceNumber())) : (studAss.getStudent().getName() == null ? "" : studAss.getStudent().getName());
				candidateBean.setLastname(lastName);
				String email = studAss.getStudent() == null ? "" : (studAss.getStudent().getEmail() == null ? "" : studAss.getStudent().getEmail());
				candidateBean.setEmail(email);
				//candidateBean.setIsBreakCandidate(value) TODO need to remove
			}
		}
	}
	
	private void stations(Long osceId, ObjectFactory factory, Oscedata oscedata) {

		Stations stationsBean = factory.createOscedataStations();
		oscedata.setStations(stationsBean);
		
		List<OscePostRoom> oscePostRoomList = OscePostRoom.findOscePostRoomByOsce(osceId);
		
		for (OscePostRoom oscePostRoom : oscePostRoomList)
		{
			Station stationBean = factory.createOscedataStationsStation();
			stationsBean.getStation().add(stationBean);
			
			stationBean.setId(oscePostRoom.getId());
			//stationBean.setTitle("");//TODO need to ask
			
			if (oscePostRoom.getRoom() == null)
				stationBean.setIsBreakStation("yes");
			else 
				stationBean.setIsBreakStation("no");
			
			OscePost oscepost = OscePost.findOscePost(oscePostRoom.getOscePost().getId());
			
			if (oscepost.getStandardizedRole() != null)
			{
				StandardizedRole standardizedRole = StandardizedRole.findStandardizedRole(oscepost.getStandardizedRole().getId());
				CheckList checklist = CheckList.findCheckList(standardizedRole.getCheckList().getId());
				stationBean.setChecklistId(checklist.getId());
				
				if (oscepost.getStandardizedRole().getRoleTopic() != null && oscepost.getStandardizedRole().getRoleTopic().getSpecialisation() != null)
				{
					String stationTitle = oscepost.getStandardizedRole().getRoleTopic().getSpecialisation().getName() + " / " + oscepost.getStandardizedRole().getRoleTopic().getName();
					stationBean.setTitle(stationTitle);
				}
				else{
					stationBean.setTitle("");
				}
			}
		}
		
		Long logicalBreakAssignmentCount = Assignment.countAssignmentOfLogicalBreakPostPerOsce(osceId);
		
		if (logicalBreakAssignmentCount > 0)
		{
			Station stationBean = factory.createOscedataStationsStation();
			stationsBean.getStation().add(stationBean);
			stationBean.setIsBreakStation("yes");
			stationBean.setTitle("");
		}
	}
	
	
	private void courses(Long osceId, ObjectFactory factory, Oscedata oscedata, OsceConstantsWithLookup constants) {
		Courses coursesBean = factory.createOscedataCourses();
		oscedata.setCourses(coursesBean);
		List<OsceDay> osceDayList = OsceDay.findOsceDayByOsce(osceId);
		
		int startrotation = 0;
		int totalrotation = 0;
		
		for (int j=0; j<osceDayList.size(); j++)
		{
			List<OsceSequence> sequenceList = OsceSequence.findOsceSequenceByOsceDay(osceDayList.get(j).getId());
			
			/*if (sequenceList.size() == 1){
				totalrotation = sequenceList.get(0).getNumberRotation();
			}
			else if (sequenceList.size() == 2){
				totalrotation = sequenceList.get(0).getNumberRotation() + sequenceList.get(1).getNumberRotation();
			}*/
			
			for (int k=0; k<sequenceList.size(); k++)
			{
				startrotation = totalrotation;
				totalrotation = totalrotation + sequenceList.get(k).getNumberRotation();
				
				List<Course> courseList = sequenceList.get(k).getCourses();
				
				for (Course course : courseList)
				{
					ch.unibas.medizin.osce.server.bean.Oscedata.Courses.Course courseBean = factory.createOscedataCoursesCourse();
					coursesBean.getCourse().add(courseBean);
					
					courseBean.setId(course.getId());
					courseBean.setTitle(constants.getString(course.getColor()));
				}
			}
		}
	}

	private void rotations(Long osceId, ObjectFactory factory, Oscedata oscedata, OsceConstantsWithLookup constants) {
		List<OsceDay> osceDayList = OsceDay.findOsceDayByOsce(osceId);
		int startrotation = 0;
		int totalrotation = 0;
		
		Rotations rotationsBean = factory.createOscedataRotations();
		oscedata.setRotations(rotationsBean);
		
		for (int j=0; j<osceDayList.size(); j++)
		{
			List<OsceSequence> sequenceList = OsceSequence.findOsceSequenceByOsceDay(osceDayList.get(j).getId());
			
			/*if (sequenceList.size() == 1){
				totalrotation = sequenceList.get(0).getNumberRotation();
			}
			else if (sequenceList.size() == 2){
				totalrotation = sequenceList.get(0).getNumberRotation() + sequenceList.get(1).getNumberRotation();
			}*/
			
			for (int k=0; k<sequenceList.size(); k++)
			{
				startrotation = totalrotation;
				totalrotation = totalrotation + sequenceList.get(k).getNumberRotation();
				
				List<Course> courseList = sequenceList.get(k).getCourses();
				
				for (Course course : courseList)
				{
					for (int l=startrotation; l<totalrotation; l++)
					{	
						Rotation rotationBean = factory.createOscedataRotationsRotation();
						rotationsBean.getRotation().add(rotationBean);
						
						rotationBean.setId(Long.parseLong((l+1) + "" + course.getId()));
						rotationBean.setTitle(("Rotation " + String.format("%02d", (l+1)) + " " + constants.getString(course.getColor())));
						rotationBean.setCourseId(course.getId());
						
						ch.unibas.medizin.osce.server.bean.Oscedata.Rotations.Rotation.Stations stationsBean = factory.createOscedataRotationsRotationStations();
						rotationBean.setStations(stationsBean);
						
						List<OscePostRoom> oscePostRoomList = OscePostRoom.findOscePostRoomByCourseID(course.getId());
						
						for (OscePostRoom oscePostRoom : oscePostRoomList)
						{
							ch.unibas.medizin.osce.server.bean.Oscedata.Rotations.Rotation.Stations.Station stationBean = factory.createOscedataRotationsRotationStationsStation();
							stationsBean.getStation().add(stationBean);
							
							stationBean.setId(oscePostRoom.getId());
							
							List<Assignment> assignmentlist = Assignment.findAssignmentByOscePostRoom(oscePostRoom.getId(), osceId, l);
							
							List<Assignment> examinerAssList = new ArrayList<Assignment>();
							
							if (assignmentlist.size() > 0)
							{
								Date timestart = assignmentlist.get(0).getTimeStart();
								Date timeend = assignmentlist.get(assignmentlist.size()-1).getTimeEnd();
								examinerAssList = Assignment.findAssignmentExamnierByOscePostRoom(oscePostRoom.getId(), osceId, timestart, timeend);
							}
							
							for (Assignment examinerAss : examinerAssList) //TODO need to change logic
							{
								if (examinerAss.getExaminer() != null)
								{
									stationBean.setExaminerId(examinerAss.getExaminer().getId());
									break;
								}
							}
							
							for (Assignment studAss : assignmentlist) //TODO need to change logic
							{	
								if(studAss.getStudent() != null) {
									stationBean.setFirstCandidateId(studAss.getStudent().getId());
								} 
								
								/*if (studAss.getStudent() == null)
								{
									stationBean.setIsBreakCandidate("yes");
								}
								else
								{
									if (oscePostRoom.getRoom() == null)
										stationBean.setIsBreakCandidate("yes");
									else
										stationBean.setIsBreakCandidate("no");
								}*/
								break;
							}
						}
						
						List<Assignment> logicalBreakAssignment = Assignment.findAssignmentOfLogicalBreakPostPerRotation(osceDayList.get(j).getId(), course.getId(), l);
						
						if (logicalBreakAssignment != null && logicalBreakAssignment.size() > 0)
						{
							ch.unibas.medizin.osce.server.bean.Oscedata.Rotations.Rotation.Stations.Station stationBean = factory.createOscedataRotationsRotationStationsStation();
							stationsBean.getStation().add(stationBean);
							
							//stationBean.setIsBreakStation("yes");
							
							for (Assignment assignment : logicalBreakAssignment) //TODO need to change logic 
							{
								//stationBean.setIsBreakCandidate("yes");
								
								String id = assignment.getStudent() == null ? ("S" + String.format("%03d", assignment.getSequenceNumber())) : assignment.getStudent().getId().toString();
								if(assignment.getStudent()  != null) {
									stationBean.setFirstCandidateId(assignment.getStudent().getId());
								}
								
								break;
							}
						}
					}
				}
			}
		}
	}
	
	private static XMLSerializer getXMLSerializer(File file) throws FileNotFoundException {
	        // configure an OutputFormat to handle CDATA
	        OutputFormat of = new OutputFormat();

	        // specify which of your elements you want to be handled as CDATA.
	        // The use of the '^' between the namespaceURI and the localname
	        // seems to be an implementation detail of the xerces code.
		// When processing xml that doesn't use namespaces, simply omit the
		// namespace prefix as shown in the third CDataElement below.
	        of.setCDataElements(new String[] { "^host",  "^user","^password","^encryptionKey","^instruction","^salutation","^firstname","^lastname","^phone","^email" });   

	        // set any other options you'd like
	        of.setPreserveSpace(true);
	        of.setIndenting(true);

	        // create the serializer
	        XMLSerializer serializer = new XMLSerializer(of);
	        serializer.setOutputByteStream(new FileOutputStream(file));

	        return serializer;
    }
	
	public List<ExportOsceData> findProcessedFileNameFromLocal(ExportOsceType osceType, Long semesterID) throws eOSCESyncException {
		//List<String> fileList = new ArrayList<String>();
		List<ExportOsceData> exportOsceDataList = new ArrayList<ExportOsceData>();
		try
		{
			Semester semester = Semester.findSemester(semesterID);
			String path = OsMaFilePathConstant.IMPORT_PROCESSED_EOSCE_PATH + semester.getSemester() + semester.getCalYear();
	    	if (ExportOsceType.EOSCE.equals(osceType)) {
	    		path = path + OsMaFilePathConstant.EXPORT_EOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);	
	    	}
	    	else if (ExportOsceType.IOSCE.equals(osceType)) {
	    		path = path + OsMaFilePathConstant.EXPORT_IOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
	    	}
	    	
			File folder = new File(path);
			if (folder.exists() && folder.list() != null) {
				String[] fileNameList = folder.list();
				
				for (String fileName : fileNameList) {
					ExportOsceData exportOsceData = new ExportOsceData();
					exportOsceData.setFilename(fileName);
					exportOsceData.setFilepath((path + fileName));
					
					exportOsceDataList.add(exportOsceData);
				}
			}
		}
		catch(AmazonServiceException ase)
		{
			Log.error(ase.getMessage());
			throw new eOSCESyncException(ase.getErrorType().toString(),ase.getMessage());
		}
		catch(AmazonClientException ace)
		{
			Log.error(ace.getMessage());
			throw new eOSCESyncException("",ace.getMessage());
		}
		catch(Exception e)
		{
			Log.error(e.getMessage());
			throw new eOSCESyncException("",e.getMessage());
		}
		
		return exportOsceDataList;
	}
	 
	public List<ExportOsceData> findUnProcessedFileNameFromLocal(ExportOsceType osceType, Long semesterID) throws eOSCESyncException {
		//List<String> fileList = new ArrayList<String>();
		List<ExportOsceData> exportOsceDataList = new ArrayList<ExportOsceData>();
		try
		{
			Semester semester = Semester.findSemester(semesterID);
			String path = OsMaFilePathConstant.IMPORT_UNPROCESSED_EOSCE_PATH + semester.getSemester() + semester.getCalYear();
	    	if (ExportOsceType.EOSCE.equals(osceType)) {
	    		path = path + OsMaFilePathConstant.EXPORT_EOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);	
	    	}
	    	else if (ExportOsceType.IOSCE.equals(osceType)) {
	    		path = path + OsMaFilePathConstant.EXPORT_IOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
	    	}
	    	
			File folder = new File(path);
			if (folder.exists() && folder.list() != null) {
				String[] fileNameList = folder.list();
				
				for (String fileName : fileNameList) {
					ExportOsceData exportOsceData = new ExportOsceData();
					exportOsceData.setFilename(fileName);
					exportOsceData.setFilepath((path + fileName));
					
					exportOsceDataList.add(exportOsceData);
				}
			}
		}
		catch(AmazonServiceException ase)
		{
			Log.error(ase.getMessage());
			throw new eOSCESyncException(ase.getErrorType().toString(),ase.getMessage());
		}
		catch(AmazonClientException ace)
		{
			Log.error(ace.getMessage());
			throw new eOSCESyncException("",ace.getMessage());
		}
		catch(Exception e)
		{
			Log.error(e.getMessage());
			throw new eOSCESyncException("",e.getMessage());
		}
		
		return exportOsceDataList;
	}
	
	public List<ExportOsceData> findProcessedFilesFromCloud(BucketInfoType bucketInfoType, ExportOsceType osceType, Long semesterID) throws eOSCESyncException {
		if (BucketInfoType.S3.equals(bucketInfoType)) {
			return findProcessedFilesFromS3(osceType, semesterID);
		}
		else if (BucketInfoType.FTP.equals(bucketInfoType)) {
			return findProcessedFilesFromSFTP(osceType, semesterID);
		}
		return new ArrayList<ExportOsceData>();
	}
	
	public List<ExportOsceData> findProcessedFilesFromS3(ExportOsceType osceType, Long semesterID) throws eOSCESyncException{

		List<ExportOsceData> exportOsceDataList = new ArrayList<ExportOsceData>();
		
		try
		{
			Semester semester = Semester.findSemester(semesterID);
			String path = OsMaFilePathConstant.IMPORT_PROCESSED_EOSCE_PATH + semester.getSemester() + semester.getCalYear();
	    	if (ExportOsceType.EOSCE.equals(osceType)) {
	    		path = path + OsMaFilePathConstant.EXPORT_EOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);	
	    	}
	    	else if (ExportOsceType.IOSCE.equals(osceType)) {
	    		path = path + OsMaFilePathConstant.EXPORT_IOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
	    	}
	    	
			String accessKey = "";
			String secretKey = "";
			String bucketName = "";
			
			BucketInformation bucketInformation = BucketInformation.findBucketInformationBySemesterForImport(semesterID);
			
			if (bucketInformation != null)
			{
				accessKey = bucketInformation.getAccessKey() == null ? "" : bucketInformation.getAccessKey();
				secretKey = bucketInformation.getSecretKey() == null ? "" : bucketInformation.getSecretKey();
				bucketName = bucketInformation.getBucketName() == null ? "" : bucketInformation.getBucketName();
			}
			
			AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
			AmazonS3Client client;
			
			Properties properties =  new Properties();
			properties.load(OsMaFilePathConstant.class.getResourceAsStream("/META-INF/spring/proxy.properties"));			
			String proxyHost = properties.getProperty("proxy.host");
			String proxyPort = properties.getProperty("proxy.port");
			
			if (StringUtils.isNotBlank(proxyHost) && StringUtils.isNotBlank(proxyPort))
			{
				ClientConfiguration configuration = new ClientConfiguration();
				configuration.setProtocol(Protocol.HTTP);
				configuration.setProxyHost(proxyHost);
				configuration.setProxyPort(Integer.parseInt(proxyPort));
				client = new AmazonS3Client(credentials, configuration);
			}
			else  
			{
				client = new AmazonS3Client(credentials);			
			}
			
			ObjectListing objectListing = client.listObjects(bucketName.toLowerCase());
			
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries())
			{
				String fileName = objectSummary.getKey();
				 
				if (ExportOsceType.EOSCE.equals(osceType)) {
					if (StringUtils.startsWith(fileName, "00") == false && FilenameUtils.getExtension(fileName).equals(IMPORT_EOSCE_FILE_EXTENSION))
					{	
						fileName = fileName.replaceAll(" ", "_");
						String fullFilename = path + fileName;
						if (new File(fullFilename).exists() == true)
						{
							ExportOsceData exportOsceData = new ExportOsceData();
							exportOsceData.setFilename(objectSummary.getKey());
							exportOsceData.setFilepath(objectSummary.getKey());
							
							exportOsceDataList.add(exportOsceData);
							//fileList.add(objectSummary.getKey());					
						}
					}
				}
				
				else if (ExportOsceType.IOSCE.equals(osceType)) {
					if (FilenameUtils.getExtension(fileName).equals(IMPORT_IOSCE_FILE_EXTENSION))
					{	
						fileName = fileName.replaceAll(" ", "_");
						String fullFilename = path + fileName;
						if (new File(fullFilename).exists() == true)
						{
							ExportOsceData exportOsceData = new ExportOsceData();
							exportOsceData.setFilename(objectSummary.getKey());
							exportOsceData.setFilepath(objectSummary.getKey());
							
							exportOsceDataList.add(exportOsceData);
							//fileList.add(objectSummary.getKey());					
						}
					}
				}
			}
		}
		catch(AmazonServiceException ase)
		{
			Log.error(ase.getMessage());
			throw new eOSCESyncException(ase.getErrorType().toString(),ase.getMessage());
		}
		catch(AmazonClientException ace)
		{
			Log.error(ace.getMessage());
			throw new eOSCESyncException("",ace.getMessage());
		}
		catch(Exception e)
		{
			Log.error(e.getMessage());
			throw new eOSCESyncException("",e.getMessage());
		}
		
		return exportOsceDataList;
	
	}
	
	public List<ExportOsceData> findProcessedFilesFromSFTP(ExportOsceType osceType, Long semesterID) throws eOSCESyncException {
		List<ExportOsceData> exportOsceDataList = new ArrayList<ExportOsceData>(); 
		
		Semester semester = Semester.findSemester(semesterID);
		String path = OsMaFilePathConstant.IMPORT_PROCESSED_EOSCE_PATH + semester.getSemester() + semester.getCalYear();
    	if (ExportOsceType.EOSCE.equals(osceType)) {
    		path = path + OsMaFilePathConstant.EXPORT_EOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);	
    	}
    	else if (ExportOsceType.IOSCE.equals(osceType)) {
    		path = path + OsMaFilePathConstant.EXPORT_IOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
    	}
		
		BucketInformation bucketInformation = BucketInformation.findBucketInformationBySemesterForImport(semesterID);
		
		String SFTPHOST = bucketInformation == null ? "" : bucketInformation.getBucketName();
		int    SFTPPORT = 22;
		String SFTPUSER = (bucketInformation == null || bucketInformation.getAccessKey().isEmpty()) ? "" : bucketInformation.getAccessKey();
		String SFTPPASS = (bucketInformation == null || bucketInformation.getSecretKey().isEmpty()) ? "" : bucketInformation.getSecretKey();
		String SFTPWORKINGDIR = (bucketInformation == null || bucketInformation.getBasePath().isEmpty()) ? "" : bucketInformation.getBasePath();
		
		Session     session     = null;
		Channel     channel     = null;
		ChannelSftp channelSftp = null;
		
		try{
			JSch jsch = new JSch();
			session = jsch.getSession(SFTPUSER,SFTPHOST,SFTPPORT);
			session.setPassword(SFTPPASS);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp)channel;
			channelSftp.cd(SFTPWORKINGDIR);
				
			Vector<ChannelSftp.LsEntry> list = null;
			if (ExportOsceType.EOSCE.equals(osceType)) {
				list =  channelSftp.ls("*.*"); 
			}
			else if (ExportOsceType.IOSCE.equals(osceType)) {
				list =  channelSftp.ls("."); 
			}
			
			
			if (list != null && list.size() > 0)
			{
				for(ChannelSftp.LsEntry lsEntry : list) 
				{
					String fileOrDirName = lsEntry.getFilename();
					String originalFileName = fileOrDirName;
					
					if (ExportOsceType.EOSCE.equals(osceType)) {
						if (StringUtils.startsWith(fileOrDirName, "00") == false && FilenameUtils.getExtension(fileOrDirName).equals(IMPORT_EOSCE_FILE_EXTENSION))
						{	
							fileOrDirName = fileOrDirName.replaceAll(" ", "_");
							String fullFilename = path + fileOrDirName;
							if (new File(fullFilename).exists() == true)
							{
								ExportOsceData exportOsceData = new ExportOsceData();
								exportOsceData.setFilename(originalFileName);
								exportOsceData.setFilepath(originalFileName);
								
								exportOsceDataList.add(exportOsceData);
								//fileList.add(fileName);					
							}
						}
					}
					else if (ExportOsceType.IOSCE.equals(osceType)) {
						if (lsEntry.getAttrs().isDir())
						{
							if((StringUtils.equalsIgnoreCase(fileOrDirName, ".") == true || StringUtils.equalsIgnoreCase(fileOrDirName, "..") == true)) {
								continue;
							}
							
							String dir = SFTPWORKINGDIR;
							
							if (StringUtils.endsWith(dir, "/"))
								dir = dir + fileOrDirName;
							else
								dir = dir + "/" + fileOrDirName;
							
							channelSftp.cd(dir);
							
							Vector<ChannelSftp.LsEntry> sftpFileList = channelSftp.ls("*.*");
							
							for (ChannelSftp.LsEntry entry : sftpFileList)
							{
								String filename = entry.getFilename();
								
								if (FilenameUtils.getExtension(filename).equals(IMPORT_IOSCE_FILE_EXTENSION))
								{	
									String newFileName = filename.replaceAll(" ", "_"); 
									String fullFilename = path + newFileName;
									if (new File(fullFilename).exists() == true)
									{
										ExportOsceData exportOsceData = new ExportOsceData();
										exportOsceData.setFilename(filename);
										exportOsceData.setFilepath((fileOrDirName + "/" + filename));
										
										exportOsceDataList.add(exportOsceData);
										
										//fileList.add(fileName);					
									}
								}
							}	
						}						
					}
				}
			}	
		}
		catch (JSchException e) {
			Log.error(e.getMessage(), e);
			throw new eOSCESyncException("",e.getMessage());
		}
		catch (SftpException e) {
			Log.error(e.getMessage(), e);
			throw new eOSCESyncException("",e.getMessage());
		}			
		catch(Exception ex){
			Log.error(ex.getMessage(), ex);
			throw new eOSCESyncException("",ex.getMessage());
		}
		finally{
			if (channel != null)
				channel.disconnect();
			
			if (session != null)
				session.disconnect();
		}
		
		return exportOsceDataList;
	}
	
	public List<ExportOsceData> findUnProcessedFilesFromCloud(BucketInfoType bucketInfoType, ExportOsceType osceType, Long semesterID) throws eOSCESyncException {
		if (BucketInfoType.S3.equals(bucketInfoType)) {
			return findUnProcessedFilesFromS3(osceType, semesterID);
		}
		else if (BucketInfoType.FTP.equals(bucketInfoType)) {
			return findUnProcessedFilesFromSFTP(osceType, semesterID);
		}
		return new ArrayList<ExportOsceData>();
	}
	
	public List<ExportOsceData> findUnProcessedFilesFromS3(ExportOsceType osceType, Long semesterID) throws eOSCESyncException{
		List<String> fileList = new ArrayList<String>();
		List<ExportOsceData> exportOsceDataList = new ArrayList<ExportOsceData>();
		try
		{
			Semester semester = Semester.findSemester(semesterID);
			String path = OsMaFilePathConstant.IMPORT_PROCESSED_EOSCE_PATH + semester.getSemester() + semester.getCalYear();
	    	if (ExportOsceType.EOSCE.equals(osceType)) {
	    		path = path + OsMaFilePathConstant.EXPORT_EOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);	
	    	}
	    	else if (ExportOsceType.IOSCE.equals(osceType)) {
	    		path = path + OsMaFilePathConstant.EXPORT_IOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
	    	}
			
			String accessKey = "";
			String secretKey = "";
			String bucketName = "";
			
			BucketInformation bucketInformation = BucketInformation.findBucketInformationBySemesterForImport(semesterID);
			
			if (bucketInformation != null)
			{
				accessKey = bucketInformation.getAccessKey() == null ? "" : bucketInformation.getAccessKey();
				secretKey = bucketInformation.getSecretKey() == null ? "" : bucketInformation.getSecretKey();
				bucketName = bucketInformation.getBucketName() == null ? "" : bucketInformation.getBucketName();
			}
			
			AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
			AmazonS3Client client;
			
			Properties properties =  new Properties();
			properties.load(OsMaFilePathConstant.class.getResourceAsStream("/META-INF/spring/proxy.properties"));			
			String proxyHost = properties.getProperty("proxy.host");
			String proxyPort = properties.getProperty("proxy.port");
			
			if (StringUtils.isNotBlank(proxyHost) && StringUtils.isNotBlank(proxyPort))
			{
				ClientConfiguration configuration = new ClientConfiguration();
				configuration.setProtocol(Protocol.HTTP);
				configuration.setProxyHost(proxyHost);
				configuration.setProxyPort(Integer.parseInt(proxyPort));
				client = new AmazonS3Client(credentials, configuration);
			}
			else  
			{
				client = new AmazonS3Client(credentials);			
			}
			
			ObjectListing objectListing = client.listObjects(bucketName.toLowerCase());
			
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries())
			{
				String fileName = objectSummary.getKey();
				
				if (ExportOsceType.EOSCE.equals(osceType)) {
					if (StringUtils.startsWith(fileName, "00") == false && FilenameUtils.getExtension(fileName).equals(IMPORT_EOSCE_FILE_EXTENSION))
					{
						fileName = fileName.replaceAll(" ", "_");
						String fullFilename = path + fileName;
						if (new File(fullFilename).exists() == false)
						{
							ExportOsceData exportOsceData = new ExportOsceData();
							exportOsceData.setFilename(objectSummary.getKey());
							exportOsceData.setFilepath(objectSummary.getKey());
							
							exportOsceDataList.add(exportOsceData);
							
							//fileList.add(objectSummary.getKey());					
						}
					}
				}
				else if (ExportOsceType.IOSCE.equals(osceType)) {
					if (StringUtils.startsWith(fileName, "00") == false && FilenameUtils.getExtension(fileName).equals(IMPORT_IOSCE_FILE_EXTENSION))
					{
						fileName = fileName.replaceAll(" ", "_");
						String fullFilename = path + fileName;
						if (new File(fullFilename).exists() == false)
						{
							ExportOsceData exportOsceData = new ExportOsceData();
							exportOsceData.setFilename(objectSummary.getKey());
							exportOsceData.setFilepath(objectSummary.getKey());
							
							exportOsceDataList.add(exportOsceData);
							//fileList.add(objectSummary.getKey());					
						}
					}
				}
			}
		}
		catch(AmazonServiceException ase)
		{
			Log.error(ase.getMessage());
			throw new eOSCESyncException(ase.getErrorType().toString(),ase.getMessage());
		}
		catch(AmazonClientException ace)
		{
			Log.error(ace.getMessage());
			throw new eOSCESyncException("",ace.getMessage());
		}
		catch(Exception e)
		{
			Log.error(e.getMessage());
			throw new eOSCESyncException("",e.getMessage());
		}
		
		//return fileList;
		
		return exportOsceDataList;
	
	}
	
	public List<ExportOsceData> findUnProcessedFilesFromSFTP(ExportOsceType osceType, Long semesterID) throws eOSCESyncException {
		List<ExportOsceData> exportOsceDataList = new ArrayList<ExportOsceData>();
		
		Semester semester = Semester.findSemester(semesterID);
		String path = OsMaFilePathConstant.IMPORT_PROCESSED_EOSCE_PATH + semester.getSemester() + semester.getCalYear();
    	if (ExportOsceType.EOSCE.equals(osceType)) {
    		path = path + OsMaFilePathConstant.EXPORT_EOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);	
    	}
    	else if (ExportOsceType.IOSCE.equals(osceType)) {
    		path = path + OsMaFilePathConstant.EXPORT_IOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
    	}
		
		BucketInformation bucketInformation = BucketInformation.findBucketInformationBySemesterForImport(semesterID);
		
		String SFTPHOST = bucketInformation == null ? "" : bucketInformation.getBucketName();
		int    SFTPPORT = 22;
		String SFTPUSER = (bucketInformation == null || bucketInformation.getAccessKey().isEmpty()) ? "" : bucketInformation.getAccessKey();
		String SFTPPASS = (bucketInformation == null || bucketInformation.getSecretKey().isEmpty()) ? "" : bucketInformation.getSecretKey();
		String SFTPWORKINGDIR = (bucketInformation == null || bucketInformation.getBasePath().isEmpty()) ? "" : bucketInformation.getBasePath();
		
		Session     session     = null;
		Channel     channel     = null;
		ChannelSftp channelSftp = null;
		
		try{
			JSch jsch = new JSch();
			session = jsch.getSession(SFTPUSER,SFTPHOST,SFTPPORT);
			session.setPassword(SFTPPASS);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp)channel;
			channelSftp.cd(SFTPWORKINGDIR);
				
			Vector<ChannelSftp.LsEntry> list = null;
			if (ExportOsceType.EOSCE.equals(osceType)) {
				list =  channelSftp.ls("*.*"); 
			}
			else if (ExportOsceType.IOSCE.equals(osceType)) {
				list =  channelSftp.ls("."); 
			}
			
			if (list != null && list.size() > 0)
			{
				for(ChannelSftp.LsEntry lsEntry : list) 
				{
					String fileOrDirName = lsEntry.getFilename();
					String fileName = fileOrDirName;
					
					if (ExportOsceType.EOSCE.equals(osceType)) {
						if (StringUtils.startsWith(fileOrDirName, "00") == false && FilenameUtils.getExtension(fileOrDirName).equals(IMPORT_EOSCE_FILE_EXTENSION))
						{	
							fileOrDirName = fileOrDirName.replaceAll(" ", "_");
							String fullFilename = path + fileOrDirName;
							if (new File(fullFilename).exists() == false)
							{
								ExportOsceData exportOsceData = new ExportOsceData();
								exportOsceData.setFilename(fileName);
								exportOsceData.setFilepath(fileName);
								
								exportOsceDataList.add(exportOsceData);
								
								//fileList.add(fileOrDirName);					
							}
						}
					}
					else if (ExportOsceType.IOSCE.equals(osceType)) {
						if (lsEntry.getAttrs().isDir())
						{
							if((StringUtils.equalsIgnoreCase(fileOrDirName, ".") == true || StringUtils.equalsIgnoreCase(fileOrDirName, "..") == true)) {
								continue;
							}
							
							String dir = SFTPWORKINGDIR;
							
							if (StringUtils.endsWith(dir, "/"))
								dir = dir + fileOrDirName;
							else
								dir = dir + "/" + fileOrDirName;
							
							channelSftp.cd(dir);
							
							Vector<ChannelSftp.LsEntry> sftpFileList = channelSftp.ls("*.*");
							
							for (ChannelSftp.LsEntry entry : sftpFileList)
							{
								String filename = entry.getFilename();
								
								if (FilenameUtils.getExtension(filename).equals(IMPORT_IOSCE_FILE_EXTENSION))
								{	
									String newFileName = filename.replaceAll(" ", "_"); 
									String fullFilename = path + newFileName;
									if (new File(fullFilename).exists() == false)
									{
										ExportOsceData exportOsceData = new ExportOsceData();
										exportOsceData.setFilename(filename);
										exportOsceData.setFilepath((fileOrDirName + "/" + filename));
										
										exportOsceDataList.add(exportOsceData);
										
										//fileList.add(fileName);					
									}
								}
							}	
						}						
					/*
						
						if (FilenameUtils.getExtension(fileOrDirName).equals(IMPORT_IOSCE_FILE_EXTENSION))
						{	
							fileOrDirName = fileOrDirName.replaceAll(" ", "_");
							String fullFilename = path + fileOrDirName;
							if (new File(fullFilename).exists() == false)
							{
								fileList.add(fileOrDirName);					
							}
						}*/
					}
				}
			}	
		}
		catch (JSchException e) {
			Log.error(e.getMessage(), e);
			throw new eOSCESyncException("",e.getMessage());
		}
		catch (SftpException e) {
			Log.error(e.getMessage(), e);
			throw new eOSCESyncException("",e.getMessage());
		}			
		catch(Exception ex){
			Log.error(ex.getMessage(), ex);
			throw new eOSCESyncException("",ex.getMessage());
		}
		finally{
			if (channel != null)
				channel.disconnect();
			
			if (session != null)
				session.disconnect();
		}
		
		return exportOsceDataList;
	}
	
	public void importFileFromLocal(ExportOsceType osceType, Long semesterID, List<String> fileList) throws eOSCESyncException{
		
		try{
			BucketInformation bucketInformation = BucketInformation.findBucketInformation(semesterID);
			
		     Semester semester = Semester.findSemester(semesterID);
			 String path = OsMaFilePathConstant.IMPORT_PROCESSED_EOSCE_PATH + semester.getSemester() + semester.getCalYear();
	    	 if (ExportOsceType.EOSCE.equals(osceType)) {
	    	 	path = path + OsMaFilePathConstant.EXPORT_EOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);	
	    	 }
	    	 else if (ExportOsceType.IOSCE.equals(osceType)) {
	    	 	path = path + OsMaFilePathConstant.EXPORT_IOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
	    	 }
	    	 boolean resultFlag = true;
	    	 
	 		 for (String fullFilename : fileList) {
	 			 String filename = FilenameUtils.getName(fullFilename);
	 			 File file = new File(fullFilename);
	 			 
	 			 if (file.exists()) {
	 				 byte[] byteArray = FileUtils.readFileToByteArray(file);
	 	 			 
	 	 			 if (ExportOsceType.EOSCE.equals(osceType) && FilenameUtils.getExtension(filename).equals(IMPORT_EOSCE_FILE_EXTENSION)) {
	 	 				String encryptionKey = bucketInformation.getEncryptionKey();
	 	 				String secretKey = bucketInformation.getSecretKey();
	 	 				
	 	 				String symmetricKey = "";
	 	 				if (StringUtils.isNotBlank(encryptionKey) && encryptionKey.length() >= 16)
	 	 					symmetricKey = encryptionKey.substring(0, 16);
	 	 				else
	 	 					symmetricKey = secretKey.substring(0, 16);
	 	 				
	 	 				String decFileName = S3Decryptor.decrypt(symmetricKey, filename, path);
	 	 				
	 	 				resultFlag = resultFlag & importEOSCE(decFileName);
	 	 			 }
	 	 			 else if (ExportOsceType.IOSCE.equals(osceType) && FilenameUtils.getExtension(filename).equals(IMPORT_IOSCE_FILE_EXTENSION)) {
	 	 				NSDictionary rootDict = (NSDictionary) PropertyListParser.parse(byteArray);
	 	 				NSObject submit = rootDict.objectForKey(IMPORT_ANSWER_PLIST_SUBMIT);
	 	 				NSString submitData = (NSString) submit;
	 	 				if (submitData.getContent().equals("1")) {
	 	 					boolean importiOSCEFlag = importIOSCE(fullFilename, semesterID);
	 	 					resultFlag = resultFlag & importiOSCEFlag;
	 	 				}else{
							throw new eOSCESyncException("Invalid file","Invalid file with submit tag 0");
						}
	 	 			 }
	 			 }
	 			 	
	 		 }
	 		 
	 		 if (resultFlag == false) {
	 			throw new eOSCESyncException("", "");
	 		 }
		}
		catch (Exception e) {
			throw new eOSCESyncException("", e.getMessage());
		} 	
	}
	
	public void importFileFromCloud(BucketInfoType bucketInfoType, ExportOsceType osceType, Long semesterID, List<String> fileList) throws eOSCESyncException{
		if (BucketInfoType.S3.equals(bucketInfoType)) {
			importFileFromS3(osceType, semesterID, fileList);
		}
		else if (BucketInfoType.FTP.equals(bucketInfoType)) {
			importFileFromSFTP(osceType, semesterID, fileList);
		}
	}
	
	public void importFileFromS3(ExportOsceType osceType, Long semesterID, List<String> fileList) throws eOSCESyncException{
		try
		{
			Semester semester = Semester.findSemester(semesterID);
			String path = OsMaFilePathConstant.IMPORT_PROCESSED_EOSCE_PATH + semester.getSemester() + semester.getCalYear();
	    	if (ExportOsceType.EOSCE.equals(osceType)) {
	    		path = path + OsMaFilePathConstant.EXPORT_EOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);	
	    	}
	    	else if (ExportOsceType.IOSCE.equals(osceType)) {
	    		path = path + OsMaFilePathConstant.EXPORT_IOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
	    	}
	    	
			String accessKey = "";
			String secretKey = "";
			String bucketName = "";
			String encryptionKey = "";
			
			BucketInformation bucketInformation = BucketInformation.findBucketInformationBySemesterForImport(semesterID);
			
			if (bucketInformation != null)
			{
				accessKey = bucketInformation.getAccessKey() == null ? "" : bucketInformation.getAccessKey();
				secretKey = bucketInformation.getSecretKey() == null ? "" : bucketInformation.getSecretKey();
				bucketName = bucketInformation.getBucketName() == null ? "" : bucketInformation.getBucketName();
				encryptionKey = bucketInformation.getEncryptionKey() == null ? "" : bucketInformation.getEncryptionKey();
			}
			
			AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
			AmazonS3Client client;
			
			Properties properties =  new Properties();
			properties.load(OsMaFilePathConstant.class.getResourceAsStream("/META-INF/spring/proxy.properties"));			
			String proxyHost = properties.getProperty("proxy.host");
			String proxyPort = properties.getProperty("proxy.port");
			
			if (StringUtils.isNotBlank(proxyHost) && StringUtils.isNotBlank(proxyPort))
			{
				ClientConfiguration configuration = new ClientConfiguration();
				configuration.setProtocol(Protocol.HTTP);
				configuration.setProxyHost(proxyHost);
				configuration.setProxyPort(Integer.parseInt(proxyPort));
				client = new AmazonS3Client(credentials, configuration);
			}
			else  
			{
				client = new AmazonS3Client(credentials);			
			}	
			
			S3Object object = null;			
			boolean deleteFlag = true;
			
			for (int i=0; i<fileList.size(); i++)
			{
				 object = client.getObject(new GetObjectRequest(bucketName, fileList.get(i)));
				 InputStream inputStream = object.getObjectContent();
				 byte[] byteArray = IOUtils.toByteArray(inputStream);
								 
				 if (ExportOsceType.EOSCE.equals(osceType) && FilenameUtils.getExtension(object.getKey()).equals(IMPORT_EOSCE_FILE_EXTENSION)) {
					//import in answer table is done from add file
					deleteFlag = deleteFlag & addFile(osceType, path, object.getKey(), byteArray, secretKey, encryptionKey, semesterID);
				 }
				 else if (ExportOsceType.IOSCE.equals(osceType) && FilenameUtils.getExtension(fileList.get(i)).equals(IMPORT_IOSCE_FILE_EXTENSION)) {
					NSDictionary rootDict = (NSDictionary) PropertyListParser.parse(byteArray);
					NSObject submit = rootDict.objectForKey(IMPORT_ANSWER_PLIST_SUBMIT);
					NSString submitData = (NSString) submit;
					if (submitData.getContent().equals("1")) {
						/*deleteFlag = deleteFlag & */addFile(osceType, path, object.getKey(), byteArray, secretKey, encryptionKey, semesterID);
					}else{
						throw new eOSCESyncException("Invalid file","Invalid file with submit tag 0");
					}
				 }		
				 		
			}
		
			/*if (flag == true && deleteFlag == true) {
				deleteAmzonS3Object(osceType, semesterID, fileList, bucketName, accessKey, secretKey);
			}*/
		}
		catch(AmazonServiceException ase)
		{
			Log.error(ase.getMessage(),ase);
			throw new eOSCESyncException(ase.getErrorType().toString(),ase.getMessage());
		}
		catch(AmazonClientException ace)
		{
			Log.error(ace.getMessage(),ace);
			throw new eOSCESyncException("",ace.getMessage());
		}
		catch(Exception e)
		{
			Log.error(e.getMessage(),e);
			throw new eOSCESyncException("",e.getMessage());
		}
	}
	
	public void importFileFromSFTP(ExportOsceType osceType, Long semesterID, List<String> fileList) throws eOSCESyncException {
		
		Semester semester = Semester.findSemester(semesterID);
		String path = OsMaFilePathConstant.IMPORT_PROCESSED_EOSCE_PATH + semester.getSemester() + semester.getCalYear();
    	if (ExportOsceType.EOSCE.equals(osceType)) {
    		path = path + OsMaFilePathConstant.EXPORT_EOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);	
    	}
    	else if (ExportOsceType.IOSCE.equals(osceType)) {
    		path = path + OsMaFilePathConstant.EXPORT_IOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
    	}
		
		BucketInformation bucketInformation = BucketInformation.findBucketInformationBySemesterForImport(semesterID);
		
		String SFTPHOST = bucketInformation == null ? "" : bucketInformation.getBucketName();
		int    SFTPPORT = 22;
		String SFTPUSER = (bucketInformation == null || bucketInformation.getAccessKey().isEmpty()) ? "" : bucketInformation.getAccessKey();
		String SFTPPASS = (bucketInformation == null || bucketInformation.getSecretKey().isEmpty()) ? "" : bucketInformation.getSecretKey();
		String SFTPWORKINGDIR = (bucketInformation == null || bucketInformation.getBasePath().isEmpty()) ? "" : bucketInformation.getBasePath();
		
		Session     session     = null;
		Channel     channel     = null;
		ChannelSftp channelSftp = null;
		
		try{
			JSch jsch = new JSch();
			session = jsch.getSession(SFTPUSER,SFTPHOST,SFTPPORT);
			session.setPassword(SFTPPASS);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp)channel;
			channelSftp.cd(SFTPWORKINGDIR);
				
			Vector<ChannelSftp.LsEntry> list =  channelSftp.ls("*.*");
			
			String dir = SFTPWORKINGDIR;
			if (dir.endsWith("/") == false) {
				dir = dir + "/";
			}
			
			/*if (list.size() > 0)
			{*/
				boolean deleteFlag = true;
				for (int i=0; i<fileList.size(); i++)
				{
					String fullFilePath = dir + fileList.get(i);
					String fileName = FilenameUtils.getName(fullFilePath);
					InputStream inputStream = channelSftp.get(fullFilePath);
					byte[] byteArray = IOUtils.toByteArray(inputStream);
					
					if (ExportOsceType.EOSCE.equals(osceType) && FilenameUtils.getExtension(fileList.get(i)).equals(IMPORT_EOSCE_FILE_EXTENSION)) {
						//import in answer table is done from add file
						deleteFlag = deleteFlag & addFile(osceType, path, fileName, byteArray, bucketInformation.getSecretKey(), bucketInformation.getEncryptionKey(), semesterID);
					}
					else if (ExportOsceType.IOSCE.equals(osceType) && FilenameUtils.getExtension(fileList.get(i)).equals(IMPORT_IOSCE_FILE_EXTENSION)) {
						NSDictionary rootDict = (NSDictionary) PropertyListParser.parse(byteArray);
						NSObject submit = rootDict.objectForKey(IMPORT_ANSWER_PLIST_SUBMIT);
						NSString submitData = (NSString) submit;
						if (submitData.getContent().equals("1")) {
							inputStream = channelSftp.get(fileList.get(i));
							/*deleteFlag = deleteFlag &*/ addFile(osceType, path, fileName, byteArray, bucketInformation.getSecretKey(), bucketInformation.getEncryptionKey(), semesterID);
						}else{
							throw new eOSCESyncException("Invalid file","Invalid file with submit tag 0");
						}
					}		
				}
			
				/*if (flag == true && deleteFlag == true) {
					deleteSFTPObject(osceType, semesterID, fileList);
				}*/
			//}	
		}
		catch (JSchException e) {
			Log.error(e.getMessage(), e);
			throw new eOSCESyncException("",e.getMessage());
		}
		catch (SftpException e) {
			Log.error(e.getMessage(), e);
			throw new eOSCESyncException("",e.getMessage());
		}			
		catch(Exception ex){
			Log.error(ex.getMessage(), ex);
			throw new eOSCESyncException("",ex.getMessage());
		}
		finally{
			if (channel != null)
				channel.disconnect();
			
			if (session != null)
				session.disconnect();
		}
	}
	
	public void deleteFileFromCloud(ExportOsceType osceType, BucketInfoType bucketInfoType, Long semesterID, List<String> fileList) throws eOSCESyncException {
		BucketInformation bucketInformation = BucketInformation.findBucketInformationBySemesterForImport(semesterID);
		
		if (BucketInfoType.S3.equals(bucketInfoType) && bucketInformation != null) {
			deleteAmzonS3Object(osceType, semesterID, fileList, bucketInformation.getBucketName(), bucketInformation.getAccessKey(), bucketInformation.getSecretKey());
		}
		else if (BucketInfoType.FTP.equals(bucketInfoType)) {
			deleteSFTPObject(osceType, semesterID, fileList);
		}
	}
	
	public void deleteSFTPObject(ExportOsceType osceType, Long semesterID, List<String> fileList) throws eOSCESyncException {
		Semester semester = Semester.findSemester(semesterID);
		String path = OsMaFilePathConstant.IMPORT_PROCESSED_EOSCE_PATH + semester.getSemester() + semester.getCalYear();
    	if (ExportOsceType.EOSCE.equals(osceType)) {
    		path = path + OsMaFilePathConstant.EXPORT_EOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);	
    	}
    	else if (ExportOsceType.IOSCE.equals(osceType)) {
    		path = path + OsMaFilePathConstant.EXPORT_IOSCE + (isLocal==true ? folderSeparatorLocal : folderSeparatorProduction);
    	}
    	
    	BucketInformation bucketInformation = BucketInformation.findBucketInformationBySemesterForImport(semesterID);
		
		String SFTPHOST = bucketInformation == null ? "" : bucketInformation.getBucketName();
		int    SFTPPORT = 22;
		String SFTPUSER = (bucketInformation == null || bucketInformation.getAccessKey().isEmpty()) ? "" : bucketInformation.getAccessKey();
		String SFTPPASS = (bucketInformation == null || bucketInformation.getSecretKey().isEmpty()) ? "" : bucketInformation.getSecretKey();
		String SFTPWORKINGDIR = (bucketInformation == null || bucketInformation.getBasePath().isEmpty()) ? "" : bucketInformation.getBasePath();
		
		Session     session     = null;
		Channel     channel     = null;
		ChannelSftp channelSftp = null;
		
		try{
			JSch jsch = new JSch();
			session = jsch.getSession(SFTPUSER,SFTPHOST,SFTPPORT);
			session.setPassword(SFTPPASS);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp)channel;
			channelSftp.cd(SFTPWORKINGDIR);
				
			Vector<ChannelSftp.LsEntry> list =  channelSftp.ls("*.*");
			
			/*if (list.size() > 0)
			{*/
				for (int i=0; i<fileList.size(); i++)
				{
					String filename = fileList.get(i);
					String fileName = fileList.get(i);
					fileName = fileName.replaceAll(" ", "_");
					File file = new File(fileName);
					
					if (file.exists() == true)
					{
						file.delete();
					}
					//System.out.println("DELETED : " + fileName);
					//write bucket name
					//channelSftp.rm(filename);
				}
			//}	
		}
		catch (JSchException e) {
			Log.error(e.getMessage(), e);
			throw new eOSCESyncException("",e.getMessage());
		}
		catch (SftpException e) {
			Log.error(e.getMessage(), e);
			throw new eOSCESyncException("",e.getMessage());
		}			
		catch(Exception ex){
			Log.error(ex.getMessage(), ex);
			throw new eOSCESyncException("",ex.getMessage());
		}
		finally{
			if (channel != null)
				channel.disconnect();
			
			if (session != null)
				session.disconnect();
		}
	}
	
	private ByteArrayOutputStream decryptFileWithAdminPrivateKey(byte[] bytes, BucketInformation bucketInformation)
	{
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		try
		{	
			KeyPair keyPair = KeyPair.findActiveAdminKeyPair();
			ByteArrayOutputStream generateDecryptedDataFile = null;
			if (keyPair.getPrivateKey() != null)
			{
				PrivateKey privateKey = getPrivateKeyForDecrypt(keyPair.getPrivateKey(), bucketInformation.getEncryptionKey());
				
				if(privateKey==null)
				{
					return bout;
				}
				
				NSDictionary rootDict = (NSDictionary) PropertyListParser.parse(bytes);
				NSObject encryptedObject = rootDict.objectForKey(IMPORT_ANSWER_PLIST_DATA);
				NSData encryptedData = (NSData) encryptedObject;
				
				generateDecryptedDataFile = generateDecryptedDataFile(encryptedData.bytes(), privateKey);
				
				NSObject signMechanismObject = rootDict.objectForKey(IMPORT_ANSWER_PLIST_SIGN_MECHANISM);
				NSString signMechanismData = (NSString) signMechanismObject;
				
				if (generateDecryptedDataFile != null && signMechanismData.getContent().equals(String.valueOf(EncryptionType.ASYM.ordinal()))) {
					
					//Need to discuss with team.
					/*NSObject shaHashObject = rootDict.objectForKey(IMPORT_ANSWER_PLIST_SHA_CODE);
					NSData shaHashValue = (NSData) shaHashObject;
					
					if (shaHashValue == null) {
						generateDecryptedDataFile = null;
					}
					else {
						JSONDeserializer<StudentAnswer> deserializer = new JSONDeserializer<StudentAnswer>().use("values", StudentAnswer.class);				
						StudentAnswer studentAnswer = new StudentAnswer();
						studentAnswer = deserializer.deserializeInto(new InputStreamReader(new ByteArrayInputStream(generateDecryptedDataFile.toByteArray())), studentAnswer);
						
						if (studentAnswer.getPublicKey() != null) {
							PublicKey publicKey = generatePublicKey(studentAnswer.getPublicKey());
							boolean isSigned = generateSignData(publicKey, shaHashValue.bytes(), generateDecryptedDataFile.toByteArray());
							if (isSigned == false)
								generateDecryptedDataFile = null;
						}
					}*/
				}
				else if (generateDecryptedDataFile != null && signMechanismData.getContent().equals(EncryptionType.HASH.ordinal())) {
					NSObject shaHashObject = rootDict.objectForKey(IMPORT_ANSWER_PLIST_SHA_CODE);
					NSString shaHashValue = (NSString) shaHashObject;
					
					byte[] calculatedHashValue = checkSignedWithSHA256(generateDecryptedDataFile.toByteArray());
					String base64CalculatedHashValue = Base64.encodeBase64String(calculatedHashValue);
					
					if (shaHashValue == null || base64CalculatedHashValue == null) {
						generateDecryptedDataFile = null;
					}
					else if (shaHashValue.getContent().equals(base64CalculatedHashValue) == false) {
						generateDecryptedDataFile = null;
					}
				}
			}
			
			return generateDecryptedDataFile;
		}
		catch (Exception e) {
			Log.error(e.getMessage(), e);
		}
		return bout;
	}
	
	private byte[] checkSignedWithSHA256(byte[] bytes) {
		try
		{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(bytes);
			/*FileInputStream fis = new FileInputStream(bytes);
			byte[] dataBytes = new byte[1024];
			
			int nread = 0; 
	        while ((nread = fis.read(dataBytes)) != -1) {
	          md.update(dataBytes, 0, nread);
	        };*/
	        byte[] mdbytes = md.digest();
	        
	        return mdbytes;
		}
		catch (Exception e) {
			Log.error(e.getMessage(), e);
		}
		return null;
	}
	
	private PrivateKey getPrivateKeyForDecrypt(String privateKeyFormDB, String certificatePassword)
	{
		PrivateKey privateKey=null;
		try {
			KeyStore keyStoreForPrivateKey = KeyStore.getInstance("PKCS12");
			ByteArrayInputStream bin = new ByteArrayInputStream(Base64.decodeBase64(privateKeyFormDB));
			keyStoreForPrivateKey.load(bin, certificatePassword.toCharArray());
			
			privateKey = (PrivateKey) keyStoreForPrivateKey.getKey(OsMaFilePathConstant.CERTIFICATE_ALIAS, certificatePassword.toCharArray());
		} catch (Exception e) {
			Log.info("private key generation fail");
			e.printStackTrace();
		}
		
		return privateKey;
	}
	
	private static ByteArrayOutputStream generateDecryptedDataFile(byte[] encryptedData, PrivateKey privateKey) throws Exception
	{
		ByteArrayInputStream inStream = new ByteArrayInputStream(encryptedData);
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		try {					
			Cipher dCipher = Cipher.getInstance("RSA");
			dCipher.init(Cipher.DECRYPT_MODE, privateKey);
		
			int nRead = 0;
			byte[] data = new byte[256];
			while ((nRead = inStream.read(data, 0, data.length)) != -1) {

				byte[] cipherData = dCipher.doFinal(data, 0, nRead);

				bout.write(cipherData);
			}

			bout.flush();
			bout.close();
	
			return bout;
			
		}catch(Exception e) {
			Log.error(e.getMessage(), e);
		}
		
		Log.info("Encryted data file  generation success");	
		return bout;			
	}
	
	public static boolean generateSignData(PublicKey publicKey, byte[] signedData, byte[] decryptedDataBytes)
	{
		boolean isSign=false;
		try
		{						
			java.security.Signature sig = java.security.Signature.getInstance(OsMaFilePathConstant.SIGN_ALGO,"BC");
			sig.initVerify(publicKey);
			
			ByteArrayInputStream fis = new ByteArrayInputStream(decryptedDataBytes);
			byte[] dataBytes = new byte[1048];
			int nread = fis.read(dataBytes);
			while (nread > 0) {
				sig.update(dataBytes, 0, nread);
				nread = fis.read(dataBytes);
			}
			
			isSign = sig.verify(signedData);
			
		}catch(Exception e) {
			Log.error(e.getMessage(), e);
		}
		
		return isSign;
	}
	
	public static PublicKey generatePublicKey(String examinerPublicKey)
	{
		PublicKey publicKey =null;
		try {
		
			ByteArrayInputStream inStream = new ByteArrayInputStream(examinerPublicKey.getBytes());	
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			BufferedInputStream bis = new BufferedInputStream(inStream);
			java.security.cert.Certificate cert = cf.generateCertificate(bis);
			
			publicKey = cert.getPublicKey();
			
		}catch(Exception e) {
			Log.error(e.getMessage(), e);
		}
		return publicKey;
	}
	
}
