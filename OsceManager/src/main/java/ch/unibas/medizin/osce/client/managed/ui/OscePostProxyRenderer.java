package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;
import java.util.Set;

public class OscePostProxyRenderer extends ProxyRenderer<OscePostProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.OscePostProxyRenderer INSTANCE;

    protected OscePostProxyRenderer() {
        super(new String[] { "id" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.OscePostProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new OscePostProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(OscePostProxy object) {
        if (object == null) {
            return "";
        }
        return object.getId() + " (" + object.getId() + ")";
    }
}
