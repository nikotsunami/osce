package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.AdministratorProxy;
import ch.unibas.medizin.osce.client.managed.request.AdministratorRequest;
import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.ui.AdministratorDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.AdministratorEditView;
import ch.unibas.medizin.osce.client.managed.ui.AdministratorListView;
import ch.unibas.medizin.osce.client.managed.ui.AdministratorMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.AdministratorMobileEditView;
import ch.unibas.medizin.osce.client.managed.ui.SemesterSetEditor;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import java.util.Set;

public class AdministratorActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public AdministratorActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new AdministratorDetailsActivity((EntityProxyId<AdministratorProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? AdministratorMobileDetailsView.instance() : AdministratorDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.AdministratorProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<AdministratorProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        AdministratorEditView.instance().setCreating(true);
        final AdministratorRequest request = requests.administratorRequest();
        Activity activity = new CreateAndEditProxy<AdministratorProxy>(AdministratorProxy.class, request, ScaffoldApp.isMobile() ? AdministratorMobileEditView.instance() : AdministratorEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(AdministratorProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new AdministratorEditActivityWrapper(requests, ScaffoldApp.isMobile() ? AdministratorMobileEditView.instance() : AdministratorEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        AdministratorEditView.instance().setCreating(false);
        EntityProxyId<AdministratorProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<AdministratorProxy>(proxyId, requests, ScaffoldApp.isMobile() ? AdministratorMobileEditView.instance() : AdministratorEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(AdministratorProxy proxy) {
                AdministratorRequest request = requests.administratorRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new AdministratorEditActivityWrapper(requests, ScaffoldApp.isMobile() ? AdministratorMobileEditView.instance() : AdministratorEditView.instance(), activity, proxyId);
    }
}
