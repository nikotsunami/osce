package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.domain.RoleTopic;
import ch.unibas.medizin.osce.shared.Sorting;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(RoleTopic.class)
public interface RoleTopicRequestNonRoo extends RequestContext{


abstract Request<Long> countRoleTopicBySpecialisationId(String searchValue,Long specialisationId);
abstract Request<List<RoleTopicProxy>> findRoleTopicBySpecialisationId(String sortname,Sorting sortorder,String srarchValue,Long specialisationId, int firstResult, int maxResults);
abstract Request<List<SpecialisationProxy>> findAllSpecialisation();
//spec start
abstract Request<List<RoleTopicProxy>> findRoleTopicsByAdvancedSearchAndSort(
		String sortColumn,
		Sorting order,
		String searchWord, 
		List<String> searchThrough,
		List<AdvancedSearchCriteriaProxy> searchCriteria,
		Integer firstResult, 
		Integer maxResults
);
}