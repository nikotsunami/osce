package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;
import ch.unibas.medizin.osce.client.style.widgets.QuickSearchBox;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;

public interface AnamnesisCheckView extends IsWidget{
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void moveUp(AnamnesisCheckProxy proxy);
		void moveDown(AnamnesisCheckProxy proxy);
		void deleteClicked(AnamnesisCheckProxy proxy);
		void newDetailClicked(String titleId);
		void newTitleClicked();
		void performSearch(String q);
//		void changeNumRowShown(String selectedValue);
		void changeFilterTitleShown(String selectedTitle);
		void saveOrder();
		void orderEdited(AnamnesisCheckProxy proxy, String sortOrder); 
//		void resetUserSpecifiedOrder(AnamnesisCheckProxy anamnesisCheck, String value);
		
		void setQuestionTableData(ListDataProvider<AnamnesisCheckProxy> dataProvider, AnamnesisCheckTitleProxy title);
		
		void showDetails(AnamnesisCheckProxy anamnesisCheck);
		void goToTitle(AnamnesisCheckTitleProxy anamnesisCheckTitleProxy);
	}

//    CellTable<AnamnesisCheckProxy> getTable();
    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    
	SimplePanel getDetailsPanel();
    void setPresenter(Presenter systemStartActivity);
    
//    void setListBoxItem(String length);
    void setSearchBoxShown(String selectedValue);
    String getSearchBoxShown();
    
    void setSearchFocus(boolean focused);
	ListBox getFilterTitle();
    
    QuickSearchBox getSearchBox();

    VerticalPanel getAnamnesisCheckPanel();
    
    void loadAnamnesisCheckPanel(List<AnamnesisCheckTitleProxy> anamnesisCheckTitleList, boolean isOpen);

//	void addDisclosurePanel(AnamnesisCheckTitleProxy anamnesisCheckTitleProxy, List<AnamnesisCheckProxy> anamnesisChecks);
    
    
//    ListBox getRangNumBox();
}
