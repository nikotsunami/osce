package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.request.ScarRequest;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisFormSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.ScarDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.ScarEditView;
import ch.unibas.medizin.osce.client.managed.ui.ScarListView;
import ch.unibas.medizin.osce.client.managed.ui.ScarMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.ScarMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import java.util.Set;

public class ScarActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public ScarActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new ScarDetailsActivity((EntityProxyId<ScarProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? ScarMobileDetailsView.instance() : ScarDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.ScarProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<ScarProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        ScarEditView.instance().setCreating(true);
        final ScarRequest request = requests.scarRequest();
        Activity activity = new CreateAndEditProxy<ScarProxy>(ScarProxy.class, request, ScaffoldApp.isMobile() ? ScarMobileEditView.instance() : ScarEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(ScarProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new ScarEditActivityWrapper(requests, ScaffoldApp.isMobile() ? ScarMobileEditView.instance() : ScarEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        ScarEditView.instance().setCreating(false);
        EntityProxyId<ScarProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<ScarProxy>(proxyId, requests, ScaffoldApp.isMobile() ? ScarMobileEditView.instance() : ScarEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(ScarProxy proxy) {
                ScarRequest request = requests.scarRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new ScarEditActivityWrapper(requests, ScaffoldApp.isMobile() ? ScarMobileEditView.instance() : ScarEditView.instance(), activity, proxyId);
    }
}
