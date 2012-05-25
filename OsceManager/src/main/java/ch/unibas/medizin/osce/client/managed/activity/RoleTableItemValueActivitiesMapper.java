package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.RoleTableItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTableItemValueProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTableItemValueRequest;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.managed.ui.RoleTableItemValueDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.RoleTableItemValueEditView;
import ch.unibas.medizin.osce.client.managed.ui.RoleTableItemValueListView;
import ch.unibas.medizin.osce.client.managed.ui.RoleTableItemValueMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.RoleTableItemValueMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class RoleTableItemValueActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public RoleTableItemValueActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new RoleTableItemValueDetailsActivity((EntityProxyId<RoleTableItemValueProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? RoleTableItemValueMobileDetailsView.instance() : RoleTableItemValueDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.RoleTableItemValueProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<RoleTableItemValueProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        RoleTableItemValueEditView.instance().setCreating(true);
        final RoleTableItemValueRequest request = requests.roleTableItemValueRequest();
        Activity activity = new CreateAndEditProxy<RoleTableItemValueProxy>(RoleTableItemValueProxy.class, request, ScaffoldApp.isMobile() ? RoleTableItemValueMobileEditView.instance() : RoleTableItemValueEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(RoleTableItemValueProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new RoleTableItemValueEditActivityWrapper(requests, ScaffoldApp.isMobile() ? RoleTableItemValueMobileEditView.instance() : RoleTableItemValueEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        RoleTableItemValueEditView.instance().setCreating(false);
        EntityProxyId<RoleTableItemValueProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<RoleTableItemValueProxy>(proxyId, requests, ScaffoldApp.isMobile() ? RoleTableItemValueMobileEditView.instance() : RoleTableItemValueEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(RoleTableItemValueProxy proxy) {
                RoleTableItemValueRequest request = requests.roleTableItemValueRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new RoleTableItemValueEditActivityWrapper(requests, ScaffoldApp.isMobile() ? RoleTableItemValueMobileEditView.instance() : RoleTableItemValueEditView.instance(), activity, proxyId);
    }
}
