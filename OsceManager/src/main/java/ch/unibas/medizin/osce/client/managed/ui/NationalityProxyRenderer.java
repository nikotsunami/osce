package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import java.util.Set;

public class NationalityProxyRenderer extends ProxyRenderer<NationalityProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.NationalityProxyRenderer INSTANCE;

    protected NationalityProxyRenderer() {
        super(new String[] { "nationality" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.NationalityProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new NationalityProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(NationalityProxy object) {
        if (object == null) {
            return "";
        }
        return object.getNationality() + " (" + object.getId() + ")";
    }
}
