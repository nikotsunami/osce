package ch.unibas.medizin.osce.shared.i18n;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface OsceConstantsWithLookup extends ConstantsWithLookup {
	public String QUESTION_OPEN();
	public String QUESTION_YES_NO();
	public String QUESTION_MULT_S();
	public String QUESTION_MULT_M();
	public String QUESTION_TITLE();
	
	public String STUDENT();
	public String PATIENT();
	public String EXAMINER();
	
	public String AND();
	public String OR();
	
	public String EQUALS();
	public String NOT_EQUALS();
	public String LESS();
	public String MORE();
	public String ANAMNESIS_EQUALS();
	public String ANAMNESIS_NOT_EQUALS();
	public String ANAMNESIS_LESS();
	public String ANAMNESIS_MORE();
	public String NUMERIC_EQUALS();
	public String NUMERIC_NOT_EQUALS();
	public String NUMERIC_LESS();
	public String NUMERIC_MORE();
	public String LANGSKILL_EQUALS();
	public String LANGSKILL_NOT_EQUALS();
	public String LANGSKILL_LESS();
	public String LANGSKILL_MORE();
	public String NATIONALITY_EQUALS();
	public String NATIONALITY_NOT_EQUALS();
	public String NATIONALITY_LESS();
	public String NATIONALITY_MORE();
	public String SCAR_EQUALS();
	public String SCAR_NOT_EQUALS();
	public String SCAR_MORE();
	public String SCAR_LESS();
	
	public String MALE();
	public String FEMALE();
	
	public String A1();
	public String A2();
	public String B1();
	public String B2();
	public String C1();
	public String C2();
	public String NATIVE_SPEAKER();
	
	public String COMMENT();
	public String HEIGHT();
	public String WEIGHT();
	public String BMI();
	public String SCAR();
	public String LANGUAGE();
	public String ANAMNESIS();
	//By SPEC[
	public String NATIONALITY();
	public String PERPIECE();
	public String PERDAY();
	public String CONSUMABLE();
	public String EQUIPMENT();
	public String FORSIMPAT();

	
	//By SPEC]
	
	public String AUTHOR();
	public String REVIEWER();
	
	public String ASC();
	public String DESC();
	
//	public String SCAR();
	public String TATTOO();
	public String NOT_TO_EXAMINE();
	
	public String EDIT();
	public String DETAILS();
	public String CREATE();
	public String NEW();
	//Assginment G[
	public String DAY();	
	//]Assignment G
	
	public String SJ1();
	public String SJ2();
	public String SJ3();
	public String SJ4();
	public String SJ5();
	public String SJ6();
	
	public String B();
	public String C();
	public String L();
	
	public String UNMARRIED();
	public String MARRIED();
	public String CIVIL_UNION();
	public String DIVORCED();
	public String SEPARATED();
	public String WIDOWED();
	
	//From SPEC Start
	public String Simpat();
	public String Statist();
	public String Material();		
	
	public String rich_text_item();
	public String table_item();
	//From SPEC End
	
	

    public String notSetHostAddressException();
    public String connectHostAddressException();
    public String httpClientException();
    public String serializingException();
    
    public String patientNotExistException();
    
    public String syncStandardizedPatientException();
    public String syncNationalityException();
    public String syncProfessionException();
    public String syncDescriptionException();
    public String syncBankaccountException();
    public String syncAnamnesisFormException();
    public String syncAnamnesisCheckValuesException();
    
    
    public String exportSuccessful();
    public String serverReturndError();
    public String importSussessful();
    
 // G: SPEC START =
 	public String HS();
 	public String FS();
 	// G: SPEC END =
 	
 	//Osce Screen Status
 	
 		public String NEW_STATUS();
 		
 		public String BluePrint_Status();
 		public String Genrated_Status();
 		public String Fixed_Status();
 		public String Closed_Status();
 		
 // 5C: SPEC START =
 		
 		public String NORMAL();
 		public String BREAK();
 		public String ANAMNESIS_THERAPY();
 		public String PREPARATION();
 		
 // 5C: SPEC END
    public String err_DialogBoxTitle();
	public String err_DialogBoxCloseButton();
}
