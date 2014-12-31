package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import java.util.Set;

public class SpecialisationProxyRenderer extends ProxyRenderer<SpecialisationProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.SpecialisationProxyRenderer INSTANCE;

    protected SpecialisationProxyRenderer() {
        super(new String[] { "name" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.SpecialisationProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new SpecialisationProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(SpecialisationProxy object) {
        if (object == null) {
            return "";
        }
        return object.getName() + " (" + object.getId() + ")";
    }
}
