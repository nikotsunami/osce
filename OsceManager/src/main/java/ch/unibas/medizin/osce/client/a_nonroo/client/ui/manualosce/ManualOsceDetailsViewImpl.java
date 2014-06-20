package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import ch.unibas.medizin.osce.client.managed.request.OsceProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ManualOsceDetailsViewImpl extends Composite implements ManualOsceDetailsView {

	private static ManualOsceDetailsViewImplUiBinder uiBinder = GWT.create(ManualOsceDetailsViewImplUiBinder.class);
	
	interface ManualOsceDetailsViewImplUiBinder extends UiBinder<Widget, ManualOsceDetailsViewImpl> {
	}

	private Delegate delegate;

	@UiField
	VerticalPanel mainVerticalPanel;
	
	@UiField
	ManualOsceSubViewImpl manualOsceSubViewImpl;
	
	private OsceProxy osceProxy;
	
	public ManualOsceDetailsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	public VerticalPanel getMainVerticalPanel() {
		return mainVerticalPanel;
	}
	
	public ManualOsceSubViewImpl getManualOsceSubViewImpl() {
		return manualOsceSubViewImpl;
	}
	
	public OsceProxy getOsceProxy() {
		return osceProxy;
	}
	
	public void setOsceProxy(OsceProxy osceProxy) {
		this.osceProxy = osceProxy;
	}
}
