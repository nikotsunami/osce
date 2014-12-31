package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.OfficeProxy;
import ch.unibas.medizin.osce.shared.Gender;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class OfficeProxyRenderer extends ProxyRenderer<OfficeProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.OfficeProxyRenderer INSTANCE;

    protected OfficeProxyRenderer() {
        super(new String[] { "title" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.OfficeProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new OfficeProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(OfficeProxy object) {
        if (object == null) {
            return "";
        }
        return object.getTitle() + " (" + object.getId() + ")";
    }
}
