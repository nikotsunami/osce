package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.TabPanelHelper;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
<<<<<<< HEAD
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
=======
>>>>>>> paul/master
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
<<<<<<< HEAD
import com.google.gwt.user.client.Window;
=======
>>>>>>> paul/master
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

public class AnamnesisCheckTitleEditViewImpl extends Composite implements AnamnesisCheckTitleEditView, Editor<AnamnesisCheckTitleProxy> {

	private static final Binder BINDER = GWT.create(Binder.class);

	private boolean multipleFields = false;
<<<<<<< HEAD
	public int selectedIndex= -1;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	private  AnamnesisCheckTitleProxy anamnesisCheckTitle=null;
=======
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
>>>>>>> paul/master

	@UiField
	TabPanel anamnesisTitlePanel;
	
	@UiField
	SpanElement header;
	
<<<<<<< HEAD

	
	@UiField
	TextBox text;

=======
//	@UiField(provided = true)
//    ValueListBox<AnamnesisCheckTypes> type = new ValueListBox<AnamnesisCheckTypes>(new AbstractRenderer<AnamnesisCheckTypes>() {
//    	EnumRenderer<AnamnesisCheckTypes> renderer = new EnumRenderer<AnamnesisCheckTypes>();
//        public String render(AnamnesisCheckTypes obj) {
//            return obj == null ? "" : renderer.render(obj);
//        }
//    }
//    );
	
	@UiField
	TextBox text;
//	@UiField
//	VerticalPanel valuePanel;
>>>>>>> paul/master
	
	// TODO: Fill
	public String value = "";
	
<<<<<<< HEAD

	@UiField
	SpanElement labelText;

	@UiField
	SpanElement labelpreviousTitle;
//

	@UiField
	ListBox	previousTitleListBox;


=======
//	@UiField
//	SpanElement labelType;
	@UiField
	SpanElement labelText;
//	@UiField
//	SpanElement labelValue;
//	@UiField
//	SpanElement labelinsideTitle;
//	@UiField
//	SpanElement labelpreviousQuestion;
//
//	@UiField
//	ListBox insideTitleListBox;
//	@UiField
//	ListBox	previousQuestionListBox;

//	@UiField
//	DateBox createDate;
//
//	@UiField
//	AnamnesisChecksValueSetEditor anamnesischecksvalues;
//
//	@UiField
//	ScarSetEditor scars;
>>>>>>> paul/master

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

	public AnamnesisCheckTitleEditViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
		
		anamnesisTitlePanel.selectTab(0);
		anamnesisTitlePanel.getTabBar().setTabText(0, constants.anamnesisValues());
		TabPanelHelper.moveTabBarToBottom(anamnesisTitlePanel);

		cancel.setText(constants.cancel());
		save.setText(constants.save());
		addButton.setIcon("plusthick");
		addButton.setText(constants.addAnswer());
		addButton.setVisible(false);

<<<<<<< HEAD
		labelText.setInnerText(constants.anamnesisCheckTitle() + ":");
		labelpreviousTitle.setInnerText(constants.previousTitle()+":");
		previousTitleListBox.addItem(constants.previousTitle(),"");
	}
	

=======
//		labelType.setInnerText(constants.type() + ":");
		labelText.setInnerText(constants.question() + ":");
//		labelpreviousQuestion.setInnerText(constants.previousQuestion() + ":");
//		previousQuestionListBox.addItem(constants.previousQuestion(), "");
		
//		insideTitleListBox.addItem(constants.insideTitle(), "");
//		insideTitleListBox.setVisible(false);
		
		//addValueField();
		
		addButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//addValueField();
			}
		});
		
		
		
