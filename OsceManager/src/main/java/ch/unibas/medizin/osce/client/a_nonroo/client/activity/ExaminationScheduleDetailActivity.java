package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.ExaminationScheduleDetailPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.AccordianPanelView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.AccordianPanelViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.ContentView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.ContentViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.HeaderView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.HeaderViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan.ExaminationScheduleDetailView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan.ExaminationScheduleDetailViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan.ExaminationView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan.ExaminationViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan.OscePostView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan.OscePostViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan.OsceSequenceView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan.OsceSequenceViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan.PopupView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan.PopupViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan.SPView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan.SPViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan.SequenceComparator;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan.StudentView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan.StudentViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.client.managed.request.AssignmentRequest;
import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.client.style.widgets.ProxySuggestOracle;
import ch.unibas.medizin.osce.shared.AssignmentTypes;
import ch.unibas.medizin.osce.shared.ColorPicker;
import ch.unibas.medizin.osce.shared.PostType;
import ch.unibas.medizin.osce.shared.util;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;

public class ExaminationScheduleDetailActivity extends AbstractActivity implements ExaminationScheduleDetailView.Presenter,ExaminationScheduleDetailView.Delegate,AccordianPanelView.Delegate
,StudentView.Delegate,SPView.Delegate,ExaminationView.Delegate,ContentView.Delegate{

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	ExaminationScheduleDetailPlace place;
	ExaminationScheduleDetailActivity activity;
	private final OsceConstants constants;
	ExaminationScheduleDetailView view;
	private OsceProxy osceProxy;
	
	private Map<OscePostProxy, List<Date>> postLunchStartTimeMap=new HashMap();
	private Map<OscePostProxy, List<Date>> postLongStartTimeMap=new HashMap();
	private Map<OscePostProxy, List<Date>> postEndTimeMap=new HashMap();
	
	private List<DoctorProxy> doctorList;
	public void setOsceProxy(OsceProxy osceProxy) {
		this.osceProxy = osceProxy;
	}

	public ExaminationScheduleDetailActivity(ExaminationScheduleDetailPlace place, OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
    	this.requests = requests;
    	this.placeController = placeController;
    	this.activity=this;
   
    	constants = GWT.create(OsceConstants.class);	 
    	
    	ApplicationLoadingScreenEvent.initialCounter();
		ApplicationLoadingScreenEvent.register(requests.getEventBus(),
				new ApplicationLoadingScreenHandler() {
					@Override
					public void onEventReceived(
							ApplicationLoadingScreenEvent event) {
						Log.info("ApplicationLoadingScreenEvent onEventReceived Called");
						event.display();
					}
				});
    }
	
	public void onStop(){

	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		requests.getEventBus().fireEvent(
				new ApplicationLoadingScreenEvent(true));
		Log.info("ExaminationScheduleDetailActivity.start()");
		this.widget = panel;
		
		init();
		requests.getEventBus().fireEvent(
				new ApplicationLoadingScreenEvent(false));
	}
	
	
	public void init()
	{
		final ExaminationScheduleDetailView examinationScheduleDetailView=new ExaminationScheduleDetailViewImpl();
		examinationScheduleDetailView.setPresenter(this);
		examinationScheduleDetailView.setDelegate(this);
		widget.setWidget(examinationScheduleDetailView.asWidget());
		this.view=examinationScheduleDetailView;
		
		MenuClickEvent.register(requests.getEventBus(), (ExaminationScheduleDetailViewImpl)view);
		
		requests.find(place.getProxyId()).with("osce_days","osce_days.osceSequences","osce_days.osceSequences.courses","osce_days.osceSequences.oscePosts","osce_days.osceSequences.oscePosts.oscePostBlueprint","osce_days.osceSequences.oscePosts.standardizedRole","osce_days.osceSequences.oscePosts.standardizedRole.roleTopic").fire(new OSCEReceiver<Object>() {

			@Override
			public void onSuccess(Object response) {
				requests.getEventBus().fireEvent(
						new ApplicationLoadingScreenEvent(true));
				// TODO Auto-generated method stub
				OsceProxy osceProxy=(OsceProxy)response;
				setOsceProxy(osceProxy);
				if(response instanceof Object)
				{
					
					Log.info("Osce Proxy Name :" + osceProxy.getName());
					examinationScheduleDetailView.setOsceProxy(osceProxy);
					
					/*examinationScheduleDetailView.getShortBreakValue().setText(osceProxy.getShortBreak().toString());
					examinationScheduleDetailView.getMiddleBreakValue().setText(osceProxy.getMiddleBreak().toString());
					examinationScheduleDetailView.getLunchTimeValue().setText(osceProxy.getLunchBreak().toString());
					examinationScheduleDetailView.getNumOfRoomsValue().setText(osceProxy.getNumberRooms().toString());
					examinationScheduleDetailView.getLongBreakValue().setText(osceProxy.getLongBreak().toString());
					examinationScheduleDetailView.getShortBreakSimPatChangeValue().setText(osceProxy.getShortBreakSimpatChange().toString());*/
					
					if(osceProxy.getShortBreak()==null)
						examinationScheduleDetailView.getShortBreakValue().setText("");
					else
						examinationScheduleDetailView.getShortBreakValue().setText(util.getEmptyIfNull(osceProxy.getShortBreak().toString()));
					if(osceProxy.getMiddleBreak()==null)
						examinationScheduleDetailView.getMiddleBreakValue().setText("");
					else
						examinationScheduleDetailView.getMiddleBreakValue().setText(util.getEmptyIfNull(osceProxy.getMiddleBreak().toString()));					
					if(osceProxy.getLunchBreak()==null)
						examinationScheduleDetailView.getLunchTimeValue().setText("");
					else
						examinationScheduleDetailView.getLunchTimeValue().setText(util.getEmptyIfNull(osceProxy.getLunchBreak().toString()));
					if(osceProxy.getNumberRooms()==null)
						examinationScheduleDetailView.getNumOfRoomsValue().setText("");
					else						
						examinationScheduleDetailView.getNumOfRoomsValue().setText(util.getEmptyIfNull(osceProxy.getNumberRooms().toString()));
					if(osceProxy.getLongBreak()==null)
						examinationScheduleDetailView.getLongBreakValue().setText("");
					else						
						examinationScheduleDetailView.getLongBreakValue().setText(util.getEmptyIfNull(osceProxy.getLongBreak().toString()));
					if(osceProxy.getShortBreakSimpatChange()==null)
						examinationScheduleDetailView.getShortBreakSimPatChangeValue().setText("");
					else
						examinationScheduleDetailView.getShortBreakSimPatChangeValue().setText(util.getEmptyIfNull(osceProxy.getShortBreakSimpatChange().toString()));
					
					Iterator<OsceDayProxy> osceDayProxyIterator=osceProxy.getOsce_days().iterator();
					while(osceDayProxyIterator.hasNext())
					{
						OsceDayProxy osceDayProxy=osceDayProxyIterator.next();
						
						//Iterator<OsceSequenceProxy> osceSequenceProxyIterator=osceDayProxy.getOsceSequences().iterator();
						
						//sort sequence
						List<OsceSequenceProxy> seqList=new ArrayList<OsceSequenceProxy>();
						seqList.addAll(osceDayProxy.getOsceSequences());
						Collections.sort(seqList,new SequenceComparator());
						
						Iterator<OsceSequenceProxy> osceSequenceProxyIterator=seqList.iterator();
						Log.info("oSCE Sequence Size :" + osceDayProxy.getOsceSequences().size());
						
						OsceSequenceView osceSequenceView=null;
						while(osceSequenceProxyIterator.hasNext())
						{
							OsceSequenceProxy oscesequenceProxy=osceSequenceProxyIterator.next();
							Log.info("oSCE Sequence " + oscesequenceProxy.getLabel());
							
							Iterator<CourseProxy> courseProxyIterator=oscesequenceProxy.getCourses().iterator();
							
							 osceSequenceView=new OsceSequenceViewImpl();
							 
							 AccordianPanelView accordianView=new AccordianPanelViewImpl(true);
							 accordianView.setDelegate(activity);
							 accordianView.setOsceDayProxy(osceDayProxy);
							 accordianView.setOsceSequenceProxy(oscesequenceProxy);
							 
							 Log.info("oSCE Course Size :" + oscesequenceProxy.getCourses().size());
							 
							while(courseProxyIterator.hasNext())
							{
								CourseProxy courseProxy=courseProxyIterator.next();
								Log.info("oSCE course " + courseProxy.getId());
								
								
								
								//accordianView.setCourseProxy(courseProxy);
								
								
								HeaderView headerView=new HeaderViewImpl();
								
								ColorPicker color=ColorPicker.valueOf(courseProxy.getColor());
								
								Log.info("Header Color :" +color);
								//headerView.getColorPicker().setValue(color);
								
								
								//headerView.getDeleteBtn().setVisible(false);
								headerView.getColorPicker().setVisible(false);
								headerView.getHeaderPanel().setHeight("360px");
								
								
								ContentView contentView=new ContentViewImpl();
								contentView.setDelegate(activity);
								contentView.setCourseProxy(courseProxy);
								contentView.getOscePostHP().setHeight("350px");
								contentView.getScrollPanel().setHeight("350px");
								
								//accordianView.setContentView(contentView);
								//OscePostView oscePostView=new OscePostViewImpl();
								
								//((ContentViewImpl)contentView).getOscePostHP().insert(oscePostView, ((ContentViewImpl)contentView).getOscePostHP().getWidgetCount());
								accordianView.add(headerView.asWidget(), contentView.asWidget());
								((HeaderViewImpl)headerView).changeHeaderColor(color);
								
							}
							osceSequenceView.getAccordianHP().insert(accordianView, osceSequenceView.getAccordianHP().getWidgetCount());
							examinationScheduleDetailView.getSequenceVP().insert(osceSequenceView, examinationScheduleDetailView.getSequenceVP().getWidgetCount());
						}
						
						
					}
					
				}
				requests.getEventBus().fireEvent(
						new ApplicationLoadingScreenEvent(false));
			}
			
		});
	}
	@Override
	public void goTo(Place place) {
		placeController.goTo(place);		
	}

	@Override
	public void retrieveContent(final AccordianPanelViewImpl accordianPanelViewImpl, Widget header,
			Widget sp) {
		
		requests.getEventBus().fireEvent(
				new ApplicationLoadingScreenEvent(true));
		Log.info("retrieveContent :");
		if(doctorList!=null)
		doctorList.clear();
		//Iterator<OscePostProxy> oscePostProxyIterator=accordianPanelViewImpl.getOsceSequenceProxy().getOscePosts().iterator();
		final List<OscePostProxy> oscePostProxies=accordianPanelViewImpl.getOsceSequenceProxy().getOscePosts();
		
		final ContentView contentView=(ContentView)sp;
		
		contentView.getOscePostHP().clear();
		
		
		
		for(int i=0;i<oscePostProxies.size();i++)
		{	
			final OscePostProxy oscePostProxy=oscePostProxies.get(i);
			Log.info("Osce Post ID :" + oscePostProxy.getId());
			
			
			
			
			final OscePostView oscePostView=new OscePostViewImpl();
			oscePostView.getOscePostLbl().setText("Post " +oscePostProxy.getSequenceNumber());
			oscePostView.setOscePostProxy(oscePostProxy);
			oscePostView.getOscePostPanel().addStyleName("oscePost-bg");
			if(i==0)
			{
				oscePostView.getOscePostPanel().addStyleName("oscePost-leftTop-radius");
			}
			else if(i==oscePostProxies.size()-1)
				oscePostView.getOscePostPanel().addStyleName("oscePost-RightTop-radius");
			
			//create Student Slots
			//if(oscePostProxy.getOscePostBlueprint().getPostType()!=PostType.BREAK)
			requests.assignmentRequestNonRoo().retrieveAssignmenstOfTypeStudent(accordianPanelViewImpl.getOsceDayProxy().getId(), accordianPanelViewImpl.getOsceSequenceProxy().getId(), contentView.getCourseProxy().getId(),oscePostProxy.getId())
			.with("student","oscePostRoom").fire(new OSCEReceiver<List<AssignmentProxy>>() {

				@Override
				public void onSuccess(List<AssignmentProxy> response) {
					requests.getEventBus().fireEvent(
							new ApplicationLoadingScreenEvent(true));
					Log.info("onSuccess retrieveContent : response size type Student :" + response.size());
					Log.info("onSuccess retrieveContent : response size :" + response.size());
					if(response.size()==0)
					{
						requests.getEventBus().fireEvent(
								new ApplicationLoadingScreenEvent(false));
						return;
					}
						final List<AssignmentProxy> studentAssignmentProxy=response;
					//create Empty examiner slot
					
					//calculate Examination duration
					final  Date examinerTimeEnd=response.get(response.size()-1).getTimeEnd();
					
					ArrayList<Date> endTimeList=new ArrayList<Date>();
					endTimeList.add(DateTimeFormat.getShortDateTimeFormat().parse(DateTimeFormat.getShortDateTimeFormat().format(examinerTimeEnd)));
					final Date examinerTimeStart=response.get(0).getTimeStart();
					
					
					
					//calculate duration
					

					List<Date> lunchstartTimeList=new ArrayList<Date>();
					List<Date> longstartTimeList=new ArrayList<Date>();
					
					for(int j=0;j<response.size();j++)
					{
						 AssignmentProxy assignmentProxy=response.get(j);
						 	
						 	//calculate duration
						 	Long studentSlotLength=assignmentProxy.getTimeEnd().getTime()-assignmentProxy.getTimeStart().getTime();
							studentSlotLength=studentSlotLength/60000;
							
							/*if(j==response.size()-1)
							{
								examinerTimeEnd.setDate(assignmentProxy.getTimeEnd().getDate());
								examinerTimeEnd.setHours(assignmentProxy.getTimeEnd().getHours());
								examinerTimeEnd.setMinutes(assignmentProxy.getTimeEnd().getMinutes());
								examinerTimeEnd.setSeconds(assignmentProxy.getTimeEnd().getSeconds());
							}*/
							if(assignmentProxy.getTimeStart().getHours() != accordianPanelViewImpl.getOsceDayProxy().getTimeStart().getHours() && (oscePostProxy.getOscePostBlueprint().getPostType()==PostType.ANAMNESIS_THERAPY || oscePostProxy.getOscePostBlueprint().getPostType()==PostType.PREPARATION) && j==0)
							{
								studentSlotLength=studentSlotLength/2;
							}
							//create student view
						 	StudentView studentView=new StudentViewImpl();
						 	studentView.getStudentPanel().addStyleName("border-bottom-yellow");
						 	studentView.getStudentPanel().addStyleName("student-bg");
						 	
							studentView.setAssignmentProxy(assignmentProxy);
							studentView.setDelegate(activity);
							
							NumberFormat.getFormat("000").format(assignmentProxy.getSequenceNumber());
							
							
							if(assignmentProxy.getSequenceNumber() !=null)
								studentView.getStudentLbl().setText("S"+NumberFormat.getFormat("000").format(assignmentProxy.getSequenceNumber()));
							
							
							if(j==response.size()-1 && oscePostProxy.getId()==1)
								studentView.getStudentPanel().addStyleName("leftBottom-radius");
							
							oscePostView.getStudentSlotsVP().insert(studentView, oscePostView.getStudentSlotsVP().getWidgetCount());
							
							//calculate break
						 	Long breakTime=0l;
						 	if(j!=response.size()-1)
						 		breakTime=response.get(j+1).getTimeStart().getTime()-assignmentProxy.getTimeEnd().getTime();
							breakTime=breakTime/60000;
							Log.info("Break :" + breakTime);
							
							
							//insert break if any
							if(j!=response.size()-1 && (long)osceProxy.getShortBreakSimpatChange()==breakTime)
							{
								
								studentSlotLength--;
								
								//insert simpat change break
								breakTime--;
								Short simpatchangeLength=osceProxy.getShortBreakSimpatChange();
								SPView simpatBreak=new SPViewImpl();
								simpatBreak.getSpPanel().addStyleName("empty-bg");
								simpatBreak.getSpPanel().addStyleName("border-bottom-blue");
								simpatBreak.setDelegate(activity);
								simpatBreak.getSpPanel().setHeight(breakTime.toString()+"px");
								simpatBreak.getSpPanel().setWidth("30px");
								
								oscePostView.getStudentSlotsVP().insert(simpatBreak, oscePostView.getStudentSlotsVP().getWidgetCount());
							}
							//insert long break
							else if(j!=response.size()-1 && (long)osceProxy.getLongBreak()==breakTime)
							{
								
								longstartTimeList.add(DateTimeFormat.getShortDateTimeFormat().parse(DateTimeFormat.getShortDateTimeFormat().format(assignmentProxy.getTimeEnd())));
								
								studentSlotLength--;
								
								//insert simpat change break
								breakTime--;
								Short simpatchangeLength=osceProxy.getLongBreak();
								SPView simpatBreak=new SPViewImpl();
								simpatBreak.getSpPanel().addStyleName("border-bottom-red");
								simpatBreak.getSpPanel().addStyleName("empty-bg");
								studentView.getStudentPanel().removeStyleName("border-bottom-yellow");
								studentView.getStudentPanel().addStyleName("border-bottom-red");
								
								simpatBreak.getSpPanel().setHeight(breakTime.toString()+"px");
								simpatBreak.getSpPanel().setWidth("30px");
								
								oscePostView.getStudentSlotsVP().insert(simpatBreak, oscePostView.getStudentSlotsVP().getWidgetCount());
							}
							//insert long break
							else if(j!=response.size()-1 && (long)osceProxy.getLunchBreak()==breakTime)
							{
								
								lunchstartTimeList.add(DateTimeFormat.getShortDateTimeFormat().parse(DateTimeFormat.getShortDateTimeFormat().format(assignmentProxy.getTimeEnd())));
								studentSlotLength--;
								
								//insert simpat change break
								breakTime--;
								Short simpatchangeLength=osceProxy.getLongBreak();
								SPView simpatBreak=new SPViewImpl();
								simpatBreak.getSpPanel().addStyleName("border-bottom-red");
								simpatBreak.getSpPanel().addStyleName("empty-bg");
								studentView.getStudentPanel().removeStyleName("border-bottom-yellow");
								studentView.getStudentPanel().addStyleName("border-bottom-red");
								
								simpatBreak.getSpPanel().setHeight(breakTime.toString()+"px");
								simpatBreak.getSpPanel().setWidth("30px");
								
								oscePostView.getStudentSlotsVP().insert(simpatBreak, oscePostView.getStudentSlotsVP().getWidgetCount());
							}
							else if(j!=response.size()-1 && (long)osceProxy.getMiddleBreak()==breakTime)
							{
								//endTimeList.add(assignmentProxy.getTimeEnd());
								studentSlotLength--;
								
								//insert simpat change break
								breakTime--;
								Short simpatchangeLength=osceProxy.getMiddleBreak();
								SPView simpatBreak=new SPViewImpl();
								
								simpatBreak.getSpPanel().addStyleName("empty-bg");
								simpatBreak.getSpPanel().addStyleName("border-bottom-red");
								studentView.getStudentPanel().removeStyleName("border-bottom-yellow");
								studentView.getStudentPanel().addStyleName("border-bottom-red");
								
								simpatBreak.setDelegate(activity);
								simpatBreak.getSpPanel().setHeight(breakTime.toString()+"px");
								simpatBreak.getSpPanel().setWidth("30px");
								
								oscePostView.getStudentSlotsVP().insert(simpatBreak, oscePostView.getStudentSlotsVP().getWidgetCount());
							}
							
							studentView.getStudentPanel().setHeight(studentSlotLength.toString()+"px");
						 
					}
					//startTimeList.add(response.get(response.size()-1).getTimeEnd());
					postLunchStartTimeMap.put(oscePostProxy, lunchstartTimeList);
					postLongStartTimeMap.put(oscePostProxy, longstartTimeList);
					
					postEndTimeMap.put(oscePostProxy, endTimeList);
					
					//create Examiner Slots
					if(oscePostProxy.getOscePostBlueprint().getPostType()!=PostType.BREAK)
					requests.assignmentRequestNonRoo().retrieveAssignmenstOfTypeExaminer(accordianPanelViewImpl.getOsceDayProxy().getId(), accordianPanelViewImpl.getOsceSequenceProxy().getId(), contentView.getCourseProxy().getId(),oscePostProxy.getId())
					.with("examiner").fire(new OSCEReceiver<List<AssignmentProxy>>() {

						@Override
						public void onSuccess(List<AssignmentProxy> response) {
							
							
							
							Log.info("onSuccess retrieveContent : response size :" + response.size());
							
							//create examination slot 
							if(response.size()==0)
							{
								//calculate duration
								
								Long examinerSlotLength=0l;
								//if(examinerTimeEnd.before(examinerTimeStart))
								
								if(oscePostProxy.getOscePostBlueprint().getPostType()==PostType.ANAMNESIS_THERAPY || oscePostProxy.getOscePostBlueprint().getPostType()==PostType.PREPARATION)
									examinerSlotLength=examinerTimeEnd.getTime()- accordianPanelViewImpl.getOsceDayProxy().getTimeStart().getTime();
								else
									 examinerSlotLength=examinerTimeEnd.getTime()-examinerTimeStart.getTime();
					
								examinerSlotLength=examinerSlotLength/60000;
								examinerSlotLength--;
								
								//create examiner view
							 	ExaminationView examinationView=new ExaminationViewImpl();
							 	examinationView.setOsceSequenceProxy(accordianPanelViewImpl.getOsceSequenceProxy());
							 	examinationView.setOscePostView(oscePostView);
							 	examinationView.setCourseProxy(contentView.getCourseProxy());
							 	
							 	
							 	
							 	examinationView.setOscePostRoomProxy(studentAssignmentProxy.get(0).getOscePostRoom());
							 	examinationView.setTimeStart(examinerTimeStart);
							 	examinationView.setOsceDayProxy(accordianPanelViewImpl.getOsceDayProxy());
							 	examinationView.getExaminerPanel().addStyleName("examiner-bg");
							 	examinationView.getExaminerPanel().addStyleName("border-bottom-red");
							 	examinationView.setOscePostProxy(oscePostProxy);
								examinationView.setDelegate(activity);
								if(examinerSlotLength>=0)
									examinationView.getExaminerPanel().setHeight(examinerSlotLength+"px");
								
								
								oscePostView.getExaminerVP().insert(examinationView, oscePostView.getExaminerVP().getWidgetCount());
								return ;
							}
						//	if(response.get(response.size()-1).getTimeEnd().equals(examinerTimeEnd))
							for(int j=0;j<response.size();j++)
							{
								 AssignmentProxy assignmentProxy=response.get(j);
								 
								 	//calculate duration
								 	Long examinerSlotLength=0l;
								 	 if(j==0)
									 {
								 		examinerSlotLength=assignmentProxy.getTimeEnd().getTime()- accordianPanelViewImpl.getOsceDayProxy().getTimeStart().getTime();
									 }
									 else							
										 examinerSlotLength=assignmentProxy.getTimeEnd().getTime()-assignmentProxy.getTimeStart().getTime();
								 	 
									examinerSlotLength=examinerSlotLength/60000;
									examinerSlotLength--;
									//examinerSlotLength=assignmentProxy.getTimeEnd().getMinutes()-assignmentProxy.getTimeStart().getMinutes();
									//examinerSlotLength++;
								 
									//create examiner view
								 	ExaminationView examinationView=new ExaminationViewImpl();
								 	examinationView.setCourseProxy(contentView.getCourseProxy());
								 	examinationView.setOsceSequenceProxy(accordianPanelViewImpl.getOsceSequenceProxy());
								 	examinationView.setOsceDayProxy(accordianPanelViewImpl.getOsceDayProxy());
								 	examinationView.setOscePostRoomProxy(studentAssignmentProxy.get(0).getOscePostRoom());
								 	examinationView.getExaminerPanel().addStyleName("examiner-bg");
								 	examinationView.getExaminerPanel().addStyleName("border-bottom-red");
									examinationView.setAssignmentProxy(assignmentProxy);
									examinationView.setDelegate(activity);
									examinationView.setOscePostProxy(oscePostProxy);
									examinationView.getExaminerPanel().setHeight(examinerSlotLength+"px");
									examinationView.setOscePostView(oscePostView);
									
									if(j==response.size()-1 && oscePostProxy.getId()==oscePostProxies.get(oscePostProxies.size()-1).getId())
										examinationView.getExaminerPanel().addStyleName("rightBottom-radius");
									
									if(j>0)
								 		examinationView.setPreviousAssignmentProxy(response.get(j-1));
									
									if(assignmentProxy.getSequenceNumber() != null)
									{
										
										examinationView.getExaminerLbl().setText("E"+NumberFormat.getFormat("00").format(assignmentProxy.getSequenceNumber()));
									}
									
									oscePostView.getExaminerVP().insert(examinationView, oscePostView.getExaminerVP().getWidgetCount());
									
									//calulate break
									Long breakTime=0l;
									if(j!=response.size()-1)
										breakTime=response.get(j+1).getTimeStart().getTime()-assignmentProxy.getTimeEnd().getTime();
									breakTime=breakTime/60000;
									
									Log.info("Break :" + breakTime);
									
									
									//insert long break
									if(j!=response.size()-1 && (long)osceProxy.getLongBreak()==breakTime)
									{
										//insert simpat change break
										breakTime--;
										
										Short simpatchangeLength=osceProxy.getLongBreak();
										SPView simpatBreak=new SPViewImpl();
										simpatBreak.getSpPanel().addStyleName("empty-bg");
										simpatBreak.getSpPanel().addStyleName("border-bottom-red");
										simpatBreak.setDelegate(activity);
										simpatBreak.getSpPanel().setHeight(breakTime.toString()+"px");
										simpatBreak.getSpPanel().setWidth("30px");
										
										oscePostView.getExaminerVP().insert(simpatBreak, oscePostView.getExaminerVP().getWidgetCount());
									}
									//insert lunchBreak
									else if(j!=response.size()-1 && (long)osceProxy.getLunchBreak()==breakTime)
									{
										//insert simpat change break
										breakTime--;
										
										Short simpatchangeLength=osceProxy.getLongBreak();
										SPView simpatBreak=new SPViewImpl();
										simpatBreak.getSpPanel().addStyleName("empty-bg");
										simpatBreak.getSpPanel().addStyleName("border-bottom-red");
										simpatBreak.setDelegate(activity);
										simpatBreak.getSpPanel().setHeight(breakTime.toString()+"px");
										simpatBreak.getSpPanel().setWidth("30px");
										
										oscePostView.getExaminerVP().insert(simpatBreak, oscePostView.getExaminerVP().getWidgetCount());
									}
									
							
									
									
								 
							}
							
							
							
						}
					});
					
					
					
					requests.getEventBus().fireEvent(
							new ApplicationLoadingScreenEvent(false));
				}
			});
			
			if(oscePostProxy.getOscePostBlueprint().getPostType()!=PostType.BREAK)
			requests.assignmentRequestNonRoo().retrieveAssignmenstOfTypeSP(accordianPanelViewImpl.getOsceDayProxy().getId(), accordianPanelViewImpl.getOsceSequenceProxy().getId(), contentView.getCourseProxy().getId(),oscePostProxy.getId())
			.with("patientInRole","patientInRole.patientInSemester","patientInRole.patientInSemester.standardizedPatient").fire(new OSCEReceiver<List<AssignmentProxy>>() {

				@Override
				public void onSuccess(List<AssignmentProxy> response) {
					
					requests.getEventBus().fireEvent(
							new ApplicationLoadingScreenEvent(true));
					
					Log.info("onSuccess retrieveContent : response size :" + response.size());
					
					
					for(int j=0;j<response.size();j++)
					{
						 AssignmentProxy assignmentProxy=response.get(j);
						 	
						 	//calculate duration
						
						 Long spSlotLength=0l;
						 if(j==0)
						 {
							 spSlotLength=assignmentProxy.getTimeEnd().getTime()- accordianPanelViewImpl.getOsceDayProxy().getTimeStart().getTime();
						 }
						 else							
						 	 spSlotLength=assignmentProxy.getTimeEnd().getTime()-assignmentProxy.getTimeStart().getTime();
						 
							spSlotLength=spSlotLength/60000;
							spSlotLength--;
							
							//create SP View
							SPView spView=null;
							
							if(oscePostProxy.getOscePostBlueprint().getPostType()==PostType.BREAK )
							{
								if(j!= 0 && assignmentProxy.getTimeStart().equals(response.get(j-1)))
								{
									spView=new SPViewImpl();
									spView.getSpPanel().addStyleName("border-bottom-blue");
									spView.getSpPanel().addStyleName("sp-bg");
									spView.setAssignmentProxy(assignmentProxy);
									spView.setDelegate(activity);
									spView.getSpPanel().setHeight(spSlotLength+"px");
									spView.getSpPanel().setWidth("45px");
									if(assignmentProxy.getSequenceNumber() != null)
									{
										spView.getSpLbl().setText("SP"+NumberFormat.getFormat("000").format(assignmentProxy.getSequenceNumber()));
										
									}
									
									
									oscePostView.getSpSlotsVP().insert(spView, oscePostView.getSpSlotsVP().getWidgetCount());
									
								}
								else
								{
									 spView=new SPViewImpl();
									spView.getSpPanel().addStyleName("border-bottom-blue");
									spView.getSpPanel().addStyleName("sp-bg");
									spView.setAssignmentProxy(assignmentProxy);
									spView.setDelegate(activity);
									spView.getSpPanel().setWidth("45px");
									spView.getSpPanel().setHeight(spSlotLength+"px");
									spView.getSpPanel().setWidth("45px");
									if(assignmentProxy.getSequenceNumber() != null)
									{
										spView.getSpLbl().setText("SP"+NumberFormat.getFormat("000").format(assignmentProxy.getSequenceNumber()));
										
									}
									
									oscePostView.getStudentSlotsVP().insert(spView, oscePostView.getStudentSlotsVP().getWidgetCount());
									
									
								}
							}
							else
							{
								 spView=new SPViewImpl();
								spView.getSpPanel().addStyleName("border-bottom-blue");
								spView.getSpPanel().addStyleName("sp-bg");
								spView.setAssignmentProxy(assignmentProxy);
								spView.setDelegate(activity);
								if(spSlotLength>=0)
								spView.getSpPanel().setHeight(spSlotLength+"px");
								if(assignmentProxy.getSequenceNumber() != null)
								{
									spView.getSpLbl().setText("SP"+NumberFormat.getFormat("000").format(assignmentProxy.getSequenceNumber()));
									
								}
								
								oscePostView.getSpSlotsVP().insert(spView, oscePostView.getSpSlotsVP().getWidgetCount());
							}
							//create Break
							
							Long breakTime=0l;
							if(j!=response.size()-1)
								breakTime=response.get(j+1).getTimeStart().getTime()-assignmentProxy.getTimeEnd().getTime();
							breakTime=breakTime/60000;
							Log.info("Break :" + breakTime);
							if(j!=response.size()-1 && (long)osceProxy.getShortBreakSimpatChange()==breakTime)
							{
								//insert simpat change break
								breakTime--;
								Short simpatchangeLength=osceProxy.getShortBreakSimpatChange();
								SPView simpatBreak=new SPViewImpl();
								simpatBreak.getSpPanel().addStyleName("border-bottom-blue");
								simpatBreak.getSpPanel().addStyleName("empty-bg");
								simpatBreak.setDelegate(activity);
								//breakTime--;
								simpatBreak.getSpPanel().setHeight(breakTime.toString()+"px");
								simpatBreak.getSpPanel().setWidth("30px");
								
								oscePostView.getSpSlotsVP().insert(simpatBreak, oscePostView.getSpSlotsVP().getWidgetCount());
							}
							//insert lunch break
							else if(j!=response.size()-1 && (long)osceProxy.getLongBreak()==breakTime)
							{
								//insert simpat change break
								breakTime--;
								Short simpatchangeLength=osceProxy.getLongBreak();
								SPView simpatBreak=new SPViewImpl();
								simpatBreak.getSpPanel().addStyleName("empty-bg");
								simpatBreak.getSpPanel().addStyleName("border-bottom-red");
								spView.getSpPanel().removeStyleName("border-bottom-blue");
								spView.getSpPanel().addStyleName("border-bottom-red");
								simpatBreak.setDelegate(activity);
								simpatBreak.getSpPanel().setHeight(breakTime.toString()+"px");
								simpatBreak.getSpPanel().setWidth("30px");
								
								oscePostView.getSpSlotsVP().insert(simpatBreak, oscePostView.getSpSlotsVP().getWidgetCount());
							}
							else if(j!=response.size()-1 && (long)osceProxy.getLunchBreak()==breakTime)
							{
								//studentSlotLength--;
								
								//insert simpat change break
								breakTime--;
								Short simpatchangeLength=osceProxy.getLongBreak();
								SPView simpatBreak=new SPViewImpl();
								simpatBreak.getSpPanel().addStyleName("empty-bg");
								simpatBreak.getSpPanel().addStyleName("border-bottom-red");
								spView.getSpPanel().removeStyleName("border-bottom-blue");
								spView.getSpPanel().addStyleName("border-bottom-red");
								simpatBreak.setDelegate(activity);
								simpatBreak.getSpPanel().setHeight(breakTime.toString()+"px");
								simpatBreak.getSpPanel().setWidth("30px");
								
								oscePostView.getSpSlotsVP().insert(simpatBreak, oscePostView.getSpSlotsVP().getWidgetCount());
							}
							else if(j!=response.size()-1 && (long)osceProxy.getMiddleBreak()==breakTime)
							{
								//insert simpat change break
								breakTime--;
								Short simpatchangeLength=osceProxy.getMiddleBreak();
								SPView simpatBreak=new SPViewImpl();
								
								simpatBreak.getSpPanel().addStyleName("empty-bg");
								simpatBreak.getSpPanel().addStyleName("border-bottom-red");
								spView.getSpPanel().removeStyleName("border-bottom-blue");
								spView.getSpPanel().addStyleName("border-bottom-red");
								
								simpatBreak.setDelegate(activity);
								simpatBreak.getSpPanel().setHeight(breakTime.toString()+"px");
								simpatBreak.getSpPanel().setWidth("30px");
								
								oscePostView.getSpSlotsVP().insert(simpatBreak, oscePostView.getSpSlotsVP().getWidgetCount());
							}
							
							
						 
					}
				
					requests.getEventBus().fireEvent(
							new ApplicationLoadingScreenEvent(false));
				}
				
			});
			
			
			
			contentView.getOscePostHP().insert(oscePostView, contentView.getOscePostHP().getWidgetCount());
			
			
			
			/*
			requests.assignmentRequestNonRoo().retrieveAssignments(accordianPanelViewImpl.getOsceDayProxy().getId(), accordianPanelViewImpl.getOsceSequenceProxy().getId(),accordianPanelViewImpl.getCourseProxy().getId(),oscePostProxy.getId())
			.with("student","examiner","patientInRole").fire(new OSCEReceiver<List<AssignmentProxy>>() {

				@Override
				public void onSuccess(List<AssignmentProxy> response) {
					
				Long studentSlotLength=0l;
				Long spSlotLength=0l;
				Long examinerSlotLength=0l;
				
				
				Log.info("onSuccess retrieveContent : response size :" + response.size());
				ContentView contentView=accordianPanelViewImpl.getContentView();
				
				OscePostView oscePostView=new OscePostViewImpl();
				oscePostView.getOscePostLbl().setText("Post" +oscePostProxy.getSequenceNumber());
				oscePostView.setOscePostProxy(oscePostProxy);
				
				
				
				for(int j=0;j<response.size();j++)
				{
					//if student
					 AssignmentProxy assignmentProxy=response.get(j);
					
					if(assignmentProxy.getType()==AssignmentTypes.STUDENT)
					{
						if(studentSlotLength == 0)
						{
							studentSlotLength=assignmentProxy.getTimeEnd().getTime()-assignmentProxy.getTimeStart().getTime();
									studentSlotLength=studentSlotLength/60000;
							//studentSlotLength=assignmentProxy.getTimeEnd().getMinutes()-assignmentProxy.getTimeStart().getMinutes();
							//studentSlotLength++;
						}
						
						//insert simpat change break
						if(j!=response.size()-1 && osceProxy.getShortBreakSimpatChange()==assignmentProxy.getTimeEnd().getMinutes()-response.get(j+1).getTimeStart().getMinutes())
						{
							//insert simpat change break
							
							Short simpatchangeLength=osceProxy.getShortBreakSimpatChange();
							SPView simpatBreak=new SPViewImpl();
							simpatBreak.setDelegate(activity);
							simpatBreak.getSpPanel().setHeight(simpatchangeLength.toString()+"px");
							simpatBreak.getSpPanel().setWidth("30px");
							
							oscePostView.getStudentSlotsVP().insert(simpatBreak, oscePostView.getStudentSlotsVP().getWidgetCount());
						}
						
						//insert lunch break
						if(j!=response.size()-1 && osceProxy.getLongBreak()==assignmentProxy.getTimeEnd().getMinutes()-response.get(j+1).getTimeStart().getMinutes())
						{
							//insert simpat change break
							
							Short simpatchangeLength=osceProxy.getLongBreak();
							SPView simpatBreak=new SPViewImpl();
							simpatBreak.setDelegate(activity);
							simpatBreak.getSpPanel().setHeight(simpatchangeLength.toString()+"px");
							simpatBreak.getSpPanel().setWidth("30px");
							
							oscePostView.getStudentSlotsVP().insert(simpatBreak, oscePostView.getStudentSlotsVP().getWidgetCount());
						}
						if(j!=response.size()-1 && osceProxy.getMiddleBreak()==assignmentProxy.getTimeEnd().getMinutes()-response.get(j+1).getTimeStart().getMinutes())
						{
							//insert simpat change break
							
							Short simpatchangeLength=osceProxy.getMiddleBreak();
							SPView simpatBreak=new SPViewImpl();
							simpatBreak.setDelegate(activity);
							simpatBreak.getSpPanel().setHeight(simpatchangeLength.toString()+"px");
							simpatBreak.getSpPanel().setWidth("30px");
							
							oscePostView.getStudentSlotsVP().insert(simpatBreak, oscePostView.getStudentSlotsVP().getWidgetCount());
						}
						
						StudentView studentView=new StudentViewImpl();
						studentView.setAssignmentProxy(assignmentProxy);
						studentView.setDelegate(activity);
						studentView.getStudentPanel().setHeight(studentSlotLength.toString()+"px");
						if(assignmentProxy.getSequenceNumber() !=null)
							studentView.getStudentLbl().setText(assignmentProxy.getSequenceNumber().toString());
						
						oscePostView.getStudentSlotsVP().insert(studentView, oscePostView.getStudentSlotsVP().getWidgetCount());
						
						
					}
					else if(assignmentProxy.getType() == AssignmentTypes.PATIENT)
					{
						
						//insert simpatchange view
						//if(!isFirst)
						//{
						//Short simpatchangeLength=osceProxy.getShortBreakSimpatChange();
						//SPView simpatBreak=new SPViewImpl();
						//simpatBreak.setDelegate(activity);
						//simpatBreak.getSpPanel().setHeight(simpatchangeLength.toString()+"px");
						//simpatBreak.getSpPanel().setWidth("30px");
						
						//oscePostView.getSpSlotsVP().insert(simpatBreak, oscePostView.getSpSlotsVP().getWidgetCount());
						//}
						//insert simpat change break
						
						
						//isFirst=false;
						
						//if(spSlotLength ==0)
						//{
							spSlotLength=assignmentProxy.getTimeEnd().getTime()-assignmentProxy.getTimeStart().getTime();
							spSlotLength=spSlotLength/60000;
							//spSlotLength=assignmentProxy.getTimeEnd().getMinutes()-assignmentProxy.getTimeStart().getMinutes();
							//spSlotLength++;
						//}
						
						
						SPView spView=new SPViewImpl();
						spView.setAssignmentProxy(assignmentProxy);
						spView.setDelegate(activity);
						spView.getSpPanel().setHeight(spSlotLength+"px");
						if(assignmentProxy.getSequenceNumber() != null)
						{
							spView.getSpLbl().setText(assignmentProxy.getSequenceNumber().toString());
							
						}
						
						oscePostView.getSpSlotsVP().insert(spView, oscePostView.getSpSlotsVP().getWidgetCount());
						
						
						Long breakTime=0l;
						breakTime=response.get(j+1).getTimeStart().getTime()-assignmentProxy.getTimeEnd().getTime();
						breakTime=breakTime/60000;
						Log.info("Break :" + breakTime);
						if(j!=response.size()-1 && (long)osceProxy.getShortBreakSimpatChange()==breakTime)
						{
							//insert simpat change break
							
							Short simpatchangeLength=osceProxy.getShortBreakSimpatChange();
							SPView simpatBreak=new SPViewImpl();
							simpatBreak.setDelegate(activity);
							simpatBreak.getSpPanel().setHeight(breakTime.toString()+"px");
							simpatBreak.getSpPanel().setWidth("30px");
							
							oscePostView.getSpSlotsVP().insert(simpatBreak, oscePostView.getSpSlotsVP().getWidgetCount());
						}
						//insert lunch break
						if(j!=response.size()-1 && (long)osceProxy.getLongBreak()==breakTime)
						{
							//insert simpat change break
							
							Short simpatchangeLength=osceProxy.getLongBreak();
							SPView simpatBreak=new SPViewImpl();
							simpatBreak.setDelegate(activity);
							simpatBreak.getSpPanel().setHeight(breakTime.toString()+"px");
							simpatBreak.getSpPanel().setWidth("30px");
							
							oscePostView.getSpSlotsVP().insert(simpatBreak, oscePostView.getSpSlotsVP().getWidgetCount());
						}
						if(j!=response.size()-1 && (long)osceProxy.getMiddleBreak()==breakTime)
						{
							//insert simpat change break
							
							Short simpatchangeLength=osceProxy.getMiddleBreak();
							SPView simpatBreak=new SPViewImpl();
							simpatBreak.setDelegate(activity);
							simpatBreak.getSpPanel().setHeight(breakTime.toString()+"px");
							simpatBreak.getSpPanel().setWidth("30px");
							
							oscePostView.getSpSlotsVP().insert(simpatBreak, oscePostView.getSpSlotsVP().getWidgetCount());
						}
					}
					else if(assignmentProxy.getType() == AssignmentTypes.EXAMINER)
					{
						
						//if(examinerSlotLength==0)
						//{
							examinerSlotLength=assignmentProxy.getTimeEnd().getTime()-assignmentProxy.getTimeStart().getTime();
							examinerSlotLength=examinerSlotLength/60000;
							//examinerSlotLength=assignmentProxy.getTimeEnd().getMinutes()-assignmentProxy.getTimeStart().getMinutes();
							//examinerSlotLength++;
						//}
						
						ExaminationView examinationView=new ExaminationViewImpl();
						examinationView.setAssignmentProxy(assignmentProxy);
						examinationView.setDelegate(activity);
						examinationView.getExaminerPanel().setHeight(examinerSlotLength+"px");
						if(assignmentProxy.getSequenceNumber() != null)
						{
							
							examinationView.getExaminerLbl().setText(assignmentProxy.getSequenceNumber().toString());
						}
						
						oscePostView.getExaminerVP().insert(examinationView, oscePostView.getExaminerVP().getWidgetCount());
					}
					
					
				}
				
				
				contentView.getOscePostHP().insert(oscePostView, contentView.getOscePostHP().getWidgetCount());
					
				}
			});*/
			
		/*	if(i==oscePostProxies.size()-1)
			{
				
				accordianPanelViewImpl.expand((HeaderViewImpl)header, sp);
			}
		*/
		}
		requests.getEventBus().fireEvent(
				new ApplicationLoadingScreenEvent(false));
		
	}
	public void saveExaminer(final String value,final ExaminationViewImpl view){
		AssignmentRequest assignmentRequest=requests.assignmentRequest();
		AssignmentProxy assignmentProxy=view.getAssignmentProxy();
		assignmentProxy=assignmentRequest.edit(view.getAssignmentProxy());
		
		DoctorProxy doctorProxy=null;
		for(DoctorProxy dp:doctorList)
		{
			if(dp.getName().equals(value))
			{
				doctorProxy=dp;
				break;
			}
		}
		assignmentProxy.setExaminer(doctorProxy);
		assignmentRequest.persist().using(assignmentProxy).fire(new OSCEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				Log.info("saveExaminer Success");
				
				view.getExamInfoPopupView().getExaminerNameValue().setVisible(true);
				
				view.getExamInfoPopupView().getExaminerSuggestionBox().setVisible(false);
				
				view.getExamInfoPopupView().getEdit().setVisible(true);
				
				
				view.getExamInfoPopupView().getSaveBtn().setVisible(false);
				view.getExamInfoPopupView().getExaminerNameValue().setText(value);
				//view.getEditPopup().hide();
			}
		});
	}
	
	public void createExaminerAssignmnet(final ExaminationViewImpl view)
	{
		
		AssignmentRequest assignmentRequest=requests.assignmentRequest();
		AssignmentProxy assignmentProxy=null;
		 int sequenceNumber=0;
		if(view.getAssignmentProxy()==null)
		{
			assignmentProxy=assignmentRequest.create(AssignmentProxy.class);
			assignmentProxy.setTimeStart(view.getTimeStart());
			sequenceNumber=01;
		}
		else
		{	
			sequenceNumber=view.getPreviousAssignmentProxy().getSequenceNumber()+1;
			view.getExaminerLbl().setText("E"+sequenceNumber);
			assignmentProxy=assignmentRequest.edit(view.getAssignmentProxy());
		}
		
		DoctorProxy doctorProxy=null;
		for(DoctorProxy dp:doctorList)
		{
			if(dp.getName().equals(view.getPopupView().getExaminerSuggestionBox().getValue()))
			{
				doctorProxy=dp;
				break;
			}
		}
		assignmentProxy.setExaminer(doctorProxy);
		
		//assignmentProxy.setTimeStart(DateTimeFormat.getShortDateTimeFormat().parse(view.getPopupView().getStartTimeValue().getText()));
		
		
		Date endDateTime=view.getPopupView().getEndTimeListBox().getValue();
		assignmentProxy.setTimeEnd(endDateTime);
		assignmentProxy.setSequenceNumber(sequenceNumber);
		assignmentProxy.setType(AssignmentTypes.EXAMINER);
		assignmentProxy.setOsceDay(view.getOsceDayProxy());
		assignmentProxy.setOscePostRoom(view.getOscePostRoomProxy());
		
		assignmentRequest.persist().using(assignmentProxy).fire(new OSCEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				Log.info("createExaminerAssignmnet Success");
				boolean isLongbreak=false;
				boolean isLunctBreak=false;
				boolean isEndTime=false;
				List<Date> dateList=postEndTimeMap.get(view.getOscePostProxy());
				for(Date d:dateList)
				{
					if(d.equals(view.getPopupView().getEndTimeListBox().getValue()))
					{
						isEndTime=true;
					}
				}
				
				 dateList= postLongStartTimeMap.get(view.getOscePostProxy());
				for(Date d:dateList)
				{
					if(d.equals(view.getPopupView().getEndTimeListBox().getValue()))
					{
						isLongbreak=true;
						break;
					}
				}
				dateList=postLunchStartTimeMap.get(view.getOscePostProxy());
				for(Date d:dateList)
				{
					if(d.equals(view.getPopupView().getEndTimeListBox().getValue()))
					{
						isLunctBreak=true;
						break;
					}
				}
				
				if(isLunctBreak)
				{
					AssignmentRequest assignmentRequest1=requests.assignmentRequest();
					AssignmentProxy assignmentProxy1=assignmentRequest1.create(AssignmentProxy.class);
					
					Short lunchBreak=osceProxy.getLunchBreak();
					Log.info("Lunch Break :" + lunchBreak);
					String endDateString=DateTimeFormat.getFullDateTimeFormat().format(view.getPopupView().getEndTimeListBox().getValue());
					Date endDate=DateTimeFormat.getFullDateTimeFormat().parse(endDateString);
					int mt=endDate.getMinutes();
					int hr=endDate.getHours();
					mt=mt+lunchBreak;
					if(mt>59)
					{
						hr++;
						mt=mt-60;
					}
					endDate.setMinutes(mt);
					endDate.setHours(hr);
					
					assignmentProxy1.setTimeStart(endDate);
					assignmentProxy1.setTimeEnd(postEndTimeMap.get(view.getOscePostProxy()).get(0));
					assignmentProxy1.setType(AssignmentTypes.EXAMINER);
					//assignmentProxy1.setSequenceNumber(view.getPreviousAssignmentProxy().getSequenceNumber()+2);
					assignmentProxy1.setOsceDay(view.getOsceDayProxy());
					assignmentProxy1.setOscePostRoom(view.getOscePostRoomProxy());
					assignmentRequest1.persist().using(assignmentProxy1).fire(new OSCEReceiver<Void>() {

						@Override
						public void onSuccess(Void response) {
							Log.info("createExaminerAssignmnet Success Splited Row ");
							((PopupViewImpl)view.getPopupView()).hide();
							//refresh ExaminerView;
							refreshExaminerView(view);
						}
					});
				}
				else if(isLongbreak)
				{
					AssignmentRequest assignmentRequest1=requests.assignmentRequest();
					AssignmentProxy assignmentProxy1=assignmentRequest1.create(AssignmentProxy.class);
					
					Short longBreak=osceProxy.getLongBreak();
					Log.info("Long Break :" + longBreak);
					
					
					String endDateString=DateTimeFormat.getFullDateTimeFormat().format(view.getPopupView().getEndTimeListBox().getValue());
					Date endDate=DateTimeFormat.getFullDateTimeFormat().parse(endDateString);
					int mt=endDate.getMinutes();
					int hr=endDate.getHours();
					mt=mt+longBreak;
					if(mt>59)
					{
						hr++;
						mt=mt-60;
					}
					endDate.setMinutes(mt);
					endDate.setHours(hr);
					
					assignmentProxy1.setTimeStart(endDate);
					assignmentProxy1.setTimeEnd(postEndTimeMap.get(view.getOscePostProxy()).get(0));
					assignmentProxy1.setType(AssignmentTypes.EXAMINER);
					assignmentProxy1.setOsceDay(view.getOsceDayProxy());
					/*if(view.getAssignmentProxy()==null)
						assignmentProxy1.setSequenceNumber(2);
					else
						assignmentProxy1.setSequenceNumber(view.getPreviousAssignmentProxy().getSequenceNumber()+2);*/
					assignmentProxy1.setOscePostRoom(view.getOscePostRoomProxy());
					assignmentRequest1.persist().using(assignmentProxy1).fire(new OSCEReceiver<Void>() {

						@Override
						public void onSuccess(Void response) {
							Log.info("createExaminerAssignmnet Success Splited Row ");
							((PopupViewImpl)view.getPopupView()).hide();
							refreshExaminerView(view);
						}
					});
				}
				((PopupViewImpl)view.getPopupView()).hide();
			}
		});
		
	}
	
	public void refreshExaminerView(final ExaminationViewImpl examinationView)
	{
		final OscePostProxy oscePostProxy=examinationView.getOscePostProxy();
		final OsceDayProxy osceDayProxy=examinationView.getOsceDayProxy();
		final OscePostView oscePostView=examinationView.getOscePostView();
		oscePostView.getExaminerVP().clear();
		requests.assignmentRequestNonRoo().retrieveAssignmenstOfTypeExaminer(osceDayProxy.getId(), examinationView.getOsceSequenceProxy().getId(), examinationView.getCourseProxy().getId(),oscePostProxy.getId())
		.with("examiner").fire(new OSCEReceiver<List<AssignmentProxy>>() {

			@Override
			public void onSuccess(List<AssignmentProxy> response) {
				
				
				
				Log.info("onSuccess retrieveContent : response size :" + response.size());
				
				//create examination slot 

			//	if(response.get(response.size()-1).getTimeEnd().equals(examinerTimeEnd))
				for(int j=0;j<response.size();j++)
				{
					 AssignmentProxy assignmentProxy=response.get(j);
					 
					 	//calculate duration
					 	Long examinerSlotLength=0l;
					 	 if(j==0)
						 {
					 		examinerSlotLength=assignmentProxy.getTimeEnd().getTime()- osceDayProxy.getTimeStart().getTime();
						 }
						 else							
							 examinerSlotLength=assignmentProxy.getTimeEnd().getTime()-assignmentProxy.getTimeStart().getTime();
					 	 
						examinerSlotLength=examinerSlotLength/60000;
						examinerSlotLength--;
						//examinerSlotLength=assignmentProxy.getTimeEnd().getMinutes()-assignmentProxy.getTimeStart().getMinutes();
						//examinerSlotLength++;
					 
						//create examiner view
					 	ExaminationView examinationView=new ExaminationViewImpl();
					 	examinationView.setOsceDayProxy(osceDayProxy);
					 	//examinationView.setOscePostRoomProxy(studentAssignmentProxy.get(0).getOscePostRoom());
					 	examinationView.getExaminerPanel().addStyleName("examiner-bg");
					 	examinationView.getExaminerPanel().addStyleName("border-bottom-red");
						examinationView.setAssignmentProxy(assignmentProxy);
						examinationView.setDelegate(activity);
						examinationView.setOscePostProxy(oscePostProxy);
						examinationView.getExaminerPanel().setHeight(examinerSlotLength+"px");
						
						if(j>0)
					 		examinationView.setPreviousAssignmentProxy(response.get(j-1));
						if(assignmentProxy.getSequenceNumber() != null)
						{
							
							examinationView.getExaminerLbl().setText("E"+assignmentProxy.getSequenceNumber().toString());
						}
						
						oscePostView.getExaminerVP().insert(examinationView, oscePostView.getExaminerVP().getWidgetCount());
						
						//calulate break
						Long breakTime=0l;
						if(j!=response.size()-1)
							breakTime=response.get(j+1).getTimeStart().getTime()-assignmentProxy.getTimeEnd().getTime();
						breakTime=breakTime/60000;
						
						Log.info("Break :" + breakTime);
						
						
						//insert long break
						if(j!=response.size()-1 && (long)osceProxy.getLongBreak()==breakTime)
						{
							//insert simpat change break
							breakTime--;
							
							Short simpatchangeLength=osceProxy.getLongBreak();
							SPView simpatBreak=new SPViewImpl();
							simpatBreak.getSpPanel().addStyleName("empty-bg");
							simpatBreak.getSpPanel().addStyleName("border-bottom-red");
							simpatBreak.setDelegate(activity);
							simpatBreak.getSpPanel().setHeight(breakTime.toString()+"px");
							simpatBreak.getSpPanel().setWidth("30px");
							
							oscePostView.getExaminerVP().insert(simpatBreak, oscePostView.getExaminerVP().getWidgetCount());
						}
						//insert lunchBreak
						else if(j!=response.size()-1 && (long)osceProxy.getLunchBreak()==breakTime)
						{
							//insert simpat change break
							breakTime--;
							
							Short simpatchangeLength=osceProxy.getLongBreak();
							SPView simpatBreak=new SPViewImpl();
							simpatBreak.getSpPanel().addStyleName("empty-bg");
							simpatBreak.getSpPanel().addStyleName("border-bottom-red");
							simpatBreak.setDelegate(activity);
							simpatBreak.getSpPanel().setHeight(breakTime.toString()+"px");
							simpatBreak.getSpPanel().setWidth("30px");
							
							oscePostView.getExaminerVP().insert(simpatBreak, oscePostView.getExaminerVP().getWidgetCount());
						}
						
				
						
						
					 
				}
				
				
				
			}
		});
	}
	public void retrieveAllExaminers(final PopupView popup,final SuggestBox sb)
	{
		if(doctorList==null)
		requests.doctorRequest().findAllDoctors().fire(new OSCEReceiver<List<DoctorProxy>>() {

			@Override
			public void onSuccess(List<DoctorProxy> response) {
				doctorList=response;
				((ProxySuggestOracle<DoctorProxy>)sb.getSuggestOracle()).addAll(response);
				//((PopupViewImpl)popup).show();
				
			}
		});
		else
		{
			((ProxySuggestOracle<DoctorProxy>)sb.getSuggestOracle()).addAll(doctorList);
		}
	}
	
	public void retrieveAllExaminers(final ExaminationViewImpl view)
	{
		requests.doctorRequest().findAllDoctors().fire(new OSCEReceiver<List<DoctorProxy>>() {

			@Override
			public void onSuccess(List<DoctorProxy> response) {
				doctorList=response;
			view.showExaminerPopupView();
			PopupView popup=view.getPopupView();
			((ProxySuggestOracle<DoctorProxy>)popup.getExaminerSuggestionBox().getSuggestOracle()).addAll(response);
			Date timeStart=null;
			if(view.getAssignmentProxy()==null)
			{
				timeStart=view.getTimeStart();
				
				popup.getStartTimeValue().setText(DateTimeFormat.getShortDateTimeFormat().format(view.getTimeStart()));
			}
			else
			{
				timeStart=view.getAssignmentProxy().getTimeStart();
				popup.getStartTimeValue().setText(DateTimeFormat.getShortDateTimeFormat().format(view.getAssignmentProxy().getTimeStart()));
			}
				ArrayList<Date> endTimeList=new ArrayList<Date>();
			endTimeList.addAll(postLongStartTimeMap.get(view.getOscePostProxy()));
			endTimeList.addAll(postLunchStartTimeMap.get(view.getOscePostProxy()));
			endTimeList.addAll(postEndTimeMap.get(view.getOscePostProxy()));
			Collections.sort(endTimeList);
			
			for(int i=0;i<endTimeList.size();i++)
			{
				Date d=endTimeList.get(i);
				if(d.before(timeStart))
					endTimeList.remove(i);
			}
			popup.getEndTimeListBox().setAcceptableValues(endTimeList);
			
		//	popup.getEndTimeListBox().setAcceptableValues(postLongStartTimeMap.get(view.getOscePostProxy()));
			//popup.getEndTimeListBox().setAcceptableValues(postEndTimeMap.get(view.getOscePostProxy()));
				
			}
		});
	}
	
	public void autoAssignSP(long id){
		requests.osceRequestNonRoo().autoAssignPatientInRole(id).fire(new OSCEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				
				Log.info("autoAssignSP :" + response);
				MessageConfirmationDialogBox dialogBox=null;
				if(response)
				{
				dialogBox =new MessageConfirmationDialogBox(constants.success());
				dialogBox.showConfirmationDialog(constants.autoSPSuccess());
				}
				else
				{
					dialogBox =new MessageConfirmationDialogBox(constants.failure());
					dialogBox.showConfirmationDialog(constants.autoSPFailure());
				}
				dialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						requests.getEventBus().fireEvent(
								new ApplicationLoadingScreenEvent(true));
						init();
						
						requests.getEventBus().fireEvent(
								new ApplicationLoadingScreenEvent(false));
						
					}
				});
				
			}
		});
	}
	
	public void autoAssignStudent(long id)
	{
		Log.info("autoAssignStudent Clicked :");
		requests.osceRequestNonRoo().autoAssignStudent(id).fire(new OSCEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				Log.info("autoAssignStudent :" + response);
				MessageConfirmationDialogBox dialogBox=null;
				if(response)
				{
				dialogBox =new MessageConfirmationDialogBox(constants.success());
				dialogBox.showConfirmationDialog(constants.autoStudentSuccess());
				}
				else
				{
					dialogBox =new MessageConfirmationDialogBox(constants.failure());
					dialogBox.showConfirmationDialog(constants.autoStudentFailure());
				}
				dialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						requests.getEventBus().fireEvent(
								new ApplicationLoadingScreenEvent(true));
						init();
						
						requests.getEventBus().fireEvent(
								new ApplicationLoadingScreenEvent(false));
						
					}
				
			});
			}
		});
	}
	
    		
}
