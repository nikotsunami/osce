package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.CheckListProxy;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldMobileApp;
import ch.unibas.medizin.osce.client.scaffold.activity.IsScaffoldMobileActivity;
import ch.unibas.medizin.osce.client.scaffold.place.AbstractProxyListActivity;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyListView;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.view.client.Range;
import java.util.List;

public class CheckListListActivity extends AbstractProxyListActivity<CheckListProxy> implements IsScaffoldMobileActivity {

    private final ApplicationRequestFactory requests;

    public CheckListListActivity(ApplicationRequestFactory requests, ProxyListView<ch.unibas.medizin.osce.client.managed.request.CheckListProxy> view, PlaceController placeController) {
        super(placeController, view, CheckListProxy.class);
        this.requests = requests;
    }

    public Place getBackButtonPlace() {
        return ScaffoldMobileApp.ROOT_PLACE;
    }

    public String getBackButtonText() {
        return "Entities";
    }

    public Place getEditButtonPlace() {
        return null;
    }

    public String getTitleText() {
        return "CheckLists";
    }

    public boolean hasEditButton() {
        return false;
    }

    protected Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.CheckListProxy>> createRangeRequest(Range range) {
        return requests.checkListRequest().findCheckListEntries(range.getStart(), range.getLength());
    }

    protected void fireCountRequest(Receiver<Long> callback) {
        requests.checkListRequest().countCheckLists().fire(callback);
    }
}
