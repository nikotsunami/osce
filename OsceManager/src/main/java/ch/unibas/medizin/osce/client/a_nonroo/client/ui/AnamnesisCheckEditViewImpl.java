package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.Arrays;
import java.util.List;

import ch.unibas.medizin.osce.client.i18n.Messages;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisChecksValueSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.ScarSetEditor;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.TabPanelHelper;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;
import ch.unibas.medizin.osce.shared.Gender;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class AnamnesisCheckEditViewImpl extends Composite implements AnamnesisCheckEditView, Editor<AnamnesisCheckProxy> {

	private static final Binder BINDER = GWT.create(Binder.class);

	private static AnamnesisCheckEditView instance;
	private boolean multipleFields = false;

	@UiField
	TabPanel anamnesisPanel;
	
	@UiField
	SpanElement header;
	
	@UiField(provided = true)
    ValueListBox<AnamnesisCheckTypes> type = new ValueListBox<AnamnesisCheckTypes>(new AbstractRenderer<AnamnesisCheckTypes>() {
        public String render(AnamnesisCheckTypes obj) {
            return obj == null ? "" : String.valueOf(obj);
        }
    });
	
	@UiField
	TextBox text;
	@UiField
	VerticalPanel valuePanel;
//	@UiField
//	TextBox value;
	
	// TODO: Fill
//	String value = "";
	
	@UiField
	SpanElement labelType;
	@UiField
	SpanElement labelText;
	@UiField
	SpanElement labelValue;

//	@UiField
//	DateBox createDate;
//
//	@UiField
//	AnamnesisChecksValueSetEditor anamnesischecksvalues;
//
//	@UiField
//	ScarSetEditor scars;

	@UiField
	IconButton cancel;
	@UiField
	IconButton save;

	IconButton addButton = new IconButton();

	@UiField
	DivElement errors;

	private Delegate delegate;
	private Presenter presenter;

	public AnamnesisCheckEditViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
		type.setAcceptableValues(Arrays.asList(AnamnesisCheckTypes.values()));
		
		anamnesisPanel.selectTab(0);
		anamnesisPanel.getTabBar().setTabText(0, Messages.ANAMNESIS_VALUES);
		TabPanelHelper.moveTabBarToBottom(anamnesisPanel);

		cancel.setText(Messages.CANCEL);
		save.setText(Messages.SAVE);
		addButton.setIcon("plusthick");
		addButton.setText(Messages.ADD_ANSWER);
		addButton.setVisible(false);

		labelType.setInnerText(Messages.TYPE + ":");
		labelText.setInnerText(Messages.TEXT + ":");
		labelValue.setInnerText(Messages.VALUE + ":");
		
		HorizontalPanel valueThing = new HorizontalPanel();
		valueThing.add(new TextBox());
		valueThing.add(addButton);
		valuePanel.add(valueThing);
		
		addButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				IndexedPanel lastPanel = (IndexedPanel) valuePanel.getWidget(valuePanel.getWidgetCount() - 1);
				lastPanel.getWidget(1).removeFromParent();
				HorizontalPanel newPanel = new HorizontalPanel();
				newPanel.add(new TextBox());
				newPanel.add(addButton);
				valuePanel.add(newPanel);
			}
			
		});
		
		type.addValueChangeHandler(new ValueChangeHandler<AnamnesisCheckTypes>() {
			@Override
			public void onValueChange(ValueChangeEvent<AnamnesisCheckTypes> event) {
				AnamnesisCheckTypes selectedValue = event.getValue();
				switch(selectedValue) {
					case QuestionMultM:
					case QuestionMultS:
						setMultipleFields(true);
						break;
					default:
						setMultipleFields(false);
				}
			}
		});
		
		if (type.getValue() == AnamnesisCheckTypes.QuestionMultM || type.getValue() == AnamnesisCheckTypes.QuestionMultS) {
			setMultipleFields(true);
			
		}
//		Log.info("value = " + value);
	}
	
	private void setMultipleFields(boolean multipleFields) {
		this.multipleFields = multipleFields;
		addButton.setVisible(multipleFields);
		
		// removes the additional fields...
		if (!multipleFields) {
			for (int i = valuePanel.getWidgetCount() - 1; i > 0; i--) {
				valuePanel.remove(i);
			}
			((HorizontalPanel) valuePanel.getWidget(0)).add(addButton);
		}
	}

	@Override
	public RequestFactoryEditorDriver<AnamnesisCheckProxy, AnamnesisCheckEditViewImpl> createEditorDriver() {
		RequestFactoryEditorDriver<AnamnesisCheckProxy, AnamnesisCheckEditViewImpl> driver = GWT.create(Driver.class);
		driver.initialize(this);
		return driver;
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
//		value = ((TextBox)((IndexedPanel) valuePanel.getWidget(0)).getWidget(0)).getText();
		if (multipleFields) {
			for (int i=1; i < valuePanel.getWidgetCount(); i++) {
//				value += "|" + ((TextBox)((IndexedPanel) valuePanel.getWidget(i)).getWidget(0)).getText();
			}
		}
		delegate.saveClicked();
	}

	interface Binder extends UiBinder<Widget, AnamnesisCheckEditViewImpl> {
	}

	interface Driver extends RequestFactoryEditorDriver<AnamnesisCheckProxy, AnamnesisCheckEditViewImpl> {
	}

	public void setCreating(boolean creating) {
		if (creating) {
			header.setInnerText(Messages.EDIT_ANAMNESIS_VALUE);
		} else {
			header.setInnerText(Messages.ADD_ANAMNESIS_VALUE);
		}
	}
	
	@Override
	public void setEditTitle(boolean edit) {
		if (edit) {
			header.setInnerText(Messages.EDIT_ANAMNESIS_VALUE);
		} else {
			header.setInnerText(Messages.ADD_ANAMNESIS_VALUE);
		}

	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}
