package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import com.google.gwt.user.client.ui.IsWidget;

public interface QRPopupView extends IsWidget {

	interface Delegate {

		void exportChecklistQRCodePopUp(Long checklistId);
		void exportSettingsQRCodePopUp(Long osceSettingsId);
	}
	public void setDelegate(Delegate delegate);
}
