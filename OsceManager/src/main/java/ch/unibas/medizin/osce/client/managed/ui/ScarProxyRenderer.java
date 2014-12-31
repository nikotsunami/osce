package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.shared.TraitTypes;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import java.util.Set;

public class ScarProxyRenderer extends ProxyRenderer<ScarProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.ScarProxyRenderer INSTANCE;

    protected ScarProxyRenderer() {
        super(new String[] { "bodypart" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.ScarProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new ScarProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(ScarProxy object) {
        if (object == null) {
            return "";
        }
        return object.getBodypart() + " (" + object.getId() + ")";
    }
}
