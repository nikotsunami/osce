package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.NationalityDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.NationalityDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.NationalityDetailsViewImpl;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;

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

public class NationalityDetailsActivity extends AbstractActivity implements
NationalityDetailsView.Presenter, NationalityDetailsView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeControler;
	private AcceptsOneWidget widget;
	private NationalityDetailsView view;
	private CellTable<NationalityProxy> table;
	private SingleSelectionModel<NationalityProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;

	private NationalityDetailsPlace place;
	private NationalityProxy nationalityProxy;
	

	public NationalityDetailsActivity(NationalityDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
    	this.requests = requests;
    	this.placeControler = placeController;

		
    	
    }

	public void onStop(){

	}
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("NationalityDetailsActivity.start()");
		NationalityDetailsView nationalityDetailsView = new NationalityDetailsViewImpl();
		nationalityDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = nationalityDetailsView;
		widget.setWidget(nationalityDetailsView.asWidget());
		
		view.setDelegate(this);
		
		requests.find(place.getProxyId()).fire(new Receiver<Object>() {

			public void onFailure(ServerFailure error){
				Log.error(error.getMessage());
			}
			@Override
			public void onSuccess(Object response) {
				if(response instanceof NationalityProxy){
					Log.info(((NationalityProxy) response).getNationality());
					init((NationalityProxy) response);
				}

				
			}
		    });
		
	}
	
	private void init(NationalityProxy nationalityProxy) {

		this.nationalityProxy = nationalityProxy;
		
		view.setValue(nationalityProxy);
		
		
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
