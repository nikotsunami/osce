package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.PatientInRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;

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
