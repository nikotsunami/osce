package ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment;

import ch.unibas.medizin.osce.client.managed.request.PatientInRoleProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class PatientInRoleSubViewImpl extends Composite implements PatientInRoleSubView{

	private Delegate delegate;
	
	@UiField
	Label patientInRoleLbl;
	
	@UiField
	IconButton deletePatientInRole;
	
	PatientInRoleProxy patientInRoleProxy;
	
	RoleSubView roleSubView;
	
	public void setRoleSubView(RoleSubView roleSubView) {
		this.roleSubView = roleSubView;
	}



	public RoleSubView getRoleSubView() {
		return roleSubView;
	}

	

	public PatientInRoleProxy getPatientInRoleProxy() {
		return patientInRoleProxy;
	}

	public void setPatientInRoleProxy(PatientInRoleProxy patientInRoleProxy) {
		this.patientInRoleProxy = patientInRoleProxy;
	}

	public Label getPatientInRoleLbl() {
		return patientInRoleLbl;
	}

	public void setPatientInRoleLbl(Label patientInRoleLbl) {
		this.patientInRoleLbl = patientInRoleLbl;
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate=delegate;
		
	}
	private static PatientInRoleSubViewImplUiBinder uiBinder = GWT
			.create(PatientInRoleSubViewImplUiBinder.class);
	
	interface PatientInRoleSubViewImplUiBinder extends UiBinder<Widget, PatientInRoleSubViewImpl> {
	}
	
	public PatientInRoleSubViewImpl()
	{
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("deletePatientInRole")
	public void deletePatientInRole(ClickEvent event)
	{
		delegate.deletePatientInRole(this);
	}

}
