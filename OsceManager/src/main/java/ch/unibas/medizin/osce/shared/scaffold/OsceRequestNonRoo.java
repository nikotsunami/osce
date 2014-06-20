package ch.unibas.medizin.osce.shared.scaffold;

import java.util.Date;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.MapOsceRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.shared.OsceCreationType;
import ch.unibas.medizin.osce.shared.OsceStatus;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(Osce.class)
public interface OsceRequestNonRoo extends RequestContext{

	abstract Request<List<OsceProxy>> findAllOsceBySemster(Long id);
	
	abstract Request<OsceProxy> findMaxOsce();
	
	public abstract Request<List<OsceProxy>> findAllOsceOnSemesterId(Long id);
	
	public abstract Request<List<OsceProxy>> findAllOsceSemester(Long id , Date startDate, Date endDate);
	
	public abstract Request<Boolean> generateOsceScaffold(Long osceId);
	
	public abstract Request<Boolean> generateAssignments(Long osceId);
	
	public abstract Request<Integer> initOsceBySecurity();	
	
	public abstract Request<Boolean> autoAssignPatientInRole(Long osceId);
	
	public abstract Request<Boolean> autoAssignStudent(Long osceId,Integer orderType, boolean changeRequire);
	
	public abstract Request<Void> autoAssignPatientInsemester(Long semesterId);

	abstract Request<Long> findOsceIdByOsceName(String osceName);

	public abstract Request<Boolean> removeassignment(OsceProxy osceName);
	
	public abstract Request<List<MapOsceRoleProxy>> findAllOsceSemesterByRole(List<Long>  StandardizedRoleId , Date startDate, Date endDate);

	public abstract Request<Void> createOsceDaySequeceAndCourse(Long osceId);
	
	public abstract Request<List<OsceProxy>> findAllOsceByStatusAndSemesterId(Long semesterId);
	
	public abstract Request<List<OsceProxy>> findAllOsceBySemesterIdAndCreationType(Long semesterId, OsceCreationType osceCreationType);
	
	public abstract Request<Void> calculateManualOsce(Long osceId);
	
	public abstract Request<String> createAssignmentInManualOsce(Long osceId);
	
	public abstract Request<OsceProxy> clearAllManualOsce(Long osceId);
	
	public abstract Request<OsceProxy> changeOsceStatus(Long osceId, OsceStatus osceStatus);
}
