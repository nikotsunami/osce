package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.UsedMaterialProxy;
import ch.unibas.medizin.osce.client.managed.request.UsedMaterialRequest;
import ch.unibas.medizin.osce.client.managed.ui.UsedMaterialDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.UsedMaterialEditView;
import ch.unibas.medizin.osce.client.managed.ui.UsedMaterialListView;
import ch.unibas.medizin.osce.client.managed.ui.UsedMaterialMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.UsedMaterialMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class UsedMaterialActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public UsedMaterialActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new UsedMaterialDetailsActivity((EntityProxyId<UsedMaterialProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? UsedMaterialMobileDetailsView.instance() : UsedMaterialDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.UsedMaterialProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<UsedMaterialProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        UsedMaterialEditView.instance().setCreating(true);
        final UsedMaterialRequest request = requests.usedMaterialRequest();
        Activity activity = new CreateAndEditProxy<UsedMaterialProxy>(UsedMaterialProxy.class, request, ScaffoldApp.isMobile() ? UsedMaterialMobileEditView.instance() : UsedMaterialEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(UsedMaterialProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new UsedMaterialEditActivityWrapper(requests, ScaffoldApp.isMobile() ? UsedMaterialMobileEditView.instance() : UsedMaterialEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        UsedMaterialEditView.instance().setCreating(false);
        EntityProxyId<UsedMaterialProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<UsedMaterialProxy>(proxyId, requests, ScaffoldApp.isMobile() ? UsedMaterialMobileEditView.instance() : UsedMaterialEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(UsedMaterialProxy proxy) {
                UsedMaterialRequest request = requests.usedMaterialRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new UsedMaterialEditActivityWrapper(requests, ScaffoldApp.isMobile() ? UsedMaterialMobileEditView.instance() : UsedMaterialEditView.instance(), activity, proxyId);
    }
}
