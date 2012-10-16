package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;



import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTableItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTableItemValueProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Widget;

public interface RoleBaseTableItemValueView extends IsWidget {
	public interface Presenter {
		void goTo(Place place);
	}

	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {

		void addRoleScriptTableItemValue(RoleTableItemValueProxy roleTableItem,
				Long id, CellTable<RoleTableItemValueProxy> table,int left,int top);
		// todo
//		void newClicked(String itemName, int item_defination);

		void addRichTextAreaValue(RoleBaseItemProxy roleBaseItemProxy, RichTextArea description,int i, Map<String, Widget> roleSubItemValueMap,DivElement descriptionValue,IconButton saveButton);

		
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
