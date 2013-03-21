package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.HashMap;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.Validator;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicProxy;
import ch.unibas.medizin.osce.client.style.resources.UiIcons;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RoleDetailsChecklistSubViewChecklistTopicItemViewImpl  extends Composite implements RoleDetailsChecklistSubViewChecklistTopicItemView{

	private static final Binder BINDER = GWT.create(Binder.class);
	
	private final UiIcons uiIcons = GWT.create(UiIcons.class);
	
	private ChecklistTopicProxy proxy;
	
	private RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView;
	
	private Delegate delegate;
	
	// SPEC Change
	private MessageConfirmationDialogBox confirmationDialogBox;
	

	AbsolutePanel discloserAP;
	
	public void setDiscloserAP(AbsolutePanel discloserAP) {
		this.discloserAP = discloserAP;
	}


	@UiField
	VerticalPanel discloserVP;
	
	
	@UiField
	VerticalPanel topicsdiscloserVP;
	
	
	
	
	public VerticalPanel getTopicsdiscloserVP() {
		return topicsdiscloserVP;
	}

	public void setTopicsdiscloserVP(VerticalPanel topicsdiscloserVP) {
		this.topicsdiscloserVP = topicsdiscloserVP;
	}


	@UiField
	public Button addCheckListQuestionButton;
	
	public CheckListTopicPopupView topicPopup;
	
	@UiField
	public VerticalPanel questionButtonVP;
	
	//public CheckListTopicPopupView questionPopup;
	public CheckListQuestionPopupView questionPopup;
	
	public ImportTopicPopupView importQuestionPopup;
	
	@UiField
	Image arrow;
	
	@UiField
	VerticalPanel addQuestionHP;
	
	@UiField
	public Button importQuestionButton;
	
	@UiField
	public Label checkListTopicLbl;
	
	@UiField
	public AbsolutePanel queAP;
	
	

	
	@UiField
	public VerticalPanel checkListQuestionVerticalPanel;
	
	public VerticalPanel getCheckListQuestionVerticalPanel() {
		return checkListQuestionVerticalPanel;
	}


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
	
	
	
//	@UiField
//	public Label draglbl;
	
	public IconButton getDelete() {
		return delete;
	}

	public void setDelete(IconButton delete) {
		this.delete = delete;
	}

	public IconButton getEdit() {
		return edit;
	}

	public void setEdit(IconButton edit) {
		this.edit = edit;
	}

	public Image getDown() {
		return down;
	}

	public void setDown(Image down) {
		this.down = down;
	}

	public Image getUp() {
		return up;
	}

	public void setUp(Image up) {
		this.up = up;
	}


	@UiField
	public AbsolutePanel topicAP;
	
	//change SPEC
	
	
	@Override
	public VerticalPanel getqueVP()
	{
	return this.checkListQuestionVerticalPanel;
	}
	
	@Override
	public AbsolutePanel getTopicAP()
	{
	return this.topicAP;
	}

	
	@Override
	public VerticalPanel getDiscloserVP()
	{
	return this.discloserVP;
	}
	
	@Override
	public AbsolutePanel getDiscloserAP()
	{
	return this.discloserAP;
	}


//	AbsolutePositionDropController dropControllerAP;
	
	PickupDragController dragController;
	
	PickupDragController dragControllerTopics;
	
	
	public PickupDragController getDragController() {
		return dragController;
	}
	
	public PickupDragController getDragControllerTopics() {
		return dragControllerTopics;
	}
//	public AbsolutePositionDropController getDropControllerAP() {
//		return dropControllerAP;
//	}

	
	VerticalPanelDropController dropController;
	VerticalPanelDropController dropControllerTopics;
	
	@Override
	public VerticalPanelDropController getDropTopicsController() {
		// TODO Auto-generated method stub
		return dropControllerTopics;
	}
	
	@Override
	public DisclosurePanel getRoleDetailsTopicDP()
	{
		return this.checkListTopicDisclosurePanel;
	}
	//End
	
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
	

	
	public ChecklistTopicProxy getProxy() {
		return proxy;
	}

	public void setProxy(ChecklistTopicProxy proxy) {
		this.proxy = proxy;
	}

	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	// Highlight onViolation
			Map<String, Widget> checklistQuestionMap;			
			Map<String, Widget> checklistTopicMap;			
	// E Highlight onViolation
	
	public RoleDetailsChecklistSubViewChecklistTopicItemViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
		
	
		
		addCheckListQuestionButton.setTitle(constants.addCheckListQuestion());
		addCheckListQuestionButton.setText(constants.addCheckListQuestion());
		importQuestionButton.setText(constants.roleImportQuestion());
		
		//SPEC Change
		addCheckListQuestionButton.addStyleName("expbtn");
		importQuestionButton.addStyleName("expbtn");
		//SPEC Change
		
		topicView=this;
		
		dragController = new PickupDragController(topicAP,false);
		dropController = new VerticalPanelDropController(checkListQuestionVerticalPanel);
		dragController.registerDropController(dropController);
		dragController.setBehaviorScrollIntoView(true);
		
		
//		dragControllerTopics = new PickupDragController(discloserAP, false);
//		dropControllerTopics = new VerticalPanelDropController(discloserVP);
//		dragControllerTopics.registerDropController(dropControllerTopics);
//		dragControllerTopics.setBehaviorBoundaryPanelDrop(true);
	}
	
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	interface Binder extends UiBinder<Widget, RoleDetailsChecklistSubViewChecklistTopicItemViewImpl> {
	}
	
	@UiHandler("addCheckListQuestionButton")
	public void addCheckListQuestion(ClickEvent event)
	{
		Log.info("Call addCheckListQuestion");
		showQuestionPopup();
		//delegate.saveCheckListQuestion(checkListQuestionTxtBox.getText(),this);
	}
	
	public void showQuestionPopup()
	{
		if (questionPopup == null)
		{
			questionPopup = new CheckListQuestionPopupViewImpl();
			
			((CheckListQuestionPopupViewImpl) questionPopup).setAnimationEnabled(true);
			
			((CheckListQuestionPopupViewImpl) questionPopup).setWidth("350px");
			
			RootPanel.get().add(((CheckListQuestionPopupViewImpl) questionPopup));
			
			questionPopup.getOkBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {

					if(Validator.isNotNull(questionPopup.getQuestionTextArea().getValue())){
						delegate.saveCheckListQuestion(questionPopup.getQuestionTextArea().getValue(),questionPopup.getInstructionTextArea().getValue(),topicView);
					}else{
						confirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
						confirmationDialogBox.showConfirmationDialog(constants.warningFillRequiredFields());
					}
				}
			});
			
			questionPopup.getCancelBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					((CheckListQuestionPopupViewImpl)questionPopup).hide(true);					
					questionPopup.getQuestionTextArea().setValue("");
					questionPopup.getInstructionTextArea().setValue("");					
				}
			});
		}
		
		((CheckListQuestionPopupViewImpl)questionPopup).setPopupPosition(addCheckListQuestionButton.getAbsoluteLeft()-150, addCheckListQuestionButton.getAbsoluteTop()-250); //SPEC Change
		
		checklistQuestionMap=new HashMap<String, Widget>();
		checklistQuestionMap.put("question", questionPopup.getQuestionTextArea());
		checklistQuestionMap.put("instruction", questionPopup.getInstructionTextArea());
		
		((CheckListQuestionPopupViewImpl)questionPopup).getQuestionTextArea().setText("");
		((CheckListQuestionPopupViewImpl)questionPopup).getInstructionTextArea().setText("");
		
		((CheckListQuestionPopupViewImpl)questionPopup).show();
	}
	
	/*public void showQuestionPopup()
	{
		if(questionPopup==null)
		{
			questionPopup=new CheckListTopicPopupViewImpl(true);
			
		 // SPEC Change	
			((CheckListTopicPopupViewImpl)questionPopup).setAnimationEnabled(true);
		
			questionPopup.getDescriptionLbl().setText(constants.roleQuestionInstruction());
			
			questionPopup.getTopicLbl().setText(constants.roleQuestionName());
			
			questionPopup.getTopicTxtBox().setWidth("300px");
			questionPopup.getDescriptionTxtBox().setWidth("300px");
			

			
			((CheckListTopicPopupViewImpl)questionPopup).setWidth("350px");

		
			RootPanel.get().add(((CheckListTopicPopupViewImpl)questionPopup));
			
			
			
			questionPopup.getOkBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					Log.info("Call onClick");
					// Highlight onViolation
					//comment by user
					if(questionPopup.getTopicTxtBox().getValue()=="" || questionPopup.getDescriptionTxtBox().getValue()=="")
					{
					}	
					else
					{	
					
					// SPEC Change
					if(Validator.isNotNull(questionPopup.getTopicTxtBox().getValue())){
						delegate.saveCheckListQuestion(questionPopup.getTopicTxtBox().getValue(),questionPopup.getDescriptionTxtBox().getValue(),topicView);
						// E Highlight onViolation
						//((CheckListTopicPopupViewImpl)questionPopup).hide(true);				
						//questionPopup.getTopicTxtBox().setValue("");
						//questionPopup.getDescriptionTxtBox().setValue("");
					}else{
						confirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
						confirmationDialogBox.showConfirmationDialog(constants.warningFillRequiredFields());
					}
					
					// Highlight onViolation
					//}
					// E Highlight onViolation
				}
		});
		// Issue Role
		questionPopup.getCancelBtn().addClickHandler(new ClickHandler() {
				
		@Override
		public void onClick(ClickEvent event) 
		{
				Log.info("Cancel Click...");
				((CheckListTopicPopupViewImpl)questionPopup).hide(true);					
					questionPopup.getTopicTxtBox().setValue("");
					questionPopup.getDescriptionTxtBox().setValue("");
			
		}
		});
		// E: Issue Role
	
			
		
		}
		
		((CheckListTopicPopupViewImpl)questionPopup).setPopupPosition(addCheckListQuestionButton.getAbsoluteLeft()-150, addCheckListQuestionButton.getAbsoluteTop()-252); //SPEC Change
		// Highlight onViolation
		checklistQuestionMap=new HashMap<String, Widget>();
		checklistQuestionMap.put("question", questionPopup.getTopicTxtBox());
		checklistQuestionMap.put("instruction", questionPopup.getDescriptionTxtBox());
		// E Highlight onViolation
		((CheckListTopicPopupViewImpl)questionPopup).getTopicTxtBox().setText("");
		((CheckListTopicPopupViewImpl)questionPopup).getDescriptionTxtBox().setText("");
		((CheckListTopicPopupViewImpl)questionPopup).show();
	}*/
	@UiHandler("delete")
	public void deleteCheckListTopic(ClickEvent event)
	{
		Log.info("delete Topic");
		final ChecklistTopicProxy tempproxy;
		tempproxy=proxy;
		/*if(Window.confirm("are you sure you want to delete this Topic?"))
			delegate.deleteCheckListTopic(this.proxy,topicView);*/
		
		// Issue Role
				 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
				 dialogBox.showYesNoDialog("are you sure you want to delete this Topic?");
				 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							dialogBox.hide();							
							Log.info("yes click");
							delegate.deleteCheckListTopic(tempproxy,topicView);
							return;

								}
							});

					dialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							dialogBox.hide();
							Log.info("no click");
							return;
							
						}
					});
				// E: Issue Role
	}
	
	@UiHandler("edit")
	public void editCheckListTopic(ClickEvent event)
	{
		Log.info("edit Topic");
		
		//Window.alert("Edit Clicked");
		showTopicPopupPanel(this.proxy, edit.getAbsoluteLeft(), edit.getAbsoluteTop()); //SPEC Change
		
		
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
			
			
			topicPopup.getTopicLbl().setText(constants.checklistTopic());
			
			
			topicPopup.getTopicTxtBox().setWidth("300px");
			topicPopup.getDescriptionTxtBox().setWidth("300px");
			((CheckListTopicPopupViewImpl)topicPopup).setWidth("350px");

//			((CheckListTopicPopupViewImpl)topicPopup).setWidth("150px");

		
			RootPanel.get().add(((CheckListTopicPopupViewImpl)topicPopup));
			
			
			
			topicPopup.getOkBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event)
				{
					// Highlight onViolation
					Log.info("Call onClick");
					/*if(topicPopup.getTopicTxtBox().getValue()=="" || topicPopup.getDescriptionTxtBox().getValue()=="")
					{
					}	
					else
					{*/
											
					//SPEC Change
					//if(Validator.isNotNull(topicPopup.getTopicTxtBox().getValue(),topicPopup.getDescriptionTxtBox().getValue())){
					if(Validator.isNotNull(topicPopup.getTopicTxtBox().getValue())){
					delegate.updateCheckListTopic(proxy,topicPopup.getTopicTxtBox().getValue(),topicPopup.getDescriptionTxtBox().getValue(),topicView);
					}else{
						confirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
						confirmationDialogBox.showConfirmationDialog(constants.warningFillRequiredFields());
					}
					// E Highlight onViolation
					
						//((CheckListTopicPopupViewImpl)topicPopup).hide(true);
					// E Highlight onViolation
				
						//topicPopup.getTopicTxtBox().setValue("");
						//topicPopup.getDescriptionTxtBox().setValue("");
					// Highlight onViolation
					//}
					// E Highlight onViolation
				}
		});
			// Issue Role
			topicPopup.getCancelBtn().addClickHandler(new ClickHandler() 
			{
				
				@Override
				public void onClick(ClickEvent event) 
				{
					((CheckListTopicPopupViewImpl)topicPopup).hide();
				}
			});
			// E: Issue Role
		
		}
		
		((CheckListTopicPopupViewImpl)topicPopup).setPopupPosition(x-450, y - 195); //SPEC Change+
		
		
		// Highlight onViolation
					checklistTopicMap=new HashMap<String, Widget>();
					checklistTopicMap.put("title", topicPopup.getTopicTxtBox());
					checklistTopicMap.put("description", topicPopup.getDescriptionTxtBox());			
		// E Highlight onViolation
		
		topicPopup.getDescriptionTxtBox().setValue(proxy.getDescription());
		topicPopup.getTopicTxtBox().setValue(proxy.getTitle());
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
		Log.info("Call importQuestiuons");
		showImportquestionView();
	}
	
	public void showImportquestionView()
	{
		if(importQuestionPopup==null)
		{
			importQuestionPopup=new ImportTopicPopupViewImpl(true,this.topicView);
			
			
			((ImportTopicPopupViewImpl)importQuestionPopup).setAnimationEnabled(true);
		
			
			delegate.setRoleListBoxValue(importQuestionPopup);
			
			
			((ImportTopicPopupViewImpl)importQuestionPopup).setWidth("150px");

		
			RootPanel.get().add(((ImportTopicPopupViewImpl)importQuestionPopup));
			
			/*importQuestionPopup.getOkBtn().addClickHandler(new ClickHandler() {
				
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
		});*/
			
	}
		
		((ImportTopicPopupViewImpl)importQuestionPopup).setPopupPosition(importQuestionButton.getAbsoluteLeft(), importQuestionButton.getAbsoluteTop()-260); // SPEC Change
		
		((ImportTopicPopupViewImpl)importQuestionPopup).roleLstBox.setText("");
		((ImportTopicPopupViewImpl)importQuestionPopup).topicLstBox.setText("");
		((ImportTopicPopupViewImpl)importQuestionPopup).queListBox.setText("");
		((ImportTopicPopupViewImpl)importQuestionPopup).show();
	}
	// Highlight onViolation
	
	@Override
	public Label getDraglbl() {
		// TODO Auto-generated method stub
		return checkListTopicLbl;
	}
	
	@Override
	public Map getChecklistTopicMap()	
	{
		return this.checklistTopicMap;
	}

	@Override
	public Map getChecklistQuestionMap() {
		// TODO Auto-generated method stub
		return this.checklistQuestionMap;
	}
	


}
