package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;
import java.util.Set;

public class AdvancedSearchCriteriaProxyRenderer extends ProxyRenderer<AdvancedSearchCriteriaProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.AdvancedSearchCriteriaProxyRenderer INSTANCE;

    protected AdvancedSearchCriteriaProxyRenderer() {
        super(new String[] { "email" });
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
