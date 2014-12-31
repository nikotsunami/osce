package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import java.util.Set;

public class AnamnesisFormProxyRenderer extends ProxyRenderer<AnamnesisFormProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.AnamnesisFormProxyRenderer INSTANCE;

    protected AnamnesisFormProxyRenderer() {
        super(new String[] { "id" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.AnamnesisFormProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new AnamnesisFormProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(AnamnesisFormProxy object) {
        if (object == null) {
            return "";
        }
        return object.getId() + " (" + object.getId() + ")";
    }
}
