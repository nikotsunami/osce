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
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

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
		
		generateXMLFile(semesterProxy.getId());
		
		view.setDelegate(this);		
	}
	
	public void loadBucketInformation(SemesterProxy semesterProxy)
	{
		requests.bucketInformationRequestNonRoo().findBucketInformationBySemester(semesterProxy.getId()).fire(new OSCEReceiver<BucketInformationProxy>() {

			@Override
			public void onSuccess(BucketInformationProxy response) {
				if (response != null)
				{
					//System.out.println("RESPONSE FOUND");
					view.getBucketName().setText(response.getBucketName());
					view.getAccessKey().setText(response.getAccessKey());
					view.getSecretKey().setText(response.getSecretKey());
					
					view.getBucketName().setEnabled(false);
					view.getAccessKey().setEnabled(false);
					view.getSecretKey().setEnabled(false);
					
					view.getSaveEditButton().setText(constants.edit());
					view.setBucketInformationProxy(response);
					
					view.getCancelButton().setVisible(false);
				}
				else
				{
					view.getBucketName().setText("");
					view.getAccessKey().setText("");
					view.getSecretKey().setText("");
					//System.out.println("RESPONSE NOT FOUND");
					view.getSaveEditButton().setText(constants.save());
					
					view.getCancelButton().setVisible(true);
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
	
	public void generateXMLFile(Long semesterID)
	{
		
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		
		eOsceServiceAsync.exportOsceFile(semesterID, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
				
				if (view.checkRadio())
					processedFileList();
				else
					init();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
				MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
				messageConfirmationDialogBox.showConfirmationDialog(constants.exportError());
			}
		});
		
		
	}
	
	public void init()
	{
		try
		{
			checkBoxList.clear();
			view.getFileListPanel().clear();
		
			eOsceServiceAsync.exportUnprocessedFileList(new AsyncCallback<List<String>>() {
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
								downloadFile(anchor.getName(), true);
							}
						});
						
						anchor.setName(result.get(i));
						
						anchor.addStyleName("exportAnchor");
						
						label.setText(result.get(i));
						/*label.setText(util.getFormatedString(result.get(i), 20));
						label.setTitle(result.get(i));*/
						
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
	}
	
	public void processedFileList()
	{
		try
		{
			checkBoxList.clear();
			view.getFileListPanel().clear();
			
			eOsceServiceAsync.exportProcessedFileList(new AsyncCallback<List<String>>() {
				@Override
				public void onFailure(Throwable caught) {
					MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
					messageConfirmationDialogBox.showConfirmationDialog(constants.exportFetchProcessedError());
				}

				@Override
				public void onSuccess(List<String> result) {
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
					
					for (int i=0; i<result.size(); i++)
					{
						CheckBox checkBox = new CheckBox();
						Label label = new Label();
						
						final Anchor anchor = new Anchor(constants.download());
						
						anchor.addClickHandler(new ClickHandler() {							
							@Override
							public void onClick(ClickEvent event) {
								downloadFile(anchor.getName(), false);
							}
						});
						
						anchor.setName(result.get(i));
						
						anchor.addStyleName("exportAnchor");
						
						
						label.setText(result.get(i));
						/*label.setText(util.getFormatedString(result.get(i), 20));
						label.setTitle(result.get(i));*/
						
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
	}
	
	public void processedClicked()
	{
		processedFileList();
	}
	
	public void unprocessedClicked()
	{
		//System.out.println("unprocessed clicked");
		init();
	}

	@Override
	public void goTo(Place place) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exportButtonClicked(final Boolean flag) {
		
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
			
			eOsceServiceAsync.putAmazonS3Object(view.getBucketName().getText(), view.getAccessKey().getText(), view.getSecretKey().getText(), fileList, flag, new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
					messageConfirmationDialogBox.showConfirmationDialog(caught.getMessage());
				}

				@Override
				public void onSuccess(Void result) {
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					
					MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.success());
					messageConfirmationDialogBox.showConfirmationDialog(constants.exportSuccess());
					
					if (flag)
						unprocessedClicked();					
					else 
						processedClicked();
				}
			});
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
	public void downloadFile(String filename, Boolean flag)
	{
		final String url=GWT.getHostPageBaseURL() + "downloadExportOsceFile?path="+filename+"&flag="+flag;
		Window.open(url, filename, "enabled");
	}

	@Override
	public void bucketSaveButtonClicked(BucketInformationProxy proxy, String bucketName, String accessKey, String secretKey) {
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
		
			
		bucketInformationProxy.setBucketName(bucketName);
		bucketInformationProxy.setAccessKey(accessKey);
		bucketInformationProxy.setSecretKey(secretKey);
		
		
		request.persist().using(bucketInformationProxy).fire(new OSCEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				view.getBucketName().setEnabled(false);
				view.getAccessKey().setEnabled(false);
				view.getSecretKey().setEnabled(false);
				
				view.getSaveEditButton().setText(constants.edit());
				view.setBucketInformationProxy(bucketInformationProxy);
			}
		});
	}
}
