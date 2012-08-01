package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import ch.unibas.medizin.osce.client.managed.request.ClassificationTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.MainClassificationProxy;
import ch.unibas.medizin.osce.client.managed.request.SkillLevelProxy;
import ch.unibas.medizin.osce.client.managed.request.SkillProxy;
import ch.unibas.medizin.osce.client.managed.request.TopicProxy;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ValueListBox;

public interface RoleLearningPopUpView extends IsWidget {

	interface Delegate{
		public void mainClassiListBoxClicked(MainClassificationProxy proxy, RoleLearningPopUpView popupView);
		public void classiTopicListBoxClicked(ClassificationTopicProxy proxy, RoleLearningPopUpView popupView);
	}
	
	public void setDelegate(Delegate delegate);	
	public Label getMainClssiLbl();
	public Label getClassiTopicLbl();
	public Label getTopicLbl();
	public ValueListBox<MainClassificationProxy> getMainClassiListBox();
	public ValueListBox<ClassificationTopicProxy> getClassiTopicListBox(); 
	public ValueListBox<TopicProxy> getTopicListBox();
	public Label getLevelLbl();
	public ValueListBox<SkillLevelProxy> getLevelListBox();
	public Button getOkBtn(); 
	public Button getCancelBtn(); 
	
}
