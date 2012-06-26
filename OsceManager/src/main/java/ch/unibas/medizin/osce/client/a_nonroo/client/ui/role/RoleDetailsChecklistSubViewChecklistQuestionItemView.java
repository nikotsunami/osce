package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;


import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicProxy;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;

public interface RoleDetailsChecklistSubViewChecklistQuestionItemView extends IsWidget{

	interface Delegate {
		void saveCriteria(String criteria,RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl view);
		void saveOption(String option,String value,RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl view);
		void editOption(String question, String instruction, RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView);
		void deleteQuestion(RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView);
		void questionMoveUp(RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView,ChecklistTopicProxy topicProxy,RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView);
		void questionMoveDown(RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView,ChecklistTopicProxy topicProxy,RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView);
		
	}
	
	  void setDelegate(Delegate delegate);
	  void setChecklistTopicProxy(ChecklistTopicProxy proxy);
	  void setRoleDetailsChecklistSubViewChecklistTopicItemView(RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView);
	  public Label getQuestionItemLbl() ;
	  
	  public void setQuestionItemLbl(Label questionItemLbl);
	  
	  public Label getQuestionInstruction();

	  public void setQuestionInstruction(Label questionInstruction);
	  
	  public ChecklistQuestionProxy getProxy();
	  
	  public void setProxy(ChecklistQuestionProxy proxy);
}
