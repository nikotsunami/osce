/**
 * 
 */
package ch.unibas.medizin.osce.client;

import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.shared.TemplateTypes;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author rahul
 *
 */
public interface IndividualScheduleServiceAsync  
{	
	void getTemplateContent(String osceId, TemplateTypes templateTypes, AsyncCallback<String[]> asyncCallback);

	//void getStudTemplateContent(String templateName, AsyncCallback<String[]> asyncCallback);
	void getStudTemplateContent(String osceId, TemplateTypes templateTypes, AsyncCallback<String[]> asyncCallback);

	void getExaminerTemplateContent(String osceId, TemplateTypes templateTypes, AsyncCallback<String[]> asyncCallback);

	void saveTemplate(String osceId,TemplateTypes templateTypes, String templateContent, AsyncCallback<Boolean> asyncCallback);

	void deleteTemplate(String osceId,TemplateTypes templateTypes, AsyncCallback<Boolean> asyncCallback);

	void generateSPPDFUsingTemplate(String osceId, TemplateTypes templateTypes, Map templateVariables, List<Long> spId, Long semesterId, String localeName, AsyncCallback<String> asyncCallback);

	void generateStudentPDFUsingTemplate(String osceId, TemplateTypes templateTypes, List<Long> studId, Long semesterId, AsyncCallback<String> asyncCallback);

	void generateExaminerPDFUsingTemplate(String osceId, TemplateTypes templateTypes, List<Long> examinerId, Long semesterId, AsyncCallback<String> asyncCallback);
}
