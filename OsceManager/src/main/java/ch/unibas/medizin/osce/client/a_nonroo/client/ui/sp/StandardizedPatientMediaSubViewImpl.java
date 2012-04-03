package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientEditViewImpl.Binder;
import ch.unibas.medizin.osce.client.managed.request.MediaContentProxy;
import ch.unibas.medizin.osce.client.managed.request.MediaContentTypeProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
/**
 * NOT IN USE
 * @author Pavel
 *
 */
public class StandardizedPatientMediaSubViewImpl extends Composite 
	implements StandardizedPatientMediaSubView {

	private static final Binder BINDER = GWT.create(Binder.class);

	//file upload
	@UiField
	FileUpload fileUpload;
	@UiField
	IconButton uploadButton;
	@UiField
	FormPanel uploadFormPanel;
	@UiField
	DivElement uploadMessage;
	
	//end file upload
	private Delegate delegate;
	private Presenter presenter;

	/**
	 * Constructor of sub view
	 */
	public StandardizedPatientMediaSubViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
		//ps: upload
	    uploadFormPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
	    uploadFormPanel.setMethod(FormPanel.METHOD_POST);
	    uploadFormPanel.setAction(GWT.getHostPageBaseURL()+"UploadServlet");
	    uploadFormPanel.addSubmitHandler(new FormPanel.SubmitHandler() {

	        @Override
	        public void onSubmit(SubmitEvent event) {
	             if (!"".equalsIgnoreCase(fileUpload.getFilename())) { 
	                 Log.info("PS UPLOADING");   
	                 }  
	             else{  
	                 Log.info("PS UPLOADING cancel");   
	                 event.cancel(); // cancel the event  
	                 } 
	             } 
	         }); 

	    uploadFormPanel
	    .addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
	    	
	        @Override
	        public void onSubmitComplete(SubmitCompleteEvent event) {
                Log.info("PS Submit is Complete "+event.getResults());
                uploadMessage.setInnerHTML(event.getResults());
                delegate.uploadSuccesfull(event.getResults());
	        }
	    });
	}

	//ps upload button handler
	@UiHandler("uploadButton")
	void onClickUploadButton(ClickEvent event) {
	    Log.info("You selected: " + fileUpload.getFilename());
	    uploadFormPanel.submit();
	}
	
	interface Binder extends UiBinder<Widget, StandardizedPatientMediaSubViewImpl> {
	}



	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate=delegate;
	}


	@Override
	public String getMediaContent() {
		return null;
	}

	@Override
	public void setMediaContent(String description) {
		
	}

}
