package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomRequest;
import ch.unibas.medizin.osce.client.managed.request.RoomProxy;
import ch.unibas.medizin.osce.client.managed.ui.AssignmentSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.OscePostRoomDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.OscePostRoomEditView;
import ch.unibas.medizin.osce.client.managed.ui.OscePostRoomListView;
import ch.unibas.medizin.osce.client.managed.ui.OscePostRoomMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.OscePostRoomMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import java.util.Set;

public class OscePostRoomActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public OscePostRoomActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new OscePostRoomDetailsActivity((EntityProxyId<OscePostRoomProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? OscePostRoomMobileDetailsView.instance() : OscePostRoomDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<OscePostRoomProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        OscePostRoomEditView.instance().setCreating(true);
        final OscePostRoomRequest request = requests.oscePostRoomRequest();
        Activity activity = new CreateAndEditProxy<OscePostRoomProxy>(OscePostRoomProxy.class, request, ScaffoldApp.isMobile() ? OscePostRoomMobileEditView.instance() : OscePostRoomEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(OscePostRoomProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new OscePostRoomEditActivityWrapper(requests, ScaffoldApp.isMobile() ? OscePostRoomMobileEditView.instance() : OscePostRoomEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        OscePostRoomEditView.instance().setCreating(false);
        EntityProxyId<OscePostRoomProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<OscePostRoomProxy>(proxyId, requests, ScaffoldApp.isMobile() ? OscePostRoomMobileEditView.instance() : OscePostRoomEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(OscePostRoomProxy proxy) {
                OscePostRoomRequest request = requests.oscePostRoomRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new OscePostRoomEditActivityWrapper(requests, ScaffoldApp.isMobile() ? OscePostRoomMobileEditView.instance() : OscePostRoomEditView.instance(), activity, proxyId);
    }
}
