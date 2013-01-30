package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.Validator;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison;
import ch.unibas.medizin.osce.shared.PossibleFields;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class StandartizedPatientAdvancedSearchBasicCriteriaPopUpImpl extends
		StandardizedPatientAbstractPopupImpl implements StandartizedPatientAdvancedSearchBasicCriteriaPopUp {

	private static StandartizedPatientAdvancedSearchBasicCriteriaPopUpImplUiBinder uiBinder = GWT
			.create(StandartizedPatientAdvancedSearchBasicCriteriaPopUpImplUiBinder.class);

	interface StandartizedPatientAdvancedSearchBasicCriteriaPopUpImplUiBinder
			extends
			UiBinder<Widget, StandartizedPatientAdvancedSearchBasicCriteriaPopUpImpl> {
	}
	
	// SPEC Change
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	private MessageConfirmationDialogBox confirmationDialogBox;
	
	private Delegate delegate;
	
	@UiField
	TextBox value;
	@UiField
	IconButton addAdvSeaBasicButton;
	
	@UiField
	Label unit;
	
	@UiField
	Label valueNotAvail;
	
	/*Advance search popup changes start*/
	@UiHandler ("addAdvSeaBasicButton")
	public void addAdvSeaBasicButtonClicked(ClickEvent e) {
	// Highlight onViolation
		Log.info("Call addAdvSeaBasicButtonClicked");
		addAdvSearchSaveMethod();
/*	if (value.getValue().trim().compareToIgnoreCase("") == 0) {
		valueNotAvail.setText("Please enter a Value");
		return;
	}
	else{*/
	// E Highlight onViolation		
		/*valueNotAvail.setText("");
		
		// Highlight onViolation		
		delegate.addAdvSeaBasicButtonClicked(null, value.getValue(), "", bindType.getValue(), field.getValue(), comparison.getValue());*/
		//this.hide();	
	//}
	// E Highlight onViolation

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
	
	public void addAdvSearchSaveMethod()
	{
		Log.info("Call addAdvSearchSaveMethod");
		/*valueNotAvail.setText("");*/
		valueNotAvail.setText("");
		// Highlight onViolation		
		if(Validator.isNotNull(value.getValue())){
		delegate.addAdvSeaBasicButtonClicked(null, value.getValue(), "", bindType.getValue(), field.getValue(), comparison.getValue());
		this.hide();
		}else{
			confirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
			confirmationDialogBox.showConfirmationDialog(constants.warningFillRequiredFields());
		}
	}

	/*Advance search popup changes end*/
	
	@UiField
	IconButton closeBoxButton;
	
	@UiHandler ("closeBoxButton")
	public void closeBoxButtonClicked(ClickEvent e) {
		this.hide();
	}
	
    @UiField(provided = true)
    ValueListBox<BindType> bindType = new ValueListBox<BindType>(new EnumRenderer<BindType>());
    
    @UiField(provided = true)
    ValueListBox<Comparison> comparison = new ValueListBox<Comparison>(new EnumRenderer<Comparison>(EnumRenderer.Type.NUMERIC));
    
    @UiField(provided = true)
    ValueListBox<PossibleFields> field = new ValueListBox<PossibleFields>(new EnumRenderer<PossibleFields>());
	
	@UiField
	HorizontalPanel parentPanel;
	
	StandartizedPatientAdvancedSearchBasicCriteriaPopUpImpl advanceSearchView;
	// E Highlight onViolation

	public StandartizedPatientAdvancedSearchBasicCriteriaPopUpImpl() {
		setWidget(uiBinder.createAndBindUi(this));
		
		bindType.setValue(BindType.values()[0]);
		setBindTypePickerValues(Arrays.asList(BindType.values()));
		
		field.setValue(PossibleFields.HEIGHT);
		field.setAcceptableValues(Arrays.asList(new PossibleFields[] 
				{PossibleFields.HEIGHT, PossibleFields.WEIGHT, PossibleFields.BMI, PossibleFields.AGE}));
		
		comparison.setValue(Comparison.values()[0]);
		setComparisonPickerValues(Arrays.asList(Comparison.values()));
		
		final OsceConstants constants = GWT.create(OsceConstants.class);
		addAdvSeaBasicButton.setText(constants.add());
		unit.setText("[" + constants.heightUnit() + "]");
		
		value.addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					addAdvSeaBasicButton.click();
			}
		});
		
		valueNotAvail.setText("");		
		field.addValueChangeHandler(new ValueChangeHandler<PossibleFields>() {
			@Override
			public void onValueChange(ValueChangeEvent<PossibleFields> event) {
				if (event.getValue() == PossibleFields.BMI) {
					comparison.setValue(Comparison.values()[0]);
					setComparisonPickerValues(Arrays.asList(Comparison.values()));
					unit.setText("");
				} else if (event.getValue() == PossibleFields.HEIGHT) {
					comparison.setValue(Comparison.values()[0]);
					setComparisonPickerValues(Arrays.asList(Comparison.values()));
					unit.setText("[" + constants.heightUnit() + "]");
				} else if (event.getValue() == PossibleFields.WEIGHT) {
					comparison.setValue(Comparison.values()[0]);
					setComparisonPickerValues(Arrays.asList(Comparison.values()));
					unit.setText("[" + constants.weightUnit() + "]");
				}/* else if (event.getValue() == PossibleFields.GENDER) {
					comparison.setValue(Comparison.values()[0]);
					setComparisonPickerValues(Arrays.asList(new Comparison[]{Comparison.EQUALS, Comparison.NOT_EQUALS}));
					unit.setText("");
				}*/ else if (event.getValue() == PossibleFields.AGE) {
					comparison.setValue(Comparison.values()[0]);
					setComparisonPickerValues(Arrays.asList(Comparison.values()));
					unit.setText("[" + constants.ageUnit() + "]");
				}
			}
		});
		
		// Highlight onViolation
		advanceSearchView=this;
		advanceSearchCriteriaMap=new HashMap<String, Widget>();
		advanceSearchCriteriaMap.put("field", field);
		advanceSearchCriteriaMap.put("bindType", bindType);
		advanceSearchCriteriaMap.put("comparation", comparison);
		advanceSearchCriteriaMap.put("value", value);
		advanceSearchCriteriaMap.put("shownValue", value);
				
		// E Highlight onViolation
		
		 /*Advance search popup changes start*/
			this.sinkEvents(Event.KEYEVENTS);
			this.sinkEvents(Event.ONFOCUS);
			/*Advance search popup changes end*/
	}
	
    public void setBindTypePickerValues(Collection<BindType> values) {
        bindType.setAcceptableValues(values);
    }
    
    public void setComparisonPickerValues(Collection<Comparison> values) {
        comparison.setAcceptableValues(values);
    }
    
    public void setFieldPickerValues(Collection<PossibleFields> values) {
        field.setAcceptableValues(values);
    }

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
}
