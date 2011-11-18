package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.ProfessionDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ProfessionView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ProfessionViewImpl;
import ch.unibas.medizin.osce.client.managed.request.ProfessionProxy;
import ch.unibas.medizin.osce.client.managed.request.ProfessionRequest;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.request.ScarRequest;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
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

public class ProfessionActivity extends AbstractActivity implements
ProfessionView.Presenter, ProfessionView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private ProfessionView view;
	private CellTable<ProfessionProxy> table;
	private SingleSelectionModel<ProfessionProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	private ActivityManager activityManger;
	private ProfessionDetailsActivityMapper ProfessionDetailsActivityMapper;
	

	public ProfessionActivity(OsMaRequestFactory requests, PlaceController placeController) {
    	this.requests = requests;
    	this.placeController = placeController;
    	ProfessionDetailsActivityMapper = new ProfessionDetailsActivityMapper(requests, placeController);
		this.activityManger = new ActivityManager(ProfessionDetailsActivityMapper, requests.getEventBus());
    }

	public void onStop(){
		activityManger.setDisplay(null);
	}
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		ProfessionView systemStartView = new ProfessionViewImpl();
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		setTable(view.getTable());

//		eventBus.addHandler(PlaceChangeEvent.TYPE,
//				new PlaceChangeEvent.Handler() {
//					public void onPlaceChange(PlaceChangeEvent event) {
//						// updateSelection(event.getNewPlace());
//						// TODO implement
//					}
//				});
		init();

//		activityManger.setDisplay(view.getDetailsPanel());

		// Inherit the view's key provider
		ProvidesKey<ProfessionProxy> keyProvider = ((AbstractHasData<ProfessionProxy>) table)
				.getKeyProvider();
		selectionModel = new SingleSelectionModel<ProfessionProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						ProfessionProxy selectedObject = selectionModel
								.getSelectedObject();
						if (selectedObject != null) {
							Log.debug(selectedObject.getProfession()
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

		// fix to avoid having multiple rangeChangeHandlers attached
		if (rangeChangeHandler != null){
			rangeChangeHandler.removeHandler();
		}
		
		fireCountRequest(q, new Receiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Berufe aus der Datenbank: " + response);
				view.getTable().setRowCount(response.intValue(), true);

				onRangeChanged(q);
			}

		});

		rangeChangeHandler = table
				.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {
						ProfessionActivity.this.onRangeChanged(q);
					}
				});
	}
	
	protected void showDetails(ProfessionProxy Profession) {
		
		Log.debug(Profession.getProfession());
		
		goTo(new ProfessionDetailsPlace(Profession.stableId(),
				ProfessionDetailsPlace.Operation.DETAILS));
	}
	

	protected void onRangeChanged(String q) {
		final Range range = table.getVisibleRange();

		final Receiver<List<ProfessionProxy>> callback = new Receiver<List<ProfessionProxy>>() {
			@Override
			public void onSuccess(List<ProfessionProxy> values) {
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
	
	private void fireRangeRequest(String name, final Range range,
			final Receiver<List<ProfessionProxy>> callback) {
		createRangeRequest(name, range).with(view.getPaths()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}
	
	protected Request<List<ProfessionProxy>> createRangeRequest(String name, Range range) {
//		return requests.professionRequest().findProfessionEntries(range.getStart(), range.getLength());
		return requests.professionRequestNonRoo().findProfessionsByName(name, range.getStart(), range.getLength());
	}

	protected void fireCountRequest(String name, Receiver<Long> callback) {
//		requests.professionRequest().countProfessions().fire(callback);
		requests.professionRequestNonRoo().countProfessionsByName(name).fire(callback);
	}

	private void setTable(CellTable<ProfessionProxy> table) {
		this.table = table;
		
	}

	@Override
	public void newClicked(String name) {
		Log.debug("Add profession");
		ProfessionRequest profReq = requests.professionRequest();
		ProfessionProxy profession = profReq.create(ProfessionProxy.class);
		profession.setProfession(name);
		
		profReq.persist().using(profession).fire(new Receiver<Void>(){
			@Override
			public void onSuccess(Void arg0) {
				init();
			}
		});
	}
	
	@Override
	public void deleteClicked(ProfessionProxy prof) {
		requests.professionRequest().remove().using(prof).fire(new Receiver<Void>() {
			public void onSuccess(Void ignore) {
				Log.debug("Sucessfully deleted");
				init();
			}
		});
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
