package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.ProfessionDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ProfessionDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ProfessionDetailsViewImpl;
import ch.unibas.medizin.osce.client.managed.request.ProfessionProxy;

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

public class ProfessionDetailsActivity extends AbstractActivity implements
ProfessionDetailsView.Presenter, ProfessionDetailsView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeControler;
	private AcceptsOneWidget widget;
	private ProfessionDetailsView view;
	private CellTable<ProfessionProxy> table;
	private SingleSelectionModel<ProfessionProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;

	private ProfessionDetailsPlace place;
	private ProfessionProxy ProfessionProxy;
	

	public ProfessionDetailsActivity(ProfessionDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
    	this.requests = requests;
    	this.placeControler = placeController;

		
    	
    }

	public void onStop(){

	}
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("ProfessionDetailsActivity.start()");
		ProfessionDetailsView ProfessionDetailsView = new ProfessionDetailsViewImpl();
		ProfessionDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = ProfessionDetailsView;
		widget.setWidget(ProfessionDetailsView.asWidget());
		
		view.setDelegate(this);
		
		requests.find(place.getProxyId()).fire(new Receiver<Object>() {

			public void onFailure(ServerFailure error){
				Log.error(error.getMessage());
			}
			@Override
			public void onSuccess(Object response) {
				if(response instanceof ProfessionProxy){
					Log.info(((ProfessionProxy) response).getProfession());
					init((ProfessionProxy) response);
				}

				
			}
		    });
		
	}
	
	private void init(ProfessionProxy ProfessionProxy) {

		this.ProfessionProxy = ProfessionProxy;
		
		view.setValue(ProfessionProxy);
		
		
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
