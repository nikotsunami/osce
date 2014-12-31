package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.ChecklistCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistOptionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.ItemAnalysisProxy;
import ch.unibas.medizin.osce.client.managed.request.PostAnalysisProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import java.util.List;

public class ChecklistQuestionProxyRenderer extends ProxyRenderer<ChecklistQuestionProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.ChecklistQuestionProxyRenderer INSTANCE;

    protected ChecklistQuestionProxyRenderer() {
        super(new String[] { "question" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.ChecklistQuestionProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new ChecklistQuestionProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(ChecklistQuestionProxy object) {
        if (object == null) {
            return "";
        }
        return object.getQuestion() + " (" + object.getId() + ")";
    }
}
