package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.LangSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;

public class LangSkillProxyRenderer extends ProxyRenderer<LangSkillProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.LangSkillProxyRenderer INSTANCE;

    protected LangSkillProxyRenderer() {
        super(new String[] { "skill" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.LangSkillProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new LangSkillProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(LangSkillProxy object) {
        if (object == null) {
            return "";
        }
        return object.getSkill() + " (" + object.getId() + ")";
    }
}
