package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.shared.BellAssignmentType;
import ch.unibas.medizin.osce.shared.TimeBell;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author dk
 *
 */
public interface BellScheduleView extends IsWidget{
	
    public interface Presenter {
        void goTo(Place place);
    }

	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		public SemesterProxy getSemester();
		public void getNewSchedule();
		public void onBellScheduleUpload();
		public void exportCsvSupervisorClicked(int x, int y);
	}

    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    
    void setPresenter(Presenter systemStartActivity);

	CellTable<BellAssignmentType> getTable();
	
	void setSemesterName(String semesterName);
	
	int getTimeInMinute();
	
	TimeBell isPlusTime();
}
