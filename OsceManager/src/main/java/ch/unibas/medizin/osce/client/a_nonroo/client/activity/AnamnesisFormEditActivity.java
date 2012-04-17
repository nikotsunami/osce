package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.Iterator;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisFormDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisFormPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisFormEditView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisFormEditViewImpl;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormRequest;
import ch.unibas.medizin.osce.shared.Operation;

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

public class AnamnesisFormEditActivity extends AbstractActivity implements
		AnamnesisFormEditView.Presenter, AnamnesisFormEditView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private AnamnesisFormEditView view;
	private AnamnesisFormDetailsPlace place;

	private RequestFactoryEditorDriver<AnamnesisFormProxy, AnamnesisFormEditViewImpl> editorDriver;
	private AnamnesisFormProxy anamnesisForm;
	private boolean save;

	public AnamnesisFormEditActivity(AnamnesisFormDetailsPlace place,
			OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;

	}

	public AnamnesisFormEditActivity(AnamnesisFormDetailsPlace place,
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
		AnamnesisFormEditView anamnesisFormEditView = new AnamnesisFormEditViewImpl();

		this.widget = panel;
		this.view = anamnesisFormEditView;
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

		if (this.place.getOperation() == Operation.EDIT) {
			Log.info("edit");
			requests.find(place.getProxyId()).with("anamnesisForm")
					.fire(new Receiver<Object>() {

						public void onFailure(ServerFailure error) {
							Log.error(error.getMessage());
						}

						@Override
						public void onSuccess(Object response) {
							if (response instanceof AnamnesisFormProxy) {
								Log.info(((AnamnesisFormProxy) response).getId().toString());
								// init((AnamnesisFormProxy) response);
								anamnesisForm = (AnamnesisFormProxy) response;
								init();
							}

						}
					});
		} else {

			Log.info("new AnamnesisForm");
			// anamnesisFormPlace.setProxyId(anamnesisForm.stableId());
			init();
		}
		// view.initialiseDriver(requests);
		widget.setWidget(anamnesisFormEditView.asWidget());
		// setTable(view.getTable());

	}

	private void init() {

		AnamnesisFormRequest request = requests.anamnesisFormRequest();

		if (anamnesisForm == null) {

			AnamnesisFormProxy anamnesisForm = request.create(AnamnesisFormProxy.class);
			this.anamnesisForm = anamnesisForm;
			view.setEditTitle(false);

		} else {

			view.setEditTitle(true);
		}

		Log.info("edit");

		Log.info("persist");
		request.persist().using(anamnesisForm);
		editorDriver.edit(anamnesisForm, request);

		Log.info("flush");
		editorDriver.flush();
		Log.debug("Create für: " + anamnesisForm.getId());
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);

	}

	@Override
	public void cancelClicked() {
		if (this.place.getOperation() == Operation.EDIT)
			placeController.goTo(new AnamnesisFormDetailsPlace(anamnesisForm.stableId(),
					Operation.DETAILS));
		else
			placeController.goTo(new AnamnesisFormPlace("AnamnesisFormPlace!CANCEL"));

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
				Log.warn(" in AnamnesisForm -" + message);

				// TODO mcAppFactory.getErrorPanel().setErrorMessage(message);

			}

			@Override
			public void onSuccess(Void response) {
				Log.info("AnamnesisForm successfully saved.");

				save = true;

				placeController.goTo(new AnamnesisFormDetailsPlace(anamnesisForm.stableId(),
						Operation.DETAILS));
			}

		});

	}

}
