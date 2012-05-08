package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.DoctorDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.DoctorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.DoctorEditView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.DoctorEditViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.OfficeEditView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.OfficeEditViewImpl;
import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorRequest;
import ch.unibas.medizin.osce.client.managed.request.OfficeProxy;
import ch.unibas.medizin.osce.client.managed.request.OfficeRequest;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.requestfactory.shared.Violation;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.SingleSelectionModel;

public class DoctorEditActivity extends AbstractActivity implements
DoctorEditView.Presenter, DoctorEditView.Delegate, OfficeEditView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private DoctorEditView view;
	private CellTable<DoctorProxy> table;
	private SingleSelectionModel<DoctorProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;

	private DoctorDetailsPlace place;
	private DoctorProxy doctorProxy;
	//private Operation operation;	

	private RequestFactoryEditorDriver<DoctorProxy,DoctorEditViewImpl> editorDriver;
	private DoctorProxy doctor;
	private OfficeProxy office;
	private OfficeEditView officeEditView;
	private RequestFactoryEditorDriver<OfficeProxy, OfficeEditViewImpl> officeDriver;
	//private RequestFactoryEditorDriver<OfficeProxy, OfficeEditViewImpl> officeEditorDriver;
	private boolean save;

	public DoctorEditActivity(DoctorDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;
	}

	public DoctorEditActivity(DoctorDetailsPlace place,
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
		DoctorEditView doctorEditView = new DoctorEditViewImpl();

		this.widget = panel;
		this.view = doctorEditView;

		OfficeEditView officeView = new OfficeEditViewImpl();
		view.getOfficePanel().add(officeView);
		officeDriver = officeView.createEditorDriver();

		editorDriver = view.createEditorDriver();

		view.setDelegate(this);

		view.setClinicPickerValues(Collections.<ClinicProxy>emptyList());
		requests.clinicRequest().findClinicEntries(0, 50).with(ch.unibas.medizin.osce.client.managed.ui.ClinicProxyRenderer.instance().getPaths()).fire(new Receiver<List<ClinicProxy>>() {

			public void onSuccess(List<ClinicProxy> response) {
				List<ClinicProxy> values = new ArrayList<ClinicProxy>();
				values.add(null);
				values.addAll(response);
				view.setClinicPickerValues(values);
			}
		});

		eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {
				//updateSelection(event.getNewPlace());
				// TODO implement
			}
		});
		//init();

		if (this.place.getOperation() == Operation.EDIT) {
			Log.info("edit");
			requests.find(place.getProxyId()).with("office","clinic").fire(new Receiver<Object>() {

				public void onFailure(ServerFailure error){
					Log.error(error.getMessage());
				}
				
				@Override
				public void onSuccess(Object response) {
					if(response instanceof DoctorProxy){
						Log.info(((DoctorProxy) response).getEmail());
						//init((DoctorProxy) response);
						doctor = (DoctorProxy)response;
						init();
					}
				}
			});
		} else {
			Log.info("new Doctor");
			//doctorPlace.setProxyId(doctor.stableId());
			init();
		}
		//		view.initialiseDriver(requests);
		widget.setWidget(doctorEditView.asWidget());
		//setTable(view.getTable());
	}

	private void init() {

		DoctorRequest request = requests.doctorRequest();
//		OfficeRequest requestOffice = requests.officeRequest();

		if(doctor == null) {
			doctor = request.create(DoctorProxy.class);
			office = request.create(OfficeProxy.class);
			doctor.setOffice(office);
			
			//this.doctor = doctor;
			view.setEditTitle(false);
		} else {
			office = doctor.getOffice();
			view.setEditTitle(true);
		}

		Log.info("edit");

		Log.info("persist");
		request.persist().using(doctor);
//		requestOffice.persist().using(office);

		editorDriver.edit(doctor, request);
		officeDriver.edit(office, request);
		//officeEditorDriver.edit(doctor.getOffice(), request);

//		Log.info("flush");
//		editorDriver.flush();
//		officeDriver.flush();
		//officeEditorDriver.flush();
		Log.debug("Create für: "+doctor.getEmail());
		//		view.setValue(doctor);
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public void cancelClicked() {
		if(this.place.getOperation()== Operation.EDIT)
			placeController.goTo(new DoctorDetailsPlace(doctor.stableId(), Operation.DETAILS));
		else
			placeController.goTo(new DoctorPlace("DoctorPlace!CANCEL"));
	}

	@Override
	public void saveClicked() {
		Log.info("saveClicked");

		// FIX bug(2011-11-17) - office is NOT saved the first time!
		// office data is read from view (data save is cascaded, therefore office has to be updated before doctor is saved!)
		officeDriver.flush();
		
		editorDriver.flush().fire(new Receiver<Void>() {
			@Override
			public void onSuccess(Void response) {
				Log.info("Doctor successfully saved.");

				//saveOffice();
				save = true;
				
				placeController.goTo(new DoctorDetailsPlace(doctor.stableId(), Operation.NEW));
			}

			@Override
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
				Log.warn(" in Doctor -" + message);

				// TODO mcAppFactory.getErrorPanel().setErrorMessage(message);
			}
		}); 
	}

//	private void saveOffice() {
//		// TODO: bug(2011-11-17) - office is NOT saved the first time!
//		
//		officeDriver.flush().fire(new Receiver<Void>() {
//
//			@Override
//			public void onSuccess(Void response) {
//				Log.info("Office successfully saved.");
//
//				placeController.goTo(new DoctorDetailsPlace(doctor.stableId(), DoctorDetailsPlace.Operation.DETAILS));				
//			}
//
//			public void onFailure(ServerFailure error){
//				Log.error(error.getMessage());
//			}
//
//		});
//
//		save = true;
//	}

}
