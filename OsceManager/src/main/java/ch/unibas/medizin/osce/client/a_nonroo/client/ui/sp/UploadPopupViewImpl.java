package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;


import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class UploadPopupViewImpl extends PopupPanel  implements UploadPopupView {

	private static final Binder BINDER = GWT.create(Binder.class);
	
	private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	interface Binder extends UiBinder<Widget, UploadPopupViewImpl> {
	}
	
	private StandardizedPatientMediaSubViewImpl standardizedPatientMediaSubViewImpl;
	
	public StandardizedPatientMediaSubViewImpl getStandardizedPatientMediaSubViewImpl() {
		return standardizedPatientMediaSubViewImpl;
	}

	public void setStandardizedPatientMediaSubViewImpl(
			StandardizedPatientMediaSubViewImpl standardizedPatientMediaSubViewImpl) {
		this.standardizedPatientMediaSubViewImpl = standardizedPatientMediaSubViewImpl;
	}

	@UiField
	FileUpload fileUpload;
	
	@UiField
	FormPanel uploadFormPanel;
	
	@UiField
	public TextBox id;
	
	@UiField
	public TextBox name;
	
	@UiField
	public Label uploadLbl;
	
	UploadPopupViewImpl  uploadPopupViewImpl;
	
	private boolean isVideo=false;
	
	public boolean isVideo() {
		return isVideo;
	}

	public void setVideo(boolean isVideo) {
		this.isVideo = isVideo;
	}

	public UploadPopupViewImpl()
	{
		super(true);
		add(BINDER.createAndBindUi(this));
		uploadLbl.setText(constants.imageUpload());
		uploadPopupViewImpl=this;
		
		id.setVisible(false);
		name.setVisible(false);
		id.setName("id");		
		name.setName("name");
		//ps: upload
	    uploadFormPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
	    uploadFormPanel.setMethod(FormPanel.METHOD_POST);
	    uploadFormPanel.setAction(GWT.getHostPageBaseURL()+"UploadServlet");
	    
	    fileUpload.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				uploadFormPanel.submit();
			}
		});
	    
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
                standardizedPatientMediaSubViewImpl.setMediaContent(event.getResults());
                standardizedPatientMediaSubViewImpl.getDelegate().uploadSuccesfull(event.getResults());
                
                uploadPopupViewImpl.hide();
	        }
	    });
	}
	
	public UploadPopupViewImpl(boolean isVideo)
	{
		super(true);
		add(BINDER.createAndBindUi(this));
		
		uploadLbl.setText(constants.videoUpload());
		
		uploadPopupViewImpl=this;
		this.isVideo=isVideo;
		id.setVisible(false);
		name.setVisible(false);
		id.setName("id");		
		name.setName("name");
		//ps: upload
	    uploadFormPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
	    uploadFormPanel.setMethod(FormPanel.METHOD_POST);
	    uploadFormPanel.setAction(GWT.getHostPageBaseURL()+"VideoUploadServlet");
	    
	    fileUpload.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				uploadFormPanel.submit();
			}
		});
	    
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
                if(!uploadPopupViewImpl.isVideo)
                {
                	standardizedPatientMediaSubViewImpl.setMediaContent(event.getResults());
                	standardizedPatientMediaSubViewImpl.getDelegate().uploadSuccesfull(event.getResults());
                }
                else{
                	standardizedPatientMediaSubViewImpl.setVideoMediaContent(event.getResults());
                	standardizedPatientMediaSubViewImpl.getDelegate().videoUploadSuccesfull(event.getResults());
                }
                uploadPopupViewImpl.hide();
	        }
	    });
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
		
	}

}
