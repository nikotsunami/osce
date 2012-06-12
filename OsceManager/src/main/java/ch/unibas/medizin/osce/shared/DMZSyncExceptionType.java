package ch.unibas.medizin.osce.shared;

public class DMZSyncExceptionType {
	
	public static final String HOST_ADDRESS_EXCEPTION = "notSetHostAddressException";
	public static final String CONNECT_HOST_ADDRESS_EXCEPTION = "connectHostAddressException";
	public static final String HTTP_EXCEPTION = "httpClientException";
	public static final String SERIALIZING_EXCEPTION = "serializingException";
	
	public static final String PATIENT_EXIST_EXCEPTION = "patientNotExistException";
	
	public static final String SYNC_PATIENT_EXCEPTION = "syncStandardizedPatientException"; 
	public static final String SYNC_NATIONALITY_EXCEPTION = "syncNationalityException"; 
	public static final String SYNC_PROFESSION_EXCEPTION = "syncProfessionException"; 
	public static final String SYNC_DESCRIPTION_EXCEPTION = "syncDescriptionException"; 
	public static final String SYNC_BANKACCOUNT_EXCEPTION = "syncBankaccountException"; 
	public static final String SYNC_ANAMNESISFROM_EXCEPTION = "syncAnamnesisFormException"; 
	public static final String SYNC_ANAMNESISCHECKVALUES_EXCEPTION = "syncAnamnesisCheckValuesException"; 
}
