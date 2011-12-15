package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientDetailsPlace.Operation;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.DescriptionEditView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.DescriptionEditViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientEditView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientEditViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientLangSkillSubView;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.BankaccountProxy;
import ch.unibas.medizin.osce.client.managed.request.DescriptionProxy;
import ch.unibas.medizin.osce.client.managed.request.DescriptionRequest;
import ch.unibas.medizin.osce.client.managed.request.LangSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.managed.request.ProfessionProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientRequest;

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

public class StandardizedPatientEditActivity extends AbstractActivity implements
StandardizedPatientEditView.Presenter, 
StandardizedPatientEditView.Delegate,
StandardizedPatientLangSkillSubView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private StandardizedPatientEditView view;
	private StandardizedPatientDetailsPlace place;
	
	private StandardizedPatientLangSkillSubView standardizedPatientLangSkillSubView; 

	private RequestFactoryEditorDriver<StandardizedPatientProxy,StandardizedPatientEditViewImpl> editorDriver;
	private StandardizedPatientProxy standardizedPatient;
	private DescriptionProxy description;
	private boolean save;
	private RequestFactoryEditorDriver<DescriptionProxy, DescriptionEditViewImpl> descriptionDriver;

	public StandardizedPatientEditActivity(StandardizedPatientDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;
	}

	public StandardizedPatientEditActivity(StandardizedPatientDetailsPlace place,
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
		StandardizedPatientEditView standardizedPatientEditView = new StandardizedPatientEditViewImpl();

		this.widget = panel;
		this.view = standardizedPatientEditView;
		this.standardizedPatientLangSkillSubView = view.getStandardizedPatientLangSkillSubView();

		editorDriver = view.createEditorDriver();

		view.setDelegate(this);
		standardizedPatientLangSkillSubView.setDelegate(this);

		DescriptionEditView descriptionView = new DescriptionEditViewImpl();
		view.getDescriptionPanel().add(descriptionView);
		descriptionDriver = descriptionView.createEditorDriver();
		eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {
				//updateSelection(event.getNewPlace());
				// TODO implement
			}
		});
		//init();

		view.setNationalityPickerValues(Collections.<NationalityProxy>emptyList());
		requests.nationalityRequest().findNationalityEntries(0, 50).with(ch.unibas.medizin.osce.client.managed.ui.NationalityProxyRenderer.instance().getPaths()).fire(new Receiver<List<NationalityProxy>>() {

			public void onSuccess(List<NationalityProxy> response) {
				List<NationalityProxy> values = new ArrayList<NationalityProxy>();
				values.add(null);
				values.addAll(response);
				view.setNationalityPickerValues(values);
			}
		});

		view.setProfessionPickerValues(Collections.<ProfessionProxy>emptyList());
		requests.professionRequest().findProfessionEntries(0, 50).with(ch.unibas.medizin.osce.client.managed.ui.ProfessionProxyRenderer.instance().getPaths()).fire(new Receiver<List<ProfessionProxy>>() {

			public void onSuccess(List<ProfessionProxy> response) {
				List<ProfessionProxy> values = new ArrayList<ProfessionProxy>();
				values.add(null);
				values.addAll(response);
				view.setProfessionPickerValues(values);
			}
		});

		view.setBankaccountPickerValues(Collections.<BankaccountProxy>emptyList());
		requests.bankaccountRequest().findBankaccountEntries(0, 50).with(ch.unibas.medizin.osce.client.managed.ui.BankaccountProxyRenderer.instance().getPaths()).fire(new Receiver<List<BankaccountProxy>>() {

			public void onSuccess(List<BankaccountProxy> response) {
				List<BankaccountProxy> values = new ArrayList<BankaccountProxy>();
				values.add(null);
				values.addAll(response);
				view.setBankaccountPickerValues(values);
			}
		});

		view.setAnamnesisFormPickerValues(Collections.<AnamnesisFormProxy>emptyList());
		requests.anamnesisFormRequest().findAnamnesisFormEntries(0, 50).with(ch.unibas.medizin.osce.client.managed.ui.AnamnesisFormProxyRenderer.instance().getPaths()).fire(new Receiver<List<AnamnesisFormProxy>>() {

			public void onSuccess(List<AnamnesisFormProxy> response) {
				List<AnamnesisFormProxy> values = new ArrayList<AnamnesisFormProxy>();
				values.add(null);
				values.addAll(response);
				view.setAnamnesisFormPickerValues(values);
			}
		});
		
		if (this.place.getOperation()==StandardizedPatientDetailsPlace.Operation.EDIT){
			Log.info("edit");
			requests.find(place.getProxyId()).with("nationality", "profession", "langskills", "bankAccount", "anamnesisForm", "descriptions").fire(new Receiver<Object>() {

				public void onFailure(ServerFailure error){
					Log.error(error.getMessage());
				}
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
		widget.setWidget(standardizedPatientEditView.asWidget());
		//setTable(view.getTable());
	}

	private void init() {

		StandardizedPatientRequest request = requests.standardizedPatientRequest();
//		DescriptionRequest descriptionRequest = requests.descriptionRequest();

		if(standardizedPatient == null) {
			standardizedPatient = request.create(StandardizedPatientProxy.class);
			description = request.create(DescriptionProxy.class);
			standardizedPatient.setDescriptions(description);
			
			view.setEditTitle(false);
			Log.info("create");
		} else {
			description = standardizedPatient.getDescriptions();
			view.setEditTitle(true);
			Log.info("edit");
		}
		
		Log.info("edit");
		editorDriver.edit(standardizedPatient, request);
		descriptionDriver.edit(description, request);

		Log.info("persist");
		request.persist().using(standardizedPatient);
//		request.persist().using(description);

//		Log.info("flush");
//		editorDriver.flush();
//		descriptionDriver.flush();
		
		Log.debug("Create für: "+standardizedPatient.getName());
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public void cancelClicked() {
		if(this.place.getOperation() == StandardizedPatientDetailsPlace.Operation.EDIT)
			placeController.goTo(new StandardizedPatientDetailsPlace(standardizedPatient.stableId(), StandardizedPatientDetailsPlace.Operation.DETAILS));
		else
			placeController.goTo(new StandardizedPatientPlace("StandardizedPatientPlace!CANCEL"));
	}

	@Override
	public void saveClicked() {
		Log.info("saveClicked");

		// FIX bug(2011-11-12) - description is NOT saved the first time!
		// description data is read from view (data save is cascaded, therefore description has to be updated before SP is saved!)
		descriptionDriver.flush();
		
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
				Log.warn(" in StandardizedPatient -" + message);
			}
			@Override
			public void onSuccess(Void response) {
				Log.info("StandardizedPatient successfully saved.");

				save = true;
				
				placeController.goTo(new StandardizedPatientDetailsPlace(standardizedPatient.stableId(), StandardizedPatientDetailsPlace.Operation.DETAILS));

				//saveDescription();
			}
		}); 
	}

	@Override
	public void deleteLangSkillClicked(LangSkillProxy langSkill) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addLangSkillClicked() {
		// TODO Auto-generated method stub
		
	}

//	private void saveDescription() {
//		// TODO: bug(2011-11-12) - description is NOT saved the first time!
//		
//		descriptionDriver.flush().fire(new Receiver<Void>() {
//
//			@Override
//			public void onSuccess(Void response) {
//				Log.info("Description successfully saved.");
//
//				placeController.goTo(new StandardizedPatientDetailsPlace(standardizedPatient.stableId(), StandardizedPatientDetailsPlace.Operation.DETAILS));		
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
