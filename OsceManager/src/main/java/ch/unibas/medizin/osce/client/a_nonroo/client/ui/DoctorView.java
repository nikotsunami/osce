package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;

public interface DoctorView  extends IsWidget{
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void newClicked();
		
		void performSearch(String q);
		//Issue # 122 : Replace pull down with autocomplete.
		void changeFilterTitleShown(ClinicProxy selectedTitle);
		//void changeFilterTitleShown();
		//Issue # 122 : Replace pull down with autocomplete.
	}

    CellTable<DoctorProxy> getTable();
    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    
	SimplePanel getDetailsPanel();
    void setPresenter(Presenter systemStartActivity);
    Map getSortMap();
	List<String> getColumnSortSet();
    //Module : 6
    ListBox getFilterTitle();
	DefaultSuggestBox<ClinicProxy, EventHandlingValueHolderItem<ClinicProxy>> getSuggestBox();
	   public void setDetailPanel(boolean isDetailPlace);
}
