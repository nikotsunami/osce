package ch.unibas.medizin.osce.shared.scaffold;

import ch.unibas.medizin.osce.domain.spportal.SpPerson;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(SpPerson.class)
public interface SpPortalPersonRequestNonRoo extends RequestContext {
	
		// This method is used to save standardized patient data in sp portal db.
		abstract Request<Void> insertStandardizedPatientDetailsInSPportal(Long id);
}
