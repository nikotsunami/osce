package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.SimpleSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;

public interface RoleOtherSearchCriteriaView extends IsWidget{
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void simpleSearchMoveUp(SimpleSearchCriteriaProxy proxy,StandardizedRoleProxy standardizedRoleProxy);
		void simpleSearchMoveDown(SimpleSearchCriteriaProxy proxy,StandardizedRoleProxy standardizedRoleProxy);
		void simpleSearchDeleteClicked(SimpleSearchCriteriaProxy proxy,StandardizedRoleProxy standardizedRoleProxy);
		void newSimpleSearchClicked(String SearchName,String SearchValue,StandardizedRoleProxy standardizedRoleProxy);
		//void performSearch(String q);
		//void changeNumRowShown(String selectedValue);
		//void changeFilterTitleShown(String selectedTitle);

	}

    CellTable<SimpleSearchCriteriaProxy> getTable();
    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    public void setValue(StandardizedRoleProxy proxy);
    
	//SimplePanel getDetailsPanel();
    void setPresenter(Presenter systemStartActivity);
	Map getSimpleSearchCriteriaMap();
    
	// SPEC Change
	Button getAddSimpleSearch();
	TextBox getSearchName();
	TextBox getSearchValue();
	// SPEC Change
    
    //void setListBoxItem(String length);
    
//    String getMediaContent();

	//void setMediaContent(String link);
    //void setSearchBoxShown(String selectedValue);
    //String getSearchBoxShown();
    
    //void setSearchFocus(boolean focused);
	//ListBox getFilterTitle();
    
    //QuickSearchBox getSearchBox();
    
    //ListBox getRangNumBox();
}
