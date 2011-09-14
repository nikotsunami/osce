package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormRequest;
import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisChecksValueSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisFormDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisFormEditView;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisFormListView;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisFormMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisFormMobileEditView;
import ch.unibas.medizin.osce.client.managed.ui.ScarSetEditor;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import java.util.Set;

public class AnamnesisFormActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public AnamnesisFormActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new AnamnesisFormDetailsActivity((EntityProxyId<AnamnesisFormProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? AnamnesisFormMobileDetailsView.instance() : AnamnesisFormDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<AnamnesisFormProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        AnamnesisFormEditView.instance().setCreating(true);
        final AnamnesisFormRequest request = requests.anamnesisFormRequest();
        Activity activity = new CreateAndEditProxy<AnamnesisFormProxy>(AnamnesisFormProxy.class, request, ScaffoldApp.isMobile() ? AnamnesisFormMobileEditView.instance() : AnamnesisFormEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(AnamnesisFormProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new AnamnesisFormEditActivityWrapper(requests, ScaffoldApp.isMobile() ? AnamnesisFormMobileEditView.instance() : AnamnesisFormEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        AnamnesisFormEditView.instance().setCreating(false);
        EntityProxyId<AnamnesisFormProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<AnamnesisFormProxy>(proxyId, requests, ScaffoldApp.isMobile() ? AnamnesisFormMobileEditView.instance() : AnamnesisFormEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(AnamnesisFormProxy proxy) {
                AnamnesisFormRequest request = requests.anamnesisFormRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new AnamnesisFormEditActivityWrapper(requests, ScaffoldApp.isMobile() ? AnamnesisFormMobileEditView.instance() : AnamnesisFormEditView.instance(), activity, proxyId);
    }
}
