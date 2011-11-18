package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.Iterator;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckDetailsPlace.Operation;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisCheckEditView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisCheckEditViewImpl;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckRequest;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.requestfactory.shared.Violation;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class AnamnesisCheckEditActivity extends AbstractActivity implements
AnamnesisCheckEditView.Presenter, AnamnesisCheckEditView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private AnamnesisCheckEditView view;
	private AnamnesisCheckDetailsPlace place;

	private RequestFactoryEditorDriver<AnamnesisCheckProxy, AnamnesisCheckEditViewImpl> editorDriver;
	private AnamnesisCheckProxy anamnesisCheck;
	private boolean save;

	public AnamnesisCheckEditActivity(AnamnesisCheckDetailsPlace place,
			OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;
	}

	public AnamnesisCheckEditActivity(AnamnesisCheckDetailsPlace place,
			OsMaRequestFactory requests, PlaceController placeController,
			Operation operation) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;
		// this.operation=operation;
	}

	public void onStop() {

	}

	@Override
	public String mayStop() {
		if (!save)
			return "Changes will be discarded!";
		else
			return null;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {

		Log.info("start");
		AnamnesisCheckEditView anamnesisCheckEditView = new AnamnesisCheckEditViewImpl();

		this.widget = panel;
		this.view = anamnesisCheckEditView;
		editorDriver = view.createEditorDriver();

		view.setDelegate(this);

		eventBus.addHandler(PlaceChangeEvent.TYPE,
				new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {

				// updateSelection(event.getNewPlace());
				// TODO implement
			}
		});
		// init();

		if (this.place.getOperation() == AnamnesisCheckDetailsPlace.Operation.EDIT) {
			Log.info("edit");
			requests.find(place.getProxyId()).with("anamnesisForm")
			.fire(new Receiver<Object>() {

				public void onFailure(ServerFailure error) {
					Log.error(error.getMessage());
				}

				@Override
				public void onSuccess(Object response) {
					if (response instanceof AnamnesisCheckProxy) {
						Log.info(((AnamnesisCheckProxy) response).getId().toString());
						// init((AnamnesisCheckProxy) response);
						anamnesisCheck = (AnamnesisCheckProxy) response;
						init();
					}

				}
			});
		} else {

			Log.info("new AnamnesisCheck");
			// anamnesisCheckPlace.setProxyId(anamnesisCheck.stableId());
			init();
		}
		// view.initialiseDriver(requests);
		widget.setWidget(anamnesisCheckEditView.asWidget());
		// setTable(view.getTable());
	}

	private void init() {

		AnamnesisCheckRequest request = requests.anamnesisCheckRequest();

		if (anamnesisCheck == null) {

			AnamnesisCheckProxy anamnesisCheck = request.create(AnamnesisCheckProxy.class);
			this.anamnesisCheck = anamnesisCheck;
			view.setEditTitle(false);

		} else {

			view.setEditTitle(true);
		}

		Log.info("edit");

		Log.info("persist");
		request.persist().using(anamnesisCheck);
		editorDriver.edit(anamnesisCheck, request);

		Log.info("flush");
		editorDriver.flush();
		Log.debug("Create für: " + anamnesisCheck.getId());
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public void cancelClicked() {
		if (this.place.getOperation() == AnamnesisCheckDetailsPlace.Operation.EDIT)
			placeController.goTo(new AnamnesisCheckDetailsPlace(anamnesisCheck.stableId(),
					AnamnesisCheckDetailsPlace.Operation.DETAILS));
		else
			placeController.goTo(new AnamnesisCheckPlace("AnamnesisCheckPlace!CANCEL"));
	}

	@Override
	public void saveClicked() {
		Log.info("saveClicked");

		editorDriver.flush().fire(new Receiver<Void>() {

			public void onFailure(ServerFailure error) {
				Log.error(error.getMessage());

			}

			@Override
			public void onViolation(Set<Violation> errors) {
				Iterator<Violation> iter = errors.iterator();
				String message = "";
				while (iter.hasNext()) {
					message += iter.next().getMessage() + "<br>";
				}
				Log.warn(" in AnamnesisCheck -" + message);

				// TODO mcAppFactory.getErrorPanel().setErrorMessage(message);

			}

			@Override
			public void onSuccess(Void response) {
				Log.info("AnamnesisCheck successfully saved.");

				save = true;

				placeController.goTo(new AnamnesisCheckDetailsPlace(anamnesisCheck.stableId(),
						AnamnesisCheckDetailsPlace.Operation.DETAILS));
			}
		});
	}
}
