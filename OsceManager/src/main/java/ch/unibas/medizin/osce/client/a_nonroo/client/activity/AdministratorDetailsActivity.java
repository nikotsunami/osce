package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AdministratorDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AdministratorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AdministratorDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AdministratorDetailsViewImpl;
import ch.unibas.medizin.osce.client.managed.request.AdministratorProxy;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.SingleSelectionModel;

public class AdministratorDetailsActivity extends AbstractActivity implements
AdministratorDetailsView.Presenter, AdministratorDetailsView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private AdministratorDetailsView view;
	private CellTable<AdministratorProxy> table;
	private SingleSelectionModel<AdministratorProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;

	private AdministratorProxy administratorProxy;
	private AdministratorDetailsPlace place;

	public AdministratorDetailsActivity(AdministratorDetailsPlace place,
			OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;
	}

	public void onStop() {

	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("AdministratorDetailsActivity.start()");
		AdministratorDetailsView administratorDetailsView = new AdministratorDetailsViewImpl();
		administratorDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = administratorDetailsView;
		widget.setWidget(administratorDetailsView.asWidget());

		view.setDelegate(this);

		requests.find(place.getProxyId()).fire(new Receiver<Object>() {

			public void onFailure(ServerFailure error) {
				Log.error(error.getMessage());
			}

			@Override
			public void onSuccess(Object response) {
				if (response instanceof AdministratorProxy) {
					Log.info(((AdministratorProxy) response).getEmail());
					init((AdministratorProxy) response);
				}

			}
		});

	}

	private void init(AdministratorProxy administratorProxy) {
		this.administratorProxy = administratorProxy;

		view.setValue(administratorProxy);

		view.setDelegate(this);
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);

	}

	@Override
	public void editClicked() {
		Log.info("edit clicked");
		goTo(new AdministratorDetailsPlace(administratorProxy.stableId(),
				Operation.EDIT));

	}

	@Override
	public void deleteClicked() {
		if (!Window.confirm("Really delete this entry? You cannot undo this change.")) {
			return;
		}
		requests.administratorRequest().remove().using(administratorProxy).fire(new Receiver<Void>() {

			public void onSuccess(Void ignore) {
				if (widget == null) {
					return;
				}
				placeController.goTo(new AdministratorPlace("AdministratorPlace!DELETED"));
			}
		});
	}
}
