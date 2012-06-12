// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.activity.ChecklistCriteriaEditActivityWrapper.View;
import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.ChecklistCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.scaffold.activity.IsScaffoldMobileActivity;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyEditView;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyListPlace;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class ChecklistCriteriaEditActivityWrapper_Roo_Gwt implements Activity, IsScaffoldMobileActivity {

    protected Activity wrapped;

    protected View<?> view;

    protected ApplicationRequestFactory requests;

    @Override
    public void start(AcceptsOneWidget display, EventBus eventBus) {
        view.setChecklistQuestionPickerValues(Collections.<ChecklistQuestionProxy>emptyList());
        requests.checklistQuestionRequest().findChecklistQuestionEntries(0, 50).with(ch.unibas.medizin.osce.client.managed.ui.ChecklistQuestionProxyRenderer.instance().getPaths()).fire(new Receiver<List<ChecklistQuestionProxy>>() {

            public void onSuccess(List<ChecklistQuestionProxy> response) {
                List<ChecklistQuestionProxy> values = new ArrayList<ChecklistQuestionProxy>();
                values.add(null);
                values.addAll(response);
                view.setChecklistQuestionPickerValues(values);
            }
        });
        wrapped.start(display, eventBus);
    }

    public interface View_Roo_Gwt<V extends ch.unibas.medizin.osce.client.scaffold.place.ProxyEditView<ch.unibas.medizin.osce.client.managed.request.ChecklistCriteriaProxy, V>> extends ProxyEditView<ChecklistCriteriaProxy, V> {

        void setChecklistQuestionPickerValues(Collection<ChecklistQuestionProxy> values);
    }
}