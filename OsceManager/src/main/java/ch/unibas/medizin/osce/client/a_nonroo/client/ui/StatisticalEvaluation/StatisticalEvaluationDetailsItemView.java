package ch.unibas.medizin.osce.client.a_nonroo.client.ui.StatisticalEvaluation;

import ch.unibas.medizin.osce.client.managed.request.AnswerProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;

import com.google.gwt.dom.client.TableElement;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public interface StatisticalEvaluationDetailsItemView {

	
		interface Delegate 
		{
			public void onOffButtonClicked(Long id,Boolean missing);
		}

		
	    
	    void setDelegate(Delegate delegate);
	    
	    public ChecklistQuestionProxy getChecklistQuestionProxy();
	    
	    public void setChecklistQuestionProxy(
				ChecklistQuestionProxy checklistQuestionProxy);
	    
	    public Widget asWidget();
	    
		public Label getSumPerSequenceLbl();

		public HorizontalPanel getPostDataHP();
		


		public Label getSequenceLbl();
		
		public TableElement getSequenceHeader() ;
		
		public AnswerProxy getAnswerProxy();
		

		public void setAnswerProxy(AnswerProxy answerProxy);
		
		public DoctorProxy getDoctorProxy();

		public void setDoctorProxy(DoctorProxy doctorProxy);

	
		public ToggleButton getOnOffButton();

		public HorizontalPanel getFourthColumnHP();
		
		public Button createAddPointButton();
	
}
