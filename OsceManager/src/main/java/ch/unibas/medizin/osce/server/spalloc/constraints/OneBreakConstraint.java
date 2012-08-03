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
		
		for(VarAssignment va : assignedVariables()) {
			Assignment a = va.getOsceAssignment();
			
			if(a.equals(patient.variable().getOsceAssignment())/* || (a.getSlotNumber() + 1 != assignment.getSlotNumber() &&
					a.getSlotNumber() - 1 != assignment.getSlotNumber())*/)
				continue;
			
			ValPatient p = va.getAssignment();
			if(p.hasAssignments() && p.hasTwoConcurrentBreaks()) {
				conflicts.add(p);
			}
		}
	}
}
