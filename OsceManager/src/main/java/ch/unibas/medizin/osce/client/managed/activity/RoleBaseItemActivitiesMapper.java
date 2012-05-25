package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemRequest;
import ch.unibas.medizin.osce.client.managed.request.RoleItemAccessProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTableItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTemplateProxy;
import ch.unibas.medizin.osce.client.managed.ui.RoleBaseItemDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.RoleBaseItemEditView;
import ch.unibas.medizin.osce.client.managed.ui.RoleBaseItemListView;
import ch.unibas.medizin.osce.client.managed.ui.RoleBaseItemMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.RoleBaseItemMobileEditView;
import ch.unibas.medizin.osce.client.managed.ui.RoleItemAccessSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.RoleTableItemListEditor;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import java.util.List;
import java.util.Set;

public class RoleBaseItemActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public RoleBaseItemActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new RoleBaseItemDetailsActivity((EntityProxyId<RoleBaseItemProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? RoleBaseItemMobileDetailsView.instance() : RoleBaseItemDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<RoleBaseItemProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        RoleBaseItemEditView.instance().setCreating(true);
        final RoleBaseItemRequest request = requests.roleBaseItemRequest();
        Activity activity = new CreateAndEditProxy<RoleBaseItemProxy>(RoleBaseItemProxy.class, request, ScaffoldApp.isMobile() ? RoleBaseItemMobileEditView.instance() : RoleBaseItemEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(RoleBaseItemProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new RoleBaseItemEditActivityWrapper(requests, ScaffoldApp.isMobile() ? RoleBaseItemMobileEditView.instance() : RoleBaseItemEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        RoleBaseItemEditView.instance().setCreating(false);
        EntityProxyId<RoleBaseItemProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<RoleBaseItemProxy>(proxyId, requests, ScaffoldApp.isMobile() ? RoleBaseItemMobileEditView.instance() : RoleBaseItemEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(RoleBaseItemProxy proxy) {
                RoleBaseItemRequest request = requests.roleBaseItemRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new RoleBaseItemEditActivityWrapper(requests, ScaffoldApp.isMobile() ? RoleBaseItemMobileEditView.instance() : RoleBaseItemEditView.instance(), activity, proxyId);
    }
}
