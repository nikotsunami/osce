package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.MainClassificationProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class MainClassificationProxyRenderer extends ProxyRenderer<MainClassificationProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.MainClassificationProxyRenderer INSTANCE;

    protected MainClassificationProxyRenderer() {
        super(new String[] { "shortcut" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.MainClassificationProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new MainClassificationProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(MainClassificationProxy object) {
        if (object == null) {
            return "";
        }
        return object.getShortcut() + " (" + object.getId() + ")";
    }
}
