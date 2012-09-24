package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;

import java.util.Date;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

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
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class ExaminationViewImpl extends Composite implements  ExaminationView{

	private static ExaminationViewImplUiBinder uiBinder = GWT
			.create(ExaminationViewImplUiBinder.class);

	interface ExaminationViewImplUiBinder extends UiBinder<Widget, ExaminationViewImpl> {
	}
	
	ExaminationViewImpl examinationViewImpl;
	
	private Delegate delegate;
	
	private OsceDayProxy osceDayProxy;
	
	private OsceSequenceProxy osceSequenceProxy;
	
	private CourseProxy courseProxy;
	
	private OscePostView oscePostView;
	
	private AssignmentProxy previousAssignmentProxy;
	
	public AssignmentProxy getPreviousAssignmentProxy() {
		return previousAssignmentProxy;
	}

	public void setPreviousAssignmentProxy(AssignmentProxy previousAssignmentProxy) {
		this.previousAssignmentProxy = previousAssignmentProxy;
	}

	public OscePostView getOscePostView() {
		return oscePostView;
	}

	public void setOscePostView(OscePostView oscePostView) {
		this.oscePostView = oscePostView;
	}

	public CourseProxy getCourseProxy() {
		return courseProxy;
	}

	public void setCourseProxy(CourseProxy courseProxy) {
		this.courseProxy = courseProxy;
	}

	public OsceSequenceProxy getOsceSequenceProxy() {
		return osceSequenceProxy;
	}
	
	public void setOsceSequenceProxy(OsceSequenceProxy osceSequenceProxy) {
		this.osceSequenceProxy = osceSequenceProxy;
	}

	private final OsceConstants constants=GWT.create(OsceConstants.class);	
	
	public OsceDayProxy getOsceDayProxy() {
		return osceDayProxy;
	}

	public void setOsceDayProxy(OsceDayProxy osceDayProxy) {
		this.osceDayProxy = osceDayProxy;
	}
	
	private OscePostProxy oscePostProxy;

	public OscePostProxy getOscePostProxy() {
		return oscePostProxy;
	}
	private Date timeStart;
	
	public Date getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(Date timeStart) {
		this.timeStart = timeStart;
	}

	public void setOscePostProxy(OscePostProxy oscePostProxy) {
		this.oscePostProxy = oscePostProxy;
	}

	@UiField
	Label examinerLbl;
	
	@UiField
	FocusPanel examinerPanel;
	
	PopupView popupView;
	
	PopupView examInfoPopupView;
	
	public PopupView getExamInfoPopupView() {
		return examInfoPopupView;
	}

	PopupPanel editPopup;
	
	public PopupPanel getEditPopup() {
		return editPopup;
	}

	public PopupView getPopupView() {
		return popupView;
	}
	
	private Date endTime;
	private Date lunchTime;
	

	private OscePostRoomProxy oscePostRoomProxy;
	


	public OscePostRoomProxy getOscePostRoomProxy() {
		return oscePostRoomProxy;
	}

	public void setOscePostRoomProxy(OscePostRoomProxy oscePostRoomProxy) {
		this.oscePostRoomProxy = oscePostRoomProxy;
	}

	

	public FocusPanel getExaminerPanel() {
		return examinerPanel;
	}

	private AssignmentProxy assignmentProxy;
	
	public AssignmentProxy getAssignmentProxy() {
		return assignmentProxy;
	}

	public void setAssignmentProxy(AssignmentProxy assignmentProxy) {
		this.assignmentProxy = assignmentProxy;
	}	

	public Label getExaminerLbl() {
		return examinerLbl;
	}
	
	public ExaminationViewImpl()
	{
		initWidget(uiBinder.createAndBindUi(this));
		examinationViewImpl=this;
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate=delegate;
	}
	
	@UiHandler("examinerPanel")
	public void examinerPanelClicked(ClickEvent event)
	{
		
		
		if(assignmentProxy==null || assignmentProxy.getExaminer() ==null)
		{
			delegate.retrieveAllExaminers(this);
			
		}
		else
		{
			showExaminerInfoPopupView();
		}
	}	
	
	public void showExaminerInfoPopupView()
	{
		if(examInfoPopupView == null)
		{
			examInfoPopupView=new PopupViewImpl();
			examInfoPopupView.createExaminerInfoPopupView();
			
			((PopupViewImpl)examInfoPopupView).setAnimationEnabled(true);
			
			//((PopupViewImpl)examInfoPopupView).setWidth("150px");

			
			RootPanel.get().add(((PopupViewImpl)examInfoPopupView));
			
			
			//((PopupViewImpl)examInfoPopupView).setPopupPosition(this.getAbsoluteLeft()-50, this.getAbsoluteTop()-140);
			
			examInfoPopupView.getOkButton().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					((PopupViewImpl)examInfoPopupView).hide();
				}
			});
			examInfoPopupView.getSaveBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					if(examInfoPopupView.getExaminerSuggestionBox().getValue()==null || examInfoPopupView.getExaminerSuggestionBox().getValue()=="")
					{
						MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
						dialogBox.showConfirmationDialog(constants.examinerNotNull());
					}
					else
						delegate.saveExaminer(examInfoPopupView.getExaminerSuggestionBox().getValue(),examinationViewImpl);
					
				}
			});
			examInfoPopupView.getEdit().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					delegate.retrieveAllExaminers(examInfoPopupView,examInfoPopupView.getExaminerSuggestionBox());
					
					examInfoPopupView.getExaminerNameValue().setVisible(false);
					
					examInfoPopupView.getExaminerSuggestionBox().setVisible(true);
					
					examInfoPopupView.getEdit().setVisible(false);
					
					examInfoPopupView.getSaveBtn().setText(constants.save());
					examInfoPopupView.getSaveBtn().setVisible(true);
					
					
					//create edit popupview
					/*if(editPopup==null)
					{
					 editPopup=new PopupPanel(true);
					editPopup.setAnimationEnabled(true);
					HorizontalPanel hp=new HorizontalPanel();
					hp.setSpacing(5);
					
					//autocomplete
					 final SuggestBox examinerSuggestionBox =  //new SuggestBox(keywordoracle);
							new SuggestBox(
									new ProxySuggestOracle<DoctorProxy>(
											new AbstractRenderer<DoctorProxy>() {
												@Override
												public String render(DoctorProxy object) {
													return object.getName() ;
												}
											}// ));
											, ",;:. \t?!_-/\\"));
					 delegate.retrieveAllExaminers(editPopup,examinerSuggestionBox);
					 
					 Button okBtn=new Button();
					 okBtn.setText(constants.okBtn());
					 
					 okBtn.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub
							delegate.saveExaminer(examinerSuggestionBox.getValue(),examinationViewImpl);
						}
					});
					 
					 hp.add(examinerSuggestionBox);
					 hp.add(okBtn);
					 editPopup.add(hp);
					 RootPanel.get().add(editPopup);
					
					 editPopup.setPopupPosition(((PopupViewImpl)examinationViewImpl.examInfoPopupView).getAbsoluteLeft(), ((PopupViewImpl)examinationViewImpl.examInfoPopupView).getAbsoluteLeft()-260);
					}
					else
						editPopup.show();
						*/
				}
			});
			//set data in information popup view
			examInfoPopupView.getExaminerNameValue().setText(assignmentProxy.getExaminer().getName());
		
			examInfoPopupView.getStartTimeValue().setText(DateTimeFormat.getShortDateTimeFormat().format(assignmentProxy.getTimeStart()));
			examInfoPopupView.getEndTimeValue().setText(DateTimeFormat.getShortDateTimeFormat().format(assignmentProxy.getTimeEnd()));
		}
		
		examInfoPopupView.createExaminerInfoPopupView();
		((PopupViewImpl)examInfoPopupView).setPopupPosition(this.getAbsoluteLeft()-45, this.getAbsoluteTop()-200);
		((PopupViewImpl)examInfoPopupView).show();
	}
	public void showExaminerPopupView()
	{
		if(popupView==null)
		{
			
			
			popupView=new PopupViewImpl();
			popupView.createExaminerAssignPopupView();
			
			((PopupViewImpl)popupView).setAnimationEnabled(true);
		
			
			
			
			((PopupViewImpl)popupView).setWidth("150px");

		
			RootPanel.get().add(((PopupViewImpl)popupView));
			
			
		//	((PopupViewImpl)popupView).setPopupPosition(this.getAbsoluteLeft()-50, this.getAbsoluteTop()-180);
			
			popupView.getOkButton().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					//validate Entered Data
					if(popupView.getExaminerSuggestionBox().getValue()=="" || popupView.getExaminerSuggestionBox().getValue()==null || popupView.getEndTimeListBox().getValue()==null || popupView.getEndTimeListBox().getValue().toString()=="")
					{
						MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox("Warning");
						dialogBox.showConfirmationDialog("Examiner name and End Time Cannot be null");
						return;
					}
					
					delegate.createExaminerAssignmnet(examinationViewImpl);
				}
			});
			
			popupView.getCancelButton().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					((PopupViewImpl)popupView).hide();
				}
			});
		}
		((PopupViewImpl)popupView).setPopupPosition(this.getAbsoluteLeft()-45, this.getAbsoluteTop()-180);
		((PopupViewImpl)popupView).show();
		
	}
}
	
