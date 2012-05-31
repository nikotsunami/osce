package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.ChecklistCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;

public class ChecklistCriteriaProxyRenderer extends ProxyRenderer<ChecklistCriteriaProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.ChecklistCriteriaProxyRenderer INSTANCE;

    protected ChecklistCriteriaProxyRenderer() {
        super(new String[] { "criteria" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.ChecklistCriteriaProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new ChecklistCriteriaProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(ChecklistCriteriaProxy object) {
        if (object == null) {
            return "";
        }
        return object.getCriteria() + " (" + object.getId() + ")";
    }
}
