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
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.domain.MediaContent;
import ch.unibas.medizin.osce.server.OsMaFilePathConstant;


public class RoleFileUploadServlet extends HttpServlet {

	private static Logger Log = Logger.getLogger(RoleFileUploadServlet.class);
	// private static String UPLOAD_DIRECTORY="d://sp//images";

	// SPEC[Start
	private static String appUploadDirectory = OsMaFilePathConstant.ROLE_IMAGE_FILEPATH;

	// private static String localUploadDirectory="d://role//images/";
	// private static String srcPath="/osMaEntry/gwt/unibas/role/images/";
	// SPEC]End
	/**
	 * Generated
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		if (!ServletFileUpload.isMultipartContent(request)) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unsupported content");
		}

		// SPEC[Start
		// String cntxtpath=request.getSession().getServletContext().getRealPath(".");
		// String uploadDir=cntxtpath+appUploadDirectory;
		// SPEC]End

		// Create a factory for disk-based file items
		FileItemFactory factory = new DiskFileItemFactory();

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// TODO: add progress bar
		ProgressListener progressListener = new ProgressListener() {
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
					Log.info("So far, " + pBytesRead + " of " + pContentLength + " bytes have been read.");
				}
			}
		};
		upload.setProgressListener(progressListener);
		String fileName = "";
		String temp = "";

		// fileName=request.getAttribute("id").toString()+request.getAttribute("name").toString()+"_";
		// spec end
		// upload
		try {
			@SuppressWarnings("unchecked")
			List<FileItem> items = upload.parseRequest(request);

			// spec change file name
			for (FileItem item : items) {
				if (item.isFormField()) {
					// fileName=item.getString() +"_"+fileName;
					fileName = fileName;
				} else {

					temp = FilenameUtils.getName(item.getName());

				}
			}
			// fileName=fileName+"_"+temp;
			fileName = temp;
			// spec
			for (FileItem item : items) {
				// process only file upload - discard other form item types

				/*
				 * if (item.isFormField()) { Log.info("Form Field: "+item.getFieldName()+" = "+item.getString()); continue; }
				 */
				if (item.isFormField()) {
					continue;
				} else {

					// Upload File to Application Directory
					// appUploadDirectory=session.getServletContext().getRealPath(".") + appUploadDirectory;
					File appUploadedFile = new File(appUploadDirectory, fileName);

					FileUtils.touch(appUploadedFile);
					appUploadedFile.createNewFile();
					// save
					item.write(appUploadedFile);
					// resp.setStatus(HttpServletResponse.SC_CREATED);
					Log.info("file name " + fileName);

					// upload file to local directory

					/*
					 * File localUploadedFile = new File( localUploadDirectory, fileName); localUploadedFile.createNewFile(); //save item.write(localUploadedFile); //resp.setStatus(HttpServletResponse.SC_CREATED); Log.info("file name " + fileName);
					 */

					System.out.println("File Name :" + fileName);
					MediaContent.createMedia(new Long(19), fileName);
				}
			}
		} catch (Exception e) {
			// resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while creating the file : " + e.getMessage());
			Log.error("An error occurred while creating the file : " + e.getMessage());
		}
		// service info
		Log.info("Attribtue names: ");
		Enumeration<?> enu = request.getAttributeNames();
		while (enu.hasMoreElements()) {
			Log.info("Attribute name: " + enu.nextElement().toString());
		}
		Log.info("Content type: " + request.getContentType());
		// TODO: resolve the content type and show different links
		resp.setContentType("text/html");

		// return application path
		/*
		 * resp.getOutputStream().write((srcPath+fileName).getBytes()); cntxtpath=""; resp.getOutputStream().flush();
		 */
	}
}
