package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.eOSCESyncService;
import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.eOSCESyncServiceAsync;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ExportOscePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ExportOsceView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ExportOsceViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.BucketInformationProxy;
import ch.unibas.medizin.osce.client.managed.request.BucketInformationRequest;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.shared.BucketInfoType;
import ch.unibas.medizin.osce.shared.EosceStatus;
import ch.unibas.medizin.osce.shared.ExportOsceData;
import ch.unibas.medizin.osce.shared.ExportOsceType;
import ch.unibas.medizin.osce.shared.ResourceDownloadProps;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.shared.i18n.OsceConstantsWithLookup;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.http.client.URL;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

@SuppressWarnings("deprecation")
public class ExportOsceActivity extends AbstractActivity implements ExportOsceView.Delegate, ExportOsceView.Presenter {

	private OsMaRequestFactory requests;
	private PlaceController placeControler;
	private AcceptsOneWidget widget;
	private ExportOsceView view;
	private List<CheckBox> checkBoxList = new ArrayList<CheckBox>();
	private SemesterProxy semesterProxy;
	private HandlerManager handlerManager;
	private SelectChangeHandler removeHandler;
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	private eOSCESyncServiceAsync eOsceServiceAsync = null;
	private static OsceConstantsWithLookup osceConstantsWithLookup = GWT.create(OsceConstantsWithLookup.class);
	
	Boolean flag = false;
		
	public ExportOsceActivity(OsMaRequestFactory requests, PlaceController placeController, ExportOscePlace place) {
    	this.requests = requests;
    	this.placeControler = placeController;
    	this.semesterProxy = place.semesterProxy;
    	this.handlerManager = place.handlerManager;
    }

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		eOsceServiceAsync = eOSCESyncService.ServiceFactory.instance();
		ExportOsceView systemStartView = new ExportOsceViewImpl();
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		Log.info("~~CALLED~~");
		
		this.addSelectChangeHandler(new SelectChangeHandler() {			
			@Override
			public void onSelectionChange(SelectChangeEvent event) {
				semesterProxy = event.getSemesterProxy();
				loadBucketInformation(semesterProxy);
				//createView(semesterProxy.getId());
				generateXMLFile(event.getSemesterProxy().getId());			
			}
		});
		
		ApplicationLoadingScreenEvent.register(requests.getEventBus(),
				new ApplicationLoadingScreenHandler() {
					@Override
					public void onEventReceived(
							ApplicationLoadingScreenEvent event) {
						//Log.info("~~~~~~~~ApplicationLoadingScreenEvent onEventReceived Called");
						event.display();
						
					}
		});
		
		//requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		//System.out.println("SEMESTER ID : " + semesterProxy.getId());
		
		loadBucketInformation(semesterProxy);
		
		//createView(semesterProxy.getId());
		generateXMLFile(semesterProxy.getId());
		
