package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.RoomProxy;
import ch.unibas.medizin.osce.client.managed.request.RoomRequest;
import ch.unibas.medizin.osce.client.managed.ui.OscePostRoomSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.RoomDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.RoomEditView;
import ch.unibas.medizin.osce.client.managed.ui.RoomListView;
import ch.unibas.medizin.osce.client.managed.ui.RoomMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.RoomMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import java.util.Set;

public class RoomActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public RoomActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new RoomDetailsActivity((EntityProxyId<RoomProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? RoomMobileDetailsView.instance() : RoomDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.RoomProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<RoomProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        RoomEditView.instance().setCreating(true);
        final RoomRequest request = requests.roomRequest();
        Activity activity = new CreateAndEditProxy<RoomProxy>(RoomProxy.class, request, ScaffoldApp.isMobile() ? RoomMobileEditView.instance() : RoomEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(RoomProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new RoomEditActivityWrapper(requests, ScaffoldApp.isMobile() ? RoomMobileEditView.instance() : RoomEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        RoomEditView.instance().setCreating(false);
        EntityProxyId<RoomProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<RoomProxy>(proxyId, requests, ScaffoldApp.isMobile() ? RoomMobileEditView.instance() : RoomEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(RoomProxy proxy) {
                RoomRequest request = requests.roomRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new RoomEditActivityWrapper(requests, ScaffoldApp.isMobile() ? RoomMobileEditView.instance() : RoomEditView.instance(), activity, proxyId);
    }
}
