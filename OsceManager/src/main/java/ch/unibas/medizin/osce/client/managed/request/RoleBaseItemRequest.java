// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName("ch.unibas.medizin.osce.domain.RoleBaseItem")
public interface RoleBaseItemRequest extends RequestContext {

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy, java.lang.Void> persist();

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countRoleBaseItems();

    abstract Request<ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy> findRoleBaseItem(Long id);

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy>> findAllRoleBaseItems();

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy>> findRoleBaseItemEntries(int firstResult, int maxResults);

	abstract Request<List<RoleBaseItemProxy>> findAllDeletedRoleBaseItems(Integer id);
	abstract InstanceRequest<RoleBaseItemProxy, Void> baseItemUpButtonClicked();
	abstract InstanceRequest<RoleBaseItemProxy, Void> baseItemDownButtonClicked();
	abstract Request<List<RoleBaseItemProxy>> findAllRoleBaseItemOnTemplateId(Integer id);
	abstract Request<List<RoleBaseItemProxy>> findRoleBaseItemByStandardizedRole(Long id);
	abstract Request<List<RoleBaseItemProxy>> findRoleBaseItemByStandardizedRoleAndRoleTemplateId(Long srID, Integer rtId);
	abstract Request<List<RoleBaseItemProxy>> findRoleBaseItemByTemplateId(Integer rtId);
	abstract Request<Boolean> createRoleBaseItemValueForStandardizedRole(Long srID,Integer rtId);

}