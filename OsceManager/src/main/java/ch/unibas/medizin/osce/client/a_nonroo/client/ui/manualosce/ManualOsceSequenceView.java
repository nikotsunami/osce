package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface ManualOsceSequenceView extends IsWidget {
	
	interface Delegate{

		void addOsceSequenceClicked(OsceDayProxy osceDayProxy);

		void changeLunchBreak(String value);
		
	}
	
	public void setDelegate(Delegate delegate);
	
	public ManualOsceLunchBreakViewImpl getManualOscelunchBreakViewImpl();
	
	public HorizontalPanel getFirstOsceSequencePanel();
	
	public HorizontalPanel getSecondOsceSequencePanel();
	
	public VerticalPanel getAddSequencePanel();
	
	public OsceDayProxy getOsceDayProxy();
	
	public void setOsceDayProxy(OsceDayProxy osceDayProxy);
}
