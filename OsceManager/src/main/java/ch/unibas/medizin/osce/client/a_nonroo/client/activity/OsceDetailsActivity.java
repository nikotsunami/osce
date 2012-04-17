package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.OsceDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.OscePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.OsceDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.OsceDetailsViewImpl;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyListPlace;
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

/**
 * @author dk
 *
 */
public class OsceDetailsActivity extends AbstractActivity implements
OsceDetailsView.Presenter, OsceDetailsView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private OsceDetailsView view;
	private CellTable<OsceProxy> table;
	private SingleSelectionModel<OsceProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;

	private OsceProxy osceProxy;
	private OsceDetailsPlace place;

	public OsceDetailsActivity(OsceDetailsPlace place,
			OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;
	}

	public void onStop() {

	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("OsceDetailsActivity.start()");
		OsceDetailsView osceDetailsView = new OsceDetailsViewImpl();
		osceDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = osceDetailsView;
		widget.setWidget(osceDetailsView.asWidget());

		view.setDelegate(this);

		requests.find(place.getProxyId()).fire(new Receiver<Object>() {

			public void onFailure(ServerFailure error) {
				Log.error(error.getMessage());
			}

			@Override
			public void onSuccess(Object response) {
				if (response instanceof OsceProxy) {
					Log.info("loaded osce with id " + ((OsceProxy) response).getId());
					init((OsceProxy) response);
				}

			}
		});

	}

	private void init(OsceProxy osceProxy) {
		this.osceProxy = osceProxy;

		view.setValue(osceProxy);

		view.setDelegate(this);
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);

	}

	@Override
	public void editClicked() {
		Log.info("edit clicked");
		goTo(new OsceDetailsPlace(osceProxy.stableId(),
				Operation.EDIT));

	}

	@Override
	public void deleteClicked() {
		if (!Window.confirm("Really delete this entry? You cannot undo this change.")) {
			return;
		}
		requests.osceRequest().remove().using(osceProxy).fire(new Receiver<Void>() {

			public void onSuccess(Void ignore) {
				if (widget == null) {
					return;
				}
				placeController.goTo(new OscePlace("OscePlace!DELETED"));
			}
		});
	}
}
