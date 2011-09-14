package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.NationalityDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.NationalityView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.NationalityViewImpl;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;

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

public class NationalityActivity extends AbstractActivity implements
NationalityView.Presenter, NationalityView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeControler;
	private AcceptsOneWidget widget;
	private NationalityView view;
	private CellTable<NationalityProxy> table;
	private SingleSelectionModel<NationalityProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	private ActivityManager activityManger;
	private NationalityDetailsActivityMapper nationalityDetailsActivityMapper;
	

	public NationalityActivity(OsMaRequestFactory requests, PlaceController placeController) {
    	this.requests = requests;
    	this.placeControler = placeController;
    	nationalityDetailsActivityMapper = new NationalityDetailsActivityMapper(requests, placeController);
		this.activityManger = new ActivityManager(nationalityDetailsActivityMapper, requests.getEventBus());
    }

	public void onStop(){
		activityManger.setDisplay(null);
	}
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		NationalityView systemStartView = new NationalityViewImpl();
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

		activityManger.setDisplay(view.getDetailsPanel());

		// Inherit the view's key provider
		ProvidesKey<NationalityProxy> keyProvider = ((AbstractHasData<NationalityProxy>) table)
				.getKeyProvider();
		selectionModel = new SingleSelectionModel<NationalityProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						NationalityProxy selectedObject = selectionModel
								.getSelectedObject();
						if (selectedObject != null) {
							Log.debug(selectedObject.getNationality()
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
						NationalityActivity.this.onRangeChanged();
					}
				});
	}
	
	protected void showDetails(NationalityProxy nationality) {
		
		Log.debug(nationality.getNationality());
		
		goTo(new NationalityDetailsPlace(nationality.stableId(),
				NationalityDetailsPlace.Operation.DETAILS));
	}
	

	protected void onRangeChanged() {
		final Range range = table.getVisibleRange();

		final Receiver<List<NationalityProxy>> callback = new Receiver<List<NationalityProxy>>() {
			@Override
			public void onSuccess(List<NationalityProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
//				idToRow.clear();
//				idToProxy.clear();
//				for (int i = 0, row = range.getStart(); i < values.size(); i++, row++) {
//					NationalityProxy nationality = values.get(i);
//					@SuppressWarnings("unchecked")
//					// Why is this cast needed?
//					EntityProxyId<NationalityProxy> proxyId = (EntityProxyId<NationalityProxy>) nationality
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
			final Receiver<List<NationalityProxy>> callback) {
		createRangeRequest(range).with(view.getPaths()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}
	
	protected Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.NationalityProxy>> createRangeRequest(
			Range range) {
		return requests.nationalityRequest().findNationalityEntries(range.getStart(), range.getLength());
	}

	protected void fireCountRequest(Receiver<Long> callback) {
		requests.nationalityRequest()
				.countNationalitys().fire(callback);
	}

	private void setTable(CellTable<NationalityProxy> table) {
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
