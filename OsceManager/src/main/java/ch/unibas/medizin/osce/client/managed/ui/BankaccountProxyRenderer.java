package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.BankaccountProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;

public class BankaccountProxyRenderer extends ProxyRenderer<BankaccountProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.BankaccountProxyRenderer INSTANCE;

    protected BankaccountProxyRenderer() {
        super(new String[] { "bankName" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.BankaccountProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new BankaccountProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(BankaccountProxy object) {
        if (object == null) {
            return "";
        }
        return object.getBankName() + " (" + object.getId() + ")";
    }
}
