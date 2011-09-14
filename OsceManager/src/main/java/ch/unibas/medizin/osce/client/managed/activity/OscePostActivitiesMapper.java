package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRequest;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.ui.OscePostDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.OscePostEditView;
import ch.unibas.medizin.osce.client.managed.ui.OscePostListView;
import ch.unibas.medizin.osce.client.managed.ui.OscePostMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.OscePostMobileEditView;
import ch.unibas.medizin.osce.client.managed.ui.OscePostRoomSetEditor;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import java.util.Set;

public class OscePostActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public OscePostActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new OscePostDetailsActivity((EntityProxyId<OscePostProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? OscePostMobileDetailsView.instance() : OscePostDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.OscePostProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<OscePostProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        OscePostEditView.instance().setCreating(true);
        final OscePostRequest request = requests.oscePostRequest();
        Activity activity = new CreateAndEditProxy<OscePostProxy>(OscePostProxy.class, request, ScaffoldApp.isMobile() ? OscePostMobileEditView.instance() : OscePostEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(OscePostProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new OscePostEditActivityWrapper(requests, ScaffoldApp.isMobile() ? OscePostMobileEditView.instance() : OscePostEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        OscePostEditView.instance().setCreating(false);
        EntityProxyId<OscePostProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<OscePostProxy>(proxyId, requests, ScaffoldApp.isMobile() ? OscePostMobileEditView.instance() : OscePostEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(OscePostProxy proxy) {
                OscePostRequest request = requests.oscePostRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new OscePostEditActivityWrapper(requests, ScaffoldApp.isMobile() ? OscePostMobileEditView.instance() : OscePostEditView.instance(), activity, proxyId);
    }
}
