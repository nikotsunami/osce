package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.MediaContentProxy;
import ch.unibas.medizin.osce.client.managed.request.MediaContentRequest;
import ch.unibas.medizin.osce.client.managed.ui.MediaContentDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.MediaContentEditView;
import ch.unibas.medizin.osce.client.managed.ui.MediaContentListView;
import ch.unibas.medizin.osce.client.managed.ui.MediaContentMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.MediaContentMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class MediaContentActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public MediaContentActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new MediaContentDetailsActivity((EntityProxyId<MediaContentProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? MediaContentMobileDetailsView.instance() : MediaContentDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.MediaContentProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<MediaContentProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        MediaContentEditView.instance().setCreating(true);
        final MediaContentRequest request = requests.mediaContentRequest();
        Activity activity = new CreateAndEditProxy<MediaContentProxy>(MediaContentProxy.class, request, ScaffoldApp.isMobile() ? MediaContentMobileEditView.instance() : MediaContentEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(MediaContentProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new MediaContentEditActivityWrapper(requests, ScaffoldApp.isMobile() ? MediaContentMobileEditView.instance() : MediaContentEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        MediaContentEditView.instance().setCreating(false);
        EntityProxyId<MediaContentProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<MediaContentProxy>(proxyId, requests, ScaffoldApp.isMobile() ? MediaContentMobileEditView.instance() : MediaContentEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(MediaContentProxy proxy) {
                MediaContentRequest request = requests.mediaContentRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new MediaContentEditActivityWrapper(requests, ScaffoldApp.isMobile() ? MediaContentMobileEditView.instance() : MediaContentEditView.instance(), activity, proxyId);
    }
}
