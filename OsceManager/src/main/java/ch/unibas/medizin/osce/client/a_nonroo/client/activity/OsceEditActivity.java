package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.OsceDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.OscePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.OsceEditView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.OsceEditViewImpl;
import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceRequest;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentOscesProxy;
import ch.unibas.medizin.osce.client.managed.request.TaskProxy;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.StudyYears;

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

public class OsceEditActivity extends AbstractActivity implements
OsceEditView.Presenter, OsceEditView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private OsceEditView view;
	private OsceDetailsPlace place;

	private RequestFactoryEditorDriver<OsceProxy, OsceEditViewImpl> editorDriver;
	private OsceProxy osce;
	private boolean save;

	public OsceEditActivity(OsceDetailsPlace place,
			OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;

	}

	public OsceEditActivity(OsceDetailsPlace place,
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
		this.widget = panel;
		this.view = new OsceEditViewImpl();
		editorDriver = view.createEditorDriver();

		view.setDelegate(this);


		view.setStudyYearPickerValues(Arrays.asList(StudyYears.values()));


		view.setTasksPickerValues(Collections.<TaskProxy>emptyList());
		requests.taskRequest().findTaskEntries(0, 50).with(ch.unibas.medizin.osce.client.managed.ui.TaskProxyRenderer.instance().getPaths()).fire(new Receiver<List<TaskProxy>>() {

			public void onSuccess(List<TaskProxy> response) {
				List<TaskProxy> values = new ArrayList<TaskProxy>();
				values.add(null);
				values.addAll(response);
				view.setTasksPickerValues(values);
			}
		});


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
			requests.find(place.getProxyId()).with("osce")
			.fire(new Receiver<Object>() {

				public void onFailure(ServerFailure error) {
					Log.error(error.getMessage());
				}

				@Override
				public void onSuccess(Object response) {
					if (response instanceof OsceProxy) {
						Log.info("edit osce with id " + ((OsceProxy) response).getId());
						// init((OsceProxy) response);
						osce = (OsceProxy) response;
						init();
					}

				}
			});
		} else {

			Log.info("new Osce");
			// oscePlace.setProxyId(osce.stableId());
			init();
		}
		// view.initialiseDriver(requests);
		widget.setWidget(view.asWidget());
		// setTable(view.getTable());

	}

	private void init() {

		OsceRequest request = requests.osceRequest();

		if (osce == null) {

			OsceProxy osce = request.create(OsceProxy.class);
			this.osce = osce;
			view.setEditTitle(false);

		} else {

			view.setEditTitle(true);
		}

		Log.info("edit");

		Log.info("persist");
		request.persist().using(osce);
		editorDriver.edit(osce, request);

		Log.info("flush");
		editorDriver.flush();
		Log.debug("Create für: " + osce.getId());
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);

	}

	@Override
	public void cancelClicked() {
		if (this.place.getOperation() == Operation.EDIT)
			placeController.goTo(new OsceDetailsPlace(osce.stableId(),
					Operation.DETAILS));
		else
			placeController.goTo(new OscePlace("OscePlace!CANCEL"));

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
				Log.warn(" in Osce -" + message);

				// TODO mcAppFactory.getErrorPanel().setErrorMessage(message);

			}

			@Override
			public void onSuccess(Void response) {
				Log.info("Osce successfully saved.");

				save = true;

				placeController.goTo(new OsceDetailsPlace(osce.stableId(),
						Operation.DETAILS));
			}

		});

	}

}
