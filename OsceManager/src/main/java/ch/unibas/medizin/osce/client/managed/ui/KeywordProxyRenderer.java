package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.KeywordProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;

public class KeywordProxyRenderer extends ProxyRenderer<KeywordProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.KeywordProxyRenderer INSTANCE;

    protected KeywordProxyRenderer() {
        super(new String[] { "id" });
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
        return object.getId() + " (" + object.getId() + ")";
    }
}
