package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleItemAccessProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTableItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTemplateProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;
import java.util.List;
import java.util.Set;

public class RoleBaseItemProxyRenderer extends ProxyRenderer<RoleBaseItemProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.RoleBaseItemProxyRenderer INSTANCE;

    protected RoleBaseItemProxyRenderer() {
        super(new String[] { "item_name" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.RoleBaseItemProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new RoleBaseItemProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(RoleBaseItemProxy object) {
        if (object == null) {
            return "";
        }
        return object.getItem_name() + " (" + object.getId() + ")";
    }
}
