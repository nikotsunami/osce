package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.eOSCESyncService;
import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.eOSCESyncServiceAsync;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ImporteOSCEPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ImporteOSCEView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ImporteOSCEViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.BucketInformationProxy;
import ch.unibas.medizin.osce.client.managed.request.BucketInformationRequest;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.shared.BucketInfoType;
import ch.unibas.medizin.osce.shared.EosceStatus;
import ch.unibas.medizin.osce.shared.ExportOsceData;
import ch.unibas.medizin.osce.shared.ExportOsceType;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

@SuppressWarnings("deprecation")
public class ImporteOSCEActivity extends AbstractActivity implements ImporteOSCEView.Delegate, ImporteOSCEView.Presenter {
	
	private OsMaRequestFactory requests;
	private PlaceController placeControler;
	private AcceptsOneWidget widget;
	private ImporteOSCEView view;
	private List<CheckBox> checkBoxList = new ArrayList<CheckBox>();	
	private final OsceConstants constants = GWT.create(OsceConstants.class);	
	private eOSCESyncServiceAsync eOsceServiceAsync = null;
	
	private SemesterProxy semesterProxy;
	private HandlerManager handlerManager;
	private SelectChangeHandler removeHandler;
		
	public ImporteOSCEActivity(OsMaRequestFactory requests, PlaceController placeController, ImporteOSCEPlace place) {
    	this.requests = requests;
    	this.placeControler = placeController;
    	this.semesterProxy = place.semesterProxy;
    	this.handlerManager = place.handlerManager;
    }

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		eOsceServiceAsync = eOSCESyncService.ServiceFactory.instance();
		ImporteOSCEView systemStartView = new ImporteOSCEViewImpl();
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		
		this.addSelectChangeHandler(new SelectChangeHandler() {			
			@Override
			public void onSelectionChange(SelectChangeEvent event) {
				semesterProxy = event.getSemesterProxy();
				loadBucketInformation(semesterProxy);
				if (view.getProcessed().getValue() == true) {
					//init(view.selectedOsceType());				
					fetchProcessedFileFromLocal(view.selectedOsceType());
				}
				else if (view.getUnprocessed().getValue() == true) {
					//unprocessedClicked(view.selectedOsceType());
					fetchUnProcessedFileFromLocal(view.selectedOsceType());
				}
			}
		});
		
