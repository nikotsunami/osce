package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisCheckProxyRenderer;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisCheckTitleProxyRenderer;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.TabPanelHelper;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AnamnesisCheckEditViewImpl extends Composite implements AnamnesisCheckEditView, Editor<AnamnesisCheckProxy> {

	private static final Binder BINDER = GWT.create(Binder.class);

	private boolean multipleFields = false;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);

	@UiField
	TabPanel anamnesisPanel;
	
	@UiField
	SpanElement header;
	
	@UiField(provided = true)
    ValueListBox<AnamnesisCheckTypes> type = new ValueListBox<AnamnesisCheckTypes>(new AbstractRenderer<AnamnesisCheckTypes>() {
    	EnumRenderer<AnamnesisCheckTypes> renderer = new EnumRenderer<AnamnesisCheckTypes>();
        public String render(AnamnesisCheckTypes obj) {
            return obj == null ? "" : renderer.render(obj);
        }
    });
	
	@UiField
	TextBox text;
	@UiField
	VerticalPanel valuePanel;
	
	// TODO: Fill
	public String value = "";
	
	@UiField
	SpanElement labelType;
	@UiField
	SpanElement labelText;
	@UiField
	SpanElement labelValue;
	@UiField
	SpanElement labelinsideTitle;
	@UiField
	SpanElement labelpreviousQuestion;

	
	
	//Issue # 122 : Replace pull down with autocomplete.
	@UiField
	public DefaultSuggestBox<AnamnesisCheckTitleProxy, EventHandlingValueHolderItem<AnamnesisCheckTitleProxy>> insideTitleListBox;

	/*@UiField
	ListBox insideTitleListBox;
	*/
	//Issue # 122 : Replace pull down with autocomplete.
	
	//Issue # 122 : Replace pull down with autocomplete.
	
	@UiField
	public DefaultSuggestBox<AnamnesisCheckProxy, EventHandlingValueHolderItem<AnamnesisCheckProxy>> previousQuestionListBox;

/*	@UiField
	ListBox	previousQuestionListBox;
*/
	//Issue # 122 : Replace pull down with autocomplete.

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
	
	ArrayList<HorizontalPanel> mCFields = new ArrayList<HorizontalPanel>();

	// Highlight onViolation
	Map<String, Widget> anamnesisCheckMap;
	// E Highlight onViolation
	
	
	public AnamnesisCheckEditViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
		
		anamnesisPanel.selectTab(0);
		anamnesisPanel.getTabBar().setTabText(0, constants.anamnesisValues());
		TabPanelHelper.moveTabBarToBottom(anamnesisPanel);

		cancel.setText(constants.cancel());
		save.setText(constants.save());
		addButton.setIcon("plusthick");
		addButton.setText(constants.addAnswer());
		addButton.setVisible(false);

		labelType.setInnerText(constants.type() + ":");
		labelText.setInnerText(constants.question() + ":");
