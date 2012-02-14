package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.RoleParticipantProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleParticipantRequest;
import ch.unibas.medizin.osce.client.managed.ui.RoleParticipantDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.RoleParticipantEditView;
import ch.unibas.medizin.osce.client.managed.ui.RoleParticipantListView;
import ch.unibas.medizin.osce.client.managed.ui.RoleParticipantMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.RoleParticipantMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class RoleParticipantActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public RoleParticipantActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new RoleParticipantDetailsActivity((EntityProxyId<RoleParticipantProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? RoleParticipantMobileDetailsView.instance() : RoleParticipantDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.RoleParticipantProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<RoleParticipantProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        RoleParticipantEditView.instance().setCreating(true);
        final RoleParticipantRequest request = requests.roleParticipantRequest();
        Activity activity = new CreateAndEditProxy<RoleParticipantProxy>(RoleParticipantProxy.class, request, ScaffoldApp.isMobile() ? RoleParticipantMobileEditView.instance() : RoleParticipantEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(RoleParticipantProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new RoleParticipantEditActivityWrapper(requests, ScaffoldApp.isMobile() ? RoleParticipantMobileEditView.instance() : RoleParticipantEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        RoleParticipantEditView.instance().setCreating(false);
        EntityProxyId<RoleParticipantProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<RoleParticipantProxy>(proxyId, requests, ScaffoldApp.isMobile() ? RoleParticipantMobileEditView.instance() : RoleParticipantEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(RoleParticipantProxy proxy) {
                RoleParticipantRequest request = requests.roleParticipantRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new RoleParticipantEditActivityWrapper(requests, ScaffoldApp.isMobile() ? RoleParticipantMobileEditView.instance() : RoleParticipantEditView.instance(), activity, proxyId);
    }
}
