package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;


import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicProxy;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;

public interface RoleDetailsChecklistSubViewChecklistQuestionItemView extends IsWidget{

	interface Delegate {
		void saveCriteria(String criteria,RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl view);
		void saveOption(String option,String value,String description, RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl view);
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
	  public Map getChecklistQuestionMap();
	  public Map getChecklistOptionMap();
	  public Map getChecklistCriteriaMap();
	  public ChecklistQuestionProxy getProxy();
	  
	  public void setProxy(ChecklistQuestionProxy proxy);
	
	public FlowPanel getRoleQueFP();
	public  HorizontalPanel getRoleQueHP();
	public  AbsolutePanel getRoleQueAP();
		
	public PickupDragController getDragController();
	
	public int getChecklistQuestionCount();
	
	public void setChecklistQuestionCount(int checklistQuestionCount);
		
}
