package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.CheckListProxy;
import ch.unibas.medizin.osce.client.managed.request.CheckListRequest;
import ch.unibas.medizin.osce.client.managed.ui.CheckListDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.CheckListEditView;
import ch.unibas.medizin.osce.client.managed.ui.CheckListListView;
import ch.unibas.medizin.osce.client.managed.ui.CheckListMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.CheckListMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class CheckListActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public CheckListActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new CheckListDetailsActivity((EntityProxyId<CheckListProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? CheckListMobileDetailsView.instance() : CheckListDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.CheckListProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<CheckListProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        CheckListEditView.instance().setCreating(true);
        final CheckListRequest request = requests.checkListRequest();
        Activity activity = new CreateAndEditProxy<CheckListProxy>(CheckListProxy.class, request, ScaffoldApp.isMobile() ? CheckListMobileEditView.instance() : CheckListEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(CheckListProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new CheckListEditActivityWrapper(requests, ScaffoldApp.isMobile() ? CheckListMobileEditView.instance() : CheckListEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        CheckListEditView.instance().setCreating(false);
        EntityProxyId<CheckListProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<CheckListProxy>(proxyId, requests, ScaffoldApp.isMobile() ? CheckListMobileEditView.instance() : CheckListEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(CheckListProxy proxy) {
                CheckListRequest request = requests.checkListRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new CheckListEditActivityWrapper(requests, ScaffoldApp.isMobile() ? CheckListMobileEditView.instance() : CheckListEditView.instance(), activity, proxyId);
    }
}
