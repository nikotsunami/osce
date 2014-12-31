package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleItemAccessProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import java.util.Set;

public class RoleItemAccessProxyRenderer extends ProxyRenderer<RoleItemAccessProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.RoleItemAccessProxyRenderer INSTANCE;

    protected RoleItemAccessProxyRenderer() {
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
