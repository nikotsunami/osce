package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.Map;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.datepicker.client.DateBox;


import ch.unibas.medizin.osce.client.a_nonroo.client.ui.LearningObjectiveViewImpl;
import ch.unibas.medizin.osce.client.managed.request.MainSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.MinorSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.SkillLevelProxy;
import ch.unibas.medizin.osce.client.managed.request.TopicProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;


public interface RoleOsceSemesterSubView {
	
	interface Delegate {

		void changeDasteValueForOsceSemesterCall();
		
	}
	public void setDelegate(Delegate delegate);
	
	public CellTable<OsceProxy> getOsceSemesterTable();
	
	public DateBox getStartDate();
	public DateBox getEndDate();
	public IconButton getSearchButton();

	
	//learning
				

}
