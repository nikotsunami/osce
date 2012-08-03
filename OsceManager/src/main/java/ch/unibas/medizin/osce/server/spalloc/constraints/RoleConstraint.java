package ch.unibas.medizin.osce.server.spalloc.constraints;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.domain.OsceDay;
import ch.unibas.medizin.osce.domain.RoleTopic;
import ch.unibas.medizin.osce.server.spalloc.model.OsceModel;
import ch.unibas.medizin.osce.server.spalloc.model.ValPatient;
import ch.unibas.medizin.osce.server.spalloc.model.VarAssignment;

import net.sf.cpsolver.ifs.model.GlobalConstraint;

/**
 * This constraint assures that only SimPats are assigned that are capable of
 * playing the requested role. Also only SimPats with correct role-types are assigned<br />
 * <br />
 * OSCE has 1 day:<br />
 *  - role A in the first half-day<br />
 *  - role B in the second half-day<br />
 *  <br />
 * OSCE has 2 days:<br />
 *  - role A on day 1<br />
 *  - role B on day 2<br />
 *  <br />
 * Repetition-OSCE:<br />
 *  - role C
 *  
 * @author dk
 *
 */
public class RoleConstraint extends GlobalConstraint<VarAssignment, ValPatient> {

	@Override
	public void computeConflicts(ValPatient patient, Set<ValPatient> conflicts) {
		
		// compare for correct role (only SimPats with role enacted at this post)
		RoleTopic patientRoleTopic = patient.getPatientInRole().getOscePost().getStandardizedRole().getRoleTopic();
		RoleTopic assignmentRoleTopic = patient.variable().getOsceAssignment().getOscePostRoom().getOscePost().getOscePostBlueprint().getRoleTopic();
		
		// compare for correct role-type (A, B or C)
		String patientRoleType = patient.getPatientInRole().getOscePost().getStandardizedRole().getRoleType().toString();
		String currentRoleType = currentRoleType(patient.variable());
		
		// check for correct role (or spear carrier role) and role-type (A, B or C)
		if(!((assignmentRoleTopic.getSlotsUntilChange() == 0 || patientRoleTopic.equals(assignmentRoleTopic)) &&
				patientRoleType.equals(currentRoleType))) {
			conflicts.add(patient);
		}
	}

	/**
	 * Get role type as it currently should be ("A", "B" or "C" are supported at the moment)
	 * @param va
	 * @return currently required role
	 */
	public String currentRoleType(VarAssignment va) {
		OsceModel model = (OsceModel) va.getModel();
		Assignment a = va.getOsceAssignment();
		OsceDay d = a.getOsceDay();
		Osce o = d.getOsce();
		
		if(o.getIsRepeOsce()) {
			return "C";
		} else {
			if(o.getOsce_days().size() == 1) {
				// half of the day already past?
				if(a.getSequenceNumber() > (model.getNumberSlots() / 2))
					return "B";
				else
					return "A";
			} else {
				// get other day (momentarily only two days are supported)
				List<OsceDay> oDays = o.getOsce_days();
				Iterator<OsceDay> it = oDays.iterator();
				OsceDay day1 = d;
				OsceDay day2 = null;
				while (it.hasNext()) {
					OsceDay osceDay = (OsceDay) it.next();
					if(!osceDay.equals(day1))
						day2 = osceDay;
				}
				
				// check for day we are currently in
				if(day1.getTimeStart().getTime() > day2.getTimeStart().getTime())
					return "B";
				else
					return "A";
			}
		}
	}
}
