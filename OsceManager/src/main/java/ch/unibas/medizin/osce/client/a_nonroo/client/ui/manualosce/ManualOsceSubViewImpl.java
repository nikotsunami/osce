package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ManualOsceSubViewImpl extends Composite implements ManualOsceSubView {

	private static ManualOsceSubViewImplUiBinder uiBinder = GWT.create(ManualOsceSubViewImplUiBinder.class);
	
	interface ManualOsceSubViewImplUiBinder extends UiBinder<Widget, ManualOsceSubViewImpl> {
	}
	
	private Delegate delegate;
	
	@UiField
	VerticalPanel osceDayVp;
	
	@UiField
	ManualOsceEditViewImpl manualOsceEditViewImpl;
	
	@UiField
	IconButton addOsceDay;
	
	@UiField
	VerticalPanel addOsceDayVerticalPanel;

	public ManualOsceSubViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("addOsceDay")
	public void addOsceDayClicked(ClickEvent event)
	{
		delegate.addOsceDayClicked();
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;		
	}
	
	public VerticalPanel getOsceDayVp() {
		return osceDayVp;
	}
	
	public ManualOsceEditViewImpl getManualOsceEditViewImpl() {
		return manualOsceEditViewImpl;
	}

	public VerticalPanel getAddOsceDayVerticalPanel() {
		return addOsceDayVerticalPanel;
	}
	
	public IconButton getAddOsceDay() {
		return addOsceDay;
	}
}
