package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisFormDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisFormPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisFormDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisFormDetailsViewImpl;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;

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

public class AnamnesisFormDetailsActivity extends AbstractActivity implements
AnamnesisFormDetailsView.Presenter, AnamnesisFormDetailsView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private AnamnesisFormDetailsView view;
	private CellTable<AnamnesisFormProxy> table;
	private SingleSelectionModel<AnamnesisFormProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;

	private AnamnesisFormDetailsPlace place;
	private AnamnesisFormProxy anamnesisFormProxy;
	

	public AnamnesisFormDetailsActivity(AnamnesisFormDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
    	this.requests = requests;
    	this.placeController = placeController;
    }

	public void onStop(){

	}
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("AnamnesisFormDetailsActivity.start()");
		AnamnesisFormDetailsView AnamnesisFormDetailsView = new AnamnesisFormDetailsViewImpl();
		AnamnesisFormDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = AnamnesisFormDetailsView;
		widget.setWidget(AnamnesisFormDetailsView.asWidget());
		
		view.setDelegate(this);
		
		requests.find(place.getProxyId()).fire(new Receiver<Object>() {

			public void onFailure(ServerFailure error){
				Log.error(error.getMessage());
			}
			@Override
			public void onSuccess(Object response) {
				if(response instanceof AnamnesisFormProxy){
					Log.info(((AnamnesisFormProxy) response).getId().toString());
					init((AnamnesisFormProxy) response);
				}
			}
		    });
		
	}
	
	private void init(AnamnesisFormProxy anamnesisFormProxy) {

		this.anamnesisFormProxy = anamnesisFormProxy;
		
		view.setValue(anamnesisFormProxy);
		
		
	}
	



	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
		
	}

	@Override
	public void editClicked() {
		Log.info("edit clicked");
		goTo(new AnamnesisFormDetailsPlace(anamnesisFormProxy.stableId(),
				AnamnesisFormDetailsPlace.Operation.EDIT));
	}

	@Override
	public void deleteClicked() {
		if (!Window.confirm("Really delete this entry? You cannot undo this change.")) {
			return;
		}
		requests.anamnesisFormRequest().remove().using(anamnesisFormProxy).fire(new Receiver<Void>() {

			public void onSuccess(Void ignore) {
				if (widget == null) {
					return;
				}
				placeController.goTo(new AnamnesisFormPlace("AnamnesisFormPlace!DELETED"));
			}
		});
	}

}
