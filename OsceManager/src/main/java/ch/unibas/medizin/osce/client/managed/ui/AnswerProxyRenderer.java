package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.AnswerProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistOptionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import java.util.Set;

public class AnswerProxyRenderer extends ProxyRenderer<AnswerProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.AnswerProxyRenderer INSTANCE;

    protected AnswerProxyRenderer() {
        super(new String[] { "answer" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.AnswerProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new AnswerProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(AnswerProxy object) {
        if (object == null) {
            return "";
        }
        return object.getAnswer() + " (" + object.getId() + ")";
    }
}
