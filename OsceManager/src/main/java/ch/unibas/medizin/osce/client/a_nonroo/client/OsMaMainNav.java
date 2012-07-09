package ch.unibas.medizin.osce.client.a_nonroo.client;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AdministratorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.BellSchedulePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.CircuitPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.DoctorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ExaminationSchedulePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.IndividualSchedulesPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.LogPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.NationalityPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.OscePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ProfessionPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RolePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleScriptTemplatePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomMaterialsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleAssignmentPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ScarPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.SpokenLanguagePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StudentsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.SummoningsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.TopicsAndSpecPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.SemesterPopupView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.SemesterPopupViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeEvent;


import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterRequest;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class OsMaMainNav extends Composite {

	private static MainNavUiBinder uiBinder = GWT.create(MainNavUiBinder.class);

	interface MainNavUiBinder extends UiBinder<Widget, OsMaMainNav> {
	}

	public OsMaMainNav() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	private OsMaRequestFactory requests;

	private PlaceController placeController;

	// G: SPEC START =
		final private HandlerManager handlerManager = new HandlerManager(this);
		static SemesterPopupView semesterPopupView;	
		SemesterProxy semesterProxy=null;
	// G: SPEC END =
		
		// Role Module Issue V4
		OsceConstants constants = GWT.create(OsceConstants.class);
	
	@Inject
	public OsMaMainNav(OsMaRequestFactory requests, PlaceController placeController) {
		
		initWidget(uiBinder.createAndBindUi(this));
		this.requests = requests;
		this.placeController = placeController;
		
		doctorDataPanel.setAnimationEnabled(true);
		administrationPanel.setAnimationEnabled(true);
		simPatDataPanel.setAnimationEnabled(true);
		examinationsPanel.setAnimationEnabled(true);
		rolePanel.setAnimationEnabled(true);

		simPatDataPanel.getHeaderTextAccessor().setText(constants.simPat());
		doctorDataPanel.getHeaderTextAccessor().setText(constants.doctors());
		administrationPanel.getHeaderTextAccessor().setText(constants.administration());
		examinationsPanel.getHeaderTextAccessor().setText(constants.exams());		
		//By Spec[
		//simulationPatientsPanel.getHeaderTextAccessor().setText(constants.simPat());
		rolePanel.getHeaderTextAccessor().setText(constants.role());
		//By SPec]

		people.setText(constants.simulationPatients());
		scars.setText(constants.traits());
		anamnesisChecks.setText(constants.anamnesisValues());
		clinics.setText(constants.clinics());
		doctors.setText(constants.doctors());

		administrators.setText(constants.user());
		nationalities.setText(constants.nationalities());
		languages.setText(constants.languages());
		professions.setText(constants.professions());
		rooms.setText(constants.rooms());
		log.setText(constants.log());

		//By Spec[
		topicsAndSpec.setText(constants.topicsAndSpec());
		roleScriptTemplate.setText(constants.roleScriptTemplate());
		roomMaterials.setText(constants.roomMaterials());

		roleAssignmentPanel.setAnimationEnabled(true);
		roleAssignmentPanel.getHeaderTextAccessor().setText(
				constants.simulationPatients());

		roleAssignment.setText(constants.roleAssignments());
		//By Spec]
		
		osces.setText(constants.osces());
		circuit.setText(constants.circuit());
		students.setText(constants.students());
		examinationSchedule.setText(constants.examinationSchedule());
		summonings.setText(constants.sendSummonings());
		individualSchedules.setText(constants.printIndividualSchedules());
		bellSchedule.setText(constants.exportBellSchedule());

		roles.setText(constants.roles());
		
		
		
		/* commented by spec
		roleAssignments.setText(constants.roleAssignments());
		*/
		
		// G: SPEC START =

		requests.semesterRequest().findAllSemesters().fire(new OSCEReceiver<List<SemesterProxy>>() 
		{
				@Override
				public void onSuccess(List<SemesterProxy> response) 
				{						
						
						Log.info("~Success...");
						Log.info("~findAllSemesters()");
						Log.info("~SetValueinListBox");
						lstSemester.setAcceptableValues(response);	
						if(response != null && response.size()>0)
							lstSemester.setValue(response.get(0));
				}
		});
			
		
		final OsMaRequestFactory inrequests;
		final PlaceController inplaceController;
		inrequests=requests;
		inplaceController=placeController;
			
		
		lstSemester.addValueChangeHandler(new ValueChangeHandler() 
		{			
			@Override
			public void onValueChange(ValueChangeEvent event) 
			{
				if(lstSemester.getValue()!=null)
				{
					Log.info("~Selected value: " + lstSemester.getValue().getCalYear());
					handlerManager.fireEvent(new SelectChangeEvent(lstSemester.getValue()));
				}
				else
				{
					Log.info("Selected Value is Null");
				}
				//Log.info("~Selected value: " + lstSemester.getValue().getCalYear());		
				//handlerManager.fireEvent(new SelectChangeEvent(lstSemester.getValue()));				
			}			
		});
		
		// G: SPEC END =		
	}

	// G: SPEC START =
	
		@UiField(provided = true)
	    public ValueListBox<SemesterProxy> lstSemester = new ValueListBox<SemesterProxy>(new AbstractRenderer<SemesterProxy>() 
	    {
	        public String render(SemesterProxy obj) 
	        {	          
	        	return obj == null ? "" :  String.valueOf(obj.getSemester())+" "+String.valueOf(obj.getCalYear());
	        }
	    });
		
		@UiField
		IconButton btnEditSem;
		
		@UiField
		IconButton btnAddSem;
		
	// G: SPEC END =
	
	@UiField
	DisclosurePanel simPatDataPanel;		// Stammdaten
	
	@UiField
	DisclosurePanel doctorDataPanel;

	@UiField
	Anchor people;					// Personen
	@UiField
	Anchor scars;					// Merkmale
	@UiField
	Anchor anamnesisChecks;			// Anamnesewerte
	@UiField
	Anchor clinics;					// Spitäler
	@UiField
	Anchor doctors;					// Ärzte


	@UiField
	DisclosurePanel administrationPanel;	//Verwaltung

	@UiField
	Anchor administrators;			// Benutzer
	@UiField
	Anchor nationalities;			// Nationalitäten
	@UiField
	Anchor languages;				// Sprachen
	@UiField
	Anchor professions;				// Berufe
	@UiField
	Anchor rooms;					// Räume
	@UiField
	Anchor log;						// Log

	
	//By SPEC Role[
	@UiField 
	DisclosurePanel rolePanel;
	
	@UiField
	Anchor roles;
	
	@UiField
	Anchor topicsAndSpec;
	
	@UiField
	Anchor roleScriptTemplate;
	
	@UiField
	Anchor roomMaterials;
	
	//By SPEC Role]

	// Assignment : 3 By SPEC[

	@UiField
	DisclosurePanel roleAssignmentPanel;
	@UiField
	Anchor roleAssignment; 

	// By SPEC End//

	@UiField
	DisclosurePanel examinationsPanel;		// Prüfungen

	@UiField
	Anchor osces;					// OSCEs
	@UiField
	Anchor circuit;					// Postenlauf
	@UiField
	Anchor students;				// Studenten
	@UiField
	Anchor examinationSchedule;		// Prüfungsplan
	@UiField
	Anchor summonings;				// Aufgebote versenden
	@UiField
	Anchor individualSchedules; 	// Individuelle Pläne drucken
	@UiField
	Anchor bellSchedule;			// Klingelplan erstellen
	
	/* commented by spec
	@UiField
	DisclosurePanel simulationPatientsPanel;// Simulationspatienten
	*/
	//commented  by spec[
	//@UiField
	//Anchor roles;					// Rollen
	//commented by spec]
	
	/* commented by spec
	@UiField
	Anchor roleAssignments;			// Rollenzuweisung
	*/

	@UiHandler("people")
	void patientsClicked(ClickEvent event) {
		placeController.goTo(new StandardizedPatientPlace("PatientPlace"));
	}

	@UiHandler("scars")
	void scarsClicked(ClickEvent event) {
		placeController.goTo(new ScarPlace("ScarPlace"));
	}

	@UiHandler("anamnesisChecks")
	void anamnesisChecksClicked(ClickEvent event) {
		placeController.goTo(new AnamnesisCheckPlace("AnamnesisCheckPlace"));
	}

	@UiHandler("clinics")
	void clinicClicked(ClickEvent event) {
		placeController.goTo(new ClinicPlace("ClinicPlace"));
	}

	@UiHandler("doctors")
	void doctorClicked(ClickEvent event) {
		placeController.goTo(new DoctorPlace("DoctorPlace"));
	}

	@UiHandler("administrators")
	void administratorsClicked(ClickEvent event) {
		placeController.goTo(new AdministratorPlace("AdministratorPlace"));
	}

	@UiHandler("nationalities")
	void nationalitiesClicked(ClickEvent event) {
		placeController.goTo(new NationalityPlace("NationalityPlace"));
	}

	@UiHandler("languages")
	void languagesClicked(ClickEvent event) {
		placeController.goTo(new SpokenLanguagePlace("SpokenLanguagePlace"));
	}

	@UiHandler("professions")
	void professionsClicked(ClickEvent event) {
		placeController.goTo(new ProfessionPlace("ProfessionPlace"));
	}

	@UiHandler("rooms")
	void roomsClicked(ClickEvent event) {
		placeController.goTo(new RoomPlace("RoomPlace"));
	}
	
	@UiHandler("log")
	void logClicked(ClickEvent event) {
		placeController.goTo(new LogPlace("LogPlace"));
	}

	//By Spec[
	@UiHandler("topicsAndSpec")
	void topicsAndSpecClicked(ClickEvent event) {
		placeController.goTo(new TopicsAndSpecPlace("TopicsAndSpecPlace"));
	}
	
	@UiHandler("roleScriptTemplate")
	void roleScriptTemplateClicked(ClickEvent event) {
		placeController.goTo(new RoleScriptTemplatePlace("RoleScriptTemplatePlace"));
	}
	
	@UiHandler("roomMaterials")
	void roomMaterialsClicked(ClickEvent event) {
		placeController.goTo(new RoomMaterialsPlace("RoomMaterialsPlace"));
	}

	@UiHandler("roleAssignment")
	void roleAssignmentClicked(ClickEvent event) {
		ApplicationLoadingScreenEvent.register(requests.getEventBus(),
				new ApplicationLoadingScreenHandler() {
					@Override
					public void onEventReceived(
							ApplicationLoadingScreenEvent event) {
						Log.info("~~~~~~~~ApplicationLoadingScreenEvent onEventReceived Called");
						event.display();
					}
				});
		requests.getEventBus().fireEvent(
				new ApplicationLoadingScreenEvent(true));
		placeController.goTo(new RoleAssignmentPlace("SPRoleAssignmentPlace",
				handlerManager, lstSemester.getValue()));
		requests.getEventBus().fireEvent(
				new ApplicationLoadingScreenEvent(false));
	}

	//By Spec]
	@UiHandler("osces")
	void oscesClicked(ClickEvent event) 
	{
		
		placeController.goTo(new OscePlace("OscePlace",handlerManager,lstSemester.getValue()));
	}

	@UiHandler("circuit")
	void circuitClicked(ClickEvent event) 
	{
		placeController.goTo(new CircuitPlace("CircuitPlace",handlerManager,lstSemester.getValue()));
	}

	@UiHandler("students")
	void studentsClicked(ClickEvent event) {
		
		placeController.goTo(new StudentsPlace("StudentsPlace", handlerManager, lstSemester.getValue()));
	}

	@UiHandler("examinationSchedule")
	void examinationScheduleClicked(ClickEvent event) {
		placeController.goTo(new ExaminationSchedulePlace("ExaminationSchedulePlace"));
	}
	
	@UiHandler("summonings")
	void summoningsClicked(ClickEvent event) {
		placeController.goTo(new SummoningsPlace("SummoningsPlace"));
	}

	@UiHandler("individualSchedules")
	void individualSchedulesClicked(ClickEvent event) {
		placeController.goTo(new IndividualSchedulesPlace("IndividualSchedulesPlace"));
	}

	@UiHandler("bellSchedule")
	void bellScheduleClicked(ClickEvent event) {
		placeController.goTo(new BellSchedulePlace("BellSchedulePlace"));
	}
	
	@UiHandler("roles")
	void rolesClicked(ClickEvent event) {
		placeController.goTo(new RolePlace("RolePlace"));
	}
	
	/* commented by spec
	@UiHandler("roleAssignments")
	void roleAssignmentsClicked(ClickEvent event) {
		placeController.goTo(new RoleAssignmentsPlace("RoleAssignmentsPlace"));
	}
	*/
	
	// G: SPEC START =
	
	@UiHandler("btnAddSem")
	void btnAddSemClicked(ClickEvent event)
	{
		Log.info("~Add Clicked");
		Log.info("~btnAddSemClicked()");				
		initPopup();			
		btnCloseClick();	// Bind Event
		btnSaveClick(0);		// Bind Event
		//refreshListBox();
	}
		
	@UiHandler("btnEditSem")
	void btnEditSemClicked(ClickEvent event) 
	{
		Log.info("~Edit Clicked");
		Log.info("~btnEditSemClicked()");	
		
		if(lstSemester.getValue()==null)
		{
			// Role Module Issue V4
			//Window.alert("Please Select Semester to Edit.....");	
			final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.success());
			
			 dialogBox.showConfirmationDialog("Please Select Semester to Edit.....");
			 
			 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					dialogBox.hide();							
					Log.info("ok click");	
						}
					});	
		}
		else
		{																										
			Log.info("Semester Selected: " + lstSemester.getValue().getCalYear());			
			initPopup();								
			 
			((SemesterPopupViewImpl)semesterPopupView).txtYear.setText(lstSemester.getValue().getCalYear().toString());
			((SemesterPopupViewImpl)semesterPopupView).enumSemester.setValue(lstSemester.getValue().getSemester());
			((SemesterPopupViewImpl)semesterPopupView).txtMaxYearEarning.setText(lstSemester.getValue().getMaximalYearEarnings() == null? "" : lstSemester.getValue().getMaximalYearEarnings().toString());			
			((SemesterPopupViewImpl)semesterPopupView).txtPriceStatist.setText(lstSemester.getValue().getPricestatist() == null? "" : lstSemester.getValue().getPricestatist().toString());
			((SemesterPopupViewImpl)semesterPopupView).txtPriceSP.setText(lstSemester.getValue().getPriceStandardizedPartient() == null? "" : lstSemester.getValue().getPriceStandardizedPartient().toString());
			
			btnCloseClick();	// Bind Event
			btnSaveClick(1);	// Bind Event
			
		}	
		
	}
	
	
	private void btnCloseClick() 
	{
		Log.info("Call Close Button");
		((SemesterPopupViewImpl)semesterPopupView).btnClose.addClickHandler(new ClickHandler() 
		{				
			@Override
			public void onClick(ClickEvent event) 
			{
				Log.info("~Click on Close Button");
				((SemesterPopupViewImpl)semesterPopupView).hide();	
				semesterPopupView=null;					
			}
		});				
	}
		
	private void btnSaveClick(int flag)
	{
		final int inflag=flag;
		((SemesterPopupViewImpl)semesterPopupView).btnSave.addClickHandler(new ClickHandler() 
		{				
			@Override
			public void onClick(ClickEvent event) 
			{
				
				if((((SemesterPopupViewImpl)semesterPopupView).enumSemester.getValue()) == null)
				{
					// Role Module Issue V4
					//Window.alert("Please Enter Semester" );					
					final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.success());
					
					 dialogBox.showConfirmationDialog("Please Enter Semester");
					 
					 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							dialogBox.hide();							
							Log.info("ok click");	
								}
							});	
				}
																
				else
				{
					Log.info("~Semester is not null");	
					if(((SemesterPopupViewImpl)semesterPopupView).txtYear.getText().trim().equals(""))
					{
						// Issue Role Module V4
						//Window.alert("Please Enter Year");
						final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.success());
						
						 dialogBox.showConfirmationDialog("Please Enter Year");
						 
						 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) {
								dialogBox.hide();							
								Log.info("ok click");	
									}
								});	
					}
					else
					{
						SemesterRequest semesterRequest=requests.semesterRequest();	

						if(inflag==0)	// In Create Mode
						{
							Log.info("~Create");
							semesterProxy=semesterRequest.create(SemesterProxy.class);							
						}
						else	// In Edit Mode
						{						
							Log.info("~Edit");								
							semesterProxy=semesterRequest.edit(lstSemester.getValue());	
						}
						
						String maxyearear = ((SemesterPopupViewImpl)semesterPopupView).txtMaxYearEarning.getText().trim() ;
						String pricestat=((SemesterPopupViewImpl)semesterPopupView).txtPriceStatist.getText().trim();
						String priceSP=((SemesterPopupViewImpl)semesterPopupView).txtPriceSP.getText().trim();
						String year=((SemesterPopupViewImpl)semesterPopupView).txtYear.getText().trim();
						
						Log.info("Year: "+year+"PriceStat: "+pricestat+"Price SP"+priceSP+"Max Year Ear"+maxyearear);
						//(maxyearear.matches("^[0-9]*.[0-9]*") || maxyearear.trim().equals("")) &&
						if((maxyearear.matches("^[0-9]*\\.?[0-9]*") || maxyearear.trim().equals("")) && 
							(pricestat.matches("^[0-9]*\\.?[0-9]*") || pricestat.trim().equals("")) && 
							(priceSP.matches("^[0-9]*\\.?[0-9]*") || priceSP.trim().equals(""))	
							&& (year.matches("^[0-9]{4}$"))
							) 
						{
							
							semesterProxy.setCalYear(Integer.parseInt(((SemesterPopupViewImpl)semesterPopupView).txtYear.getText().toString()));
							semesterProxy.setSemester(((SemesterPopupViewImpl)semesterPopupView).enumSemester.getValue());
							semesterProxy.setMaximalYearEarnings(((maxyearear == null) || (maxyearear.compareTo("")== 0)) ? null : Double.parseDouble(maxyearear));						
							semesterProxy.setPricestatist(((pricestat == null) || (pricestat.compareTo("")== 0)) ? null : Double.parseDouble(pricestat));
							semesterProxy.setPriceStandardizedPartient(((priceSP == null) || (priceSP.compareTo("")== 0)) ? null : Double.parseDouble(priceSP));															
							lstSemester.setValue(semesterProxy);
							
							final SemesterProxy insemesterPropxy;
							insemesterPropxy=semesterProxy;
							
							semesterRequest.persist().using(semesterProxy).fire(new OSCEReceiver<Void>() 
							{						
								@Override
								public void onSuccess(Void response1) 
								{													
									
									Log.info("~Success Call....");
									Log.info("~semesterRequest.persist()");	
									Log.info("Add New Semester in Semester Table");						
									
									if(((SemesterPopupViewImpl)semesterPopupView).isShowing())
									{
										((SemesterPopupViewImpl)semesterPopupView).hide();	
									}
																																	
									
									// 	Get Stable Id
									
									requests.find(insemesterPropxy.stableId()).fire(new OSCEReceiver<Object>() 
									{
										@Override
										public void onSuccess(Object response) 
										{
											Log.info("~Call Success..." );
											Log.info("~Find Semester Proxy: "+ ((SemesterProxy)response).getCalYear());																										
											refreshListBox((SemesterProxy)response);													
											semesterPopupView=null;										
										}							
									});												
								}					
							});	
						}
						else
						{
							// Role Module Issue V4
							
							//Window.alert("Please Enter Valid Data");
							final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.success());
							
							 dialogBox.showConfirmationDialog("Please Enter Valid Data");
							 
							 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									dialogBox.hide();							
									Log.info("ok click");	
										}
									});	
							
							
						}	
					}					
				}
				
			}
		});			
	}
		
		;

		private void initPopup() 
		{
			Log.info("~initPopup() Call");				
			semesterPopupView=new SemesterPopupViewImpl();	
		}

		public void refreshListBox(SemesterProxy semesterProxy)
		{								
			requests.semesterRequest().findAllSemesters().fire(new OSCEReceiver<List<SemesterProxy>>() 
			{
						@Override
						public void onSuccess(List<SemesterProxy> response) 
						{						
								Log.info("~Success...");
								Log.info("~findAllSemesters()");
								Log.info("~SetValueinListBox");																		
								lstSemester.setAcceptableValues(response);									
						}						
			});
			Log.info("~Semester Proxy Find: " + semesterProxy.getCalYear());
			lstSemester.setValue(semesterProxy);			
		}
		
	// G: SPEC END =
	
	
	
}
