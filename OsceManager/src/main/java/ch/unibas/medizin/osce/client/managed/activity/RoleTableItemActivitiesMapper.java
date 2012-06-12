package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTableItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTableItemRequest;
import ch.unibas.medizin.osce.client.managed.ui.RoleTableItemDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.RoleTableItemEditView;
import ch.unibas.medizin.osce.client.managed.ui.RoleTableItemListView;
import ch.unibas.medizin.osce.client.managed.ui.RoleTableItemMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.RoleTableItemMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class RoleTableItemActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public RoleTableItemActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new RoleTableItemDetailsActivity((EntityProxyId<RoleTableItemProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? RoleTableItemMobileDetailsView.instance() : RoleTableItemDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.RoleTableItemProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<RoleTableItemProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        RoleTableItemEditView.instance().setCreating(true);
        final RoleTableItemRequest request = requests.roleTableItemRequest();
        Activity activity = new CreateAndEditProxy<RoleTableItemProxy>(RoleTableItemProxy.class, request, ScaffoldApp.isMobile() ? RoleTableItemMobileEditView.instance() : RoleTableItemEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(RoleTableItemProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new RoleTableItemEditActivityWrapper(requests, ScaffoldApp.isMobile() ? RoleTableItemMobileEditView.instance() : RoleTableItemEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        RoleTableItemEditView.instance().setCreating(false);
        EntityProxyId<RoleTableItemProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<RoleTableItemProxy>(proxyId, requests, ScaffoldApp.isMobile() ? RoleTableItemMobileEditView.instance() : RoleTableItemEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(RoleTableItemProxy proxy) {
                RoleTableItemRequest request = requests.roleTableItemRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new RoleTableItemEditActivityWrapper(requests, ScaffoldApp.isMobile() ? RoleTableItemMobileEditView.instance() : RoleTableItemEditView.instance(), activity, proxyId);
    }
}