		view.setDelegate(this);		
	}
	
	private void createView(final ExportOsceType osceType) {
		
		try
		{
			checkBoxList.clear();
			view.getFileListPanel().clear();
		
			eOsceServiceAsync.exportUnprocessedFileList(osceType, semesterProxy.getId(),new AsyncCallback<List<ExportOsceData>>() {
				@Override
				public void onFailure(Throwable caught) {
					MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
					messageConfirmationDialogBox.showConfirmationDialog(constants.exportFetchUnprocessedError());
				}

				@Override
				public void onSuccess(List<ExportOsceData> result) {
					if (result.size() == 0)
					{
						Label label = new Label();
						label.setText(constants.exportAllFilesProcessed());
						label.addStyleName("eOSCElable");
						HorizontalPanel horizontalPanel = new HorizontalPanel();
						horizontalPanel.add(label);
						horizontalPanel.addStyleName("eOSCEHorizontalPanel");
						view.getFileListPanel().add(horizontalPanel);
					}
					
					for (ExportOsceData osceData : result)
					{
						final CheckBox checkBox = new CheckBox();
						Label label = new Label();
						Anchor exportOSCEAnchor = null;
						
						if (ExportOsceType.EOSCE.equals(osceType)) {
							exportOSCEAnchor = new Anchor(constants.downloadeOSCE());
							
							exportOSCEAnchor.addClickHandler(new ClickHandler() {							
								@Override
								public void onClick(ClickEvent event) {
									downloadeOSCEFile(checkBox.getFormValue(), true);
								}
							});
							exportOSCEAnchor.setName(osceData.getOsceId().toString());
							exportOSCEAnchor.addStyleName("exportAnchor");
						}
						else if (ExportOsceType.IOSCE.equals(osceType)) {
							exportOSCEAnchor = new Anchor(constants.downloadiOSCE());
							
							exportOSCEAnchor.addClickHandler(new ClickHandler() {							
								@Override
								public void onClick(ClickEvent event) {
									downloadiOSCEFile(checkBox.getFormValue(), true);
								}							
							});
							
							exportOSCEAnchor.setName(osceData.getOsceId().toString());
							exportOSCEAnchor.addStyleName("exportAnchor");
						}
						
						label.setText(osceData.getFilename());
						checkBox.setFormValue(osceData.getOsceId().toString());
						checkBoxList.add(checkBox);
						
						HorizontalPanel horizontalPanel = new HorizontalPanel();
						horizontalPanel.setSpacing(3);
						horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
						horizontalPanel.add(checkBox);
						horizontalPanel.add(label);
						horizontalPanel.add(exportOSCEAnchor);
						horizontalPanel.add(exportOSCEAnchor);
						label.addStyleName("eOSCElable");
						horizontalPanel.addStyleName("eOSCEHorizontalPanel");
						view.getFileListPanel().add(horizontalPanel);
					}	
				}
			});
		}
		catch(Exception e)
		{
			Log.info(e.getMessage());
		}
	}

	public void loadBucketInformation(SemesterProxy semesterProxy)
	{
		requests.bucketInformationRequest().findBucketInformationBySemesterForExport(semesterProxy.getId()).fire(new OSCEReceiver<BucketInformationProxy>() {

			@Override
			public void onSuccess(BucketInformationProxy response) {
				if (response != null)
				{
					view.setBucketInformationProxy(response);
					//System.out.println("RESPONSE FOUND");
					if(BucketInfoType.FTP.equals(response.getType())) {
						/*view.getBasePath().setText(response.getBasePath());
						view.getBasePath().setEnabled(false);*/
						view.getFtp().setValue(true, true);
						view.typeValueChanged(true);
					} else {
						view.getS3().setValue(true,true);
						view.typeValueChanged(false);
					}
					
					/*view.getBucketName().setText(response.getBucketName());
					view.getAccessKey().setText(response.getAccessKey());
					view.getSecretKey().setText(response.getSecretKey());
					view.getEncryptionKey().setText(response.getEncryptionKey());
					
					view.getBucketName().setEnabled(false);
					view.getAccessKey().setEnabled(false);
					view.getSecretKey().setEnabled(false);
					view.getEncryptionKey().setEnabled(false);
					
					view.getSaveEditButton().setText(constants.edit());
					//view.setBucketInformationProxy(response);
					
					view.getCancelButton().setVisible(false);*/
					
					
				}
				else
				{
					view.setBucketInformationProxy(response);
					view.getFtp().setValue(view.getFtp().getValue(),true);
					view.getS3().setValue(view.getS3().getValue(),true);
					
					view.getBucketName().setEnabled(true);
					view.getAccessKey().setEnabled(true);
					view.getSecretKey().setEnabled(true);
					view.getEncryptionKey().setEnabled(true);
					
					if(view.getFtp().getValue()) {
						view.getBasePath().setEnabled(true);	
					} else {
						view.getBasePath().setVisible(false);
					}
					
					view.getBucketName().setText("");
					view.getAccessKey().setText("");
					view.getSecretKey().setText("");
					view.getEncryptionKey().setText("");
					view.getBasePath().setText("");
					//System.out.println("RESPONSE NOT FOUND");
					view.getSaveEditButton().setText(constants.save());
					
					view.getCancelButton().setVisible(true);
					
					view.getBucketName().setFocus(true);
				}
			}
		});
	}
	
	
	@Override
	public void onStop() {	
		super.onStop();
		handlerManager.removeHandler(SelectChangeEvent.getType(), removeHandler);	
	}
	
	public void addSelectChangeHandler(SelectChangeHandler handler) 
	{
		handlerManager.addHandler(SelectChangeEvent.getType(), handler);	
		removeHandler=handler;
	}
	
	public void generateXMLFile(final Long semesterID)
	{
		if (view.checkRadio()) {
			if (view.geteOSCE().getValue()) {
				processedFileList(ExportOsceType.EOSCE);
			} 
			else if (view.getiOSCE().getValue()) {
				processedFileList(ExportOsceType.IOSCE);
			}
			
		}			
		else {
			if (view.geteOSCE().getValue()) {
				createView(ExportOsceType.EOSCE);
			}
			else if (view.getiOSCE().getValue()) {
				createView(ExportOsceType.IOSCE);
			}
		}
			
		
		/*requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		
		eOsceServiceAsync.exportOsceFile(semesterID, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
				
				if (view.checkRadio())
					processedFileList();
				else
					createView(semesterID);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
				MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
				messageConfirmationDialogBox.showConfirmationDialog(constants.exportError());
			}
		});*/		
	}
	
	/*public void init()
	{
		try
		{
			checkBoxList.clear();
			view.getFileListPanel().clear();
		
			eOsceServiceAsync.exportUnprocessedFileList(semesterProxy.getId(),new AsyncCallback<List<String>>() {
				@Override
				public void onFailure(Throwable caught) {
					MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
					messageConfirmationDialogBox.showConfirmationDialog(constants.exportFetchUnprocessedError());
				}

				@Override
				public void onSuccess(List<String> result) {
					if (result.size() == 0)
					{
						Label label = new Label();
						label.setText(constants.exportAllFilesProcessed());
						label.addStyleName("eOSCElable");
						HorizontalPanel horizontalPanel = new HorizontalPanel();
						horizontalPanel.add(label);
						horizontalPanel.addStyleName("eOSCEHorizontalPanel");
						view.getFileListPanel().add(horizontalPanel);
					}
					
					for (int i=0; i<result.size(); i++)
					{
						CheckBox checkBox = new CheckBox();
						Label label = new Label();
						final Anchor anchor = new Anchor(constants.download());
						
						anchor.addClickHandler(new ClickHandler() {							
							@Override
							public void onClick(ClickEvent event) {
								downloadeOSCEFile(anchor.getName(), true);
							}
						});
						
						anchor.setName(result.get(i));
						
						anchor.addStyleName("exportAnchor");
						
						label.setText(result.get(i));
						label.setText(util.getFormatedString(result.get(i), 20));
						label.setTitle(result.get(i));
						
						checkBox.setFormValue(result.get(i));
						
						checkBoxList.add(checkBox);
						
						HorizontalPanel horizontalPanel = new HorizontalPanel();
						horizontalPanel.add(checkBox);
						horizontalPanel.add(label);
						horizontalPanel.add(anchor);
						label.addStyleName("eOSCElable");
						horizontalPanel.addStyleName("eOSCEHorizontalPanel");
						//view.getFileListPanel().insert(horizontalPanel, view.getFileListPanel().getWidgetCount() + 1);
						view.getFileListPanel().add(horizontalPanel);
					}	
				}
			});
		}
		catch(Exception e)
		{
			Log.info(e.getMessage());
		}
	}*/
	
	public void processedFileList(final ExportOsceType osceType)
	{
		try
		{
			checkBoxList.clear();
			view.getFileListPanel().clear();
			
			eOsceServiceAsync.exportProcessedFileList(osceType, semesterProxy.getId(),new AsyncCallback<List<ExportOsceData>>() {
				@Override
				public void onFailure(Throwable caught) {
					MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
					messageConfirmationDialogBox.showConfirmationDialog(constants.exportFetchProcessedError());
				}

				@Override
				public void onSuccess(List<ExportOsceData> result) {
					if (result.size() == 0)
					{
						Label label = new Label();
						label.setText(constants.exportProcessedExported());
						label.addStyleName("eOSCElable");
						HorizontalPanel horizontalPanel = new HorizontalPanel();
						horizontalPanel.add(label);
						horizontalPanel.addStyleName("eOSCEHorizontalPanel");
						view.getFileListPanel().add(horizontalPanel);
					}
					
					for (ExportOsceData osceData : result)
					{
						final CheckBox checkBox = new CheckBox();
						Label label = new Label();
						Anchor exportOSCEAnchor = null;
						
						if (ExportOsceType.EOSCE.equals(osceType)) {
							exportOSCEAnchor = new Anchor(constants.downloadeOSCE());
							
							exportOSCEAnchor.addClickHandler(new ClickHandler() {							
								@Override
								public void onClick(ClickEvent event) {
									downloadeOSCEFile(checkBox.getFormValue(), true);
								}
							});
							exportOSCEAnchor.setName(osceData.getOsceId().toString());
							exportOSCEAnchor.addStyleName("exportAnchor");
						}
						else if (ExportOsceType.IOSCE.equals(osceType)) {
							exportOSCEAnchor = new Anchor(constants.downloadiOSCE());
							
							exportOSCEAnchor.addClickHandler(new ClickHandler() {							
								@Override
								public void onClick(ClickEvent event) {
									downloadiOSCEFile(checkBox.getFormValue(), true);
								}							
							});
							
							exportOSCEAnchor.setName(osceData.getOsceId().toString());
							exportOSCEAnchor.addStyleName("exportAnchor");
						}
						
						label.setText(osceData.getFilename());
						checkBox.setFormValue(osceData.getOsceId().toString());
						
						checkBoxList.add(checkBox);
						
						HorizontalPanel horizontalPanel = new HorizontalPanel();
						horizontalPanel.setSpacing(3);
						horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
						horizontalPanel.add(checkBox);
						horizontalPanel.add(label);
						horizontalPanel.add(exportOSCEAnchor);
						horizontalPanel.add(exportOSCEAnchor);
						label.addStyleName("eOSCElable");
						horizontalPanel.addStyleName("eOSCEHorizontalPanel");
						view.getFileListPanel().add(horizontalPanel);
					}	
				}
			});
			
		}
		catch(Exception e)
		{
			Log.info(e.getMessage());
		}
	}
	
	public void processedClicked(ExportOsceType osceType)
	{
		processedFileList(osceType);
	}
	
	public void unprocessedClicked(ExportOsceType osceType)
	{
		//System.out.println("unprocessed clicked");
		//init();
		createView(osceType);
	}

	@Override
	public void goTo(Place place) {
	}

	@Override
	public void exporteOSCEButtonClicked(final Boolean flag) {
		
		if (semesterProxy != null)
		{
			requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
			
			List<String> fileList = new ArrayList<String>();
			
			for (int i=0; i<checkBoxList.size(); i++)
			{
				if (checkBoxList.get(i).getValue() == true)
				{
					fileList.add(checkBoxList.get(i).getFormValue());
				}
			}
			
			AsyncCallback<Void> submitCallback = new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
					if(caught.getMessage() != null){
						messageConfirmationDialogBox.showConfirmationDialog(osceConstantsWithLookup.getString(caught.getMessage()));
					} 
				}

				@Override
				public void onSuccess(Void result) {
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					
					MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.success());
					messageConfirmationDialogBox.showConfirmationDialog(constants.exportSuccess());
					
					if (view.geteOSCE().getValue()) {
						if (flag)
							unprocessedClicked(ExportOsceType.EOSCE);					
						else 
							processedClicked(ExportOsceType.EOSCE);
					} 
					else if (view.getiOSCE().getValue()) {
						if (flag)
							unprocessedClicked(ExportOsceType.IOSCE);					
						else 
							processedClicked(ExportOsceType.IOSCE);
					}
					
				}
			};
			
			if(view.getFtp().getValue()) {
				eOsceServiceAsync.putFTP(ExportOsceType.EOSCE, semesterProxy.getId(),view.getBucketName().getText(), view.getAccessKey().getText(), view.getSecretKey().getText(),view.getBasePath().getText(), fileList, flag, submitCallback);	
			} else if(view.getS3().getValue()) {
				eOsceServiceAsync.putAmazonS3Object(ExportOsceType.EOSCE, semesterProxy.getId(),view.getBucketName().getText(), view.getAccessKey().getText(), view.getSecretKey().getText(), fileList, flag, submitCallback);	
			} else {
				Log.error("Error in Export");
			}
		}		
	}
	
	@Override
	public void exportiOSCEButtonClicked(final Boolean flag) {
		
		if (semesterProxy != null)
		{
			requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
			
			List<String> fileList = new ArrayList<String>();
			
			for (int i=0; i<checkBoxList.size(); i++)
			{
				if (checkBoxList.get(i).getValue() == true)
				{
					fileList.add(checkBoxList.get(i).getFormValue());
				}
			}
			
			AsyncCallback<Void> submitCallback = new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
					if(caught.getMessage() != null){
						messageConfirmationDialogBox.showConfirmationDialog(osceConstantsWithLookup.getString(caught.getMessage()));
					} 
				}

				@Override
				public void onSuccess(Void result) {
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					
					MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.success());
					messageConfirmationDialogBox.showConfirmationDialog(constants.exportSuccess());
					
					if (view.geteOSCE().getValue()) {
						if (flag)
							unprocessedClicked(ExportOsceType.EOSCE);					
						else 
							processedClicked(ExportOsceType.EOSCE);
					} 
					else if (view.getiOSCE().getValue()) {
						if (flag)
							unprocessedClicked(ExportOsceType.IOSCE);					
						else 
							processedClicked(ExportOsceType.IOSCE);
					}
					
				}
			};
			
			if(view.getFtp().getValue()) {
				eOsceServiceAsync.putFTP(ExportOsceType.IOSCE, semesterProxy.getId(),view.getBucketName().getText(), view.getAccessKey().getText(), view.getSecretKey().getText(),view.getBasePath().getText(), fileList, flag, submitCallback);	
			} else if(view.getS3().getValue()) {
				eOsceServiceAsync.putAmazonS3Object(ExportOsceType.IOSCE, semesterProxy.getId(),view.getBucketName().getText(), view.getAccessKey().getText(), view.getSecretKey().getText(), fileList, flag, submitCallback);	
			} else {
				Log.error("Error in Export");
			}
		}		
	}
	
	@Override
	public Boolean checkSelectedValue() {
		Boolean flag = false;
		
		for (int i=0; i<checkBoxList.size(); i++)
		{
			if (checkBoxList.get(i).getValue() == true)
			{
				flag = true;
				break;
			}			
		}
		
		return flag;
	}
	
	//issue change
	public void downloadeOSCEFile(final String osceId, Boolean flag)
	{
		/*final String url=GWT.getHostPageBaseURL() + "downloadExportOsceFile?path="+filename+"&flag="+flag+"&semester=" + semesterProxy.getId();
		Window.open(url, filename, "enabled");*/
		requests.osceRequest().findOsce(Long.parseLong(osceId)).fire(new OSCEReceiver<OsceProxy>() {

			@Override
			public void onSuccess(OsceProxy osceProxy) {
				if (osceProxy.getIsFormativeOsce() != null && osceProxy.getIsFormativeOsce()) {
					MessageConfirmationDialogBox confirmDialogBox = new MessageConfirmationDialogBox(constants.error());
					confirmDialogBox.showConfirmationDialog(constants.exportFormativeOsceError());
				} 
				else {
					String ordinal = URL.encodeQueryString(String.valueOf(ResourceDownloadProps.Entity.EOSCE_XML.ordinal()));          
					String url = GWT.getHostPageBaseURL() + "downloadFile?".concat(ResourceDownloadProps.ENTITY).concat("=").concat(ordinal)
							.concat("&").concat(ResourceDownloadProps.ID).concat("=").concat(URL.encodeQueryString(osceId));
					Log.info("--> url is : " +url);
					Window.open(url, "", "");
				}
			}
		});
	}
	
	private void downloadiOSCEFile(String osceId, Boolean flag) {
		String ordinal = URL.encodeQueryString(String.valueOf(ResourceDownloadProps.Entity.IOSCE_XML.ordinal()));          
		String url = GWT.getHostPageBaseURL() + "downloadFile?".concat(ResourceDownloadProps.ENTITY).concat("=").concat(ordinal)
				.concat("&").concat(ResourceDownloadProps.ID).concat("=").concat(URL.encodeQueryString(osceId));
		Log.info("--> url is : " +url);
		Window.open(url, "", "");
	}

	@Override
	public void bucketSaveButtonClicked(BucketInformationProxy proxy, String bucketName, String accessKey, String secretKey, String password, String encryptionKey, String basePath, Boolean isFTP) {
		BucketInformationRequest request = requests.bucketInformationRequest();
		final BucketInformationProxy bucketInformationProxy;
		
		if (proxy == null)
		{	
			bucketInformationProxy = request.create(BucketInformationProxy.class);
			bucketInformationProxy.setSemester(semesterProxy);
		}
		else
		{
			bucketInformationProxy = request.edit(proxy);
			bucketInformationProxy.setSemester(proxy.getSemester());
		}
		
		bucketInformationProxy.setEosceStatusType(EosceStatus.Export);	
		bucketInformationProxy.setBucketName(bucketName);
		bucketInformationProxy.setAccessKey(accessKey);
		bucketInformationProxy.setEncryptionKey(encryptionKey);
		if(isFTP == true) {
			bucketInformationProxy.setBasePath(basePath);
			bucketInformationProxy.setType(BucketInfoType.FTP);
			bucketInformationProxy.setSecretKey(password);
		} else {
			bucketInformationProxy.setType(BucketInfoType.S3);
			bucketInformationProxy.setSecretKey(secretKey);
		}
		
		request.persist().using(bucketInformationProxy).fire(new OSCEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				view.getBucketName().setEnabled(false);
				view.getAccessKey().setEnabled(false);
				view.getSecretKey().setEnabled(false);
				view.getEncryptionKey().setEnabled(false);
				view.getBasePath().setEnabled(false);
				view.getPassword().setEnabled(false);
				
				view.getSaveEditButton().setText(constants.edit());
				view.setBucketInformationProxy(bucketInformationProxy);
			}
		});
	}

	@Override
	public void eOsceClicked() {
		if (view.getUnprocessed().getValue()) {
			createView(ExportOsceType.EOSCE);
		} 
		else if (view.getProcessed().getValue()) {
			processedClicked(ExportOsceType.EOSCE);
		}
	}

	@Override
	public void iOsceClicked() {
		if (view.getUnprocessed().getValue()) {
			createView(ExportOsceType.IOSCE);
		} 
		else if (view.getProcessed().getValue()) {
			processedClicked(ExportOsceType.IOSCE);
		}
	}

}
