package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentProxy;
import ch.unibas.medizin.osce.shared.Gender;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;
import java.util.Set;

public class StudentProxyRenderer extends ProxyRenderer<StudentProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.StudentProxyRenderer INSTANCE;

    protected StudentProxyRenderer() {
        super(new String[] { "name" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.StudentProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new StudentProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(StudentProxy object) {
        if (object == null) {
            return "";
        }
        return object.getName() + " (" + object.getId() + ")";
    }
}
