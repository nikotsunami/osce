package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;



import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicProxy;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
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
	
	
	DisclosurePanel getRoleDetailsTopicDP();
	public VerticalPanel getqueVP();
	
	AbsolutePanel getDiscloserAP();
	public VerticalPanel getDiscloserVP();
	public VerticalPanel getTopicsdiscloserVP();
	public VerticalPanel getCheckListQuestionVerticalPanel();
	public VerticalPanelDropController getDropTopicsController();
	
	public void setTopicsdiscloserVP(VerticalPanel topicsdiscloserVP);
	
	public void setDiscloserAP(AbsolutePanel discloserAP);
	
	public PickupDragController getDragControllerTopics();
	public PickupDragController getDragController();
	
	public Label getDraglbl();
	
//	public AbsolutePositionDropController getDropControllerAP();
	public AbsolutePanel getTopicAP();
	
	public ChecklistTopicProxy getProxy();
	
	public void setProxy(ChecklistTopicProxy proxy);
	// Highlight onViolation
	Map getChecklistQuestionMap();
	
	Map getChecklistTopicMap();
	// E Highlight onViolation

	
	
	
}
