package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.user.client.ui.IsWidget;

public interface AnamnesisCheckEditView extends IsWidget {
	void setDelegate(Delegate delegate);
	void setEditTitle(boolean edit);

	public interface Presenter {
		void goTo(Place place);
	}

	interface Delegate {
		void cancelClicked();
		void saveClicked();
	}

	RequestFactoryEditorDriver<AnamnesisCheckProxy, AnamnesisCheckEditViewImpl> createEditorDriver();
	void setPresenter(Presenter presenter);

}
