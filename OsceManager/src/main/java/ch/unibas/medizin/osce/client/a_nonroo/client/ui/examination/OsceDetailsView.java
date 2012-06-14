package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import java.util.Date;
import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AdministratorProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.request.TaskProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;

public interface OsceDetailsView extends IsWidget{
	
	
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void osceEditClicked();
		void osceGenerateClicked();
		
		void osceDeleteClicked();
		void deleteClicked(TaskProxy task);
		void editForDone(TaskProxy task);

		void saveClicked(Boolean isedit, String innerText,
				AdministratorProxy value, Date value2,OsceProxy osceproxy,TaskProxy task);
		
		
		
	}
	
	CellTable<TaskProxy> getTable();
	String[] getPaths();
	
    public void setValue(OsceProxy proxy); 
  
    void setDelegate(Delegate delegate);
    
    void setPresenter(Presenter osceActivity);
	void setAdministratorValue(List<AdministratorProxy> emptyList);
}
