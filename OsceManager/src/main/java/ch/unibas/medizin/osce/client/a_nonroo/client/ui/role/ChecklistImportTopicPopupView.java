package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import com.google.gwt.user.client.ui.IsWidget;

public interface ChecklistImportTopicPopupView  extends IsWidget{
	
	interface Delegate{
		
		void standardizedRoleSuggectionBoxValueSelectedTopicPopUp(Long id,ChecklistImportTopicPopupViewImpl checklistImportTopicPopup);
	}

	void setDelegate(Delegate delegate);
	ChecklistImportTopicPopupViewImpl getView();
	public Long getTopicId();
	public Long getStandardizedRoleId(); 
}
