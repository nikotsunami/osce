package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.domain.PatientInSemester;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(PatientInSemester.class)
public interface PatientInSemesterRequestNonRoo extends RequestContext {

	abstract Request<Long> countPatientinSemesterByAdvancedCriteria(Long semesterId, List<AdvancedSearchCriteriaProxy> searchCriteria);

	abstract Request<List<PatientInSemesterProxy>> findPatientInSemesterByAdvancedCriteria(Long semesterId, List<AdvancedSearchCriteriaProxy> searchCriteria);

	abstract Request<List<PatientInSemesterProxy>> findPatientInSemesterByOsceDayAdvancedCriteria(Long semesterId,Long osceDayId,Boolean useOsceDay, List<AdvancedSearchCriteriaProxy> searchCriteria,boolean ignoreAcceptedOsceDay);

	abstract Request<List<PatientInSemesterProxy>> findPatientInSemesterBySemester(Long semesterId,boolean ignoreAcceptedOsceDays,String searchWord);

	abstract Request<List<StandardizedPatientProxy>> findAvailableSPBySemester(Long semesterId);

	abstract Request<Boolean> findAvailableSPActiveBySemester(Long semesterId);
	
	abstract Request<Boolean> checkAndSetFitCriteriaOfRole(Long post,Long semesterId, List<AdvancedSearchCriteriaProxy> searchCriteria);
	
	abstract Request<PatientInSemesterProxy> findPisBySemesterSp(Long semesterId,Long standardizedPatientId);

	abstract Request<Boolean> updatePatientInSemesterForOsceDay(Long patientInSemsterId, List<Long> osceDayIdList);

	abstract  Request<Boolean>  assignSPToSemester(Long semId);
}
