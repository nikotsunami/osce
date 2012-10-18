package ch.unibas.medizin.osce.server.util.resourcedownloader;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import ch.unibas.medizin.osce.server.OsMaFilePathConstant;
import ch.unibas.medizin.osce.shared.ResourceDownloadProps;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;

public class ResourceDownloaderServlet extends HttpServlet {
		
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
