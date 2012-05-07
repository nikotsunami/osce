package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;


import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientDetailsView.Delegate;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientDetailsView.Presenter;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class RoomMaterialsDetailsViewImpl extends Composite implements RoomMaterialsDetailsView{
	
	private static RoomMaterialsDetailsViewImplUiBinder uiBinder = GWT
			.create(RoomMaterialsDetailsViewImplUiBinder.class);

	interface RoomMaterialsDetailsViewImplUiBinder extends UiBinder<Widget, RoomMaterialsDetailsViewImpl> {
		
	}
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
	private Presenter presenter;
	private Delegate delegate;
	
	public RoomMaterialsDetailsViewImpl() {
		// TODO Auto-generated constructor stub
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter StandardizedPatientActivity) {
		this.presenter =  StandardizedPatientActivity;
	}
	
	public Widget asWidget() {
		return this;
	}

}
