package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.AdministratorProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.TaskProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import java.util.Set;

public class AdministratorProxyRenderer extends ProxyRenderer<AdministratorProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.AdministratorProxyRenderer INSTANCE;

    protected AdministratorProxyRenderer() {
        super(new String[] { "email" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.AdministratorProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new AdministratorProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(AdministratorProxy object) {
        if (object == null) {
            return "";
        }
        return object.getEmail() + " (" + object.getId() + ")";
    }
}
