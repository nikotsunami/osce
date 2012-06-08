package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import ch.unibas.medizin.osce.client.managed.request.OsceProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;

public interface OsceView  extends IsWidget{

	public interface Presenter {
		void goTo(Place place);
	}
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void newClicked();

		CellTable<OsceProxy> getTable();
	}

	CellTable<OsceProxy> getTable();
	String[] getPaths();

	void setDelegate(Delegate delegate);

	SimplePanel getDetailsPanel();
	void setPresenter(Presenter osceActivity);
}
