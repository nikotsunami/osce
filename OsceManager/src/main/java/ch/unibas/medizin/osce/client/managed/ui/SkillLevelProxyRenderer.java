package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.SkillLevelProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class SkillLevelProxyRenderer extends ProxyRenderer<SkillLevelProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.SkillLevelProxyRenderer INSTANCE;

    protected SkillLevelProxyRenderer() {
        super(new String[] { "id" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.SkillLevelProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new SkillLevelProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(SkillLevelProxy object) {
        if (object == null) {
            return "";
        }
        return object.getId() + " (" + object.getId() + ")";
    }
}
