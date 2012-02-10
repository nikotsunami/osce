package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.StudentOscesProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentOscesRequest;
import ch.unibas.medizin.osce.client.managed.ui.StudentOscesDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.StudentOscesEditView;
import ch.unibas.medizin.osce.client.managed.ui.StudentOscesListView;
import ch.unibas.medizin.osce.client.managed.ui.StudentOscesMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.StudentOscesMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class StudentOscesActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public StudentOscesActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new StudentOscesDetailsActivity((EntityProxyId<StudentOscesProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? StudentOscesMobileDetailsView.instance() : StudentOscesDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.StudentOscesProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<StudentOscesProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        StudentOscesEditView.instance().setCreating(true);
        final StudentOscesRequest request = requests.studentOscesRequest();
        Activity activity = new CreateAndEditProxy<StudentOscesProxy>(StudentOscesProxy.class, request, ScaffoldApp.isMobile() ? StudentOscesMobileEditView.instance() : StudentOscesEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(StudentOscesProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new StudentOscesEditActivityWrapper(requests, ScaffoldApp.isMobile() ? StudentOscesMobileEditView.instance() : StudentOscesEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        StudentOscesEditView.instance().setCreating(false);
        EntityProxyId<StudentOscesProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<StudentOscesProxy>(proxyId, requests, ScaffoldApp.isMobile() ? StudentOscesMobileEditView.instance() : StudentOscesEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(StudentOscesProxy proxy) {
                StudentOscesRequest request = requests.studentOscesRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new StudentOscesEditActivityWrapper(requests, ScaffoldApp.isMobile() ? StudentOscesMobileEditView.instance() : StudentOscesEditView.instance(), activity, proxyId);
    }
}
