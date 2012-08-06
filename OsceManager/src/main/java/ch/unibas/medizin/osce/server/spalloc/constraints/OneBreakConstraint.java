package ch.unibas.medizin.osce.server.spalloc.constraints;

import java.util.Set;

import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.server.spalloc.model.ValPatient;
import ch.unibas.medizin.osce.server.spalloc.model.VarAssignment;

import net.sf.cpsolver.ifs.model.GlobalConstraint;

/**
 * This constraint assures that a SimPat's break is not longer than one slot.
 * 
 * @author dk
 *
 */
public class OneBreakConstraint extends GlobalConstraint<VarAssignment, ValPatient> {

	@Override
	public void computeConflicts(ValPatient patient, Set<ValPatient> conflicts) {
		VarAssignment varAssignment = patient.variable();
		Assignment assignment = patient.variable().getOsceAssignment();
		
		for(VarAssignment va : assignedVariables()) {
			Assignment a = va.getOsceAssignment();
			
			// skip check of assignment with itself and assignments that are further away than +/- 1
			if(assignment.equals(a) ||
					(varAssignment.getNextAssignment() != assignment &&
					varAssignment.getPrevAssignment() != assignment))
				continue;
			
			ValPatient p = va.getAssignment();
			if(p.hasAssignments() && assignment.getOscePostRoom().equals(null) && a.getOscePostRoom().equals(null)) {
				conflicts.add(p);
			}
		}
	}
}
