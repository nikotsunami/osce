package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.CheckListProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;
import java.util.List;

public class ChecklistTopicProxyRenderer extends ProxyRenderer<ChecklistTopicProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.ChecklistTopicProxyRenderer INSTANCE;

    protected ChecklistTopicProxyRenderer() {
        super(new String[] { "title" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.ChecklistTopicProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new ChecklistTopicProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(ChecklistTopicProxy object) {
        if (object == null) {
            return "";
        }
        return object.getTitle() + " (" + object.getId() + ")";
    }
}
