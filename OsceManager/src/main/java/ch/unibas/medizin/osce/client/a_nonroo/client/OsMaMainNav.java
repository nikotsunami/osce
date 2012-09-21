package ch.unibas.medizin.osce.client.a_nonroo.client;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AdministratorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.BellSchedulePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.CircuitDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.CircuitPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.DoctorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ExaminationScheduleDetailPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ExaminationSchedulePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ExportOscePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ImportObjectiveViewPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ImporteOSCEPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.IndividualSchedulesPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.LogPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.NationalityPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.OscePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ProfessionPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleAssignmentPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RolePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleScriptTemplatePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomMaterialsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomPlace;
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
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
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
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;


public class OsMaMainNav extends Composite {

	private static MainNavUiBinder uiBinder = GWT.create(MainNavUiBinder.class);

	interface MainNavUiBinder extends UiBinder<Widget, OsMaMainNav> {
	}
	
	public OsMaMainNav() {
		initWidget(uiBinder.createAndBindUi(this));
		registerLoading();
	}
		
	private void registerLoading() {
		ApplicationLoadingScreenEvent.register(requests.getEventBus(),
				new ApplicationLoadingScreenHandler() {
					@Override
					public void onEventReceived(
							ApplicationLoadingScreenEvent event) {
//						Log.info(" ApplicationLoadingScreenEvent onEventReceived Called");
						event.display();
					}
				});
	}

	private static int menuStatus = 1;
	
	

	public static int getMenuStatus() {
		return menuStatus;
	}

	public static void setMenuStatus(int menuStatus) {
		OsMaMainNav.menuStatus = menuStatus;
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
	
	
		
	private class CloseClickHandler implements ClickHandler{

		private Panel sourcePanel;
		private Panel destinationPanel;
		private Widget dataWidget;
		
		private String minimizedText;
		
		private PopupPanel popupPanel;
		
		private HandlerRegistration handlerRegistration;
		
		public CloseClickHandler(Panel sourcePanel, Panel destinationPanel, Widget dataWidget,PopupPanel popupPanel, String minimizedText) {
			super();
			this.sourcePanel = sourcePanel;
			this.destinationPanel = destinationPanel;
			this.dataWidget = dataWidget;
			this.minimizedText = minimizedText;
			this.popupPanel = popupPanel;
			this.popupPanel.addStyleName("noBorder");
		}

		@Override
		public void onClick(ClickEvent event) {
			
			Log.info("old Adm");
			sourcePanel.remove(dataWidget);

			if(!sourcePanel.iterator().hasNext()){
				
				sourcePanel.setWidth("0px");
				
				sourcePanel.getParent().getParent().getParent().getParent().getParent().getElement().removeClassName("menuOpenContainer");
				sourcePanel.getParent().getParent().getParent().getParent().getParent().getElement().addClassName("menuCloseContainer");
				
				Log.info("Parent size == 0");
				menuStatus = 0;
				
				requests.getEventBus().fireEvent(new MenuClickEvent(menuStatus));
				
			}
			
			Button maxButton = new Button(minimizedText);
			maxButton.addStyleName("dockButton");
			
			maxButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					popupPanel.clear();
					
					Button sourceButton = (Button) event.getSource();
					
					setButtonHandler(sourceButton);
					popupPanel.add(dataWidget);
					
					int x = sourceButton.getAbsoluteLeft() + 5;
					int y = sourceButton.getAbsoluteTop();
					
					Log.info("pop-up postion: x="+x+"  y="+y);
					
					popupPanel.setPopupPosition(x+20, y);
					popupPanel.show();
					
				}
			});
			
			
			VerticalPanel panel = new VerticalPanel();
//			panel.addStyleName("verticalCenter");
//			panel.setHeight(((minimizedText.length()*7)+5)+"px");
			panel.setHeight("110px");
			panel.setWidth("20px");
			
			
			panel.add(maxButton);
			
