package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.Arrays;
import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisChecksValueSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.ScarSetEditor;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;
import ch.unibas.medizin.osce.shared.Gender;

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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class AnamnesisCheckEditViewImpl extends Composite implements AnamnesisCheckEditView, Editor<AnamnesisCheckProxy> {

	private static final Binder BINDER = GWT.create(Binder.class);

	private static AnamnesisCheckEditView instance;

	@UiField
	Element editTitle;

	@UiField
	Element createTitle;
	
	@UiField(provided = true)
    ValueListBox<AnamnesisCheckTypes> type = new ValueListBox<AnamnesisCheckTypes>(new AbstractRenderer<AnamnesisCheckTypes>() {

        public String render(AnamnesisCheckTypes obj) {
            return obj == null ? "" : String.valueOf(obj);
        }
    });
	
	@UiField
	TextBox text;
	
	@UiField
	TextBox value;

//	@UiField
//	DateBox createDate;
//
//	@UiField
//	AnamnesisChecksValueSetEditor anamnesischecksvalues;
//
//	@UiField
//	ScarSetEditor scars;

	@UiField
	Button cancel;

	@UiField
	Button save;

	@UiField
	DivElement errors;

	private Delegate delegate;

	private Presenter presenter;


	public AnamnesisCheckEditViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
		type.setAcceptableValues(Arrays.asList(AnamnesisCheckTypes.values()));
	}


	@Override
	public RequestFactoryEditorDriver<AnamnesisCheckProxy, AnamnesisCheckEditViewImpl> createEditorDriver() {
		RequestFactoryEditorDriver<AnamnesisCheckProxy, AnamnesisCheckEditViewImpl> driver = GWT.create(Driver.class);
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

	interface Binder extends UiBinder<Widget, AnamnesisCheckEditViewImpl> {
	}

	interface Driver extends RequestFactoryEditorDriver<AnamnesisCheckProxy, AnamnesisCheckEditViewImpl> {
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
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}
