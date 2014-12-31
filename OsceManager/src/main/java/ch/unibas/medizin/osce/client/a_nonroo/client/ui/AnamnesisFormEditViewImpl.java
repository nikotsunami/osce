

package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisChecksValueSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.ScarSetEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class AnamnesisFormEditViewImpl extends Composite implements AnamnesisFormEditView, Editor<AnamnesisFormProxy> {


	private static final Binder BINDER = GWT.create(Binder.class);

	private static AnamnesisFormEditView instance;

	@UiField
	Element editTitle;

	@UiField
	Element createTitle;
	
    @UiField
    DateBox createDate;

    @UiField
    AnamnesisChecksValueSetEditor anamnesischecksvalues;

    @UiField
    ScarSetEditor scars;
    
		@UiField
		Button cancel;

		@UiField
		Button save;
		
		@UiField
		DivElement errors;
	    
	private Delegate delegate;

	private Presenter presenter;

	
	public AnamnesisFormEditViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
	}


	@Override
	public RequestFactoryEditorDriver<AnamnesisFormProxy, AnamnesisFormEditViewImpl> createEditorDriver() {
		RequestFactoryEditorDriver<AnamnesisFormProxy, AnamnesisFormEditViewImpl> driver = GWT.create(Driver.class);
		driver.initialize(this);
		return driver;
	}

	public void setCreating(boolean creating) {
		if (creating) {
			editTitle.getStyle().setDisplay(Display.NONE);
			createTitle.getStyle().clearDisplay();
		} else {
			editTitle.getStyle().clearDisplay();
			createTitle.getStyle().setDisplay(Display.NONE);
		}
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

	@UiHandler("cancel")
	void onCancel(ClickEvent event) {
		delegate.cancelClicked();
	}

	@UiHandler("save")
	void onSave(ClickEvent event) {
		delegate.saveClicked();
	}

	interface Binder extends UiBinder<Widget, AnamnesisFormEditViewImpl> {
	}

	interface Driver extends RequestFactoryEditorDriver<AnamnesisFormProxy, AnamnesisFormEditViewImpl> {
	}

	@Override
	public void setEditTitle(boolean edit) {
		
		if (edit) {
            editTitle.getStyle().clearDisplay();
            createTitle.getStyle().setDisplay(Display.NONE);
        } else {
            editTitle.getStyle().setDisplay(Display.NONE);
            createTitle.getStyle().clearDisplay();
        }

	}

	@Override
	public void setPresenter(Presenter doctorEditActivity) {
		this.presenter=doctorEditActivity;
	}
	
}
