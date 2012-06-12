package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.ChecklistCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistCriteriaRequest;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.ui.ChecklistCriteriaDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.ChecklistCriteriaEditView;
import ch.unibas.medizin.osce.client.managed.ui.ChecklistCriteriaListView;
import ch.unibas.medizin.osce.client.managed.ui.ChecklistCriteriaMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.ChecklistCriteriaMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class ChecklistCriteriaActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public ChecklistCriteriaActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new ChecklistCriteriaDetailsActivity((EntityProxyId<ChecklistCriteriaProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? ChecklistCriteriaMobileDetailsView.instance() : ChecklistCriteriaDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.ChecklistCriteriaProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<ChecklistCriteriaProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        ChecklistCriteriaEditView.instance().setCreating(true);
        final ChecklistCriteriaRequest request = requests.checklistCriteriaRequest();
        Activity activity = new CreateAndEditProxy<ChecklistCriteriaProxy>(ChecklistCriteriaProxy.class, request, ScaffoldApp.isMobile() ? ChecklistCriteriaMobileEditView.instance() : ChecklistCriteriaEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(ChecklistCriteriaProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new ChecklistCriteriaEditActivityWrapper(requests, ScaffoldApp.isMobile() ? ChecklistCriteriaMobileEditView.instance() : ChecklistCriteriaEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        ChecklistCriteriaEditView.instance().setCreating(false);
        EntityProxyId<ChecklistCriteriaProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<ChecklistCriteriaProxy>(proxyId, requests, ScaffoldApp.isMobile() ? ChecklistCriteriaMobileEditView.instance() : ChecklistCriteriaEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(ChecklistCriteriaProxy proxy) {
                ChecklistCriteriaRequest request = requests.checklistCriteriaRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new ChecklistCriteriaEditActivityWrapper(requests, ScaffoldApp.isMobile() ? ChecklistCriteriaMobileEditView.instance() : ChecklistCriteriaEditView.instance(), activity, proxyId);
    }
}
