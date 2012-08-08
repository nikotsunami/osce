package ch.unibas.medizin.osce.server.spalloc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.domain.PatientInRole;
import ch.unibas.medizin.osce.domain.PatientInSemester;
import ch.unibas.medizin.osce.domain.StandardizedPatient;
import ch.unibas.medizin.osce.domain.StandardizedRole;
import net.sf.cpsolver.ifs.model.Model;
import net.sf.cpsolver.ifs.util.ToolBox;

/**
 * Model for optimizing the assignment of SimPats to Assignments. This class initiates all
 * variables (VarAssignment) and the possible corresponding values (ValPatient), which link
 * to Assignment (VarAssignment) and PatientInRole (ValPatient).<br />
 * <br />
 * Information on assignments with a mapping of StandardizedPatient to Assignment is hold in
 * order to be able to optimize the number of assignments for a single SimPat and assure not
 * violating any constraints.<br />
 * <br />
 * Methods to add/remove/read assignments for a SimPat are used by the value wrapper class
 * (ValPatient).
 * 
 * @see ch.unibas.medizin.osce.server.spalloc.model.VarAssignment
 * @see ch.unibas.medizin.osce.server.spalloc.model.ValPatient
 * 
 * @author dk
 *
 */
public class OsceModel extends Model<VarAssignment, ValPatient> {

	private static final int weightNrPatients = 4;
	private static final int weightSingleAssignments = 2;
	private static final int weightAllAssignments = 2;
	
	private Osce osce;
	private List<Assignment> assignments;
	private List<PatientInRole> patients;
	private Map<StandardizedPatient, Map<Integer, Assignment>> patientAssignments;
	private Set<StandardizedPatient> allPatients = new HashSet<StandardizedPatient>(); 
	
	// used for caching since Assignment.findAssignmentsByOscePostRoomAndType(...)
	// does not have to be called on each cp-solver iteration (slow!)
	private int nrStudentSlotsCoveredBySPSlot;
	
	public OsceModel(Osce osce) {
		this.osce = osce;
		
		nrStudentSlotsCoveredBySPSlot = osce.simpatAssignmentSlots();
		
		patientAssignments = new HashMap<StandardizedPatient, Map<Integer, Assignment>>();
		loadAssignments();
		loadPatients();
		generateModel();
	}
	
	/**
	 * Get OSCE object
	 * @return OSCE object
	 */
	public Osce getOsce() {
		return osce;
	}
	
	/**
	 * Get number of student-slots that are covered by one SimPat-slot
	 * @return number of student-slots covered by one SimPat-slot
	 */
	public int getNrStudentSlotsCoveredBySPSlot() {
		return nrStudentSlotsCoveredBySPSlot;
	}
	
	/**
	 * Load assignments for this OSCE
	 */
	public void loadAssignments() {
		this.assignments = Assignment.retrieveAssignmentsOfTypeSP(osce);
	}
	
	/**
	 * Load all SimPat that define the initial domain of the variables (VarAssignment)
	 */
	public void loadPatients() {
		List<PatientInRole> patients = new ArrayList<PatientInRole>();
		
		List<PatientInSemester> patientsInSemester = PatientInSemester.findPatientInSemestersBySemester(osce.getSemester()).getResultList();
		
		// get all roles used in this osce
		Set<StandardizedRole> roles = osce.usedRoles();
		
		// TODO: reconsider this fragment below - why is there no link from PatientInRole to StandardizedRole anymore?
		Iterator<PatientInRole> it = PatientInRole.findAllPatientInRoles().iterator();
		while (it.hasNext()) {
			PatientInRole patientInRole = (PatientInRole) it.next();
			
			if(patientsInSemester.contains(patientInRole.getPatientInSemester()) && roles.contains(patientInRole.getOscePost().getStandardizedRole())) {
				patients.add(patientInRole);
				
				allPatients.add(patientInRole.getPatientInSemester().getStandardizedPatient());
			}
		}
		
		this.patients = patients;
	}
	
	/**
	 * Set assignment for a patient (StandardizedPatient)
	 * @param p SimPat
	 * @param a assignment
	 */
	public void setPatientAssignment(StandardizedPatient p, Assignment a) {
		Map<Integer, Assignment> assignmentMap;
		
		if(patientAssignments.containsKey(p)) {
			assignmentMap = patientAssignments.get(p);
		} else {
			assignmentMap = new HashMap<Integer, Assignment>();
		}
		
		assignmentMap.put(a.getSequenceNumber(), a);
		patientAssignments.put(p, assignmentMap);
	}
	
	
	/**
	 * Remove assignment from a patient (StandardizedPatient)
	 * @param p SimPat
	 * @param a assignment
	 */
	public void removePatientAssignment(StandardizedPatient p, Assignment a) {
		if(patientAssignments.containsKey(p)) {
			patientAssignments.get(p).remove(a.getSequenceNumber());
			
			if(patientAssignments.get(p).size() == 0) {
				patientAssignments.remove(p);
			}
		}
	}
	
	/**
	 * Get assignment for patient (StandardizedPatient) in specific slot
	 * @param p SimPat
	 * @param sequenceNumber number of the slot to get
	 * @return assignment for a SimPat during the slot
	 */
	public Assignment getPatientAssignment(StandardizedPatient p, Integer sequenceNumber) {
		if(patientAssignments.containsKey(p)) {
			Map<Integer, Assignment> assignmentMap = patientAssignments.get(p);
			
			if(assignmentMap.containsKey(sequenceNumber)) {
				return assignmentMap.get(sequenceNumber);
			}
		}
		
		return null;
	}
	
