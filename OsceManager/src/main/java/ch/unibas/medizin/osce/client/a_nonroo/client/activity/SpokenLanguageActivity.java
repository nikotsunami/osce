package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.SpokenLanguageDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.SpokenLanguageView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.SpokenLanguageViewImpl;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;

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

public class SpokenLanguageActivity extends AbstractActivity implements
SpokenLanguageView.Presenter, SpokenLanguageView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeControler;
	private AcceptsOneWidget widget;
	private SpokenLanguageView view;
	private CellTable<SpokenLanguageProxy> table;
	private SingleSelectionModel<SpokenLanguageProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	private ActivityManager activityManger;
	private SpokenLanguageDetailsActivityMapper SpokenLanguageDetailsActivityMapper;
	

	public SpokenLanguageActivity(OsMaRequestFactory requests, PlaceController placeController) {
    	this.requests = requests;
    	this.placeControler = placeController;
    	SpokenLanguageDetailsActivityMapper = new SpokenLanguageDetailsActivityMapper(requests, placeController);
		this.activityManger = new ActivityManager(SpokenLanguageDetailsActivityMapper, requests.getEventBus());
    }

	public void onStop(){
		activityManger.setDisplay(null);
	}
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		SpokenLanguageView systemStartView = new SpokenLanguageViewImpl();
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
		ProvidesKey<SpokenLanguageProxy> keyProvider = ((AbstractHasData<SpokenLanguageProxy>) table)
				.getKeyProvider();
		selectionModel = new SingleSelectionModel<SpokenLanguageProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						SpokenLanguageProxy selectedObject = selectionModel
								.getSelectedObject();
						if (selectedObject != null) {
							Log.debug(selectedObject.getLanguageName()
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
						SpokenLanguageActivity.this.onRangeChanged();
					}
				});
	}
	
	protected void showDetails(SpokenLanguageProxy SpokenLanguage) {
		
		Log.debug(SpokenLanguage.getLanguageName());
		
		goTo(new SpokenLanguageDetailsPlace(SpokenLanguage.stableId(),
				SpokenLanguageDetailsPlace.Operation.DETAILS));
	}
	

	protected void onRangeChanged() {
		final Range range = table.getVisibleRange();

		final Receiver<List<SpokenLanguageProxy>> callback = new Receiver<List<SpokenLanguageProxy>>() {
			@Override
			public void onSuccess(List<SpokenLanguageProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
//				idToRow.clear();
//				idToProxy.clear();
//				for (int i = 0, row = range.getStart(); i < values.size(); i++, row++) {
//					SpokenLanguageProxy SpokenLanguage = values.get(i);
//					@SuppressWarnings("unchecked")
//					// Why is this cast needed?
//					EntityProxyId<SpokenLanguageProxy> proxyId = (EntityProxyId<SpokenLanguageProxy>) SpokenLanguage
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
			final Receiver<List<SpokenLanguageProxy>> callback) {
		createRangeRequest(range).with(view.getPaths()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}
	
	protected Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy>> createRangeRequest(
			Range range) {
		return requests.spokenLanguageRequest().findSpokenLanguageEntries(range.getStart(), range.getLength());
	}

	protected void fireCountRequest(Receiver<Long> callback) {
		requests.spokenLanguageRequest()
				.countSpokenLanguages().fire(callback);
	}

	private void setTable(CellTable<SpokenLanguageProxy> table) {
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
