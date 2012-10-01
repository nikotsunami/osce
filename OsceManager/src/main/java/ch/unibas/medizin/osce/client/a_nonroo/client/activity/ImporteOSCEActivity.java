package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.eOSCESyncService;
import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.eOSCESyncServiceAsync;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ImporteOSCEView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ImporteOSCEViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HRElement;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class ImporteOSCEActivity extends AbstractActivity implements ImporteOSCEView.Delegate, ImporteOSCEView.Presenter {
	
	private OsMaRequestFactory requests;
	private PlaceController placeControler;
	private AcceptsOneWidget widget;
	private ImporteOSCEView view;
	private List<CheckBox> checkBoxList = new ArrayList<CheckBox>();
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	private eOSCESyncServiceAsync eOsceServiceAsync = null;
		
	public ImporteOSCEActivity(OsMaRequestFactory requests, PlaceController placeController) {
    	this.requests = requests;
    	this.placeControler = placeController;
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
		init();
		view.setDelegate(this);
	}
	
	public void init()
	{
		try
		{
			checkBoxList.clear();
			view.getFileListPanel().clear();
				
			eOsceServiceAsync.processedFileList(new AsyncCallback<List<String>>() {
				
				@Override
				public void onSuccess(List<String> result) {
					
					if (result.size() == 0)
					{
						Label label = new Label();
						label.setText(constants.importProcessedFilesDeleted());
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
				
				@Override
				public void onFailure(Throwable caught) {
					MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
					messageConfirmationDialogBox.showConfirmationDialog(constants.errorImportFetch());
				}
			});			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}					
	}

	@Override
	public void goTo(Place place) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void importButtonClicked(Boolean flag) {
		List<String> fileList = new ArrayList<String>();
		
		for (int i=0; i<checkBoxList.size(); i++)
		{
			if (checkBoxList.get(i).getValue() == true)
			{
				fileList.add(checkBoxList.get(i).getFormValue());
			}
		}
		
		System.out.println("IMPORT FILELIST SIZE : " + fileList.size());
		
		eOsceServiceAsync.importFileList(fileList, flag, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
				messageConfirmationDialogBox.showConfirmationDialog(constants.importFileError());
			}

			@Override
			public void onSuccess(Void result) {
				MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.success());
				messageConfirmationDialogBox.showConfirmationDialog(constants.importSuccess());
				unprocessedClicked();
			//	init();
			}
		});
	}

	@Override
	public void unprocessedClicked() {
		try
		{
			checkBoxList.clear();
			view.getFileListPanel().clear();
			
			eOsceServiceAsync.unprocessedFileList(new AsyncCallback<List<String>>() {
				
				@Override
				public void onSuccess(List<String> result) {
					
					if (result.size() == 0)
					{
						Label label = new Label();
						label.setText(constants.importProcessedMsg());
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
				
				@Override
				public void onFailure(Throwable caught) {
					MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
					messageConfirmationDialogBox.showConfirmationDialog(constants.errorImportFetch());
				}
			});			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}					
	}

	@Override
	public void processedClicked() {
		init();
	}

	@Override
	public void deleteButtonClicked() {
		
		List<String> fileList = new ArrayList<String>();
		
		for (int i=0; i<checkBoxList.size(); i++)
		{
			if (checkBoxList.get(i).getValue() == true)
			{
				fileList.add(checkBoxList.get(i).getFormValue());
			}
		}		
		
		System.out.println("DELETE FILELIST SIZE : " + fileList.size());
		
		eOsceServiceAsync.deleteAmzonS3Object(fileList, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {				
				MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
				messageConfirmationDialogBox.showConfirmationDialog(constants.errorImportDelete());
			}

			@Override
			public void onSuccess(Void result) {
				MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.success());
				messageConfirmationDialogBox.showConfirmationDialog(constants.confirmationImportDelete());
				processedClicked();
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

}
