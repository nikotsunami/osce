package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.SimpleSearchCriteriaProxy;
import ch.unibas.medizin.osce.domain.SimpleSearchCriteria;

import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(SimpleSearchCriteria.class)
public interface SimpleSearchCriteriaRequestNonRoo extends RequestContext {
	
	//abstract Request<Long> countFilesByName(String name);
	
	//abstract Request<List<FileProxy>> findFileEntriesByName(String name, int firstResult, int maxResults);
	
	abstract Request<Long> countSimpleSearchByStandardizedRoleID(long standardizedRoleID);
	
	abstract Request<List<SimpleSearchCriteriaProxy>> findSimpleSearchByStandardizedRoleID(long standardizedRoleID, int firstResult, int maxResults);
	
	public abstract InstanceRequest<SimpleSearchCriteriaProxy, Void> simpleSearchMoveUp(long standardizedRoleID);
	public abstract InstanceRequest<SimpleSearchCriteriaProxy, Void> simpleSearchMoveDown(long standardizedRoleID);
	public abstract Request<SimpleSearchCriteriaProxy> findSimpleSearchByOrderSmaller(long standardizedRoleID,int sort_order);
	public abstract Request<SimpleSearchCriteriaProxy> findSimpleSearchByOrderGreater(long standardizedRoleID,int sort_order);
	
	
	//abstract Request<Long> countScarsByAnamnesisForm(Long anamnesisFormId);

	//abstract Request<List<ScarProxy>> findScarEntriesByAnamnesisForm(Long anamnesisFormId, int firstResult, int maxResults);

	//abstract Request<List<ScarProxy>> findScarEntriesByNotAnamnesisForm(Long anamnesisFormId); 
}
