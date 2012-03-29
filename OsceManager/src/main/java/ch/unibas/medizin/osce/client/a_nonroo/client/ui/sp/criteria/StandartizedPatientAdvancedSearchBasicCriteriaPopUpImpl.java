package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria;

import java.util.Arrays;
import java.util.Collection;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison2;
import ch.unibas.medizin.osce.shared.PossibleFields;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class StandartizedPatientAdvancedSearchBasicCriteriaPopUpImpl extends
 PopupPanel implements StandartizedPatientAdvancedSearchBasicCriteriaPopUp {

	private static StandartizedPatientAdvancedSearchBasicCriteriaPopUpImplUiBinder uiBinder = GWT
			.create(StandartizedPatientAdvancedSearchBasicCriteriaPopUpImplUiBinder.class);

	interface StandartizedPatientAdvancedSearchBasicCriteriaPopUpImplUiBinder
			extends
			UiBinder<Widget, StandartizedPatientAdvancedSearchBasicCriteriaPopUpImpl> {
	}
	
	@UiField
	TextBox value;
	@UiField
	IconButton addAdvSeaBasicButton;
		
	@UiHandler ("addAdvSeaBasicButton")
	public void addAdvSeaBasicButtonClicked(ClickEvent e) {
		delegate.addAdvSeaBasicButtonClicked(value.getValue(), bindType.getValue(), field.getValue(), comparison.getValue());
		this.hide();
	}
	
	@UiField
	IconButton addBasicData;
	
	@UiHandler ("addBasicData")
	public void addBasicDataClicked(ClickEvent e) {
		this.hide();
	}

	@UiField
	IconButton closeBoxButton;
	
	@UiHandler ("closeBoxButton")
	public void closeBoxButtonClicked(ClickEvent e) {
		this.hide();
	}
	
    @UiField(provided = true)
    ValueListBox<BindType> bindType = new ValueListBox<BindType>(new EnumRenderer<BindType>());
    
    @UiField(provided = true)
    ValueListBox<Comparison2> comparison = new ValueListBox<Comparison2>(new EnumRenderer<Comparison2>(EnumRenderer.Type.NUMERIC));
    
    @UiField(provided = true)
    ValueListBox<PossibleFields> field = new ValueListBox<PossibleFields>(new EnumRenderer<PossibleFields>());
	
	private Delegate delegate;
	
	@UiField
	HorizontalPanel parentPanel;

	public StandartizedPatientAdvancedSearchBasicCriteriaPopUpImpl() {
		
		setWidget(uiBinder.createAndBindUi(this));
		
		bindType.setValue(BindType.values()[0]);
		setBindTypePickerValues(Arrays.asList(BindType.values()));
		
		field.setValue(PossibleFields.HEIGHT);
		field.setAcceptableValues(Arrays.asList(new PossibleFields[] 
				{PossibleFields.HEIGHT, PossibleFields.WEIGHT, PossibleFields.BMI}));
		
		comparison.setValue(Comparison2.values()[0]);
		setComparisonPickerValues(Arrays.asList(Comparison2.values()));
		
		OsceConstants constants = GWT.create(OsceConstants.class);
		addAdvSeaBasicButton.setText(constants.add());
		addBasicData.setText(constants.basicFilter());
	}
	
    public void setBindTypePickerValues(Collection<BindType> values) {
        bindType.setAcceptableValues(values);
    }
    
    public void setComparisonPickerValues(Collection<Comparison2> values) {
        comparison.setAcceptableValues(values);
    }
    
    public void setFieldPickerValues(Collection<PossibleFields> values) {
        field.setAcceptableValues(values);
    }

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void display(Button addBasicData) {
		this.show();
		this.setPopupPosition(addBasicData.getAbsoluteLeft() - 5, addBasicData.getAbsoluteTop() - getOffsetHeight()/2 - 4);
	}
}
