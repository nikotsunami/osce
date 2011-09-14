package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.client.managed.request.AssignmentRequest;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentProxy;
import ch.unibas.medizin.osce.client.managed.ui.AssignmentDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.AssignmentEditView;
import ch.unibas.medizin.osce.client.managed.ui.AssignmentListView;
import ch.unibas.medizin.osce.client.managed.ui.AssignmentMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.AssignmentMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import ch.unibas.medizin.osce.shared.AssignmentTypes;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class AssignmentActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public AssignmentActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new AssignmentDetailsActivity((EntityProxyId<AssignmentProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? AssignmentMobileDetailsView.instance() : AssignmentDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.AssignmentProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<AssignmentProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        AssignmentEditView.instance().setCreating(true);
        final AssignmentRequest request = requests.assignmentRequest();
        Activity activity = new CreateAndEditProxy<AssignmentProxy>(AssignmentProxy.class, request, ScaffoldApp.isMobile() ? AssignmentMobileEditView.instance() : AssignmentEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(AssignmentProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new AssignmentEditActivityWrapper(requests, ScaffoldApp.isMobile() ? AssignmentMobileEditView.instance() : AssignmentEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        AssignmentEditView.instance().setCreating(false);
        EntityProxyId<AssignmentProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<AssignmentProxy>(proxyId, requests, ScaffoldApp.isMobile() ? AssignmentMobileEditView.instance() : AssignmentEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(AssignmentProxy proxy) {
                AssignmentRequest request = requests.assignmentRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new AssignmentEditActivityWrapper(requests, ScaffoldApp.isMobile() ? AssignmentMobileEditView.instance() : AssignmentEditView.instance(), activity, proxyId);
    }
}
