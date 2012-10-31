package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.DoctorDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.DoctorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.OsMaPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.OsceDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.DoctorEditView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.DoctorEditViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.OfficeEditView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.OfficeEditViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.UserPlaceSettings;
import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorRequest;
import ch.unibas.medizin.osce.client.managed.request.OfficeProxy;
import ch.unibas.medizin.osce.client.managed.request.OfficeRequest;
import ch.unibas.medizin.osce.client.managed.request.OsceRequest;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.Widget;
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
	OfficeEditView officeView;
	//private Operation operation;	

	private RequestFactoryEditorDriver<DoctorProxy,DoctorEditViewImpl> editorDriver;
	private DoctorProxy doctor;
	private OfficeProxy office;
	private OfficeEditView officeEditView;
	private RequestFactoryEditorDriver<OfficeProxy, OfficeEditViewImpl> officeDriver;
	//private RequestFactoryEditorDriver<OfficeProxy, OfficeEditViewImpl> officeEditorDriver;
	private boolean save;
	private UserPlaceSettings userSettings;
	private OsceConstants constants = GWT.create(OsceConstants.class);
	DoctorRequest doctorRequest1;
	OfficeRequest officeRequest1;
	
	public DoctorEditActivity(DoctorDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;
		this.userSettings = new UserPlaceSettings((OsMaPlace) place);
	}

	public DoctorEditActivity(DoctorDetailsPlace place,
			OsMaRequestFactory requests, PlaceController placeController,
			Operation operation) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;
		this.userSettings = new UserPlaceSettings((OsMaPlace) place);
		//this.operation=operation;
	}

	public void onStop(){

	}

	/*@Override
	public String mayStop() {
		if (!save && changed())
			return constants.changesDiscarded();
		else
			return null;
	}*/
	
	// use this to check if some value has changed since editing has started
	/*private boolean changed() {
		return editorDriver != null && editorDriver.flush().isChanged();
	}*/

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {

		Log.info("start");
		DoctorEditView doctorEditView = new DoctorEditViewImpl();

		this.widget = panel;
		this.view = doctorEditView;
		
		officeView= new OfficeEditViewImpl();
		
		//view.getOfficePanel().add(officeView);
		
		//officeDriver = officeView.createEditorDriver();

		//editorDriver = view.createEditorDriver();

		view.setDelegate(this);
		loadDisplaySettings();
		Log.info("before doctor edit");
		 doctorRequest1=requests.doctorRequest();
		officeRequest1=requests.officeRequest();
		
		view.setClinicPickerValues(Collections.<ClinicProxy>emptyList());
		requests.clinicRequest().findAllClinics().with(ch.unibas.medizin.osce.client.managed.ui.ClinicProxyRenderer.instance().getPaths()).fire(new Receiver<List<ClinicProxy>>() {

			public void onSuccess(List<ClinicProxy> response) {
				List<ClinicProxy> values = new ArrayList<ClinicProxy>();
				values.add(null);
				values.addAll(response);
				view.setClinicPickerValues(values);
			}
		});

		requests.specialisationRequest().findAllSpecialisations().fire(new Receiver<List<SpecialisationProxy>>() {

			@Override
			public void onSuccess(List<SpecialisationProxy> response) {
				// TODO Auto-generated method stub
				List<SpecialisationProxy> values = new ArrayList<SpecialisationProxy>();
				values.add(null);
				values.addAll(response);
				view.setSpecialisationPickerValues(values);
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
			requests.find(place.getProxyId()).with("office","clinic","specialisation").fire(new Receiver<Object>() {

				public void onFailure(ServerFailure error){
					Log.error(error.getMessage());
				}
				
				@Override
				public void onSuccess(Object response) {
					if(response instanceof DoctorProxy){
						Log.info(((DoctorProxy) response).getEmail());
						//init((DoctorProxy) response);
						doctor = (DoctorProxy)response;
						office=((DoctorProxy)response).getOffice();
						view.setValueForEdit(doctor);
						
						//init();
					}
				}
			});
		} else {
			Log.info("new Doctor");
			//doctorPlace.setProxyId(doctor.stableId());
			//init();
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

		/*Log.info("edit");

		Log.info("persist");
		request.persist().using(doctor);
//		requestOffice.persist().using(office);

	//	editorDriver.edit(doctor, request);
	//	officeDriver.edit(office, request);
		//officeEditorDriver.edit(doctor.getOffice(), request);

//		Log.info("flush");
//		editorDriver.flush();
//		officeDriver.flush();
		//officeEditorDriver.flush();
		Log.debug("Create für: "+doctor.getEmail());
		//		view.setValue(doctor);
*/	}

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
		Map<String, Widget> tempMap=new HashMap<String, Widget>();

		/*if(view.getSelectedTab()==0)
		{
			Log.info("Map Size: " + view.getDoctorMap().size());
			tempMap=view.getDoctorMap();
		}
		else
		{
			Log.info("Map Size: " +officeView.getOfficeMap().size());
			tempMap=officeView.getOfficeMap();
		}*/
		
		if(this.place.getOperation()== Operation.EDIT)
		{
			Log.info("Edit Mode");
			//editorDriver.flush();
			
			/*doctor = doctorRequest1.edit(doctor);
			office = doctorRequest1.edit(office);*/
			/*doctor=requests.doctorRequest().edit(doctor);
			office=requests.doctorRequest().edit(office);*/
			
			
			OfficeRequest officeRequest=requests.officeRequest();
			/*OfficeProxy no;*/
		/*	no=office;
			no = officeRequest.edit(no);*/
			//office = officeRequest.edit(office);
			
			Log.info("before doctor edit");
			
			
			DoctorRequest doctorRequest=requests.doctorRequest();
			DoctorProxy doctor1;
			doctor1=doctor;
			doctor1 = doctorRequest.edit(doctor1);
			Log.info("after doctor edit");
			
			OfficeProxy office1;
			office1=office;
			office1 = doctorRequest.edit(office1);
			/*OfficeRequest officeRequest=requests.officeRequest();
			office = doctorRequest.edit(office);*/
			
			Log.info("after office edit");
			office1.setTitle(view.getDoctorEditViewImpl().officeEditViewImpl.title.getValue());
			office1.setName(view.getDoctorEditViewImpl().officeEditViewImpl.name.getValue());
			office1.setPreName(view.getDoctorEditViewImpl().officeEditViewImpl.preName.getValue());
			office1.setGender(view.getDoctorEditViewImpl().officeEditViewImpl.gender.getValue());
			office1.setEmail(view.getDoctorEditViewImpl().officeEditViewImpl.email.getValue());
			office1.setTelephone(view.getDoctorEditViewImpl().officeEditViewImpl.telephone.getValue());
			Log.info("after office value set");
			
			doctor1.setTitle(view.getDoctorEditViewImpl().title.getValue());
			doctor1.setName(view.getDoctorEditViewImpl().name.getValue());
			doctor1.setPreName(view.getDoctorEditViewImpl().preName.getValue());
			doctor1.setGender(view.getDoctorEditViewImpl().gender.getValue());
			doctor1.setEmail(view.getDoctorEditViewImpl().email.getValue());
			doctor1.setTelephone(view.getDoctorEditViewImpl().telephone.getValue());
			doctor1.setClinic(view.getDoctorEditViewImpl().clinic.getSelected());
			doctor1.setSpecialisation(view.getDoctorEditViewImpl().specialisation.getSelected());
			Log.info("after doctor value set");
			doctor1.setOffice(office1);
			final DoctorProxy newdoctor=doctor1;
			Log.info("before persis");
			doctorRequest.persist().using(doctor1).fire(new OSCEReceiver<Void>(view.getDoctorMap()) {
				// E Highlight onViolation
				@Override
				public void onFailure(ServerFailure error) {
					Log.error(error.getMessage());

				}
				
				/*@Override
				public void onViolation(Set<Violation> errors) {
					Iterator<Violation> iter = errors.iterator();
					String message = "";
					while (iter.hasNext()) {
						message += iter.next().getMessage() + "<br>";
					}
					Log.warn(" in doctor edit -" + message);
				}*/
				@Override
				public void onSuccess(Void response) {

					save = true;
					placeController.goTo(new DoctorDetailsPlace(newdoctor.stableId(), Operation.NEW));
					Log.info("doctor edit successfull");
				
					
				}
			});
			
			
			
		}
		else
		{
			Log.info("Create Mode");
			DoctorRequest doctorRequest=requests.doctorRequest();
			doctorProxy = doctorRequest.create(DoctorProxy.class);
			
			OfficeRequest officeRequest=requests.officeRequest();
			office = doctorRequest.create(OfficeProxy.class);
			
			/*doctor = requests.create(DoctorProxy.class);
			office = requests.create(OfficeProxy.class);*/
			//doctor.setOffice(office);
			
			office.setTitle(view.getDoctorEditViewImpl().officeEditViewImpl.title.getValue());
			office.setName(view.getDoctorEditViewImpl().officeEditViewImpl.name.getValue());
			office.setPreName(view.getDoctorEditViewImpl().officeEditViewImpl.preName.getValue());
			office.setGender(view.getDoctorEditViewImpl().officeEditViewImpl.gender.getValue());
			office.setEmail(view.getDoctorEditViewImpl().officeEditViewImpl.email.getValue());
			office.setTelephone(view.getDoctorEditViewImpl().officeEditViewImpl.telephone.getValue());
			
			
			doctorProxy.setTitle(view.getDoctorEditViewImpl().title.getValue());
			doctorProxy.setName(view.getDoctorEditViewImpl().name.getValue());
			doctorProxy.setPreName(view.getDoctorEditViewImpl().preName.getValue());
			doctorProxy.setGender(view.getDoctorEditViewImpl().gender.getValue());
			doctorProxy.setEmail(view.getDoctorEditViewImpl().email.getValue());
			doctorProxy.setTelephone(view.getDoctorEditViewImpl().telephone.getValue());
			doctorProxy.setClinic(view.getDoctorEditViewImpl().clinic.getSelected());
			doctorProxy.setSpecialisation(view.getDoctorEditViewImpl().specialisation.getSelected());
			doctorProxy.setOffice(office);
			Log.info("office--"+doctorProxy.getOffice());
			doctorRequest.persist().using(doctorProxy).fire(new OSCEReceiver<Void>(view.getDoctorMap()) {
				// E Highlight onViolation
				@Override
				public void onFailure(ServerFailure error) {
					Log.error(error.getMessage());

				}
				
				/*@Override
				public void onViolation(Set<Violation> errors) {
					Iterator<Violation> iter = errors.iterator();
					String message = "";
					while (iter.hasNext()) {
						message += iter.next().getMessage() + "<br>";
					}
					Log.warn(" in doctor edit -" + message);
				}*/
				@Override
				public void onSuccess(Void response) {

					save = true;
					placeController.goTo(new DoctorDetailsPlace(doctorProxy.stableId(), Operation.NEW));
					Log.info("doctor created successfull");
				
					
				}
			});
			

		}
	}

	/*@Override
	public void saveClicked() {
		Log.info("saveClicked");

		// FIX bug(2011-11-17) - office is NOT saved the first time!
		// office data is read from view (data save is cascaded, therefore office has to be updated before doctor is saved!)
		officeDriver.flush();
		
		// Highlight onViolation
		Map<String, Widget> tempMap=new HashMap<String, Widget>();
		
		
		if(view.getSelectedTab()==0)
		{
			Log.info("Map Size: " + view.getDoctorMap().size());
			tempMap=view.getDoctorMap();
		}
		else
		{
			Log.info("Map Size: " +officeView.getOfficeMap().size());
			tempMap=officeView.getOfficeMap();
		}
		editorDriver.flush().fire(new OSCEReceiver<Void>(tempMap) {
			// E Highlight onViolation
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
			// Highlight onViolation
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
			// E Highlight onViolation
		}); 
	}*/
	
	@Override
	public void storeDisplaySettings() {
		userSettings.setValue("detailsTab", view.getSelectedDetailsTab());
		userSettings.flush();
	}
	
	private void loadDisplaySettings() {
		int detailsTab = 0;
		/*if (userSettings.hasSettings()) {
			detailsTab = userSettings.getIntValue("detailsTab");
		}*/
		
		view.setSelectedDetailsTab(detailsTab);
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
