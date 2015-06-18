package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;



import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaMainNav;
import ch.unibas.medizin.osce.client.a_nonroo.client.ResolutionSettings;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickHandler;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.ValueListBox;
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
	
	@UiField
	Button exportSPPlans;
	
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
	Button updateSPsAssignmentButton;
	
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
	
	@UiField
	IconButton moveLunchBreakRotation;
	
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
		//Added as per OMS-161.
		updateSPsAssignmentButton.setText(constants.updateSPsAssignment());
		shortBreakSimPatChangeTxt.setText(constants.exaPlanChangeBreak());
		longBreakTxt.setText(constants.exaPlanLongBreak());
		
		exportButtonStudent.setText(constants.exportStudent());
		exportButtonSP.setText(constants.exportSP());
		int height = ResolutionSettings.getRightWidgetHeight() - 55;
		scrollPanel.setHeight(height+"px");
		
		moveLunchBreakRotation.setText(constants.moveRotLunchBreak());
		moveLunchBreakRotation.setIcon("triangle-2-n-s");
		//moveLunchBreakRotation.setIcon("triangle-1-s");
		
		exportSPPlans.setText(constants.exportSPPlans());
	}
	
	@UiHandler("moveLunchBreakRotation")
	public void moveLunchBreakRotationClicked(ClickEvent event)
	{
		showOsceDayPopup(moveLunchBreakRotation.getAbsoluteLeft(),moveLunchBreakRotation.getAbsoluteTop());
	}
	
	private void showOsceDayPopup(int left, int top) {
		
		final PopupPanel panel = new PopupPanel();
		panel.setAutoHideEnabled(true);
		
		VerticalPanel mainVp = new VerticalPanel();
		mainVp.setSpacing(10);		
		
		Label label = new Label(constants.osceDay());
		
		final ValueListBox<OsceDayProxy> listBox = new ValueListBox<OsceDayProxy>(new AbstractRenderer<OsceDayProxy>() {

			@Override
			public String render(OsceDayProxy object) {
				String dateVal = "";
				if (object != null)
					dateVal = DateTimeFormat.getFormat("yyyy-MM-dd").format(object.getOsceDate());
				
				return dateVal;
			}
		});
		
		OsceDayProxy osceDayProxy = null;
		
		if (osceProxy != null && osceProxy.getOsce_days() != null && osceProxy.getOsce_days().size() > 0)
		{
			osceDayProxy = osceProxy.getOsce_days().get(0);
			listBox.setValue(osceProxy.getOsce_days().get(0));
			listBox.setAcceptableValues(osceProxy.getOsce_days());
		}
		
		HorizontalPanel listHp = new HorizontalPanel();
		listHp.setSpacing(5);
		listHp.add(label);
		listHp.add(listBox);
		
		HorizontalPanel btnHp = new HorizontalPanel();
		btnHp.setSpacing(5);
		final IconButton upRot = new IconButton(constants.upRotation());
		final IconButton downRot = new IconButton(constants.downRotation());
		
		upRot.setIcon("triangle-1-n");
		downRot.setIcon("triangle-1-s");		
		
		if (osceDayProxy != null)
		{
			if (osceDayProxy.getLunchBreakStart() == null)
			{
				upRot.setEnabled(false);
				downRot.setEnabled(false);
			}
			else
			{
				upRot.setEnabled(true);
				downRot.setEnabled(true);
			}
		}
		
		listBox.addValueChangeHandler(new ValueChangeHandler<OsceDayProxy>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<OsceDayProxy> event) {
				if (event.getValue() != null)
				{
					if (osceProxy != null)
					{
						OsceDayProxy osceDayProxy = event.getValue();
						String rotationStr = osceDayProxy.getBreakByRotation();
						
						if (rotationStr.contains(osceProxy.getLunchBreak().toString()))
						{
							upRot.setEnabled(true);
							downRot.setEnabled(true);
						}
						else
						{
							upRot.setEnabled(false);
							downRot.setEnabled(false);
						}
					}					
				}
			}
		});
		
		upRot.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				panel.hide();
				delegate.moveLunchBreak(1, listBox.getValue());
			}
		});
		
		downRot.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				panel.hide();
				delegate.moveLunchBreak(2, listBox.getValue());
			}
		});
		
		btnHp.add(upRot);
		btnHp.add(downRot);
		
		mainVp.add(listHp);
		mainVp.add(btnHp);
		
		panel.add(mainVp);
		
		//panel.setPopupPosition(x - 150, y - 175);
		panel.setPopupPosition(left - 125, top + 24);
		panel.show();
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
		delegate.countOsceWiseStudent(osceProxy);
		
		//showStudentAssignPopup();
		
	}
	public void showStudentAssignPopup(final boolean isLessStudent)
	{
		final PopupPanel popup=new PopupPanel(true);
	
		final CheckBox allowStructureChange = new CheckBox();
		Label textLbl = new Label(constants.requireOsceChange());
		textLbl.getElement().getStyle().setMarginTop(8, Unit.PX);
		HorizontalPanel structureChangeHp = new HorizontalPanel();
		structureChangeHp.add(allowStructureChange);
		structureChangeHp.add(textLbl);
		
		Label structureChangeMsg = new Label(constants.lessStudentMsg());
		VerticalPanel vp=new VerticalPanel();
		
		vp.setWidth("400px");
		
		VerticalPanel vp1 = new VerticalPanel();
		VerticalPanel vp2 = new VerticalPanel();
		vp1.addStyleName("studentpopup-arrow-popup-border");
		vp2.addStyleName("studentpopup-arrow-popup");
		
		vp.add(vp1);
		vp.add(vp2);
		
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
				if (isLessStudent && allowStructureChange.getValue())
					delegate.autoAssignStudent(osceProxy.getId(),0, true);
				else
					delegate.autoAssignStudent(osceProxy.getId(),0, false);
					
				popup.hide();
			}
		});
		
		scrumbleButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (isLessStudent && allowStructureChange.getValue())
					delegate.autoAssignStudent(osceProxy.getId(),1, true);
				else
					delegate.autoAssignStudent(osceProxy.getId(),1, false);
				popup.hide();
			}
		});
		
		hp.setSpacing(3);
		hp.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		hp.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		if (isLessStudent)
			hp.add(structureChangeHp);
			
		hp.add(alphabeticButton);
		hp.add(scrumbleButton);
		hp.add(cancelButton);		
		
		Label msgLbl=new Label();
		msgLbl.setText(constants.previousAssignmentWillLost());
		
		vp.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		vp.setSpacing(5);
		vp.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		
		if (isLessStudent)
			vp.add(structureChangeMsg);
		
		vp.add(msgLbl);
		vp.add(hp);
		
		
		popup.add(vp);
		
		popup.setPopupPosition(studentAssignmentButton.getAbsoluteLeft()-110, studentAssignmentButton.getAbsoluteTop()+28);
		popup.show();
	}
	public void showPopup()
	{
		final PopupPanel popup=new PopupPanel(true);
		
		VerticalPanel vp=new VerticalPanel();
		
		VerticalPanel vp1 = new VerticalPanel();
		VerticalPanel vp2 = new VerticalPanel();
		vp1.addStyleName("studentpopup-arrow-popup-border");
		vp2.addStyleName("studentpopup-arrow-popup");
		
		vp.add(vp1);
		vp.add(vp2);
		
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
		
		popup.setPopupPosition(studentAssignmentButton.getAbsoluteLeft()+40, studentAssignmentButton.getAbsoluteTop()+28);
		popup.show();
	}
	
	@UiHandler("spAssignmentButton")
	public void spAssignmentButtonClicked(ClickEvent event)
	{
		showPopup();
		
	}
	
	@UiHandler("updateSPsAssignmentButton")
	public void updateSPsAssignmentButtonClicked(ClickEvent event){
		delegate.updateSPsAssignmentButtonClicked(osceProxy.getId());
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
	
	@UiHandler("exportSPPlans")
	public void exportSPPlansButtonClicked(ClickEvent evennt)
	{
		Log.info("exportSPPlans");
		delegate.exportAssignment(osceProxy.getId(),2);
	}

}
