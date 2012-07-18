package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;



import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicProxy;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface RoleDetailsChecklistSubViewChecklistTopicItemView extends IsWidget{

	void setDelegate(Delegate delegate);
	
	interface Delegate {

		public void saveCheckListQuestion(String question,String instruction,RoleDetailsChecklistSubViewChecklistTopicItemViewImpl view);
		public void deleteCheckListTopic(ChecklistTopicProxy proxy,RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView);
		public void updateCheckListTopic(ChecklistTopicProxy proxy, String topicname, String description, RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView);
		void setRoleListBoxValue(ImportTopicPopupView popupView);
		void topicMoveDown(ChecklistTopicProxy proxy,RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView);
		void topicMoveUp(ChecklistTopicProxy proxy,RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView);
	}
	
	public ChecklistTopicProxy getProxy();
	
	public void setProxy(ChecklistTopicProxy proxy);
	// Highlight onViolation
	Map getChecklistQuestionMap();
	Map getChecklistTopicMap();
	// E Highlight onViolation

	
	
	
}
