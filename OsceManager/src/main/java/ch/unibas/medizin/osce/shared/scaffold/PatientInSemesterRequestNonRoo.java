package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;

import ch.unibas.medizin.osce.domain.PatientInSemester;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(PatientInSemester.class)
public interface PatientInSemesterRequestNonRoo extends RequestContext {

	abstract Request<Long> countPatientinSemesterByAdvancedCriteria(
			Long semesterId, List<AdvancedSearchCriteriaProxy> searchCriteria);

	abstract Request<List<PatientInSemesterProxy>> findPatientInSemesterByAdvancedCriteria(Long semesterId, List<AdvancedSearchCriteriaProxy> searchCriteria);

	abstract Request<List<PatientInSemesterProxy>> findPatientInSemesterByOsceDayAdvancedCriteria(Long semesterId,Long osceDayId,Boolean useOsceDay, List<AdvancedSearchCriteriaProxy> searchCriteria);

}
