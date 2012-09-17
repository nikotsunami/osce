package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.Map;

import com.google.gwt.user.client.ui.IsWidget;


import ch.unibas.medizin.osce.client.a_nonroo.client.ui.LearningObjectiveViewImpl;
import ch.unibas.medizin.osce.client.managed.request.MainSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.MinorSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.SkillLevelProxy;
import ch.unibas.medizin.osce.client.managed.request.TopicProxy;


public interface RoleLearningSubView {
	
	interface Delegate {
		public void minorDeleteClicked(MinorSkillProxy minorSkill);
		public void majorDeleteClicked(MainSkillProxy mainSkill);
		
		public void addMainClicked();
		public void addMinorClicked();
		
		public void setMainClassiPopUpListBox(RoleLearningPopUpView popupView);
		public void setSkillLevelPopupListBox(RoleLearningPopUpView popupView);
		
		public void loadLearningObjectiveData();
		
		public void addLearningObjectiveTableRangeHandler();
		
		public void closeButtonClicked();
	}
	public void setDelegate(Delegate delegate);
	Map getMainSkillMap();
	
	//learning
	public LearningObjectiveViewImpl getLearningObjectiveViewImpl();			

}
