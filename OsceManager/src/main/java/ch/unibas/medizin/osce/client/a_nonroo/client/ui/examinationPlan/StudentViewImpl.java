package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class StudentViewImpl extends Composite implements StudentView,HasMouseDownHandlers{
	
	private static StudentViewImplUiBinder uiBinder = GWT
			.create(StudentViewImplUiBinder.class);

	interface StudentViewImplUiBinder extends UiBinder<Widget, StudentViewImpl> {
	}

	private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	private Long osceDayId;
	
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
		
		//by spec change
		studentPanel.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
			
				int button = event.getNativeEvent().getButton();
		        
				if (button == NativeEvent.BUTTON_LEFT) {
		        	
		        	Log.info("studentPanel Clicked");
		    		
		    		///event.getNativeEvent().get
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

		        if (button == NativeEvent.BUTTON_RIGHT) {
		        	event.preventDefault();
		        	showExchangeStudentPopup();
		         }
				
			}
		});
		//by spec change
	
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
	
	public void showBreakBurationPopupView()
	{
		if(popupView==null)
		{
			popupView=new PopupViewImpl();
			popupView.createEditBreakDurationPopupView();
			
			((PopupViewImpl)popupView).setAnimationEnabled(true);
		
			
			
			
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
		((PopupViewImpl)popupView).setPopupPosition(this.getAbsoluteLeft()-45, this.getAbsoluteTop()-135);
		((PopupViewImpl)popupView).getExaminerNameValue().setText(breakDuration+"");
		((PopupViewImpl)popupView).show();
	}
	public void showStudentPopupView()
	{
		if(popupView==null)
		{
			popupView=new PopupViewImpl();
			popupView.createSPPopupView();
			
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
			popupView.getNameValue().setText(assignmentProxy.getStudent().getName());
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
		
		if (assignmentProxy.getStudent() == null)
		{
			exchangePopupView.getExchangeStudentListBox().setEnabled(false);
			
			exchangePopupView.getNameValue().setText(constants.notAssigned());
			
			exchangePopupView.getStartTimeValue().setText(DateTimeFormat.getShortDateTimeFormat().format(assignmentProxy.getTimeStart()));
			Log.info("student end time " + assignmentProxy.getTimeEnd());
			exchangePopupView.getEndTimeValue().setText(DateTimeFormat.getShortDateTimeFormat().format(assignmentProxy.getTimeEnd()));
			((PopupViewImpl)exchangePopupView).show();
		}
		else
			delegate.showExchangeStudentPopup(exchangePopupView, assignmentProxy);
		
	}
	//by spec change]
}
