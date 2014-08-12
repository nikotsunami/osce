package ch.unibas.medizin.osce.server.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.domain.Student;
import ch.unibas.medizin.osce.domain.StudentOsces;
import ch.unibas.medizin.osce.server.OsMaFilePathConstant;
import ch.unibas.medizin.osce.shared.Gender;

import com.csvreader.CsvReader;

/**
 * Servlet implementation class CsvFileUploadServlet
 */

@SuppressWarnings("serial")
public class CsvFileUploadServlet extends HttpServlet {
	
	private static Logger Log = Logger.getLogger(CsvFileUploadServlet.class);
	
	public String fetchRealPath(HttpServletRequest request) {

		String fileSeparator = System.getProperty("file.separator");
		return request.getSession().getServletContext().getRealPath(fileSeparator) + OsMaFilePathConstant.DOWNLOAD_DIR_PATH + fileSeparator;

	}
			
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
	
		String path="";
		String osceid = "";
		File appUploadedFile = null;
		
		if(!ServletFileUpload.isMultipartContent(request)){
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
	            		"Unsupported content");
		}
	
		String uploadDir = fetchRealPath(request);
		 
		// Create a factory for disk-based file items
		FileItemFactory factory = new DiskFileItemFactory();

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
	       
		ProgressListener progressListener = new ProgressListener(){
			private long megaBytes = -1;
			public void update(long pBytesRead, long pContentLength, int pItems) {
				long mBytes = pBytesRead / 1000000;
				if (megaBytes == mBytes) {
					return;
				}
				megaBytes = mBytes;
				Log.info("We are currently reading item " + pItems);
				if (pContentLength == -1) {
					Log.info("So far, " + pBytesRead + " bytes have been read.");
				} else {
					Log.info("So far, " + pBytesRead + " of " + pContentLength
							+ " bytes have been read.");
				}
			}
		};
		
		upload.setProgressListener(progressListener); 
		String fileName = "";
		String temp="";
		
		try {
			@SuppressWarnings("unchecked")
			List<FileItem> items = upload.parseRequest(request);
	        	
			for (FileItem item : items)
			{
				if (item.isFormField())
				{
					osceid = item.getString();
				}
				else
				{
					temp=FilenameUtils. getName(item.getName());
				}
			}
	           
			fileName = temp;
	            
			for (FileItem item : items) {
				// process only file upload - discard other form item types
				if (item.isFormField())
				{
					continue;
				}
				else
				{
					path = uploadDir + fileName;
					appUploadedFile = new File(uploadDir, fileName);
					appUploadedFile.createNewFile();
					item.write(appUploadedFile);
				}
			}
		} catch (Exception e) {
			Log.info("An error occurred while creating the file : " + e.getMessage());
		}
	     
		Osce osce = Osce.findOsce(Long.parseLong(osceid));
		
		CsvReader student = new CsvReader(new FileInputStream(path), Charset.forName("UTF-8"));
		
		student.readHeaders();
		
		String studentIdCol, nameCol, prenameCol, emailCol, streetCol, cityCol, genderCol, studyYearCol;
		
		studentIdCol = nameCol = prenameCol = emailCol = streetCol = cityCol = genderCol = studyYearCol = "";
		
		
		for (int i=0; i<student.getHeaderCount(); i++)
		{
			if (student.getHeader(i).equalsIgnoreCase("student_id"))
				studentIdCol = student.getHeader(i);
			else if (student.getHeader(i).equalsIgnoreCase("name"))
				nameCol = student.getHeader(i);
			else if (student.getHeader(i).equalsIgnoreCase("prename"))
				prenameCol = student.getHeader(i);
			else if (student.getHeader(i).equalsIgnoreCase("email_id"))
				emailCol = student.getHeader(i);
			else if (student.getHeader(i).equalsIgnoreCase("street"))
				streetCol = student.getHeader(i);
			else if (student.getHeader(i).equalsIgnoreCase("city"))
				cityCol = student.getHeader(i);
			else if (student.getHeader(i).equalsIgnoreCase("gender"))
				genderCol = student.getHeader(i);
		}
		
		if (nameCol.equals("") || prenameCol.equals(""))
		{
			String status = "false";
			resp.getOutputStream().write(status.getBytes());
		}
		else if (studentIdCol.equals("") && emailCol.equals(""))
		{
			String status = "false";
			resp.getOutputStream().write(status.getBytes());
		}
		else
		{
			List<Student> studentList = new ArrayList<Student>();
			
			while (student.readRecord())
			{
				
				if (emailCol.equals(""))
					studentList = Student.findStudentByStudentIdAndByEmail(student.get(studentIdCol), "");
				else if (studentIdCol.equals(""))
					studentList = Student.findStudentByStudentIdAndByEmail("", student.get(emailCol));
				else
					studentList = Student.findStudentByStudentIdAndByEmail(student.get(studentIdCol), student.get(emailCol));
					
				
				if (studentList.size() == 0)
				{
					Student s=new Student();
					
					if (!studentIdCol.equals(""))
						s.setStudentId(student.get(studentIdCol));
					
					if (!nameCol.equals(""))
						s.setName(student.get(nameCol));
					
					if (!prenameCol.equals(""))
						s.setPreName(student.get(prenameCol));
					
					if (!emailCol.equals(""))
						s.setEmail(student.get(emailCol));
					
					if (!streetCol.equals(""))
						s.setStreet(student.get(streetCol));
					
					if (!cityCol.equals(""))
						s.setCity(student.get(cityCol));
					
					if (!genderCol.equals(""))
						s.setGender(Gender.valueOf(student.get(genderCol).toUpperCase()));
				
					s.persist();
					
					int oscecount = StudentOsces.findStudentByStudIdAndOsceId(s.getId(), Long.parseLong(osceid));
					
					if (oscecount == 0)
					{
						StudentOsces studosces = new StudentOsces();
						studosces.setIsEnrolled(true);
						studosces.setStudent(s);
						studosces.setOsce(osce);
						studosces.persist();
					}
				}
				else
				{
					int oscecount = StudentOsces.findStudentByStudIdAndOsceId(studentList.get(0).getId(), Long.parseLong(osceid));
		         		
					if (oscecount == 0)
					{
						StudentOsces studosces = new StudentOsces();
						studosces.setIsEnrolled(true);
						studosces.setStudent(studentList.get(0));
						studosces.setOsce(osce);
						studosces.persist();
					}	
				}
			}
			
			String status = "true";
			resp.getOutputStream().write(status.getBytes());
		}
		try
		{
			Log.info("~~Deleted : " + appUploadedFile.delete());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
