package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.shared.OsceStatus;

import com.google.gwt.user.client.ui.IsWidget;

public interface ManualOsceEditView extends IsWidget {

	interface Delegate{

		void saveOsceClicked(OsceProxy osceProxy, ManualOsceEditViewImpl manualOsceEditViewImpl);

		void fixedButtonClicked(OsceProxy osceProxy, ManualOsceEditViewImpl manualOsceEditViewImpl);

		void clearAllButtonClicked(OsceProxy osceProxy, ManualOsceEditViewImpl manualOsceEditViewImpl);

		void closeButtonClicked(OsceProxy osceProxy, ManualOsceEditViewImpl manualOsceEditViewImpl);

		void reopenButtonClicked(OsceProxy osceProxy, ManualOsceEditViewImpl manualOsceEditViewImpl);

		void calculateButtonClicked(OsceProxy osceProxy);
		
	}
	
	public void setDelegate(Delegate delegate);
	
	public void setValueInOsceProxy(OsceProxy osceProxy);
	
	public void changeButtonByOsceStatus(OsceStatus osceStatus);
	
	public void disableEnableTextBox(boolean value);
}
