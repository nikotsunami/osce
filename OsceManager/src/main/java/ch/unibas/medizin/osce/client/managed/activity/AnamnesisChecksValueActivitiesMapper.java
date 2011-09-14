package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueRequest;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisChecksValueDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisChecksValueEditView;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisChecksValueListView;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisChecksValueMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisChecksValueMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class AnamnesisChecksValueActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public AnamnesisChecksValueActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new AnamnesisChecksValueDetailsActivity((EntityProxyId<AnamnesisChecksValueProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? AnamnesisChecksValueMobileDetailsView.instance() : AnamnesisChecksValueDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<AnamnesisChecksValueProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        AnamnesisChecksValueEditView.instance().setCreating(true);
        final AnamnesisChecksValueRequest request = requests.anamnesisChecksValueRequest();
        Activity activity = new CreateAndEditProxy<AnamnesisChecksValueProxy>(AnamnesisChecksValueProxy.class, request, ScaffoldApp.isMobile() ? AnamnesisChecksValueMobileEditView.instance() : AnamnesisChecksValueEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(AnamnesisChecksValueProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new AnamnesisChecksValueEditActivityWrapper(requests, ScaffoldApp.isMobile() ? AnamnesisChecksValueMobileEditView.instance() : AnamnesisChecksValueEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        AnamnesisChecksValueEditView.instance().setCreating(false);
        EntityProxyId<AnamnesisChecksValueProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<AnamnesisChecksValueProxy>(proxyId, requests, ScaffoldApp.isMobile() ? AnamnesisChecksValueMobileEditView.instance() : AnamnesisChecksValueEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(AnamnesisChecksValueProxy proxy) {
                AnamnesisChecksValueRequest request = requests.anamnesisChecksValueRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new AnamnesisChecksValueEditActivityWrapper(requests, ScaffoldApp.isMobile() ? AnamnesisChecksValueMobileEditView.instance() : AnamnesisChecksValueEditView.instance(), activity, proxyId);
    }
}
