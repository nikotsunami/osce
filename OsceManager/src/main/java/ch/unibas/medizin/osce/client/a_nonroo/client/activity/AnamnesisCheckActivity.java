package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisCheckView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisCheckViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.VisibleRange;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.shared.Operation;

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

public class AnamnesisCheckActivity extends AbstractActivity implements
		AnamnesisCheckView.Presenter, AnamnesisCheckView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AnamnesisCheckPlace place;
	private AcceptsOneWidget widget;
	private AnamnesisCheckView view;
	private CellTable<AnamnesisCheckProxy> table;
	private SingleSelectionModel<AnamnesisCheckProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	private ActivityManager activityManger;
	private AnamnesisCheckDetailsActivityMapper anamnesisCheckDetailsActivityMapper;

	private static final String placeToken = "AnamnesisCheckPlace";
	private static final String tempValue = "&&&*(&";

	private String listSelectedValue = "10";

	public AnamnesisCheckActivity(OsMaRequestFactory requests,
			PlaceController placeController, AnamnesisCheckPlace place) {
		this.requests = requests;
		this.placeController = placeController;
		anamnesisCheckDetailsActivityMapper = new AnamnesisCheckDetailsActivityMapper(
				requests, placeController);
		this.activityManger = new ActivityManager(
				anamnesisCheckDetailsActivityMapper, requests.getEventBus());
		this.place = place;
	}

	/**
	 * Called when the activity stops
	 */
	public void onStop() {
		activityManger.setDisplay(null);
	}

	/**
	 * The activity has started
	 */
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");

		AnamnesisCheckView systemStartView = getAnamnesisCheckView();
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;

		widget.setWidget(systemStartView.asWidget());
		setTable(view.getTable());

		init();

		activityManger.setDisplay(view.getDetailsPanel());

		// Inherit the view's key provider
		ProvidesKey<AnamnesisCheckProxy> keyProvider = ((AbstractHasData<AnamnesisCheckProxy>) table)
				.getKeyProvider();
		selectionModel = new SingleSelectionModel<AnamnesisCheckProxy>(
				keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						AnamnesisCheckProxy selectedObject = selectionModel
								.getSelectedObject();
						if (selectedObject != null) {
							Log.debug(selectedObject.getId() + " selected!");
							showDetails(selectedObject);
						}
					}
				});

		view.setDelegate(this);
		view.setSearchFocus(true);
	}

	private void init() {

		setStartAndVisiableRange(place.getPageStart(), place.getPageLen());

		if (place.getSearchStr().equals(place.DEFAULT_SEARCHSTR)) {
			view.setSearchBoxShown(place.DEFAULT_SEARCHSTR);
			init2("");
			view.setSearchFocus(false);
		} else if (place.getSearchStr().equals("")) {
			view.setSearchBoxShown(place.DEFAULT_SEARCHSTR);
			init2("");
			view.setSearchFocus(true);
		} else {
			view.setSearchBoxShown(place.getSearchStr());
			init2(view.getSearchBoxShown());
		}

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
				view.setListBoxItem(place.getPageLen());
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

	/**
	 * Called from the table selection handler
	 * 
	 * @param AnamnesisCheck
	 */
	protected void showDetails(AnamnesisCheckProxy AnamnesisCheck) {

		Log.debug(AnamnesisCheck.getId().toString());

		goTo(new AnamnesisCheckDetailsPlace(AnamnesisCheck.stableId(),
				Operation.DETAILS));
	}

	protected void onRangeChanged(final String q) {

		final Range range = table.getVisibleRange();

		final Receiver<List<AnamnesisCheckProxy>> callback = new Receiver<List<AnamnesisCheckProxy>>() {
			@Override
			public void onSuccess(List<AnamnesisCheckProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
				table.setRowData(range.getStart(), values);

				if (widget != null) {
					widget.setWidget(view.asWidget());
				}

			}
		};

		fireRangeRequest(q, range, callback);
	}

	@Override
	public void moveUp(AnamnesisCheckProxy proxy) {
		requests.anamnesisCheckRequestNonRoo().moveUp().using(proxy)
				.fire(new Receiver<Void>() {
					@Override
					public void onSuccess(Void response) {
						Log.info("moved");
						init();

					}
				});
	}

	@Override
	public void moveDown(AnamnesisCheckProxy proxy) {
		requests.anamnesisCheckRequestNonRoo().moveDown().using(proxy)
				.fire(new Receiver<Void>() {
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

	private void fireRangeRequest(String q, final Range range,
			final Receiver<List<AnamnesisCheckProxy>> callback) {
		createRangeRequest(q, range).with(view.getPaths()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}

	protected Request<java.util.List<AnamnesisCheckProxy>> createRangeRequest(
			String q, Range range) {
		// return
		// requests.anamnesisCheckRequest().findAnamnesisCheckEntries(range.getStart(),
		// range.getLength());
		return requests.anamnesisCheckRequestNonRoo()
				.findAnamnesisChecksBySearch(q, range.getStart(),
						range.getLength());
	}

	protected void fireCountRequest(String q, Receiver<Long> callback) {
		// requests.anamnesisCheckRequest().countAnamnesisChecks().fire(callback);
		requests.anamnesisCheckRequestNonRoo().countAnamnesisChecksBySearch(q)
				.fire(callback);
	}

	private void setTable(CellTable<AnamnesisCheckProxy> table) {
		this.table = table;
	}

	@Override
	public void newClicked() {
		goTo(new AnamnesisCheckDetailsPlace(Operation.CREATE));
	}

	@Override
	public void performSearch(String q) {
		Log.debug("Search for " + q);

		init2(q);

		goTo(new AnamnesisCheckPlace(placeToken, table.getVisibleRange()
				.getStart(), listSelectedValue, q));

	}

	@Override
	public void goTo(Place place) {

		placeController.goTo(place);

	}

	@Override
	public void changeNumRowShown(String selectedValue) {

		listSelectedValue = selectedValue;

		goTo(new AnamnesisCheckPlace(placeToken, table.getVisibleRange()
				.getStart(), selectedValue, view.getSearchBoxShown()));
	}

	/**
	 * Find matching VisibleRange enum value and set "table" range keeping the
	 * start value from the original range
	 **/
	public void findVisiableRange(String selectedValue) {
		int start = table.getPageStart();
		setStartAndVisiableRange(start, selectedValue);
	}

	/**
	 * Find matching VisibleRange enum value and set "table" range keeping the
	 * start value from the original range
	 **/
	public void setStartAndVisiableRange(final int start, String selectedValue) {

		if (VisibleRange.ALL.getName().equals(selectedValue)) {

			// Show all the rows.

			fireCountRequest("", new Receiver<Long>() {

				@Override
				public void onSuccess(Long response) {

					int rows = response.intValue();

					Range range = new Range(start, rows);
					table.setVisibleRange(range);

				}

			});

			return;
		} else {

			VisibleRange selectedRange = null;

			for (VisibleRange range : VisibleRange.values()) {
				if (range.getName().equals(selectedValue)) {
					selectedRange = range;

				}
			}
			if (selectedRange != null) {
				Range range = new Range(start, selectedRange.getValue());
				table.setVisibleRange(range);
			}
		}
	}

	static AnamnesisCheckView systemStartViewS = null;

	private static AnamnesisCheckView getAnamnesisCheckView() {
		if (systemStartViewS == null) {
			systemStartViewS = new AnamnesisCheckViewImpl();
		}
		return systemStartViewS;
	}
}
