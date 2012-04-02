package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.MediaContentTypeProxy;
import ch.unibas.medizin.osce.client.managed.request.MediaContentTypeRequest;
import ch.unibas.medizin.osce.client.managed.ui.MediaContentTypeDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.MediaContentTypeEditView;
import ch.unibas.medizin.osce.client.managed.ui.MediaContentTypeListView;
import ch.unibas.medizin.osce.client.managed.ui.MediaContentTypeMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.MediaContentTypeMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class MediaContentTypeActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public MediaContentTypeActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new MediaContentTypeDetailsActivity((EntityProxyId<MediaContentTypeProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? MediaContentTypeMobileDetailsView.instance() : MediaContentTypeDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.MediaContentTypeProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<MediaContentTypeProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        MediaContentTypeEditView.instance().setCreating(true);
        final MediaContentTypeRequest request = requests.mediaContentTypeRequest();
        Activity activity = new CreateAndEditProxy<MediaContentTypeProxy>(MediaContentTypeProxy.class, request, ScaffoldApp.isMobile() ? MediaContentTypeMobileEditView.instance() : MediaContentTypeEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(MediaContentTypeProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new MediaContentTypeEditActivityWrapper(requests, ScaffoldApp.isMobile() ? MediaContentTypeMobileEditView.instance() : MediaContentTypeEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        MediaContentTypeEditView.instance().setCreating(false);
        EntityProxyId<MediaContentTypeProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<MediaContentTypeProxy>(proxyId, requests, ScaffoldApp.isMobile() ? MediaContentTypeMobileEditView.instance() : MediaContentTypeEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(MediaContentTypeProxy proxy) {
                MediaContentTypeRequest request = requests.mediaContentTypeRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new MediaContentTypeEditActivityWrapper(requests, ScaffoldApp.isMobile() ? MediaContentTypeMobileEditView.instance() : MediaContentTypeEditView.instance(), activity, proxyId);
    }
}
