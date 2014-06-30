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

	/*void sendMailUsingTemplate(String semesterId, Boolean isExaminer, Map templateVariables,AsyncCallback<Boolean> asyncCallback);*/
	
	void saveTemplate(String templateFileName/*semesterId*/, Boolean isExaminer,Boolean isEmail, String templateContent, AsyncCallback<Boolean> asyncCallback);

	void getTemplateContent(String templateFileName/*semesterId*/, Boolean isExaminer,Boolean isEmail, AsyncCallback<String[]> asyncCallback);
	
	void deleteTemplate(String semesterId, Boolean isExaminer,Boolean isEmail,AsyncCallback<Boolean> asyncCallback);
	
	/*void generateMailPDFUsingTemplate(String semesterId,Boolean isExaminer,Map templateVariables, AsyncCallback<String> asyncCallback);*/
	
	void generateSPMailPDF(Long semesterId, List<Long> spIds,String templateFileName, AsyncCallback<String> asyncCallback);
	
	void sendSPMail(Long semesterId, List<Long> spIds,String templateFileName, String subject, String sendCopy, String emailFrom, AsyncCallback<Boolean> asyncCallback);
	
	void generateExaminerMailPDF(Long semesterId, List<Long> examinerIds,String templateFileName, AsyncCallback<String> asyncCallback);
	
	void sendExaminerMail(Long semesterId, List<Long> examinerIds,String templateFileName, String subject, String sendCopy, String emailFrom, AsyncCallback<Boolean> asyncCallback);

	void getAllTemplateFileNames(Boolean isExaminer, Boolean isEmail, AsyncCallback<List<String>> asyncCallback);

	void getDefaultTemplateContent(AsyncCallback<String> asyncCallback);

	void checkIfFileExists(String templateFilePath, Boolean isExaminer, Boolean isEmail, AsyncCallback<Boolean> asyncCallback);
}
