package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintRequest;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.managed.ui.OscePostBlueprintDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.OscePostBlueprintEditView;
import ch.unibas.medizin.osce.client.managed.ui.OscePostBlueprintListView;
import ch.unibas.medizin.osce.client.managed.ui.OscePostBlueprintMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.OscePostBlueprintMobileEditView;
import ch.unibas.medizin.osce.client.managed.ui.OscePostSetEditor;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import ch.unibas.medizin.osce.shared.PostType;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import java.util.Set;

public class OscePostBlueprintActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public OscePostBlueprintActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new OscePostBlueprintDetailsActivity((EntityProxyId<OscePostBlueprintProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? OscePostBlueprintMobileDetailsView.instance() : OscePostBlueprintDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<OscePostBlueprintProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        OscePostBlueprintEditView.instance().setCreating(true);
        final OscePostBlueprintRequest request = requests.oscePostBlueprintRequest();
        Activity activity = new CreateAndEditProxy<OscePostBlueprintProxy>(OscePostBlueprintProxy.class, request, ScaffoldApp.isMobile() ? OscePostBlueprintMobileEditView.instance() : OscePostBlueprintEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(OscePostBlueprintProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new OscePostBlueprintEditActivityWrapper(requests, ScaffoldApp.isMobile() ? OscePostBlueprintMobileEditView.instance() : OscePostBlueprintEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        OscePostBlueprintEditView.instance().setCreating(false);
        EntityProxyId<OscePostBlueprintProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<OscePostBlueprintProxy>(proxyId, requests, ScaffoldApp.isMobile() ? OscePostBlueprintMobileEditView.instance() : OscePostBlueprintEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(OscePostBlueprintProxy proxy) {
                OscePostBlueprintRequest request = requests.oscePostBlueprintRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new OscePostBlueprintEditActivityWrapper(requests, ScaffoldApp.isMobile() ? OscePostBlueprintMobileEditView.instance() : OscePostBlueprintEditView.instance(), activity, proxyId);
    }
}
