package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.AutoAssignPatientInSemesterService;
import ch.unibas.medizin.osce.client.AutoAssignPatientInSemesterServiceAsync;
//import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.DMZSyncException;
import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.DMZSyncService;
import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.DMZSyncServiceAsync;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleAssignmentPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment.ManualStandardizedPatientInSemesterAssignmentPopupView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment.ManualStandardizedPatientInSemesterAssignmentPopupViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment.OsceDaySubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment.OsceDaySubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment.PatientInRoleSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment.PatientInRoleSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment.PatientInSemesterData;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment.RoleAssignmentView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment.RoleAssignmentViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment.RoleSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment.RoleSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.PatientInSemesterSelectedEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RoleFulfilCriteriaEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RoleSelectedEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceRequest;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInRoleRequest;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterRequest;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.shared.AutoAssignPatientInSemesterEvent;
import ch.unibas.medizin.osce.shared.AutoAssignPatientInSemesterListener;
import ch.unibas.medizin.osce.shared.OSCESecurityStatus;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.OsceStatus;
import ch.unibas.medizin.osce.shared.PatientAveragePerPost;
import ch.unibas.medizin.osce.shared.RoleTypes;
import ch.unibas.medizin.osce.shared.StandardizedPatientStatus;
import ch.unibas.medizin.osce.shared.StudyYears;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.shared.i18n.OsceConstantsWithLookup;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.Range;

import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.RemoteEventServiceFactory;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;




@SuppressWarnings("deprecation")
public class RoleAssignmentPatientInSemesterActivity extends AbstractActivity
		implements RoleAssignmentView.Presenter, RoleAssignmentView.Delegate,
		ManualStandardizedPatientInSemesterAssignmentPopupView.Delegate,OsceDaySubView.Delegate,RoleSubView.Delegate,PatientInRoleSubView.Delegate {//Module 2:Assignment D

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private RoleAssignmentViewImpl view;
	private RoleAssignmentPlace place;
	private RoleAssignmentPatientInSemesterActivity spRoleAssignmentActivity;
	private SemesterProxy semesterProxy = null;
	private ActivityManager activityManager;
	private ManualStandardizedPatientInSemesterAssignmentPopupViewImpl manualStdPatientInSemesterAssignmentPopupViewImpl;
	private List<PatientInSemesterProxy> patientInSemesterProxies;
	//Module 3 : Assignment E : Start
	// 
	private List<PatientInSemesterProxy> allPatientInSemesterProxies;
	//Module 3 : Assignment E : Stop
	// 
	private OscePostProxy oscePostProxy;
	private DMZSyncServiceAsync dmxSyncService = null;
	private OsceConstantsWithLookup messageLookup = GWT.create(OsceConstantsWithLookup.class);
	//Module 3 {
	// Module 3 {
	private static final OsceConstants constants = GWT.create(OsceConstants.class);
	// private SemesterProxy semesterProxy;
	private OsceProxy osceProxy;
	private OsceDayProxy osceDayProxy;
	private OsceDaySubViewImpl osceDaySubViewImpl;
	
	//change
	private List<OsceDaySubViewImpl> osceDaySubViewImplList=new ArrayList<OsceDaySubViewImpl>();
//	private DisclosurePanel disCloserPanel;
	private StudentsActivity activity;
	private boolean isAssignedFirst =false;
	// private HashMap<String,Object> timerMap;
	private Timer osceDayTimer;
	private OsMaConstant osMaConstant = GWT.create(OsMaConstant.class);
	private boolean isPatientInSemesterFulfill;
	
	// change {
		private OsceDayProxy roleSelectedInOsceDay;
		private OsceDayProxy osce_DayAtDelete;
		private PatientInRoleProxy patientInRole;
		// change }
	private List<Boolean> advanceSearchCriteriaList;
	private List<AdvancedSearchCriteriaProxy> advancedSearchCriteriaProxies;
	private RoleSubViewImpl roleSubViewSelected;
	private boolean isPatientInSemesterProxiesAvail;

	// Module 3 }

	//ServerPush event {
	private static final Domain DOMAIN = DomainFactory.getDomain("localhost");
	private AutoAssignPatientInSemesterServiceAsync autoAssignmentPatientInSemesterService = GWT.create(AutoAssignPatientInSemesterService.class);
	//ServerPush event }
	public OscePostProxy getOscePostProxy() {
		return oscePostProxy;
	}

	public void setOscePost(OscePostProxy oscePostProxy) {
		this.oscePostProxy = oscePostProxy;
	}

	private List<PatientInSemesterData> patientInSemesterDataList;
	public HandlerManager handlerManager;

	private RoleAssignmentPatientInSemesterActivityMapper activityMapper;

	public RoleAssignmentPatientInSemesterActivity(OsMaRequestFactory requests,
			PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;

		activityMapper = new RoleAssignmentPatientInSemesterActivityMapper(
				requests, placeController);
		this.activityManager = new ActivityManager(activityMapper,
				requests.getEventBus());
		ApplicationLoadingScreenEvent.initialCounter();
		ApplicationLoadingScreenEvent.register(requests.getEventBus(),
				new ApplicationLoadingScreenHandler() {
					@Override
					public void onEventReceived(
							ApplicationLoadingScreenEvent event) {
//						Log.info("ApplicationLoadingScreenEvent onEventReceived Called");
						event.display();
					}
				});

	}

	public RoleAssignmentPatientInSemesterActivity(OsMaRequestFactory requests,
			PlaceController placeController, RoleAssignmentPlace place) {

		this.requests = requests;		
		this.placeController = placeController;
		this.place = place;
		this.handlerManager = place.handler;
		this.semesterProxy = place.semesterProxy;

		activityMapper = new RoleAssignmentPatientInSemesterActivityMapper(
				requests, placeController);
		this.activityManager = new ActivityManager(activityMapper,
				requests.getEventBus());
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

		this.addSelectChangeHandler(new SelectChangeHandler() {
			@Override
			public void onSelectionChange(SelectChangeEvent event) {
				Log.info("Call Role Activity");
				Log.info("onSelectionChange Get Semester: "
						+ event.getSemesterProxy().getCalYear());
				semesterProxy = event.getSemesterProxy();
				init();
			}
		});
	}

	public void addSelectChangeHandler(SelectChangeHandler handler) {
		handlerManager.addHandler(SelectChangeEvent.getType(), handler);
	}

	/**
	 * Clean up activity on finish (close popups and disable display of current
	 * activities view)
	 */
	public void onStop() {
	}

	/**
	 * Initializes the corresponding views and initializes the tables as well as
	 * their corresponding handlers.
	 */

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		dmxSyncService = DMZSyncService.ServiceFactory.instance();
		view = new RoleAssignmentViewImpl();
		view.setDelegate(this);
		RoleSelectedEvent.register(requests.getEventBus(), view);
		spRoleAssignmentActivity = this;

		MenuClickEvent.register(requests.getEventBus(), (RoleAssignmentViewImpl) view);
		
		this.widget = panel;
		widget.setWidget(view.asWidget());
		init();
		
	}
	// Module 3 {
	public void initOsceDaySubView(){
	osceDayTimer = new Timer() {
		
		@Override
		public void run() {
			Collection<String> cookie =Cookies.getCookieNames();
			for(String cook : cookie){
				//Window.alert(Cookies.getCookie(cook));
				Log.info(""+Cookies.getCookie(cook));
			}
			
			//change
			for(int i=0;i<osceDaySubViewImplList.size();i++)
			{
				for(String cook : cookie){
					//Window.alert(Cookies.getCookie(cook));
					Log.info("Cookie Osce Day Id :"+Cookies.getCookie(cook));
					OsceDayProxy osceDayProxy=osceDaySubViewImplList.get(i).getOsceDayProxy();
					OsceDaySubViewImpl osceDaySubViewImpl=osceDaySubViewImplList.get(i);
					
					if(osceDayProxy.getId().toString().trim().equals(Cookies.getCookie(cook).trim()))
					{
						refreshOsceSequences(osceDayProxy, osceDaySubViewImpl);
					}
				}
			}
			//change
		}
	};

	//osceDayTimer.schedule(osMaConstant.OSCEDAYTIMESCHEDULE);
	
	
	//timerMap = new HashMap<String, Object>();
	//Module 3:Assignment D
	
	requests.osceRequestNonRoo().initOsceBySecurity().fire(new OSCEReceiver<Object>() {
		@Override
		public void onSuccess(Object response) {
			Log.info("Osce Changed : "+response);
	
	
	requests.find(semesterProxy.stableId()).with("osces","osces.osce_days","osces.osce_days.osce").fire(new OSCEReceiver<Object>() {

		@Override
		public void onSuccess(Object response) {
			Log.info("Semester Id " + ((SemesterProxy)response).getId());
			Log.info("OSCE Size " + ((SemesterProxy)response).getOsces().size());
			
			semesterProxy=((SemesterProxy)response);
			VerticalPanel osceDaySubViewContainerPanel = view.getOsceDaySubViewContainerPanel();
			boolean isopen =true;
			osceDaySubViewContainerPanel.clear();
			
			Set<OsceProxy> setOsceProxy = semesterProxy.getOsces();
			if(setOsceProxy==null)
			{
				Log.info("No Any OsceProxy Found");
			}
			else{
			
			Log.info(" Total Osces is :" + setOsceProxy.size());
			
			Iterator<OsceProxy> iteratorOsceProxy = setOsceProxy.iterator();
				
			while(iteratorOsceProxy.hasNext()){
				osceProxy=iteratorOsceProxy.next();
					
				List<OsceDayProxy> setOsceDayProxy = osceProxy.getOsce_days();
				
				if(setOsceDayProxy == null){
					Log.info("No OsceDay Found");
				}
				else{
				Log.info("Total OSce Day is : " + setOsceDayProxy.size());
				Iterator<OsceDayProxy> iteratorOSceDayProxy = setOsceDayProxy.iterator();
				
				
				
				while(iteratorOSceDayProxy.hasNext()){
					
					osceDayProxy=iteratorOSceDayProxy.next();
					// Module 3 f {
					if(osceProxy.getOsceStatus()==OsceStatus.OSCE_CLOSED){
						// Module 3 f }
					OsceDaySubViewImpl osceDaySubViewImpl = new OsceDaySubViewImpl();
                                        osceDaySubViewImpl.setOsceProxy(osceProxy);
					osceDaySubViewImpl.setDelegate(spRoleAssignmentActivity);
					osceDaySubViewImpl.setOsceDayProxy(osceDayProxy);
					StudyYears studyYear =osceProxy.getStudyYear();
					String name =semesterProxy.getSemester().name();

					if(studyYear != null && name !=null){
						String header = "" +studyYear +"." + name +" - " + DateTimeFormat.getShortDateFormat().format(osceDayProxy.getOsceDate());
//						osceDaySubViewImpl.simpleDiscloserPanel.getHeaderTextAccessor().setText(header);
						
						osceDaySubViewImpl.getHeaderPanelForTitle(header);
						osceDaySubViewContainerPanel.add(osceDaySubViewImpl);
					}
					else{
						Window.alert("Semester and study year must not empty to show on OSce Day");
					}
					
					
					// Module 3 d {
					
					//register events
					PatientInSemesterSelectedEvent.register(requests.getEventBus(), osceDaySubViewImpl);
					RoleSelectedEvent.register(requests.getEventBus(), osceDaySubViewImpl);
					
					// Module 3 d }
					if(isopen){
						osceDaySubViewImpl.simpleDiscloserPanel.setOpen(isopen);
						isopen=false;
					}
					
					//change
					osceDaySubViewImplList.add(osceDaySubViewImpl);
					Collection<String> cookie =Cookies.getCookieNames();
					
					for(String cook : cookie){
						//Window.alert(Cookies.getCookie(cook));
						Log.info("Cookie Osce Day Id :"+Cookies.getCookie(cook));
						if(osceDayProxy.getId().toString().trim().equals(Cookies.getCookie(cook).trim()))
						{
							osceDaySubViewImpl.simpleDiscloserPanel.setOpen(true);
						}
					}
					//change
					
					//StudyYears studyYear =osceProxy.getStudyYear();
					//String name =semesterProxy.getSemester().name();
					//Date date =osceDayProxy.getOsceDate();
						
					//if(studyYear != null && name !=null){
					//	osceDaySubViewImpl.simpleDiscloserPanel.getHeaderTextAccessor().setText("" +studyYear +"." + name +" - " + DateTimeFormat.getShortDateFormat().format(osceDayProxy.getOsceDate()));
					//	osceDaySubViewContainerPanel.add(osceDaySubViewImpl);
					//}
					//else{
					//	Window.alert("Semester and study year must not empty to show on OSce Day");
					//}
						
							
				}
				
				
				}
				
			}
		 }
		}
			
	}
});
	
		}});
	
	requests.getEventBus().fireEvent(
			new ApplicationLoadingScreenEvent(false));
	
}
@Override
public void discloserPanelOpened(final OsceDayProxy osceDayProxy,final OsceDaySubViewImpl osceDaySubViewImpl) {
	Log.info("OsceDay Proxy " + osceDayProxy.getId());
	//(new Date()).getTime()+ (1000 * 60 * 60 * 24))  new Date((new Date()).getTime() + (1000 * 60 * 60 * 24))
	
	
	if(Cookies.getCookie(osceDayProxy.getId().toString()) == null){
		Date todayDate= new Date();
		Log.info("Date is " + todayDate);
		Date cookieDate =new Date();
		
		cookieDate.setYear(todayDate.getYear());
		cookieDate.setMonth(todayDate.getMonth());
		cookieDate.setDate(todayDate.getDate());
		cookieDate.setHours(23);
		cookieDate.setMinutes(59);
		cookieDate.setSeconds(58);
		Cookies.setCookie(osceDayProxy.getId().toString(),osceDayProxy.getId().toString(),cookieDate);
		Log.info("Cookie Will expire after :" + cookieDate);
		Log.info("Cookie Created for :" + osceDayProxy.getId());
		
	}
		//refreshData(osceDayProxy);
	
		
		
		//timerMap.put(osceDayProxy.getId().toString(),osceDayTimer);
		
		//Module 3:Assignment D[
		//retrieve data for osceDay
		if(osceDayProxy.getOsceSequences() ==null)
		{
			
			refreshOsceSequences(osceDayProxy, osceDaySubViewImpl);
			
		}
		osceDayTimer.scheduleRepeating(osMaConstant.OSCEDAYTIMESCHEDULE);
		//Module 3:Assignment D]
		
}

