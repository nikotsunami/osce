package ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment;

import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface RoleSubView extends IsWidget{


	interface Delegate {
		public void roleSelected(RoleSubView view);
		
		public void editBackUpFlag(RoleSubView view,PatientInRoleSubView patientInRoleSubView,PatientInRoleProxy proxy,boolean isBackUp);
		
		public void updatePostOfPatient(OscePostProxy newProxy,OscePostProxy oldProxy,PatientInRoleSubViewImpl newPatientView,PatientInRoleProxy proxy);
		//public boolean patientInRoleIsFirstAssigned(OsceSequenceProxy seqProxy,PatientInRoleProxy patientInRoleProxy);
	}
	
	 void setDelegate(Delegate delegate);
	 
	 public VerticalPanel getPatientInRoleVP();
	 
	 public VerticalPanel getBackUpVP();
	 
	 public PickupDragController getDragController1();
	 
	 public PickupDragController getDragController2();
	 
	 public StandardizedRoleProxy getRoleProxy();
	 
	 public void setRoleProxy(StandardizedRoleProxy roleProxy);
	 
	 public void setBoundaryPanel(AbsolutePanel boundaryPanel);
		
		public AbsolutePanel getBoundaryPanel();
	 
	 public Label getRoleLbl();
	 
	 public Label getCountLbl();
	 
	 public OscePostProxy getPostProxy();
	 
	 public void setPostProxy(OscePostProxy postProxy);
	 
	 public OsceDayProxy getOsceDayProxy();
	 
	 public void setOsceDayProxy(OsceDayProxy osceDayProxy);
	 
	 public OsceSequenceProxy getOsceSequenceProxy();
	 
	 public void setOsceSequenceProxy(OsceSequenceProxy osceSequenceProxy);
	 
	 public void refreshCountLabel();
	 
	 public OsceDaySubViewImpl getOsceDaySubViewImpl();
	 
	 public void setOsceDaySubViewImpl(OsceDaySubViewImpl osceDaySubViewImpl);
	 
		//modul 3 changes {

	 public Label getbackupLabel();
	 
	 public boolean getIsBackupPanel();
	 
	 public void setIsBackupPanel(boolean isBackup);
	 
	 public RoleSubView getBackUpRoleView() ;
	 public void setBackUpRoleView(RoleSubView backUpRoleView) ;
	 public HorizontalPanel getRoleParent();
	 public void setRoleParent(HorizontalPanel parent);
	 
	 public VerticalPanelDropController getDropController2();
	 
	//modul 3 changes }
}
