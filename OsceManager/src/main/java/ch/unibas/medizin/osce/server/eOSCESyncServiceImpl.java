package ch.unibas.medizin.osce.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.eOSCESyncException;
import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.eOSCESyncService;
import ch.unibas.medizin.osce.domain.Answer;
import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.domain.CheckList;
import ch.unibas.medizin.osce.domain.ChecklistCriteria;
import ch.unibas.medizin.osce.domain.ChecklistOption;
import ch.unibas.medizin.osce.domain.ChecklistQuestion;
import ch.unibas.medizin.osce.domain.ChecklistTopic;
import ch.unibas.medizin.osce.domain.Course;
import ch.unibas.medizin.osce.domain.Doctor;
import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.domain.OsceDay;
import ch.unibas.medizin.osce.domain.OscePost;
import ch.unibas.medizin.osce.domain.OscePostRoom;
import ch.unibas.medizin.osce.domain.OsceSequence;
import ch.unibas.medizin.osce.domain.StandardizedRole;
import ch.unibas.medizin.osce.domain.Student;
import ch.unibas.medizin.osce.server.i18n.GWTI18N;
import ch.unibas.medizin.osce.shared.i18n.OsceConstantsWithLookup;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.Region;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
public class eOSCESyncServiceImpl extends RemoteServiceServlet implements eOSCESyncService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger Log = Logger.getLogger(eOSCESyncServiceImpl.class);
	private static String appUploadDirectory= OsMaFilePathConstant.DEFAULT_IMPORT_EOSCE_PATH;
	
	
	public List<String> processedFileList() throws eOSCESyncException
	{
		List<String> fileList = new ArrayList<String>();
		try
		{
			//write access and secret key
			//AWSCredentials credentials = new BasicAWSCredentials("", "");
			AmazonS3Client client = new AmazonS3Client(new PropertiesCredentials(eOSCESyncServiceImpl.class.getResourceAsStream("AwsCredentials.properties")));
			//write bucket name in ""
			ObjectListing objectListing = client.listObjects("");
			
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries())
			{
				if (fileExist(objectSummary.getKey()))
				{
					fileList.add(objectSummary.getKey());
					
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
		
		//System.out.println("~~PROCESSED FILE SIZE : " + fileList.size());
		return fileList;
	}
	
	public List<String> unprocessedFileList() throws eOSCESyncException
	{
		List<String> fileList = new ArrayList<String>();
		try
		{
			//write access and secret key.
			//AWSCredentials credentials = new BasicAWSCredentials("", "");
			AmazonS3Client client = new AmazonS3Client(new PropertiesCredentials(eOSCESyncServiceImpl.class.getResourceAsStream("AwsCredentials.properties")));
			S3Object object = null;
			//write bucket name
			ObjectListing objectListing = client.listObjects("");
			
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries())
			{
				if (!fileExist(objectSummary.getKey()))
				{
					fileList.add(objectSummary.getKey());					
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
		
		//System.out.println("~~UNPROCESSED FILE SIZE : " + fileList.size());
		return fileList;
	}

	@Override
	public void deleteAmzonS3Object(List<String> fileList)
			throws eOSCESyncException {
		try
		{
			//write access and secret key
			//AWSCredentials credentials = new BasicAWSCredentials("", "");
			AmazonS3Client client = new AmazonS3Client(new PropertiesCredentials(eOSCESyncServiceImpl.class.getResourceAsStream("AwsCredentials.properties")));
			
			for (int i=0; i<fileList.size(); i++)
			{
				//System.out.println("KEY NAME : " + fileList.get(i));
				//write bucket name
				client.deleteObject("", fileList.get(i));
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
	
	public void addFile(String filename, InputStream input)
	{
		//String cntxtPath = getServletContext().getRealPath(".");
		
		String path = appUploadDirectory;
		
		filename = path + filename;
				
		Boolean success = false;
		
	/*	if (!(new File(path).exists()))
			success = new File(path).mkdir(); */
		
		if (success)
		{
			//System.out.println("##Success##");
		}
		
		try
		{
			File file = new File(filename);
			FileUtils.touch(file);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		
			while(true)
			{
				String line = reader.readLine();
				if (line == null) break;
			
				writer.write(line);
			}
					
			writer.close();
			reader.close();
			
			//importEOSCE(filename);
		}
		catch(Exception e)
		{
			Log.error(e.getMessage());
		} 
		
	}
	
	public Boolean fileExist(String fileName)
	{
		String cntxt = appUploadDirectory;
		//System.out.println("CONTEXT : " + cntxt);
		cntxt = cntxt + fileName;
		//System.out.println("FILENAME : " + cntxt);
	
		File folder = new File(cntxt);
		//System.out.println("EXIST : " + folder.exists());
		
		return folder.exists();
	}
	
	public void importFileList(List<String> fileList, Boolean flag) throws eOSCESyncException
	{
		try
		{
			//System.out.println("in server side");
			/*File folder = new File(OsMaConstant.IMPORT_EOSCE_FILEPATH);
			File[] listOfFiles = folder.listFiles();
			//System.out.println("Lenght : " + listOfFiles.length);
			for (int i=0; i<listOfFiles.length; i++)
			{
				fileList.add(OsMaConstant.IMPORT_EOSCE_FILEPATH + listOfFiles[i].getName());
			}
			
			//System.out.println("in server side1");
			//System.out.println("FILE LIST SIZE : " + fileList.size());
			for (int i=0; i<fileList.size(); i++)
			{
				System.out.println("FILE NAME : " + fileList.get(i));
				//importEOSCE(fileList.get(i));
			}*/
			//write access and secret key
			//AWSCredentials credentials = new BasicAWSCredentials("", "");
			AmazonS3Client client = new AmazonS3Client(new PropertiesCredentials(eOSCESyncServiceImpl.class.getResourceAsStream("AwsCredentials.properties")));
			S3Object object = null;
			
			for (int i=0; i<fileList.size(); i++)
			{
				//System.out.println("FILENAME : " + fileList.get(i));
				//write bucket name
				object = client.getObject(new GetObjectRequest("", fileList.get(i)));
				addFile(object.getKey(), object.getObjectContent());	
				//import is done from add file
			}
		
			if (flag == true)
			{
				deleteAmzonS3Object(fileList);
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
	
	public void importEOSCE(String filename)
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
			
			String sql = "SELECT  cand.zcandidateid, ex.zexaminerid, que.zquestionid, opt.zvalue, st.zstationid, ans.zanswerquestion, ans.ztimestamp, ans.zanswerassessment FROM zanswer ans , zassessment ass, zschedule sch, z_1answeroptions ansopt, zoption opt, zcandidate cand, zquestion que, zstation st, zexaminer ex"
					+ " WHERE ans.zanswerassessment = ass.z_pk"
					+ " and  ass.zschedule = sch.z_pk"
					+ " and ansopt.z_1optionanswers = ans.z_pk"
					+ " and opt.z_pk = ansopt.z_8answeroptions"
					+ " and sch.zcandidate = cand.z_pk"
					+ " and sch.zstation = st.z_pk"
					+ " and st.zexaminer = ex.z_pk"
					+ " and ans.zanswerquestion = que.z_pk"
					+ " order by sch.zcandidate asc";
			
			ResultSet resultset = statement.executeQuery(sql);
			
			ResultSet rs;
			
			long candidateId, criteriaId, optionId, questionId, osmaid, roomid = 0, examinerid = 0;
			long tempcandidateid, tempquestionid, tempexaminerid = 0;
			String optionvalue;
			Answer answerTable;
			
			while (resultset.next())
			{				
				candidateId = Long.parseLong(resultset.getString(1));
				examinerid = Long.parseLong(resultset.getString(2));
				questionId = Long.parseLong(resultset.getString(3));
				optionvalue = String.valueOf((int)resultset.getFloat(4));
				roomid = Long.parseLong(resultset.getString(5));
				
				
				
				Student stud = Student.findStudent(candidateId);
				ChecklistQuestion checklistQuestion = ChecklistQuestion.findChecklistQuestion(questionId);
				
				
				OscePostRoom oscePostRoom = OscePostRoom.findOscePostRoomByRoomAndStudent(candidateId, roomid);
				Doctor doctor = Doctor.findDoctor(examinerid);
				ChecklistOption checklistOption = ChecklistOption.findChecklistOptionByValueAndQuestion(questionId, String.valueOf(optionvalue));
				
				answerTable = new Answer();
				answerTable.setAnswer(String.valueOf(optionvalue));
				answerTable.setStudent(stud);
				answerTable.setChecklistQuestion(checklistQuestion);
				answerTable.setChecklistOption(checklistOption);							
				answerTable.setOscePostRoom(oscePostRoom);
				answerTable.setDoctor(doctor);
				answerTable.setAnswerTimestamp(resultset.getTimestamp(6));
				answerTable.persist();
				
				//System.out.println("RECORD INSERTED SUCCESSFULLY");		
			}
		}
		catch(Exception e)
		{
			Log.error(e.getMessage());
		}
		
	}
	
	//export
	public void exportOsceFile(Long semesterID)
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
									roomIdElement.appendChild(doc.createTextNode(oscePostRoom.getRoom().getId().toString()));
									roomElement.appendChild(roomIdElement);
									
									Element roomNumElement = doc.createElement("number");
									roomNumElement.appendChild(doc.createCDATASection(oscePostRoom.getRoom().getRoomNumber() == null ? "" : oscePostRoom.getRoom().getRoomNumber()));
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
											
											Element studEndTimeElement = doc.createElement("id");
											studEndTimeElement.appendChild(doc.createTextNode(studAss.getTimeEnd() == null ? "" : studAss.getTimeEnd().toString()));
											studElement.appendChild(studEndTimeElement);
										}
									}
									
								}
								
								/*String rotNum = String.format("%02d", (l+1));
								
								String semesterValue = osceList.get(i).getSemester().getSemester().toString();
								
								String calyear = osceList.get(i).getSemester().getCalYear().toString();
								calyear = calyear.substring(2, calyear.length());
								
								EnumRenderer<Enum<StudyYears>> enumStudyYear = new EnumRenderer<Enum<StudyYears>>();
								
								String studyYear = enumStudyYear.render(osceList.get(i).getStudyYear());
								
								studyYear.replace(".", "");
								
								EnumRenderer<Enum<ColorPicker>> enumColor = new EnumRenderer<Enum<ColorPicker>>();
								String rotationString = enumColor.render(ColorPicker.valueOf(course.getColor()));*/
								
								
								
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
	
	}
	
	/*public void exportOsceFile(Long semesterID)
	{
		System.out.println("~~SEMESTER ID : " + semesterID);
		String fileName = "";
		int timeslot = 0;
		String xml;
		
		try
		{
			List<Osce> osceList = Osce.findAllOsceBySemster(semesterID);
					
			for (int i=0; i<osceList.size(); i++)
			{
				//System.out.println("OSCE : " + i);		
				
				timeslot = osceList.get(i).getOscePostBlueprints().size();
				
				List<OsceDay> osceDayList = OsceDay.findOsceDayByOsce(osceList.get(i).getId());
				int startrotation = 0;
				int totalrotation = 0;
				
				for (int j=0; j<osceDayList.size(); j++)
				{
					List<OsceSequence> sequenceList = OsceSequence.findOsceSequenceByOsceDay(osceDayList.get(j).getId());
					System.out.println("SEQUENCE LIST : " + sequenceList.size());
					
					for (int k=0; k<sequenceList.size(); k++)
					{
						startrotation = totalrotation;
						totalrotation = totalrotation + sequenceList.get(k).getNumberRotation();
						
						int rotationoffset = 0;
						int rotationStart = startrotation;
						for (int l=startrotation; l<totalrotation; l++)
						{
							
							xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n";
							xml = xml + "<rotation>" + "\n";
							//System.out.println("Rotation [" + (l+1) +"] Start");							
							
							//this count oscepostroom according to course wise
							List<OscePostRoom> oscePostRoomList = OscePostRoom.findOscePostRoomByCourse(sequenceList.get(k).getId());
							
							//get oscepostroomdata
							//System.out.println("OscePostRoom List Size : " + oscePostRoomList.size());
							xml = xml + "<oscepostrooms>" + "\n";
								
							for (int n=0; n<oscePostRoomList.size(); n++)
							{
								xml = xml + "<oscepostroom>" + "\n";
								//System.out.println("OSCEPOSTROOM : " + n);
								//System.out.println("***************************************");
								xml = xml + "<room>" + "\n";
								xml = xml + "<id>" + oscePostRoomList.get(n).getRoom().getId() + "</id>" + "\n";
								xml = xml + "<number>" + oscePostRoomList.get(n).getRoom().getRoomNumber() + "</number>" + "\n";
								xml = xml + "</room>" + "\n";
								
								//by spec[ check
								List<Assignment> assignmentlist = Assignment.findAssignmentByOscePostRoom(oscePostRoomList.get(n).getId(), osceList.get(i).getId(), l);
								//System.out.println("Student assignment list size : " + assignmentlist.size());
								
								List<Assignment> examinerAssList = new ArrayList<Assignment>();
								if (assignmentlist.size() > 0)
								{
									Date timestart = assignmentlist.get(0).getTimeStart();
									Date timeend = assignmentlist.get(assignmentlist.size()-1).getTimeStart();
									examinerAssList = Assignment.findAssignmentExamnierByOscePostRoom(oscePostRoomList.get(n).getId(), osceList.get(i).getId());
								}
								
								xml = xml + "<examiners>" + "\n";
								//System.out.println("Exminer Assignemnent List Size : " + examinerAssList.size());
								for (int q=0; q<examinerAssList.size(); q++)
								{
									//get examiner data
									if (examinerAssList.get(q).getExaminer() != null)
									{
										xml = xml + "<examiner>" + "\n";
										xml = xml + "<id>" + examinerAssList.get(q).getExaminer().getId() + "</id>" + "\n";
										xml = xml + "<firstname>" + examinerAssList.get(q).getExaminer().getPreName() + "</firstname>" + "\n";
										xml = xml + "<lastname>" + examinerAssList.get(q).getExaminer().getName() + "</lastname>" + "\n";										xml = xml + "<email>" + examinerAssList.get(q).getExaminer().getEmail() + "</email>";
										xml = xml + "<phone>" + examinerAssList.get(q).getExaminer().getTelephone() + "</phone>" + "\n";									
										xml = xml + "</examiner>" + "\n";
									}
								}
								xml = xml + "</examiners>" + "\n";
								
								OscePost oscepost = OscePost.findOscePost(oscePostRoomList.get(n).getOscePost().getId());
								StandardizedRole standardizedRole = StandardizedRole.findStandardizedRole(oscepost.getStandardizedRole().getId());
								CheckList checklist = CheckList.findCheckList(standardizedRole.getCheckList().getId());
								//get checklist data
								xml = xml + "<checklist>" + "\n";
								xml = xml + "<id>" + checklist.getId() + "</id>" + "\n";
								xml = xml + "<title>" + checklist.getTitle() + "</title>" + "\n";
								
								xml = xml + "<checklisttopics>" + "\n";
								List<ChecklistTopic> checklistTopic = checklist.getCheckListTopics();
								
								int ctr = 1;
								
								for (int ch=0; ch<checklistTopic.size(); ch++)
								{
									xml = xml + "<checklisttopic>" + "\n";
									xml = xml + "<id>" + checklistTopic.get(ch).getId() + "</id>" + "\n";
									xml = xml + "<title>" + checklistTopic.get(ch).getTitle() + "</title>" + "\n";
									xml = xml + "<description>" + checklistTopic.get(ch).getDescription() + "</description>" + "\n";
									
									xml = xml + "<checklistquestions>" + "\n";
									List<ChecklistQuestion> checklistQuestions = checklistTopic.get(ch).getCheckListQuestions();
									
									for (int qu=0; qu<checklistQuestions.size(); qu++)
									{
										xml = xml + "<checklistquestion>" + "\n";
										xml = xml + "<id>" + checklistQuestions.get(qu).getId() + "</id>" + "\n";
										xml = xml + "<question>" + checklistQuestions.get(qu).getQuestion() + "</question>" + "\n";
										xml = xml + "<instruction>" + checklistQuestions.get(qu).getInstruction() + "</instruction>" + "\n";
										xml = xml + "<key>isOverallQuestion</key>" + "\n";
										xml = xml + "<" + (checklistQuestions.get(qu).getIsOveralQuestion() == null ? "false" : checklistQuestions.get(qu).getIsOveralQuestion()) + " />" + "\n";
										xml = xml + "<sequencenumber>" + ctr + "</sequencenumber>" + "\n";
										ctr++;
										
										xml = xml + "<checklistcriterias>" + "\n";
										//Set<ChecklistCriteria> criteria = new HashSet<ChecklistCriteria>();
										//criteria = checklistQuestions.get(qu).getCheckListCriterias();
										Iterator<ChecklistCriteria> criiterator = checklistQuestions.get(qu).getCheckListCriterias().iterator();
										while (criiterator.hasNext())
										{
											ChecklistCriteria chcri = criiterator.next();
											xml = xml + "<checklistcriteria>" + "\n";
											xml = xml + "<id>" + chcri.getId() + "</id>" + "\n";
											xml = xml + "<title>" + chcri.getCriteria() + "</title>" + "\n";
											xml = xml + "</checklistcriteria>" + "\n";
										}
										xml = xml + "</checklistcriterias>" + "\n";
										
										xml = xml + "<checklistoptions>" + "\n";
										//Set<ChecklistOption> option = checklistQuestions.get(qu).getCheckListOptions();
										Iterator<ChecklistOption> opitr = checklistQuestions.get(qu).getCheckListOptions().iterator();
										while (opitr.hasNext())
										{
											ChecklistOption op = opitr.next();
											xml = xml + "<checklistoption>" + "\n";
											xml = xml + "<id>" + op.getId() + "</id>" + "\n";
											xml = xml + "<title>" + op.getOptionName() + "</title>" + "\n";
											xml = xml + "<value>" + op.getValue() + "</value>" + "\n";
											xml = xml + "<criteriacount>" + "[" + (op.getCriteriaCount() == null ? "" : op.getCriteriaCount()) + "]" + "</criteriacount>" + "\n";
											xml = xml + "</checklistoption>" + "\n";
										}
										xml = xml + "</checklistoptions>" + "\n";
										xml = xml + "</checklistquestion>" + "\n";
									}
									xml = xml + "</checklistquestions>" + "\n";
									xml = xml + "</checklisttopic>" + "\n";
								}
								
								xml = xml + "</checklisttopics>" + "\n";
								xml = xml + "</checklist>" + "\n";
								
								
								
								xml = xml + "<students>" + "\n";
								for (int p=0; p<assignmentlist.size(); p++)
								{
									//get student data
									if (assignmentlist.get(p).getStudent() != null)
									{
										xml = xml + "<student>" + "\n";
										xml = xml + "<id>" + assignmentlist.get(p).getStudent().getId() + "</id>" + "\n";
										xml = xml + "<firstname>" + assignmentlist.get(p).getStudent().getPreName() + "</firstname>" + "\n";
										xml = xml + "<lastname>" + assignmentlist.get(p).getStudent().getName() + "</lastname>" + "\n";
										xml = xml + "<email>" + assignmentlist.get(p).getStudent().getEmail() + "</email>" + "\n";
										xml = xml + "<starttime>" + assignmentlist.get(p).getTimeStart() + "</starttime>" + "\n";
										xml = xml + "<endtime>" + assignmentlist.get(p).getTimeEnd() + "</endtime>" + "\n";
										xml = xml + "</student>" + "\n";
									}
								}
								xml = xml + "</students>" + "\n";							
								
								xml = xml + "</oscepostroom>" + "\n";
								//System.out.println("jsonoscepostroom : " + jsonoscepostroom);
							}
											
							xml = xml + "</oscepostrooms>" + "\n";
							xml = xml + "</rotation>";
							rotationoffset += timeslot;
							//System.out.println("Rotation [" + (l+1) +"] End");
							//System.out.println("#################################");
							
							//fileName = (l+1) + osceList.get(i).getName() + osceList.get(i).getStudyYear() + ".xml";
							String rotNum = String.format("%02d", (l+1));
							fileName = String.valueOf(osceList.get(i).getSemester().getCalYear()) + osceList.get(i).getStudyYear() + osceList.get(i).getName() + rotNum + ".xml";
							
							String processedFileName = OsMaFilePathConstant.EXPORT_OSCE_PROCESSED_FILEPATH + fileName;
							File processfile = new File(processedFileName);
							Boolean processCheck = processfile.exists();	
							
							if (!processCheck)
							{
								fileName = OsMaFilePathConstant.EXPORT_OSCE_UNPROCESSED_FILEPATH + fileName;
								File file = new File(fileName);
								Boolean check = file.exists();
								
								if (check)
								{
									file.delete();
								}
								
									FileUtils.touch(file);
									BufferedWriter bufWriter = new BufferedWriter(new FileWriter(file));
									bufWriter.write(xml);
									bufWriter.close();
								System.out.println("* * *" + file.getName() + " IS CREATED * * *");								
							}
							xml = "";
							fileName = "";
						}
						
						startrotation = totalrotation;
					}
				}
			}
		}
		catch(Exception e)
		{
			Log.error(e.getMessage());
		}
	}*/
	
	public List<String> exportProcessedFileList()
	{
		List<String> processedList = new ArrayList<String>();
		
		File folder = new File(OsMaFilePathConstant.EXPORT_OSCE_PROCESSED_FILEPATH);
		
		if (folder.exists())
		{
			File[] listOfFiles = folder.listFiles();
		
			for (int i=0; i<listOfFiles.length; i++)
			{
				processedList.add(listOfFiles[i].getName());
			}
		}
		
		return processedList;
	}
	
	public List<String> exportUnprocessedFileList()
	{
		List<String> unprocessedList = new ArrayList<String>();
		
		File folder = new File(OsMaFilePathConstant.EXPORT_OSCE_UNPROCESSED_FILEPATH);		
		
		if (folder.exists())
		{
			File[] listOfFiles = folder.listFiles();
			for (int i=0; i<listOfFiles.length; i++)
			{
				unprocessedList.add(listOfFiles[i].getName());
			}
		}
		
		return unprocessedList;
	}
	
	public void putAmazonS3Object(String bucketName, String accessKey, String secretKey, List<String> fileList, Boolean flag) throws eOSCESyncException
	{	
		//file is put in bucketname as key.
		
		try
		{
			bucketName = bucketName.toLowerCase();
			
			//System.out.println("BUCKET NAME : " + bucketName);
			AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
			//AmazonS3Client client = new AmazonS3Client(new PropertiesCredentials(eOSCESyncServiceImpl.class.getResourceAsStream("AwsCredentials.properties")));
			AmazonS3Client client = new AmazonS3Client(credentials);
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
				for (int i=0; i<fileList.size(); i++)
				{
					String path = OsMaFilePathConstant.EXPORT_OSCE_UNPROCESSED_FILEPATH + fileList.get(i);
					
					File file = new File(path);
					
					client.putObject(bucketName, fileList.get(i), file);
				
					//move file to processed				
					File dir = new File(OsMaFilePathConstant.EXPORT_OSCE_PROCESSED_FILEPATH);
					if (dir.exists())
					{
						file.renameTo(new File(dir, file.getName()));
					}
					else
					{	
						dir.mkdirs();
						file.renameTo(new File(dir, file.getName()));
					}
				}	
			}			
			else
			{
				for (int i=0; i<fileList.size(); i++)
				{
					String path = OsMaFilePathConstant.EXPORT_OSCE_PROCESSED_FILEPATH + fileList.get(i);
					
					File file = new File(path);
					
					client.putObject(bucketName, fileList.get(i), file);
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
}