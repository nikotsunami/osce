package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckRequest;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueProxy;
import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisCheckDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisCheckEditView;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisCheckListView;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisCheckMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisCheckMobileEditView;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisChecksValueSetEditor;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import java.util.Set;

public class AnamnesisCheckActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public AnamnesisCheckActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new AnamnesisCheckDetailsActivity((EntityProxyId<AnamnesisCheckProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? AnamnesisCheckMobileDetailsView.instance() : AnamnesisCheckDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<AnamnesisCheckProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        AnamnesisCheckEditView.instance().setCreating(true);
        final AnamnesisCheckRequest request = requests.anamnesisCheckRequest();
        Activity activity = new CreateAndEditProxy<AnamnesisCheckProxy>(AnamnesisCheckProxy.class, request, ScaffoldApp.isMobile() ? AnamnesisCheckMobileEditView.instance() : AnamnesisCheckEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(AnamnesisCheckProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new AnamnesisCheckEditActivityWrapper(requests, ScaffoldApp.isMobile() ? AnamnesisCheckMobileEditView.instance() : AnamnesisCheckEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        AnamnesisCheckEditView.instance().setCreating(false);
        EntityProxyId<AnamnesisCheckProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<AnamnesisCheckProxy>(proxyId, requests, ScaffoldApp.isMobile() ? AnamnesisCheckMobileEditView.instance() : AnamnesisCheckEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(AnamnesisCheckProxy proxy) {
                AnamnesisCheckRequest request = requests.anamnesisCheckRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new AnamnesisCheckEditActivityWrapper(requests, ScaffoldApp.isMobile() ? AnamnesisCheckMobileEditView.instance() : AnamnesisCheckEditView.instance(), activity, proxyId);
    }
}
