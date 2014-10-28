package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import ch.unibas.medizin.osce.client.managed.request.ChecklistItemProxy;
import ch.unibas.medizin.osce.client.style.widgets.ScrolledTabLayoutPanel;
import ch.unibas.medizin.osce.shared.ItemType;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface RoleDetailsChecklistTabSubView extends IsWidget {

	interface Delegate {

		void addiOSCECheckListTopicClicked(ItemType itemType, String topicName, String topicDescription, RoleDetailsChecklistTabSubViewImpl roleDetailsChecklistTabSubViewImpl, ChecklistItemProxy tabProxy);

		void deleteChecklistTabClicked(ScrolledTabLayoutPanel checklistTabPanel, ChecklistItemProxy checklistItemProxy);

		void updateChecklistTab(ChecklistItemProxy checklistItemProxy, String name, String description, RoleDetailsChecklistTabSubViewImpl roleDetailsChecklistTabSubViewImpl, ScrolledTabLayoutPanel checklistTabPanel);
		
	}
	
	public void setDelegate(Delegate delegate);

	public AbsolutePanel getCheckListAP();

	public VerticalPanel getContainerVerticalPanel();

	public void setTabPanel(ScrolledTabLayoutPanel checklistTabPanel);

	public void setChecklistItemProxy(ChecklistItemProxy checklistItemProxy);

	public ChecklistItemProxy getChecklistItemProxy();
}
