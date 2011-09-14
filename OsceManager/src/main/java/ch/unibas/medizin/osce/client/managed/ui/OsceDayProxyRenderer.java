package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;
import java.util.Set;

public class OsceDayProxyRenderer extends ProxyRenderer<OsceDayProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.OsceDayProxyRenderer INSTANCE;

    protected OsceDayProxyRenderer() {
        super(new String[] { "id" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.OsceDayProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new OsceDayProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(OsceDayProxy object) {
        if (object == null) {
            return "";
        }
        return object.getId() + " (" + object.getId() + ")";
    }
}
