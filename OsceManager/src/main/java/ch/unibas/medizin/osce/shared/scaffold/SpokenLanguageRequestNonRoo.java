package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;
import ch.unibas.medizin.osce.domain.SpokenLanguage;

import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@Service(SpokenLanguage.class)
public interface SpokenLanguageRequestNonRoo extends RequestContext {
	
	abstract Request<Long> countLanguagesByName(String name);
	
	abstract Request<List<SpokenLanguageProxy>> findLanguagesByName(String name, int firstResult, int maxResults);
}
