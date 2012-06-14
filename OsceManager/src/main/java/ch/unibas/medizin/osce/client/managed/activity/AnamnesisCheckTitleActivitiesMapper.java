package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleRequest;
import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisCheckTitleDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisCheckTitleEditView;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisCheckTitleListView;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisCheckTitleMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisCheckTitleMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class AnamnesisCheckTitleActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public AnamnesisCheckTitleActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new AnamnesisCheckTitleDetailsActivity((EntityProxyId<AnamnesisCheckTitleProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? AnamnesisCheckTitleMobileDetailsView.instance() : AnamnesisCheckTitleDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<AnamnesisCheckTitleProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        AnamnesisCheckTitleEditView.instance().setCreating(true);
        final AnamnesisCheckTitleRequest request = requests.anamnesisCheckTitleRequest();
        Activity activity = new CreateAndEditProxy<AnamnesisCheckTitleProxy>(AnamnesisCheckTitleProxy.class, request, ScaffoldApp.isMobile() ? AnamnesisCheckTitleMobileEditView.instance() : AnamnesisCheckTitleEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(AnamnesisCheckTitleProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new AnamnesisCheckTitleEditActivityWrapper(requests, ScaffoldApp.isMobile() ? AnamnesisCheckTitleMobileEditView.instance() : AnamnesisCheckTitleEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        AnamnesisCheckTitleEditView.instance().setCreating(false);
        EntityProxyId<AnamnesisCheckTitleProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<AnamnesisCheckTitleProxy>(proxyId, requests, ScaffoldApp.isMobile() ? AnamnesisCheckTitleMobileEditView.instance() : AnamnesisCheckTitleEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(AnamnesisCheckTitleProxy proxy) {
                AnamnesisCheckTitleRequest request = requests.anamnesisCheckTitleRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new AnamnesisCheckTitleEditActivityWrapper(requests, ScaffoldApp.isMobile() ? AnamnesisCheckTitleMobileEditView.instance() : AnamnesisCheckTitleEditView.instance(), activity, proxyId);
    }
}
