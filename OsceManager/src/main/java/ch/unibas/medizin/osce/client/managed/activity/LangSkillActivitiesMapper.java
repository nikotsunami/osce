package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.LangSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.LangSkillRequest;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.ui.LangSkillDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.LangSkillEditView;
import ch.unibas.medizin.osce.client.managed.ui.LangSkillListView;
import ch.unibas.medizin.osce.client.managed.ui.LangSkillMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.LangSkillMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class LangSkillActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public LangSkillActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new LangSkillDetailsActivity((EntityProxyId<LangSkillProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? LangSkillMobileDetailsView.instance() : LangSkillDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.LangSkillProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<LangSkillProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        LangSkillEditView.instance().setCreating(true);
        final LangSkillRequest request = requests.langSkillRequest();
        Activity activity = new CreateAndEditProxy<LangSkillProxy>(LangSkillProxy.class, request, ScaffoldApp.isMobile() ? LangSkillMobileEditView.instance() : LangSkillEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(LangSkillProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new LangSkillEditActivityWrapper(requests, ScaffoldApp.isMobile() ? LangSkillMobileEditView.instance() : LangSkillEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        LangSkillEditView.instance().setCreating(false);
        EntityProxyId<LangSkillProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<LangSkillProxy>(proxyId, requests, ScaffoldApp.isMobile() ? LangSkillMobileEditView.instance() : LangSkillEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(LangSkillProxy proxy) {
                LangSkillRequest request = requests.langSkillRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new LangSkillEditActivityWrapper(requests, ScaffoldApp.isMobile() ? LangSkillMobileEditView.instance() : LangSkillEditView.instance(), activity, proxyId);
    }
}