//		insideTitleListBox.addChangeListener(new ChangeListener() {
//			
//			@Override
//			public void onChange(Widget sender) {
//				//TODO
//				resetpreviousQuestion(type.getValue());
//			}
//		});
		
	//	Log.info("type.getValue() = " + type.getValue());
	
	
		
	//	type.setValue(AnamnesisChecks.values()[0]);
		//type.setAcceptableValues(Arrays.asList(AnamnesisCheckTypes.values()));
		
		
	}
	
	private void resetpreviousQuestion(AnamnesisCheckTypes selectedValue){
//		previousQuestionListBox.clear();
//		if(selectedValue == AnamnesisCheckTypes.QUESTION_TITLE){
//			previousQuestionListBox.addItem(constants.previousTitle(), "");
//		}else{
//			previousQuestionListBox.addItem(constants.previousQuestion(), "");
//		}
//		delegate.changePreviousQuestion(selectedValue,insideTitleListBox.getValue(insideTitleListBox.getSelectedIndex()));

	}
	
>>>>>>> paul/master
	private IconButton createDeleteButton() {
		IconButton button = new IconButton();
		button.setIcon("trash");
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
<<<<<<< HEAD
//				deleteValueField((HorizontalPanel) ((Widget) event.getSource()).getParent());
=======
				deleteValueField((HorizontalPanel) ((Widget) event.getSource()).getParent());
>>>>>>> paul/master
			}
		});
		return button;
	}
	
<<<<<<< HEAD
	
=======
	private void deleteValueField(HorizontalPanel parentPanel) {
		// Check if last field
//		if (valuePanel.getWidget(valuePanel.getWidgetCount() - 1).equals(parentPanel)) {
//			((HorizontalPanel)valuePanel.getWidget(valuePanel.getWidgetCount() - 2)).add(addButton);
//		}
//		valuePanel.remove(parentPanel);
	}
	
//	private void addValueField() {
//		HorizontalPanel newPanel = new HorizontalPanel();
//		TextBox textBox = new TextBox();
//		textBox.addFocusHandler(new FocusHandler() {
//			@Override
//			public void onFocus(FocusEvent event) {
//				((TextBox) event.getSource()).selectAll();
//			}
//		});
//		newPanel.add(textBox);
//		if (valuePanel.getWidgetCount() > 0)
//			newPanel.add(createDeleteButton());
//		newPanel.add(addButton);
//		valuePanel.add(newPanel);
//		textBox.setFocus(true);
//	}
//	
//	private void setMultipleFields(boolean multipleFields) {
//		this.multipleFields = multipleFields;
//		addButton.setVisible(multipleFields);
//		
//		// removes the additional fields...
//		if (!multipleFields) {
//			labelValue.setInnerText("");
//			for (int i = valuePanel.getWidgetCount() - 1; i > 0; i--) {
//				valuePanel.remove(i);
//			}
//			((HorizontalPanel) valuePanel.getWidget(0)).add(addButton);
//			valuePanel.setVisible(false);
//		} else {
//			valuePanel.setVisible(true);
//			labelValue.setInnerText(constants.possibleAnswers() + ":");
//		}
//	}
>>>>>>> paul/master

	@Override
	public RequestFactoryEditorDriver<AnamnesisCheckTitleProxy, AnamnesisCheckTitleEditViewImpl> createEditorDriver() {
		RequestFactoryEditorDriver<AnamnesisCheckTitleProxy, AnamnesisCheckTitleEditViewImpl> driver = GWT.create(Driver.class);
		driver.initialize(this);
		return driver;
	}
	@Override
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
		value = text.getText();
<<<<<<< HEAD
	
=======
>>>>>>> paul/master
		delegate.saveClicked();
	}

	interface Binder extends UiBinder<Widget, AnamnesisCheckTitleEditViewImpl> {
	}

	interface Driver extends RequestFactoryEditorDriver<AnamnesisCheckTitleProxy, AnamnesisCheckTitleEditViewImpl> {
	}

