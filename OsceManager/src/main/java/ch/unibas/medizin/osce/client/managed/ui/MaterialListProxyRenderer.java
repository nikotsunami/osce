package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.MaterialListProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;

public class MaterialListProxyRenderer extends ProxyRenderer<MaterialListProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.MaterialListProxyRenderer INSTANCE;

    protected MaterialListProxyRenderer() {
        super(new String[] { "id" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.MaterialListProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new MaterialListProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(MaterialListProxy object) {
        if (object == null) {
            return "";
        }
        return object.getId() + " (" + object.getId() + ")";
    }
}
