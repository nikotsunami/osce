/**
 * 
 */
package ch.unibas.medizin.osce.client;

import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.shared.TemplateTypes;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author rahul
 *
 */
@RemoteServiceRelativePath("schedule")
public interface IndividualScheduleService extends RemoteService {

	Boolean saveTemplate(String osceId,TemplateTypes templateTypes, String templateContent);

	Boolean deleteTemplate(String osceId,TemplateTypes templateTypes);
	
	String generateStudentPDFUsingTemplate(String osceId, TemplateTypes templateTypes, List<Long> studId, Long semesterId);
	
	String generateSPPDFUsingTemplate(String osceId,
			TemplateTypes templateTypes, Map templateVariables,
			List<Long> spId, Long semesterId, String localeName);
	
	String generateExaminerPDFUsingTemplate(String osceId, TemplateTypes templateTypes, List<Long> examinerId, Long semesterId);

	//String[] getStudTemplateContent(String templateName);
	String[] getStudTemplateContent(String osceId,TemplateTypes templateTypes);
	
	String[] getExaminerTemplateContent(String osceId,TemplateTypes templateTypes);
	
	String[] getTemplateContent(String osceId,TemplateTypes templateTypes);
	
}
