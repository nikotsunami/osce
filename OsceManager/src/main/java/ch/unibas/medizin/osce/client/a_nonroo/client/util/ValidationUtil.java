package ch.unibas.medizin.osce.client.a_nonroo.client.util;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsceChecklistQuestionPojoValueProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistItemProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.shared.OptionType;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.shared.i18n.OsceConstantsWithLookup;
import ch.unibas.medizin.osce.shared.i18n.OsceMessages;

import com.google.gwt.core.client.GWT;

public class ValidationUtil {

	
	private static final OsceConstants constants = GWT.create(OsceConstants.class);
	
	private final OsceMessages osceMessages = GWT.create(OsceMessages.class);
	OsceConstantsWithLookup constantsWithLokkup = GWT.create(OsceConstantsWithLookup.class);
	
	public static final String UL_START = "<ul>";  
	public static final String UL_END = "</ul>";
	public static final String LI_START = "<li>";
	public static final String LI_END = "</li>";
	public static final String DIV_START = "<div style = \"line-height:20px;\">";
	public static final String DIV_END = "</div>";
	
	
	public String getChecklistOptionValidationMessage(List<OsceChecklistQuestionPojoValueProxy> osceChecklistPojoList, int osceListSize) {
		
		final StringBuilder validationText = new StringBuilder();
		validationText.append(constants.checklistOptionWarning() + "<br> <br>");		
		OsceProxy osceProxy = null;
		
		for (OsceChecklistQuestionPojoValueProxy osceChecklistPojo : osceChecklistPojoList) {
			
			List<ChecklistItemProxy> checklistItemProxyList = osceChecklistPojo.getQuestion();
			
			if (checklistItemProxyList.size() > 0) {
				if (osceProxy == null) {
					osceProxy = osceChecklistPojo.getOsce();
					
					String osceName = osceChecklistPojo.getSemester().getSemester().toString()
							+ osceChecklistPojo.getSemester().getCalYear().toString().substring(2, osceChecklistPojo.getSemester().getCalYear().toString().length()) 
							+ "-" + (constantsWithLokkup.getString(osceChecklistPojo.getOsce().getStudyYear().toString()).replace(".", "")); 
					
					if(osceListSize > 1)  
					validationText.append("<b>OSCE  </b>:   " + osceName);
					
					validationText.append(UL_START);
				}
				else if (osceProxy != null && osceProxy.getId().equals(osceChecklistPojo.getOsce().getId()) == false) {
					validationText.append(UL_END);
					
					osceProxy = osceChecklistPojo.getOsce();
					
					String osceName = osceChecklistPojo.getSemester().getSemester().toString()
							+ osceChecklistPojo.getSemester().getCalYear().toString().substring(2, osceChecklistPojo.getSemester().getCalYear().toString().length()) 
							+ "-" + (constantsWithLokkup.getString(osceChecklistPojo.getOsce().getStudyYear().toString()).replace(".", "")); 
					
					if(osceListSize > 1) 
					validationText.append("<b>OSCE  </b>:   " + osceName);
					
					validationText.append(UL_START);
				}
				
				//set checklist
				String checklistTitle = osceChecklistPojo.getCheckList().getTitle();
				
				validationText.append(LI_START);
				//validationText.append("<span class=\"ui-icon ui-icon-bullet\" style=\"float: left; margin-top: 1px; margin-right: 6px;\"></span>");
				validationText.append("<b> " + checklistTitle + " </b>");
				
				validationText.append(UL_START);
				
				for (ChecklistItemProxy checklistItemProxy : checklistItemProxyList) {
					//set question
					
					OptionType optionType =checklistItemProxy.getOptionType();
					String minOptions = "";
					if(OptionType.SLIDER.equals(optionType) || OptionType.POPOVER.equals(optionType) || OptionType.TOGGLE_2.equals(optionType)) {
						minOptions = "2";
					} else {
						minOptions = "3";
					}
					
					String questionText = "";
					validationText.append(DIV_START);
					validationText.append(LI_START);
					
					if(checklistItemProxy.getName().length() > 50) {
				
						String text = checklistItemProxy.getName().substring(0, 50);
						questionText = "<span title = \"" +checklistItemProxy.getName()+ " \"" + ">" + text + "</span>";
						validationText.append(questionText + "...");
						validationText.append("<span style=\" color:red\"> " + osceMessages.checklistOptionRequireMsg(minOptions) + "</span>");
						
					} else {
						questionText = "<span title = \"" +checklistItemProxy.getName()+ " \"" + ">" + checklistItemProxy.getName() + "</span>";
						validationText.append(questionText);
						validationText.append("<span style=\" color:red\"> " + osceMessages.checklistOptionRequireMsg(minOptions) + "</span>");
					}
					
					validationText.append(LI_END);
					validationText.append(DIV_END);
				}
				
				validationText.append(UL_END);
				validationText.append(LI_END);
			}
		}
		validationText.append(UL_END);
		String message = validationText.toString();
		return message;
	}
}
