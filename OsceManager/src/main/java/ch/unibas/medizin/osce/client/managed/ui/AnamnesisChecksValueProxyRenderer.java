package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueProxy;

import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class AnamnesisChecksValueProxyRenderer extends ProxyRenderer<AnamnesisChecksValueProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.AnamnesisChecksValueProxyRenderer INSTANCE;

    protected AnamnesisChecksValueProxyRenderer() {
        super(new String[] { "comment" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.AnamnesisChecksValueProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new AnamnesisChecksValueProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(AnamnesisChecksValueProxy object) {
        if (object == null) {
            return "";
        }
        return object.getComment() + " (" + object.getId() + ")";
    }
}
