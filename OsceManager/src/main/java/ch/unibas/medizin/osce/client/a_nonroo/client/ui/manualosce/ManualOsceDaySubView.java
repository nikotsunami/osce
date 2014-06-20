package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import java.util.Date;

import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface ManualOsceDaySubView extends IsWidget {

	interface Delegate{

		void deleteOsceDayClicked(ManualOsceDaySubViewImpl manualOsceDaySubViewImpl, OsceDayProxy osceDayProxy);

		void saveOsceDayClicked(OsceDayProxy osceDayProxy, Date osceDate, Date osceStartTime);
		
	}
	
	public void setDelegate(Delegate delegate);
	
	public OsceDayProxy getOsceDayProxy();
	
	public void setOsceDayProxy(OsceDayProxy osceDayProxy);
	
	public VerticalPanel getSequencePanel();
	
	public Label getDayLabel();
	
	public IconButton getDeleteOsceDay();
	
	public IconButton getSaveOsceDayValue();
}
