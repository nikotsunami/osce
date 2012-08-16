/**
 * 
 */
package ch.unibas.medizin.osce.client;

import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author rahul
 *
 */
public interface IndividualScheduleServiceAsync  
{

	void saveTemplate(String templateName, String templateContent, AsyncCallback<Boolean> asyncCallback);

	void getTemplateContent(String templateName, AsyncCallback<String[]> asyncCallback);
	
	void deleteTemplate(String templateName,AsyncCallback<Boolean> asyncCallback);
	
	void generateSPPDFUsingTemplate(String templateName,Map templateVariables, List<Long> spId, Long semesterId,AsyncCallback<String> asyncCallback);
	
	void getStudTemplateContent(String templateName, AsyncCallback<String[]> asyncCallback);
	
	void generateStudentPDFUsingTemplate(String templateName, List<Long> studId, Long semesterId,AsyncCallback<String> asyncCallback);
	
	void getExaminerTemplateContent(String templateName, AsyncCallback<String[]> asyncCallback);
	
	void generateExaminerPDFUsingTemplate(String templateName, List<Long> examinerId, Long semesterId,AsyncCallback<String> asyncCallback);
}
