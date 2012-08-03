package ch.unibas.medizin.osce.server.spalloc.heuristics;

import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.server.spalloc.model.ValPatient;
import ch.unibas.medizin.osce.server.spalloc.model.VarAssignment;

import net.sf.cpsolver.ifs.heuristics.GeneralValueSelection;
import net.sf.cpsolver.ifs.heuristics.NeighbourSelection;
import net.sf.cpsolver.ifs.heuristics.StandardNeighbourSelection;
import net.sf.cpsolver.ifs.model.Neighbour;
import net.sf.cpsolver.ifs.solution.Solution;
import net.sf.cpsolver.ifs.solver.Solver;
import net.sf.cpsolver.ifs.termination.TerminationCondition;
import net.sf.cpsolver.ifs.util.Callback;
import net.sf.cpsolver.ifs.util.DataProperties;
import net.sf.cpsolver.ifs.util.Progress;

/**
 * Examination timetabling neighbour selection. <br>
 * <br>
 * Adapted from the implementation of the exam timetabling problem of the IFS.<br />
 */
public class SimPatNeighbourSelection implements NeighbourSelection<VarAssignment, ValPatient>,
        TerminationCondition<VarAssignment, ValPatient> {
    private static Logger sLog = Logger.getLogger(SimPatNeighbourSelection.class);
//    private ExamColoringConstruction iColor = null;
//    private ExamConstruction iCon = null;
    private SimPatConstruction iCon = null;
    private StandardNeighbourSelection<VarAssignment, ValPatient> iStd = null;
//    private ExamSimulatedAnnealing iSA = null;
    private SimPatHillClimbing iHC = null;
    private SimPatHillClimbing iFin = null;
    private SimPatGreatDeluge iGD = null;
    private int iPhase = 1;
    private boolean iUseGD = false;
    private Progress iProgress = null;
    private Callback iFinalPhaseFinished = null;
    private boolean iCanContinue = true;
    private TerminationCondition<VarAssignment, ValPatient> iTerm = null;

    /**
     * Constructor
     * 
     * @param properties
     *            problem properties
     */
    public SimPatNeighbourSelection(DataProperties properties) {
//        if (properties.getPropertyBoolean("Exam.ColoringConstruction", false))
//            iColor = new ExamColoringConstruction(properties);
        iCon = new SimPatConstruction(properties);
        try {
            iStd = new StandardNeighbourSelection<VarAssignment, ValPatient>(properties);
            iStd.setVariableSelection(new SimPatUnassignedVariableSelection(properties));
            iStd.setValueSelection(new GeneralValueSelection<VarAssignment, ValPatient>(properties));
        } catch (Exception e) {
            sLog.error("Unable to initialize standard selection, reason: " + e.getMessage(), e);
            iStd = null;
        }
//        iSA = new ExamSimulatedAnnealing(properties);
        iHC = new SimPatHillClimbing(properties, "Hill Climbing");
        iFin = new SimPatHillClimbing(properties, "Finalization");
        iGD = new SimPatGreatDeluge(properties);
        iUseGD = properties.getPropertyBoolean("GreatDeluge.Use", iUseGD);
    }

    /**
     * Initialization
     */
    public void init(Solver<VarAssignment, ValPatient> solver) {
//        if (iColor != null)
//            iColor.init(solver);
        iCon.init(solver);
        iStd.init(solver);
//        iSA.init(solver);
        iHC.init(solver);
        iFin.init(solver);
        iGD.init(solver);
        if (iTerm == null) {
            iTerm = solver.getTerminationCondition();
            solver.setTerminalCondition(this);
        }
        iCanContinue = true;
        iProgress = Progress.getInstance(solver.currentSolution().getModel());
    }

    /**
     * Neighbour selection. It consists of the following three phases:
     * <ul>
     * <li>Construction phase (until all exams are
     * assigned)
     * <li>Hill-climbing phase (until the given number
     * if idle iterations)
     * <li>Simulated annealing phase (until
     * timeout is reached)
     * </ul>
     */
    @SuppressWarnings("fallthrough")
    public synchronized Neighbour<VarAssignment, ValPatient> selectNeighbour(Solution<VarAssignment, ValPatient> solution) {
//    	OsceModel model = (OsceModel) solution.getModel();
//    	
//    	for(VarAssignment va : model.variables()) {
//    		ValPatient p = va.getAssignment();
//    		if(model.getAssignmentsByPatient(p.getPatient()).size() > 0) {
//    			return new SimpleNeighbour<VarAssignment, ValPatient>(va, p);
//    		}
//    	}
//    	
//    	return iStd.selectNeighbour(solution);
        Neighbour<VarAssignment, ValPatient> n = null;
        if (!isFinalPhase() && !iTerm.canContinue(solution))
            setFinalPhase(null);
        
//        return iGD.selectNeighbour(solution);
        switch (iPhase) {
            case -1:
                iPhase++;
                sLog.info("***** construction phase *****");
//                if (iColor != null) {
//                    n = iColor.selectNeighbour(solution);
//                    if (n != null) return n;
//                }
            case 0:
                n = iCon.selectNeighbour(solution);
                if (n != null)
                    return n;
                if (solution.getModel().nrUnassignedVariables() > 0)
                    iProgress.setPhase("Searching for initial solution...", solution.getModel().variables().size());
                iPhase++;
                sLog.info("***** cbs/tabu-search phase *****");
            case 1:
                if (iStd != null && solution.getModel().nrUnassignedVariables() > 0) {
                    iProgress.setProgress(solution.getModel().variables().size() - solution.getModel().getBestUnassignedVariables());
                    n = iStd.selectNeighbour(solution);
                    if (n != null)
                        return n;
                }
                iPhase++;
                sLog.info("***** hill climbing phase *****");
            case 2:
                n = iHC.selectNeighbour(solution);
                if (n != null)
                    return n;
                iPhase++;
                sLog.info("***** " + (iUseGD ? "great deluge" : "simulated annealing") + " phase *****");
            case 3:
                if (iUseGD)
                    return iGD.selectNeighbour(solution);
//                else
//                    return iSA.selectNeighbour(solution);
            case 9999:
                n = iFin.selectNeighbour(solution);
                if (n != null)
                    return n;
                iPhase = -1;
                if (iFinalPhaseFinished != null)
                    iFinalPhaseFinished.execute();
                iCanContinue = false;
            default:
                return null;
        }
    }

    /**
     * Set final phase
     * 
     * @param finalPhaseFinished
     *            to be called when the final phase is finished
     **/
    public synchronized void setFinalPhase(Callback finalPhaseFinished) {
        sLog.info("***** final phase *****");
        iFinalPhaseFinished = finalPhaseFinished;
        iPhase = 9999;
    }

    /** Is final phase */
    public boolean isFinalPhase() {
        return iPhase == 9999;
    }

    /** Termination condition (i.e., has final phase finished) */
    public boolean canContinue(Solution<VarAssignment, ValPatient> currentSolution) {
        return iCanContinue;
    }

}
