package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTableItemProxy;
import ch.unibas.medizin.osce.domain.RoleBaseItem;

import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(RoleBaseItem.class)
public interface RoleBaseItemRequestNoonRoo   extends RequestContext{
	abstract Request<List<RoleBaseItemProxy>> findAllDeletedRoleBaseItems(Integer id);
	public abstract InstanceRequest<RoleBaseItemProxy, Void> baseItemUpButtonClicked();
	public abstract InstanceRequest<RoleBaseItemProxy, Void> baseItemDownButtonClicked();
	
}
