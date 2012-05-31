package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.CheckListProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicRequest;
import ch.unibas.medizin.osce.client.managed.ui.ChecklistQuestionListEditor;
import ch.unibas.medizin.osce.client.managed.ui.ChecklistTopicDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.ChecklistTopicEditView;
import ch.unibas.medizin.osce.client.managed.ui.ChecklistTopicListView;
import ch.unibas.medizin.osce.client.managed.ui.ChecklistTopicMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.ChecklistTopicMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import java.util.List;

public class ChecklistTopicActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public ChecklistTopicActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new ChecklistTopicDetailsActivity((EntityProxyId<ChecklistTopicProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? ChecklistTopicMobileDetailsView.instance() : ChecklistTopicDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.ChecklistTopicProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<ChecklistTopicProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        ChecklistTopicEditView.instance().setCreating(true);
        final ChecklistTopicRequest request = requests.checklistTopicRequest();
        Activity activity = new CreateAndEditProxy<ChecklistTopicProxy>(ChecklistTopicProxy.class, request, ScaffoldApp.isMobile() ? ChecklistTopicMobileEditView.instance() : ChecklistTopicEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(ChecklistTopicProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new ChecklistTopicEditActivityWrapper(requests, ScaffoldApp.isMobile() ? ChecklistTopicMobileEditView.instance() : ChecklistTopicEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        ChecklistTopicEditView.instance().setCreating(false);
        EntityProxyId<ChecklistTopicProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<ChecklistTopicProxy>(proxyId, requests, ScaffoldApp.isMobile() ? ChecklistTopicMobileEditView.instance() : ChecklistTopicEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(ChecklistTopicProxy proxy) {
                ChecklistTopicRequest request = requests.checklistTopicRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new ChecklistTopicEditActivityWrapper(requests, ScaffoldApp.isMobile() ? ChecklistTopicMobileEditView.instance() : ChecklistTopicEditView.instance(), activity, proxyId);
    }
}
