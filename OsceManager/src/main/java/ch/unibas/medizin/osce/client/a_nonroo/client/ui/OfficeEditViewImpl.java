
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.managed.request.OfficeProxy;
import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

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
	
	@UiField(provided=true)
	public ValueListBox<Gender> gender = new ValueListBox<Gender>(new AbstractRenderer<ch.unibas.medizin.osce.shared.Gender>() {
		EnumRenderer<Gender> renderer = new EnumRenderer<Gender>();
        public String render(ch.unibas.medizin.osce.shared.Gender obj) {
            return obj == null ? "" : renderer.render(obj);
        }
    });

    @UiField
   public  TextBox title;
    @UiField
    public TextBox name;
    @UiField
    public TextBox preName;
    @UiField
    public TextBox email;
    @UiField
    public TextBox telephone;
    
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
	
	// Highlight onViolation
	Map<String, Widget> officeMap;
	// E Highlight onViolation

	

	public OfficeEditViewImpl() {
		OsceConstants constants = GWT.create(OsceConstants.class);
		initWidget(BINDER.createAndBindUi(this));
		gender.setAcceptableValues(Arrays.asList(Gender.values()));

		labelGender.setInnerText(constants.gender() + ":");
		labelTitle.setInnerText(constants.title() + ":");
		labelName.setInnerText(constants.name() + ":");
		labelPreName.setInnerText(constants.preName() + ":");
		labelEmail.setInnerText(constants.email() + ":");
		labelTelephone.setInnerText(constants.telephone() + ":");
		
		// Highlight onViolation
		officeMap=new HashMap<String, Widget>();
		officeMap.put("gender",gender);		
		officeMap.put("title",title);
		officeMap.put("name",name);
		officeMap.put("preName",preName);
		officeMap.put("email",email);
		officeMap.put("telephone",telephone);	
		// E Highlight onViolation
		
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

	// Highlight onViolation
	@Override
	public Map getOfficeMap() 
	{		
		return this.officeMap;
	}
	// E Highlight onViolation	
}
