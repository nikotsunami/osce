// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName("ch.unibas.medizin.osce.domain.RoleTableItemValue")
public interface RoleTableItemValueRequest extends RequestContext {

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.RoleTableItemValueProxy, java.lang.Void> persist();

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.RoleTableItemValueProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countRoleTableItemValues();

    abstract Request<ch.unibas.medizin.osce.client.managed.request.RoleTableItemValueProxy> findRoleTableItemValue(Integer id);

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.RoleTableItemValueProxy>> findAllRoleTableItemValues();

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.RoleTableItemValueProxy>> findRoleTableItemValueEntries(int firstResult, int maxResults);

	abstract Request<List<RoleTableItemValueProxy>> findRoleTableItemValueByStandardizedRoleANDRoleBaseItemValues(Long srID, Long rbItemID);

}
