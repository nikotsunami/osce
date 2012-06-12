package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.io.IOException;


import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
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

public class ImportTopicPopupViewImpl  extends PopupPanel implements ImportTopicPopupView{

	private static final Binder BINDER = GWT.create(Binder.class);
	
	private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	private RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView;
	
	@UiField
	Button okBtn;
	
	public Button getOkBtn() {
		return okBtn;
	}

	public void setOkBtn(Button okBtn) {
		this.okBtn = okBtn;
	}

	@UiField
	Label roleLbl;
	
	@UiField(provided = true)
	ValueListBox<StandardizedRoleProxy> roleLstBox=new ValueListBox<StandardizedRoleProxy>(new Renderer<StandardizedRoleProxy>() {

		@Override
		public String render(StandardizedRoleProxy object) {
			// TODO Auto-generated method stub
			if(object==null)
				return "";
			else
			return object.getShortName();
		}

		@Override
		public void render(StandardizedRoleProxy object,
				Appendable appendable) throws IOException {
			// TODO Auto-generated method stub
			
		}
	});
	
	@UiField
	Label topicLbl;
	
	@UiField(provided = true)
	ValueListBox<ChecklistTopicProxy> topicLstBox=new ValueListBox<ChecklistTopicProxy>(new Renderer<ChecklistTopicProxy>() {

		@Override
		public String render(ChecklistTopicProxy object) {
			// TODO Auto-generated method stub
			if(object==null)
				return "";
			else
				return object.getTitle();
		}

		@Override
		public void render(ChecklistTopicProxy object, Appendable appendable)
				throws IOException {
			// TODO Auto-generated method stub
			
		}
	});
	
	@UiField
	Label questionLbl;
	
	@UiField(provided = true)
	ValueListBox<ChecklistQuestionProxy> queListBox=new ValueListBox<ChecklistQuestionProxy>(new Renderer<ChecklistQuestionProxy>() {

		@Override
		public String render(ChecklistQuestionProxy object) {
			// TODO Auto-generated method stub
			if(object==null)
				return "";
			else
				return object.getQuestion();
		}

		@Override
		public void render(ChecklistQuestionProxy object, Appendable appendable)
				throws IOException {
			// TODO Auto-generated method stub
			
		}
	});
	
	public ValueListBox<StandardizedRoleProxy> getRoleLstBox() {
		return roleLstBox;
	}

	public void setRoleLstBox(ValueListBox<StandardizedRoleProxy> roleLstBox) {
		this.roleLstBox = roleLstBox;
	}

	public ValueListBox<ChecklistTopicProxy> getTopicLstBox() {
		return topicLstBox;
	}

	public void setTopicLstBox(ValueListBox<ChecklistTopicProxy> topicLstBox) {
		this.topicLstBox = topicLstBox;
	}
	
	public ValueListBox<ChecklistQuestionProxy> getQueListBox() {
		return queListBox;
	}

	public void setQueListBox(ValueListBox<ChecklistQuestionProxy> queListBox) {
		this.queListBox = queListBox;
	}

	
	
	public ImportTopicPopupViewImpl(boolean isImportQuestion,RoleDetailsChecklistSubViewChecklistTopicItemViewImpl TopicView) {
		super(true);
		if(isImportQuestion)
		{
			add(BINDER.createAndBindUi(this));
			this.topicView=TopicView;
			okBtn.setText(constants.okBtn());
			roleLbl.setText(constants.role());
			topicLbl.setText(constants.topic());
			questionLbl.setText("Questions");
		}
		else
		{
			add(BINDER.createAndBindUi(this));
			queListBox.setVisible(false);
			questionLbl.setVisible(false);
			okBtn.setText(constants.okBtn());
			roleLbl.setText(constants.role());
			topicLbl.setText(constants.topic());
			questionLbl.setText("Questions");
			
		}
	}
	
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	interface Binder extends UiBinder<Widget, ImportTopicPopupViewImpl> {
	}
	
	@UiHandler("roleLstBox")
	public void roleLstBoxValueChangeHandler(ValueChangeEvent<StandardizedRoleProxy> event)
	{
		Log.info("roleLstBoxValueChangeHandler");
		if(this.roleLstBox.getValue()==null)
			this.topicLstBox.setAcceptableValues(null);
		
		else
			delegate.roleListBoxValueSelected(this.roleLstBox.getValue(),this);
	}
	
	@UiHandler("topicLstBox")
	public void topicLstBoxValueChangeHandler(ValueChangeEvent<ChecklistTopicProxy> event)
	{
		Log.info("topicLstBox Selected");
		if(this.topicLstBox.getValue()==null)
			this.topicLstBox.setAcceptableValues(null);
		else
			delegate.topicListBoxValueSelected(this.topicLstBox.getValue(), this);
	}
	
	@UiHandler("okBtn")
	public void okBtnClicked(ClickEvent event)
	{
		Log.info("okBtnClicked");
		if(!queListBox.isVisible())
		{
			Log.info("Import Topic");
			delegate.importTopic(this.getTopicLstBox().getValue());
		}
		else
		{
			delegate.importQuestion(this.getQueListBox().getValue(),this.topicView);
		}
			
	}
	
}
