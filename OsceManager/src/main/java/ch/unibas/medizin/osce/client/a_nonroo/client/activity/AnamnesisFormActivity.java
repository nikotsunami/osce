package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisFormDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisFormView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisFormViewImpl;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
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

public class AnamnesisFormActivity extends AbstractActivity implements
AnamnesisFormView.Presenter, AnamnesisFormView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeControler;
	private AcceptsOneWidget widget;
	private AnamnesisFormView view;
	private CellTable<AnamnesisFormProxy> table;
	private SingleSelectionModel<AnamnesisFormProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	private ActivityManager activityManger;
	private AnamnesisFormDetailsActivityMapper AnamnesisFormDetailsActivityMapper;
	

	public AnamnesisFormActivity(OsMaRequestFactory requests, PlaceController placeController) {
    	this.requests = requests;
    	this.placeControler = placeController;
    	AnamnesisFormDetailsActivityMapper = new AnamnesisFormDetailsActivityMapper(requests, placeController);
		this.activityManger = new ActivityManager(AnamnesisFormDetailsActivityMapper, requests.getEventBus());
    }

	public void onStop(){
		activityManger.setDisplay(null);
	}
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		AnamnesisFormView systemStartView = new AnamnesisFormViewImpl();
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		setTable(view.getTable());
		
		init();

		activityManger.setDisplay(view.getDetailsPanel());

		// Inherit the view's key provider
		ProvidesKey<AnamnesisFormProxy> keyProvider = ((AbstractHasData<AnamnesisFormProxy>) table)
				.getKeyProvider();
		selectionModel = new SingleSelectionModel<AnamnesisFormProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						AnamnesisFormProxy selectedObject = selectionModel
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
						AnamnesisFormActivity.this.onRangeChanged();
					}
				});
	}
	
	protected void showDetails(AnamnesisFormProxy AnamnesisForm) {
		
		Log.debug(AnamnesisForm.getId().toString());
		
		goTo(new AnamnesisFormDetailsPlace(AnamnesisForm.stableId(),
				Operation.DETAILS));
	}
	

	protected void onRangeChanged() {
		final Range range = table.getVisibleRange();

		final Receiver<List<AnamnesisFormProxy>> callback = new Receiver<List<AnamnesisFormProxy>>() {
			@Override
			public void onSuccess(List<AnamnesisFormProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
//				idToRow.clear();
//				idToProxy.clear();
//				for (int i = 0, row = range.getStart(); i < values.size(); i++, row++) {
//					AnamnesisFormProxy AnamnesisForm = values.get(i);
//					@SuppressWarnings("unchecked")
//					// Why is this cast needed?
//					EntityProxyId<AnamnesisFormProxy> proxyId = (EntityProxyId<AnamnesisFormProxy>) AnamnesisForm
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
			final Receiver<List<AnamnesisFormProxy>> callback) {
		createRangeRequest(range).with(view.getPaths()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}
	
	protected Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy>> createRangeRequest(
			Range range) {
		return requests.anamnesisFormRequest().findAnamnesisFormEntries(range.getStart(), range.getLength());
	}

	protected void fireCountRequest(Receiver<Long> callback) {
		requests.anamnesisFormRequest()
				.countAnamnesisForms().fire(callback);
	}

	private void setTable(CellTable<AnamnesisFormProxy> table) {
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
