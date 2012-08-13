package ch.unibas.medizin.osce.server.spalloc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import net.sf.cpsolver.ifs.solution.Solution;
import net.sf.cpsolver.ifs.solver.Solver;
import net.sf.cpsolver.ifs.util.DataProperties;
import net.sf.cpsolver.ifs.util.ToolBox;

import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.domain.PatientInRole;
import ch.unibas.medizin.osce.domain.PatientInSemester;
import ch.unibas.medizin.osce.domain.StandardizedPatient;
import ch.unibas.medizin.osce.server.spalloc.constraints.ChangeParcourConstraint;
import ch.unibas.medizin.osce.server.spalloc.constraints.ChangeRoleConstraint;
import ch.unibas.medizin.osce.server.spalloc.constraints.OneBreakConstraint;
import ch.unibas.medizin.osce.server.spalloc.constraints.RoleConstraint;
import ch.unibas.medizin.osce.server.spalloc.constraints.RoomConstraint;
import ch.unibas.medizin.osce.server.spalloc.constraints.StayInPostConstraint;
import ch.unibas.medizin.osce.server.spalloc.model.OsceModel;
import ch.unibas.medizin.osce.server.spalloc.model.ValPatient;
import ch.unibas.medizin.osce.server.spalloc.model.VarAssignment;
import ch.unibas.medizin.osce.shared.AssignmentTypes;

/**
 * This component allows for allocating SPs to a previous generated timetable (set up by
 * TimetableGenerator). The optimal allocation is found by employing IFS with predefined
 * hard and soft constraints (see package ch.unibas.medizin.osce.server.spalloc.constraints.*).
 * 
 * @author dk
 *
 */
public class SPAllocator {
	static Logger log = Logger.getLogger(SPAllocator.class);
	
	private Osce osce;
	
	private Solution<VarAssignment, ValPatient> solution;

	private List<Assignment> refAssignments;
	
	/**
	 * Initialize SPAllocator with OSCE
	 * @param osce
	 */
	public SPAllocator(Osce osce) {
		this.osce = osce;
		refAssignments = Assignment.retrieveAssignmentsOfTypeSPUniqueTimes(osce);
	}
	
	/**
	 * Run SPAllocator on timetable of OSCE.
	 * @param osce
	 * @return solution given by the IFS
	 */
	public Solution<VarAssignment, ValPatient> getSolution() {
		org.apache.log4j.BasicConfigurator.configure();
		
		// setup model (create variables and values)
		OsceModel model = new OsceModel(osce);
		
		// add constraints to model
		model.addGlobalConstraint(new RoleConstraint());
		model.addGlobalConstraint(new RoomConstraint());
		model.addGlobalConstraint(new ChangeParcourConstraint());
		model.addGlobalConstraint(new ChangeRoleConstraint());
		model.addGlobalConstraint(new OneBreakConstraint());
		model.addGlobalConstraint(new StayInPostConstraint());
		
		// set properties for solving the problem
		DataProperties cfg = new DataProperties();
        cfg.setProperty("Termination.Class", "net.sf.cpsolver.ifs.termination.GeneralTerminationCondition");
        cfg.setProperty("Termination.StopWhenComplete", "false");
        cfg.setProperty("Termination.TimeOut", "10");
        cfg.setProperty("Comparator.Class", "net.sf.cpsolver.ifs.solution.GeneralSolutionComparator");
        cfg.setProperty("Value.Class", "net.sf.cpsolver.ifs.heuristics.GeneralValueSelection");
        cfg.setProperty("Value.WeightConflicts", "1");
        cfg.setProperty("Tabu", "10");
        cfg.setProperty("Variable.Class", "net.sf.cpsolver.ifs.heuristics.GeneralVariableSelection");
        cfg.setProperty("Extensions.Classes", "net.sf.cpsolver.ifs.extension.ConflictStatistics");
        
        // initialize solver with given properties and set an initial solution
		Solver<VarAssignment, ValPatient> solver = new Solver<VarAssignment, ValPatient>(cfg);
		solver.setInitalSolution(model);
		
		solver.start();
		try {
            solver.getSolverThread().join();
        } catch (InterruptedException ex) {
        }
		
		solution = solver.lastSolution();
        solution.restoreBest();

        log.info("Best solution:" + ToolBox.dict2string(solution.getExtendedInfo(), 1));
        log.info("Best solution found after " + solution.getBestTime() + " seconds (" + solution.getBestIteration() + " iterations).");
        log.info("Number of assigned variables is " + solution.getModel().assignedVariables().size());
        log.info("Total value of the solution is " + solution.getModel().getTotalValue());
        
        return solution;
	}
	
