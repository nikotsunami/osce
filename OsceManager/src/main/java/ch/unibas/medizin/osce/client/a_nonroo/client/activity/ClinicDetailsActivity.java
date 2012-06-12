package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ClinicDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ClinicDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.UserPlaceSettings;
import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
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

public class ClinicDetailsActivity extends AbstractActivity implements
ClinicDetailsView.Presenter, ClinicDetailsView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private ClinicDetailsView view;
	private CellTable<ClinicProxy> table;
	private SingleSelectionModel<ClinicProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;

	private ClinicDetailsPlace place;
	private ClinicProxy clinicProxy;
	private UserPlaceSettings userSettings;

	public ClinicDetailsActivity(ClinicDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;
		this.userSettings = new UserPlaceSettings(place);
	}

	public void onStop(){

	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("ClinicDetailsActivity.start()");
		ClinicDetailsView ClinicDetailsView = new ClinicDetailsViewImpl();
		ClinicDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = ClinicDetailsView;
		widget.setWidget(ClinicDetailsView.asWidget());

		view.setDelegate(this);
		loadDisplaySettings();

		requests.find(place.getProxyId()).fire(new Receiver<Object>() {

			public void onFailure(ServerFailure error){
				Log.error(error.getMessage());
			}
			
			@Override
			public void onSuccess(Object response) {
				if(response instanceof ClinicProxy){
					Log.info(((ClinicProxy) response).getName());
					init((ClinicProxy) response);
				}
			}
		});

	}

	private void init(ClinicProxy ClinicProxy) {
		this.clinicProxy = ClinicProxy;

		view.setValue(ClinicProxy);

		view.setDelegate(this);
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public void editClicked() {
		Log.info("edit clicked");
		goTo(new ClinicDetailsPlace(clinicProxy.stableId(), Operation.EDIT));
	}

	@Override
	public void deleteClicked() {
		if (!Window.confirm("Really delete this entry? You cannot undo this change.")) {
			return;
		}
		requests.clinicRequest().remove().using(clinicProxy).fire(new Receiver<Void>() {

			public void onSuccess(Void ignore) {
				if (widget == null) {
					return;
				}
				placeController.goTo(new ClinicPlace("ClinicPlace!DELETED"));
			}
		});
	}
	
	public void storeDisplaySettings() {
		userSettings.setValue("detailsTab", view.getSelectedDetailsTab());
		userSettings.flush();
	}
	
	private void loadDisplaySettings() {
		int detailsTab = 0;
		if (userSettings.hasSettings()) {
			detailsTab = userSettings.getIntValue("detailsTab");
		}
		
		view.setSelectedDetailsTab(detailsTab);
	}
	
}
