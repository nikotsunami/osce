package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.shared.StudyYears;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import java.util.List;
import java.util.Set;

public class RoleTopicProxyRenderer extends ProxyRenderer<RoleTopicProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.RoleTopicProxyRenderer INSTANCE;

    protected RoleTopicProxyRenderer() {
        super(new String[] { "name" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.RoleTopicProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new RoleTopicProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(RoleTopicProxy object) {
        if (object == null) {
            return "";
        }
        return object.getName() + " (" + object.getId() + ")";
    }
}
