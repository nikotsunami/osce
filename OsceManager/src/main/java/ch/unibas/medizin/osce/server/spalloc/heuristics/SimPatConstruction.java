package ch.unibas.medizin.osce.server.spalloc.heuristics;

import java.util.HashSet;

import net.sf.cpsolver.ifs.heuristics.NeighbourSelection;
import net.sf.cpsolver.ifs.model.Neighbour;
import net.sf.cpsolver.ifs.model.SimpleNeighbour;
import net.sf.cpsolver.ifs.solution.Solution;
import net.sf.cpsolver.ifs.solver.Solver;
import net.sf.cpsolver.ifs.util.DataProperties;
import net.sf.cpsolver.ifs.util.Progress;
import ch.unibas.medizin.osce.server.spalloc.model.OsceModel;
import ch.unibas.medizin.osce.server.spalloc.model.ValPatient;
import ch.unibas.medizin.osce.server.spalloc.model.VarAssignment;

/**
 * Initial solution construction heuristics. <br />
 * <br />
 * Adapted from the implementation of the exam timetabling problem of the IFS.
 */
public class SimPatConstruction implements NeighbourSelection<VarAssignment, ValPatient> {
    @SuppressWarnings("unused")
	private HashSet<String> iAssignments = new HashSet<String>();
    private boolean iCheckLocalOptimality = false;
    private HashSet<VarAssignment> iSkip = new HashSet<VarAssignment>();
    private Progress iProgress = null;
    private boolean iActive;

    /**
     * Constructor
     * 
     * @param properties
     *            problem properties
     */
    public SimPatConstruction(DataProperties properties) {
        iCheckLocalOptimality = properties.getPropertyBoolean("ExamConstruction.CheckLocalOptimality",
                iCheckLocalOptimality);
    }

    /**
     * Initialization
     */
    public void init(Solver<VarAssignment, ValPatient> solver) {
        iSkip.clear();
        solver.setUpdateProgress(false);
        iProgress = Progress.getInstance(solver.currentSolution().getModel());
        iActive = false;
    }

    /**
     * Find a new assignment of one of the assigned exams that improves the time
     * cost and for which there is a set of
     * available rooms {@link Exam#findBestAvailableRooms(ExamPeriodPlacement)}.
     * Return null, if there is no such assignment (the problem is considered
     * locally optimal).
     */
//    public Neighbour<VarAssignment, ValPatient> checkLocalOptimality(OsceModel model) {
//        if (iCheckLocalOptimality) {
//            for (VarAssignment exam : model.assignedVariables()) {
//                ValPatient current = exam.getAssignment();
//                if (current.getTimeCost() <= 0)
//                    continue;
//                ValPatient best = null;
//                for (ExamPeriodPlacement period : exam.getPeriodPlacements()) {
//                    if (exam.countStudentConflicts(period) > 0) {
//                        if (iAssignments.contains(exam.getId() + ":" + period.getIndex()))
//                            continue;
//                    }
//                    if (!exam.checkDistributionConstraints(period))
//                        continue;
//                    ValPatient placement = new ValPatient(exam, period, null);
//                    if (best == null || best.getTimeCost() > placement.getTimeCost()) {
//                        Set<ExamRoomPlacement> rooms = exam.findBestAvailableRooms(period);
//                        if (rooms != null)
//                            best = new ValPatient(exam, period, rooms);
//                    }
//                }
//                if (best != null && best.getTimeCost() < current.getTimeCost())
//                    return new ExamSimpleNeighbour(best);
//            }
//        }
//        iActive = false;
//        return null;
//    }

    /**
     * Select a neighbour. While there are exams that are still not assigned:
     * <ul>
     * <li>The worst unassigned exam is selected
     * <li>The best period that does not break any hard constraint and for which
     * there is a room assignment available is selected together with the set
     * the best available rooms for the exam in the best period.
     * </ul>
     * Return null when done (all variables are assigned and the problem is
     * locally optimal).
     */
    public Neighbour<VarAssignment, ValPatient> selectNeighbour(Solution<VarAssignment, ValPatient> solution) {
        OsceModel model = (OsceModel) solution.getModel();
        if (!iActive) {
            iActive = true;
            iProgress.setPhase("Construction ...", model.variables().size());
        }
        iProgress.setProgress(model.nrAssignedVariables());
//        if (model.nrUnassignedVariables() <= iSkip.size())
//            return checkLocalOptimality(model);
        VarAssignment bestExam = null;
        for (VarAssignment exam : model.unassignedVariables()) {
            if (iSkip.contains(exam))
                continue;
            if (bestExam == null || exam.compareTo(bestExam) < 0)
                bestExam = exam;
        }
        ValPatient best = null;
//        for (ExamPeriodPlacement period : bestExam.getPeriodPlacements()) {
//            if (bestExam.countStudentConflicts(period) > 0) {
//                if (iAssignments.contains(bestExam.getId() + ":" + period.getIndex()))
//                    continue;
//            }
//            if (!bestExam.checkDistributionConstraints(period))
//                continue;
//            ValPatient placement = new ValPatient(bestVarAssignment, period, null);
//            if (best == null || best.getTimeCost() > placement.getTimeCost()) {
//                Set<ValPatient> rooms = bestExam.findBestAvailableRooms(period);
//                if (rooms != null)
//                    best = new ValPatient(bestVarAssignment, period, rooms);
//            }
//        }
        if (best != null) {
//            iAssignments.add(bestExam.getId() + ":" + best.getPeriod().getIndex());
            return new SimpleNeighbour<VarAssignment, ValPatient>(best.variable(), best);
        } /*
           * else { for (Enumeration
           * e=bestExam.getPeriodPlacements().elements();e.hasMoreElements();) {
           * ExamPeriodPlacement period = (ExamPeriodPlacement)e.nextElement();
           * ValPatient placement = new ValPatient(bestVarAssignment, period,
           * null); if (best==null ||
           * best.getTimeCost()>placement.getTimeCost()) { Set rooms =
           * bestExam.findBestAvailableRooms(period); if (rooms!=null) best =
           * new ValPatient(bestVarAssignment, period, rooms); } } if (best!=null) {
           * bestExam.allowAllStudentConflicts(best.getPeriod());
           * iAssignments.add(bestExam.getId()+":"+best.getPeriod().getIndex());
           * return new ExamSimpleNeighbour(best); } }
           */
        if (!iSkip.contains(bestExam)) {
            iSkip.add(bestExam);
            return selectNeighbour(solution);
        }
//        return checkLocalOptimality(model);
        return null;
    }

}