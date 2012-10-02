package ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment;

import java.util.Iterator;

import ch.unibas.medizin.osce.client.a_nonroo.client.util.RoleFulfilCriteriaEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RoleFulfilCriteriaHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RoleSelectedEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RoleSelectedHandler;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RoleSubViewImpl extends Composite implements DragHandler,RoleFulfilCriteriaHandler,RoleSelectedHandler,RoleSubView{

	private Delegate delegate;
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate=delegate;
		
	}
	PickupDragController dragController1;
	public PickupDragController getDragController1() {
		return dragController1;
	}
	
	private long onDragPatientID;
	
	private RoleSubViewImpl sourceRoleView;
	
	private AbsolutePanel boundaryPanel;
	
	public AbsolutePanel getBoundaryPanel() {
		return boundaryPanel;
	}
	
	private HorizontalPanel roleParent;
	public HorizontalPanel getRoleParent() {
		return roleParent;
	}

	public void setRoleParent(HorizontalPanel roleParent) {
		this.roleParent = roleParent;
	}

	public void setBoundaryPanel(AbsolutePanel boundaryPanel) {
		this.boundaryPanel = boundaryPanel;
	}
	boolean isBackUpOnDragStart=false;
	
	StandardizedRoleProxy roleProxy;
	
	OscePostProxy postProxy;
	
	OsceDayProxy osceDayProxy;
	
	OsceSequenceProxy osceSequenceProxy;
	
	OsceDaySubViewImpl osceDaySubViewImpl;
	
	//modul 3 changes {
	private RoleSubView backUpRoleView;
	
	
	public void setBackUpRoleView(RoleSubView backUpRoleView) {
		this.backUpRoleView = backUpRoleView;
	}

	public RoleSubView getBackUpRoleView() {
		return backUpRoleView;
	}
	public boolean isBackUpPanel=false;
	
	
	public boolean getIsBackupPanel(){
		return this.isBackUpPanel;
	}
	
	
	public void setIsBackupPanel(boolean isBeckupPanel){
		this.isBackUpPanel=isBeckupPanel;
	}
	//modul 3 changes }
	
	public OsceDaySubViewImpl getOsceDaySubViewImpl() {
		return osceDaySubViewImpl;
	}

	public void setOsceDaySubViewImpl(OsceDaySubViewImpl osceDaySubViewImpl) {
		this.osceDaySubViewImpl = osceDaySubViewImpl;
	}

	public OsceSequenceProxy getOsceSequenceProxy() {
		return osceSequenceProxy;
	}

	public void setOsceSequenceProxy(OsceSequenceProxy osceSequenceProxy) {
		this.osceSequenceProxy = osceSequenceProxy;
	}

	public OsceDayProxy getOsceDayProxy() {
		return osceDayProxy;
	}

	public void setOsceDayProxy(OsceDayProxy osceDayProxy) {
		this.osceDayProxy = osceDayProxy;
	}

	public OscePostProxy getPostProxy() {
		return postProxy;
	}

	public void setPostProxy(OscePostProxy postProxy) {
		this.postProxy = postProxy;
	}
	@UiField
	FocusPanel roleHeader;
	
	public StandardizedRoleProxy getRoleProxy() {
		return roleProxy;
	}

	public void setRoleProxy(StandardizedRoleProxy roleProxy) {
		this.roleProxy = roleProxy;
	}

	public void setDragController1(PickupDragController dragController1) {
		this.dragController1 = dragController1;
	}

	public PickupDragController getDragController2() {
		return dragController2;
	}

	public void setDragController2(PickupDragController dragController2) {
		this.dragController2 = dragController2;
	}
	PickupDragController dragController2;
	
	VerticalPanelDropController dropController1;
	VerticalPanelDropController dropController2;
	
	public VerticalPanelDropController getDropController2() {
		return dropController2;
	}
	@UiField
	AbsolutePanel roleAP;
	
	@UiField
	VerticalPanel patientInRoleVP;
	
	@UiField
	Label roleLbl;
	
	@UiField
	Label countLbl;
	
//	modul 3 changes {
	
	@UiField
	Label backUpLabel;
	
	public Label getBackUpLabel(){
		return this.backUpLabel;
	}


