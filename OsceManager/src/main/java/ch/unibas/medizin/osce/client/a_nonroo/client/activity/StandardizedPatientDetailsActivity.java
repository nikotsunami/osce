package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StandardizedPatientDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StandardizedPatientDetailsViewImpl;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.SingleSelectionModel;

public class StandardizedPatientDetailsActivity extends AbstractActivity implements
StandardizedPatientDetailsView.Presenter, StandardizedPatientDetailsView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private StandardizedPatientDetailsView view;
	private CellTable<StandardizedPatientProxy> table;
	private SingleSelectionModel<StandardizedPatientProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;

	private StandardizedPatientDetailsPlace place;
	private StandardizedPatientProxy standardizedPatientProxy;
	

	public StandardizedPatientDetailsActivity(StandardizedPatientDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
    	this.requests = requests;
    	this.placeController = placeController;

		
    	
    }

	public void onStop(){

	}
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("StandardizedPatientDetailsActivity.start()");
		StandardizedPatientDetailsView StandardizedPatientDetailsView = new StandardizedPatientDetailsViewImpl();
		StandardizedPatientDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = StandardizedPatientDetailsView;
		widget.setWidget(StandardizedPatientDetailsView.asWidget());
		
		view.setDelegate(this);
		
		requests.find(place.getProxyId()).fire(new Receiver<Object>() {

			public void onFailure(ServerFailure error){
				Log.error(error.getMessage());
			}
			@Override
			public void onSuccess(Object response) {
				if(response instanceof StandardizedPatientProxy){
					Log.info(((StandardizedPatientProxy) response).getName());
					init((StandardizedPatientProxy) response);
				}

				
			}
		    });
		
	}
	
	private void init(StandardizedPatientProxy StandardizedPatientProxy) {

		this.standardizedPatientProxy = StandardizedPatientProxy;
		
		view.setValue(StandardizedPatientProxy);
		
		
	}
	



	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
		
	}

	@Override
	public void editClicked() {
		Log.info("edit clicked");
		goTo(new StandardizedPatientDetailsPlace(standardizedPatientProxy.stableId(),
				StandardizedPatientDetailsPlace.Operation.EDIT));
		
	}

	@Override
	public void deleteClicked() {
		// TODO Auto-generated method stub
		
	}

}
