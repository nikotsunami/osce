package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;

import com.google.gwt.place.shared.Place;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.gwt.user.client.ui.IsWidget;

public interface AnamnesisFormEditView extends IsWidget {
    void setDelegate(Delegate delegate);
    void setEditTitle(boolean edit);

    public interface Presenter {
        void goTo(Place place);
    }
    
	interface Delegate {
		void cancelClicked();

		void saveClicked();
		
	}

	RequestFactoryEditorDriver<AnamnesisFormProxy, AnamnesisFormEditViewImpl> createEditorDriver();
	void setPresenter(Presenter doctorEditActivity);

}