//Module 3:Assignment D[
public void refreshOsceSequences(OsceDayProxy osceDayProxy,final OsceDaySubViewImpl osceDaySubViewImpl) 
{
	Log.info("refreshOsceSequences: osceDayProxy " +osceDayProxy.getId());
	osceDaySubViewImpl.getSequenceVP().clear();
	requests.osceDayRequest().findOsceDay(osceDayProxy.getId()).with("osceSequences","osceSequences.courses","osceSequences.oscePosts","osceSequences.oscePosts.standardizedRole","osceSequences.oscePosts.patientInRole","osceSequences.oscePosts.patientInRole.patientInSemester","osceSequences.oscePosts.patientInRole.patientInSemester.standardizedPatient").fire(new OSCEReceiver<OsceDayProxy>() {
		
		@Override
		public void onSuccess(OsceDayProxy response) {
			
			createSequences(response,osceDaySubViewImpl);
			
		}
	});
}

public void createSequences(OsceDayProxy osceDayProxy,OsceDaySubViewImpl osceDaySubViewImpl)
{
	requests.getEventBus().fireEvent(
			new ApplicationLoadingScreenEvent(true));
	//create Roles
	
	//modul 3 changes {

		OscePostProxy postProxy=null;
		//modul 3 changes }
		
	Iterator<OsceSequenceProxy> seqIterator=osceDayProxy.getOsceSequences().iterator();
	Log.info("Number Of Sequences : " + osceDayProxy.getOsceSequences().size());
	HorizontalPanel roleHP;
	while(seqIterator.hasNext())
	{
		OsceSequenceProxy sequenceProxy=seqIterator.next();
		
		Label sequenceLbl=new Label(sequenceProxy.getLabel());
		sequenceLbl.setStyleName("sequenceLbl");
		
		Iterator<OscePostProxy> postIterator=sequenceProxy.getOscePosts().iterator();
		 roleHP=new HorizontalPanel();
		 
		 roleHP.add(sequenceLbl);
		 RoleSubView backUpView=new RoleSubViewImpl();
		while(postIterator.hasNext())
		{
			postProxy=postIterator.next();
			
			if((postProxy.getStandardizedRole()!=null && postProxy.getStandardizedRole().getRoleType()!=null) && ( postProxy.getStandardizedRole().getRoleType() == RoleTypes.Simpat || postProxy.getStandardizedRole().getRoleType()==RoleTypes.Statist))
					{
				RoleSubView view=new RoleSubViewImpl();
				view.setBackUpRoleView(backUpView);
				RoleFulfilCriteriaEvent.register(requests.getEventBus(), (RoleSubViewImpl)view);
				RoleSelectedEvent.register(requests.getEventBus(), (RoleSubViewImpl)view);
						view.setPostProxy(postProxy);
						view.setOsceDayProxy(osceDayProxy);
						view.setOsceSequenceProxy(sequenceProxy);
						view.setOsceDaySubViewImpl(osceDaySubViewImpl);
						refreshRoleSubView(view);
						roleHP.setSpacing(10);
						roleHP.add(view);
					}
			//OscePostProxy postProxy=postIterator.next();
			//StandardizedRoleProxy roleProxy=postProxy.getStandardizedRole();
			//if(roleProxy.getRoleType() == RoleTypes.Simpat || roleProxy.getRoleType()==RoleTypes.Statist)
		//	{
			/*	RoleSubView view=new RoleSubViewImpl();
				
				view.setOsceSequenceProxy(sequenceProxy);
				view.setRoleProxy(roleProxy);
				view.setPostProxy(postProxy);
				view.setOsceDayProxy(osceDayProxy);
				view.setDelegate(this);
				view.setOsceDaySubViewImpl(osceDaySubViewImpl);
				if(roleProxy !=null && (roleProxy.getShortName() != null && roleProxy.getRoleType() != null))
				view.getRoleLbl().setText(roleProxy.getShortName() + " " + roleProxy.getRoleType());
				
				RoleSelectedEvent.register(requests.getEventBus(), (RoleSubViewImpl)view);
				
				//create Patient In Role
				Log.info("Number Of Patent In Role "+postProxy.getPatientInRole().size());
				
				Iterator<PatientInRoleProxy> patientInRoleIterator=postProxy.getPatientInRole().iterator();
				while(patientInRoleIterator.hasNext())
				{
					PatientInRoleProxy patientInRoleProxy=patientInRoleIterator.next();
					PatientInRoleSubView patientInRoleView=new PatientInRoleSubViewImpl();
					
					//check if patient in role is first assigned
					boolean isFirstAssigned=false;
					if(!patientInRoleProxy.getIs_backup())
						isFirstAssigned=patientInRoleIsFirstAssigned(sequenceProxy,patientInRoleProxy);
					
					if(isFirstAssigned)
						((PatientInRoleSubViewImpl)patientInRoleView).addStyleName("count-yellow");
					else
						((PatientInRoleSubViewImpl)patientInRoleView).removeStyleName("count-yellow");
					
					patientInRoleView.setPatientInRoleProxy(patientInRoleProxy);
					patientInRoleView.setDelegate(this);
					patientInRoleView.getPatientInRoleLbl().setText(patientInRoleProxy.getPatientInSemester().getStandardizedPatient().getName());
					patientInRoleView.setRoleSubView(view);
					if(patientInRoleProxy.getIs_backup())
					{
						view.getDragController2().makeDraggable(patientInRoleView.asWidget(), patientInRoleView.getPatientInRoleLbl());
						view.getBackUpVP().insert(patientInRoleView, view.getBackUpVP().getWidgetCount());
					}
					else
					{
						view.getDragController1().makeDraggable(patientInRoleView.asWidget(), patientInRoleView.getPatientInRoleLbl());
						view.getPatientInRoleVP().insert(patientInRoleView, view.getPatientInRoleVP().getWidgetCount());
					}
				}
				*/
				
				
				
				//roleHP.setSpacing(10);
				//roleHP.add(view);
				//roleHP.add(new HTML("<hr /> "));
		//	}
			//roleHP.remove(roleHP.getWidgetCount()-1);
		}
		
		//module 3 changes {
		
		RoleFulfilCriteriaEvent.register(requests.getEventBus(), (RoleSubViewImpl)backUpView);
		RoleSelectedEvent.register(requests.getEventBus(), (RoleSubViewImpl)backUpView);
		backUpView.setPostProxy(postProxy);
		backUpView.setDelegate(this);
		backUpView.setOsceDayProxy(osceDayProxy);
		backUpView.setOsceSequenceProxy(sequenceProxy);
		backUpView.setOsceDaySubViewImpl(osceDaySubViewImpl);

		roleHP.setSpacing(10);
		backUpView.getRoleLbl().setText(constants.backupViewHeading());
		backUpView.getCountLbl().removeFromParent();
		backUpView.getbackupLabel().removeFromParent();
		backUpView.getBackUpVP().removeFromParent();
		backUpView.setIsBackupPanel(true);
		roleHP.add(backUpView);
		assignAllBackUpRolesToBackupPanel(postProxy,backUpView);
		
		
		//module 3 changes }
		
		osceDaySubViewImpl.setOsceDayProxy(osceDayProxy);
		osceDaySubViewImpl.getSequenceVP().insert(roleHP, osceDaySubViewImpl.getSequenceVP().getWidgetCount());
		osceDaySubViewImpl.getSequenceVP().insert(new HTML("<hr class='dashed' />"), osceDaySubViewImpl.getSequenceVP().getWidgetCount());
		
	}
	if(osceDaySubViewImpl.getSequenceVP().getWidgetCount()>0)
	osceDaySubViewImpl.getSequenceVP().remove(osceDaySubViewImpl.getSequenceVP().getWidgetCount()-1);
	requests.getEventBus().fireEvent(
			new ApplicationLoadingScreenEvent(false));
}

