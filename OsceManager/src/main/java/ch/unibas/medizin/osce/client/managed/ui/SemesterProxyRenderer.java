package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.AdministratorProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.TrainingProxy;
import ch.unibas.medizin.osce.shared.Semesters;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import java.util.Set;

public class SemesterProxyRenderer extends ProxyRenderer<SemesterProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.SemesterProxyRenderer INSTANCE;

    protected SemesterProxyRenderer() {
        super(new String[] { "id" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.SemesterProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new SemesterProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(SemesterProxy object) {
        if (object == null) {
            return "";
        }
        return object.getId() + " (" + object.getId() + ")";
    }
}
