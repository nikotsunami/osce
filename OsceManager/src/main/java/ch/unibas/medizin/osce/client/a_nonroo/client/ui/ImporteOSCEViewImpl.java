package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.BucketInformationProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ImporteOSCEViewImpl extends Composite implements ImporteOSCEView {

	private static importeOSCEViewImplUiBinder uiBinder = GWT
			.create(importeOSCEViewImplUiBinder.class);

	interface importeOSCEViewImplUiBinder extends
			UiBinder<Widget, ImporteOSCEViewImpl> {
	}
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	private Delegate delegate;

	private Presenter presenter;
	
	@UiField
	DisclosurePanel disclouserPanelFlie;
	
	@UiField
	VerticalPanel fileListPanel;	
		
	@UiField
	Button importButton;
	
	@UiField
	Button delButton;
	
	@UiField
	RadioButton processed;
	
	@UiField
	RadioButton unprocessed;
	
	@UiField
	SpanElement bucketNameLbl;
	
	@UiField
	SpanElement accessKeyLbl;
	
	@UiField
	SpanElement secretKeyLbl;
	
	@UiField
	SpanElement encryptionKeyLbl;
	
	@UiField
	TextBox bucketName;
	
	@UiField
	TextBox accessKey;
	
	@UiField
	TextBox secretKey;
	
	@UiField
	TextBox encryptionKey;
	
	@UiField
	IconButton saveEditButton;
	
	@UiField
	IconButton cancelButton;
	
	BucketInformationProxy bucketInformationProxy;
		
	public ImporteOSCEViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		disclouserPanelFlie.addStyleName("eOsceSchedulePanelStyle");
		processed.setText(constants.importProcessed());
		unprocessed.setText(constants.importUnprocessed());
		importButton.setText(constants.importBtn());
		delButton.setText(constants.delete());
		delButton.setEnabled(true);
		//importButton.setEnabled(false);
		disclouserPanelFlie.getHeaderTextAccessor().setText(constants.importProcessed());
		disclouserPanelFlie.addCloseHandler(new CloseHandler<DisclosurePanel>() {
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				disclouserPanelFlie.setOpen(true);
			}
		});
		
		bucketNameLbl.setInnerText(constants.bucketName());
		accessKeyLbl.setInnerText(constants.accessKey());
		secretKeyLbl.setInnerText(constants.secretKey());
		encryptionKeyLbl.setInnerText(constants.encryptionKey());
		cancelButton.setText(constants.cancel());
	}	
	
	@UiHandler("unprocessed")
	public void unprocessedSelected(ClickEvent event)
	{
		disclouserPanelFlie.getHeaderTextAccessor().setText(constants.importUnprocessed());
		delButton.setEnabled(false);
		//importButton.setEnabled(true);
		delegate.unprocessedClicked();
	}
	
	@UiHandler("processed")
	public void processedSelected(ClickEvent event)
	{
		disclouserPanelFlie.getHeaderTextAccessor().setText(constants.importProcessed());
		delButton.setEnabled(true);
		//importButton.setEnabled(false);
		delegate.processedClicked();
	}
	
	@UiHandler("importButton")
	public void importButtonClicked(ClickEvent event)
	{
		//delegate.importButtonClicked(true);
		Boolean test = delegate.checkSelectedValue();
		
		if (test)
		{
			final MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.confirmation());
			messageConfirmationDialogBox.showYesNoDialog(constants.confirmationDeleteAfterImport());
			messageConfirmationDialogBox.getYesBtn().addClickHandler(new ClickHandler() {			
				@Override
				public void onClick(ClickEvent event) {
					messageConfirmationDialogBox.hide();
					delegate.importButtonClicked(true);
				}
			});
			messageConfirmationDialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					messageConfirmationDialogBox.hide();
					delegate.importButtonClicked(false);
				}
			});
		}
		else
		{
			MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
			messageConfirmationDialogBox.showConfirmationDialog(constants.warningImportNoFileSelected());
		}
	}
	
	@UiHandler("delButton")
	public void delButtonClicked(ClickEvent event)
	{

		Boolean test = delegate.checkSelectedValue();
		
		if (test)
		{
			delegate.deleteButtonClicked();
		}
		else
		{
			MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
			messageConfirmationDialogBox.showConfirmationDialog(constants.warningImportNoFileSelected());
		}
	}
	
	public VerticalPanel getFileListPanel() {
		return fileListPanel;
	}

	public void setFileListPanel(VerticalPanel fileListPanel) {
		this.fileListPanel = fileListPanel;
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter systemStartActivity) {
		this.presenter = systemStartActivity;
	}
	
	public TextBox getBucketName() {
		return bucketName;
	}

	public void setBucketName(TextBox bucketName) {
		this.bucketName = bucketName;
	}

	public TextBox getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(TextBox accessKey) {
		this.accessKey = accessKey;
	}

	public TextBox getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(TextBox secretKey) {
		this.secretKey = secretKey;
	}

	public IconButton getSaveEditButton() {
		return saveEditButton;
	}

	public void setSaveEditButton(IconButton saveEditButton) {
		this.saveEditButton = saveEditButton;
	}

	public IconButton getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(IconButton cancelButton) {
		this.cancelButton = cancelButton;
	}

	public BucketInformationProxy getBucketInformationProxy() {
		return bucketInformationProxy;
	}

	public void setBucketInformationProxy(
			BucketInformationProxy bucketInformationProxy) {
		this.bucketInformationProxy = bucketInformationProxy;
	}
	
	public TextBox getEncryptionKey() {
		return encryptionKey;
	}
	
	public void setEncryptionKey(TextBox encryptionKey) {
		this.encryptionKey = encryptionKey;
	}
	
	@UiHandler("saveEditButton")
	public void saveEditButtonClicked(ClickEvent event)
	{
		if (saveEditButton.getText().equals(constants.save()))
		{
			cancelButton.setVisible(false);
			delegate.bucketSaveButtonClicked(bucketInformationProxy, bucketName.getText(), accessKey.getText(), secretKey.getText(), encryptionKey.getText());
		}
		else if (saveEditButton.getText().equals(constants.edit()))
		{
			bucketName.setEnabled(true);
			accessKey.setEnabled(true);
			secretKey.setEnabled(true);
			encryptionKey.setEnabled(true);
			
			saveEditButton.setText(constants.save());
			
			cancelButton.setVisible(true);
		}
	}
	
	@UiHandler("cancelButton")
	public void cancelButtonClicked(ClickEvent event)
	{
		bucketName.setEnabled(false);
		accessKey.setEnabled(false);
		secretKey.setEnabled(false);
		encryptionKey.setEnabled(false);
		
		saveEditButton.setText(constants.edit());
		cancelButton.setVisible(false);
	}

	public RadioButton getProcessed() {
		return processed;
	}

	public void setProcessed(RadioButton processed) {
		this.processed = processed;
	}

	public RadioButton getUnprocessed() {
		return unprocessed;
	}

	public void setUnprocessed(RadioButton unprocessed) {
		this.unprocessed = unprocessed;
	}
	
	
}
