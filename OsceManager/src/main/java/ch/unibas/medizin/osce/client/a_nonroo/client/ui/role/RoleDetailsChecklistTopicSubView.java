package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import ch.unibas.medizin.osce.client.managed.request.ChecklistItemProxy;
import ch.unibas.medizin.osce.shared.ItemType;
import ch.unibas.medizin.osce.shared.OptionType;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface RoleDetailsChecklistTopicSubView extends IsWidget {
	
	interface Delegate {

		void addiOsceChecklistQuestionClicked(ItemType itemType, String name, String description, Boolean isOverallQuestion, OptionType optionType, RoleDetailsChecklistTopicSubViewImpl checklistTopicSubViewImpl, ChecklistItemProxy checklistTopicProxy);

		void deleteTopicClicked(RoleDetailsChecklistTopicSubViewImpl roleDetailsChecklistTopicSubViewImpl, ChecklistItemProxy checklistItemProxy);

		void updateChecklistTopic(ItemType itemType, String name, String description, RoleDetailsChecklistTopicSubViewImpl roleDetailsChecklistTopicSubViewImpl, ChecklistItemProxy checklistItemProxy);
		
	}
	
	public void setDelegate(Delegate delegate);
	
	public Label getCheckListTopicLbl();
	
	public Label getDescriptionLbl();
	
	public VerticalPanel getContainerVerticalPanel();

	public ChecklistItemProxy getChecklistItemProxy();

	public void setChecklistItemProxy(ChecklistItemProxy checklistItemProxy);

}
