package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.MainSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.SkillProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class MainSkillProxyRenderer extends ProxyRenderer<MainSkillProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.MainSkillProxyRenderer INSTANCE;

    protected MainSkillProxyRenderer() {
        super(new String[] { "id" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.MainSkillProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new MainSkillProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(MainSkillProxy object) {
        if (object == null) {
            return "";
        }
        return object.getId() + " (" + object.getId() + ")";
    }
}
