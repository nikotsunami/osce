package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import ch.unibas.medizin.osce.client.a_nonroo.client.place.TopicsAndSpecDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.TopicsAndSpecView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.TopicsAndSpecViewImpl;
import ch.unibas.medizin.osce.client.style.widgets.ProxySuggestOracle;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.requestfactory.shared.Violation;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

public class TopicsAndSpecActivity extends  AbstractActivity implements TopicsAndSpecView.Presenter, TopicsAndSpecView.Delegate {


	/** Holds the applications request factory */
	private OsMaRequestFactory requests;
	/** Holds the applications placeController */
	private PlaceController placeController;
	/** Holds the applications' activityManager */
	private ActivityManager activityManager;
	/** Holds the panel in which the view will be displayed */
	private AcceptsOneWidget widget;
	/** Holds the main view managed by this activity */
	private TopicsAndSpecView view;
	/** Holds this activities' activityMapper */
	private TopicsAndSpecDetailsActivityMapper topicsAndSpecDetailsActivityMapper;
	

	

	
	/**
	 * go to another place
	 * @param place the place to go to
	 */
	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	/**
	 * Sets the dependencies of this activity and initializes the corresponding activity manager 
	 * @param requests The request factory to use
	 * @param placeController the place controller to use
	 */
	@Inject
	public TopicsAndSpecActivity(OsMaRequestFactory requests, PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;
		topicsAndSpecDetailsActivityMapper = new TopicsAndSpecDetailsActivityMapper(requests, placeController);
		this.activityManager = new ActivityManager(topicsAndSpecDetailsActivityMapper, requests.getEventBus());

	}

	/**
	 * Clean up activity on finish (close popups and disable display of current activities view)
	 */
	public void onStop() {
		
		activityManager.setDisplay(null);
	}

	

	/**
	 * Initializes the corresponding views and initializes the tables as well as their
	 * corresponding handlers.
	 */
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("TopicsAndSpecDetailsActivityMapper.start()");
		//By SPEC[Start 
		//StandardizedPatientView systemStartView = new StandardizedPatientViewImpl();
		final TopicsAndSpecView topicsAndSpecView = new TopicsAndSpecViewImpl();
		//By SPEC] End
		topicsAndSpecView.setPresenter(this);
		initSearch();
		this.widget = panel;
		this.view = topicsAndSpecView;
		widget.setWidget(topicsAndSpecView.asWidget());
		activityManager.setDisplay(view.getDetailsPanel());
		view.setDelegate(this);
	}
	
	
	/**
	 * Initializes the search for standardized patients, by first 
	 * executing a count request. Execution is then continued in 
	 * StandardizedPatientCountReceiver
	 */
	@SuppressWarnings("deprecation")
	private void initSearch() {
	
	}
	
	@Override
	public void showSubviewClicked() {
		goTo(new TopicsAndSpecDetailsPlace(Operation.DETAILS));
	}
	
	


	

	
	
}
