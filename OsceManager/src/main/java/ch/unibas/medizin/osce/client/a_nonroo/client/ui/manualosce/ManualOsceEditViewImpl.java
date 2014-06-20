package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import java.util.HashMap;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.scaffold.ui.ShortBox;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.OsceStatus;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ManualOsceEditViewImpl extends Composite implements ManualOsceEditView {

	private static ManualOsceEditViewImplUiBinder uiBinder = GWT.create(ManualOsceEditViewImplUiBinder.class);
	
	interface ManualOsceEditViewImplUiBinder extends UiBinder<Widget, ManualOsceEditViewImpl> {	
	}
	
	private Delegate delegate;

	private OsceConstants constants = GWT.create(OsceConstants.class);

	private OsceProxy osceProxy;
	
	@UiField
	Label shortBreakLabel;

	@UiField
	Label middleBreakLabel;	

	@UiField
	Label shortBreakSimpatLabel;		

	@UiField
	Label longBreakLabel;

	@UiField
	Label launchBreakLabel;

	@UiField
	Label maxStudentLabel;

	@UiField
	Label maxParcourLabel;

	@UiField
	Label maxRoomsLabel;
	
	@UiField
	ShortBox shortBreakTextBox;		  
	
	@UiField
	ShortBox middleBreakTextBox;		  
	
	@UiField
	ShortBox shortBreakSimpatTextBox;	
	
	@UiField
	ShortBox longBreakTextBox;	
	
	@UiField
	ShortBox launchBreakTextBox;
	
	@UiField
	IntegerBox maxStudentTextBox;	
	
	@UiField
	IntegerBox maxParcourTextBox;	
	
	@UiField
	IntegerBox maxRoomsTextBox;
	
	@UiField
	IconButton saveOsce;
	
	@UiField
	IconButton fixedBtn;

	@UiField
	IconButton closedBtn;
	
	@UiField	
	IconButton clearAllBtn;
	
	@UiField
	IconButton reopenBtn;
	
	@UiField
	IconButton calculateBtn;
	
	public Map<String, Widget> osceMap;
	
	public ManualOsceEditViewImpl() 
	{
		initWidget(uiBinder.createAndBindUi(this));
		
		saveOsce.setText(constants.save());
		clearAllBtn.setText(constants.clearAll());	
		fixedBtn.setText(constants.fixedButtonString());
		closedBtn.setText(constants.close());
		reopenBtn.setText(constants.reopenButtonString());
		calculateBtn.setText(constants.calculate());
		
		reopenBtn.getElement().getStyle().setDisplay(Display.NONE);
	
		osceMap=new HashMap<String, Widget>();
		osceMap.put("shortBreak", shortBreakTextBox);
		osceMap.put("LongBreak", longBreakTextBox);
		osceMap.put("lunchBreak", launchBreakTextBox);
		osceMap.put("maxNumberStudents", maxStudentTextBox);
		osceMap.put("numberCourses", maxParcourTextBox);
		osceMap.put("numberRooms", maxRoomsTextBox);
		osceMap.put("shortBreakSimpatChange", shortBreakSimpatTextBox);
		osceMap.put("middleBreak", middleBreakTextBox);
		
		disableEnableTextBox(false);
		
		shortBreakLabel.setText(constants.osceShortBreak() + ":");
		middleBreakLabel.setText(constants.osceMediumBreak() + ":");
		longBreakLabel.setText(constants.osceLongBreak() + ":");
		launchBreakLabel.setText(constants.osceLunchBreak() + ":");
		maxParcourLabel.setText(constants.osceMaxCircuits() + ":");
		maxStudentLabel.setText(constants.osceMaxStudents() + ":");
		maxRoomsLabel.setText(constants.osceMaxRooms() + ":");
		shortBreakSimpatLabel.setText(constants.osceSimpatsInShortBreak() + ":");
		
		saveOsce.setEnabled(false);
		fixedBtn.setEnabled(false);
		clearAllBtn.setEnabled(false);
		closedBtn.setEnabled(false);
		calculateBtn.setEnabled(false);
	}
	
	private void init() {
		if (osceProxy != null)
		{
			if (OsceStatus.OSCE_NEW.equals(osceProxy.getOsceStatus()) || OsceStatus.OSCE_GENRATED.equals(osceProxy.getOsceStatus()))
			{
				disableEnableTextBox(true);
			}
			else 
			{
				disableEnableTextBox(false);
			}
			
			changeButtonByOsceStatus(osceProxy.getOsceStatus());
			
			if (osceProxy.getShortBreak() != null)
                shortBreakTextBox.setValue(osceProxy.getShortBreak());		  
               
			if (osceProxy.getMiddleBreak() != null)
				middleBreakTextBox.setValue(osceProxy.getMiddleBreak());		
                
			if (osceProxy.getShortBreakSimpatChange() != null) 
				shortBreakSimpatTextBox.setValue(osceProxy.getShortBreakSimpatChange());	
                
			if (osceProxy.getLongBreak() != null)    
				longBreakTextBox.setValue(osceProxy.getLongBreak());	      
                
			if (osceProxy.getLunchBreakRequiredTime() != null) 
				launchBreakTextBox.setValue(osceProxy.getLunchBreak());      
                
			if (osceProxy.getMaxNumberStudents() != null)
				maxStudentTextBox.setValue(osceProxy.getMaxNumberStudents());	  
                
			if (osceProxy.getNumberCourses() != null)    
				maxParcourTextBox.setValue(osceProxy.getNumberCourses());	  
                
			if (osceProxy.getNumberRooms() != null)
				maxRoomsTextBox.setValue(osceProxy.getNumberRooms());       
		}
	}

	public void changeButtonByOsceStatus(OsceStatus osceStatus)
	{
		if (OsceStatus.OSCE_NEW.equals(osceStatus) || OsceStatus.OSCE_GENRATED.equals(osceStatus))
		{
			saveOsce.setEnabled(true);
			calculateBtn.setEnabled(true);
			fixedBtn.setEnabled(true);
			clearAllBtn.setEnabled(true);
			closedBtn.setEnabled(false);
			reopenBtn.getElement().getStyle().setDisplay(Display.NONE);
		}
		else if (OsceStatus.OSCE_FIXED.equals(osceStatus))
		{
			saveOsce.setEnabled(false);
			calculateBtn.setEnabled(false);
			fixedBtn.setEnabled(false);
			clearAllBtn.setEnabled(true);
			closedBtn.setEnabled(true);
			reopenBtn.getElement().getStyle().setDisplay(Display.NONE);
			clearAllBtn.getElement().getStyle().clearDisplay();
		}
		else if (OsceStatus.OSCE_CLOSED.equals(osceStatus))
		{
			saveOsce.setEnabled(false);
			calculateBtn.setEnabled(false);
			fixedBtn.setEnabled(false);
			closedBtn.setEnabled(false);
			reopenBtn.getElement().getStyle().clearDisplay();
			clearAllBtn.getElement().getStyle().setDisplay(Display.NONE);
		}
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}	
	
	public OsceProxy getOsceProxy() {
		return osceProxy;
	}
	
	public void setOsceProxy(OsceProxy osceProxy) {
		this.osceProxy = osceProxy;
		init();
	}	
	
	@UiHandler("calculateBtn")
	public void calculateBtnClicked(ClickEvent event)
	{
		if (osceProxy != null)
		{
			delegate.calculateButtonClicked(osceProxy);
		}
	}

	@UiHandler("saveOsce")
	public void saveOsceClicked(ClickEvent event)
	{
		if (osceProxy != null)
		{
			delegate.saveOsceClicked(osceProxy, this);
		}
	}
	
	@UiHandler("fixedBtn")
	public void fixedButtonClicked(ClickEvent event)
	{
		if (osceProxy != null)
		{	
			delegate.fixedButtonClicked(osceProxy, this);
		}
	}
	
	@UiHandler("clearAllBtn")
	public void clearAllButtonClicked(ClickEvent event)
	{
		if (osceProxy != null)
		{
			final MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.warning());
			dialogBox.showYesNoDialog(constants.manualOsceConfirmation());
			dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					dialogBox.hide();
					delegate.clearAllButtonClicked(osceProxy, ManualOsceEditViewImpl.this);
				}
			});
		}
	}
	
	@UiHandler("closedBtn")
	public void closeButtonClicked(ClickEvent event)
	{
		if (osceProxy != null)
		{
			final MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.warning());
			dialogBox.showYesNoDialog(constants.manualOsceConfirmation());
			dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					dialogBox.hide();
					delegate.closeButtonClicked(osceProxy, ManualOsceEditViewImpl.this);
				}
			});
		}
	}
	
	@UiHandler("reopenBtn")
	public void reopenButtonClicked(ClickEvent event)
	{
		if (osceProxy != null)
		{
			final MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.warning());
			dialogBox.showYesNoDialog(constants.manualOsceConfirmation());
			dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					dialogBox.hide();
					delegate.reopenButtonClicked(osceProxy, ManualOsceEditViewImpl.this);
				}
			});
		}
	}
	
	public void disableEnableTextBox(boolean value)
	{
		shortBreakTextBox.setEnabled(value);
		longBreakTextBox.setEnabled(value);
		launchBreakTextBox.setEnabled(value);
		maxStudentTextBox.setEnabled(value);
		maxParcourTextBox.setEnabled(value);
		maxRoomsTextBox.setEnabled(value);
		shortBreakSimpatTextBox.setEnabled(value);
		middleBreakTextBox.setEnabled(value);
		saveOsce.setEnabled(value);
	}
	
	public void setValueInOsceProxy(OsceProxy osceProxy)
	{
		osceProxy.setShortBreak(shortBreakTextBox.getValue());
		osceProxy.setLongBreak(longBreakTextBox.getValue());
		osceProxy.setLunchBreak(launchBreakTextBox.getValue());
		osceProxy.setMaxNumberStudents(maxStudentTextBox.getValue());
		osceProxy.setNumberCourses(maxParcourTextBox.getValue());
		osceProxy.setNumberRooms(maxRoomsTextBox.getValue());
		osceProxy.setShortBreakSimpatChange(shortBreakSimpatTextBox.getValue());
		osceProxy.setMiddleBreak(middleBreakTextBox.getValue());
		
		this.osceProxy = osceProxy;
	}
}
