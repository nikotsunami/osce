package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.BucketInformationProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.BucketInfoType;
import ch.unibas.medizin.osce.shared.ExportOsceType;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
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
	Label bucketNameLbl;
	
	@UiField
	Label accessKeyLbl;
	
	@UiField
	Label secretKeyLbl;
	
	@UiField
	Label encryptionKeyLbl;
	
	@UiField
	Label basePathLbl;
	
	@UiField
	TextBox bucketName;
	
	@UiField
	TextBox accessKey;
	
	@UiField
	TextBox secretKey;
	
	@UiField
	PasswordTextBox password;
	
	@UiField
	TextBox encryptionKey;
	
	@UiField
	TextBox basePath;
	
	@UiField
	IconButton saveEditButton;
	
	@UiField
	IconButton cancelButton;
	
	@UiField
	IconButton fetchFiles;
	
	BucketInformationProxy bucketInformationProxy;
	
	@UiField
	RadioButton eOSCE;
	
	@UiField
	RadioButton iOSCE;
	
	@UiField
	RadioButton s3;
	
	@UiField
	RadioButton ftp;
	
	ExportOsceType osceType = null;
		
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
		
		bucketNameLbl.setText(constants.bucketName());
		accessKeyLbl.setText(constants.accessKey());
		secretKeyLbl.setText(constants.secretKey());
		basePath.setText(constants.basePath());
		encryptionKeyLbl.setText(constants.encryptionKey());
		cancelButton.setText(constants.cancel());
		fetchFiles.setText(constants.fetchFilesFromCloud());
		
		eOSCE.setText(constants.eOSCE());
		iOSCE.setText(constants.iOSCE());
		
		s3.setText(constants.s3());
		ftp.setText(constants.ftp());
		
		if(s3.getValue()){
			secretKey.setVisible(true);
			password.setVisible(false);
		}else if(ftp.getValue()){
			secretKey.setVisible(false);
			password.setVisible(true);
		}
	}	
	
	@UiHandler("fetchFiles")
	public void fetchFilesClicked(ClickEvent e) {
		if (eOSCE.getValue() && unprocessed.getValue()) {
			delegate.fetchUnprocessedFilesFromCloud(ExportOsceType.EOSCE, selectedBucketType());
		}
		else if (eOSCE.getValue() && processed.getValue()) {
			delegate.fetchProcessedFilesFromCloud(ExportOsceType.EOSCE, selectedBucketType());
		}
		else if (iOSCE.getValue() && unprocessed.getValue()) {
			delegate.fetchUnprocessedFilesFromCloud(ExportOsceType.IOSCE, selectedBucketType());
		}
		else if (iOSCE.getValue() && processed.getValue()) {
			delegate.fetchProcessedFilesFromCloud(ExportOsceType.IOSCE, selectedBucketType());
		}
	}
	
	@UiHandler("eOSCE")
	public void eOSCESelected(ClickEvent e) {
		delegate.eOsceClicked();
	}
	
	@UiHandler("iOSCE")
	public void iOSCESelected(ClickEvent e) {
		delegate.iOsceClicked();
	}
	
	@UiHandler("unprocessed")
	public void unprocessedSelected(ClickEvent event)
	{
		disclouserPanelFlie.getHeaderTextAccessor().setText(constants.importUnprocessed());
		delButton.setEnabled(false);
		//importButton.setEnabled(true);
		if (eOSCE.getValue())
			delegate.unprocessedClicked(ExportOsceType.EOSCE);
		else if (iOSCE.getValue())
			delegate.unprocessedClicked(ExportOsceType.IOSCE);
		
	}
	
	@UiHandler("processed")
	public void processedSelected(ClickEvent event)
	{
		disclouserPanelFlie.getHeaderTextAccessor().setText(constants.importProcessed());
		delButton.setEnabled(true);
		//importButton.setEnabled(false);
		if (eOSCE.getValue())
			delegate.processedClicked(ExportOsceType.EOSCE);
		else if (iOSCE.getValue())
			delegate.processedClicked(ExportOsceType.IOSCE);
		
	}
	
	@UiHandler("importButton")
	public void importButtonClicked(ClickEvent event)
	{
		//delegate.importButtonClicked(true);
		Boolean test = delegate.checkSelectedValue();
		
		if (eOSCE.getValue())
			osceType = ExportOsceType.EOSCE;
		else if (iOSCE.getValue())
			osceType = ExportOsceType.IOSCE;
			
		
		if (test)
		{
			final MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.confirmation());
			messageConfirmationDialogBox.showYesNoDialog(constants.confirmationDeleteAfterImport());
			messageConfirmationDialogBox.getYesBtn().addClickHandler(new ClickHandler() {			
				@Override
				public void onClick(ClickEvent event) {
					messageConfirmationDialogBox.hide();
					delegate.importButtonClicked(osceType, true, selectedBucketType());
				}
			});
			messageConfirmationDialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					messageConfirmationDialogBox.hide();
					delegate.importButtonClicked(osceType, false, selectedBucketType());
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
			final MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
			messageConfirmationDialogBox.showYesNoDialog(constants.confirmDelete());
			messageConfirmationDialogBox.getYesBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					messageConfirmationDialogBox.hide();
					delegate.deleteButtonClicked(selectedOsceType(), selectedBucketType());
				}
			});
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
	
	public PasswordTextBox getPassword() {
		return password;
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

	public void setBucketInformationProxy(BucketInformationProxy bucketInformationProxy) {
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
			delegate.bucketSaveButtonClicked(bucketInformationProxy, bucketName.getText(), accessKey.getText(), secretKey.getText(),password.getValue(), encryptionKey.getText(), basePath.getText(), ftp.getValue());
		}
		else if (saveEditButton.getText().equals(constants.edit()))
		{
			bucketName.setEnabled(true);
			accessKey.setEnabled(true);
			secretKey.setEnabled(true);
			password.setEnabled(true);
			encryptionKey.setEnabled(true);
			basePath.setEnabled(true);
			
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
		password.setEnabled(false);
		basePath.setEnabled(false);
		
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

	public ExportOsceType selectedOsceType() {
		if (eOSCE.getValue())
			return ExportOsceType.EOSCE;
		else if (iOSCE.getValue())
			return ExportOsceType.IOSCE;
		
		return null;
	}
	

	@UiHandler("s3")
	public void s3Selected(ClickEvent event)
	{
		if(s3.getValue() == true) {
			encryptionKeyLbl.setText(constants.encryptionKey());
			bucketNameLbl.setText(constants.bucketName());
			accessKeyLbl.setText(constants.accessKey());
			secretKeyLbl.setText(constants.secretKey());
			basePath.setVisible(false);
			basePathLbl.setVisible(false);
			password.setVisible(false);
			secretKey.setVisible(true);
			
			boolean isFTP;
			boolean empty;
			boolean enabled;
			
			if(bucketInformationProxy != null && (bucketInformationProxy.getType() == null || BucketInfoType.S3.equals(bucketInformationProxy.getType()))) {
				isFTP = false;
				empty = false;
				enabled = false;
			} else {
				isFTP = false;
				empty = true;
				enabled = true;
			}
			setValuesToTextBoxs(isFTP,empty);
			enableTextBoxs(enabled,isFTP);	
			
			bucketName.setFocus(true);
		}else {
			Log.info("changes of ftp");
		}
		
	}

	@UiHandler("ftp")
	public void ftpSelected(ClickEvent event)
	{
		if(ftp.getValue() == true) {
			basePathLbl.setText(constants.basePath());
			encryptionKeyLbl.setText(constants.encryptionKey());
			bucketNameLbl.setText(constants.host());
			accessKeyLbl.setText(constants.userName());
			secretKeyLbl.setText(constants.password());
			basePath.setVisible(true);
			basePathLbl.setVisible(true);
			secretKey.setVisible(false);
			password.setVisible(true);
			
			boolean isFTP;
			boolean empty;
			boolean enabled;
			
			if(bucketInformationProxy != null && BucketInfoType.FTP.equals(bucketInformationProxy.getType())) {
				isFTP = true;
				empty = false;
				enabled = false;
			} else {
				isFTP = true;
				empty = true;
				enabled = true;
			}
			
			setValuesToTextBoxs(isFTP,empty);
			enableTextBoxs(enabled,isFTP);
			
			bucketName.setFocus(true);
		}else {
			Log.info("changes of s3");
		}
		
	}
	
	private void setValuesToTextBoxs(boolean isFTP,boolean empty) {
		if(empty == false) {
			bucketName.setText(bucketInformationProxy.getBucketName());
			accessKey.setText(bucketInformationProxy.getAccessKey());
			encryptionKey.setText(bucketInformationProxy.getEncryptionKey());

			if(isFTP) {
				basePath.setText(bucketInformationProxy.getBasePath());
				secretKey.setVisible(false);
				password.setText(bucketInformationProxy.getSecretKey());
				password.setVisible(true);
			}	else{
				password.setVisible(false);
				secretKey.setText(bucketInformationProxy.getSecretKey());
				secretKey.setVisible(true);
			}
		} else {
			bucketName.setText("");
			accessKey.setText("");
			password.setText("");
			encryptionKey.setText("");
			secretKey.setText("");
			if(isFTP) {
				basePath.setText("");
			}	
		}
		
	}

	private void enableTextBoxs(boolean enabled,boolean isFTP) {
		bucketName.setEnabled(enabled);
		accessKey.setEnabled(enabled);
		encryptionKey.setEnabled(enabled);
		
		if(isFTP == true) {
			basePath.setEnabled(enabled);	
			password.setEnabled(enabled);
		} else {
			secretKey.setEnabled(enabled);
			basePath.setVisible(false);
		}
		
		if(enabled == false) {
			saveEditButton.setText(constants.edit());
			cancelButton.setVisible(false);	
		} else {
			saveEditButton.setText(constants.save());
			cancelButton.setVisible(true);
		}
		
	}
	
	@Override
	public void typeValueChanged(boolean isFTP) {
		if(isFTP) {
			ftpSelected(null);
		}else {
			s3Selected(null);
		}
	}
	
	public RadioButton getFtp() {
		return ftp;
	}
	
	public RadioButton getS3() {
		return s3;
	}
	
	public TextBox getBasePath() {
		return basePath;
	}
	
	public BucketInfoType selectedBucketType() {
		if (s3.getValue()) 
			return BucketInfoType.S3;
		else if (ftp.getValue()) 
			return BucketInfoType.FTP;
		
		return null;
	}
}

