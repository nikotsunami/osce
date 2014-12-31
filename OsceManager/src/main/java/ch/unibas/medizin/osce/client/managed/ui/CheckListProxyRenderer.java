package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.CheckListProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import java.util.List;

public class CheckListProxyRenderer extends ProxyRenderer<CheckListProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.CheckListProxyRenderer INSTANCE;

    protected CheckListProxyRenderer() {
        super(new String[] { "title" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.CheckListProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new CheckListProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(CheckListProxy object) {
        if (object == null) {
            return "";
        }
        return object.getTitle() + " (" + object.getId() + ")";
    }
}
