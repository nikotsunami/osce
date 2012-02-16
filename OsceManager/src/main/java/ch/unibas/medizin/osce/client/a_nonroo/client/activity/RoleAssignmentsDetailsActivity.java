package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleAssignmentsDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.RoleAssignmentsDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.RoleAssignmentsDetailsViewImpl;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

/**
 * @author dk
 *
 */
public class RoleAssignmentsDetailsActivity extends AbstractActivity implements
RoleAssignmentsDetailsView.Presenter, RoleAssignmentsDetailsView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private RoleAssignmentsDetailsView view;

	private RoleAssignmentsDetailsPlace place;

	public RoleAssignmentsDetailsActivity(RoleAssignmentsDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;
	}

	public void onStop() {

	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("RoleAssignmentsDetailsActivity.start()");
		RoleAssignmentsDetailsView roleAssignmentsDetailsView = new RoleAssignmentsDetailsViewImpl();
		roleAssignmentsDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = roleAssignmentsDetailsView;
		widget.setWidget(roleAssignmentsDetailsView.asWidget());
		
		init();

		view.setDelegate(this);
	}

	private void init() {
		// TODO implement this!
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}
}
