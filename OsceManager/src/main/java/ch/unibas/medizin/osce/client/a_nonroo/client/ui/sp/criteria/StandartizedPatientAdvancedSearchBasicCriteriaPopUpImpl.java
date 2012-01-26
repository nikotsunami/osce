package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria;


import java.util.Arrays;
import java.util.Collection;

import ch.unibas.medizin.osce.client.style.widgets.FocusableValueListBox;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison2;
import ch.unibas.medizin.osce.shared.PossibleFields;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class StandartizedPatientAdvancedSearchBasicCriteriaPopUpImpl extends
 DecoratedPopupPanel implements StandartizedPatientAdvancedSearchBasicCriteriaPopUp {

	private static StandartizedPatientAdvancedSearchBasicCriteriaPopUpImplUiBinder uiBinder = GWT
			.create(StandartizedPatientAdvancedSearchBasicCriteriaPopUpImplUiBinder.class);

	interface StandartizedPatientAdvancedSearchBasicCriteriaPopUpImplUiBinder
			extends
			UiBinder<Widget, StandartizedPatientAdvancedSearchBasicCriteriaPopUpImpl> {
	}
	
	@UiField
	TextBox value;
	@UiField
	Button addAdvSeaBasicButton;
	
	@UiHandler ("addAdvSeaBasicButton")
	public void addAdvSeaBasicButtonClicked(ClickEvent e) {
		delegate.addAdvSeaBasicButtonClicked(value.getValue(), bindType.getValue(), field.getValue(), comparition.getValue());
		this.hide();
	}

	@UiField
	Button closeBoxButton;
	
	@UiHandler ("closeBoxButton")
	public void closeBoxButtonClicked(ClickEvent e) {
		
		this.hide();
	}
	
    @UiField(provided = true)
    ValueListBox<BindType> bindType = new ValueListBox<BindType>(new AbstractRenderer<ch.unibas.medizin.osce.shared.BindType>() {

        public String render(ch.unibas.medizin.osce.shared.BindType obj) {
            return obj == null ? "" : String.valueOf(obj);
        }
    });
    
    @UiField(provided = true)
    ValueListBox<Comparison2> comparition = new ValueListBox<Comparison2>(new AbstractRenderer<ch.unibas.medizin.osce.shared.Comparison2>() {

        public String render(ch.unibas.medizin.osce.shared.Comparison2 obj) {
            return obj == null ? "" : String.valueOf(obj);
        }
    });
    
    @UiField(provided = true)
    ValueListBox<PossibleFields> field = new ValueListBox<PossibleFields>(new AbstractRenderer<ch.unibas.medizin.osce.shared.PossibleFields>() {

        public String render(ch.unibas.medizin.osce.shared.PossibleFields obj) {
            return obj == null ? "" : String.valueOf(obj);
        }
    });
	
	
	private Delegate delegate;
	

	public StandartizedPatientAdvancedSearchBasicCriteriaPopUpImpl() {
		setWidget(uiBinder.createAndBindUi(this));
		setBindTypePickerValues(Arrays.asList(BindType.values()));
		setFieldPickerValues(Arrays.asList(PossibleFields.values()));
		setComparisonPickerValues(Arrays.asList(Comparison2.values()));
	}
	
    public void setBindTypePickerValues(Collection<BindType> values) {
        bindType.setAcceptableValues(values);
    }
    
    public void setComparisonPickerValues(Collection<Comparison2> values) {
        comparition.setAcceptableValues(values);
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

		this.showRelativeTo(addBasicData);
		
	}

}
