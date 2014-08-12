package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import java.util.Date;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayRotationProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.BreakType;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class ManualOsceDaySubViewImpl extends Composite implements ManualOsceDaySubView {

	private static ManualOsceDaySubViewImplUiBinder uiBinder = GWT.create(ManualOsceDaySubViewImplUiBinder.class);
	
	interface ManualOsceDaySubViewImplUiBinder extends UiBinder<Widget, ManualOsceDaySubViewImpl> {
	}

	private Delegate delegate;
	
	private static final DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("yyyy-MM-dd hh:mm");
	
	private static final DateTimeFormat dateToString = DateTimeFormat.getFormat("yyyy-MM-dd");
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
	
	@UiField
	DateBox dateTextBox;
	
	@UiField
	TextBox startTimeTextBox;
	
	@UiField
	IconButton saveOsceDayValue;
	
	@UiField
	IconButton deleteOsceDay;
	
	@UiField
	Label firstLongBreakValue;
	
	@UiField
	Label lunchBreakValue;
	
	@UiField
	Label secondLongBreakValue;
	
	@UiField
	Label timeEndValue;
	
	@UiField
	Label studentValue;
	
	@UiField
	Label standardizedPatientValue;
	
	@UiField
	Label roomValue;
	
	@UiField
	Label dayLabel;
	
	private OsceDayProxy osceDayProxy;	
	
	@UiField
	VerticalPanel sequencePanel;
	
	public ManualOsceDaySubViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		dateTextBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("MMM dd, yyyy")));
		
		firstLongBreakValue.setText(constants.manualOsceUnavailable());
		secondLongBreakValue.setText(constants.manualOsceUnavailable());
		lunchBreakValue.setText(constants.manualOsceUnavailable());
		dayLabel.setText(constants.circuitDay());
		studentValue.setText("0");
		standardizedPatientValue.setText("0");
		roomValue.setText("0");
	}

	public void init(){
		if (osceDayProxy != null)
		{
			if (osceDayProxy.getOsceDate() != null)
				dateTextBox.setValue(osceDayProxy.getOsceDate());
				
			if (osceDayProxy.getTimeStart() != null)
				startTimeTextBox.setText(DateTimeFormat.getFormat("HH:mm").format(osceDayProxy.getTimeStart()).substring(0,5));
			
			if (osceDayProxy.getTimeEnd() != null)
				timeEndValue.setText(DateTimeFormat.getFormat("HH:mm").format(osceDayProxy.getTimeEnd()).substring(0,5));
			else
				timeEndValue.setText(DateTimeFormat.getFormat("HH:mm").format(osceDayProxy.getTimeStart()).substring(0,5));
			
			if (osceDayProxy.getLunchBreakStart() != null)
				lunchBreakValue.setText(DateTimeFormat.getFormat("HH:mm").format(osceDayProxy.getLunchBreakStart()).substring(0,5));
			else
				lunchBreakValue.setText(constants.manualOsceUnavailable());
			
			if (osceDayProxy.getStudentCount() != null)
				studentValue.setText(osceDayProxy.getStudentCount().toString());
			
			if (osceDayProxy.getSpCount() != null)
				standardizedPatientValue.setText(osceDayProxy.getSpCount().toString());
			
			if (osceDayProxy.getRoomCount() != null)
				roomValue.setText(osceDayProxy.getRoomCount().toString());
			
			if (osceDayProxy.getOsceSequences() != null)
			{	
				firstLongBreakValue.setText(constants.manualOsceUnavailable());
				secondLongBreakValue.setText(constants.manualOsceUnavailable());
				
				List<OsceSequenceProxy> osceSeqList = osceDayProxy.getOsceSequences();
				for (int i=0; i<osceSeqList.size(); i++)
				{
					OsceSequenceProxy osceSeq = osceSeqList.get(i);
					List<OsceDayRotationProxy> osceDayRotationList = osceSeq.getOsceDayRotations();
					if (osceSeq.getNumberRotation() == 1 && osceDayRotationList.size() == 2)
					{
						OsceDayRotationProxy osceDayRotationProxy = osceDayRotationList.get(0);
						if (BreakType.LONG_BREAK.equals(osceDayRotationProxy.getBreakType()))
						{
							if (i == 0)
							{
								firstLongBreakValue.setText(DateTimeFormat.getFormat("HH:mm").format(osceDayRotationProxy.getTimeEnd()).substring(0,5));
							}
							else if (i == 1)
							{
								secondLongBreakValue.setText(DateTimeFormat.getFormat("HH:mm").format(osceDayRotationProxy.getTimeEnd()).substring(0,5));
							}
						}
					}
					else
					{
						if (osceDayRotationList.size() == 0)
						{
							firstLongBreakValue.setText(constants.manualOsceUnavailable());
							secondLongBreakValue.setText(constants.manualOsceUnavailable());
						}
						else
						{
							for (int j=0; j<osceDayRotationList.size(); j++)
							{
								OsceDayRotationProxy osceDayRotationProxy = osceDayRotationList.get(j);
								if (BreakType.LONG_BREAK.equals(osceDayRotationProxy.getBreakType()) && j < (osceDayRotationList.size() - 1))
								{
									if (constants.manualOsceUnavailable().equals(firstLongBreakValue.getText()))
									{
										firstLongBreakValue.setText(DateTimeFormat.getFormat("HH:mm").format(osceDayRotationProxy.getTimeEnd()).substring(0,5));
									}
									else if (constants.manualOsceUnavailable().equals(secondLongBreakValue.getText()))
									{
										secondLongBreakValue.setText(DateTimeFormat.getFormat("HH:mm").format(osceDayRotationProxy.getTimeEnd()).substring(0,5));
									}
								}
							}
						}
					}
				}
			}
				
			
		}
	}
	
	@UiHandler("saveOsceDayValue")
	public void saveOsceDayClicked(ClickEvent event)
	{
		if (osceDayProxy == null)
			return;
		
		if(dateTextBox.getValue()==null){
			MessageConfirmationDialogBox dateDialog=new MessageConfirmationDialogBox(constants.warning());
			dateDialog.showConfirmationDialog(constants.warningDateEmpty());				
			return;
		}
		
		if(startTimeTextBox.getValue()==null || startTimeTextBox.getValue().isEmpty()){
			MessageConfirmationDialogBox startTimeMessageDialog=new MessageConfirmationDialogBox(constants.warning());
			startTimeMessageDialog.showConfirmationDialog(constants.warningStartTime());
			return;
		}

		if(startTimeTextBox.getValue().isEmpty() == false){			
			if(checkTimeValidation(startTimeTextBox.getValue()) == false)
			{
				startTimeTextBox.setValue("");
				return;
			}
		}
		
		Date osceDate = dateTextBox.getValue();
		String osceDateString = dateToString.format(osceDate);
		Date osceStartTime = dateTimeFormat.parse(osceDateString + " " + startTimeTextBox.getValue());
			
		delegate.saveOsceDayClicked(osceDayProxy, osceDate, osceStartTime);
	}
	
	@UiHandler("deleteOsceDay")
	public void deleteOsceDayClicked(ClickEvent event)
	{
		if (osceDayProxy != null)
		{
			final MessageConfirmationDialogBox confirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
			confirmationDialogBox.showYesNoDialog(constants.manualOsceDeleteOsceDay());
			
			confirmationDialogBox.getYesBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					confirmationDialogBox.hide();
					delegate.deleteOsceDayClicked(ManualOsceDaySubViewImpl.this, osceDayProxy);
				}
			});
		}
	}
	
	public OsceDayProxy getOsceDayProxy() {
		return osceDayProxy;
	}
	
	public void setOsceDayProxy(OsceDayProxy osceDayProxy) {
		this.osceDayProxy = osceDayProxy;
		init();
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
		
	public VerticalPanel getSequencePanel() {
		return sequencePanel;
	}
	
	public boolean checkTimeValidation(String time){
		boolean dayStartTimeValidflag=true;
		
		if(time.matches("^[0-9]{2}\\:[0-9]{2}$") == false)
		{
			MessageConfirmationDialogBox sTimeValueDialog=new MessageConfirmationDialogBox(constants.warning());
			sTimeValueDialog.showConfirmationDialog(constants.warningTimeFormat());			
			dayStartTimeValidflag=false;	
		}
		
		if(new Integer(time.substring(0,2)) >= 24)
		{						
			
			MessageConfirmationDialogBox dialog1=new MessageConfirmationDialogBox(constants.warning());
			dialog1.showConfirmationDialog(constants.warningTimeHour());
			dayStartTimeValidflag=false;
		}
		
		if(new Integer(time.substring(3,5)) > 59)
		{
			MessageConfirmationDialogBox dialog2=new MessageConfirmationDialogBox(constants.warning());
			dialog2.showConfirmationDialog(constants.warningTimeMinute());
			dayStartTimeValidflag=false;
		}
		
		return dayStartTimeValidflag;
	}
	
	public Label getDayLabel() {
		return dayLabel;
	}
	
	public IconButton getDeleteOsceDay() {
		return deleteOsceDay;
	}
	
	public IconButton getSaveOsceDayValue() {
		return saveOsceDayValue;
	}
}
