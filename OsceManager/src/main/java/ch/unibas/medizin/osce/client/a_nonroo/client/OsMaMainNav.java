package ch.unibas.medizin.osce.client.a_nonroo.client;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AdministratorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisFormPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.BellSchedulePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.CircuitPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.DoctorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ExaminationSchedulePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.IndividualSchedulesPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.NationalityPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.OscePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ProfessionPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ScarPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.SpokenLanguagePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StudentsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.SummoningsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.i18n.Messages;

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
		initWidget(uiBinder.createAndBindUi(this));
		this.requests = requests;
		this.placeController = placeController;

		masterDataPanel.getHeaderTextAccessor().setText(Messages.MASTER_DATA);
		administrationPanel.getHeaderTextAccessor().setText(Messages.ADMINISTRATION);
		examinationsPanel.getHeaderTextAccessor().setText(Messages.EXAMS);
		simulationPatientsPanel.getHeaderTextAccessor().setText(Messages.SIM_PAT);

		people.setText(Messages.SIMULATION_PATIENTS);
		scars.setText(Messages.TRAITS);
		anamnesisChecks.setText(Messages.ANAMNESIS_VALUES);
		clinics.setText(Messages.CLINICS);
		doctors.setText(Messages.DOCTORS);

		administrators.setText(Messages.USER);
		nationalities.setText(Messages.NATIONALITIES);
		languages.setText(Messages.LANGUAGES);
		professions.setText(Messages.PROFESSIONS);
		rooms.setText(Messages.ROOMS);

		osces.setText(Messages.OSCES);
		circuit.setText(Messages.CIRCUIT);
		students.setText(Messages.STUDENTS);
		examinationSchedule.setText(Messages.EXAM_SCHEDULE);
		summonings.setText(Messages.SUMMONINGS);
		individualSchedules.setText(Messages.PRINT_INDIVIDUAL_SCHEDULES);
		bellSchedule.setText(Messages.EXPORT_BELL_SCHEDULE);

		roles.setText(Messages.ROLES);
		roleAssignment.setText(Messages.ROLE_ASSIGNMENT);
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
	DisclosurePanel simulationPatientsPanel;// Simulationspatienten

	@UiField
	Anchor roles;					// Rollen
	@UiField
	Anchor roleAssignment;			// Rollenzuweisung


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

	// TODO: UiHandler for roles
	// TODO: UiHandler for roleAssignment
}
