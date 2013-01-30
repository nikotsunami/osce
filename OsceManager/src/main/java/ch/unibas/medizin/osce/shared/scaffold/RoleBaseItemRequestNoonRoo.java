package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy;
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
	abstract Request<List<RoleBaseItemProxy>> findAllRoleBaseItemOnTemplateId(Integer id);
	public abstract Request<List<RoleBaseItemProxy>> findRoleBaseItemByStandardizedRole(Long id);
	public abstract Request<List<RoleBaseItemProxy>> findRoleBaseItemByStandardizedRoleAndRoleTemplateId(Long srID, Integer rtId);
	public abstract Request<List<RoleBaseItemProxy>> findRoleBaseItemByTemplateId(Integer rtId);
	public abstract Request<Boolean> createRoleBaseItemValueForStandardizedRole(Long srID,Integer rtId);
	
}
