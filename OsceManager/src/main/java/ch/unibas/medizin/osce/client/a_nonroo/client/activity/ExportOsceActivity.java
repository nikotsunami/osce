package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.eOSCESyncService;
import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.eOSCESyncServiceAsync;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ExportOscePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ExportOsceView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ExportOsceViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
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
		
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		
		generateXMLFile(semesterProxy.getId());
		
		view.setDelegate(this);		
	}
	
	public void addSelectChangeHandler(SelectChangeHandler handler) 
	{
		handlerManager.addHandler(SelectChangeEvent.getType(), handler);	
	}
	
	public void generateXMLFile(Long semesterID)
	{
		
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		
		eOsceServiceAsync.exportOsceFile(semesterID, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				if (view.checkRadio())
					processedFileList();
				else
					init();
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
				MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
				messageConfirmationDialogBox.showConfirmationDialog(constants.exporterror());
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
					messageConfirmationDialogBox.showConfirmationDialog(constants.exportunprocessederror());
				}

				@Override
				public void onSuccess(List<String> result) {
					if (result.size() == 0)
					{
						Label label = new Label();
						label.setText(constants.exportprocessedmsg());
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
						label.setText(result.get(i));
						checkBox.setFormValue(result.get(i));
						
						checkBoxList.add(checkBox);
						
						HorizontalPanel horizontalPanel = new HorizontalPanel();
						horizontalPanel.add(checkBox);
						horizontalPanel.add(label);
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
					messageConfirmationDialogBox.showConfirmationDialog(constants.exportprocessederror());
				}

				@Override
				public void onSuccess(List<String> result) {
					if (result.size() == 0)
					{
						Label label = new Label();
						label.setText(constants.exportprocessedlabel());
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
						label.setText(result.get(i));
						checkBox.setFormValue(result.get(i));
						
						checkBoxList.add(checkBox);
						
						HorizontalPanel horizontalPanel = new HorizontalPanel();
						horizontalPanel.add(checkBox);
						horizontalPanel.add(label);
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
		
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		
		List<String> fileList = new ArrayList<String>();
		
		for (int i=0; i<checkBoxList.size(); i++)
		{
			if (checkBoxList.get(i).getValue() == true)
			{
				fileList.add(checkBoxList.get(i).getFormValue());
			}
		}
		
		System.out.println("EXPORT FILELIST SIZE : " + fileList.size());
		
		eOsceServiceAsync.putAmazonS3Object(fileList, flag, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
				MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
				messageConfirmationDialogBox.showConfirmationDialog(constants.exportunprocessederror());
			}

			@Override
			public void onSuccess(Void result) {
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
				
				MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.success());
				messageConfirmationDialogBox.showConfirmationDialog(constants.exportsuccess());
				
				System.out.println("FLAG : " + flag);
				
				if (flag)
					unprocessedClicked();					
				else 
					processedClicked();
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
}
