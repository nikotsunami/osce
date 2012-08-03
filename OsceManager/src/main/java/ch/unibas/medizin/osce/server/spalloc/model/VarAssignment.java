package ch.unibas.medizin.osce.server.spalloc.model;

import java.util.List;

import ch.unibas.medizin.osce.domain.Assignment;
import net.sf.cpsolver.ifs.model.Variable;

/**
 * This wrapper class represents a variable (Assignment) which gets a value
 * (ValPatient) assigned. It contains links to an Assignment, the next and
 * the previous Assignment (used for constraints). Initially, the variable's
 * domain consists of all values (ValPatient), even the ones to be excluded by
 * hard constraints. Step-by-step, the domain is reduced to values fitting to
 * the variable without harming constraints.<br />
 * <br />
 * Information on assignments with a mapping of StandardizedPatient to Assignment is hold in
 * OsceModel.
 * 
 * @see ch.unibas.medizin.osce.server.spalloc.model.OsceModel
 * @see ch.unibas.medizin.osce.server.spalloc.model.ValPatient
 * 
 * @author dk
 *
 */
public class VarAssignment extends Variable<VarAssignment, ValPatient> {

	private Assignment assignment;
	private Assignment prevAssignment;
	private Assignment nextAssignment;
	
	public VarAssignment(Assignment assignment) {
		this.assignment = assignment;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * This method is only inherited to be able to call it in OsceModel
	 * (@see ch.unibas.medizin.osce.simpat.OsceModel)
	 * 
	 * @see net.sf.cpsolver.ifs.model.Variable#setValues(java.util.List)
	 */
	@Override
	protected void setValues(List<ValPatient> values) {
		super.setValues(values);
	}
	
	/**
	 * Set previous Assignment (basically Assignment which is placed on the
	 * same OsceDay and a slot number decreased by one.
	 * @param prevAssignment previous assignment
	 */
	public void setPrevAssignment(Assignment prevAssignment) {
		this.prevAssignment = prevAssignment;
	}
	
	/**
	 * Get previous Assignment
	 * @return link to previous Assignment
	 */
	public Assignment getPrevAssignment() {
		return prevAssignment;
	}
	
	/**
	 * Set next Assignment (basically Assignment which is placed on the
	 * same OsceDay and a slot number increased by one.
	 * @param nextAssignment next assignment
	 */
	public void setNextAssignment(Assignment nextAssignment) {
		this.nextAssignment = nextAssignment;
	}
	
	/**
	 * Get next Assignment
	 * @return link to next Assignment
	 */
	public Assignment getNextAssignment() {
		return nextAssignment;
	}

	/**
	 * Get this Assignment ("getAssignment()" not possible because that
	 * method already links to the currently assigned ValPatient)
	 * @return link to current Assignment
	 */
	public Assignment getOsceAssignment() {
		return assignment;
	}

	/**
	 * Compare this Assignment to another (currently only required by
	 * SimPatConstruction, which is only used for testing purposes)
	 */
	@Override
	public int compareTo(VarAssignment o) {
		VarAssignment a = o;
		
		int cmp = Double.compare(values().size(), a.values().size());
		return cmp;
	}
}
