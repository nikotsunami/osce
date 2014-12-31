package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleParticipantProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.shared.RoleParticipantTypes;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

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
