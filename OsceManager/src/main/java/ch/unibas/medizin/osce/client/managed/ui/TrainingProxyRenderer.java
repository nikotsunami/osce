package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.TrainingProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;
import java.util.Set;

public class TrainingProxyRenderer extends ProxyRenderer<TrainingProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.TrainingProxyRenderer INSTANCE;

    protected TrainingProxyRenderer() {
        super(new String[] { "name" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.TrainingProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new TrainingProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(TrainingProxy object) {
        if (object == null) {
            return "";
        }
        return object.getName() + " (" + object.getId() + ")";
    }
}
