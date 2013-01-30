package ch.unibas.medizin.osce.server.spalloc.heuristics;

import java.util.Collection;
import java.util.Map;

import net.sf.cpsolver.ifs.heuristics.NeighbourSelection;
import net.sf.cpsolver.ifs.heuristics.StandardNeighbourSelection;
import net.sf.cpsolver.ifs.model.Neighbour;
import net.sf.cpsolver.ifs.solution.Solution;
import net.sf.cpsolver.ifs.solution.SolutionListener;
import net.sf.cpsolver.ifs.solver.Solver;
import net.sf.cpsolver.ifs.util.DataProperties;
import net.sf.cpsolver.ifs.util.Progress;
import net.sf.cpsolver.ifs.util.ToolBox;
import ch.unibas.medizin.osce.server.spalloc.model.ValPatient;
import ch.unibas.medizin.osce.server.spalloc.model.VarAssignment;

/**
 * Hill climber. <br />
 * <br />
 * Adapted from the implementation of the exam timetabling problem of the IFS.<br />
 * <br />
 * 
 * In each iteration, a neighbour is generated and it is accepted if its value
 * is below or equal to zero. The search is stopped after a given amount of idle
 * iterations ( can be defined by problem property HillClimber.MaxIdle).
 */
public class SimPatHillClimbing implements NeighbourSelection<VarAssignment, ValPatient>, SolutionListener<VarAssignment, ValPatient> {
    private NeighbourSelection<VarAssignment, ValPatient>[] iNeighbours = null;
    private int iMaxIdleIters = 25000;
    private int iLastImprovingIter = 0;
    private double iBestValue = 0;
    private int iIter = 0;
    private Progress iProgress = null;
    private boolean iActive;
    private String iName = "Hill climbing";

    /**
     * Constructor
     * 
     * @param properties
     *            problem properties (use HillClimber.MaxIdle to set maximum
     *            number of idle iterations)
     */
    public SimPatHillClimbing(DataProperties properties) {
        this(properties, "Hill Climbing");
    }

    /**
     * Constructor
     * 
     * @param properties
     *            problem properties (use HillClimber.MaxIdle to set maximum
     *            number of idle iterations)
     */
    @SuppressWarnings("unchecked")
    public SimPatHillClimbing(DataProperties properties, String name) {
        iMaxIdleIters = properties.getPropertyInt("HillClimber.MaxIdle", iMaxIdleIters);
//        iNeighbours = new NeighbourSelection[] { new ExamRandomMove(properties), new ExamRoomMove(properties), new ExamTimeMove(properties) };
		try {
			iNeighbours = new NeighbourSelection[] { new StandardNeighbourSelection<VarAssignment, ValPatient>(properties) };
		} catch (Exception e) {
			e.printStackTrace();
		}
        iName = name;
    }

    /**
     * Initialization
     */
    public void init(Solver<VarAssignment, ValPatient> solver) {
        solver.currentSolution().addSolutionListener(this);
        for (int i = 0; i < iNeighbours.length; i++)
            iNeighbours[i].init(solver);
        solver.setUpdateProgress(false);
        iProgress = Progress.getInstance(solver.currentSolution().getModel());
        iActive = false;
    }

    /**
     * Select one of the given neighbourhoods randomly, select neighbour, return
     * it if its value is below or equal to zero (continue with the next
     * selection otherwise). Return null when the given number of idle
     * iterations is reached.
     */
    public Neighbour<VarAssignment, ValPatient> selectNeighbour(Solution<VarAssignment, ValPatient> solution) {
        if (!iActive) {
            iProgress.setPhase(iName + "...");
            iActive = true;
        }
        while (true) {
            iIter++;
            iProgress.setProgress(Math.round(100.0 * (iIter - iLastImprovingIter) / iMaxIdleIters));
            if (iIter - iLastImprovingIter >= iMaxIdleIters)
                break;
            NeighbourSelection<VarAssignment, ValPatient> ns = iNeighbours[ToolBox.random(iNeighbours.length)];
            Neighbour<VarAssignment, ValPatient> n = ns.selectNeighbour(solution);
            if (n != null && n.value() <= 0)
                return n;
        }
        iIter = 0;
        iLastImprovingIter = 0;
        iActive = false;
        return null;
    }

    /**
     * Memorize the iteration when the last best solution was found.
     */
    public void bestSaved(Solution<VarAssignment, ValPatient> solution) {
        if (Math.abs(iBestValue - solution.getBestValue()) >= 1.0) {
            iLastImprovingIter = iIter;
            iBestValue = solution.getBestValue();
        }
    }

    public void solutionUpdated(Solution<VarAssignment, ValPatient> solution) {}

    public void getInfo(Solution<VarAssignment, ValPatient> solution, Map<String, String> info) {}

    public void getInfo(Solution<VarAssignment, ValPatient> solution, Map<String, String> info, Collection<VarAssignment> variables) {}

    public void bestCleared(Solution<VarAssignment, ValPatient> solution) {}

    public void bestRestored(Solution<VarAssignment, ValPatient> solution) {}
}
