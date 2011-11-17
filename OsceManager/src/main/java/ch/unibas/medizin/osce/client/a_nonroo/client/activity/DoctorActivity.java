package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.DoctorDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.DoctorView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.DoctorViewImpl;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;

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

public class DoctorActivity extends AbstractActivity implements
DoctorView.Presenter, DoctorView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeControler;
	private AcceptsOneWidget widget;
	private DoctorView view;
	private CellTable<DoctorProxy> table;
	private SingleSelectionModel<DoctorProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	private ActivityManager activityManger;
	private DoctorDetailsActivityMapper DoctorDetailsActivityMapper;
	

	public DoctorActivity(OsMaRequestFactory requests, PlaceController placeController) {
    	this.requests = requests;
    	this.placeControler = placeController;
    	DoctorDetailsActivityMapper = new DoctorDetailsActivityMapper(requests, placeController);
		this.activityManger = new ActivityManager(DoctorDetailsActivityMapper, requests.getEventBus());
    }

	public void onStop(){
		activityManger.setDisplay(null);
	}
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		DoctorView systemStartView = new DoctorViewImpl();
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		setTable(view.getTable());

		eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {
				
				if (event.getNewPlace() instanceof DoctorDetailsPlace){
					init();
				}
			}
		});
		
		
		init();

		activityManger.setDisplay(view.getDetailsPanel());

		// Inherit the view's key provider
		ProvidesKey<DoctorProxy> keyProvider = ((AbstractHasData<DoctorProxy>) table)
				.getKeyProvider();
		selectionModel = new SingleSelectionModel<DoctorProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						DoctorProxy selectedObject = selectionModel
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
		init2("");
	}
	
	private void init2(final String q) {
		
		if(rangeChangeHandler!=null){
			rangeChangeHandler.removeHandler();
			rangeChangeHandler=null;
		}

		fireCountRequest(q, new Receiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Intitution aus der Datenbank: " + response);
				view.getTable().setRowCount(response.intValue(), true);

				onRangeChanged(q);
			}

		});

		rangeChangeHandler = table
				.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {
						DoctorActivity.this.onRangeChanged(q);
					}
				});
	}
	
	protected void showDetails(DoctorProxy Doctor) {
		
		Log.debug(Doctor.getName());
		
		goTo(new DoctorDetailsPlace(Doctor.stableId(),
				DoctorDetailsPlace.Operation.DETAILS));
	}
	

	protected void onRangeChanged(String q) {
		final Range range = table.getVisibleRange();

		final Receiver<List<DoctorProxy>> callback = new Receiver<List<DoctorProxy>>() {
			@Override
			public void onSuccess(List<DoctorProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
//				idToRow.clear();
//				idToProxy.clear();
//				for (int i = 0, row = range.getStart(); i < values.size(); i++, row++) {
//					DoctorProxy Doctor = values.get(i);
//					@SuppressWarnings("unchecked")
//					// Why is this cast needed?
//					EntityProxyId<DoctorProxy> proxyId = (EntityProxyId<DoctorProxy>) Doctor
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

		fireRangeRequest(q, range, callback);

	}
	
	private void fireRangeRequest(String q, final Range range, final Receiver<List<DoctorProxy>> callback) {
		createRangeRequest(q, range).with(view.getPaths()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}
	
	protected Request<List<DoctorProxy>> createRangeRequest(String q, Range range) {
//		return requests.doctorRequest().findDoctorEntries(range.getStart(), range.getLength());
		return requests.doctorRequestNonRoo().findDoctorsBySearch(q, range.getStart(), range.getLength());
	}

	protected void fireCountRequest(String q, Receiver<Long> callback) {
//		requests.doctorRequest().countDoctors().fire(callback);
		requests.doctorRequestNonRoo().countDoctorsBySearch(q).fire(callback);
	}

	private void setTable(CellTable<DoctorProxy> table) {
		this.table = table;
		
	}

	@Override
	public void newClicked() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void performSearch(String q) {
		Log.debug("Search for " + q);
		init2(q);
	}

	@Override
	public void goTo(Place place) {
		placeControler.goTo(place);
		
	}

}
