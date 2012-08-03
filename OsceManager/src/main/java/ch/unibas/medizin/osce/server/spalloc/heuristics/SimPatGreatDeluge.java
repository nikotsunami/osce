package ch.unibas.medizin.osce.server.spalloc.heuristics;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Map;

import net.sf.cpsolver.ifs.heuristics.NeighbourSelection;
import net.sf.cpsolver.ifs.heuristics.StandardNeighbourSelection;
import net.sf.cpsolver.ifs.model.Neighbour;
import net.sf.cpsolver.ifs.solution.Solution;
import net.sf.cpsolver.ifs.solution.SolutionListener;
import net.sf.cpsolver.ifs.solver.Solver;
import net.sf.cpsolver.ifs.util.DataProperties;
import net.sf.cpsolver.ifs.util.JProf;
import net.sf.cpsolver.ifs.util.Progress;
import net.sf.cpsolver.ifs.util.ToolBox;

import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.server.spalloc.model.ValPatient;
import ch.unibas.medizin.osce.server.spalloc.model.VarAssignment;

/**
 * Greate deluge. <br />
 * <br />
 * Adapted from the implementation of the exam timetabling problem of the IFS.<br />
 * <br />
 * In each iteration, a neighbour is generated and it is accepted if the value of the new
 * solution is below certain bound. This bound is initialized to the
 * GreatDeluge.UpperBoundRate &times; value of the best solution ever found.
 * After each iteration, the bound is decreased by GreatDeluge.CoolRate (new
 * bound equals to old bound &times; GreatDeluge.CoolRate). If the bound gets
 * bellow GreatDeluge.LowerBoundRate &times; value of the best solution ever
 * found, it is changed back to GreatDeluge.UpperBoundRate &times; value of the
 * best solution ever found.
 * 
 * If there was no improvement found between the bounds, the new bounds are
 * changed to GreatDeluge.UpperBoundRate^2 and GreatDeluge.LowerBoundRate^2,
 * GreatDeluge.UpperBoundRate^3 and GreatDeluge.LowerBoundRate^3, etc. till
 * there is an improvement found.
 */
public class SimPatGreatDeluge implements NeighbourSelection<VarAssignment, ValPatient>, SolutionListener<VarAssignment, ValPatient> {
    private static Logger sLog = Logger.getLogger(SimPatGreatDeluge.class);
    private static DecimalFormat sDF2 = new DecimalFormat("0.00");
    private static DecimalFormat sDF5 = new DecimalFormat("0.00000");
    private double iBound = 0.0;
    private double iCoolRate = 0.99999995;
    private long iIter;
    private double iUpperBound;
    private double iUpperBoundRate = 1.05;
    private double iLowerBoundRate = 0.95;
    private int iMoves = 0;
    private int iAcceptedMoves = 0;
    private int iNrIdle = 0;
    private long iT0 = -1;
    private long iLastImprovingIter = 0;
    private double iBestValue = 0;
    private Progress iProgress = null;

    private NeighbourSelection<VarAssignment, ValPatient>[] iNeighbours = null;