//	modul 3 changes }
	
	public Label getCountLbl() {
		return countLbl;
	}

	public Label getRoleLbl() {
		return roleLbl;
	}

	public VerticalPanel getPatientInRoleVP() {
		return patientInRoleVP;
	}

	public void setPatientInRoleVP(VerticalPanel patientInRoleVP) {
		this.patientInRoleVP = patientInRoleVP;
	}

	public VerticalPanel getBackUpVP() {
		return backUpVP;
	}

	public void setBackUpVP(VerticalPanel backUpVP) {
		this.backUpVP = backUpVP;
	}
	@UiField
	VerticalPanel backUpVP;
	
	private static RoleSubViewImplUiBinder uiBinder = GWT
			.create(RoleSubViewImplUiBinder.class);
	
	interface RoleSubViewImplUiBinder extends UiBinder<Widget, RoleSubViewImpl> {
	}
	
	public RoleSubViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		dragController1=new PickupDragController(roleAP, false);
		dragController2=new PickupDragController(roleAP, false);
		
		dragController2.addDragHandler(this);
		dragController1.addDragHandler(this);
		
		dropController1=new VerticalPanelDropController(patientInRoleVP);
		dropController2=new VerticalPanelDropController(backUpVP);
		
		dragController1.registerDropController(dropController2);
		dragController2.registerDropController(dropController1);
		
		
		
		
	}
	public RoleSubViewImpl(AbsolutePanel boundaryPanel) {
		initWidget(uiBinder.createAndBindUi(this));
		
		setBoundaryPanel(boundaryPanel);
		
		dragController1=new PickupDragController(boundaryPanel, false);
		dragController2=new PickupDragController(boundaryPanel, false);
		
		dragController2.addDragHandler(this);
		dragController1.addDragHandler(this);
		
		dropController1=new VerticalPanelDropController(patientInRoleVP);
		dropController2=new VerticalPanelDropController(backUpVP);
		
		dragController1.registerDropController(dropController2);
		dragController2.registerDropController(dropController1);
		
		
		
		
	}

	@Override
	public void onDragEnd(DragEndEvent event) {
		Log.info("Drag End");
		Log.info("Patient In Role ID :" + ((PatientInRoleSubView)event.getSource()).getPatientInRoleProxy().getId());
		Log.info("Draggable Widget :" + event.getContext().dropController);
		//Log.info("dropController Widget :" + event.getContext().);
		Log.info("Parent Widget Count on End :" +((VerticalPanel)((PatientInRoleSubViewImpl)event.getSource()).getParent()).getWidgetCount());
		
		//Not Backup On End
		if(((VerticalPanel)((PatientInRoleSubViewImpl)event.getSource()).getParent()).getParent() instanceof AbsolutePanel && isBackUpOnDragStart)
		{
			this.getDragController2().makeNotDraggable(((PatientInRoleSubView)event.getSource()).asWidget());
			this.getDragController1().makeDraggable(((PatientInRoleSubView)event.getSource()).asWidget(), ((PatientInRoleSubView)event.getSource()).getPatientInRoleLbl());
			
			
			
			//refresh count
			refreshCountLabel();
			
			delegate.editBackUpFlag(this,(PatientInRoleSubView)event.getSource(),((PatientInRoleSubView)event.getSource()).getPatientInRoleProxy(),false);
			
			Log.info("Not Back On End");
		}
		
		// drag to backup / drag to any other role view
		else if(((VerticalPanel)((PatientInRoleSubViewImpl)event.getSource()).getParent()).getParent() instanceof SimplePanel && !isBackUpOnDragStart)
		{
			this.getDragController1().makeNotDraggable(((PatientInRoleSubView)event.getSource()).asWidget());
			this.getDragController2().makeDraggable(((PatientInRoleSubView)event.getSource()).asWidget(), ((PatientInRoleSubView)event.getSource()).getPatientInRoleLbl());
			refreshCountLabel();
			delegate.editBackUpFlag(this,(PatientInRoleSubView)event.getSource(),((PatientInRoleSubView)event.getSource()).getPatientInRoleProxy(),true);
			
			Log.info("Back On End");
		}
		else if((event.getContext().dragController).equals(dragController1) && event.getContext().finalDropController != null)
		{
			Log.info("Patient Drop Target Widget"+(VerticalPanel)event.getContext().finalDropController.getDropTarget());
			VerticalPanel patientVp=(VerticalPanel)event.getContext().finalDropController.getDropTarget();
			
		
			Log.info("Patient Drop Target Widget Count"+((VerticalPanel)event.getContext().finalDropController.getDropTarget()).getWidgetCount());
			PatientInRoleSubViewImpl patientDroped=(PatientInRoleSubViewImpl)event.getSource();
			RoleSubViewImpl patientDropedIn=(RoleSubViewImpl)((VerticalPanel)((AbsolutePanel)((AbsolutePanel)((VerticalPanel)patientDroped.getParent()).getParent()).getParent()).getParent()).getParent();
			int count=0;
			for(int i=0;i<patientDropedIn.getPatientInRoleVP().getWidgetCount();i++)
			{
				PatientInRoleSubViewImpl patientView=(PatientInRoleSubViewImpl)((patientDropedIn.getPatientInRoleVP()).getWidget(i));
				if(patientView.getPatientInRoleProxy().getPatientInSemester().getStandardizedPatient().getId()==onDragPatientID)
				{
					count++;
					
				}
				if(count==2)
				{
					patientView.removeFromParent();
					sourceRoleView.getPatientInRoleVP().insert(patientView,sourceRoleView.getPatientInRoleVP().getWidgetCount());
					
					return;
				}
			}
			
			
			patientDroped.setRoleSubView(patientDropedIn);
			this.refreshCountLabel();
			OscePostProxy newPost=patientDropedIn.getPostProxy();
			delegate.updatePostOfPatient(newPost, this.getPostProxy(), patientDroped,patientDroped.getPatientInRoleProxy());
			patientDropedIn.refreshCountLabel();
			//update post of patient dropped
		
		}
	}
	
	public void refreshCountLabel()
	{
		int requiredPatient=0;
		if(osceSequenceProxy.getCourses() !=null)
			requiredPatient=-((2*osceSequenceProxy.getCourses().size())-this.getPatientInRoleVP().getWidgetCount());
		
		
		//refresh count
		this.getCountLbl().setStylePrimaryName("count");
		//view.getCountLbl().addStyleName("count-yellow");
		if(requiredPatient>0)
		{
			this.getCountLbl().removeStyleDependentName("green");
			this.getCountLbl().removeStyleDependentName("red");
			this.getCountLbl().addStyleDependentName("yellow");
			this.getCountLbl().setText("+"+requiredPatient);
		}
		else if(requiredPatient < 0)
		{
			this.getCountLbl().removeStyleDependentName("green");
			this.getCountLbl().removeStyleDependentName("yellow");
			this.getCountLbl().addStyleDependentName("red");
			this.getCountLbl().setText(""+requiredPatient);
		}
		else
		{	
			this.getCountLbl().removeStyleDependentName("yellow");
			this.getCountLbl().removeStyleDependentName("red");
			this.getCountLbl().addStyleDependentName("green");
			this.getCountLbl().setText("0");
		}
	}
	
	@Override
	public void onDragStart(DragStartEvent event) {
		
		if((PatientInRoleSubViewImpl)event.getSource() instanceof PatientInRoleSubViewImpl)
		{
			onDragPatientID=((PatientInRoleSubViewImpl)event.getSource()).getPatientInRoleProxy().getPatientInSemester().getStandardizedPatient().getId();
			sourceRoleView=(RoleSubViewImpl)((PatientInRoleSubViewImpl)event.getSource()).getRoleSubView();
		}
		
		//Log.info("Parent Widget Count on Start :" +((VerticalPanel)((PatientInRoleSubViewImpl)event.getSource()).getParent()).getWidgetCount());
		if(((VerticalPanel)((PatientInRoleSubViewImpl)event.getSource()).getParent()).getParent() instanceof AbsolutePanel)
		{
			Log.info("Not Back On Start");
			isBackUpOnDragStart=false;
		}
		else
		{
			Log.info("Back On Start");
			isBackUpOnDragStart=true;
		}
	}

	@Override
	public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPreviewDragStart(DragStartEvent event)
			throws VetoDragException {
		// TODO Auto-generated method stub
		
	}
	
	@UiHandler("roleHeader")
	public void roleHeaderClicked(ClickEvent event)
	{
		Log.info("roleHeader Clicked : Is backup" + isBackUpPanel);
		//modul 3 changes {
		
		if(this.isBackUpPanel==true)
			return;
		
		//modul 3 changes }
		this.roleHeader.getWidget().addStyleName("highlight-role");
		delegate.roleSelected(this);
	}

	@Override
	public void onRoleFulfilCriteriaEventReceived(RoleFulfilCriteriaEvent event) {
		
		Log.info("onRoleFulfilCriteriaEventReceived");
		if(event.getListOsceDayProxy() !=null && event.getListOsceDayProxy().contains(osceDayProxy))
		{
		
		if(osceDayProxy.getId()==event.getOsceDayProxy().getId())
		{
			Iterator<StandardizedRoleProxy> roles=event.getStandardizedroleProxies().iterator();
			
			while(roles.hasNext())
			{
				if(roles.next().getId()==roleProxy.getId())
				{
						Log.info("roProxy Highlited is  :" +roleProxy.getId());
					this.roleHeader.getWidget().addStyleName("highlight-role");
					break;
					//this.addStyleName("");
				}
				
			}
			
		}
	}
		else
		{
			this.roleHeader.getWidget().removeStyleName("highlight-role");
		}
	}

	@Override
	public void onRoleSelectedEventReceived(RoleSelectedEvent event) {
		
		Log.info("onRoleSelectedEventReceived");
		if(! this.isBackUpPanel && event.getStandardizedRoleProxy().getId()!=roleProxy.getId() ) //&& (event.getOsceDayProxy().getId() != this.getOsceDayProxy().getId()))
		{
			this.roleHeader.getWidget().removeStyleName("highlight-role");
		}
		else if(! this.isBackUpPanel && event.getStandardizedRoleProxy().getId()==roleProxy.getId()  && (event.getOsceDayProxy().getId() != this.getOsceDayProxy().getId()))
		{
			this.roleHeader.getWidget().removeStyleName("highlight-role");
		}
		
	}

	@Override
	public Label getbackupLabel() {
		
		return this.backUpLabel;
	}
	


}
