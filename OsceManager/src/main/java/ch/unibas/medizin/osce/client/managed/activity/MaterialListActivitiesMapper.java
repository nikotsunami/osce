package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.MaterialListProxy;
import ch.unibas.medizin.osce.client.managed.request.MaterialListRequest;
import ch.unibas.medizin.osce.client.managed.ui.MaterialListDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.MaterialListEditView;
import ch.unibas.medizin.osce.client.managed.ui.MaterialListListView;
import ch.unibas.medizin.osce.client.managed.ui.MaterialListMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.MaterialListMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class MaterialListActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public MaterialListActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new MaterialListDetailsActivity((EntityProxyId<MaterialListProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? MaterialListMobileDetailsView.instance() : MaterialListDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.MaterialListProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<MaterialListProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        MaterialListEditView.instance().setCreating(true);
        final MaterialListRequest request = requests.materialListRequest();
        Activity activity = new CreateAndEditProxy<MaterialListProxy>(MaterialListProxy.class, request, ScaffoldApp.isMobile() ? MaterialListMobileEditView.instance() : MaterialListEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(MaterialListProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new MaterialListEditActivityWrapper(requests, ScaffoldApp.isMobile() ? MaterialListMobileEditView.instance() : MaterialListEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        MaterialListEditView.instance().setCreating(false);
        EntityProxyId<MaterialListProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<MaterialListProxy>(proxyId, requests, ScaffoldApp.isMobile() ? MaterialListMobileEditView.instance() : MaterialListEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(MaterialListProxy proxy) {
                MaterialListRequest request = requests.materialListRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new MaterialListEditActivityWrapper(requests, ScaffoldApp.isMobile() ? MaterialListMobileEditView.instance() : MaterialListEditView.instance(), activity, proxyId);
    }
}
