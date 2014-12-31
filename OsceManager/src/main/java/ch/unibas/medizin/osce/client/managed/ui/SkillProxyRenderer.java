package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.SkillHasApplianceProxy;
import ch.unibas.medizin.osce.client.managed.request.SkillLevelProxy;
import ch.unibas.medizin.osce.client.managed.request.SkillProxy;
import ch.unibas.medizin.osce.client.managed.request.TopicProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import java.util.Set;

public class SkillProxyRenderer extends ProxyRenderer<SkillProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.SkillProxyRenderer INSTANCE;

    protected SkillProxyRenderer() {
        super(new String[] { "description" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.SkillProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new SkillProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(SkillProxy object) {
        if (object == null) {
            return "";
        }
        return object.getDescription() + " (" + object.getId() + ")";
    }
}
