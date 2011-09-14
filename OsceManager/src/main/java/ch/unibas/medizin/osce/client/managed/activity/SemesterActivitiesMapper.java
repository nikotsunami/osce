package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.AdministratorProxy;
import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterRequest;
import ch.unibas.medizin.osce.client.managed.ui.AdministratorSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.OsceSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.PatientInSemesterSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.SemesterDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.SemesterEditView;
import ch.unibas.medizin.osce.client.managed.ui.SemesterListView;
import ch.unibas.medizin.osce.client.managed.ui.SemesterMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.SemesterMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import ch.unibas.medizin.osce.shared.Semesters;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import java.util.Set;

public class SemesterActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public SemesterActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new SemesterDetailsActivity((EntityProxyId<SemesterProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? SemesterMobileDetailsView.instance() : SemesterDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.SemesterProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<SemesterProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        SemesterEditView.instance().setCreating(true);
        final SemesterRequest request = requests.semesterRequest();
        Activity activity = new CreateAndEditProxy<SemesterProxy>(SemesterProxy.class, request, ScaffoldApp.isMobile() ? SemesterMobileEditView.instance() : SemesterEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(SemesterProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new SemesterEditActivityWrapper(requests, ScaffoldApp.isMobile() ? SemesterMobileEditView.instance() : SemesterEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        SemesterEditView.instance().setCreating(false);
        EntityProxyId<SemesterProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<SemesterProxy>(proxyId, requests, ScaffoldApp.isMobile() ? SemesterMobileEditView.instance() : SemesterEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(SemesterProxy proxy) {
                SemesterRequest request = requests.semesterRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new SemesterEditActivityWrapper(requests, ScaffoldApp.isMobile() ? SemesterMobileEditView.instance() : SemesterEditView.instance(), activity, proxyId);
    }
}
