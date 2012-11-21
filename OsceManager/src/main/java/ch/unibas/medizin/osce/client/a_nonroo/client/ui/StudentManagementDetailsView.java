package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentProxy;
import ch.unibas.medizin.osce.client.style.resources.AdvanceCellTable;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;

public interface StudentManagementDetailsView extends IsWidget {

	 public interface Presenter {
	 
	 }
	 
	 void setDelegate(Delegate delegate);

	 void setPresenter(Presenter systemStartActivity);

	 interface Delegate {
		 public void printCheckList(OsceProxy osceProxy,StudentProxy studentProxy);
		
	 }

	void setStudentProxy(StudentProxy studentProxy);

	CellTable<OsceProxy> getTable();

	List<String> getPaths();

	
}


