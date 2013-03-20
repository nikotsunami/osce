package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

public interface CheckListQuestionPopupView extends IsWidget {

	interface Delegate{
		
	}
	
	void setDelegate(Delegate delegate);
	
	public CheckBox getIsOverallQuestionChkBox();
	
	public TextArea getQuestionTextArea();

	public TextArea getInstructionTextArea();
	
	public Button getOkBtn();
	
	public Button getCancelBtn();	
}
