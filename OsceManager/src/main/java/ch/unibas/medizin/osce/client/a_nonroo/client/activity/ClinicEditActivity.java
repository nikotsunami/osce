package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicDetailsPlace.Operation;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ClinicEditView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ClinicEditViewImpl;
import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
import ch.unibas.medizin.osce.client.managed.request.ClinicRequest;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;

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

public class ClinicEditActivity extends AbstractActivity implements
ClinicEditView.Presenter, ClinicEditView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private ClinicEditView view;
	private ClinicDetailsPlace place;

	private RequestFactoryEditorDriver<ClinicProxy,ClinicEditViewImpl> editorDriver;
	private ClinicProxy clinic;
	private boolean save;

	public ClinicEditActivity(ClinicDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;



	}

	public ClinicEditActivity(ClinicDetailsPlace place,
			OsMaRequestFactory requests, PlaceController placeController,
			Operation operation) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;
		//this.operation=operation;
	}



	public void onStop(){

	}

	@Override
	public String mayStop() {
		if(!save)
			return "Changes will be discarded!";
		else
			return null;
	}


	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {

		Log.info("start");
		ClinicEditView clinicEditView = new ClinicEditViewImpl();

		this.widget = panel;
		this.view = clinicEditView;
		editorDriver = view.createEditorDriver();

		view.setDelegate(this);

		eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {

				//updateSelection(event.getNewPlace());
				// TODO implement
			}
		});
		//init();

	      view.setDoctorsPickerValues(Collections.<DoctorProxy>emptyList());
	        requests.doctorRequest().findDoctorEntries(0, 50).with(ch.unibas.medizin.osce.client.managed.ui.DoctorProxyRenderer.instance().getPaths()).fire(new Receiver<List<DoctorProxy>>() {

	            public void onSuccess(List<DoctorProxy> response) {
	                List<DoctorProxy> values = new ArrayList<DoctorProxy>();
	                values.add(null);
	                values.addAll(response);
	                view.setDoctorsPickerValues(values);
	            }
	        });
		
		if (this.place.getOperation()==ClinicDetailsPlace.Operation.EDIT){
			Log.info("edit");
			requests.find(place.getProxyId()).with("clinic", "doctor").fire(new Receiver<Object>() {

				public void onFailure(ServerFailure error){
					Log.error(error.getMessage());
				}
				@Override
				public void onSuccess(Object response) {
					if(response instanceof ClinicProxy){
						Log.info(((ClinicProxy) response).getName());
						//init((ClinicProxy) response);
						clinic=(ClinicProxy)response;
						init();
					}


				}
			});
		}
		else{

			Log.info("new Clinic");
			//clinicPlace.setProxyId(clinic.stableId());
			init();
		}
		//		view.initialiseDriver(requests);
		widget.setWidget(clinicEditView.asWidget());
		//setTable(view.getTable());


	}




	private void init() {

		ClinicRequest request = requests.clinicRequest();

		if(clinic==null){

			ClinicProxy clinic = request.create(ClinicProxy.class);
			this.clinic=clinic;
			view.setEditTitle(false);

		}else{

			view.setEditTitle(true);
		}

		Log.info("edit");

		Log.info("persist");
		request.persist().using(clinic);
		editorDriver.edit(clinic, request);

		Log.info("flush");
		editorDriver.flush();
		Log.debug("Create für: "+clinic.getName());
	}




	@Override
	public void goTo(Place place) {
		placeController.goTo(place);

	}

	@Override
	public void cancelClicked() {
		if(this.place.getOperation()==ClinicDetailsPlace.Operation.EDIT)
			placeController.goTo(new ClinicDetailsPlace(clinic.stableId(), ClinicDetailsPlace.Operation.DETAILS));
		else
			placeController.goTo(new ClinicPlace("ClinicPlace!CANCEL"));

	}

	@Override
	public void saveClicked() {
		Log.info("saveClicked");

		editorDriver.flush().fire(new Receiver<Void>() {
			
			public void onFailure(ServerFailure error){
				Log.error(error.getMessage());

			}
			@Override
			public void onViolation(Set<Violation> errors) {
				Iterator<Violation> iter = errors.iterator();
				String message = "";
				while(iter.hasNext()){
					message += iter.next().getMessage() + "<br>";
				}
				Log.warn(" in Clinic -" + message);

				// TODO mcAppFactory.getErrorPanel().setErrorMessage(message);


			}
			@Override
			public void onSuccess(Void response) {
				Log.info("Clinic successfully saved.");
				
				save=true;
				
				placeController.goTo(new ClinicDetailsPlace(clinic.stableId(), ClinicDetailsPlace.Operation.DETAILS));		
			}

		}); 

	}


}
