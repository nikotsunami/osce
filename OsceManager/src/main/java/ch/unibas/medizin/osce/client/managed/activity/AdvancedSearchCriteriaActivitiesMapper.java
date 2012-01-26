package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaRequest;
import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.ui.AdvancedSearchCriteriaDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.AdvancedSearchCriteriaEditView;
import ch.unibas.medizin.osce.client.managed.ui.AdvancedSearchCriteriaListView;
import ch.unibas.medizin.osce.client.managed.ui.AdvancedSearchCriteriaMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.AdvancedSearchCriteriaMobileEditView;
import ch.unibas.medizin.osce.client.managed.ui.SemesterSetEditor;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import java.util.Set;

public class AdvancedSearchCriteriaActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public AdvancedSearchCriteriaActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new AdvancedSearchCriteriaDetailsActivity((EntityProxyId<AdvancedSearchCriteriaProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? AdvancedSearchCriteriaMobileDetailsView.instance() : AdvancedSearchCriteriaDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<AdvancedSearchCriteriaProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        AdvancedSearchCriteriaEditView.instance().setCreating(true);
        final AdvancedSearchCriteriaRequest request = requests.advancedSearchCriteriaRequest();
        Activity activity = new CreateAndEditProxy<AdvancedSearchCriteriaProxy>(AdvancedSearchCriteriaProxy.class, request, ScaffoldApp.isMobile() ? AdvancedSearchCriteriaMobileEditView.instance() : AdvancedSearchCriteriaEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(AdvancedSearchCriteriaProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new AdvancedSearchCriteriaEditActivityWrapper(requests, ScaffoldApp.isMobile() ? AdvancedSearchCriteriaMobileEditView.instance() : AdvancedSearchCriteriaEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        AdvancedSearchCriteriaEditView.instance().setCreating(false);
        EntityProxyId<AdvancedSearchCriteriaProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<AdvancedSearchCriteriaProxy>(proxyId, requests, ScaffoldApp.isMobile() ? AdvancedSearchCriteriaMobileEditView.instance() : AdvancedSearchCriteriaEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(AdvancedSearchCriteriaProxy proxy) {
                AdvancedSearchCriteriaRequest request = requests.advancedSearchCriteriaRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new AdvancedSearchCriteriaEditActivityWrapper(requests, ScaffoldApp.isMobile() ? AdvancedSearchCriteriaMobileEditView.instance() : AdvancedSearchCriteriaEditView.instance(), activity, proxyId);
    }
}
