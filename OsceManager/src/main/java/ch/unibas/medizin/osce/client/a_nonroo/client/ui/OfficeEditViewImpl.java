
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.Arrays;

import ch.unibas.medizin.osce.client.i18n.Messages;
import ch.unibas.medizin.osce.client.managed.request.OfficeProxy;
import ch.unibas.medizin.osce.shared.Gender;
//import ch.unibas.medizin.osce.client.shared.Gender;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class OfficeEditViewImpl extends Composite implements OfficeEditView, Editor<OfficeProxy> {

	private static final Binder BINDER = GWT.create(Binder.class);

	private static OfficeEditView instance;
	
	@UiField(provided=true)
	ValueListBox<Gender> gender = new ValueListBox<Gender>(new AbstractRenderer<ch.unibas.medizin.osce.shared.Gender>() {

        public String render(ch.unibas.medizin.osce.shared.Gender obj) {
            return obj == null ? "" : String.valueOf(obj);
        }
    });

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
    
    @UiField
	SpanElement labelGender;
	@UiField
	SpanElement labelTitle;
	@UiField
	SpanElement labelName;
	@UiField
	SpanElement labelPreName;
	@UiField
	SpanElement labelEmail;
	@UiField
	SpanElement labelTelephone;

	private Delegate delegate;

	public OfficeEditViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
		gender.setAcceptableValues(Arrays.asList(Gender.values()));

		labelGender.setInnerText(Messages.GENDER + ":");
		labelTitle.setInnerText(Messages.TITLE + ":");
		labelName.setInnerText(Messages.NAME + ":");
		labelPreName.setInnerText(Messages.PRENAME + ":");
		labelEmail.setInnerText(Messages.EMAIL + ":");
		labelTelephone.setInnerText(Messages.TELEPHONE + ":");
	}
    
	interface Binder extends UiBinder<Widget, OfficeEditViewImpl> {
	}

	interface Driver extends RequestFactoryEditorDriver<OfficeProxy, OfficeEditViewImpl> {
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate=delegate;
	}

	@Override
	public RequestFactoryEditorDriver<OfficeProxy, OfficeEditViewImpl> createEditorDriver() {
		RequestFactoryEditorDriver<OfficeProxy, OfficeEditViewImpl> driver = GWT.create(Driver.class);
		driver.initialize(this);
		return driver;
	}
	


}
