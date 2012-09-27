package ch.unibas.medizin.osce.server.spalloc.constraints;

import java.util.Set;

import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.server.spalloc.model.OsceModel;
import ch.unibas.medizin.osce.server.spalloc.model.ValPatient;
import ch.unibas.medizin.osce.server.spalloc.model.VarAssignment;

/**
 * This constraint assures that a SimPat changes parcour if the SP change
 * occurs during a rotation
 * 
 * @author dk
 *
 */
public class ChangeParcourConstraint extends AssignmentConstraint {

	@Override
	public void computeConflicts(ValPatient patient, Set<ValPatient> conflicts) {
		VarAssignment varAssignment = patient.variable();
		Assignment assignment = varAssignment.getOsceAssignment();
		OsceModel model = (OsceModel) varAssignment.getModel();
		Boolean spStayInPost = assignment.getOsceDay().getOsce().getSpStayInPost();
		
		for(VarAssignment va : assignedVariables()) {
			Assignment a = va.getOsceAssignment();
			ValPatient p = va.getAssignment();
			
			// skip check of assignment with itself and assignments that are further away than +/- 1
			if(assignment.equals(a) || !isNeighborAssignment(varAssignment, a) || spStayInPost.equals(true))
				continue;
			
			// calculate time difference between two assignments (later compared with length of break) 
			int diffInMinutes;
			if(a.getSequenceNumber() - 1 == assignment.getSequenceNumber()) {
				diffInMinutes = (int) ((assignment.getTimeEnd().getTime() - a.getTimeStart().getTime()) / (1000 * 60));
			} else {
				diffInMinutes = (int) ((a.getTimeEnd().getTime() - assignment.getTimeStart().getTime()) / (1000 * 60));
			}
			
			// if time between two assignments is larger than shortBreakSimpatChange, the change occurs during a rotation
			boolean isDuringRotation = diffInMinutes > model.getOsce().getShortBreakSimpatChange();
			
			if(p.getPatient().equals(patient.getPatient()) &&
					isDuringRotation &&
					!assignment.getOscePostRoom().getOscePost().equals(a.getOscePostRoom().getOscePost()) &&
					assignment.getOsceDay().equals(a.getOsceDay()) &&
					assignment.getOscePostRoom().getCourse().equals(a.getOscePostRoom().getCourse())) {
				conflicts.add(p);
			}
		}
	}
}
