package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;

/**
 * @author dk
 *
 */
public interface PrintTemplatePopupView extends IsWidget{
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		// TODO define methods to be delegated!
	}

    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    
    void setPresenter(Presenter systemStartActivity);

	//Button getSendMailButton();

	String getMessageContent();

	void setMessageContent(String html);

	Button getSaveTemplateButton();

	Button getRestoreTemplateButton();

	ListBox getOsceList();

	Button getLoadTemplateButton();
	
	Button getPrintTemplateButton();
	
	void displayShortRoleAndPostNumberField();
}
