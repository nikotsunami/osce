package ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface TrainingSuggestionView extends IsWidget {

	interface Delegate{

	}
	
	public void setDelegate(Delegate delegate);

	public void setIsMorning(boolean b);

	public void setMorningRoleList(List<StandardizedRoleProxy> morningRolesList);

	public void setAfterNoonRoleList(List<StandardizedRoleProxy> afterNoonRolesList);

	public VerticalPanel getRoleNameVerticalPanel();

	public IconButton getMoreButton();

	public List<StandardizedRoleProxy> getMorningRoleList();

	public List<StandardizedRoleProxy> getAfterNoonRoleList();

}