			if(destinationPanel instanceof DockPanel)
				((DockPanel)destinationPanel).add(panel,DockPanel.NORTH);
			else
				destinationPanel.add(panel);
		}
		
		private void setOpenDisclosure(){
			if(((DisclosurePanel)dataWidget).isOpen())
				((DisclosurePanel)dataWidget).setOpen(false);
		}
		
		private void setButtonHandler(final Button maxButton) {
			
			if(constants.administration().equals(maxButton.getText())){
				Log.info("administration found");
				
				handlerRegistration.removeHandler();
				administrationClose.setIcon("arrowreturnthick-1-e");
				administrationClose.addStyleName("verticalFlip");
				handlerRegistration = administrationClose.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						
						Log.info("new administration click");
						sourcePanel.add(dataWidget);
						
						setOpenDisclosure();
						
						CloseClickHandler clickHandler = new CloseClickHandler(sourcePanel, destinationPanel, dataWidget, popupPanel, constants.administration());
						clickHandler.setHandlerRegistration(administrationClose.addClickHandler(clickHandler));
						
						administrationClose.setIcon("arrowreturnthick-1-w");
						administrationClose.removeStyleName("verticalFlip");
						
						handlerRegistration.removeHandler();
						maxButton.getParent().removeFromParent();
						
						sourcePanel.getParent().getParent().getParent().getParent().getParent().getElement().removeClassName("menuCloseContainer");
						sourcePanel.getParent().getParent().getParent().getParent().getParent().getElement().addClassName("menuOpenContainer");
						
						menuStatus = 1;
						requests.getEventBus().fireEvent(new MenuClickEvent(menuStatus));
					}
				});
				
			}else if(constants.doctors().equals(maxButton.getText())){
				
				Log.info("doctors found");
				
				handlerRegistration.removeHandler();
				doctorClose.setIcon("arrowreturnthick-1-e");
				doctorClose.addStyleName("verticalFlip");
				handlerRegistration = doctorClose.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						
						Log.info("new doctors click");
						sourcePanel.add(dataWidget);
						
						setOpenDisclosure();
						
						CloseClickHandler clickHandler = new CloseClickHandler(sourcePanel, destinationPanel, dataWidget, popupPanel, constants.doctors());
						clickHandler.setHandlerRegistration(doctorClose.addClickHandler(clickHandler));
						
						doctorClose.setIcon("arrowreturnthick-1-w");
						doctorClose.removeStyleName("verticalFlip");
						
						handlerRegistration.removeHandler();
						maxButton.getParent().removeFromParent();
						
						sourcePanel.getParent().getParent().getParent().getParent().getParent().getElement().removeClassName("menuCloseContainer");
						sourcePanel.getParent().getParent().getParent().getParent().getParent().getElement().addClassName("menuOpenContainer");
						
						menuStatus = 1;
						requests.getEventBus().fireEvent(new MenuClickEvent(menuStatus));
					}
				});
				
			}else if(constants.exams().equals(maxButton.getText())){
				
				Log.info("exams found");
				
				handlerRegistration.removeHandler();
				examinationsClose.setIcon("arrowreturnthick-1-e");
				examinationsClose.addStyleName("verticalFlip");
				handlerRegistration = examinationsClose.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						
						Log.info("new exams click");
						sourcePanel.add(dataWidget);
						
						setOpenDisclosure();
						
						CloseClickHandler clickHandler = new CloseClickHandler(sourcePanel, destinationPanel, dataWidget, popupPanel, constants.exams());
						clickHandler.setHandlerRegistration(examinationsClose.addClickHandler(clickHandler));
						
						examinationsClose.setIcon("arrowreturnthick-1-w");
						examinationsClose.removeStyleName("verticalFlip");
						
						handlerRegistration.removeHandler();
						maxButton.getParent().removeFromParent();
						
						sourcePanel.getParent().getParent().getParent().getParent().getParent().getElement().removeClassName("menuCloseContainer");
						sourcePanel.getParent().getParent().getParent().getParent().getParent().getElement().addClassName("menuOpenContainer");
						menuStatus = 1;
						requests.getEventBus().fireEvent(new MenuClickEvent(menuStatus));
					}
				});
				
			}else if(constants.roleAssignments().equals(maxButton.getText())){
				
				Log.info("roleAssignments found");
				
				handlerRegistration.removeHandler();
				roleAssignmentClose.setIcon("arrowreturnthick-1-e");
				roleAssignmentClose.addStyleName("verticalFlip");
				handlerRegistration = roleAssignmentClose.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						
						Log.info("new roleAssignments click");
						sourcePanel.add(dataWidget);
						
						setOpenDisclosure();
						
						CloseClickHandler clickHandler = new CloseClickHandler(sourcePanel, destinationPanel, dataWidget, popupPanel, constants.roleAssignments());
						clickHandler.setHandlerRegistration(roleAssignmentClose.addClickHandler(clickHandler));
						
						roleAssignmentClose.setIcon("arrowreturnthick-1-w");
						roleAssignmentClose.removeStyleName("verticalFlip");
						
						handlerRegistration.removeHandler();
						maxButton.getParent().removeFromParent();
						
						sourcePanel.getParent().getParent().getParent().getParent().getParent().getElement().removeClassName("menuCloseContainer");
						sourcePanel.getParent().getParent().getParent().getParent().getParent().getElement().addClassName("menuOpenContainer");
						
						menuStatus = 1;
						requests.getEventBus().fireEvent(new MenuClickEvent(menuStatus));
					}
				});
				
			}else if(constants.role().equals(maxButton.getText())){
				
				Log.info("role found");
				
				handlerRegistration.removeHandler();
				roleClose.setIcon("arrowreturnthick-1-e");
				roleClose.addStyleName("verticalFlip");
				handlerRegistration = roleClose.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						
						Log.info("new role click");
						sourcePanel.add(dataWidget);
						
						setOpenDisclosure();
						
						CloseClickHandler clickHandler = new CloseClickHandler(sourcePanel, destinationPanel, dataWidget, popupPanel, constants.role());
						clickHandler.setHandlerRegistration(roleClose.addClickHandler(clickHandler));
						
						roleClose.setIcon("arrowreturnthick-1-w");
						roleClose.removeStyleName("verticalFlip");
						
						handlerRegistration.removeHandler();
						maxButton.getParent().removeFromParent();
						
						sourcePanel.getParent().getParent().getParent().getParent().getParent().getElement().removeClassName("menuCloseContainer");
						sourcePanel.getParent().getParent().getParent().getParent().getParent().getElement().addClassName("menuOpenContainer");
						
						menuStatus = 1;
						requests.getEventBus().fireEvent(new MenuClickEvent(menuStatus));
					}
				});
				
			}else if(constants.simPat().equals(maxButton.getText())){
				
				Log.info("simPat found");
				
				handlerRegistration.removeHandler();
				simPatClose.setIcon("arrowreturnthick-1-e");
				simPatClose.addStyleName("verticalFlip");
				handlerRegistration = simPatClose.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						
						Log.info("new simPat click");
						sourcePanel.add(dataWidget);
						
						setOpenDisclosure();
						
						CloseClickHandler clickHandler = new CloseClickHandler(sourcePanel, destinationPanel, dataWidget, popupPanel, constants.simPat());
						clickHandler.setHandlerRegistration(simPatClose.addClickHandler(clickHandler));
						
						simPatClose.setIcon("arrowreturnthick-1-w");
						simPatClose.removeStyleName("verticalFlip");
						
						handlerRegistration.removeHandler();
						maxButton.getParent().removeFromParent();
						
						sourcePanel.getParent().getParent().getParent().getParent().getParent().getElement().removeClassName("menuCloseContainer");
						sourcePanel.getParent().getParent().getParent().getParent().getParent().getElement().addClassName("menuOpenContainer");
						
						menuStatus = 1;
						requests.getEventBus().fireEvent(new MenuClickEvent(menuStatus));
					}
				});
			}
			
		}


		public Panel getSourcePanel() {
			return sourcePanel;
		}


		public void setSourcePanel(Panel sourcePanel) {
			this.sourcePanel = sourcePanel;
		}


		public Panel getDestinationPanel() {
			return destinationPanel;
		}


		public void setDestinationPanel(Panel destinationPanel) {
			this.destinationPanel = destinationPanel;
		}

		public Widget getDataWidget() {
			return dataWidget;
		}

		public void setDataWidget(Widget dataWidget) {
			this.dataWidget = dataWidget;
		}

		public String getMinimizedText() {
			return minimizedText;
		}

		public void setMinimizedText(String minimizedText) {
			this.minimizedText = minimizedText;
		}

		public HandlerRegistration getHandlerRegistration() {
			return handlerRegistration;
		}

		public void setHandlerRegistration(HandlerRegistration handlerRegistration) {
			this.handlerRegistration = handlerRegistration;
		}
		
		
		
	}
	
	@Inject
	public OsMaMainNav(OsMaRequestFactory requests, PlaceController placeController,final PlaceHistoryHandler placeHistoryHandler) {
		
		initWidget(uiBinder.createAndBindUi(this));
		this.requests = requests;
		this.placeController = placeController;
		
		this.menuStatus = 1;
		
		CloseClickHandler adminHandler = new CloseClickHandler(menuContainer, dockPanel, administrationPanel, new PopupPanel(true), constants.administration());
		CloseClickHandler doctorHandler = new CloseClickHandler(menuContainer, dockPanel, doctorDataPanel,new PopupPanel(true) , constants.doctors());
		CloseClickHandler examsHandler = new CloseClickHandler(menuContainer, dockPanel, examinationsPanel,new PopupPanel(true) , constants.exams());
		CloseClickHandler roleAssignmentHandler = new CloseClickHandler(menuContainer, dockPanel, roleAssignmentPanel,new PopupPanel(true) , constants.roleAssignments());
		CloseClickHandler roleHandler = new CloseClickHandler(menuContainer, dockPanel, rolePanel,new PopupPanel(true) , constants.role());
		CloseClickHandler simPatHandler = new CloseClickHandler(menuContainer, dockPanel, simPatDataPanel,new PopupPanel(true) , constants.simPat());
		
		doctorDataPanel.setAnimationEnabled(true);
		administrationPanel.setAnimationEnabled(true);
		simPatDataPanel.setAnimationEnabled(true);
		examinationsPanel.setAnimationEnabled(true);
		rolePanel.setAnimationEnabled(true);

		simPatHandler.setHandlerRegistration(simPatClose.addClickHandler(simPatHandler));
		doctorHandler.setHandlerRegistration(doctorClose.addClickHandler(doctorHandler));
		examsHandler.setHandlerRegistration(examinationsClose.addClickHandler(examsHandler));
		adminHandler.setHandlerRegistration(administrationClose.addClickHandler(adminHandler));
		roleHandler.setHandlerRegistration(roleClose.addClickHandler(roleHandler));
		roleAssignmentHandler.setHandlerRegistration(roleAssignmentClose.addClickHandler(roleAssignmentHandler));
		
		simPatDataPanelHeaderText.setText(constants.simPat());
		doctorDataPanelHeaderText.setText(constants.doctors());
		examinationsPanelHeaderText.setText(constants.exams());
		administrationPanelHeaderText.setText(constants.administration());
		rolePanelHeaderText.setText(constants.role());
		roleAssignmentPanelHeaderText.setText(constants.roleAssignments());
		
//		simPatDataPanel.getHeaderTextAccessor().setText(constants.simPat());
//		doctorDataPanel.getHeaderTextAccessor().setText(constants.doctors());
//		administrationPanel.getHeaderTextAccessor().setText(constants.administration());
//		examinationsPanel.getHeaderTextAccessor().setText(constants.exams());
		
		//By Spec[
		//simulationPatientsPanel.getHeaderTextAccessor().setText(constants.simPat());
		//rolePanel.getHeaderTextAccessor().setText(constants.role());
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
		//roleAssignmentPanel.getHeaderTextAccessor().setText(
			//	constants.simulationPatients());

		roleAssignment.setText(constants.roleAssignments());
		//By Spec]
		
		labelSemester.setText(constants.semester() + ":");
		osces.setText(constants.manageOsces());
		circuit.setText(constants.circuit());
		students.setText(constants.students());
		examinationSchedule.setText(constants.examinationSchedule());
		summonings.setText(constants.sendSummonings());
		individualSchedules.setText(constants.printIndividualSchedules());
		bellSchedule.setText(constants.exportBellSchedule());

		roles.setText(constants.roles());
		
		importObjective.setText(constants.importObjective());
		
		importeOSCE.setText(constants.importeosce());
		
		exportOSCE.setText(constants.exportosce());
		
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
						
						
						ExaminationSchedulePlace.semesterProxy=lstSemester.getValue();
						CircuitPlace.semesterProxy=lstSemester.getValue();
						placeHistoryHandler.handleCurrentHistory();
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
	registerLoading();
		
		ExaminationSchedulePlace.handler=handlerManager;
		CircuitPlace.handler=handlerManager;
		
		
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
	
	
//	For Test
	
	@UiField
	DockPanel dockPanel;
	
//	@UiField
//	VerticalPanel dockPanel;
	
	@UiField
	VerticalPanel menuContainer;
	
	@UiField
	IconButton simPatClose;
	
	@UiField
	Label simPatDataPanelHeaderText;
	
	@UiField
	IconButton doctorClose;
	
	@UiField
	Label doctorDataPanelHeaderText;
	
	@UiField
	IconButton roleClose;
	
	@UiField
	Label rolePanelHeaderText;
	
	@UiField
	IconButton examinationsClose;
	
	@UiField
	Label examinationsPanelHeaderText;
	
	@UiField
	IconButton roleAssignmentClose;
	
	@UiField
	Label roleAssignmentPanelHeaderText;
	
	@UiField
	IconButton administrationClose;
	
	@UiField
	Label administrationPanelHeaderText;

//	For Test
	
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
	@UiField
	Label labelSemester;
	
	//learning objective
	@UiField
	Anchor importObjective;
	
	//eosce
	@UiField
	Anchor importeOSCE;
	
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
	
	@UiField
	Anchor exportOSCE;
	
	@UiHandler("exportOSCE")
	void exportOSCEClicked(ClickEvent event)
	{
		placeController.goTo(new ExportOscePlace("ExportOscePlace", handlerManager, lstSemester.getValue()));
	}
	
	@UiHandler("importeOSCE")
	void importeOSCEClicked(ClickEvent event)
	{
		placeController.goTo(new ImporteOSCEPlace("ImporteOSCEPlace"));
	}
	
	//learning objective
	@UiHandler("importObjective")
	void importObjectiveClicked(ClickEvent event)
	{
		placeController.goTo(new ImportObjectiveViewPlace("ImportObjectiveViewPlace"));
	}
	//learning objective

	@UiHandler("people")
	void patientsClicked(ClickEvent event) {
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		placeController.goTo(new StandardizedPatientPlace("PatientPlace"));
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
	}

	@UiHandler("scars")
	void scarsClicked(ClickEvent event) {
		placeController.goTo(new ScarPlace("ScarPlace"));
	}

	@UiHandler("anamnesisChecks")
	void anamnesisChecksClicked(ClickEvent event) {
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		placeController.goTo(new AnamnesisCheckPlace("AnamnesisCheckPlace"));
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
		
	}

	@UiHandler("clinics")
	void clinicClicked(ClickEvent event) {
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		placeController.goTo(new ClinicPlace("ClinicPlace"));
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
	}

	@UiHandler("doctors")
	void doctorClicked(ClickEvent event) {
		
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		placeController.goTo(new DoctorPlace("DoctorPlace"));
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
	}

	@UiHandler("administrators")
	void administratorsClicked(ClickEvent event) {
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		placeController.goTo(new AdministratorPlace("AdministratorPlace"));
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
		
	}

	@UiHandler("nationalities")
	void nationalitiesClicked(ClickEvent event) {
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		placeController.goTo(new NationalityPlace("NationalityPlace"));
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
		
	}

	@UiHandler("languages")
	void languagesClicked(ClickEvent event) {
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		placeController.goTo(new SpokenLanguagePlace("SpokenLanguagePlace"));
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
		
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
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		placeController.goTo(new TopicsAndSpecPlace("TopicsAndSpecPlace"));
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
		
	}
	
	@UiHandler("roleScriptTemplate")
	void roleScriptTemplateClicked(ClickEvent event) {
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		placeController.goTo(new RoleScriptTemplatePlace("RoleScriptTemplatePlace"));
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
		
	}
	
	@UiHandler("roomMaterials")
	void roomMaterialsClicked(ClickEvent event) {
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		placeController.goTo(new RoomMaterialsPlace("RoomMaterialsPlace"));
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
	
	}

	@UiHandler("roleAssignment")
	void roleAssignmentClicked(ClickEvent event) {
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		placeController.goTo(new RoleAssignmentPlace("SPRoleAssignmentPlace",handlerManager, lstSemester.getValue()));
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
	}

	//By Spec]
	@UiHandler("osces")
	void oscesClicked(ClickEvent event) 
	{
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		placeController.goTo(new OscePlace("OscePlace",handlerManager,lstSemester.getValue()));
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
		
		//placeController.goTo(new OscePlace("OscePlace",handlerManager,lstSemester.getValue()));
	}

	@UiHandler("circuit")
	void circuitClicked(ClickEvent event) 
	{
		CircuitPlace.handler=handlerManager;
		CircuitPlace.semesterProxy=lstSemester.getValue();
		
		 if (placeController.getWhere() instanceof CircuitDetailsPlace) {		      
		      return;
		    }
		 else
		     placeController.goTo(new CircuitPlace("CircuitPlace",handlerManager,lstSemester.getValue()));
	}

	@UiHandler("students")
	void studentsClicked(ClickEvent event) {
		
		placeController.goTo(new StudentsPlace("StudentsPlace", handlerManager, lstSemester.getValue()));
	}

	@UiHandler("examinationSchedule")
	void examinationScheduleClicked(ClickEvent event) {
		//placeController.goTo(new ExaminationSchedulePlace("ExaminationSchedulePlace"));
		ExaminationSchedulePlace.handler=handlerManager;
		ExaminationSchedulePlace.semesterProxy=lstSemester.getValue();
		 if (placeController.getWhere() instanceof ExaminationScheduleDetailPlace) {		      
		      return;
		    }
		 else
		    placeController.goTo(new ExaminationSchedulePlace("ExaminationSchedulePlace",handlerManager,lstSemester.getValue()));
	}
	
	@UiHandler("summonings")
	void summoningsClicked(ClickEvent event) {
//		placeController.goTo(new SummoningsPlace("SummoningsPlace"));
		placeController.goTo(new SummoningsPlace("SummoningsPlace", handlerManager, lstSemester.getValue()));
	}

	@UiHandler("individualSchedules")
	void individualSchedulesClicked(ClickEvent event) {
		//placeController.goTo(new IndividualSchedulesPlace("IndividualSchedulesPlace"));
		// Module10 Create plans
		Log.info("Click on individual Schedules.");
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		placeController.goTo(new IndividualSchedulesPlace("IndividualSchedulesPlace", handlerManager, lstSemester.getValue()));
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
		// E Module10 Create plans
	}

	@UiHandler("bellSchedule")
	void bellScheduleClicked(ClickEvent event) {
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		placeController.goTo(new BellSchedulePlace("BellSchedulePlace",handlerManager, lstSemester.getValue()));
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
	}
	
	@UiHandler("roles")
	void rolesClicked(ClickEvent event) {
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		placeController.goTo(new RolePlace("RolePlace"));
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
	
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
							
							// Highlight onViolation
							Log.info("Map Size: " + ((SemesterPopupViewImpl)semesterPopupView).semesterMap.size());
							// E Highlight onViolation
							
							semesterRequest.persist().using(semesterProxy).fire(new OSCEReceiver<Void>(((SemesterPopupViewImpl)semesterPopupView).semesterMap) 
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
			handlerManager.fireEvent(new SelectChangeEvent(lstSemester.getValue()));			
		}
		
	// G: SPEC END =
	
	
	
}
