package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.SkillHasApplianceProxy;
import ch.unibas.medizin.osce.domain.SkillHasAppliance;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(SkillHasAppliance.class)
public interface SkillHasApplianceRequestNonRoo extends RequestContext {
	
	abstract Request<List<SkillHasApplianceProxy>> findSkillHasApplianceBySkill(Long skillId);
	
}
