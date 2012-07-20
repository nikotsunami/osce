

package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.HashMap;
import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.DescriptionProxy;
import ch.unibas.medizin.osce.client.style.widgets.richtext.RichTextToolbar;
//import ch.unibas.medizin.osce.client.shared.Gender;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Widget;

public class DescriptionEditViewImpl extends Composite implements DescriptionEditView, Editor<DescriptionProxy> {

	private static final Binder BINDER = GWT.create(Binder.class);

	@UiField
	DivElement errors;

//	@UiField
//	Element editTitle;
//
//	@UiField
//	Element createTitle;
	
	@UiField (provided=true)
	RichTextToolbar toolbar;

	@UiField (provided = true)
	RichTextArea description;

	private Delegate delegate;

	// Highlight onViolation
	Map<String, Widget> descriptionMap;
	// E Highlight onViolation
	
	public DescriptionEditViewImpl() {
		description = new RichTextArea();
		description.setSize("100%", "14em");
		toolbar = new RichTextToolbar(description);
		toolbar.setWidth("100%");
		initWidget(BINDER.createAndBindUi(this));
//		toolbar.setRichText(description);
		
		// Highlight onViolation
		descriptionMap=new HashMap<String, Widget>();
		descriptionMap.put("description", description);
		// E Highlight onViolation
	}

	interface Binder extends UiBinder<Widget, DescriptionEditViewImpl> {
	}

	interface Driver extends RequestFactoryEditorDriver<DescriptionProxy, DescriptionEditViewImpl> {
	}

	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public RequestFactoryEditorDriver<DescriptionProxy, DescriptionEditViewImpl> createEditorDriver() {
		RequestFactoryEditorDriver<DescriptionProxy, DescriptionEditViewImpl> driver = GWT.create(Driver.class);
		driver.initialize(this);
		return driver;
	}

	@Override
	public String getDescriptionContent() {
		// TODO Auto-generated method stub
		return description.getHTML();
	}

	@Override
	public void setDescriptionContent(String description) {
		// TODO Auto-generated method stub
		this.description.setHTML(description);
	}

	@Override
	public Map getDescriptionMap() {
		// TODO Auto-generated method stub
		return this.descriptionMap;
	}
}
