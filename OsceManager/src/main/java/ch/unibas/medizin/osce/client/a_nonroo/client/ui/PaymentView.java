package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.MultiSelectionModel;

public interface PaymentView extends IsWidget {

	 public interface Presenter {
	 
	 }
	 
	 void setDelegate(Delegate delegate);

	 void setPresenter(Presenter systemStartActivity);

	 interface Delegate {
		 void printButtonClicked();
		 void exportButtonClicked();
		 
	 }	    
	 
	 public CellTable<StandardizedPatientProxy> getTable();
	 
	 public List<String> getPaths();
	 
	 public MultiSelectionModel<StandardizedPatientProxy> getMultiselectionModel();
}
