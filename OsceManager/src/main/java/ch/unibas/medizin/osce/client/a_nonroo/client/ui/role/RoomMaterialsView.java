package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import ch.unibas.medizin.osce.client.managed.request.MaterialListProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;

public interface RoomMaterialsView extends IsWidget {

	public interface Presenter {
		void goTo(Place place);
	}

	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void showSubviewClicked();

		// void deleteClicked(SpecialisationProxy specialization);
		public void performSearch(String q);

		void editClicked();

		void deleteClicked(MaterialListProxy materialListProxy);
	}

	void setDelegate(Delegate delegate);

	SimplePanel getDetailsPanel();

	void setPresenter(Presenter presenter);

	CellTable<MaterialListProxy> getTable();

	String[] getPaths();

	public String getQuery();

	public void updateSearch();
}
