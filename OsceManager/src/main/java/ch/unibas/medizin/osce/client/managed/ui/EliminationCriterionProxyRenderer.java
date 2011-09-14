package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.EliminationCriterionProxy;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;

public class EliminationCriterionProxyRenderer extends ProxyRenderer<EliminationCriterionProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.EliminationCriterionProxyRenderer INSTANCE;

    protected EliminationCriterionProxyRenderer() {
        super(new String[] { "id" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.EliminationCriterionProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new EliminationCriterionProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(EliminationCriterionProxy object) {
        if (object == null) {
            return "";
        }
        return object.getId() + " (" + object.getId() + ")";
    }
}
