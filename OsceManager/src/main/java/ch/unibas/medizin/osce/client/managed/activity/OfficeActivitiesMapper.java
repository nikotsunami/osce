package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.OfficeProxy;
import ch.unibas.medizin.osce.client.managed.request.OfficeRequest;
import ch.unibas.medizin.osce.client.managed.ui.OfficeDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.OfficeEditView;
import ch.unibas.medizin.osce.client.managed.ui.OfficeListView;
import ch.unibas.medizin.osce.client.managed.ui.OfficeMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.OfficeMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import ch.unibas.medizin.osce.shared.Gender;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class OfficeActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public OfficeActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new OfficeDetailsActivity((EntityProxyId<OfficeProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? OfficeMobileDetailsView.instance() : OfficeDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.OfficeProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<OfficeProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        OfficeEditView.instance().setCreating(true);
        final OfficeRequest request = requests.officeRequest();
        Activity activity = new CreateAndEditProxy<OfficeProxy>(OfficeProxy.class, request, ScaffoldApp.isMobile() ? OfficeMobileEditView.instance() : OfficeEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(OfficeProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new OfficeEditActivityWrapper(requests, ScaffoldApp.isMobile() ? OfficeMobileEditView.instance() : OfficeEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        OfficeEditView.instance().setCreating(false);
        EntityProxyId<OfficeProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<OfficeProxy>(proxyId, requests, ScaffoldApp.isMobile() ? OfficeMobileEditView.instance() : OfficeEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(OfficeProxy proxy) {
                OfficeRequest request = requests.officeRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new OfficeEditActivityWrapper(requests, ScaffoldApp.isMobile() ? OfficeMobileEditView.instance() : OfficeEditView.instance(), activity, proxyId);
    }
}
