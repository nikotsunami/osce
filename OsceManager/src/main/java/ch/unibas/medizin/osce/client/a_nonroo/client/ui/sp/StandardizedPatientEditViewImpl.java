

package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.BankaccountProxy;
import ch.unibas.medizin.osce.client.managed.request.DescriptionProxy;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.managed.request.ProfessionProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.ui.DoctorSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.LangSkillSetEditor;
import ch.unibas.medizin.osce.shared.Gender;
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
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class StandardizedPatientEditViewImpl extends Composite implements StandardizedPatientEditView, Editor<StandardizedPatientProxy> {

	private static final Binder BINDER = GWT.create(Binder.class);

	private static StandardizedPatientEditView instance;

	@UiField
	Element editTitle;

	@UiField
	Element createTitle;
	
    @UiField(provided = true)
    ValueListBox<Gender> gender = new ValueListBox<Gender>(new AbstractRenderer<ch.unibas.medizin.osce.shared.Gender>() {

        public String render(ch.unibas.medizin.osce.shared.Gender obj) {
            return obj == null ? "" : String.valueOf(obj);
        }
    });

    @UiField
    TextBox name;

    @UiField
    TextBox preName;

    @UiField
    TextBox street;

    @UiField
    TextBox city;

    @UiField
    IntegerBox postalCode;

    @UiField
    TextBox telephone;
    
    @UiField
    TextBox telephone2;

    @UiField
    TextBox mobile;

    @UiField
    DateBox birthday;

    @UiField
    TextBox email;

    @UiField(provided = true)
    ValueListBox<NationalityProxy> nationality = new ValueListBox<NationalityProxy>(ch.unibas.medizin.osce.client.managed.ui.NationalityProxyRenderer.instance(), new com.google.gwt.requestfactory.ui.client.EntityProxyKeyProvider<ch.unibas.medizin.osce.client.managed.request.NationalityProxy>());

    @UiField(provided = true)
    ValueListBox<ProfessionProxy> profession = new ValueListBox<ProfessionProxy>(ch.unibas.medizin.osce.client.managed.ui.ProfessionProxyRenderer.instance(), new com.google.gwt.requestfactory.ui.client.EntityProxyKeyProvider<ch.unibas.medizin.osce.client.managed.request.ProfessionProxy>());

    @UiField
    LangSkillSetEditor langskills;

    @UiField(provided = true)
    ValueListBox<BankaccountProxy> bankAccount = new ValueListBox<BankaccountProxy>(ch.unibas.medizin.osce.client.managed.ui.BankaccountProxyRenderer.instance(), new com.google.gwt.requestfactory.ui.client.EntityProxyKeyProvider<ch.unibas.medizin.osce.client.managed.request.BankaccountProxy>());


	@UiField
	SimplePanel descriptionPanel;
	
    @UiField(provided = true)
    ValueListBox<AnamnesisFormProxy> anamnesisForm = new ValueListBox<AnamnesisFormProxy>(ch.unibas.medizin.osce.client.managed.ui.AnamnesisFormProxyRenderer.instance(), new com.google.gwt.requestfactory.ui.client.EntityProxyKeyProvider<ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy>());

	
		@UiField
		Button cancel;

		@UiField
		Button save;
		
		@UiField
		DivElement errors;
	    
	private Delegate delegate;

	private Presenter presenter;

	
	public StandardizedPatientEditViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
		gender.setAcceptableValues(Arrays.asList(Gender.values()));
	}

	@Override
	public RequestFactoryEditorDriver<StandardizedPatientProxy, StandardizedPatientEditViewImpl> createEditorDriver() {
		RequestFactoryEditorDriver<StandardizedPatientProxy, StandardizedPatientEditViewImpl> driver = GWT.create(Driver.class);
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

	interface Binder extends UiBinder<Widget, StandardizedPatientEditViewImpl> {
	}

	interface Driver extends RequestFactoryEditorDriver<StandardizedPatientProxy, StandardizedPatientEditViewImpl> {
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
	public void setPresenter(Presenter standardizedPatientEditActivity) {
		this.presenter=standardizedPatientEditActivity;
	}


	@Override
	public SimplePanel getDescriptionPanel() {
		return descriptionPanel;
	}


	@Override
	public void setNationalityPickerValues(Collection<NationalityProxy> values) {
		nationality.setAcceptableValues(values);		
	}


	@Override
	public void setProfessionPickerValues(Collection<ProfessionProxy> values) {
		profession.setAcceptableValues(values);		
	}


	@Override
	public void setBankaccountPickerValues(Collection<BankaccountProxy> values) {
		bankAccount.setAcceptableValues(values);		
	}


	@Override
	public void setAnamnesisFormPickerValues(Collection<AnamnesisFormProxy> values) {
		anamnesisForm.setAcceptableValues(values);		
	}
}
