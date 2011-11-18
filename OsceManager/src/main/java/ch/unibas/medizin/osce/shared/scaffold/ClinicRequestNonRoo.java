package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
import ch.unibas.medizin.osce.domain.Clinic;

import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@Service(Clinic.class)
public interface ClinicRequestNonRoo extends RequestContext {
	
	abstract Request<Long> countClinicsBySearch(String q);
	
	abstract Request<List<ClinicProxy>> findClinicsBySearch(String q, int firstResult, int maxResults);
}
