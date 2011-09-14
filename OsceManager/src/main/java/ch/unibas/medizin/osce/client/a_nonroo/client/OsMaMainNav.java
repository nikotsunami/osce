package ch.unibas.medizin.osce.client.a_nonroo.client;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AdministratorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisFormPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.DoctorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.NationalityPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ProfessionPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ScarPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.SpokenLanguagePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;

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
   }

	
	@UiField
	DisclosurePanel masterDataPanel;		// Stammdaten

			@UiField
			Anchor people;					// Personen
			@UiField
			Anchor scars;					// Narben
			@UiField
			Anchor anamnesisForms;			// Anamnesewerte
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
	DisclosurePanel examinationsPanel;		// Prüfungen
	
			@UiField
			Anchor osces;					// OSCEs
			@UiField
			Anchor course;					// Postenlauf
			@UiField
			Anchor students;				// Studenten
			@UiField
			Anchor examinationSchedule;		// Prüfungsplan
			@UiField
			Anchor printIndividualSchedules; // Individuelle Pläne drucken
	
	@UiField
	DisclosurePanel simulationPatientsPanel;// Simulationspatienten
	
			@UiField
			Anchor simulationPatients;		// Simulationspatienten
			@UiField
			Anchor roles;					// Rollen
			@UiField
			Anchor roleAssignment;			// Rollenzuweisung
	
			
			
	//Old stuff, just for testing
			
//	@UiField
//	DisclosurePanel administratorPanel;
//	@UiField
//	DisclosurePanel anamnesisPanel;
//	@UiField
//	DisclosurePanel patientPanel;
//
//	@UiField
//	Anchor administratorsOld;
//	@UiField
//	Anchor anamnesisFormsOld;
//	@UiField
//	Anchor patient;
//	@UiField
//	Anchor doctor;
	
	
	@UiHandler("people")
	void patientsClicked(ClickEvent event) {
		placeController.goTo(new StandardizedPatientPlace("PatientPlace"));
	}
	
	@UiHandler("scars")
	void scarsClicked(ClickEvent event) {
		placeController.goTo(new ScarPlace("ScarPlace"));
	}
	
	@UiHandler("anamnesisForms")
	void anamnesisClicked(ClickEvent event) {
		placeController.goTo(new AnamnesisFormPlace("AnamnesisFormPlace"));
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
	
	// TODO: UiHandler for nationalities
	// TODO: UiHandler for languages
	// TODO: UiHandler for professions
	// TODO: UiHandler for osces
	// TODO: UiHandler for course
	// TODO: UiHandler for students
	// TODO: UiHandler for examinationSchedule
	// TODO: UiHandler for printIndividualSchedules
	// TODO: UiHandler for simulationPatients
	// TODO: UiHandler for roles
	// TODO: UiHandler for roleAssignment
}