//modul 3 changes {

public void assignAllBackUpRolesToBackupPanel(OscePostProxy oscePostPrtoxy,RoleSubView roleSubView){
	
	Log.info("Post Proxy is To Assign RoleThat was Backuped :" + oscePostPrtoxy.getId());
	
	Iterator<PatientInRoleProxy> patientInRoleProxyIterator=oscePostPrtoxy.getPatientInRole().iterator();
	
	while(patientInRoleProxyIterator.hasNext())
	{
		PatientInRoleProxy patientInRoleProxy=patientInRoleProxyIterator.next();
		
		if(patientInRoleProxy.getIs_backup()==true){
			PatientInRoleSubView patientInRoleView=new PatientInRoleSubViewImpl();
			patientInRoleView.setPatientInRoleProxy(patientInRoleProxy);
			patientInRoleView.getDeleteButton().removeFromParent();
			patientInRoleView.setDelegate(this);
			patientInRoleView.getPatientInRoleLbl().setText(patientInRoleProxy.getPatientInSemester().getStandardizedPatient().getName());
			patientInRoleView.setRoleSubView(roleSubView);
			roleSubView.getPatientInRoleVP().add(patientInRoleView);
		}
	}
}

//modul 3 changes }

public void createRoleSubView(RoleSubView roleSubView,OscePostProxy postProxy)
{
	requests.getEventBus().fireEvent(
			new ApplicationLoadingScreenEvent(true));
	
	final RoleSubView roleSubView1=roleSubView;
	StandardizedRoleProxy roleProxy=postProxy.getStandardizedRole();
	if(roleProxy.getRoleType() == RoleTypes.Simpat || roleProxy.getRoleType()==RoleTypes.Statist)
	{
	
		/*if(roleSubView==null)
		{
			
			roleSubView = new RoleSubViewImpl();
		}*/
		
		roleSubView.setRoleProxy(roleProxy);
		roleSubView.setPostProxy(postProxy);
		
		roleSubView.setDelegate(this);
		
		if(roleProxy !=null && (roleProxy.getShortName() != null && roleProxy.getRoleType() != null))
		roleSubView.getRoleLbl().setText(roleProxy.getShortName() + " " + roleProxy.getRoleType());
		
		
		
		
		roleSubView.getPatientInRoleVP().clear();
		roleSubView.getBackUpVP().clear();
		//create Patient In Role
		Log.info("Number Of Patent In Role "+postProxy.getPatientInRole().size());
		Iterator<PatientInRoleProxy> patientInRoleProxyIterator=postProxy.getPatientInRole().iterator();
		
		while(patientInRoleProxyIterator.hasNext())
		{
			PatientInRoleProxy patientInRoleProxy=patientInRoleProxyIterator.next();
			
			PatientInRoleSubView patientInRoleView=new PatientInRoleSubViewImpl();
			
			//check if patient in role is first assigned
			boolean isFirstAssigned=false;
			if(!patientInRoleProxy.getIs_backup())
				isFirstAssigned=patientInRoleIsFirstAssigned(roleSubView.getOsceSequenceProxy(),patientInRoleProxy,patientInRoleView);
			
			/*if(isFirstAssigned && patientInRoleProxy.getFit_criteria())
			{
				((PatientInRoleSubViewImpl)patientInRoleView).addStyleName("count-yellow");
				((PatientInRoleSubViewImpl)patientInRoleView).removeStyleName("count-red");
			}
			else
			{
				((PatientInRoleSubViewImpl)patientInRoleView).removeStyleName("count-yellow");
			}*/
			
			patientInRoleView.setPatientInRoleProxy(patientInRoleProxy);
			patientInRoleView.setDelegate(this);
			patientInRoleView.getPatientInRoleLbl().setText(patientInRoleProxy.getPatientInSemester().getStandardizedPatient().getName());
			patientInRoleView.setRoleSubView(roleSubView);
			
			
			boolean isOsceDayPatientInSemester = true;
			Set<OsceDayProxy> osceDayProxies =patientInRoleProxy.getPatientInSemester().getOsceDays();
				Log.info("!!!!OSCE day proxy is : "+osceDayProxy.getId());
				if (osceDayProxies != null) {
					for (Iterator<OsceDayProxy> iterator = osceDayProxies
							.iterator(); iterator.hasNext();) {
						Log.info("osceDayProxy" + osceDayProxy.getId());
						// Module 3 : Change
						OsceDayProxy tempOsceDayProxy = (OsceDayProxy) iterator
								.next();

						if (tempOsceDayProxy.getId() == osceDayProxy.getId()) {
							// Module 3 : Change
							isOsceDayPatientInSemester = false;
							break;
						}

					}
				}
			
			if(!patientInRoleProxy.getFit_criteria() || isOsceDayPatientInSemester)
			{
				((PatientInRoleSubViewImpl)patientInRoleView).addStyleName("count-red");
				((PatientInRoleSubViewImpl)patientInRoleView).removeStyleName("count-yellow");
			}
			else
				((PatientInRoleSubViewImpl)patientInRoleView).removeStyleName("count-red");
			if(patientInRoleProxy.getIs_backup())
			{
				roleSubView.getDragController2().makeDraggable(patientInRoleView.asWidget(), patientInRoleView.getPatientInRoleLbl());
				roleSubView.getBackUpVP().insert(patientInRoleView, roleSubView.getBackUpVP().getWidgetCount());
			}
			else
			{
				roleSubView.getDragController1().makeDraggable(patientInRoleView.asWidget(), patientInRoleView.getPatientInRoleLbl());
				roleSubView.getPatientInRoleVP().insert(patientInRoleView, roleSubView.getPatientInRoleVP().getWidgetCount());
			}
		}

		
		
		
		roleSubView.refreshCountLabel();
		requests.getEventBus().fireEvent(
				new ApplicationLoadingScreenEvent(false));
	}
}

//change
public void refreshAllRoleSubeView(OsceDaySubViewImpl osceDaySubViewImpl,OsceSequenceProxy osceSequenceProxy)
{
	requests.getEventBus().fireEvent(
			new ApplicationLoadingScreenEvent(true));
	for(int i=0;i<osceDaySubViewImpl.getSequenceVP().getWidgetCount();i++)
	{
		if(i%2==0)
		for(int j=1;j<((HorizontalPanel)osceDaySubViewImpl.getSequenceVP().getWidget(i)).getWidgetCount();j++)
		{
			Log.info("refreshAllRoleSubeView :"+((HorizontalPanel)osceDaySubViewImpl.getSequenceVP().getWidget(i)).getWidget(j));
			RoleSubView roleSubView=((RoleSubView)((HorizontalPanel)osceDaySubViewImpl.getSequenceVP().getWidget(i)).getWidget(j));
			if(roleSubView.getOsceSequenceProxy().getId()==osceSequenceProxy.getId())
			{
				refreshRoleSubView(roleSubView);
			}
			else
				break;
		}
	}
	requests.getEventBus().fireEvent(
			new ApplicationLoadingScreenEvent(false));
}


