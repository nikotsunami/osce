package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleRequest;
import ch.unibas.medizin.osce.client.managed.ui.StandardizedRoleDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.StandardizedRoleEditView;
import ch.unibas.medizin.osce.client.managed.ui.StandardizedRoleListView;
import ch.unibas.medizin.osce.client.managed.ui.StandardizedRoleMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.StandardizedRoleMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class StandardizedRoleActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public StandardizedRoleActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new StandardizedRoleDetailsActivity((EntityProxyId<StandardizedRoleProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? StandardizedRoleMobileDetailsView.instance() : StandardizedRoleDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<StandardizedRoleProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        StandardizedRoleEditView.instance().setCreating(true);
        final StandardizedRoleRequest request = requests.standardizedRoleRequest();
        Activity activity = new CreateAndEditProxy<StandardizedRoleProxy>(StandardizedRoleProxy.class, request, ScaffoldApp.isMobile() ? StandardizedRoleMobileEditView.instance() : StandardizedRoleEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(StandardizedRoleProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new StandardizedRoleEditActivityWrapper(requests, ScaffoldApp.isMobile() ? StandardizedRoleMobileEditView.instance() : StandardizedRoleEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        StandardizedRoleEditView.instance().setCreating(false);
        EntityProxyId<StandardizedRoleProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<StandardizedRoleProxy>(proxyId, requests, ScaffoldApp.isMobile() ? StandardizedRoleMobileEditView.instance() : StandardizedRoleEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(StandardizedRoleProxy proxy) {
                StandardizedRoleRequest request = requests.standardizedRoleRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new StandardizedRoleEditActivityWrapper(requests, ScaffoldApp.isMobile() ? StandardizedRoleMobileEditView.instance() : StandardizedRoleEditView.instance(), activity, proxyId);
    }
}
