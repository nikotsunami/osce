package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.EliminationCriterionProxy;
import ch.unibas.medizin.osce.client.managed.request.EliminationCriterionRequest;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.managed.ui.EliminationCriterionDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.EliminationCriterionEditView;
import ch.unibas.medizin.osce.client.managed.ui.EliminationCriterionListView;
import ch.unibas.medizin.osce.client.managed.ui.EliminationCriterionMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.EliminationCriterionMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class EliminationCriterionActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public EliminationCriterionActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new EliminationCriterionDetailsActivity((EntityProxyId<EliminationCriterionProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? EliminationCriterionMobileDetailsView.instance() : EliminationCriterionDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.EliminationCriterionProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<EliminationCriterionProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        EliminationCriterionEditView.instance().setCreating(true);
        final EliminationCriterionRequest request = requests.eliminationCriterionRequest();
        Activity activity = new CreateAndEditProxy<EliminationCriterionProxy>(EliminationCriterionProxy.class, request, ScaffoldApp.isMobile() ? EliminationCriterionMobileEditView.instance() : EliminationCriterionEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(EliminationCriterionProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new EliminationCriterionEditActivityWrapper(requests, ScaffoldApp.isMobile() ? EliminationCriterionMobileEditView.instance() : EliminationCriterionEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        EliminationCriterionEditView.instance().setCreating(false);
        EntityProxyId<EliminationCriterionProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<EliminationCriterionProxy>(proxyId, requests, ScaffoldApp.isMobile() ? EliminationCriterionMobileEditView.instance() : EliminationCriterionEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(EliminationCriterionProxy proxy) {
                EliminationCriterionRequest request = requests.eliminationCriterionRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new EliminationCriterionEditActivityWrapper(requests, ScaffoldApp.isMobile() ? EliminationCriterionMobileEditView.instance() : EliminationCriterionEditView.instance(), activity, proxyId);
    }
}
