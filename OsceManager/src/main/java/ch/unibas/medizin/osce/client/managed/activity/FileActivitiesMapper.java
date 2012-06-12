package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.FileProxy;
import ch.unibas.medizin.osce.client.managed.request.FileRequest;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.managed.ui.FileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.FileEditView;
import ch.unibas.medizin.osce.client.managed.ui.FileListView;
import ch.unibas.medizin.osce.client.managed.ui.FileMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.FileMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class FileActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public FileActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new FileDetailsActivity((EntityProxyId<FileProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? FileMobileDetailsView.instance() : FileDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.FileProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<FileProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        FileEditView.instance().setCreating(true);
        final FileRequest request = requests.fileRequest();
        Activity activity = new CreateAndEditProxy<FileProxy>(FileProxy.class, request, ScaffoldApp.isMobile() ? FileMobileEditView.instance() : FileEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(FileProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new FileEditActivityWrapper(requests, ScaffoldApp.isMobile() ? FileMobileEditView.instance() : FileEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        FileEditView.instance().setCreating(false);
        EntityProxyId<FileProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<FileProxy>(proxyId, requests, ScaffoldApp.isMobile() ? FileMobileEditView.instance() : FileEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(FileProxy proxy) {
                FileRequest request = requests.fileRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new FileEditActivityWrapper(requests, ScaffoldApp.isMobile() ? FileMobileEditView.instance() : FileEditView.instance(), activity, proxyId);
    }
}
