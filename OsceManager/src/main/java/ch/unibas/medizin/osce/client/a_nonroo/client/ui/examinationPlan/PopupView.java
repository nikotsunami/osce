package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;

import java.util.Date;

import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.ValueListBox;


public interface PopupView extends IsWidget {
	
	interface Delegate {
		
		
	}
	
	void setDelegate(Delegate delegate);
	
	public ValueListBox<Date> getEndTimeListBox() ;
	
	public void createExaminerAssignPopupView();
	
	public SuggestBox getExaminerSuggestionBox();
	
	public void setExaminerSuggestionBox(SuggestBox examinerSuggestionBox);
	
	public Label getStartTimeValue();
	
	public Button getOkButton();
	
	public Button getCancelButton();
	
	public void createExaminerInfoPopupView();
	
	public Label getNameValue();
	
	public Label getEndTimeValue();
	
	public IconButton getEdit();
	
	public void createOscePostPopupView();
	
	public void createSPPopupView();
	
	public Label getExaminerNameValue() ;
	
	public Button getSaveBtn();
	
	public void createEditBreakDurationPopupView();
	
	public IntegerBox getBreakDuration() ;
}
