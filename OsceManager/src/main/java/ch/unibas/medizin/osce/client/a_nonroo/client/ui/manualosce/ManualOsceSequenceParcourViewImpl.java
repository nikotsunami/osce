package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ManualOsceSequenceParcourViewImpl extends Composite implements ManualOsceSequenceParcourView {

	private static ManualOsceSequenceParcourViewImplUiBinder uiBinder = GWT.create(ManualOsceSequenceParcourViewImplUiBinder.class);
	
	interface ManualOsceSequenceParcourViewImplUiBinder extends UiBinder<Widget, ManualOsceSequenceParcourViewImpl> {
	}
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
	
	private Delegate delegate;
	
	@UiField
	ManualOsceChangeBreakViewImpl manualOsceChangeBreakViewImpl;
	
	@UiField
	IconButton addParcour;
	
	@UiField
	HorizontalPanel parcourPanel;
	
	@UiField
	VerticalPanel addParcourVerticalPanel;
	
	private OsceSequenceProxy osceSequenceProxy;
	
	private OsceDayProxy osceDayProxy;
	
	private IconButton deleteOsceSeqBtn = new IconButton();

	public ManualOsceSequenceParcourViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		manualOsceChangeBreakViewImpl.getEditSequence().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (manualOsceChangeBreakViewImpl.getEditSequence().getIcon().equals("pencil"))
				{
					if (manualOsceChangeBreakViewImpl.getOsceSequenceProxy() != null)
					{
						manualOsceChangeBreakViewImpl.getEditSequence().setIcon("disk");
						manualOsceChangeBreakViewImpl.getNameOfSequence().getElement().getStyle().setDisplay(Display.NONE);
						manualOsceChangeBreakViewImpl.getEditNameOfSequence().getElement().getStyle().clearDisplay();
						manualOsceChangeBreakViewImpl.getEditNameOfSequence().setValue(manualOsceChangeBreakViewImpl.getOsceSequenceProxy().getLabel());
					}
					
				}
				else if (manualOsceChangeBreakViewImpl.getEditSequence().getIcon().equals("disk"))
				{
					if (manualOsceChangeBreakViewImpl.getEditNameOfSequence().getValue().isEmpty())
					{
						MessageConfirmationDialogBox dateDialog=new MessageConfirmationDialogBox(constants.warning());
						dateDialog.showConfirmationDialog(constants.manualOsceSequenceWarning());				
						return;
					}
						
					if (manualOsceChangeBreakViewImpl.getOsceSequenceProxy() != null)
					{
						manualOsceChangeBreakViewImpl.getEditSequence().setIcon("pencil");
						String sequenceName = manualOsceChangeBreakViewImpl.getEditNameOfSequence().getValue();
						manualOsceChangeBreakViewImpl.getEditNameOfSequence().getElement().getStyle().setDisplay(Display.NONE);
						manualOsceChangeBreakViewImpl.getNameOfSequence().getElement().getStyle().clearDisplay();
						delegate.changeOsceSequenceLabel(sequenceName, manualOsceChangeBreakViewImpl.getOsceSequenceProxy());
					}					
				}
			}
		});
		
		deleteOsceSeqBtn.setIcon("trash");
		deleteOsceSeqBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				final MessageConfirmationDialogBox confirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
				confirmationDialogBox.showYesNoDialog(constants.manualOsceDeleteOsceSequence());
				
				confirmationDialogBox.getYesBtn().addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						confirmationDialogBox.hide();
						delegate.deleteOsceSequenceClicked(osceSequenceProxy, osceDayProxy);
					}
				});
				
				
			}
		});
	}

	@UiHandler("addParcour")
	public void addParcourClicked(ClickEvent event)
	{
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setAutoHideEnabled(true);
		dialogBox.setGlassEnabled(true);
		
		VerticalPanel vp=new VerticalPanel();
		HorizontalPanel hp=new HorizontalPanel();
		HorizontalPanel checkBoxHp=new HorizontalPanel();
		
		IconButton yesBtn=new IconButton();
		IconButton noBtnl=new IconButton();
		 
		final RadioButton copyWithBreak = new RadioButton("copyBreak");
		final RadioButton copyWithoutBreak = new RadioButton("copyBreak");
		
		copyWithBreak.setText(constants.manualOsceCopyWithBreak());
		copyWithoutBreak.setText(constants.manualOsceCopyWithoutBreak());
		
		copyWithBreak.setValue(true);
		
		checkBoxHp.add(copyWithBreak);
		checkBoxHp.add(copyWithoutBreak);
		
		yesBtn.setIcon("check");
		noBtnl.setIcon("closethick");

		yesBtn.setText(constants.yes());
		noBtnl.setText(constants.no());
		
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp.setSpacing(5);
		
		hp.add(yesBtn);
		hp.add(noBtnl);
				
		Label msgLbl=new Label();
		msgLbl.setText(constants.manualOsceParcourWarning());

		HorizontalPanel msgHp = new HorizontalPanel();
		msgHp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		msgHp.add(msgLbl);
		
		vp.setSpacing(5);
		vp.add(msgHp);
		vp.add(checkBoxHp);
		vp.add(hp);
				
		dialogBox.getCaption().asWidget().addStyleName("confirmbox");		
		dialogBox.setText(constants.warning());
		dialogBox.add(vp);
		
		dialogBox.getElement().getStyle().setZIndex(3);
		
		dialogBox.center();
		
		yesBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				delegate.addParcourClicked(osceSequenceProxy, copyWithBreak.getValue(), true);
			}
		});
		
		noBtnl.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				delegate.addParcourClicked(osceSequenceProxy, copyWithBreak.getValue(), false);
			}
		});
		
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	public HorizontalPanel getParcourPanel() {
		return parcourPanel;
	}
	
	public ManualOsceChangeBreakViewImpl getManualOsceChangeBreakViewImpl() {
		return manualOsceChangeBreakViewImpl;
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
	
	public void addOsceSequenceDeleteButton(){
		this.manualOsceChangeBreakViewImpl.getSequencePanel().add(deleteOsceSeqBtn);
	}
	
	public void removeOsceSequenceDeleteButton(){
		this.manualOsceChangeBreakViewImpl.getSequencePanel().remove(deleteOsceSeqBtn);
	}
	
	public VerticalPanel getAddParcourVerticalPanel() {
		return addParcourVerticalPanel;
	}
	
	public IconButton getDeleteOsceSeqBtn() {
		return deleteOsceSeqBtn;
	}
}
