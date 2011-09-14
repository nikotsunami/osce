package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ClinicView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ClinicViewImpl;
import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;

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

public class ClinicActivity extends AbstractActivity implements
ClinicView.Presenter, ClinicView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeControler;
	private AcceptsOneWidget widget;
	private ClinicView view;
	private CellTable<ClinicProxy> table;
	private SingleSelectionModel<ClinicProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	private ActivityManager activityManger;
	private ClinicDetailsActivityMapper ClinicDetailsActivityMapper;
	

	public ClinicActivity(OsMaRequestFactory requests, PlaceController placeController) {
    	this.requests = requests;
    	this.placeControler = placeController;
    	ClinicDetailsActivityMapper = new ClinicDetailsActivityMapper(requests, placeController);
		this.activityManger = new ActivityManager(ClinicDetailsActivityMapper, requests.getEventBus());
    }

	public void onStop(){
		activityManger.setDisplay(null);
	}
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		ClinicView systemStartView = new ClinicViewImpl();
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		setTable(view.getTable());

		eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {
				
				if (event.getNewPlace() instanceof ClinicDetailsPlace){
					init();
				}
			}
		});
		

		init();

		activityManger.setDisplay(view.getDetailsPanel());

		// Inherit the view's key provider
		ProvidesKey<ClinicProxy> keyProvider = ((AbstractHasData<ClinicProxy>) table)
				.getKeyProvider();
		selectionModel = new SingleSelectionModel<ClinicProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						ClinicProxy selectedObject = selectionModel
								.getSelectedObject();
						if (selectedObject != null) {
							Log.debug(selectedObject.getName()
									+ " selected!");
							showDetails(selectedObject);
						}
					}
				});

		view.setDelegate(this);
		
	}
	
	private void init() {

		fireCountRequest(new Receiver<Long>() {
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

		rangeChangeHandler = table
				.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {
						ClinicActivity.this.onRangeChanged();
					}
				});
	}
	
	protected void showDetails(ClinicProxy Clinic) {
		
		Log.debug(Clinic.getName());
		
		goTo(new ClinicDetailsPlace(Clinic.stableId(),
				ClinicDetailsPlace.Operation.DETAILS));
	}
	

	protected void onRangeChanged() {
		final Range range = table.getVisibleRange();

		final Receiver<List<ClinicProxy>> callback = new Receiver<List<ClinicProxy>>() {
			@Override
			public void onSuccess(List<ClinicProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
//				idToRow.clear();
//				idToProxy.clear();
//				for (int i = 0, row = range.getStart(); i < values.size(); i++, row++) {
//					ClinicProxy Clinic = values.get(i);
//					@SuppressWarnings("unchecked")
//					// Why is this cast needed?
//					EntityProxyId<ClinicProxy> proxyId = (EntityProxyId<ClinicProxy>) Clinic
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
	
	private void fireRangeRequest(final Range range,
			final Receiver<List<ClinicProxy>> callback) {
		createRangeRequest(range).with(view.getPaths()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}
	
	protected Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.ClinicProxy>> createRangeRequest(
			Range range) {
		return requests.clinicRequest().findClinicEntries(range.getStart(), range.getLength());
	}

	protected void fireCountRequest(Receiver<Long> callback) {
		requests.clinicRequest()
				.countClinics().fire(callback);
	}

	private void setTable(CellTable<ClinicProxy> table) {
		this.table = table;
		
	}

	@Override
	public void newClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goTo(Place place) {
		placeControler.goTo(place);
		
	}

}
