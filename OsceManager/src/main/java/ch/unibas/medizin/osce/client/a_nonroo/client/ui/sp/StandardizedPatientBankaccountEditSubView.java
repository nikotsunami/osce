package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import ch.unibas.medizin.osce.client.managed.request.BankaccountProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.user.client.ui.IsWidget;

public interface StandardizedPatientBankaccountEditSubView extends IsWidget {
    void setDelegate(Delegate delegate);

    public interface Presenter {
        void goTo(Place place);
    }
    
	interface Delegate {
		void cancelClicked();
		void saveClicked();
	}

	RequestFactoryEditorDriver<BankaccountProxy, StandardizedPatientBankaccountEditSubViewImpl> createEditorDriver();
}
