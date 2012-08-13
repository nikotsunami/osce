package ch.unibas.medizin.osce.server.spalloc.constraints;

import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.server.spalloc.model.ValPatient;
import ch.unibas.medizin.osce.server.spalloc.model.VarAssignment;
import net.sf.cpsolver.ifs.model.GlobalConstraint;

/**
 * Base Constraint which all SPAllocator constraints should extend. Provides functionality
 * for comparing assignments
 * 
 * @author dk
 *
 */
public abstract class AssignmentConstraint extends GlobalConstraint<VarAssignment, ValPatient> {
	
	/**
	 * Check whether two assignments are neighbors (with respect to their time)
	 * @param a1
	 * @param a2
	 * @return
	 */
	protected boolean isNeighborAssignment(VarAssignment a1, Assignment a2) {
		return (a1.getNextAssignment().equals(a2) || a1.getPrevAssignment().equals(a2));
	}
}
