package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTemplateProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;
import java.util.List;

public class RoleTemplateProxyRenderer extends ProxyRenderer<RoleTemplateProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.RoleTemplateProxyRenderer INSTANCE;

    protected RoleTemplateProxyRenderer() {
        super(new String[] { "templateName" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.RoleTemplateProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new RoleTemplateProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(RoleTemplateProxy object) {
        if (object == null) {
            return "";
        }
        return object.getTemplateName() + " (" + object.getId() + ")";
    }
}
