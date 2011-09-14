package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.CourseRequest;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.ui.CourseDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.CourseEditView;
import ch.unibas.medizin.osce.client.managed.ui.CourseListView;
import ch.unibas.medizin.osce.client.managed.ui.CourseMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.CourseMobileEditView;
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

public class CourseActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public CourseActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new CourseDetailsActivity((EntityProxyId<CourseProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? CourseMobileDetailsView.instance() : CourseDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.CourseProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<CourseProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        CourseEditView.instance().setCreating(true);
        final CourseRequest request = requests.courseRequest();
        Activity activity = new CreateAndEditProxy<CourseProxy>(CourseProxy.class, request, ScaffoldApp.isMobile() ? CourseMobileEditView.instance() : CourseEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(CourseProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new CourseEditActivityWrapper(requests, ScaffoldApp.isMobile() ? CourseMobileEditView.instance() : CourseEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        CourseEditView.instance().setCreating(false);
        EntityProxyId<CourseProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<CourseProxy>(proxyId, requests, ScaffoldApp.isMobile() ? CourseMobileEditView.instance() : CourseEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(CourseProxy proxy) {
                CourseRequest request = requests.courseRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new CourseEditActivityWrapper(requests, ScaffoldApp.isMobile() ? CourseMobileEditView.instance() : CourseEditView.instance(), activity, proxyId);
    }
}
