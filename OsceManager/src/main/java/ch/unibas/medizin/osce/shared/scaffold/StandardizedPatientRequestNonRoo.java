package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.domain.StandardizedPatient;


import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

import com.google.gwt.view.client.Range;

@SuppressWarnings("deprecation")
@Service(StandardizedPatient.class)
public interface StandardizedPatientRequestNonRoo extends RequestContext {
	
	abstract Request<Long> countPatientsBySearch(String q);
	
	abstract Request<Long> countPatientsBySearchAndSort(String q, List<String> searchThrough, List<String> fields, List<Integer> comparisons, List<String> values);
	
	abstract Request<List<StandardizedPatientProxy>> findPatientsBySearch(String q, Integer firstResult, Integer maxResults);
	
	abstract Request<List<StandardizedPatientProxy>> findPatientsBySearchAndSort(String sortField, Boolean asc, String q, Integer firstResult, Integer maxResults, List<String> searchThrough, List<String> fields, List<Integer> comparisons, List<String> values);
}
