package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayRequest;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.ui.AssignmentSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.OsceDayDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.OsceDayEditView;
import ch.unibas.medizin.osce.client.managed.ui.OsceDayListView;
import ch.unibas.medizin.osce.client.managed.ui.OsceDayMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.OsceDayMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import java.util.Set;

public class OsceDayActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public OsceDayActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new OsceDayDetailsActivity((EntityProxyId<OsceDayProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? OsceDayMobileDetailsView.instance() : OsceDayDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.OsceDayProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<OsceDayProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        OsceDayEditView.instance().setCreating(true);
        final OsceDayRequest request = requests.osceDayRequest();
        Activity activity = new CreateAndEditProxy<OsceDayProxy>(OsceDayProxy.class, request, ScaffoldApp.isMobile() ? OsceDayMobileEditView.instance() : OsceDayEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(OsceDayProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new OsceDayEditActivityWrapper(requests, ScaffoldApp.isMobile() ? OsceDayMobileEditView.instance() : OsceDayEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        OsceDayEditView.instance().setCreating(false);
        EntityProxyId<OsceDayProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<OsceDayProxy>(proxyId, requests, ScaffoldApp.isMobile() ? OsceDayMobileEditView.instance() : OsceDayEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(OsceDayProxy proxy) {
                OsceDayRequest request = requests.osceDayRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new OsceDayEditActivityWrapper(requests, ScaffoldApp.isMobile() ? OsceDayMobileEditView.instance() : OsceDayEditView.instance(), activity, proxyId);
    }
}
