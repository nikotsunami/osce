package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class StandardizedPatientAdvancedSearchNationalityPopupImpl extends PopupPanel implements StandardizedPatientAdvancedSearchNationalityPopup {

	private static StandardizedPatientAdvancedSearchNationalityPopUpImplUiBinder uiBinder = GWT
			.create(StandardizedPatientAdvancedSearchNationalityPopUpImplUiBinder.class);

	interface StandardizedPatientAdvancedSearchNationalityPopUpImplUiBinder
			extends
			UiBinder<Widget, StandardizedPatientAdvancedSearchNationalityPopupImpl> {
	}
	
	private Delegate delegate;
	
	@UiField
	IconButton addNationalityButton;
	@UiField
	IconButton closeBoxButton;
	@UiField
	IconButton nationalityButton;
	
	@UiField (provided=true)
	ValueListBox<NationalityProxy> nationality = new ValueListBox<NationalityProxy>(new AbstractRenderer<NationalityProxy>() {
        public String render(NationalityProxy obj) {
            return obj == null ? "" : String.valueOf(obj.getNationality());
        }
    });
	
	@UiField(provided = true)
    ValueListBox<BindType> bindType = new ValueListBox<BindType>(new EnumRenderer<BindType>());
    
    @UiField(provided = true)
    ValueListBox<Comparison> comparison = new ValueListBox<Comparison>(new EnumRenderer<Comparison>(EnumRenderer.Type.NATIONALITY));

 // Highlight onViolation
    Map<String, Widget> advanceSearchCriteriaMap;
 	// E Highlight onViolation
    
	public StandardizedPatientAdvancedSearchNationalityPopupImpl() {
		setWidget(uiBinder.createAndBindUi(this));
		bindType.setValue(BindType.values()[0]);
		bindType.setAcceptableValues(Arrays.asList(BindType.values()));
		comparison.setValue(Comparison.EQUALS);
		comparison.setAcceptableValues(Comparison.getNonNumericComparisons());

		OsceConstants constants = GWT.create(OsceConstants.class);
		addNationalityButton.setText(constants.add());
		nationalityButton.setText(constants.nationalities());
		
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
	
	@UiHandler("addNationalityButton")
	public void addLanguageButtonClicked(ClickEvent event) {
	/*	delegate.addNationalityButtonClicked(nationality.getValue(), bindType.getValue(), comparison.getValue());
		this.hide();*/
		Log.info("Call addAdvSearchSaveMethod");
		addAdvSearchSaveMethod();
	}

	public void addAdvSearchSaveMethod()
	{
		Log.info("Call addAdvSearchSaveMethod");
		delegate.addNationalityButtonClicked(nationality.getValue(), bindType.getValue(), comparison.getValue());
		this.hide();
	}

	/*Advance search popup changes end*/
	@UiHandler("closeBoxButton")
	public void closeBoxButtonClicked(ClickEvent event) {
		this.hide();
	}
	
	@UiHandler("nationalityButton")
	public void languageButtonClicked(ClickEvent event) {
		this.hide();
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public void display(Button sourceButton) {
		this.show();
		this.setPopupPosition(sourceButton.getAbsoluteLeft() - 5, sourceButton.getAbsoluteTop() - getOffsetHeight()/2 - 4);
	}

	//SPEC Change
	@Override
	public void display(int positionX,int positionY) {
		this.show();
		this.setPopupPosition(positionX,positionY);
	}
	//SPEC Change
	@Override
	public ValueListBox<NationalityProxy> getNationalityBox() {
		return nationality;
	}

	@Override
	public Map getMap() {
		// TODO Auto-generated method stub
		return this.advanceSearchCriteriaMap;
	}

}
