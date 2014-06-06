package ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining;

import java.util.Date;

import ch.unibas.medizin.osce.client.managed.request.TrainingBlockProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;

public interface BlockView extends IsWidget {

	interface Delegate{

		void splitBlockClicked(Date blockStartDate,TrainingBlockProxy trainingBlockProxy);

		void joinBlockButtonClicked(Date blockStartDate,TrainingBlockProxy trainingBlockProxy);

	}
	
	public void setDelegate(Delegate delegate);

	public void setBlockStartDate(Date firstDateOfWeekOfCreatedTD);

	public void setTrainingBlockProxy(TrainingBlockProxy blockProxy);

	public void setIsSplitted(boolean b);

	public void clearView();

	public IconButton getSplitBlockButton();

	public IconButton getJoinBlockButton();

	public boolean getIsSplitted();

	public Label getBlockLabel();

	public Date getBlockStartDate();


}
