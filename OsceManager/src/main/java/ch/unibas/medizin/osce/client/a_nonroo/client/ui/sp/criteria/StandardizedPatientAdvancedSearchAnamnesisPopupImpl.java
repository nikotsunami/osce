package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria;

import java.util.ArrayList;
import java.util.Arrays;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.ProxySuggestOracle;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison2;

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
	
	private ValueListBox<String> anamnesisAnswerMCSelector = new ValueListBox<String>(new AbstractRenderer<String>() {
		@Override
		public String render(String object) {
			return (object == null) ? "" : object;
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
    ValueListBox<Comparison2> comparison = new ValueListBox<Comparison2>(new EnumRenderer<Comparison2>(EnumRenderer.Type.ANAMNESIS));

	private AnamnesisCheckProxy selectedProxy;

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
		comparison.setValue(Comparison2.EQUALS);
		comparison.setAcceptableValues(Arrays.asList(new Comparison2[] {Comparison2.EQUALS, Comparison2.NOT_EQUALS}));
		
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
	}
	
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
			anamnesisAnswerMCSelector.setValue(proxy.getValue().split("\\|")[0]);
			anamnesisAnswerMCSelector.setAcceptableValues(Arrays.asList(proxy.getValue().split("\\|")));
		} else if (proxy.getType() == AnamnesisCheckTypes.QUESTION_YES_NO) {
			currentAnswerWidget = anamnesisAnswerYesNoSelector;
		} else {
			currentAnswerWidget = anamnesisAnswerText;
		}
		anamnesisAnswerPanel.add(currentAnswerWidget);
	}

	private void changeQuestionFieldWidth(String stringForOrientation) {
		Label dummyLabel = new Label(stringForOrientation);
		parentPanel.add(dummyLabel);
		anamnesisQuestionSuggestBox.getTextBox().setWidth("" + (dummyLabel.getElement().getClientWidth() + 10) + "px");
		parentPanel.remove(dummyLabel);
	}
	
	@UiHandler("addAnamnesisValueButton")
	public void addAnamnesisValueButtonClicked(ClickEvent e) {
		String answer = "";
		if (currentAnswerWidget == anamnesisAnswerMCSelector) {
			answer = anamnesisAnswerMCSelector.getValue();
		} else if (currentAnswerWidget == anamnesisAnswerYesNoSelector) {
			answer = (anamnesisAnswerYesNoSelector.getValue().booleanValue()) ? constants.no() : constants.yes();
		} else if (currentAnswerWidget == anamnesisAnswerText) {
			answer = anamnesisAnswerText.getValue();
		}
		delegate.addAnamnesisValueButtonClicked(selectedProxy, answer, bindType.getValue(), comparison.getValue());
		hide();
	}
	
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
		this.setPopupPosition(parentButton.getAbsoluteLeft() - 5, parentButton.getAbsoluteTop() - getOffsetHeight()/2 - 6);
	}

	@Override
	public SuggestBox getAnamnesisQuestionSuggestBox() {
		return anamnesisQuestionSuggestBox;
	}
}
