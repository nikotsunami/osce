package ch.unibas.medizin.osce.shared.scaffold;
//compelete file spec
import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.domain.StandardizedRole;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(StandardizedRole.class)
public interface StandardizedRoleRequestNonRoo extends RequestContext 
{
	
	//abstract Request<Long> countKeywordsByStandRole(Long standardizedRoleProxy);	
	abstract Request<StandardizedRoleProxy> createStandardizedRoleMajorVersion(Long standardizedRoleId,Integer roleSubItemValueId,String value);
	abstract Request<Boolean> copyStandardizedRole(Long standardizedRoleId);
	
	//Issue : 120
	abstract Request<String> getRolesPrintPdfBySearch(Long standardizedRoleId , List<String> itemsList,Long roleItemAccessId,String locale);
	//Issue : 120
}


//spec end