//refresh RoleSubView
public void refreshRoleSubView(final RoleSubView roleSubView)
{
	
	//modul 3 changes {
	
		if(roleSubView.getIsBackupPanel()==true){
			return;
		}
		
		//modul 3 changes }
		
	requests.getEventBus().fireEvent(
			new ApplicationLoadingScreenEvent(true));
	OscePostProxy postProxy=roleSubView.getPostProxy();
	requests.oscePostRequest().findOscePost(postProxy.getId()).with("patientInRole","standardizedRole","standardizedRole.advancedSearchCriteria","patientInRole.patientInSemester","patientInRole.patientInSemester.standardizedPatient").fire(new OSCEReceiver<OscePostProxy>() {

		@Override
		public void onSuccess(OscePostProxy response) {
			if(response.getStandardizedRole().getRoleType() == RoleTypes.Simpat || response.getStandardizedRole().getRoleType()==RoleTypes.Statist)
			{
				createRoleSubView(roleSubView,response);
			}
			
		}
	});
	requests.getEventBus().fireEvent(
			new ApplicationLoadingScreenEvent(false));
}


public boolean patientInRoleIsFirstAssigned(OsceSequenceProxy sequenceProxy,final PatientInRoleProxy patientInRoleProxy,final PatientInRoleSubView patientInRoleSubView)
{
	isAssignedFirst=false;
	Log.info("patientInRoleIsFirstAssigned : "+patientInRoleProxy.getPatientInSemester().getStandardizedPatient().getName());
	requests.osceSequenceRequest().findOsceSequence(sequenceProxy.getId()).with("courses","oscePosts","oscePosts.standardizedRole","oscePosts.patientInRole","oscePosts.patientInRole.patientInSemester","oscePosts.patientInRole.patientInSemester.standardizedPatient").fire(new OSCEReceiver<OsceSequenceProxy>() {
		
		
		
		@Override
		public void onSuccess(OsceSequenceProxy response) {
			Iterator<OscePostProxy> postIterator=response.getOscePosts().iterator();
	int count=0;
	while(postIterator.hasNext())
	{
		OscePostProxy postProxy=postIterator.next();
		Iterator<PatientInRoleProxy> patientInRoleIterator=postProxy.getPatientInRole().iterator();
		while(patientInRoleIterator.hasNext())
		{
			PatientInRoleProxy proxy=patientInRoleIterator.next();
					Log.info("patientInRoleIsFirstAssigned in sequence: "+proxy.getPatientInSemester().getStandardizedPatient().getName());
				if(patientInRoleProxy.getPatientInSemester().getStandardizedPatient().getId()==proxy.getPatientInSemester().getStandardizedPatient().getId() && !proxy.getIs_backup())
					count++;
				
				if(count>=2)
				{
					Log.info("Count patientInRoleIsFirstAssigned  :" + count);
							isAssignedFirst=false;
							break;
				}
			
		}
	}
	Log.info("Count patientInRoleIsFirstAssigned  :" + count);
			if(count==1)
				isAssignedFirst= true;
			
			if(isAssignedFirst && patientInRoleProxy.getFit_criteria())
			{
				((PatientInRoleSubViewImpl)patientInRoleSubView).addStyleName("count-yellow");
				((PatientInRoleSubViewImpl)patientInRoleSubView).removeStyleName("count-red");
			}
			else
			{
				((PatientInRoleSubViewImpl)patientInRoleSubView).removeStyleName("count-yellow");
			}
			
		}
	});
	
	
	return isAssignedFirst;
	
}
//change[
public void roleSelected(RoleSubView roleSubView)
{
	
	Log.info("roleSelected");
	view.getDataTable().setNavigationButtonEnable(true);
	oscePostProxy=roleSubView.getPostProxy();
	roleSubViewSelected=(RoleSubViewImpl)roleSubView;
	osceDayProxy=roleSubView.getOsceDayProxy();
	requests.getEventBus().fireEvent(new RoleSelectedEvent(roleSubView.getRoleProxy(), roleSubView.getOsceDayProxy()));	
	
	checkFitCriteria(roleSubView);
	osceDayTimer.scheduleRepeating(osMaConstant.OSCEDAYTIMESCHEDULE);
		
	
}

public void checkFitCriteria(RoleSubView view)
{
	List<AdvancedSearchCriteriaProxy> listAdvanceSearchCirteria=new ArrayList<AdvancedSearchCriteriaProxy>();
	
	listAdvanceSearchCirteria.addAll(view.getRoleProxy().getAdvancedSearchCriteria());
	
	for(int i=0;i<view.getPatientInRoleVP().getWidgetCount();i++)
	{
		final PatientInSemesterProxy semester=((PatientInRoleSubView)view.getPatientInRoleVP().getWidget(i)).getPatientInRoleProxy().getPatientInSemester();
		final PatientInRoleProxy patientInRoleProxy=((PatientInRoleSubView)view.getPatientInRoleVP().getWidget(i)).getPatientInRoleProxy();
		Log.info("Semester Id :" + semester.getId());
		if(listAdvanceSearchCirteria.size()!=0)
		Log.info("Advance Search : " + listAdvanceSearchCirteria.get(0).getId());
		
		requests.patientInSemesterRequestNonRoo().findPatientInSemesterByAdvancedCriteria(semesterProxy.getId(),listAdvanceSearchCirteria).with("standardizedPatient", "semester", "trainings",
				"osceDays.osce",
				"patientInRole.oscePost.standardizedRole").fire(new OSCEReceiver<List<PatientInSemesterProxy>>() {

			@Override
			public void onSuccess(List<PatientInSemesterProxy> response) {
				Log.info("" +response);
				boolean flag=false;
				if(response==null)
				{
					Log.info(" onSuccess response null");
				}
				else
				for(PatientInSemesterProxy p:response)
				{
					if(p.getStandardizedPatient().getId()==semester.getStandardizedPatient().getId() && !patientInRoleProxy.getFit_criteria())
					{
						//setFitCriteria(patientInRoleProxy,true);
						flag=true;
						break;
					}
					/*else if(p.getStandardizedPatient().getId()!=semester.getStandardizedPatient().getId() && patientInRoleProxy.getFit_criteria())
					{
						setFitCriteria(patientInRoleProxy,false);
						break;
					}*/
				}
				if(flag && !patientInRoleProxy.getFit_criteria())
				{
					setFitCriteria(patientInRoleProxy,true);
				}
				else if(!flag && patientInRoleProxy.getFit_criteria())
				{
					setFitCriteria(patientInRoleProxy,false);
				}
				
			}
		});
	}
}
//change]
public void setFitCriteria(PatientInRoleProxy patientInRoleProxy,boolean fit_criteria)
{
	Log.info("setFitCriteria :");
	PatientInRoleRequest patientInRoleRequest=requests.patientInRoleRequest();
	patientInRoleProxy=patientInRoleRequest.edit(patientInRoleProxy);
	patientInRoleProxy.setFit_criteria(fit_criteria);
	patientInRoleRequest.persist().using(patientInRoleProxy).fire(new OSCEReceiver<Void>() {

		@Override
		public void onSuccess(Void response) {
			Log.info("setFitCriteria success");
			
		}
	});
}
public void editBackUpFlag(final RoleSubView view,final PatientInRoleSubView patientInRoleSubView, PatientInRoleProxy proxy,final boolean isBackUp)
{
	final PatientInRoleProxy patientInRoleProxy=proxy;
	PatientInRoleRequest patientInRoleRequest=requests.patientInRoleRequest();
	
	proxy=patientInRoleRequest.edit(proxy);
	proxy.setIs_backup(isBackUp);
	
	patientInRoleRequest.persist().using(proxy).fire(new OSCEReceiver<Void>() {

		@Override
		public void onSuccess(Void response) {
			Log.info("editBackUpFlag : onSuccess");
		
//modul 3 changes {
			
			if(isBackUp)
			{
				//add patient in Role Sub View To Back Up
				Log.info("Assigning New Roles As Backupo to Backup panel");
				PatientInRoleSubView patientInRoleView=new PatientInRoleSubViewImpl();
				patientInRoleView.setPatientInRoleProxy(patientInRoleProxy);
				patientInRoleView.getDeleteButton().removeFromParent();
				patientInRoleView.setDelegate(spRoleAssignmentActivity);
				patientInRoleView.getPatientInRoleLbl().setText(patientInRoleProxy.getPatientInSemester().getStandardizedPatient().getName());
				patientInRoleView.setRoleSubView(view);
				view.getBackUpRoleView().getPatientInRoleVP().add(patientInRoleView);
				
			}
			else
			{
				Log.info("Removing Role From Backup Panel");
				Log.info("patientInRoleProxy Id :" + patientInRoleProxy.getId());
						
				deleteBackupRoles(view, patientInRoleProxy);
			}
			
			//modul 3 changes }
			//refreshOsceSequences(view.getOsceDayProxy(), view.getOsceDaySubViewImpl());
			refreshAllRoleSubeView(patientInRoleSubView.getRoleSubView().getOsceDaySubViewImpl(), patientInRoleSubView.getRoleSubView().getOsceSequenceProxy());
		}
	});
	osceDayTimer.scheduleRepeating(osMaConstant.OSCEDAYTIMESCHEDULE);
}
//change {

