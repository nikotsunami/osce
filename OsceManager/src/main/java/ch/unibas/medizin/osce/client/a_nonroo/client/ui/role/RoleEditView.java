package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.Collection;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.BankaccountProxy;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.managed.request.ProfessionProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;

public interface RoleEditView extends IsWidget {
    void setDelegate(Delegate delegate);
    void setEditTitle(boolean edit);


    public interface Presenter {
        void goTo(Place place);
    }
    
	interface Delegate {
		void cancelClicked();
		void saveClicked();
		
	}

	RequestFactoryEditorDriver<StandardizedRoleProxy, RoleEditViewImpl> createEditorDriver();
	
	void setPresenter(Presenter doctorEditActivity);
	
	 public StandardizedRoleProxy getStandardizedRoleProxy();
	 
	 public void setStandardizedRoleProxy(StandardizedRoleProxy standardizedRoleProxy) ;
	 
	 public TabPanel getRoleDetailPanel();
	 
	 public void setRoleDetailPanel(TabPanel roleDetailPanel);
	 
	 public int getMajorMinorChange(); 
	
	
}
