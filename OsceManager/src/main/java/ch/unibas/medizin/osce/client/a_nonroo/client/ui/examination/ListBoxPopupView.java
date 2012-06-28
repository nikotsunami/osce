package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import com.google.gwt.requestfactory.shared.EntityProxy;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ValueListBox;

public interface ListBoxPopupView extends IsWidget{
	
	interface Delegate {
		public void saveStandardizedRole(ListBoxPopupView view);
		
	}
	
	void setDelegate(Delegate delegate);
	
	public Button getOkBtn();
	
	public ValueListBox<EntityProxy> getListBox();
	
	public EntityProxy getProxy();
	
	public void setProxy(EntityProxy proxy);
	
	public OscePostSubView getOscePostSubView();
	
	public void setOscePostSubView(OscePostSubView oscePostSubView);
	
	
}
