package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentRequest;
import ch.unibas.medizin.osce.client.managed.ui.OsceSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.StudentDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.StudentEditView;
import ch.unibas.medizin.osce.client.managed.ui.StudentListView;
import ch.unibas.medizin.osce.client.managed.ui.StudentMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.StudentMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import ch.unibas.medizin.osce.shared.Gender;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import java.util.Set;

public class StudentActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public StudentActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new StudentDetailsActivity((EntityProxyId<StudentProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? StudentMobileDetailsView.instance() : StudentDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.StudentProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<StudentProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        StudentEditView.instance().setCreating(true);
        final StudentRequest request = requests.studentRequest();
        Activity activity = new CreateAndEditProxy<StudentProxy>(StudentProxy.class, request, ScaffoldApp.isMobile() ? StudentMobileEditView.instance() : StudentEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(StudentProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new StudentEditActivityWrapper(requests, ScaffoldApp.isMobile() ? StudentMobileEditView.instance() : StudentEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        StudentEditView.instance().setCreating(false);
        EntityProxyId<StudentProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<StudentProxy>(proxyId, requests, ScaffoldApp.isMobile() ? StudentMobileEditView.instance() : StudentEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(StudentProxy proxy) {
                StudentRequest request = requests.studentRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new StudentEditActivityWrapper(requests, ScaffoldApp.isMobile() ? StudentMobileEditView.instance() : StudentEditView.instance(), activity, proxyId);
    }
}
