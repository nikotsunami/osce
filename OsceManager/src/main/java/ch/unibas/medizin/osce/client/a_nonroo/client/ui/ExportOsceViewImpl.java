package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.BucketInformationProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.BucketInfoType;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
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
	Button exportButton;
	
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
	
	public ExportOsceViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		disclouserPanelFlie.addStyleName("eOsceSchedulePanelStyle");
		processed.setText(constants.exportProcessed());
		unprocessed.setText(constants.exportUnprocessed());
		exportButton.setText(constants.export());
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
		
		basePath.setVisible(false);
		basePathLbl.setVisible(false);
	}
	
	@UiHandler("unprocessed")
	public void unprocessedSelected(ClickEvent event)
	{
		disclouserPanelFlie.getHeaderTextAccessor().setText(constants.exportUnprocessed());
		delegate.unprocessedClicked();
	}
	
	@UiHandler("processed")
	public void processedSelected(ClickEvent event)
	{
		disclouserPanelFlie.getHeaderTextAccessor().setText(constants.exportProcessed());
		delegate.processedClicked();
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
	
	@UiHandler("exportButton")
	public void exportButtonClicked(ClickEvent event)
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
							delegate.exportButtonClicked(unprocessed.getValue());				
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
					delegate.exportButtonClicked(unprocessed.getValue());
				}
			}
			else 
			{
				MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
				messageConfirmationDialogBox.showConfirmationDialog(constants.exportError());
			}
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
			delegate.bucketSaveButtonClicked(bucketInformationProxy, bucketName.getText(), accessKey.getText(), secretKey.getText(),encryptionKey.getText(),basePath.getText(),ftp.getValue());
		}
		else if (saveEditButton.getText().equals(constants.edit()))
		{
			bucketName.setEnabled(true);
			accessKey.setEnabled(true);
			secretKey.setEnabled(true);
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
		basePath.setEnabled(false);
		
		saveEditButton.setText(constants.edit());
		cancelButton.setVisible(false);
	}
	
	private void setValuesToTextBoxs(boolean isFTP,boolean empty) {
		if(empty == false) {
			bucketName.setText(bucketInformationProxy.getBucketName());
			accessKey.setText(bucketInformationProxy.getAccessKey());
			secretKey.setText(bucketInformationProxy.getSecretKey());
			encryptionKey.setText(bucketInformationProxy.getEncryptionKey());
			
			if(isFTP) {
				basePath.setText(bucketInformationProxy.getBasePath());
			}	
		} else {
			bucketName.setText("");
			accessKey.setText("");
			secretKey.setText("");
			encryptionKey.setText("");
			if(isFTP) {
				basePath.setText("");
			}	
		}
		
	}

	private void enableTextBoxs(boolean enabled,boolean isFTP) {
		bucketName.setEnabled(enabled);
		accessKey.setEnabled(enabled);
		secretKey.setEnabled(enabled);
		encryptionKey.setEnabled(enabled);
		
		if(isFTP == true) {
			basePath.setEnabled(enabled);	
		} else {
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
}
