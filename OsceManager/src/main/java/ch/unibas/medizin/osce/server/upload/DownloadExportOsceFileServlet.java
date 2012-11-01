package ch.unibas.medizin.osce.server.upload;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.server.OsMaFilePathConstant;


/**
 * Servlet implementation class DownloadExportOsceFileServlet
 */
public class DownloadExportOsceFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger Log = Logger.getLogger(DownloadExportOsceFileServlet.class);
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try{
			 String path = request.getParameter("path");
			 String flag = request.getParameter("flag");
			 
			 response.setContentType("application/x-download");
			 
			 response.setHeader("Content-Disposition", "attachment; filename=\"" + path +"\"");

			 Log.info("path :" + path);
			
			 String file = "";
			 
			 if (flag.equals("true"))
				 file=OsMaFilePathConstant.EXPORT_OSCE_UNPROCESSED_FILEPATH+path;
			 else
				 file=OsMaFilePathConstant.EXPORT_OSCE_PROCESSED_FILEPATH+path;
			 
			 Log.info(" file :" + file);
				
			 OutputStream out = response.getOutputStream();
			 
			 FileInputStream in = new FileInputStream(file);
			
			 byte[] buffer = new byte[4096];
			
			 int length;
			
			 while ((length = in.read(buffer)) > 0){
				 out.write(buffer, 0, length);
			}
			 
			 in.close();
			
			 out.flush();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
