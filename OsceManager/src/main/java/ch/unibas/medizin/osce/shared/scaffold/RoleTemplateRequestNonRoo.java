package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.RoleTemplateProxy;
import ch.unibas.medizin.osce.domain.RoleTemplate;

import ch.unibas.medizin.osce.shared.Sorting;
import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;
import com.google.gwt.requestfactory.shared.ServiceName;


@SuppressWarnings("deprecation")
@ServiceName("ch.unibas.medizin.osce.domain.RoleTemplate")
public interface RoleTemplateRequestNonRoo extends RequestContext{
	
	// abstract Request<Long> countRoleTemplateName(String name);
	//
	// abstract Request<List<RoleTemplateProxy>>
	// findRoleTemplateEntriesByName(String name, int firstResult, int
	// maxResults);

	abstract Request<java.util.List<RoleTemplateProxy>> findRoleTemplateByName(
			String sortColumn, Sorting order, String searchWord,
			List<String> searchThrough, int firstResult, int maxResults);

	abstract Request<java.lang.Long> countRoleTemplateByName(String searchWord,
			List<String> searchThrough);
	
	abstract Request<List<RoleTemplateProxy>> findAllTemplateName(String sortColumn,Sorting order,String searchWord, int firstResult,int maxResult);
	
	abstract Request<Long>findCountOfStandardizedRoleAssignForTemplate(Integer id);
	
	abstract Request<Boolean> deleteRoleTemplate(Integer roleTemplateId);
}
