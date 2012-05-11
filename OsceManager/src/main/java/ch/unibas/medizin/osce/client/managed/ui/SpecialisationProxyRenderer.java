package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;

public class SpecialisationProxyRenderer extends ProxyRenderer<SpecialisationProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.SpecialisationProxyRenderer INSTANCE;
//change by spec
    public SpecialisationProxyRenderer() {
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
        return object.getName() ;
    }
}
