package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import java.util.HashMap;
import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.RoomProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class OscePostSubViewImpl extends Composite implements OscePostSubView {
	
	private Delegate delegate;
	
	private static OscePostSubViewImplUiBinder uiBinder = GWT
			.create(OscePostSubViewImplUiBinder.class);

	interface OscePostSubViewImplUiBinder extends UiBinder<Widget, OscePostSubViewImpl> {
	}
	
	// Highlight onViolation
		public Map<String, Widget> oscePostBluePrintMap;
	// E Highlight onViolation
	
	// 5C: SPEC START =
	public ListBoxPopupViewImpl listBoxPopupViewImpl;
	public OscePostBlueprintProxy oscePostBlueprintProxy;
	
	public OscePostBlueprintProxy oscePostBlueprintProxyNext;
	OscePostSubViewImpl oscePostSubViewImpl;
	
	@UiField
	HorizontalPanel specializationHP;
	
	@UiField
	HorizontalPanel roleTopicHP;
	// 5C: SPEC END =
	
	public OscePostProxy oscePostProxy;
	
	// Module 5 and TTG Bug Changes
	CourseProxy courseProxy;
	// E Module 5 and TTG Bug Changes
	
	public OscePostProxy getOscePostProxy() {
		return oscePostProxy;
	}

	public void setOscePostProxy(OscePostProxy oscePostProxy) {
		this.oscePostProxy = oscePostProxy;
	}

	@UiField
	Label specializationLbl;
	
	public Label getSpecializationLbl() {
		return specializationLbl;
	}

	public Label getPostNameLbl() {
		return postNameLbl;
	}

	public Label getRoleTopicLbl() {
		return roleTopicLbl;
	}

	public Label getRoomLbl() {
		return roomLbl;
	}

	//Module 5 Bug Report Solution
	@Override	
	public IconButton getSpecializationedit() {
	//E Module 5 Bug Report Solution
		return specializationedit;
	}

	// Module 5 bug Report Change
	@Override
	public IconButton getRoomedit() {
	//E Module 5 bug Report Change
		return roomedit;
	}
	//Module 5 Bug Report Solution
	@Override
	public IconButton getRoleTopicEdit() {
	//E Module 5 Bug Report Solution
		return roleTopicedit;
	}
	//Module 5 Bug Report Solution
	@Override
	public IconButton getStandardizedRoleEdit() {
	//Module 5 Bug Report Solution
		return standardizedRoleedit;
	}
	// E Module 5 bug Report Change
	

	@UiField
	public Label postNameLbl;
	
	@UiField
	Label roleTopicLbl;
	
	@UiField
	Label standardizedRoleLbl;
	
	public Label getStandardizedRoleLbl() {
		return standardizedRoleLbl;
	}

	@UiField
	Label roomLbl;
	
	@UiField
	IconButton specializationedit;
	
	@UiField
	IconButton roleTopicedit;
	
	@UiField
	IconButton standardizedRoleedit;
	
	@UiField
	IconButton roomedit;
	
	@UiField
	HorizontalPanel standardizedRoleHP;
	
	
	
	@Override
	public void setDelegate(Delegate delegate) {

		this.delegate=delegate;
		
	}
	
	public OscePostSubViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		oscePostSubViewImpl=this;
		
		// Highlight onViolation
		oscePostBluePrintMap=new HashMap<String, Widget>();
		oscePostBluePrintMap.put("roleTopic", roleTopicLbl);
		oscePostBluePrintMap.put("specialisation", specializationLbl);				
		// E Highlight onViolation
		
	}
	
	public void enableDisableforGeneratedStatus()
	{
		specializationedit.setVisible(false);
		roleTopicedit.setVisible(false);			
		// Module 5 and TTG Bug Changes
		//roomedit.setVisible(false);		
		// E Module 5 and TTG Bug Changes
	}
	
	public ListBoxPopupView popupView;
	
	@UiHandler("standardizedRoleedit")
	public void standardizedRoleeditClicked(ClickEvent event)
	{
		//delegate.findStandradizedRole
		delegate.findStandardizedRoles(this);
	}
	
	public void showPopUpView()
	{
		// Change in ParcourView
		//((ListBoxPopupViewImpl)popupView).setPopupPosition(standardizedRoleHP.getAbsoluteLeft()-40, standardizedRoleHP.getAbsoluteTop()-80);
		((ListBoxPopupViewImpl)popupView).setPopupPosition(standardizedRoleHP.getAbsoluteLeft()-209, standardizedRoleHP.getAbsoluteTop()-48);
		// E Change in ParcourView
		((ListBoxPopupViewImpl)popupView).show();
	}
	public void createOptionPopup()
	{
		if(popupView==null)
		{
			popupView=new ListBoxPopupViewImpl();
			listBoxPopupViewImpl=(ListBoxPopupViewImpl)popupView;
			((ListBoxPopupViewImpl)popupView).setAnimationEnabled(true);
			
			
			
			((ListBoxPopupViewImpl)popupView).setWidth("160px");

		
			RootPanel.get().add(((ListBoxPopupViewImpl)popupView));
			
			popupView.getOkBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					//Issue # 122 : Replace pull down with autocomplete.
					//EntityProxy object=((ListBoxPopupViewImpl)popupView).listBox.getValue();
					Object object=((ListBoxPopupViewImpl)popupView).listBox.getSelected();
					//Issue # 122 : Replace pull down with autocomplete.
					Log.info("okBtnClicked  :" + object);
					
					if(object instanceof SpecialisationProxy)
					{
						Log.info("Specialisation Proxy");						
						delegate.saveSpecialisation(oscePostSubViewImpl);
					}
					else if(object instanceof RoleTopicProxy)
					{
						Log.info("Role Topic Proxy");
						delegate.saveRoleTopic(oscePostSubViewImpl);
					}
					// Module 5 and TTG Bug Changes
					else if(object instanceof RoomProxy)
					{
						Log.info("OscePostRoomProxy");
						delegate.saveOscePostRoom(oscePostSubViewImpl,((ListBoxPopupViewImpl)popupView));
					}
					// E Module 5 and TTG Bug Changes					
				}
			});
		}
		
		
		
	}
	
	public void enableDisableforBluePrintStatus()
	{		
		standardizedRoleLbl.setVisible(false);
		roomLbl.setVisible(false);
		standardizedRoleedit.setVisible(false);
		roomedit.setVisible(false);
	}
	
	// 5C: SPEC START
	
		@UiHandler("specializationedit")
		public void specializationeditClicked(ClickEvent event)
		{
			Log.info("~Specialization Edit Clicked.");
			/*showOptionPopup();		
			((ListBoxPopupViewImpl)popupView).setPopupPosition(specializationHP.getAbsoluteLeft()-40, specializationHP.getAbsoluteTop()-80);
			((ListBoxPopupViewImpl)popupView).show();	*/	
			delegate.specializationEditClicked(this);
		}
		
		@UiHandler("roleTopicedit")
		public void roleTopiceditClicked(ClickEvent event)
		{
			Log.info("~Role Topic Edit Clicked.");
			/*createOptionPopup();		
			((ListBoxPopupViewImpl)popupView).setPopupPosition(roleTopicHP.getAbsoluteLeft()-40, roleTopicHP.getAbsoluteTop()-80);
			((ListBoxPopupViewImpl)popupView).show();*/
			//Log.info("Proxy:" + this.oscePostBlueprintProxy.getId());
			delegate.roleEditClicked(this);
		}
		// Module 5 and TTG Bug Changes
		@UiHandler("roomedit")
		public void roomEditClicked(ClickEvent event)
		{
			Log.info("~Room Edit Clicked.");			
			delegate.roomEditClicked(this,event.getClientX(),event.getClientY());
		}
		// E Module 5 and TTG Bug Changes
		
		public ListBoxPopupViewImpl getListBoxPopupViewImpl()
		{
			Log.info("~getListBoxPopupViewImpl() call");
			return (ListBoxPopupViewImpl)popupView;
		}
		
		public HorizontalPanel getSpecializationHP()
		{
			return this.specializationHP;
		}
		
		public HorizontalPanel getRoleTopicHP()
		{
			return this.roleTopicHP;
		}
		
		// Module 5 and TTG Bug Changes
		public CourseProxy getCourseProxy()
		{
			return this.courseProxy;
		}
		public void setCourseProxy(CourseProxy courseProxy)
		{
			this.courseProxy=courseProxy;
		}
		
		public void showPopUpViewForRoom(int left,int top)
		{
			((ListBoxPopupViewImpl)popupView).setPopupPosition(left-340, top-60);				
			((ListBoxPopupViewImpl)popupView).show();
		}
		// E Module 5 and TTG Bug Changes
		
		// 5C: SPEC END
	
}
