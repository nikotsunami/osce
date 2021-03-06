// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName("ch.unibas.medizin.osce.domain.StandardizedRole")
public interface StandardizedRoleRequest extends RequestContext {

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy, java.lang.Void> persist();

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countStandardizedRoles();

    abstract Request<ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy> findStandardizedRole(Long id);

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy>> findAllStandardizedRoles();

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy>> findStandardizedRoleEntries(int firstResult, int maxResults);

	abstract Request<StandardizedRoleProxy> createStandardizedRoleMajorVersion(Long standardizedRoleId,Integer roleSubItemValueId,String value);
	abstract Request<Boolean> copyStandardizedRole(Long standardizedRoleId);
	
	//Issue : 120
	abstract Request<String> getRolesPrintPdfBySearch(Long standardizedRoleId , List<String> itemsList,Long roleItemAccessId,String locale);
	//Issue : 120
	
	//export checklist
	abstract Request<String> exportChecklistByStandardizedRole(Long standardizedRoleId);
	abstract Request<List<StandardizedRoleProxy>> findRoleByRoleTopic(Long roleTopicId);
	
	abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy>> findAllStandardizeRolesOfPreviousVersion(Long id);

	abstract Request<String> createChecklistQRImageByChecklistId(Long checklistId);
	//export checklist
	//abstract Request<String> exportOsce(Long id);

	abstract Request<List<StandardizedRoleProxy>> findRolesFromSpecialisationId(Long specialisationId, Long currentStandardizedRoleId);

	abstract Request<List<StandardizedRoleProxy>> findRolesExceptCurrentRole(Long currentRoleId);

}
