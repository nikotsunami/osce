package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.SpokenLanguageDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.SpokenLanguageDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.SpokenLanguageDetailsViewImpl;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.SingleSelectionModel;

public class SpokenLanguageDetailsActivity extends AbstractActivity implements
SpokenLanguageDetailsView.Presenter, SpokenLanguageDetailsView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeControler;
	private AcceptsOneWidget widget;
	private SpokenLanguageDetailsView view;
	private CellTable<SpokenLanguageProxy> table;
	private SingleSelectionModel<SpokenLanguageProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;

	private SpokenLanguageDetailsPlace place;
	private SpokenLanguageProxy SpokenLanguageProxy;
	

	public SpokenLanguageDetailsActivity(SpokenLanguageDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
    	this.requests = requests;
    	this.placeControler = placeController;

		
    	
    }

	public void onStop(){

	}
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SpokenLanguageDetailsActivity.start()");
		SpokenLanguageDetailsView SpokenLanguageDetailsView = new SpokenLanguageDetailsViewImpl();
		SpokenLanguageDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = SpokenLanguageDetailsView;
		widget.setWidget(SpokenLanguageDetailsView.asWidget());
		
		view.setDelegate(this);
		
		requests.find(place.getProxyId()).fire(new Receiver<Object>() {

			public void onFailure(ServerFailure error){
				Log.error(error.getMessage());
			}
			@Override
			public void onSuccess(Object response) {
				if(response instanceof SpokenLanguageProxy){
					Log.info(((SpokenLanguageProxy) response).getLanguageName());
					init((SpokenLanguageProxy) response);
				}

				
			}
		    });
		
	}
	
	private void init(SpokenLanguageProxy SpokenLanguageProxy) {

		this.SpokenLanguageProxy = SpokenLanguageProxy;
		
		view.setValue(SpokenLanguageProxy);
		
		
	}
	



	@Override
	public void goTo(Place place) {
		placeControler.goTo(place);
		
	}

	@Override
	public void editClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteClicked() {
		// TODO Auto-generated method stub
		
	}

}
