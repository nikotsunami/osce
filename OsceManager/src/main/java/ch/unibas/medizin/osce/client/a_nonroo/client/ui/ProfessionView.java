package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.ProfessionProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;

public interface ProfessionView  extends IsWidget{

	public interface Presenter {
		void goTo(Place place);
	}
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void newClicked(String string);
		
		void deleteClicked(ProfessionProxy prof);
		
		void performSearch(String q);
		
		void updateClicked(ProfessionProxy proxy, String value);
	}

	CellTable<ProfessionProxy> getTable();
	String[] getPaths();

	void setDelegate(Delegate delegate);

//	SimplePanel getDetailsPanel();
	void setPresenter(Presenter systemStartActivity);
	// Highlight onViolation
	ProfessionView getProfessionView();
	EditPopView getEditPopView();
	Map getProfessionMap();
	Map getNewLanguageMap();
	
	// E Highlight onViolation
}