		loadBucketInformation(semesterProxy);
		if (view.getProcessed().getValue()) {
			fetchProcessedFileFromLocal(view.selectedOsceType());
		}
		else if (view.getUnprocessed().getValue()) {
			fetchUnProcessedFileFromLocal(view.selectedOsceType());
		}
		//init(view.selectedOsceType());
		view.setDelegate(this);
	}
	
	public void addSelectChangeHandler(SelectChangeHandler handler) 
	{
		handlerManager.addHandler(SelectChangeEvent.getType(), handler);	
		removeHandler=handler;
	}
	
	
	public void loadBucketInformation(SemesterProxy semesterProxy)
	{
		requests.bucketInformationRequestNonRoo().findBucketInformationBySemesterForImport(semesterProxy.getId()).fire(new OSCEReceiver<BucketInformationProxy>() {

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
					//System.out.println("RESPONSE FOUND");
					/*view.getBucketName().setText(response.getBucketName());
					view.getAccessKey().setText(response.getAccessKey());
					view.getSecretKey().setText(response.getSecretKey());
					if (response.getEncryptionKey() != null)
						view.getEncryptionKey().setText(response.getEncryptionKey());
					
					view.getBucketName().setEnabled(false);
					view.getAccessKey().setEnabled(false);
					view.getSecretKey().setEnabled(false);
					view.getEncryptionKey().setEnabled(false);
					
					view.getSaveEditButton().setText(constants.edit());
					view.setBucketInformationProxy(response);
					
					view.getCancelButton().setVisible(false);
					view.setBucketInformationProxy(response);
					
					view.getBucketName().setFocus(true);*/
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
					
					/*view.getBucketName().setEnabled(true);
					view.getAccessKey().setEnabled(true);
					view.getSecretKey().setEnabled(true);
					view.getEncryptionKey().setEnabled(true);
					
					view.getBucketName().setText("");
					view.getAccessKey().setText("");
					view.getSecretKey().setText("");
					view.getEncryptionKey().setText("");
					//System.out.println("RESPONSE NOT FOUND");
					view.getSaveEditButton().setText(constants.save());
					
					view.getCancelButton().setVisible(true);
					view.setBucketInformationProxy(response);
					
					view.getBucketName().setFocus(true);*/
				}
			}
		});
	}
	
	public void fetchProcessedFileFromLocal(ExportOsceType osceType) {
		try {
			showApplicationLoading(true);
			
			checkBoxList.clear();
			view.getFileListPanel().clear();
			
			eOsceServiceAsync.findProcessedFileNameFromLocal(osceType, semesterProxy.getId(), new AsyncCallback<List<ExportOsceData>>() {

				@Override
				public void onFailure(Throwable caught) {
					showApplicationLoading(false);
					MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
					messageConfirmationDialogBox.showConfirmationDialog(constants.errorImportFetch());
				}

				@Override
				public void onSuccess(List<ExportOsceData> result) {
					if (result.size() == 0) {
						Label label = new Label();
						label.setText(constants.importFilesProcessed());
						HorizontalPanel horizontalPanel = new HorizontalPanel();
						horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
						horizontalPanel.add(label);
						horizontalPanel.addStyleName("eOSCEHorizontalPanel");
						view.getFileListPanel().add(horizontalPanel);										
					}
					
					for (int i=0; i<result.size(); i++)
					{
						CheckBox checkBox = new CheckBox();
						Label label = new Label();
						label.setText(result.get(i).getFilename());
						checkBox.setFormValue(result.get(i).getFilepath());
						
						checkBoxList.add(checkBox);
						
						HorizontalPanel horizontalPanel = new HorizontalPanel();
						horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
						horizontalPanel.add(checkBox);
						horizontalPanel.add(label);
						
						horizontalPanel.addStyleName("eOSCEHorizontalPanel");
						view.getFileListPanel().add(horizontalPanel);
					}					
					
					showApplicationLoading(false);
				}
			});
		}
		catch (Exception e) {
			showApplicationLoading(false);
			Log.error(e.getMessage(), e);
		}
	}
	
	public void fetchUnProcessedFileFromLocal(ExportOsceType osceType) {
		try {
			showApplicationLoading(true);
			
			checkBoxList.clear();
			view.getFileListPanel().clear();
			
			eOsceServiceAsync.findUnProcessedFileNameFromLocal(osceType, semesterProxy.getId(), new AsyncCallback<List<ExportOsceData>>() {

				@Override
				public void onFailure(Throwable caught) {
					showApplicationLoading(false);
					MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
					messageConfirmationDialogBox.showConfirmationDialog(constants.errorImportFetch());
				}

				@Override
				public void onSuccess(List<ExportOsceData> result) {
					if (result.size() == 0) {
						Label label = new Label();
						label.setText(constants.importFilesProcessed());
						HorizontalPanel horizontalPanel = new HorizontalPanel();
						horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
						horizontalPanel.add(label);
						horizontalPanel.addStyleName("eOSCEHorizontalPanel");
						view.getFileListPanel().add(horizontalPanel);										
					}
					
					for (int i=0; i<result.size(); i++)
					{
						CheckBox checkBox = new CheckBox();
						Label label = new Label();
						label.setText(result.get(i).getFilename());
						checkBox.setFormValue(result.get(i).getFilepath());
						
						checkBoxList.add(checkBox);
						
						HorizontalPanel horizontalPanel = new HorizontalPanel();
						horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
						horizontalPanel.add(checkBox);
						horizontalPanel.add(label);
						
						horizontalPanel.addStyleName("eOSCEHorizontalPanel");
						view.getFileListPanel().add(horizontalPanel);
					}					
					
					showApplicationLoading(false);
				}
			});
		}
		catch (Exception e) {
			showApplicationLoading(false);
			Log.error(e.getMessage(), e);
		}
	}
	
	/*public void init(ExportOsceType osceType)
	{
		try
		{
			if (ExportOsceType.EOSCE.equals(osceType)) {
				showApplicationLoading(true);
				
				checkBoxList.clear();
				view.getFileListPanel().clear();
						
				eOsceServiceAsync.processedFileList(osceType, semesterProxy.getId(), new AsyncCallback<List<String>>() {
					
					@Override
					public void onSuccess(List<String> result) {
						
						if (result.size() == 0)
						{
							Label label = new Label();
							label.setText(constants.importProcessedFilesDeleted());
							//label.addStyleName("eOSCElable");
							HorizontalPanel horizontalPanel = new HorizontalPanel();
							horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
							horizontalPanel.add(label);
							horizontalPanel.addStyleName("eOSCEHorizontalPanel");
							view.getFileListPanel().add(horizontalPanel);										
						}
						
						for (int i=0; i<result.size(); i++)
						{
							CheckBox checkBox = new CheckBox();
							Label label = new Label();
							label.setText(result.get(i));
							checkBox.setFormValue(result.get(i));
							
							checkBoxList.add(checkBox);
							
							HorizontalPanel horizontalPanel = new HorizontalPanel();
							horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
							horizontalPanel.add(checkBox);
							horizontalPanel.add(label);
							
							//label.addStyleName("eOSCElable");
							horizontalPanel.addStyleName("eOSCEHorizontalPanel");
							//view.getFileListPanel().insert(horizontalPanel, view.getFileListPanel().getWidgetCount() + 1);
							view.getFileListPanel().add(horizontalPanel);
						}					
						
						showApplicationLoading(false);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						showApplicationLoading(false);
						MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
						messageConfirmationDialogBox.showConfirmationDialog(constants.errorImportFetch());
					}
				});			
			}
			else if (ExportOsceType.IOSCE.equals(osceType)) {
				view.getFileListPanel().clear();
			}
		}
		catch(Exception e)
		{
			showApplicationLoading(false);
			e.printStackTrace();
		}					
	}*/

	@Override
	public void goTo(Place place) {
		
	}

	@Override
	public void importButtonClicked(final ExportOsceType osceType, Boolean flag, BucketInfoType bucketInfoType) {
		showApplicationLoading(true);
		
		List<String> fileList = new ArrayList<String>();
		
		for (int i=0; i<checkBoxList.size(); i++)
		{
			if (checkBoxList.get(i).getValue() == true)
			{
				fileList.add(checkBoxList.get(i).getFormValue());
			}
		}
		
		System.out.println("IMPORT FILELIST SIZE : " + fileList.size());
		
		if (view.getProcessed().getValue()) {
			eOsceServiceAsync.importFileFromLocal(osceType, semesterProxy.getId(), fileList, new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
					showApplicationLoading(false);
					MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
					messageConfirmationDialogBox.showConfirmationDialog(constants.importFileError());
				}

				@Override
				public void onSuccess(Void result) {
					showApplicationLoading(false);
					MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.success());
					messageConfirmationDialogBox.showConfirmationDialog(constants.importSuccess());
					messageConfirmationDialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							if (view.getUnprocessed().getValue())
								fetchUnProcessedFileFromLocal(osceType);
							else if (view.getProcessed().getValue()) 
								fetchProcessedFileFromLocal(osceType);
						}
					});
				}
			});
		}
		else if (view.getUnprocessed().getValue()) {
			eOsceServiceAsync.importFileFromCloud(bucketInfoType, osceType, semesterProxy.getId(), fileList, flag, new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
					showApplicationLoading(false);
					MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
					messageConfirmationDialogBox.showConfirmationDialog(constants.importFileError());
				}

				@Override
				public void onSuccess(Void result) {
					showApplicationLoading(false);
					MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.success());
					messageConfirmationDialogBox.showConfirmationDialog(constants.importSuccess());
					messageConfirmationDialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							if (view.getUnprocessed().getValue())
								fetchUnProcessedFileFromLocal(osceType);
							else if (view.getProcessed().getValue()) 
								fetchProcessedFileFromLocal(osceType);
						}
					});
				}
			});
		}
		
		
			
			/*eOsceServiceAsync.importFileList(osceType, semesterProxy.getId(), fileList, flag, new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					showApplicationLoading(false);
					MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
					messageConfirmationDialogBox.showConfirmationDialog(constants.importFileError());
				}

				@Override
				public void onSuccess(Void result) {
					showApplicationLoading(false);
					MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.success());
					messageConfirmationDialogBox.showConfirmationDialog(constants.importSuccess());
					messageConfirmationDialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							if (view.getUnprocessed().getValue())
								unprocessedClicked(osceType);
							else if (view.getProcessed().getValue()) 
								init(osceType);
						}
					});
					
				//	init();
				}
			});*/
		
	}

	@Override
	public void unprocessedClicked(ExportOsceType osceType) {
		fetchUnProcessedFileFromLocal(osceType);
		/*try
		{
			if (ExportOsceType.EOSCE.equals(osceType)) {
				showApplicationLoading(true);
				
				checkBoxList.clear();
				view.getFileListPanel().clear();
				
				eOsceServiceAsync.unprocessedFileList(osceType, semesterProxy.getId(), new AsyncCallback<List<String>>() {
					
					@Override
					public void onSuccess(List<String> result) {
						
						if (result.size() == 0)
						{
							Label label = new Label();
							label.setText(constants.importProcessedMsg());
							//label.addStyleName("eOSCElable");
							HorizontalPanel horizontalPanel = new HorizontalPanel();
							horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
							horizontalPanel.add(label);
							horizontalPanel.addStyleName("eOSCEHorizontalPanel");
							view.getFileListPanel().add(horizontalPanel);
						}
						
						for (int i=0; i<result.size(); i++)
						{
							CheckBox checkBox = new CheckBox();
							Label label = new Label();
							label.setText(result.get(i));
							checkBox.setFormValue(result.get(i));
							
							checkBoxList.add(checkBox);
							
							HorizontalPanel horizontalPanel = new HorizontalPanel();
							horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
							horizontalPanel.add(checkBox);
							horizontalPanel.add(label);
							//label.addStyleName("eOSCElable");
							horizontalPanel.addStyleName("eOSCEHorizontalPanel");
							//view.getFileListPanel().insert(horizontalPanel, view.getFileListPanel().getWidgetCount() + 1);
							view.getFileListPanel().add(horizontalPanel);
						}
						
						showApplicationLoading(false);
					}
					
					
					@Override
					public void onFailure(Throwable caught) {
						showApplicationLoading(false);
						MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
						messageConfirmationDialogBox.showConfirmationDialog(constants.errorImportFetch());
					}
				});			
			}
			else if (ExportOsceType.IOSCE.equals(osceType)) {
				view.getFileListPanel().clear();
			}
			
		}
		catch(Exception e)
		{
			showApplicationLoading(false);
			e.printStackTrace();
		}*/					
	}

	@Override
	public void processedClicked(ExportOsceType osceType) {
		//init(osceType);
		fetchProcessedFileFromLocal(osceType);
	}

	@Override
	public void deleteButtonClicked(ExportOsceType osceType, BucketInfoType bucketInfoType) {
		showApplicationLoading(true);
		
		List<String> fileList = new ArrayList<String>();
		
		for (int i=0; i<checkBoxList.size(); i++)
		{
			if (checkBoxList.get(i).getValue() == true)
			{
				fileList.add(checkBoxList.get(i).getFormValue());
			}
		}		
		
		System.out.println("DELETE FILELIST SIZE : " + fileList.size());
		
		eOsceServiceAsync.deleteFileFromCloud(osceType, bucketInfoType, semesterProxy.getId(), fileList, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {	
				showApplicationLoading(false);
				MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
				messageConfirmationDialogBox.showConfirmationDialog(constants.errorImportDelete());
			}

			@Override
			public void onSuccess(Void result) {
				showApplicationLoading(false);
				MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.success());
				messageConfirmationDialogBox.showConfirmationDialog(constants.confirmationImportDelete());
				messageConfirmationDialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						fetchProcessedFileFromLocal(view.selectedOsceType());
					}
				});
				
			//	init();
			}
		});
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

	@SuppressWarnings("deprecation")
	@Override
	public void bucketSaveButtonClicked(BucketInformationProxy proxy, String bucketName, String accessKey, String secretKey, String encryptionKey, String basePath, Boolean isFTP) {
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
		
		bucketInformationProxy.setEosceStatusType(EosceStatus.Import);	
		bucketInformationProxy.setBucketName(bucketName);
		bucketInformationProxy.setAccessKey(accessKey);
		bucketInformationProxy.setSecretKey(secretKey);
		bucketInformationProxy.setEncryptionKey(encryptionKey);
		if(isFTP == true) {
			bucketInformationProxy.setBasePath(basePath);
			bucketInformationProxy.setType(BucketInfoType.FTP);
		} else {
			bucketInformationProxy.setType(BucketInfoType.S3);
		}
		
		request.persist().using(bucketInformationProxy).fire(new OSCEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				view.getBucketName().setEnabled(false);
				view.getAccessKey().setEnabled(false);
				view.getSecretKey().setEnabled(false);
				view.getEncryptionKey().setEnabled(false);
				view.getBasePath().setEnabled(false);
				
				view.getSaveEditButton().setText(constants.edit());
				view.setBucketInformationProxy(bucketInformationProxy);
			}
		});
		/*BucketInformationRequest request = requests.bucketInformationRequest();
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
		
		bucketInformationProxy.setEosceStatusType(EosceStatus.Import);
		bucketInformationProxy.setBucketName(bucketName);
		bucketInformationProxy.setAccessKey(accessKey);
		bucketInformationProxy.setSecretKey(secretKey);
		bucketInformationProxy.setEncryptionKey(encryptionKey);
		
		
		request.persist().using(bucketInformationProxy).fire(new OSCEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				view.getBucketName().setEnabled(false);
				view.getAccessKey().setEnabled(false);
				view.getSecretKey().setEnabled(false);
				view.getEncryptionKey().setEnabled(false);
				
				view.getSaveEditButton().setText(constants.edit());
				view.setBucketInformationProxy(bucketInformationProxy);
				
				if (view.getProcessed().getValue() == true)
				{
					init(view.selectedOsceType());					
				}
				else if (view.getUnprocessed().getValue() == true)
				{
					unprocessedClicked(view.selectedOsceType());
				}
			}
		});*/
	}

	public void showApplicationLoading(Boolean show) {
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(show));
	}
	
	@Override
	public void onStop() {	
		super.onStop();
		handlerManager.removeHandler(SelectChangeEvent.getType(), removeHandler);	
	}
	
	public void eOsceClicked() {
		if (view.getProcessed().getValue()) {
			//init(ExportOsceType.EOSCE);
			fetchProcessedFileFromLocal(ExportOsceType.EOSCE);
		} 
		else if (view.getUnprocessed().getValue()) {
			//unprocessedClicked(ExportOsceType.EOSCE);
			fetchUnProcessedFileFromLocal(ExportOsceType.EOSCE);
		}
	}
	 
	public void iOsceClicked() {
		if (view.getProcessed().getValue()) {
			//init(ExportOsceType.IOSCE);
			fetchProcessedFileFromLocal(ExportOsceType.IOSCE);
		} 
		else if (view.getUnprocessed().getValue()) {
			//unprocessedClicked(ExportOsceType.IOSCE);
			fetchUnProcessedFileFromLocal(ExportOsceType.IOSCE);
		} 
	}

	@Override
	public void fetchUnprocessedFilesFromCloud(ExportOsceType osceType, BucketInfoType bucketInfoType) {

		try {
			showApplicationLoading(true);
			
			checkBoxList.clear();
			view.getFileListPanel().clear();
			
			eOsceServiceAsync.findUnProcessedFilesFromCloud(bucketInfoType, osceType, semesterProxy.getId(), new AsyncCallback<List<ExportOsceData>>() {

				@Override
				public void onFailure(Throwable caught) {
					showApplicationLoading(false);
					MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
					messageConfirmationDialogBox.showConfirmationDialog(constants.errorImportFetch());
				}

				@Override
				public void onSuccess(List<ExportOsceData> result) {
					if (result.size() == 0) {
						Label label = new Label();
						label.setText(constants.importFilesProcessed());
						HorizontalPanel horizontalPanel = new HorizontalPanel();
						horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
						horizontalPanel.add(label);
						horizontalPanel.addStyleName("eOSCEHorizontalPanel");
						view.getFileListPanel().add(horizontalPanel);										
					}
					
					for (int i=0; i<result.size(); i++)
					{
						CheckBox checkBox = new CheckBox();
						Label label = new Label();
						label.setText(result.get(i).getFilename());
						checkBox.setFormValue(result.get(i).getFilepath());
						
						checkBoxList.add(checkBox);
						
						HorizontalPanel horizontalPanel = new HorizontalPanel();
						horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
						horizontalPanel.add(checkBox);
						horizontalPanel.add(label);
						
						horizontalPanel.addStyleName("eOSCEHorizontalPanel");
						view.getFileListPanel().add(horizontalPanel);
					}					
					
					showApplicationLoading(false);
				}
			});
		}
		catch (Exception e) {
			showApplicationLoading(false);
			Log.error(e.getMessage(), e);
		}
	
	}

	@Override
	public void fetchProcessedFilesFromCloud(ExportOsceType osceType, BucketInfoType bucketInfoType) {
		try {
			showApplicationLoading(true);
			
			checkBoxList.clear();
			view.getFileListPanel().clear();
			
			eOsceServiceAsync.findProcessedFilesFromCloud(bucketInfoType, osceType, semesterProxy.getId(), new AsyncCallback<List<ExportOsceData>>() {

				@Override
				public void onFailure(Throwable caught) {
					showApplicationLoading(false);
					MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
					messageConfirmationDialogBox.showConfirmationDialog(constants.errorImportFetch());
				}

				@Override
				public void onSuccess(List<ExportOsceData> result) {
					if (result.size() == 0) {
						Label label = new Label();
						label.setText(constants.importFilesProcessed());
						HorizontalPanel horizontalPanel = new HorizontalPanel();
						horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
						horizontalPanel.add(label);
						horizontalPanel.addStyleName("eOSCEHorizontalPanel");
						view.getFileListPanel().add(horizontalPanel);										
					}
					
					for (int i=0; i<result.size(); i++)
					{
						CheckBox checkBox = new CheckBox();
						Label label = new Label();
						label.setText(result.get(i).getFilename());
						checkBox.setFormValue(result.get(i).getFilepath());
						
						checkBoxList.add(checkBox);
						
						HorizontalPanel horizontalPanel = new HorizontalPanel();
						horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
						horizontalPanel.add(checkBox);
						horizontalPanel.add(label);
						
						horizontalPanel.addStyleName("eOSCEHorizontalPanel");
						view.getFileListPanel().add(horizontalPanel);
					}					
					
					showApplicationLoading(false);
				}
			});
		}
		catch (Exception e) {
			showApplicationLoading(false);
			Log.error(e.getMessage(), e);
		}
	
	
	}
}
