package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.managed.request.LogEntryProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author dk
 *
 */
public interface LogView extends IsWidget{

	public interface Presenter {
		void goTo(Place place);
	}
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void performSearch(String q);
	}

	CellTable<LogEntryProxy> getTable();
	String[] getPaths();

	void setDelegate(Delegate delegate);

	//	SimplePanel getDetailsPanel();
	void setPresenter(Presenter systemStartActivity);
}
