package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.management.relation.RoleList;

import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.text.shared.AbstractRenderer;
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
	
	// Issue Role 
		public Button getCancelBtn() {
			return cancelBtn;
		}
		
		public void setCancelBtn(Button cancelBtn) {
			this.cancelBtn = cancelBtn;
		}
		// E: Issue Role 

	@UiField
	Label roleLbl;
	

	// Issue Role 
	@UiField
	Button cancelBtn;
	// E: Issue Role 
			
	//Issue # 122 : Replace pull down with autocomplete.
	
	@UiField
	public DefaultSuggestBox<StandardizedRoleProxy, EventHandlingValueHolderItem<StandardizedRoleProxy>> roleLstBox;//=new DefaultSuggestBox<StandardizedRoleProxy, EventHandlingValueHolderItem<StandardizedRoleProxy>>();

/*	
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
	
	*/
	//Issue # 122 : Replace pull down with autocomplete.
	
	
	@UiField
	Label topicLbl;
	
	//Issue # 122 : Replace pull down with autocomplete.
	
	@UiField
	public DefaultSuggestBox<ChecklistTopicProxy, EventHandlingValueHolderItem<ChecklistTopicProxy>> topicLstBox;//=new DefaultSuggestBox<StandardizedRoleProxy, EventHandlingValueHolderItem<StandardizedRoleProxy>>();

	
	/*
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
	});*/
	
	//Issue # 122 : Replace pull down with autocomplete.
	
	@UiField
	Label questionLbl;
	
	@UiField
	Label specializationLbl;
	
	@UiField
	DefaultSuggestBox<SpecialisationProxy, EventHandlingValueHolderItem<SpecialisationProxy>> specializationLstBox;
	
	
	//Issue # 122 : Replace pull down with autocomplete.
	
	  
	public Label getSpecializationLbl() {
		return specializationLbl;
	}

	public void setSpecializationLbl(Label specializationLbl) {
		this.specializationLbl = specializationLbl;
	}

	public DefaultSuggestBox<SpecialisationProxy, EventHandlingValueHolderItem<SpecialisationProxy>> getSpecializationLstBox() {
		return specializationLstBox;
	}

	public void setSpecializationLstBox(
			DefaultSuggestBox<SpecialisationProxy, EventHandlingValueHolderItem<SpecialisationProxy>> specializationLstBox) {
		this.specializationLstBox = specializationLstBox;
	}

	@UiField
	public DefaultSuggestBox<ChecklistQuestionProxy, EventHandlingValueHolderItem<ChecklistQuestionProxy>> queListBox;//=new DefaultSuggestBox<StandardizedRoleProxy, EventHandlingValueHolderItem<StandardizedRoleProxy>>();

	/*
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
	});*/
	
	//Issue # 122 : Replace pull down with autocomplete.
	
	/*public DefaultSuggestBox<StandardizedRoleProxy> getRoleLstBox() {
		//Issue # 122 : Replace pull down with autocomplete.
		//return roleLstBox;
		return roleLstBox;
		//Issue # 122 : Replace pull down with autocomplete.
	}*/

	public void setRoleLstBox(ValueListBox<StandardizedRoleProxy> roleLstBox) {
		//Issue # 122 : Replace pull down with autocomplete.
		//this.roleLstBox = roleLstBox;
		//Issue # 122 : Replace pull down with autocomplete.
	}

	public ValueListBox<ChecklistTopicProxy> getTopicLstBox() {
		//Issue # 122 : Replace pull down with autocomplete.
		//return topicLstBox;
		return null;
		//Issue # 122 : Replace pull down with autocomplete.
	}

	public void setTopicLstBox(ValueListBox<ChecklistTopicProxy> topicLstBox) {
		//Issue # 122 : Replace pull down with autocomplete.
		//this.topicLstBox = topicLstBox;
		//Issue # 122 : Replace pull down with autocomplete.
	}
	
	public ValueListBox<ChecklistQuestionProxy> getQueListBox() {
		//Issue # 122 : Replace pull down with autocomplete.
		//return queListBox;
		return null;
		//Issue # 122 : Replace pull down with autocomplete.
	}

	public void setQueListBox(ValueListBox<ChecklistQuestionProxy> queListBox) {
		//Issue # 122 : Replace pull down with autocomplete.
		//this.queListBox = queListBox;
		//Issue # 122 : Replace pull down with autocomplete.
	}

	// Highlight onViolation
	ImportTopicPopupViewImpl importTopicPopupView;
	Map<String, Widget> checklistTopicMap;
	Map<String, Widget> checklistQuestionMap;
	// E Highlight onViolation
		
	
	public ImportTopicPopupViewImpl(boolean isImportQuestion,RoleDetailsChecklistSubViewChecklistTopicItemViewImpl TopicView) {
		super(true);
		Log.info("after super");
		
		
		
		if(isImportQuestion)
		{
			Log.info("inside if");
			add(BINDER.createAndBindUi(this));
			Log.info("inside if2");
			this.topicView=TopicView;
			okBtn.setText(constants.okBtn());
			roleLbl.setText(constants.role());
			topicLbl.setText(constants.roleTopic());
			questionLbl.setText("Questions");
			queListBox.setVisible(true);
			// Issue Role 
			cancelBtn.setText(constants.cancel());
			specializationLbl.removeFromParent();;
			specializationLstBox.removeFromParent();
			// E: Issue Role 
			Log.info(" if completed");
		}
		else
		{
			Log.info("inside else");
			
			add(BINDER.createAndBindUi(this));
			specializationLbl.setText(constants.specialisation());
			queListBox.setVisible(false);
			queListBox.removeFromParent();
			questionLbl.removeFromParent();
			okBtn.setText(constants.okBtn());
			roleLbl.setText(constants.role());
			topicLbl.setText(constants.roleTopic());
			questionLbl.setText("Questions");
			// Issue Role 
			cancelBtn.setText(constants.cancel());
			
			Log.info("else end");
			// E: Issue Role 
			
		}
		
		this.roleLstBox.setRenderer(new AbstractRenderer<StandardizedRoleProxy>() {

			@Override
			public String render(StandardizedRoleProxy object) {
				// TODO Auto-generated method stub
				if(object!=null)
				{
				return object.getShortName();
				}
				else
				{
					return "";
				}
			}
		});
		
		this.getSpecializationLstBox().setRenderer(new AbstractRenderer<SpecialisationProxy>() {

				@Override
				public String render(SpecialisationProxy object) {
					// TODO Auto-generated method stub
					if(object!=null)
					{
					return object.getName();
					}
					else
					{
						return "";
					}
				}
			});
		
		// Highlight onViolation
		importTopicPopupView=this;
		checklistTopicMap=new HashMap<String, Widget>();
		//Issue # 122 : Replace pull down with autocomplete.
		checklistTopicMap.put("title", topicLstBox.getTextField().advancedTextBox);
		checklistTopicMap.put("description", topicLstBox.getTextField().advancedTextBox);
		
		checklistQuestionMap=new HashMap<String, Widget>();
		checklistQuestionMap.put("checkListTopic", topicLstBox.getTextField().advancedTextBox);
		//Issue # 122 : Replace pull down with autocomplete.
		//Issue # 122 : Replace pull down with autocomplete.
		checklistQuestionMap.put("instruction", roleLstBox.getTextField().advancedTextBox);
		checklistQuestionMap.put("instruction", roleLstBox.getTextField().advancedTextBox);
		//Issue # 122 : Replace pull down with autocomplete.
		//Issue # 122 : Replace pull down with autocomplete.
		//checklistQuestionMap.put("question", queListBox);
		checklistQuestionMap.put("question", queListBox.getTextField().advancedTextBox);
		//Issue # 122 : Replace pull down with autocomplete.
		// E Highlight onViolation
		
		
		roleLstBox.addHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
			Log.info("value change handler");	
			//Issue # 122 : Replace pull down with autocomplete.
			//if(this.roleLstBox.getValue()==null)
			valueChangeEventForRoleLstBox();
			
			}
			
			
		});
		
		specializationLstBox.addHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				Log.info("specializationLstBox value changed");
				
				valueChangeEventSpecialisationLstBox();
			}
		});
		
		topicLstBox.addHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
			Log.info("value change handler");	
			//Issue # 122 : Replace pull down with autocomplete.
			//if(this.roleLstBox.getValue()==null)
			valueChangeEventForTopicLstBox();
			}
			
			
		});
		
		queListBox.setRenderer(new AbstractRenderer<ChecklistQuestionProxy>() {

			@Override
			public String render(ChecklistQuestionProxy object) {
				// TODO Auto-generated method stub
				if(object!=null)
				{
				return object.getQuestion();
				}
				else
				{
					return "";
				}
			}
		});
	}
	
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	interface Binder extends UiBinder<Widget, ImportTopicPopupViewImpl> {
	}
	
	public void valueChangeEventSpecialisationLstBox()
	{
		Log.info("valueChangeEvent call method");
		if(this.specializationLstBox.getSelected()==null)
		{
			//Issue # 122 : Replace pull down with autocomplete.
			//this.topicLstBox.setAcceptableValues(null);
			//topicLstBox.setSuggestOracle(null);
			//Issue # 122 : Replace pull down with autocomplete.
		}
		else
			delegate.specialisationListBoxValueSelected(this.specializationLstBox.getSelected(),this);
	}
	
	
	//Issue # 122 : Replace pull down with autocomplete.
	
	public void valueChangeEventForRoleLstBox()
	{
		//Issue # 122 : Replace pull down with autocomplete.
				//if(this.roleLstBox.getValue()==null)
		Log.info("valueChangeEvent call method");
				if(this.roleLstBox.getSelected()==null)
				{
					//Issue # 122 : Replace pull down with autocomplete.
					//this.topicLstBox.setAcceptableValues(null);
					//topicLstBox.setSuggestOracle(null);
					//Issue # 122 : Replace pull down with autocomplete.
				}
				else
					delegate.roleListBoxValueSelected(this.roleLstBox.getSelected(),this);
					//delegate.roleListBoxValueSelected(this.roleLstBox.getValue(),this);
				//Issue # 122 : Replace pull down with autocomplete.
	}
	
	//Issue # 122 : Replace pull down with autocomplete.
	
	//Issue # 122 : Replace pull down with autocomplete.
	
	
	public void valueChangeEventForTopicLstBox()
	{
		//Issue # 122 : Replace pull down with autocomplete.
				//if(this.roleLstBox.getValue()==null)
		Log.info("valueChangeEvent call method");
				if(this.roleLstBox.getSelected()==null)
				{
					//Issue # 122 : Replace pull down with autocomplete.
					//this.topicLstBox.setAcceptableValues(null);
				//	topicLstBox.setSuggestOracle(null);
					//Issue # 122 : Replace pull down with autocomplete.
				}
				else
					delegate.topicListBoxValueSelected(this.topicLstBox.getSelected(), this);
					//delegate.roleListBoxValueSelected(this.roleLstBox.getValue(),this);
				//Issue # 122 : Replace pull down with autocomplete.
	}
	
	//Issue # 122 : Replace pull down with autocomplete.
	/*
	@UiHandler("roleLstBox")
	public void roleLstBoxValueChangeHandler(ValueChangeEvent<StandardizedRoleProxy> event)
	{
		Log.info("roleLstBoxValueChangeHandler");
		//Issue # 122 : Replace pull down with autocomplete.
		//if(this.roleLstBox.getValue()==null)
		if(this.roleLstBox.getSelected()==null)
			this.topicLstBox.setAcceptableValues(null);
		
		else
			delegate.roleListBoxValueSelected(this.roleLstBox.getSelected(),this);
			//delegate.roleListBoxValueSelected(this.roleLstBox.getValue(),this);
		//Issue # 122 : Replace pull down with autocomplete.
	}
	*/
	
	//Issue # 122 : Replace pull down with autocomplete.
