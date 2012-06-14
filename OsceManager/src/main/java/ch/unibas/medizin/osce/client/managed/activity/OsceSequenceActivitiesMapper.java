package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceRequest;
import ch.unibas.medizin.osce.client.managed.ui.CourseSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.OscePostSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.OsceSequenceDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.OsceSequenceEditView;
import ch.unibas.medizin.osce.client.managed.ui.OsceSequenceListView;
import ch.unibas.medizin.osce.client.managed.ui.OsceSequenceMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.OsceSequenceMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import java.util.Set;

public class OsceSequenceActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public OsceSequenceActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new OsceSequenceDetailsActivity((EntityProxyId<OsceSequenceProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? OsceSequenceMobileDetailsView.instance() : OsceSequenceDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<OsceSequenceProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        OsceSequenceEditView.instance().setCreating(true);
        final OsceSequenceRequest request = requests.osceSequenceRequest();
        Activity activity = new CreateAndEditProxy<OsceSequenceProxy>(OsceSequenceProxy.class, request, ScaffoldApp.isMobile() ? OsceSequenceMobileEditView.instance() : OsceSequenceEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(OsceSequenceProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new OsceSequenceEditActivityWrapper(requests, ScaffoldApp.isMobile() ? OsceSequenceMobileEditView.instance() : OsceSequenceEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        OsceSequenceEditView.instance().setCreating(false);
        EntityProxyId<OsceSequenceProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<OsceSequenceProxy>(proxyId, requests, ScaffoldApp.isMobile() ? OsceSequenceMobileEditView.instance() : OsceSequenceEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(OsceSequenceProxy proxy) {
                OsceSequenceRequest request = requests.osceSequenceRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new OsceSequenceEditActivityWrapper(requests, ScaffoldApp.isMobile() ? OsceSequenceMobileEditView.instance() : OsceSequenceEditView.instance(), activity, proxyId);
    }
}
