package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import com.google.gwt.user.client.ui.IsWidget;

public interface ChecklistImportQuestionPopupView  extends IsWidget{

	interface Delegate{

		void standardizedRoleSuggectionBoxValueSelectedQuestionPopup(Long role,ChecklistImportQuestionPopupViewImpl checklistImportQuestionPopupViewImpl);
		void topicSuggestionBoxValueSelectedQuestionPopup(Long topicId, ChecklistImportQuestionPopupViewImpl checklistImportQuestionPopup);
	}

	void setDelegate(Delegate delegate);
	ChecklistImportQuestionPopupViewImpl getView();
	Long getRoleId();
	Long getTopicId();
	Long getQuestionId();
}
