

package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.Arrays;
import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
import ch.unibas.medizin.osce.client.managed.request.DescriptionProxy;
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
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class DescriptionEditViewImpl extends Composite implements DescriptionEditView, Editor<DescriptionProxy> {

	private static final Binder BINDER = GWT.create(Binder.class);

	private static DescriptionEditView instance;

	@UiField
	DivElement errors;

//	@UiField
//	Element editTitle;
//
//	@UiField
//	Element createTitle;

	@UiField
	RichTextArea description;

	private Delegate delegate;

	public DescriptionEditViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
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
}
