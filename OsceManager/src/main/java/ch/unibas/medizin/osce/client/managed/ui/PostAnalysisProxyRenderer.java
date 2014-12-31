package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.PostAnalysisProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class PostAnalysisProxyRenderer extends ProxyRenderer<PostAnalysisProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.PostAnalysisProxyRenderer INSTANCE;

    protected PostAnalysisProxyRenderer() {
        super(new String[] { "id" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.PostAnalysisProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new PostAnalysisProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(PostAnalysisProxy object) {
        if (object == null) {
            return "";
        }
        return object.getId() + " (" + object.getId() + ")";
    }
}
