package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.KeywordProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import java.util.Set;

public class KeywordProxyRenderer extends ProxyRenderer<KeywordProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.KeywordProxyRenderer INSTANCE;

    protected KeywordProxyRenderer() {
        super(new String[] { "name" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.KeywordProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new KeywordProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(KeywordProxy object) {
        if (object == null) {
            return "";
        }
        return object.getName() + " (" + object.getId() + ")";
    }
}
