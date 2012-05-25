package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;



import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTableItemProxy;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;

public interface RoleBaseTableItemView extends IsWidget {
	public interface Presenter {
		void goTo(Place place);
	}

	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		// todo
//		void newClicked(String itemName, int item_defination);

		void addRoleBaseSubItem(RoleBaseItemProxy roleBaseItemProxy,CellTable<RoleTableItemProxy> table);

		void  pencliButtonclickEvent(RoleBaseItemProxy roleBaseItemProxy);

		void roleTableItemEditButtonClicked(RoleTableItemProxy roleTableItem,Long id,CellTable<RoleTableItemProxy> table);

		void roleTableItemDeleteClicked(RoleTableItemProxy roleTableItem,Long id,CellTable<RoleTableItemProxy> roleTableItemProxyTable);

		void deleteButtonClickEvent(RoleBaseItemProxy roleBasedItemProxy);

		void roleTableItemMoveUp(RoleTableItemProxy roleTableItem,Long id,CellTable<RoleTableItemProxy> toleTableItem);

		void roleTableItemMoveDown(RoleTableItemProxy roleTableItem,Long id,CellTable<RoleTableItemProxy> toleTableItem);

		void baseItemUpButtonClicked(RoleBaseItemProxy roleBasedItemProxy);
		
		void baseItemDownButtonClicked(RoleBaseItemProxy roleBasedItemProxy);

		void baseItemAccessButtonClicked(ClickEvent event,
				RoleBaseItemProxy roleBasedItemProxy,HorizontalPanel accessDataPanel);
		
	}

	void setDelegate(Delegate delegate);

	Delegate getDelegate();

	public void setBaseItemMidifiedValue(String value);

	void setPresenter(Presenter systemStartActivity);
	
	public CellTable<RoleTableItemProxy> getTable();

	RoleBaseTableAccessViewImpl getRoleBaseTableAccessViewImpl();

	HorizontalPanel getAccessDataPanel();
	
	//public Label getLabel();

}
