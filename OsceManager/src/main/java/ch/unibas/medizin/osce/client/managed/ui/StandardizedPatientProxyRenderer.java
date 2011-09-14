package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.BankaccountProxy;
import ch.unibas.medizin.osce.client.managed.request.DescriptionProxy;
import ch.unibas.medizin.osce.client.managed.request.LangSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.managed.request.ProfessionProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.shared.Gender;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;
import java.util.Set;

public class StandardizedPatientProxyRenderer extends ProxyRenderer<StandardizedPatientProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.StandardizedPatientProxyRenderer INSTANCE;

    protected StandardizedPatientProxyRenderer() {
        super(new String[] { "name" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.StandardizedPatientProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new StandardizedPatientProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(StandardizedPatientProxy object) {
        if (object == null) {
            return "";
        }
        return object.getName() + " (" + object.getId() + ")";
    }
}
