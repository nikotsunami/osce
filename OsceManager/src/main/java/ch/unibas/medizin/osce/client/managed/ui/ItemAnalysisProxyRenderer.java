package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.request.ItemAnalysisProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class ItemAnalysisProxyRenderer extends ProxyRenderer<ItemAnalysisProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.ItemAnalysisProxyRenderer INSTANCE;

    protected ItemAnalysisProxyRenderer() {
        super(new String[] { "points" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.ItemAnalysisProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new ItemAnalysisProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(ItemAnalysisProxy object) {
        if (object == null) {
            return "";
        }
        return object.getPoints() + " (" + object.getId() + ")";
    }
}
