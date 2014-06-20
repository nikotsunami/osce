package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import ch.unibas.medizin.osce.client.managed.request.OsceProxy;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface ManualOsceDetailsView extends IsWidget {

	interface Delegate{
		
	}
	
	public void setDelegate(Delegate delegate);
	
	public VerticalPanel getMainVerticalPanel();
	
	public ManualOsceSubViewImpl getManualOsceSubViewImpl();
	
	public OsceProxy getOsceProxy();
	
	public void setOsceProxy(OsceProxy osceProxy);
}
