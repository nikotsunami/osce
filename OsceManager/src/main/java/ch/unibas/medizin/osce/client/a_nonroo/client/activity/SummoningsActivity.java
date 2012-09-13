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
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.shared.util;
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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;

/**
 * @author dk
 *
 */
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
		
	}
	
	public void onStop(){
		
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		SummoningsView systemStartView = new SummoningsViewImpl();
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		
		init();

		view.setDelegate(this);
	}
	
	
	
	@SuppressWarnings("deprecation")
	private void init() {
		Log.info("Patient in Semester : "+place.getSemesterProxy());
		
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

							Log.info("asd");
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
		
			requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
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
									confirmationDialogBox.showConfirmationDialog("Template not found");
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
		sendMailButton.setText("Send Mail");
		sendMailButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
				summoningsServiceAsync.sendSPMail(semesterProxy.getId(),spIds, new AsyncCallback<Boolean>() {
					
					@Override
					public void onSuccess(Boolean result) {
						
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
						if(result){
							popupView.hide();
							Log.info("Mail Sent Successfully");
							
							confirmationDialogBox = new MessageConfirmationDialogBox(constants.success());
							confirmationDialogBox.showConfirmationDialog("Mail Sent Successfully");
						}else{
							Log.info("Error sending email");
							
							confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
							confirmationDialogBox.showConfirmationDialog("Error sending email");
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
									confirmationDialogBox.showConfirmationDialog("Template saved successfully.");
								}else if(!result){
									
									Log.info("Error saving template : Default template path is not set.");
									confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
									confirmationDialogBox.showConfirmationDialog("Invalid Path for Saving Template. Please contact Administrator.");
									
								}else{
									Log.info("Error saving template");
									
									confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
									confirmationDialogBox.showConfirmationDialog("Error saving template");
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
									confirmationDialogBox.showConfirmationDialog("Template restored successfully.");
									
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
									confirmationDialogBox.showConfirmationDialog("Error restoring template");
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
		popupView.center();
		
		}else{
			Log.info("No checkbox selected.");

			confirmationDialogBox = new MessageConfirmationDialogBox(constants.noSelection());
			confirmationDialogBox.showConfirmationDialog("Please select any Standardized Patient");
		}
	}

	@SuppressWarnings("deprecation")
	private void loadSemesterList() {
		popupView.getSemesterList().clear();
		popupView.getSemesterList().addItem("Select", "0");
		
		requests.semesterRequest().findAllSemesters().fire(new OSCEReceiver<List<SemesterProxy>>() {

			@Override
			public void onSuccess(List<SemesterProxy> response) {
				
				for(SemesterProxy semesterProxy : response){
					popupView.getSemesterList().addItem(semesterProxy.getSemester()+" "+semesterProxy.getCalYear(),semesterProxy.getId().toString());
				}
			}
		});
	}

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



summoningsServiceAsync.getTemplateContent(semesterProxy.getId().toString(),true,true, 
						new AsyncCallback<String[]>() {
							
							@Override
							public void onSuccess(String[] response) {
								
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								if(response[2].equals("found")){
									
									popupView.setMessageContent(response[1]);
								}else{
									
									Log.info("Error loading template");
									
									confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
									confirmationDialogBox.showConfirmationDialog("Template not found");
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
		sendMailButton.setText("Send Mail");
		sendMailButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
				summoningsServiceAsync.sendExaminerMail(semesterProxy.getId(),examinerIds, new AsyncCallback<Boolean>() {
					
					@Override
					public void onSuccess(Boolean result) {
						
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
						if(result){
						
							popupView.hide();
							Log.info("Mail sent successfully.");
							
							confirmationDialogBox = new MessageConfirmationDialogBox(constants.success());
							confirmationDialogBox.showConfirmationDialog("Mail sent successfully.");
						}else{
							Log.error("Error sending mail.");
							
							confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
							confirmationDialogBox.showConfirmationDialog("Error sending mail.");
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
									confirmationDialogBox.showConfirmationDialog("Template saved successfully.");
								}else if(!result){
									
									Log.info("Error saving template : Default template path is not set.");
									confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
									confirmationDialogBox.showConfirmationDialog("Invalid Path for Saving Template. Please contact Administrator.");
									
								}else{
									Log.error("Error saving template.");
									
									confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
									confirmationDialogBox.showConfirmationDialog("Error saving template.");
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
									confirmationDialogBox.showConfirmationDialog("Template restored successfully.");
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
									confirmationDialogBox.showConfirmationDialog("Error restoring template.");
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
		popupView.center();
		}else{
			Log.info("No checkbox selected.");
			
			confirmationDialogBox = new MessageConfirmationDialogBox(constants.noSelection());
			confirmationDialogBox.showConfirmationDialog("Please select any Examiner");
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
					summoningsServiceAsync.getTemplateContent(semesterProxy.getId().toString(),false,false, 
							new AsyncCallback<String[]>() {
								
								@Override
								public void onSuccess(String[] response) {
									
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									if(response[2].equals("found")){
										
										popupView.setMessageContent(response[1]);
									}else{
										
										Log.info("Error loading template");
										
										confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
										confirmationDialogBox.showConfirmationDialog("Template not found");
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
			sendMailButton.setText("Generate PDF");
			sendMailButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent arg0) {
					
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
					summoningsServiceAsync.generateSPMailPDF(semesterProxy.getId(),spIds, new AsyncCallback<String>() {
						
						@Override
						public void onSuccess(String response) {
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							popupView.hide();
							Window.open(response, "_blank", "enabled");
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
										confirmationDialogBox.showConfirmationDialog("Template saved successfully.");
									}else if(!result){
										
										Log.info("Error saving template : Default template path is not set.");
										confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
										confirmationDialogBox.showConfirmationDialog("Invalid Path for Saving Template. Please contact Administrator.");
										
									}else{
										Log.info("Error saving template.");
										
										confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
										confirmationDialogBox.showConfirmationDialog("Error saving template.");
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
										confirmationDialogBox.showConfirmationDialog("Template restored successfully.");
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
										confirmationDialogBox.showConfirmationDialog("Error restoring template.");
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
			popupView.center();	
		
		
		}else{
			Log.info("No checkbox selected.");
			
			confirmationDialogBox = new MessageConfirmationDialogBox(constants.noSelection());
			confirmationDialogBox.showConfirmationDialog("Please select any Standardized Patient");
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
					summoningsServiceAsync.getTemplateContent(semesterProxy.getId().toString(),true,false, 
							new AsyncCallback<String[]>() {
								
								@Override
								public void onSuccess(String[] response) {
									
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									if(response[2].equals("found")){
										
										popupView.setMessageContent(response[1]);
									}else{
										
										Log.info("Error loading template");
										
										confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
										confirmationDialogBox.showConfirmationDialog("Template not found");
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
			sendMailButton.setText("Generate PDF");
			sendMailButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent arg0) {
					
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
					summoningsServiceAsync.generateExaminerMailPDF(semesterProxy.getId(),examinerIds, new AsyncCallback<String>() {
						
						@Override
						public void onSuccess(String response) {
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							popupView.hide();
							Window.open(response, "_blank", "enabled");
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
										confirmationDialogBox.showConfirmationDialog("Template saved successfully.");
									}else if(!result){
										
										Log.info("Error saving template : Default template path is not set.");
										confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
										confirmationDialogBox.showConfirmationDialog("Invalid Path for Saving Template. Please contact Administrator.");
										
									}else{
										Log.info("Error saving template.");
										
										confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
										confirmationDialogBox.showConfirmationDialog("Error saving template.");
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
										confirmationDialogBox.showConfirmationDialog("Template restored successfully.");
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
										confirmationDialogBox.showConfirmationDialog("Error restoring template.");
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
			popupView.center();	
		
		
		}else{
			Log.info("No checkbox selected.");
			
			confirmationDialogBox = new MessageConfirmationDialogBox(constants.noSelection());
			confirmationDialogBox.showConfirmationDialog("Please select any Examiner");
		}
		
	}
	
}
