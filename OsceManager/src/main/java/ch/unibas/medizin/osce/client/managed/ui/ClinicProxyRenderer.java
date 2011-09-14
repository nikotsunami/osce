package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;
import java.util.Set;

public class ClinicProxyRenderer extends ProxyRenderer<ClinicProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.ClinicProxyRenderer INSTANCE;

    protected ClinicProxyRenderer() {
        super(new String[] { "name" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.ClinicProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new ClinicProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(ClinicProxy object) {
        if (object == null) {
            return "";
        }
        return object.getName() + " (" + object.getId() + ")";
    }
}
