package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.BellSchedulePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.BellScheduleView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.BellScheduleViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.shared.BellAssignmentType;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;

/**
 * @author dk
 * 
 */

@SuppressWarnings("deprecation")
public class BellScheduleActivity extends AbstractActivity implements
		BellScheduleView.Presenter, BellScheduleView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private BellScheduleView view;

	private BellSchedulePlace place;
	private BellScheduleActivity bellScheduleActivity;
	private SemesterProxy semesterProxy = null;
	private ActivityManager activityManager;
	public HandlerManager handlerManager;
	private BellScheduleActivityMapper activityMapper;
	private HandlerRegistration rangeChangeHandler;
	// @SPEC table to add data and remove
	private CellTable<BellAssignmentType> table;
	private List<AssignmentProxy> assignmentProxies;
	List<BellAssignmentType> bellAssignmentTypes;

	public BellScheduleActivity(OsMaRequestFactory requests,
			PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;
	}

	public BellScheduleActivity(OsMaRequestFactory requests,
			PlaceController placeController, BellSchedulePlace place) {

		this.requests = requests;
		this.placeController = placeController;
		this.place = place;
		this.handlerManager = place.handler;
		this.semesterProxy = place.semesterProxy;

		activityMapper = new BellScheduleActivityMapper(requests,
				placeController);
		this.activityManager = new ActivityManager(activityMapper,
				requests.getEventBus());

		// ApplicationLoadingScreenEvent.initialCounter();
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
				semesterProxy = event.getSemesterProxy();
				init();
			}
		});

	}

	public void addSelectChangeHandler(SelectChangeHandler handler) {
		handlerManager.addHandler(SelectChangeEvent.getType(), handler);
	}

	public void onStop() {

	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		BellScheduleView systemStartView = new BellScheduleViewImpl(
				getSemesterName());
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());

		RecordChangeEvent.register(requests.getEventBus(),
				(BellScheduleViewImpl) view);
		setTable(view.getTable());

		init();

		view.setDelegate(this);
	}

	private String getSemesterName() {
		return this.semesterProxy.getSemester().name() + " "
				+ this.semesterProxy.getCalYear();
	}

	private void init() {
		requests.getEventBus().fireEvent(
				new ApplicationLoadingScreenEvent(true));

		view.setSemesterName(getSemesterName());
		init2();
	}

	private class QwtFileReceiver extends OSCEReceiver<String> {
		@Override
		public void onFailure(ServerFailure error) {
			Log.error(error.getMessage());
			// onStop();
		}

		@Override
		public void onSuccess(String response) {
			Window.open(response, "_blank", "enabled");
		}
	}

	public void init2() {

		System.out.println("Inside INIT2()");

		// fireCountAssignmentRequest(new OSCEReceiver<Integer>() {
		// @Override
		// public void onSuccess(Integer response) {
		// if (view == null) {
		// // This activity is dead
		// return;
		// }
		// Log.debug("Geholte Nationalitäten aus der Datenbank: "
		// + response);
		// System.out
		// .println("Arrived result of count set table size according to it");
		// view.getTable().setRowCount(response.intValue(), true);
		//
		// onRangeChanged();
		// }
		// });

		view.getTable().setRowCount(0, true);

		onRangeChanged();

		rangeChangeHandler = table
				.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {						
						onRangeChanged();
					}
				});

		requests.getEventBus().fireEvent(
				new ApplicationLoadingScreenEvent(false));
	}

	private void setTable(CellTable<BellAssignmentType> table) {
		this.table = table;

	}

	public void fireCountAssignmentRequest(OSCEReceiver<Integer> callback) {

		requests.assignmentRequestNonRoo()
				.getCountAssignmentsBySemester(this.semesterProxy.getId())
				.fire(callback);
	}

	protected void onRangeChanged() {
		final Range range = table.getVisibleRange();

		final OSCEReceiver<List<AssignmentProxy>> callback = new OSCEReceiver<List<AssignmentProxy>>() {

			@Override
			public void onSuccess(List<AssignmentProxy> response) {
				if (view == null) {
					return;
				}
				System.out.println("Successfully result set in table");

				bellAssignmentTypes = BellAssignmentType
						.getBellAssignmentProxyType(response,
								view.getTimeInMinute(), view.isPlusTime(),
								semesterProxy);

				assignmentProxies = response;

				table.setRowData(range.getStart(), bellAssignmentTypes);

				if (widget != null) {
					widget.setWidget(view.asWidget());
				}
			}

		};

		fireRangeRequest(range, callback);
	}

	private void fireRangeRequest(final Range range,
			final OSCEReceiver<List<AssignmentProxy>> callback) {
		createRangeRequest(range).with(view.getPaths()).fire(callback);
	}

	protected Request<List<AssignmentProxy>> createRangeRequest(Range range) {

		// return null;

		return requests.assignmentRequestNonRoo()
				.getAssignmentsBySemester(this.semesterProxy.getId())
				.with("osceDay", "osceDay.osce", "osceDay.osce.semester");
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public SemesterProxy getSemester() {
		return this.semesterProxy;
	}

	@Override
	public void getNewSchedule() {
		init();

	}

	@Override
	public void onBellScheduleUpload() {

		requests.getEventBus().fireEvent(
				new ApplicationLoadingScreenEvent(true));

		requests.assignmentRequestNonRoo()
				.getQwtBellSchedule(
						// bellAssignmentTypes,assignmentProxies,
						this.semesterProxy.getId(), view.getTimeInMinute(),
						view.isPlusTime()).fire(new QwtFileReceiver());

		requests.getEventBus().fireEvent(
				new ApplicationLoadingScreenEvent(false));

	}
}
