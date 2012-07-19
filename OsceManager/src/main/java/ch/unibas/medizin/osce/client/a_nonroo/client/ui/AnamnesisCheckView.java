package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
import ch.unibas.medizin.osce.client.style.widgets.QuickSearchBox;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.UIObject;
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
		void moveUp(AnamnesisCheckTitleProxy title, AnamnesisCheckProxy proxy);
		void moveDown(AnamnesisCheckTitleProxy title, AnamnesisCheckProxy proxy);
		void moveUpTitle(AnamnesisCheckTitleProxy proxy);
		void moveDownTitle(AnamnesisCheckTitleProxy proxy);
		void deleteClicked(AnamnesisCheckProxy proxy);
		void newDetailClicked(String titleId);
		void performSearch();
		void changeFilterTitleShown(String selectedTitle);
		void orderEdited(AnamnesisCheckProxy proxy, String sortOrder);
		
		void setQuestionTableData(AnamnesisCheckTitleProxy title);
		
		void showDetails(AnamnesisCheckProxy anamnesisCheck);
		void addDataProvider(AnamnesisCheckTitleProxy title,
				ListDataProvider<AnamnesisCheckProxy> dataProvider);
		void editTitle(AnamnesisCheckTitleProxy title, UIObject refObj);
		void deleteTitle(AnamnesisCheckTitleProxy title);
		void addNewTitleClicked(String titleText);
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

	void filterTitle(AnamnesisCheckTitleProxy title);
	
	// Highlight onViolation
	Map getAnamnesisCheckTitleMap();
	// E Highlight onViolation
}
