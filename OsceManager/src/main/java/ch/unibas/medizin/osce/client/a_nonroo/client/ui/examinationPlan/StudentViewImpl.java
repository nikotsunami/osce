package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;

import java.util.Date;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class StudentViewImpl extends Composite implements StudentView,HasMouseDownHandlers{
	
	private static StudentViewImplUiBinder uiBinder = GWT
			.create(StudentViewImplUiBinder.class);

	interface StudentViewImplUiBinder extends UiBinder<Widget, StudentViewImpl> {
	}

	private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	private Long osceDayId;
	
	private OsceProxy osceProxy;
	
	public OsceProxy getOsceProxy() {
		return osceProxy;
	}

	public void setOsceProxy(OsceProxy osceProxy) {
		this.osceProxy = osceProxy;
	}

	public Long getOsceDayId() {
		return osceDayId;
	}

	public void setOsceDayId(Long osceDayId) {
		this.osceDayId = osceDayId;
	}

	private Long breakDuration;
	
	public Long getBreakDuration() {
		return breakDuration;
	}

	public void setBreakDuration(Long breakDuration) {
		this.breakDuration = breakDuration;
	}

	private AssignmentProxy previousAssignment;
	
	private AssignmentProxy preOfPrevAssignment;
	
	public AssignmentProxy getPreOfPrevAssignment() {
		return preOfPrevAssignment;
	}

	public void setPreOfPrevAssignment(AssignmentProxy preOfPrevAssignment) {
		this.preOfPrevAssignment = preOfPrevAssignment;
	}

	public AssignmentProxy getNextAssignmentProxy() {
		return nextAssignmentProxy;
	}

	public void setNextAssignmentProxy(AssignmentProxy nextAssignmentProxy) {
		this.nextAssignmentProxy = nextAssignmentProxy;
	}

	private AssignmentProxy nextAssignmentProxy;
	
	public AssignmentProxy getPreviousAssignment() {
		return previousAssignment;
	}

	public void setPreviousAssignment(AssignmentProxy previousAssignment) {
		this.previousAssignment = previousAssignment;
	}

	@UiField
	Label studentLbl;
	
	@UiField
	FocusPanel studentPanel;
	
	PopupView popupView;
	
	PopupView exchangePopupView;
	
	//Added for OMS-153.
	NamePrenamePopupView namePrenamePopupView;
	
	public FocusPanel getStudentPanel() {
		return studentPanel;
	}

	private AssignmentProxy assignmentProxy;
	
	public AssignmentProxy getAssignmentProxy() {
		return assignmentProxy;
	}

	public void setAssignmentProxy(AssignmentProxy assignmentProxy) {
		this.assignmentProxy = assignmentProxy;
	}

	public Label getStudentLbl() {
		return studentLbl;
	}

	public StudentViewImpl() {
		RootLayoutPanel.get().addDomHandler(new ContextMenuHandler() {

			@Override
			public void onContextMenu(ContextMenuEvent event) {
				event.preventDefault();
				event.stopPropagation();
			}
		}, ContextMenuEvent.getType());
	
		initWidget(uiBinder.createAndBindUi(this));
		
		studentPanel.addDomHandler(new ContextMenuHandler() {
			
			@Override
			public void onContextMenu(ContextMenuEvent event) {
				
				
				event.preventDefault();
				event.stopPropagation();			
				
				if(event.getNativeEvent().getButton()==NativeEvent.BUTTON_RIGHT)
				{
					if (assignmentProxy != null)
		        	{
		        		//event.preventDefault();
		        		showExchangeStudentPopup();
		        	}
		        	
		        	if (previousAssignment != null)
		        	{
		        		//event.preventDefault();
		        		//event.stopPropagation();
		        		showPopup();
		        	}
				}
			}
		}, ContextMenuEvent.getType());
		
		//by spec change
		studentPanel.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
			
				int button = event.getNativeEvent().getButton();
		        
				if (button == NativeEvent.BUTTON_LEFT) {
		        	
		        	Log.info("studentPanel Clicked");
		    		
		    		///event.getNativeEvent().get
		        	
		        	if (exchangePopupView != null && ((PopupViewImpl)exchangePopupView).isShowing())
					{
						((PopupViewImpl)exchangePopupView).hide();
					}
		        	
		    		if(previousAssignment !=null)
		    		{
		    			showBreakBurationPopupView();
		    		}
		    		if(assignmentProxy == null)
		    			return;
		    		
		    		/*if(assignmentProxy.getStudent()==null)
		    		{
		    			MessageConfirmationDialogBox dialgoBox=new MessageConfirmationDialogBox(constants.warning());
		    			dialgoBox.showConfirmationDialog(constants.studentNotAssigned());
		    		}
		    		else*/
		    		else
		    		{
		    			showStudentPopupView();
		    			
		    		}
		        }

		        /*if (button == NativeEvent.BUTTON_RIGHT) {
		        	
		        	if (assignmentProxy == null)
		        		return;
		        	event.preventDefault();
		        	
		        	if (assignmentProxy != null)
		        	{
		        		//event.preventDefault();
		        		showExchangeStudentPopup();
		        	}
		        	
		        	if (previousAssignment != null)
		        	{
		        		//event.preventDefault();
		        		//event.stopPropagation();
		        		showPopup();
		        	}
		         }*/
				
			}
		});
		//by spec change
	
		//Added for OMS-153.
		registerAndHandleMouseOverAndMouseOutEvent();
	}
	
	//Added for OMS-153.
		/**
		 * registering and handling mouse over and mouse out event on student panel.
		 */
		private void registerAndHandleMouseOverAndMouseOutEvent() {
			Log.info("registering mouse over event");
			studentPanel.addMouseOverHandler(new MouseOverHandler() {
				
				@Override
				public void onMouseOver(MouseOverEvent event) {
						Log.info("onMouseOver() called");
						showStudentPrenameNamePopup();
				}
			});
			studentPanel.addMouseOutHandler(new MouseOutHandler() {
				
				@Override
				public void onMouseOut(MouseOutEvent event) {
					Log.info("onMouseOut() called");
					if(namePrenamePopupView!=null){
						namePrenamePopupView.hidePopup();
					}
				}
			});
		}
		//Added for OMS-153.
		/**
		 * show student pre-name name popup
		 */
		private void showStudentPrenameNamePopup() {
		 Log.info("showing prename name popup");	
		 
		 if(assignmentProxy!=null){
				if(namePrenamePopupView == null)
				{
					namePrenamePopupView=new NamePrenamePopupViewImpl();
					
					RootPanel.get().add(((NamePrenamePopupViewImpl)namePrenamePopupView));
				}
				
				namePrenamePopupView.setPopupPosition(this.getAbsoluteLeft()-45, this.getAbsoluteTop()-86);
				//setDAta
				
				if(assignmentProxy.getStudent()!=null){
					namePrenamePopupView.setPreNameAndName(assignmentProxy.getStudent().getPreName()!=null ? assignmentProxy.getStudent().getPreName():constants.notAssigned(),
							assignmentProxy.getStudent().getName()!=null ? assignmentProxy.getStudent().getName():constants.notAssigned());
				}
				else{
					namePrenamePopupView.setPrenameValue(constants.notAssigned());
				}
				namePrenamePopupView.showPopup();
		 }
			
		}
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	/*@UiHandler("studentPanel")
	public void studentPanelClicked(ClickEvent event)
	{
		Log.info("studentPanel Clicked");
		
		///event.getNativeEvent().get
		if(previousAssignment !=null)
		{
			showBreakBurationPopupView();
		}
		if(assignmentProxy == null)
			return;
		
		if(assignmentProxy.getStudent()==null)
		{
			MessageConfirmationDialogBox dialgoBox=new MessageConfirmationDialogBox(constants.warning());
			dialgoBox.showConfirmationDialog(constants.studentNotAssigned());
		}
		else
		else
		{
			showStudentPopupView();
			
		}
	}*/
	PopupPanel panel;
	public void showPopup()
	{
		if (breakDuration == (osceProxy == null ? 0 : osceProxy.getLongBreak().intValue()))
		{
			panel = new PopupPanel();
			
			panel.setAnimationEnabled(true);
			panel.setAutoHideEnabled(true);
			
			VerticalPanel mainVP = new VerticalPanel();
			
			mainVP.setWidth("230px");
			mainVP.setSpacing(10);
			
			panel.add(mainVP);
			
			HorizontalPanel firstHP = new HorizontalPanel();
			firstHP.setSpacing(10);
			Label showLbl = new Label(constants.shiftBreak());
			showLbl.getElement().getStyle().setFontWeight(FontWeight.BOLD);
			firstHP.add(showLbl);
			mainVP.add(firstHP);
			
			HorizontalPanel secondHp = new HorizontalPanel();
			secondHp.setSpacing(10);
			Button shiftPrev = new Button();
			Button nextPrev = new Button();
			
			shiftPrev.setText(constants.prevRotation());
			nextPrev.setText(constants.nextRotation());
			
			secondHp.add(shiftPrev);
			secondHp.add(nextPrev);
			mainVP.add(secondHp);
			
			if (osceProxy != null)
			{	
				if (breakDuration == osceProxy.getLongBreak().longValue())
				{
					int totalRotation = 0;
					Date lunchBreakStartTime = new Date(0);
				
					for (OsceDayProxy proxy : osceProxy.getOsce_days())
					{
						if (proxy.getId().equals(previousAssignment.getOsceDay().getId()))
						{
							lunchBreakStartTime = proxy.getLunchBreakStart();
							
							for (OsceSequenceProxy osceSeqProxy : proxy.getOsceSequences())
							{
								totalRotation = totalRotation + osceSeqProxy.getNumberRotation();
							}
							
							if (proxy.getOsceSequences().size() == 2)
							{
								if (previousAssignment.getRotationNumber() == (proxy.getOsceSequences().get(0).getNumberRotation() - 2))
									nextPrev.setEnabled(false);
								
								if (previousAssignment.getRotationNumber() == proxy.getOsceSequences().get(0).getNumberRotation())
									shiftPrev.setEnabled(false);
							}
						}
					}
				
					if (previousAssignment.getRotationNumber() == 0 || lunchBreakStartTime.equals(preOfPrevAssignment == null ? "" : preOfPrevAssignment.getTimeEnd()))
						shiftPrev.setEnabled(false);
					
					if (previousAssignment.getRotationNumber() == (totalRotation-2) || lunchBreakStartTime.equals(nextAssignmentProxy == null ? "" : nextAssignmentProxy.getTimeEnd()))
						nextPrev.setEnabled(false);
					
					shiftPrev.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							panel.hide();
							
							if (previousAssignment != null)
								delegate.shiftLongBreakClicked(previousAssignment, 0, popupView);
						}
					});
					
					nextPrev.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							panel.hide();
							
							if (previousAssignment != null)
								delegate.shiftLongBreakClicked(previousAssignment, 1, popupView);
						}
					});
				}
			}	
			
			RootPanel.get().add(panel);
			
			panel.setPopupPosition(this.getAbsoluteLeft()-45, this.getAbsoluteTop()-100);
			
			panel.show();
		}
	}
	
	public void showBreakBurationPopupView()
	{
		if(popupView==null)
		{
			popupView=new PopupViewImpl();
			popupView.createEditBreakDurationPopupView();
			
			((PopupViewImpl)popupView).setAnimationEnabled(true);
			
			//checkForLunchBreak(previousAssignment);
			
			//if (previousAssignment != null && previousAssignment.getOsceDay() != null && previousAssignment.getTimeEnd().equals(previousAssignment.getOsceDay().getLunchBreakStart()))
			if (checkForLunchBreak(previousAssignment))
			{
				popupView.getWarningLbl().setVisible(false);				
			}
			else
			{
				popupView.getWarningLbl().setText(constants.longBreakWarningMsg());
				popupView.getWarningLbl().getElement().getStyle().setColor("#6E6E6E");
				popupView.getWarningLbl().getElement().getStyle().setFontWeight(FontWeight.NORMAL);
				popupView.getWarningLbl().getElement().getStyle().setFontStyle(FontStyle.ITALIC);
			}			
			
			/*if (osceProxy != null)
			{	
				
				if (breakDuration == osceProxy.getLongBreak().longValue())
				{
					int totalRotation = 0;
					Date lunchBreakStartTime = null;
				
					for (OsceDayProxy proxy : osceProxy.getOsce_days())
					{
						if (proxy.getId().equals(previousAssignment.getOsceDay().getId()))
							lunchBreakStartTime = proxy.getLunchBreakStart();
							
						for (OsceSequenceProxy osceSeqProxy : proxy.getOsceSequences())
						{
							totalRotation = totalRotation + osceSeqProxy.getNumberRotation();
						}
					}
					
					Button prevButton = new Button("Previous");
					Button nextButton = new Button("Next");
				
					popupView.getLongBreakHorizontalPanel().add(prevButton);
					popupView.getLongBreakHorizontalPanel().add(nextButton);
					
					if (previousAssignment.getRotationNumber() == 0 || lunchBreakStartTime.equals(preOfPrevAssignment.getTimeEnd()))
						prevButton.setEnabled(false);
					
					if (previousAssignment.getRotationNumber() == (totalRotation-2) || lunchBreakStartTime.equals(nextAssignmentProxy.getTimeEnd()))
						nextButton.setEnabled(false);
					
					prevButton.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							((PopupViewImpl)popupView).hide();
							
							if (preOfPrevAssignment != null)
								delegate.shiftLongBreakClicked(previousAssignment, preOfPrevAssignment.getTimeEnd(), new Date(), 0, popupView);
						}
					});
					
					nextButton.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							((PopupViewImpl)popupView).hide();
							
							if (nextAssignmentProxy != null)
								delegate.shiftLongBreakClicked(previousAssignment, new Date(), nextAssignmentProxy.getTimeEnd(), 1, popupView);
						}
					});
				}
			}	*/
			
			//((PopupViewImpl)popupView).setWidth("150px");

		
			RootPanel.get().add(((PopupViewImpl)popupView));
			
			
			
			
			popupView.getOkButton().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					//validate Entered Data
					if(previousAssignment!=null)
					{
						if(popupView.getBreakDuration().getValue()==null || popupView.getBreakDuration().getValue() <0)
						{
							MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
							dialogBox.showConfirmationDialog(constants.newBreakDurationNotNull());
						}
						else
						{
							delegate.shiftBreak(osceDayId,previousAssignment.getTimeEnd(),(popupView.getBreakDuration().getValue()-breakDuration.intValue()),popupView);
						}
					}
					
					
				}
			});
			
			popupView.getCancelButton().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					((PopupViewImpl)popupView).hide();
					
				}
			});
			
			
			((PopupViewImpl)popupView).getExaminerNameValue().setWidth("80px");
			
			
		}
		if (checkForLunchBreak(previousAssignment))
			((PopupViewImpl)popupView).setPopupPosition(this.getAbsoluteLeft()-45, this.getAbsoluteTop()-150);
		else
			((PopupViewImpl)popupView).setPopupPosition(this.getAbsoluteLeft()-45, this.getAbsoluteTop()-210);
		
		((PopupViewImpl)popupView).getExaminerNameValue().setText(breakDuration+"");
		((PopupViewImpl)popupView).show();
	}
	
	public boolean checkForLunchBreak(AssignmentProxy assignmentProxy)
	{
		boolean flag = false;
		if (assignmentProxy != null && assignmentProxy.getOsceDay() != null)
		{
			if (assignmentProxy.getTimeEnd().equals(assignmentProxy.getOsceDay().getLunchBreakStart()))
			{
				flag = true;
			}
			else
			{
				String breakByRotStr = assignmentProxy.getOsceDay().getBreakByRotation();
				String[] rotationStr = breakByRotStr.split("-");
				
				for (String str : rotationStr)
				{
					String[] rotString = str.split(":");
					if (osceProxy != null)
					{
						if (rotString[0].equals(assignmentProxy.getRotationNumber().toString()) && rotString[1].equals(osceProxy.getLunchBreak().toString()))
						{
							flag = true;
							break;	
						}
					}				
				}
			}
		}
		
		return flag;
	}
	
	public void showStudentPopupView()
	{
		if(popupView==null)
		{
			popupView=new PopupViewImpl();
			popupView.createSPPopupView();
			((PopupViewImpl)popupView).nameLbl.setText(constants.studentName());
			((PopupViewImpl)popupView).setAnimationEnabled(true);
			
			//((PopupViewImpl)popupView).setWidth("150px");
	
			RootPanel.get().add(((PopupViewImpl)popupView));
			
			popupView.getOkButton().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					//validate Entered Data
					((PopupViewImpl)popupView).hide();
					
					
				}
			});
			
		}
		
		((PopupViewImpl)popupView).setPopupPosition(this.getAbsoluteLeft()-45, this.getAbsoluteTop()-205);
		//setDAta
		if(assignmentProxy.getStudent()!=null)
			popupView.getNameValue().setText(assignmentProxy.getStudent().getPreName() +" "+assignmentProxy.getStudent().getName());
		else
			popupView.getNameValue().setText(constants.notAssigned());
		
		popupView.getStartTimeValue().setText(DateTimeFormat.getShortDateTimeFormat().format(assignmentProxy.getTimeStart()));
		Log.info("student end time " + assignmentProxy.getTimeEnd());
		popupView.getEndTimeValue().setText(DateTimeFormat.getShortDateTimeFormat().format(assignmentProxy.getTimeEnd()));
		((PopupViewImpl)popupView).show();
		
	}
	

	//by spec change[
	@Override
	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		// TODO Auto-generated method stub
		return addDomHandler(handler, MouseDownEvent.getType());
	}
	
	public void showExchangeStudentPopup()
	{
		if(exchangePopupView==null)
		{
			exchangePopupView=new PopupViewImpl();
			exchangePopupView.createExchangeStudentPopupView();
			
			((PopupViewImpl)exchangePopupView).setAnimationEnabled(true);
		
			//RootPanel.get().add(((PopupViewImpl)popupView));
			exchangePopupView.getOkButton().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					//validate Entered Data
					((PopupViewImpl)exchangePopupView).hide();
					if (exchangePopupView.getExchangeStudentListBox().getSelected() == null)
						((PopupViewImpl)exchangePopupView).hide();					
					else
						delegate.exchangeStudentClicked(assignmentProxy, exchangePopupView.getExchangeStudentListBox().getSelected());
				}
			});
		}
		
		((PopupViewImpl)exchangePopupView).setPopupPosition(this.getAbsoluteLeft()-45, this.getAbsoluteTop()-265);
		
		//setDAta		
		
		/*if (assignmentProxy.getStudent() == null)
		{
			exchangePopupView.getExchangeStudentListBox().setEnabled(false);
			
			exchangePopupView.getNameValue().setText(constants.notAssigned());
			
			exchangePopupView.getStartTimeValue().setText(DateTimeFormat.getShortDateTimeFormat().format(assignmentProxy.getTimeStart()));
			Log.info("student end time " + assignmentProxy.getTimeEnd());
			exchangePopupView.getEndTimeValue().setText(DateTimeFormat.getShortDateTimeFormat().format(assignmentProxy.getTimeEnd()));
			((PopupViewImpl)exchangePopupView).show();
		}
		else*/
		
		delegate.showExchangeStudentPopup(exchangePopupView, assignmentProxy);
		
	}
	//by spec change]
}
