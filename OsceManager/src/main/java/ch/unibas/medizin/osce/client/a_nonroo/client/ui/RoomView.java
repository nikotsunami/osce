package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.managed.request.RoomProxy;
import ch.unibas.medizin.osce.shared.TraitTypes;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author dk
 *
 */
public interface RoomView extends IsWidget{

	public interface Presenter {
		void goTo(Place place);

			}
	/**
	 * Implemented by the owner of the view.
	 */
	public interface Delegate {
		void newClicked(String name, double length, double width);

		void editClicked(RoomProxy proxy, String name, double length, double width);
		
		void deleteClicked(RoomProxy room);

		void performSearch(String q);
	}

	CellTable<RoomProxy> getTable();
	String[] getPaths();

	void setDelegate(Delegate delegate);

	//	SimplePanel getDetailsPanel();
	void setPresenter(Presenter systemStartActivity);
}
