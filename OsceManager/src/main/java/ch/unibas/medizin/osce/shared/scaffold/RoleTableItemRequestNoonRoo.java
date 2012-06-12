package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.RoleTableItemProxy;

import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.ServiceName;

@SuppressWarnings("deprecation")
@ServiceName("ch.unibas.medizin.osce.domain.RoleTableItem")
public interface RoleTableItemRequestNoonRoo extends RequestContext{

	abstract Request<List<RoleTableItemProxy>> findRoleTableItemByBaseItemId(Long id);
	public abstract InstanceRequest<RoleTableItemProxy, Void> roleTableItemMoveUp(Long id);
	public abstract InstanceRequest<RoleTableItemProxy, Void> roleTableItemMoveDown(Long id);
}
