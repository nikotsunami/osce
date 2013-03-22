package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.domain.OsceDay;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(OsceDay.class)
public interface OsceDayRequestNooRoo extends RequestContext {

	public abstract Request<List<StandardizedRoleProxy>> findRoleForSPInSemester (Long patientInSemesterId,Long osceDayId);
	public abstract Request<Boolean> findRoleAssignedInOsceDay(Long standardizedRoleId, Long OsceDayId);
	
	abstract Request<java.util.List<OsceDayProxy>> findOsceDayByDoctorAssignment(DoctorProxy proxy);
	
	public abstract Request<Boolean> updateLunchBreak(Long osceDayId, Integer afterRotation, Integer flag);
	public abstract Request<Boolean> updateTimesAfterRotationShift(Long osceDayIdFrom, Long osceDayIdTo);
	
	//Module 5 Bug Report Solution
		public abstract Request<String> schedulePostpone(OsceDayProxy osceDayProxy);
		public abstract Request<String> scheduleEarlier(OsceDayProxy osceDayProxy);
		public abstract Request<Long> findOsceIdByOsceDayId(Long osceDayId);
		//E Module 5 Bug Report Solution
		
	public abstract Request<Boolean> updateRotation(Long osceDayId, Integer rotation);
	public abstract Request<java.util.List<OsceDayProxy>> findOSceDaysForAnOsceId(Long osceId);
	
	public abstract Request<java.util.List<OsceDayProxy>> findOsceDayBySemester(Long semesterId);
}
