package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTableItemProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;

public class RoleTableItemProxyRenderer extends ProxyRenderer<RoleTableItemProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.RoleTableItemProxyRenderer INSTANCE;

    protected RoleTableItemProxyRenderer() {
        super(new String[] { "itemName" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.RoleTableItemProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new RoleTableItemProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(RoleTableItemProxy object) {
        if (object == null) {
            return "";
        }
        return object.getItemName() + " (" + object.getId() + ")";
    }
}
