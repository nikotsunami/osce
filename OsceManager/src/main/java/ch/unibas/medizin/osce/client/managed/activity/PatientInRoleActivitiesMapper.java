package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.PatientInRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInRoleRequest;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.managed.ui.PatientInRoleDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.PatientInRoleEditView;
import ch.unibas.medizin.osce.client.managed.ui.PatientInRoleListView;
import ch.unibas.medizin.osce.client.managed.ui.PatientInRoleMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.PatientInRoleMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class PatientInRoleActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public PatientInRoleActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new PatientInRoleDetailsActivity((EntityProxyId<PatientInRoleProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? PatientInRoleMobileDetailsView.instance() : PatientInRoleDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.PatientInRoleProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<PatientInRoleProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        PatientInRoleEditView.instance().setCreating(true);
        final PatientInRoleRequest request = requests.patientInRoleRequest();
        Activity activity = new CreateAndEditProxy<PatientInRoleProxy>(PatientInRoleProxy.class, request, ScaffoldApp.isMobile() ? PatientInRoleMobileEditView.instance() : PatientInRoleEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(PatientInRoleProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new PatientInRoleEditActivityWrapper(requests, ScaffoldApp.isMobile() ? PatientInRoleMobileEditView.instance() : PatientInRoleEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        PatientInRoleEditView.instance().setCreating(false);
        EntityProxyId<PatientInRoleProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<PatientInRoleProxy>(proxyId, requests, ScaffoldApp.isMobile() ? PatientInRoleMobileEditView.instance() : PatientInRoleEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(PatientInRoleProxy proxy) {
                PatientInRoleRequest request = requests.patientInRoleRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new PatientInRoleEditActivityWrapper(requests, ScaffoldApp.isMobile() ? PatientInRoleMobileEditView.instance() : PatientInRoleEditView.instance(), activity, proxyId);
    }
}
