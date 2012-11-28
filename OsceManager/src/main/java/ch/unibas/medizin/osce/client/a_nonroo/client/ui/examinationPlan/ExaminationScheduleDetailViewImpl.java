package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;



import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaMainNav;
import ch.unibas.medizin.osce.client.a_nonroo.client.ResolutionSettings;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickHandler;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ExaminationScheduleDetailViewImpl extends Composite implements ExaminationScheduleDetailView, MenuClickHandler{
	
	private static ExaminationScheduleDetailViewImplUiBinder uiBinder = GWT
			.create(ExaminationScheduleDetailViewImplUiBinder.class);

	interface ExaminationScheduleDetailViewImplUiBinder extends UiBinder<Widget, ExaminationScheduleDetailViewImpl> {
	}

	private Delegate delegate;
	
	private Presenter presenter;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	@UiField
	ScrollPanel scrollPanel;
	
	@UiField
	Label shortBreakTxt;
	
	@UiField
	Label shortBreakSimPatChangeTxt;
	
	@UiField
	Label shortBreakSimPatChangeValue;
	
	@UiField
	Label longBreakTxt;
	
	@UiField
	Label longBreakValue;
	
	@UiField
	Button exportButtonStudent;
	
	@UiField
	Button exportButtonSP;
	
	public Label getShortBreakSimPatChangeValue() {
		return shortBreakSimPatChangeValue;
	}

	public Label getLongBreakValue() {
		return longBreakValue;
	}

	public Label getShortBreakTxt() {
		return shortBreakTxt;
	}

	public Label getShortBreakValue() {
		return shortBreakValue;
	}

	public Label getLunchTimeTxt() {
		return lunchTimeTxt;
	}

	public Label getLunchTimeValue() {
		return lunchTimeValue;
	}

	public Label getMiddleBreakTxt() {
		return middleBreakTxt;
	}

	public Label getMiddleBreakValue() {
		return middleBreakValue;
	}

	public Label getNumOfRoomsTxt() {
		return numOfRoomsTxt;
	}

	public Label getNumOfRoomsValue() {
		return numOfRoomsValue;
	}

	@UiField
	Button studentAssignmentButton;
	
	public Button getStudentAssignmentButton() {
		return studentAssignmentButton;
	}

	public Button getSpAssignmentButton() {
		return spAssignmentButton;
	}

	@UiField
	Button spAssignmentButton;
	
	
	@UiField
	Label shortBreakValue;
	
	@UiField
	Label lunchTimeTxt;
	
	@UiField
	Label lunchTimeValue;
	
	@UiField
	Label middleBreakTxt;
	
	@UiField
	Label middleBreakValue;
	
	@UiField
	Label numOfRoomsTxt;
	
	@UiField 
	Label numOfRoomsValue;
	
	@UiField
	VerticalPanel sequenceVP;
	
	private OsceProxy osceProxy;
	
	public OsceProxy getOsceProxy() {
		return osceProxy;
	}

	public void setOsceProxy(OsceProxy osceProxy) {
		this.osceProxy = osceProxy;
	}

	public VerticalPanel getSequenceVP() {
		return sequenceVP;
	}

	public ExaminationScheduleDetailViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		shortBreakTxt.setText(constants.exaPlanShortBreak());
		lunchTimeTxt.setText(constants.exaPlanLunchBreak());
		middleBreakTxt.setText(constants.exaPlanMiddleBreak());
		numOfRoomsTxt.setText(constants.exaPlanNumRooms());
		studentAssignmentButton.setText(constants.exaPlanAssignStudents());
		spAssignmentButton.setText(constants.exaPlanAssignSp());
		shortBreakSimPatChangeTxt.setText(constants.exaPlanChangeBreak());
		longBreakTxt.setText(constants.exaPlanLongBreak());
		
		exportButtonStudent.setText(constants.exportStudent());
		exportButtonSP.setText(constants.exportSP());
		int height = ResolutionSettings.getRightWidgetHeight() - 55;
		scrollPanel.setHeight(height+"px");		
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@UiHandler("studentAssignmentButton")
	public void studentAssignmentButtonClicked(ClickEvent event)
	{
		showStudentAssignPopup();
		
	}
	public void showStudentAssignPopup()
	{
		final PopupPanel popup=new PopupPanel(true);
		
		VerticalPanel vp=new VerticalPanel();
		
		HorizontalPanel hp=new HorizontalPanel();
		Button alphabeticButton=new Button();
		alphabeticButton.setText(constants.alphabeticOrder());
		
		Button scrumbleButton=new Button();
		scrumbleButton.setText(constants.scrumbleOrder());
		
		Button cancelButton=new Button();
		cancelButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				popup.hide();
				
			}
		});
		cancelButton.setText(constants.cancel());
		
	
		
		alphabeticButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				delegate.autoAssignStudent(osceProxy.getId(),0);
				popup.hide();
			}
		});
		
		scrumbleButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				delegate.autoAssignStudent(osceProxy.getId(),1);
				popup.hide();
			}
		});
		
		hp.setSpacing(3);
		hp.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		hp.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		hp.add(alphabeticButton);
		hp.add(scrumbleButton);
		hp.add(cancelButton);
		
		
		Label msgLbl=new Label();
		msgLbl.setText(constants.previousAssignmentWillLost());
		
		vp.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		vp.setSpacing(5);
		vp.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		vp.add(msgLbl);
		vp.add(hp);
		
		popup.add(vp);
		
		popup.setPopupPosition(studentAssignmentButton.getAbsoluteLeft()-40, studentAssignmentButton.getAbsoluteTop()-60);
		popup.show();
	}
	public void showPopup()
	{
		final PopupPanel popup=new PopupPanel(true);
		
		VerticalPanel vp=new VerticalPanel();
		
		HorizontalPanel hp=new HorizontalPanel();
		Button okButton=new Button();
		okButton.setText(constants.okBtn());
		
		Button cancelButton=new Button();
		cancelButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				popup.hide();
				
			}
		});
		cancelButton.setText(constants.cancel());
		
	
		
		okButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				delegate.autoAssignSP(osceProxy.getId());
				
			}
		});
		
		hp.setSpacing(3);
		hp.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		hp.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		hp.add(okButton);
		hp.add(cancelButton);
		
		Label msgLbl=new Label();
		msgLbl.setText(constants.previousAssignmentWillLost());
		
		vp.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		vp.setSpacing(5);
		vp.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		vp.add(msgLbl);
		vp.add(hp);
		
		popup.add(vp);
		
		popup.setPopupPosition(studentAssignmentButton.getAbsoluteLeft()+40, studentAssignmentButton.getAbsoluteTop()-60);
		popup.show();
	}
	
	@UiHandler("spAssignmentButton")
	public void spAssignmentButtonClicked(ClickEvent event)
	{
		showPopup();
		
	}
	
	@Override
	public void onMenuClicked(MenuClickEvent event) {
		
		OsMaMainNav.setMenuStatus(event.getMenuStatus());
		
		int height = ResolutionSettings.getRightWidgetHeight() - 55;
		scrollPanel.setHeight(height+"px");
	}
	
	@UiHandler("exportButtonStudent")
	public void exportButtonStudentClicked(ClickEvent event)
	{
		Log.info("exportButton");
		delegate.exportAssignment(osceProxy.getId(),0);
	}
	
	@UiHandler("exportButtonSP")
	public void exportButtonSPClicked(ClickEvent event)
	{
		Log.info("exportButton");
		delegate.exportAssignment(osceProxy.getId(),1);
	}

}
