package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.RoleTableItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTableItemValueProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class RoleTableItemValueProxyRenderer extends ProxyRenderer<RoleTableItemValueProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.RoleTableItemValueProxyRenderer INSTANCE;

    protected RoleTableItemValueProxyRenderer() {
        super(new String[] { "value" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.RoleTableItemValueProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new RoleTableItemValueProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(RoleTableItemValueProxy object) {
        if (object == null) {
            return "";
        }
        return object.getValue() + " (" + object.getId() + ")";
    }
}
