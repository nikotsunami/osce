package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.AdministratorProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.TaskProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class TaskProxyRenderer extends ProxyRenderer<TaskProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.TaskProxyRenderer INSTANCE;

    protected TaskProxyRenderer() {
        super(new String[] { "name" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.TaskProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new TaskProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(TaskProxy object) {
        if (object == null) {
            return "";
        }
        return object.getName() + " (" + object.getId() + ")";
    }
}
