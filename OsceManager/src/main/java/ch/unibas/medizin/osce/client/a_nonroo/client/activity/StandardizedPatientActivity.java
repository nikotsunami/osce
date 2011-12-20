package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientViewImpl;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;

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

public class StandardizedPatientActivity extends AbstractActivity implements
StandardizedPatientView.Presenter, StandardizedPatientView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private StandardizedPatientView view;
	private CellTable<StandardizedPatientProxy> table;
	private SingleSelectionModel<StandardizedPatientProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	private ActivityManager activityManger;
	private StandardizedPatientDetailsActivityMapper StandardizedPatientDetailsActivityMapper;


	public StandardizedPatientActivity(OsMaRequestFactory requests, PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;
		StandardizedPatientDetailsActivityMapper = new StandardizedPatientDetailsActivityMapper(requests, placeController);
		this.activityManger = new ActivityManager(StandardizedPatientDetailsActivityMapper, requests.getEventBus());
	}

	public void onStop(){
		activityManger.setDisplay(null);
	}
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		StandardizedPatientView systemStartView = new StandardizedPatientViewImpl();
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		setTable(view.getTable());

		eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {

				if (event.getNewPlace() instanceof StandardizedPatientDetailsPlace){
					init();
				}
			}
		});

		init();

		activityManger.setDisplay(view.getDetailsPanel());

		// Inherit the view's key provider
		ProvidesKey<StandardizedPatientProxy> keyProvider = ((AbstractHasData<StandardizedPatientProxy>) table)
				.getKeyProvider();
		selectionModel = new SingleSelectionModel<StandardizedPatientProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel
		.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				StandardizedPatientProxy selectedObject = selectionModel
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

		fireCountRequest(q, new Receiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Patienten aus der Datenbank: " + response);
				view.getTable().setRowCount(response.intValue(), true);

				onRangeChanged(q);
			}

		});

		rangeChangeHandler = table
				.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {
						StandardizedPatientActivity.this.onRangeChanged(q);
					}
				});
	}

	protected void showDetails(StandardizedPatientProxy StandardizedPatient) {

		Log.debug(StandardizedPatient.getName());

		goTo(new StandardizedPatientDetailsPlace(StandardizedPatient.stableId(),
				StandardizedPatientDetailsPlace.Operation.DETAILS));
	}


	protected void onRangeChanged(String q) {
		final Range range = table.getVisibleRange();

		
		
		final Receiver<List<StandardizedPatientProxy>> callback = new Receiver<List<StandardizedPatientProxy>>() {
			@Override
			public void onSuccess(List<StandardizedPatientProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
				table.setRowData(range.getStart(), values);

				// finishPendingSelection();
				if (widget != null) {
					widget.setWidget(view.asWidget());
				}
			}
		};

		fireRangeRequest(q, range, callback);

	}

	private void fireRangeRequest(String q, final Range range, final Receiver<List<StandardizedPatientProxy>> callback) {
		createRangeRequest(q, range).with(view.getPaths()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}
	
	//TODO: ###SIEBERS### implement findPatienBySearchAndSort
	/*
	 * Attributes
	 * String sortColumn - name of the column to sort, at the moment only name, prename, email
	 * String q, Range range - the same like now
	 * String[] searchTrough - Stringarray with attributenames which should be searched, like 'name', 'prename'
	 * 
	 * Integer[][][][] searchKriteria
	 * 		- first value is the ID of the Entity
	 * 		- second value is if it should be AND or OR
	 * 		- third value is the type, possible tipes are: anamnesisCheck, scar, birthday, gender, height, weight, bmi, nationality, profession, spoken_language 
	 * 		- forth value is the comparator: 0-3   (<, >, =, like)
	 * String [] searchedValue - possible values
	 * 		* anamnesisCheck - 'string', '0|0|0|1|0', '0', '0|1|0|1|0'. The last one is special, if the type 
	 * is QuestionMultM the value '0|0|0|1|0' is true for '0|1|0|1|0'.
	 * 		* scar 0 or 1 
	 * 		*birthday 22.12.1978, 1978
	 * 		*gender 0 or 1 (male, female)
	 * 		*height 180
	 * 		*weight 90
	 * 		*bmi 30
	 * 		*nationality only id
	 * 		*profession only id
	 * 		*spoken_language A1, B2, native
	 */

	protected Request<List<StandardizedPatientProxy>> createRangeRequest(String q, Range range) {
//		return requests.standardizedPatientRequest().findStandardizedPatientEntries(range.getStart(), range.getLength());
		return requests.standardizedPatientRequestNonRoo().findPatientsBySearch(q, range.getStart(), range.getLength());
	}

	protected void fireCountRequest(String q, Receiver<Long> callback) {
//		requests.standardizedPatientRequest().countStandardizedPatients().fire(callback);
		requests.standardizedPatientRequestNonRoo().countPatientsBySearch(q).fire(callback);
	}

	private void setTable(CellTable<StandardizedPatientProxy> table) {
		this.table = table;
	}

	@Override
	public void newClicked() {
		Log.info("create clicked");
		placeController.goTo(new StandardizedPatientDetailsPlace(StandardizedPatientDetailsPlace.Operation.CREATE));
	}
	
	@Override
	public void performSearch(String q) {
		Log.debug("Search for " + q);
		init2(q);
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

}
