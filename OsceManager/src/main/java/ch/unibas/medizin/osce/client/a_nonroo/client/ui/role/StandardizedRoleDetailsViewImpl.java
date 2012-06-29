/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.ArrayList;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandartizedPatientAdvancedSearchSubViewImpl;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.RoleTemplateProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.TabPanelHelper;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
/**
 * @author niko2
 * 
 */
public class StandardizedRoleDetailsViewImpl extends Composite implements
		StandardizedRoleDetailsView {

	private static StandardizedRoleDetailsViewImplUiBinder uiBinder = GWT
			.create(StandardizedRoleDetailsViewImplUiBinder.class);

	interface StandardizedRoleDetailsViewImplUiBinder extends
			UiBinder<Widget, StandardizedRoleDetailsViewImpl> {

	}

	private OsceConstants constants = GWT.create(OsceConstants.class);

	StandardizedRoleProxy proxy;
	StandardizedRoleProxy baseProxy;


	@UiField
	DisclosurePanel roleDisclosurePanel;
	@UiField
	Image arrow;
	// SPEC End
	
ImportTopicPopupView importTopicView;
	
	//Assignment E[
	
	public ArrayList<RoleDetailsChecklistSubViewChecklistTopicItemView> checkListTopicView = new ArrayList<RoleDetailsChecklistSubViewChecklistTopicItemView>();
	
	@UiField
	public VerticalPanel checkListsVerticalPanel;
	
	@UiField
	public HorizontalPanel addTopicHP;
	
	@UiField
	public Button addCheckListTopicButton;
	
	public CheckListTopicPopupView topicPopup;
	
	public StandardizedRoleDetailsViewImpl studentRoleView;
	
	@UiField
	Button importTopicButton;
	
	public Button getImportTopicButton() {
		return importTopicButton;
	}

	public void setImportTopicButton(Button importTopicButton) {
		this.importTopicButton = importTopicButton;
	}
	//Assignment E]

	// SPEC START =
		@UiField
		RoleRoleParticipantSubViewImpl roleRoleParticipantSubViewImpl; 
		
		@UiField
		RoleKeywordSubViewImpl roleKeywordSubViewImpl; 
		// SPEC END =
	
	// Panels

	@UiField
	public TabPanel rolePanel;

	// Buttons
	@UiField
	IconButton print;
	@UiField
	public IconButton edit;
	@UiField
	public IconButton delete;

	@UiField
	public IconButton previous;
	
	@UiField
	public IconButton home;
	

	// Labels (Fieldnames)

	@UiField
	SpanElement labelShortName;
	@UiField
	SpanElement labellongName;
	@UiField
	SpanElement labelroletype;
	@UiField
	SpanElement labelstudyYear;

	// Temp Fields

	/*
	 * @UiField SpanElement labelShortNameValue;
	 * 
	 * @UiField SpanElement labellongValue;
	 * 
	 * @UiField SpanElement labelroletypeValue;
	 * 
	 * @UiField SpanElement labelstudyYearValue;
	 */
	@UiField
	public com.google.gwt.user.client.ui.Label labelLongNameHeader;

	
	
	// Fields
	@UiField
	public SpanElement shortName;
	@UiField
	public SpanElement longName;
	// @UiField(provided = true)
	// public FocusableValueListBox<RoleTypes> roleType = new
	// FocusableValueListBox<RoleTypes>(new EnumRenderer<RoleTypes>());

	@UiField
	public SpanElement roleType;

	@UiField
	public SpanElement studyYear;

	@UiField
	public TabPanel roleSubPanel;
	/*
	 * @UiField(provided = true) public FocusableValueListBox<StudyYears>
	 * studyYear = new FocusableValueListBox<StudyYears>(new
	 * EnumRenderer<StudyYears>());
	 */

	// Assignment H[
	@UiField
	RoleFileSubViewImpl roleFileSubViewImpl;

	public RoleFileSubViewImpl getRoleFileSubViewImpl() {
		return roleFileSubViewImpl;
	}

	// Assignment G[
	@UiField
	RoomMaterialsDetailsSubViewImpl roomMaterialsDetailsSubViewImpl;

	@Override
	public RoomMaterialsDetailsSubViewImpl getRoomMaterialsDetailsSubViewImpl() {

		return roomMaterialsDetailsSubViewImpl;
	}

	// ]End
	
	//Assignment I
//	@UiField
//	RoleBaseTableItemViewImpl roleBaseTableItemViewImpl;
//	
//	@Override
//	public RoleBaseTableItemViewImpl getroleBaseTableItemViewImpl(){
//		return roleBaseTableItemViewImpl;
//	}
	
	@UiField(provided = true)
	public ValueListBox<RoleTemplateProxy> roleTemplateListBox = new ValueListBox<RoleTemplateProxy>(new AbstractRenderer<RoleTemplateProxy>() {

		@Override
		public String render(RoleTemplateProxy object) {

			 return object == null ? "" : String.valueOf(object.getTemplateName());
		}
		
	}); 
	@UiField
	public IconButton roleTemplateValueButon;
	
	@Override
	public void setRoleTemplateListBox(List<RoleTemplateProxy> roleTemplateproxy) {
		roleTemplateListBox.setAcceptableValues(roleTemplateproxy);
		
	}

	@UiHandler("roleTemplateValueButon")
	public void roleTemplateValueSelction(ClickEvent event)
	{	
		//System.out.println(roleTemplateListBox.getValue().getTemplateName());
		// Issue Role
		if(roleTemplateListBox.getValue()==null)
		{
			// Issue Role
			 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox("Select Role Template");
			 dialogBox.showConfirmationDialog();
			 
			 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					dialogBox.hide();							
					Log.info("ok click");	
					return;
						}
					});

		
