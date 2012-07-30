package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ImportObjectiveViewImpl extends Composite implements ImportObjectiveView {

	private static final Binder BINDER = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, ImportObjectiveViewImpl> {
	}

	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	private Delegate delegate;
	
	private Presenter presenter;
	
	@UiField
	FileUpload fileUpload;
	
	@UiField
	FormPanel uploadFormPanel;
	
	@UiField Button importfile;
	
	@UiField 
	Label importLabel;
	
	public ImportObjectiveViewImpl() {
	
		 initWidget(BINDER.createAndBindUi(this));
		
		 uploadFormPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		 uploadFormPanel.setMethod(FormPanel.METHOD_POST);
		 uploadFormPanel.setAction(GWT.getHostPageBaseURL()+"ExcelFileImportServlet");		 
		
		 importfile.setText(constants.importObjective());
		 importLabel.setText(constants.importObjective());
		 
		 uploadFormPanel.addSubmitHandler(new FormPanel.SubmitHandler()
		    {
		        @Override
		        public void onSubmit(SubmitEvent event)
		        {
		            String fileName = fileUpload.getFilename();

		            if(fileName.length() == 0)
		            {
		            	MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
		            	dialogBox.showConfirmationDialog("Error: no file is selected. Please select a file to be uploaded.");	            	
		                //Window.alert("Error: no file is selected. Please select a file to be uploaded.");
		                event.cancel();
		            }
		            else if(!fileName.endsWith("xls") && !fileName.endsWith("xlsx"))
		            {
		            	MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
		            	dialogBox.showConfirmationDialog("Error: file format not supported. Only supports XML and XLSX");
		              //  Window.alert("Error: file format not supported. Only supports XML and XLSX");
		            	event.cancel();
		            }
		       
		        }
		    });

		 uploadFormPanel.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
		        @Override
		        public void onSubmitComplete(SubmitCompleteEvent event)
		        {
		        	MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.success());
		        	dialogBox.showConfirmationDialog("Record Inserted Successfully");
		        	Log.info("~~~Completed Successfully");
		        }
		    });
	}
	
	
	
	@UiHandler("importfile")
	public void importfileClicked(ClickEvent event)
	{
		uploadFormPanel.submit();
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
