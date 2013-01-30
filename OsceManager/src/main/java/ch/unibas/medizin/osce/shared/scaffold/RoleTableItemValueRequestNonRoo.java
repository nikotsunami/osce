package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.RoleTableItemValueProxy;
import ch.unibas.medizin.osce.domain.RoleTableItemValue;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(RoleTableItemValue.class)
public interface RoleTableItemValueRequestNonRoo extends RequestContext {
	public abstract Request<List<RoleTableItemValueProxy>> findRoleTableItemValueByStandardizedRoleANDRoleBaseItemValues(Long srID, Long rbItemID);
}
