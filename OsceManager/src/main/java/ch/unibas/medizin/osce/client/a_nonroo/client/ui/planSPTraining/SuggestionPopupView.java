package ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining;

import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SuggestionPopupView extends PopupPanel {

	VerticalPanel verticalPanel = new VerticalPanel();
	
	public SuggestionPopupView() {
	
		this.addStyleName("trainingSuggestionPopup");
		this.setAnimationEnabled(true);
		this.setAutoHideEnabled(true);
		this.add(verticalPanel);
	}

	public VerticalPanel getVerticalPanel() {
		return verticalPanel;
	}

	public void setVerticalPanel(VerticalPanel verticalPanel) {
		this.verticalPanel = verticalPanel;
	}
	
	
}
