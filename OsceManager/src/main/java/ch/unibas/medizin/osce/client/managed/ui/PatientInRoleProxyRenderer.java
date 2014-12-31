package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import java.util.Set;

public class PatientInRoleProxyRenderer extends ProxyRenderer<PatientInRoleProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.PatientInRoleProxyRenderer INSTANCE;

    protected PatientInRoleProxyRenderer() {
        super(new String[] { "id" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.PatientInRoleProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new PatientInRoleProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(PatientInRoleProxy object) {
        if (object == null) {
            return "";
        }
        return object.getId() + " (" + object.getId() + ")";
    }
}
