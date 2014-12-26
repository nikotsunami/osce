package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import ch.unibas.medizin.osce.client.managed.request.ChecklistCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistItemProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistOptionProxy;
import ch.unibas.medizin.osce.shared.ItemType;
import ch.unibas.medizin.osce.shared.OptionType;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;

public interface RoleDetailsChecklistItemSubView extends IsWidget {

	interface Delegate {

		void deleteChecklistQuestionClicked(RoleDetailsChecklistItemSubViewImpl roleDetailsChecklistItemSubViewImpl, ChecklistItemProxy checklistItemProxy);

		void updateChecklistQuestion(ItemType itemType, String name, String description, Boolean isOverallQuestion, OptionType optionType, RoleDetailsChecklistItemSubViewImpl roleDetailsChecklistItemSubViewImpl, ChecklistItemProxy checklistQuestionProxy);

		void saveCriteriaClicked(String name, ChecklistItemProxy checklistItemProxy, String description, RoleDetailsChecklistItemSubViewImpl roleDetailsChecklistItemSubViewImpl);

		void saveOptionClicked(String name, String description, String value, String criteriaCount, ChecklistItemProxy checklistItemProxy, RoleDetailsChecklistItemSubViewImpl roleDetailsChecklistItemSubViewImpl);

		void deleteCriteriaClicked(ChecklistCriteriaProxy criteriaProxy, RoleDetailsChecklistItemSubViewImpl roleDetailsChecklistItemSubViewImpl);

		void deleteOptionClicked(ChecklistOptionProxy optionProxy, RoleDetailsChecklistItemSubViewImpl roleDetailsChecklistItemSubViewImpl);

		void updateCriteriaClicked(String name, String description, ChecklistItemProxy checklistItemProxy, ChecklistCriteriaProxy criteriaProxy, RoleDetailsChecklistItemSubViewImpl roleDetailsChecklistItemSubViewImpl);

		void updateOptionClicked(String name, String description, String value, String criteriaCount, ChecklistItemProxy checklistItemProxy, ChecklistOptionProxy optionProxy, RoleDetailsChecklistItemSubViewImpl roleDetailsChecklistItemSubViewImpl);

		void upArrowChecklistQuestionClicked(RoleDetailsChecklistItemSubViewImpl roleDetailsChecklistItemSubViewImpl,ChecklistItemProxy checklistItemProxy);

		void downArrowChecklistQuestionClicked(RoleDetailsChecklistItemSubViewImpl roleDetailsChecklistItemSubViewImpl,ChecklistItemProxy checklistItemProxy);
		
	}
	
	public void setDelegate(Delegate delegate);
	
	/*public FlowPanel getOptionContainerPanel();
	
	public FlowPanel getCriteriaContainerPanel();*/
	
	public Label getQuestionNameLbl();
	
	public Label getQuestionDescLbl();

	public ChecklistItemProxy getChecklistItemProxy();

	public void setChecklistItemProxy(ChecklistItemProxy checklistItemProxy);
	
	public CellTable<ChecklistOptionProxy> getOptionTable();
	
	public CellTable<ChecklistCriteriaProxy> getCriteriaTable();
	
	public void setQuestionNameDescription(String name, String description);
}
