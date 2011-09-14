package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.BankaccountProxy;
import ch.unibas.medizin.osce.client.managed.request.DescriptionProxy;
import ch.unibas.medizin.osce.client.managed.request.LangSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.managed.request.ProfessionProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientRequest;
import ch.unibas.medizin.osce.client.managed.ui.LangSkillSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.StandardizedPatientDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.StandardizedPatientEditView;
import ch.unibas.medizin.osce.client.managed.ui.StandardizedPatientListView;
import ch.unibas.medizin.osce.client.managed.ui.StandardizedPatientMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.StandardizedPatientMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import ch.unibas.medizin.osce.shared.Gender;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import java.util.Set;

public class StandardizedPatientActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public StandardizedPatientActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new StandardizedPatientDetailsActivity((EntityProxyId<StandardizedPatientProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? StandardizedPatientMobileDetailsView.instance() : StandardizedPatientDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<StandardizedPatientProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        StandardizedPatientEditView.instance().setCreating(true);
        final StandardizedPatientRequest request = requests.standardizedPatientRequest();
        Activity activity = new CreateAndEditProxy<StandardizedPatientProxy>(StandardizedPatientProxy.class, request, ScaffoldApp.isMobile() ? StandardizedPatientMobileEditView.instance() : StandardizedPatientEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(StandardizedPatientProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new StandardizedPatientEditActivityWrapper(requests, ScaffoldApp.isMobile() ? StandardizedPatientMobileEditView.instance() : StandardizedPatientEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        StandardizedPatientEditView.instance().setCreating(false);
        EntityProxyId<StandardizedPatientProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<StandardizedPatientProxy>(proxyId, requests, ScaffoldApp.isMobile() ? StandardizedPatientMobileEditView.instance() : StandardizedPatientEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(StandardizedPatientProxy proxy) {
                StandardizedPatientRequest request = requests.standardizedPatientRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new StandardizedPatientEditActivityWrapper(requests, ScaffoldApp.isMobile() ? StandardizedPatientMobileEditView.instance() : StandardizedPatientEditView.instance(), activity, proxyId);
    }
}
