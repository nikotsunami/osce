package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.DescriptionProxy;
import ch.unibas.medizin.osce.client.managed.request.DescriptionRequest;
import ch.unibas.medizin.osce.client.managed.ui.DescriptionDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.DescriptionEditView;
import ch.unibas.medizin.osce.client.managed.ui.DescriptionListView;
import ch.unibas.medizin.osce.client.managed.ui.DescriptionMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.DescriptionMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class DescriptionActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public DescriptionActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new DescriptionDetailsActivity((EntityProxyId<DescriptionProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? DescriptionMobileDetailsView.instance() : DescriptionDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.DescriptionProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<DescriptionProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        DescriptionEditView.instance().setCreating(true);
        final DescriptionRequest request = requests.descriptionRequest();
        Activity activity = new CreateAndEditProxy<DescriptionProxy>(DescriptionProxy.class, request, ScaffoldApp.isMobile() ? DescriptionMobileEditView.instance() : DescriptionEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(DescriptionProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new DescriptionEditActivityWrapper(requests, ScaffoldApp.isMobile() ? DescriptionMobileEditView.instance() : DescriptionEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        DescriptionEditView.instance().setCreating(false);
        EntityProxyId<DescriptionProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<DescriptionProxy>(proxyId, requests, ScaffoldApp.isMobile() ? DescriptionMobileEditView.instance() : DescriptionEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(DescriptionProxy proxy) {
                DescriptionRequest request = requests.descriptionRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new DescriptionEditActivityWrapper(requests, ScaffoldApp.isMobile() ? DescriptionMobileEditView.instance() : DescriptionEditView.instance(), activity, proxyId);
    }
}
