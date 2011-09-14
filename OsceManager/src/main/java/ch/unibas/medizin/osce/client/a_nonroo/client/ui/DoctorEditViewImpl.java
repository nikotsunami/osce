

package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
//import ch.unibas.medizin.osce.client.shared.Gender;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class DoctorEditViewImpl extends Composite implements DoctorEditView, Editor<DoctorProxy> {


	private static final Binder BINDER = GWT.create(Binder.class);

	private static DoctorEditView instance;

	@UiField
	Button cancel;

	@UiField
	Button save;

	@UiField
	DivElement errors;

	@UiField
	Element editTitle;

	@UiField
	Element createTitle;

//	@UiField(provided=true)
//	ValueListBox<Gender> gender = new ValueListBox<Gender>(new AbstractRenderer<ch.unibas.medizin.osce.client.shared.Gender>() {
//
//        public String render(ch.unibas.medizin.osce.client.shared.Gender obj) {
//            return obj == null ? "" : String.valueOf(obj);
//        }
//    });

	@UiField
	TextBox title;

	@UiField
	TextBox name;

	@UiField
	TextBox preName;

	@UiField
	TextBox email;

	@UiField
	TextBox telephone;

	@UiField(provided = true)
	ValueListBox<ClinicProxy> clinic = new ValueListBox<ClinicProxy>(ch.unibas.medizin.osce.client.managed.ui.ClinicProxyRenderer.instance(), new com.google.gwt.requestfactory.ui.client.EntityProxyKeyProvider<ch.unibas.medizin.osce.client.managed.request.ClinicProxy>());

	@UiField
	SimplePanel officePanel;
	
	@Override
	public SimplePanel getOfficePanel(){
		return officePanel;
	}
	
	private Delegate delegate;

	private Presenter presenter;

	
	public DoctorEditViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
//		gender.setAcceptableValues(Arrays.asList(Gender.values()));
	}


	@Override
	public RequestFactoryEditorDriver<DoctorProxy, DoctorEditViewImpl> createEditorDriver() {
		RequestFactoryEditorDriver<DoctorProxy, DoctorEditViewImpl> driver = GWT.create(Driver.class);
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

	interface Binder extends UiBinder<Widget, DoctorEditViewImpl> {
	}

	interface Driver extends RequestFactoryEditorDriver<DoctorProxy, DoctorEditViewImpl> {
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
	public void setClinicPickerValues(Collection<ClinicProxy> clinicList) {
		clinic.setAcceptableValues(clinicList);
	}
	
}
