package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.domain.StandardizedPatient;

import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(StandardizedPatient.class)
public interface StandardizedPatientRequestNonRoo extends RequestContext {
	
	abstract Request<Long> countPatientsBySearch(String q);
	
	abstract Request<List<StandardizedPatientProxy>> findPatientsBySearch(String q, int firstResult, int maxResults);
}
