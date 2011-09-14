package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;
import java.util.Set;

public class AnamnesisCheckProxyRenderer extends ProxyRenderer<AnamnesisCheckProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.AnamnesisCheckProxyRenderer INSTANCE;

    protected AnamnesisCheckProxyRenderer() {
        super(new String[] { "text" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.AnamnesisCheckProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new AnamnesisCheckProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(AnamnesisCheckProxy object) {
        if (object == null) {
            return "";
        }
        return object.getText() + " (" + object.getId() + ")";
    }
}
