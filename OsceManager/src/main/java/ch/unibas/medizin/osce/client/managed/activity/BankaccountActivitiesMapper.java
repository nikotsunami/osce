package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.BankaccountProxy;
import ch.unibas.medizin.osce.client.managed.request.BankaccountRequest;
import ch.unibas.medizin.osce.client.managed.ui.BankaccountDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.BankaccountEditView;
import ch.unibas.medizin.osce.client.managed.ui.BankaccountListView;
import ch.unibas.medizin.osce.client.managed.ui.BankaccountMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.BankaccountMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class BankaccountActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public BankaccountActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new BankaccountDetailsActivity((EntityProxyId<BankaccountProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? BankaccountMobileDetailsView.instance() : BankaccountDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.BankaccountProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<BankaccountProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        BankaccountEditView.instance().setCreating(true);
        final BankaccountRequest request = requests.bankaccountRequest();
        Activity activity = new CreateAndEditProxy<BankaccountProxy>(BankaccountProxy.class, request, ScaffoldApp.isMobile() ? BankaccountMobileEditView.instance() : BankaccountEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(BankaccountProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new BankaccountEditActivityWrapper(requests, ScaffoldApp.isMobile() ? BankaccountMobileEditView.instance() : BankaccountEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        BankaccountEditView.instance().setCreating(false);
        EntityProxyId<BankaccountProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<BankaccountProxy>(proxyId, requests, ScaffoldApp.isMobile() ? BankaccountMobileEditView.instance() : BankaccountEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(BankaccountProxy proxy) {
                BankaccountRequest request = requests.bankaccountRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new BankaccountEditActivityWrapper(requests, ScaffoldApp.isMobile() ? BankaccountMobileEditView.instance() : BankaccountEditView.instance(), activity, proxyId);
    }
}
