package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ManualOsceChangeBreakViewImpl extends Composite implements ManualOsceChangeBreakView {

	private static ManualOsceChangeBreakViewImplUiBinder uiBinder = GWT.create(ManualOsceChangeBreakViewImplUiBinder.class);
	
	interface ManualOsceChangeBreakViewImplUiBinder extends UiBinder<Widget, ManualOsceChangeBreakViewImpl> {
	}
	
	private Delegate delegate;
	
	private OsceSequenceProxy osceSequenceProxy;
	
	@UiField
	IconButton breakSooner;
	
	@UiField
	IconButton breakLater;
	
	@UiField
	IconButton addRotation;
	
	@UiField
	IconButton removeRotation;
	
	@UiField
	IconButton editSequence;
	
	@UiField
	Label nameOfSequence;
	
	@UiField
	TextBox rotationNumber;	
	
	@UiField
	TextBox editNameOfSequence;
	
	@UiField
	HorizontalPanel sequencePanel;
	
	public ManualOsceChangeBreakViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		rotationNumber.setReadOnly(true);
		editSequence.setIcon("pencil");
		editNameOfSequence.getElement().getStyle().setDisplay(Display.NONE);
	}
	
	private void init(){
		if (osceSequenceProxy != null)
		{
			if (osceSequenceProxy.getLabel() != null && osceSequenceProxy.getLabel().isEmpty() == false)
			{
				nameOfSequence.setText(osceSequenceProxy.getLabel());
			}
			
			if (osceSequenceProxy.getNumberRotation() != null)
			{
				rotationNumber.setText(osceSequenceProxy.getNumberRotation().toString());
			}
		}
	}
	
	@UiHandler("breakSooner")
	public void breakSoonerClicked(ClickEvent event)
	{
		if (osceSequenceProxy != null && delegate != null)
		{
			delegate.breakSoonerClicked(osceSequenceProxy);
		}
	}
	
	@UiHandler("breakLater")
	public void breakLaterClicked(ClickEvent event)
	{
		if (osceSequenceProxy != null && delegate != null)
		{
			delegate.breakLaterClicked(osceSequenceProxy);
		}
	}
	
	@UiHandler("addRotation")
	public void addRotationClicked(ClickEvent event)
	{
		if (osceSequenceProxy != null && delegate != null)
		{
			delegate.addRotationClicked(osceSequenceProxy);
		}
	}
	
	@UiHandler("removeRotation")
	public void removeRotationClicked(ClickEvent event)
	{
		if (osceSequenceProxy != null && delegate != null)
		{
			delegate.removeRotationClicked(osceSequenceProxy);
		}
	}
		
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	public OsceSequenceProxy getOsceSequenceProxy() {
		return osceSequenceProxy;
	}
	
	public void setOsceSequenceProxy(OsceSequenceProxy osceSequenceProxy) {
		this.osceSequenceProxy = osceSequenceProxy;
		init();
	}
	
	public HorizontalPanel getSequencePanel() {
		return sequencePanel;
	}
	
	public void setSequencePanel(HorizontalPanel sequencePanel) {
		this.sequencePanel = sequencePanel;
	}
	
	public IconButton getEditSequence() {
		return editSequence;
	}
	
	public TextBox getEditNameOfSequence() {
		return editNameOfSequence;
	}
	
	public void setEditNameOfSequence(TextBox editNameOfSequence) {
		this.editNameOfSequence = editNameOfSequence;
	}
	
	public Label getNameOfSequence() {
		return nameOfSequence;
	}
	
	public IconButton getBreakSooner() {
		return breakSooner;
	}
	
	public IconButton getBreakLater() {
		return breakLater;
	}
	
	public IconButton getAddRotation() {
		return addRotation;
	}
	
	public IconButton getRemoveRotation() {
		return removeRotation;
	}
}
