package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;



import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl extends Composite implements RoleDetailsChecklistSubViewChecklistQuestionItemView {
	
	private static final Binder BINDER = GWT.create(Binder.class);
	
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
	
	
	public RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView;
	
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
	public VerticalPanel optionVerticalPanel;
	
	@UiField
	public IconButton addOptionButton;
	
	@UiField
	public VerticalPanel addOptionVerticalPanel;
	
	@UiField
	public VerticalPanel addBtnPanel;
	
	@UiField
	public VerticalPanel editQuestionVP;
	
	
	public ChecklistQuestionProxy proxy;
	
	CriteriaPopupView criteriaPopup;
	
	CheckListTopicPopupView optionPopup;
	
	CriteriaPopupView questionPopup;
	
	public ChecklistQuestionProxy getProxy() {
		return proxy;
	}

	public void setProxy(ChecklistQuestionProxy proxy) {
		this.proxy = proxy;
	}

	public RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
		questionView=this;
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
		
		
			((CriteriaPopupViewImpl)criteriaPopup).setAnimationEnabled(true);
		
			
			((CriteriaPopupViewImpl)criteriaPopup).setWidth("100px");
	
		
			RootPanel.get().add(((CriteriaPopupViewImpl)criteriaPopup));
			
			criteriaPopup.getOkBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					if(criteriaPopup.getCriteriaTxtBox().getValue()=="")
					{
					}	
					else
					{
						delegate.saveCriteria(criteriaPopup.getCriteriaTxtBox().getValue(),questionView);
					
						((CriteriaPopupViewImpl)criteriaPopup).hide(true);
				
						((CriteriaPopupViewImpl)criteriaPopup).criteriaTxtBox.setValue("");
					}
				}
			});
		}
		((CriteriaPopupViewImpl)criteriaPopup).setPopupPosition(addBtnPanel.getAbsoluteLeft(), addBtnPanel.getAbsoluteTop()-40);
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
			optionPopup=new CheckListTopicPopupViewImpl();
			
			
			((CheckListTopicPopupViewImpl)optionPopup).setAnimationEnabled(true);
		
			optionPopup.getDescriptionLbl().setText(constants.optionValue());
			
			optionPopup.getTopicLbl().setText(constants.optionName());
			
			((CheckListTopicPopupViewImpl)optionPopup).setWidth("160px");

		
			RootPanel.get().add(((CheckListTopicPopupViewImpl)optionPopup));
			
			optionPopup.getOkBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					if(optionPopup.getTopicTxtBox().getValue()=="" || optionPopup.getDescriptionTxtBox().getValue()=="")
					{
					}	
					else
					{
						//delegate.saveCheckListTopic(optionPopup.getTopicTxtBox().getValue(),optionPopup.getDescriptionTxtBox().getValue());
						delegate.saveOption(optionPopup.getTopicTxtBox().getValue(), optionPopup.getDescriptionTxtBox().getValue(),questionView);
						((CheckListTopicPopupViewImpl)optionPopup).hide(true);
				
						optionPopup.getTopicTxtBox().setValue("");
						optionPopup.getDescriptionTxtBox().setValue("");
					}
				}
		});
		}
		
		((CheckListTopicPopupViewImpl)optionPopup).setPopupPosition(addOptionVerticalPanel.getAbsoluteLeft(), addOptionVerticalPanel.getAbsoluteTop()-180);
		((CheckListTopicPopupViewImpl)optionPopup).show();
		
	}
	
	@UiHandler("delete")
	public void deleteQuestion(ClickEvent event)
	{
		if(Window.confirm("are you sure you want to delete this Question?"))
			delegate.deleteQuestion(this);
	}
	
	@UiHandler("edit")
	public void editClicked(ClickEvent event)
	{
		Log.info("editClicked");
		showQuestionPopup();
	}
	
	public void showQuestionPopup()
	{
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
	}

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
}