public void deletePatientInRole(final PatientInRoleSubViewImpl patientInRoleView)
{
	
	Log.info("deletePatientInRole PatientInRoleProxy: " + patientInRoleView.getPatientInRoleProxy().getId());
	
	
	requests.patientInRoleRequestNonRoo().deletePatientInRole(patientInRoleView.getPatientInRoleProxy()).fire(new OSCEReceiver<Boolean>() {

		@Override
		public void onSuccess(Boolean response) {
			Log.info("PatientInRole Deleted Successfully :" + response);
			if(response==true){
				//refreshOsceSequences(patientInRoleView.getRoleSubView().getOsceDayProxy(), patientInRoleView.getRoleSubView().getOsceDaySubViewImpl());
				refreshAllRoleSubeView(patientInRoleView.getRoleSubView().getOsceDaySubViewImpl(), patientInRoleView.getRoleSubView().getOsceSequenceProxy());
				initPatientInSemester(true,false);
				
				//modul 3 changes {
				
				Log.info("Removing Role From Backup Panel");
				PatientInRoleProxy patientInRoleProxy= patientInRoleView.getPatientInRoleProxy();
				Log.info("patientInRoleProxy Id :" + patientInRoleProxy.getId());
				deleteBackupRoles(patientInRoleView.getRoleSubView(), patientInRoleProxy);
			}
			
		}
	});
	
	osceDayTimer.scheduleRepeating(osMaConstant.OSCEDAYTIMESCHEDULE);
}	

//change }
//Module 3:Assignment D]

//modul 3 changes {

public void deleteBackupRoles(RoleSubView view,PatientInRoleProxy patientInRoleProxy){
	
	RoleSubView backupRoleView=view.getBackUpRoleView();
	int totalBackupRoles=view.getBackUpRoleView().getPatientInRoleVP().getWidgetCount();
	for(int count=0;count<totalBackupRoles;count++){
		Log.info("patientInRoleProxy Id  backup :" +((PatientInRoleSubView)backupRoleView.getPatientInRoleVP().getWidget(count)).getPatientInRoleProxy().getId());
		if((long)((PatientInRoleSubView)backupRoleView.getPatientInRoleVP().getWidget(count)).getPatientInRoleProxy().getId()==(long)patientInRoleProxy.getId()){
			((PatientInRoleSubViewImpl)backupRoleView.getPatientInRoleVP().getWidget(count)).removeFromParent();
			break;
		}
	}
}
//modul 3 changes }


@Override
public void discloserPanelClosed(OsceDayProxy osceDayProxy,OsceDaySubViewImpl osceDaySubViewImpl) {
//			Log.info("Cookie Removed for :" +osceDayProxy.getId());
			Cookies.removeCookie(osceDayProxy.getId().toString());
			//timerMap.remove(osceDayProxy.getId().toString());
	
}




// Module 3 }
	/**
	 * go to another place
	 * 
	 * @param place
	 *            the place to go to
	 */
	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public void onAcceptedClick(
			final PatientInSemesterData patientInSemesterData) {

		requests.find(
				patientInSemesterData.getPatientInSemesterProxy().stableId())
				.fire(new OSCEReceiver<Object>() {

					@Override
					public void onSuccess(Object arg0) {

						PatientInSemesterRequest patientInSemesterRequest = requests
								.patientInSemesterRequest();
						PatientInSemesterProxy tempPatientInSemesterProxy = patientInSemesterRequest
								.edit((PatientInSemesterProxy) arg0);
						final boolean acceptedFlag = !(tempPatientInSemesterProxy
								.getAccepted().booleanValue());
						tempPatientInSemesterProxy.setAccepted(acceptedFlag);

						patientInSemesterRequest.persist()
								.using(tempPatientInSemesterProxy)
								.fire(new OSCEReceiver<Void>() {

									@Override
									public void onSuccess(Void response) {
										Log.info("In Success");

										requests.find(
												patientInSemesterData
														.getPatientInSemesterProxy()
														.stableId())
												.with("standardizedPatient",
														"semester",
														"trainings",
														"osceDays.osce",
														"patientInRole.oscePost.standardizedRole")
												.fire(new OSCEReceiver<Object>() {

													@Override
													public void onSuccess(
															Object arg0) {
														patientInSemesterData
																.setPatientInSemesterProxy((PatientInSemesterProxy) arg0);
														patientInSemesterData
																.setAcceptedImage();
													}
												});
										// init();

									}

									@Override
									public void onFailure(ServerFailure error) {
										super.onFailure(error);

										Log.info(error.getMessage());
										System.out.println(error
												.getStackTraceString());
									}

								});
					}
				});

	}



	private void init() {

		requests.getEventBus().fireEvent(
				new ApplicationLoadingScreenEvent(true));
		initPatientInSemester(true,false);	
		initOsceDaySubView();
	}

	private void initPatientInSemester(final boolean isFirstData,final boolean resetTable) {

		requests.patientInSemesterRequest()
				.findAllPatientInSemesters()
				.with("standardizedPatient", "semester", "trainings",
						"osceDays.osce",
						"patientInRole.oscePost.standardizedRole")
				.fire(new OSCEReceiver<List<PatientInSemesterProxy>>() {
					@Override
					public void onSuccess(
							List<PatientInSemesterProxy> patientInSemesterProxies) {
						// Module 3 : Assignment E : Start
						//  
						allPatientInSemesterProxies = patientInSemesterProxies;
						// Module 3 : Assignment E : Stop
						//  
						System.out.println("response : "
								+ patientInSemesterProxies.size());
						Log.info("response : "
								+ patientInSemesterProxies.size());
						initPatientInSemesterData(patientInSemesterProxies,!resetTable);
						if (isFirstData || resetTable) {
							view.getDataTable()
									.setNavigationButtonEnable(false);
						}
						// Module 3 : Assignment E : Stop
						//  
					}
				});
		if (isFirstData) {
			view.getAdvancedSearchCriteriaTable().setRowCount(0, true);
			view.getAdvancedSearchCriteriaTable().setRowData(0,
					new ArrayList<AdvancedSearchCriteriaProxy>());
		}
		if(resetTable){
			refreshPatientInSemesterTable();
		}

	}

