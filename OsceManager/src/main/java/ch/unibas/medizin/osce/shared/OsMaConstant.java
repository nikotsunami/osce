package ch.unibas.medizin.osce.shared;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class OsMaConstant {
//	public static final SafeHtml DELETE_ICON = new SafeHtmlBuilder().appendHtmlConstant("<img src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAwAAAAMCAYAAABWdVznAAAAAXNSR0IArs4c6QAAAAZiS0dEAP8A/wD/oL2nkwAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9sLFw4ZOlmhRdsAAAAZdEVYdENvbW1lbnQAQ3JlYXRlZCB3aXRoIEdJTVBXgQ4XAAAAzElEQVQoz42RPQ5BURCFv/vYgLeEl7sIC6ChUCh0Il5LbRsSosOLTiHR0CgswQYmt9E/sQE0Q4b4O8lNzpk5M/fkXofCJ74GtIA2z1gASwmyBXBq7gEjvqMvQcZON2/4D/UIaJpCKkHcXShPTb/pfOKvLwZ84ruqZ6ofnqcBc8vMDE5tM3qT8/KBA1C0wkTqqM6AzKaIgIn5i67SuR5bA5g4n/gKsPvzWauF/JSHuBQfgcYPcypBVgWA/JQf4lK8B85A+cU4BAYSZA1wAxNORj272z0sAAAAAElFTkSuQmCC\" />").toSafeHtml();
	public static final SafeHtml DELETE_ICON = new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-trash\"></span>").toSafeHtml();
	public static final SafeHtml YES_ICON = new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-circle-check\"></span>").toSafeHtml();
	public static final SafeHtml NO_ICON = new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-circle-close\"></span>").toSafeHtml();
	public static final SafeHtml PLUS_ICON = new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-plusthick\"></span>").toSafeHtml();
	public static final SafeHtml EDIT_ICON = new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-pencil\"></span>").toSafeHtml();
	public static final SafeHtml UP_ICON = new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-triangle-1-n\"></span>").toSafeHtml();
	public static final SafeHtml DOWN_ICON = new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-triangle-1-s\"></span>").toSafeHtml();
	public static final SafeHtml CHECK_ICON = new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-check\"></span>").toSafeHtml();
	public static final SafeHtml UNCHECK_ICON = new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-close\"></span>").toSafeHtml();
	public static final SafeHtml SEEK_FIRST_ICON = new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-seek-next\"></span>").toSafeHtml();
	public static final SafeHtml DOWN_ICONFORCELLTABLE = new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon-triangle-1-s1\"></span>").toSafeHtml();
	public static final SafeHtml PRINT_ICON = new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-print\"></span>").toSafeHtml();
	
	public static final SafeHtml DOWNLOAD_ICON = new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-arrowthickstop-1-s\"></span>").toSafeHtml();
	
	public static int TABLE_PAGE_SIZE = 15;
	public static final int TABLE_JUMP_SIZE = 30;
	public static final int SPLIT_PANEL_MINWIDTH = 520;
	public static final int ENTRY_TIMEOUT_MS = 700;
	
	
	// Constants moved from I18N-properties files.
	public static final int WIDTH_SIZE = 1225;
	public static final int WIDTH_MIN = 705;
	public static final int ANIMATION_TIME = 3000;
	
	//By Spec[start
//Feature : 154
//	public static String FILENAME = "StandardizedPatientList.csv";
//	public static String FILE_NAME_PDF_FORMAT = "StandardizedPatientDetails.pdf";
//	public static String ROLE_FILE_NAME_PDF_FORMAT = "StandardizedRoleDetails.pdf";
	public static String BLANK_FIELD_MESSAGE = "Please enter details for";
//Feature : 154
	
	// For Osce Status of Tabpanel
	public static String NEW_STATUS="Osce_New_Status";
	public static String BluePrint_Status="Osce_BluePrint_Status";
	public static String Genrated_Status="Osce_Genrated_Status";
	public static String Fixed_Status="Osce_Fixed_Status";
	public static String Closed_Status="Osce_Closed_Status";
	
	//By Spec]End
	
	// Module 3 {
	public static int OSCECOOKIEDAY = 0;
	public static int OSCEDAYTIMESCHEDULE =600000;
	
	// Module 3 }
	public static final SafeHtml FLAG_ICON = new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-flag\"></span>").toSafeHtml();
	public static final SafeHtml COMMENT_ICON = new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-comment\"></span>").toSafeHtml();
	public static final SafeHtml SEARCH_ICON = new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-search\"></span>").toSafeHtml();
	public static final SafeHtml WRENCH_ICON = new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-wrench\"></span>").toSafeHtml();
	//By Spec]End
	
	public static final SafeHtml COLOR_PICKER_ICON = new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-triangle-1-s\"></span>").toSafeHtml();
	
	public static int RANDOM_STRING_LENGTH=10;
	
}
