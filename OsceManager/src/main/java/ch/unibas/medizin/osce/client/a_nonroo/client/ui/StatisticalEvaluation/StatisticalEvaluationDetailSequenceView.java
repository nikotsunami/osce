package ch.unibas.medizin.osce.client.a_nonroo.client.ui.StatisticalEvaluation;

import ch.unibas.medizin.osce.client.managed.request.AnswerProxy;
import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.dom.client.TableElement;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public interface StatisticalEvaluationDetailSequenceView {

	 public interface Presenter {
	        void goTo(Place place);
	    }
		/**
		 * Implemented by the owner of the view.
		 */
		interface Delegate 
		{
			public void sequenceDisclosurePanelOpen(StatisticalEvaluationDetailSequenceViewImpl view);
			
			public void parcourDisclosurePanelOpen(StatisticalEvaluationDetailSequenceViewImpl view);
			
			public void graphBtnClicked(Long osceId);
		}

		
		public Label getSequenceLbl();
		
	    void setDelegate(Delegate delegate);
	    
	    public OsceSequenceProxy getOsceSequenceProxy();
	    
	    public void setOsceSequenceProxy(OsceSequenceProxy osceSequenceProxy);
	    
	    public HorizontalPanel getPostViewHP();
	    
	    public HorizontalPanel getPostDataHP();
	    
	    public Label createPostDataLabel();
	    
	    public Label getSumPerSequenceLbl();
	    
	    public HTMLPanel getMainPanel();
	    
	    public VerticalPanel getDisclosureVP();
	    
	    public Widget asWidget();
	    
	    public CourseProxy getCourseProxy();
	    
	    public void setCourseProxy(CourseProxy courseProxy);
	    
	    public TableElement getSequenceHeader();
	    
	    public DisclosurePanel getSequenceDisclosurePanel();
	    
	    public OsceDayProxy getOsceDayProxy() ;
	    
	    public void setOsceDayProxy(OsceDayProxy osceDayProxy);
	    
	    public AnswerProxy getAnswerProxy();
	    
	    public void setAnswerProxy(AnswerProxy answerProxy);
	    
	    public OscePostProxy getOscePostProxy();
	    
	    public void setOscePostProxy(OscePostProxy oscePostProxy);
	    
	    public boolean isSequencePanel();


		public void setSequencePanel(boolean isSequencePanel) ;
		
		
		public boolean isPostPanel();


		public void setPostPanel(boolean isPostPanel);
		
		public HorizontalPanel getFourthColumnHP();
		
		public IconButton getGraphBtn();
}
