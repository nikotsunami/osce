package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class AnamnesisCheckTitleProxyRenderer extends ProxyRenderer<AnamnesisCheckTitleProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.AnamnesisCheckTitleProxyRenderer INSTANCE;

    protected AnamnesisCheckTitleProxyRenderer() {
        super(new String[] { "text" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.AnamnesisCheckTitleProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new AnamnesisCheckTitleProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(AnamnesisCheckTitleProxy object) {
        if (object == null) {
            return "";
        }
        return object.getText() + " (" + object.getId() + ")";
    }
}
