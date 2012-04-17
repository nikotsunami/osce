package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleAssignmentsDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.RoleAssignmentsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.RoleAssignmentsViewImpl;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

public class RoleAssignmentsActivity extends AbstractActivity implements RoleAssignmentsView.Presenter, RoleAssignmentsView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private RoleAssignmentsView view;
	private ActivityManager activityManager;
	private RoleAssignmentsDetailsActivityMapper roleAssignmentsDetailsActivityMapper;


	@Inject
	public RoleAssignmentsActivity(OsMaRequestFactory requests, PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;
		roleAssignmentsDetailsActivityMapper = new RoleAssignmentsDetailsActivityMapper(requests, placeController);
		this.activityManager = new ActivityManager(roleAssignmentsDetailsActivityMapper, requests.getEventBus());
	}

	public void onStop(){
		activityManager.setDisplay(null);
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("RoleAssignmentsActivity.start()");
		RoleAssignmentsView roleAssignmentsView = new RoleAssignmentsViewImpl();
		roleAssignmentsView.setPresenter(this);
		this.widget = panel;
		this.view = roleAssignmentsView;
		widget.setWidget(roleAssignmentsView.asWidget());

		init();

		activityManager.setDisplay(view.getDetailsPanel());

		view.setDelegate(this);
	}

	private void init() {
		// TODO implement this!
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public void showSubviewClicked() {
		goTo(new RoleAssignmentsDetailsPlace(Operation.DETAILS));
	}
}
