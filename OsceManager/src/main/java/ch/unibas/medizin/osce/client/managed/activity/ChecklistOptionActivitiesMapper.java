package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.ChecklistOptionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistOptionRequest;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.ui.ChecklistOptionDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.ChecklistOptionEditView;
import ch.unibas.medizin.osce.client.managed.ui.ChecklistOptionListView;
import ch.unibas.medizin.osce.client.managed.ui.ChecklistOptionMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.ChecklistOptionMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class ChecklistOptionActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public ChecklistOptionActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new ChecklistOptionDetailsActivity((EntityProxyId<ChecklistOptionProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? ChecklistOptionMobileDetailsView.instance() : ChecklistOptionDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.ChecklistOptionProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<ChecklistOptionProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        ChecklistOptionEditView.instance().setCreating(true);
        final ChecklistOptionRequest request = requests.checklistOptionRequest();
        Activity activity = new CreateAndEditProxy<ChecklistOptionProxy>(ChecklistOptionProxy.class, request, ScaffoldApp.isMobile() ? ChecklistOptionMobileEditView.instance() : ChecklistOptionEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(ChecklistOptionProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new ChecklistOptionEditActivityWrapper(requests, ScaffoldApp.isMobile() ? ChecklistOptionMobileEditView.instance() : ChecklistOptionEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        ChecklistOptionEditView.instance().setCreating(false);
        EntityProxyId<ChecklistOptionProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<ChecklistOptionProxy>(proxyId, requests, ScaffoldApp.isMobile() ? ChecklistOptionMobileEditView.instance() : ChecklistOptionEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(ChecklistOptionProxy proxy) {
                ChecklistOptionRequest request = requests.checklistOptionRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new ChecklistOptionEditActivityWrapper(requests, ScaffoldApp.isMobile() ? ChecklistOptionMobileEditView.instance() : ChecklistOptionEditView.instance(), activity, proxyId);
    }
}
