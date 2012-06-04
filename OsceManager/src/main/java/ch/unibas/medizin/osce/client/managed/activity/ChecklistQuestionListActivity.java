package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.ChecklistCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistOptionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicProxy;
import ch.unibas.medizin.osce.client.managed.ui.ChecklistCriteriaSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.ChecklistOptionSetEditor;
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
import java.util.Set;

public class ChecklistQuestionListActivity extends AbstractProxyListActivity<ChecklistQuestionProxy> implements IsScaffoldMobileActivity {

    private final ApplicationRequestFactory requests;

    public ChecklistQuestionListActivity(ApplicationRequestFactory requests, ProxyListView<ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy> view, PlaceController placeController) {
        super(placeController, view, ChecklistQuestionProxy.class);
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
        return "ChecklistQuestions";
    }

    public boolean hasEditButton() {
        return false;
    }

    protected Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy>> createRangeRequest(Range range) {
        return requests.checklistQuestionRequest().findChecklistQuestionEntries(range.getStart(), range.getLength());
    }

    protected void fireCountRequest(Receiver<Long> callback) {
        requests.checklistQuestionRequest().countChecklistQuestions().fire(callback);
    }
}
