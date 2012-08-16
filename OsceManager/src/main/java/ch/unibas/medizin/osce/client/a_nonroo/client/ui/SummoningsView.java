package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author dk
 *
 */
public interface SummoningsView extends IsWidget{
	
	public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
			
		void clickAllSP();
		void clickAllExaminor();
		
		void clickSelectedSP();							
		void clickSelectedExaminor();
				
		void sendEmailtoSP(ClickEvent event);
		void sendEmailtoExaminor();
		
		void printCopyforSP();		
		void printCopyforExaminor();		
	}

    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    
    void setPresenter(Presenter systemStartActivity);
    
    
    VerticalPanel getVpSP();
    
    VerticalPanel getVpExaminor();

	Label getSpLabel();

	Label getExaminorLabel();

	RadioButton getSpRb();

	RadioButton getExaminerRb();

	RadioButton getSpAllRb();

	RadioButton getExaminerAllRb();
}
