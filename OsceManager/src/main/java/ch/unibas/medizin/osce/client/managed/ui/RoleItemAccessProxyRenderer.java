package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleItemAccessProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;
import java.util.Set;

public class RoleItemAccessProxyRenderer extends ProxyRenderer<RoleItemAccessProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.RoleItemAccessProxyRenderer INSTANCE;

    public RoleItemAccessProxyRenderer() {
        super(new String[] { "name" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.RoleItemAccessProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new RoleItemAccessProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(RoleItemAccessProxy object) {
        if (object == null) {
            return "";
        }
        return object.getName() + " (" + object.getId() + ")";
    }
}
