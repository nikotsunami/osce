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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ExportOsceViewImpl extends Composite implements ExportOsceView {

	private static ExportOsceViewImplUiBinder uiBinder = GWT
			.create(ExportOsceViewImplUiBinder.class);

	interface ExportOsceViewImplUiBinder extends
			UiBinder<Widget, ExportOsceViewImpl> {
	}
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	private Delegate delegate;

	private Presenter presenter;
	
	@UiField
	DisclosurePanel disclouserPanelFlie;
	
	@UiField
	VerticalPanel fileListPanel;	
		
	@UiField
	IconButton exportOSCEButton;
		
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
	Label basePathLbl;
	
	@UiField
	Label encryptionKeyLbl;
	
	@UiField
	TextBox bucketName;
	
	@UiField
	TextBox accessKey;
	
	@UiField
	TextBox secretKey;
	
	@UiField
	PasswordTextBox password;
	
	@UiField
	TextBox basePath;
	
	@UiField
	TextBox encryptionKey;
	
	@UiField
	IconButton saveEditButton;
	
	@UiField
	IconButton cancelButton;
	
	@UiField
	RadioButton s3;
	
	@UiField
	RadioButton ftp;
	
	BucketInformationProxy bucketInformationProxy;
	
	@UiField
	RadioButton eOSCE;
	
	@UiField
	RadioButton iOSCE;
	
	public ExportOsceViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		disclouserPanelFlie.addStyleName("eOsceSchedulePanelStyle");
		processed.setText(constants.exportProcessed());
		unprocessed.setText(constants.exportUnprocessed());
		
		disclouserPanelFlie.getHeaderTextAccessor().setText(constants.exportProcessed());
		disclouserPanelFlie.addCloseHandler(new CloseHandler<DisclosurePanel>() {
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				disclouserPanelFlie.setOpen(true);
			}
		});
		
		basePathLbl.setText(constants.basePath());
		encryptionKeyLbl.setText(constants.encryptionKey());
		bucketNameLbl.setText(constants.bucketName());
		accessKeyLbl.setText(constants.accessKey());
		secretKeyLbl.setText(constants.secretKey());
		cancelButton.setText(constants.cancel());
		
		s3.setText(constants.s3());
		ftp.setText(constants.ftp());
		
		eOSCE.setText(constants.eOSCE());
		iOSCE.setText(constants.iOSCE());
		
		basePath.setVisible(false);
		basePathLbl.setVisible(false);
		
		if (eOSCE.getValue()) {
			exportOSCEButton.setText(constants.exporteOSCE());
		}
		else if (iOSCE.getValue()) {
			exportOSCEButton.setText(constants.exportiOSCE());
		}
		
		if(s3.getValue()){
			secretKey.setVisible(true);
			password.setVisible(false);
		}else if(ftp.getValue()){
			secretKey.setVisible(false);
			password.setVisible(true);
		}
	}
	
	@UiHandler("eOSCE")
	public void eOSCESelected(ClickEvent e) {
		exportOSCEButton.setText(constants.exporteOSCE());
		delegate.eOsceClicked();
	}
	
	@UiHandler("iOSCE")
	public void iOSCESelected(ClickEvent e) {
		exportOSCEButton.setText(constants.exportiOSCE());
		delegate.iOsceClicked();
	}
	
	@UiHandler("unprocessed")
	public void unprocessedSelected(ClickEvent event)
	{
		disclouserPanelFlie.getHeaderTextAccessor().setText(constants.exportUnprocessed());
		if (eOSCE.getValue())
			delegate.unprocessedClicked(ExportOsceType.EOSCE);
		else if (iOSCE.getValue())
			delegate.unprocessedClicked(ExportOsceType.IOSCE);
	}
	
	@UiHandler("processed")
	public void processedSelected(ClickEvent event)
	{
		disclouserPanelFlie.getHeaderTextAccessor().setText(constants.exportProcessed());
		if (eOSCE.getValue())
			delegate.processedClicked(ExportOsceType.EOSCE);
		else if (iOSCE.getValue())
			delegate.processedClicked(ExportOsceType.IOSCE);
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
			secretKey.setVisible(true);
			password.setVisible(false);
			
			boolean isFTP;
			boolean empty;
			boolean enabled;
			
			if(bucketInformationProxy != null && BucketInfoType.S3.equals(bucketInformationProxy.getType())) {
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
	
	public boolean checkRadio()
	{ 
		if(processed.getValue().booleanValue() == true)
			return true;
		else if (unprocessed.getValue().booleanValue() == true)
			return false;
		else
			return true;
	}
	
	@UiHandler("exportOSCEButton")
	public void exportOSCEButtonClicked(ClickEvent event)
	{
		if (bucketName.getText().equals("") || accessKey.getText().equals("") || secretKey.getText().equals("") || encryptionKey.getText().equals("") || (ftp.getValue() == true && basePath.getText().equals("")))
		{
			final MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
			messageConfirmationDialogBox.showConfirmationDialog(constants.bucketInfoError());
		}
		else
		{
			Boolean flag = delegate.checkSelectedValue();
			if (flag)
			{
				if (unprocessed.getValue() == false)
				{
					final MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
					messageConfirmationDialogBox.showYesNoDialog(constants.exportWarningAlreadyExported());
					
					messageConfirmationDialogBox.getYesBtn().addClickHandler(new ClickHandler() {					
						@Override
						public void onClick(ClickEvent event) {
							messageConfirmationDialogBox.hide();
							if (eOSCE.getValue()) {
								delegate.exporteOSCEButtonClicked(unprocessed.getValue());
							}
							else if (iOSCE.getValue()) {
								delegate.exportiOSCEButtonClicked(unprocessed.getValue());
							}		
						}
					});
					
					messageConfirmationDialogBox.getNoBtnl().addClickHandler(new ClickHandler() {					
						@Override
						public void onClick(ClickEvent event) {
											
						}
					});
				}
				else
				{
					if (eOSCE.getValue()) {
						delegate.exporteOSCEButtonClicked(unprocessed.getValue());
					}
					else if (iOSCE.getValue()) {
						delegate.exportiOSCEButtonClicked(unprocessed.getValue());
					}
				}
			}
			else 
			{
				MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
				messageConfirmationDialogBox.showConfirmationDialog(constants.exportError());
			}
		}
	}
	
	/*@UiHandler("exportiOSCEButton")
	public void exportiOSCEButtonClicked(ClickEvent event)
	{
		if (bucketName.getText().equals("") || accessKey.getText().equals("") || secretKey.getText().equals("") || encryptionKey.getText().equals("") || (ftp.getValue() == true && basePath.getText().equals("")))
		{
			final MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
			messageConfirmationDialogBox.showConfirmationDialog(constants.bucketInfoError());
		}
		else
		{
			Boolean flag = delegate.checkSelectedValue();
			if (flag)
			{
				if (unprocessed.getValue() == false)
				{
					final MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
					messageConfirmationDialogBox.showYesNoDialog(constants.exportWarningAlreadyExported());
					
					messageConfirmationDialogBox.getYesBtn().addClickHandler(new ClickHandler() {					
						@Override
						public void onClick(ClickEvent event) {
							messageConfirmationDialogBox.hide();
							delegate.exportiOSCEButtonClicked(unprocessed.getValue());				
						}
					});
					
					messageConfirmationDialogBox.getNoBtnl().addClickHandler(new ClickHandler() {					
						@Override
						public void onClick(ClickEvent event) {
											
						}
					});
				}
				else
				{
					delegate.exportiOSCEButtonClicked(unprocessed.getValue());
				}
			}
			else 
			{
				MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
				messageConfirmationDialogBox.showConfirmationDialog(constants.exportError());
			}
		}
	}*/

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

	public PasswordTextBox getPassword(){
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

	public void setBucketInformationProxy(
			BucketInformationProxy bucketInformationProxy) {
		this.bucketInformationProxy = bucketInformationProxy;
	}
	
	@Override
	public TextBox getBasePath() {
		return basePath;
	}
	
	@Override
	public TextBox getEncryptionKey() {
		return encryptionKey;
	}
	
	@Override
	public RadioButton getS3() {
		return s3;
	}
	
	@Override
	public RadioButton getFtp() {
		return ftp;
	}
	
	@UiHandler("saveEditButton")
	public void saveEditButtonClicked(ClickEvent event)
	{
		if (saveEditButton.getText().equals(constants.save()))
		{
			cancelButton.setVisible(false);
			delegate.bucketSaveButtonClicked(bucketInformationProxy, bucketName.getText(), accessKey.getText(), secretKey.getText(),password.getText(),encryptionKey.getText(),basePath.getText(),ftp.getValue());
		}
		else if (saveEditButton.getText().equals(constants.edit()))
		{
			bucketName.setEnabled(true);
			accessKey.setEnabled(true);
			secretKey.setEnabled(true);
			encryptionKey.setEnabled(true);
			basePath.setEnabled(true);
			password.setEnabled(true);
			
			saveEditButton.setText(constants.save());
			
			cancelButton.setVisible(true);
		}
	}
	
	@UiHandler("cancelButton")
	public void cancelButtonClicked(ClickEvent event)
	{
		if(bucketInformationProxy != null){
			if(s3.getValue()){
				if(bucketInformationProxy.getType().equals(BucketInfoType.S3) ==false){
					bucketName.setText("");
					password.setText("");
					accessKey.setText("");
					encryptionKey.setText("");
					basePath.setText("");
					secretKey.setText("");
				}else{
					checkPreviousValuesForBucketInformation(bucketInformationProxy);	
				}
				
			}
			
			if(ftp.getValue()){
				if(bucketInformationProxy.getType().equals(BucketInfoType.FTP) ==false){
					bucketName.setText("");
					password.setText("");
					accessKey.setText("");
					encryptionKey.setText("");
					basePath.setText("");
					secretKey.setText("");
				}else{
					checkPreviousValuesForBucketInformation(bucketInformationProxy);	
				}
			}
			
		}else{
			bucketName.setValue("");
			password.setValue("");
			accessKey.setValue("");
			encryptionKey.setValue("");
			basePath.setValue("");
			secretKey.setValue("");
				
		}
		bucketName.setEnabled(false);
		accessKey.setEnabled(false);
		secretKey.setEnabled(false);
		encryptionKey.setEnabled(false);
		basePath.setEnabled(false);
		password.setEnabled(false);
		
		saveEditButton.setText(constants.edit());
		cancelButton.setVisible(false);
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
			}else{
				secretKey.setText(bucketInformationProxy.getSecretKey());
				password.setVisible(false);
				secretKey.setText(bucketInformationProxy.getSecretKey());
				secretKey.setVisible(true);
			}
		} else {
			bucketName.setText("");
			accessKey.setText("");
			secretKey.setText("");
			encryptionKey.setText("");
			password.setText("");
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
			basePath.setVisible(false);
			secretKey.setEnabled(enabled);
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

	public RadioButton geteOSCE() {
		return eOSCE;
	}
	
	public RadioButton getiOSCE() {
		return iOSCE;
	}

	private void checkPreviousValuesForBucketInformation(BucketInformationProxy bucketInformationProxy) {
		
			if(bucketInformationProxy.getBasePath().equals(basePath.getValue()) == false){
				basePath.setValue(bucketInformationProxy.getBasePath());
			}
			if(bucketInformationProxy.getBucketName().equals(bucketName.getValue()) == false){
				bucketName.setValue(bucketInformationProxy.getBucketName());
			}
			if(bucketInformationProxy.getEncryptionKey().equals(encryptionKey.getValue()) == false){
				encryptionKey.setValue(bucketInformationProxy.getEncryptionKey());
			}
			if(bucketInformationProxy.getAccessKey().equals(accessKey.getValue()) == false){
				accessKey.setValue(bucketInformationProxy.getAccessKey());
			}
			if(password.getValue().equals("") == false){
				if(bucketInformationProxy.getSecretKey().equals(password.getValue()) == false){
					password.setValue(bucketInformationProxy.getSecretKey());
				}
						
			}
			if(secretKey.getValue().equals("") == false){
				if(bucketInformationProxy.getSecretKey().equals(secretKey.getValue()) == false){
					secretKey.setValue(bucketInformationProxy.getSecretKey());
				}
						
			}
	}
}
