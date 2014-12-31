package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentOscesProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentProxy;
import ch.unibas.medizin.osce.shared.AssignmentTypes;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class AssignmentProxyRenderer extends ProxyRenderer<AssignmentProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.AssignmentProxyRenderer INSTANCE;

    protected AssignmentProxyRenderer() {
        super(new String[] { "id" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.AssignmentProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new AssignmentProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(AssignmentProxy object) {
        if (object == null) {
            return "";
        }
        return object.getId() + " (" + object.getId() + ")";
    }
}
