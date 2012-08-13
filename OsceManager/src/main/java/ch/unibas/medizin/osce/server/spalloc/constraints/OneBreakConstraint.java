package ch.unibas.medizin.osce.server.spalloc.constraints;

import java.util.Set;

import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.server.spalloc.model.ValPatient;
import ch.unibas.medizin.osce.server.spalloc.model.VarAssignment;

/**
 * This constraint assures that a SP is in break at least once
 * 
 * @author dk
 *
 */
public class OneBreakConstraint extends AssignmentConstraint {

	@Override
	public void computeConflicts(ValPatient patient, Set<ValPatient> conflicts) {
		VarAssignment varAssignment = patient.variable();
		Assignment assignment = patient.variable().getOsceAssignment();
		
		for(VarAssignment va : assignedVariables()) {
			Assignment a = va.getOsceAssignment();
			ValPatient p = va.getAssignment();
			
			// skip check of assignment with itself and assignments that are further away than +/- 1
			if(assignment.equals(a) || !isNeighborAssignment(varAssignment, a) || p.getPatientInRole().getStayInPost().equals(true))
				continue;
			
			if(p.hasAssignments() && p.getNumberBreaks() < 1) {
				conflicts.add(p);
			}
		}
	}
}
