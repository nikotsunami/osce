package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;
import java.util.Set;

public class CourseProxyRenderer extends ProxyRenderer<CourseProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.CourseProxyRenderer INSTANCE;

    protected CourseProxyRenderer() {
        super(new String[] { "color" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.CourseProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new CourseProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(CourseProxy object) {
        if (object == null) {
            return "";
        }
        return object.getColor() + " (" + object.getId() + ")";
    }
}
