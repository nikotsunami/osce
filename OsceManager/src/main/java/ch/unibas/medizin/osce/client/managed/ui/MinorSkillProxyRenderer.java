package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.MinorSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.SkillProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class MinorSkillProxyRenderer extends ProxyRenderer<MinorSkillProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.MinorSkillProxyRenderer INSTANCE;

    protected MinorSkillProxyRenderer() {
        super(new String[] { "id" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.MinorSkillProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new MinorSkillProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(MinorSkillProxy object) {
        if (object == null) {
            return "";
        }
        return object.getId() + " (" + object.getId() + ")";
    }
}
