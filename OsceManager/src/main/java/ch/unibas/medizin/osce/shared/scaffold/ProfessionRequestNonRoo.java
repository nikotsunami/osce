package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.ProfessionProxy;
import ch.unibas.medizin.osce.domain.Profession;

import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@Service(Profession.class)
public interface ProfessionRequestNonRoo extends RequestContext {
	
	abstract Request<Long> countProfessionsByName(String name);
	
	abstract Request<List<ProfessionProxy>> findProfessionsByName(String name, int firstResult, int maxResults);
}
