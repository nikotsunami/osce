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

	Boolean sendMailUsingTemplate(String semesterId,Boolean isExaminer, Map templateVariables);

	Boolean saveTemplate(String semesterId, Boolean isExaminer,Boolean isEmail, String templateContent);

	String[] getTemplateContent(String semesterId, Boolean isExaminer,Boolean isEmail);

	Boolean deleteTemplate(String semesterId, Boolean isExaminer,Boolean isEmail);

	String generateMailPDFUsingTemplate(String semesterId,Boolean isExaminer,Map templateVariables);

	String generateSPMailPDF(Long semesterId, List<Long> spIds);

	Boolean sendSPMail(Long semesterId, List<Long> spIds);

	String generateExaminerMailPDF(Long semesterId, List<Long> examinerIds);

	Boolean sendExaminerMail(Long semesterId, List<Long> examinerIds);

}
