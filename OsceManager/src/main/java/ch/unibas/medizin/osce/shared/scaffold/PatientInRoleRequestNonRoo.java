package ch.unibas.medizin.osce.shared.scaffold;

import ch.unibas.medizin.osce.domain.PatientInRole;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(PatientInRole.class)
public interface PatientInRoleRequestNonRoo extends RequestContext {

	public abstract Request<Integer> removePatientInRoleByOSCE(Long osceId);	

}
