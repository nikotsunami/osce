package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.RoomProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;
import java.util.Set;

public class OscePostRoomProxyRenderer extends ProxyRenderer<OscePostRoomProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.OscePostRoomProxyRenderer INSTANCE;

    protected OscePostRoomProxyRenderer() {
        super(new String[] { "id" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.OscePostRoomProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new OscePostRoomProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(OscePostRoomProxy object) {
        if (object == null) {
            return "";
        }
        return object.getId() + " (" + object.getId() + ")";
    }
}
