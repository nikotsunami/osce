package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.LogView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.LogViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.managed.request.LogEntryProxy;

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
public class LogActivity extends AbstractActivity implements LogView.Presenter, LogView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeControler;
	private AcceptsOneWidget widget;
	private LogView view;
	private CellTable<LogEntryProxy> table;
	private SingleSelectionModel<LogEntryProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;


	public LogActivity(OsMaRequestFactory requests, PlaceController placeController) {
		this.requests = requests;
		this.placeControler = placeController;
	}

	public void onStop(){

	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		LogView systemStartView = new LogViewImpl();
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		setTable(view.getTable());
		
		//by spec
		RecordChangeEvent.register(requests.getEventBus(), (LogViewImpl)view);
		//by spec
		MenuClickEvent.register(requests.getEventBus(), (LogViewImpl)view);

		init();

		// Inherit the view's key provider
		ProvidesKey<LogEntryProxy> keyProvider = ((AbstractHasData<LogEntryProxy>) table)
				.getKeyProvider();
		selectionModel = new SingleSelectionModel<LogEntryProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel
		.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				LogEntryProxy selectedObject = selectionModel.getSelectedObject();
				if (selectedObject != null) {
					Log.debug(selectedObject.getId() + " selected!");
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
				Log.debug("Geholte Log-Einträge aus der Datenbank: " + response);
				view.getTable().setRowCount(response.intValue(), true);

				onRangeChanged(q);
			}

		});

		rangeChangeHandler = table
				.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {
						LogActivity.this.onRangeChanged(q);
					}
				});
	}


	protected void onRangeChanged(String q) {
		final Range range = table.getVisibleRange();

		final Receiver<List<LogEntryProxy>> callback = new Receiver<List<LogEntryProxy>>() {
			@Override
			public void onSuccess(List<LogEntryProxy> values) {
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

	private void fireRangeRequest(String name, final Range range, final Receiver<List<LogEntryProxy>> callback) {
		createRangeRequest(name, range).with(view.getPaths()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}

	protected Request<List<LogEntryProxy>> createRangeRequest(String name, Range range) {
		//return requests.LogEntryRequest().findScarEntries(range.getStart(), range.getLength());
		return requests.logEntryRequestNonRoo().findLogEntriesByName(name, range.getStart(), range.getLength());
	}

	protected void fireCountRequest(String name, Receiver<Long> callback) {
		//requests.LogEntryRequest().countScars().fire(callback);
		requests.logEntryRequestNonRoo().countLogEntriesByName(name).fire(callback);
	}

	private void setTable(CellTable<LogEntryProxy> table) {
		this.table = table;
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
