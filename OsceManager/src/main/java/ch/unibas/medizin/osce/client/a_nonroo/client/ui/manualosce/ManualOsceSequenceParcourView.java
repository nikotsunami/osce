package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface ManualOsceSequenceParcourView extends IsWidget {

	interface Delegate{

		void addParcourClicked(OsceSequenceProxy osceSequenceProxy, Boolean copyWithBreak, boolean copyToAllSequence);

		void deleteOsceSequenceClicked(OsceSequenceProxy osceSequenceProxy, OsceDayProxy osceDayProxy);

		void changeOsceSequenceLabel(String sequenceName, OsceSequenceProxy osceSequenceProxy);		
	}
	
	public void setDelegate(Delegate delegate);
	
	public OsceSequenceProxy getOsceSequenceProxy();
	
	public void setOsceSequenceProxy(OsceSequenceProxy osceSequenceProxy);
	
	public OsceDayProxy getOsceDayProxy();
	
	public void setOsceDayProxy(OsceDayProxy osceDayProxy);
	
	public void addOsceSequenceDeleteButton();
	
	public void removeOsceSequenceDeleteButton();
	
	public VerticalPanel getAddParcourVerticalPanel();
	
	public IconButton getDeleteOsceSeqBtn();
}
