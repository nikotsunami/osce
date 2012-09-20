package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;


import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ValueListBox;

public interface ImportTopicPopupView extends IsWidget{

	interface Delegate {
		
		
		public void roleListBoxValueSelected(StandardizedRoleProxy proxy,ImportTopicPopupViewImpl view);
		
		public void specialisationListBoxValueSelected(SpecialisationProxy proxy,ImportTopicPopupViewImpl view);
		
		public void topicListBoxValueSelected(ChecklistTopicProxy proxy,ImportTopicPopupViewImpl view);
		
		// Highlight onViolation
		public void importTopic(ChecklistTopicProxy proxy, ImportTopicPopupViewImpl importTopicPopupView);
		
		public void importQuestion(ChecklistQuestionProxy proxy,RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView, ImportTopicPopupViewImpl importTopicPopupView);
		// E Highlight onViolation

	}
	
	void setDelegate(Delegate delegate);
	
	public Button getOkBtn() ;
	
	// Issue Role	
	public Button getCancelBtn() ;
	// E: Issue Role
	
	//Issue # 122 : Replace pull down with autocomplete.
	//public ValueListBox<StandardizedRoleProxy> getRoleLstBox();
	//public DefaultSuggestBox<StandardizedRoleProxy> getRoleLstBox();
	//Issue # 122 : Replace pull down with autocomplete.
	
	public ValueListBox<ChecklistTopicProxy> getTopicLstBox();
	
	//public void setRoleLstBox(ValueListBox<StandardizedRoleProxy> roleLstBox) ;
	
	public void setTopicLstBox(ValueListBox<ChecklistTopicProxy> topicLstBox);

	//Issue # 122 : Replace pull down with autocomplete.
	public ImportTopicPopupViewImpl getView();
	//Issue # 122 : Replace pull down with autocomplete.

	// Highlight onViolation
	Map getChecklistTopicMap();
	Map getChecklistQuestionMap();
	// E Highlight onViolation

	
}
