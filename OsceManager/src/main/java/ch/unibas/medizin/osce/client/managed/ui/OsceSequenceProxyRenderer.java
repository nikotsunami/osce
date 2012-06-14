package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;
import java.util.Set;

public class OsceSequenceProxyRenderer extends ProxyRenderer<OsceSequenceProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.OsceSequenceProxyRenderer INSTANCE;

    protected OsceSequenceProxyRenderer() {
        super(new String[] { "label" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.OsceSequenceProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new OsceSequenceProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(OsceSequenceProxy object) {
        if (object == null) {
            return "";
        }
        return object.getLabel() + " (" + object.getId() + ")";
    }
}
