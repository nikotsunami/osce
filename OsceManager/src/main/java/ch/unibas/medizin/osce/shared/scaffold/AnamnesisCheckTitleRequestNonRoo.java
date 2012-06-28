package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
import ch.unibas.medizin.osce.domain.AnamnesisCheckTitle;

import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(AnamnesisCheckTitle.class)
public interface AnamnesisCheckTitleRequestNonRoo extends RequestContext{
	
	public abstract InstanceRequest<AnamnesisCheckTitleProxy, Void> insertNewSortOder(int preSortorder);
	public abstract Request<AnamnesisCheckTitleProxy> findAnamnesisChecksBySortOder(int sort_order);
	 public abstract InstanceRequest<AnamnesisCheckTitleProxy, Void> oderByPreviousAnamnesisCheckTitle(int preSortorder);
	 public abstract Request<List<AnamnesisCheckTitleProxy>> findAnamnesisCheckTitlesBySortOderBetween(int lower, int upper);
	 
	 
}
