package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class SPViewImpl extends Composite implements SPView{
	
	private static SPViewImplUiBinder uiBinder = GWT
			.create(SPViewImplUiBinder.class);

	interface SPViewImplUiBinder extends UiBinder<Widget, SPViewImpl> {
	}
	
	private final OsceConstants constants=GWT.create(OsceConstants.class);	
	
	private Delegate delegate;
	
	@UiField
	Label spLbl;
	
	@UiField
	FocusPanel spPanel;
	
	PopupView popupView;
	
	public FocusPanel getSpPanel() {
		return spPanel;
	}

	private AssignmentProxy assignmentProxy;
	
	public AssignmentProxy getAssignmentProxy() {
		return assignmentProxy;
	}

	public void setAssignmentProxy(AssignmentProxy assignmentProxy) {
		this.assignmentProxy = assignmentProxy;
	}	

	public Label getSpLbl() {
		return spLbl;
	}

	public SPViewImpl()
	{
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	@UiHandler("spPanel")
	public void spPanelClicked(ClickEvent event)
	{
		Log.info("spPanel Clicked");
		if(assignmentProxy.getPatientInRole()==null)
		{
			MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
			dialogBox.showConfirmationDialog(constants.patientNotAssigned());
		}
		else	
			showSPPanelPopup();
	}
	public void showSPPanelPopup()
	{
		if(popupView == null)
		{
			popupView=new PopupViewImpl();
			popupView.createSPPopupView();
			
			((PopupViewImpl)popupView).setAnimationEnabled(true);
		
			
			
			
			//((PopupViewImpl)popupView).setWidth("150px");

		
			RootPanel.get().add(((PopupViewImpl)popupView));
			
			
			((PopupViewImpl)popupView).setPopupPosition(this.getAbsoluteLeft(), this.getAbsoluteTop()-130);
			
			popupView.getOkButton().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					//validate Entered Data
					((PopupViewImpl)popupView).hide();
					
					
				}
			});
			
			
			//setDAta
			popupView.getNameValue().setText(assignmentProxy.getPatientInRole().getPatientInSemester().getStandardizedPatient().getName());
			popupView.getStartTimeValue().setText(DateTimeFormat.getShortDateTimeFormat().format(assignmentProxy.getTimeStart()));
			popupView.getEndTimeValue().setText(DateTimeFormat.getShortDateTimeFormat().format(assignmentProxy.getTimeEnd()));
			
		}
		
		((PopupViewImpl)popupView).show();
		
	}
	
}
