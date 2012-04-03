package ch.unibas.medizin.osce.server.upload;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
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

import ch.unibas.medizin.osce.domain.MediaContent;

import com.allen_sauer.gwt.log.client.Log;

public class UploadServlet extends HttpServlet {

	private static String UPLOAD_DIRECTORY=".";
	/**
	 * Generated 
	 */
	private static final long serialVersionUID = 1L;
	@Override
  protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		if(!ServletFileUpload.isMultipartContent(request)){
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
            		"Unsupported content");
		}
	    
    	// Create a factory for disk-based file items
        FileItemFactory factory = new DiskFileItemFactory();

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        //TODO: add progress bar
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
        String fileName = null;
        //upload
        try {
            @SuppressWarnings("unchecked")
			List<FileItem> items = upload.parseRequest(request);
            for (FileItem item : items) {
                // process only file upload - discard other form item types
                if (item.isFormField()) {
                	Log.info("Form Field: "+item.getFieldName()+" = "+item.getString());
                	continue;
                }
                
                fileName = item.getName();
                // get only the file name not whole path
                if (fileName != null) {
                    fileName = FilenameUtils. getName(fileName);
                }

                File uploadedFile = new File(UPLOAD_DIRECTORY, fileName);
                
                if(uploadedFile.createNewFile()) {
                	//save
                	item.write(uploadedFile);
                	//resp.setStatus(HttpServletResponse.SC_CREATED);
                	Log.info("file name " + fileName);
                }
                MediaContent.createMedia(new Long(19), fileName);
            }
        } catch (Exception e) {
            //resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while creating the file : " + e.getMessage());
        	Log.error("An error occurred while creating the file : " + e.getMessage());
        }
        //service info
        Log.info("Attribtue names: ");
        Enumeration<?> enu = request.getAttributeNames();
        while (enu.hasMoreElements()) {
          Log.info("Attribute name: " + enu.nextElement().toString());
        }
        Log.info("Content type: " + request.getContentType());
        //TODO: resolve the content type and show different links
        resp.getOutputStream().write(("<img src='"+fileName+"' width='80' height='60' />").getBytes());
        resp.getOutputStream().flush();
	}
}
