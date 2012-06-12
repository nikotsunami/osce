package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleSubItemValueProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleSubItemValueRequest;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.managed.ui.RoleSubItemValueDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.RoleSubItemValueEditView;
import ch.unibas.medizin.osce.client.managed.ui.RoleSubItemValueListView;
import ch.unibas.medizin.osce.client.managed.ui.RoleSubItemValueMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.RoleSubItemValueMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class RoleSubItemValueActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public RoleSubItemValueActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new RoleSubItemValueDetailsActivity((EntityProxyId<RoleSubItemValueProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? RoleSubItemValueMobileDetailsView.instance() : RoleSubItemValueDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.RoleSubItemValueProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<RoleSubItemValueProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        RoleSubItemValueEditView.instance().setCreating(true);
        final RoleSubItemValueRequest request = requests.roleSubItemValueRequest();
        Activity activity = new CreateAndEditProxy<RoleSubItemValueProxy>(RoleSubItemValueProxy.class, request, ScaffoldApp.isMobile() ? RoleSubItemValueMobileEditView.instance() : RoleSubItemValueEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(RoleSubItemValueProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new RoleSubItemValueEditActivityWrapper(requests, ScaffoldApp.isMobile() ? RoleSubItemValueMobileEditView.instance() : RoleSubItemValueEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        RoleSubItemValueEditView.instance().setCreating(false);
        EntityProxyId<RoleSubItemValueProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<RoleSubItemValueProxy>(proxyId, requests, ScaffoldApp.isMobile() ? RoleSubItemValueMobileEditView.instance() : RoleSubItemValueEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(RoleSubItemValueProxy proxy) {
                RoleSubItemValueRequest request = requests.roleSubItemValueRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new RoleSubItemValueEditActivityWrapper(requests, ScaffoldApp.isMobile() ? RoleSubItemValueMobileEditView.instance() : RoleSubItemValueEditView.instance(), activity, proxyId);
    }
}
