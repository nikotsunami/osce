package ch.unibas.medizin.osce.client.a_nonroo.client.activity;


import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleScriptTemplateDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleScriptTemplateView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleScriptTemplateViewImpl;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class RoleScriptTemplateActivity extends  AbstractActivity implements RoleScriptTemplateView.Presenter, RoleScriptTemplateView.Delegate{



	/** Holds the applications request factory */
	private OsMaRequestFactory requests;
	/** Holds the applications placeController */
	private PlaceController placeController;
	/** Holds the applications' activityManager */
	private ActivityManager activityManger;
	/** Holds the panel in which the view will be displayed */
	private AcceptsOneWidget widget;
	/** Holds the main view managed by this activity */
	private RoleScriptTemplateView view;
	/** Holds this activities' activityMapper */
	private RoleScriptTemplateDetailsActivityMapper RoleScriptTemplateDetailsActivityMapper;
	

	

	
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
	public RoleScriptTemplateActivity(OsMaRequestFactory requests, PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;
		RoleScriptTemplateDetailsActivityMapper = new RoleScriptTemplateDetailsActivityMapper(requests, placeController);
		this.activityManger = new ActivityManager(RoleScriptTemplateDetailsActivityMapper, requests.getEventBus());
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
		Log.info("RoleScriptTemplateActivity.start()");
		//By SPEC[Start 
		//StandardizedPatientView systemStartView = new StandardizedPatientViewImpl();
		final RoleScriptTemplateView roleScriptTemplateView = new RoleScriptTemplateViewImpl();
		//By SPEC] End
		roleScriptTemplateView.setPresenter(this);

		this.widget = panel;
		this.view = roleScriptTemplateView;
		widget.setWidget(roleScriptTemplateView.asWidget());
		
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
	
	@Override
	public void goToDetailClicked() {
		goTo(new RoleScriptTemplateDetailsPlace(Operation.DETAILS));
	}

	

	
		
}
