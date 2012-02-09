package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.TaskProxy;
import ch.unibas.medizin.osce.client.managed.request.TaskRequest;
import ch.unibas.medizin.osce.client.managed.ui.TaskDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.TaskEditView;
import ch.unibas.medizin.osce.client.managed.ui.TaskListView;
import ch.unibas.medizin.osce.client.managed.ui.TaskMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.TaskMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;

public class TaskActivitiesMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;

    public TaskActivitiesMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
                return new TaskDetailsActivity((EntityProxyId<TaskProxy>) place.getProxyId(), requests, placeController, ScaffoldApp.isMobile() ? TaskMobileDetailsView.instance() : TaskDetailsView.instance());
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }

    @SuppressWarnings("unchecked")
    private EntityProxyId<ch.unibas.medizin.osce.client.managed.request.TaskProxy> coerceId(ProxyPlace place) {
        return (EntityProxyId<TaskProxy>) place.getProxyId();
    }

    private Activity makeCreateActivity() {
        TaskEditView.instance().setCreating(true);
        final TaskRequest request = requests.taskRequest();
        Activity activity = new CreateAndEditProxy<TaskProxy>(TaskProxy.class, request, ScaffoldApp.isMobile() ? TaskMobileEditView.instance() : TaskEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(TaskProxy proxy) {
                request.persist().using(proxy);
                return request;
            }
        };
        return new TaskEditActivityWrapper(requests, ScaffoldApp.isMobile() ? TaskMobileEditView.instance() : TaskEditView.instance(), activity, null);
    }

    private Activity makeEditActivity(ProxyPlace place) {
        TaskEditView.instance().setCreating(false);
        EntityProxyId<TaskProxy> proxyId = coerceId(place);
        Activity activity = new FindAndEditProxy<TaskProxy>(proxyId, requests, ScaffoldApp.isMobile() ? TaskMobileEditView.instance() : TaskEditView.instance(), placeController) {

            @Override
            protected RequestContext createSaveRequest(TaskProxy proxy) {
                TaskRequest request = requests.taskRequest();
                request.persist().using(proxy);
                return request;
            }
        };
        return new TaskEditActivityWrapper(requests, ScaffoldApp.isMobile() ? TaskMobileEditView.instance() : TaskEditView.instance(), activity, proxyId);
    }
}
