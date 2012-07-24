package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.Iterator;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AdministratorDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AdministratorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AdministratorEditView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AdministratorEditViewImpl;
import ch.unibas.medizin.osce.client.managed.request.AdministratorProxy;
import ch.unibas.medizin.osce.client.managed.request.AdministratorRequest;
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

public class AdministratorEditActivity extends AbstractActivity implements
		AdministratorEditView.Presenter, AdministratorEditView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private AdministratorEditView view;
	private AdministratorDetailsPlace place;

	private RequestFactoryEditorDriver<AdministratorProxy, AdministratorEditViewImpl> editorDriver;
	private AdministratorProxy administrator;
	private boolean save;

	public AdministratorEditActivity(AdministratorDetailsPlace place,
			OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;

	}

	public AdministratorEditActivity(AdministratorDetailsPlace place,
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
		if (!save && changed())
			return "Changes will be discarded!";
		else
			return null;
	}
	
	// use this to check if some value has changed since editing has started
	private boolean changed() {
		return editorDriver != null && editorDriver.flush().isChanged();
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {

		Log.info("start");
		AdministratorEditView administratorEditView = new AdministratorEditViewImpl();

		this.widget = panel;
		this.view = administratorEditView;
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
			requests.find(place.getProxyId()).with("administrator")
					.fire(new Receiver<Object>() {

						public void onFailure(ServerFailure error) {
							Log.error(error.getMessage());
						}

						@Override
						public void onSuccess(Object response) {
							if (response instanceof AdministratorProxy) {
								Log.info(((AdministratorProxy) response).getName());
								// init((AdministratorProxy) response);
								administrator = (AdministratorProxy) response;
								init();
							}

						}
					});
		} else {

			Log.info("new Administrator");
			// administratorPlace.setProxyId(administrator.stableId());
			init();
		}
		// view.initialiseDriver(requests);
		widget.setWidget(administratorEditView.asWidget());
		// setTable(view.getTable());

	}

	private void init() {

		AdministratorRequest request = requests.administratorRequest();

		if (administrator == null) {

			AdministratorProxy administrator = request.create(AdministratorProxy.class);
			this.administrator = administrator;
			view.setEditTitle(false);

		} else {

			view.setEditTitle(true);
		}

		Log.info("edit");

		Log.info("persist");
		request.persist().using(administrator);
		editorDriver.edit(administrator, request);

		Log.info("flush");
		editorDriver.flush();
		Log.debug("Create für: " + administrator.getName());
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);

	}

	@Override
	public void cancelClicked() {
		if (this.place.getOperation() == Operation.EDIT)
			placeController.goTo(new AdministratorDetailsPlace(administrator.stableId(),
					Operation.DETAILS));
		else
			placeController.goTo(new AdministratorPlace("AdministratorPlace!CANCEL"));

	}

	@Override
	public void saveClicked() {
		Log.info("saveClicked");
		
		// Highlight onViolation
		Log.info("Map Size: " + view.getAdministratorMap().size());
		editorDriver.flush().fire(new OSCEReceiver<Void>(view.getAdministratorMap()) {
		// E Highlight onViolation
			public void onFailure(ServerFailure error) {
				Log.error(error.getMessage());

			}
		// Highlight onViolation
	
		/*	@Override
			public void onViolation(Set<Violation> errors) {
				Iterator<Violation> iter = errors.iterator();
				String message = "";
				while (iter.hasNext()) {
					message += iter.next().getMessage() + "<br>";
				}
				Log.warn(" in Administrator -" + message);

				// TODO mcAppFactory.getErrorPanel().setErrorMessage(message);

			}
*/
			// E Highlight onViolation
			@Override
			public void onSuccess(Void response) {
				Log.info("Administrator successfully saved.");

				save = true;

				placeController.goTo(new AdministratorDetailsPlace(administrator.stableId(),
						Operation.DETAILS));
			}

		});

	}

}
