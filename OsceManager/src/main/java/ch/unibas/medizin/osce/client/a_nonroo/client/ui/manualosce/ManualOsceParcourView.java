package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.AccordianPanelView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.ContentViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.HeaderView;
import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.client.style.widgets.FocusableValueListBox;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.PostType;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface ManualOsceParcourView extends IsWidget {

	interface Delegate{
		
		void addOscePost(CourseProxy courseProxy, PostType value, FocusableValueListBox<PostType> postTypeListBox);

		void deleteParcour(OsceSequenceProxy osceSequenceProxy, CourseProxy courseProxy);
		
	}
	
	public void setDelegate(Delegate delegate);
	
	public ContentViewImpl getContentView();
	
	public HeaderView getHeaderView();
	
	public AccordianPanelView getAccordianPanelView();
	
	public CourseProxy getCourseProxy();
	
	public void setCourseProxy(CourseProxy courseProxy);

	public void addDeleteButton();
	
	public void removeDeleteButton();
	
	public OsceSequenceProxy getOsceSequenceProxy();
	
	public void setOsceSequenceProxy(OsceSequenceProxy osceSequenceProxy);
	
	public VerticalPanel getAddOscePostVerticalPanel();
	
	public FocusableValueListBox<PostType> getPostTypeListBox();
	
	public IconButton getDeleteButton();
}