package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.RoomEditPopupViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.RoomView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.RoomViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.managed.request.RoomProxy;
import ch.unibas.medizin.osce.client.managed.request.RoomRequest;
import ch.unibas.medizin.osce.shared.TraitTypes;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
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
public class RoomActivity extends AbstractActivity implements RoomView.Presenter, RoomView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeControler;
	private AcceptsOneWidget widget;
	private RoomView view;
	private CellTable<RoomProxy> table;
	private SingleSelectionModel<RoomProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;


	public RoomActivity(OsMaRequestFactory requests, PlaceController placeController) {
		this.requests = requests;
		this.placeControler = placeController;
	}

	public void onStop(){

	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		RoomView systemStartView = new RoomViewImpl();
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		setTable(view.getTable());

		//by spec
		RecordChangeEvent.register(requests.getEventBus(), (RoomViewImpl)view);
		//by spec

		init();

		// Inherit the view's key provider
		ProvidesKey<RoomProxy> keyProvider = ((AbstractHasData<RoomProxy>) table)
				.getKeyProvider();
		selectionModel = new SingleSelectionModel<RoomProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel
		.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				RoomProxy selectedObject = selectionModel.getSelectedObject();
				if (selectedObject != null) {
					Log.debug(selectedObject.getRoomNumber() + " selected!");
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
				Log.debug("Geholte Räume aus der Datenbank: " + response);
				view.getTable().setRowCount(response.intValue(), true);

				onRangeChanged(q);
			}

		});

		rangeChangeHandler = table
				.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {
						RoomActivity.this.onRangeChanged(q);
					}
				});
	}


	protected void onRangeChanged(String q) {
		final Range range = table.getVisibleRange();

		final Receiver<List<RoomProxy>> callback = new Receiver<List<RoomProxy>>() {
			@Override
			public void onSuccess(List<RoomProxy> values) {
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

	private void fireRangeRequest(String name, final Range range, final Receiver<List<RoomProxy>> callback) {
		createRangeRequest(name, range).with(view.getPaths()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}

	protected Request<List<RoomProxy>> createRangeRequest(String name, Range range) {
		//return requests.RoomRequest().findScarEntries(range.getStart(), range.getLength());
		return requests.roomRequestNonRoo().findRoomEntriesByName(name, range.getStart(), range.getLength());
	}

	protected void fireCountRequest(String name, Receiver<Long> callback) {
		//requests.RoomRequest().countScars().fire(callback);
		requests.roomRequestNonRoo().countRoomsByName(name).fire(callback);
	}

	private void setTable(CellTable<RoomProxy> table) {
		this.table = table;
	}

	@Override
	public void newClicked(String name, double length, double width) {
		Log.debug("Add room");
		RoomRequest roomReq = requests.roomRequest();
		RoomProxy room = roomReq.create(RoomProxy.class);
		room.setRoomNumber(name);
		room.setLength(length);
		room.setWidth(width);
		// Highlight onViolation
		Log.info("Map Size: " + view.getNewRoomMap().size());
		roomReq.persist().using(room).fire(new OSCEReceiver<Void>(view.getNewRoomMap()){
			// E Highlight onViolation
			@Override
			public void onSuccess(Void arg0) {
				init();
			}
		});
	}

	@Override
	public void deleteClicked(RoomProxy room) {
		requests.roomRequest().remove().using(room).fire(new Receiver<Void>() {
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

	@Override
	public void editClicked(RoomProxy proxy, String name, double length, double width) {
		// TODO Auto-generated method stub
		
		RoomRequest roomrequest = requests.roomRequest();
		proxy = roomrequest.edit(proxy);
		
		proxy.setRoomNumber(name);
		proxy.setLength(length);
		proxy.setWidth(width);
		
		// Highlight onViolation
		Log.info("Map Size: " + view.getRoomMap().size());
		roomrequest.persist().using(proxy).fire(new OSCEReceiver<Void>(view.getRoomMap()) {
			// E Highlight onViolation
			@Override
			public void onSuccess(Void arg0) {
				// TODO Auto-generated method stub
				init();
				// Highlight onViolation
				((RoomEditPopupViewImpl)view.getRoomEditPopView()).hide();				
				// E Highlight onViolation
				Log.info("~~Record Updated Successfully");
			}
		});
		
	}
}
