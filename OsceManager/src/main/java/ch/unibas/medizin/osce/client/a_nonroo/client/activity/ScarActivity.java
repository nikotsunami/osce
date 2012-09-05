package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ScarView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ScarViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.request.ScarRequest;
import ch.unibas.medizin.osce.shared.TraitTypes;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
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

/**
 * @author dk
 *
 */
public class ScarActivity extends AbstractActivity implements ScarView.Presenter, ScarView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeControler;
	private AcceptsOneWidget widget;
	private ScarView view;
	private CellTable<ScarProxy> table;
	private SingleSelectionModel<ScarProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	

	public ScarActivity(OsMaRequestFactory requests, PlaceController placeController) {
    	this.requests = requests;
    	this.placeControler = placeController;
    }

	public void onStop(){
		
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		ScarView systemStartView = new ScarViewImpl();
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		setTable(view.getTable());
		
		//by spec
		RecordChangeEvent.register(requests.getEventBus(), (ScarViewImpl)view);
		//by spec
		
		MenuClickEvent.register(requests.getEventBus(), (ScarViewImpl) view);
		
		init();

		// Inherit the view's key provider
		ProvidesKey<ScarProxy> keyProvider = ((AbstractHasData<ScarProxy>) table)
				.getKeyProvider();
		selectionModel = new SingleSelectionModel<ScarProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						ScarProxy selectedObject = selectionModel.getSelectedObject();
						if (selectedObject != null) {
							Log.debug(selectedObject.getBodypart() + " selected!");
							//showDetails(selectedObject);
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
		if (rangeChangeHandler!=null){
			rangeChangeHandler.removeHandler();
		}

		fireCountRequest(q, new Receiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Narben aus der Datenbank: " + response);
				view.getTable().setRowCount(response.intValue(), true);

				onRangeChanged(q);
			}
		});

		rangeChangeHandler = table
				.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {
						ScarActivity.this.onRangeChanged(q);
					}
				});
	}
	

	protected void onRangeChanged(String q) {
		final Range range = table.getVisibleRange();

		final Receiver<List<ScarProxy>> callback = new Receiver<List<ScarProxy>>() {
			@Override
			public void onSuccess(List<ScarProxy> values) {
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
	
	private void fireRangeRequest(String name, final Range range, final Receiver<List<ScarProxy>> callback) {
		createRangeRequest(name, range).with(view.getPaths()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}
	
	protected Request<List<ScarProxy>> createRangeRequest(String name, Range range) {
		//return requests.scarRequest().findScarEntries(range.getStart(), range.getLength());
		return requests.scarRequestNonRoo().findScarEntriesByName(name, range.getStart(), range.getLength());
	}

	protected void fireCountRequest(String name, Receiver<Long> callback) {
		//requests.scarRequest().countScars().fire(callback);
		requests.scarRequestNonRoo().countScarsByName(name).fire(callback);
	}

	private void setTable(CellTable<ScarProxy> table) {
		this.table = table;
	}

	@Override
	public void newClicked(TraitTypes traitType, String name) {
		Log.debug("Add scar");
		ScarRequest scarReq = requests.scarRequest();
		ScarProxy scar = scarReq.create(ScarProxy.class);
		//reques.edit(scar);
		scar.setBodypart(name);
		scar.setTraitType(traitType);

		// Highlight onViolation
		Log.info("Map Size: " + view.getScarMap().size());
		scarReq.persist().using(scar).fire(new OSCEReceiver<Void>(view.getScarMap()){
		// E Highlight onViolation
			@Override
			public void onSuccess(Void arg0) {
				init();
			}
		});
	}
	
	@Override
	public void deleteClicked(ScarProxy scar) {
		requests.scarRequest().remove().using(scar).fire(new Receiver<Void>() {
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
		placeControler.goTo(place);
	}
}
