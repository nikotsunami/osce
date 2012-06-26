package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleAssignmentPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment.ManualStandardizedPatientInSemesterAssignmentPopupView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment.ManualStandardizedPatientInSemesterAssignmentPopupViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment.PatientInSemesterData;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment.RoleAssignmentView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment.RoleAssignmentViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.PatientInSemesterSelectedEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.PatientInSemesterSelectedHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInRoleRequest;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterRequest;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

@SuppressWarnings("deprecation")
public class RoleAssignmentPatientInSemesterActivity extends AbstractActivity
		implements RoleAssignmentView.Presenter, RoleAssignmentView.Delegate,
		ManualStandardizedPatientInSemesterAssignmentPopupView.Delegate {

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
