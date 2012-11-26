package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.managed.request.CourseProxy;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author dk
 *
 */
public interface IndividualSchedulesDetailsView extends IsWidget{
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate 
	{
		void clickAllStud();	
		void clickAllSP();
		void clickAllExaminor();
		
		void clickSelectedStud();
		void clickSelectedSP();							
		void clickSelectedExaminor();
		
		void printCopyforStud(ClickEvent event);
		void printCopyforSP(ClickEvent event);		
		void printCopyforExaminor(ClickEvent event);		
	}

    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    
    void setPresenter(Presenter systemStartActivity);
    
    VerticalPanel getVpStudent();
    
    VerticalPanel getVpSP();
    
    VerticalPanel getVpExaminor();
	

	RadioButton getSpRb();

	RadioButton getStudRb();

	RadioButton getExaminerRb();

	RadioButton getSpAllRb();

	RadioButton getStudAllRb();

	RadioButton getExaminerAllRb();

	VerticalPanel getDataVPStud();
	
	VerticalPanel getDataVPSP();
	
	VerticalPanel getDataVPExaminer();

	DisclosurePanel getDisclosureStudentPanel();

	DisclosurePanel getDisclosureSPPanel();

	DisclosurePanel getDisclosureExaminerPanel();
	
	ValueListBox<CourseProxy> getParcourListBox();
	
	void setParcourListBox(ValueListBox<CourseProxy> parcourListBox);
	
  
}
