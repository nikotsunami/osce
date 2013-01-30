package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;

public interface ClinicSubView  extends IsWidget{
	
	 public interface Presenter {
	       }

	 interface Delegate {
			
		}
	 
	    CellTable<DoctorProxy> getTable();
	    String[] getPaths();
	    
	    void setDelegate(Delegate delegate);
	    

	    void setPresenter(Presenter systemStartActivity);
	    
	    public Label getHeaderLabel();
}
