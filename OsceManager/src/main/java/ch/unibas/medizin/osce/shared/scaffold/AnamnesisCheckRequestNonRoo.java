package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.domain.AnamnesisCheck;

import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@Service(AnamnesisCheck.class)
public interface AnamnesisCheckRequestNonRoo extends RequestContext {
	
	abstract Request<Long> countAnamnesisChecksBySearch(String q);
	
	abstract Request<List<AnamnesisCheckProxy>> findAnamnesisChecksBySearch(String q, int firstResult, int maxResults);
}
