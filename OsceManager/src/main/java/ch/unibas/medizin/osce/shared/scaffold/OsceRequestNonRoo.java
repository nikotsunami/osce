package ch.unibas.medizin.osce.shared.scaffold;

import java.util.Date;
import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.domain.Osce;

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
	
	
	public abstract Request<Boolean> autoAssignStudent(Long osceId);
	
	// module 3 f {
		public abstract Request<Void> autoAssignPatientInsemester(Long semesterId);

		
		// module 3 f }
		
		// Module 5 changes {
		
		//public abstract Request<Void> deleteAllPatentInRoleForOsce(Long osceId);
		
		// Module 5 changes }
		// Module10 Create plans
		abstract Request<Long> findOsceIdByOsceName(String osceName);
		// E Module10 Create plans
		
		public abstract Request<Boolean> removeassignment(OsceProxy osceName);
		
		/*abstract Request<Boolean> removeassignment(OsceProxy osceName);*/
}