	/**
	 * Get all assignment of a patient (StandardizedPatient)
	 * @param p SimPat
	 * @return map (sequenceNumber, assignment) of assignments for a patient
	 */
	public Map<Integer, Assignment> getAssignmentsByPatient(StandardizedPatient p) {
		return patientAssignments.get(p);
	}
	
	/**
	 * Check whether a patient (StandardizedPatient) has assignments
	 * @param p SimPat
	 * @return true/false
	 */
	public boolean hasAssignments(StandardizedPatient p) {
		return patientAssignments.containsKey(p);
	}
	
	/**
	 * Get total count of slots (highest slot number in all assignments)
	 * @return number of slots
	 */
	public int getNumberSlots() {
		int numberSlotsMax = 0;
		
		for(Assignment a : assignments) {
			if(a.getSequenceNumber() > numberSlotsMax)
				numberSlotsMax = a.getSequenceNumber();
		}
		
		return numberSlotsMax;
	}
	
	/**
	 * Optimize with the following soft constraints:
	 * - number of SP employed -> more = better
	 * - number of SP with 1 assignment -> less = better
	 * - number of SP employed at any time -> less = better (this should
	 * 		actually not occur if OneBreakConstraint is active!)
	 */
	@Override
	public double getTotalValue() {
		
		int nrSingleAssignments = 0;
		int nrAllAssignments = 0;
		for(StandardizedPatient p : patientAssignments.keySet()) {
			if(patientAssignments.get(p).size() == 1) {
				nrSingleAssignments++;
			} else if(patientAssignments.get(p).size() == getNumberSlots()) {
				nrAllAssignments++;
			}
		}
		
		return weightNrPatients * (allPatients.size() - patientAssignments.size()) +
				weightSingleAssignments * nrSingleAssignments +
				weightAllAssignments * nrAllAssignments;
	}

	/**
	 * Generate the model. Iterate through all assignments and create variables
	 * (VarAssignment) with corresponding values (ValPatient).
	 * 
	 * @see ch.unibas.medizin.osce.server.spalloc.model.VarAssignment
	 * @see ch.unibas.medizin.osce.server.spalloc.model.ValPatient
	 */
	public void generateModel() {
		Iterator<Assignment> it = assignments.iterator();
		while(it.hasNext()) {
			Assignment thisAssignment = it.next();			
			VarAssignment assignment = new VarAssignment(thisAssignment);
			List<ValPatient> values = generateValues(assignment);
			assignment.setPrevAssignment(thisAssignment.retrieveAssignmentNeighbourOfTypeSP(-1));
			assignment.setNextAssignment(thisAssignment.retrieveAssignmentNeighbourOfTypeSP(+1));
			assignment.setValues(values);
			assignment.setInitialAssignment(ToolBox.random(values));
			addVariable(assignment);
		}
	}
	
	/**
	 * Generate domain (values, ValPatient) for a variable (VarAssignment).
	 * Initially, the domain consists of all SimPat available on this Semester)
	 * @param vAssignment variable
	 * @return list of values assigned to the variable
	 * @see ch.unibas.medizin.osce.server.spalloc.model.VarAssignment
	 * @see ch.unibas.medizin.osce.server.spalloc.model.ValPatient
	 */
	// TODO consider only assigning SimPats that fit role of Assignment (db might take
	// the load off CPSolver)
	public List<ValPatient> generateValues(VarAssignment vAssignment) {
		List<ValPatient> values = new ArrayList<ValPatient>();
		
		Iterator<PatientInRole> it = patients.iterator();
		while(it.hasNext()) {
			PatientInRole patient = it.next();
			values.add(new ValPatient(vAssignment, patient));
		}
		
		return values;
	}

	/* with current knowledge, this can also be done by the methods in OsceModelListener
	 * (without getting all assignments afresh when assigning a single variable) */
	@Override
	public void beforeUnassigned(long iteration, ValPatient patient) {
		super.beforeUnassigned(iteration, patient);
		
		VarAssignment a = patient.variable();
		removePatientAssignment(patient.getPatient(), a.getOsceAssignment());
		
//		patientAssignments.clear();
//		
//		for(VarAssignment va : assignedVariables()) {
//			Assignment a = va.getOsceAssignment();
//			
//			if(a != null) {
//				setPatientAssignment(va.getAssignment().getPatient(), a);
//			}
//		}
	}

	/* with current knowledge, this can also be done by the methods in OsceModelListener
	 * (without getting all assignments afresh when assigning a single variable) */
	@Override
	public void afterAssigned(long iteration, ValPatient patient) {
		super.afterAssigned(iteration, patient);
		
		VarAssignment a = patient.variable();
		setPatientAssignment(patient.getPatient(), a.getOsceAssignment());
		
//		patientAssignments.clear();
//		
//		for(VarAssignment va : assignedVariables()) {
//			Assignment a = va.getOsceAssignment();
//			
//			if(a != null) {
//				setPatientAssignment(va.getAssignment().getPatient(), a);
//			}
//		}
	}
}
