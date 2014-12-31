package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison;
import ch.unibas.medizin.osce.shared.PossibleFields;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class AdvancedSearchCriteriaProxyRenderer extends ProxyRenderer<AdvancedSearchCriteriaProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.AdvancedSearchCriteriaProxyRenderer INSTANCE;

    protected AdvancedSearchCriteriaProxyRenderer() {
        super(new String[] { "value" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.AdvancedSearchCriteriaProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new AdvancedSearchCriteriaProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(AdvancedSearchCriteriaProxy object) {
        if (object == null) {
            return "";
        }
        return object.getValue() + " (" + object.getId() + ")";
    }
}
