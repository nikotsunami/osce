// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName("ch.unibas.medizin.osce.domain.LangSkill")
public interface LangSkillRequest extends RequestContext {

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.LangSkillProxy, java.lang.Void> persist();

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.LangSkillProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countLangSkills();

    abstract Request<ch.unibas.medizin.osce.client.managed.request.LangSkillProxy> findLangSkill(Long id);

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.LangSkillProxy>> findAllLangSkills();

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.LangSkillProxy>> findLangSkillEntries(int firstResult, int maxResults);

	abstract Request<List<LangSkillProxy>> findLangSkillsByPatientId(Long patientId,  int firstResult, int maxResults);
	abstract Request<Long> countLangSkillsByPatientId(Long patientId);

}