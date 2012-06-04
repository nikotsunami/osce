package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.ChecklistCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
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

public class ChecklistCriteriaListActivity extends AbstractProxyListActivity<ChecklistCriteriaProxy> implements IsScaffoldMobileActivity {

    private final ApplicationRequestFactory requests;

    public ChecklistCriteriaListActivity(ApplicationRequestFactory requests, ProxyListView<ch.unibas.medizin.osce.client.managed.request.ChecklistCriteriaProxy> view, PlaceController placeController) {
        super(placeController, view, ChecklistCriteriaProxy.class);
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
        return "ChecklistCriterias";
    }

    public boolean hasEditButton() {
        return false;
    }

    protected Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.ChecklistCriteriaProxy>> createRangeRequest(Range range) {
        return requests.checklistCriteriaRequest().findChecklistCriteriaEntries(range.getStart(), range.getLength());
    }

    protected void fireCountRequest(Receiver<Long> callback) {
        requests.checklistCriteriaRequest().countChecklistCriterias().fire(callback);
    }
}
