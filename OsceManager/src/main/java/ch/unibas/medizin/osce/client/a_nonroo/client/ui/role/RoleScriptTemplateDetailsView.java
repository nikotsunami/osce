package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.TopicsAndSpecDetailsView.Delegate;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.TopicsAndSpecDetailsView.Presenter;
import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTemplateProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.shared.ItemDefination;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface RoleScriptTemplateDetailsView extends IsWidget{
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		//todo
	void newClicked(String itemName,ItemDefination itemDefination);

		void restoreButtonClicked(RoleBaseItemProxy roleBaseItem);
	}
	
	 
    void setDelegate(Delegate delegate);
    Delegate getDelegate();
    public void setValue(RoleTemplateProxy proxy);
    public void addRoleBaseWidget(RoleBaseItemProxy rolebaseItem);
    void setPresenter(Presenter systemStartActivity);
    public RoleBaseTableItemViewImpl getRoleBaseTableItemViewImpl();
    public VerticalPanel getTableItem();
	//RoleBaseTableItemViewImpl getRoleBaseTableItemViewImpl();
    CellTable<RoleBaseItemProxy> getTable();
    
}
