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
import java.util.List;

import org.apache.commons.io.FileUtils;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.eOSCESyncException;
import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.eOSCESyncService;
import ch.unibas.medizin.osce.domain.Answer;
import ch.unibas.medizin.osce.domain.ChecklistCriteria;
import ch.unibas.medizin.osce.domain.ChecklistOption;
import ch.unibas.medizin.osce.domain.ChecklistQuestion;
import ch.unibas.medizin.osce.domain.Doctor;
import ch.unibas.medizin.osce.domain.OscePostRoom;
import ch.unibas.medizin.osce.domain.Student;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class eOSCESyncServiceImpl extends RemoteServiceServlet implements eOSCESyncService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String appUploadDirectory= OsMaConstant.DEFAULT_IMPORT_EOSCE_PATH;
	
	
	public List<String> processedFileList() throws eOSCESyncException
	{
		List<String> fileList = new ArrayList<String>();
		try
		{
			//write access and secret key
			AWSCredentials credentials = new BasicAWSCredentials("", "");
			AmazonS3Client client = new AmazonS3Client(credentials);
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
			ase.printStackTrace();
			throw new eOSCESyncException(ase.getErrorType().toString(),ase.getMessage());
		}
		catch(AmazonClientException ace)
		{
			ace.printStackTrace();
			throw new eOSCESyncException("",ace.getMessage());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new eOSCESyncException("",e.getMessage());
		}
		
		System.out.println("~~PROCESSED FILE SIZE : " + fileList.size());
		return fileList;
	}
	
	public List<String> unprocessedFileList() throws eOSCESyncException
	{
		List<String> fileList = new ArrayList<String>();
		try
		{
			//write access and secret key.
			AWSCredentials credentials = new BasicAWSCredentials("", "");
			AmazonS3Client client = new AmazonS3Client(credentials);
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
			ase.printStackTrace();
			throw new eOSCESyncException(ase.getErrorType().toString(),ase.getMessage());
		}
		catch(AmazonClientException ace)
		{
			ace.printStackTrace();
			throw new eOSCESyncException("",ace.getMessage());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new eOSCESyncException("",e.getMessage());
		}
		
		System.out.println("~~UNPROCESSED FILE SIZE : " + fileList.size());
		return fileList;
	}

	@Override
	public void deleteAmzonS3Object(List<String> fileList)
			throws eOSCESyncException {
		try
		{
			//write access and secret key
			AWSCredentials credentials = new BasicAWSCredentials("", "");
			AmazonS3Client client = new AmazonS3Client(credentials);
			
			for (int i=0; i<fileList.size(); i++)
			{
				System.out.println("KEY NAME : " + fileList.get(i));
				//write bucket name
				client.deleteObject("", fileList.get(i));
			}
		}
		catch(AmazonServiceException ase)
		{
			ase.printStackTrace();
			throw new eOSCESyncException(ase.getErrorType().toString(),ase.getMessage());
		}
		catch(AmazonClientException ace)
		{
			ace.printStackTrace();
			throw new eOSCESyncException("",ace.getMessage());
		}
		catch(Exception e)
		{
			e.printStackTrace();
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
			e.printStackTrace();
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
			//write access and secret key
			AWSCredentials credentials = new BasicAWSCredentials("", "");
			AmazonS3Client client = new AmazonS3Client(credentials);
			S3Object object = null;
			
			for (int i=0; i<fileList.size(); i++)
			{
				System.out.println("FILENAME : " + fileList.get(i));
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
			ase.printStackTrace();
			throw new eOSCESyncException(ase.getErrorType().toString(),ase.getMessage());
		}
		catch(AmazonClientException ace)
		{
			ace.printStackTrace();
			throw new eOSCESyncException("",ace.getMessage());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new eOSCESyncException("",e.getMessage());
		}
	}
	
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
			
			//SELECT * FROM candidate WHERE present = 'TRUE'
			//SELECT a.*,c.osmaId FROM candidate c, answer a WHERE a.candidateId = c.id AND c.present = TRUE
			//SELECT * FROM answer WHERE candidateId = candResultSet.getInt(1)
			ResultSet resultset = statement.executeQuery("SELECT a.*,c.osmaId FROM candidate c, answer a WHERE a.candidateId = c.id AND c.present = 'TRUE'");
			
			ResultSet rs;
			
			long candidateId, criteriaId, optionId, questionId, osmaid, oscepostroomid = 0, examinerid;
			long tempcandidateid, tempquestionid, tempexaminerid;
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
				
				rs = st.executeQuery("SELECT * FROM examiner WHERE firstname = 'Rath'");
				tempexaminerid = rs.getInt("id");
				examinerid = rs.getInt("osmaId");
				//System.out.println("EXAMINER ID : " + tempexaminerid);				
				
				rs = st.executeQuery("SELECT osmaId FROM station WHERE candidateId = " + tempcandidateid + " AND examinerId = " + tempexaminerid);
				if (rs.next())
				{
					oscepostroomid = rs.getInt("osmaId");
					//System.out.println("OSMAID OF OSCEPOSTROOM : " + oscepostroomid);
				}
				
				//System.out.println("OSMAID OF EXAMINER : " + examinerid);
							
				Student stud = Student.findStudent(candidateId);
				ChecklistQuestion checklistQuestion = ChecklistQuestion.findChecklistQuestion(questionId);
				ChecklistOption checklistOption = ChecklistOption.findChecklistOption(optionId);
								
				OscePostRoom oscePostRoom = OscePostRoom.findOscePostRoom(oscepostroomid);
				Doctor doctor = Doctor.findDoctor(examinerid);				
			
				answerTable = new Answer();
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
				
				System.out.println("RECORD INSERTED SUCCESSFULLY");		
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
}