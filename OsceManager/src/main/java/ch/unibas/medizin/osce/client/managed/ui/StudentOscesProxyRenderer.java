package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentOscesProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class StudentOscesProxyRenderer extends ProxyRenderer<StudentOscesProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.StudentOscesProxyRenderer INSTANCE;

    protected StudentOscesProxyRenderer() {
        super(new String[] { "id" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.StudentOscesProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new StudentOscesProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(StudentOscesProxy object) {
        if (object == null) {
            return "";
        }
        return object.getId() + " (" + object.getId() + ")";
    }
}
