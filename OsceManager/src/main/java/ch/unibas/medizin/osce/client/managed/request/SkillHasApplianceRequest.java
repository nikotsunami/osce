// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName("ch.unibas.medizin.osce.domain.SkillHasAppliance")
public interface SkillHasApplianceRequest extends RequestContext {

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.SkillHasApplianceProxy, java.lang.Void> persist();

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.SkillHasApplianceProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countSkillHasAppliances();

    abstract Request<ch.unibas.medizin.osce.client.managed.request.SkillHasApplianceProxy> findSkillHasAppliance(Long id);

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.SkillHasApplianceProxy>> findAllSkillHasAppliances();

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.SkillHasApplianceProxy>> findSkillHasApplianceEntries(int firstResult, int maxResults);
}
