package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.RoleParticipantProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;

public class RoleParticipantProxyRenderer extends ProxyRenderer<RoleParticipantProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.RoleParticipantProxyRenderer INSTANCE;

    protected RoleParticipantProxyRenderer() {
        super(new String[] { "id" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.RoleParticipantProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new RoleParticipantProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(RoleParticipantProxy object) {
        if (object == null) {
            return "";
        }
        return object.getId() + " (" + object.getId() + ")";
    }
}
