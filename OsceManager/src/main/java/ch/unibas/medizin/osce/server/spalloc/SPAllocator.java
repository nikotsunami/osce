package ch.unibas.medizin.osce.server.spalloc;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.cpsolver.ifs.solution.Solution;
import net.sf.cpsolver.ifs.solver.Solver;
import net.sf.cpsolver.ifs.util.DataProperties;
import net.sf.cpsolver.ifs.util.ToolBox;

import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.domain.Osce;
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

/**
 * @author dk
 *
 */
public class SPAllocator {
	static Logger log = Logger.getLogger(SPAllocator.class);
	
	private Osce osce;
	
	private Solution<VarAssignment, ValPatient> solution;
	
	public SPAllocator(Osce osce) {
		this.osce = osce;
	}
	
	/**
	 * Run SimPat-Allocator on timetable of dummy OSCE.
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
        cfg.setProperty("Termination.StopWhenComplete", "true");
        cfg.setProperty("Termination.TimeOut", "5");
        cfg.setProperty("Comparator.Class", "net.sf.cpsolver.ifs.solution.GeneralSolutionComparator");
        cfg.setProperty("Value.Class", "net.sf.cpsolver.ifs.heuristics.GeneralValueSelection");
        cfg.setProperty("Value.WeightConflicts", "1");
        cfg.setProperty("Tabu", "10");
        cfg.setProperty("Variable.Class", "net.sf.cpsolver.ifs.heuristics.GeneralVariableSelection");
        cfg.setProperty("Extensions.Classes", "net.sf.cpsolver.ifs.extension.ConflictStatistics");
        
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
	
	public void printSolution() {
		// get detailed information on SimPat-Allocator's results
        int nrTwoConcBreaks = 0;
        int[] nrAssignments = new int[((OsceModel) solution.getModel()).getNumberSlots()];
        Set<StandardizedPatient> usedPatients = new HashSet<StandardizedPatient>();
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
        				System.out.print(", " + assignmentMap.get(index).getSequenceNumber());
        				System.out.println();
        			}
        			System.out.println("    min key: " + p.minSlotNumber());
        			System.out.println("    max key: " + p.maxSlotNumber());
        			System.out.println("    nr. assignments: " + p.getNumberAssignments());
        			System.out.println("    nr. concurrent breaks: " + p.hasTwoConcurrentBreaks());
        			System.out.println();

        			if(p != null && p.hasAssignments()) {
        				if(p.getNumberAssignments() > 1 && p.hasTwoConcurrentBreaks()) {
        					nrTwoConcBreaks++;
        				}
        				
        				nrAssignments[p.getNumberAssignments() - 1]++;
        			}

        			usedPatients.add(p.getPatient());
        		}
        	}
        }
        
        System.out.println();
        System.out.println("Best solution found after " + solution.getBestTime() + " seconds (" + solution.getBestIteration() + " iterations).");
        System.out.println("Number of assigned variables is " + solution.getModel().assignedVariables().size());
        System.out.println("Total value of the solution is " + solution.getModel().getTotalValue());
	}
}
