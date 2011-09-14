package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceRequest;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentProxy;
import ch.unibas.medizin.osce.client.managed.ui.CourseSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.OsceDaySetEditor;
import ch.unibas.medizin.osce.client.managed.ui.OsceDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.OsceEditView;
import ch.unibas.medizin.osce.client.managed.ui.OsceListView;
import ch.unibas.medizin.osce.client.managed.ui.OsceMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.OsceMobileEditView;
import ch.unibas.medizin.osce.client.managed.ui.StudentSetEditor;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import ch.unibas.medizin.osce.shared.StudyYears;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import java.util.Set;

public class OsceActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public OsceActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new OsceDetailsActivity((EntityProxyId<OsceProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? OsceMobileDetailsView.instance() : OsceDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.OsceProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<OsceProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        OsceEditView.instance().setCreating(true);
        final OsceRequest request = requests.osceRequest();
        Activity activity = new CreateAndEditProxy<OsceProxy>(OsceProxy.class, request, ScaffoldApp.isMobile() ? OsceMobileEditView.instance() : OsceEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(OsceProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new OsceEditActivityWrapper(requests, ScaffoldApp.isMobile() ? OsceMobileEditView.instance() : OsceEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        OsceEditView.instance().setCreating(false);
        EntityProxyId<OsceProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<OsceProxy>(proxyId, requests, ScaffoldApp.isMobile() ? OsceMobileEditView.instance() : OsceEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(OsceProxy proxy) {
                OsceRequest request = requests.osceRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new OsceEditActivityWrapper(requests, ScaffoldApp.isMobile() ? OsceMobileEditView.instance() : OsceEditView.instance(), activity, proxyId);
    }
}
