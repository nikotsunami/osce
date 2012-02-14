package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationRequest;
import ch.unibas.medizin.osce.client.managed.ui.SpecialisationDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.SpecialisationEditView;
import ch.unibas.medizin.osce.client.managed.ui.SpecialisationListView;
import ch.unibas.medizin.osce.client.managed.ui.SpecialisationMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.SpecialisationMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class SpecialisationActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public SpecialisationActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new SpecialisationDetailsActivity((EntityProxyId<SpecialisationProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? SpecialisationMobileDetailsView.instance() : SpecialisationDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<SpecialisationProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        SpecialisationEditView.instance().setCreating(true);
        final SpecialisationRequest request = requests.specialisationRequest();
        Activity activity = new CreateAndEditProxy<SpecialisationProxy>(SpecialisationProxy.class, request, ScaffoldApp.isMobile() ? SpecialisationMobileEditView.instance() : SpecialisationEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(SpecialisationProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new SpecialisationEditActivityWrapper(requests, ScaffoldApp.isMobile() ? SpecialisationMobileEditView.instance() : SpecialisationEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        SpecialisationEditView.instance().setCreating(false);
        EntityProxyId<SpecialisationProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<SpecialisationProxy>(proxyId, requests, ScaffoldApp.isMobile() ? SpecialisationMobileEditView.instance() : SpecialisationEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(SpecialisationProxy proxy) {
                SpecialisationRequest request = requests.specialisationRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new SpecialisationEditActivityWrapper(requests, ScaffoldApp.isMobile() ? SpecialisationMobileEditView.instance() : SpecialisationEditView.instance(), activity, proxyId);
    }
}
