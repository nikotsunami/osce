package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.shared.TraitTypes;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;

public interface ScarEditPopupView extends IsWidget{

	interface Delegate {
		
		
	}
	
	void setDelegate(Delegate delegate);
	
	public ValueListBox<TraitTypes> getTraitTypeBox();
	
	public Label getTypeLbl();

	public Label getLocationLbl();
	
	public TextBox getLocationTxtBox();
	
	public Button getOkBtn();
	
	// Issue Role
	public Button getCancelBtn();
}
