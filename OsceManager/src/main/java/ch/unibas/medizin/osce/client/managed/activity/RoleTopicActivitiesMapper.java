package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicRequest;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.managed.ui.RoleTopicDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.RoleTopicEditView;
import ch.unibas.medizin.osce.client.managed.ui.RoleTopicListView;
import ch.unibas.medizin.osce.client.managed.ui.RoleTopicMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.RoleTopicMobileEditView;
import ch.unibas.medizin.osce.client.managed.ui.StandardizedRoleSetEditor;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import ch.unibas.medizin.osce.shared.StudyYears;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import java.util.Set;

public class RoleTopicActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public RoleTopicActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new RoleTopicDetailsActivity((EntityProxyId<RoleTopicProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? RoleTopicMobileDetailsView.instance() : RoleTopicDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<RoleTopicProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        RoleTopicEditView.instance().setCreating(true);
        final RoleTopicRequest request = requests.roleTopicRequest();
        Activity activity = new CreateAndEditProxy<RoleTopicProxy>(RoleTopicProxy.class, request, ScaffoldApp.isMobile() ? RoleTopicMobileEditView.instance() : RoleTopicEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(RoleTopicProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new RoleTopicEditActivityWrapper(requests, ScaffoldApp.isMobile() ? RoleTopicMobileEditView.instance() : RoleTopicEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        RoleTopicEditView.instance().setCreating(false);
        EntityProxyId<RoleTopicProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<RoleTopicProxy>(proxyId, requests, ScaffoldApp.isMobile() ? RoleTopicMobileEditView.instance() : RoleTopicEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(RoleTopicProxy proxy) {
                RoleTopicRequest request = requests.roleTopicRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new RoleTopicEditActivityWrapper(requests, ScaffoldApp.isMobile() ? RoleTopicMobileEditView.instance() : RoleTopicEditView.instance(), activity, proxyId);
    }
}