public void initPatientInSemesterData(
			List<PatientInSemesterProxy> patientInSemesterProxies,boolean setDataInTable) {
		if (patientInSemesterProxies != null
				&& patientInSemesterProxies.size() >= 0) {
			this.patientInSemesterProxies = patientInSemesterProxies;

			Iterator<PatientInSemesterProxy> iterator = patientInSemesterProxies
					.iterator();
			PatientInSemesterProxy patientInSemesterProxy;
			patientInSemesterDataList = new ArrayList<PatientInSemesterData>();

			while (iterator.hasNext()) {
				patientInSemesterProxy = iterator.next();

				// Log.info("IsAccepted : " +
				// patientInSemesterProxy.getAccepted());
				// Log.info("getPatientInRole : "
				// + patientInSemesterProxy.getPatientInRole().size());
				// Log.info("getStandardizedPatient().getPreName() : "
				// + patientInSemesterProxy.getStandardizedPatient());

				Log.info("semesterProxy.getId()" + semesterProxy.getId());
				if (patientInSemesterProxy.getSemester() != null) {
					Log.info("patientInSemesterProxy.getId()"+ patientInSemesterProxy.getSemester().getId());
				} 
				else 
				{
					Log.info("semesterProxy is null ...");
				}
				if (semesterProxy.getId() == patientInSemesterProxy	.getSemester().getId()) {
					patientInSemesterDataList.add(new PatientInSemesterData(patientInSemesterProxy, spRoleAssignmentActivity));
				}

			}
			// patientInSemesterProxies = response;

			if(setDataInTable)
			view.setData(patientInSemesterDataList);
			Log.info("PatientInSemesterProxy Size : "
					+ patientInSemesterProxies.size());

			//Change  
		}else{
			this.patientInSemesterProxies = new  ArrayList<PatientInSemesterProxy>();
			//Change  
		}

	}

	@Override
	public void onAddManuallyClicked() {
		requests.standardizedPatientRequest().findAllStandardizedPatients()
				.fire(new OSCEReceiver<List<StandardizedPatientProxy>>() {
					@Override
					public void onSuccess(
							List<StandardizedPatientProxy> response) {
						// Log.info("~Success Call....");
						// Log.info("~REFRESH SUGGESION BOX DATA"
						// + response.size());

						Iterator<StandardizedPatientProxy> SProxyIterator = response
								.iterator();
						if(patientInSemesterProxies != null ){
						Iterator<PatientInSemesterProxy> patientInsemesterIterator = patientInSemesterProxies
								.iterator();
						PatientInSemesterProxy patientInSemesterProxy;
						StandardizedPatientProxy standardizedPatientProxy;
						boolean addPatientInSemester;

						List<StandardizedPatientProxy> tempStandardizedPatientProxies = new ArrayList<StandardizedPatientProxy>();
						while (SProxyIterator.hasNext()) {

							standardizedPatientProxy = SProxyIterator.next();
							patientInsemesterIterator = patientInSemesterProxies
									.iterator();
							addPatientInSemester = true;
							while (patientInsemesterIterator.hasNext()) {
								patientInSemesterProxy = patientInsemesterIterator
										.next();
								if ((patientInSemesterProxy
										.getStandardizedPatient().getId() == standardizedPatientProxy
										.getId())
										&& patientInSemesterProxy.getSemester()
												.getId() == semesterProxy
												.getId()) {
									addPatientInSemester = false;
									break;
								}

							}
							if (addPatientInSemester) {
								tempStandardizedPatientProxies
										.add(standardizedPatientProxy);
							}

						}

						manualStdPatientInSemesterAssignmentPopupViewImpl = new ManualStandardizedPatientInSemesterAssignmentPopupViewImpl();

						manualStdPatientInSemesterAssignmentPopupViewImpl
								.setDetails(tempStandardizedPatientProxies,
										spRoleAssignmentActivity,
										view.getAddManuallyBtn());

					}}
				});

	}

	@Override
	public void onStandizedPatientAddBtnClick(StandardizedPatientProxy standardizedPatientProxy) 
	{
	//	Log.info("Call onStandizedPatientAddBtnClick");
		requests.getEventBus().fireEvent(
				new ApplicationLoadingScreenEvent(true));
		
		if(standardizedPatientProxy==null)
		{
			return;
		}
		Log.info("StandardizedPatientProxy " + standardizedPatientProxy.getName());
		PatientInSemesterRequest patientInSemesterRequest = requests.patientInSemesterRequest();
		PatientInSemesterProxy patientInSemesterProxy = patientInSemesterRequest.create(PatientInSemesterProxy.class);

		patientInSemesterProxy.setSemester(semesterProxy);
		patientInSemesterProxy.setStandardizedPatient(standardizedPatientProxy);
		patientInSemesterProxy.setAccepted(false);

		Log.info("Map Size: " + manualStdPatientInSemesterAssignmentPopupViewImpl.getPatientInSemesterMap().size());
		patientInSemesterRequest.persist().using(patientInSemesterProxy).fire(new OSCEReceiver<Void>(manualStdPatientInSemesterAssignmentPopupViewImpl.getPatientInSemesterMap()) 
		{

					@Override
					public void onSuccess(Void arg0) {
						Log.info("Value saved successfully");
//						initPatientInSemester(false,false);
						initPatientInSemester(true,false);
						manualStdPatientInSemesterAssignmentPopupViewImpl
								.hide();
						requests.getEventBus().fireEvent(
								new ApplicationLoadingScreenEvent(false));
					}
				});

	}

	@Override
	public void onDetailViewClicked(
			final PatientInSemesterData patientInSemesterData) {

		requests.getEventBus().fireEvent(
				new ApplicationLoadingScreenEvent(true));

		if (patientInSemesterData.getPatientInSemesterProxy().getAccepted()) {
//		boolean addOsceDayPatientInSemester = true;
//		Set<OsceDayProxy> osceDayProxies = patientInSemesterData
//				.getPatientInSemesterProxy().getOsceDays();
//			Log.info("!!!!OSCE day proxy is : "+osceDayProxy.getId());
//		for (Iterator<OsceDayProxy> iterator = osceDayProxies.iterator(); iterator
//				.hasNext();) {
//			Log.info("osceDayProxy"+osceDayProxy.getId());
//			//Module 3 :   Change
//			OsceDayProxy tempOsceDayProxy = (OsceDayProxy) iterator.next();
//
//			if (tempOsceDayProxy.getId() == osceDayProxy.getId()) {
//				//Module 3 :   Change
//				addOsceDayPatientInSemester = false;
//				break;
//			}
//
//		}
		
		
//		if (addOsceDayPatientInSemester) {
//
//			PatientInSemesterProxy editPatientInSemesterProxy = patientInSemesterData
//					.getPatientInSemesterProxy();
//
//			PatientInSemesterRequest patientInSemesterRequest = requests
//					.patientInSemesterRequest();
//			editPatientInSemesterProxy = patientInSemesterRequest
//					.edit(editPatientInSemesterProxy);
//
//			editPatientInSemesterProxy.getOsceDays().add(getOsceDayProxy());
//			patientInSemesterRequest.persist()
//					.using(editPatientInSemesterProxy)
//					.fire(new OSCEReceiver<Void>() {
//
//						@Override
//						public void onSuccess(Void response) {
//							requests.find(
//									patientInSemesterData
//											.getPatientInSemesterProxy()
//											.stableId())
//									.with("standardizedPatient", "semester",
//											"trainings", "osceDays",
//											"patientInRole.oscePost","patientInRole.oscePost.standardizedRole")
//									.fire(new OSCEReceiver<Object>() {
//
//										@Override
//										public void onSuccess(
//												Object patientInSemesterProxy) {
//
//											patientInSemesterData
//													.setPatientInSemesterProxy((PatientInSemesterProxy) patientInSemesterProxy);
//											System.out
//											.println("patientInRoleProxy saved successfully" + ((PatientInSemesterProxy) patientInSemesterProxy).getOsceDays().size());
//											firePatientInSemesterSelectedEvent((PatientInSemesterProxy) patientInSemesterProxy);
//										}
//									});
//						}
//
//					});
//		} else 
            {
			System.out
			.println("patientInRoleProxy saved successfully" + patientInSemesterData
					.getPatientInSemesterProxy().getOsceDays().size());
			firePatientInSemesterSelectedEvent(patientInSemesterData
					.getPatientInSemesterProxy());
		}
	} else {
			MessageConfirmationDialogBox msg=new MessageConfirmationDialogBox("Warning");
			msg.showConfirmationDialog(constants.patientIsNotAccepted());
			
		}
		requests.getEventBus().fireEvent(
				new ApplicationLoadingScreenEvent(false));

	}

	
	private void onPersistPatientInRole(final PatientInSemesterProxy patientInSemesterProxy){
		// change {
		
				requests.patientInRoleRequestNonRoo().getTotalTimePatientAssignInRole(roleSelectedInOsceDay.getId(), patientInSemesterProxy.getId()).fire( new OSCEReceiver<Integer>() {

					@Override
					public void onSuccess(Integer response) {

						Log.info("Total Times Role Assifn Is :" + response);
						
						if(response > 0){
							assignPatientInRoleNormally(patientInSemesterProxy);
						}
						else if(response==0){
							assignPatientInRoleWithOnePostNull(patientInSemesterProxy);
						}
					}
				});
				
		}

			private void assignPatientInRoleWithOnePostNull(final PatientInSemesterProxy patientInSemesterProxy){
				PatientInRoleRequest patientInRoleRequest = requests.patientInRoleRequest();
				PatientInRoleProxy patientInRoleProxy = patientInRoleRequest.create(PatientInRoleProxy.class);

				patientInRoleProxy.setPatientInSemester(patientInSemesterProxy);
				patientInRoleProxy.setOscePost(oscePostProxy);
				Log.info("isPatientInSemesterFulfill is " + isPatientInSemesterFulfill);
				patientInRoleProxy.setFit_criteria(isPatientInSemesterFulfill);
				patientInRoleProxy.setIs_backup(false);

				patientInRoleRequest.persist().using(patientInRoleProxy).fire(new OSCEReceiver<Void>() {

							@Override
							public void onSuccess(Void arg0) {
								System.out.println("patientInRoleProxy saved successfully with Given Post and sem :" + patientInSemesterProxy);
								
								PatientInRoleRequest patientInRoleRequest2 = requests.patientInRoleRequest();
								PatientInRoleProxy patientInRoleProxy2 = patientInRoleRequest2.create(PatientInRoleProxy.class);

								patientInRoleProxy2.setPatientInSemester(patientInSemesterProxy);
								patientInRoleProxy2.setOscePost(null);
								Log.info("isPatientInSemesterFulfill is " + isPatientInSemesterFulfill);
								patientInRoleProxy2.setFit_criteria(isPatientInSemesterFulfill);
								patientInRoleProxy2.setIs_backup(false);

								patientInRoleRequest2.persist().using(patientInRoleProxy2).fire( new OSCEReceiver<Void>() {

									@Override
									public void onSuccess(Void response) {
										
											System.out.println("patientInRoleProxy saved successfully with Post NULL and sem :" + patientInSemesterProxy);
											
											requests.getEventBus().fireEvent(new PatientInSemesterSelectedEvent(patientInSemesterProxy,patientInSemesterProxy.getOsceDays()));
											// refreshRoleSubView(roleSubViewSelected);
											refreshAllRoleSubeView(roleSubViewSelected.getOsceDaySubViewImpl(),	roleSubViewSelected.getOsceSequenceProxy());
											//For reload whole table pass true , false else reload only selected patient pass false , true
											//initPatientInSemester(false,true);		
											initPatientInSemester(true,false);
							
											view.getDataTable().setNavigationButtonEnable(false);
							
										}
								});

						   	}
						});

						
						
		}
			private void assignPatientInRoleNormally(final PatientInSemesterProxy patientInSemesterProxy){
				
				PatientInRoleRequest patientInRoleRequest = requests.patientInRoleRequest();
				PatientInRoleProxy patientInRoleProxy = patientInRoleRequest.create(PatientInRoleProxy.class);

				patientInRoleProxy.setPatientInSemester(patientInSemesterProxy);
				patientInRoleProxy.setOscePost(oscePostProxy);
				Log.info("isPatientInSemesterFulfill is " + isPatientInSemesterFulfill);
				patientInRoleProxy.setFit_criteria(isPatientInSemesterFulfill);
				patientInRoleProxy.setIs_backup(false);

				patientInRoleRequest.persist().using(patientInRoleProxy).fire(new OSCEReceiver<Void>() {

							@Override
							public void onSuccess(Void arg0) {
								System.out.println("patientInRoleProxy saved successfully"+ patientInSemesterProxy.getOsceDays().size());
								requests.getEventBus().fireEvent(new PatientInSemesterSelectedEvent(patientInSemesterProxy,patientInSemesterProxy.getOsceDays()));
								// refreshRoleSubView(roleSubViewSelected);
								refreshAllRoleSubeView(roleSubViewSelected.getOsceDaySubViewImpl(),roleSubViewSelected.getOsceSequenceProxy());
								//For reload whole table pass true , false else reload only selected patient pass false , true
//								initPatientInSemester(false,true);		
								initPatientInSemester(true,false);
								
								view.getDataTable().setNavigationButtonEnable(
												false);
							}

						});
			}
			
			// change }	
	
	public void firePatientInSemesterSelectedEvent(
			final PatientInSemesterProxy patientInSemesterProxy) {

		boolean addPatientInRole = true;
		// Module 3 :   Change
		if (oscePostProxy != null && oscePostProxy.getPatientInRole() != null) {
			// Module 3 :   Change
			Set<PatientInRoleProxy> patientInRoleProxies = oscePostProxy
					.getPatientInRole();
			for (Iterator<PatientInRoleProxy> iterator = patientInRoleProxies
					.iterator(); iterator.hasNext();) {
				PatientInRoleProxy patientInRoleProxy = (PatientInRoleProxy) iterator
						.next();

				if (patientInRoleProxy.getPatientInSemester().getId() == patientInSemesterProxy
						.getId()) {
					addPatientInRole = false;
					break;
				}

			}
			if (addPatientInRole) {
				// Module 3 : Assignment E : Start
				//  
				isPatientInSemesterFulfill = false;

				if (roleSubViewSelected.getRoleProxy() != null
						&& roleSubViewSelected.getRoleProxy()
								.getAdvancedSearchCriteria() != null
						&& roleSubViewSelected.getRoleProxy()
								.getAdvancedSearchCriteria().size() > 0) {

					ArrayList<AdvancedSearchCriteriaProxy> advancedSearchCriteriaProxies = new ArrayList<AdvancedSearchCriteriaProxy>(
							roleSubViewSelected.getRoleProxy()
									.getAdvancedSearchCriteria());

					requests.patientInSemesterRequestNonRoo()
							.findPatientInSemesterByAdvancedCriteria(
									semesterProxy.getId(),
									advancedSearchCriteriaProxies)
							.fire(new OSCEReceiver<List<PatientInSemesterProxy>>() {
								@Override
								public void onSuccess(
										List<PatientInSemesterProxy> patientInSemesterProxies) {

//								//	Log.info("patientInSemesterProxies"
//								//			+ patientInSemesterProxies.size());
									if (patientInSemesterProxies != null
											&& patientInSemesterProxies.size() > 0) {
										Log.info("Return from isRoleCriteriaFulfill");
										// return;

										for (PatientInSemesterProxy tempPatientInSemesterProxy : patientInSemesterProxies) {
											if (tempPatientInSemesterProxy
													.getId() == patientInSemesterProxy
													.getId()) {
												isPatientInSemesterFulfill = true;
												Log.info("isRoleCriteriaFulfill : true");
												break;
											}
										}
									}

									onPersistPatientInRole(patientInSemesterProxy);

								}
							});
				} else {
					onPersistPatientInRole(patientInSemesterProxy);
				}

				// Module 3 : Assignment E : Stop
				//  
			} else {
				view.getDataTable().setNavigationButtonEnable(false);
				MessageConfirmationDialogBox msg = new MessageConfirmationDialogBox(
						"Warning");
				msg.showConfirmationDialog(constants.patientAlreadyAssigned());
			}
			// Module 3 :   Change
		} else {
			view.getDataTable().setNavigationButtonEnable(false);
			MessageConfirmationDialogBox msg = new MessageConfirmationDialogBox(
					"Warning");
			msg.showConfirmationDialog(constants.pleaseSelectRole());
		}
		// Module 3 :   Change
		// init();
	}

	@Override
	public String onAdvancedSearchCriteriaClicked(
			AdvancedSearchCriteriaProxy advancedSearchCriteriaProxy) {

		// if
		// (advancedSearchCriteriaProxies.contains(advancedSearchCriteriaProxy))
		{
			// Log.info("!!!Size of advancedSearchCriteriaProxies"
			// + advancedSearchCriteriaProxies.size());
			// Log.info("!!!Size of advanceSearchCriteriaList"
			// + advanceSearchCriteriaList.size());

			int i = advancedSearchCriteriaProxies
					.indexOf(advancedSearchCriteriaProxy);
			// Log.info("i : " + i);
			//
			// Log.info("advanceSearchCriteriaList Value "
			// + advanceSearchCriteriaList.get(i).booleanValue());
			advanceSearchCriteriaList.set(i,
					!(advanceSearchCriteriaList.get(i).booleanValue()));
			// Log.info("advanceSearchCriteriaList Value "
			// + advanceSearchCriteriaList.get(i).booleanValue());
			refreshPatientInSemesterTable();
			return ((advanceSearchCriteriaList.get(i).booleanValue()) ? OsMaConstant.CHECK_ICON
					.asString() : OsMaConstant.UNCHECK_ICON.asString());
		}

	}

	public void refreshPatientInSemesterTable() {
		List<AdvancedSearchCriteriaProxy> searchCriteria = new ArrayList<AdvancedSearchCriteriaProxy>();

		for (int i = 0; i < advanceSearchCriteriaList.size(); i++) {
			if (advanceSearchCriteriaList.get(i)) {
				searchCriteria.add(advancedSearchCriteriaProxies.get(i));
			}

		}

		final OSCEReceiver<List<PatientInSemesterProxy>> callback = new OSCEReceiver<List<PatientInSemesterProxy>>() {
			@Override
			public void onSuccess(
					List<PatientInSemesterProxy> patientInSemesterProxies) {
				
//				Log.info("patientInSemesterProxies" + patientInSemesterProxies.size());
				if (patientInSemesterProxies == null
						|| patientInSemesterProxies.size() == 0) {
					view.setData(new ArrayList<PatientInSemesterData>());
				}
				initPatientInSemesterData(patientInSemesterProxies,true);

			}
		};
//		if (searchCriteria != null && searchCriteria.size() > 0) {
			// Log.info("searchCriteria Id" +searchCriteria.get(0).getId());

			requests.patientInSemesterRequestNonRoo()
					.findPatientInSemesterByAdvancedCriteria(
							semesterProxy.getId(), searchCriteria)
					.with("standardizedPatient", "semester", "trainings",
							"osceDays.osce",
							"patientInRole.oscePost.standardizedRole")
					.fire(callback);
//		}

	}

	
	public void initAdvancedSearchByStandardizedRole(
			final long standardizedRoleID) {

		// Log.info("standardizedRoleID:" + standardizedRoleID);
		fireAdvancedSearchCriteriasCountRequest(standardizedRoleID,
				new OSCEReceiver<Long>() {
					@Override
					public void onSuccess(Long response) {
						if (view == null) {
							// This activity is dead
							return;
						}
						// Log.debug("Advanced search init: " + response);
						view.getAdvancedSearchCriteriaTable().setRowCount(
								response.intValue(), true);

						onRangeChangedAdvancedSearchCriteriaTable(standardizedRoleID);
					}
				});
		
	}

	protected void onRangeChangedAdvancedSearchCriteriaTable(
			long standardizedRoleID) {
		final Range range = view.getAdvancedSearchCriteriaTable()
				.getVisibleRange();

		final OSCEReceiver<List<AdvancedSearchCriteriaProxy>> callback = new OSCEReceiver<List<AdvancedSearchCriteriaProxy>>() {
			@Override
			public void onSuccess(List<AdvancedSearchCriteriaProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
				view.getAdvancedSearchCriteriaTable().setRowData(
						range.getStart(), values);
				advancedSearchCriteriaProxies = values;
				initAdvanceSearchCriteriaList(advancedSearchCriteriaProxies
						.size());
				//Module 3 : Assignment E : Start
				// 
				
				initPatientInSemester(false,false);
								
				//Module 3 : Assignment E : Stop
				// 
				// finishPendingSelection();
				if (widget != null) {
					widget.setWidget(view.asWidget());
				}
			}
		};

		fireAdvancedSearchRangeRequest(standardizedRoleID, range, callback);
	}

	@Override
	public void surveyImpBtnClicked(){
		String locale = LocaleInfo.getCurrentLocale().getLocaleName();
		dmxSyncService.sync(locale,new AsyncCallback<String>(){

			@Override
			public void onFailure(Throwable caught) {
			   try {
		          throw caught;
		        } catch (DMZSyncException e) {
		        	Window.alert(messageLookup.serverReturndError()+messageLookup.getString(e.getType())+e.getMessage());
		        } catch (Throwable e) {
		        	Window.alert(messageLookup.serverReturndError()+e.getMessage());
		        }
				
			}

			@Override
			public void onSuccess(String result) {
				GWT.log("################onSuccess result = "+result);
				
				String[] messages = result.split("#&"); 
				GWT.log("################onSuccess messages = "+messages);				
				DialogBox dialogBox = createDialogBox(messages);
				dialogBox.center();
				dialogBox.show();

			}
			
		});
	}
	
	
	
	/**
   * Create the dialog box for this example.
   *
   * @return the new dialog box
   */
  private DialogBox createDialogBox(String[] messages) {
		// Create a dialog box and set the caption text
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Show the return messages from DMZ");

		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setSpacing(4);
		dialogBox.setWidget(dialogContents);
		for(String message : messages){
			// Add some text to the top of the dialog
			HTML details = new HTML(message+"<br/><br/>");
			dialogContents.add(details);
			dialogContents.setCellHorizontalAlignment(
				details, HasHorizontalAlignment.ALIGN_LEFT);
		}
		
		// Add a close button at the bottom of the dialog
		Button closeButton = new Button(
			"close", new ClickHandler() {
			  public void onClick(ClickEvent event) {
				dialogBox.hide();
			  }
			});
		
		dialogContents.add(closeButton);
		if (LocaleInfo.getCurrentLocale().isRTL()) {
		  dialogContents.setCellHorizontalAlignment(
			  closeButton, HasHorizontalAlignment.ALIGN_LEFT);

		} else {
		  dialogContents.setCellHorizontalAlignment(
			  closeButton, HasHorizontalAlignment.ALIGN_RIGHT);
		}
		
		// Return the dialog box
		return dialogBox;
  }

	
	
	
	
	private void initAdvanceSearchCriteriaList(int size) {
		advanceSearchCriteriaList = new ArrayList<Boolean>();
		int i = 0;
		while (i < size) {
			advanceSearchCriteriaList.add(false);
			i++;
		}

	}

	private void fireAdvancedSearchRangeRequest(long standardizedRoleID,
			final Range range,
			final OSCEReceiver<List<AdvancedSearchCriteriaProxy>> callback) {
		requests.advancedSearchCriteriaNonRoo()
				.findAdvancedSearchCriteriasByStandardizedRoleID(
						standardizedRoleID, range.getStart(), range.getLength())
				.fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}

	protected void fireAdvancedSearchCriteriasCountRequest(
			long standardizedRoleID, OSCEReceiver<Long> callback) {
		requests.advancedSearchCriteriaNonRoo()
				.countAdvancedSearchCriteriasByStandardizedRoleID(
						standardizedRoleID).fire(callback);
	}
	
	// Module 3 d {
	
		public void patientInSemesterSelected(PatientInSemesterProxy patientInSemesterProxy,Set<OsceDayProxy> setOsceDayProxy,OsceDaySubViewImpl osceDaySubViewImpl)
		{
			
			 

			 /*
				Registration of RoleFulfill Criteria Event
				
				RoleFulfilCriteriaEvent.register(requests.getEventBus(), (RoleSubViewImpl)view);
				
			 */			
			 
			Log.info("patientInSemesterSelected() Called");
			
			//Module 3 :   
			Log.info("patientInSemesterProxy is :"+ patientInSemesterProxy.getId());
			if(patientInSemesterProxy.getOsceDays() !=null){
			Log.info("patientInSemesterProxy is size:"+ patientInSemesterProxy.getOsceDays().size());}
			//Module 3 :  
			
			final List<StandardizedRoleProxy> listStandardizedRole = new ArrayList<StandardizedRoleProxy>();
			
					
			Log.info("Set Size :" + setOsceDayProxy.size());
			
			Iterator<OsceDayProxy> itOsceDayProxy = setOsceDayProxy.iterator();
			
			while(itOsceDayProxy.hasNext()){
			
				final  OsceDayProxy oDProxy=itOsceDayProxy.next();

							
				if(osceDaySubViewImpl.getOsceDayProxy().getId()==oDProxy.getId()){
				
				Log.info("Impl Day Proxy :" +osceDaySubViewImpl.getOsceDayProxy().getId());
				Log.info("Found Key For Day Proxy  :" +oDProxy.getId());
				
//				if(osceDaySubViewImpl.getOsceDayProxy()==osceDayProxy){
					
									
				osceDaySubViewImpl.simpleDiscloserPanel.getHeader().setStyleName("highlight-role");

				requests.osceDayRequestNooRoo().findRoleForSPInSemester(patientInSemesterProxy.getId(), oDProxy.getId()).fire(new OSCEReceiver<List<StandardizedRoleProxy>>() {

						@Override
						public void onSuccess(List<StandardizedRoleProxy> response ) {
							
							//listStandardizedRole.addAll(response);
							Log.info("@Suucssfully Arrived Standardizes Patient List   "+response.size());
							
							for(StandardizedRoleProxy  role: listStandardizedRole){
								Log.info("Role is " + role.getShortName());
							}	
								/*Event Fire Code */
								
								requests.getEventBus().fireEvent(new RoleFulfilCriteriaEvent(oDProxy,response));
						}
					});
					
				}
				else{

						osceDaySubViewImpl.simpleDiscloserPanel.getHeader().setStyleName("mainNavPanel");
				 }
		}
				
				
	}
		
		
		public OsceDayProxy getOsceDayProxy() {
			return osceDayProxy;
		}

		public void setOsceDayProxy(OsceDayProxy osceDayProxy) {
			this.osceDayProxy = osceDayProxy;
		}

		@Override
		public void roleSelectedevent(StandardizedRoleProxy standardizedRoleProxy,
				final OsceDaySubViewImpl osceDaySubViewImpl) {
			
			// change {
				roleSelectedInOsceDay=osceDaySubViewImpl.getOsceDayProxy();
			// change }
			Log.info("Inside roleSelectedevent() at RoleAssignmentPatientInSemesterActivity.java");
			
			requests.osceDayRequestNooRoo().findRoleAssignedInOsceDay(standardizedRoleProxy.getId(),osceDaySubViewImpl.getOsceDayProxy().getId()).fire(new OSCEReceiver<Boolean>() {

				@Override
				public void onSuccess(Boolean response) {
					if(response){
						Log.info("Selected Role is In OsceDay :" + osceDaySubViewImpl.getOsceDayProxy().getId());
						
						osceDaySubViewImpl.simpleDiscloserPanel.getHeader().setStyleName("highlight-role");
					}
					else{
						osceDaySubViewImpl.simpleDiscloserPanel.getHeader().setStyleName("mainNavPanel");
					}
					
				}
			});
			
		}

		// Module 3 d }
	
	// Module 3 Task B
	@Override
	public void onAddAllActive(
			List<StandardizedPatientProxy> standardizedPatientProxies) {
		if (standardizedPatientProxies != null) {
			for (Iterator<StandardizedPatientProxy> iterator = standardizedPatientProxies
					.iterator(); iterator.hasNext();) {
				StandardizedPatientProxy standardizedPatientProxy = (StandardizedPatientProxy) iterator
						.next();
				if (standardizedPatientProxy.getStatus() == StandardizedPatientStatus.ACTIVE) {
					onStandizedPatientAddBtnClick(standardizedPatientProxy);
				}

			}
		}

	}
	
	public void onDeleteButtonClicked(
			PatientInSemesterData patientInSemesterData) {
		PatientInSemesterProxy patientInSemesterProxy = patientInSemesterData
				.getPatientInSemesterProxy();

		Log.info("patientInSemesterProxy.getPatientInRole().size: "
				+ patientInSemesterProxy.getPatientInRole().size());
		// if (patientInSemesterProxy.getPatientInRole() != null
		// && patientInSemesterProxy.getPatientInRole().size() > 0) {
		//
		// MessageConfirmationDialogBox dialogBox = new
		// MessageConfirmationDialogBox(
		// "Warning");
		// dialogBox.showConfirmationDialog(constants.onDeleteRoleAssignedToPatient());
		//
		// } else
		{
			requests.patientInSemesterRequest().remove()
					.using(patientInSemesterProxy)
					.fire(new OSCEReceiver<Void>() {

						public void onSuccess(Void ignore) {
							Log.debug("Sucessfully deleted");
							init();
						}
					});
		}
		
	}

	// Module 3 Task B

