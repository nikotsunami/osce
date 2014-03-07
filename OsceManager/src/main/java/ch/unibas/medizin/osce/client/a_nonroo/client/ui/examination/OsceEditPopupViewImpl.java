package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import java.util.Date;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class OsceEditPopupViewImpl extends PopupPanel implements OsceEditPopupView {

	private static final Binder BINDER = GWT.create(Binder.class);
	
	interface Binder extends UiBinder<Widget, OsceEditPopupViewImpl> {
	}

	private Delegate delegate;
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
	
	private static final DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("yyyy-MM-dd hh:mm");
	private static final DateTimeFormat dateToString = DateTimeFormat.getFormat("yyyy-MM-dd");
	
	@UiField
	DateBox dateTextBox;
	
	@UiField 
	TextBox startTime;
	
	@UiField
	TextBox endTime;
	
	@UiField
	TextBox oscePostTextBox;
	
	@UiField
	IconButton previewButton;
	
	public OsceEditPopupViewImpl() {
		super(true);
		add(BINDER.createAndBindUi(this));
		setAutoHideEnabled(true);
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	public DateBox getDateTextBox() {
		return dateTextBox;
	}
	
	public TextBox getStartTime() {
		return startTime;
	}
	
	public TextBox getEndTime() {
		return endTime;
	}

	@UiHandler("previewButton")
	public void previewButtonClicked(ClickEvent e)
	{
		if (checkOscePost(oscePostTextBox.getValue()) == false)
		{
			MessageConfirmationDialogBox dateDialog=new MessageConfirmationDialogBox(constants.warning());
			dateDialog.showConfirmationDialog(constants.warningNoOfOscePost());				
			return;
		}
			
		if(dateTextBox.getValue()==null){
			MessageConfirmationDialogBox dateDialog=new MessageConfirmationDialogBox(constants.warning());
			dateDialog.showConfirmationDialog(constants.warningDateEmpty());				
			return;
		}
		
		if(startTime.getValue()==null || startTime.getValue().isEmpty()){
			MessageConfirmationDialogBox startTimeMessageDialog=new MessageConfirmationDialogBox(constants.warning());
			startTimeMessageDialog.showConfirmationDialog(constants.warningStartTime());
			return;
		}

		if(startTime.getValue().isEmpty() == false){			
			if(checkTimeValidation(startTime.getValue()) == false)
			{
				startTime.setValue("");
				return;
			}
		}
						
		if(endTime.getValue()==null || endTime.getValue().isEmpty()){
			MessageConfirmationDialogBox endTimeMessageDialog=new MessageConfirmationDialogBox(constants.warning());
			endTimeMessageDialog.showConfirmationDialog(constants.warningEndTime());
			return;
		}
		
		if(endTime.getValue().isEmpty() == false){
			if(checkTimeValidation(endTime.getValue()) == false)
			{
				endTime.setValue("");
				return;
			}
		}
		
		try
		{
			Date osceDate = dateTextBox.getValue();
			
			String osceDateString = dateToString.format(osceDate);
			Date osceStartTime = dateTimeFormat.parse(osceDateString + " " + startTime.getValue());
			Date osceEndTime = dateTimeFormat.parse(osceDateString + " " + endTime.getValue());
			
			if(osceStartTime.before(osceEndTime) == false)
			{
				MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
				dialog.showConfirmationDialog(constants.warningStartEndTime());
				return;
			}
			
			int noOfOscePost = Integer.parseInt(oscePostTextBox.getValue(), 10);		
			this.hide();
			delegate.oscePreviewButtonClicked(noOfOscePost, osceStartTime, osceEndTime);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
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
	
	public static boolean checkOscePost(String value) {
		boolean flag = true;	
		try  
		{  
			if (Integer.parseInt(value) < 1)
				flag = false;
		} 	 
		catch(NumberFormatException nfe)  
		{  
			flag = false;  
		}  
		catch (Exception e) {
			flag = false;
		}	
	  
		return flag;  
	}

}
