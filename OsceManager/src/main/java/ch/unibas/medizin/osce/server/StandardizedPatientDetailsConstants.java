package ch.unibas.medizin.osce.server;

public interface StandardizedPatientDetailsConstants {

	public String name = "Name";
	public String preName = "Vorname";
	public String street = "Strasse";

	public String plzCity = "PLZ, Ort";
	public String telephone = "Telefon";
	public String mobile = "Mobiltelefon";
	public String birthday = "Geburtsdatum";
	public String height = "Grösse";
	public String weight = "Gewicht";
	public String email = "E-Mail";
	public String nationality = "Nationalität";
	public String profession = "Beruf/Ausbildung";
	public String languageSkills = "Sprachkenntnisse";
	public String bankAccount = "Bankkonto";
	public String description = "Kommentar";
	public String anamnesisForm = "Anamneseformular";
	public String contactInfo = "Kontaktinformationen";
	public String details = "Details";
	public String gender = "Geschlecht";

	public String all = "Alle";
	public String noMatter = "Macht nichts";

	public String male = "Männlich";
	public String female = "Weiblich";

	public String yes = "Ja";
	public String no = "Nein";

	public String from = "Von";
	public String to = "Zu";

	public String scars = "Narben";

	public String googleMaps = "Auf Google Maps anschauen";

	public String iban = "IBAN";
	public String bic = "BIC";

	public String docDesc = "Diese Informationen werden vertraulich behandelt.";

	public String signatureDoc = "Kontakt:\n"
			+ "SimulationsPatientenProgramm SPP\n"
			+ "z. Hd. Dr. Gabriele Voigt\n"
			+ "LZM - Medizinische Fakultät Basel\n"
			+ "Klingelbergstr. 61\n"
			+ "CH 4056 Basel\n"
			+ "Tel.  061 265 33 84 \n"
			+ "Schicken Sie dieses Formular bitte elektronisch ausgefüllt an:\n";
	public String emailSignDoc = "simulationspatienten-hmed@unibas.ch";

	public String ImageNotFoundPath = "http://127.0.0.1:8888/osMaEntry/gwt/unibas/images/Image_not_found.png";

	// AnamnesisCheckTypes

	// String AnamnesisCheckTypes[] = { "Offene Frage", "Ja/Nein-Frage ",
	// "Multiple-Choice mit Einfachauswahl",
	// "Multiple-Choice mit Mehrfachauswahl", "Trenntitel" };

	// public String QUESTION_OPEN = "Offene Frage";
	// public String QUESTION_YES_NO = "Ja/Nein-Frage ";
	// public String QUESTION_MULT_S = "Multiple-Choice mit Einfachauswahl";
	// public String QUESTION_MULT_M = "Multiple-Choice mit Mehrfachauswahl";
	// public String QUESTION_TITLE = "Trenntitel";

}
