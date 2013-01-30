

package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.AdministratorProxy;

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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AdministratorEditViewImpl extends Composite implements AdministratorEditView, Editor<AdministratorProxy> {


	private static final Binder BINDER = GWT.create(Binder.class);

	private static AdministratorEditView instance;

	@UiField
	Element editTitle;

	@UiField
	Element createTitle;
	
    @UiField
    TextBox name;

    @UiField
    TextBox preName;

    @UiField
    TextBox email;

 //   @UiField
 //   ResponsibilitySetEditor responsibilities;
    
		@UiField
		Button cancel;

		@UiField
		Button save;
		
		@UiField
		DivElement errors;
	    
	private Delegate delegate;

	private Presenter presenter;

	// Highlight onViolation
	Map<String, Widget> administratorMap;
	// E Highlight onViolation
	
	public AdministratorEditViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
		
		// Highlight onViolation
		administratorMap=new HashMap<String, Widget>();
		administratorMap.put("name", name);
		administratorMap.put("preName", preName);
		administratorMap.put("email", email);
		// E Highlight onViolation
	}


	@Override
	public RequestFactoryEditorDriver<AdministratorProxy, AdministratorEditViewImpl> createEditorDriver() {
		RequestFactoryEditorDriver<AdministratorProxy, AdministratorEditViewImpl> driver = GWT.create(Driver.class);
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

	interface Binder extends UiBinder<Widget, AdministratorEditViewImpl> {
	}

	interface Driver extends RequestFactoryEditorDriver<AdministratorProxy, AdministratorEditViewImpl> {
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

	// Highlight onViolation
	@Override
	public Map getAdministratorMap() 
	{
		return this.administratorMap;
	}
	// E Highlight onViolation
}
