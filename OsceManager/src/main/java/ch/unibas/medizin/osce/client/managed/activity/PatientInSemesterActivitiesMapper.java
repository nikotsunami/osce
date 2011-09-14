package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterRequest;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.ui.PatientInSemesterDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.PatientInSemesterEditView;
import ch.unibas.medizin.osce.client.managed.ui.PatientInSemesterListView;
import ch.unibas.medizin.osce.client.managed.ui.PatientInSemesterMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.PatientInSemesterMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class PatientInSemesterActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public PatientInSemesterActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new PatientInSemesterDetailsActivity((EntityProxyId<PatientInSemesterProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? PatientInSemesterMobileDetailsView.instance() : PatientInSemesterDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<PatientInSemesterProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        PatientInSemesterEditView.instance().setCreating(true);
        final PatientInSemesterRequest request = requests.patientInSemesterRequest();
        Activity activity = new CreateAndEditProxy<PatientInSemesterProxy>(PatientInSemesterProxy.class, request, ScaffoldApp.isMobile() ? PatientInSemesterMobileEditView.instance() : PatientInSemesterEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(PatientInSemesterProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new PatientInSemesterEditActivityWrapper(requests, ScaffoldApp.isMobile() ? PatientInSemesterMobileEditView.instance() : PatientInSemesterEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        PatientInSemesterEditView.instance().setCreating(false);
        EntityProxyId<PatientInSemesterProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<PatientInSemesterProxy>(proxyId, requests, ScaffoldApp.isMobile() ? PatientInSemesterMobileEditView.instance() : PatientInSemesterEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(PatientInSemesterProxy proxy) {
                PatientInSemesterRequest request = requests.patientInSemesterRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new PatientInSemesterEditActivityWrapper(requests, ScaffoldApp.isMobile() ? PatientInSemesterMobileEditView.instance() : PatientInSemesterEditView.instance(), activity, proxyId);
    }
}
