/**
 * 
 */
package ch.unibas.medizin.osce.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author rahul
 *
 */
public interface SummoningsServiceAsync  {

	void sendMailUsingTemplate(String templateName, Map templateVariables,AsyncCallback<Boolean> asyncCallback);
	
	void saveTemplate(String templateName, String templateContent, AsyncCallback<Boolean> asyncCallback);

	void getTemplateContent(String templateName, AsyncCallback<String[]> asyncCallback);
	
	void deleteTemplate(String templateName,AsyncCallback<Boolean> asyncCallback);
	
	void generateMailPDFUsingTemplate(String templateName,Map templateVariables, AsyncCallback<String> asyncCallback);
	
	void generateSPMailPDF(Long semesterId, List<Long> spIds, AsyncCallback<String> asyncCallback);
	
	void sendSPMail(Long semesterId, List<Long> spIds, AsyncCallback<Boolean> asyncCallback);
	
	void generateExaminerMailPDF(Long semesterId, List<Long> examinerIds, AsyncCallback<String> asyncCallback);
	
	void sendExaminerMail(Long semesterId, List<Long> examinerIds, AsyncCallback<Boolean> asyncCallback);
}
