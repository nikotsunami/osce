package ch.unibas.medizin.osce.shared;

public final class ResourceDownloadProps {

	public static final String ID = "id";
	public static final String LOCALE = "locale";
	public static final String FILTER = "filter";
	public static final String SELECTED_ROLE_ITEM_ACCESS = "selectedRoleItemAccess";
	public static final String ENTITY = "entity";

	public enum Entity {
		STANDARDIZED_PATIENT,STANDARDIZED_ROLE
	}
}
