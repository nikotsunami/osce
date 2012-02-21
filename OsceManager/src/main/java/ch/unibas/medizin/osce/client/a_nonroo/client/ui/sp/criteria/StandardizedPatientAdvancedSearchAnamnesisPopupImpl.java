package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria;

import java.util.ArrayList;
import java.util.Arrays;

import ch.unibas.medizin.osce.client.i18n.Messages;
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
			return (object.booleanValue()) ? Messages.YES : Messages.NO;
		}
	});
	
	private TextBox anamnesisAnswerText = new TextBox();
	private IsWidget currentAnswerWidget;
	
	@UiField(provided = true)
    ValueListBox<BindType> bindType = new ValueListBox<BindType>(new AbstractRenderer<ch.unibas.medizin.osce.shared.BindType>() {
        public String render(ch.unibas.medizin.osce.shared.BindType obj) {
            return obj == null ? "" : String.valueOf(obj);
        }
    });
    
    @UiField(provided = true)
    ValueListBox<Comparison2> comparison = new ValueListBox<Comparison2>(new AbstractRenderer<ch.unibas.medizin.osce.shared.Comparison2>() {
        public String render(ch.unibas.medizin.osce.shared.Comparison2 obj) {
            return obj == null ? "" : String.valueOf(obj);
        }
    });

	public StandardizedPatientAdvancedSearchAnamnesisPopupImpl() {
		
		anamnesisQuestionSuggestBox = new SuggestBox(new ProxySuggestOracle<AnamnesisCheckProxy>(new AbstractRenderer<AnamnesisCheckProxy>() {
			@Override
			public String render(AnamnesisCheckProxy object) {
				return object.getText();
			}
		}, ",;:. \t?!_-/\\"));
		
		anamnesisQuestionSuggestBox.setText(Messages.ENTER_QUESTION);
		anamnesisQuestionSuggestBox.getTextBox().addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				if (anamnesisQuestionSuggestBox.getText().equals(Messages.ENTER_QUESTION)) {
					anamnesisQuestionSuggestBox.setText("");	
				}
			}
		});
		anamnesisQuestionSuggestBox.getTextBox().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (anamnesisQuestionSuggestBox.getText().equals("")) {
					anamnesisQuestionSuggestBox.setText(Messages.ENTER_QUESTION);
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
		comparison.setValue(Comparison2.values()[0]);
		comparison.setAcceptableValues(Arrays.asList(Comparison2.values()));
		
		bindType.setValue(BindType.values()[0]);
		bindType.setAcceptableValues(Arrays.asList(BindType.values()));
		
		addAnamnesisValueButton.setText(Messages.ADD);
		addAnamnesisValues.setText(Messages.ANAMNESIS_VALUES);
		
		anamnesisAnswerText.setText(Messages.ENTER_ANSWER);
		anamnesisAnswerText.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				if (anamnesisAnswerText.getText().equals(Messages.ENTER_ANSWER)) {
					anamnesisAnswerText.setText("");
				}
			}
		});
		anamnesisAnswerText.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (anamnesisAnswerText.getText().equals("")) {
					anamnesisAnswerText.setText(Messages.ENTER_ANSWER);
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
		if (currentAnswerWidget != null) {
			anamnesisAnswerPanel.remove(currentAnswerWidget);
		}
		
		if (proxy == null) {
			return;
		} else if (proxy.getType() == AnamnesisCheckTypes.QuestionMultM || proxy.getType() == AnamnesisCheckTypes.QuestionMultS) {
			currentAnswerWidget = anamnesisAnswerMCSelector;
			Log.info("proxy.getValue() = " + proxy.getValue());
			anamnesisAnswerMCSelector.setValue(proxy.getValue().split("\\|")[0]);
			anamnesisAnswerMCSelector.setAcceptableValues(Arrays.asList(proxy.getValue().split("\\|")));
		} else if (proxy.getType() == AnamnesisCheckTypes.QuestionYesNo) {
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
		delegate.addAnamnesisValueButtonClicked();
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
