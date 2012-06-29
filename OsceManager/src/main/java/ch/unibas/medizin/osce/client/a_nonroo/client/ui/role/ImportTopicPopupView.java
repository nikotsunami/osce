package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;


import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ValueListBox;

public interface ImportTopicPopupView extends IsWidget{

	interface Delegate {
		
		public void roleListBoxValueSelected(StandardizedRoleProxy proxy,ImportTopicPopupViewImpl view);
		
		public void topicListBoxValueSelected(ChecklistTopicProxy proxy,ImportTopicPopupViewImpl view);
		
		public void importTopic(ChecklistTopicProxy proxy);
		
		public void importQuestion(ChecklistQuestionProxy proxy,RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView);
	}
	
	void setDelegate(Delegate delegate);
	
	public Button getOkBtn() ;
	
	// Issue Role	
	public Button getCancelBtn() ;
	// E: Issue Role
	
	public ValueListBox<StandardizedRoleProxy> getRoleLstBox();
	
	public ValueListBox<ChecklistTopicProxy> getTopicLstBox();
	
	public void setRoleLstBox(ValueListBox<StandardizedRoleProxy> roleLstBox) ;
	
	public void setTopicLstBox(ValueListBox<ChecklistTopicProxy> topicLstBox);
}
