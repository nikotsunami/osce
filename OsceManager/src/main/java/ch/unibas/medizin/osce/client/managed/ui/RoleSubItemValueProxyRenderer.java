package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleSubItemValueProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class RoleSubItemValueProxyRenderer extends ProxyRenderer<RoleSubItemValueProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.RoleSubItemValueProxyRenderer INSTANCE;

    protected RoleSubItemValueProxyRenderer() {
        super(new String[] { "itemText" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.RoleSubItemValueProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new RoleSubItemValueProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(RoleSubItemValueProxy object) {
        if (object == null) {
            return "";
        }
        return object.getItemText() + " (" + object.getId() + ")";
    }
}
