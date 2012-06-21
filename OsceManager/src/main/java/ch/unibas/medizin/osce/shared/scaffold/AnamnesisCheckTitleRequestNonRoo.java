package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
import ch.unibas.medizin.osce.domain.AnamnesisCheckTitle;

import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(AnamnesisCheckTitle.class)
public interface AnamnesisCheckTitleRequestNonRoo extends RequestContext{
	
	 public abstract InstanceRequest<AnamnesisCheckTitleProxy, Void> insertNewSortOder(Integer preSortorder);
	 
	 public abstract Request<List<AnamnesisCheckTitleProxy>> getReSortingList(Integer sortFrom);
		
	 public abstract Request<Void> reSorting(Integer sortFrom);	 
}
