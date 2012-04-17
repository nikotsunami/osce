package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.RoleView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.RoleViewImpl;
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

public class RoleActivity extends AbstractActivity implements RoleView.Presenter, RoleView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private RoleView view;
	private ActivityManager activityManager;
	private RoleDetailsActivityMapper roleDetailsActivityMapper;


	@Inject
	public RoleActivity(OsMaRequestFactory requests, PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;
		roleDetailsActivityMapper = new RoleDetailsActivityMapper(requests, placeController);
		this.activityManager = new ActivityManager(roleDetailsActivityMapper, requests.getEventBus());
	}

	public void onStop(){
		activityManager.setDisplay(null);
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("RoleActivity.start()");
		RoleView roleView = new RoleViewImpl();
		roleView.setPresenter(this);
		this.widget = panel;
		this.view = roleView;
		widget.setWidget(roleView.asWidget());

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
		goTo(new RoleDetailsPlace(Operation.DETAILS));
	}
}
