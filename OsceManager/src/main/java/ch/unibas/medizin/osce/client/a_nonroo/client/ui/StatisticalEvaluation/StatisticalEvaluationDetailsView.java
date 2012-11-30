package ch.unibas.medizin.osce.client.a_nonroo.client.ui.StatisticalEvaluation;

import ch.unibas.medizin.osce.shared.AnalysisType;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author dk
 *
 */
public interface StatisticalEvaluationDetailsView extends IsWidget{
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate 
	{
		public void analysisListBoxValueChanged(AnalysisType a);
		
		public void calculate();
		
		public void exportStatisticData();
	}

	public VerticalPanel getSequenceVP();
    
    void setDelegate(Delegate delegate);
    
    void setPresenter(Presenter systemStartActivity);
    
    public Button getExportBtn();
   
}