/* MODULE 3 : Assignment I */
	@Override
	public void onOSCESecurityChange(final OsceProxy osceProxy,
			OSCESecurityStatus osceSecurityStatus,
			PatientAveragePerPost patientAveragePerPost,
			final boolean isSecurityChange) {

		OsceRequest osceRequest = requests.osceRequest();

		OsceProxy tempOsceProxy = osceRequest.edit(osceProxy);
		if (isSecurityChange) {
			tempOsceProxy.setSecurity(osceSecurityStatus);
		} else {
			tempOsceProxy.setPatientAveragePerPost(patientAveragePerPost);
		}
		
		Log.debug("isSecurityChange is : "
				+ isSecurityChange);

		osceRequest.persist().using(tempOsceProxy)
				.fire(new OSCEReceiver<Void>() {

					@Override
					public void onSuccess(Void arg0) {

						if (isSecurityChange) {
							requests.patientInRoleRequestNonRoo()
									.removePatientInRoleByOSCE(
											osceProxy.getId())
									.fire(new OSCEReceiver<Integer>() {
										
										public void onSuccess(Integer response) {
											Log.debug("No. of Patient removed from day is : "
													+ response.intValue());
											
											initOsceDaySubView();
											initPatientInSemester(true,false);	

											view.getDataTable()
													.setNavigationButtonEnable(
															false);
										}
										
										public void onViolation(java.util.Set<com.google.gwt.requestfactory.shared.Violation> errors) {
											
											
										}
										
										public void onFailure(ServerFailure error) {
											error.getStackTraceString();
											System.out.println(error.getMessage() +"\n"+error.getStackTraceString());
										}

									});
						}
						else{
							initOsceDaySubView();
						}
					}
	
				});
	}

