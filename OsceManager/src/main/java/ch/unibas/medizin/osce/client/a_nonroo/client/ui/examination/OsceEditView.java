package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.CloudConfigurationProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSettingsProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.TaskProxy;
import ch.unibas.medizin.osce.shared.BucketInfoType;
import ch.unibas.medizin.osce.shared.StudyYears;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ValueListBox;

public interface OsceEditView extends IsWidget {
	void setDelegate(Delegate delegate);
	void setEditTitle(boolean edit);

	public interface Presenter {
		void goTo(Place place);
	}

	interface Delegate {
		void cancelClicked();

		void saveClicked(OsceProxy osce, OsceSettingsProxy oscSettingsProxy);

		void previewButtonClicked(int left, int top);

		CloudConfigurationProxy findCloudConfigurationSetting();

	}

	//RequestFactoryEditorDriver<OsceProxy, OsceEditViewImpl> createEditorDriver();
	void setPresenter(Presenter osceEditActivity);
	void setStudyYearPickerValues(List<StudyYears> asList);
	void setTasksPickerValues(List<TaskProxy> emptyList);
	void setSemsterValues(List<SemesterProxy> emptyList);
	void setOsceValues(List<OsceProxy> emptyList);
	Set<TaskProxy> getTaskValue();
	Map getOsceMap();
	HorizontalPanel getHorizontalTabPanel();	
	DivElement getLabelOsceCreationType();
	void setOsceProxy(OsceProxy osce);
	void setValue(OsceProxy osce, OsceSettingsProxy response);
	void setOsceSttingsProxy(OsceSettingsProxy response);
	void setS3Values();
}