    /**
     * Constructor. Following problem properties are considered:
     * <ul>
     * <li>GreatDeluge.CoolRate ... bound cooling rate (default 0.99999995)
     * <li>GreatDeluge.UpperBoundRate ... bound upper bound relative to best
     * solution ever found (default 1.05)
     * <li>GreatDeluge.LowerBoundRate ... bound lower bound relative to best
     * solution ever found (default 0.95)
     * </ul>
     * 
     * @param properties
     *            problem properties
     */
    @SuppressWarnings("unchecked")
    public SimPatGreatDeluge(DataProperties properties) {
        iCoolRate = properties.getPropertyDouble("GreatDeluge.CoolRate", iCoolRate);
        iUpperBoundRate = properties.getPropertyDouble("GreatDeluge.UpperBoundRate", iUpperBoundRate);
        iLowerBoundRate = properties.getPropertyDouble("GreatDeluge.LowerBoundRate", iLowerBoundRate);
//        iNeighbours = new NeighbourSelection[] { new ExamRandomMove(properties), new ExamRoomMove(properties), new ExamTimeMove(properties) };
        try {
			iNeighbours = new NeighbourSelection[] { new StandardNeighbourSelection<VarAssignment, ValPatient>(properties) };
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    /** Initialization */
    public void init(Solver<VarAssignment, ValPatient> solver) {
        iIter = -1;
        solver.currentSolution().addSolutionListener(this);
        for (int i = 0; i < iNeighbours.length; i++)
            iNeighbours[i].init(solver);
        solver.setUpdateProgress(false);
        iProgress = Progress.getInstance(solver.currentSolution().getModel());
    }

    /** Print some information */
    protected void info(Solution<VarAssignment, ValPatient> solution) {
        sLog.info("Iter=" + iIter / 1000 + "k, NonImpIter=" + sDF2.format((iIter - iLastImprovingIter) / 1000.0)
                + "k, Speed=" + sDF2.format(1000.0 * iIter / (JProf.currentTimeMillis() - iT0)) + " it/s");
        sLog.info("Bound is " + sDF2.format(iBound) + ", " + "best value is " + sDF2.format(solution.getBestValue())
                + " (" + sDF2.format(100.0 * iBound / solution.getBestValue()) + "%), " + "current value is "
                + sDF2.format(solution.getModel().getTotalValue()) + " ("
                + sDF2.format(100.0 * iBound / solution.getModel().getTotalValue()) + "%), " + "#idle=" + iNrIdle
                + ", " + "Pacc=" + sDF5.format(100.0 * iAcceptedMoves / iMoves) + "%");
        iAcceptedMoves = iMoves = 0;
    }

    /**
     * Generate neighbour -- select neighbourhood randomly, select neighbour
     */
    public Neighbour<VarAssignment, ValPatient> genMove(Solution<VarAssignment, ValPatient> solution) {
        while (true) {
            incIter(solution);
            NeighbourSelection<VarAssignment, ValPatient> ns = iNeighbours[ToolBox.random(iNeighbours.length)];
            Neighbour<VarAssignment, ValPatient> n = ns.selectNeighbour(solution);
            if (n != null)
                return n;
        }
    }

    /** Accept neighbour */
    protected boolean accept(Solution<VarAssignment, ValPatient> solution, Neighbour<VarAssignment, ValPatient> neighbour) {
        return (neighbour.value() <= 0 || solution.getModel().getTotalValue() + neighbour.value() <= iBound);
    }

    /** Increment iteration count, update bound */
    protected void incIter(Solution<VarAssignment, ValPatient> solution) {
        if (iIter < 0) {
            iIter = 0;
            iLastImprovingIter = 0;
            iT0 = JProf.currentTimeMillis();
            iBound = (solution.getBestValue() > 0.0 ? iUpperBoundRate * solution.getBestValue() : solution
                    .getBestValue()
                    / iUpperBoundRate);
            iUpperBound = iBound;
            iNrIdle = 0;
            iProgress.setPhase("Great deluge [" + (1 + iNrIdle) + "]...");
        } else {
            iIter++;
            if (solution.getBestValue() >= 0.0)
                iBound *= iCoolRate;
            else
                iBound /= iCoolRate;
        }
        if (iIter % 100000 == 0) {
            info(solution);
        }
        double lowerBound = (solution.getBestValue() >= 0.0 ? Math.pow(iLowerBoundRate, 1 + iNrIdle)
                * solution.getBestValue() : solution.getBestValue() / Math.pow(iLowerBoundRate, 1 + iNrIdle));
        if (iBound < lowerBound) {
            iNrIdle++;
            sLog.info(" -<[" + iNrIdle + "]>- ");
            iBound = Math.max(solution.getBestValue() + 2.0, (solution.getBestValue() >= 0.0 ? Math.pow(
                    iUpperBoundRate, iNrIdle)
                    * solution.getBestValue() : solution.getBestValue() / Math.pow(iUpperBoundRate, iNrIdle)));
            iUpperBound = iBound;
            iProgress.setPhase("Great deluge [" + (1 + iNrIdle) + "]...");
        }
        iProgress.setProgress(100 - Math.round(100.0 * (iBound - lowerBound) / (iUpperBound - lowerBound)));
    }

    /**
     * A neighbour is generated randomly untill an acceptable one is found.
     */
    public Neighbour<VarAssignment, ValPatient> selectNeighbour(Solution<VarAssignment, ValPatient> solution) {
        Neighbour<VarAssignment, ValPatient> neighbour = null;
        while ((neighbour = genMove(solution)) != null) {
            iMoves++;
            if (accept(solution, neighbour)) {
                iAcceptedMoves++;
                break;
            }
        }
        return (neighbour == null ? null : neighbour);
    }

    /** Update last improving iteration count */
    public void bestSaved(Solution<VarAssignment, ValPatient> solution) {
        if (Math.abs(iBestValue - solution.getBestValue()) >= 1.0) {
            iLastImprovingIter = iIter;
            iNrIdle = 0;
            iBestValue = solution.getBestValue();
        }
    }

    public void solutionUpdated(Solution<VarAssignment, ValPatient> solution) {}

    public void getInfo(Solution<VarAssignment, ValPatient> solution, Map<String, String> info) {}

    public void getInfo(Solution<VarAssignment, ValPatient> solution, Map<String, String> info, Collection<VarAssignment> variables) {}

    public void bestCleared(Solution<VarAssignment, ValPatient> solution) {}

    public void bestRestored(Solution<VarAssignment, ValPatient> solution) {}
}