/*
	@UiHandler("topicLstBox")
	public void topicLstBoxValueChangeHandler(ValueChangeEvent<ChecklistTopicProxy> event)
	{
		Log.info("topicLstBox Selected");
		if(this.topicLstBox.getValue()==null)
			this.topicLstBox.setAcceptableValues(null);
		else
			delegate.topicListBoxValueSelected(this.topicLstBox.getValue(), this);
	}
	*/
	//Issue # 122 : Replace pull down with autocomplete.

	
	@UiHandler("okBtn")
	public void okBtnClicked(ClickEvent event)
	{
		Log.info("okBtnClicked");
		if(!queListBox.isVisible())
		{
			Log.info("Import Topic #");
			//Log.info("Value: " + this.getTopicLstBox().getValue());
			// Highlight onViolation
			//Issue # 122 : Replace pull down with autocomplete.
			/*if(this.getTopicLstBox().getValue()!=null)
			{
				delegate.importTopic(this.getTopicLstBox().getValue(),importTopicPopupView);	
			}*/
			
			if(topicLstBox.getSelected()!=null)
			{
				delegate.importTopic(topicLstBox.getSelected(),importTopicPopupView);	
			}
			else
			{
				MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
				dialogBox.showConfirmationDialog(constants.checklistTopicNotNull());
			}
			//Issue # 122 : Replace pull down with autocomplete.
			
			
		}
		else
		{
			//Issue # 122 : Replace pull down with autocomplete.
			/*if(this.getQueListBox().getValue()!=null)
			{
			delegate.importQuestion(this.getQueListBox().getValue(),this.topicView,importTopicPopupView);
			}*/
			if(queListBox.getSelected()!=null)
			{
				delegate.importQuestion(queListBox.getSelected(),this.topicView,importTopicPopupView);
			}
			else
			{
				MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
				dialogBox.showConfirmationDialog(constants.questionNotNull());
			}
			//Issue # 122 : Replace pull down with autocomplete.
		}
		// E Highlight onViolation
			
	}
	
	// Issue Role
	@UiHandler("cancelBtn")
	public void cancelBtnClicked(ClickEvent event)
	{
		Log.info("Cancel Topic");
		hide();
	}
	// E: Issue Role
	
	// Highlight onViolation
	@Override
	public Map getChecklistTopicMap()
	{
		return this.checklistTopicMap;
	}
	
	@Override 
	public Map getChecklistQuestionMap()
	{
		return this.checklistQuestionMap;
	}
	// E Highlight onViolation	

	@Override
	public ImportTopicPopupViewImpl getView() {
		// TODO Auto-generated method stub
		return this;
	}
}
