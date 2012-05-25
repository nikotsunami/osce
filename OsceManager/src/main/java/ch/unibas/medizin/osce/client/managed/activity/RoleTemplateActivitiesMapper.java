package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTemplateProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTemplateRequest;
import ch.unibas.medizin.osce.client.managed.ui.RoleBaseItemListEditor;
import ch.unibas.medizin.osce.client.managed.ui.RoleTemplateDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.RoleTemplateEditView;
import ch.unibas.medizin.osce.client.managed.ui.RoleTemplateListView;
import ch.unibas.medizin.osce.client.managed.ui.RoleTemplateMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.RoleTemplateMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import java.util.List;

public class RoleTemplateActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public RoleTemplateActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new RoleTemplateDetailsActivity((EntityProxyId<RoleTemplateProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? RoleTemplateMobileDetailsView.instance() : RoleTemplateDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.RoleTemplateProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<RoleTemplateProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        RoleTemplateEditView.instance().setCreating(true);
        final RoleTemplateRequest request = requests.roleTemplateRequest();
        Activity activity = new CreateAndEditProxy<RoleTemplateProxy>(RoleTemplateProxy.class, request, ScaffoldApp.isMobile() ? RoleTemplateMobileEditView.instance() : RoleTemplateEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(RoleTemplateProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new RoleTemplateEditActivityWrapper(requests, ScaffoldApp.isMobile() ? RoleTemplateMobileEditView.instance() : RoleTemplateEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        RoleTemplateEditView.instance().setCreating(false);
        EntityProxyId<RoleTemplateProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<RoleTemplateProxy>(proxyId, requests, ScaffoldApp.isMobile() ? RoleTemplateMobileEditView.instance() : RoleTemplateEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(RoleTemplateProxy proxy) {
                RoleTemplateRequest request = requests.roleTemplateRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new RoleTemplateEditActivityWrapper(requests, ScaffoldApp.isMobile() ? RoleTemplateMobileEditView.instance() : RoleTemplateEditView.instance(), activity, proxyId);
    }
}
