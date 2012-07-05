

package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.Arrays;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.managed.ui.RoleTopicProxyRenderer;
import ch.unibas.medizin.osce.client.style.widgets.FocusableValueListBox;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.TabPanelHelper;
import ch.unibas.medizin.osce.shared.RoleTypes;
import ch.unibas.medizin.osce.shared.StudyYears;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RoleEditViewImpl extends Composite implements RoleEditView, Editor<StandardizedRoleProxy> {

	private static final Binder BINDER = GWT.create(Binder.class);
	private static RoleEditView instance;
	private OsceConstants constants = GWT.create(OsceConstants.class);
	private StandardizedRoleProxy standardizedRoleProxy;
	
	private StandardizedRoleProxy proxy;
	
	public StandardizedRoleProxy getProxy() {
		return proxy;
	}
	public void setProxy(StandardizedRoleProxy proxy) {
		this.proxy = proxy;
	}

	private MajorMinorPopupPanelViewImpl popupPanel;
	
	
	@UiField
	
	public CheckBox active;
	
	
	@UiField
	SimplePanel roleEditCheckListPanel;//spec
	
	@UiField
	public IntegerBox subVersion;

	@UiField
	public IntegerBox mainVersion;

	private boolean minorClick=false;
	
	
	
	public StandardizedRoleProxy getStandardizedRoleProxy() {
		return standardizedRoleProxy;
	}
	public void setStandardizedRoleProxy(StandardizedRoleProxy standardizedRoleProxy) {
		this.standardizedRoleProxy = standardizedRoleProxy;
	}

	@UiField
	TabPanel rolePanel;
	
	@UiField
	public TabPanel roleDetailPanel;
	
	
	public TabPanel getRoleDetailPanel() {
		return roleDetailPanel;
	}
	public void setRoleDetailPanel(TabPanel roleDetailPanel) {
		this.roleDetailPanel = roleDetailPanel;
	}

	@UiField
	SpanElement title;
	
	
	// Fields
	@UiField
	public TextBox shortName;
	@UiField
	
	public TextBox longName;
	
	@UiField(provided = true)
	public FocusableValueListBox<RoleTypes> roleType = new FocusableValueListBox<RoleTypes>(new EnumRenderer<RoleTypes>());
	
/*	@UiField
	TextBox roleType;*/
/*	@UiField
	TextBox studyYear;*/
	@UiField(provided = true)
	public FocusableValueListBox<StudyYears> studyYear = new FocusableValueListBox<StudyYears>(new EnumRenderer<StudyYears>());

	
	//spec statrt
	@UiField
	public SpanElement labelRoleTopic;
		
	@UiField(provided = true)
	public FocusableValueListBox<RoleTopicProxy> roleTopic = new FocusableValueListBox<RoleTopicProxy>(new RoleTopicProxyRenderer());
	//spec end
	
	// Labels 
	@UiField
	SpanElement labelShortName;
	@UiField
	SpanElement labellongName;
	@UiField
	SpanElement labelroletype;
	@UiField
	SpanElement labelstudyYear;
	
	@UiField
	IconButton cancel;
	@UiField
	IconButton save;
	@UiField
	DivElement errors;
	
	private Delegate delegate;
	private Presenter presenter;
	
	public RoleEditViewImpl() 
	{
		
		
		
		initWidget(BINDER.createAndBindUi(this));
		
		
		
		
		studyYear.setAcceptableValues(Arrays.asList(StudyYears.values()));
		roleType.setAcceptableValues(Arrays.asList(RoleTypes.values()));
		TabPanelHelper.moveTabBarToBottom(rolePanel);
		cancel.setText(constants.cancel());
		save.setText(constants.save());
		rolePanel.getTabBar().setTabText(0, constants.roleDetail());
		//rolePanel.getTabBar().setTabText(1,  constants.roleParticipants());
		//rolePanel.getTabBar().setTabText(2, constants.keywords());
		//rolePanel.getTabBar().setTabText(3, constants.learningObjectives());	
		
		setTabTexts();
		setLabelTexts();
		
		rolePanel.selectTab(0);
		roleDetailPanel.selectTab(0);
		
		
		
		
	}
	private void setTabTexts() {
		rolePanel.getTabBar().setTabText(0, constants.roleDetail());
		//rolePanel.getTabBar().setTabText(1,  constants.roleParticipants());
		//rolePanel.getTabBar().setTabText(2, constants.keywords());
		//rolePanel.getTabBar().setTabText(3, constants.learningObjectives());	
	}
	private void setLabelTexts() {
		labelShortName.setInnerText(constants.shortName() + ":");
		labellongName.setInnerText(constants.name() + ":");
		labelroletype.setInnerText(constants.roleType() + ":");		
		labelstudyYear.setInnerText(constants.studyYear() + ":");
		labelRoleTopic.setInnerText(constants.roletopic());//spec
	}
	
	public void setCreating(boolean creating) {
		if (creating) {
			title.setInnerText(constants.addRole());
		} else {
			title.setInnerText(constants.editRole());
		}
	}
	
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	public void setEnabled(boolean enabled) {
		save.setEnabled(enabled);
	}
	//spec start
	
	@Override
	public SimplePanel getRoleEditCheckListPanel()
	{
		return roleEditCheckListPanel;
	}
	
	//spec end
	public void showErrors(List<EditorError> errors) {
		SafeHtmlBuilder b = new SafeHtmlBuilder();
		for (EditorError error : errors) {
			b.appendEscaped(error.getPath()).appendEscaped(": ");
			b.appendEscaped(error.getMessage()).appendHtmlConstant("<br>");
		}
		this.errors.setInnerHTML(b.toSafeHtml().asString());
	}
	
	@UiHandler("cancel")
	void onCancel(ClickEvent event) {
		Log.info("Click Cancel");
		delegate.cancelClicked();
	}

	@UiHandler("save")
	void onSave(ClickEvent event) {
		Log.info("Click Save");
		delegate.saveClicked();
	}
	interface Binder extends UiBinder<Widget, RoleEditViewImpl> {
	}
	@Override
	public void setEditTitle(boolean edit) {
		if (edit) {
			title.setInnerText(standardizedRoleProxy.getLongName());
		} else {
			title.setInnerText(constants.addRole());
		}
	}
	
	@Override
	public void setPresenter(Presenter standardizedRoleEditActivity) {
		this.presenter=standardizedRoleEditActivity;
	}
	
	@Override
	public RequestFactoryEditorDriver<StandardizedRoleProxy, RoleEditViewImpl> createEditorDriver() {
		RequestFactoryEditorDriver<StandardizedRoleProxy, RoleEditViewImpl> driver = GWT.create(Driver.class);
		driver.initialize(this);
		return driver;
	}
	
	interface Driver extends RequestFactoryEditorDriver<StandardizedRoleProxy, RoleEditViewImpl> {
	}
	
	
	
	
	
	
	@Override
	public int getMajorMinorChange() {
		showPopup();
		if(minorClick)
			return 0;
		else
			return 1;
	}
	
	public void showPopup()
	{
		Log.info("Major Minor Popup");
		
			
			//popupPanel=new PopupPanel();
			popupPanel=new MajorMinorPopupPanelViewImpl();
			popupPanel.setWidth("300px");
			//popupPanel.setHeight("350px");
			popupPanel.setAnimationEnabled(true);
			popupPanel.setPopupPosition(500, 300);
			popupPanel.setGlassEnabled(true);
			
			DecoratorPanel dp=new DecoratorPanel();
			
			VerticalPanel vp=new VerticalPanel();
			Label minorLbl=new Label("Minor Change ?");
			Label majorLbl=new Label("Major Change ?");
			
			
			Button minorBtn=new Button("Minor Change");
			Button majorBtn=new Button("Major Change");
			
			minorBtn.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
				if(standardizedRoleProxy.getSubVersion()==null)
				{
					subVersion.setValue(1,true);
				}
				else
				{
					subVersion.setValue(standardizedRoleProxy.getSubVersion()+1,true);
				}
					
					//subVersion.setText(new Integer(standardizedRoleProxy.getSubVersion()+1).toString());
				//	standardizedRoleProxy.setSubVersion(standardizedRoleProxy.getSubVersion()+1);
					minorClick=true;
					popupPanel.hide();
					
					
				}
			});
			
			majorBtn.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					
					mainVersion.addValueChangeHandler(new ValueChangeHandler<Integer>() {
						
						@Override
						public void onValueChange(ValueChangeEvent<Integer> event) {
							delegate.saveMajor();
							
						}
					});
					//mainVersion.setValue(standardizedRoleProxy.getMainVersion()+1,true);
					delegate.saveMajor();
					minorClick=false;
					popupPanel.hide();
					
					
					
					
				}
			});
			
			
			//minor.add(minorLbl);
			popupPanel.minor.add(minorBtn);
			
			
			
			
			//major.add(majorLbl);
			popupPanel.major.add(majorBtn);
			popupPanel.minor.setSpacing(20);
			popupPanel.major.setSpacing(20);
			
			//dp.setSize("300px", "100px");
			
			//dp.add(minor);
			
			
			//vp.add(dp);
			//vp.add(major);
			
			//vp.setSpacing(35);
			
			//popupPanel.add(vp);
			
			//RootPanel.get().add(popupPanel);
			
			popupPanel.show();
			
		
	}
	
	
	@UiHandler("subVersion")
	public void ValueChanged(ValueChangeEvent<Integer> event)
	{
		delegate.save();
	}
	
	

	@Override
	public void setRoleTopicListBoxValues(List<RoleTopicProxy> values) {
		boolean areValuesValid = (values != null) && (values.size() > 0);
		
		if (!areValuesValid) {
			//hideAddBoxes();
			return;
		}
	
		System.out.println("roletopic set");
		roleTopic.setAcceptableValues(values);
	}
	
	/*@UiHandler("mainVersion")
	public void ValueChangedMainVersion(ValueChangeEvent<Integer> event)
	{
		delegate.saveMajor();
	}

	*/

}
