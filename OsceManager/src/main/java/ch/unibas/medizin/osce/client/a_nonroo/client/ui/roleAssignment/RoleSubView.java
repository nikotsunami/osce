package ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment;

import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface RoleSubView extends IsWidget{


	interface Delegate {
		public void roleSelected(RoleSubView view);
		
		public void editBackUpFlag(RoleSubView view,PatientInRoleSubView patientInRoleSubView,PatientInRoleProxy proxy,boolean isBackUp);
		
		//public boolean patientInRoleIsFirstAssigned(OsceSequenceProxy seqProxy,PatientInRoleProxy patientInRoleProxy);
	}
	
	 void setDelegate(Delegate delegate);
	 
	 public VerticalPanel getPatientInRoleVP();
	 
	 public VerticalPanel getBackUpVP();
	 
	 public PickupDragController getDragController1();
	 
	 public PickupDragController getDragController2();
	 
	 public StandardizedRoleProxy getRoleProxy();
	 
	 public void setRoleProxy(StandardizedRoleProxy roleProxy);
	 
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
	 
	//modul 3 changes }
}
