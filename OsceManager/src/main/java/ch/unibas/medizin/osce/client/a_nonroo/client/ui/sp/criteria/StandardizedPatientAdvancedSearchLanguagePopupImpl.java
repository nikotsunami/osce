package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison;
import ch.unibas.medizin.osce.shared.LangSkillLevel;

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

public class StandardizedPatientAdvancedSearchLanguagePopupImpl extends PopupPanel 
		implements StandardizedPatientAdvancedSearchLanguagePopup {

	private static StandardizedPatientAdvancedSearchLanguagePopupImplUiBinder uiBinder = GWT
			.create(StandardizedPatientAdvancedSearchLanguagePopupImplUiBinder.class);

	interface StandardizedPatientAdvancedSearchLanguagePopupImplUiBinder extends
			UiBinder<Widget, StandardizedPatientAdvancedSearchLanguagePopupImpl> {
	}
	
	private Delegate delegate;
	
	@UiField
	IconButton addLanguageButton;
	@UiField
	IconButton closeBoxButton;
	@UiField
	IconButton languageButton;
	
	@UiField (provided=true)
	ValueListBox<SpokenLanguageProxy> language = new ValueListBox<SpokenLanguageProxy>(new AbstractRenderer<SpokenLanguageProxy>() {
        public String render(SpokenLanguageProxy obj) {
            return obj == null ? "" : String.valueOf(obj.getLanguageName());
        }
    });
	
	@UiField (provided=true)
	ValueListBox<LangSkillLevel> skill = new ValueListBox<LangSkillLevel>(new EnumRenderer<LangSkillLevel>());
	
	@UiField(provided = true)
    ValueListBox<BindType> bindType = new ValueListBox<BindType>(new EnumRenderer<BindType>());
    
    @UiField(provided = true)
    ValueListBox<Comparison> comparison = new ValueListBox<Comparison>(new EnumRenderer<Comparison>(EnumRenderer.Type.LANGSKILL));

    // Highlight onViolation
    Map<String, Widget> advanceSearchCriteriaMap;
 	// E Highlight onViolation
    
	public StandardizedPatientAdvancedSearchLanguagePopupImpl() {
		setWidget(uiBinder.createAndBindUi(this));
		skill.setValue(LangSkillLevel.values()[0]);
		skill.setAcceptableValues(Arrays.asList(LangSkillLevel.values()));
		bindType.setValue(BindType.values()[0]);
		bindType.setAcceptableValues(Arrays.asList(BindType.values()));
		comparison.setValue(Comparison.values()[0]);
		comparison.setAcceptableValues(Arrays.asList(Comparison.values()));

		OsceConstants constants = GWT.create(OsceConstants.class);
		addLanguageButton.setText(constants.add());
		languageButton.setText(constants.languages());
		
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
	@UiHandler("addLanguageButton")
	public void addLanguageButtonClicked(ClickEvent event) {
		Log.info("Call addAdvSearchSaveMethod");
		/*delegate.addLanguageButtonClicked(language.getValue(), skill.getValue(), bindType.getValue(), comparison.getValue());
		this.hide();*/
		addAdvSearchSaveMethod();
	}

	public void addAdvSearchSaveMethod()
	{
		Log.info("Call addAdvSearchSaveMethod");
		delegate.addLanguageButtonClicked(language.getValue(), skill.getValue(), bindType.getValue(), comparison.getValue());
		this.hide();
	}
	/*Advance search popup changes start*/
	@UiHandler("closeBoxButton")
	public void closeBoxButtonClicked(ClickEvent event) {
		this.hide();
	}
	
	@UiHandler("languageButton")
	public void languageButtonClicked(ClickEvent event) {
		this.hide();
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void display(Button addLanguage) {
		this.show();
		this.setPopupPosition(addLanguage.getAbsoluteLeft() - 5, addLanguage.getAbsoluteTop() - getOffsetHeight()/2 - 4);
	}

	@Override
	public ValueListBox<SpokenLanguageProxy> getLanguageBox() {
		return language;
	}

	@Override
	public Map getMap() {
		// TODO Auto-generated method stub
		return this.advanceSearchCriteriaMap;
	}
}
