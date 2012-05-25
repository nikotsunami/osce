package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleItemAccessProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleItemAccessRequest;
import ch.unibas.medizin.osce.client.managed.ui.RoleBaseItemSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.RoleItemAccessDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.RoleItemAccessEditView;
import ch.unibas.medizin.osce.client.managed.ui.RoleItemAccessListView;
import ch.unibas.medizin.osce.client.managed.ui.RoleItemAccessMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.RoleItemAccessMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import java.util.Set;

public class RoleItemAccessActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public RoleItemAccessActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new RoleItemAccessDetailsActivity((EntityProxyId<RoleItemAccessProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? RoleItemAccessMobileDetailsView.instance() : RoleItemAccessDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.RoleItemAccessProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<RoleItemAccessProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        RoleItemAccessEditView.instance().setCreating(true);
        final RoleItemAccessRequest request = requests.roleItemAccessRequest();
        Activity activity = new CreateAndEditProxy<RoleItemAccessProxy>(RoleItemAccessProxy.class, request, ScaffoldApp.isMobile() ? RoleItemAccessMobileEditView.instance() : RoleItemAccessEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(RoleItemAccessProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new RoleItemAccessEditActivityWrapper(requests, ScaffoldApp.isMobile() ? RoleItemAccessMobileEditView.instance() : RoleItemAccessEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        RoleItemAccessEditView.instance().setCreating(false);
        EntityProxyId<RoleItemAccessProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<RoleItemAccessProxy>(proxyId, requests, ScaffoldApp.isMobile() ? RoleItemAccessMobileEditView.instance() : RoleItemAccessEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(RoleItemAccessProxy proxy) {
                RoleItemAccessRequest request = requests.roleItemAccessRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new RoleItemAccessEditActivityWrapper(requests, ScaffoldApp.isMobile() ? RoleItemAccessMobileEditView.instance() : RoleItemAccessEditView.instance(), activity, proxyId);
    }
}
