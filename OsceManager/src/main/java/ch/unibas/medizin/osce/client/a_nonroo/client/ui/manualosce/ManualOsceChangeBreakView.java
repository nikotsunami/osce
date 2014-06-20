package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;

public interface ManualOsceChangeBreakView extends IsWidget {

	interface Delegate{

		void addRotationClicked(OsceSequenceProxy osceSequenceProxy);

		void removeRotationClicked(OsceSequenceProxy osceSequenceProxy);

		void breakSoonerClicked(OsceSequenceProxy osceSequenceProxy);

		void breakLaterClicked(OsceSequenceProxy osceSequenceProxy);
		
	}
	
	public void setDelegate(Delegate delegate);
	
	public HorizontalPanel getSequencePanel();
	
	public void setSequencePanel(HorizontalPanel sequencePanel);
	
	public IconButton getEditSequence();
	
	public Label getNameOfSequence();
	
	public IconButton getBreakSooner();
	
	public IconButton getBreakLater();
	
	public IconButton getAddRotation();
	
	public IconButton getRemoveRotation();
}
