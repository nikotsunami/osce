package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.SimpleSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.SimpleSearchCriteriaRequest;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.managed.ui.SimpleSearchCriteriaDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.SimpleSearchCriteriaEditView;
import ch.unibas.medizin.osce.client.managed.ui.SimpleSearchCriteriaListView;
import ch.unibas.medizin.osce.client.managed.ui.SimpleSearchCriteriaMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.SimpleSearchCriteriaMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class SimpleSearchCriteriaActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public SimpleSearchCriteriaActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new SimpleSearchCriteriaDetailsActivity((EntityProxyId<SimpleSearchCriteriaProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? SimpleSearchCriteriaMobileDetailsView.instance() : SimpleSearchCriteriaDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.SimpleSearchCriteriaProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<SimpleSearchCriteriaProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        SimpleSearchCriteriaEditView.instance().setCreating(true);
        final SimpleSearchCriteriaRequest request = requests.simpleSearchCriteriaRequest();
        Activity activity = new CreateAndEditProxy<SimpleSearchCriteriaProxy>(SimpleSearchCriteriaProxy.class, request, ScaffoldApp.isMobile() ? SimpleSearchCriteriaMobileEditView.instance() : SimpleSearchCriteriaEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(SimpleSearchCriteriaProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new SimpleSearchCriteriaEditActivityWrapper(requests, ScaffoldApp.isMobile() ? SimpleSearchCriteriaMobileEditView.instance() : SimpleSearchCriteriaEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        SimpleSearchCriteriaEditView.instance().setCreating(false);
        EntityProxyId<SimpleSearchCriteriaProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<SimpleSearchCriteriaProxy>(proxyId, requests, ScaffoldApp.isMobile() ? SimpleSearchCriteriaMobileEditView.instance() : SimpleSearchCriteriaEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(SimpleSearchCriteriaProxy proxy) {
                SimpleSearchCriteriaRequest request = requests.simpleSearchCriteriaRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new SimpleSearchCriteriaEditActivityWrapper(requests, ScaffoldApp.isMobile() ? SimpleSearchCriteriaMobileEditView.instance() : SimpleSearchCriteriaEditView.instance(), activity, proxyId);
    }
}
