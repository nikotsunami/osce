package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import ch.unibas.medizin.osce.client.managed.request.ChecklistItemProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.ItemType;
import ch.unibas.medizin.osce.shared.OptionType;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface RoleDetailsChecklistTopicSubView extends IsWidget {
	
	interface Delegate {

		void addiOsceChecklistQuestionClicked(ItemType itemType, String name, String description, Boolean isOverallQuestion, OptionType optionType, RoleDetailsChecklistTopicSubViewImpl checklistTopicSubViewImpl, ChecklistItemProxy checklistTopicProxy);

		void deleteTopicClicked(RoleDetailsChecklistTopicSubViewImpl roleDetailsChecklistTopicSubViewImpl, ChecklistItemProxy checklistItemProxy);

		void updateChecklistTopic(ItemType itemType, String name, String description, RoleDetailsChecklistTopicSubViewImpl roleDetailsChecklistTopicSubViewImpl, ChecklistItemProxy checklistItemProxy, String weight);

		void createImportQuestionPopUp(IconButton importSectionButton,Long topicId);

		void upArrowClicked(RoleDetailsChecklistTopicSubViewImpl roleDetailsChecklistTopicSubViewImpl,ChecklistItemProxy checklistItemProxy);

		void downArrowClicked(RoleDetailsChecklistTopicSubViewImpl roleDetailsChecklistTopicSubViewImpl,ChecklistItemProxy checklistItemProxy);
	}
	
	public void setDelegate(Delegate delegate);
	
	public Label getCheckListTopicLbl();
	
	public Label getDescriptionLbl();
	
	public VerticalPanel getContainerVerticalPanel();

	public ChecklistItemProxy getChecklistItemProxy();

	public void setChecklistItemProxy(ChecklistItemProxy checklistItemProxy);

	public DisclosurePanel getCheckListTopicDisclosurePanel();

	public Image getArrow();

	public PickupDragController getPickupDragController();

	public VerticalPanelDropController getVerticalPanelDropController();

}
