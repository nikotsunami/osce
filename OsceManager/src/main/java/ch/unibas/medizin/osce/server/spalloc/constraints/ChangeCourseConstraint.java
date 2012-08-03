package ch.unibas.medizin.osce.server.spalloc.constraints;

import java.util.Set;

import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.server.spalloc.model.ValPatient;
import ch.unibas.medizin.osce.server.spalloc.model.VarAssignment;

import net.sf.cpsolver.ifs.model.GlobalConstraint;

/**
 * This constraint assures that a SimPat changes course if he/she does not stay
 * on the same post for multiple slots.
 * 
 * @author dk
 *
 */
public class ChangeCourseConstraint extends GlobalConstraint<VarAssignment, ValPatient> {

	@Override
	public void computeConflicts(ValPatient patient, Set<ValPatient> conflicts) {
		
		Assignment assignment = patient.variable().getOsceAssignment();
		
		for(VarAssignment va : assignedVariables()) {
			Assignment a = va.getOsceAssignment();
			
			if(assignment.equals(a) || (a.getSequenceNumber() + 1 != assignment.getSequenceNumber() &&
					a.getSequenceNumber() - 1 != assignment.getSequenceNumber()))
				continue;
			
			ValPatient p = va.getAssignment();
			if(assignment.getOsceDay().equals(a.getOsceDay()) &&
					assignment.getOscePostRoom().getCourse().equals(a.getOscePostRoom().getCourse()) &&
					!assignment.getOscePostRoom().getOscePost().equals(a.getOscePostRoom().getOscePost()) &&
					p.getPatient().equals(patient.getPatient())) {
				conflicts.add(p);
			}
		}
		
	}

}
