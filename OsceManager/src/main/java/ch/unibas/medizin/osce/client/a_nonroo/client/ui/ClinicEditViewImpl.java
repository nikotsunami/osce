

package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.Collection;
import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.ui.DoctorSetEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ClinicEditViewImpl extends Composite implements ClinicEditView, Editor<ClinicProxy> {


	private static final Binder BINDER = GWT.create(Binder.class);

	private static ClinicEditView instance;

	@UiField
	Element editTitle;

	@UiField
	Element createTitle;
	
	  @UiField
	    TextBox name;

	    @UiField
	    TextBox street;

	    @UiField
	    TextBox city;

	    @UiField
	    IntegerBox postalCode;

	    @UiField
	    DoctorSetEditor doctors;
	
		@UiField
		Button cancel;

		@UiField
		Button save;
		
		@UiField
		DivElement errors;
	    
	private Delegate delegate;

	private Presenter presenter;

	
	public ClinicEditViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
	}


	@Override
	public RequestFactoryEditorDriver<ClinicProxy, ClinicEditViewImpl> createEditorDriver() {
		RequestFactoryEditorDriver<ClinicProxy, ClinicEditViewImpl> driver = GWT.create(Driver.class);
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

	interface Binder extends UiBinder<Widget, ClinicEditViewImpl> {
	}

	interface Driver extends RequestFactoryEditorDriver<ClinicProxy, ClinicEditViewImpl> {
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

	@Override
    public void setDoctorsPickerValues(Collection<DoctorProxy> values) {
        doctors.setAcceptableValues(values);
    }
	
}
