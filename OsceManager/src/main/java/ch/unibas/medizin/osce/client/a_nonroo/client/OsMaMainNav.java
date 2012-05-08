package ch.unibas.medizin.osce.client.a_nonroo.client;

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
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleAssignmentsPlace;
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
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
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

	@Inject
	public OsMaMainNav(OsMaRequestFactory requests, PlaceController placeController) {
		OsceConstants constants = GWT.create(OsceConstants.class);
		initWidget(uiBinder.createAndBindUi(this));
		this.requests = requests;
		this.placeController = placeController;
		
		masterDataPanel.setAnimationEnabled(true);
		administrationPanel.setAnimationEnabled(true);
		examinationsPanel.setAnimationEnabled(true);
		rolePanel.setAnimationEnabled(true);

		masterDataPanel.getHeaderTextAccessor().setText(constants.masterData());
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
	}

	@UiField
	DisclosurePanel masterDataPanel;		// Stammdaten

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
	//By Spec]
	@UiHandler("osces")
	void oscesClicked(ClickEvent event) {
		placeController.goTo(new OscePlace("OscePlace"));
	}

	@UiHandler("circuit")
	void circuitClicked(ClickEvent event) {
		placeController.goTo(new CircuitPlace("CircuitPlace"));
	}

	@UiHandler("students")
	void studentsClicked(ClickEvent event) {
		placeController.goTo(new StudentsPlace("StudentsPlace"));
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
}
