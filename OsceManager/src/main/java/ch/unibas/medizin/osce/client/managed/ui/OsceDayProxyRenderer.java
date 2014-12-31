package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;

import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class OsceDayProxyRenderer extends ProxyRenderer<OsceDayProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.OsceDayProxyRenderer INSTANCE;

    protected OsceDayProxyRenderer() {
        super(new String[] { "breakByRotation" });
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
        return object.getBreakByRotation() + " (" + object.getId() + ")";
    }
}
