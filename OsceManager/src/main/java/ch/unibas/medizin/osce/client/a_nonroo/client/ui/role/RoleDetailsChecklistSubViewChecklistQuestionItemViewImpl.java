package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;



import java.util.HashMap;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.Validator;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicProxy;
import ch.unibas.medizin.osce.client.style.resources.UiIcons;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.FlowPanelDropController;
import com.allen_sauer.gwt.dnd.client.drop.HorizontalPanelDropController;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl extends Composite implements RoleDetailsChecklistSubViewChecklistQuestionItemView {
	
	private static final Binder BINDER = GWT.create(Binder.class);
	private final UiIcons uiIcons = GWT.create(UiIcons.class);
	
	private Delegate delegate;
	
	private ChecklistTopicProxy topicProxy;
	private RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView;
	
		
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	@UiField
	public IconButton delete;
	
	
	@UiField 
	public VerticalPanel checkListQuestionItemVerticalPanel;
	
	@UiField
	public Label questionItemLbl;
	
	@UiField
	public Label questionInstruction;
	
	
	@UiField
	public AbsolutePanel optionBoundryPanel;
	
	@UiField
	public AbsolutePanel criteriaBoundryPanel;
	
	private PickupDragController criteriaDragController;
	
	public PickupDragController getCriteriaDragController() {
		return criteriaDragController;
	}

	private FlowPanelDropController criteriaDropController;
	
	private PickupDragController optionDragController;
	
	public PickupDragController getOptionDragController() {
		return optionDragController;
	}

	private FlowPanelDropController optionDropController;
	
	//SPEC Change
	
	private MessageConfirmationDialogBox confirmationDialogBox;
	
	//spec
	
				
	
	@UiField
	public AbsolutePanel roleQueAP;
	
	@UiField
	Image arrow;
	
	@UiField
	DisclosurePanel checkListQuestionDisclosurePanel;
	
	@UiField
	Label optionLbl;
	
	@UiField
	Label criteriaLbl;
	
//	@UiField
//	public IconButton edit;
//	
//	
//	@UiField
//	public FlowPanel criteriaHorizontalPanel;
//	
//	
//
//	
//	@UiField
//	public IconButton addCriteriaButton;
//	
//	@UiField
//	public Image down;
	
	@Override
	public AbsolutePanel getRoleQueAP()
	{
		Log.info("Impl Drag getRoleCriteriaAP Called");
		return this.roleQueAP;
	}
	
	@UiField
	HorizontalPanel roleQueHP;
	
	
	PickupDragController dragController;
	
	public PickupDragController getDragController() {
		Log.info("Impl Drag controller Called");
		return dragController;
	}
	
	//FlowPanelDropController dropController;
	HorizontalPanelDropController dropController;
	
	
	public HorizontalPanel getRoleQueHP(){
		Log.info("Impl Drag getRoleCriteriaHP Called");
		return this.roleQueHP;
	}
	
	public FlowPanel getRoleQueFP(){
		Log.info("Impl Drag getRoleCriteriaFP Called");
		return this.criteriaHorizontalPanel;
	}
	
	//end
	
	public RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView;
	
	
	public Label getQuestionInstruction() {
		return questionInstruction;
	}

	public void setQuestionInstruction(Label questionInstruction) {
		this.questionInstruction = questionInstruction;
	}

	public Label getQuestionItemLbl() {
		return questionItemLbl;
	}

	public void setQuestionItemLbl(Label questionItemLbl) {
		this.questionItemLbl = questionItemLbl;
	}

	@UiField
	public IconButton edit;
	
	@UiField
	public FlowPanel criteriaHorizontalPanel;
	
	@UiField
	public IconButton addCriteriaButton;
	
	@UiField
	public Image down;
	
	@UiHandler("down")
	public void questionDown(ClickEvent event)
	{
		Log.info("question Move Down");
//		System.out.println("move down topic title "+topicView);
		delegate.questionMoveDown(this.questionView,this.getChecklistTopicProxy(),this.getRoleDetailsChecklistSubViewChecklistTopicItemView());
	}
	
	@UiField
	public Image up;
	@UiHandler("up")
	public void questionUp(ClickEvent event)
	{
		Log.info("Question Move Up");
//		System.out.println("move up topic title "+topicView);
		delegate.questionMoveUp(this.questionView,this.getChecklistTopicProxy(),this.getRoleDetailsChecklistSubViewChecklistTopicItemView());
	}
	
	@UiField
	VerticalPanel headerPanel;
	
	
	
	@UiHandler("arrow")
	public void minmaxTopic(ClickEvent event)
	{
		if(checkListQuestionDisclosurePanel.isOpen())
		{
			checkListQuestionDisclosurePanel.setOpen(false);
			arrow.setResource(uiIcons.triangle1East());
			headerPanel.addStyleName("bottomBorder");
		}
		else
		{
			checkListQuestionDisclosurePanel.setOpen(true);
			arrow.setResource(uiIcons.triangle1South());
			headerPanel.removeStyleName("bottomBorder");
			
		}
	}
//	@UiField
//	public VerticalPanel optionVerticalPanel;

	@UiField
	public FlowPanel optionVerticalPanel;
	
	@UiField
	public IconButton addOptionButton;
	
	@UiField
	public VerticalPanel addOptionVerticalPanel;
	
	@UiField
	public VerticalPanel addBtnPanel;
	
	@UiField
	public VerticalPanel editQuestionVP;
	
	
	public ChecklistQuestionProxy proxy;
	
	public CriteriaPopupView criteriaPopup;
	
	public CheckListTopicPopupView optionPopup;
	
	CriteriaPopupView questionPopup;
	
	//public CheckListTopicPopupView editquestionpopup;
	public CheckListQuestionPopupView editquestionpopup;
	
	public int checklistQuestionCount = 0;
	
	public int getChecklistQuestionCount() {
		return checklistQuestionCount;
	}

	public void setChecklistQuestionCount(int checklistQuestionCount) {
		this.checklistQuestionCount = checklistQuestionCount;
	}

	public ChecklistQuestionProxy getProxy() {
		return proxy;
	}

	public void setProxy(ChecklistQuestionProxy proxy) {
		this.proxy = proxy;
	}

	// Highlight onViolation
			Map<String, Widget> checklistQuestionMap;
			Map<String, Widget> checklistOptionMap;
			Map<String, Widget> checklistCriteriaMap;
	// E Highlight onViolation
			
	public RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
		
		optionLbl.setText(constants.roleOptionLabel());
		criteriaLbl.setText(constants.roleCriteriaLabel());
		
		questionView=this;
		
		optionDragController=new PickupDragController(optionBoundryPanel, false);
		optionDropController = new FlowPanelDropController(optionVerticalPanel);
		optionDragController.registerDropController(optionDropController);
		optionDragController.setBehaviorScrollIntoView(true);
		
		
		criteriaDragController = new PickupDragController(criteriaBoundryPanel, false);
		criteriaDropController = new FlowPanelDropController(criteriaHorizontalPanel);
		criteriaDragController.registerDropController(criteriaDropController);
		criteriaDragController.setBehaviorBoundaryPanelDrop(false);
		
		Log.info("View IMPL callled");
		
	}
	
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	interface Binder extends UiBinder<Widget, RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl> {
	}
	
	
	@UiHandler("addCriteriaButton")
	public void addCriteriaButtonClickHandler(ClickEvent event)
	{
		
		Log.info("addCriteriaButtonClickHandler");
		showCriteriaPopup();
		
		
	}
	
	public void showCriteriaPopup()
	{
		if(criteriaPopup==null)
		{
			criteriaPopup=new CriteriaPopupViewImpl();
		    //SPEC Change
		
			((CriteriaPopupViewImpl)criteriaPopup).setAnimationEnabled(true);
		
			
			((CriteriaPopupViewImpl)criteriaPopup).setWidth("100px");
	
		
			RootPanel.get().add(((CriteriaPopupViewImpl)criteriaPopup));


			
			criteriaPopup.getOkBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					//SPEC Change
					
					if(Validator.isNotNull(criteriaPopup.getCriteriaTxtBox().getValue()))
					{
						delegate.saveCriteria(criteriaPopup.getCriteriaTxtBox().getValue(),questionView);
					
						((CriteriaPopupViewImpl)criteriaPopup).hide(true);
				
						((CriteriaPopupViewImpl)criteriaPopup).criteriaTxtBox.setValue("");
					}
					else
					{
						confirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
						confirmationDialogBox.showConfirmationDialog(constants.warningFillRequiredFields());
					}
				}
			});
		} // SPEC Change
		((CriteriaPopupViewImpl)criteriaPopup).setPopupPosition(addCriteriaButton.getAbsoluteLeft()-50, addCriteriaButton.getAbsoluteTop()-85); // SPEC Change
		
		
		// Highlight onViolation			
		checklistCriteriaMap=new HashMap<String, Widget>();
		checklistCriteriaMap.put("criteria",criteriaPopup.getCriteriaTxtBox());
		checklistCriteriaMap.put("checklistQuestion",criteriaPopup.getCriteriaTxtBox());
		// E Highlight onViolation
		
		((CriteriaPopupViewImpl)criteriaPopup).show();
	}
	
	
	@UiHandler("addOptionButton")
	public void addOptionButtonClickHandler(ClickEvent event)
	{
		Log.info("addOptionButton");
		showOptionPopup();
	}
	
	public void showOptionPopup()
	{
		if(optionPopup==null)
		{
			optionPopup=new CheckListTopicPopupViewImpl(false);
			
			//SPEC Change
			((CheckListTopicPopupViewImpl)optionPopup).setAnimationEnabled(true);
		
			optionPopup.getDescriptionLbl().setText(constants.roleOptionValue());
			
			optionPopup.getTopicLbl().setText(constants.roleOptionName());
			
			((CheckListTopicPopupViewImpl)optionPopup).setWidth("160px");
		
		
			RootPanel.get().add(((CheckListTopicPopupViewImpl)optionPopup));

			
			
			
			optionPopup.getOkBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					//SPEC Change
					
					if(Validator.isNotNull(optionPopup.getTopicTxtBox().getValue(),optionPopup.getDescriptionTxtBox().getValue()))
					{
						if (!optionPopup.getDescriptionTxtBox().getValue().matches("[0-9]+"))
						{
							confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
							confirmationDialogBox.showConfirmationDialog(constants.checklistOptionErr());
						}
						else
						{
							//delegate.saveCheckListTopic(optionPopup.getTopicTxtBox().getValue(),optionPopup.getDescriptionTxtBox().getValue());
							delegate.saveOption(optionPopup.getTopicTxtBox().getValue(), optionPopup.getDescriptionTxtBox().getValue(), optionPopup.getOptionDescTextArea().getValue(), questionView);
							((CheckListTopicPopupViewImpl)optionPopup).hide(true);
					
							optionPopup.getTopicTxtBox().setValue("");
							optionPopup.getDescriptionTxtBox().setValue("");
						}
					}
					else
					{
						confirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
						confirmationDialogBox.showConfirmationDialog(constants.warningFillRequiredFields());
					}
				}
		});

		// Issue Role V1 
		optionPopup.getCancelBtn().addClickHandler(new ClickHandler() 
		{				
				@Override
				public void onClick(ClickEvent event) 
				{
					((CheckListTopicPopupViewImpl)optionPopup).hide(true);					
					optionPopup.getTopicTxtBox().setValue("");
					optionPopup.getDescriptionTxtBox().setValue("");
				}
		});
		// E: Issue Role V1
		
		
		} //SPEC Change
		
		((CheckListTopicPopupViewImpl)optionPopup).getCriteriaCountLstBox().setSelectedIndex(0);
		((CheckListTopicPopupViewImpl)optionPopup).setPopupPosition(addOptionButton.getAbsoluteLeft()-205, addOptionButton.getAbsoluteTop()-280); //SPEC Change+
		
		
		// Highlight onViolation
					checklistOptionMap=new HashMap<String, Widget>();
					checklistOptionMap.put("optionName", optionPopup.getTopicTxtBox());
					checklistOptionMap.put("name", optionPopup.getTopicTxtBox());
					checklistOptionMap.put("value", optionPopup.getDescriptionTxtBox());
					// E Highlight onViolation
		
		((CheckListTopicPopupViewImpl)optionPopup).show();
		
	}
	
	@UiHandler("delete")
	public void deleteQuestion(ClickEvent event)
	{
		// Issue Role
				 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
				 dialogBox.showYesNoDialog("are you sure you want to delete this Question?");
				 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							dialogBox.hide();
							
							Log.info("yes click");	
							delegate.deleteQuestion(questionView);
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
	public void editClicked(ClickEvent event)
	{
		Log.info("editClicked");
		showQuestionPopup();
	}
	
	public void showQuestionPopup()
	{
		if (editquestionpopup == null)
		{
			editquestionpopup = new CheckListQuestionPopupViewImpl();
			
			((CheckListQuestionPopupViewImpl)editquestionpopup).setWidth("350px");
			
			RootPanel.get().add(((CheckListQuestionPopupViewImpl)editquestionpopup));			
			
			editquestionpopup.getOkBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					if(Validator.isNotNull(editquestionpopup.getQuestionTextArea().getValue()))
					{	
						delegate.editOption(editquestionpopup.getQuestionTextArea().getValue(),editquestionpopup.getInstructionTextArea().getValue(),questionView);						
						((CheckListQuestionPopupViewImpl)editquestionpopup).hide(true);
					}
					else
					{	
						confirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
						confirmationDialogBox.showConfirmationDialog(constants.warningFillRequiredFields());
					}
				}
			});
			
			editquestionpopup.getCancelBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					((CheckListQuestionPopupViewImpl)editquestionpopup).hide(true);
				}
			});
		} 
		
		((CheckListQuestionPopupViewImpl)editquestionpopup).setPopupPosition(edit.getAbsoluteLeft()-350, edit.getAbsoluteTop()-235);
		
		checklistQuestionMap=new HashMap<String, Widget>();
		checklistQuestionMap.put("question", editquestionpopup.getQuestionTextArea());
		checklistQuestionMap.put("instruction", editquestionpopup.getInstructionTextArea());
		
		editquestionpopup.getQuestionTextArea().setText(proxy.getQuestion());	
		editquestionpopup.getInstructionTextArea().setText(proxy.getInstruction());
		editquestionpopup.getIsOverallQuestionChkBox().setChecked(proxy.getIsOveralQuestion());
		
		((CheckListQuestionPopupViewImpl)editquestionpopup).show();
	}
	
	/*public void showQuestionPopup()
	{
		Log.info("Call show Question Popup()");
		if(questionPopup==null)
		{
			questionPopup=new CriteriaPopupViewImpl();
		
		
			((CriteriaPopupViewImpl)questionPopup).setAnimationEnabled(true);
		
			
			((CriteriaPopupViewImpl)questionPopup).setWidth("100px");
	
		
			RootPanel.get().add(((CriteriaPopupViewImpl)questionPopup));
			
			questionPopup.getOkBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					if(questionPopup.getCriteriaTxtBox().getValue()=="")
					{
					}	
					else
					{
						//questionView.getProxy().setQuestion(questionPopup.getCriteriaTxtBox().getValue());
						delegate.editOption(questionPopup.getCriteriaTxtBox().getValue(),questionView);
						
						((CriteriaPopupViewImpl)questionPopup).hide(true);
				
						((CriteriaPopupViewImpl)questionPopup).criteriaTxtBox.setValue("");
					}
				}
			});
		}
		((CriteriaPopupViewImpl)questionPopup).setPopupPosition(editQuestionVP.getAbsoluteLeft(), editQuestionVP.getAbsoluteTop()-40);
		((CriteriaPopupViewImpl)questionPopup).show();
		questionPopup.getCriteriaTxtBox().setText(questionItemLbl.getText());
		questionPopup.getCriteriaTxtBox().selectAll();
		questionPopup.getCriteriaTxtBox().setFocus(true); 
		
		//spec india
		if(editquestionpopup==null)
		{
			editquestionpopup=new CheckListTopicPopupViewImpl(true);
			//SPEC Change
			
			((CheckListTopicPopupViewImpl)editquestionpopup).setAnimationEnabled(true);
		
			editquestionpopup.getDescriptionLbl().setText(constants.roleQuestionInstruction());
			
			editquestionpopup.getTopicLbl().setText(constants.roleQuestionName());
			
			editquestionpopup.getTopicTxtBox().setWidth("300px");
			editquestionpopup.getDescriptionTxtBox().setWidth("300px");
			
			((CheckListTopicPopupViewImpl)editquestionpopup).setWidth("350px");

		
			RootPanel.get().add(((CheckListTopicPopupViewImpl)editquestionpopup));
			
			
			
			editquestionpopup.getOkBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					// SPEC Change
					
					//if(Validator.isNotNull(editquestionpopup.getTopicTxtBox().getValue(),editquestionpopup.getDescriptionTxtBox().getValue()))
					if(Validator.isNotNull(editquestionpopup.getTopicTxtBox().getValue()))
					{	
						delegate.editOption(editquestionpopup.getTopicTxtBox().getValue(),editquestionpopup.getDescriptionTxtBox().getValue(),questionView);						
						//delegate.e
						//delegate.saveCheckListQuestion(editquestionpopup.getTopicTxtBox().getValue(),editquestionpopup.getDescriptionTxtBox().getValue(),topicView);
						((CheckListTopicPopupViewImpl)editquestionpopup).hide(true);
				
						//editquestionpopup.getTopicTxtBox().setValue("");
						//editquestionpopup.getDescriptionTxtBox().setValue("");
					}
					else
					{	
						confirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
						confirmationDialogBox.showConfirmationDialog(constants.warningFillRequiredFields());
					}
				}
		});
			
			// Issue Role
				editquestionpopup.getCancelBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					((CheckListTopicPopupViewImpl)editquestionpopup).hide(true);
				}
				});
			// E: Issue Role
		
		} // SPEC Change
		((CheckListTopicPopupViewImpl)editquestionpopup).setPopupPosition(edit.getAbsoluteLeft()-450, edit.getAbsoluteTop()-240); // SPEC Change+
		
		
		
		
		
		// Highlight onViolation
			checklistQuestionMap=new HashMap<String, Widget>();
			checklistQuestionMap.put("question", editquestionpopup.getTopicTxtBox());
			checklistQuestionMap.put("instruction", editquestionpopup.getDescriptionTxtBox());
		// E Highlight onViolation
		
		editquestionpopup.getTopicTxtBox().setText(proxy.getQuestion());	
		editquestionpopup.getDescriptionTxtBox().setText(proxy.getInstruction());
		editquestionpopup.getIsOverallQuestionChkBox().setChecked(proxy.getIsOveralQuestion());
		((CheckListTopicPopupViewImpl)editquestionpopup).show();
		
	}*/

	@Override
	public void setChecklistTopicProxy(ChecklistTopicProxy proxy) {
		this.topicProxy=proxy;
		
	}
	public ChecklistTopicProxy getChecklistTopicProxy()
	{
		return this.topicProxy;
	}

	@Override
	public void setRoleDetailsChecklistSubViewChecklistTopicItemView(
			RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView) {
		this.topicView=topicView;
//		System.out.println("topic title "+topicView);
		
		
	}
	public RoleDetailsChecklistSubViewChecklistTopicItemViewImpl getRoleDetailsChecklistSubViewChecklistTopicItemView(){
//		System.out.println("get topic title "+topicView);
		return this.topicView;
	}

	// Highlight onViolation
	
		@Override
		public Map getChecklistQuestionMap()
		{
			return this.checklistQuestionMap;
		}
		
		@Override
		public Map getChecklistOptionMap()
		{
			return this.checklistOptionMap;
		}
		
		@Override
		public Map getChecklistCriteriaMap()
		{
			return this.checklistCriteriaMap;
		}
		
		// E Highlight onViolation

}
