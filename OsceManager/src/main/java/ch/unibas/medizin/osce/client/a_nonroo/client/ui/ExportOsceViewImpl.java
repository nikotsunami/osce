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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.RadioButton;
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
	
	public ExportOsceViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		disclouserPanelFlie.addStyleName("schedulePanelStyle");
		processed.setText(constants.processed());
		unprocessed.setText(constants.unprocessed());
		exportButton.setText(constants.export());
		disclouserPanelFlie.getHeaderTextAccessor().setText(constants.exportprocessed());
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
		disclouserPanelFlie.getHeaderTextAccessor().setText(constants.exportunprocessed());
		delegate.unprocessedClicked();
	}
	
	@UiHandler("processed")
	public void processedSelected(ClickEvent event)
	{
		disclouserPanelFlie.getHeaderTextAccessor().setText(constants.exportprocessed());
		delegate.processedClicked();
	}
	
	@UiHandler("exportButton")
	public void exportButtonClicked(ClickEvent event)
	{
		Boolean flag = delegate.checkSelectedValue();
		if (flag)
		{
			if (unprocessed.getValue() == false)
			{
				final MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
				messageConfirmationDialogBox.showYesNoDialog(constants.exportwarning());
				
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
