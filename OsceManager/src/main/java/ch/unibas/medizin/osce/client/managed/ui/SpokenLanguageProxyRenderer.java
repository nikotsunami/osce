package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.LangSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;
import java.util.Set;

public class SpokenLanguageProxyRenderer extends ProxyRenderer<SpokenLanguageProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.SpokenLanguageProxyRenderer INSTANCE;

    protected SpokenLanguageProxyRenderer() {
        super(new String[] { "languageName" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.SpokenLanguageProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new SpokenLanguageProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(SpokenLanguageProxy object) {
        if (object == null) {
            return "";
        }
        return object.getLanguageName() + " (" + object.getId() + ")";
    }
}
