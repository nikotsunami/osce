package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.DMZSyncService;
import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.DMZSyncServiceAsync;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.OsMaPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.DescriptionEditView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.DescriptionEditViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientBankaccountEditSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientBankaccountEditSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientEditView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientEditViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.CalendarUtil;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.UserPlaceSettings;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.BankaccountProxy;
import ch.unibas.medizin.osce.client.managed.request.DescriptionProxy;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.managed.request.ProfessionProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientRequest;
import ch.unibas.medizin.osce.shared.MaritalStatus;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.WorkPermission;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.requestfactory.shared.Violation;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class StandardizedPatientEditActivity extends AbstractActivity implements
StandardizedPatientEditView.Presenter, 
StandardizedPatientEditView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private StandardizedPatientEditView view;
	private StandardizedPatientDetailsPlace place; 

	private RequestFactoryEditorDriver<StandardizedPatientProxy,StandardizedPatientEditViewImpl> editorDriver;
	private StandardizedPatientProxy standardizedPatient;
	private DescriptionProxy description;
	private BankaccountProxy bankAccount;
	private AnamnesisFormProxy anamnesisForm;
	private boolean save;
	private RequestFactoryEditorDriver<DescriptionProxy, DescriptionEditViewImpl> descriptionDriver;
	private RequestFactoryEditorDriver<BankaccountProxy, StandardizedPatientBankaccountEditSubViewImpl> bankaccountDriver;
	
	private CalendarUtil cal = new CalendarUtil();
	private StandardizedPatientBankaccountEditSubView bankaccountView;
	private UserPlaceSettings userSettings;
	
	private DMZSyncServiceAsync dmxSyncService = null;
	

	public StandardizedPatientEditActivity(StandardizedPatientDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;
		this.userSettings = new UserPlaceSettings((OsMaPlace) place);
		
	}

	public StandardizedPatientEditActivity(StandardizedPatientDetailsPlace place,
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

	@Override
	public String mayStop() {
		if (!save && changed())
			return "Changes will be discarded!";
		else
			return null;
	}
	
	private DescriptionEditView descriptionView;
	
	// use this to check if some value has changed since editing has started
	private boolean changed() {
		return editorDriver != null && editorDriver.flush().isChanged();
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		dmxSyncService = DMZSyncService.ServiceFactory.instance();
		
		Log.info("start");
		this.view = new StandardizedPatientEditViewImpl();
		this.widget = panel;
		loadDisplaySettings();

		editorDriver = view.createEditorDriver();

		view.setDelegate(this);
		
		bankaccountView = new StandardizedPatientBankaccountEditSubViewImpl();
		view.getBankEditPanel().add(bankaccountView);
		bankaccountDriver = bankaccountView.createEditorDriver();
		
		
		descriptionView = new DescriptionEditViewImpl();
		view.getDescriptionPanel().add(descriptionView);
		descriptionDriver = descriptionView.createEditorDriver();

		view.setWorkPermissionPickerValues(Arrays.asList(WorkPermission.values()));
		view.setMaritalStatusPickerValues(Arrays.asList(MaritalStatus.values()));
		view.setNationalityPickerValues(Collections.<NationalityProxy>emptyList());
		bankaccountView.setCountryPickerValues(Collections.<NationalityProxy>emptyList());
		
		requests.nationalityRequest().findNationalityEntries(0, 50).
				with(ch.unibas.medizin.osce.client.managed.ui.NationalityProxyRenderer.instance().getPaths()).
				fire(new OSCEReceiver<List<NationalityProxy>>() {

			public void onSuccess(List<NationalityProxy> response) {
				List<NationalityProxy> values = new ArrayList<NationalityProxy>();
				values.add(null);
				values.addAll(response);
				view.setNationalityPickerValues(values);
				bankaccountView.setCountryPickerValues(values);
			}
		});

		view.setProfessionPickerValues(Collections.<ProfessionProxy>emptyList());
		requests.professionRequest().findProfessionEntries(0, 50).
				with(ch.unibas.medizin.osce.client.managed.ui.ProfessionProxyRenderer.instance().getPaths()).
				fire(new OSCEReceiver<List<ProfessionProxy>>() {

			public void onSuccess(List<ProfessionProxy> response) {
				List<ProfessionProxy> values = new ArrayList<ProfessionProxy>();
				values.add(null);
				values.addAll(response);
				view.setProfessionPickerValues(values);
			}
		});

//		view.setBankaccountPickerValues(Collections.<BankaccountProxy>emptyList());
//		requests.bankaccountRequest().findBankaccountEntries(0, 50).with(ch.unibas.medizin.osce.client.managed.ui.BankaccountProxyRenderer.instance().getPaths()).fire(new Receiver<List<BankaccountProxy>>() {
//
//			public void onSuccess(List<BankaccountProxy> response) {
//				List<BankaccountProxy> values = new ArrayList<BankaccountProxy>();
//				values.add(null);
//				values.addAll(response);
//				view.setBankaccountPickerValues(values);
//			}
//		});

//		view.setAnamnesisFormPickerValues(Collections.<AnamnesisFormProxy>emptyList());
//		requests.anamnesisFormRequest().findAnamnesisFormEntries(0, 50).with(ch.unibas.medizin.osce.client.managed.ui.AnamnesisFormProxyRenderer.instance().getPaths()).fire(new Receiver<List<AnamnesisFormProxy>>() {
//
//			public void onSuccess(List<AnamnesisFormProxy> response) {
//				List<AnamnesisFormProxy> values = new ArrayList<AnamnesisFormProxy>();
//				values.add(null);
//				values.addAll(response);
//				view.setAnamnesisFormPickerValues(values);
//			}
//		});
		
		if (this.place.getOperation()== Operation.EDIT){
			Log.info("edit");
			requests.find(place.getProxyId()).
					with("nationality", "profession", "langskills", "bankAccount", "bankAccount.country", "anamnesisForm", "descriptions").
					fire(new OSCEReceiver<Object>() {

				@Override
				public void onSuccess(Object response) {
					if(response instanceof StandardizedPatientProxy){
						Log.info(((StandardizedPatientProxy) response).getName());
						//init((StandardizedPatientProxy) response);
						standardizedPatient=(StandardizedPatientProxy)response;
						init();
					}
				}
			});
		} else {
			Log.info("new StandardizedPatient");
			//standardizedPatientPlace.setProxyId(standardizedPatient.stableId());
			init();
		}
		//		view.initialiseDriver(requests);
		widget.setWidget(view.asWidget());
		//setTable(view.getTable());
	}

	private void init() {

		StandardizedPatientRequest request = requests.standardizedPatientRequest();
//		DescriptionRequest descriptionRequest = requests.descriptionRequest();

		if(standardizedPatient == null) {
			standardizedPatient = request.create(StandardizedPatientProxy.class);
			//editorDriver.edit(standardizedPatient, request);
			description = request.create(DescriptionProxy.class);
			standardizedPatient.setDescriptions(description);
			
			bankAccount = request.create(BankaccountProxy.class);
			standardizedPatient.setBankAccount(bankAccount);
			
			anamnesisForm = request.create(AnamnesisFormProxy.class);
			anamnesisForm.setCreateDate(new Date());
			standardizedPatient.setAnamnesisForm(anamnesisForm);
			
			view.setEditTitle(false);
			Log.info("create");
		} else {
			
			standardizedPatient = request.edit(standardizedPatient);
			//editorDriver.edit(standardizedPatient, request);
			description = standardizedPatient.getDescriptions();
			bankAccount = standardizedPatient.getBankAccount();
			
			if (description == null){
				description = request.create(DescriptionProxy.class);
				standardizedPatient.setDescriptions(description);
			}
			
			if (bankAccount == null){
				bankAccount = request.create(BankaccountProxy.class);
				standardizedPatient.setBankAccount(bankAccount);
			}
			
			view.setEditTitle(true);
		//	view.setPatientId(""+standardizedPatient.getId());
			Log.info("edit");
		}
		
		Log.info("edit");
		editorDriver.edit(standardizedPatient, request);
		descriptionDriver.edit(description, request);
		bankaccountDriver.edit(bankAccount, request);
		
		descriptionView.setDescriptionContent(description.getDescription());
		initBirthDate();

		Log.info("persist");
		request.persist().using(standardizedPatient);
//		request.persist().using(description);

//		Log.info("flush");
//		editorDriver.flush();
//		descriptionDriver.flush();
		
		Log.debug("Create für: "+standardizedPatient.getName());
	}
	
	private void initBirthDate() {
		CalendarUtil cal = new CalendarUtil();
		int currentYear = cal.getYear();
		int oldestYear = currentYear - 123;
		
		cal.setDate(standardizedPatient.getBirthday());
		view.setDay(cal.getDay());
		view.setMonth(cal.getMonth());
		view.setYear(cal.getYear());
		
		if (view.getYear() < oldestYear) {
			oldestYear = view.getYear();
		}
		
		view.setAcceptableDays(getIntegerList(1, cal.getDaysInMonth()));
		view.setAcceptableMonths(getIntegerList(1, 12));
		view.setAcceptableYears(getIntegerList(oldestYear, currentYear));
	}
	
	private List<Integer> getIntegerList(int minValue, int maxValue) {
		List<Integer> values = new ArrayList<Integer>();
		for (int i = minValue; i <= maxValue; i++) {
			values.add(new Integer(i));
		}
		return values;
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public void cancelClicked() {
		if(this.place.getOperation() == Operation.EDIT)
			placeController.goTo(new StandardizedPatientDetailsPlace(standardizedPatient.stableId(), Operation.DETAILS));
		else
			placeController.goTo(new StandardizedPatientPlace("StandardizedPatientPlace!CANCEL"));
	}

	@Override
	public void saveClicked() {
		Log.info("saveClicked");

		// FIX bug(2011-11-12) - description is NOT saved the first time!
		// description data is read from view (data save is cascaded, therefore description has to be updated before SP is saved!)
		//descriptionDriver.flush();
		description.setDescription(descriptionView.getDescriptionContent());
		bankaccountDriver.flush();
		cal.setMonth(view.getMonth());
		cal.setYear(view.getYear());
		cal.setDay(view.getDay());
		standardizedPatient.setBirthday(cal.getDate());
		editorDriver.flush().fire(new OSCEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				Log.info("StandardizedPatient successfully saved.");
				
				save = true;
				placeController.goTo(new StandardizedPatientDetailsPlace(standardizedPatient.stableId(), Operation.NEW));
				//saveDescription();
			}
		});
	}
	
	public void storeDisplaySettings() {
		int detailsTab = view.getSelectedDetailsTab();
		userSettings.setValue("detailsTab", detailsTab);
		userSettings.flush();
	}
	
	private void loadDisplaySettings() {
		int detailsTab = 0;
		if (userSettings.hasSettings()) {
			detailsTab = userSettings.getIntValue("detailsTab");
		}
		
		// because of language & scar tab
		if (detailsTab > 3) {
			detailsTab = 0;
		}
		
		view.setSelectedDetailsTab(detailsTab);
	}
}