//		labelpreviousQuestion.setInnerText(constants.previousQuestion() + ":");
//		previousQuestionListBox.addItem(constants.previousQuestion(), "");
		
		//Issue # 122 : Replace pull down with autocomplete.
		//insideTitleListBox.addItem(constants.insideTitle(), "");
		//Issue # 122 : Replace pull down with autocomplete.
		insideTitleListBox.setVisible(false);
		
		addValueField();
		
		addButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addValueField();
			}
		});
		
		type.addValueChangeHandler(new ValueChangeHandler<AnamnesisCheckTypes>() {
			@Override
			public void onValueChange(ValueChangeEvent<AnamnesisCheckTypes> event) {
				AnamnesisCheckTypes selectedValue = event.getValue();
				switch(selectedValue) {
					case QUESTION_MULT_M:
					case QUESTION_MULT_S:
						setMultipleFields(true);
						break;
					default:
						setMultipleFields(false);
				}
				labelinsideTitle.setInnerText(constants.insideTitle() + ":");
				insideTitleListBox.setVisible(true);
				labelpreviousQuestion.setInnerText(constants.previousQuestion() + ":");
				
				//TODO
				resetpreviousQuestion(selectedValue);
			}
			
		});
		
		
		//Issue # 122 : Replace pull down with autocomplete.
		/*
		insideTitleListBox.addChangeListener(new ChangeListener() {
			
			@Override
			public void onChange(Widget sender) {
				//TODO
				resetpreviousQuestion(type.getValue());
			}
		});
		*/
		//Issue # 122 : Replace pull down with autocomplete.
		
		
		Log.info("type.getValue() = " + type.getValue());
		
		if (type.getValue() == AnamnesisCheckTypes.QUESTION_MULT_M || type.getValue() == AnamnesisCheckTypes.QUESTION_MULT_S) {
			setMultipleFields(true);
		} else {
			setMultipleFields(false);
		}
		
		labelinsideTitle.setInnerText(constants.insideTitle() + ":");
		insideTitleListBox.setVisible(true);
		
		text.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				text.selectAll();
			}
		});
		
		type.setValue(AnamnesisCheckTypes.values()[0]);
		type.setAcceptableValues(Arrays.asList(AnamnesisCheckTypes.values()));
		
		//Issue # 122 : Replace pull down with autocomplete.
		insideTitleListBox.addHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				
				// TODO Auto-generated method stub
				Log.info("change handler");
				resetpreviousQuestion(type.getValue());
			}
		});
		
		//Issue # 122 : Replace pull down with autocomplete.
		
		
	}
	
	private void resetpreviousQuestion(AnamnesisCheckTypes selectedValue){
		
		//Issue # 122 : Replace pull down with autocomplete.
		
		//previousQuestionListBox.clear();
		//previousQuestionListBox.addItem(constants.thisIsTheFirstQuestion(), "");
		
		Log.info("in method resetpreviousQuestion ");
		//delegate.changePreviousQuestion(selectedValue,insideTitleListBox.getValue(insideTitleListBox.getSelectedIndex()));
		delegate.changePreviousQuestion(selectedValue,insideTitleListBox.getSelected().getId().toString());
		
		//Issue # 122 : Replace pull down with autocomplete.
	}
	
	private IconButton createDeleteButton() {
		IconButton button = new IconButton();
		button.setIcon("trash");
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				deleteValueField((HorizontalPanel) ((Widget) event.getSource()).getParent());
			}
		});
		return button;
	}
	
	private void deleteValueField(HorizontalPanel parentPanel) {
		// Check if last field
		if (valuePanel.getWidget(valuePanel.getWidgetCount() - 1).equals(parentPanel)) {
			((HorizontalPanel)valuePanel.getWidget(valuePanel.getWidgetCount() - 2)).add(addButton);
		}
		valuePanel.remove(parentPanel);
	}
	
	private void addValueField() {
		HorizontalPanel newPanel = new HorizontalPanel();
		TextBox textBox = new TextBox();
		textBox.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				((TextBox) event.getSource()).selectAll();
			}
		});
		newPanel.add(textBox);
		if (valuePanel.getWidgetCount() > 0)
			newPanel.add(createDeleteButton());
		newPanel.add(addButton);
		valuePanel.add(newPanel);
		textBox.setFocus(true);
	}
	
	private void setMultipleFields(boolean multipleFields) {
		this.multipleFields = multipleFields;
		addButton.setVisible(multipleFields);
		
		// removes the additional fields...
		if (!multipleFields) {
			labelValue.setInnerText("");
			for (int i = valuePanel.getWidgetCount() - 1; i > 0; i--) {
				valuePanel.remove(i);
			}
			((HorizontalPanel) valuePanel.getWidget(0)).add(addButton);
			valuePanel.setVisible(false);
		} else {
			valuePanel.setVisible(true);
			labelValue.setInnerText(constants.possibleAnswers() + ":");
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
		value = ((TextBox)((IndexedPanel) valuePanel.getWidget(0)).getWidget(0)).getText();
		value.replace('|', '/');
		if (multipleFields) {
			for (int i=1; i < valuePanel.getWidgetCount(); i++) {
				value += "|" + ((TextBox)((IndexedPanel) valuePanel.getWidget(i)).getWidget(0)).getText().replace('|', '/');
			}
		}
		delegate.saveClicked();
	}

	interface Binder extends UiBinder<Widget, AnamnesisCheckEditViewImpl> {
	}

	interface Driver extends RequestFactoryEditorDriver<AnamnesisCheckProxy, AnamnesisCheckEditViewImpl> {
	}

//	public void setCreating(boolean creating) {
//		Log.debug("setCreating()");
//		if (creating) {
//			header.setInnerText(constants.editAnamnesisValue());
//		} else {
//			header.setInnerText(constants.addAnamnesisValue());
//		}
//	}
	
	@Override
	public void setEditTitle(boolean edit) {
		if (edit) {
			header.setInnerText(constants.editAnamnesisValue());
		} else {
			header.setInnerText(constants.addAnamnesisValue());
			type.setValue(AnamnesisCheckTypes.values()[0]);
		}

	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@Override
	public String getValue() {
		return value;
	}
	
	@Override
	public void update(AnamnesisCheckProxy anamnesisCheckProxy) {
		String value = anamnesisCheckProxy.getValue();
		labelinsideTitle.setInnerText(constants.insideTitle() + ":");
		insideTitleListBox.setVisible(true);
		labelpreviousQuestion.setInnerText(constants.previousQuestion() + ":");
		
		//Issue # 122 : Replace pull down with autocomplete.
		
		//previousQuestionListBox.addItem(constants.thisIsTheFirstQuestion(), "");
		
		//Issue # 122 : Replace pull down with autocomplete.
		
		if (type.getValue() == AnamnesisCheckTypes.QUESTION_MULT_M || type.getValue() == AnamnesisCheckTypes.QUESTION_MULT_S){
			setMultipleFields(true);
		}
		else{
			setMultipleFields(false);
		}
		
		if (value == null) {
			this.value = "";
			return;
		}
		
		this.value = value;
		String substr[] = value.split("\\|");
		
		IndexedPanel lastPanel = (IndexedPanel) valuePanel.getWidget(valuePanel.getWidgetCount() - 1);
		((HasText)lastPanel.getWidget(0)).setText(substr[0]);
		
		for (int i=1; i < substr.length; i++) {
			addValueField();
			lastPanel = (IndexedPanel) valuePanel.getWidget(valuePanel.getWidgetCount() - 1);
			((HasText)lastPanel.getWidget(0)).setText(substr[i]);
		}
		
	}

	@Override
	public void setInsideTitleListBox(List<AnamnesisCheckTitleProxy> titleList) {
		
		//Issue # 122 : Replace pull down with autocomplete.
		
		/*
		for(AnamnesisCheckTitleProxy title : titleList){
			if (title != null) {
				insideTitleListBox.addItem(title.getText(), String.valueOf(title.getId()));
			}
		}
		insideTitleListBox.setSelectedIndex(insideTitleListBox.getItemCount() - 1);
		*/
		
		DefaultSuggestOracle<AnamnesisCheckTitleProxy> suggestOracle1 = (DefaultSuggestOracle<AnamnesisCheckTitleProxy>) insideTitleListBox.getSuggestOracle();
		suggestOracle1.setPossiblilities(titleList);
		insideTitleListBox.setSuggestOracle(suggestOracle1);

		insideTitleListBox.setRenderer(new AbstractRenderer<AnamnesisCheckTitleProxy>() {

			@Override
			public String render(AnamnesisCheckTitleProxy object) {
				// TODO Auto-generated method stub
				if(object!=null)
				{
					return object.getText();
				}
				else
				{
					return "";
				}
			}
		});
		
		//Issue # 122 : Replace pull down with autocomplete.
	}


	@Override
	public void setPreviousQuestionListBox(List<AnamnesisCheckProxy> anamnesisCheckList) {
		
		//Issue # 122 : Replace pull down with autocomplete.
		/*
		for(AnamnesisCheckProxy anamnesisCheck : anamnesisCheckList){
			if (anamnesisCheck != null) {
				previousQuestionListBox.addItem(anamnesisCheck.getText(), String.valueOf(anamnesisCheck.getSort_order()));
			}
		}
		
		*/
		DefaultSuggestOracle<AnamnesisCheckProxy> suggestOracle1 = (DefaultSuggestOracle<AnamnesisCheckProxy>) previousQuestionListBox.getSuggestOracle();
		suggestOracle1.setPossiblilities(anamnesisCheckList);
		previousQuestionListBox.setSuggestOracle(suggestOracle1);

		previousQuestionListBox.setRenderer(new AbstractRenderer<AnamnesisCheckProxy>() {

			@Override
			public String render(AnamnesisCheckProxy object) {
				
				// TODO Auto-generated method stub
				if(object!=null)
				{
					return object.getValue();	
				}
				else
				{
					return "";
				}
				
			}
		});
		previousQuestionListBox.setWidth(300);
		//Issue # 122 : Replace pull down with autocomplete.
	}
	
	@Override
	public String getSelectedInsideTitle() {
		//Issue # 122 : Replace pull down with autocomplete.
		
		//int selectedIndex = insideTitleListBox.getSelectedIndex();
		
	//	String selectedInsideTitle = insideTitleListBox.getValue(selectedIndex);
		String selectedInsideTitle = insideTitleListBox.getSelected().getId().toString();
		
		//Issue # 122 : Replace pull down with autocomplete.
		return selectedInsideTitle;
	}

	@Override
	public String getSelectedPreviousQuestion() {
		
		//Issue # 122 : Replace pull down with autocomplete.
		/*int selectedIndex = previousQuestionListBox.getSelectedIndex();
		String selectedPreviousQuestion = previousQuestionListBox.getValue(selectedIndex);
		return selectedPreviousQuestion;
		*/
		
		if(previousQuestionListBox.getSelected()==null)
		{
			return "0";
		}
		else
		{
			return previousQuestionListBox.getSelected().getId().toString();
	}
		//Issue # 122 : Replace pull down with autocomplete.
	}

	
	//Issue # 122 : Replace pull down with autocomplete.
	@Override
	public void setSeletedInsideTitle(String anamnesisCheckTitleId) {
		
		Log.info("old value setter setSeletedInsideTitle");
		insideTitleListBox.setText(anamnesisCheckTitleId);
		/*
		for (int i = 0; i < insideTitleListBox.getItemCount(); i++) {
			GWT.log("insideTitleListBox.getValue(i) = "+insideTitleListBox.getValue(i));
			if (insideTitleListBox.getValue(i).equals(anamnesisCheckTitleId)) {
				insideTitleListBox.setSelectedIndex(i);
			}
		}
		*/
	}
	
	@Override
	public void setSeletedInsideTitle(AnamnesisCheckTitleProxy anamnesisCheckTitleProxy)
	{
		Log.info("new value setter setSeletedInsideTitle");
		insideTitleListBox.setRenderer(new AbstractRenderer<AnamnesisCheckTitleProxy>() {

			@Override
			public String render(AnamnesisCheckTitleProxy object) {
				// TODO Auto-generated method stub
				if(object!=null)
				{
				return object.getText();
				}
				else
				{
					return "";
				}
			}
		});
		//insideTitleListBox.setRenderer(new AnamnesisCheckTitleProxyRenderer());
		insideTitleListBox.setSelected(anamnesisCheckTitleProxy);
		
	}
	//Issue # 122 : Replace pull down with autocomplete.

	//Issue # 122 : Replace pull down with autocomplete.
	@Override
	public void setSeletedPreviousQuestion(String previousSortId) {
		
		
		GWT.log("?????previousSortId = "+previousSortId);
		/*
		for (int i = 0; i < previousQuestionListBox.getItemCount(); i++) {
			GWT.log("previousQuestionListBox.getValue(i) = "+previousQuestionListBox.getValue(i));
			if (previousQuestionListBox.getValue(i).equals(previousSortId)) {
				previousQuestionListBox.setSelectedIndex(i);
			}
		}	
		*/
	}

	// Highlight onViolation
	@Override
	public void setSeletedPreviousQuestion(
			AnamnesisCheckProxy anamnesisCheckList) {
		// TODO Auto-generated method stub
		Log.info("previous value set setSeletedPreviousQuestion");
		//previousQuestionListBox.setRenderer(new AnamnesisCheckProxyRenderer());
		previousQuestionListBox.setRenderer(new AbstractRenderer<AnamnesisCheckProxy>() {

			@Override
			public String render(AnamnesisCheckProxy object) {
				// TODO Auto-generated method stub
				if(object!=null)
				{
				return object.getText();
				}
				else
				{
					return "";
				}
			}
		});
		previousQuestionListBox.setSelected(anamnesisCheckList);
		
		
	}
	// E Highlight onViolation


	//Issue # 122 : Replace pull down with autocomplete.
	
	// Highlight onViolation
	@Override
	public Map getAnamnesisCheckMap() 
	{
		return this.anamnesisCheckMap;
	}
	// E Highlight onViolation

}