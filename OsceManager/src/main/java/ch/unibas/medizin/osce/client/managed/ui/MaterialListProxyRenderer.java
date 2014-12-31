package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.MaterialListProxy;
import ch.unibas.medizin.osce.client.managed.request.UsedMaterialProxy;
import ch.unibas.medizin.osce.shared.MaterialType;
import ch.unibas.medizin.osce.shared.PriceType;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import java.util.Set;

public class MaterialListProxyRenderer extends ProxyRenderer<MaterialListProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.MaterialListProxyRenderer INSTANCE;

    protected MaterialListProxyRenderer() {
        super(new String[] { "name" });
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
        return object.getName() + " (" + object.getId() + ")";
    }
}
