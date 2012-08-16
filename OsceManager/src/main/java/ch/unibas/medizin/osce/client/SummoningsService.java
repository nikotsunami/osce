/**
 * 
 */
package ch.unibas.medizin.osce.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author rahul
 *
 */
@RemoteServiceRelativePath("summonings")
public interface SummoningsService extends RemoteService {

	Boolean sendMailUsingTemplate(String templateName, Map templateVariables);

	Boolean saveTemplate(String templateName, String templateContent);

	String[] getTemplateContent(String templateName);

	Boolean deleteTemplate(String templateName);

	String generateMailPDFUsingTemplate(String templateName,Map templateVariables);

	String generateSPMailPDF(Long semesterId, List<Long> spIds);

	Boolean sendSPMail(Long semesterId, List<Long> spIds);

	String generateExaminerMailPDF(Long semesterId, List<Long> examinerIds);

	Boolean sendExaminerMail(Long semesterId, List<Long> examinerIds);

}