//E: Issue Role	
		}
		else
		{
		delegate.roleTemplateValueButtonClicked(roleTemplateListBox.getValue());
		}
		// E: Issue Role
	}
	
	@UiField
	VerticalPanel roleBaseItemPanel;
	
	@Override
	public VerticalPanel getRoleBaseItemVerticalPanel(){
		return roleBaseItemPanel;
	}
	
	// END I

	// AssignmentF[
	@Override
	public TabPanel getRoleSubPanel() {
		return roleSubPanel;
	}
	@UiField
	public StandartizedPatientAdvancedSearchSubViewImpl standartizedPatientAdvancedSearchSubViewImpl;

	public StandartizedPatientAdvancedSearchSubViewImpl getStandartizedPatientAdvancedSearchSubViewImpl() {
		return standartizedPatientAdvancedSearchSubViewImpl;
	}

	@UiField
	RoleOtherSearchCriteriaViewImpl roleOtherSearchCriteriaViewImpl;

	public RoleOtherSearchCriteriaViewImpl getRoleOtherSearchCriteriaViewImpl() {
		return roleOtherSearchCriteriaViewImpl;
	}

	// ]AssignmentF
	private Delegate delegate;

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */

	public StandardizedRoleDetailsViewImpl() {

		initWidget(uiBinder.createAndBindUi(this));
		// rolePanel.selectTab(0);
		roleDisclosurePanel.setOpen(true);

		TabPanelHelper.moveTabBarToBottom(rolePanel);

		print.setText(constants.print());
		edit.setText(constants.edit());
		delete.setText(constants.delete());

		roleDisclosurePanel.setContent(rolePanel);
		roleDisclosurePanel.setStyleName("");

		setLabelTexts();

		//Assignment E start[
		roleSubPanel.selectTab(0);
		addCheckListTopicButton.setText(constants.addCheckListTopic());
		studentRoleView=this;
		importTopicButton.setText(constants.importTopic());
		//Assignment E start]
		
		roleTemplateValueButon.setText("Select Role Template");
	}

	private void setLabelTexts() {
		labelLongNameHeader.setText("");
		labelShortName.setInnerText(constants.shortName() + ":");
		labellongName.setInnerText(constants.name() + ":");
		labelroletype.setInnerText(constants.roleType() + ":");
		labelstudyYear.setInnerText(constants.studyYear() + ":");
	}

	@UiHandler("previous")
	public void onPreviousClick(ClickEvent e) {
		System.out.println("============================Click previous Button=========================");
		System.out.println("============================Call delegate.previousClicked=========================");
		
			
		delegate.previousRoleClicked(this.getValue());
		
	}
	
	

	@UiHandler("home")
	public void onHomeClick(ClickEvent e) {
		System.out.println("============================Click Actual Button=========================");
		System.out.println("============================Call delegate.actualClicked=========================");
		
			
		delegate.actualRoleClicked(this.getBaseValue());
		
	}
	
	@Override
	public void setValue(StandardizedRoleProxy proxy) {
		/*
		 * this.proxy = proxy; shortName.setInnerText(proxy.getShortName() ==
		 * null ? "" : String.valueOf(proxy.getShortName()));
		 * longName.setInnerText(proxy.getLongName() == null ? "" :
		 * String.valueOf(proxy.getLongName()));
		 * roleType.setInnerText(proxy.getRoleType() == null ? "" :
		 * String.valueOf(proxy.getRoleType()));
		 * studyYear.setInnerText(proxy.getStudyYear() == null ? "" :
		 * String.valueOf(proxy.getStudyYear()));
		 */
		System.out.println("new value set");
		this.proxy = proxy;
	}

	@Override
	public void setBaseProxy(StandardizedRoleProxy proxy)
	{
		baseProxy=proxy;
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	public Widget asWidget() {
		return this;
	}

	@UiHandler("print")
	public void onPrintClicked(ClickEvent e) {
		// delegate.printRoleClicked();
	}

	@UiHandler("delete")
	public void onDeleteClicked(ClickEvent e) {
		delegate.deleteRoleClicked(this.getValue());
	}

	@UiHandler("edit")
	public void onEditClicked(ClickEvent e) {
		System.out
				.println("============================Click Edit Button=========================");
		System.out
				.println("============================Call delegate.editPatientClciked=========================");
		delegate.editRoleClicked(this.getValue());
	}

	@UiHandler("arrow")
	void handleClick(ClickEvent e) {
		if (roleDisclosurePanel.isOpen()) {
			roleDisclosurePanel.setOpen(false);
			arrow.setUrl("/osMaEntry/gwt/unibas/images/arrowdownselect.png");// set
																				// url
																				// of
																				// up
																				// image

		} else {
			roleDisclosurePanel.setOpen(true);
			arrow.setUrl("/osMaEntry/gwt/unibas/images/arrowdownselect.png");// set
																				// url
																				// of
																				// down
																				// image
		}

	}

	/*
	 * @UiHandler("roleDetailPanel") public void
	 * roleDetailPanelClicked(SelectionEvent<Integer> click) {
	 * if(roleDetailPanel
	 * .getTabBar().getSelectedTab()==(roleDetailPanel.getWidgetCount()-1)) {
	 * Log.info("roleDetailPanel plus clicked"); delegate.createRole(); } else {
	 * Log.info("roleDetailPanel clicked"); } }
	 */

	public StandardizedRoleProxy getValue() {
		return proxy;
	}

	public StandardizedRoleProxy getBaseValue() {
		return baseProxy;
	}

	// SPEC START =

	@Override
	public RoleRoleParticipantSubViewImpl getRoleRoleParticipantSubViewImpl() 
	{	
		return roleRoleParticipantSubViewImpl;
	}

	@Override
	public RoleKeywordSubViewImpl getRoleKeywordSubViewImpl() 
	{	
		return roleKeywordSubViewImpl;
	}
	// SPEC END =
	//Assignment E[
	@UiHandler("addCheckListTopicButton")
	public void addCheckListTopicButtonClickHandler(ClickEvent event)
	{
		showTopicPopupPanel();
		
		//if( addCheckListTopicTxtBox.getValue() != "")
		//delegate.saveCheckListTopic(addCheckListTopicTxtBox.getValue());
	}

	public void showTopicPopupPanel()
	{
		if(topicPopup==null)
		{
			topicPopup=new CheckListTopicPopupViewImpl();
			
			
			((CheckListTopicPopupViewImpl)topicPopup).setAnimationEnabled(true);
		
			topicPopup.getDescriptionLbl().setText(constants.topicDescription());
			
			topicPopup.getTopicLbl().setText(constants.checklistTopic());
			
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
						delegate.saveCheckListTopic(topicPopup.getTopicTxtBox().getValue(),topicPopup.getDescriptionTxtBox().getValue());
					
						((CheckListTopicPopupViewImpl)topicPopup).hide(true);
				
						topicPopup.getTopicTxtBox().setValue("");
						topicPopup.getDescriptionTxtBox().setValue("");
					}
				}
		});
			// Issue Role
			topicPopup.getCancelBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) 
				{
					((CheckListTopicPopupViewImpl)topicPopup).hide(true);
					topicPopup.getTopicTxtBox().setValue("");
					topicPopup.getDescriptionTxtBox().setValue("");
				}
			});
			// E: Issue Role
		}
			
		
		
		((CheckListTopicPopupViewImpl)topicPopup).setPopupPosition(addTopicHP.getAbsoluteLeft(), addTopicHP.getAbsoluteTop()-180);
		((CheckListTopicPopupViewImpl)topicPopup).show();
		
	}

		@UiHandler("importTopicButton")
		public void importTopicButtonClickHandler(ClickEvent event)
		{
			Log.info("importTopicButtonClickHandler");
			showImportTopicView();
			
		}
		
		public void showImportTopicView()
		{
//			if(importTopicView==null)
			{
				importTopicView=new ImportTopicPopupViewImpl(false,null);
				((ImportTopicPopupViewImpl)importTopicView).setAnimationEnabled(true);
				
				delegate.setRoleListBoxValue(importTopicView);
				
				((ImportTopicPopupViewImpl)importTopicView).setWidth("150px");			
				RootPanel.get().add(((ImportTopicPopupViewImpl)importTopicView));
				
				importTopicView.getOkBtn().addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) 
					{
						{
							((ImportTopicPopupViewImpl)importTopicView).hide(true);
						}
					}
				});
				// Issue Role
					importTopicView.getCancelBtn().addClickHandler(new ClickHandler() 
					{
						@Override
					public void onClick(ClickEvent event) 
						{
					((ImportTopicPopupViewImpl)importTopicView).hide(true);
						}
					});
				// E: Issue Role
				
		}
			((ImportTopicPopupViewImpl)importTopicView).setPopupPosition(addTopicHP.getAbsoluteLeft(), addTopicHP.getAbsoluteTop()-180);
			((ImportTopicPopupViewImpl)importTopicView).show();
		}
	//Assignment E]
	
}
