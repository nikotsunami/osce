package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.MainSkillProxy;
import ch.unibas.medizin.osce.domain.MainSkill;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(MainSkill.class)
public interface MainSkillRequestNonRoo extends RequestContext {

	abstract Request<List<MainSkillProxy>> findMainSkillEntriesByRoleID(long value,int start,int end);
	abstract Request<Long> countMainSkillEntriesByRoleID(long value);
}
