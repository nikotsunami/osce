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
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
	
	private Map<OscePostProxy, List<Date>> postLunchStartTimeMap=new HashMap<OscePostProxy, List<Date>>();
	private Map<OscePostProxy, List<Date>> postLongStartTimeMap=new HashMap<OscePostProxy, List<Date>>();
	private Map<OscePostProxy, List<Date>> postAnyStartTimeMap=new HashMap<OscePostProxy, List<Date>>();
	private Map<OscePostProxy,Map<Date,Long>> postAnyEndTimeBreakMap=new HashMap<OscePostProxy, Map<Date,Long>>();
	private Map<OscePostProxy, List<Date>> postEndTimeMap=new HashMap<OscePostProxy, List<Date>>();
	
	private List<DoctorProxy> doctorList;


	public void setOsceProxy(OsceProxy osceProxy) {
		this.osceProxy = osceProxy;
	}
	private long earlyStart=0;
	
	private List<OscePostProxy> earlyStartPost=new ArrayList<OscePostProxy>();
	
	public long getEarlyStart() {
		return earlyStart;
	}

	public void setEarlyStart(long earlyStart) {
		this.earlyStart =  earlyStart;
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
		
		//main view into which all other views are displayed
		final ExaminationScheduleDetailView examinationScheduleDetailView=new ExaminationScheduleDetailViewImpl();
		examinationScheduleDetailView.setPresenter(this);
		examinationScheduleDetailView.setDelegate(this);
		widget.setWidget(examinationScheduleDetailView.asWidget());
		this.view=examinationScheduleDetailView;
		
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
							 
							 
							 //accordian panel which one per sequence
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
								
								//create parcour header. Clicking on it will open the content
								HeaderView headerView=new HeaderViewImpl();
								
								ColorPicker color=ColorPicker.valueOf(courseProxy.getColor());
								
								Log.info("Header Color :" +color);
								//headerView.getColorPicker().setValue(color);
								
								
								//headerView.getDeleteBtn().setVisible(false);
								headerView.getColorPicker().setVisible(false);
								headerView.getHeaderPanel().setHeight("360px");
								headerView.getHeaderSimplePanel().setHeight("330px");
								
								ContentView contentView=new ContentViewImpl();
								contentView.setDelegate(activity);
								contentView.setCourseProxy(courseProxy);
								contentView.getOscePostHP().setHeight("350px");
								contentView.getScrollPanel().setHeight("350px");
								
								// Change in ParcourView
								headerView.setContentView(contentView);
								// E Change in ParcourView
								//accordianView.setContentView(contentView);
								//OscePostView oscePostView=new OscePostViewImpl();
								
								//((ContentViewImpl)contentView).getOscePostHP().insert(oscePostView, ((ContentViewImpl)contentView).getOscePostHP().getWidgetCount());
								//add content and header to accordian
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
	
	
	//calculate time difference in minute
	public long calculateTimeInMinute(Date endTime,Date startTime)
	{
	 	return (endTime.getTime()-startTime.getTime())/60000;		
	}
	
	//insert Student simpat change breaks
	public void insertStudentSimpatChangeBreak(Long slotLength,OscePostView oscePostView)
	{
		
		slotLength--;
		SPView simpatBreak=new SPViewImpl();
		simpatBreak.getSpPanel().addStyleName("empty-bg");
		simpatBreak.getSpPanel().addStyleName("border-bottom-blue");
		simpatBreak.setDelegate(activity);
		simpatBreak.getSpPanel().setHeight(slotLength.toString()+"px");
		simpatBreak.getSpPanel().setWidth("30px");
		
		oscePostView.getStudentSlotsVP().insert(simpatBreak, oscePostView.getStudentSlotsVP().getWidgetCount());
	}
	
	//insert Student breaks
	public void insertStudentBreak(Long slotLength,OscePostView oscePostView,StudentView studentView)
	{
		
		slotLength--;
		
		
		SPView simpatBreak=new SPViewImpl();
		simpatBreak.getSpPanel().addStyleName("border-bottom-red");
		simpatBreak.getSpPanel().addStyleName("empty-bg");
		studentView.getStudentPanel().removeStyleName("border-bottom-yellow");
		studentView.getStudentPanel().addStyleName("border-bottom-red");
		
		simpatBreak.getSpPanel().setHeight(slotLength.toString()+"px");
		simpatBreak.getSpPanel().setWidth("30px");
		
		oscePostView.getStudentSlotsVP().insert(simpatBreak, oscePostView.getStudentSlotsVP().getWidgetCount());
	}
	
	//retrieves content when clicked on any header of content panel. It retrieves data and displays assessment table
	@Override
	public void retrieveContent(final AccordianPanelViewImpl accordianPanelViewImpl, Widget header,
			Widget sp) {
		
		requests.getEventBus().fireEvent(
				new ApplicationLoadingScreenEvent(true));
		Log.info("retrieveContent :");
		if(doctorList!=null)
		doctorList.clear();
		earlyStartPost.clear();
		setEarlyStart(0);
		//Iterator<OscePostProxy> oscePostProxyIterator=accordianPanelViewImpl.getOsceSequenceProxy().getOscePosts().iterator();
		final List<OscePostProxy> oscePostProxies=accordianPanelViewImpl.getOsceSequenceProxy().getOscePosts();
		
		//this is content view into which assessment table will be drawn
		final ContentView contentView=(ContentView)sp;
		
		contentView.getOscePostHP().clear();
		
		
		for(int i=0;i<oscePostProxies.size();i++)
		{	
			final OscePostProxy oscePostProxy=oscePostProxies.get(i);
			Log.info("Osce Post ID :" + oscePostProxy.getId());
			
			 boolean isLastPost=false;
			
			
			if(i==oscePostProxies.size()-1)
				isLastPost=true;
			
			final boolean  isLastPost1=isLastPost;
			//create post which contains student,sp and examiners slot.
			final OscePostView oscePostView=new OscePostViewImpl();
			oscePostView.getOscePostLbl().setText(constants.circuitStation() + " " +oscePostProxy.getSequenceNumber());
			oscePostView.setOscePostProxy(oscePostProxy);
			oscePostView.getOscePostPanel().addStyleName("oscePost-bg");
			if(i==0)
			{
				oscePostView.getOscePostPanel().addStyleName("oscePost-leftTop-radius");
			}
			
			
			
			
			//retrieve student data of particular post and course from assignment table.
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
					
					//calculate Examination duration. If no examiner data is present than end time will be calculated from this.
					final  Date examinerTimeEnd=response.get(response.size()-1).getTimeEnd();
					final  Date examinerStartTime=response.get(0).getTimeStart();
					ArrayList<Date> endTimeList=new ArrayList<Date>();
					endTimeList.add(DateTimeFormat.getShortDateTimeFormat().parse(DateTimeFormat.getShortDateTimeFormat().format(examinerTimeEnd)));
					final Date examinerTimeStart=response.get(0).getTimeStart();
					
					
					
					//calculate duration					
					List<Date> lunchstartTimeList=new ArrayList<Date>();
					List<Date> longstartTimeList=new ArrayList<Date>();
					Map<Date, Long> dateBreakTimeMap=new HashMap<Date, Long>();
					List<Date> anyendTimeList=new ArrayList<Date>();
					
					//1. calculate early start time duration
					if(response.get(0).getTimeStart().before(accordianPanelViewImpl.getOsceDayProxy().getTimeStart()))
					{
						long earlyStart=calculateTimeInMinute(accordianPanelViewImpl.getOsceDayProxy().getTimeStart(), response.get(0).getTimeStart());
						if(getEarlyStart() < earlyStart)
							setEarlyStart(earlyStart);
						
						if(getEarlyStart() != 0)
						earlyStartPost.add(oscePostProxy);
					}
					
					for(int j=0;j<response.size();j++)
					{
						 AssignmentProxy assignmentProxy=response.get(j);
						 	
						 	//calculate student slot length
						 	Long studentSlotLength = calculateTimeInMinute(assignmentProxy.getTimeEnd(), assignmentProxy.getTimeStart());
						 	
							
						 	//if dual post than first slot length is half
							/*if(assignmentProxy.getTimeStart().getHours() != accordianPanelViewImpl.getOsceDayProxy().getTimeStart().getHours() && (oscePostProxy.getOscePostBlueprint().getPostType()==PostType.ANAMNESIS_THERAPY || oscePostProxy.getOscePostBlueprint().getPostType()==PostType.PREPARATION) && j==0)
							{
								studentSlotLength=studentSlotLength/2;
							}*/
							
							
						 	if(j==response.size()-1)
								insertEarlyStartSlot(contentView,isLastPost1);
						 	
						 	
							
							
							//create student view
						 	StudentView studentView=new StudentViewImpl();
						 	studentView.getStudentPanel().addStyleName("border-bottom-yellow");
						 	studentView.getStudentPanel().addStyleName("student-bg");
						 	
							studentView.setAssignmentProxy(assignmentProxy);
							studentView.setDelegate(activity);
							
							NumberFormat.getFormat("000").format(assignmentProxy.getSequenceNumber());
							
							
							if(assignmentProxy.getSequenceNumber() !=null)
								studentView.getStudentLbl().setText(constants.exaPlanStudShort()+NumberFormat.getFormat("000").format(assignmentProxy.getSequenceNumber()));
							
							
							if(j==response.size()-1 && oscePostProxy.getId()==1)
								studentView.getStudentPanel().addStyleName("leftBottom-radius");
							
							oscePostView.getStudentSlotsVP().insert(studentView, oscePostView.getStudentSlotsVP().getWidgetCount());
							
							
							//calculate break Slot length
						 	Long breakTime=0l;
						 	if(j!=response.size()-1)
						 		breakTime=calculateTimeInMinute(response.get(j+1).getTimeStart(),assignmentProxy.getTimeEnd());
							
							Log.info("Student Break Slot length:" + breakTime);
							
							
							int continousShortBreak=osceProxy.getPostLength()+osceProxy.getShortBreak();
							int continousSPChangeBreak=osceProxy.getPostLength()+osceProxy.getShortBreakSimpatChange();
							
							//insert break if any after the student slot
							if((long)osceProxy.getShortBreak()==breakTime)
							{
								studentView.getStudentPanel().getElement().getStyle().setProperty("borderBottomWidth", breakTime+"px");
							}
							else if((long)osceProxy.getShortBreakSimpatChange()==breakTime)
							{
								
								studentSlotLength--;
								
								//insert simpat change break
								insertStudentSimpatChangeBreak(breakTime, oscePostView);
								
								/*breakTime--;
							
								SPView simpatBreak=new SPViewImpl();
								simpatBreak.getSpPanel().addStyleName("empty-bg");
								simpatBreak.getSpPanel().addStyleName("border-bottom-blue");
								simpatBreak.setDelegate(activity);
								simpatBreak.getSpPanel().setHeight(breakTime.toString()+"px");
								simpatBreak.getSpPanel().setWidth("30px");
								
								oscePostView.getStudentSlotsVP().insert(simpatBreak, oscePostView.getStudentSlotsVP().getWidgetCount());*/
							}
							//insert long break
							else if((long)osceProxy.getLongBreak()==breakTime )
							{
								
								longstartTimeList.add(DateTimeFormat.getShortDateTimeFormat().parse(DateTimeFormat.getShortDateTimeFormat().format(assignmentProxy.getTimeEnd())));
								
								studentSlotLength--;
								
								//insert long break
								insertStudentBreak(breakTime, oscePostView,studentView);

								
								//insert simpat change break
								/*breakTime--;
								Short simpatchangeLength=osceProxy.getLongBreak();
								SPView simpatBreak=new SPViewImpl();
								simpatBreak.getSpPanel().addStyleName("border-bottom-red");
								simpatBreak.getSpPanel().addStyleName("empty-bg");
								studentView.getStudentPanel().removeStyleName("border-bottom-yellow");
								studentView.getStudentPanel().addStyleName("border-bottom-red");
								
								simpatBreak.getSpPanel().setHeight(breakTime.toString()+"px");
								simpatBreak.getSpPanel().setWidth("30px");
								
								oscePostView.getStudentSlotsVP().insert(simpatBreak, oscePostView.getStudentSlotsVP().getWidgetCount());
								*/
							}
							//insert long break
							else if(j!=response.size()-1 && (long)osceProxy.getLunchBreak()==breakTime)
							{
								
								lunchstartTimeList.add(DateTimeFormat.getShortDateTimeFormat().parse(DateTimeFormat.getShortDateTimeFormat().format(assignmentProxy.getTimeEnd())));
								studentSlotLength--;
								
								//insert lunch break
								insertStudentBreak(breakTime, oscePostView,studentView);
								
								//insert simpat change break
								/*breakTime--;
								Short simpatchangeLength=osceProxy.getLongBreak();
								SPView simpatBreak=new SPViewImpl();
								simpatBreak.getSpPanel().addStyleName("border-bottom-red");
								simpatBreak.getSpPanel().addStyleName("empty-bg");
								studentView.getStudentPanel().removeStyleName("border-bottom-yellow");
								studentView.getStudentPanel().addStyleName("border-bottom-red");
								
								simpatBreak.getSpPanel().setHeight(breakTime.toString()+"px");
								simpatBreak.getSpPanel().setWidth("30px");
								
								oscePostView.getStudentSlotsVP().insert(simpatBreak, oscePostView.getStudentSlotsVP().getWidgetCount());*/
							}
							
							//any break
							else if((long)osceProxy.getMiddleBreak()==breakTime || (long)osceProxy.getLunchBreak()==breakTime || (continousShortBreak+osceProxy.getMiddleBreak()==breakTime)  || (continousShortBreak+osceProxy.getLunchBreak()==breakTime) || continousShortBreak+osceProxy.getLongBreak()==breakTime || continousSPChangeBreak+osceProxy.getLongBreak()==breakTime || continousSPChangeBreak+osceProxy.getLunchBreak()==breakTime 
									|| continousSPChangeBreak+osceProxy.getMiddleBreak()==breakTime)
							{
								anyendTimeList.add(DateTimeFormat.getShortDateTimeFormat().parse(DateTimeFormat.getShortDateTimeFormat().format(assignmentProxy.getTimeEnd())));
								dateBreakTimeMap.put(DateTimeFormat.getShortDateTimeFormat().parse(DateTimeFormat.getShortDateTimeFormat().format(assignmentProxy.getTimeEnd())), breakTime);
								studentSlotLength--;
								
								//insert lunch break
								insertStudentBreak(breakTime, oscePostView,studentView);
								
							}
							
						/*	else if(j!=response.size()-1 && (long)osceProxy.getMiddleBreak()==breakTime)
							{
								//endTimeList.add(assignmentProxy.getTimeEnd());
								studentSlotLength--;
								
								//insert lunch break
								insertStudentBreak(breakTime, oscePostView,studentView);
								
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
							
							else if((continousShortBreak+osceProxy.getMiddleBreak()==breakTime))
							{
								//endTimeList.add(assignmentProxy.getTimeEnd());
								studentSlotLength--;
								
								//insert middle break
								insertStudentBreak(breakTime, oscePostView,studentView);
								
								//insert simpat change break
								breakTime--;
								
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
							else if((continousShortBreak+osceProxy.getLunchBreak()==breakTime) || continousShortBreak+osceProxy.getLongBreak()==breakTime || continousSPChangeBreak+osceProxy.getLongBreak()==breakTime || continousSPChangeBreak+osceProxy.getLunchBreak()==breakTime 
									|| continousSPChangeBreak+osceProxy.getMiddleBreak()==breakTime)
							{
								//endTimeList.add(assignmentProxy.getTimeEnd());
								studentSlotLength--;
								
								//insert continousShortBreak break
								insertStudentBreak(breakTime, oscePostView,studentView);
								
								//insert simpat change break
								breakTime--;
								
								SPView simpatBreak=new SPViewImpl();
								
								simpatBreak.getSpPanel().addStyleName("empty-bg");
								simpatBreak.getSpPanel().addStyleName("border-bottom-red");
								studentView.getStudentPanel().removeStyleName("border-bottom-yellow");
								studentView.getStudentPanel().addStyleName("border-bottom-red");
								
								simpatBreak.setDelegate(activity);
								simpatBreak.getSpPanel().setHeight(breakTime.toString()+"px");
								simpatBreak.getSpPanel().setWidth("30px");
								
								oscePostView.getStudentSlotsVP().insert(simpatBreak, oscePostView.getStudentSlotsVP().getWidgetCount());
							}*/
							/*else if((continousShortBreak+osceProxy.getLongBreak()==breakTime))
							{
								//endTimeList.add(assignmentProxy.getTimeEnd());
								studentSlotLength--;
								
								//insert simpat change break
								breakTime--;
								
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
							else if((continousSPChangeBreak+osceProxy.getLongBreak()==breakTime))
							{
								//endTimeList.add(assignmentProxy.getTimeEnd());
								studentSlotLength--;
								
								//insert simpat change break
								breakTime--;
								
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
							else if((continousSPChangeBreak+osceProxy.getLunchBreak()==breakTime))
							{
								//endTimeList.add(assignmentProxy.getTimeEnd());
								studentSlotLength--;
								
								//insert simpat change break
								breakTime--;
								
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
							else if((continousSPChangeBreak+osceProxy.getMiddleBreak()==breakTime))
							{
								//endTimeList.add(assignmentProxy.getTimeEnd());
								studentSlotLength--;
								
								//insert simpat change break
								breakTime--;
								
								SPView simpatBreak=new SPViewImpl();
								
								simpatBreak.getSpPanel().addStyleName("empty-bg");
								simpatBreak.getSpPanel().addStyleName("border-bottom-red");
								studentView.getStudentPanel().removeStyleName("border-bottom-yellow");
								studentView.getStudentPanel().addStyleName("border-bottom-red");
								
								simpatBreak.setDelegate(activity);
								simpatBreak.getSpPanel().setHeight(breakTime.toString()+"px");
								simpatBreak.getSpPanel().setWidth("30px");
								
								oscePostView.getStudentSlotsVP().insert(simpatBreak, oscePostView.getStudentSlotsVP().getWidgetCount());
							}*/
							studentView.getStudentPanel().setHeight(studentSlotLength.toString()+"px");
						 
					}
					//startTimeList.add(response.get(response.size()-1).getTimeEnd());
					postLunchStartTimeMap.put(oscePostProxy, lunchstartTimeList);
					postAnyStartTimeMap.put(oscePostProxy, anyendTimeList);
					postLongStartTimeMap.put(oscePostProxy, longstartTimeList);
					postAnyEndTimeBreakMap.put(oscePostProxy, dateBreakTimeMap);
					postEndTimeMap.put(oscePostProxy, endTimeList);
					
					//create Examiners Slots/ Create Examiner view inside oscePostView
					if(oscePostProxy.getOscePostBlueprint().getPostType()!=PostType.BREAK)
					requests.assignmentRequestNonRoo().retrieveAssignmenstOfTypeExaminer(accordianPanelViewImpl.getOsceDayProxy().getId(), accordianPanelViewImpl.getOsceSequenceProxy().getId(), contentView.getCourseProxy().getId(),oscePostProxy.getId())
					.with("examiner").fire(new OSCEReceiver<List<AssignmentProxy>>() {

						@Override
						public void onSuccess(List<AssignmentProxy> response) {
							
							
							
							Log.info("onSuccess retrieveContent : response size :" + response.size());
							
							//create examination slot 
							if(response.size()==0)
							{
								//calculate examiner slot length								
								Long examinerSlotLength=0l;
								//if(examinerTimeEnd.before(examinerTimeStart))
								
							//	if(oscePostProxy.getOscePostBlueprint().getPostType()==PostType.ANAMNESIS_THERAPY || oscePostProxy.getOscePostBlueprint().getPostType()==PostType.PREPARATION)
							//		examinerSlotLength=calculateTimeInMinute(examinerTimeEnd,examinerStartTime);
							//	else
									 examinerSlotLength=calculateTimeInMinute(examinerTimeEnd,examinerStartTime);
					
								//examinerSlotLength=examinerSlotLength/60000;
								examinerSlotLength--;
								
								//create examiner slot view
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
								
							}
						//	if(response.get(response.size()-1).getTimeEnd().equals(examinerTimeEnd))
							for(int j=0;j<response.size();j++)
							{
								 AssignmentProxy assignmentProxy=response.get(j);
								 
								 	//calculate duration
								 	Long examinerSlotLength=0l;
								 	/* if(j==0)
									 {
								 		examinerSlotLength=calculateTimeInMinute(assignmentProxy.getTimeEnd(), accordianPanelViewImpl.getOsceDayProxy().getTimeStart());
									 }
									 else	*/						
										 examinerSlotLength=calculateTimeInMinute(assignmentProxy.getTimeEnd(), assignmentProxy.getTimeStart());
								 	 
									//examinerSlotLength=examinerSlotLength/60000;
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
										
										examinationView.getExaminerLbl().setText(constants.exaPlanExaShort()+NumberFormat.getFormat("00").format(assignmentProxy.getSequenceNumber()));
									}
									
									oscePostView.getExaminerVP().insert(examinationView, oscePostView.getExaminerVP().getWidgetCount());
									
									
									//calulate break
									Long breakTime=0l;
									if(j!=response.size()-1)
										breakTime=calculateTimeInMinute(response.get(j+1).getTimeStart(),assignmentProxy.getTimeEnd());
									//breakTime=breakTime/60000;
									
									Log.info("Examiner Break :" + breakTime);
									
									int continousShortBreak=osceProxy.getPostLength()+osceProxy.getShortBreak();
									int continousSPChangeBreak=osceProxy.getPostLength()+osceProxy.getShortBreakSimpatChange();
									
									
									//insert long or lunch break
									if((long)osceProxy.getLongBreak()==breakTime || (long)osceProxy.getLunchBreak()==breakTime)
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
									//any break
									else if((long)osceProxy.getMiddleBreak()==breakTime || (long)osceProxy.getLunchBreak()==breakTime || (continousShortBreak+osceProxy.getMiddleBreak()==breakTime)  || (continousShortBreak+osceProxy.getLunchBreak()==breakTime) || continousShortBreak+osceProxy.getLongBreak()==breakTime || continousSPChangeBreak+osceProxy.getLongBreak()==breakTime || continousSPChangeBreak+osceProxy.getLunchBreak()==breakTime 
											|| continousSPChangeBreak+osceProxy.getMiddleBreak()==breakTime)
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
									/*else if(j!=response.size()-1 && (long)osceProxy.getLunchBreak()==breakTime)
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
									}*/
									
							
									
									
								 
							}
							
							
							
							
							
						}
					});
					
					
					
					requests.getEventBus().fireEvent(
							new ApplicationLoadingScreenEvent(false));
				}
			});
			//retrieve sp data and creates all slots of sp and add it to oscePostView
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
						 	
						 	//calculate sp slot length
						
						 Long spSlotLength=0l;
						// if((oscePostProxy.getOscePostBlueprint().getPostType()==PostType.ANAMNESIS_THERAPY || oscePostProxy.getOscePostBlueprint().getPostType()==PostType.PREPARATION)&& j==0)
						// {
						//	 spSlotLength=calculateTimeInMinute(assignmentProxy.getTimeEnd(), accordianPanelViewImpl.getOsceDayProxy().getTimeStart());
						// }
						 //else							
						 	 spSlotLength=calculateTimeInMinute(assignmentProxy.getTimeEnd(),assignmentProxy.getTimeStart());
						 
						//	spSlotLength=spSlotLength/60000;
							spSlotLength--;
							
							//create SP View
							SPView spView=null;
							
							//If
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
										spView.getSpLbl().setText(constants.exaPlanSpShort()+NumberFormat.getFormat("00").format(assignmentProxy.getSequenceNumber()));
										
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
										spView.getSpLbl().setText(constants.exaPlanSpShort()+NumberFormat.getFormat("00").format(assignmentProxy.getSequenceNumber()));
										
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
									spView.getSpLbl().setText(constants.exaPlanSpShort()+NumberFormat.getFormat("00").format(assignmentProxy.getSequenceNumber()));									
								}
								
								oscePostView.getSpSlotsVP().insert(spView, oscePostView.getSpSlotsVP().getWidgetCount());
							}
							
							//create Breaks							
							Long breakTime=0l;
							if(j!=response.size()-1)
								breakTime=calculateTimeInMinute(response.get(j+1).getTimeStart(),assignmentProxy.getTimeEnd());
							//breakTime=breakTime/60000;
							Log.info("SP Break :" + breakTime);
							
							//insert simpat change break
							if((long)osceProxy.getShortBreakSimpatChange()==breakTime)
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
							
							//insert lunch/long/middle break
							else if((long)osceProxy.getLongBreak()==breakTime || (long)osceProxy.getLunchBreak()==breakTime || (long)osceProxy.getMiddleBreak()==breakTime)
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
							/*else if(j!=response.size()-1 && (long)osceProxy.getLunchBreak()==breakTime)
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
							}*/
							
							
							
							
							
					}
				
					requests.getEventBus().fireEvent(
							new ApplicationLoadingScreenEvent(false));
				}
				
			});
			
			
			
			
			contentView.getOscePostHP().insert(oscePostView, contentView.getOscePostHP().getWidgetCount());
			
			

		}
		
		
		//create logical Sp Break
		createLogicalBreakOfSP(accordianPanelViewImpl, contentView);
		
		
		requests.getEventBus().fireEvent(
				new ApplicationLoadingScreenEvent(false));
		
	}
	
	
	public void insertEarlyStartSlot(ContentView contentView,boolean isLastPost)
	{
		//insert earlyStart empty slot
		if(isLastPost && getEarlyStart() != 0)
		{	
			
			
			earlyStart--;
			for(int k=0;k<contentView.getOscePostHP().getWidgetCount();k++)
			{
				OscePostView postView=(OscePostView)contentView.getOscePostHP().getWidget(k);
				
				if(postView.getOscePostProxy()!= null && !checkIfPostIsEarly(postView.getOscePostProxy()))
				{
					
					SPView simpatBreak=new SPViewImpl();
					simpatBreak.getSpPanel().addStyleName("empty-bg");
					simpatBreak.getSpPanel().addStyleName("border-bottom-red");

					//simpatBreak.setDelegate(activity);
					simpatBreak.getSpPanel().setHeight(getEarlyStart()+"px");
					simpatBreak.getSpPanel().setWidth("30px");
					
					postView.getStudentSlotsVP().insert(simpatBreak, 0);
					
					SPView simpatBreak1=new SPViewImpl();
					simpatBreak1.getSpPanel().addStyleName("empty-bg");
					simpatBreak1.getSpPanel().addStyleName("border-bottom-red");

					//simpatBreak.setDelegate(activity);
					simpatBreak1.getSpPanel().setHeight(getEarlyStart()+"px");
					simpatBreak1.getSpPanel().setWidth("30px");
					
					postView.getSpSlotsVP().insert(simpatBreak1, 0);
					
					SPView simpatBreak2=new SPViewImpl();
					simpatBreak2.getSpPanel().addStyleName("empty-bg");
					simpatBreak2.getSpPanel().addStyleName("border-bottom-red");

					//simpatBreak.setDelegate(activity);
					simpatBreak2.getSpPanel().setHeight(getEarlyStart()+"px");
					simpatBreak2.getSpPanel().setWidth("30px");
					postView.getExaminerVP().insert(simpatBreak2, 0);
				}
			}
			
			
			//setEarlyStart(0);
			//earlyStartPost.clear();
			
		}
	}
	public boolean checkIfPostIsEarly(OscePostProxy postProxy)
	{
		for(OscePostProxy earlyPost: earlyStartPost)
		{
			if(earlyPost.getId() == postProxy.getId())
				return true;
		}
		
		return false;
	}
	//create Logical break for each parcor
	public void createLogicalBreakOfSP(final AccordianPanelViewImpl accordianPanelViewImpl,final ContentView contentView)
	{
		//create logical break post
		//retrieve data of logical sp break and create slots.
		requests.assignmentRequestNonRoo().retrieveAssignmentOfLogicalBreakPost(accordianPanelViewImpl.getOsceDayProxy().getId(), accordianPanelViewImpl.getOsceSequenceProxy().getId(), contentView.getCourseProxy().getId()).with("patientInRole","patientInRole.patientInSemester","patientInRole.patientInSemester.standardizedPatient").fire(new OSCEReceiver<List<AssignmentProxy>>() {

			@Override
			public void onSuccess(List<AssignmentProxy> response) {
				Log.info("retrieveAssignmentOfLogicalBreakPost Success Assignment Size :" + response.size());
				
				requests.getEventBus().fireEvent(
						new ApplicationLoadingScreenEvent(true));
				
				Log.info("onSuccess retrieveAssignmentOfLogicalBreakPost : response size :" + response.size());
				
				
				//create osce post view				
				final OscePostView oscePostView=new OscePostViewImpl();
				oscePostView.getOscePostLbl().setText(constants.exaPlanBreakPost());
				
				oscePostView.getOscePostPanel().addStyleName("oscePost-bg");
				oscePostView.getOscePostPanel().addStyleName("oscePost-RightTop-radius");
				//create all slots of SP which are in logical break
				
				for(int j=0;j<response.size();j++)
				{
					 	AssignmentProxy assignmentProxy=response.get(j);
					 	
					 	//calculate duration /slot length
						
					 Long spSlotLength=0l;
					 

					 	 spSlotLength=calculateTimeInMinute(assignmentProxy.getTimeEnd(),assignmentProxy.getTimeStart());
					 
						//spSlotLength=spSlotLength/60000;
						spSlotLength--;
						

							
						HorizontalPanel spHp=new HorizontalPanel();
						spHp.setSpacing(0);
						for(int k=j;k<response.size();k++)
						{
							//create SP slot View
							SPView spView=new SPViewImpl();
							spView.getSpPanel().addStyleName("border-bottom-blue");
							spView.getSpPanel().addStyleName("sp-bg");
							spView.setAssignmentProxy(response.get(k));
							spView.setDelegate(activity);
							if(spSlotLength>=0)
								spView.getSpPanel().setHeight(spSlotLength+"px");
							
							if(response.get(k).getSequenceNumber() != null)
							{
								spView.getSpLbl().setText(constants.exaPlanSpShort()+NumberFormat.getFormat("00").format(response.get(k).getSequenceNumber()));
								
							}
							
							spHp.add(spView);
							if((k+1)<response.size() && response.get(k+1).getTimeStart().getTime()!= response.get(k).getTimeStart().getTime())
							{
								j=k;
								break;
							}
							j=k;
							
						}
							
						oscePostView.getStudentSlotsVP().insert(spHp, oscePostView.getStudentSlotsVP().getWidgetCount());	
						
						//calculate Break						
						Long breakTime=0l;
						if(j!=response.size()-1)
							breakTime=response.get(j+1).getTimeStart().getTime()-assignmentProxy.getTimeEnd().getTime();
						breakTime=breakTime/60000;
						Log.info("Break :" + breakTime);
						
																								
						//create break
						if((long)osceProxy.getShortBreakSimpatChange()==breakTime)
						{
							//insert simpat change break
							breakTime--;
							
							HorizontalPanel breakHP=new HorizontalPanel();
							for(int i=0;i<spHp.getWidgetCount();i++)
							{
							SPView simpatBreak=new SPViewImpl();
							simpatBreak.getSpPanel().addStyleName("border-bottom-blue");
							simpatBreak.getSpPanel().addStyleName("empty-bg");
							simpatBreak.setDelegate(activity);
							//breakTime--;
							simpatBreak.getSpPanel().setHeight(breakTime.toString()+"px");
							simpatBreak.getSpPanel().setWidth("30px");
							}
							oscePostView.getStudentSlotsVP().insert(breakHP, oscePostView.getStudentSlotsVP().getWidgetCount());
						}
						//insert lunch/long/middle break
						else if((long)osceProxy.getLongBreak()==breakTime || (long)osceProxy.getMiddleBreak()==breakTime || (long)osceProxy.getLunchBreak()==breakTime)
						{
							//insert simpat change break
							breakTime--;
							
							
							HorizontalPanel breakHP=new HorizontalPanel();
							for(int i=0;i<spHp.getWidgetCount();i++)
							{
								SPView simpatBreak=new SPViewImpl();
								simpatBreak.getSpPanel().addStyleName("empty-bg");
								simpatBreak.getSpPanel().addStyleName("border-bottom-red");
								((SPView)(spHp.getWidget(i))).getSpPanel().removeStyleName("border-bottom-blue");
								((SPView)(spHp.getWidget(i))).getSpPanel().addStyleName("border-bottom-red");
								simpatBreak.setDelegate(activity);
								simpatBreak.getSpPanel().setHeight(breakTime.toString()+"px");
								simpatBreak.getSpPanel().setWidth("30px");
								breakHP.add(simpatBreak);
							}
							
							
							oscePostView.getStudentSlotsVP().insert(breakHP, oscePostView.getStudentSlotsVP().getWidgetCount());
						}
						/*else if( (long)osceProxy.getLunchBreak()==breakTime)
						{
							//studentSlotLength--;
							
							//insert simpat change break
							breakTime--;
							Short simpatchangeLength=osceProxy.getLunchBreak();
							
							HorizontalPanel breakHP=new HorizontalPanel();
							for(int i=0;i<spHp.getWidgetCount();i++)
							{
								SPView simpatBreak=new SPViewImpl();
								simpatBreak.getSpPanel().addStyleName("empty-bg");
								simpatBreak.getSpPanel().addStyleName("border-bottom-red");
								((SPView)(spHp.getWidget(i))).getSpPanel().removeStyleName("border-bottom-blue");
								((SPView)(spHp.getWidget(i))).getSpPanel().addStyleName("border-bottom-red");
								simpatBreak.setDelegate(activity);
								simpatBreak.getSpPanel().setHeight(breakTime.toString()+"px");
								simpatBreak.getSpPanel().setWidth("30px");
								breakHP.add(simpatBreak);
							}
							
							
							oscePostView.getStudentSlotsVP().insert(breakHP, oscePostView.getStudentSlotsVP().getWidgetCount());
						}
						else if((long)osceProxy.getMiddleBreak()==breakTime)
						{
							//insert simpat change break
							breakTime--;
							Short simpatchangeLength=osceProxy.getMiddleBreak();
							
							HorizontalPanel breakHP=new HorizontalPanel();
							for(int i=0;i<spHp.getWidgetCount();i++)
							{
								SPView simpatBreak=new SPViewImpl();
								simpatBreak.getSpPanel().addStyleName("empty-bg");
								simpatBreak.getSpPanel().addStyleName("border-bottom-red");
								((SPView)(spHp.getWidget(i))).getSpPanel().removeStyleName("border-bottom-blue");
								((SPView)(spHp.getWidget(i))).getSpPanel().addStyleName("border-bottom-red");
								simpatBreak.setDelegate(activity);
								simpatBreak.getSpPanel().setHeight(breakTime.toString()+"px");
								simpatBreak.getSpPanel().setWidth("30px");
								breakHP.add(simpatBreak);
							}
							
							
							oscePostView.getStudentSlotsVP().insert(breakHP, oscePostView.getStudentSlotsVP().getWidgetCount());
						}
						*/
					 
				}
				contentView.getOscePostHP().insert(oscePostView, contentView.getOscePostHP().getWidgetCount());
				
				requests.getEventBus().fireEvent(
						new ApplicationLoadingScreenEvent(false));
				
			}
		});
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
			view.getExaminerLbl().setText(constants.exaPlanExaShort()+sequenceNumber);
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
				boolean isAnyBreak=false;
				List<Date> dateList=postEndTimeMap.get(view.getOscePostProxy());
				for(Date d:dateList)
				{
					if(d.equals(view.getPopupView().getEndTimeListBox().getValue()))
					{
						isEndTime=true;
					}
				}
				
				 Map<Date,Long> dateBreakMap=postAnyEndTimeBreakMap.get(view.getOscePostProxy());
				 
				 
				if(dateBreakMap != null && dateBreakMap.containsKey(view.getPopupView().getEndTimeListBox().getValue()))
				{
					
					isAnyBreak=true;
					Log.info("isAnyBreak  :" +isAnyBreak);
				}
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
				if(isAnyBreak)
				{
					AssignmentRequest assignmentRequest1=requests.assignmentRequest();
					AssignmentProxy assignmentProxy1=assignmentRequest1.create(AssignmentProxy.class);
					
					Short lunchBreak=dateBreakMap.get(view.getPopupView().getEndTimeListBox().getValue()).shortValue();
					Log.info("Any Break :" + lunchBreak);
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
				else{
					
					//refresh ExaminerView;
					refreshExaminerView(view);
				}
				((PopupViewImpl)view.getPopupView()).hide();
			}
		});
		
	}
	
	public void refreshExaminerView(final ExaminationViewImpl examinationViewOld)
	{
		final OscePostProxy oscePostProxy=examinationViewOld.getOscePostProxy();
		final OsceDayProxy osceDayProxy=examinationViewOld.getOsceDayProxy();
		final OscePostView oscePostView=examinationViewOld.getOscePostView();
		oscePostView.getExaminerVP().clear();
		requests.assignmentRequestNonRoo().retrieveAssignmenstOfTypeExaminer(osceDayProxy.getId(), examinationViewOld.getOsceSequenceProxy().getId(), examinationViewOld.getCourseProxy().getId(),oscePostProxy.getId())
		.with("examiner").fire(new OSCEReceiver<List<AssignmentProxy>>() {

			@Override
			public void onSuccess(List<AssignmentProxy> response) {
				
				
				
				Log.info("onSuccess retrieveContent : response size :" + response.size());
				
				
				//add earlystart if there
				
				if(!response.get(0).getTimeStart().before(examinationViewOld.getOsceDayProxy().getTimeStart()))
				{
					
					if(earlyStart > 0)
					{
						SPView simpatBreak=new SPViewImpl();
						simpatBreak.getSpPanel().addStyleName("empty-bg");
						simpatBreak.getSpPanel().addStyleName("border-bottom-red");

						//simpatBreak.setDelegate(activity);
						simpatBreak.getSpPanel().setHeight(getEarlyStart()+"px");
						simpatBreak.getSpPanel().setWidth("30px");
						
						oscePostView.getExaminerVP().insert(simpatBreak, 0);
					}
				}
				
				//create examination slot 

			//	if(response.get(response.size()-1).getTimeEnd().equals(examinerTimeEnd))
				for(int j=0;j<response.size();j++)
				{
					 AssignmentProxy assignmentProxy=response.get(j);
					 
					 	//calculate duration
					 	Long examinerSlotLength=0l;
					/* 	 if(j==0)
						 {
					 		examinerSlotLength=assignmentProxy.getTimeEnd().getTime()- osceDayProxy.getTimeStart().getTime();
						 }
						 else*/							
							 examinerSlotLength=calculateTimeInMinute(assignmentProxy.getTimeEnd(),assignmentProxy.getTimeStart());
						
						
					 	 
						//examinerSlotLength=examinerSlotLength/60000;
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
						examinationView.setOscePostView(oscePostView);
						examinationView.setOsceDayProxy(osceDayProxy);
						
						examinationView.setCourseProxy(examinationViewOld.getCourseProxy());
					 	examinationView.setOsceSequenceProxy(examinationViewOld.getOsceSequenceProxy());
					 	examinationView.setOsceDayProxy(examinationViewOld.getOsceDayProxy());
					 	examinationView.setOscePostRoomProxy(examinationViewOld.getOscePostRoomProxy());
				
						
						
						if(j>0)
					 		examinationView.setPreviousAssignmentProxy(response.get(j-1));
						if(assignmentProxy.getSequenceNumber() != null)
						{
							
							examinationView.getExaminerLbl().setText(constants.exaPlanExaShort()+assignmentProxy.getSequenceNumber().toString());
						}
						
						oscePostView.getExaminerVP().insert(examinationView, oscePostView.getExaminerVP().getWidgetCount());
						
						//calulate break
						Long breakTime=0l;
						if(j!=response.size()-1)
							breakTime=response.get(j+1).getTimeStart().getTime()-assignmentProxy.getTimeEnd().getTime();
						breakTime=breakTime/60000;
						
						Log.info("Break :" + breakTime);
						
						int continousShortBreak=osceProxy.getPostLength()+osceProxy.getShortBreak();
						int continousSPChangeBreak=osceProxy.getPostLength()+osceProxy.getShortBreakSimpatChange();
						
						//insert long break
						if( (long)osceProxy.getLongBreak()==breakTime)
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
						else if( (long)osceProxy.getLunchBreak()==breakTime)
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
						//any break
						else if((long)osceProxy.getMiddleBreak()==breakTime || (long)osceProxy.getLunchBreak()==breakTime || (continousShortBreak+osceProxy.getMiddleBreak()==breakTime)  || (continousShortBreak+osceProxy.getLunchBreak()==breakTime) || continousShortBreak+osceProxy.getLongBreak()==breakTime || continousSPChangeBreak+osceProxy.getLongBreak()==breakTime || continousSPChangeBreak+osceProxy.getLunchBreak()==breakTime 
								|| continousSPChangeBreak+osceProxy.getMiddleBreak()==breakTime)
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
			endTimeList.addAll(postAnyStartTimeMap.get(view.getOscePostProxy()));
			Collections.sort(endTimeList);
			int i=0;
			while(i<endTimeList.size())
			{
				
				Date d=endTimeList.get(i);
				if(d.before(timeStart))
				{
					endTimeList.remove(i);
					i=0;
				}
				else
					i++;
			}
			
			popup.getEndTimeListBox().setAcceptableValues(endTimeList);
			
		//	popup.getEndTimeListBox().setAcceptableValues(postLongStartTimeMap.get(view.getOscePostProxy()));
			//popup.getEndTimeListBox().setAcceptableValues(postEndTimeMap.get(view.getOscePostProxy()));
				
			}
		});
	}
	
	public void autoAssignSP(long id){
	
		requests.getEventBus().fireEvent(
				new ApplicationLoadingScreenEvent(true));
		
		requests.osceRequestNonRoo().autoAssignPatientInRole(id).fire(new OSCEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				
				
				
				Log.info("autoAssignSP :" + response);
				MessageConfirmationDialogBox dialogBox=null;
				if(response)
				{
				dialogBox =new MessageConfirmationDialogBox(constants.success());
				dialogBox.showConfirmationDialog(constants.exaPlanSpSuccess());
				}
				else
				{
					dialogBox =new MessageConfirmationDialogBox(constants.warning());
					dialogBox.showConfirmationDialog(constants.exaPlanSpFailure());
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
			
				
				requests.getEventBus().fireEvent(
						new ApplicationLoadingScreenEvent(false));	
			}
		});
		
		
	}
	
	public void autoAssignStudent(long id)
	{
		Log.info("autoAssignStudent Clicked :");
		
		requests.getEventBus().fireEvent(
				new ApplicationLoadingScreenEvent(true));
		
		requests.osceRequestNonRoo().autoAssignStudent(id).fire(new OSCEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				
				
				
				Log.info("autoAssignStudent :" + response);
				MessageConfirmationDialogBox dialogBox=null;
				if(response)
				{
				dialogBox =new MessageConfirmationDialogBox(constants.success());
				dialogBox.showConfirmationDialog(constants.exaPlanStudentSuccess());
				}
				else
				{
					dialogBox =new MessageConfirmationDialogBox(constants.warning());
					dialogBox.showConfirmationDialog(constants.exaPlanStudentFailure());
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
				
				requests.getEventBus().fireEvent(
						new ApplicationLoadingScreenEvent(false));
			}
		});
		
		
	}
	
    		
}
