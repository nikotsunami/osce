package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.shared.AssignmentTypes;

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

	//Testing task {
		public abstract Request<List<AssignmentProxy>> findAssignmentForTestBasedOnCriteria(Long osceDayId,List<AssignmentTypes> type,Long oscePostRoomId);
		
		public abstract Request<Integer> findTotalStudentsBasedOnOsce(Long osceId);
		
		public abstract Request<List<AssignmentProxy>> findAssignmentBasedOnOsceDay(Long osceDayId);
		//Testing task }

}
