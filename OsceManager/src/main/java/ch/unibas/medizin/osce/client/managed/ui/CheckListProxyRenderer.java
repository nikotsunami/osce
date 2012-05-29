package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.CheckListProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;

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
