package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ClinicDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ClinicDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ClinicSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ClinicSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.UserPlaceSettings;
import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
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
ClinicDetailsView.Presenter, ClinicDetailsView.Delegate ,ClinicSubView.Delegate , ClinicSubView.Presenter {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private ClinicDetailsView view;
	private CellTable<ClinicProxy> table;
	private SingleSelectionModel<ClinicProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	private List<ClinicProxy> clinicProxyList = new ArrayList<ClinicProxy>();
	private ClinicDetailsPlace place;
	private ClinicProxy clinicProxy;
	private UserPlaceSettings userSettings;
	private ActivityManager activityManager;
	private int stackIndex=0;
	private DoctorProxy doctorProxy;
	private ClinicDetailsActivityMapper activityMapper;


	//sample
	public ClinicSubView[] tempClinicSubViews;
	public int arrarycount = 0;
	
	private ClinicDetailsActivity clinicDetailsActivity;
	
	private ClinicSubView view_c = new ClinicSubViewImpl();

	public ClinicDetailsActivity(ClinicDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
		
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;
		this.userSettings = new UserPlaceSettings(place);
	
		clinicDetailsActivity = this;
	}

	public void onStop(){

	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
	
		Log.info("ClinicDetailsActivity.start()");
		ClinicDetailsView ClinicDetailsView = new ClinicDetailsViewImpl();
		ClinicDetailsView.setPresenter(this);
		
		this.widget = panel;
		this.view = ClinicDetailsView;
		widget.setWidget(ClinicDetailsView.asWidget());
		
		ApplicationLoadingScreenEvent.register(requests.getEventBus(),
				new ApplicationLoadingScreenHandler() {
					@Override
					public void onEventReceived(
							ApplicationLoadingScreenEvent event) {
						//Log.info("~~~~~~~~ApplicationLoadingScreenEvent onEventReceived Called");
						event.display();
						
					}
		});

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

	
	@SuppressWarnings("deprecation")
	private void init(ClinicProxy ClinicProxy) {
			
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
			Log.info("response size ");			
			this.clinicProxy = ClinicProxy;
			Log.info("Clinic ID ::: "+ clinicProxy.getId());
			
			requests.doctorRequestNonRoo().findSpecialisationByClinicId(clinicProxy.getId()).with("specialisation").fire(new OSCEReceiver<List<SpecialisationProxy>>() {

				@Override
				public void onSuccess(List<SpecialisationProxy> response) {
					
					for (int i=0; i<response.size(); i++)
					{
						final String specialisationLbl = response.get(i).getName();
						
						requests.doctorRequestNonRoo().findDoctorByAssignment(response.get(i).getId(), clinicProxy.getId()).with("assignments", "assignments.osceDay", "assignments.osceDay.osce").fire(new OSCEReceiver<List<DoctorProxy>>() {

							@Override
							public void onSuccess(List<DoctorProxy> response) {
								
								ClinicSubView clinicSubView = new ClinicSubViewImpl();
								clinicSubView.getTable().setRowData(response);
								clinicSubView.getHeaderLabel().setText(specialisationLbl);
								
								view.getSpecialTabPanel().add(clinicSubView);
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							}
						});
					}
					
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
				}
			});
		
	
	/*requests.clinicRequestNonRoo().findAllDoctorsId(clinicProxy.getId()).with("doctars","doctors.specialisation").fire(new OSCEReceiver<List<ClinicProxy>>() {
			
			@Override
			public void onSuccess(List<ClinicProxy> response) {
				Log.info("inside Success ");

				stackIndex = 0;
	
	if(response != null && response.size() > 0){
			
		clinicProxyList=response;

		Iterator<ClinicProxy> clinicList = response.iterator();
		
		clinicProxy = response.get(0);
		
	while(clinicList.hasNext()){
			
			
			Log.info("into next value" );
			ClinicProxy clinicProxy = clinicList.next();
			
		Log.info("Size of Doctors " + clinicProxy.getDoctors().size());
		
			
		List<DoctorProxy> l = new ArrayList<DoctorProxy>(clinicProxy.getDoctors());
	
		
		final ClinicSubView clinicSubView = new ClinicSubViewImpl();
	
		clinicSubView.setDelegate(clinicDetailsActivity);
		String clinicLable= "" ;
		
		//if(l.get(0) != null && l.get(0).getSpecialisation()!= null)
		if(l.size() > 0){
			
			Log.info("Data is  available");
		
			clinicLable =l.get(0).getSpecialisation().getName();
		}

	//	view.setValue(ClinicProxy);

		//view.getSpecialTabPanel().insert(clinicSubView, stackIndex);
			view.getSpecialTabPanel().add((Widget) clinicSubView, clinicLable, true);
	
			Log.info("clinicID::::"+ clinicProxy.getId());
			
			requests.clinicRequest().findClinic(clinicProxy.getId()).with("doctors","doctors.specialisation","doctors.specialisation.oscePostBlueprint","doctors.specialisation.oscePostBlueprint.osce").fire(new OSCEReceiver<ClinicProxy>() {

				@Override
				public void onSuccess(
						ch.unibas.medizin.osce.client.managed.request.ClinicProxy response) {
					Log.info("response" + response.getDoctors().size());
					List<DoctorProxy> doctors=new ArrayList<DoctorProxy>();
					doctors.addAll(response.getDoctors());
					clinicSubView.getTable().setRowData(doctors);
					Log.info("Array count" + arrarycount);

				}
			});
					stackIndex++;
			
			Log.info("Stack value:::::" + stackIndex);
			
		}

}
	}
});
	*/
			view.setValue(clinicProxy);
			view.setDelegate(this);
			initDoctorsDetails(clinicProxy.getId());
		
	}

	private void initDoctorsDetails(Long clinicId) {
		
		requests.doctorRequestNonRoo().findDoctorByClinicID(clinicProxy.getId()).fire(new OSCEReceiver<List<DoctorProxy>>() {

			@Override
			public void onSuccess(List<DoctorProxy> response) {
			
				view.getLecturersTable().setRowData(response);	
			}
		});
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void editClicked() {
		Log.info("edit clicked");
		goTo(new ClinicDetailsPlace(clinicProxy.stableId(), Operation.EDIT));
	}

	@SuppressWarnings("deprecation")
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
			Log.info("Loading Display");
			detailsTab = userSettings.getIntValue("detailsTab");
		}
		
		view.setSelectedDetailsTab(0);
	}
}
	

