package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.ApplianceProxy;
import ch.unibas.medizin.osce.client.managed.request.SkillHasApplianceProxy;
import ch.unibas.medizin.osce.client.managed.request.SkillProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class SkillHasApplianceProxyRenderer extends ProxyRenderer<SkillHasApplianceProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.SkillHasApplianceProxyRenderer INSTANCE;

    protected SkillHasApplianceProxyRenderer() {
        super(new String[] { "id" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.SkillHasApplianceProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new SkillHasApplianceProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(SkillHasApplianceProxy object) {
        if (object == null) {
            return "";
        }
        return object.getId() + " (" + object.getId() + ")";
    }
}
