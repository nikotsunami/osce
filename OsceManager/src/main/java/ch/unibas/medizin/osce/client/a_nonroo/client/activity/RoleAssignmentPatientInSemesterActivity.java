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
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment.ManualStandardizedPatientInSemesterAssignmentPopupView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment.ManualStandardizedPatientInSemesterAssignmentPopupViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment.OsceDaySubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment.OsceDaySubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment.PatientInSemesterData;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment.RoleAssignmentView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment.RoleAssignmentViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.PatientInSemesterSelectedEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.PatientInSemesterSelectedHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInRoleRequest;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterRequest;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.shared.StudyYears;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

@SuppressWarnings("deprecation")
public class RoleAssignmentPatientInSemesterActivity extends AbstractActivity
		implements RoleAssignmentView.Presenter, RoleAssignmentView.Delegate,
		ManualStandardizedPatientInSemesterAssignmentPopupView.Delegate,OsceDaySubView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private RoleAssignmentView view;
	private RoleAssignmentPlace place;
	private RoleAssignmentPatientInSemesterActivity spRoleAssignmentActivity;
	private SemesterProxy semesterProxy = null;
	private ActivityManager activityManager;
	private ManualStandardizedPatientInSemesterAssignmentPopupViewImpl manualStdPatientInSemesterAssignmentPopupViewImpl;
	private List<PatientInSemesterProxy> patientInSemesterProxies;
	private OscePostProxy oscePostProxy;
	//Module 3 {
	
		//private SemesterProxy semesterProxy;
		private OsceProxy osceProxy;
		private OsceDayProxy osceDayProxy;
		private OsceDaySubViewImpl osceDaySubViewImpl;
		private DisclosurePanel disCloserPanel;
		private StudentsActivity activity;
//		private HashMap<String,Object> timerMap;
		private Timer osceDayTimer;
		private OsMaConstant constant = GWT.create(OsMaConstant.class);
		
		
		//Module 3 }
	
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
						Log.info("~~~~~~~~ApplicationLoadingScreenEvent onEventReceived Called");
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
						Log.info("~~~~~~~~ApplicationLoadingScreenEvent onEventReceived Called");
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
	requests.find(semesterProxy.stableId()).with("osces","osces.osce_days").fire(new OSCEReceiver<Object>() {

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
					OsceDaySubViewImpl osceDaySubViewImpl = new OsceDaySubViewImpl();
					osceDaySubViewImpl.setDelegate(spRoleAssignmentActivity);
					osceDaySubViewImpl.setOsceDayProxy(osceDayProxy);
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
});
	
}
@Override
public void discloserPanelOpened(final OsceDayProxy osceDayProxy,OsceDaySubViewImpl osceDaySubViewImpl) {
	Log.info("OsceDay Proxy " + osceDayProxy.getId());
	//(new Date()).getTime()+ (1000 * 60 * 60 * 24))
	
	
	if(Cookies.getCookie(osceDayProxy.getId().toString()) == null){
		Cookies.setCookie(osceDayProxy.getId().toString(),osceDayProxy.getId().toString(),new Date((new Date()).getTime() + (1000 * 60 * 60 * 24)));
		Log.info("Cookie Created for :" + osceDayProxy.getId());
		
	}
		refreshData(osceDayProxy);
	
		osceDayTimer.schedule(constant.OSCEDAYTIMESCHEDULE);
		
		//timerMap.put(osceDayProxy.getId().toString(),osceDayTimer);
}


@Override
public void discloserPanelClosed(OsceDayProxy osceDayProxy,OsceDaySubViewImpl osceDaySubViewImpl) {
			Log.info("Cookie Removed for :" +osceDayProxy.getId());
			Cookies.removeCookie(osceDayProxy.getId().toString());
			//timerMap.remove(osceDayProxy.getId().toString());
	
}

