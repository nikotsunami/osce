package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.domain.Doctor;

import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(Doctor.class)
public interface DoctorRequestNonRoo extends RequestContext {
	
	abstract Request<Long> countDoctorsBySearch(String q);
	
	abstract Request<List<DoctorProxy>> findDoctorsBySearch(String q, int firstResult, int maxResults);
	
	// SPEC START =
	abstract Request<List<DoctorProxy>> findDoctorWithRoleTopic(Long standroleid);
	// SPEC END =
	
}
