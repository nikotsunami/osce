package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.MaterialListProxy;
import ch.unibas.medizin.osce.shared.MaterialType;
import ch.unibas.medizin.osce.shared.PriceType;

import com.google.gwt.place.shared.Place;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.gwt.user.client.ui.IsWidget;

public interface RoomMaterialsEditView extends IsWidget {
	void setDelegate(Delegate delegate);

	void setEditTitle(boolean edit);

	public interface Presenter {
		void goTo(Place place);
	}

	interface Delegate {

		void cancelClicked();

		void saveClicked();

	}

	RequestFactoryEditorDriver<MaterialListProxy, RoomMaterialsEditViewImpl> createEditorDriver();

	void setPresenter(Presenter activity);

	// public void setRoomDetails(MaterialListProxy materialListProxy);

	abstract String getName();

	abstract MaterialType getType();

	abstract Integer getPrice();

	abstract PriceType getPriceType();
	
	// Violation Changes Highlight
		public Map getEditViewMap();
		// E Violation Changes Highlight
}