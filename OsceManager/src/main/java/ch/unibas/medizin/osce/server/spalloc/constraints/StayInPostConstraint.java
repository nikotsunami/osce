package ch.unibas.medizin.osce.server.spalloc.constraints;

import java.util.Set;

import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.server.spalloc.model.ValPatient;
import ch.unibas.medizin.osce.server.spalloc.model.VarAssignment;

/**
 * This constraint assures that a patient who is meant to stay in the same post
 * during the whole day does not change the post
 * 
 * @author dk
 *
 */
public class StayInPostConstraint extends AssignmentConstraint {

	@Override
	public void computeConflicts(ValPatient patient, Set<ValPatient> conflicts) {
		Assignment assignment = patient.variable().getOsceAssignment();
		Boolean spStayInPost = assignment.getOsceDay().getOsce().getSpStayInPost();
		
		for(VarAssignment va : assignedVariables()) {
			Assignment a = va.getOsceAssignment();
			ValPatient p = va.getAssignment();
			
			// skip check of assignment with itself patients that should not stay in post
			if(assignment.equals(a) || spStayInPost.equals(false))
				continue;
			
			if(!p.getPatient().equals(patient.getPatient()) &&
					spStayInPost.equals(true) &&
					assignment.getOscePostRoom().equals(a.getOscePostRoom()))
				conflicts.add(p);
		}
	}
}
