package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.LangSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageRequest;
import ch.unibas.medizin.osce.client.managed.ui.LangSkillSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.SpokenLanguageDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.SpokenLanguageEditView;
import ch.unibas.medizin.osce.client.managed.ui.SpokenLanguageListView;
import ch.unibas.medizin.osce.client.managed.ui.SpokenLanguageMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.SpokenLanguageMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import java.util.Set;

public class SpokenLanguageActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public SpokenLanguageActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new SpokenLanguageDetailsActivity((EntityProxyId<SpokenLanguageProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? SpokenLanguageMobileDetailsView.instance() : SpokenLanguageDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<SpokenLanguageProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        SpokenLanguageEditView.instance().setCreating(true);
        final SpokenLanguageRequest request = requests.spokenLanguageRequest();
        Activity activity = new CreateAndEditProxy<SpokenLanguageProxy>(SpokenLanguageProxy.class, request, ScaffoldApp.isMobile() ? SpokenLanguageMobileEditView.instance() : SpokenLanguageEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(SpokenLanguageProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new SpokenLanguageEditActivityWrapper(requests, ScaffoldApp.isMobile() ? SpokenLanguageMobileEditView.instance() : SpokenLanguageEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        SpokenLanguageEditView.instance().setCreating(false);
        EntityProxyId<SpokenLanguageProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<SpokenLanguageProxy>(proxyId, requests, ScaffoldApp.isMobile() ? SpokenLanguageMobileEditView.instance() : SpokenLanguageEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(SpokenLanguageProxy proxy) {
                SpokenLanguageRequest request = requests.spokenLanguageRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new SpokenLanguageEditActivityWrapper(requests, ScaffoldApp.isMobile() ? SpokenLanguageMobileEditView.instance() : SpokenLanguageEditView.instance(), activity, proxyId);
    }
}
