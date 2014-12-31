package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.ApplianceProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class ApplianceProxyRenderer extends ProxyRenderer<ApplianceProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.ApplianceProxyRenderer INSTANCE;

    protected ApplianceProxyRenderer() {
        super(new String[] { "shortcut" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.ApplianceProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new ApplianceProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(ApplianceProxy object) {
        if (object == null) {
            return "";
        }
        return object.getShortcut() + " (" + object.getId() + ")";
    }
}
