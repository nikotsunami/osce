package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisCheckView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisCheckViewImpl;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;

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

public class AnamnesisCheckActivity extends AbstractActivity implements
AnamnesisCheckView.Presenter, AnamnesisCheckView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private AnamnesisCheckView view;
	private CellTable<AnamnesisCheckProxy> table;
	private SingleSelectionModel<AnamnesisCheckProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	private ActivityManager activityManger;
	private AnamnesisCheckDetailsActivityMapper anamnesisCheckDetailsActivityMapper;
	

	public AnamnesisCheckActivity(OsMaRequestFactory requests, PlaceController placeController) {
    	this.requests = requests;
    	this.placeController = placeController;
    	anamnesisCheckDetailsActivityMapper = new AnamnesisCheckDetailsActivityMapper(requests, placeController);
		this.activityManger = new ActivityManager(anamnesisCheckDetailsActivityMapper, requests.getEventBus());
    }

	public void onStop(){
		activityManger.setDisplay(null);
	}
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		AnamnesisCheckView systemStartView = new AnamnesisCheckViewImpl();
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		setTable(view.getTable());

		eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {
				
				if (event.getNewPlace() instanceof AnamnesisCheckDetailsPlace){
					init();
				}
			}
		});
		
		init();

		activityManger.setDisplay(view.getDetailsPanel());

		// Inherit the view's key provider
		ProvidesKey<AnamnesisCheckProxy> keyProvider = ((AbstractHasData<AnamnesisCheckProxy>) table)
				.getKeyProvider();
		selectionModel = new SingleSelectionModel<AnamnesisCheckProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						AnamnesisCheckProxy selectedObject = selectionModel
								.getSelectedObject();
						if (selectedObject != null) {
							Log.debug(selectedObject.getId()
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
				Log.debug("Geholte Intitution aus der Datenbank: " + response);
				view.getTable().setRowCount(response.intValue(), true);

				onRangeChanged(q);
			}

		});

		if (rangeChangeHandler != null) {
			rangeChangeHandler.removeHandler();
		}
		
		rangeChangeHandler = table
				.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {
						Log.debug("onRangeChange() - " + q);
						AnamnesisCheckActivity.this.onRangeChanged(q);
					}
				});
	}
	
	protected void showDetails(AnamnesisCheckProxy AnamnesisCheck) {
		
		Log.debug(AnamnesisCheck.getId().toString());
		
		goTo(new AnamnesisCheckDetailsPlace(AnamnesisCheck.stableId(),
				AnamnesisCheckDetailsPlace.Operation.DETAILS));
	}
	

	protected void onRangeChanged(String q) {
		final Range range = table.getVisibleRange();

		final Receiver<List<AnamnesisCheckProxy>> callback = new Receiver<List<AnamnesisCheckProxy>>() {
			@Override
			public void onSuccess(List<AnamnesisCheckProxy> values) {
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
	
	@Override
	public void moveUp(AnamnesisCheckProxy proxy) {
		requests.anamnesisCheckRequestNonRoo().moveUp().using(proxy).fire(new Receiver<Void>() {
			@Override
			public void onSuccess(Void response) {
				Log.info("moved");
				init();
			}
		});
	}
	
	@Override
	public void moveDown(AnamnesisCheckProxy proxy) {
		requests.anamnesisCheckRequestNonRoo().moveDown().using(proxy).fire(new Receiver<Void>() {
			@Override
			public void onSuccess(Void response) {
				Log.info("moved");
				init();
			}
		});
	}
	
	@Override
	public void deleteClicked(AnamnesisCheckProxy proxy) {
		// TODO implement
	}
	
	private void fireRangeRequest(String q, final Range range, final Receiver<List<AnamnesisCheckProxy>> callback) {
		createRangeRequest(q, range).with(view.getPaths()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}
	
	protected Request<java.util.List<AnamnesisCheckProxy>> createRangeRequest(String q, Range range) {
//		return requests.anamnesisCheckRequest().findAnamnesisCheckEntries(range.getStart(), range.getLength());
		return requests.anamnesisCheckRequestNonRoo().findAnamnesisChecksBySearch(q, range.getStart(), range.getLength());
	}

	protected void fireCountRequest(String q, Receiver<Long> callback) {
//		requests.anamnesisCheckRequest().countAnamnesisChecks().fire(callback);
		requests.anamnesisCheckRequestNonRoo().countAnamnesisChecksBySearch(q).fire(callback);
	}

	private void setTable(CellTable<AnamnesisCheckProxy> table) {
		this.table = table;
	}

	@Override
	public void newClicked() {
		goTo(new AnamnesisCheckDetailsPlace(AnamnesisCheckDetailsPlace.Operation.CREATE));
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
