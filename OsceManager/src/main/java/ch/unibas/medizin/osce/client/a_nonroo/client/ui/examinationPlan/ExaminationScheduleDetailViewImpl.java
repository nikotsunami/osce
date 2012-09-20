package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;



import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaMainNav;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickHandler;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
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
		shortBreakTxt.setText(constants.shortBreak());
		lunchTimeTxt.setText(constants.circuitLunchBreak());
		middleBreakTxt.setText(constants.middleBreak());
		numOfRoomsTxt.setText(constants.numOfRooms());
		studentAssignmentButton.setText(constants.studentAssignment());
		spAssignmentButton.setText(constants.spAssignment());
		shortBreakSimPatChangeTxt.setText(constants.simpatChangeBreak());
		longBreakTxt.setText(constants.longBreak());
		
		 //String panelWidth = (OsMaMainNav.getMenuStatus() == 0) ? "1350px" : "1130px";
		 //scrollPanel.setWidth(panelWidth);
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
		delegate.autoAssignStudent(osceProxy.getId());
	}
	
	@UiHandler("spAssignmentButton")
	public void spAssignmentButtonClicked(ClickEvent event)
	{
		delegate.autoAssignSP(osceProxy.getId());
	}
	

	@Override
	public void onMenuClicked(MenuClickEvent event) {
		
		OsMaMainNav.setMenuStatus(event.getMenuStatus());
		
		String panelWidth = (OsMaMainNav.getMenuStatus() == 0) ? "1350px" : "1130px";
		scrollPanel.setWidth(panelWidth);
		
	}

}
