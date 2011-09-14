package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.ProfessionProxy;
import ch.unibas.medizin.osce.client.managed.request.ProfessionRequest;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.ui.ProfessionDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.ProfessionEditView;
import ch.unibas.medizin.osce.client.managed.ui.ProfessionListView;
import ch.unibas.medizin.osce.client.managed.ui.ProfessionMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.ProfessionMobileEditView;
import ch.unibas.medizin.osce.client.managed.ui.StandardizedPatientSetEditor;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import java.util.Set;

public class ProfessionActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public ProfessionActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new ProfessionDetailsActivity((EntityProxyId<ProfessionProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? ProfessionMobileDetailsView.instance() : ProfessionDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.ProfessionProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<ProfessionProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        ProfessionEditView.instance().setCreating(true);
        final ProfessionRequest request = requests.professionRequest();
        Activity activity = new CreateAndEditProxy<ProfessionProxy>(ProfessionProxy.class, request, ScaffoldApp.isMobile() ? ProfessionMobileEditView.instance() : ProfessionEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(ProfessionProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new ProfessionEditActivityWrapper(requests, ScaffoldApp.isMobile() ? ProfessionMobileEditView.instance() : ProfessionEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        ProfessionEditView.instance().setCreating(false);
        EntityProxyId<ProfessionProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<ProfessionProxy>(proxyId, requests, ScaffoldApp.isMobile() ? ProfessionMobileEditView.instance() : ProfessionEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(ProfessionProxy proxy) {
                ProfessionRequest request = requests.professionRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new ProfessionEditActivityWrapper(requests, ScaffoldApp.isMobile() ? ProfessionMobileEditView.instance() : ProfessionEditView.instance(), activity, proxyId);
    }
}
