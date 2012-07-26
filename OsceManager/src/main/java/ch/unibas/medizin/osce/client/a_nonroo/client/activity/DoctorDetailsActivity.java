package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.DoctorDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.DoctorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.OsMaPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.DoctorDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.DoctorDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.OfficeDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.OfficeDetailsView.Presenter;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.OfficeDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.UserPlaceSettings;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleParticipantProxy;
import ch.unibas.medizin.osce.domain.OsceDay;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
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
	
	DoctorDetailsView doctorDetailsView;
	private UserPlaceSettings userSettings;
	
	public DoctorDetailsActivity(DoctorDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
    	this.requests = requests;
    	this.placeController = placeController;
    	this.userSettings = new UserPlaceSettings((OsMaPlace) place);
    }

	public void onStop(){

	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("DoctorDetailsActivity.start()");
		doctorDetailsView = new DoctorDetailsViewImpl();
		doctorDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = doctorDetailsView;
		widget.setWidget(doctorDetailsView.asWidget());
		
		view.setDelegate(this);
		loadDisplaySettings();

		requests.find(place.getProxyId()).with("office", "clinic").fire(new Receiver<Object>() {

			public void onFailure(ServerFailure error){
				Log.error(error.getMessage());
			}
			@Override
			public void onSuccess(Object response) {
				if(response instanceof DoctorProxy){
					Log.info(((DoctorProxy) response).getName());
					init((DoctorProxy) response);
					
					initOsceTable((DoctorProxy) response);
					initRoleTable((DoctorProxy) response);
				}			
			}
		});
		
	
	}
	
	private void initOsceTable(DoctorProxy doctorProxy)
	{
		
		requests.osceDayRequestNooRoo().findOsceDayByDoctorAssignment(doctorProxy).with("osce").fire(new OSCEReceiver<List<OsceDayProxy>>() 
				{

			@Override
			public void onSuccess(List<OsceDayProxy> response) {
				
				view.getTable().setRowData(response);
			}
			
		});		
				}

	private void initRoleTable(DoctorProxy doctorProxy)
	{
				
		requests.roleParticipantRequestNonRoo().findRoleParticipatentByDoctor(doctorProxy).with("standardizedRole").fire(new OSCEReceiver<List<RoleParticipantProxy>>() 
		{
			@Override
			public void onSuccess(List<RoleParticipantProxy> response) {
				
				view.getRoleTable().setRowData(response);
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
		goTo(new DoctorDetailsPlace(doctorProxy.stableId(), Operation.EDIT));
	}

	@Override
	public void deleteClicked() {
		if (!Window.confirm("Really delete this entry? You cannot undo this change.")) {
			return;
		}
		requests.doctorRequest().remove().using(doctorProxy).fire(new Receiver<Void>() {

			public void onSuccess(Void ignore) {
				if (widget == null) {
					return;
				}
				placeController.goTo(new DoctorPlace("DoctorPlace!DELETED"));
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
			detailsTab = userSettings.getIntValue("detailsTab");
		}
		
		view.setSelectedDetailsTab(detailsTab);
	}

}
