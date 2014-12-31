package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;

import com.google.gwt.place.shared.Place;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
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
		 void save();
		
		 public void saveMajor();
		
	}

	RequestFactoryEditorDriver<StandardizedRoleProxy, RoleEditViewImpl> createEditorDriver();
	
	
	
	void setPresenter(Presenter doctorEditActivity);
	
	 public StandardizedRoleProxy getStandardizedRoleProxy();
	 
	 public void setStandardizedRoleProxy(StandardizedRoleProxy standardizedRoleProxy) ;
	 
	 public TabPanel getRoleDetailPanel();
	 
	 public void setRoleDetailPanel(TabPanel roleDetailPanel);
	 
	 public int getMajorMinorChange(); 
	 
	 public StandardizedRoleProxy getProxy() ;
	 
	 public void setProxy(StandardizedRoleProxy proxy);
	 
	 void setRoleTopicListBoxValues(List<RoleTopicProxy> values);
	 
	 public SimplePanel getRoleEditCheckListPanel();//spec
	//public RoleEditCheckListSubViewImpl getRoleEditCheckListSubViewImpl();
	//public void setRoleEditCheckListSubViewImpl(RoleEditCheckListSubViewImpl roleEditCheckListSubViewImpl);
	
	// Highlight onViolation
	 public Map getStandardizedRoleMap();
	// E Highlight onViolation
	 
	
	
	
	
	
}
