package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandartizedPatientAdvancedSearchSubView;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.QuickSearchBox;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;

public interface StandardizedPatientView  extends IsWidget{
	
    public interface Presenter {
    	public void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
    public interface Delegate {
    	public void newClicked();
    	public void performSearch(String q, List<String> list);
    	public void doAnimation(boolean flag);
		public void showDeletedSpCheckBoxSelected();
	}

     CellTable<StandardizedPatientProxy> getTable();
    /*custom celltable start code*/
    /*public String[] getPaths();*/
    List<String> getPaths();
    /*custom celltable end code*/
    public List<String> getSearchFilters();
    
    public String getQuery();
    
    public void setDelegate(Delegate delegate);
    public void updateSearch();
    public void setDetailPanel(boolean isDetailPlace);
    
    public SimplePanel getDetailsPanel();
    public void setPresenter(Presenter systemStartActivity);
    public StandartizedPatientAdvancedSearchSubView getStandartizedPatientAdvancedSearchSubViewImpl();
    //By Spec[Start
    public IconButton getExportButton();
    //By Spec]End
	QuickSearchBox getSearchBox();
	Map getSortMap();
	List<String> getColumnSortSet();
	//Added for OMS-148
	Boolean getShowDeletedSpCheckBoxValue();
}
