package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.SimpleSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class SimpleSearchCriteriaProxyRenderer extends ProxyRenderer<SimpleSearchCriteriaProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.SimpleSearchCriteriaProxyRenderer INSTANCE;

    protected SimpleSearchCriteriaProxyRenderer() {
        super(new String[] { "name" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.SimpleSearchCriteriaProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new SimpleSearchCriteriaProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(SimpleSearchCriteriaProxy object) {
        if (object == null) {
            return "";
        }
        return object.getName() + " (" + object.getId() + ")";
    }
}
