package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
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
	public abstract InstanceRequest<AnamnesisCheckProxy, Void> changeSortOrder(Integer newSortOrder);
    public abstract InstanceRequest<AnamnesisCheckProxy, Void> oderByPreviousAnamnesisCheck(int preSortorder);
    public abstract InstanceRequest<AnamnesisCheckProxy, Void> insertAnamnesisCheck(int preSortorder);
    public abstract Request<List<AnamnesisCheckProxy>> findAnamnesisChecksBySearch(String q, int firstResult, int maxResults);
    public abstract Request<Long> countAnamnesisChecksBySearch(String q);
    public abstract Request<Long> countAnamnesisChecksBySearchWithTitle(String q,AnamnesisCheckProxy title);
    public abstract Request<List<AnamnesisCheckProxy>> findAnamnesisChecksBySearchWithTitle(String q,AnamnesisCheckProxy title, int firstResult, int maxResults);
    public abstract Request<List<AnamnesisCheckProxy>> findAnamnesisChecksByType(AnamnesisCheckTypes type);
    public abstract Request<List<AnamnesisCheckProxy>> findAnamnesisChecksByTitle(String searchValue,AnamnesisCheckProxy title);
    public abstract Request<AnamnesisCheckProxy> findAnamnesisChecksBySortOder(int sort_order);
	public abstract Request<List<AnamnesisCheckProxy>> findAnamnesisChecksBySearchWithAnamnesisCheckTitle(String q,AnamnesisCheckTitleProxy title);
	public abstract Request<List<AnamnesisCheckTitleProxy>> findTitlesContatisAnamnesisChecksWithSearching(String q,AnamnesisCheckTitleProxy title);
	public abstract Request<AnamnesisCheckProxy> findPreviousAnamnesisCheck(int sort_order, AnamnesisCheckTitleProxy anamnesisCheckTitleProxy);
	
	public abstract Request<List<AnamnesisCheckProxy>> getReSortingList(AnamnesisCheckTitleProxy anamnesisCheckTitle, Integer sortFrom);
	public abstract Request<Void> reSorting(AnamnesisCheckTitleProxy anamnesisCheckTitle, Integer sortFrom);
	public abstract Request<Boolean> saveOrEditAnamnesisCheck(AnamnesisCheckProxy anamnesisCheck);
	public abstract Request<Boolean> deleteAnamnesisCheckFromSpPortal(Long long1);
}
