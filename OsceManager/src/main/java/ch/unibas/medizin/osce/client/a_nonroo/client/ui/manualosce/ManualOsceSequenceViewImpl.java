package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ManualOsceSequenceViewImpl extends Composite implements ManualOsceSequenceView {

	private static ManualOsceSequenceViewImplUiBinder uiBinder = GWT.create(ManualOsceSequenceViewImplUiBinder.class);
	
	interface ManualOsceSequenceViewImplUiBinder extends UiBinder<Widget, ManualOsceSequenceViewImpl> {
	}
	
	private Delegate delegate;
	
	@UiField
	ManualOsceLunchBreakViewImpl manualOscelunchBreakViewImpl;
	
	@UiField
	IconButton addOsceSequence;
	
	@UiField
	HorizontalPanel firstOsceSequencePanel;
	
	@UiField
	HorizontalPanel secondOsceSequencePanel;
	
	@UiField
	VerticalPanel addSequencePanel;

	private OsceDayProxy osceDayProxy;
	
	String regex = "\\d+";
	
	public ManualOsceSequenceViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		manualOscelunchBreakViewImpl.getLunchBreakDuration().addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (manualOscelunchBreakViewImpl.getLunchBreakDuration().getValue().matches(regex))
				{
					delegate.changeLunchBreak(event.getValue());
				}
			}
		});
	}
	
	@UiHandler("addOsceSequence")
	public void addOsceSequenceClicked(ClickEvent event){
		if (osceDayProxy != null)
			delegate.addOsceSequenceClicked(osceDayProxy);
	}
		
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	public ManualOsceLunchBreakViewImpl getManualOscelunchBreakViewImpl() {
		return manualOscelunchBreakViewImpl;
	}
	
	public HorizontalPanel getFirstOsceSequencePanel() {
		return firstOsceSequencePanel;
	}
	
	public HorizontalPanel getSecondOsceSequencePanel() {
		return secondOsceSequencePanel;
	}
	
	public VerticalPanel getAddSequencePanel() {
		return addSequencePanel;
	}
	
	public OsceDayProxy getOsceDayProxy() {
		return osceDayProxy;
	}
	
	public void setOsceDayProxy(OsceDayProxy osceDayProxy) {
		this.osceDayProxy = osceDayProxy;
	}
}
