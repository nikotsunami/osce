package ch.unibas.medizin.osce.server.util.resourcedownloader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.domain.OsceDay;
import ch.unibas.medizin.osce.domain.PatientInSemester;
import ch.unibas.medizin.osce.domain.StandardizedPatient;
import ch.unibas.medizin.osce.domain.StandardizedRole;
import ch.unibas.medizin.osce.server.util.file.StandardizedPatientPaymentUtil;
import ch.unibas.medizin.osce.shared.ResourceDownloadProps;
import ch.unibas.medizin.osce.shared.Sorting;

public class ResourceUtil {
	
	private static Logger Log = Logger.getLogger(ResourceUtil.class);
	
	public static void setResource(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		int ordinal = Integer.parseInt(request
				.getParameter(ResourceDownloadProps.ENTITY));
		ResourceDownloadProps.Entity entity =  ResourceDownloadProps.Entity.values()[ordinal];
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		String fileName = "default.pdf";

		switch (entity) {
		case STANDARDIZED_PATIENT: {
			fileName = setStandardizedPatientResource(request, os);
			break;
		}

		case STANDARDIZED_PATIENT_EXPORT: {
			fileName = setStandardizedPatientExportResource(request, response,
					os);
			break;
		}
		case STANDARDIZED_ROLE: {
			fileName = setStandardizedRoleResource(request, os);
			break;
		}
		case SUMMONINGS : {
			fileName = setSummoningsResouce(request,os);
			break;
		}

		case INDIVIDUAL_SCHEDULE : {
			fileName = setIndividualScheduleResouce(request,os);
			break;
		}
		
		case STUDENT_MANAGEMENT : {
			fileName = setStudentManagementResouce(request,os);
			break;
		}
		
		case CHECKLIST : {
			fileName = setChecklistResouce(request,os);
			break;
		}
		
		case STANDARDIZED_PATIENT_PAYMENT : {
			fileName = setStandardizedPatientPaymentResource(request, response, os);
			break;
		}
		
		case PATIENT_IN_SEMESTER_CSV : {
			fileName = setExportPatientInSemesterCsv(request, response, os);
			break;
		}
		
		case ALARM_SCHEDULE_SUPERVISOR: {
			fileName = setExportScheduleCsv(request, response, os);
			break;
		}
		
		case STUDENT_MANAGEMENT_MIN_OPTION_VALUE:{
			fileName = setStudentManagementResouceMinValue(request,os);
			break;
		}
		case ROLE_CHECKLIST_EOSCE:{
			fileName = setRoleChecklisteOSCE(request,os);
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

	private static String setRoleChecklisteOSCE(HttpServletRequest request,ByteArrayOutputStream os) {
		Long roleId = Long.parseLong(request.getParameter(ResourceDownloadProps.ID));		
		
		return StandardizedRole.exportOsce(roleId,os);
	}

	private static String setStudentManagementResouceMinValue(HttpServletRequest request, ByteArrayOutputStream os) {
		Long studentId = Long
				.parseLong(request.getParameter(ResourceDownloadProps.ID));		
		Long osceId = Long
				.parseLong(request.getParameter(ResourceDownloadProps.OSCE_ID));
		String locale = request.getParameter(ResourceDownloadProps.LOCALE);
		
		String fileName = StandardizedRole.getRolePrintPDFByStudentForMinValueUsingServlet(studentId, osceId, locale, os);
		return fileName;
	}

	private static String setExportScheduleCsv(HttpServletRequest request, HttpServletResponse response, ByteArrayOutputStream os) {
		
		String fileName = "Schedule.csv";
		String osceDayId = request.getParameter(ResourceDownloadProps.OSCEDAYID);
		String plusTime = request.getParameter(ResourceDownloadProps.PLUS_TIME);
		String startTone = request.getParameter(ResourceDownloadProps.START_TONE);
		String endTone = request.getParameter(ResourceDownloadProps.END_TONE);
		String prePostEndTime = request.getParameter(ResourceDownloadProps.PRE_POST_END_TIME);
		String prePostEndTone = request.getParameter(ResourceDownloadProps.PRE_POST_END_TONE);
		String preBreakEndTime = request.getParameter(ResourceDownloadProps.PRE_BREAK_END_TIME);
		String preBreakEndTone = request.getParameter(ResourceDownloadProps.PRE_BREAK_END_TONE);
		
		if (osceDayId != null && plusTime != null && startTone != null && endTone != null && prePostEndTime != null && prePostEndTone != null && preBreakEndTime != null && preBreakEndTone != null)
		{
			OsceDay.exportCsvClicked(os, Long.parseLong(osceDayId), Integer.parseInt(startTone), Integer.parseInt(endTone), Integer.parseInt(prePostEndTime), Integer.parseInt(prePostEndTone), Integer.parseInt(preBreakEndTime), Integer.parseInt(preBreakEndTone), Integer.parseInt(plusTime));
		}
		
		return fileName;
	}

	private static String setExportPatientInSemesterCsv(HttpServletRequest request, HttpServletResponse response, ByteArrayOutputStream os) {
		
		String semesterIdTemp = request.getParameter(ResourceDownloadProps.ID);
		String filename = "default.csv";
		
		if (semesterIdTemp != null && semesterIdTemp.isEmpty() == false)
		{
			filename = PatientInSemester.exportCsv(Long.parseLong(semesterIdTemp), os);
		}
		return filename;	
	}

	private static String setChecklistResouce(HttpServletRequest request,
			ByteArrayOutputStream os) throws FileNotFoundException, IOException {
		
		HttpSession session = request.getSession();
		String fileName = request.getParameter(ResourceDownloadProps.NAME);
		
		if(StringUtils.isNotBlank(fileName) && session.getAttribute(fileName) != null) {
			try{	
				os.write(((ByteArrayOutputStream)session.getAttribute(fileName)).toByteArray());
			}finally {
				session.removeAttribute(fileName);
			}
		}
		
		return fileName;
	}

	private static String setStudentManagementResouce(HttpServletRequest request, ByteArrayOutputStream os) 
	{
	
		Long studentId = Long
				.parseLong(request.getParameter(ResourceDownloadProps.ID));		
		Long osceId = Long
				.parseLong(request.getParameter(ResourceDownloadProps.OSCE_ID));
		String locale = request.getParameter(ResourceDownloadProps.LOCALE);
		
		String fileName = StandardizedRole.getRolePrintPDFByStudentUsingServlet(studentId, osceId, locale, os);
		return fileName;
	}

	private static String setIndividualScheduleResouce(
			HttpServletRequest request, ByteArrayOutputStream os) throws IOException {
		
		HttpSession session = request.getSession();
		String fileName = "default.pdf";
		String key = request.getParameter(ResourceDownloadProps.INDIVIDUAL_SCHEDULE_KEY);
		
		
		if(StringUtils.isNotBlank(key) && session.getAttribute(key) != null) {
			try{
				fileName = key;
				os.write(((ByteArrayOutputStream)session.getAttribute(key)).toByteArray());
			}finally {
				session.removeAttribute(key);
			}
		}
		return fileName;
	}

	private static String setSummoningsResouce(HttpServletRequest request, ByteArrayOutputStream os) throws IOException {
		
		HttpSession session = request.getSession();
		String fileName = "default.pdf";
		String key = request.getParameter(ResourceDownloadProps.SUMMONING_KEY);
		
		
		if(StringUtils.isNotBlank(key) && session.getAttribute(key) != null) {
			try{
				fileName = key;
				os.write(((ByteArrayOutputStream)session.getAttribute(key)).toByteArray());
			}finally {
				session.removeAttribute(key);
			}
		}
		return fileName;
	}

//	private static String setStandardizedPatientExportResource(
//			HttpServletRequest request, HttpServletResponse response,
//			ByteArrayOutputStream os) {
//
//		HttpSession session = request.getSession();
//		
//		String name = (String) session.getAttribute(ResourceDownloadProps.NAME);
//		Sorting order = (Sorting) session.getAttribute(ResourceDownloadProps.SORT_ORDER);
//		String quickSearchTerm = (String) session
//				.getAttribute(ResourceDownloadProps.QUICK_SEARCH_TERM);
//		List<String> searchThrough = (List<String>) session
//				.getAttribute(ResourceDownloadProps.SEARCH_THROUGH_KEY);	
//		List<AdvancedSearchCriteria> searchCriterias  = (List<AdvancedSearchCriteria>) session
//				.getAttribute(ResourceDownloadProps.SEARCH_CRITERIA_MASTER_KEY);
//		int rangeStart = (Integer) session
//				.getAttribute(ResourceDownloadProps.RANGE_START);
//		int rangeLength = (Integer) session
//				.getAttribute(ResourceDownloadProps.RANGE_LENGTH);	
//
//		String fileName = "default.csv";
//		if(name != null && order != null && quickSearchTerm != null) {
//			fileName = StandardizedPatient
//					.getCSVMapperFindPatientsByAdvancedSearchAndSortUsingServlet(
//							name, order, quickSearchTerm, searchThrough,
//							searchCriterias, rangeStart,
//							rangeLength, os);
//			session.removeAttribute(ResourceDownloadProps.NAME);
//			session.removeAttribute(ResourceDownloadProps.SORT_ORDER);
//			session.removeAttribute(ResourceDownloadProps.QUICK_SEARCH_TERM);
//			session.removeAttribute(ResourceDownloadProps.SEARCH_THROUGH_KEY);
//			session.removeAttribute(ResourceDownloadProps.SEARCH_CRITERIA_MASTER_KEY);
//			session.removeAttribute(ResourceDownloadProps.RANGE_START);
//			session.removeAttribute(ResourceDownloadProps.RANGE_LENGTH);
//		}
//		
//		return fileName;
//	}

	private static String setStandardizedPatientExportResource(
			HttpServletRequest request, HttpServletResponse response,
			ByteArrayOutputStream os) {

		
		HttpSession session = request.getSession();
		String fileName = "default.csv";
		
		if(session.getAttribute(ResourceDownloadProps.SP_LIST) != null) {
			List<Long> ids = (List<Long>) session.getAttribute(ResourceDownloadProps.SP_LIST);
	
			
			fileName = StandardizedPatient
					.getCSVMapperFindPatientsByAdvancedSearchAndSortForSP(ids,
						os);
			session.removeAttribute(ResourceDownloadProps.SP_LIST);
		}
		
		return fileName;
	}
	private static String getPayload(List<Cookie> cookies, final String key, HttpServletResponse response) {
		
		String payload = null;
		Cookie cookie = (Cookie) CollectionUtils.find(cookies,
				new Predicate() {

			@Override
			public boolean evaluate(Object object) {
				String currentKey = "";
				try {
					currentKey = URLDecoder.decode(((Cookie) object)
							.getName(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					Log.error("Error in Key ",e);						
				}
						
				return currentKey.equals(key);
			}
		});
		
		if(cookie != null) {
			payload = cookie.getValue();
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
		
		return payload; 
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
				.getRolesPrintPdfBySearchUsingServlet(id,Arrays.asList(filter), selectedRoleItemAccess, locale,os);

		return fileName;
	}

	
	private static String setStandardizedPatientPaymentResource(
			HttpServletRequest request, HttpServletResponse response,
			ByteArrayOutputStream os) throws IOException {
		
		HttpSession session = request.getSession();
		String fileName = "default.pdf";
		
		if(session.getAttribute(ResourceDownloadProps.SP_LIST) != null) {
			
			final List<Long> ids = (List<Long>) session.getAttribute(ResourceDownloadProps.SP_LIST);
			final String column = (String) session.getAttribute(ResourceDownloadProps.COLUMN_NAME);
			final Sorting sortOrder = (Sorting) session.getAttribute(ResourceDownloadProps.SORT_ORDER);
			final List<StandardizedPatient> spList; 
			if(StringUtils.isNotBlank(column) && sortOrder != null) {
				spList = StandardizedPatient.findPatientsByidsAndSortByColumn(ids,column,sortOrder);
			}else{
				spList = StandardizedPatient.findPatientsByids(ids);	
			}
				
			
			
			if(spList != null & spList.size() > 0) {
				StandardizedPatientPaymentUtil paymentUtil =  new StandardizedPatientPaymentUtil(spList,os,session);
				fileName = paymentUtil.createPDF();
			}
			
			session.removeAttribute(ResourceDownloadProps.SP_LIST);
			session.removeAttribute(ResourceDownloadProps.COLUMN_NAME);
			session.removeAttribute(ResourceDownloadProps.SORT_ORDER);
		}
		
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
		if(resource.length > 0) {
			stream.write(resource);
		}
		stream.close();
	}

}
