/**
 * 
 */
package ch.unibas.medizin.osce.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author rahul
 *
 */
@RemoteServiceRelativePath("summonings")
public interface SummoningsService extends RemoteService {

	/*Boolean sendMailUsingTemplate(String semesterId,Boolean isExaminer, Map templateVariables);*/

	Boolean saveTemplate(String templateFileName/*semesterId*/, Boolean isExaminer,Boolean isEmail, String templateContent);

	String[] getTemplateContent(String templateFileName/*semesterId*/, Boolean isExaminer,Boolean isEmail);

	Boolean deleteTemplate(String templateFileName/*semesterId*/, Boolean isExaminer,Boolean isEmail);

	/*String generateMailPDFUsingTemplate(String semesterId,Boolean isExaminer,Map templateVariables);*/

	String generateSPMailPDF(Long semesterId, List<Long> spIds,String templateFileName);

	Boolean sendSPMail(Long semesterId, List<Long> spIds,String templateFileName,String subject);

	String generateExaminerMailPDF(Long semesterId, List<Long> examinerIds,String templateFileName);

	Boolean sendExaminerMail(Long semesterId, List<Long> examinerIds,String templateFileName, String subject);

	List<String> getAllTemplateFileNames(Boolean isExaminer, Boolean isEmail);
	
	String getDefaultTemplateContent();
}
