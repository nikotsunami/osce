// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.LogEntryProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyDetailsView;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyListView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class LogEntryDetailsView_Roo_Gwt extends Composite implements ProxyDetailsView<LogEntryProxy> {

    @UiField
    SpanElement id;

    @UiField
    SpanElement version;

    @UiField
    SpanElement shibId;

    @UiField
    SpanElement logtime;

    @UiField
    SpanElement oldValue;

    @UiField
    SpanElement newValue;

    LogEntryProxy proxy;

    @UiField
    SpanElement displayRenderer;

    public void setValue(LogEntryProxy proxy) {
        this.proxy = proxy;
        id.setInnerText(proxy.getId() == null ? "" : String.valueOf(proxy.getId()));
        version.setInnerText(proxy.getVersion() == null ? "" : String.valueOf(proxy.getVersion()));
        shibId.setInnerText(proxy.getShibId() == null ? "" : String.valueOf(proxy.getShibId()));
        logtime.setInnerText(proxy.getLogtime() == null ? "" : DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM).format(proxy.getLogtime()));
        oldValue.setInnerText(proxy.getOldValue() == null ? "" : String.valueOf(proxy.getOldValue()));
        newValue.setInnerText(proxy.getNewValue() == null ? "" : String.valueOf(proxy.getNewValue()));
        displayRenderer.setInnerText(LogEntryProxyRenderer.instance().render(proxy));
    }
}