	/**
	 * Print solution of the CPSolver - for debugging only
	 */
	public void printSolution() {
        for (VarAssignment va : ((OsceModel) solution.getModel()).variables()) {
        	Assignment a = va.getOsceAssignment();

        	if (va.getOsceAssignment() != null) {
        		ValPatient p = va.getAssignment();
        		
            	System.out.print("Assignment (");
            	System.out.print(a.getOscePostRoom().getCourse().getColor() + ", ");
            	System.out.print(a.getOscePostRoom().getOscePost().getStandardizedRole().getShortName());
            	System.out.print(", " + a.getSequenceNumber() + ") = ");
            	
            	if(p != null) 
            		System.out.print(p.getPatientInRole().getPatientInSemester().getStandardizedPatient().getName());
            	
            	System.out.println();
            } else
            	System.out.println("Assignment " + va.getName() + " is empty");
        }
        
		// print assignments per SimPat
        Set<StandardizedPatient> usedPatients = new HashSet<StandardizedPatient>();
        for(VarAssignment va : ((OsceModel) solution.getModel()).assignedVariables()) {
        	if (va.getOsceAssignment() != null) {
        		ValPatient p = va.getAssignment();
        		
        		if(!usedPatients.contains(p.getPatient())) {
        			Map<Integer, Assignment> assignmentMap = p.getAssignments();
        			Iterator<Integer> it = assignmentMap.keySet().iterator();
        			System.out.println(p.getPatient().getName() + ", " + p.getPatient().getPreName());
        			while (it.hasNext()) {
        				Integer index = (Integer) it.next();
        				System.out.print("    ");
        				System.out.print(assignmentMap.get(index).getOscePostRoom().getCourse().getColor() + ", ");
        				System.out.print(assignmentMap.get(index).getOscePostRoom().getOscePost().getStandardizedRole().getShortName());
        				System.out.print(", " + debugTime(assignmentMap.get(index).getTimeStart()) + " - " + debugTime(assignmentMap.get(index).getTimeEnd()));
        				System.out.println();
        			}
        			System.out.println("    nr. assignments: " + p.getNumberAssignments());
        			System.out.println();

        			usedPatients.add(p.getPatientInRole().getPatientInSemester().getStandardizedPatient());
        		}
        	}
        }
        
        System.out.println();
        System.out.println("Best solution found after " + solution.getBestTime() + " seconds (" + solution.getBestIteration() + " iterations).");
        System.out.println("Number of assigned variables is " + solution.getModel().assignedVariables().size());
        System.out.println("Total value of the solution is " + solution.getModel().getTotalValue());
	}
	
	/**
	 * Update assignment records with allocated SPs and add break for SPs when they
	 * are not allocated to any assignment for a given time-slot.
	 */
	public void saveSolution() {
		Set<PatientInSemester> usedPatients = new HashSet<PatientInSemester>();
		
		// link Assignments to PatientInRole
		for(VarAssignment va : ((OsceModel) solution.getModel()).assignedVariables()) {
			Assignment a = va.getOsceAssignment();
			
        	if (a != null) {
        		ValPatient p = va.getAssignment();
        		a.setPatientInRole(p.getPatientInRole());
        		a.flush();
        		
        		if(!usedPatients.contains(p.getPatientInRole().getPatientInSemester()))
        			usedPatients.add(p.getPatientInRole().getPatientInSemester());
        	}
        }
		
		// save SP in break assignment if assignments exists but not for all SP slots
		Assignment.clearSPBreakAssignments(osce);
		OsceModel model = ((OsceModel) solution.getModel());
		Iterator<PatientInSemester> it = usedPatients.iterator();
		int numberSlots = model.getNumberSlots();
		
		while (it.hasNext()) {
			PatientInSemester sp = (PatientInSemester) it.next();
			
			for(int i = 1; i <= numberSlots; i++) {
				if(model.getPatientAssignment(sp.getStandardizedPatient(), i) == null) {
					createSPBreakAssignment(sp, i);
					log.info(sp.getStandardizedPatient().getName() + ", " + sp.getStandardizedPatient().getPreName() + " added break for patient " + sp.getId() + " and slot " + i);
				}
			}
		}
	}
	
	/**
	 * Generate break slot for SP (time-slots with same times as "normal"
	 * posts but with osce_post_room = null and patientInRole.isBreak = true).
	 * SP that are not allocated to any post at a given time are allocated to this
	 * assignment.
	 */
	private void createSPBreakAssignment(PatientInSemester p, int sequenceNumber) {
		Assignment assignment = refAssignments.get(sequenceNumber - 1);
		if(assignment != null) {
			try {
				// get "break role" for this SP
				PatientInRole breakRole = PatientInRole.findPatientInRolesByPatientInSemesterAndOscePostIsNull(p).getSingleResult();
				
				// create break assignment
				Assignment ass = new Assignment();
				ass.setType(AssignmentTypes.PATIENT);
				ass.setOscePostRoom(null);
				ass.setOsceDay(assignment.getOsceDay());
				ass.setTimeStart(assignment.getTimeStart());
				ass.setTimeEnd(assignment.getTimeEnd());
				ass.setSequenceNumber(sequenceNumber);
				ass.setPatientInRole(breakRole);
				ass.persist();
			} catch(NoResultException e) {
				log.error(e.getMessage());
			} catch(NonUniqueResultException e) {
				log.error(e.getMessage());
			}
		}
	}
	
	private String debugTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(date);
	}
}
