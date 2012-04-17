package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.OsceDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.OsceView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.OsceViewImpl;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

public class OsceActivity extends AbstractActivity implements OsceView.Presenter, OsceView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private OsceView view;
	private CellTable<OsceProxy> table;
	private SingleSelectionModel<OsceProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	private ActivityManager activityManager;
	private OsceDetailsActivityMapper osceDetailsActivityMapper;


	@Inject
	public OsceActivity(OsMaRequestFactory requests, PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;
		osceDetailsActivityMapper = new OsceDetailsActivityMapper(requests, placeController);
		this.activityManager = new ActivityManager(osceDetailsActivityMapper, requests.getEventBus());
	}

	public void onStop(){
		activityManager.setDisplay(null);
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		OsceView systemStartView = new OsceViewImpl();
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		setTable(view.getTable());

		init();

		activityManager.setDisplay(view.getDetailsPanel());

		// Inherit the view's key provider
		ProvidesKey<OsceProxy> keyProvider = ((AbstractHasData<OsceProxy>) table).getKeyProvider();
		selectionModel = new SingleSelectionModel<OsceProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				OsceProxy selectedObject = selectionModel
						.getSelectedObject();
				if (selectedObject != null) {
					Log.debug("OSCE with id " + selectedObject.getId() + " selected!");
					showDetails(selectedObject);
				}
			}
		});

		view.setDelegate(this);

	}

	private void init() {
		requests.osceRequest().countOsces().fire(new Receiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Intitution aus der Datenbank: " + response);
				view.getTable().setRowCount(response.intValue(), true);

				onRangeChanged();
			}

		});

		rangeChangeHandler = table.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				OsceActivity.this.onRangeChanged();
			}
		});
	}

	protected void showDetails(OsceProxy osce) {
		Log.debug("show details for osce with id " + osce.getId());
		goTo(new OsceDetailsPlace(osce.stableId(), Operation.DETAILS));
	}


	protected void onRangeChanged() {
		final Range range = table.getVisibleRange();

		final Receiver<List<OsceProxy>> callback = new Receiver<List<OsceProxy>>() {
			@Override
			public void onSuccess(List<OsceProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
				//				idToRow.clear();
				//				idToProxy.clear();
				//				for (int i = 0, row = range.getStart(); i < values.size(); i++, row++) {
				//					OsceProxy administrator = values.get(i);
				//					@SuppressWarnings("unchecked")
				//					// Why is this cast needed?
				//					EntityProxyId<OsceProxy> proxyId = (EntityProxyId<OsceProxy>) administrator
				//							.stableId();
				//
				//				}
				table.setRowData(range.getStart(), values);

				// finishPendingSelection();
				if (widget != null) {
					widget.setWidget(view.asWidget());
				}
			}
		};

		fireRangeRequest(range, callback);
	}

	private void fireRangeRequest(final Range range, final Receiver<List<OsceProxy>> callback) {
		createRangeRequest(range).with(view.getPaths()).fire(callback);
	}

	protected Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.OsceProxy>> createRangeRequest(Range range) {
		return requests.osceRequest().findOsceEntries(range.getStart(), range.getLength());
	}


	private void setTable(CellTable<OsceProxy> table) {
		this.table = table;
	}

	@Override
	public void newClicked() {
		Log.info("create clicked");
		placeController.goTo(new OsceDetailsPlace(Operation.CREATE));
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

}
