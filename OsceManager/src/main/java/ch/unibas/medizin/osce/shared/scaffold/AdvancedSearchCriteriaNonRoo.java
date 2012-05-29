package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.domain.AdvancedSearchCriteria;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(AdvancedSearchCriteria.class)
public interface AdvancedSearchCriteriaNonRoo extends RequestContext {
	
	abstract Request<Long> countAdvancedSearchCriteriasByStandardizedRoleID(
			long standardizedRoleID);
	
	abstract Request<List<AdvancedSearchCriteriaProxy>> findAdvancedSearchCriteriasByStandardizedRoleID(
			long standardizedRoleID, int firstResult, int maxResults);
	
}
