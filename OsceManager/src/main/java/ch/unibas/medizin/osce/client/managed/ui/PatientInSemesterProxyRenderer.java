package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;

public class PatientInSemesterProxyRenderer extends ProxyRenderer<PatientInSemesterProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.PatientInSemesterProxyRenderer INSTANCE;

    protected PatientInSemesterProxyRenderer() {
        super(new String[] { "id" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.PatientInSemesterProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new PatientInSemesterProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(PatientInSemesterProxy object) {
        if (object == null) {
            return "";
        }
        return object.getId() + " (" + object.getId() + ")";
    }
}
