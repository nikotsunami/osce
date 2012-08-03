package ch.unibas.medizin.osce.server.spalloc.heuristics;

import ch.unibas.medizin.osce.server.spalloc.model.OsceModel;
import ch.unibas.medizin.osce.server.spalloc.model.ValPatient;
import ch.unibas.medizin.osce.server.spalloc.model.VarAssignment;
import net.sf.cpsolver.ifs.heuristics.VariableSelection;
import net.sf.cpsolver.ifs.solution.Solution;
import net.sf.cpsolver.ifs.solver.Solver;
import net.sf.cpsolver.ifs.util.DataProperties;
import net.sf.cpsolver.ifs.util.ToolBox;

/**
 * Unassigned variable selection. <br />
 * <br />
 * Adapted from the implementation of the exam timetabling problem of the IFS.<br />
 * <br />
 */
public class SimPatUnassignedVariableSelection implements VariableSelection<VarAssignment, ValPatient> {
    private boolean iRandomSelection = true;

    /** Constructor */
    public SimPatUnassignedVariableSelection(DataProperties properties) {
        iRandomSelection = properties.getPropertyBoolean("SimPatUnassignedVariableSelection.random", iRandomSelection);
    }

    /** Initialization */
    public void init(Solver<VarAssignment, ValPatient> solver) {
    }

    /** Variable selection */
    public VarAssignment selectVariable(Solution<VarAssignment, ValPatient> solution) {
        OsceModel model = (OsceModel) solution.getModel();
        if (model.nrUnassignedVariables() == 0)
            return null;
        if (iRandomSelection)
            return ToolBox.random(model.unassignedVariables());
        VarAssignment variable = null;
        for (VarAssignment v : model.unassignedVariables()) {
            if (variable == null || v.compareTo(variable) < 0)
                variable = v;
        }
        return variable;
    }
}
