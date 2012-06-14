package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.shared.PostType;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;
import java.util.Set;

public class OscePostBlueprintProxyRenderer extends ProxyRenderer<OscePostBlueprintProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.OscePostBlueprintProxyRenderer INSTANCE;

    protected OscePostBlueprintProxyRenderer() {
        super(new String[] { "id" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.OscePostBlueprintProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new OscePostBlueprintProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(OscePostBlueprintProxy object) {
        if (object == null) {
            return "";
        }
        return object.getId() + " (" + object.getId() + ")";
    }
}
