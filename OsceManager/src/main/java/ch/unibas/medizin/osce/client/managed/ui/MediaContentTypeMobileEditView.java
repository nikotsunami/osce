package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.activity.MediaContentTypeEditActivityWrapper;
import ch.unibas.medizin.osce.client.managed.request.MediaContentTypeProxy;
import ch.unibas.medizin.osce.client.managed.ui.MediaContentTypeMobileEditView.Binder;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyEditView;
import ch.unibas.medizin.osce.client.scaffold.ui.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.requestfactory.shared.RequestFactory;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.LongBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.datepicker.client.DateBox;
import java.util.Collection;
import java.util.List;

public class MediaContentTypeMobileEditView extends MediaContentTypeMobileEditView_Roo_Gwt {

    private static final Binder BINDER = GWT.create(Binder.class);

    private static ch.unibas.medizin.osce.client.managed.ui.MediaContentTypeMobileEditView instance;

    @UiField
    Button save;

    @UiField
    DivElement errors;

    private Delegate delegate;

    public MediaContentTypeMobileEditView() {
        initWidget(BINDER.createAndBindUi(this));
    }

    public static ch.unibas.medizin.osce.client.managed.ui.MediaContentTypeMobileEditView instance() {
        if (instance == null) {
            instance = new MediaContentTypeMobileEditView();
        }
        return instance;
    }

    @Override
    public RequestFactoryEditorDriver<ch.unibas.medizin.osce.client.managed.request.MediaContentTypeProxy, ch.unibas.medizin.osce.client.managed.ui.MediaContentTypeMobileEditView> createEditorDriver() {
        RequestFactoryEditorDriver<MediaContentTypeProxy, MediaContentTypeMobileEditView> driver = GWT.create(Driver.class);
        driver.initialize(this);
        return driver;
    }

    public void setCreating(boolean creating) {
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    public void setEnabled(boolean enabled) {
        save.setEnabled(enabled);
    }

    public void showErrors(List<EditorError> errors) {
        SafeHtmlBuilder b = new SafeHtmlBuilder();
        for (EditorError error : errors) {
            b.appendEscaped(error.getPath()).appendEscaped(": ");
            b.appendEscaped(error.getMessage()).appendHtmlConstant("<br>");
        }
        this.errors.setInnerHTML(b.toSafeHtml().asString());
    }

    @UiHandler("save")
    void onSave(ClickEvent event) {
        delegate.saveClicked();
    }

    interface Binder extends UiBinder<HTMLPanel, MediaContentTypeMobileEditView> {
    }

    interface Driver extends RequestFactoryEditorDriver<MediaContentTypeProxy, MediaContentTypeMobileEditView> {
    }
}