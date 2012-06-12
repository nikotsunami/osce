package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.UsedMaterialProxy;
import ch.unibas.medizin.osce.domain.UsedMaterial;

import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(UsedMaterial.class)
public interface UsedMaterialRequestNonRoo extends RequestContext {
	public abstract InstanceRequest<UsedMaterialProxy, Void> moveMaterialUp(
			long standardizedRoleID);

	public abstract InstanceRequest<UsedMaterialProxy, Void> moveMaterialDown(
			long standardizedRoleID);

	public abstract Request<UsedMaterialProxy> findUsedMaterialByOrderSmaller(
			long standardizedRoleID, int sort_order);

	public abstract Request<UsedMaterialProxy> findUsedMaterialByOrderGreater(
			long standardizedRoleID, int sort_order);

	abstract Request<Long> countUsedMaterialsByStandardizedRoleID(
			long standardizedRoleID);

	abstract Request<List<UsedMaterialProxy>> findUsedMaterialsByStandardizedRoleID(
			long standardizedRoleID, int firstResult, int maxResults);

}
