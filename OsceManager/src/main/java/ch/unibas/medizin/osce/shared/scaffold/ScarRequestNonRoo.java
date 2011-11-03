package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.domain.Scar;

import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@Service(Scar.class)
public interface ScarRequestNonRoo extends RequestContext {
	
	abstract Request<Long> countScarsByName(String name);
	
	abstract Request<List<ScarProxy>> findScarEntriesByName(String name, int firstResult, int maxResults);

	abstract Request<Long> countScarsByAnamnesisForm(Long anamnesisFormId);

	abstract Request<List<ScarProxy>> findScarEntriesByAnamnesisForm(Long anamnesisFormId, int firstResult, int maxResults);

	abstract Request<List<ScarProxy>> findScarEntriesByNotAnamnesisForm(Long anamnesisFormId); 
}
