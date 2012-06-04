package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;



import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTableItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTableItemValueProxy;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RichTextArea;

public interface RoleBaseTableItemValueView extends IsWidget {
	public interface Presenter {
		void goTo(Place place);
	}

	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {

		void addRoleScriptTableItemValue(RoleTableItemValueProxy roleTableItem,
				Long id, CellTable<RoleTableItemValueProxy> table);
		// todo
//		void newClicked(String itemName, int item_defination);

		void addRichTextAreaValue(RoleBaseItemProxy roleBaseItemProxy, RichTextArea description);

		
//		void addRoleScriptTableItemValue(RoleTableItemProxy roleTableItem,Long id,CellTable<RoleTableItemValueProxy> table);
		
	}

	void setDelegate(Delegate delegate);

	Delegate getDelegate();

	public void setBaseItemMidifiedValue(String value);

	void setPresenter(Presenter systemStartActivity);
	
	public CellTable<RoleTableItemValueProxy> getTable();

	RoleBaseTableAccessViewImpl getRoleBaseTableAccessViewImpl();

	HorizontalPanel getAccessDataPanel();

	String getDescriptionContent();

	void setDescriptionContent(String description);
	
	//public Label getLabel();

}
