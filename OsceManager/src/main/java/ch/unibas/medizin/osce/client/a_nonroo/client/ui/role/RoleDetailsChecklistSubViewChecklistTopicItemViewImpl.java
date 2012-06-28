package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicProxy;
import ch.unibas.medizin.osce.client.style.resources.UiIcons;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RoleDetailsChecklistSubViewChecklistTopicItemViewImpl  extends Composite implements RoleDetailsChecklistSubViewChecklistTopicItemView{

	private static final Binder BINDER = GWT.create(Binder.class);
	
	private Delegate delegate;
	

	@UiField
	public Button addCheckListQuestionButton;
	
	public CheckListTopicPopupView topicPopup;
	
	@UiField
	public VerticalPanel questionButtonVP;
	
	public CheckListTopicPopupView questionPopup;
	
	public ImportTopicPopupView importQuestionPopup;
	
	@UiField
	Image arrow;
	
	@UiField
	HorizontalPanel addQuestionHP;
	
	@UiField
	public Button importQuestionButton;
	
	@UiField
	public Label checkListTopicLbl;
	
	@UiField
	public VerticalPanel checkListQuestionVerticalPanel;
	
	@UiField 
	public Label descriptionLbl;
	
	@UiField 
	IconButton delete;
	
	@UiField 
	IconButton edit;
	
	@UiField
	Image down;
	
	@UiField
	Image up;
	
	@UiHandler("down")
	public void topicMoveDown(ClickEvent event){
		Log.info("Topic Move Down");
		delegate.topicMoveDown(this.proxy, topicView);
	}
	
	@UiHandler("up")
	public void topicMoveUp(ClickEvent event){
		Log.info("Topic Move Up");
		delegate.topicMoveUp(this.proxy, this.topicView);
	}
	
	@UiField
	DisclosurePanel checkListTopicDisclosurePanel;
	
	private final UiIcons uiIcons = GWT.create(UiIcons.class);
	
	private ChecklistTopicProxy proxy;
	
	private RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView;
	
	public ChecklistTopicProxy getProxy() {
		return proxy;
	}

	public void setProxy(ChecklistTopicProxy proxy) {
		this.proxy = proxy;
	}

	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	public RoleDetailsChecklistSubViewChecklistTopicItemViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
		addCheckListQuestionButton.setText(constants.addCheckListQuestion());
		importQuestionButton.setText(constants.importQuestion());
		
		topicView=this;
		
	}
	
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	interface Binder extends UiBinder<Widget, RoleDetailsChecklistSubViewChecklistTopicItemViewImpl> {
	}
	
	@UiHandler("addCheckListQuestionButton")
	public void addCheckListQuestion(ClickEvent event)
	{
		showQuestionPopup();
		//delegate.saveCheckListQuestion(checkListQuestionTxtBox.getText(),this);
	}
	
	
	public void showQuestionPopup()
	{
		if(questionPopup==null)
		{
			questionPopup=new CheckListTopicPopupViewImpl();
			
			
			((CheckListTopicPopupViewImpl)questionPopup).setAnimationEnabled(true);
		
			questionPopup.getDescriptionLbl().setText(constants.questionInstruction());
			
			questionPopup.getTopicLbl().setText(constants.questionName());
			
			((CheckListTopicPopupViewImpl)questionPopup).setWidth("150px");

		
			RootPanel.get().add(((CheckListTopicPopupViewImpl)questionPopup));
			
			questionPopup.getOkBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					if(questionPopup.getTopicTxtBox().getValue()=="" || questionPopup.getDescriptionTxtBox().getValue()=="")
					{
					}	
					else
					{						
						delegate.saveCheckListQuestion(questionPopup.getTopicTxtBox().getValue(),questionPopup.getDescriptionTxtBox().getValue(),topicView);
						((CheckListTopicPopupViewImpl)questionPopup).hide(true);
				
						questionPopup.getTopicTxtBox().setValue("");
						questionPopup.getDescriptionTxtBox().setValue("");
					}
				}
		});
		}
		
		((CheckListTopicPopupViewImpl)questionPopup).setPopupPosition(questionButtonVP.getAbsoluteLeft(), questionButtonVP.getAbsoluteTop()-180);
		((CheckListTopicPopupViewImpl)questionPopup).show();
	}
	@UiHandler("delete")
	public void deleteCheckListTopic(ClickEvent event)
	{
		Log.info("delete Topic");
		if(Window.confirm("are you sure you want to delete this Topic?"))
			delegate.deleteCheckListTopic(this.proxy,topicView);
	}
	
	@UiHandler("edit")
	public void editCheckListTopic(ClickEvent event)
	{
		Log.info("edit Topic");
		
		//Window.alert("Edit Clicked");
		showTopicPopupPanel(this.proxy, event.getScreenX(), event.getScreenY());
		
		
		//if(Window.confirm("are you sure you want to delete this Topic?"))
		//	delegate.deleteCheckListTopic(this.proxy,topicView);
	}
	
	public void showTopicPopupPanel(final ChecklistTopicProxy proxy, int x, int y)
	{
		if(topicPopup==null)
		{
			topicPopup=new CheckListTopicPopupViewImpl();
			
			
			((CheckListTopicPopupViewImpl)topicPopup).setAnimationEnabled(true);
		
			topicPopup.getDescriptionLbl().setText(constants.topicDescription());
			topicPopup.getDescriptionTxtBox().setValue(proxy.getDescription());
			
			topicPopup.getTopicLbl().setText(constants.checklistTopic());
			topicPopup.getTopicTxtBox().setValue(proxy.getTitle());
			
			((CheckListTopicPopupViewImpl)topicPopup).setWidth("150px");

		
			RootPanel.get().add(((CheckListTopicPopupViewImpl)topicPopup));
			
			topicPopup.getOkBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					if(topicPopup.getTopicTxtBox().getValue()=="" || topicPopup.getDescriptionTxtBox().getValue()=="")
					{
					}	
					else
					{
						delegate.updateCheckListTopic(proxy,topicPopup.getTopicTxtBox().getValue(),topicPopup.getDescriptionTxtBox().getValue(),topicView);
					
						((CheckListTopicPopupViewImpl)topicPopup).hide(true);
				
						//topicPopup.getTopicTxtBox().setValue("");
						//topicPopup.getDescriptionTxtBox().setValue("");
					}
				}
		});
		}
		
		
		((CheckListTopicPopupViewImpl)topicPopup).setPopupPosition(x-200, y - 265);
		((CheckListTopicPopupViewImpl)topicPopup).show();
		
	}
	
	@UiHandler("arrow")
	public void minmaxTopic(ClickEvent event)
	{
		if(checkListTopicDisclosurePanel.isOpen())
		{
			checkListTopicDisclosurePanel.setOpen(false);
			arrow.setResource(uiIcons.triangle1East());
		}
		else
		{
			checkListTopicDisclosurePanel.setOpen(true);
			arrow.setResource(uiIcons.triangle1South());
		}
	}
	
	@UiHandler("importQuestionButton")
	public void importQuestiuons(ClickEvent event)
	{
		showImportquestionView();
	}
	
	public void showImportquestionView()
	{
//		if(importQuestionPopup==null)
		{
			importQuestionPopup=new ImportTopicPopupViewImpl(true,this.topicView);
			
			
			((ImportTopicPopupViewImpl)importQuestionPopup).setAnimationEnabled(true);
		
			
			delegate.setRoleListBoxValue(importQuestionPopup);
			
			
			((ImportTopicPopupViewImpl)importQuestionPopup).setWidth("150px");

		
			RootPanel.get().add(((ImportTopicPopupViewImpl)importQuestionPopup));
			
			importQuestionPopup.getOkBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					//if(importTopicView.getRoleLstBox().getValue()=="" || importTopicView.getTopicLstBox().to.getValue()=="")
					{
					}	
				//	else
					{
					//	delegate.saveCheckListTopic(topicPopup.getTopicTxtBox().getValue(),topicPopup.getDescriptionTxtBox().getValue());
						
						((ImportTopicPopupViewImpl)importQuestionPopup).hide(true);
				
						//importTopicView.getTopicTxtBox().setValue("");
						//importTopicView.getDescriptionTxtBox().setValue("");
					}
				
			}
		});
			
	}
		((ImportTopicPopupViewImpl)importQuestionPopup).setPopupPosition(addQuestionHP.getAbsoluteLeft(), addQuestionHP.getAbsoluteTop()-180);
		((ImportTopicPopupViewImpl)importQuestionPopup).show();
	}
}
