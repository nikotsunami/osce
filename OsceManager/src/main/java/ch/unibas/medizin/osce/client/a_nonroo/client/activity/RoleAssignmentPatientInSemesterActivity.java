package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
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
import ch.unibas.medizin.osce.client.a_nonroo.client.util.PatientInSemesterSelectedEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RoleFulfilCriteriaEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RoleSelectedEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInRoleRequest;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterRequest;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.shared.OsceStatus;
import ch.unibas.medizin.osce.shared.RoleTypes;
import ch.unibas.medizin.osce.shared.StudyYears;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.Range;

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
	private OscePostProxy oscePostProxy;
	// Module 3 {
	private static final OsceConstants constants = GWT.create(OsceConstants.class);
	// private SemesterProxy semesterProxy;
	private OsceProxy osceProxy;
	private OsceDayProxy osceDayProxy;
	private OsceDaySubViewImpl osceDaySubViewImpl;
	private DisclosurePanel disCloserPanel;
	private StudentsActivity activity;
	// private HashMap<String,Object> timerMap;
	private Timer osceDayTimer;
	private OsMaConstant osMaConstant = GWT.create(OsMaConstant.class);
	

	private List<Boolean> advanceSearchCriteriaList;
	private List<AdvancedSearchCriteriaProxy> advancedSearchCriteriaProxies;
	private RoleSubViewImpl roleSubViewSelected;

	// Module 3 }

	
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
						Log.info("ApplicationLoadingScreenEvent onEventReceived Called");
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

		view = new RoleAssignmentViewImpl();
		view.setDelegate(this);
		RoleSelectedEvent.register(requests.getEventBus(), view);
		spRoleAssignmentActivity = this;

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
			
		}
	};

	//timerMap = new HashMap<String, Object>();
	//Module 3:Assignment D
	requests.find(semesterProxy.stableId()).with("osces","osces.osce_days","osces.osce_days","osces.osce_days").fire(new OSCEReceiver<Object>() {

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
					
				Set<OsceDayProxy> setOsceDayProxy = osceProxy.getOsce_days();
				
				if(setOsceDayProxy == null){
					Log.info("No OsceDay Found");
				}
				else{
				Log.info("Total OSce Day is : " + setOsceDayProxy.size());
				Iterator<OsceDayProxy> iteratorOSceDayProxy = setOsceDayProxy.iterator();
				
				
				
				while(iteratorOSceDayProxy.hasNext()){
					
					osceDayProxy=iteratorOSceDayProxy.next();
					if(osceProxy.getOsceStatus()==OsceStatus.OSCE_FIXED){
					OsceDaySubViewImpl osceDaySubViewImpl = new OsceDaySubViewImpl();
					osceDaySubViewImpl.setDelegate(spRoleAssignmentActivity);
					osceDaySubViewImpl.setOsceDayProxy(osceDayProxy);
					
					// Module 3 d {
					
					//register events
					PatientInSemesterSelectedEvent.register(requests.getEventBus(), osceDaySubViewImpl);
					RoleSelectedEvent.register(requests.getEventBus(), osceDaySubViewImpl);
					
					// Module 3 d }
					if(isopen){
						osceDaySubViewImpl.simpleDiscloserPanel.setOpen(isopen);
						isopen=false;
					}
					StudyYears studyYear =osceProxy.getStudyYear();
					String name =semesterProxy.getSemester().name();
					//Date date =osceDayProxy.getOsceDate();
						
					if(studyYear != null && name !=null){
						osceDaySubViewImpl.simpleDiscloserPanel.getHeaderTextAccessor().setText("" +studyYear +"." + name +" - " + DateTimeFormat.getShortDateFormat().format(osceDayProxy.getOsceDate()));
						osceDaySubViewContainerPanel.add(osceDaySubViewImpl);
					}
					else{
						Window.alert("Semester and study year must not empty to show on OSce Day");
					}
						
							
				}
				
				
				}
				
			}
		 }
		}
			
	}
});
	
}
@Override
public void discloserPanelOpened(final OsceDayProxy osceDayProxy,final OsceDaySubViewImpl osceDaySubViewImpl) {
	Log.info("OsceDay Proxy " + osceDayProxy.getId());
	//(new Date()).getTime()+ (1000 * 60 * 60 * 24))
	
	
	if(Cookies.getCookie(osceDayProxy.getId().toString()) == null){
		Cookies.setCookie(osceDayProxy.getId().toString(),osceDayProxy.getId().toString(),new Date((new Date()).getTime() + (1000 * 60 * 60 * 24)));
		Log.info("Cookie Created for :" + osceDayProxy.getId());
		
	}
		refreshData(osceDayProxy);
	
		osceDayTimer.schedule(osMaConstant.OSCEDAYTIMESCHEDULE);
		
		//timerMap.put(osceDayProxy.getId().toString(),osceDayTimer);
		
		//Module 3:Assignment D[
		//retrieve data for osceDay
		if(osceDayProxy.getOsceSequences() ==null)
		{
			
			refreshOsceSequences(osceDayProxy, osceDaySubViewImpl);
			
		}
		//Module 3:Assignment D]
		
}

