package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.DoctorDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.DoctorDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.DoctorDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.OfficeDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.OfficeDetailsView.Presenter;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.OfficeDetailsViewImpl;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;

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

public class DoctorDetailsActivity extends AbstractActivity implements
DoctorDetailsView.Presenter, DoctorDetailsView.Delegate , OfficeDetailsView.Delegate, Presenter{
	
    private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private DoctorDetailsView view;
	private CellTable<DoctorProxy> table;
	private SingleSelectionModel<DoctorProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;

	private DoctorDetailsPlace place;
	private DoctorProxy doctorProxy;
	private OfficeDetailsView officeDetailsView;
	

	public DoctorDetailsActivity(DoctorDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
    	this.requests = requests;
    	this.placeController = placeController;

    }

	public void onStop(){

	}
	
	DoctorDetailsView doctorDetailsView;
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("DoctorDetailsActivity.start()");
		doctorDetailsView = new DoctorDetailsViewImpl();
		doctorDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = doctorDetailsView;
		widget.setWidget(doctorDetailsView.asWidget());
		
		view.setDelegate(this);

		requests.find(place.getProxyId()).with("office").fire(new Receiver<Object>() {

			public void onFailure(ServerFailure error){
				Log.error(error.getMessage());
			}
			@Override
			public void onSuccess(Object response) {
				if(response instanceof DoctorProxy){
					Log.info(((DoctorProxy) response).getName());
					init((DoctorProxy) response);
				}

				
			}
		    });
		
	}
	
	private void init(DoctorProxy doctorProxy) {

		this.doctorProxy = doctorProxy;
		
		view.setValue(doctorProxy);
		officeDetailsView = new OfficeDetailsViewImpl();
		officeDetailsView.setPresenter(this);
		
		officeDetailsView.setValue(doctorProxy.getOffice());
		doctorDetailsView.getOfficeDetailsPanel().add(officeDetailsView);
		
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
		
	}

	@Override
	public void editClicked() {
		goTo(new DoctorDetailsPlace(doctorProxy.stableId(), DoctorDetailsPlace.Operation.EDIT));
	}

	@Override
	public void deleteClicked() {
		// TODO Auto-generated method stub
		
	}

}
