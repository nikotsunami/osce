package ch.unibas.medizin.osce.shared;

public final class ResourceDownloadProps {

	public static final String SEPARATOR = ",";
	
	public static final String ID = "1"; 									// id
	public static final String LOCALE = "2"; 								// locale
	public static final String FILTER = "3";								// filter 
	public static final String SELECTED_ROLE_ITEM_ACCESS = "4";				// selectedRoleItemAccess
	public static final String ENTITY = "5";								// entity
	public static final String NAME = "6";									// name
	public static final String SORT_ORDER = "7";							// sortOrder
	public static final String QUICK_SEARCH_TERM = "8";						// quickSearchTerm
	public static final String SEARCH_THROUGH_KEY = "9";					// searchThrough
	public static final String SEARCH_CRITERIA_MASTER_KEY = "a";			// searchCriteriaKey
	public static final String RANGE_START = "b";							// rangeStart
	public static final String RANGE_LENGTH = "c";							// rangeLength

	public static final String SP_LIST = "d";
	public static final String SUMMONING_KEY = "e";
	public static final String INDIVIDUAL_SCHEDULE_KEY = "f";

	public static final String OSCE_ID = "g";
	public static final String COLUMN_NAME = "h";

	public static final String OSCEDAYID = "i";
	public static final String START_TONE = "j";
	public static final String END_TONE = "k";
	public static final String PRE_POST_END_TIME = "l";
	public static final String PRE_POST_END_TONE = "m";
	public static final String PRE_BREAK_END_TIME = "n";
	public static final String PRE_BREAK_END_TONE = "o";
	public static final String PLUS_TIME = "p";
	
	public enum Entity {
		STANDARDIZED_PATIENT,STANDARDIZED_ROLE, STANDARDIZED_PATIENT_EXPORT, SUMMONINGS, INDIVIDUAL_SCHEDULE, STUDENT_MANAGEMENT, CHECKLIST, STANDARDIZED_PATIENT_PAYMENT, PATIENT_IN_SEMESTER_CSV, ALARM_SCHEDULE_SUPERVISOR, STUDENT_MANAGEMENT_MIN_OPTION_VALUE, ROLE_CHECKLIST_EOSCE
	}
}
