package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;



import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleItemAccessProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;

public interface RoleBaseTableAccessView extends IsWidget {
	public interface Presenter {
		void goTo(Place place);
	}

	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {

		void deleteAccessType(RoleBaseItemProxy roleBasedItemProxy,Label dataaccessLabel,RoleItemAccessProxy roleItemAccessProxy);
		// todo
//		void newClicked(String itemName, int item_defination);

		
		
	}

	void setDelegate(Delegate delegate);

	Delegate getDelegate();

	public void setBaseItemMidifiedValue(String value);

	void setPresenter(Presenter systemStartActivity);

	
}
