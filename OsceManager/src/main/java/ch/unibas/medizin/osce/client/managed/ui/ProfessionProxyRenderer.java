package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.ProfessionProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import java.util.Set;

public class ProfessionProxyRenderer extends ProxyRenderer<ProfessionProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.ProfessionProxyRenderer INSTANCE;

    protected ProfessionProxyRenderer() {
        super(new String[] { "profession" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.ProfessionProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new ProfessionProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(ProfessionProxy object) {
        if (object == null) {
            return "";
        }
        return object.getProfession() + " (" + object.getId() + ")";
    }
}
