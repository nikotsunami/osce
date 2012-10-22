package ch.unibas.medizin.osce.server.util.resourcedownloader;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.output.ByteArrayOutputStream;

import ch.unibas.medizin.osce.domain.StandardizedPatient;
import ch.unibas.medizin.osce.domain.StandardizedRole;
import ch.unibas.medizin.osce.shared.ResourceDownloadProps;
import ch.unibas.medizin.osce.shared.ResourceDownloadProps.Entity;

import com.allen_sauer.gwt.log.client.Log;

public class ResourceUtil {

	public static void setResource(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		Entity entity = ResourceDownloadProps.Entity.valueOf(request
				.getParameter(ResourceDownloadProps.ENTITY));
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		String fileName = "default.pdf";

		switch (entity) {
		case STANDARDIZED_PATIENT: {
			fileName = setStandardizedPatientResource(request, os);
			break;
		}

		case STANDARDIZED_ROLE: {
			fileName = setStandardizedRoleResource(request, os);
			break;
		}

		default: {
			Log.info("Error in entity : " + entity);
			break;
		}
		}

		sendFile(response, os.toByteArray(), fileName);
		os = null;
	}

	private static String setStandardizedPatientResource(
			HttpServletRequest request, ByteArrayOutputStream os) {

		Long id = Long
				.parseLong(request.getParameter(ResourceDownloadProps.ID));
		String locale = request.getParameter(ResourceDownloadProps.LOCALE);
		String fileName = StandardizedPatient
				.getPdfPatientsBySearchUsingServlet(id, locale, os);
		return fileName;
	}

	private static String setStandardizedRoleResource(
			HttpServletRequest request, ByteArrayOutputStream os) {

		Long id = Long
				.parseLong(request.getParameter(ResourceDownloadProps.ID));
		String locale = request.getParameter(ResourceDownloadProps.LOCALE);
		String[] filter = request
				.getParameterValues(ResourceDownloadProps.FILTER);
		Long selectedRoleItemAccess = Long.parseLong(request
				.getParameter(ResourceDownloadProps.SELECTED_ROLE_ITEM_ACCESS));

		String fileName = StandardizedRole
				.getRolesPrintPdfBySearchUsingServlet(id,
						Arrays.asList(filter), selectedRoleItemAccess, locale,
						os);

		return fileName;
	}

	private static void sendFile(HttpServletResponse response, byte[] resource,
			String fileName) throws IOException {
		ServletOutputStream stream = null;
		stream = response.getOutputStream();
		response.setContentType("application/x-download");
		response.addHeader("Content-Disposition", "inline; filename=\""
				+ fileName + "\"");
		response.setContentLength((int) resource.length);
		stream.write(resource);
		stream.close();
	}

}
