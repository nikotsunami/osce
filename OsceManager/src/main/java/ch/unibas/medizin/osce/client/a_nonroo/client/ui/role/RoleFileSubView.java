package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.FileProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;

public interface RoleFileSubView extends IsWidget{
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void fileMoveUp(FileProxy proxy,StandardizedRoleProxy standardizedRoleProxy);
		void fileMoveDown(FileProxy proxy,StandardizedRoleProxy standardizedRoleProxy);
		void fileDeleteClicked(FileProxy proxy,StandardizedRoleProxy standardizedRoleProxy);
		void newFileClicked(String fileName,String fileDescription,StandardizedRoleProxy standardizedRoleProxy,FormPanel formPanel);
		//void performSearch(String q);
		//void changeNumRowShown(String selectedValue);
		void changeFilterTitleShown(String selectedTitle);
		
		public void downloadFile(String path);

	}

    CellTable<FileProxy> getTable();
    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    public void setValue(StandardizedRoleProxy proxy);
    
	//SimplePanel getDetailsPanel();
    void setPresenter(Presenter systemStartActivity);
    
    void setListBoxItem(String length);
	// Highlight onViolation
    Map getFileMap();
	// E Highlight onViolation
    
    //SPEC Change
	Button getNewButton();
	FileUpload getFileUpload();
	TextBox getFileDescription();
    
//    String getMediaContent();

	//void setMediaContent(String link);
    //void setSearchBoxShown(String selectedValue);
    //String getSearchBoxShown();
    
    //void setSearchFocus(boolean focused);
	//ListBox getFilterTitle();
    
    //QuickSearchBox getSearchBox();
    
    //ListBox getRangNumBox();
}
