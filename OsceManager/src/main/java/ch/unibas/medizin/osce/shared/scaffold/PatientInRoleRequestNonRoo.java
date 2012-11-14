package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;
import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.domain.PatientInRole;

import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(PatientInRole.class)
public interface PatientInRoleRequestNonRoo extends RequestContext {

	public abstract Request<Integer> removePatientInRoleByOSCE(Long osceId);
	public abstract Request<List<PatientInRoleProxy>> findPatientsInRoleForAssignmentBySPIdandSemesterId(long spId,long semId);
	public abstract Request<List<Long>> findPatientInRoleByOsceDayfromAssignment(long osceDayId);
	// change {
	public abstract Request<Integer> getTotalTimePatientAssignInRole(Long osceDayId,Long patientInsemesterId);
	public abstract Request<Boolean> deletePatientInRole(PatientInRoleProxy patientInRole);
	 public abstract Request<String> savePatientInRole(Long osceDayId,Long oscePostId,Long patientInsemesterId,Long standardizedRoleId);
	// change }
	 
	 abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.PatientInRoleProxy, ch.unibas.medizin.osce.client.managed.request.PatientInRoleProxy> save();
	 
	//spec change[
	 public abstract Request<List<PatientInRoleProxy>> findPatientInRoleByRotation(AssignmentProxy ass);
	//spec change]
}
