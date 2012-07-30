package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.MinorSkillProxy;
import ch.unibas.medizin.osce.domain.MinorSkill;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;


@SuppressWarnings("deprecation")
@Service(MinorSkill.class)
public interface MinorSkillRequestNonRoo extends RequestContext {
	abstract Request<List<MinorSkillProxy>> findMinorSkillEntriesByRoleID(long value);
}
