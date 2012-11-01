package ch.unibas.medizin.osce.server.util.resourcedownloader;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.shared.ResourceDownloadProps;


public class ResourceDownloaderServlet extends HttpServlet {
	
	private static Logger Log = Logger.getLogger(ResourceDownloaderServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {

		String entity = request.getParameter(ResourceDownloadProps.ENTITY);
		
		if(StringUtils.isNotBlank(entity)) {
			try {
				ResourceUtil.setResource(request, response);	
			} catch (IOException e) {
				Log.error("IO error in ResourceDownloaderServlet ",e);
			}
		}else {
			Log.error("Entity is not valid : " + entity);
		}
	}
	
}
