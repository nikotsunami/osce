package ch.unibas.medizin.osce.client.a_nonroo.client.activity;



import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomMaterialsDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.TopicsAndSpecDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoomMaterialsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoomMaterialsViewImpl;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class RoomMaterialsActivity extends AbstractActivity implements RoomMaterialsView.Presenter,RoomMaterialsView.Delegate{

	/** Holds the applications request factory */
	private OsMaRequestFactory requests;
	/** Holds the applications placeController */
	private PlaceController placeController;
	/** Holds the applications' activityManager */
	private ActivityManager activityManger;
	/** Holds the panel in which the view will be displayed */
	private AcceptsOneWidget widget;
	/** Holds the main view managed by this activity */
	private RoomMaterialsView view;
	/** Holds this activities' activityMapper */
	private RoomMaterialsDetailsActivityMapper RoomMaterialsDetailsActivityMapper;
	
	
	public RoomMaterialsActivity(OsMaRequestFactory requests, PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;
		RoomMaterialsDetailsActivityMapper = new RoomMaterialsDetailsActivityMapper(requests, placeController);
		this.activityManger = new ActivityManager(RoomMaterialsDetailsActivityMapper, requests.getEventBus());
	}
	
	/**
	 * Clean up activity on finish (close popups and disable display of current activities view)
	 */
	public void onStop() {
		
		activityManger.setDisplay(null);
	}
	/**
	 * Initializes the corresponding views and initializes the tables as well as their
	 * corresponding handlers.
	 */
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("RoomMaterialsActivity.start()");
		//By SPEC[Start 
		//StandardizedPatientView systemStartView = new StandardizedPatientViewImpl();
		final RoomMaterialsView roomMaterialsView = new RoomMaterialsViewImpl();
		//By SPEC] End
		roomMaterialsView.setPresenter(this);

		this.widget = panel;
		this.view = roomMaterialsView;
		widget.setWidget(roomMaterialsView.asWidget());
		

		

	
		

		
		initSearch();
		
		activityManger.setDisplay(view.getDetailsPanel());
		
	

	
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
	
	
	/**
	 * go to another place
	 * @param place the place to go to
	 */
	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}
		
	@Override
	public void showSubviewClicked() {
		goTo(new RoomMaterialsDetailsPlace(Operation.DETAILS));
	}

}