<<<<<<< HEAD
	@Override
	public void update(AnamnesisCheckTitleProxy anamnesisCheckTitleProxy) {
		String value = anamnesisCheckTitleProxy.getText();
	}

	@Override
	public void setInsideTitleListBox(List<AnamnesisCheckTitleProxy> titleList) {
		for(AnamnesisCheckTitleProxy title:titleList){
			
			if (title != null) {
				previousTitleListBox.addItem(title.getText(), String.valueOf(title.getId()));
			}
		}
		
	}
	
	@Override
	public String getSelectedInsideTitle() {
		int selectedIndex = previousTitleListBox.getSelectedIndex();
		String selectedInsideTitle = previousTitleListBox.getValue(selectedIndex);
		return selectedInsideTitle;
	}


	@Override
	public void setSeletedInsideTitle(String anamnesisCheckTitleId) {
		for (int i = 0; i < previousTitleListBox.getItemCount(); i++) {
			GWT.log("insideTitleListBox.getValue(i) = "+previousTitleListBox.getValue(i));
			if (previousTitleListBox.getValue(i).equals(anamnesisCheckTitleId)) {
				previousTitleListBox.setSelectedIndex(i);
			}
		}
		
	}

=======
//	public void setCreating(boolean creating) {
//		Log.debug("setCreating()");
//		if (creating) {
//			header.setInnerText(constants.editAnamnesisValue());
//		} else {
//			header.setInnerText(constants.addAnamnesisValue());
//		}
//	}
	
//	@Override
//	public void setEditTitle(boolean edit) {
//		if (edit) {
//			header.setInnerText(constants.editAnamnesisValue());
//		} else {
//			header.setInnerText(constants.addAnamnesisValue());
//			type.setValue(AnamnesisCheckTypes.values()[0]);
//		}
//
//	}
//
//	@Override
//	public void setPresenter(Presenter presenter) {
//		this.presenter = presenter;
//	}
//	
	@Override
	public String getValue() {
		return value;
	}
	
//	@Override
//	public void update(AnamnesisCheckTitleProxy anamnesisCheckProxy) {
//		String value = anamnesisCheckTitleProxy.getText();
//	}

//	@Override
//	public void setInsideTitleListBox(List<AnamnesisCheckTitleProxy> titleList) {
//		for(AnamnesisCheckTitleProxy title : titleList){
//			if (title != null) {
//				insideTitleListBox.addItem(title.getText(), String.valueOf(title.getId()));
//			}
//		}
//		
//	}


//	@Override
//	public void setPreviousQuestionListBox(List<AnamnesisCheckTitleProxy> anamnesisCheckList) {
//		for(AnamnesisCheckTitleProxy anamnesisCheck : anamnesisCheckList){
//			if (anamnesisCheck != null) {
//				previousQuestionListBox.addItem(anamnesisCheck.getText(), String.valueOf(anamnesisCheck.getSort_order()));
//			}
//		}
//		
//	}
	
//	@Override
//	public String getSelectedInsideTitle() {
//		int selectedIndex = insideTitleListBox.getSelectedIndex();
//		String selectedInsideTitle = insideTitleListBox.getValue(selectedIndex);
//		return selectedInsideTitle;
//	}
//
//	@Override
//	public String getSelectedPreviousQuestion() {
//		int selectedIndex = previousQuestionListBox.getSelectedIndex();
//		String selectedPreviousQuestion = previousQuestionListBox.getValue(selectedIndex);
//		return selectedPreviousQuestion;
//	}
//
//	@Override
//	public void setSeletedInsideTitle(String anamnesisCheckTitleId) {
//		for (int i = 0; i < insideTitleListBox.getItemCount(); i++) {
//			GWT.log("insideTitleListBox.getValue(i) = "+insideTitleListBox.getValue(i));
//			if (insideTitleListBox.getValue(i).equals(anamnesisCheckTitleId)) {
//				insideTitleListBox.setSelectedIndex(i);
//			}
//		}
//		
//	}
//
//	@Override
//	public void setSeletedPreviousQuestion(String previousSortId) {
//		GWT.log("?????previousSortId = "+previousSortId);
//		for (int i = 0; i < previousQuestionListBox.getItemCount(); i++) {
//			GWT.log("previousQuestionListBox.getValue(i) = "+previousQuestionListBox.getValue(i));
//			if (previousQuestionListBox.getValue(i).equals(previousSortId)) {
//				previousQuestionListBox.setSelectedIndex(i);
//			}
//		}	
//	}
>>>>>>> paul/master

}