/* MODULE 3 : Assignment I */
	
	// module 3 f {

			@Override
			public void autoAssignmentBtnClicked() {
				
				//ServerPush event {
				RemoteEventServiceFactory theEventServiceFactory = RemoteEventServiceFactory.getInstance();
				final RemoteEventService theEventService = theEventServiceFactory.getRemoteEventService();
				
				theEventService.addListener(DOMAIN, new AutoAssignPatientInSemesterListener(){
					public void autoAssignPatientInSemesterEvent(AutoAssignPatientInSemesterEvent event){
						
						Log.info("@@Event Received At Client Side After autoAssignPatientInSemester has push it from server");
						
						if(event.getResult()==true){
						
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							theEventService.removeListeners();
							Log.info("@@Algoritham Implemented Successfully Patient Assign In Role Automatically");
							
							MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.success());
							dialogBox.showConfirmationDialog(constants.autoAssignmentSuccess());
							
							Iterator<OsceDaySubViewImpl> osceDaySubViewImplIterator =osceDaySubViewImplList.iterator();
							OsceDaySubViewImpl osceDaySubViewImpl;
							while(osceDaySubViewImplIterator.hasNext()){
								osceDaySubViewImpl=osceDaySubViewImplIterator.next();
								refreshOsceSequences(osceDaySubViewImpl.getOsceDayProxy(),osceDaySubViewImpl );
							}
							
						}
						else{
						
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							theEventService.removeListeners();
							
							MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.failure());
							dialogBox.showConfirmationDialog(constants.autoAssignmentFailure());
						}
						
					}
				});
				
				
				// For Loding Image
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
				
				autoAssignmentPatientInSemesterService.autoAssignPatientInSemester(semesterProxy.getId(), new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
						theEventService.removeListeners();
						
						Log.info("AutoAssignment PatientIn semester Request Failed Due to" + caught.getMessage());
						caught.printStackTrace();
						
						MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.failure());
						dialogBox.showConfirmationDialog(constants.autoAssignmentFailure());
						
					}

					@Override
					public void onSuccess(Void result) {
						Log.info("AutoAssignment PatientIn semester Is executing but response has returned");
						
					}
				});
				
			}
			//ServerPush event }
			// module 3 f }
}
