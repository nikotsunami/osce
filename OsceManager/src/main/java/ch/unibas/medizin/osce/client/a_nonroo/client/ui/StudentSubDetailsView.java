package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.managed.request.StudentOscesProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.IsWidget;

public interface StudentSubDetailsView extends IsWidget {
	
	 public interface Presenter {
	        void goTo(Place place);
	    }
	 
	 
		/**
		 * Implemented by the owner of the view.
		 */
		interface Delegate {
		

			void deleteClicked(StudentOscesProxy studentOscesProxy);
			void performSearch(String q);

			Boolean onRender(StudentOscesProxy studentOscesProxy);
			
			void importClicked();
		}

	    CellTable<StudentOscesProxy> getTable();
	    String[] getPaths();
	    
	    void setDelegate(Delegate delegate);
	    
	    public Hidden getHidden();

	    void setPresenter(Presenter systemStartActivity);
	}


