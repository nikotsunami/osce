package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.managed.request.StudentProxy;
import ch.unibas.medizin.osce.client.style.widgets.QuickSearchBox;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

public interface StudentManagementView extends IsWidget {

	 public interface Presenter {
	 
		 public void goTo(Place place);
	 }
	 
	 void setDelegate(Delegate delegate);

	 void setPresenter(Presenter systemStartActivity);

	 interface Delegate {

		void doAnimation(boolean b);

		void editStudentData(StudentProxy studentProxy, String string, String string2, String string3);

		void performSearch(String value);
		
	 }

	CellTable<StudentProxy> getTable();
	List<String> getPaths();
	void onRecordChange(RecordChangeEvent event);

	Map getSortMap();
	
	List<String> getColumnSortSet();
	
	void setDetailPanel(boolean b);

	AcceptsOneWidget getDetailsPanel();

	StudentManagementEditPopupView getStudentManagementEditPopView();
	public void editPopupView(final StudentProxy studentProxy);

	QuickSearchBox getSearchBox();
}
