package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.domain.AnamnesisCheck;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;

import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(AnamnesisCheck.class)
public interface AnamnesisCheckRequestNonRoo extends RequestContext {
	public abstract InstanceRequest<AnamnesisCheckProxy, Void> moveUp();
	public abstract InstanceRequest<AnamnesisCheckProxy, Void> moveDown();
	public abstract Request<AnamnesisCheckProxy> findAnamnesisCheckByOrderSmaller(int sort_order);
	public abstract Request<AnamnesisCheckProxy> findAnamnesisCheckByOrderGreater(int sort_order);
	public abstract Request<List<AnamnesisCheckProxy>> findAnamnesisChecksBySearch(String q, int firstResult, int maxResults);
	public abstract Request<Long> countAnamnesisChecksBySearch(String q,AnamnesisCheckProxy title);
	public abstract Request<List<AnamnesisCheckProxy>> findAnamnesisChecksBySearch(String q,AnamnesisCheckProxy title, int firstResult, int maxResults);
	public abstract Request<List<AnamnesisCheckProxy>> findAnamnesisChecksByType(AnamnesisCheckTypes type);
	public abstract Request<List<AnamnesisCheckProxy>> findAnamnesisChecksByTitle(String searchValue,AnamnesisCheckProxy title);
	public abstract Request<List<AnamnesisCheckProxy>> findAnamnesisChecksByType(AnamnesisCheckTypes type);
	public abstract InstanceRequest<AnamnesisCheckProxy, Void> orderUpByPrevious(int preSortorder);
	public abstract InstanceRequest<AnamnesisCheckProxy, Void> orderDownByPrevious(int preSortorder);
	public abstract InstanceRequest<AnamnesisCheckProxy, Void> insertNewSortOder(int preSortorder);
	public abstract Request<AnamnesisCheckProxy> findAnamnesisChecksBySortOder(int sort_order);
	public abstract Request<List<AnamnesisCheckProxy>> findAnamnesisChecksByTitle(String searchValue, AnamnesisCheckProxy title);
	public abstract Request<AnamnesisCheckProxy> findPreviousTitleBySortOder(int sort_order);	
//	public abstract Request<List<AnamnesisCheckProxy>> findAnamnesisChecksByAnamnesisForm(Long anamnesisFormId, int firstResult, int maxResults);
//	public abstract Request<Long> countAnamnesisChecksByAnamnesisForm(Long anamnesisFormId);
}
