package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.TrainingProxy;
import ch.unibas.medizin.osce.client.managed.request.TrainingRequest;
import ch.unibas.medizin.osce.client.managed.ui.PatientInSemesterSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.TrainingDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.TrainingEditView;
import ch.unibas.medizin.osce.client.managed.ui.TrainingListView;
import ch.unibas.medizin.osce.client.managed.ui.TrainingMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.TrainingMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import java.util.Set;

public class TrainingActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public TrainingActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new TrainingDetailsActivity((EntityProxyId<TrainingProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? TrainingMobileDetailsView.instance() : TrainingDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.TrainingProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<TrainingProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        TrainingEditView.instance().setCreating(true);
        final TrainingRequest request = requests.trainingRequest();
        Activity activity = new CreateAndEditProxy<TrainingProxy>(TrainingProxy.class, request, ScaffoldApp.isMobile() ? TrainingMobileEditView.instance() : TrainingEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(TrainingProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new TrainingEditActivityWrapper(requests, ScaffoldApp.isMobile() ? TrainingMobileEditView.instance() : TrainingEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        TrainingEditView.instance().setCreating(false);
        EntityProxyId<TrainingProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<TrainingProxy>(proxyId, requests, ScaffoldApp.isMobile() ? TrainingMobileEditView.instance() : TrainingEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(TrainingProxy proxy) {
                TrainingRequest request = requests.trainingRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new TrainingEditActivityWrapper(requests, ScaffoldApp.isMobile() ? TrainingMobileEditView.instance() : TrainingEditView.instance(), activity, proxyId);
    }
}
