package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.OfficeProxy;
import ch.unibas.medizin.osce.client.managed.request.PostAnalysisProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleParticipantProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.shared.Gender;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import java.util.List;
import java.util.Set;

public class DoctorProxyRenderer extends ProxyRenderer<DoctorProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.DoctorProxyRenderer INSTANCE;

    protected DoctorProxyRenderer() {
        super(new String[] { "title" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.DoctorProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new DoctorProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(DoctorProxy object) {
        if (object == null) {
            return "";
        }
        return object.getTitle() + " (" + object.getId() + ")";
    }
}
