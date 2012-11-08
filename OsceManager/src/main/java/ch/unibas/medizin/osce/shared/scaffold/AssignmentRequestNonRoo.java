package ch.unibas.medizin.osce.shared.scaffold;

import java.util.Date;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.shared.AssignmentTypes;
import ch.unibas.medizin.osce.shared.TimeBell;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(Assignment.class)
public interface AssignmentRequestNonRoo extends RequestContext{

	public abstract Request<List<AssignmentProxy>> retrieveAssignmentsOfTypeStudent(Long osceId);

	public abstract Request<List<AssignmentProxy>> retrieveAssignments(Long osceDayId,Long osceSequenceId,Long courseId,Long oscePostId);

	public abstract Request<List<AssignmentProxy>> retrieveAssignmenstOfTypeStudent(Long osceDayId,Long osceSequenceId,Long courseId,Long oscePostId);

	public abstract Request<List<AssignmentProxy>> retrieveAssignmenstOfTypeSP(Long osceDayId,Long osceSequenceId,Long courseId,Long oscePostId);

	public abstract Request<List<AssignmentProxy>> retrieveAssignmenstOfTypeExaminer(Long osceDayId,Long osceSequenceId,Long courseId,Long oscePostId);

	public abstract Request<List<AssignmentProxy>> retrieveAssignmentOfLogicalBreakPost(Long osceDayId,Long osceSequenceId);
	//Testing task {
	
	//Testing task }

		// Module10 Create plans		
		public abstract Request<List<AssignmentProxy>> findAssignmentsBySPIdandSemesterId(long spId,long semId,long pirId);
		// E Module10 Create plans
		
		//Module 9
		abstract Request<List<StandardizedPatientProxy>> findAssignedSP(Long semesterId);
	    
	    abstract Request<List<DoctorProxy>> findAssignedExaminer(Long semesterId);
	    
		//Module 9

	// Module : 15
	public abstract Request<List<AssignmentProxy>> getAssignmentsBySemester(
			Long semesterId);

	public abstract Request<Integer> getCountAssignmentsBySemester(
			Long semesterId);

	public abstract Request<String> getQwtBellSchedule(// List<AssignmentProxy>
														// assignmentProxies,
			Long semesterId, Integer time, TimeBell isPlusTime);

	// Module : 15
	
	public abstract Request<List<AssignmentProxy>> findAssignedDoctorBySpecialisation(Long specialisationId, Long clinicId);
	
	public abstract Request<Void> updateAssignmentByDiff(Long osceDayId, int diff, Date endTimeSlot);
	
	public abstract Request<Date> minmumStartTime(Long osceDayId,Long osceSequenceId,Long courseId);
	
	//payment 
	public abstract Request<String> findAssignmentByPatinetInRole(Long semesterId);
	//payment

}
