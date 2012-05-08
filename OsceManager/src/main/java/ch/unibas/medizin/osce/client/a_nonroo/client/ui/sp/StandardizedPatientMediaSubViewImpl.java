package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import java.util.ArrayList;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientEditViewImpl.Binder;
import ch.unibas.medizin.osce.client.managed.request.MediaContentProxy;
import ch.unibas.medizin.osce.client.managed.request.MediaContentTypeProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;

import fr.hd3d.html5.video.client.VideoSource;
import fr.hd3d.html5.video.client.VideoWidget;
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
	FormPanel uploadFormPanel;
	@UiField
	Image uploadMessage;
	@UiField
	public TextBox id;
	@UiField
	public TextBox name;
	
	VideoWidget videoPlayer=null;
	//spec video upload
	private boolean videoFlg=false;
	public boolean isVideoFlg() {
		return videoFlg;
	}

	public void setVideoFlg(boolean videoFlg) {
		this.videoFlg = videoFlg;
	}

	
	//file upload
		@UiField
		FileUpload videoFileUpload;
		@UiField
		FormPanel videoUploadFormPanel;
		@UiField
		DivElement videoUploadMessage;
		@UiField
		public TextBox vid;
		@UiField
		public TextBox vname;
		
		@UiField
		public HorizontalPanel videoPanel;
	//spec video upload
	
	//end file upload
	private Delegate delegate;
	private Presenter presenter;

	/**
	 * Constructor of sub view
	 */
	public StandardizedPatientMediaSubViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
		//spec start
		id.setVisible(false);
		name.setVisible(false);
		vid.setVisible(false);
		vname.setVisible(false);
		//spec end
		
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
	    
	    videoFileUpload.addChangeHandler(new ChangeHandler() {
	    	@Override
	    	public void onChange(ChangeEvent event) {
	    		videoUploadFormPanel.submit();
	    	}
	    });
	   
	    //spec video upload[
	 
	    videoUploadFormPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
	    videoUploadFormPanel.setMethod(FormPanel.METHOD_POST);
	    videoUploadFormPanel.setAction(GWT.getHostPageBaseURL()+"VideoUploadServlet");
	    
	    videoUploadFormPanel.addSubmitHandler(new FormPanel.SubmitHandler() {

	        @Override
	        public void onSubmit(SubmitEvent event) {
	             if (!"".equalsIgnoreCase(videoFileUpload.getFilename())) {
	            	 
	            	 videoFlg=true;
	                 Log.info("Video UPLOADING");   
	                 }  
	             else{
	            	 videoFlg=false;
	                 Log.info("Video UPLOADING cancel");   
	                 event.cancel(); // cancel the event  
	                 } 
	             } 
	         }); 
	    //spec video upload]
	    
	    
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
                setMediaContent(event.getResults());
                delegate.uploadSuccesfull(event.getResults());
	        }
	    });
	    
	  //spec
	    videoUploadFormPanel
	    .addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
	    	
	        @Override
	        public void onSubmitComplete(SubmitCompleteEvent event) {
                Log.info("video Submit is Complete "+event.getResults() );
                setVideoMediaContent(event.getResults());
                delegate.videoUploadSuccesfull(event.getResults());
            
	        }
	    });
		id.setName("id");
		
		name.setName("name");
		
		vid.setName("vid");
		
		vname.setName("vname");
		
		
	   	 //spec
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
		uploadMessage.setUrl(description);
		int height = uploadMessage.getHeight();
		int width = uploadMessage.getWidth();
		double ratio = (double) width/height;
		
		Log.info("width, height, ratio: " + width + ", " + height + ", " + ratio);
		
		if (height > 100) {
			height = 100;
			uploadMessage.setHeight("" + height + "px");
			uploadMessage.setWidth("" + Math.round(ratio * height) + "px");
			width = uploadMessage.getWidth();
		}
		
		if (width > 200) {
			width = 200;
			uploadMessage.setHeight("" + Math.round(width/ratio) + "px");
			uploadMessage.setWidth("" + width + "px");
		}		
	}
	
	
	//spec video upload
	@Override 
	public void setVideoMediaContent(String description) {
		//spec display video
		if(description == null)
			return;
		 videoPlayer = new VideoWidget(true, true, "");
	        List<VideoSource> sources = new ArrayList<VideoSource>();
	      
	        sources.add(new VideoSource(description));
	        videoPlayer.setSources(sources);
	        videoPlayer.setPixelSize(100, 120);
	       
	        videoPanel.add(videoPlayer);
		
		//spec display video
		
	}

	//spec video upload

}
