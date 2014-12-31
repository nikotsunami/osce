package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.ChecklistOptionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class ChecklistOptionProxyRenderer extends ProxyRenderer<ChecklistOptionProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.ChecklistOptionProxyRenderer INSTANCE;

    protected ChecklistOptionProxyRenderer() {
        super(new String[] { "optionName" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.ChecklistOptionProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new ChecklistOptionProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(ChecklistOptionProxy object) {
        if (object == null) {
            return "";
        }
        return object.getOptionName() + " (" + object.getId() + ")";
    }
}
