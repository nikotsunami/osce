

package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EnumType;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.widgets.FocusableValueListBox;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.TabPanelHelper;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import ch.unibas.medizin.osce.shared.BucketInfoType;
import ch.unibas.medizin.osce.shared.RoleTopicFactor;
import ch.unibas.medizin.osce.shared.RoleTypes;
import ch.unibas.medizin.osce.shared.StudyYears;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

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
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
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
	
	// Highlight onViolation
		Map<String, Widget> standardizedRoleMap;
	// E Highlight onViolation
	
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
	
	@UiField(provided = true)
	public ListBox sum=new ListBox();
	
	@UiField(provided = true)
	public ListBox factor=new ListBox();
	
	@UiField(provided = true)
	public FocusableValueListBox<RoleTopicFactor> topicFactor = new FocusableValueListBox<RoleTopicFactor>(new EnumRenderer<RoleTopicFactor>(EnumRenderer.Type.ROLETOPICFACTOR));
	
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
	
	@UiField
	public SpanElement labelActive;
	
	@UiField
	public SpanElement labelSum;
	
	@UiField
	public SpanElement labelFactor;
		
	//Issue # 122 : Replace pull down with autocomplete.	
	@UiField
	public DefaultSuggestBox<RoleTopicProxy, EventHandlingValueHolderItem<RoleTopicProxy>> roleTopic;
	//
	/*@UiField(provided = true)
	public FocusableValueListBox<RoleTopicProxy> roleTopic = new FocusableValueListBox<RoleTopicProxy>(RoleTopicProxyRenderer.instance());
	*///spec end
	//Issue # 122 : Replace pull down with autocomplete.
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
	SpanElement	topicFactorLbl;
	
	
	
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
		//rolePanel.getTabBar().setTabText(1,  constants.roleParticipants());
		//rolePanel.getTabBar().setTabText(2, constants.keywords());
		//rolePanel.getTabBar().setTabText(3, constants.learningObjectives());	
		
		setTabTexts();
		setLabelTexts();
		
		
		rolePanel.selectTab(0);
		roleDetailPanel.selectTab(0);
		topicFactor.setValue(RoleTopicFactor.WEIGHT);
		topicFactor.setAcceptableValues(Arrays.asList(RoleTopicFactor.values()));
		// Highlight onViolation
		for(int i=1;i<=5;i++)
		{
			factor.addItem(i+"");
		}
	//	factor.setSelectedIndex(0);

		for(int i=0;i<=10;i++)
		{
			sum.addItem(i+"");
		}
	//	sum.setSelectedIndex(0);

		
		standardizedRoleMap=new HashMap<String, Widget>();
		standardizedRoleMap.put("shortName", shortName);
		standardizedRoleMap.put("longName", longName);
		standardizedRoleMap.put("roleType", roleType);
		standardizedRoleMap.put("studyYear", studyYear);
		standardizedRoleMap.put("active", active);
		standardizedRoleMap.put("factor", factor);
		standardizedRoleMap.put("sum", sum);
		standardizedRoleMap.put("topicFactor", topicFactor);
		
		// E Highlight onViolation
		
		
	}
	private void setTabTexts() {
		rolePanel.getTabBar().setTabText(0, constants.roleDetail());
		rolePanel.getTabBar().setTabText(1,  constants.checkList());
		//rolePanel.getTabBar().setTabText(2, constants.keywords());
		//rolePanel.getTabBar().setTabText(3, constants.learningObjectives());	
	}
	private void setLabelTexts() {
		labelShortName.setInnerText(constants.roleAcronym() + ":");
		labellongName.setInnerText(constants.roleName() + ":");
		labelroletype.setInnerText(constants.roleType() + ":");		
		labelstudyYear.setInnerText(constants.studyYears() + ":");
		labelRoleTopic.setInnerText(constants.roleTopic());//spec
		labelActive.setInnerText(constants.roleActive());
		labelSum.setInnerText(constants.sum());
		labelFactor.setInnerText(constants.factor());
		topicFactorLbl.setInnerText(constants.topicFactor());
	}
	
	public void setCreating(boolean creating) {
		if (creating) {
			roleDetailPanel.getTabBar().setTabText(0, constants.newRole());
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
			
			IconButton minorBtn = new IconButton(constants.minorLbl());
			minorBtn.setIcon("battery-1");
			minorBtn.setWidth("100%");
			IconButton majorBtn = new IconButton(constants.majorLbl());
			majorBtn.setIcon("battery-3");
			majorBtn.setWidth("100%");
			
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
			//enable for major save
			popupPanel.major.add(majorBtn);
			/*
			if(standardizedRoleProxy.getOscePosts().size() > 0)
				majorBtn.setEnabled(false);
			else
				majorBtn.setEnabled(true);*/
			
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
		//Issue # 122 : Replace pull down with autocomplete.
		//roleTopic.setAcceptableValues(values);
		
		DefaultSuggestOracle<RoleTopicProxy> suggestOracle1 = (DefaultSuggestOracle<RoleTopicProxy>) roleTopic.getSuggestOracle();
		suggestOracle1.setPossiblilities(values);
		roleTopic.setSuggestOracle(suggestOracle1);
		//roleTopic.setRenderer(new RoleTopicProxyRenderer());
			
		roleTopic.setRenderer(new AbstractRenderer<RoleTopicProxy>() {

			@Override
			public String render(RoleTopicProxy object) {
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
		//Issue # 122 : Replace pull down with autocomplete.
	}
	
	// Highlight onViolation
	@Override
	public Map getStandardizedRoleMap()
	{
		return this.standardizedRoleMap;
	}
	// E Highlight onViolation
	
	/*@UiHandler("mainVersion")
	public void ValueChangedMainVersion(ValueChangeEvent<Integer> event)
	{
		delegate.saveMajor();
	}

	*/

}
