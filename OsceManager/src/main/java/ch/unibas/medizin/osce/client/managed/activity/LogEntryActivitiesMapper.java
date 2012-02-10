package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.LogEntryProxy;
import ch.unibas.medizin.osce.client.managed.request.LogEntryRequest;
import ch.unibas.medizin.osce.client.managed.ui.LogEntryDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.LogEntryEditView;
import ch.unibas.medizin.osce.client.managed.ui.LogEntryListView;
import ch.unibas.medizin.osce.client.managed.ui.LogEntryMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.LogEntryMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class LogEntryActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public LogEntryActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new LogEntryDetailsActivity((EntityProxyId<LogEntryProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? LogEntryMobileDetailsView.instance() : LogEntryDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.LogEntryProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<LogEntryProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        LogEntryEditView.instance().setCreating(true);
        final LogEntryRequest request = requests.logEntryRequest();
        Activity activity = new CreateAndEditProxy<LogEntryProxy>(LogEntryProxy.class, request, ScaffoldApp.isMobile() ? LogEntryMobileEditView.instance() : LogEntryEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(LogEntryProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new LogEntryEditActivityWrapper(requests, ScaffoldApp.isMobile() ? LogEntryMobileEditView.instance() : LogEntryEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        LogEntryEditView.instance().setCreating(false);
        EntityProxyId<LogEntryProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<LogEntryProxy>(proxyId, requests, ScaffoldApp.isMobile() ? LogEntryMobileEditView.instance() : LogEntryEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(LogEntryProxy proxy) {
                LogEntryRequest request = requests.logEntryRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new LogEntryEditActivityWrapper(requests, ScaffoldApp.isMobile() ? LogEntryMobileEditView.instance() : LogEntryEditView.instance(), activity, proxyId);
    }
}
