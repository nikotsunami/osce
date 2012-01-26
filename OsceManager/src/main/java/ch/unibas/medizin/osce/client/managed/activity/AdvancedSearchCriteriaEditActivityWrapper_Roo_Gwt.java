// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.activity.AdvancedSearchCriteriaEditActivityWrapper.View;
import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.scaffold.activity.IsScaffoldMobileActivity;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyEditView;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyListPlace;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison2;
import ch.unibas.medizin.osce.shared.PossibleFields;
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

public abstract class AdvancedSearchCriteriaEditActivityWrapper_Roo_Gwt implements Activity, IsScaffoldMobileActivity {

    protected Activity wrapped;

    protected View<?> view;

    protected ApplicationRequestFactory requests;

    @Override
    public void start(AcceptsOneWidget display, EventBus eventBus) {
        view.setFieldPickerValues(Arrays.asList(PossibleFields.values()));
        view.setBindTypePickerValues(Arrays.asList(BindType.values()));
        view.setComparationPickerValues(Arrays.asList(Comparison2.values()));
        wrapped.start(display, eventBus);
    }

    public interface View_Roo_Gwt<V extends ch.unibas.medizin.osce.client.scaffold.place.ProxyEditView<ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy, V>> extends ProxyEditView<AdvancedSearchCriteriaProxy, V> {

        void setFieldPickerValues(Collection<PossibleFields> values);

        void setBindTypePickerValues(Collection<BindType> values);

        void setComparationPickerValues(Collection<Comparison2> values);
    }
}
