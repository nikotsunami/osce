/**
 * 
 */
package ch.unibas.medizin.osce.client;

import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author rahul
 *
 */
@RemoteServiceRelativePath("schedule")
public interface IndividualScheduleService extends RemoteService {

	String generateSPPDFUsingTemplate(String templateName,Map templateVariables, List<Long> spId,Long semesterId);
	
	Boolean saveTemplate(String templateName, String templateContent);

	String[] getTemplateContent(String templateName);

	Boolean deleteTemplate(String templateName);

	String[] getStudTemplateContent(String templateName);
	
	String generateStudentPDFUsingTemplate(String templateName, List<Long> studId, Long semesterId);
	
	String[] getExaminerTemplateContent(String templateName);
	
	String generateExaminerPDFUsingTemplate(String templateName, List<Long> examinerId, Long semesterId);

	
}
