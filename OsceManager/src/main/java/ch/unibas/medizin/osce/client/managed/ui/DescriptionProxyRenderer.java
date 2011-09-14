package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.DescriptionProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;

public class DescriptionProxyRenderer extends ProxyRenderer<DescriptionProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.DescriptionProxyRenderer INSTANCE;

    protected DescriptionProxyRenderer() {
        super(new String[] { "description" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.DescriptionProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new DescriptionProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(DescriptionProxy object) {
        if (object == null) {
            return "";
        }
        return object.getDescription() + " (" + object.getId() + ")";
    }
}
