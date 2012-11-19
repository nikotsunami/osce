package ch.unibas.medizin.osce.client.a_nonroo.client.ui.StatisticalEvaluation;

import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public interface StatisticalEvaluationDetailPostView {
	 public interface Presenter {
	        void goTo(Place place);
	    }
		/**
		 * Implemented by the owner of the view.
		 */
		interface Delegate 
		{
			
		}

		public OscePostProxy getOscePostProxy();
	    
	    void setDelegate(Delegate delegate);
	    
	    public void setOscePostProxy(OscePostProxy oscePostProxy);
	    
	    public Label getPostNameLbl();
	    
	    public Widget asWidget();
	    
	    public HorizontalPanel getPostNameHP();
}
