package ch.unibas.medizin.osce.server.upload;

import java.io.File;
import java.io.IOException;
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

import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.domain.Student;
import ch.unibas.medizin.osce.domain.StudentOsces;
import ch.unibas.medizin.osce.server.OsMaFilePathConstant;
import ch.unibas.medizin.osce.shared.Gender;

import com.allen_sauer.gwt.log.client.Log;
import com.csvreader.CsvReader;

/**
 * Servlet implementation class CsvFileUploadServlet
 */

@SuppressWarnings("serial")
public class CsvFileUploadServlet extends HttpServlet {

	//	 private static String appUploadDirectory=OsMaFilePathConstant.DOWNLOAD_DIR_PATH;//OsMaFilePathConstant.CSV_FILEPATH;

	public String fetchRealPath(HttpServletRequest request) {

		String fileSeparator = System.getProperty("file.separator");
		return request.getSession().getServletContext().getRealPath(fileSeparator) + OsMaFilePathConstant.DOWNLOAD_DIR_PATH + fileSeparator;

	}
			
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		
		System.out.println("~~Inside Servlet");
		
		String path="";
		String osceid = "";
		File appUploadedFile = null;
		
		  if(!ServletFileUpload.isMultipartContent(request)){
	            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
	                    "Unsupported content");
	        }
		  
		//		  String  cntxtpath=fetchRealPath(request);
		//String  cntxtpath=request.getSession().getServletContext().getRealPath(".");
		String uploadDir = fetchRealPath(request);//cntxtpath;+appUploadDirectory;
		 
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
	            System.out.println("ITEM SIZE : " + items.size());
           
	            for (FileItem item : items)
	            {
	                if (item.isFormField())
	                {
	                	System.out.println("~~Inside IF");
	                	System.out.println("~~ITEM NAME : " + item.getString());
                        osceid = item.getString();
	                }
	                else
	                {
	                	System.out.println("~~Inside Else");
	                	System.out.println("~~ITEM NAME : " + item.getName());
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
	                	System.out.println("~~Upload Dir : " + uploadDir);
	                	path = uploadDir + fileName;
	                	appUploadedFile = new File(uploadDir, fileName);
	                	appUploadedFile.createNewFile();
	                	item.write(appUploadedFile);
	                	System.out.println("~~AppUploadedFile : " + appUploadedFile);
	                }
	            }
	        } catch (Exception e) {
	            System.out.println("An error occurred while creating the file : " + e.getMessage());
	        }
	  
	        System.out.println("~~OSCE ID IN SERVLET : " + osceid);
	        
	        Osce osce = Osce.findOsce(Long.parseLong(osceid));
	        
	        CsvReader student = new CsvReader(path);
	        student.readHeaders();
	        
	        String id = "0";

	        while (student.readRecord())
	        {
	        	id = student.get(student.getHeader(4));
	        	if (id == "")
	        	{
	        		id = "0";
	        	}
	        	
	        	List<Student> studentList = Student.findStudentByEmail(student.get(student.getHeader(3)));
	        	//Long ctr = Student.findStudentByIDOrByEmail(id, student.get(student.getHeader(3)));
	         	String name;
	         	String email;
	         	String prename;
	           	String gender;
	         	
	         	if (studentList.size() == 0)
	         	{
	         		name=student.get(student.getHeader(0));
	         		prename=student.get(student.getHeader(1));
	         		gender = student.get(student.getHeader(2));
	         		email=student.get(student.getHeader(3));	         		
	         		
	         		
	         		
	         		Student s=new Student();
	         	       		
	         		s.setName(name);
	         		s.setPreName(prename);
	         		s.setGender(Gender.valueOf(gender));
	         		s.setEmail(email);
	         		
	      
	         		try{
	         		
	         		System.out.println("Inside try before persist");
	         		System.out.println("Name : " + s.getName());
	         		System.out.println("PreName : " + s.getPreName());
	         		System.out.println("Email : " + s.getEmail());
	         		System.out.println("Gender : " + s.getGender().name());
	         				
	         		
	         		s.persist();        	
	         		
	         		System.out.println("~~After Persist");
	         		
	         		Long tempid = Long.parseLong(osceid);
	         		
	         		System.out.println("~~Temp id : " + tempid);
	         		
	         		Long oscecount = null;
	         		try{
	         			oscecount = StudentOsces.findStudentByStudIdAndOsceId(s.getId(), Long.parseLong(osceid));
	         		}catch(Exception e)
	         		{
	         			e.printStackTrace();
	         		}
	         		
	         		System.out.println("OSCE Count : " + oscecount);
	         		
	         			if (oscecount == 0)
	         			{
	         				System.out.println("Insert Data in Student OSCE");
	         				
	         				StudentOsces studosces = new StudentOsces();
	         				studosces.setIsEnrolled(true);
	         				studosces.setStudent(s);
	         				studosces.setOsce(osce);
	         				studosces.persist();
	         				
	         				System.out.println("Record Inserted in Student OSCES");
	         			}
	         		
	         		}catch(Exception e){
	         			e.getMessage();
	         		}
	         		
	         		s.flush();
	         	}
	         	else
	         	{
	         		System.out.println("~~Record is Already there");
	  
	         		Long oscecount = null;
	         		try{
	         			oscecount = StudentOsces.findStudentByStudIdAndOsceId(studentList.get(0).getId(), Long.parseLong(osceid));  
	         		}catch(Exception e)
	         		{
	         			e.printStackTrace();
	         		}
	         		
	         		System.out.println("OSCE Count : " + oscecount);
	         		
	         			if (oscecount == 0)
	         			{
	         				System.out.println("Insert Data in Student OSCE");
	         				
	         				List<Student> studlist = Student.findStudentByEmail(student.get(student.getHeader(3)));
	    	         		
	    	         		Student stud = studlist.get(0);         				
	         				
	         				StudentOsces studosces = new StudentOsces();
	         				studosces.setIsEnrolled(true);
	         				studosces.setStudent(stud);
	         				studosces.setOsce(osce);
	         				studosces.persist();
	         				
	         				System.out.println("Record Inserted in Student OSCES");
	         			}
	         		
	         		
	         	}
	        }
	        try
	        {
	        	
	        System.out.println("~~Deleted : " + appUploadedFile.delete());
	        }
	        catch (Exception e) {
				e.printStackTrace();
			}
	        
	}
}
