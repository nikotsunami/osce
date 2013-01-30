package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison;
import ch.unibas.medizin.osce.shared.MaritalStatus;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class StandardizedPatientAdvancedSearchMaritialStatusPopupViewImpl
		extends StandardizedPatientAbstractPopupImpl implements StandardizedPatientAdvancedSearchMaritialStatusPopupView {

	private static StandardizedPatientAdvancedSearchMaritialStatusPopupViewImplUiBinder uiBinder = GWT
			.create(StandardizedPatientAdvancedSearchMaritialStatusPopupViewImplUiBinder.class);

	interface StandardizedPatientAdvancedSearchMaritialStatusPopupViewImplUiBinder
			extends
			UiBinder<Widget, StandardizedPatientAdvancedSearchMaritialStatusPopupViewImpl> {
	}

	@UiField
	IconButton addMaritialStatusButton;
	@UiField
	IconButton closeBoxButton;
	
	@UiField(provided = true)
    ValueListBox<BindType> bindType = new ValueListBox<BindType>(new EnumRenderer<BindType>());
    
    @UiField(provided = true)
    ValueListBox<Comparison> comparison = new ValueListBox<Comparison>(new EnumRenderer<Comparison>(EnumRenderer.Type.MARITIALSTATUS));
	
	@UiField (provided=true)
	ValueListBox<MaritalStatus> maritialStatusBox = new ValueListBox<MaritalStatus>(new EnumRenderer<MaritalStatus>());
	
	protected Delegate delegate;
	
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
		
	public StandardizedPatientAdvancedSearchMaritialStatusPopupViewImpl() {
		OsceConstants constants = GWT.create(OsceConstants.class);
		setWidget(uiBinder.createAndBindUi(this));
		bindType.setValue(BindType.values()[0]);
		bindType.setAcceptableValues(Arrays.asList(BindType.values()));
		comparison.setValue(Comparison.EQUALS);
		comparison.setAcceptableValues(Comparison.getNonNumericComparisons());
		
		maritialStatusBox.setValue(MaritalStatus.values()[0]);
		maritialStatusBox.setAcceptableValues(Arrays.asList(MaritalStatus.values()));
		addMaritialStatusButton.setText(constants.add());
		
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

	
	/*Advance search popup changes start*/
	@UiHandler("addMaritialStatusButton")
	public void addMaritialStatusButtonClicked(ClickEvent event) {
		Log.info("Call Add addMaritialStatusButton Button Clicked");
		/*delegate.addMaritialStatusButtonClicked(maritialStatusBox.getValue(), bindType.getValue(), comparison.getValue());
		//delegate.addProfessionButtonClicked(professionBox.getValue(), bindType.getValue(), comparison.getValue());
		this.hide();*/
		addAdvSearchSaveMethod();
	}


	public void addAdvSearchSaveMethod()
	{
		Log.info("Call addAdvSearchSaveMethod");
		delegate.addMaritialStatusButtonClicked(maritialStatusBox.getValue(), bindType.getValue(), comparison.getValue());
		//delegate.addProfessionButtonClicked(professionBox.getValue(), bindType.getValue(), comparison.getValue());
		this.hide();
	}

	/*Advance search popup changes end*/
	
	@UiHandler("closeBoxButton")
	public void closeBoxButtonClicked(ClickEvent event) {
		this.hide();
	}
	
	@Override
	public ValueListBox<MaritalStatus> getMaritialStatusBox() {
		return maritialStatusBox;
	}
}