//Module 3:Assignment D[
public void refreshOsceSequences(OsceDayProxy osceDayProxy,final OsceDaySubViewImpl osceDaySubViewImpl) 
{
	
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
	//create Roles
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
		 
		while(postIterator.hasNext())
		{
			OscePostProxy postProxy=postIterator.next();
			
			if(postProxy.getStandardizedRole().getRoleType() == RoleTypes.Simpat || postProxy.getStandardizedRole().getRoleType()==RoleTypes.Statist)
					{
				RoleSubView view=new RoleSubViewImpl();
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
		osceDaySubViewImpl.setOsceDayProxy(osceDayProxy);
		osceDaySubViewImpl.getSequenceVP().insert(roleHP, osceDaySubViewImpl.getSequenceVP().getWidgetCount());
		osceDaySubViewImpl.getSequenceVP().insert(new HTML("<hr class='dashed' />"), osceDaySubViewImpl.getSequenceVP().getWidgetCount());
		
	}
	osceDaySubViewImpl.getSequenceVP().remove(osceDaySubViewImpl.getSequenceVP().getWidgetCount()-1);
}

public void createRoleSubView(RoleSubView roleSubView,OscePostProxy postProxy)
{
	
	
	StandardizedRoleProxy roleProxy=postProxy.getStandardizedRole();
	if(roleProxy.getRoleType() == RoleTypes.Simpat || roleProxy.getRoleType()==RoleTypes.Statist)
	{
	
		if(roleSubView==null)
		{
			
			roleSubView = new RoleSubViewImpl();
		}
		
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
				isFirstAssigned=patientInRoleIsFirstAssigned(roleSubView.getOsceSequenceProxy(),patientInRoleProxy);
			
			if(isFirstAssigned)
				((PatientInRoleSubViewImpl)patientInRoleView).addStyleName("count-yellow");
			else
				((PatientInRoleSubViewImpl)patientInRoleView).removeStyleName("count-yellow");
			
			patientInRoleView.setPatientInRoleProxy(patientInRoleProxy);
			patientInRoleView.setDelegate(this);
			patientInRoleView.getPatientInRoleLbl().setText(patientInRoleProxy.getPatientInSemester().getStandardizedPatient().getName());
			patientInRoleView.setRoleSubView(roleSubView);
			
			if(!patientInRoleProxy.getFit_criteria())
				((PatientInRoleSubViewImpl)patientInRoleView).addStyleName("count-red");
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
	}
}

//refresh RoleSubView
public void refreshRoleSubView(final RoleSubView roleSubView)
{
	OscePostProxy postProxy=roleSubView.getPostProxy();
	requests.oscePostRequest().findOscePost(postProxy.getId()).with("patientInRole","standardizedRole","patientInRole.patientInSemester","patientInRole.patientInSemester.standardizedPatient").fire(new OSCEReceiver<OscePostProxy>() {

		@Override
		public void onSuccess(OscePostProxy response) {
			if(response.getStandardizedRole().getRoleType() == RoleTypes.Simpat || response.getStandardizedRole().getRoleType()==RoleTypes.Statist)
			{
				createRoleSubView(roleSubView,response);
			}
			
		}
	});
}


public boolean patientInRoleIsFirstAssigned(OsceSequenceProxy sequenceProxy,PatientInRoleProxy patientInRoleProxy)
{
	Iterator<OscePostProxy> postIterator=sequenceProxy.getOscePosts().iterator();
	int count=0;
	while(postIterator.hasNext())
	{
		OscePostProxy postProxy=postIterator.next();
		Iterator<PatientInRoleProxy> patientInRoleIterator=postProxy.getPatientInRole().iterator();
		while(patientInRoleIterator.hasNext())
		{
			PatientInRoleProxy proxy=patientInRoleIterator.next();
			
				if(patientInRoleProxy.getPatientInSemester().getStandardizedPatient().getId()==proxy.getPatientInSemester().getStandardizedPatient().getId() && !proxy.getIs_backup())
					count++;
				
				if(count>=2)
				{
					Log.info("Count patientInRoleIsFirstAssigned  :" + count);
					return false;
				}
			
		}
	}
	Log.info("Count patientInRoleIsFirstAssigned  :" + count);
	return true;
	
}
public void roleSelected(RoleSubView view)
{
	Log.info("roleSelected");
	oscePostProxy=view.getPostProxy();
	roleSubViewSelected=(RoleSubViewImpl)view;
	requests.getEventBus().fireEvent(new RoleSelectedEvent(view.getRoleProxy(), view.getOsceDayProxy()));
}

public void editBackUpFlag(final RoleSubView view,final PatientInRoleSubView patientInRoleSubView, PatientInRoleProxy proxy,boolean isBackUp)
{
	final PatientInRoleProxy patientInRoleProxy=proxy;
	PatientInRoleRequest patientInRoleRequest=requests.patientInRoleRequest();
	
	proxy=patientInRoleRequest.edit(proxy);
	proxy.setIs_backup(isBackUp);
	
	patientInRoleRequest.persist().using(proxy).fire(new OSCEReceiver<Void>() {

		@Override
		public void onSuccess(Void response) {
			Log.info("editBackUpFlag : onSuccess");
		
			refreshOsceSequences(view.getOsceDayProxy(), view.getOsceDaySubViewImpl());
			
		}
	});
}

public void deletePatientInRole(final PatientInRoleSubViewImpl patientInRoleView)
{
	Log.info("deletePatientInRole");
	requests.patientInRoleRequest().remove().using(patientInRoleView.getPatientInRoleProxy()).fire(new OSCEReceiver<Void>() {

		@Override
		public void onSuccess(Void response) {
			Log.info("deletePatientInRole : onSuccess");
			
			
			refreshOsceSequences(patientInRoleView.getRoleSubView().getOsceDayProxy(), patientInRoleView.getRoleSubView().getOsceDaySubViewImpl());
			
		}
	});
}
//Module 3:Assignment D]


@Override
public void discloserPanelClosed(OsceDayProxy osceDayProxy,OsceDaySubViewImpl osceDaySubViewImpl) {
			Log.info("Cookie Removed for :" +osceDayProxy.getId());
			Cookies.removeCookie(osceDayProxy.getId().toString());
			//timerMap.remove(osceDayProxy.getId().toString());
	
}

public void refreshData(OsceDayProxy osceDayProxy){
	//Do Data Refreshing Task
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
		initPatientInSemester();

		requests.getEventBus().fireEvent(
				new ApplicationLoadingScreenEvent(false));

		initOsceDaySubView();
	}

	private void initPatientInSemester() {

		requests.patientInSemesterRequest()
				.findAllPatientInSemesters()
				.with("standardizedPatient", "semester", "trainings",
						"osceDays.osce",
						"patientInRole.oscePost.standardizedRole")
				.fire(new OSCEReceiver<List<PatientInSemesterProxy>>() {
					@Override
					public void onSuccess(
							List<PatientInSemesterProxy> patientInSemesterProxies) {
						System.out.println("response : "
								+ patientInSemesterProxies.size());
						Log.info("response : "
								+ patientInSemesterProxies.size());
						initPatientInSemesterData(patientInSemesterProxies);
					}
				});
		view.getAdvancedSearchCriteriaTable().setRowCount(0, true);
		view.getAdvancedSearchCriteriaTable().setRowData(0,
				new ArrayList<AdvancedSearchCriteriaProxy>());

	}

public void initPatientInSemesterData(
			List<PatientInSemesterProxy> patientInSemesterProxies) {
		if (patientInSemesterProxies != null
				&& patientInSemesterProxies.size() > 0) {
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
					Log.info("patientInSemesterProxy.getId()"
							+ patientInSemesterProxy.getSemester().getId());
				} else {
					Log.info("semesterProxy is null ...");
				}
				if (semesterProxy.getId() == patientInSemesterProxy
						.getSemester().getId()) {
					patientInSemesterDataList.add(new PatientInSemesterData(
							patientInSemesterProxy, spRoleAssignmentActivity));
				}

			}
			// patientInSemesterProxies = response;

			view.setData(patientInSemesterDataList);
			Log.info("PatientInSemesterProxy Size : "
					+ patientInSemesterProxies.size());

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

					}
				});

	}

	@Override
	public void onStandizedPatientAddBtnClick(
			StandardizedPatientProxy standardizedPatientProxy) {
		PatientInSemesterRequest patientInSemesterRequest = requests
				.patientInSemesterRequest();
		PatientInSemesterProxy patientInSemesterProxy = patientInSemesterRequest
				.create(PatientInSemesterProxy.class);

		patientInSemesterProxy.setSemester(semesterProxy);
		patientInSemesterProxy.setStandardizedPatient(standardizedPatientProxy);
		patientInSemesterProxy.setAccepted(false);

		patientInSemesterRequest.persist().using(patientInSemesterProxy)
				.fire(new OSCEReceiver<Void>() {

					@Override
					public void onSuccess(Void arg0) {
						System.out.println("Value saved successfully");
						initPatientInSemester();
						manualStdPatientInSemesterAssignmentPopupViewImpl
								.hide();
					}
				});

	}

	@Override
	public void onDetailViewClicked(
			final PatientInSemesterData patientInSemesterData) {

		boolean addOsceDayPatientInSemester = true;
		Set<OsceDayProxy> osceDayProxies = patientInSemesterData
				.getPatientInSemesterProxy().getOsceDays();
		for (Iterator<OsceDayProxy> iterator = osceDayProxies.iterator(); iterator
				.hasNext();) {
			OsceDayProxy osceDayProxy = (OsceDayProxy) iterator.next();

			if (osceDayProxy.getId() == osceDayProxy.getId()) {
				addOsceDayPatientInSemester = false;
				break;
			}

		}
		
		
		if (addOsceDayPatientInSemester) {

			PatientInSemesterProxy editPatientInSemesterProxy = patientInSemesterData
					.getPatientInSemesterProxy();

			PatientInSemesterRequest patientInSemesterRequest = requests
					.patientInSemesterRequest();
			editPatientInSemesterProxy = patientInSemesterRequest
					.edit(editPatientInSemesterProxy);

			editPatientInSemesterProxy.getOsceDays().add(getOsceDayProxy());
			patientInSemesterRequest.persist()
					.using(editPatientInSemesterProxy)
					.fire(new OSCEReceiver<Void>() {

						@Override
						public void onSuccess(Void response) {
							requests.find(
									patientInSemesterData
											.getPatientInSemesterProxy()
											.stableId())
									.with("standardizedPatient", "semester",
											"trainings", "osceDays",
											"patientInRole.oscePost","patientInRole.oscePost.standardizedRole")
									.fire(new OSCEReceiver<Object>() {

										@Override
										public void onSuccess(
												Object patientInSemesterProxy) {

											patientInSemesterData
													.setPatientInSemesterProxy((PatientInSemesterProxy) patientInSemesterProxy);
											System.out
											.println("~~~~patientInRoleProxy saved successfully" + ((PatientInSemesterProxy) patientInSemesterProxy).getOsceDays().size());
											firePatientInSemesterSelectedEvent((PatientInSemesterProxy) patientInSemesterProxy);
										}
									});
						}

					});
		} else {
			System.out
			.println("~~~~patientInRoleProxy saved successfully" + patientInSemesterData
					.getPatientInSemesterProxy().getOsceDays().size());
			firePatientInSemesterSelectedEvent(patientInSemesterData
					.getPatientInSemesterProxy());
		}

	}

	public void firePatientInSemesterSelectedEvent(final 
			PatientInSemesterProxy patientInSemesterProxy) {

		boolean addPatientInRole = true;
		Set<PatientInRoleProxy> patientInRoleProxies =  oscePostProxy.getPatientInRole();
		for (Iterator<PatientInRoleProxy> iterator = patientInRoleProxies.iterator(); iterator
				.hasNext();) {
			PatientInRoleProxy patientInRoleProxy = (PatientInRoleProxy) iterator
					.next();
			
			if(patientInRoleProxy.getPatientInSemester().getId() == patientInSemesterProxy.getId()){
				addPatientInRole= false;
				break;
			}
			
		}
		if(addPatientInRole){
		
		
		PatientInRoleRequest patientInRoleRequest = requests
				.patientInRoleRequest();
		PatientInRoleProxy patientInRoleProxy = patientInRoleRequest
				.create(PatientInRoleProxy.class);

		patientInRoleProxy.setPatientInSemester(patientInSemesterProxy);
		patientInRoleProxy.setOscePost(oscePostProxy);
		patientInRoleProxy.setFit_criteria(false);
		patientInRoleProxy.setIs_backup(false);

		patientInRoleRequest.persist().using(patientInRoleProxy)
				.fire(new OSCEReceiver<Void>() {

					@Override
					public void onSuccess(Void arg0) {
						System.out
								.println("~~~~patientInRoleProxy saved successfully" + patientInSemesterProxy.getOsceDays().size());
						requests.getEventBus().fireEvent(
								new PatientInSemesterSelectedEvent(patientInSemesterProxy,
										patientInSemesterProxy.getOsceDays()));
						refreshRoleSubView(roleSubViewSelected);
					}

				});
		}
		else
		{
			MessageConfirmationDialogBox msg=new MessageConfirmationDialogBox("Warning");
			msg.showConfirmationDialog(constants.patientAlreadyAssigned());
		}
//		init();
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
				if (patientInSemesterProxies == null
						|| patientInSemesterProxies.size() == 0) {
					view.setData(new ArrayList<PatientInSemesterData>());
				}
				initPatientInSemesterData(patientInSemesterProxies);

			}
		};

		requests.patientInSemesterRequestNonRoo()
				.findPatientInSemesterByAdvancedCriteria(semesterProxy.getId(),
						searchCriteria)
				.with("standardizedPatient", "semester", "trainings",
						"osceDays.osce",
						"patientInRole.oscePost.standardizedRole")
				.fire(callback);

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

				// finishPendingSelection();
				if (widget != null) {
					widget.setWidget(view.asWidget());
				}
			}
		};

		fireAdvancedSearchRangeRequest(standardizedRoleID, range, callback);
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
			
			final List<StandardizedRoleProxy> listStandardizedRole = new ArrayList<StandardizedRoleProxy>();
			
					
			Log.info("Set Size :" + setOsceDayProxy.size());
			
			Iterator<OsceDayProxy> itOsceDayProxy = setOsceDayProxy.iterator();
			
			while(itOsceDayProxy.hasNext()){
			
				final  OsceDayProxy oDProxy=itOsceDayProxy.next();

							
				if(osceDaySubViewImpl.getOsceDayProxy().getId()==oDProxy.getId()){
				
				Log.info("Impl Day Proxy :" +osceDaySubViewImpl.getOsceDayProxy().getId());
				Log.info("Lase Day Proxy :" +osceDayProxy.getId());
				
//				if(osceDaySubViewImpl.getOsceDayProxy()==osceDayProxy){
					
				Log.info("Found Key For Day Proxy :" + oDProxy);
					
				osceDaySubViewImpl.simpleDiscloserPanel.getHeader().setStyleName("discloserPanel_Selected_header_Color");

				requests.osceDayRequestNooRoo().findRoleForSPInSemester(patientInSemesterProxy.getId(), oDProxy.getId()).fire(new OSCEReceiver<List<StandardizedRoleProxy>>() {

						@Override
						public void onSuccess(List<StandardizedRoleProxy> response ) {
							
							//listStandardizedRole.addAll(response);
							Log.info("@Suucssfully Arrived Standardizes Patient List   "+listStandardizedRole.size());
							
							for(StandardizedRoleProxy  role: listStandardizedRole){
								Log.info("Role is " + role.getShortName());
							}	
								/*Event Fire Code */
								
								requests.getEventBus().fireEvent(new RoleFulfilCriteriaEvent(oDProxy,response));
						}
					});
					
				}
				else{

						osceDaySubViewImpl.simpleDiscloserPanel.getHeader().setStyleName("discloserPanel_DeSlected_header_Color");
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
	
}
