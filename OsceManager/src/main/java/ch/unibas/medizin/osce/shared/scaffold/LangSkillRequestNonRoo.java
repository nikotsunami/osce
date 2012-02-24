package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.LangSkillProxy;
import ch.unibas.medizin.osce.domain.LangSkill;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(LangSkill.class)
public interface LangSkillRequestNonRoo extends RequestContext {
	
	abstract Request<List<LangSkillProxy>> findLangSkillsByPatientId(Long patientId,  int firstResult, int maxResults);
	abstract Request<Long> countLangSkillsByPatientId(Long patientId);
	
//	abstract Request<List<SpokenLanguageProxy>> findAllLanguages();
//	
//	abstract Request<Long> countLanguagesByName(String name);
//	
//	abstract Request<List<SpokenLanguageProxy>> findLanguagesByName(String name, int firstResult, int maxResults);
//	
//	abstract Request<List<SpokenLanguageProxy>> findLanguagesByNotStandardizedPatient(Long patientId);
}
