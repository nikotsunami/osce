// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentOscesProxy;
import ch.unibas.medizin.osce.client.managed.request.TaskProxy;
import ch.unibas.medizin.osce.client.managed.ui.OsceDaySetEditor;
import ch.unibas.medizin.osce.client.managed.ui.OscePostBlueprintListEditor;
import ch.unibas.medizin.osce.client.managed.ui.StudentOscesSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.TaskSetEditor;
import ch.unibas.medizin.osce.client.scaffold.activity.IsScaffoldMobileActivity;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyDetailsView;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyDetailsView.Delegate;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyListPlace;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace.Operation;
import ch.unibas.medizin.osce.shared.OsceStatus;
import ch.unibas.medizin.osce.shared.StudyYears;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxy;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import java.util.List;
import java.util.Set;

public abstract class OsceDetailsActivity_Roo_Gwt extends AbstractActivity implements Delegate, IsScaffoldMobileActivity {

    protected ApplicationRequestFactory requests;

    protected EntityProxyId<OsceProxy> proxyId;

    protected void find(Receiver<EntityProxy> callback) {
        requests.find(proxyId).with("semester", "osce_days", "oscePostBlueprints", "tasks", "osceStudents", "copiedOsce").fire(callback);
    }
}
