package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import ch.unibas.medizin.osce.client.managed.request.MaterialListProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface RoomMaterialsDetailsView extends IsWidget {

	public interface Presenter {
		void goTo(Place place);
	}

	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void editClicked();

		void deleteClicked();
		// void deleteClicked(MaterialListProxy materialList);
	}

	void setDelegate(Delegate delegate);

	void setPresenter(Presenter systemStartActivity);

	public void setValue(MaterialListProxy proxy);
}
