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

	public enum Entity {
		STANDARDIZED_PATIENT,STANDARDIZED_ROLE, STANDARDIZED_PATIENT_EXPORT
	}
}
