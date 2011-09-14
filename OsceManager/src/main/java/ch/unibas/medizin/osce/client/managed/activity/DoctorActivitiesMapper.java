package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorRequest;
import ch.unibas.medizin.osce.client.managed.request.OfficeProxy;
import ch.unibas.medizin.osce.client.managed.ui.DoctorDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.DoctorEditView;
import ch.unibas.medizin.osce.client.managed.ui.DoctorListView;
import ch.unibas.medizin.osce.client.managed.ui.DoctorMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.DoctorMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import ch.unibas.medizin.osce.shared.Gender;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class DoctorActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public DoctorActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new DoctorDetailsActivity((EntityProxyId<DoctorProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? DoctorMobileDetailsView.instance() : DoctorDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.DoctorProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<DoctorProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        DoctorEditView.instance().setCreating(true);
        final DoctorRequest request = requests.doctorRequest();
        Activity activity = new CreateAndEditProxy<DoctorProxy>(DoctorProxy.class, request, ScaffoldApp.isMobile() ? DoctorMobileEditView.instance() : DoctorEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(DoctorProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new DoctorEditActivityWrapper(requests, ScaffoldApp.isMobile() ? DoctorMobileEditView.instance() : DoctorEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        DoctorEditView.instance().setCreating(false);
        EntityProxyId<DoctorProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<DoctorProxy>(proxyId, requests, ScaffoldApp.isMobile() ? DoctorMobileEditView.instance() : DoctorEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(DoctorProxy proxy) {
                DoctorRequest request = requests.doctorRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new DoctorEditActivityWrapper(requests, ScaffoldApp.isMobile() ? DoctorMobileEditView.instance() : DoctorEditView.instance(), activity, proxyId);
    }
}
