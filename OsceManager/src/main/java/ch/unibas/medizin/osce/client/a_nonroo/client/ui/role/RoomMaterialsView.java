package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;


import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;

public interface RoomMaterialsView extends IsWidget{

    public interface Presenter {
    	public void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
    public interface Delegate {
    	void showSubviewClicked();
	}
    public SimplePanel getDetailsPanel();
    public void setPresenter(Presenter systemStartActivity);
    public void setDelegate(Delegate delegate);
}
