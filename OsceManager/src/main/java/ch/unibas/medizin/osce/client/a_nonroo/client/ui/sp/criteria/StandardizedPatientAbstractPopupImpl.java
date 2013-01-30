package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria;

import java.util.Map;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class StandardizedPatientAbstractPopupImpl extends PopupPanel {
    // Highlight onViolation
    Map<String, Widget> advanceSearchCriteriaMap;
 	// E Highlight onViolation

	public void display(Button parentButton) {
		this.show();
		int left, top;
		
		if (parentButton.getAbsoluteLeft() + parentButton.getOffsetWidth()/2 + this.getOffsetWidth()/2 > Window.getClientWidth()) {
			left = Window.getClientWidth() - this.getOffsetWidth();
		} else {
			left = parentButton.getAbsoluteLeft() + parentButton.getOffsetWidth()/2 - this.getOffsetWidth()/2;
		}
		
		top = parentButton.getAbsoluteTop() - getOffsetHeight()/2 - 20;
		this.setPopupPosition(left, top);
	}

	public void display(int positionX,int positionY) {
		this.show();
		this.setPopupPosition(positionX,positionY);
	}

	public Map<String, Widget> getMap() {
		// TODO Auto-generated method stub
		return this.advanceSearchCriteriaMap;
	}
	
	public Map<String, Widget> getAdvanceSearchCriteriaMap() {
		// TODO Auto-generated method stub
		return this.advanceSearchCriteriaMap;
	}
}
