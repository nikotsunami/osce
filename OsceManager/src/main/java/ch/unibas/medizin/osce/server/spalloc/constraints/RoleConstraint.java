package ch.unibas.medizin.osce.server.spalloc.constraints;

import java.util.Set;

import ch.unibas.medizin.osce.domain.StandardizedRole;
import ch.unibas.medizin.osce.server.spalloc.model.ValPatient;
import ch.unibas.medizin.osce.shared.RoleTypes;

/**
 * This constraint assures that only SimPats are assigned that are capable of
 * playing the requested role.
 *  
 * @author dk
 *
 */
public class RoleConstraint extends AssignmentConstraint {

	@Override
	public void computeConflicts(ValPatient patient, Set<ValPatient> conflicts) {
		
		// compare for correct role (only SimPats with role enacted at this post)
		StandardizedRole patientRole = patient.getPatientInRole().getOscePost().getStandardizedRole();
		StandardizedRole assignmentRole = patient.variable().getOsceAssignment().getOscePostRoom().getOscePost().getStandardizedRole();
		
		// check for correct role ("statist" is always valid role and therefore NO conflict. "material" is take
		// into account since posts with role-type "material" do not have SP assignments
		if(!patientRole.equals(assignmentRole) && !assignmentRole.getRoleType().equals(RoleTypes.Statist))
			conflicts.add(patient);
	}
}
