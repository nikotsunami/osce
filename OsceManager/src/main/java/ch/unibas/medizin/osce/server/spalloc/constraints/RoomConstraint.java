package ch.unibas.medizin.osce.server.spalloc.constraints;

import java.util.Set;

import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.server.spalloc.model.ValPatient;
import ch.unibas.medizin.osce.server.spalloc.model.VarAssignment;

import net.sf.cpsolver.ifs.model.GlobalConstraint;

/**
 * This constraint assures that a SimPat is not assigned to multiple rooms at the same
 * time and a room does not contain more than one SimPat at a time.
 * 
 * @author dk
 *
 */
public class RoomConstraint extends GlobalConstraint<VarAssignment, ValPatient> {

	@Override
	public void computeConflicts(ValPatient patient, Set<ValPatient> conflicts) {
		for (VarAssignment va : assignedVariables()) {
			Assignment a = va.getOsceAssignment();
			
			// do not compare the current assignment with itself!
			if(a.equals(patient.variable().getOsceAssignment()))
				continue;
            
            ValPatient p = va.getAssignment();
            if(!isConsistent(p, patient)) {
            	conflicts.add(p);
            }
        }
	}

	@Override
	public boolean isConsistent(ValPatient p1, ValPatient p2) {
		if (p1 == null || p2 == null)
            return true;
		
		Assignment a1 = p1.variable().getOsceAssignment();
		Assignment a2 = p2.variable().getOsceAssignment();
		
		// check if time ranges overlap or start at the same time (then they would also end at the same time)
		if(p1.getPatient().equals(p2.getPatient()) &&
				!a1.getOscePostRoom().equals(a2.getOscePostRoom()) &&
				(a1.getTimeStart().before(a2.getTimeEnd()) && a2.getTimeStart().before(a1.getTimeEnd()) || a1.getTimeStart().equals(a2.getTimeStart()))) {
			return false;
		}

		return true;
	}
}
