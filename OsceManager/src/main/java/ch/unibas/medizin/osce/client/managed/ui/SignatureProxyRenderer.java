package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.SignatureProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class SignatureProxyRenderer extends ProxyRenderer<SignatureProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.SignatureProxyRenderer INSTANCE;

    protected SignatureProxyRenderer() {
        super(new String[] { "id" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.SignatureProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new SignatureProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(SignatureProxy object) {
        if (object == null) {
            return "";
        }
        return object.getId() + " (" + object.getId() + ")";
    }
}
