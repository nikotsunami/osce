package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.Validator;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.ProxySuggestOracle;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class StandardizedPatientAdvancedSearchAnamnesisPopupImpl extends PopupPanel
		implements StandardizedPatientAdvancedSearchAnamnesisPopup {

	private static StandardizedPatientAdvancedSearchAnamnesisPopupImplUiBinder uiBinder = GWT
			.create(StandardizedPatientAdvancedSearchAnamnesisPopupImplUiBinder.class);

	interface StandardizedPatientAdvancedSearchAnamnesisPopupImplUiBinder extends
			UiBinder<Widget, StandardizedPatientAdvancedSearchAnamnesisPopupImpl> {
	}
	
	// SPEC Change
	private MessageConfirmationDialogBox confirmationDialogBox;
	
	@UiField
	HorizontalPanel parentPanel;
	
	@UiField
	HorizontalPanel anamnesisAnswerPanel;
	
	@UiField
	IconButton addAnamnesisValueButton;
	@UiField
	IconButton addAnamnesisValues;
	@UiField
	IconButton closeBoxButton;
	
	@UiField (provided = true)
	SuggestBox anamnesisQuestionSuggestBox;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	private ValueListBox<Integer> anamnesisAnswerMCSelector = new ValueListBox<Integer>(new AbstractRenderer<Integer>() {
		@Override
		public String render(Integer object) {
			if (object == null) return "";
			return possibleAnswers.get(object.intValue());
		}
	});
	
	private ValueListBox<Boolean> anamnesisAnswerYesNoSelector = new ValueListBox<Boolean>(new AbstractRenderer<Boolean>() {
		@Override
		public String render(Boolean object) {
			if (object == null)
				return null;
			return (object.booleanValue()) ? constants.yes() : constants.no();
		}
	});
	
	private TextBox anamnesisAnswerText = new TextBox();
	private IsWidget currentAnswerWidget;
	
	@UiField(provided = true)
    ValueListBox<BindType> bindType = new ValueListBox<BindType>(new EnumRenderer<BindType>());
    
    @UiField(provided = true)
    ValueListBox<Comparison> comparison = new ValueListBox<Comparison>(new EnumRenderer<Comparison>(EnumRenderer.Type.ANAMNESIS));

	private AnamnesisCheckProxy selectedProxy;
	private List<String> possibleAnswers;

	// Highlight onViolation
	public Map<String, Widget> advanceSearchCriteriaMap;
	// E Highlight onViolation
	
	public StandardizedPatientAdvancedSearchAnamnesisPopupImpl() {
		
		anamnesisQuestionSuggestBox = new SuggestBox(new ProxySuggestOracle<AnamnesisCheckProxy>(new AbstractRenderer<AnamnesisCheckProxy>() {
			@Override
			public String render(AnamnesisCheckProxy object) {
				return object.getText();
			}
		}, ",;:. \t?!_-/\\"));
		
		anamnesisQuestionSuggestBox.setText(constants.enterQuestion());
		anamnesisQuestionSuggestBox.getTextBox().addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				if (anamnesisQuestionSuggestBox.getText().equals(constants.enterQuestion())) {
					anamnesisQuestionSuggestBox.setText("");	
				}
			}
		});
		anamnesisQuestionSuggestBox.getTextBox().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (anamnesisQuestionSuggestBox.getText().equals("")) {
					anamnesisQuestionSuggestBox.setText(constants.enterQuestion());
				}
			}
		});
		anamnesisQuestionSuggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion> () {
			@SuppressWarnings("unchecked")
			@Override
			public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event) {
				changeQuestionFieldWidth(event.getSelectedItem().getReplacementString());
				displayAnswerFieldForProxy((AnamnesisCheckProxy) ((ProxySuggestOracle<AnamnesisCheckProxy>.ProxySuggestion) 
						event.getSelectedItem()).getProxy());
			}
		});
		
		setWidget(uiBinder.createAndBindUi(this));
		comparison.setValue(Comparison.EQUALS);
		comparison.setAcceptableValues(Arrays.asList(new Comparison[] {Comparison.EQUALS, Comparison.NOT_EQUALS}));
		
		bindType.setValue(BindType.values()[0]);
		bindType.setAcceptableValues(Arrays.asList(BindType.values()));
		
		addAnamnesisValueButton.setText(constants.add());
		addAnamnesisValues.setText(constants.anamnesisValues());
		
		anamnesisAnswerText.setText(constants.enterAnswer());
		anamnesisAnswerText.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				if (anamnesisAnswerText.getText().equals(constants.enterAnswer())) {
					anamnesisAnswerText.setText("");
				}
			}
		});
		anamnesisAnswerText.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (anamnesisAnswerText.getText().equals("")) {
					anamnesisAnswerText.setText(constants.enterAnswer());
				}
			}
		});
		
		ArrayList<Boolean> acceptableBooleanValues = new ArrayList<Boolean>();
		acceptableBooleanValues.add(new Boolean(true));
		acceptableBooleanValues.add(new Boolean(false));
		anamnesisAnswerYesNoSelector.setValue(true);
		anamnesisAnswerYesNoSelector.setAcceptableValues(acceptableBooleanValues);
		
		// Highlight onViolation
				advanceSearchCriteriaMap=new HashMap<String, Widget>();				
				advanceSearchCriteriaMap.put("bindType", bindType);
				advanceSearchCriteriaMap.put("comparation", comparison);					
				// E Highlight onViolation
				
				/*Advance search popup changes start*/
				this.sinkEvents(Event.KEYEVENTS);
				this.sinkEvents(Event.ONFOCUS);
				/*Advance search popup changes end*/
				
	}
	
	/*Advance search popup changes start*/
	@Override
	public void onBrowserEvent(Event event) {
		// TODO Auto-generated method stub
		super.onBrowserEvent(event);
		int type = DOM.eventGetType(event);
		// Log.info("event type--"+event.getType());

		
		switch (type) {
		case Event.ONKEYUP:
			// onKeyDownEvent(event);
			
				if (event.getKeyCode() == 13) 
				{
					Log.info("Enter press");
					addAdvSearchSaveMethod();
				}
			break;
		default:
			return;

		}
	}
	
	/*Advance search popup changes end*/
	
	private void displayAnswerFieldForProxy(AnamnesisCheckProxy proxy) {
		this.selectedProxy = proxy;
		
		if (currentAnswerWidget != null) {
			anamnesisAnswerPanel.remove(currentAnswerWidget);
		}
		
		if (proxy == null) {
			return;
		} else if (proxy.getType() == AnamnesisCheckTypes.QUESTION_MULT_M || proxy.getType() == AnamnesisCheckTypes.QUESTION_MULT_S) {
			currentAnswerWidget = anamnesisAnswerMCSelector;
			Log.info("proxy.getValue() = " + proxy.getValue());
			possibleAnswers = Arrays.asList(proxy.getValue().split("\\|"));
			int numAnswers = possibleAnswers.size();
			List<Integer> numericAnswers = new ArrayList<Integer>();
			for (int i=0; i < numAnswers; i++) {
				numericAnswers.add(i);
			}
			anamnesisAnswerMCSelector.setValue(0);
			anamnesisAnswerMCSelector.setAcceptableValues(numericAnswers);

		} else if (proxy.getType() == AnamnesisCheckTypes.QUESTION_YES_NO) {
			currentAnswerWidget = anamnesisAnswerYesNoSelector;
		} else {
			currentAnswerWidget = anamnesisAnswerText;
		}
		anamnesisAnswerPanel.add(currentAnswerWidget);
	}
	
	private String createMultipleChoiceString(int selectedAnswerId, int numAnswers) {
		StringBuilder builder = new StringBuilder();
		for (int i=0; i < numAnswers; i++) {
			if (i == selectedAnswerId) {
				builder.append(1);
			} else {
				builder.append(0);
			}
			builder.append("-");
		}
		builder.deleteCharAt(builder.length()-1);
		return builder.toString();
	}

	private void changeQuestionFieldWidth(String stringForOrientation) {
		Label dummyLabel = new Label(stringForOrientation);
		parentPanel.add(dummyLabel);
		anamnesisQuestionSuggestBox.getTextBox().setWidth("" + (dummyLabel.getElement().getClientWidth() + 10) + "px");
		parentPanel.remove(dummyLabel);
	}
	
	/*Advance search popup changes start*/

	
	@UiHandler("addAnamnesisValueButton")
	public void addAnamnesisValueButtonClicked(ClickEvent e) {
		// Highlight onViolation
		Log.info("Call addAnamnesisValueButton");
		// E Highlight onViolation
	/*	String answer = "";
		if (currentAnswerWidget == anamnesisAnswerMCSelector) {
			answer = createMultipleChoiceString(anamnesisAnswerMCSelector.getValue().intValue(), possibleAnswers.size());
		} else if (currentAnswerWidget == anamnesisAnswerYesNoSelector) {
			answer = (anamnesisAnswerYesNoSelector.getValue().booleanValue()) ? "1" : "0";
		} else if (currentAnswerWidget == anamnesisAnswerText) {
			answer = anamnesisAnswerText.getValue();
		}
		if (currentAnswerWidget != null) {
			delegate.addAnamnesisValueButtonClicked(selectedProxy, answer, bindType.getValue(), comparison.getValue());
		}
		hide();*/
		addAdvSearchSaveMethod();
	}
	
	public void addAdvSearchSaveMethod()
	{
		Log.info("Call addAdvSearchSaveMethod");
		String answer = "";
		if (currentAnswerWidget == anamnesisAnswerMCSelector) {
			answer = createMultipleChoiceString(anamnesisAnswerMCSelector.getValue().intValue(), possibleAnswers.size());
		} else if (currentAnswerWidget == anamnesisAnswerYesNoSelector) {
			answer = (anamnesisAnswerYesNoSelector.getValue().booleanValue()) ? "1" : "0";
		} else if (currentAnswerWidget == anamnesisAnswerText) {
			answer = (constants.enterAnswer().equals(anamnesisAnswerText.getValue()))? "" : anamnesisAnswerText.getValue(); //SPEC Change
		}
		if (currentAnswerWidget != null) {
			
			// SPEC Change
			Log.info("Answer = "+answer);
			
			if(Validator.isNotNull(answer)){
			delegate.addAnamnesisValueButtonClicked(selectedProxy, answer, bindType.getValue(), comparison.getValue());
				hide();
			}else{
				confirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
				confirmationDialogBox.showConfirmationDialog(constants.warningFillRequiredFields());
			}
		}else{
			confirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
			confirmationDialogBox.showConfirmationDialog(constants.warningFillRequiredFields());
		}
	}
	
	/*Advance search popup changes end*/
	
	@UiHandler("addAnamnesisValues")
	public void addAnamnesisValuesClicked(ClickEvent e) {
		hide();
	}
	
	@UiHandler("closeBoxButton")
	public void closeBoxButtonClicked(ClickEvent e) {
		hide();
	}
	
	private Delegate delegate;
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void display(Button parentButton) {
		this.show();
		this.setPopupPosition(parentButton.getAbsoluteLeft() - 250, parentButton.getAbsoluteTop() - getOffsetHeight()/2 - 34);
	}

	//change for advance search
		@Override
		public void display(int positionX,int positionY) {
			this.show();
			this.setPopupPosition(positionX-175,positionY-34);
		}
		//change for advance search
		
	@Override
	public SuggestBox getAnamnesisQuestionSuggestBox() {
		return anamnesisQuestionSuggestBox;
	}

	@Override
	public Map getMap() {
		// TODO Auto-generated method stub
		return this.advanceSearchCriteriaMap;
	}
}
