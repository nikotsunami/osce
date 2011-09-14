package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentProxy;
import ch.unibas.medizin.osce.shared.StudyYears;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;
import java.util.Set;

public class OsceProxyRenderer extends ProxyRenderer<OsceProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.OsceProxyRenderer INSTANCE;

    protected OsceProxyRenderer() {
        super(new String[] { "id" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.OsceProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new OsceProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(OsceProxy object) {
        if (object == null) {
            return "";
        }
        return object.getId() + " (" + object.getId() + ")";
    }
}
