package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
import ch.unibas.medizin.osce.client.managed.request.ClinicRequest;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.ui.ClinicDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.ClinicEditView;
import ch.unibas.medizin.osce.client.managed.ui.ClinicListView;
import ch.unibas.medizin.osce.client.managed.ui.ClinicMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.ClinicMobileEditView;
import ch.unibas.medizin.osce.client.managed.ui.DoctorSetEditor;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import java.util.Set;

public class ClinicActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public ClinicActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new ClinicDetailsActivity((EntityProxyId<ClinicProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? ClinicMobileDetailsView.instance() : ClinicDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.ClinicProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<ClinicProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        ClinicEditView.instance().setCreating(true);
        final ClinicRequest request = requests.clinicRequest();
        Activity activity = new CreateAndEditProxy<ClinicProxy>(ClinicProxy.class, request, ScaffoldApp.isMobile() ? ClinicMobileEditView.instance() : ClinicEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(ClinicProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new ClinicEditActivityWrapper(requests, ScaffoldApp.isMobile() ? ClinicMobileEditView.instance() : ClinicEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        ClinicEditView.instance().setCreating(false);
        EntityProxyId<ClinicProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<ClinicProxy>(proxyId, requests, ScaffoldApp.isMobile() ? ClinicMobileEditView.instance() : ClinicEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(ClinicProxy proxy) {
                ClinicRequest request = requests.clinicRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new ClinicEditActivityWrapper(requests, ScaffoldApp.isMobile() ? ClinicMobileEditView.instance() : ClinicEditView.instance(), activity, proxyId);
    }
}
