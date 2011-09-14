package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;

public class StandardizedRoleProxyRenderer extends ProxyRenderer<StandardizedRoleProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.StandardizedRoleProxyRenderer INSTANCE;

    protected StandardizedRoleProxyRenderer() {
        super(new String[] { "shortName" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.StandardizedRoleProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new StandardizedRoleProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(StandardizedRoleProxy object) {
        if (object == null) {
            return "";
        }
        return object.getShortName() + " (" + object.getId() + ")";
    }
}
