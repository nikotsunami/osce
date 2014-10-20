package ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining;

import java.util.Date;

import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.IsWidget;

public interface SingleDaySelectedView extends IsWidget {

	interface Delegate{

		void proposeTrainingDateClicked();

		void proposeOsceDayButtonClicked();

		void scheduleTraingClicked(ClickEvent event);

		void removeTrainingDayButtonClicked();

		void removeOsceDayButtonClicked();

	}
	
	public void setDelegate(Delegate delegate);

	public void setDate(Date dateOnWidget);

	public IconButton getProposedTrainingDayButton();

	public IconButton getRemoveProposedTrainingDaButton();

	public IconButton getProposedOsceDayButton();

	public IconButton getRemoveProposedOsceDaybutton();
}
