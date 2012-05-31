package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.ChecklistCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistOptionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionRequest;
import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicProxy;
import ch.unibas.medizin.osce.client.managed.ui.ChecklistCriteriaSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.ChecklistOptionSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.ChecklistQuestionDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.ChecklistQuestionEditView;
import ch.unibas.medizin.osce.client.managed.ui.ChecklistQuestionListView;
import ch.unibas.medizin.osce.client.managed.ui.ChecklistQuestionMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.ChecklistQuestionMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import java.util.Set;

public class ChecklistQuestionActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public ChecklistQuestionActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new ChecklistQuestionDetailsActivity((EntityProxyId<ChecklistQuestionProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? ChecklistQuestionMobileDetailsView.instance() : ChecklistQuestionDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<ChecklistQuestionProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        ChecklistQuestionEditView.instance().setCreating(true);
        final ChecklistQuestionRequest request = requests.checklistQuestionRequest();
        Activity activity = new CreateAndEditProxy<ChecklistQuestionProxy>(ChecklistQuestionProxy.class, request, ScaffoldApp.isMobile() ? ChecklistQuestionMobileEditView.instance() : ChecklistQuestionEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(ChecklistQuestionProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new ChecklistQuestionEditActivityWrapper(requests, ScaffoldApp.isMobile() ? ChecklistQuestionMobileEditView.instance() : ChecklistQuestionEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        ChecklistQuestionEditView.instance().setCreating(false);
        EntityProxyId<ChecklistQuestionProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<ChecklistQuestionProxy>(proxyId, requests, ScaffoldApp.isMobile() ? ChecklistQuestionMobileEditView.instance() : ChecklistQuestionEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(ChecklistQuestionProxy proxy) {
                ChecklistQuestionRequest request = requests.checklistQuestionRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new ChecklistQuestionEditActivityWrapper(requests, ScaffoldApp.isMobile() ? ChecklistQuestionMobileEditView.instance() : ChecklistQuestionEditView.instance(), activity, proxyId);
    }
}
