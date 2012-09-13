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

import ch.unibas.medizin.osce.server.OsMaFilePathConstant;

import com.allen_sauer.gwt.log.client.Log;

public class VideoUploadServlet extends HttpServlet {

	// private static String UPLOAD_DIRECTORY=".";

	// SPEC[Start
	private static String appUploadDirectory = OsMaFilePathConstant.appVideoUploadDirectory;
	private static String localUploadDirectory = OsMaFilePathConstant.localVideoUploadDirectory;
	// private static String srcPath=OsMaFilePathConstant.videosrcPath;
	// SPEC]End

	/**
	 * Generated
	 */
	private static final long serialVersionUID = 2L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		if (!ServletFileUpload.isMultipartContent(request)) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unsupported content");
		}

		// SPEC[Start
		// String cntxtpath=request.getSession().getServletContext().getRealPath(".");
		appUploadDirectory = getServletConfig().getServletContext().getRealPath(OsMaFilePathConstant.appVideoUploadDirectory);
		if (OsMaFilePathConstant.realVideoPath.equals(""))
			OsMaFilePathConstant.realVideoPath = appUploadDirectory;
		// appUploadDirectory=cntxtpath+appUploadDirectory;
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
					if (!item.getString().equals(""))
						fileName = item.getString() + "_" + fileName;
				} else {

					temp = FilenameUtils.getName(item.getName());
					String[] a = temp.split("\\.");
					temp = a[a.length - 1];

				}
			}

			fileName = fileName.substring(0, fileName.length() - 1);
			fileName = fileName + "." + temp;
			// spec
			for (FileItem item : items) {
				// process only file upload - discard other form item types

				/*
				 * if (item.isFormField()) { Log.info("Form Field: "+item.getFieldName()+" = "+item.getString()); continue; }
				 */
				if (item.isFormField()) {
					continue;
				} else {

					File appUploadedFile = new File(appUploadDirectory, fileName);

					Log.info("video path : " + appUploadDirectory);
					appUploadedFile.createNewFile();
					// save
					item.write(appUploadedFile);
					// resp.setStatus(HttpServletResponse.SC_CREATED);
					Log.info("file name " + fileName);

					// upload file to local directory

					File localUploadedFile = new File(localUploadDirectory, fileName);
					localUploadedFile.createNewFile();
					// save
					item.write(localUploadedFile);
					// resp.setStatus(HttpServletResponse.SC_CREATED);
					Log.info("file name " + fileName);

					System.out.println("File Name :" + fileName);
					// MediaContent.createMedia(new Long(19), fileName);
				}
			}
		} catch (Exception e) {
			// resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while creating the file : " + e.getMessage());
			Log.error("An error occurred while creating the file : " + e.getMessage());
		}
		// service info
		/*
		 * Log.info("Attribtue names: "); Enumeration<?> enu = request.getAttributeNames(); while (enu.hasMoreElements()) { Log.info("Attribute name: " + enu.nextElement().toString()); }
		 */
		Log.info("Content type: " + request.getContentType());
		// TODO: resolve the content type and show different links
		resp.setContentType("text/html");
		resp.getOutputStream().write((OsMaFilePathConstant.appVideoUploadDirectory + "/" + fileName).getBytes());
		resp.getOutputStream().flush();
	}
}