public void refreshData(OsceDayProxy osceDayProxy){
	// Do Data Refreshing Task
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

		requests.patientInSemesterRequest()
				.findAllPatientInSemesters()
				.with("standardizedPatient", "semester", "trainings",
						"osceDays.osce",
						"patientInRole.oscePost.standardizedRole")
				.fire(new OSCEReceiver<List<PatientInSemesterProxy>>() {
					@Override
					public void onSuccess(List<PatientInSemesterProxy> response) {
						System.out.println("response : " + response.size());
						Log.info("response : " + response.size());
						patientInSemesterProxies = response;

						Iterator<PatientInSemesterProxy> iterator = response
								.iterator();
						PatientInSemesterProxy patientInSemesterProxy;
						patientInSemesterDataList = new ArrayList<PatientInSemesterData>();

						while (iterator.hasNext()) {
							patientInSemesterProxy = iterator.next();

							Log.info("IsAccepted : "
									+ patientInSemesterProxy.getAccepted());

							Log.info("getPatientInRole : "
									+ patientInSemesterProxy.getPatientInRole()
											.size());

							Log.info("getStandardizedPatient().getPreName() : "
									+ patientInSemesterProxy
											.getStandardizedPatient());

							Log.info("semesterProxy.getId()"
									+ semesterProxy.getId());
							if (patientInSemesterProxy.getSemester() != null) {
								Log.info("patientInSemesterProxy.getId()"
										+ patientInSemesterProxy.getSemester()
												.getId());
							} else {
								Log.info("semesterProxy.getId() is null ...");
							}

							if (semesterProxy.getId() == patientInSemesterProxy
									.getSemester().getId()) {
								patientInSemesterDataList
										.add(new PatientInSemesterData(
												patientInSemesterProxy,
												spRoleAssignmentActivity));
							}

						}
						// patientInSemesterProxies = response;
						if (response != null && response.size() > 0) {

							view.setData(patientInSemesterDataList);
							Log.info("PatientInSemesterProxy Size : "
									+ response.size());

							requests.getEventBus().fireEvent(
									new ApplicationLoadingScreenEvent(false));

						}
					}
				});
		initOsceDaySubView();
	}

	@Override
	public void onAddManuallyClicked() {
		requests.standardizedPatientRequest().findAllStandardizedPatients()
				.fire(new OSCEReceiver<List<StandardizedPatientProxy>>() {
					@Override
					public void onSuccess(
							List<StandardizedPatientProxy> response) {
						Log.info("~Success Call....");
						Log.info("~REFRESH SUGGESION BOX DATA"
								+ response.size());

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
						init();
						manualStdPatientInSemesterAssignmentPopupViewImpl
								.hide();
					}
				});

	}

	@Override
	public void onDetailViewClicked(
			final PatientInSemesterData patientInSemesterData) {

		PatientInRoleRequest patientInRoleRequest = requests
				.patientInRoleRequest();
		PatientInRoleProxy patientInRoleProxy = patientInRoleRequest
				.create(PatientInRoleProxy.class);

		patientInRoleProxy.setPatientInSemester(patientInSemesterData
				.getPatientInSemesterProxy());
		patientInRoleProxy.setOscePost(oscePostProxy);
		patientInRoleProxy.setFit_criteria(false);
		patientInRoleProxy.setIs_backup(false);

		patientInRoleRequest.persist().using(patientInRoleProxy)
				.fire(new OSCEReceiver<Void>() {

					@Override
					public void onSuccess(Void arg0) {
						System.out
								.println("patientInRoleProxy saved successfully");
						PatientInSemesterSelectedEvent.register(
								requests.getEventBus(),
								new PatientInSemesterSelectedHandler() {

									@Override
									public void onPatientInSemesterSelectedEventReceived(
											PatientInSemesterSelectedEvent event) {

										Log.info("onPatientInSemesterSelectedEventReceived called ... ");
										event.refreshPanels();
									}
								});
						requests.getEventBus().fireEvent(
								new PatientInSemesterSelectedEvent());
//						Log.info("After fireevent in detail view clicked ");
						// patientInSemesterData
						// .getPatientInSemesterProxy(),
						// null));
						init();
//						Log.info("After init in detail view clicked ");
						// PatientInSemesterSelectedEvent
					}
				});

	}

}
