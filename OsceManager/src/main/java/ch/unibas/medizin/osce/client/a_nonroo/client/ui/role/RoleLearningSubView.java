package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.Map;

import com.google.gwt.user.client.ui.IsWidget;


import ch.unibas.medizin.osce.client.managed.request.MainSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.MinorSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.SkillLevelProxy;
import ch.unibas.medizin.osce.client.managed.request.TopicProxy;


public interface RoleLearningSubView {
	
	interface Delegate {
		public void minorDeleteClicked(MinorSkillProxy minorSkill);
		public void majorDeleteClicked(MainSkillProxy mainSkill);
		
		public void addMainSkillClicked(TopicProxy topicProxy, SkillLevelProxy skillLevelProxy);
		public void addMinorSkillClicked(TopicProxy proxy, SkillLevelProxy skillLevelProxy);
		
		public void setMainClassiPopUpListBox(RoleLearningPopUpView popupView);
		public void setSkillLevelPopupListBox(RoleLearningPopUpView popupView);
	}
	public void setDelegate(Delegate delegate);
	Map getMainSkillMap();

}
