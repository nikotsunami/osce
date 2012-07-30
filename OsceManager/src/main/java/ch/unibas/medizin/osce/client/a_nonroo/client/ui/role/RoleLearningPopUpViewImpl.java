package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;


import java.io.IOException;

import ch.unibas.medizin.osce.client.managed.request.ClassificationTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.MainClassificationProxy;
import ch.unibas.medizin.osce.client.managed.request.SkillLevelProxy;
import ch.unibas.medizin.osce.client.managed.request.TopicProxy;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class RoleLearningPopUpViewImpl extends PopupPanel implements RoleLearningPopUpView {

	private static final Binder BINDER = GWT.create(Binder.class);
	
	interface Binder extends UiBinder<Widget, RoleLearningPopUpViewImpl> {
	}

	@UiField
	Label mainClssiLbl;
	
	@UiField
	Label classiTopicLbl;
	
	@UiField
	Label topicLbl;
	
	@UiField (provided = true)
	ValueListBox<MainClassificationProxy> mainClassiListBox = new ValueListBox<MainClassificationProxy>(new Renderer<MainClassificationProxy>() {

		@Override
		public String render(MainClassificationProxy object) {
			if (object == null)
				return "";
			else
				return object.getShortcut();
		}

		@Override
		public void render(MainClassificationProxy object, Appendable appendable)
				throws IOException {
			// TODO Auto-generated method stub
			
		}
	});
	
	@UiField (provided = true)
	ValueListBox<ClassificationTopicProxy> classiTopicListBox = new ValueListBox<ClassificationTopicProxy>(new Renderer<ClassificationTopicProxy>() {

		@Override
		public String render(ClassificationTopicProxy object) {
			if (object == null)
				return "";
			else
				return object.getShortcut();		
		}

		@Override
		public void render(ClassificationTopicProxy object,
				Appendable appendable) throws IOException {
			// TODO Auto-generated method stub
			
		}
	});
	
	@UiField (provided = true)
	ValueListBox<TopicProxy> topicListBox = new ValueListBox<TopicProxy>(new Renderer<TopicProxy>() {

		@Override
		public String render(TopicProxy object) {
			if (object == null)
				return "";
			else
				return object.getTopicDesc();
		}

		@Override
		public void render(TopicProxy object, Appendable appendable)
				throws IOException {
			// TODO Auto-generated method stub
			
		}
	});
	
	@UiField
	Label levelLbl;
	
	@UiField (provided = true)
	ValueListBox<SkillLevelProxy> levelListBox = new ValueListBox<SkillLevelProxy>(new Renderer<SkillLevelProxy>() {

		@Override
		public String render(SkillLevelProxy object) {
			if (object == null)
				return "";
			else	
				return String.valueOf(object.getLevelNumber());
		}

		@Override
		public void render(SkillLevelProxy object, Appendable appendable)
				throws IOException {
			// TODO Auto-generated method stub
			
		}
	}); 
	
	@UiField
	Button okBtn;
	
	@UiField
	Button cancelBtn;
	
	private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	public RoleLearningPopUpViewImpl() {
		super(true);
		add(BINDER.createAndBindUi(this));
		
		okBtn.setText(constants.okBtn());
		cancelBtn.setText(constants.cancel());
		
		mainClssiLbl.setText(constants.mainClassi());
		classiTopicLbl.setText(constants.classiTopic());
		topicLbl.setText(constants.topicLbl());
		levelLbl.setText(constants.skillLevel());
		
		levelListBox.getElement().scrollIntoView();
	}
	
	@UiHandler("mainClassiListBox")
	public void mainClassiListBoxChangeHandler(ValueChangeEvent<MainClassificationProxy> event)
	{
		if (this.mainClassiListBox.getValue() == null)
			this.mainClassiListBox.setAcceptableValues(null);
		else
			delegate.mainClassiListBoxClicked(this.mainClassiListBox.getValue(), this);
	}
	
	@UiHandler("classiTopicListBox")
	public void classiTopicListBoxChangeHandler(ValueChangeEvent<ClassificationTopicProxy> event)
	{
		if (this.classiTopicListBox.getValue() == null)
			this.classiTopicListBox.setAcceptableValues(null);
		else
			delegate.classiTopicListBoxClicked(this.classiTopicListBox.getValue(), this);
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	public Label getMainClssiLbl() {
		return mainClssiLbl;
	}

	public void setMainClssiLbl(Label mainClssiLbl) {
		this.mainClssiLbl = mainClssiLbl;
	}

	public Label getClassiTopicLbl() {
		return classiTopicLbl;
	}

	public void setClassiTopicLbl(Label classiTopicLbl) {
		this.classiTopicLbl = classiTopicLbl;
	}

	public Label getTopicLbl() {
		return topicLbl;
	}

	public void setTopicLbl(Label topicLbl) {
		this.topicLbl = topicLbl;
	}

	public ValueListBox<MainClassificationProxy> getMainClassiListBox() {
		return mainClassiListBox;
	}

	public ValueListBox<ClassificationTopicProxy> getClassiTopicListBox() {
		return classiTopicListBox;
	}

	public ValueListBox<TopicProxy> getTopicListBox() {
		return topicListBox;
	}

	public Button getOkBtn() {
		return okBtn;
	}

	public void setOkBtn(Button okBtn) {
		this.okBtn = okBtn;
	}

	public Button getCancelBtn() {
		return cancelBtn;
	}

	public void setCancelBtn(Button cancelBtn) {
		this.cancelBtn = cancelBtn;
	}

	public void setMainClassiListBox(
			ValueListBox<MainClassificationProxy> mainClassiListBox) {
		this.mainClassiListBox = mainClassiListBox;
	}

	public void setClassiTopicListBox(
			ValueListBox<ClassificationTopicProxy> classiTopicListBox) {
		this.classiTopicListBox = classiTopicListBox;
	}

	public void setTopicListBox(ValueListBox<TopicProxy> topicListBox) {
		this.topicListBox = topicListBox;
	}

	public Label getLevelLbl() {
		return levelLbl;
	}

	public void setLevelLbl(Label levelLbl) {
		this.levelLbl = levelLbl;
	}

	public ValueListBox<SkillLevelProxy> getLevelListBox() {
		return levelListBox;
	}

	public void setLevelListBox(ValueListBox<SkillLevelProxy> levelListBox) {
		this.levelListBox = levelListBox;
	}	
}
