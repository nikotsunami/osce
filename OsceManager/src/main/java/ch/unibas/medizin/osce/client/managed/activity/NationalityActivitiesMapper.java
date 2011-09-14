package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.managed.request.NationalityRequest;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.ui.NationalityDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.NationalityEditView;
import ch.unibas.medizin.osce.client.managed.ui.NationalityListView;
import ch.unibas.medizin.osce.client.managed.ui.NationalityMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.NationalityMobileEditView;
import ch.unibas.medizin.osce.client.managed.ui.StandardizedPatientSetEditor;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import java.util.Set;

public class NationalityActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public NationalityActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new NationalityDetailsActivity((EntityProxyId<NationalityProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? NationalityMobileDetailsView.instance() : NationalityDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.NationalityProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<NationalityProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        NationalityEditView.instance().setCreating(true);
        final NationalityRequest request = requests.nationalityRequest();
        Activity activity = new CreateAndEditProxy<NationalityProxy>(NationalityProxy.class, request, ScaffoldApp.isMobile() ? NationalityMobileEditView.instance() : NationalityEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(NationalityProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new NationalityEditActivityWrapper(requests, ScaffoldApp.isMobile() ? NationalityMobileEditView.instance() : NationalityEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        NationalityEditView.instance().setCreating(false);
        EntityProxyId<NationalityProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<NationalityProxy>(proxyId, requests, ScaffoldApp.isMobile() ? NationalityMobileEditView.instance() : NationalityEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(NationalityProxy proxy) {
                NationalityRequest request = requests.nationalityRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new NationalityEditActivityWrapper(requests, ScaffoldApp.isMobile() ? NationalityMobileEditView.instance() : NationalityEditView.instance(), activity, proxyId);
    }
}
