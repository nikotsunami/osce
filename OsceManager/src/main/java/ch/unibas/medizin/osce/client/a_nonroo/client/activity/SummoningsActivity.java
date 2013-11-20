package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.unibas.medizin.osce.client.SummoningsService;
import ch.unibas.medizin.osce.client.SummoningsServiceAsync;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.SummoningsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.SummoningsPopupViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.SummoningsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.SummoningsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.shared.ResourceDownloadProps;
import ch.unibas.medizin.osce.shared.util;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;

/**
 * @author dk
 *
 */
@SuppressWarnings("deprecation")
public class SummoningsActivity extends AbstractActivity implements SummoningsView.Presenter, SummoningsView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private SummoningsView view;
	
	private SummoningsPopupViewImpl popupView;
	
	private Button sendMailButton;
	private Button saveTemplateButton;
	private Button restoreTemplateButton;
	
	private final OsceConstants constants;
	
	List<CheckBox> lstChkSp;
	List<CheckBox> lstChkExaminor;
	CheckBox chkAllSP;
	CheckBox chkAllStud;
	CheckBox chkAllExaminor;
	
	private SummoningsPlace place;
	private SummoningsActivity activity;
	
	private SelectChangeHandler removeHandler;
	public HandlerManager handlerManager;
	private SemesterProxy semesterProxy;
	
	
	private MessageConfirmationDialogBox confirmationDialogBox;
	
	private final SummoningsServiceAsync summoningsServiceAsync = GWT.create(SummoningsService.class);

	public SummoningsActivity(SummoningsPlace place,OsMaRequestFactory requests, PlaceController placeController) {
    	Log.info("in summoningsActivity");
    	Log.info("Proxy: " + place.semesterProxy.getCalYear());
    	
		this.place = place;
		this.activity = this;
		this.requests = requests;
    	this.placeController = placeController;
    	this.handlerManager = place.handlerManager;
    	constants = GWT.create(OsceConstants.class);
    	semesterProxy = place.semesterProxy;

    	ApplicationLoadingScreenEvent.initialCounter();
		ApplicationLoadingScreenEvent.register(requests.getEventBus(),
				new ApplicationLoadingScreenHandler() {
					@Override
					public void onEventReceived(
							ApplicationLoadingScreenEvent event) {
						Log.info("ApplicationLoadingScreenEvent onEventReceived Called");
						event.display();
					}
				});
    	
    	this.addSelectChangeHandler(new SelectChangeHandler() 
		{			
			@Override
			public void onSelectionChange(SelectChangeEvent event) 
			{			
				Log.info("Call Summonings Activity");					
				Log.info("onSelectionChange Get Semester: " + event.getSemesterProxy().getCalYear());		
				semesterProxy= event.getSemesterProxy();
				
				init();
			}
		});
    }

	@SuppressWarnings("unchecked")
	public void addSelectChangeHandler(SelectChangeHandler handler) {
		Log.info("SelectChangeEvent.getType = ="+SelectChangeEvent.getType());
		handlerManager.addHandler(SelectChangeEvent.getType(), handler);
		removeHandler=handler;
		
	}
	
	public void onStop(){
		handlerManager.removeHandler(SelectChangeEvent.getType(), removeHandler);
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		SummoningsView systemStartView = new SummoningsViewImpl();
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		
		MenuClickEvent.register(requests.getEventBus(), (SummoningsViewImpl)view);
		
		widget.setWidget(systemStartView.asWidget());
		
		init();

		view.setDelegate(this);
	}
	
	
	
	@SuppressWarnings("deprecation")
	private void init() {
		Log.info("Patient in Semester : "+place.getSemesterProxy().getId());
		
		lstChkSp=new ArrayList<CheckBox>(0);
		lstChkExaminor=new ArrayList<CheckBox>(0);
		  
		requests.assignmentRequestNonRoo().findAssignedExaminer(semesterProxy.getId()).fire(new OSCEReceiver<List<DoctorProxy>>() {

			@Override
			public void onSuccess(List<DoctorProxy> response) {
				
				Log.info("Get List of Examinor(Doctor) with Size: " + response.size());
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));

				Iterator<DoctorProxy> iteratorExaminor=response.iterator();		
				if(response.size()>0)
				{
					chkAllExaminor=new CheckBox(constants.all());
					
					view.getVpExaminor().clear();
					
					view.getVpExaminor().insert(chkAllExaminor, view.getVpExaminor().getWidgetCount());
					
					chkAllExaminor.addClickHandler(new ClickHandler() 
					{
						@Override
						public void onClick(ClickEvent event) 
						{
							Log.info("Select/Deselect All Examinor");
							//checkedAllExaminor();
							checkedUncheckedAll(lstChkExaminor,chkAllExaminor);
						}

						
					});
					
					while(iteratorExaminor.hasNext())
					{
						Log.info("Panel Widget: " + view.getVpExaminor().getWidgetCount());
						
						final CheckBox chkExaminor=new CheckBox();							
						DoctorProxy s =iteratorExaminor.next();						
						
						chkExaminor.setText(util.getEmptyIfNull(s.getName())+" " +util.getEmptyIfNull(s.getPreName()));
						chkExaminor.setFormValue(s.getId().toString());
						
						lstChkExaminor.add(chkExaminor);
						
						view.getVpExaminor().insert(chkExaminor, view.getVpExaminor().getWidgetCount());
						chkExaminor.addClickHandler(new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) 
							{
								if(chkExaminor.isChecked()==false && chkAllExaminor.isChecked()==true)
								{
									chkAllExaminor.setChecked(false);	
									view.getExaminerRb().setChecked(true);
								}
							}
						});
					}
					
					view.getExaminerRb().addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							//Log.info("~~~Total Widget view.getVpSP().getWidgetCount()"+ view.getVpStudent().getWidgetCount());
							if(view.getVpExaminor().getWidgetCount()>0) {
								Log.info("True");
								if(chkAllExaminor.isChecked()==true ) {
									view.getExaminerAllRb().setChecked(true);
								}
							}
							else {
								Log.info("False");
							}
						}
					});
					
				}else{
					
					view.getVpExaminor().clear();
				}
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
				
			}
		});
		
		  
		requests.assignmentRequestNonRoo().findAssignedSP(semesterProxy.getId()).fire(new OSCEReceiver<List<StandardizedPatientProxy>>() {
					
					@Override
					public void onSuccess(List<StandardizedPatientProxy> response) {
						
						Log.info("Get List of SP with Size: " + response.size());
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
						
						Iterator<StandardizedPatientProxy> iteratorSP=response.iterator();												
						if(response.size()>0)
						{
							chkAllSP=new CheckBox(constants.all());
							
							view.getVpSP().clear();
							view.getVpSP().insert(chkAllSP, view.getVpSP().getWidgetCount());
														
							chkAllSP.addClickHandler(new ClickHandler() 
							{
								@Override
								public void onClick(ClickEvent event) 
								{
									Log.info("Select/Deselect All SP");									
									checkedUncheckedAll(lstChkSp,chkAllSP);
								}								
							});
							
							while(iteratorSP.hasNext())
							{
								Log.info("Panel Widget: " + view.getVpSP().getWidgetCount());
								
								final CheckBox chkSP=new CheckBox();																					
								
								StandardizedPatientProxy sp =iteratorSP.next();		
								
								lstChkSp.add(chkSP);
								
								chkSP.setText(util.getEmptyIfNull(sp.getName())+" " +util.getEmptyIfNull(sp.getPreName()));							
								chkSP.setFormValue(sp.getId().toString());
								view.getVpSP().insert(chkSP, view.getVpSP().getWidgetCount());	
								chkSP.addClickHandler(new ClickHandler() {
									
									@Override
									public void onClick(ClickEvent event) 
									{
										if(chkSP.isChecked()==false && chkAllSP.isChecked()==true)
										{
											chkAllSP.setChecked(false);	
											view.getSpRb().setChecked(true);
										}
									}
								});
								
							}

							view.getSpRb().addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									
									if(view.getVpSP().getWidgetCount()>0) {
										Log.info("True");
										if(chkAllSP.isChecked()==true ) {
											view.getSpAllRb().setChecked(true);
										}
									}
									else {
										Log.info("False");
									}
								}
							});
							
						}else{
							view.getVpSP().clear();
						}
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
						
					}
					
				});

	}
	
	
	@SuppressWarnings("deprecation")
	private void checkedUncheckedAll(List<CheckBox> list, CheckBox chkbox) 
	{
		Log.info("Call checkedUncheckedAll");		
		if(list.size()>0)
		{
				if(chkbox.isChecked()==true)
				{			
					Log.info("Select All");
					
						if(chkbox.equals(chkAllSP))
						{							
							view.getSpAllRb().setChecked(true);
						}
						else if(chkbox.equals(chkAllExaminor))
						{							
							view.getExaminerAllRb().setChecked(true);
						}
						
					
					Iterator<CheckBox> tempIterator=list.iterator();
					while(tempIterator.hasNext())
					{																
						tempIterator.next().setChecked(true);
					}						
				}
				else if(chkbox.isChecked()==false)
				{
					Log.info("Deselect All");
					
					if(chkbox.equals(chkAllSP))
					{						
						view.getSpRb().setChecked(true);
					}
					else if(chkbox.equals(chkAllExaminor))
					{						
						view.getExaminerRb().setChecked(true);
					}
					
					
					Iterator<CheckBox> tempIterator=list.iterator();
					while(tempIterator.hasNext())
					{																
						tempIterator.next().setChecked(false);
					}
				}
		}			
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void clickAllSP() {
		
		Log.info("Call printAllSP.");
		if(lstChkSp.size()>0)
		{
			chkAllSP.setChecked(true);
			Iterator<CheckBox> tempIterator=lstChkSp.iterator();
			while(tempIterator.hasNext())
			{																
				tempIterator.next().setChecked(true);
			}
		}	
	}

	@SuppressWarnings("deprecation")
	@Override
	public void clickAllExaminor() {
		
		Log.info("Call printAllExaminor.");
		if(lstChkExaminor.size()>0)
		{
			chkAllExaminor.setChecked(true);
			Iterator<CheckBox> tempIterator=lstChkExaminor.iterator();
			while(tempIterator.hasNext())
			{																
				tempIterator.next().setChecked(true);
			}
		}			
	}

	@Override
	public void clickSelectedSP() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clickSelectedExaminor() {
		// TODO Auto-generated method stub
		
	}

	private boolean checkSelection(List<CheckBox> chkBoxList){
		
		for(CheckBox checkBox : chkBoxList){
			if(checkBox.getValue())
				return true;
		}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void sendEmailtoSP(ClickEvent event) 
	{
		final List<Long> spIds = new ArrayList<Long>(0);
		
		if(lstChkSp.size()>0 && checkSelection(lstChkSp)) // Check that SP is Checked Or Not
		{
			Log.info("Total SP : " + lstChkSp.size());										
			int i=0;
			for(Iterator<CheckBox> iterator=lstChkSp.iterator();iterator.hasNext();i++)
			{					
				if(iterator.next().isChecked()==true)
				{
					Log.info("~~Selected Checkbox Id: " + i);
					Log.info("~~Selected Checkbox Value: " + lstChkSp.get(i));
					Log.info("~~Selected Checkbox Value: " + lstChkSp.get(i).getFormValue());
					
					spIds.add(Long.parseLong(lstChkSp.get(i).getFormValue()));
					
				}
			}
			
			popupView = new SummoningsPopupViewImpl();

			Boolean isExaminer = false;
			Boolean isEmail = true;
			loadDefaultTemplate();
			loadTemplateFileNameList(isExaminer,isEmail,null);
			handleLoadTempateEvent(isExaminer,isEmail);
		
			sendMailButton = popupView.getSendMailButton();
			sendMailButton.setText(constants.summoningsSend());
			handleSendButtonEventForSPMail(isExaminer,isEmail, spIds);
			saveTemplateButton = popupView.getSaveTemplateButton();
			saveTemplateEventHandler(isExaminer, isEmail);
			restoreTemplateButton = popupView.getRestoreTemplateButton();
			restoreTemplateEventHandler(isExaminer, isEmail);
	//		popupView.setAnimationEnabled(true);
			popupView.center();
			
			/*requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
			popupView = new SummoningsPopupViewImpl();
			summoningsServiceAsync.getTemplateContent(semesterProxy.getId().toString(),false,true, 
				new AsyncCallback<String[]>() {
					
					@Override
					public void onSuccess(String[] response) {
						
						popupView.setMessageContent(response[1]);
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					}
					
					@Override
					public void onFailure(Throwable throwable) {
						Log.error("ERROR : "+throwable.getMessage());
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					}
				});
		
		loadSemesterList();
		
		popupView.getLoadTemplateButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				int selectedIndex = popupView.getSemesterList().getSelectedIndex();
				String selectedValue = popupView.getSemesterList().getValue(selectedIndex);
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));  
					summoningsServiceAsync.getTemplateContent(selectedValue,false,true, 
						new AsyncCallback<String[]>() {
							
							@Override
							public void onSuccess(String[] response) {
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								if(response[2].equals("found")){
									
									popupView.setMessageContent(response[1]);
									
								}else{
									
									Log.info("Error loading template");
									
									confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
									confirmationDialogBox.showConfirmationDialog(constants.errorTplNotFound());
								}
								
							}
							
							@Override
							public void onFailure(Throwable throwable) {
								Log.error("ERROR : "+throwable.getMessage());
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							}
						});
				
			}
		});
		
		sendMailButton = popupView.getSendMailButton();
		sendMailButton.setText(constants.summoningsSend());
		sendMailButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
				summoningsServiceAsync.saveTemplate(semesterProxy.getId().toString(),false,true, popupView.getMessageContent(), new AsyncCallback<Boolean>() {
					@Override
					public void onSuccess(Boolean result) {
						if(result){
							Log.info("Template saved successfully.");
							
							summoningsServiceAsync.sendSPMail(semesterProxy.getId(),spIds, new AsyncCallback<Boolean>() {
								
								@Override
								public void onSuccess(Boolean result) {
									
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									if(result){
										popupView.hide();
										Log.info("Mail Sent Successfully");
										
										confirmationDialogBox = new MessageConfirmationDialogBox(constants.success());
										confirmationDialogBox.showConfirmationDialog(constants.confirmationMailSent());
									}else{
										Log.info("Error sending email");
										
										confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
										confirmationDialogBox.showConfirmationDialog(constants.errorMailSend());
									}
								}
								
								@Override
								public void onFailure(Throwable caught) {
									Log.error("ERROR : "+caught.getMessage());
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								}
							});
						
						}else if(!result){
							
							Log.info("Error saving template : Default template path is not set.");
							confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
							confirmationDialogBox.showConfirmationDialog(constants.errorInvalidTplPath());
							
						}else{
							Log.info("Error saving template");
							
							confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
							confirmationDialogBox.showConfirmationDialog(constants.errorTplSave());
						}
							
						
					}
					
					@Override
					public void onFailure(Throwable throwable) {
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
						Log.error("ERROR : "+throwable.getMessage());
					}
				});
			}
		});
		
		saveTemplateButton = popupView.getSaveTemplateButton();
		saveTemplateButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
//				summoningsServiceAsync.saveTemplate("email\\emailTemplate_SP"+semesterProxy.getId().toString()+".txt", popupView.getMessageContent(), 
			//Feature : 154
				summoningsServiceAsync.saveTemplate(semesterProxy.getId().toString(),false,true, popupView.getMessageContent(), 
						new AsyncCallback<Boolean>() {
							
							@Override
							public void onSuccess(Boolean result) {
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								if(result){
									Log.info("Template saved successfully.");
									
									confirmationDialogBox = new MessageConfirmationDialogBox(constants.success());
									confirmationDialogBox.showConfirmationDialog(constants.confirmationTplSaved());
								}else if(!result){
									
									Log.info("Error saving template : Default template path is not set.");
									confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
									confirmationDialogBox.showConfirmationDialog(constants.errorInvalidTplPath());
									
								}else{
									Log.info("Error saving template");
									
									confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
									confirmationDialogBox.showConfirmationDialog(constants.errorTplSave());
								}
									
								
							}
							
							@Override
							public void onFailure(Throwable throwable) {
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								Log.error("ERROR : "+throwable.getMessage());
							}
						});
			}
		});
		
		restoreTemplateButton = popupView.getRestoreTemplateButton();
		restoreTemplateButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
				//summoningsServiceAsync.deleteTemplate("email\\emailTemplate_SP"+semesterProxy.getId().toString()+".txt",
			//Feature : 154

summoningsServiceAsync.deleteTemplate(semesterProxy.getId().toString(),false,true,
						new AsyncCallback<Boolean>() {
							
							@Override
							public void onSuccess(Boolean result) {
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								if(result){
									
									Log.info("Template restored successfully.");
									
									confirmationDialogBox = new MessageConfirmationDialogBox(constants.success());
									confirmationDialogBox.showConfirmationDialog(constants.confirmationTplRestore());
									
			//Feature : 154
									summoningsServiceAsync.getTemplateContent(semesterProxy.getId().toString(),false,true,  
											new AsyncCallback<String[]>() {
												
												@Override
												public void onSuccess(String[] response) {
														
													popupView.setMessageContent(response[1]);
													
												}
												
												@Override
												public void onFailure(Throwable caught) {
													
													Log.error("ERROR : "+caught.getMessage());
												}
											});
								}else{
									Log.info("Error restoring template");
									
									confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
									confirmationDialogBox.showConfirmationDialog(constants.errorTplRestore());
								}
								
								
							}
							
							@Override
							public void onFailure(Throwable throwable) {
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								Log.error("ERROR : "+throwable.getMessage());
							}
						});
			}
		});
		
//		popupView.setAnimationEnabled(true);
		popupView.center();*/
		
		}else{
			Log.info("No checkbox selected.");

			confirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
			confirmationDialogBox.showConfirmationDialog(constants.warningSelectSp());
		}
	}

	/*@SuppressWarnings("deprecation")
	private void loadSemesterList() {
		popupView.getSemesterList().clear();
		popupView.getSemesterList().addItem(constants.select(), "0");
		
		requests.semesterRequest().findAllSemesters().fire(new OSCEReceiver<List<SemesterProxy>>() {

			@Override
			public void onSuccess(List<SemesterProxy> response) {
				
				for(SemesterProxy semesterProxy : response){
					popupView.getSemesterList().addItem(new EnumRenderer<Semesters>().render(semesterProxy.getSemester()) 
							+ " " + semesterProxy.getCalYear(),semesterProxy.getId().toString());
				}
			}
		});
	}*/

	@SuppressWarnings("deprecation")
	@Override
	public void sendEmailtoExaminor() {
		
		final List<Long> examinerIds = new ArrayList<Long>(0);
		
		if(lstChkExaminor.size()>0 && checkSelection(lstChkExaminor)) // Check that Examiner is Checked Or Not
		{
			Log.info("Total Examinor : " + lstChkExaminor.size());										
			int i=0;
			for(Iterator<CheckBox> iterator=lstChkExaminor.iterator();iterator.hasNext();i++)
			{					
				if(iterator.next().isChecked()==true)
				{
					Log.info("~~Selected Checkbox Id: " + i);
					Log.info("~~Selected Checkbox Value: " + lstChkExaminor.get(i));
					Log.info("~~Selected Checkbox Value: " + lstChkExaminor.get(i).getFormValue());
					
					examinerIds.add(Long.parseLong(lstChkExaminor.get(i).getFormValue()));
					
				}
			}
		
			popupView = new SummoningsPopupViewImpl();
			Boolean isExaminer = true;
			Boolean isEmail = true;
			loadDefaultTemplate();
			loadTemplateFileNameList(isExaminer,isEmail,null);
			handleLoadTempateEvent(isExaminer,isEmail);
		
			sendMailButton = popupView.getSendMailButton();
			sendMailButton.setText(constants.summoningsSend());
			handleSendButtonEventForExaminorMail(isExaminer,isEmail, examinerIds);
			saveTemplateButton = popupView.getSaveTemplateButton();
			saveTemplateEventHandler(isExaminer, isEmail);
			restoreTemplateButton = popupView.getRestoreTemplateButton();
			restoreTemplateEventHandler(isExaminer, isEmail);
	//		popupView.setAnimationEnabled(true);
			popupView.center();
			
		/*popupView = new SummoningsPopupViewImpl();
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
			//Feature : 154
//		summoningsServiceAsync.getTemplateContent("email\\emailTemplate_Ex"+semesterProxy.getId().toString()+".txt", 

		summoningsServiceAsync.getTemplateContent(semesterProxy.getId().toString(),true,true,  

				new AsyncCallback<String[]>() {
					
					@Override
					public void onSuccess(String[] response) {
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
						popupView.setMessageContent(response[1]);
					
					}
					
					@Override
					public void onFailure(Throwable caught) {
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
						Log.error("ERROR : "+caught.getMessage());
					}
				});
		
		loadSemesterList();
		
		popupView.getLoadTemplateButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				int selectedIndex = popupView.getSemesterList().getSelectedIndex();
				String selectedValue = popupView.getSemesterList().getValue(selectedIndex);
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
			//Feature : 154
//				summoningsServiceAsync.getTemplateContent("email\\emailTemplate_Ex"+selectedValue+".txt", 



				summoningsServiceAsync.getTemplateContent(selectedValue,true,true, 
						new AsyncCallback<String[]>() {
							
							@Override
							public void onSuccess(String[] response) {
								
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								if(response[2].equals("found")){
									
									popupView.setMessageContent(response[1]);
								}else{
									
									Log.info("Error loading template");
									
									confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
									confirmationDialogBox.showConfirmationDialog(constants.warningTemplateNotFound());
								}
							}
							
							@Override
							public void onFailure(Throwable throwable) {
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								Log.error("ERROR : "+throwable.getMessage());
							}
						});
			}
		});
		
		sendMailButton = popupView.getSendMailButton();
		sendMailButton.setText(constants.summoningsSend());
		sendMailButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
				summoningsServiceAsync.saveTemplate(semesterProxy.getId().toString(),true,true, popupView.getMessageContent(), 
						new AsyncCallback<Boolean>() {
							
							@Override
							public void onSuccess(Boolean result) {
								
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								if(result){
									Log.error("Template saved successfully.");
									
									summoningsServiceAsync.sendExaminerMail(semesterProxy.getId(),examinerIds, new AsyncCallback<Boolean>() {
										
										@Override
										public void onSuccess(Boolean result) {
											
											requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
											if(result){
											
												popupView.hide();
												Log.info("Mail sent successfully.");
												
												confirmationDialogBox = new MessageConfirmationDialogBox(constants.success());
												confirmationDialogBox.showConfirmationDialog(constants.confirmationMailSent());
											}else{
												Log.error("Error sending mail.");
												
												confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
												confirmationDialogBox.showConfirmationDialog(constants.errorMailSend());
											}
										}
										
										@Override
										public void onFailure(Throwable caught) {
											
											requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
											Log.error("ERROR : "+caught.getMessage());
										}
									});
								}else if(!result){
									
									Log.info("Error saving template : Default template path is not set.");
									confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
									confirmationDialogBox.showConfirmationDialog(constants.errorInvalidTplPath());
									
								}else{
									Log.error("Error saving template.");
									
									confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
									confirmationDialogBox.showConfirmationDialog(constants.errorTplSave());
								}
									
							}

							
							@Override
							public void onFailure(Throwable caught) {
								
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								Log.error("ERROR : "+caught.getMessage());
							}
						});
			}
		});
		
		saveTemplateButton = popupView.getSaveTemplateButton();
		saveTemplateButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
			//Feature : 154
				summoningsServiceAsync.saveTemplate(semesterProxy.getId().toString(),true,true, popupView.getMessageContent(), 
						new AsyncCallback<Boolean>() {
							
							@Override
							public void onSuccess(Boolean result) {
								
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								if(result){
									Log.error("Template saved successfully.");
									
									confirmationDialogBox = new MessageConfirmationDialogBox(constants.success());
									confirmationDialogBox.showConfirmationDialog(constants.confirmationTplSaved());
								}else if(!result){
									
									Log.info("Error saving template : Default template path is not set.");
									confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
									confirmationDialogBox.showConfirmationDialog(constants.errorInvalidTplPath());
									
								}else{
									Log.error("Error saving template.");
									
									confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
									confirmationDialogBox.showConfirmationDialog(constants.errorTplSave());
								}
									
							}
							
							@Override
							public void onFailure(Throwable caught) {
								
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								Log.error("ERROR : "+caught.getMessage());
							}
						});
			}
		});
		
		restoreTemplateButton = popupView.getRestoreTemplateButton();
		restoreTemplateButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
			//Feature : 154
				summoningsServiceAsync.deleteTemplate(semesterProxy.getId().toString(),true,true,
						new AsyncCallback<Boolean>() {
							
							@Override
							public void onSuccess(Boolean result) {
								
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								if(result){
									
									Log.info("Template restored successfully.");
									
									confirmationDialogBox = new MessageConfirmationDialogBox(constants.success());
									confirmationDialogBox.showConfirmationDialog(constants.confirmationTplRestore());
												//Feature : 154
									summoningsServiceAsync.getTemplateContent(semesterProxy.getId().toString(),true,true, 
											new AsyncCallback<String[]>() {
												
												@Override
												public void onSuccess(String[] response) {
													
													popupView.setMessageContent(response[1]);
												}
												
												@Override
												public void onFailure(Throwable throwable) {
													Log.error("ERROR : "+throwable.getMessage());
												}
											});
								}else{
									Log.info("Error restoring template.");
									
									confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
									confirmationDialogBox.showConfirmationDialog(constants.errorTplRestore());
								}
								
								
							}
							
							@Override
							public void onFailure(Throwable caught) {
								
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								Log.error("ERROR : "+caught.getMessage());
							}
						});
			}
		});
		
//		popupView.setAnimationEnabled(true);
		popupView.center();*/
		}else{
			Log.info("No checkbox selected.");
			
			confirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
			confirmationDialogBox.showConfirmationDialog(constants.warningSelectExaminer());
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void printCopyforSP() {
		
		final List<Long> spIds = new ArrayList<Long>(0);
		
		if(lstChkSp.size()>0 && checkSelection(lstChkSp)) // Check that SP is Checked Or Not
		{
			Log.info("Total SP : " + lstChkSp.size());										
			int i=0;
			for(Iterator<CheckBox> iterator=lstChkSp.iterator();iterator.hasNext();i++)
			{					
				if(iterator.next().isChecked()==true)
				{
					Log.info("~~Selected Checkbox Id: " + i);
					Log.info("~~Selected Checkbox Value: " + lstChkSp.get(i).getFormValue());
					
					spIds.add(Long.parseLong(lstChkSp.get(i).getFormValue()));
				}
			}
			

			popupView = new SummoningsPopupViewImpl();
			
			/*loadSemesterList();*/
			Boolean isExaminer = false;
			Boolean isEmail = false;
			loadDefaultTemplate();
			loadTemplateFileNameList(isExaminer,isEmail,null);
			handleLoadTempateEvent(isExaminer,isEmail);
			popupView.getSubject().removeFromParent();
			popupView.getSubjectLbl().removeFromParent();
			sendMailButton = popupView.getSendMailButton();
			sendMailButton.setText(constants.summoningsGeneratePdf());
			handleSendButtonEventForSPPDF(isExaminer,isEmail, spIds);
			saveTemplateButton = popupView.getSaveTemplateButton();
			saveTemplateEventHandler(isExaminer, isEmail);
			restoreTemplateButton = popupView.getRestoreTemplateButton();
			restoreTemplateEventHandler(isExaminer, isEmail);
	//		popupView.setAnimationEnabled(true);
			popupView.center();
			
			/*popupView = new SummoningsPopupViewImpl();
			requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
			//Feature : 154
			summoningsServiceAsync.getTemplateContent(semesterProxy.getId().toString(),false,false, 
					new AsyncCallback<String[]>() {
						
						@Override
						public void onSuccess(String[] response) {
							
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							popupView.setMessageContent(response[1]);
						}
						
						@Override
						public void onFailure(Throwable caught) {
							
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							Log.error("ERROR : "+caught.getMessage());
						}
					});
			
			loadSemesterList();
			
			popupView.getLoadTemplateButton().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					int selectedIndex = popupView.getSemesterList().getSelectedIndex();
					String selectedValue = popupView.getSemesterList().getValue(selectedIndex);
					
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
			//Feature : 154
					summoningsServiceAsync.getTemplateContent(selectedValue,false,false, 
							new AsyncCallback<String[]>() {
								
								@Override
								public void onSuccess(String[] response) {
									
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									if(response[2].equals("found")){
										
										popupView.setMessageContent(response[1]);
									}else{
										
										Log.info("Error loading template");
										
										confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
										confirmationDialogBox.showConfirmationDialog(constants.errorTplNotFound());
									}
								}
								
								@Override
								public void onFailure(Throwable throwable) {
									
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									Log.error("ERROR : "+throwable.getMessage());
								}
							});
				}
			});
			
			// THIS BUTTON ACT AS PDF GENERATOR BUTTON
			sendMailButton = popupView.getSendMailButton();
			sendMailButton.setText(constants.summoningsGeneratePdf());
			sendMailButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent arg0) {
					
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
					summoningsServiceAsync.saveTemplate(semesterProxy.getId().toString(),false,false, popupView.getMessageContent(), 
							new AsyncCallback<Boolean>() {
								
								@Override
								public void onSuccess(Boolean result) {
									
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									if(result){
										Log.info("Template saved successfully.");
										
										summoningsServiceAsync.generateSPMailPDF(semesterProxy.getId(),spIds, new AsyncCallback<String>() {
											
											@Override
											public void onSuccess(String response) {
												requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
												popupView.hide();
												String ordinal = URL.encodeQueryString(String.valueOf(ResourceDownloadProps.Entity.SUMMONINGS.ordinal()));          
												String url = GWT.getHostPageBaseURL() + "downloadFile?".concat(ResourceDownloadProps.ENTITY).concat("=").concat(ordinal)
														.concat("&").concat(ResourceDownloadProps.SUMMONING_KEY).concat("=").concat(URL.encodeQueryString(response));
												Log.info("--> url is : " +url);
												Window.open(url, "", "");
												//Window.open(response, "_blank", "enabled");
											}
											
											@Override
											public void onFailure(Throwable caught) {
												requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
												Log.error("ERROR : "+caught.getMessage());
											}
										});
									}else if(!result){
										
										Log.info("Error saving template : Default template path is not set.");
										confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
										confirmationDialogBox.showConfirmationDialog(constants.errorInvalidTplPath());
										
									}else{
										Log.info("Error saving template.");
										
										confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
										confirmationDialogBox.showConfirmationDialog(constants.errorTplSave());
									}
										
								}

								@Override
								public void onFailure(Throwable caught) {
									
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									Log.error("ERROR : "+caught.getMessage());
								}
							});
					
					
				}
			});
			
			saveTemplateButton = popupView.getSaveTemplateButton();
			saveTemplateButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent arg0) {
					
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
			//Feature : 154
					summoningsServiceAsync.saveTemplate(semesterProxy.getId().toString(),false,false, popupView.getMessageContent(), 
							new AsyncCallback<Boolean>() {
								
								@Override
								public void onSuccess(Boolean result) {
									
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									if(result){
										Log.info("Template saved successfully.");
										
										confirmationDialogBox = new MessageConfirmationDialogBox(constants.success());
										confirmationDialogBox.showConfirmationDialog(constants.confirmationTplSaved());
									}else if(!result){
										
										Log.info("Error saving template : Default template path is not set.");
										confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
										confirmationDialogBox.showConfirmationDialog(constants.errorInvalidTplPath());
										
									}else{
										Log.info("Error saving template.");
										
										confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
										confirmationDialogBox.showConfirmationDialog(constants.errorTplSave());
									}
										
								}
								
								@Override
								public void onFailure(Throwable caught) {
									
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									Log.error("ERROR : "+caught.getMessage());
								}
							});
				}
			});
			
			restoreTemplateButton = popupView.getRestoreTemplateButton();
			restoreTemplateButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent arg0) {
					
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
			//Feature : 154
					summoningsServiceAsync.deleteTemplate(semesterProxy.getId().toString(),false,false,
							new AsyncCallback<Boolean>() {
								
								@Override
								public void onSuccess(Boolean result) {
									
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									if(result){
										
										Log.info("Template restored successfully.");
										
										confirmationDialogBox = new MessageConfirmationDialogBox(constants.success());
										confirmationDialogBox.showConfirmationDialog(constants.confirmationTplRestore());
													//Feature : 154
										summoningsServiceAsync.getTemplateContent(semesterProxy.getId().toString(),false,false, 
												new AsyncCallback<String[]>() {
													
													@Override
													public void onSuccess(String[] response) {
														
														popupView.setMessageContent(response[1]);
													}
													
													@Override
													public void onFailure(Throwable caught) {
														
														Log.error("ERROR : "+caught.getMessage());
													}
												});
									}else{
										Log.info("Error restoring template.");
										
										confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
										confirmationDialogBox.showConfirmationDialog(constants.errorTplRestore());
									}
									
									
								}
								
								@Override
								public void onFailure(Throwable caught) {
									
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									Log.error("ERROR : "+caught.getMessage());
								}
							});
				}
			});
			
//			popupView.setAnimationEnabled(true);
			popupView.center();	*/
		
		
		}else{
			Log.info("No checkbox selected.");
			
			confirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
			confirmationDialogBox.showConfirmationDialog(constants.warningSelectSp());
		}
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void printCopyforExaminor() {
		
		final List<Long> examinerIds = new ArrayList<Long>(0);
		
		if(lstChkExaminor.size()>0 && checkSelection(lstChkExaminor)) // Check that Examiner is Checked Or Not
		{
			Log.info("Total Examiner : " + lstChkExaminor.size());										
			int i=0;
			for(Iterator<CheckBox> iterator=lstChkExaminor.iterator();iterator.hasNext();i++)
			{					
				if(iterator.next().isChecked()==true)
				{
					Log.info("~~Selected Checkbox Id: " + i);
					Log.info("~~Selected Checkbox Value: " + lstChkExaminor.get(i).getFormValue());
					
					examinerIds.add(Long.parseLong(lstChkExaminor.get(i).getFormValue()));
				}
			}
			
			popupView = new SummoningsPopupViewImpl();
			Boolean isExaminer = true;
			Boolean isEmail = false;
			loadDefaultTemplate();
			loadTemplateFileNameList(isExaminer,isEmail,null);
			handleLoadTempateEvent(isExaminer,isEmail);
			popupView.getSubject().removeFromParent();
			popupView.getSubjectLbl().removeFromParent();
			sendMailButton = popupView.getSendMailButton();
			sendMailButton.setText(constants.summoningsGeneratePdf());
			handleSendButtonEventForExaminorPDF(isExaminer,isEmail, examinerIds);
			saveTemplateButton = popupView.getSaveTemplateButton();
			saveTemplateEventHandler(isExaminer, isEmail);
			restoreTemplateButton = popupView.getRestoreTemplateButton();
			restoreTemplateEventHandler(isExaminer, isEmail);
	//		popupView.setAnimationEnabled(true);
			popupView.center();
			
			/*popupView = new SummoningsPopupViewImpl();
			
			requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
			//Feature : 154
			summoningsServiceAsync.getTemplateContent(semesterProxy.getId().toString(),true,false, 
					new AsyncCallback<String[]>() {
						
						@Override
						public void onSuccess(String[] response) {
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							popupView.setMessageContent(response[1]);
						}
						
						@Override
						public void onFailure(Throwable caught) {
							
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							Log.error("ERROR : "+caught.getMessage());
						}
					});
			
			loadSemesterList();
			
			popupView.getLoadTemplateButton().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					int selectedIndex = popupView.getSemesterList().getSelectedIndex();
					String selectedValue = popupView.getSemesterList().getValue(selectedIndex);
					
					  
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
			//Feature : 154
					summoningsServiceAsync.getTemplateContent(selectedValue,true,false, 
							new AsyncCallback<String[]>() {
								
								@Override
								public void onSuccess(String[] response) {
									
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									if(response[2].equals("found")){
										
										popupView.setMessageContent(response[1]);
									}else{
										
										Log.info("Error loading template");
										
										confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
										confirmationDialogBox.showConfirmationDialog(constants.errorTplNotFound());
									}
								}
								
								@Override
								public void onFailure(Throwable throwable) {
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									Log.error("ERROR : "+throwable.getMessage());
								}
							});
				}
			});
			
			// THIS BUTTON ACT AS PDF GENERATOR BUTTON
			sendMailButton = popupView.getSendMailButton();
			sendMailButton.setText(constants.summoningsGeneratePdf());
			sendMailButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent arg0) {
					
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
					summoningsServiceAsync.saveTemplate(semesterProxy.getId().toString(),true,false, popupView.getMessageContent(), 
							new AsyncCallback<Boolean>() {
								
								@Override
								public void onSuccess(Boolean result) {
									
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									if(result){
										Log.info("Template saved successfully.");
										
										summoningsServiceAsync.generateExaminerMailPDF(semesterProxy.getId(),examinerIds, new AsyncCallback<String>() {
											
											@Override
											public void onSuccess(String response) {
												requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
												popupView.hide();
												String ordinal = URL.encodeQueryString(String.valueOf(ResourceDownloadProps.Entity.SUMMONINGS.ordinal()));          
												String url = GWT.getHostPageBaseURL() + "downloadFile?".concat(ResourceDownloadProps.ENTITY).concat("=").concat(ordinal)
														.concat("&").concat(ResourceDownloadProps.SUMMONING_KEY).concat("=").concat(URL.encodeQueryString(response));
												Log.info("--> url is : " +url);
												Window.open(url, "", "");
												//Window.open(response, "_blank", "enabled");
											}
											
											@Override
											public void onFailure(Throwable caught) {
												requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
												Log.error("ERROR : "+caught.getMessage());
											}
										});
									}else if(!result){
										
										Log.info("Error saving template : Default template path is not set.");
										confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
										confirmationDialogBox.showConfirmationDialog(constants.errorInvalidTplPath());
										
									}else{
										Log.info("Error saving template.");
										
										confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
										confirmationDialogBox.showConfirmationDialog(constants.errorTplSave());
									}
										
								}
								
								@Override
								public void onFailure(Throwable caught) {
									
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									Log.error("ERROR : "+caught.getMessage());
								}
							});
					
					
				}
			});
			
			saveTemplateButton = popupView.getSaveTemplateButton();
			saveTemplateButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent arg0) {
					
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
			//Feature : 154
					summoningsServiceAsync.saveTemplate(semesterProxy.getId().toString(),true,false, popupView.getMessageContent(), 
							new AsyncCallback<Boolean>() {
								
								@Override
								public void onSuccess(Boolean result) {
									
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									if(result){
										Log.info("Template saved successfully.");
										
										confirmationDialogBox = new MessageConfirmationDialogBox(constants.success());
										confirmationDialogBox.showConfirmationDialog(constants.confirmationTplSaved());
									}else if(!result){
										
										Log.info("Error saving template : Default template path is not set.");
										confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
										confirmationDialogBox.showConfirmationDialog(constants.errorInvalidTplPath());
										
									}else{
										Log.info("Error saving template.");
										
										confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
										confirmationDialogBox.showConfirmationDialog(constants.errorTplSave());
									}
										
								}
								
								@Override
								public void onFailure(Throwable caught) {
									
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									Log.error("ERROR : "+caught.getMessage());
								}
							});
				}
			});
			
			restoreTemplateButton = popupView.getRestoreTemplateButton();
			restoreTemplateButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent arg0) {
					
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
			//Feature : 154
					summoningsServiceAsync.deleteTemplate(semesterProxy.getId().toString(),true,false,
							new AsyncCallback<Boolean>() {
								
								@Override
								public void onSuccess(Boolean result) {
									
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									if(result){
										Log.info("Template restored successfully.");
										
										confirmationDialogBox = new MessageConfirmationDialogBox(constants.success());
										confirmationDialogBox.showConfirmationDialog(constants.confirmationTplRestore());
													//Feature : 154
										summoningsServiceAsync.getTemplateContent(semesterProxy.getId().toString(),true,false, 
												new AsyncCallback<String[]>() {
													
													@Override
													public void onSuccess(String[] response) {
														
														popupView.setMessageContent(response[1]);
													}
													
													@Override
													public void onFailure(Throwable caught) {
														Log.error("ERROR : "+caught.getMessage());
													}
												});
									}else{
										Log.info("Error restoring template.");
										
										confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
										confirmationDialogBox.showConfirmationDialog(constants.errorTplRestore());
									}
									
								}
								
								@Override
								public void onFailure(Throwable caught) {
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									Log.error("ERROR : "+caught.getMessage());
								}
							});
				}
			});
			
//			popupView.setAnimationEnabled(true);
			popupView.center();	*/
		
		
		}else{
			Log.info("No checkbox selected.");
			
			confirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
			confirmationDialogBox.showConfirmationDialog(constants.warningSelectExaminer());
		}
		
	}

	private void loadDefaultTemplate() {
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		summoningsServiceAsync.getDefaultTemplateContent(new AsyncCallback<String>() {
					
			@Override
			public void onSuccess(String response) {
				
				popupView.setMessageContent(response);
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
			}
			
			@Override
			public void onFailure(Throwable throwable) {
				Log.error("ERROR : "+throwable.getMessage());
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
			}
		});
	}
	
	private String getTemplateFileNameForEmail() {
		String templateFilePath = popupView.getSelectedTemplateFile();
		
		String newFileName= "";
		if(popupView.getSubject().getValue().isEmpty()) {
			newFileName = semesterProxy.getSemester().toString() + " " +semesterProxy.getCalYear() ;
		}else {
			newFileName = semesterProxy.getSemester().toString() + " " +semesterProxy.getCalYear() + " - " + popupView.getSubject().getValue();
		}
		 
		if(newFileName.equals(templateFilePath)) {
			return templateFilePath;
		} else {
			return newFileName;
		}		
	}
	
	private String getTemplateFileNameForPDF() {
		String templateFilePath = popupView.getSelectedTemplateFile();
		String newFileName= "";
		if(templateFilePath == null || templateFilePath.trim().isEmpty() == true ) {
			newFileName = semesterProxy.getSemester().toString() + " " +semesterProxy.getCalYear() ;
		}else {
			newFileName = templateFilePath;
		}
		return newFileName;		
	}
	private void loadTemplateFileNameList(final Boolean isExaminer, final Boolean isEmail,final String templateFilePath) {
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		summoningsServiceAsync.getAllTemplateFileNames(isExaminer,isEmail,new AsyncCallback<List<String>>(){

			@Override
			public void onFailure(Throwable throwable) {
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
				Log.error("ERROR : "+throwable.getMessage());
			}

			@Override
			public void onSuccess(List<String> templateFileNameList) {
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
				popupView.setFileListValues(templateFileNameList);
				if(templateFilePath != null && templateFilePath.trim().isEmpty() == false) {
					popupView.selectFileName(templateFilePath);
				}
			}});
	}
	
	private void handleLoadTempateEvent(final Boolean isExaminer, final Boolean isEmail) {
		
		popupView.getLoadTemplateButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				final String selectedTemplateFile = popupView.getSelectedTemplateFile();
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
				
				summoningsServiceAsync.getTemplateContent(selectedTemplateFile,isExaminer,isEmail, 
						new AsyncCallback<String[]>() {
							
							@Override
							public void onSuccess(String[] response) {
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								if(response[2].equals("found")){
									popupView.setMessageContent(response[1]);
									popupView.getSubject().setText(getSubjectFrom(selectedTemplateFile));
									
								}else{
									Log.info("Error loading template");
									confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
									confirmationDialogBox.showConfirmationDialog(constants.errorTplNotFound());
								}
							}
							
							@Override
							public void onFailure(Throwable throwable) {
								Log.error("ERROR : "+throwable.getMessage());
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							}
						});
				
			}
		});		
	}

	private void handleSendButtonEventForSPMail(final Boolean isExaminer, final Boolean isEmail, final List<Long> spIds) {
		sendMailButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				Log.info("has Text changed : " + popupView.hasTextChanged());
				final String templateFilePath = getTemplateFileNameForEmail();
				if(popupView.hasTextChanged()) {
					confirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
					confirmationDialogBox.showYesNoDialog(constants.saveChangesOrNot());
					confirmationDialogBox.getYesBtn().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							confirmationDialogBox.hide();
							saveTemplate(templateFilePath,isExaminer,isEmail,sendSPMail(spIds,templateFilePath));		
						}
					});
					confirmationDialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							confirmationDialogBox.hide();
							sendSPMail(spIds,templateFilePath).call();
						}
					});
				} else {
					sendSPMail(spIds,templateFilePath).call();
				}
			}
		});
	}
	
	private void handleSendButtonEventForExaminorMail(final Boolean isExaminer, final Boolean isEmail, final List<Long> examinerIds) {
		sendMailButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				Log.info("has Text changed : " + popupView.hasTextChanged());
				final String templateFilePath = getTemplateFileNameForEmail();
				if(popupView.hasTextChanged()) {
					confirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
					confirmationDialogBox.showYesNoDialog(constants.saveChangesOrNot());
					confirmationDialogBox.getYesBtn().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							confirmationDialogBox.hide();
							saveTemplate(templateFilePath,isExaminer,isEmail,sendMailExaminor(examinerIds, templateFilePath));
						}
					});
					confirmationDialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							confirmationDialogBox.hide();
							sendMailExaminor(examinerIds, templateFilePath).call();
						}
					});
				}else {
					sendMailExaminor(examinerIds, templateFilePath).call();
				}
			}
		});
	}
	private void handleSendButtonEventForSPPDF(final Boolean isExaminer,final Boolean isEmail, final List<Long> spIds) {
		sendMailButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				Log.info("has Text changed : " + popupView.hasTextChanged());
				final String templateFilePath = getTemplateFileNameForPDF();
				
				if(popupView.hasTextChanged()) {
					confirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
					confirmationDialogBox.showYesNoDialog(constants.saveChangesOrNot());
					confirmationDialogBox.getYesBtn().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							confirmationDialogBox.hide();
							saveTemplate(templateFilePath,isExaminer,isEmail,generateSPPDF(spIds,templateFilePath));	
						}
					});
					confirmationDialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							confirmationDialogBox.hide();
							generateSPPDF(spIds,templateFilePath).call();
						}
					});
				}else {
					generateSPPDF(spIds,templateFilePath).call();
				}
			}
		});
		
	}
	private void handleSendButtonEventForExaminorPDF(final Boolean isExaminer, final Boolean isEmail, final List<Long> examinerIds) {
		sendMailButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				Log.info("has Text changed : " + popupView.hasTextChanged());
				
				final String templateFilePath = getTemplateFileNameForPDF();
				
				if(popupView.hasTextChanged()) {
					confirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
					confirmationDialogBox.showYesNoDialog(constants.saveChangesOrNot());
					confirmationDialogBox.getYesBtn().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							confirmationDialogBox.hide();
							saveTemplate(templateFilePath,isExaminer,isEmail,generateExaminorPDF(examinerIds, templateFilePath));
						}
					});
					confirmationDialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							confirmationDialogBox.hide();
							generateExaminorPDF(examinerIds, templateFilePath).call();
						}
					});
				}else {
					generateExaminorPDF(examinerIds, templateFilePath).call();
				}
				
			}
		});
	}

	private void saveTemplateEventHandler(final Boolean isExaminer, final Boolean isEmail) {
		saveTemplateButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				final String templateFilePath;
				if(isEmail) {
					templateFilePath = getTemplateFileNameForEmail();	
				}else {
					templateFilePath = getTemplateFileNameForPDF();
				}
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
				summoningsServiceAsync.checkIfFileExists(templateFilePath,isExaminer,isEmail, new AsyncCallback<Boolean>() {
					
					@Override
					public void onSuccess(Boolean result) {
						
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
						
						if(result == true) {
							confirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
							confirmationDialogBox.showYesNoMayBeDialog(constants.alreadyATemplateMsg());
							confirmationDialogBox.getYesBtn().setText(constants.overrideOriginal());
							confirmationDialogBox.getNoBtnl().setText(constants.storeNew());
							confirmationDialogBox.getMayBeBtn().setText(constants.cancel());
							
							confirmationDialogBox.getYesBtn().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									confirmationDialogBox.hide();
									saveTemplate(templateFilePath, isExaminer, isEmail, null);
								}
							});
							confirmationDialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									confirmationDialogBox.hide();
									String newTemplateFilePath = getNewTemplateName(templateFilePath);
									saveTemplate(newTemplateFilePath, isExaminer, isEmail, null);
								}

							});
							
							confirmationDialogBox.getMayBeBtn().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									confirmationDialogBox.hide();
								}
							});
							
						} else {
							saveTemplate(templateFilePath, isExaminer, isEmail, null);
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Log.error("ERROR : "+caught.getMessage());
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					}
				});
			}
		});
	}
	
	private void restoreTemplateEventHandler(final Boolean isExaminer, final Boolean isEmail) {
		restoreTemplateButton.addClickHandler(new ClickHandler() {
					
			@Override
			public void onClick(ClickEvent arg0) {
				final String templateFilePath;
				if(isEmail) {
					templateFilePath = getTemplateFileNameForEmail();	
				}else {
					templateFilePath = getTemplateFileNameForPDF();
				}
				deleteTemplateAndLoadDefault(templateFilePath,isExaminer,isEmail);
			}
		});
				
	}
	
	private Function sendSPMail(final List<Long> spIds, final String templateFilePath) {
		return new Function() {
			
			@Override
			public void call() {
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
				summoningsServiceAsync.sendSPMail(semesterProxy.getId(),spIds,templateFilePath,popupView.getSubject().getValue(), new AsyncCallback<Boolean>() {
					
					@Override
					public void onSuccess(Boolean result) {
						
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
						
						if(result){
							popupView.hide();
							Log.info("Mail Sent Successfully");
							
							confirmationDialogBox = new MessageConfirmationDialogBox(constants.success());
							confirmationDialogBox.showConfirmationDialog(constants.confirmationMailSent());
						}else{
							Log.info("Error sending email");
							
							confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
							confirmationDialogBox.showConfirmationDialog(constants.errorMailSend());
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Log.error("ERROR : "+caught.getMessage());
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					}
				});
			}
		};
		

	}
	private Function sendMailExaminor(final List<Long> examinerIds,final String templateFilePath) {
		return new Function() {
			
			@Override
			public void call() {
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
				summoningsServiceAsync.sendExaminerMail(semesterProxy.getId(),examinerIds,templateFilePath,popupView.getSubject().getValue(), new AsyncCallback<Boolean>() {
					
					@Override
					public void onSuccess(Boolean result) {
						
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
						if(result){
						
							popupView.hide();
							Log.info("Mail sent successfully.");
							
							confirmationDialogBox = new MessageConfirmationDialogBox(constants.success());
							confirmationDialogBox.showConfirmationDialog(constants.confirmationMailSent());
						}else{
							Log.error("Error sending mail.");
							
							confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
							confirmationDialogBox.showConfirmationDialog(constants.errorMailSend());
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
						
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
						Log.error("ERROR : "+caught.getMessage());
					}
				});
			}
		};
		
	}
	
	private Function generateSPPDF(final List<Long> spIds, final String templateFilePath) {
		return new Function() {
			
			@Override
			public void call() {
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
				summoningsServiceAsync.generateSPMailPDF(semesterProxy.getId(),spIds, templateFilePath, new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String response) {
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
						popupView.hide();
						String ordinal = URL.encodeQueryString(String.valueOf(ResourceDownloadProps.Entity.SUMMONINGS.ordinal()));          
						String url = GWT.getHostPageBaseURL() + "downloadFile?".concat(ResourceDownloadProps.ENTITY).concat("=").concat(ordinal)
								.concat("&").concat(ResourceDownloadProps.SUMMONING_KEY).concat("=").concat(URL.encodeQueryString(response));
						Log.info("--> url is : " +url);
						Window.open(url, "", "");
						//Window.open(response, "_blank", "enabled");
					}
					
					@Override
					public void onFailure(Throwable caught) {
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
						Log.error("ERROR : "+caught.getMessage());
					}
				});
			}
		};
	}

	private Function generateExaminorPDF(final List<Long> examinerIds,final String templateFilePath) {
		return new Function() {
			
			@Override
			public void call() {
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
				summoningsServiceAsync.generateExaminerMailPDF(semesterProxy.getId(),examinerIds, templateFilePath, new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String response) {
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
						popupView.hide();
						String ordinal = URL.encodeQueryString(String.valueOf(ResourceDownloadProps.Entity.SUMMONINGS.ordinal()));          
						String url = GWT.getHostPageBaseURL() + "downloadFile?".concat(ResourceDownloadProps.ENTITY).concat("=").concat(ordinal)
								.concat("&").concat(ResourceDownloadProps.SUMMONING_KEY).concat("=").concat(URL.encodeQueryString(response));
						Log.info("--> url is : " +url);
						Window.open(url, "", "");
						//Window.open(response, "_blank", "enabled");
					}
					
					@Override
					public void onFailure(Throwable caught) {
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
						Log.error("ERROR : "+caught.getMessage());
					}
				});
			}
		};
	}
	
	private void saveTemplate(final String templateFilePath, final Boolean isExaminer, final Boolean isEmail,final Function backFunction) {
		
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		summoningsServiceAsync.saveTemplate(templateFilePath,isExaminer,isEmail, popupView.getMessageContent(), new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
				if(result){
					Log.info("Template saved successfully.");
					
					if(backFunction == null) {
						confirmationDialogBox = new MessageConfirmationDialogBox(constants.success());
						confirmationDialogBox.showConfirmationDialog(constants.confirmationTplSaved());
						loadTemplateFileNameList(isExaminer, isEmail,templateFilePath);
					} else {
						backFunction.call();
					}
				
				}else if(!result){
					
					Log.info("Error saving template : Default template path is not set.");
					confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
					confirmationDialogBox.showConfirmationDialog(constants.errorInvalidTplPath());
					
				}else{
					Log.info("Error saving template");
					
					confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
					confirmationDialogBox.showConfirmationDialog(constants.errorTplSave());
				}
				
			}
			
			@Override
			public void onFailure(Throwable throwable) {
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
				Log.error("ERROR : "+throwable.getMessage());
			}
		});				
	}
	
	private void deleteTemplateAndLoadDefault(String templateFilePath, final Boolean isExaminer, final Boolean isEmail) {
		
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		
		summoningsServiceAsync.deleteTemplate(templateFilePath,isExaminer,isEmail,
				new AsyncCallback<Boolean>() {
							
						@Override
						public void onSuccess(Boolean result) {
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							if(result){
								Log.info("Template restored successfully.");
								confirmationDialogBox = new MessageConfirmationDialogBox(constants.success());
								confirmationDialogBox.showConfirmationDialog(constants.confirmationTplRestore());
								loadDefaultTemplate();
							} else{
								Log.info("Error restoring template");
								
								confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
								confirmationDialogBox.showConfirmationDialog(constants.errorTplRestore());
							}
						}
						
						@Override
						public void onFailure(Throwable throwable) {
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							Log.error("ERROR : "+throwable.getMessage());
						}
					
		});
		
	}
	
	private String getSubjectFrom(String selectedTemplateFile) {
		String subject = "";
		
		if(selectedTemplateFile == null || selectedTemplateFile.isEmpty() == true || selectedTemplateFile.contains("-") == false) {
			return subject;
		}else {
			int index = selectedTemplateFile.indexOf("-");
			
			if(index > 0 && index + 2 < selectedTemplateFile.length()) {
				return selectedTemplateFile.substring(index+2, selectedTemplateFile.length());
			}
		}
		
		return subject;
	}
	
	private String getNewTemplateName(String templateFilePath) {
		
		if(templateFilePath.contains("(") && templateFilePath.contains(")")) {
			if((templateFilePath.indexOf("(") + 2) == templateFilePath.indexOf(")")) {
				try {
					String value = templateFilePath.substring(templateFilePath.indexOf("(")+1,templateFilePath.indexOf(")"));
					return templateFilePath.substring(0,templateFilePath.indexOf("(") + 1)+ (Integer.parseInt(value) + 1) + ")";	
				}catch (Exception e) {
					Log.error(e.getMessage());
					return templateFilePath + " (1)";
				}
			}else {
				return templateFilePath + " (1)";	
			}
		}else {
			return templateFilePath + " (1)";
		}
		
	}
	interface Function {
		void call();
	}
}
