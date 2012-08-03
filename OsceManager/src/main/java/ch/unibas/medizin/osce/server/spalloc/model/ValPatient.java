package ch.unibas.medizin.osce.server.spalloc.model;

import java.util.Collections;
import java.util.Map;

import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.domain.PatientInRole;
import ch.unibas.medizin.osce.domain.StandardizedPatient;
import net.sf.cpsolver.ifs.model.Value;

/**
 * This wrapper class represents a value (SimPat) for the assignment to a variable
 * (VarAssignment). It contains a link to the according PatientInRole and
 * StandardizedPatient. This wrapper is needed to assign the same PatientInRole to
 * several variables (VarAssignment) since a value can only be associated to one
 * variable. Hence, several values (ValPatient) link to the same PatientInRole.<br />
 * <br />
 * Information on assignments with a mapping of StandardizedPatient to Assignment is hold in
 * OsceModel.
 * 
 * @see ch.unibas.medizin.osce.server.spalloc.model.OsceModel
 * @see ch.unibas.medizin.osce.server.spalloc.model.VarAssignment
 * 
 * @author dk
 *
 */
public class ValPatient extends Value<VarAssignment, ValPatient> {

	private PatientInRole patientInRole;
	private StandardizedPatient patient;
	
	public ValPatient(VarAssignment vAssignment, PatientInRole patientInRole) {
		super(vAssignment);
		
		this.patientInRole = patientInRole;
		this.patient = patientInRole.getPatientInSemester().getStandardizedPatient();
	}

	/**
	 * Get the PatientInRole of this value
	 * @return link to PatientInRole entity
	 */
	public PatientInRole getPatientInRole() {
		return patientInRole;
	}
	
	/**
	 * Get the StandardizedPatient of this value (mainly used to check for further
	 * assignments)
	 * @return link to StandardizedPatient entity
	 */
	public StandardizedPatient getPatient() {
		return patient;
	}
	
	/**
	 * Set an assignment for this patient (StandardizedPatient)
	 * @param a assignment
	 */
	public void setAssignment(Assignment a) {
		OsceModel model = (OsceModel) variable().getModel();
		model.setPatientAssignment(getPatient(), a);
	}
	
	/**
	 * Remove an assignment for this patient (StandardizedPatient)
	 * @param a assignment
	 */
	public void removeAssignment(Assignment a) {
		OsceModel model = (OsceModel) variable().getModel();
		model.removePatientAssignment(getPatient(), a);
	}
	
	/**
	 * Get assignment for this patient (StandardizedPatient) and custom slot number
	 * @param slotNumber slot number to get
	 * @return assignment for a SimPat during the slot
	 */
	public Assignment getAssignment(Integer slotNumber) {
		if(getAssignments().containsKey(slotNumber)) {
			return getAssignments().get(slotNumber);
		}
		
		return null;
	}
	
	/**
	 * Get all assignments of this patient (StandardizedPatient)
	 * @return map (slotnumber, assignment) of assignments for a patient
	 */
	public Map<Integer, Assignment> getAssignments() {
		OsceModel model = (OsceModel) variable().getModel();
		return model.getAssignmentsByPatient(getPatient());
	}
	
	/**
	 * Get number of assignments this patient (StandardizedPatient) has
	 * @return number of assignments this patient has
	 */
	public int getNumberAssignments() {
		return getAssignments().size();
	}
	
	/**
	 * Check whether this patient (StandardizedPatient) has assignments
	 * @return true/false
	 */
	public boolean hasAssignments() {
		OsceModel model = (OsceModel) variable().getModel();
		return model.hasAssignments(getPatient());
	}
	
	/**
	 * Get smallest slot number where this patient (StandardizedPatient)
	 * has an assignment
	 * @return slot number where this patient is assigned the first time
	 */
	public int minSlotNumber() {
		if(hasAssignments()) {
			return Collections.min(getAssignments().keySet());
		}
		return 0;
	}
	
	/**
	 * Get largest slot number where this patient (StandardizedPatient)
	 * has an assignment
	 * @return slot number where this patient is assigned the last time
	 */
	public int maxSlotNumber() {
		if(hasAssignments()) {
			return Collections.max(getAssignments().keySet());
		}
		return 0;
	}
	
	/**
	 * Get number of breaks this patient (StandardizedPatient) has
	 * (number of total slots - assignments of this patient)
	 * @return number of breaks this patient has
	 */
	public int getNumberBreaks() {
		OsceModel model = (OsceModel) variable().getModel();
		return (model.getNumberSlots() - getNumberAssignments());
	}
	
	/**
	 * Check whether this patient has two ore more concurrect breaks.
	 * @return true/false
	 */
	public boolean hasTwoConcurrentBreaks() {
		int numberBreaks = 0;
		
		for(int i = 1; i <= maxSlotNumber(); i++) {
			if(getAssignment(i) == null) {
				numberBreaks++;
				
				if(numberBreaks > 1)
					return true;
			} else {
				numberBreaks = 0;
			}
		}
		
		return false;
	}
}
