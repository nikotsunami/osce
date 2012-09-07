package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
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
		
	public ImporteOSCEViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		disclouserPanelFlie.addStyleName("schedulePanelStyle");
		processed.setText(constants.processed());
		unprocessed.setText(constants.unprocessed());
		importButton.setText(constants.importBtn());
		delButton.setText(constants.delete());
		delButton.setEnabled(true);
		importButton.setEnabled(false);
		disclouserPanelFlie.getHeaderTextAccessor().setText(constants.processed());
		disclouserPanelFlie.addCloseHandler(new CloseHandler<DisclosurePanel>() {
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				disclouserPanelFlie.setOpen(true);
			}
		});
	}	
	
	@UiHandler("unprocessed")
	public void unprocessedSelected(ClickEvent event)
	{
		disclouserPanelFlie.getHeaderTextAccessor().setText(constants.unprocessed());
		delButton.setEnabled(false);
		importButton.setEnabled(true);
		delegate.unprocessedClicked();
	}
	
	@UiHandler("processed")
	public void processedSelected(ClickEvent event)
	{
		disclouserPanelFlie.getHeaderTextAccessor().setText(constants.processed());
		delButton.setEnabled(true);
		importButton.setEnabled(false);
		delegate.processedClicked();
	}
	
	@UiHandler("importButton")
	public void importButtonClicked(ClickEvent event)
	{
		Boolean test = delegate.checkSelectedValue();
		
		if (test)
		{
			final MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.confirmation());
			messageConfirmationDialogBox.showYesNoDialog(constants.importprocessmsg());
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
			MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
			messageConfirmationDialogBox.showConfirmationDialog(constants.eosceerrormsg());
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
			MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
			messageConfirmationDialogBox.showConfirmationDialog(constants.eosceerrormsg());
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
	
}
