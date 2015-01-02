package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.PlanSPTrainingPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleAssignmentPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining.BlockView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining.BlockViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining.IndividualSuggestionForTrainingView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining.IndividualSuggestionForTrainingViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining.MultipleDaySelectedView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining.MultipleDaySelectedViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining.PlanSPTrainingView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining.PlanSPTrainingViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining.ScheduleTrainingForSuggestionView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining.ScheduleTrainingForSuggestionViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining.ScheduleTrainingView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining.ScheduleTrainingViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining.SingleDaySelectedView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining.SingleDaySelectedViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining.TrainingSuggestionView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining.TrainingSuggestionViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining.TrainingView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining.TrainingViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining.UpdateTrainingView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining.UpdateTrainingViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.custom.CellWidget;
import ch.unibas.medizin.osce.client.managed.request.OsceDateProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.TrainingBlockProxy;
import ch.unibas.medizin.osce.client.managed.request.TrainingDateProxy;
import ch.unibas.medizin.osce.client.managed.request.TrainingProxy;
import ch.unibas.medizin.osce.client.managed.request.TrainingSuggestionProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.SurveyStatus;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.shared.i18n.OsceMessages;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;

public class PlanSPTrainingActivity extends AbstractActivity implements PlanSPTrainingView.Delegate,SingleDaySelectedView.Delegate,MultipleDaySelectedView.Delegate,BlockView.Delegate,
	TrainingSuggestionView.Delegate,ScheduleTrainingView.Delegate,TrainingView.Delegate,UpdateTrainingView.Delegate,ScheduleTrainingForSuggestionView.Delegate,IndividualSuggestionForTrainingView.Delegate{

	private final OsMaRequestFactory requests;
	private final PlaceController placeController;
	private PlanSPTrainingView view;
	private AcceptsOneWidget widget;
	private  PlanSPTrainingPlace place;
	private HandlerManager handlerManager;
	private SemesterProxy semesterProxy;
	private SelectChangeHandler removeHandler;
	private SingleDaySelectedView singleDaySelectionview;
	private MultipleDaySelectedView multipleDaySelectedView;
	private PlanSPTrainingActivity planSPTrainingActivity;
	private Date currentlySelectedDate;
	private VerticalPanel currentlySelectdDatePanel;
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	private final OsceMessages osceMessages = GWT.create(OsceMessages.class);
	
	private List<OsceDayProxy> listOfOsceDayProxy = new ArrayList<OsceDayProxy>();
	private DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd.MM.yyyy");
	private List<Date> currentlySelectedDatesList = new ArrayList<Date>();
	private List<CellWidget> currentlySelectedWidgets; 
	int totalConsucutiveDays;
	private Date firstBlockDate;
	private Map<Date, BlockView> blockWidgetsMap = new HashMap<Date, BlockView>();
	private int totalTrainingDateCreated;
	private int totalOsceDateCreated;
	
	private boolean isSurveyIsStarted;
	
	private boolean isUpDateButtonClicked;
	
	private List<TrainingSuggestionProxy> trainingSuggestionList;
	
	private Map<Date, TrainingSuggestionView> trainingSuggestionMorningMap =new HashMap<Date, TrainingSuggestionView>();
	
	private Map<Date, TrainingSuggestionView> trainingSuggestionAfterNoonMap =new HashMap<Date, TrainingSuggestionView>();
	
	private List<Date> allSuggestedViewTrainingDates= new ArrayList<Date>();
	
	protected boolean isShowingSuggestions;
	
	private ScheduleTrainingView scheduleTrainingView;
	
	private ScheduleTrainingForSuggestionView scheduleTrainingForSuggestionView;
	
	private TrainingView allReadyScheduledTrainingsView;
	
	private UpdateTrainingView updateTrainingView;
	
	protected List<OscePostProxy> allOscePostOfSemList;
	
	private UpdateTrainingViewImpl currentlyUpdatingTrainingView;
	
	boolean isTrainingOverLaping;
	
	private List<TrainingSuggestionProxy> moreSuggestionsRoleListForMorning= new ArrayList<TrainingSuggestionProxy>();
	
	private List<TrainingSuggestionProxy> moreSuggestionsRoleListForAfternoon= new ArrayList<TrainingSuggestionProxy>();
	
	private Map<Date,List<TrainingSuggestionProxy>> moreSuggestionForMorningMap = new HashMap<Date, List<TrainingSuggestionProxy>>();
	
	private Map<Date,List<TrainingSuggestionProxy>> moreSuggestionForAfterNoonMap = new HashMap<Date, List<TrainingSuggestionProxy>>();
	
	public PlanSPTrainingActivity(OsMaRequestFactory requests, PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;
	}
		
	public PlanSPTrainingActivity(OsMaRequestFactory requests,PlaceController placeController, PlanSPTrainingPlace planSpTrainingplace) {
		this.requests = requests;
		this.placeController = placeController;
		this.handlerManager=planSpTrainingplace.handler;
		this.place=planSpTrainingplace;
		this.semesterProxy=planSpTrainingplace.semesterProxy;
		
		Log.info("Semester Proxy Get:" + semesterProxy.getCalYear());
		
		this.addSelectChangeHandler(new SelectChangeHandler() 
		{			
			@Override
			public void onSelectionChange(SelectChangeEvent event) 
			{				
				Log.info("onSelectionChange Get Semester: " + event.getSemesterProxy().getCalYear());
				semesterProxy=event.getSemesterProxy();
				init();			
			}

		});
	}


	@SuppressWarnings("deprecation")
	private void init() {
		
		PlanSPTrainingView planSPTrainingView = new PlanSPTrainingViewImpl();
		planSPTrainingView.setDelegate(this);
		this.view = planSPTrainingView;
		singleDaySelectionview=new SingleDaySelectedViewImpl();
		
		multipleDaySelectedView=new MultipleDaySelectedViewImpl();
		planSPTrainingActivity= this;
		
		singleDaySelectionview.setDelegate(this);
		multipleDaySelectedView.setDelegate(this);
		
		scheduleTrainingView= new ScheduleTrainingViewImpl();
		
		scheduleTrainingView.setDelegate(this);
		
		scheduleTrainingForSuggestionView= new ScheduleTrainingForSuggestionViewImpl();
		
		scheduleTrainingForSuggestionView.setDelegate(this);
		
		//allReadyScheduledTrainingsView= new TrainingViewImpl();
		
		//updateTrainingView = new UpdateTrainingViewImpl();
		
		widget.setWidget(planSPTrainingView.asWidget());
		
		showApplicationLoading(true);
		
		requests.semesterRequest().findSemester(semesterProxy.getId()).with("osceDates","trainingBlocks","patientsInSemester","patientsInSemester.trainingDates","patientsInSemester.osceDates","trainingBlocks.trainingDates").fire(new OSCEReceiver<SemesterProxy>() {

			@Override
			public void onSuccess(SemesterProxy response) {
				showApplicationLoading(false);
				Log.info("found Latest semster is : " +response);
				if(response!=null){
					semesterProxy = response;
					
					totalOsceDateCreated=0;
					
					if(semesterProxy.getOsceDates()!=null){
						totalOsceDateCreated=semesterProxy.getOsceDates().size();
					}
					Set<TrainingBlockProxy> setTrainingBlockProxy =semesterProxy.getTrainingBlocks();
				
					totalTrainingDateCreated=0;
					
					if(setTrainingBlockProxy!=null){
						
						for (Iterator iterator = setTrainingBlockProxy.iterator(); iterator.hasNext();) {
							
							TrainingBlockProxy trainingBlockProxy = (TrainingBlockProxy) iterator.next();
							
							if(trainingBlockProxy.getTrainingDates()!=null){
								
								totalTrainingDateCreated+= trainingBlockProxy.getTrainingDates().size();
							}
						}
					}
					
					setShowSuggestionButtonVisible();
					setStartSurverStopSurveyVisibility();
					
				}
					
				}

		});
		
		showApplicationLoading(true);
		requests.osceDayRequest().findOsceDayBySemester(semesterProxy.getId()).fire(new OSCEReceiver<List<OsceDayProxy>>() {

			@Override
			public void onSuccess(List<OsceDayProxy> response) {
				showApplicationLoading(false);
				if(response!=null){
					listOfOsceDayProxy=response;
				}
			}
		});
		
		initializeViewFromGivenDateToEndOfMonth(new Date());
		
		//initializeViewForAllReadyScheduledTrainings();
	}
	
	public void setStartSurverStopSurveyVisibility(){
		if(semesterProxy.getSurveyStatus()!=null){
			if(semesterProxy.getSurveyStatus().ordinal()==SurveyStatus.OPEN.ordinal()){
				view.getStartSurveyButton().setVisible(false);
				view.getStopSurveyButton().setVisible(true);
				isSurveyIsStarted=true;
			}else if(semesterProxy.getSurveyStatus().ordinal()==SurveyStatus.CLOSED.ordinal()){
				view.getStartSurveyButton().removeFromParent();
				view.getStopSurveyButton().removeFromParent();
				isSurveyIsStarted=false;
			}else if(semesterProxy.getSurveyStatus().ordinal()==SurveyStatus.NOT_STARTED.ordinal()){
				view.getStartSurveyButton().setVisible(true);
				view.getStartSurveyButton().setEnabled(true);
				view.getStopSurveyButton().setVisible(false);
				isSurveyIsStarted=false;
			}
		}else{
			view.getStartSurveyButton().setVisible(true);
			view.getStartSurveyButton().setEnabled(false);
			view.getStopSurveyButton().setVisible(false);
			isSurveyIsStarted=false;
		}
	}
	@SuppressWarnings("deprecation")
	private void initializeViewFromGivenDateToEndOfMonth(final Date date) {
		
		showApplicationLoading(true);
		firstBlockDate=null;
		requests.trainingDateRequest().findTrainingDatesFromGivenDateToEndOfMonth(date,semesterProxy.getId()).with("trainingBlock").fire(new OSCEReceiver<List<TrainingDateProxy>>() {

			@Override
			public void onSuccess(List<TrainingDateProxy> response) {
				Log.info("found total training date defined for moth is " + response.size());
				
				boolean isFirst=true;
				Date previousWeekDate=null;
				TrainingDateProxy previousDateProxy=null;
				boolean isBlockSplitted=false;
				

				if(response!=null){
				
					for(TrainingDateProxy trainingDate : response){
	
						Widget widget =view.getCustomCalenger().getPanelOfDate(trainingDate.getTrainingDate());
						
						VerticalPanel vpanel =((VerticalPanel)widget);
						
						Label proposedTrainingDateLbl =new Label(constants.proposeTrainingDay());
						
						proposedTrainingDateLbl.setStyleName("proposedDayLabel");
						
						vpanel.add(proposedTrainingDateLbl);
						
						if(isFirst){
							Date trainingDate2 =trainingDate.getTrainingDate();
							VerticalPanel blockPanel = view.getCustomCalenger().getBlockCell(trainingDate2);
							if(blockPanel !=null){
								Label blockLbl = new Label(constants.block() + " 1 ");
								blockPanel.add(blockLbl);
								firstBlockDate=view.getCustomCalenger().getStartDateOfWeek(trainingDate2);
								Log.info("first Block date is : " + firstBlockDate);
								previousWeekDate=firstBlockDate;
								previousDateProxy=trainingDate;
							}
						}else{
								if(previousWeekDate!=null && previousDateProxy!=null){
									
									Date firstWeekDateOfDate =view.getCustomCalenger().getStartDateOfWeek(trainingDate.getTrainingDate());
									
									if(CalendarUtil.getDaysBetween(previousWeekDate,firstWeekDateOfDate)!=0){
										if(previousDateProxy.getTrainingBlock().getId()!=trainingDate.getTrainingBlock().getId()){
											isBlockSplitted=true;
											initializeBlock(firstWeekDateOfDate, trainingDate.getTrainingBlock(),true,true);	
										}else{
											initializeBlock(firstWeekDateOfDate, trainingDate.getTrainingBlock(),false,true);
										}
										
										previousWeekDate=firstWeekDateOfDate;
										previousDateProxy=trainingDate;
									}
								}
						}
						//setting style of view based on is in splitted block or not.
						if(!isBlockSplitted){
							vpanel.setStyleName("dayIsProposedDay");
						}else{
							vpanel.setStyleName("trainingDateOfSplittedBlock");
						}
	
						isFirst=false;
					}
				setBlockTitle(2);
				showApplicationLoading(false);
				}
				//This condition is added to show suggestions when user clicks previos,next month button and suggestions are already generated.
				if(isShowingSuggestions){
					
					allSuggestedViewTrainingDates.clear();
					trainingSuggestionAfterNoonMap.clear();
					trainingSuggestionMorningMap.clear();
					
					showAlreadyGeneratedSuggestions(date);
					
				}else{
					initializeViewForAllReadyScheduledTrainings();
				}
			}
			
		});
		
		showApplicationLoading(true);
		requests.osceDateRequest().findOsceDatesFromGivenDateToEndOfMonth(date,semesterProxy.getId()).fire(new OSCEReceiver<List<OsceDateProxy>>() {

			@Override
			public void onSuccess(List<OsceDateProxy> response) {
				Log.info("found total osce date defined for moth is " + response.size());
				
				if(response!=null){
					
					for(OsceDateProxy osceDate : response){
						Widget widget =view.getCustomCalenger().getPanelOfDate(osceDate.getOsceDate());
						VerticalPanel vpanel =((VerticalPanel)widget);
						Label proposedOsceDateLbl =new Label(constants.proposeOsceDay());
						proposedOsceDateLbl.setStyleName("proposedDayLabel");
						vpanel.add(proposedOsceDateLbl);
						vpanel.setStyleName("dayIsProposedDay");
					}
				}
				showApplicationLoading(false);
			}
		});
		
	}

	/**
	 * This method is used to show already generated suggestions for previous,next months
	 */
	protected void showAlreadyGeneratedSuggestions(Date date) {
		//Now calling following method that will fetch suggestions from the given date to end of the month and show it in view.
		boolean isPreNextBtnClicked=true;
		fetchAndShowSuggestion(date,isPreNextBtnClicked);
		
	}

	private void setShowSuggestionButtonVisible() {
		Log.info("setting visibility of show suggestion button total oscedate create :" + totalOsceDateCreated + " total training date created : " + totalTrainingDateCreated);
		Set<PatientInSemesterProxy> setPatienInSems = semesterProxy.getPatientsInSemester();
		boolean foundAcceptedOsceDates=false;
		boolean foundAcceptedTrainingDates=false;
		boolean isSurveyIsClosed=false;
		
		if(semesterProxy.getSurveyStatus()!=null){
			isSurveyIsClosed=semesterProxy.getSurveyStatus().ordinal()==SurveyStatus.CLOSED.ordinal()?true:false;
		}
		
		if(setPatienInSems!=null){
			
			for (Iterator iterator = setPatienInSems.iterator(); iterator.hasNext();) {
				PatientInSemesterProxy pis = (PatientInSemesterProxy) iterator.next();
				if(pis.getOsceDates().size() >0 ){
					foundAcceptedOsceDates=true;
					break;
				}
			}
			
			for (Iterator iterator = setPatienInSems.iterator(); iterator.hasNext();) {
				PatientInSemesterProxy pis = (PatientInSemesterProxy) iterator.next();
				if(pis.getTrainingDates().size() >0 ){
					foundAcceptedTrainingDates=true;
					break;
				}
			}
		}
		if( isSurveyIsClosed &&foundAcceptedOsceDates && foundAcceptedTrainingDates){
			view.getShowSuggestionButton().setEnabled(true);
			Log.info("seted visibility of show suggestion button to true");
		}else{
			view.getShowSuggestionButton().setEnabled(false);
			Log.info("seted visibility of show suggestion button to false");
		}
		
	}
	
	public void addSelectChangeHandler(SelectChangeHandler handler) 
	{
		handlerManager.addHandler(SelectChangeEvent.getType(), handler);
		removeHandler=handler;
	
	}
	
	@Override
	public void start(AcceptsOneWidget widget, EventBus eventBus) {
		this.widget = widget;
		/*PlanSPTrainingView planSPTrainingView = new PlanSPTrainingViewImpl();
		planSPTrainingView.setDelegate(this);
		this.view = planSPTrainingView;
		singleDaySelectionview=new SingleDaySelectedViewImpl();
		singleDaySelectionview.setDelegate(this);
		widget.setWidget(planSPTrainingView.asWidget());*/
		
		init();
	}

	public void showApplicationLoading(boolean showLoading){
		
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(showLoading));
	}

	@SuppressWarnings("deprecation")
	@Override
	public void dateSelected(final VerticalPanel vpanel,final Date dateOnWidget) {
		
		/*if(true){
			scheduleTrainingView.getScheduledTrainingsPanel().add(updateTrainingView);
			((ScheduleTrainingViewImpl)scheduleTrainingView).show();
			((ScheduleTrainingViewImpl)scheduleTrainingView).setPopupPosition(200, 200);
			//vpanel.add(allReadyScheduledTrainingsView);
			return;
		}*/
		
		if(isSurveyIsStarted){
			vpanel.removeStyleName("dayCellLabel-selected");
			return;
		}
		
		if(currentlySelectdDatePanel!=null){
			if(currentlySelectdDatePanel instanceof SingleDaySelectedView){
				currentlySelectdDatePanel.clear();
			}
			currentlySelectdDatePanel.removeStyleName("dayCellLabel-selected");
		}
		if(currentlySelectedWidgets!=null){
			int middleDateIs = currentlySelectedDatesList.size()/2;
			VerticalPanel vpanel2 =((VerticalPanel)currentlySelectedWidgets.get(middleDateIs).getContent());
			if(vpanel2.getWidgetCount() > 0 && vpanel2.getWidget(0) instanceof MultipleDaySelectedView){
				vpanel2.clear();
			}
		}
		currentlySelectedDate=dateOnWidget;
		currentlySelectdDatePanel=vpanel;
		
		if(isShowingSuggestions){
			
			Log.info("Suggestios are shown so when user clicks any day showing only schedaule training button");
			
			//if(allSuggestedViewTrainingDates.contains(dateOnWidget)){
				singleDaySelectionview.getProposedTrainingDayButton().setVisible(false);
				singleDaySelectionview.getRemoveProposedTrainingDaButton().setVisible(false);
				singleDaySelectionview.getProposedOsceDayButton().setVisible(false);
				singleDaySelectionview.getRemoveProposedOsceDaybutton().setVisible(false);
				vpanel.add(singleDaySelectionview);
				return;
			//}
		}else{
			//Checking if semester status is closed then showing only schedule training button for normal scheduling
			if(semesterProxy.getSurveyStatus().ordinal()==SurveyStatus.CLOSED.ordinal()){
				
				singleDaySelectionview.getProposedTrainingDayButton().setVisible(false);
				singleDaySelectionview.getRemoveProposedTrainingDaButton().setVisible(false);
				singleDaySelectionview.getProposedOsceDayButton().setVisible(false);
				singleDaySelectionview.getRemoveProposedOsceDaybutton().setVisible(false);
				vpanel.add(singleDaySelectionview);
				return;
			}
		}
		showApplicationLoading(true);
		requests.osceDateRequest().dateIsDefinedAsOSceOrTrainingDate(semesterProxy.getId(),dateOnWidget).fire(new OSCEReceiver<String>() {

			@Override
			public void onSuccess(String response) {
				Log.info("selected date is  : " + response);
				showApplicationLoading(false);
				if(response.equals("OSCEDAY")){
					singleDaySelectionview.getProposedTrainingDayButton().setVisible(false);
					singleDaySelectionview.getRemoveProposedTrainingDaButton().setVisible(false);
					singleDaySelectionview.getProposedOsceDayButton().setVisible(false);
					singleDaySelectionview.getRemoveProposedOsceDaybutton().setVisible(true);
					//singleDaySelectionview.setDate(dateOnWidget);
					vpanel.add(singleDaySelectionview);
				}else if(response.equals("TRAININGDATE")){
					singleDaySelectionview.getProposedTrainingDayButton().setVisible(false);
					singleDaySelectionview.getRemoveProposedTrainingDaButton().setVisible(true);
					singleDaySelectionview.getProposedOsceDayButton().setVisible(false);
					singleDaySelectionview.getRemoveProposedOsceDaybutton().setVisible(false);
					//singleDaySelectionview.setDate(dateOnWidget);
					vpanel.add(singleDaySelectionview);
				}else if(response.equals("NONE")){
					singleDaySelectionview.getProposedTrainingDayButton().setVisible(true);
					singleDaySelectionview.getRemoveProposedTrainingDaButton().setVisible(false);
					singleDaySelectionview.getProposedOsceDayButton().setVisible(true);
					singleDaySelectionview.getRemoveProposedOsceDaybutton().setVisible(false);
					singleDaySelectionview.setDate(dateOnWidget);
					vpanel.add(singleDaySelectionview);
				}
			}
		});
		
	}

	@Override
	public void multipleDaysSelected(List<CellWidget> selectedWidget) {
		//Here considering that selected dates not containing any date that is either training date or osce date
		//if this is the case we do not allow to user to drag user after this date
		Log.info("Showing view to user to create tating or osce dates of selectd dates");
		
		if(isSurveyIsStarted){
			removeStyleOfSelectedDays(selectedWidget);
			return;
		}
		
		if(currentlySelectdDatePanel!=null && currentlySelectdDatePanel.getWidgetCount() > 0){
			if(currentlySelectdDatePanel.getWidget(0)instanceof SingleDaySelectedView){
				currentlySelectdDatePanel.clear();
			}else{
				//currentlySelectdWidget.set
			}
		}
		currentlySelectedWidgets=selectedWidget;
		
		currentlySelectedDatesList.clear();
		
		for (CellWidget widget : selectedWidget){
			
			Date selectdDate= widget.getDateOnWidget();
			currentlySelectedDatesList.add(selectdDate);
			widget.applySelectedStyle();
			Log.info("Selected DATE Is : " + selectdDate);
		}
		
		int middleDateIs = currentlySelectedDatesList.size()/2;
		VerticalPanel vpanel =((VerticalPanel)selectedWidget.get(middleDateIs).getContent());
		vpanel.add(multipleDaySelectedView);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void proposeTrainingDateClicked() {
		
		Log.info("creating TrainingDate of date : " + currentlySelectedDate);
		showApplicationLoading(true);
		requests.trainingDateRequest().persistThisDateAsTrainingDate(currentlySelectedDate,semesterProxy.getId()).fire(new OSCEReceiver<TrainingBlockProxy>() {

			@Override
			public void onSuccess(TrainingBlockProxy response) {
				showApplicationLoading(false);
				if(response!=null){
					Log.info("Training date created suucessfully");
					totalTrainingDateCreated+=1;
					//setShowSuggestionButtonVisible();
					if(firstBlockDate==null){
						VerticalPanel blockPanel = view.getCustomCalenger().getBlockCell(currentlySelectedDate);
						if(blockPanel !=null){
							Label blockLbl = new Label(constants.block()+" 1");
							blockPanel.add(blockLbl);
							firstBlockDate=view.getCustomCalenger().getStartDateOfWeek(currentlySelectedDate);
						}
					}
					
					Label proposedTrainingDateLbl =new Label(constants.proposeTrainingDay());
					proposedTrainingDateLbl.setStyleName("proposedDayLabel");
					currentlySelectdDatePanel.clear();
					currentlySelectdDatePanel.add(proposedTrainingDateLbl);
					currentlySelectdDatePanel.setStyleName("dayIsProposedDay");
					//logic of defining block.
					Date firstDateOfThisWeek =view.getCustomCalenger().getStartDateOfWeek(currentlySelectedDate);
					initializeBlock(firstDateOfThisWeek,response,false,true);
				}else{
					Log.info("system could not create training date for given date");
					showErrorMessageToUser("system could not create training date for given date");
				}
			}

			
		});
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void proposeMultipleDaysAsTrainingDaysButtonClicked() {
		Log.info("creating TrainingDate of selected dates : ");
		showApplicationLoading(true);
		requests.trainingDateRequest().persistSelectedMultipleDatesAsTrainingDates(currentlySelectedDatesList,semesterProxy.getId()).fire(new OSCEReceiver<TrainingBlockProxy>() {

			@Override
			public void onSuccess(TrainingBlockProxy response) {
				showApplicationLoading(false);
				if(response!=null){
					Log.info("Training dates created suucessfully");					
					totalTrainingDateCreated+=currentlySelectedDatesList.size();
					//setShowSuggestionButtonVisible();
					if(firstBlockDate==null){
						VerticalPanel blockPanel = view.getCustomCalenger().getBlockCell(currentlySelectedDatesList.get(0));
						if(blockPanel !=null){
							Label blockLbl = new Label(constants.block()+" 1");
							blockPanel.add(blockLbl);
							firstBlockDate=view.getCustomCalenger().getStartDateOfWeek(currentlySelectedDatesList.get(0));
						}
					}
					
					for(CellWidget widget : currentlySelectedWidgets){
						Label proposedTrainingDateLbl =new Label(constants.proposeTrainingDay());
						proposedTrainingDateLbl.setStyleName("proposedDayLabel");
						VerticalPanel vpanel =((VerticalPanel)widget.getContent());
						vpanel.clear();
						vpanel.add(proposedTrainingDateLbl);
						vpanel.setStyleName("dayIsProposedDay");
					}
					Date firstDateOfThisWeek =view.getCustomCalenger().getStartDateOfWeek(currentlySelectedDatesList.get(0));
					initializeBlock(firstDateOfThisWeek,response,false,false);
				}else{
					Log.info("system could not create training dates for given dates");
					showErrorMessageToUser("system could not create training dates for given dates");
				}
			}
		});
	}
	
	private void initializeBlock(Date firstDateOfWeekOfCreatedTD,TrainingBlockProxy blockProxy,boolean isSplittedBlock,boolean isSingleDaySelected) {
		Log.info("Initializing Block");
		Log.info("first block date is" + firstBlockDate);
		Log.info("first week date of slected widget  is" + firstDateOfWeekOfCreatedTD);
		
		
		if(CalendarUtil.getDaysBetween(firstBlockDate,firstDateOfWeekOfCreatedTD)!=0){

			if(blockWidgetsMap.get(firstDateOfWeekOfCreatedTD)!=null){
				
				Log.info("Block is already created for this newly created date so only setting style");
				
				//Setting style of this newly created training date based on whether is is splitted block or un-splitted block.
				
				BlockView view =blockWidgetsMap.get(firstDateOfWeekOfCreatedTD);
				
				if(view.getJoinBlockButton().isVisible()){
					if(isSingleDaySelected){
						currentlySelectdDatePanel.setStyleName("trainingDateOfSplittedBlock");
					}else{
						for(CellWidget widget : currentlySelectedWidgets){
							VerticalPanel vpanel =((VerticalPanel)widget.getContent());
							vpanel.setStyleName("trainingDateOfSplittedBlock");
						}
					}
				}
				
			}else{
				Log.info("Creating new block of created traing date and displaying to user");
				BlockView block = new BlockViewImpl();
				block.setBlockStartDate(firstDateOfWeekOfCreatedTD);
				block.setTrainingBlockProxy(blockProxy);
				block.setIsSplitted(isSplittedBlock);
				block.setDelegate(planSPTrainingActivity);
				if(isSplittedBlock){
					block.getSplitBlockButton().setVisible(false);
					block.getJoinBlockButton().setVisible(true);
					
				}else{
					block.getSplitBlockButton().setVisible(true);
					block.getJoinBlockButton().setVisible(false);
				}
				blockWidgetsMap.put(firstDateOfWeekOfCreatedTD, block);
				
				VerticalPanel blockPanel = view.getCustomCalenger().getBlockCell(firstDateOfWeekOfCreatedTD);
				if(blockPanel !=null){
					blockPanel.add(block);
				}
			}
		}
		
	}

	@Override
	public void proposeOsceDayButtonClicked() {
		
		Log.info("creating OsceDate of date : " + currentlySelectedDate);
		//showApplicationLoading(true);
		boolean isSelectedDateIsOsceDayDate=false;
		if(listOfOsceDayProxy.size() ==0){
			isSelectedDateIsOsceDayDate=true;
		}else{
			if(listOfOsceDayProxy!=null){
				for(OsceDayProxy osceDay : listOfOsceDayProxy){
					
					if(CalendarUtil.getDaysBetween(currentlySelectedDate, osceDay.getOsceDate())==0){
						isSelectedDateIsOsceDayDate=true;
						break;
					}
				}
			}
		}
		if(isSelectedDateIsOsceDayDate){
				persistThisOsceDate();
		}else{
			showWarningToUserAsHeSelectedDiffDateAsOsceDate(false);
		}
		
	}
	
	@Override
	public void proposeMultipleDaysAsOsceDaysButtonClicked() {
		Log.info("creating oscedates of selected dates : ");
	
		boolean isSelectedDateIsOsceDayDate=true;
		
		if(listOfOsceDayProxy.size()==0){
			isSelectedDateIsOsceDayDate=true;
		}else{
			List<Date> allOsceDaysDate = new ArrayList<Date>();
			
			if(listOfOsceDayProxy!=null){
				
				for(OsceDayProxy osceDay : listOfOsceDayProxy){
					allOsceDaysDate.add(osceDay.getOsceDate());
				}
			
				for(Date date : currentlySelectedDatesList){
				if(!allOsceDaysDate.contains(date)){
					isSelectedDateIsOsceDayDate=false;
					break;
				}
			}
		}
		}
		if(isSelectedDateIsOsceDayDate){
			ifUserSelectedTwoConsucutiveDatesThenShowWarningOrPersistDates();
		}else{
			showWarningToUserAsHeSelectedDiffDateAsOsceDate(true);
		}
	}

	@SuppressWarnings("deprecation")
	private void persistMultipleDaysAsOsceDays() {
		Log.info("Persisting OsceDates for selected dates :");
		
		showApplicationLoading(true);
		
		requests.osceDateRequest().persistMultipleDateAsOsceDate(currentlySelectedDatesList,semesterProxy.getId()).fire(new OSCEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				showApplicationLoading(false);
				if(response!=null){
					Log.info("osce dates created suucessfully");
					totalOsceDateCreated+=currentlySelectedDatesList.size();
					//setShowSuggestionButtonVisible();
					if(response){
						for(CellWidget widget : currentlySelectedWidgets){
							Label proposedTrainingDateLbl =new Label(constants.proposeOsceDays());
							proposedTrainingDateLbl.setStyleName("proposedDayLabel");
							VerticalPanel vpanel =((VerticalPanel)widget.getContent());
							vpanel.clear();
							vpanel.add(proposedTrainingDateLbl);
							vpanel.setStyleName("dayIsProposedDay");
						}
					}
				}else{
					Log.info("System could not create osce date for selecte dates");
					showErrorMessageToUser("System could not create osce date for selecte dates");
				}
				
			}
		});
	}

	@SuppressWarnings("deprecation")
	private void persistThisOsceDate() {
		Log.info("Persisting OsceDate for date : " + currentlySelectedDate);
		showApplicationLoading(true);
		requests.osceDateRequest().persistThisDateAsOsceDate(currentlySelectedDate,semesterProxy.getId()).fire(new OSCEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				showApplicationLoading(false);
				if(response!=null){
					Log.info("osce dates created suucessfully");
						totalOsceDateCreated+=1;
					//setShowSuggestionButtonVisible();
					if(response){
						Label proposedTrainingDateLbl =new Label(constants.proposeOsceDay());
						proposedTrainingDateLbl.setStyleName("proposedDayLabel");
						currentlySelectdDatePanel.clear();
						currentlySelectdDatePanel.add(proposedTrainingDateLbl);
						currentlySelectdDatePanel.setStyleName("dayIsProposedDay");
					}
				}else{
					Log.info(" System could not create osce date");
					showErrorMessageToUser("System could not create osce date");
				}
				
			}
		});
		
	}

	private void showWarningToUserAsHeSelectedDiffDateAsOsceDate(final boolean isMultipleDateSelected){
		
		final MessageConfirmationDialogBox confirmationDialogBox =new MessageConfirmationDialogBox(constants.warning());
		confirmationDialogBox.showYesNoDialog(osceMessages.diffentOsceDateDateWarning(getCommaSeperatedOsceDayDates()));
		confirmationDialogBox.getYesBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Log.info("User want this date as osce Date so allowing him to save data");
				confirmationDialogBox.hide();
				if(isMultipleDateSelected){
					ifUserSelectedTwoConsucutiveDatesThenShowWarningOrPersistDates();
				}else{
					persistThisOsceDate();
				}
			}
		});
	}
	protected void ifUserSelectedTwoConsucutiveDatesThenShowWarningOrPersistDates() {
		
		totalConsucutiveDays=1;
		
		Date firstDate = currentlySelectedDatesList.get(0);
		for(int count=1;count<currentlySelectedDatesList.size();count++){
			Date nextDate=currentlySelectedDatesList.get(count);
			if((firstDate.getDate()+1)==nextDate.getDate()){
				totalConsucutiveDays+=1;
			}
			firstDate=nextDate;
		}
	
		if(totalConsucutiveDays > 1) {
			
			final MessageConfirmationDialogBox confirmationDialogBox =new MessageConfirmationDialogBox(constants.warning());
			confirmationDialogBox.showYesNoDialog(osceMessages.selectedMultipleConsecutiveDays(totalConsucutiveDays));
			confirmationDialogBox.getYesBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					confirmationDialogBox.hide();
					persistMultipleDaysAsOsceDays();
					
				}
			});
		}else if(totalConsucutiveDays==1){
			persistMultipleDaysAsOsceDays();
		}
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void scheduleTraingClicked(ClickEvent event) {
		Log.info("schedule Training Button Clicked");
		
		//String format = new SimpleDateFormat("E, MMM d, yyyy").format(currentlySelectedDate.getTime());
		
		String formatedDate = DateTimeFormat.getFormat("EEE, MMM d, yyyy").format(currentlySelectedDate);
		
		Log.info("Formated Date is " + formatedDate);
		
		if(isShowingSuggestions){
			scheduleTrainingForSuggestionView.setDateValue(formatedDate);
		}else{
			scheduleTrainingView.setDateValue(formatedDate);
		}
		final int buttonClientX=event.getClientX();
		
		final int buttonClientY=event.getClientY();
		
		showApplicationLoading(true);
	
		requests.semesterRequest().findTotalPostOfTheSemester(semesterProxy.getId()).with("standardizedRole").fire(new OSCEReceiver<List<OscePostProxy>>() {

			@Override
			public void onSuccess(List<OscePostProxy> response) {
				
				Log.info("Returned call of findTotalPostOfTheSemester");
				
				if(response==null || response.size()==0){
					
					MessageConfirmationDialogBox confirmationDialogBox =new MessageConfirmationDialogBox(constants.warning());
					confirmationDialogBox.showConfirmationDialog(constants.roleNotDefined());
					
					if(currentlySelectdDatePanel.getWidget(0)instanceof SingleDaySelectedView){
						currentlySelectdDatePanel.removeStyleName("dayCellLabel-selected");
						currentlySelectdDatePanel.clear();
					}
					
				}else{
					allOscePostOfSemList=response;
					allowUserToScheduleTraining(buttonClientX,buttonClientY);
				}
				
				showApplicationLoading(false);
			}
		});
	}

	@SuppressWarnings("deprecation")
	private void allowUserToScheduleTraining(final int xPosition,final int yPosition) {
		
		Log.info("taking standardizedRole from osce post");
		
		final List<StandardizedRoleProxy> standardizedRoleProxiesList = new ArrayList<StandardizedRoleProxy>();

		Log.info("Total osce post is : " + allOscePostOfSemList.size());
		
		for(OscePostProxy oscePostProxy : allOscePostOfSemList){
		
			if(oscePostProxy.getStandardizedRole()!=null){
				standardizedRoleProxiesList.add(oscePostProxy.getStandardizedRole());
		  }
		}
		
		if(standardizedRoleProxiesList.size()==0){
			
			MessageConfirmationDialogBox confirmationDialogBox =new MessageConfirmationDialogBox(constants.warning());
			confirmationDialogBox.showConfirmationDialog(constants.roleNotDefined());
			
			if(currentlySelectdDatePanel.getWidget(0)instanceof SingleDaySelectedView){
				currentlySelectdDatePanel.removeStyleName("dayCellLabel-selected");
				currentlySelectdDatePanel.clear();
			}
			
		}else{
			
			Log.info("Total role is : " + standardizedRoleProxiesList.size());
			
			Log.info("Allowing user to schedule Training");
			
			scheduleTrainingForSuggestionView.getHideSuggestionForMorningButton().setVisible(false);
			scheduleTrainingForSuggestionView.getSuggestedTraingsForMorningPanel().clear();
			
			scheduleTrainingForSuggestionView.getHideSuggestionForAfternoonButton().setVisible(false);
			scheduleTrainingForSuggestionView.getSuggestedTraingsForAfternoonPanel().clear();
			if(isShowingSuggestions){
				scheduleTrainingForSuggestionView.getScheduledTrainingsPanel().clear();
			}else{
				scheduleTrainingView.getScheduledTrainingsPanel().clear();
			}
			showApplicationLoading(true);
			
			requests.trainingRequest().findTrainingsOfGivenDate(currentlySelectedDate,semesterProxy.getId()).with("trainingDate","standardizedRole","trainingSuggestions").fire(new OSCEReceiver<List<TrainingProxy>>() {

				@Override
				public void onSuccess(List<TrainingProxy> response) {
					
					showApplicationLoading(false);
					
					scheduleTrainingView.getScheduledTrainingsPanel().clear();
					
					if(response!=null){
						//Remove role that is already used for this day. Also showing view of all ready scheduled training.
						
						if(isShowingSuggestions){
						
							scheduleTrainingForSuggestionView.getScheduleTrainingLabel().setVisible(true);
							showAlreadyScheduledTrainingsForSuggestions(response);
							
							boolean isSomeTrainingIsExist=true;
							
							checkIsMoreSuggestionsExistForDayAndSetSuggestionButtonVisibility(standardizedRoleProxiesList,xPosition,yPosition,isSomeTrainingIsExist);
							
						}else{
							showAlreadyScheduledTrainingsForNormalView(response);
							//find all roles that is assign in block remove this from orignal list and show poup to user by adding this role to suggestion box.
							findAllRolesAssignInBlockAndRemoveThatFromOrgnalListAndShowToUser(standardizedRoleProxiesList,xPosition,yPosition);
						}
						
					}else{
						Date startTime = new Date(currentlySelectedDate.getTime());
						startTime.setHours(9);
						startTime.setMinutes(0);
						
						Date endTime = new Date(currentlySelectedDate.getTime());
						endTime.setHours(12);
						endTime.setMinutes(0);
						
						if(isShowingSuggestions){
							
							scheduleTrainingForSuggestionView.getScheduleTrainingLabel().setVisible(false);

							findAndShowSuggestionRolesForMorningAndAfternoonInPopup();
							
							/*//find all roles that is assign in block remove this from orignal list and show poup to user by adding this role to suggestion box.
							findAllRolesAssignInBlockAndRemoveThatFromOrgnalListAndShowToUser(standardizedRoleProxiesList,xPosition,yPosition);*/
							
							boolean isSomeTrainingIsExist=false;
							
							checkIsMoreSuggestionsExistForDayAndSetSuggestionButtonVisibility(standardizedRoleProxiesList,xPosition,yPosition,isSomeTrainingIsExist);
							
							scheduleTrainingForSuggestionView.setStartTimeValue(startTime);
							scheduleTrainingForSuggestionView.setEndTimeValue(endTime);
						}else{

							scheduleTrainingView.setStartTimeValue(startTime);
							scheduleTrainingView.setEndTimeValue(endTime);
							
							//find all roles that is assign in block remove this from orignal list and show poup to user by adding this role to suggestion box.
							findAllRolesAssignInBlockAndRemoveThatFromOrgnalListAndShowToUser(standardizedRoleProxiesList,xPosition,yPosition);
						}
					}
					
					
				}

			});
			
		}
	}
	
	@SuppressWarnings("deprecation")
	protected void checkForMoreSuggestionsAndSetSuggestionButtonVisibility() {

		requests.trainingDateRequest().findTrainingSuggestionsOfDate(currentlySelectedDate,semesterProxy.getId()).with("standardizedRole","trainingDate").fire(new OSCEReceiver<List<TrainingSuggestionProxy>>() {

			@Override
			public void onSuccess(List<TrainingSuggestionProxy> response) {
				if(response!=null){
					
					moreSuggestionsRoleListForMorning.clear();
					moreSuggestionsRoleListForAfternoon.clear();
				
					for(TrainingSuggestionProxy trainingSuggestionProxy : response){
						if(trainingSuggestionProxy.getTrainingDate().getIsAfternoon()){
							moreSuggestionsRoleListForAfternoon.add(trainingSuggestionProxy);
						}else{
							moreSuggestionsRoleListForMorning.add(trainingSuggestionProxy);
						}
					}
				
					moreSuggestionForMorningMap.put(currentlySelectedDate,moreSuggestionsRoleListForMorning);
					moreSuggestionForAfterNoonMap.put(currentlySelectedDate, moreSuggestionsRoleListForAfternoon);
					
					if(moreSuggestionsRoleListForMorning.size() > 0 || moreSuggestionsRoleListForAfternoon.size() > 0){
						
						/*if(currentlySelectdDatePanel.getWidgetCount()==1){
							if(currentlySelectdDatePanel.getWidget(0) instanceof TrainingView){
								TrainingView tView =(TrainingView)currentlySelectdDatePanel.getWidget(0);
								tView.getShowSuggestionsBtn().setVisible(true);
							}
						}*/
						if(currentlySelectdDatePanel.getWidgetCount() >=1){
							if(currentlySelectdDatePanel.getWidget(currentlySelectdDatePanel.getWidgetCount()-1) instanceof TrainingView){
								TrainingView tView =(TrainingView)currentlySelectdDatePanel.getWidget(currentlySelectdDatePanel.getWidgetCount()-1);
								tView.getShowSuggestionsBtn().setVisible(true);
								
								if(currentlySelectdDatePanel.getWidgetCount() > 1){
									if(currentlySelectdDatePanel.getWidget(currentlySelectdDatePanel.getWidgetCount()-2) instanceof TrainingView){
									
										TrainingView tView2 =(TrainingView)currentlySelectdDatePanel.getWidget(currentlySelectdDatePanel.getWidgetCount()-2);
										tView2.getShowSuggestionsBtn().setVisible(false);
									}
								}
							}
						}else{
							showSuggestionsOfTheDay(moreSuggestionsRoleListForMorning,moreSuggestionsRoleListForAfternoon,currentlySelectedDate);
						}
					}
				}
			}
		});
	}

	@SuppressWarnings("deprecation")
	protected void checkIsMoreSuggestionsExistForDayAndSetSuggestionButtonVisibility(final List<StandardizedRoleProxy> standardizedRoleProxiesList,final int xPosition,final int yPosition,
			final boolean isSomeTrainingIsExist) {
		
		requests.trainingDateRequest().findTrainingSuggestionsOfDate(currentlySelectedDate,semesterProxy.getId()).with("standardizedRole","trainingDate").fire(new OSCEReceiver<List<TrainingSuggestionProxy>>() {

		@Override
		public void onSuccess(List<TrainingSuggestionProxy> response) {
			
			if(response!=null){
				
					moreSuggestionsRoleListForMorning.clear();
					moreSuggestionsRoleListForAfternoon.clear();
				
					for(TrainingSuggestionProxy trainingSuggestionProxy : response){
						if(trainingSuggestionProxy.getTrainingDate().getIsAfternoon()){
							moreSuggestionsRoleListForAfternoon.add(trainingSuggestionProxy);
						}else{
							moreSuggestionsRoleListForMorning.add(trainingSuggestionProxy);
						}
					}
				
					moreSuggestionForMorningMap.put(currentlySelectedDate,moreSuggestionsRoleListForMorning);
					moreSuggestionForAfterNoonMap.put(currentlySelectedDate, moreSuggestionsRoleListForAfternoon);
					
					if(isSomeTrainingIsExist){
						if(moreSuggestionsRoleListForMorning.size() > 0){
							scheduleTrainingForSuggestionView.getShowSuugestionForMorningButton().setVisible(true);
							
						}else{
							scheduleTrainingForSuggestionView.getShowSuugestionForMorningButton().setVisible(false);
						}
						
						if(moreSuggestionsRoleListForAfternoon.size() > 0){
							scheduleTrainingForSuggestionView.getShowSuugestionForAfternoonButton().setVisible(true);
						}else{
							scheduleTrainingForSuggestionView.getShowSuugestionForAfternoonButton().setVisible(false);
						}
					}
					//Setting visibility of show suggestion button.
					if(moreSuggestionsRoleListForMorning.size() > 0 || moreSuggestionsRoleListForAfternoon.size() > 0){
					
						if(currentlySelectdDatePanel.getWidgetCount() >=2){
							if(currentlySelectdDatePanel.getWidget(currentlySelectdDatePanel.getWidgetCount()-2) instanceof TrainingView){
								TrainingView tView =(TrainingView)currentlySelectdDatePanel.getWidget(currentlySelectdDatePanel.getWidgetCount()-2);
								tView.getShowSuggestionsBtn().setVisible(true);
							}
						}	
					}
					
			}
			
			//find all roles that is assign in block remove this from orignal list and show poup to user by adding this role to suggestion box.
			findAllRolesAssignInBlockAndRemoveThatFromOrgnalListAndShowToUser(standardizedRoleProxiesList,xPosition,yPosition);
		}
	});
		
		
	}

	@SuppressWarnings("deprecation")
	private void findAllRolesAssignInBlockAndRemoveThatFromOrgnalListAndShowToUser(final List<StandardizedRoleProxy> standardizedRoleProxiesList,final int xPosition,final int yPosition){
		
		showApplicationLoading(true);
		requests.trainingRequest().findAllRolesAssignInBlock(currentlySelectedDate,semesterProxy.getId()).fire(new OSCEReceiver<List<StandardizedRoleProxy>>() {

			@Override
			public void onSuccess(List<StandardizedRoleProxy> response) {
				Log.info("find roles asign in block");
			
				if(response!=null){
					for(StandardizedRoleProxy standardizedRoleProxy : response){
						
					if(standardizedRoleProxiesList.contains(standardizedRoleProxy)){
						standardizedRoleProxiesList.remove(standardizedRoleProxy);
					}
				  }
			  }

				showApplicationLoading(false);
				
				if(isShowingSuggestions){
					scheduleTrainingForSuggestionView.getStandardizedRoleSuggestionBox().setEnabled(true);
					fillRoleBoxAndShowViewTouserForSuggestion(standardizedRoleProxiesList,xPosition,yPosition);
				}else{
					fillRoleBoxAndShowViewToUser(standardizedRoleProxiesList,xPosition,yPosition);
				}
				
			}

			
		});
	}
	private void showAlreadyScheduledTrainingsForNormalView(List<TrainingProxy> response) {
		
		for(TrainingProxy training : response){
			//Added this code to show only those role for which training is not scheduled for selected date.
			//But as per document now showing only those roles that are not assign for training_block of selected date.
			
			/*if(standardizedRoleProxiesList.contains(training.getStandardizedRole())){
				standardizedRoleProxiesList.remove(training.getStandardizedRole());	
			}*/
			UpdateTrainingView updateTrainingView = new UpdateTrainingViewImpl();
			updateTrainingView.setDelegate(planSPTrainingActivity);
			updateTrainingView.setTrainingProxy(training);
			updateTrainingView.setValue();
			scheduleTrainingView.getScheduledTrainingsPanel().add(updateTrainingView);
		}
		
		
		Date startTime =  new Date(response.get(response.size()-1).getTimeEnd().getTime() +1800000);
		scheduleTrainingView.setStartTimeValue(startTime);
		scheduleTrainingView.setEndTimeValue(new Date(startTime.getTime()+1800000));
		
	}
	
	private void showAlreadyScheduledTrainingsForSuggestions(List<TrainingProxy> response) {
		
		//remove all suggested training role and more buttons that is showing
		Widget lastWidget = currentlySelectdDatePanel.getWidget(currentlySelectdDatePanel.getWidgetCount()-1);

		currentlySelectdDatePanel.clear();

		currentlySelectdDatePanel.removeStyleName("dayIsProposedDay");
		currentlySelectdDatePanel.removeStyleName("dayCellLabel-selected");
		
		boolean isFirst=true;
		TrainingProxy lastTrainingProxy=null;
		// This is to show all previously scheduled training on the selected date view. 
		for(TrainingProxy trainingProxy : response){
			
			if(currentlySelectdDatePanel!=null){

				currentlySelectdDatePanel.addStyleName("greenBackGround");
				
				if(isFirst){
				
					lastTrainingProxy=trainingProxy;
					isFirst=false;
					setTrainingView(trainingProxy,currentlySelectdDatePanel,false);
					
				}else{
					
					Date lastEndDateTime =lastTrainingProxy.getTimeEnd();
					
					Date currentlySelectedTime = trainingProxy.getTimeStart();
				
					 if(currentlySelectedTime.getTime() <= lastEndDateTime.getTime()){
						 setTrainingView(trainingProxy,currentlySelectdDatePanel,true);
					 }else{
						 setTrainingView(trainingProxy,currentlySelectdDatePanel,false);
					 }
					 
					 lastTrainingProxy=trainingProxy;
				}
			}
		}
		currentlySelectdDatePanel.add(lastWidget);
		
		
		// This is to show all previously scheduled training on the popup view of schedule training popup.
		isFirst=true;
		scheduleTrainingForSuggestionView.getScheduledTrainingsPanel().clear();
		for(TrainingProxy training : response){
			
			UpdateTrainingView updateTrainingView = new UpdateTrainingViewImpl();
			updateTrainingView.setDelegate(planSPTrainingActivity);
			updateTrainingView.setTrainingProxy(training);
			updateTrainingView.setValue();
			scheduleTrainingForSuggestionView.getScheduledTrainingsPanel().add(updateTrainingView);
			
			/*if(isFirst){
				lastTrainingProxy=training;
				isFirst=false;
				
				if(!training.getTrainingDate().getIsAfternoon()){
					scheduleTrainingForSuggestionView.getScheduledTraingsForMorningPanel().add(updateTrainingView);
				}else{
					scheduleTrainingForSuggestionView.getScheduledTraingsForAfternoonPanel().add(updateTrainingView);
				}
			
			}else{
				
				Date lastEndDateTime =lastTrainingProxy.getTimeEnd();
				
				Date currentlySelectedTime = training.getTimeStart();
				
				if(currentlySelectedTime.getTime() < lastEndDateTime.getTime()){
						scheduleTrainingForSuggestionView.getOverrideTrainingsPanel().add(updateTrainingView);
				 }else{
					 
					 if(!training.getTrainingDate().getIsAfternoon()){
							scheduleTrainingForSuggestionView.getScheduledTraingsForMorningPanel().add(updateTrainingView);
						}else{
							scheduleTrainingForSuggestionView.getScheduledTraingsForAfternoonPanel().add(updateTrainingView);
						}
				 }
			}*/
			
		}
		
		Date startTime =  new Date(response.get(response.size()-1).getTimeEnd().getTime() +1800000);
		scheduleTrainingForSuggestionView.setStartTimeValue(startTime);
		scheduleTrainingForSuggestionView.setEndTimeValue(new Date(startTime.getTime()+1800000));
		
		//scheduleTrainingForSuggestionView.getShowSuugestionForMorningButton().setVisible(true);
		//scheduleTrainingForSuggestionView.getShowSuugestionForAfternoonButton().setVisible(true);
		
	}
	
	@SuppressWarnings("deprecation")
	private void findAndShowSuggestionRolesForMorningAndAfternoonInPopup() {
		
		//As no previously scheduled training found so removing suggestion view and only showing schedule training button now user can schedule training form popup or normally.
		/*Widget lastWidget = currentlySelectdDatePanel.getWidget(currentlySelectdDatePanel.getWidgetCount()-1);

		currentlySelectdDatePanel.clear();

		currentlySelectdDatePanel.removeStyleName("dayIsProposedDay");
		
		currentlySelectdDatePanel.add(lastWidget);*/
		
		currentlySelectdDatePanel.removeStyleName("dayIsProposedDay");
		
		//showing all morning suggestions.
		TrainingSuggestionView trainingSuggestionForMorningView = trainingSuggestionMorningMap.get(currentlySelectedDate);
		scheduleTrainingForSuggestionView.getSuggestedTraingsForMorningPanel().clear();
		if(trainingSuggestionForMorningView!=null){
			List<StandardizedRoleProxy> roleList =trainingSuggestionForMorningView.getMorningRoleList();
			if(roleList!=null){
				for(StandardizedRoleProxy roleProxy : roleList){
					IndividualSuggestionForTrainingView individualSuggestionForTrainingView = new IndividualSuggestionForTrainingViewImpl();
					individualSuggestionForTrainingView.setDelegate(planSPTrainingActivity);
					individualSuggestionForTrainingView.setStandardizedRoleProxy(roleProxy);
					individualSuggestionForTrainingView.setIsAfternoon(false);
					individualSuggestionForTrainingView.setValue();
					scheduleTrainingForSuggestionView.getSuggestedTraingsForMorningPanel().add(individualSuggestionForTrainingView);
				}
			}
		}
		
		//showing all Afternoon suggestions.
		TrainingSuggestionView trainingSuggestionForAfternoonView = trainingSuggestionAfterNoonMap.get(currentlySelectedDate);
		scheduleTrainingForSuggestionView.getSuggestedTraingsForAfternoonPanel().clear();
		if(trainingSuggestionForAfternoonView!=null){
			List<StandardizedRoleProxy> roleList =trainingSuggestionForAfternoonView.getAfterNoonRoleList();
			if(roleList!=null){
				for(StandardizedRoleProxy roleProxy : roleList){
					IndividualSuggestionForTrainingView individualSuggestionForTrainingView = new IndividualSuggestionForTrainingViewImpl();
					individualSuggestionForTrainingView.setDelegate(planSPTrainingActivity);
					individualSuggestionForTrainingView.setStandardizedRoleProxy(roleProxy);
					individualSuggestionForTrainingView.setIsAfternoon(true);
					individualSuggestionForTrainingView.setValue();
					scheduleTrainingForSuggestionView.getSuggestedTraingsForAfternoonPanel().add(individualSuggestionForTrainingView);
				}
			}
		}
		
		scheduleTrainingForSuggestionView.getShowSuugestionForMorningButton().setVisible(false);
		scheduleTrainingForSuggestionView.getShowSuugestionForAfternoonButton().setVisible(false);
		
	}

	private void fillRoleBoxAndShowViewTouserForSuggestion(List<StandardizedRoleProxy> standardizedRoleProxiesList,int xPosition,int yPosition) {

		DefaultSuggestOracle<StandardizedRoleProxy> suggestOracle1 = (DefaultSuggestOracle<StandardizedRoleProxy>)scheduleTrainingForSuggestionView.getStandardizedRoleSuggestionBox().getSuggestOracle();
		
		suggestOracle1.setPossiblilities(standardizedRoleProxiesList);
		
		scheduleTrainingForSuggestionView.getStandardizedRoleSuggestionBox().setSuggestOracle(suggestOracle1);
		
		scheduleTrainingForSuggestionView.getStandardizedRoleSuggestionBox().setRenderer(new AbstractRenderer<StandardizedRoleProxy>() {

			@Override
			public String render(StandardizedRoleProxy object) {
				if (object != null)
					return (object.getShortName() +  " - " + object.getLongName());
				else
					return "";
			}
		});
		
		
		
		((ScheduleTrainingForSuggestionViewImpl)scheduleTrainingForSuggestionView).showRelativeTo(singleDaySelectionview.getRemoveProposedTrainingDaButton());
		
		((ScheduleTrainingForSuggestionViewImpl)scheduleTrainingForSuggestionView).show();
		
		//int height = ((ScheduleTrainingForSuggestionViewImpl)scheduleTrainingForSuggestionView).getTrainingHtmlPanel().getOffsetHeight();
		//height+=0;
		
		((ScheduleTrainingForSuggestionViewImpl)scheduleTrainingForSuggestionView).setPopupPosition(xPosition-240,yPosition);	
		
		int windowHeight= Window.getClientHeight();
		
		int upperBottomValue = windowHeight-(yPosition-25);
		
		int lowerBottomValue = yPosition+20;
		
		int windowHalfHeight = (windowHeight/2);
		
		Log.info("btn width  : " + xPosition + "btn height : " + yPosition  + "window height : " + windowHeight);
		
		if(yPosition >= windowHalfHeight)
		{
			//When button y position is more or equal to window height then showing popup on upper side of button.
			
			Log.info("showing popup on upper side");
			
			((ScheduleTrainingForSuggestionViewImpl)scheduleTrainingForSuggestionView).removeStyleName("lowerPopupStyle");
			
			((ScheduleTrainingForSuggestionViewImpl)scheduleTrainingForSuggestionView).addStyleName("upperPopupStyle");
		
			if(currentlySelectdDatePanel.getWidgetCount() >=2){
				if(currentlySelectdDatePanel.getWidget(currentlySelectdDatePanel.getWidgetCount()-2) instanceof TrainingView){
					TrainingView tView =(TrainingView)currentlySelectdDatePanel.getWidget(currentlySelectdDatePanel.getWidgetCount()-2);
					if(tView.getShowSuggestionsBtn().isVisible()){
						upperBottomValue+=10;
					}
				}
			}
			
			StyleInjector.inject(".upperPopupStyle{bottom :" + upperBottomValue +"px !important;}");
			//Hiding upper arrow
			((ScheduleTrainingForSuggestionViewImpl)scheduleTrainingForSuggestionView).getUpperArrowSpan().getStyle().setDisplay(Display.NONE);
			
			//showing lower arrow
			((ScheduleTrainingForSuggestionViewImpl)scheduleTrainingForSuggestionView).getBottomArrowSpan().getStyle().clearDisplay();
			
		}else{

			//When button y position is less then window height then showing popup on lower side of button.
			
			Log.info("showing popup on lower side");
			
			((ScheduleTrainingForSuggestionViewImpl)scheduleTrainingForSuggestionView).removeStyleName("upperPopupStyle");
			
			((ScheduleTrainingForSuggestionViewImpl)scheduleTrainingForSuggestionView).addStyleName("lowerPopupStyle");
		
			StyleInjector.inject(".lowerPopupStyle{top :" + lowerBottomValue +"px !important;}");
			
			//Hiding lower arrow
			((ScheduleTrainingForSuggestionViewImpl)scheduleTrainingForSuggestionView).getBottomArrowSpan().getStyle().setDisplay(Display.NONE);
			
			//showing upper arrow
			((ScheduleTrainingForSuggestionViewImpl)scheduleTrainingForSuggestionView).getUpperArrowSpan().getStyle().clearDisplay();
			
			
		}
		
	}
	
	private void fillRoleBoxAndShowViewToUser(List<StandardizedRoleProxy> standardizedRoleProxiesList,int xPosition,int yPosition) {
		
		DefaultSuggestOracle<StandardizedRoleProxy> suggestOracle1 = (DefaultSuggestOracle<StandardizedRoleProxy>)scheduleTrainingView.getStandardizedRoleSuggestionBox().getSuggestOracle();
		
		suggestOracle1.setPossiblilities(standardizedRoleProxiesList);
		
		scheduleTrainingView.getStandardizedRoleSuggestionBox().setSuggestOracle(suggestOracle1);
		
		scheduleTrainingView.getStandardizedRoleSuggestionBox().setRenderer(new AbstractRenderer<StandardizedRoleProxy>() {

			@Override
			public String render(StandardizedRoleProxy object) {
				if (object != null)
					return (object.getShortName() +  " - " + object.getLongName());
				else
					return "";
			}
		});
		
		scheduleTrainingView.setValueOfIgnoreTrainingBlockCheckBox(false);
		
		((ScheduleTrainingViewImpl)scheduleTrainingView).show();
		
		int height = ((ScheduleTrainingViewImpl)scheduleTrainingView).getTrainingHtmlPanel().getOffsetHeight();
		height+=22;
		
		((ScheduleTrainingViewImpl)scheduleTrainingView).setPopupPosition(xPosition-250,yPosition-height);	
		
		int windowHeight= Window.getClientHeight();
		
		int upperBottomValue = windowHeight-(yPosition-20);
		
		int lowerBottomValue = yPosition+20;
		
		int windowHalfHeight = (windowHeight/2);
		
		Log.info("btn width  " +xPosition + "btn height" + yPosition  + "window height " + windowHeight);
		
		if(yPosition >= windowHalfHeight)
		{
			//When button y position is more or equal to window height then showing popup on upper side of button.
			
			Log.info("showing popup on upper side");
			
			((ScheduleTrainingViewImpl)scheduleTrainingView).removeStyleName("lowerPopupStyle");
			
			((ScheduleTrainingViewImpl)scheduleTrainingView).addStyleName("upperPopupStyle");
		
			StyleInjector.inject(".upperPopupStyle{bottom :" + upperBottomValue +"px !important;}");
			
			//Hiding upper arrow
			((ScheduleTrainingViewImpl)scheduleTrainingView).getUpperArrowSpan().getStyle().setDisplay(Display.NONE);
			
			//showing lower arrow
			((ScheduleTrainingViewImpl)scheduleTrainingView).getBottomArrowSpan().getStyle().clearDisplay();
			
		}else{
		
			//When button y position is less then window height then showing popup on lower side of button.
			
			Log.info("showing popup on lower side");
			
			((ScheduleTrainingViewImpl)scheduleTrainingView).removeStyleName("upperPopupStyle");
			
			((ScheduleTrainingViewImpl)scheduleTrainingView).addStyleName("lowerPopupStyle");
		
			StyleInjector.inject(".lowerPopupStyle{top :" + lowerBottomValue +"px !important;}");
			
			//Hiding lower arrow
			((ScheduleTrainingViewImpl)scheduleTrainingView).getBottomArrowSpan().getStyle().setDisplay(Display.NONE);
			
			//showing upper arrow
			((ScheduleTrainingViewImpl)scheduleTrainingView).getUpperArrowSpan().getStyle().clearDisplay();
		}
		
	}
	private String getCommaSeperatedOsceDayDates(){
		String dates="";
		if(listOfOsceDayProxy.size() > 0){
			for(OsceDayProxy osceDay : listOfOsceDayProxy){
				dates+=dateFormat.format(osceDay.getOsceDate());
				dates+=",";
			}
			
			dates.substring(0, dates.length());
		}
		
		return dates;
	}
	@SuppressWarnings("deprecation")
	@Override
	public void removeTrainingDayButtonClicked() {
		
		Log.info("Removing TrainingDate of date : " + currentlySelectedDate);
		showApplicationLoading(true);
		requests.trainingDateRequest().removeTrainingDate(currentlySelectedDate,semesterProxy.getId()).fire(new OSCEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				showApplicationLoading(false);
				if(response!=null){
					totalTrainingDateCreated-=1;
					//setShowSuggestionButtonVisible();
					Log.info("Training date removed suucessfully");
					currentlySelectdDatePanel.clear();
					currentlySelectdDatePanel.removeStyleName("dayIsProposedDay");
					currentlySelectdDatePanel.removeStyleName("trainingDateOfSplittedBlock");
					currentlySelectdDatePanel.removeStyleName("dayCellLabel-selected");
					if(response){
						
						VerticalPanel blockPanel = view.getCustomCalenger().getBlockCell(currentlySelectedDate);
						if(blockPanel !=null){
							blockPanel.clear();
						}
					}
					Date firstDateOfWeek = view.getCustomCalenger().getStartDateOfWeek(currentlySelectedDate);
					Date firstDate =view.getCustomCalenger().getStartDateOfWeek(currentlySelectedDate);
					Date lastDateOfWeek = view.getCustomCalenger().getLastDateOfWeek(currentlySelectedDate);
					removeBlock(firstDateOfWeek,lastDateOfWeek,firstDate);
				}else{
					Log.info("System could not remove Training date");
					showErrorMessageToUser("System could not remove Training date");
				}
				
			}

			
		});
		
	}

	private void removeBlock(Date firstDateOfWeek,Date lastDateOfWeek,Date firstDate) {

		boolean isWidgetExist=false;
		
		while(CalendarUtil.getDaysBetween(firstDateOfWeek,lastDateOfWeek)!=0){
		
			Widget widget =view.getCustomCalenger().getPanelOfDate(firstDateOfWeek);
			VerticalPanel vpanel =((VerticalPanel)widget);
			if(vpanel.getWidgetCount()>0){
				Label label =(Label)vpanel.getWidget(0);
				if(label.getText().equals(constants.proposeTrainingDay())){
					isWidgetExist=true;
					break;
				}
			}
			CalendarUtil.addDaysToDate(firstDateOfWeek,1);
		}
		
		if(!isWidgetExist){
			Log.info("Removing block of date +" + firstDate);
			if(CalendarUtil.getDaysBetween(firstBlockDate, firstDate)==0){
				VerticalPanel panel =view.getCustomCalenger().getBlockCell(firstDate);
				if(panel!=null){
					panel.clear();
				}
				blockWidgetsMap.clear();
				/*BlockView blockView = blockWidgetsMap.get(firstBlockDate);
				while(blockView==null){
					CalendarUtil.addDaysToDate(firstDate,7);
					blockView=blockWidgetsMap.get(firstDate);
				}
				blockView.clearView();
				blockView.setIsSplitted(true);
				setBlockTitle(1);
				
				blockWidgetsMap.remove(blockView.getBlockStartDate());*/
				
			}else{

				BlockView view = blockWidgetsMap.get(firstDate);
				view.clearView();
				blockWidgetsMap.remove(firstDate);
				setBlockTitle(2);
			}
			
		}else{
			Log.info("There is date on which Training date is created");
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void removeOsceDayButtonClicked() {
		Log.info("Removing osceDate for date : " + currentlySelectedDate);
		showApplicationLoading(true);
		requests.osceDateRequest().removeOsceDateForGivenDate(currentlySelectedDate,semesterProxy.getId()).fire(new OSCEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				showApplicationLoading(false);
				if(response!=null){
					totalOsceDateCreated-=1;
					//setShowSuggestionButtonVisible();
					if(response){
						Log.info("osce date removed suucessfully");
						currentlySelectdDatePanel.clear();
						currentlySelectdDatePanel.removeStyleName("dayIsProposedDay");
						currentlySelectdDatePanel.removeStyleName("dayCellLabel-selected");
					}
				}else{
					Log.info("System could not remove osce date");
					showErrorMessageToUser("System could not remove osce date");
				}
				
			}
		});
	}

	@SuppressWarnings("deprecation")
	@Override
	public void splitBlockClicked(final Date blockStartDate,TrainingBlockProxy trainingBlockProxy) {
		Log.info("split block button clicked so creating new view of date" + blockStartDate);
		showApplicationLoading(true);
		
		requests.trainingBlockRequest().splitBlock(blockStartDate, trainingBlockProxy.getId(), semesterProxy.getId()).with("trainingBlock").fire(new OSCEReceiver<List<TrainingDateProxy>>() {

			@Override
			public void onSuccess(List<TrainingDateProxy> response) {
				if(response!=null){
				
					for(TrainingDateProxy trainingDateProxy : response){
						
						Widget widget =view.getCustomCalenger().getPanelOfDate(trainingDateProxy.getTrainingDate());
						VerticalPanel vpanel =((VerticalPanel)widget);
						
						if(vpanel !=null){
							vpanel.setStyleName("trainingDateOfSplittedBlock");
						}
					}
					showTrainingBlockView(blockStartDate,true,response.get(0).getTrainingBlock());
					showApplicationLoading(false);
				}else{
					Log.info("System could not split training blocks");
					showErrorMessageToUser("System could not split training blocks");
				}
			}
			
		});
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void joinBlockButtonClicked(final Date blockStartDate,TrainingBlockProxy trainingBlockProxy) {
		Log.info("join block button clicked so joining block of date" + blockStartDate);
		showApplicationLoading(true);
		requests.trainingBlockRequest().joinBlock(blockStartDate,trainingBlockProxy.getId(),semesterProxy.getId()).with("trainingBlock").fire(new OSCEReceiver<List<TrainingDateProxy>>() {

			@Override
			public void onSuccess(List<TrainingDateProxy> response) {
				
				if(response!=null){
					
					for(TrainingDateProxy trainingDateProxy : response){
						
						Widget widget =view.getCustomCalenger().getPanelOfDate(trainingDateProxy.getTrainingDate());
						VerticalPanel vpanel =((VerticalPanel)widget);
						
						if(vpanel !=null){
							vpanel.setStyleName("dayIsProposedDay");
						}
					}
					showTrainingBlockView(blockStartDate,false,response.get(0).getTrainingBlock());
					showApplicationLoading(false);
				}else{
					Log.info("System could not join training blocks");
					showErrorMessageToUser("System could not join training blocks");
				}
			}
		});
		
	}
	
	private void showTrainingBlockView(Date blockStartDate,boolean issplitted,TrainingBlockProxy trainingBlockProxy) {
	
		BlockView view =blockWidgetsMap.get(blockStartDate);
		
		view.setTrainingBlockProxy(trainingBlockProxy);
		
		if(issplitted){
			view.setIsSplitted(true);
			view.getSplitBlockButton().setVisible(false);
			view.getJoinBlockButton().setVisible(true);
		}else{
			view.setIsSplitted(false);
			view.getSplitBlockButton().setVisible(true);
			view.getJoinBlockButton().setVisible(false);
		}
		
		setBlockTitle(2);
		
	}

	private void setBlockTitle(int blockCount) {
		
		for (Map.Entry<Date,BlockView> block : blockWidgetsMap.entrySet())
		{
		    if(block.getValue().getIsSplitted()){
		    	block.getValue().getBlockLabel().setVisible(true);
		    	block.getValue().getBlockLabel().setText(constants.block() + " "+ blockCount);
		    	blockCount+=1;
		    }else{
		    	block.getValue().getBlockLabel().setVisible(false);
		    }
		}
		
	}

	@Override
	public void startSurveyButtonClicked() {
		Log.info("User want to start survey");
		
		final MessageConfirmationDialogBox confirmationDialogBox =new MessageConfirmationDialogBox(constants.warning());
		confirmationDialogBox.showYesNoDialog(constants.startSurveyConfirmation());
		confirmationDialogBox.getYesBtn().addClickHandler(new ClickHandler() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(ClickEvent event) {
				Log.info("Yes button clicked");
				confirmationDialogBox.hide();
				
				checkUserHasDefinedAtleastBothDate();
				
				
			}

			
		});
	}
	
	@SuppressWarnings("deprecation")
	private void checkUserHasDefinedAtleastBothDate() {

		Log.info("sem id is " + semesterProxy.getId());
		
		if(totalOsceDateCreated <= 0 && totalTrainingDateCreated <= 0 ){
			
			final MessageConfirmationDialogBox confirmationDialogBox =new MessageConfirmationDialogBox(constants.warning());
			confirmationDialogBox.showYesNoDialog(constants.noOsceOrTrainingdateProposed());
			confirmationDialogBox.getYesBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					Log.info("Yes button clicked");
					confirmationDialogBox.hide();
				}
			});
			
		}
		else{
				showApplicationLoading(true);
				requests.osceDateRequest().isThereAnyTrainingDateThatIsAfterOSceDate(semesterProxy.getId()).fire(new OSCEReceiver<Boolean>() {

				@Override
				public void onSuccess(Boolean response) {
					showApplicationLoading(false);
					if(response!=null){
						
						if(response){
							showTrainingDateIsAfterOsceDateConfirmationMsg();
						}else{
						
							checkPatientInSemesterIsAssignInSemester();
						}
					}
				}

			});
		}
		
	}
	
	private void showTrainingDateIsAfterOsceDateConfirmationMsg(){
		
		Log.info("showing training date is after oscedate confirmation");
		
		final MessageConfirmationDialogBox confirmationDialogBox =new MessageConfirmationDialogBox(constants.warning());
		confirmationDialogBox.showYesNoDialog(constants.trainingDateIsAfterOsceDate());
		confirmationDialogBox.getYesBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Log.info("Yes button clicked");
				confirmationDialogBox.hide();
				checkPatientInSemesterIsAssignInSemester();
			}
		});
	}
	private void checkPatientInSemesterIsAssignInSemester() {

		if(semesterProxy.getPatientsInSemester()==null || semesterProxy.getPatientsInSemester().size() == 0){
		
			final MessageConfirmationDialogBox confirmationDialogBox =new MessageConfirmationDialogBox(constants.warning());
			
			HorizontalPanel hp = confirmationDialogBox.getHp();
			
			IconButton manuallyAssignSp = new IconButton(constants.assignSPManually(), new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					Log.info("Manually assign SPs now btn clicked");
					confirmationDialogBox.hide();
					placeController.goTo(new RoleAssignmentPlace("SPRoleAssignmentPlace",handlerManager,semesterProxy));
				}
			});
			hp.add(manuallyAssignSp);
			confirmationDialogBox.showYesNoDialog(constants.noPISFoundForSemester());
			confirmationDialogBox.getYesBtn().setText(constants.assignAllActiveSP());
			confirmationDialogBox.getYesBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					Log.info("Assign All acticve sps btn clicked");
					confirmationDialogBox.hide();
					assignPatientInSemester();
				}

				
			});
		}else{
			proceedToStartSurvey();		
		}
	}
	@SuppressWarnings("deprecation")
	private void assignPatientInSemester() {
	
		showApplicationLoading(true);
		requests.patientInSemesterRequest().assignSPToSemester(semesterProxy.getId()).fire(new OSCEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				Log.info("PIS assign successfully");
				showApplicationLoading(false);
				if(response!=null){
					if(response){
						proceedToStartSurvey();		
					}
				}
				
			}
			
		});
		
	}
	@SuppressWarnings("deprecation")
	private void proceedToStartSurvey() {
		Log.info("Starting survey");
		
		showApplicationLoading(true);
		requests.semesterRequest().surveyIsStartedSoPushDataToSpPortal(semesterProxy.getId()).fire(new OSCEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				Log.info("Surver started successfully");
				showApplicationLoading(false);
				if(response!=null){
					if(response){
						view.getStartSurveyButton().setVisible(false);
						view.getStopSurveyButton().setVisible(true);
						isSurveyIsStarted=true;
					}
				}else{
					showErrorMessageToUser("System could not start survey please try again");
				}
				
			}
		});
		
	}
	public void removeStyleOfSelectedDays(List<CellWidget> selWidgetList){

		if(selWidgetList!=null){
			for (CellWidget widget : selWidgetList){
				widget.removeSelectedStyle();
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void stopSurveyButtonClicked() {
		Log.info("Stop Survey Button clicked");
		showApplicationLoading(true);
		
		requests.semesterRequest().stopSurveyAndPushDateToOsceFromSpPortal(semesterProxy.getId()).fire(new OSCEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				showApplicationLoading(false);
				Log.info("Survey stoped and data is pushed from spportal to osce");
				if(response!=null){
					if(response){
						view.getStartSurveyButton().setVisible(true);
						view.getStartSurveyButton().setEnabled(false);
						view.getStopSurveyButton().setVisible(false);
						isSurveyIsStarted=false;
					}
				}else{
					showErrorMessageToUser("System could not stop survey please try again");
				}
				
			}
		});
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void showSuggestionButtonClicked() {
	Log.info("checking patients are assign in role");
	
	showApplicationLoading(true);
	requests.semesterRequest().checkAllSpIsAssignInRole(semesterProxy.getId()).fire(new OSCEReceiver<Boolean>() {

		@Override
		public void onSuccess(Boolean response) {
			showApplicationLoading(false);
			Log.info("Patients are assign in all posts : " + response);
			if(response!=null){
				if(response){
					createAndshowSuggestion();
				}else{
					
					final MessageConfirmationDialogBox confirmationDialogBox =new MessageConfirmationDialogBox(constants.warning());
					confirmationDialogBox.showConfirmationDialog(constants.spsAreNotAssignedToRole());
				}
			}else{
				Log.info("System could not find whether all sps are assign in role");
				showErrorMessageToUser("System could not find whether all sps are assign in role");
			}
			
		}
	});
		
	}
	@SuppressWarnings("deprecation")
	private void createAndshowSuggestion() {
		
		Log.info("creating suggestion");
		
		showApplicationLoading(true);
		 requests.trainingSuggestionRequest().createSuggestion(semesterProxy.getId()).fire(new OSCEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				showApplicationLoading(false);
				if(response!=null){
					if(response){
						boolean isPreNextBtnClicked=false;
						fetchAndShowSuggestion(new Date(),isPreNextBtnClicked);
					}
				}else{
					Log.info("System could not create training suggestions");
				}
				
			}
		});
		
	}
	
	@SuppressWarnings("deprecation")
	private void fetchAndShowSuggestion(final Date date,final boolean isPreNextBtnClicked) {
		Log.info("fetching suggestions data");
		showApplicationLoading(true);
		requests.trainingSuggestionRequest().getSuggestionsFromGivenDate(date,semesterProxy.getId()).with("trainingDate","standardizedRole").fire(new OSCEReceiver<List<TrainingSuggestionProxy>>() {

			@Override
			public void onSuccess(List<TrainingSuggestionProxy> response) {
				if(response!=null){
					trainingSuggestionList=response;
					view.getShowSuggestionButton().setVisible(false);
					view.getHideSuggestionButton().setVisible(true);
					isShowingSuggestions=true;
					showSuggestion(date,isPreNextBtnClicked);
				}else{
					Log.info("No suggestions are found for the given month");
					showErrorMessageToUser(constants.noSuggestionsFound());
				}
				
				showApplicationLoading(false);
			}

			
			
		});
	}
	
	/**
	 * This method is used to generated suggestions.(it shows remaining suggestions when isPreNextBtnClicked is true). 
	 * @param isPreNextBtnClicked
	 */
	@SuppressWarnings("deprecation")
	private void showSuggestion(final Date startDate,boolean isPreNextBtnClicked) {
		Log.info("showing suggestions");
		
		boolean isFirst=true;
		Date lastDate=null;
		//boolean isDateChanged=false;
		
		List<StandardizedRoleProxy> morningRolesList = new ArrayList<StandardizedRoleProxy>();;
		List<StandardizedRoleProxy> afterNoonRolesList = new ArrayList<StandardizedRoleProxy>();
		for(TrainingSuggestionProxy trainingSuggestion : trainingSuggestionList){
			
			Log.info("suggestion is : "  + trainingSuggestion.getId());
			Log.info("Training date is : "  + trainingSuggestion.getTrainingDate().getTrainingDate());
			
			if(isFirst){
				lastDate = trainingSuggestion.getTrainingDate().getTrainingDate();
				isFirst=false;
				allSuggestedViewTrainingDates.add(lastDate);
			}
			
			if(CalendarUtil.getDaysBetween(lastDate,trainingSuggestion.getTrainingDate().getTrainingDate())!=0){
				trainingSuggestionMorningMap.get(lastDate).setMorningRoleList(morningRolesList);
				trainingSuggestionAfterNoonMap.get(lastDate).setAfterNoonRoleList(afterNoonRolesList);
				morningRolesList = new ArrayList<StandardizedRoleProxy>();
				afterNoonRolesList = new ArrayList<StandardizedRoleProxy>();
				lastDate = trainingSuggestion.getTrainingDate().getTrainingDate();
				allSuggestedViewTrainingDates.add(trainingSuggestion.getTrainingDate().getTrainingDate());
				//isDateChanged=true;
				
			}
			if(!trainingSuggestion.getTrainingDate().getIsAfternoon()){
				
				if(trainingSuggestionMorningMap.get(trainingSuggestion.getTrainingDate().getTrainingDate())!=null){
					VerticalPanel roleNamePanel =trainingSuggestionMorningMap.get(trainingSuggestion.getTrainingDate().getTrainingDate()).getRoleNameVerticalPanel();
					if(roleNamePanel.getWidgetCount() < OsMaConstant.MAX_ROLE_NAME_DURING_SUGGESTION){
						roleNamePanel.add(setRoleNameText(trainingSuggestion.getStandardizedRole().getShortName()));
					}else if(roleNamePanel.getWidgetCount()==OsMaConstant.MAX_ROLE_NAME_DURING_SUGGESTION){
						trainingSuggestionMorningMap.get(trainingSuggestion.getTrainingDate().getTrainingDate()).getMoreButton().setVisible(true);
					}
					
				}else{
					
					TrainingSuggestionView trainingSuggestionView = new TrainingSuggestionViewImpl();
					trainingSuggestionView.setDelegate(planSPTrainingActivity);
					trainingSuggestionView.setIsMorning(true);
					trainingSuggestionView.getRoleNameVerticalPanel().add(setRoleNameText(trainingSuggestion.getStandardizedRole().getShortName()));
					trainingSuggestionMorningMap.put(trainingSuggestion.getTrainingDate().getTrainingDate(), trainingSuggestionView);
				}
				morningRolesList.add(trainingSuggestion.getStandardizedRole());
			}else{
				
				if(trainingSuggestionAfterNoonMap.get(trainingSuggestion.getTrainingDate().getTrainingDate())!=null){
					
					VerticalPanel roleNamePanel =trainingSuggestionAfterNoonMap.get(trainingSuggestion.getTrainingDate().getTrainingDate()).getRoleNameVerticalPanel();
					if(roleNamePanel.getWidgetCount() < OsMaConstant.MAX_ROLE_NAME_DURING_SUGGESTION){
						roleNamePanel.add(setRoleNameText(trainingSuggestion.getStandardizedRole().getShortName()));
					}else if(roleNamePanel.getWidgetCount()==OsMaConstant.MAX_ROLE_NAME_DURING_SUGGESTION){
						trainingSuggestionAfterNoonMap.get(trainingSuggestion.getTrainingDate().getTrainingDate()).getMoreButton().setVisible(true);
					}
				}else{
					
					TrainingSuggestionView trainingSuggestionView = new TrainingSuggestionViewImpl();
					trainingSuggestionView.setDelegate(planSPTrainingActivity);
					trainingSuggestionView.setIsMorning(false);
					trainingSuggestionView.getRoleNameVerticalPanel().add(setRoleNameText(trainingSuggestion.getStandardizedRole().getShortName()));
					trainingSuggestionAfterNoonMap.put(trainingSuggestion.getTrainingDate().getTrainingDate(), trainingSuggestionView);
				}
				afterNoonRolesList.add(trainingSuggestion.getStandardizedRole());
			}
			
		}
		
		//if(!isDateChanged){
			int lastAddedDate =allSuggestedViewTrainingDates.size()-1;
			
			if(trainingSuggestionMorningMap.get(allSuggestedViewTrainingDates.get(lastAddedDate))!=null){
				trainingSuggestionMorningMap.get(allSuggestedViewTrainingDates.get(allSuggestedViewTrainingDates.size()-1)).setMorningRoleList(morningRolesList);
			}
			if(trainingSuggestionAfterNoonMap.get(allSuggestedViewTrainingDates.get(lastAddedDate))!=null){
				trainingSuggestionAfterNoonMap.get(allSuggestedViewTrainingDates.get(allSuggestedViewTrainingDates.size()-1)).setAfterNoonRoleList(afterNoonRolesList);
			}
		//}
			//Following condition is added to show suggestions/remaining suggestions when previous or next button is clicked.
			
			/*Scenario is that 	when user generates suggestions and then clicks next/previous button to see suggestion of the previous/next month then I need to show suggestion
			of that month and when user come back on current month I need to show all remaining suggestion (if he has scheduled training of some suggestions before going on
		    previous/next month) */	

			if(isPreNextBtnClicked){

				requests.trainingSuggestionRequest().findTrainingSuggestionFromGivenDateToEndOfMonthForSem(startDate,semesterProxy.getId()).with("standardizedRole","trainingDate").fire(new OSCEReceiver<List<TrainingSuggestionProxy>>() {

					@Override
					public void onSuccess(List<TrainingSuggestionProxy> response) {
						if(response!=null){
							
							List<TrainingSuggestionProxy> morningRoleList = new ArrayList<TrainingSuggestionProxy>();
							List<TrainingSuggestionProxy> afterNoonRolesList = new ArrayList<TrainingSuggestionProxy>();
							
							Date cDate =startDate;
							
							for(TrainingSuggestionProxy trainingSuggestionProxy : response){
								/*System.out.println("T dats is : " + trainingSuggestionProxy.getId() + "Dt id : " +trainingSuggestionProxy.getId() + " Date : "
							+ trainingSuggestionProxy.getTrainingDate().getTrainingDate());*/
								if(CalendarUtil.getDaysBetween(cDate,trainingSuggestionProxy.getTrainingDate().getTrainingDate())!=0){
									
									moreSuggestionForMorningMap.put(cDate,morningRoleList);
									moreSuggestionForAfterNoonMap.put(cDate, afterNoonRolesList);
									
									cDate=trainingSuggestionProxy.getTrainingDate().getTrainingDate();
									
									morningRoleList = new ArrayList<TrainingSuggestionProxy>();
									afterNoonRolesList = new ArrayList<TrainingSuggestionProxy>();
								}
								
								if(trainingSuggestionProxy.getTrainingDate().getIsAfternoon()){
									afterNoonRolesList.add(trainingSuggestionProxy);
								}else{
									morningRoleList.add(trainingSuggestionProxy);
								}
							}
							moreSuggestionForMorningMap.put(response.get(response.size()-1).getTrainingDate().getTrainingDate(),morningRoleList);
							moreSuggestionForAfterNoonMap.put(response.get(response.size()-1).getTrainingDate().getTrainingDate(),afterNoonRolesList);
							
							for(Date date : allSuggestedViewTrainingDates){
								showRemainingSuggestions(date);
							}
							
						}
					}
				});
				
			}else{
				//For Normal scenario showing suggestions to user.
				//Now putting widget on view.
				for(Date date : allSuggestedViewTrainingDates){
					
					Widget widget =view.getCustomCalenger().getPanelOfDate(date);
					
					VerticalPanel vpanel =((VerticalPanel)widget);
					
					boolean suggestionIsExistForMorning =false;
					boolean suggestionIsExistForAfterNoo=false;
					
					if(vpanel!=null){
						vpanel.clear();
						vpanel.removeStyleName("dayIsProposedDay");
						vpanel.removeStyleName("dayCellLabel-selected");
						vpanel.removeStyleName("greenBackGround");
						//vpanel.addStyleName("yellowBackGround");
						if(trainingSuggestionMorningMap.get(date)!=null){
							vpanel.add(trainingSuggestionMorningMap.get(date));
							suggestionIsExistForMorning=true;
						}
						if(trainingSuggestionAfterNoonMap.get(date)!=null){
							vpanel.add(trainingSuggestionAfterNoonMap.get(date));
							suggestionIsExistForAfterNoo=true;
						}
					}
					if(!suggestionIsExistForMorning && suggestionIsExistForAfterNoo){
						((TrainingSuggestionViewImpl)trainingSuggestionAfterNoonMap.get(date)).setStyleName("afterNoonStyle");
					}
				}
		}
		
	}

	@Override
	public void hideSuggestionButtonClicked() {
		Log.info("Removing suggestions");
		isShowingSuggestions=false;
		view.getHideSuggestionButton().setVisible(false);
		view.getShowSuggestionButton().setVisible(true);
		
		for(Date date : allSuggestedViewTrainingDates){
			Widget widget =view.getCustomCalenger().getPanelOfDate(date);
			VerticalPanel vpanel =((VerticalPanel)widget);
			if(vpanel!=null){
				vpanel.removeStyleName("dayIsProposedDay");
				vpanel.removeStyleName("dayCellLabel-selected");
				vpanel.removeStyleName("greenBackGround");
				vpanel.clear();
			}
		}
		allSuggestedViewTrainingDates.clear();
		trainingSuggestionAfterNoonMap.clear();
		trainingSuggestionMorningMap.clear();
		init();
	}
	
	public Label setRoleNameText(String roleName)
	{
		String text;
		
		if(roleName.length() >20){
		 text= roleName.substring(0,21);
		 text = text + "...";
		}else{
			text=roleName;
		}
		Label roleNameLabel = new Label(text); 
		roleNameLabel.addStyleName("suggetionRoleLbl");
		//roleNameLabel.setTitle(roleName);
		
		return roleNameLabel;
	}

	@Override
	public void previousMonthButtonClicked(Date calenderDate) {
		Date date = new Date(calenderDate.getTime());
		CalendarUtil.setToFirstDayOfMonth(date);
		Log.info("previous month date of which data is finding is  : " + date);
		initializeViewFromGivenDateToEndOfMonth(date);
		
	}

	@Override
	public void nextMonthButtonClicked(Date calenderDate) {
		Date date = new Date(calenderDate.getTime());
		CalendarUtil.setToFirstDayOfMonth(date);
		Log.info("next month date of which data is finding is  : " + date);
		initializeViewFromGivenDateToEndOfMonth(date);
	}

	@Override
	public void toddayDateButtonClicked() {
		Log.info("Today Button Clicked");
		initializeViewFromGivenDateToEndOfMonth(new Date());
		
	}

	@Override
	public void schedultTrainingWithGivenData(String startTime, String endTime,final StandardizedRoleProxy selectedRoleProxy,boolean isBindTrainingToSuggestion) {
		Log.info("selected value is ->> starTtine : " + startTime  +" End time :" + endTime);
		
		
		try{
			final String startHour = startTime.substring(0,startTime.indexOf(":"));
			final  String startMinutes=startTime.substring(startTime.indexOf(":")+1,startTime.length()) ;
			
			final String endHour =endTime.substring(0,startTime.indexOf(":"));
			final String endMinutes=endTime.substring(startTime.indexOf(":")+1,startTime.length());
			
			if(startHour.length() < 2 || startMinutes.length() < 2 || endHour.length() < 2 || endMinutes.length() < 2 || Integer.parseInt(startHour) >= 24 || Integer.parseInt(endHour) >= 24
					|| Integer.parseInt(startMinutes) >= 60 || Integer.parseInt(endMinutes) >= 60 || Integer.parseInt(endHour) < Integer.parseInt(startHour)){
				MessageConfirmationDialogBox confirmationDialogBox =new MessageConfirmationDialogBox(constants.warning());
				confirmationDialogBox.showConfirmationDialog(constants.inValidTimeProvided());
			}else{
				
				createOrUpdateTrainingData(startHour,startMinutes,endHour,endMinutes,selectedRoleProxy,isBindTrainingToSuggestion); 
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			MessageConfirmationDialogBox confirmationDialogBox =new MessageConfirmationDialogBox(constants.warning());
			confirmationDialogBox.showConfirmationDialog(constants.inValidTimeProvided());
		}
	}

	@SuppressWarnings("deprecation")
	private void createOrUpdateTrainingData(String startHour,String startMinutes,String endHour,String endMinutes,final StandardizedRoleProxy selectedRoleProxy,final boolean isBindTrainingToSuggestion) {
		Log.info("creating or Updating Training");
		
		if(selectedRoleProxy==null){
			
			MessageConfirmationDialogBox confirmationDialogBox =new MessageConfirmationDialogBox(constants.warning());
			confirmationDialogBox.showConfirmationDialog(constants.pleaseSelectRole());
			return;
		}
		if(isUpDateButtonClicked){
			isUpDateButtonClicked=false;
			
			final Date startTimeDate = new Date(currentlySelectedDate.getTime());
			
			final Date endTimedate = new Date(currentlySelectedDate.getTime());
			
			startTimeDate.setHours(Integer.parseInt(startHour));
			startTimeDate.setMinutes(Integer.parseInt(startMinutes));
			
			endTimedate.setHours(Integer.parseInt(endHour));
			endTimedate.setMinutes(Integer.parseInt(endMinutes));
			
			Log.info("start time : " + startTimeDate);
			
			Log.info("end time : " + endTimedate);
			
			updateTraining(startTimeDate,endTimedate,selectedRoleProxy);
			
		}else{
			
			checkIsTrainingOverlapingAndCreateNewOrUpdateTraining(startHour,startMinutes,endHour,endMinutes,selectedRoleProxy,isBindTrainingToSuggestion);
		}
		
}
	@SuppressWarnings("deprecation")
	private void updateTraining(Date startTimeDate,Date endTimedate,StandardizedRoleProxy selectedRoleProxy){
		
		Log.info("Updating Training");
		showApplicationLoading(true);
		TrainingProxy trainingProxyThatIsUpdated =currentlyUpdatingTrainingView.getTrainingProxy();
		requests.trainingRequest().updateTraining(startTimeDate,endTimedate,selectedRoleProxy.getId(),semesterProxy.getId(),trainingProxyThatIsUpdated.getId()).with("standardizedRole","trainingSuggestions").fire(new OSCEReceiver<TrainingProxy>() {

			@Override
			public void onSuccess(TrainingProxy response) {
				showApplicationLoading(false);
				if(response!=null){
					//If showing suggestion end user edit any training then refreshing only view of that selected date.
					/*if(isShowingSuggestions){
							showUpdatedTrainingOnTheView();
					}else{
						//For normal scheduling refreshing view of that calender.
						//initializeViewForAllReadyScheduledTrainings();
						showUpdatedTrainingOnTheView();
					}*/
					showUpdatedTrainingOnTheView();
				}
				
			}
		});
	}
	@SuppressWarnings("deprecation")
	private void checkIsTrainingOverlapingAndCreateNewOrUpdateTraining(String startHour,String startMinutes,
			String endHour,String endMinutes,final StandardizedRoleProxy selectedRoleProxy,final boolean isBindTrainingToSuggestion){
		
		
		final Date startTimeDate = new Date(currentlySelectedDate.getTime());
		
		final Date endTimedate = new Date(currentlySelectedDate.getTime());
		
		startTimeDate.setHours(Integer.parseInt(startHour));
		startTimeDate.setMinutes(Integer.parseInt(startMinutes));
		
		endTimedate.setHours(Integer.parseInt(endHour));
		endTimedate.setMinutes(Integer.parseInt(endMinutes));
		
		Log.info("start time : " + startTimeDate);
		
		Log.info("end time : " + endTimedate);
		
		requests.trainingRequest().findIsTrainingOverLapsWithAnyTraining(startTimeDate,endTimedate,semesterProxy.getId(),selectedRoleProxy.getId()).fire(new OSCEReceiver<TrainingProxy>() {

			@Override
			public void onSuccess(TrainingProxy response) {
				if(response!=null){
					
						final MessageConfirmationDialogBox confirmationDialogBox =new MessageConfirmationDialogBox(constants.warning());
						 confirmationDialogBox.showYesNoDialog(osceMessages.trainingOverlapingMsg(response.getTimeStart().getHours()+":" +response.getTimeStart().getMinutes(),
								 response.getTimeEnd().getHours()+":" + response.getTimeEnd().getMinutes()));
						 
						 confirmationDialogBox.getYesBtn().addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								confirmationDialogBox.hide();
								
								saveNewTraining(startTimeDate,endTimedate,selectedRoleProxy,true,isBindTrainingToSuggestion);
								
							}
						});
					}else{
						
							saveNewTraining(startTimeDate,endTimedate,selectedRoleProxy,false,isBindTrainingToSuggestion);
						
					}
			}
			
		});
	}
	
	@SuppressWarnings("deprecation")
	protected void showUpdatedTrainingOnTheView() {
		showApplicationLoading(true);
		requests.trainingRequest().findTrainingsOfGivenDate(currentlySelectedDate,semesterProxy.getId()).with("trainingDate","standardizedRole").fire(new OSCEReceiver<List<TrainingProxy>>() {

			@Override
			public void onSuccess(List<TrainingProxy> response) {
				showApplicationLoading(false);
				if(response!=null){
					currentlySelectdDatePanel.clear();
					currentlySelectdDatePanel.removeStyleName("dayIsProposedDay");
					currentlySelectdDatePanel.removeStyleName("dayCellLabel-selected");
					currentlySelectdDatePanel.addStyleName("greenBackGround");

					showTrainingsFromData(response);
					
					if(isShowingSuggestions){
						checkForMoreSuggestionsAndSetSuggestionButtonVisibility();
					}
				}
			}
		});
		
	}

	@SuppressWarnings("deprecation")
	private void saveNewTraining(Date startTimeDate,Date endTimedate,StandardizedRoleProxy selectedRoleProxy,final boolean isTrainingOverlaps,boolean isBindTrainingToSuggestion){
		
		showApplicationLoading(true);
		requests.trainingRequest().createTraining(startTimeDate,endTimedate,selectedRoleProxy.getId(),semesterProxy.getId(),isBindTrainingToSuggestion).with("standardizedRole","trainingSuggestions").fire(new OSCEReceiver<TrainingProxy>() {

			@Override
			public void onSuccess(TrainingProxy response) {
				showApplicationLoading(false);		
				if(response!=null){
					
					//initializeViewForAllReadyScheduledTrainings();
					
					TrainingDateProxy td =response.getTrainingDate();
					
					if(currentlySelectdDatePanel!=null){
						boolean isThisFirstScheduling=true;
						for(int count=0; count < currentlySelectdDatePanel.getWidgetCount() ;count++){
							
							if(currentlySelectdDatePanel.getWidget(count) instanceof TrainingView){
								isThisFirstScheduling=false;
								break;
							}
						}
						if(isThisFirstScheduling){
							currentlySelectdDatePanel.clear();
							currentlySelectdDatePanel.removeStyleName("dayIsProposedDay");
							currentlySelectdDatePanel.removeStyleName("dayCellLabel-selected");
							currentlySelectdDatePanel.addStyleName("greenBackGround");
							setTrainingView(response,currentlySelectdDatePanel,isTrainingOverlaps);
							
							if(isShowingSuggestions){
								checkForMoreSuggestionsAndSetSuggestionButtonVisibility();
							}
							
						}else{
							//Removing schedule button view.
							//currentlySelectdDatePanel.remove(currentlySelectdDatePanel.getWidgetCount()-1);
							//setTrainingView(response,currentlySelectdDatePanel,isTrainingOverlaps);
							
							showUpdatedTrainingOnTheView();
						}
						
					}
						
					
				}else{
					Log.info("System could not create training");
					showErrorMessageToUser("System could not create training please try again");
				}
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	private void initializeViewForAllReadyScheduledTrainings() {
			
			showApplicationLoading(true);
			requests.trainingRequest().findAllTrainingsByTimeAsc(semesterProxy.getId()).with("trainingDate","standardizedRole").fire(new OSCEReceiver<List<TrainingProxy>>() {

				@Override
				public void onSuccess(List<TrainingProxy> response) {
					if(response!=null){
					
						//clearing old views 
						for(TrainingProxy trainingProxy : response){

							TrainingDateProxy td =trainingProxy.getTrainingDate();
							
							Widget widget = view.getCustomCalenger().getPanelOfDate(td.getTrainingDate());

							VerticalPanel vpanel =((VerticalPanel)widget);
							
							if(vpanel!=null){

								vpanel.removeStyleName("dayIsProposedDay");
								vpanel.removeStyleName("dayCellLabel-selected");
								vpanel.clear();
							}
						}
						
						showTrainingsFromData(response);
					
					}
					showApplicationLoading(false);
				}
			});
	}

	private void showTrainingsFromData(List<TrainingProxy> response){
		
		boolean isFirst=true;
		
		TrainingProxy lastTrainingProxy=null;
		 
		for(TrainingProxy trainingProxy : response){
			
			TrainingDateProxy td =trainingProxy.getTrainingDate();
			
			Widget widget = view.getCustomCalenger().getPanelOfDate(td.getTrainingDate());

			VerticalPanel vpanel =((VerticalPanel)widget);
			
			if(vpanel!=null){

				//vpanel.removeStyleName("dayIsProposedDay");
				//vpanel.removeStyleName("dayCellLabel-selected");
				
				vpanel.addStyleName("greenBackGround");
				
				if(isFirst){
				
					lastTrainingProxy=trainingProxy;
					isFirst=false;
					setTrainingView(trainingProxy,vpanel,false);
					
				}else{
					
					Date lastEndDateTime =lastTrainingProxy.getTimeEnd();
					
					Date currentlySelectedTime = trainingProxy.getTimeStart();
				
					 if(currentlySelectedTime.getTime() <= lastEndDateTime.getTime()){
						 setTrainingView(trainingProxy,vpanel,true);
					 }else{
						 setTrainingView(trainingProxy,vpanel,false);
					 }
					 
					 lastTrainingProxy=trainingProxy;
				}
				
			}
		}
	}
	private void setTrainingView(TrainingProxy trainingProxy,VerticalPanel vpanel,boolean isTrainingOverlaps){
		Log.info("setting training view ");
		TrainingView trainingView = new TrainingViewImpl();
		//trainingView.setStartTimeDate(trainingProxy.getTimeStart());
		//trainingView.setEndTimeDate(trainingProxy.getTimeEnd());
		//trainingView.setStandardizedRoleProxy(trainingProxy.getStandardizedRole());
		trainingView.setTrainingProxy(trainingProxy);
		trainingView.setValue();
		trainingView.setDelegate(planSPTrainingActivity);
		if(isTrainingOverlaps){
			((TrainingViewImpl)trainingView).addStyleName("overlapTrainingBackGround");
		}
		vpanel.add(trainingView);
	}

	@Override
	public void updateTrainingButtonClicked(UpdateTrainingViewImpl updateTrainingViewImpl) {
		Log.info("showing view to user to update training");
		
		currentlyUpdatingTrainingView=updateTrainingViewImpl;
		
		isUpDateButtonClicked=true;
		
		if(isShowingSuggestions){
			
			scheduleTrainingForSuggestionView.setStartTimeValue(updateTrainingViewImpl.getStartTimeDate());
			scheduleTrainingForSuggestionView.setEndTimeValue(updateTrainingViewImpl.getEndTimeDate());
	
			DefaultSuggestOracle<StandardizedRoleProxy> suggestOracle1 = (DefaultSuggestOracle<StandardizedRoleProxy>)scheduleTrainingForSuggestionView.getStandardizedRoleSuggestionBox().getSuggestOracle();
			
			List<StandardizedRoleProxy> allShowingRoles =suggestOracle1.getPossiblilities();
			allShowingRoles.add(updateTrainingViewImpl.getStandardizedRoleProxy());
			
			suggestOracle1.setPossiblilities(allShowingRoles);
			
			scheduleTrainingForSuggestionView.getStandardizedRoleSuggestionBox().setSuggestOracle(suggestOracle1);
			
			scheduleTrainingForSuggestionView.getStandardizedRoleSuggestionBox().setSelected(updateTrainingViewImpl.getStandardizedRoleProxy());
			
			if(updateTrainingViewImpl.getTrainingProxy().getTrainingSuggestions().size()>0){
				scheduleTrainingForSuggestionView.getStandardizedRoleSuggestionBox().setEnabled(false);
			}else{
				scheduleTrainingForSuggestionView.getStandardizedRoleSuggestionBox().setEnabled(true);
			}
			
		}else{
			scheduleTrainingView.setStartTimeValue(updateTrainingViewImpl.getStartTimeDate());
			scheduleTrainingView.setEndTimeValue(updateTrainingViewImpl.getEndTimeDate());
	
			DefaultSuggestOracle<StandardizedRoleProxy> suggestOracle1 = (DefaultSuggestOracle<StandardizedRoleProxy>)scheduleTrainingView.getStandardizedRoleSuggestionBox().getSuggestOracle();
			
			List<StandardizedRoleProxy> allShowingRoles =suggestOracle1.getPossiblilities();
			allShowingRoles.add(updateTrainingViewImpl.getStandardizedRoleProxy());
			
			suggestOracle1.setPossiblilities(allShowingRoles);
			
			scheduleTrainingView.getStandardizedRoleSuggestionBox().setSuggestOracle(suggestOracle1);
			
			scheduleTrainingView.getStandardizedRoleSuggestionBox().setSelected(updateTrainingViewImpl.getStandardizedRoleProxy());
			
			//System.out.println(" training start time : " + updateTrainingViewImpl.getStartTimeDate() + " end time :" + updateTrainingViewImpl.getEndTimeDate() + " role : " + updateTrainingViewImpl.getStandardizedRoleProxy().getShortName());
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void deleteTrainingButtonClicked(TrainingProxy trainingProxy) {
		
		Log.info("Removing training of id : " + trainingProxy.getId());
		
			showApplicationLoading(true);
			requests.trainingRequest().deleteTrainingOfGivenId(trainingProxy.getId()).fire(new OSCEReceiver<Boolean>() {

				@Override
				public void onSuccess(Boolean response) {
					
					showApplicationLoading(false);
					
					if(response!=null){
					
						if(response){
							if(isShowingSuggestions){
								((ScheduleTrainingForSuggestionViewImpl)scheduleTrainingForSuggestionView).hide();
							}else{
								((ScheduleTrainingViewImpl)scheduleTrainingView).hide();
							}
							
							if(currentlySelectdDatePanel.getWidgetCount()==2){
								
								currentlySelectdDatePanel.removeStyleName("greenBackGround");
								currentlySelectdDatePanel.clear();
								
								if(isShowingSuggestions){
									checkForMoreSuggestionsAndSetSuggestionButtonVisibility();
								}else{
									//For normal scheduling when all training are removed so finding whether selected date is training date and setting style of the selected date.
									showApplicationLoading(true);
									requests.trainingRequest().findSelectedDateISTrainingDate(currentlySelectedDate,semesterProxy.getId()).fire(new OSCEReceiver<Boolean>() {

										@Override
										public void onSuccess(Boolean response) {
											
											showApplicationLoading(false);
											
											if(response!=null){
												
												if(response){
													Log.info("Selected date is training date");
													currentlySelectdDatePanel.setStyleName("dayIsProposedDay");
													
													Label proposedTrainingDateLbl =new Label(constants.proposeTrainingDay());
													
													proposedTrainingDateLbl.setStyleName("proposedDayLabel");
													
													currentlySelectdDatePanel.add(proposedTrainingDateLbl);
												}
											}
										}
									});
								}
								
							}else{
									/*if(isShowingSuggestions){
										showUpdatedTrainingOnTheView();
									}else{
										initializeViewForAllReadyScheduledTrainings();
									}*/
									showUpdatedTrainingOnTheView();
							}
							
						}
					}else{
						
						showErrorMessageToUser("System could not delete Training");
					}
					
				}
			});
	}

	@SuppressWarnings("deprecation")
	@Override
	public void ignoreTrainingBlock(boolean isChecked) {
		
		if(isChecked){
			
			Log.info("Ignoring training block and showing all roles");
			
			showApplicationLoading(true);
			
			List<StandardizedRoleProxy> roleList = new ArrayList<StandardizedRoleProxy>();
			
			for(OscePostProxy oscePost : allOscePostOfSemList){
			
				roleList.add(oscePost.getStandardizedRole());
			}
			
			DefaultSuggestOracle<StandardizedRoleProxy> suggestOracle1 = (DefaultSuggestOracle<StandardizedRoleProxy>)scheduleTrainingView.getStandardizedRoleSuggestionBox().getSuggestOracle();
			
			suggestOracle1.setPossiblilities(roleList);
			
			scheduleTrainingView.getStandardizedRoleSuggestionBox().setSuggestOracle(suggestOracle1);
			
			showApplicationLoading(false);
		}else{
			Log.info("Showing only roles that is not assinged in training block");
			
			final List<StandardizedRoleProxy> roleList = new ArrayList<StandardizedRoleProxy>();
			
			for(OscePostProxy oscePost : allOscePostOfSemList){
			
				roleList.add(oscePost.getStandardizedRole());
			}
			
			showApplicationLoading(true);
			requests.trainingRequest().findAllRolesAssignInBlock(currentlySelectedDate,semesterProxy.getId()).fire(new OSCEReceiver<List<StandardizedRoleProxy>>() {

				@Override
				public void onSuccess(List<StandardizedRoleProxy> response) {
				
					Log.info("found roles asign in block");
				
					showApplicationLoading(false);
					
					if(response!=null){
						
						for(StandardizedRoleProxy standardizedRoleProxy : response){
						
						if(roleList !=null && roleList.size() > 0 ){
							
							if(roleList.contains(standardizedRoleProxy)){
								roleList.remove(standardizedRoleProxy);
							}
						}
					  }
						DefaultSuggestOracle<StandardizedRoleProxy> suggestOracle1 = (DefaultSuggestOracle<StandardizedRoleProxy>)scheduleTrainingView.getStandardizedRoleSuggestionBox().getSuggestOracle();
						
						suggestOracle1.setPossiblilities(roleList);
						
						scheduleTrainingView.getStandardizedRoleSuggestionBox().setSuggestOracle(suggestOracle1);					
					}
				}
				
			});
		}
		
	}

	@Override
	public void scheduleTrainingOfSuggestedRole(String startTime,String endTime, StandardizedRoleProxy standardizedRoleProxy,boolean isBindTrainingToSuggestion) {
		((ScheduleTrainingForSuggestionViewImpl)scheduleTrainingForSuggestionView).hide();
		isUpDateButtonClicked=false;
		Log.info("scheduling training of suggestede Role : " + standardizedRoleProxy.getShortName());
		schedultTrainingWithGivenData(startTime, endTime, standardizedRoleProxy,isBindTrainingToSuggestion);
		
	}

	@Override
	public void schedultTrainingWithGivenDataWhenSuggestionIsShown(String startTime, String endTime, StandardizedRoleProxy selectedRole, boolean isBindTrainingToSuggestion) {
		Log.info("scheduling training when suggestions are shown but user wnat to create training with role that is not suggested");
		schedultTrainingWithGivenData(startTime, endTime, selectedRole,isBindTrainingToSuggestion);
	}

	@Override
	public void showSuggestionsForMorningButtonClicked() {
		Log.info("showing morning suggestions");
		scheduleTrainingForSuggestionView.getSuggestedTraingsForMorningPanel().clear();
		
		if(moreSuggestionsRoleListForMorning !=null && moreSuggestionsRoleListForMorning.size() >0){
			for(TrainingSuggestionProxy tsProxy : moreSuggestionsRoleListForMorning){

				IndividualSuggestionForTrainingView individualSuggestionForTrainingView = new IndividualSuggestionForTrainingViewImpl();
				individualSuggestionForTrainingView.setDelegate(planSPTrainingActivity);
				individualSuggestionForTrainingView.setStandardizedRoleProxy(tsProxy.getStandardizedRole());
				individualSuggestionForTrainingView.setIsAfternoon(false);
				individualSuggestionForTrainingView.setValue();
				scheduleTrainingForSuggestionView.getSuggestedTraingsForMorningPanel().add(individualSuggestionForTrainingView);
		
			}
		}
		
	}

	@Override
	public void showSuggestionsForAfternoonButtonClicked() {
		Log.info("showing Afternoon suggestions");
		scheduleTrainingForSuggestionView.getSuggestedTraingsForAfternoonPanel().clear();
		if(moreSuggestionsRoleListForAfternoon !=null && moreSuggestionsRoleListForAfternoon.size() >0){
			for(TrainingSuggestionProxy tsProxy : moreSuggestionsRoleListForAfternoon){
				IndividualSuggestionForTrainingView individualSuggestionForTrainingView = new IndividualSuggestionForTrainingViewImpl();
				individualSuggestionForTrainingView.setDelegate(planSPTrainingActivity);
				individualSuggestionForTrainingView.setStandardizedRoleProxy(tsProxy.getStandardizedRole());
				individualSuggestionForTrainingView.setIsAfternoon(true);
				individualSuggestionForTrainingView.setValue();
				scheduleTrainingForSuggestionView.getSuggestedTraingsForAfternoonPanel().add(individualSuggestionForTrainingView);
			}
		}
		
	}

	@Override
	public void cancelButtonClickedOfPopup() {
		Log.info("cancel button of popup is clicked");
		currentlySelectdDatePanel.remove(currentlySelectdDatePanel.getWidgetCount()-1);
		if(currentlySelectdDatePanel.getWidgetCount()==0){
			currentlySelectdDatePanel.removeStyleName("dayCellLabel-selected");
		}
	}

	@Override
	public void cancelButtonClickedOfPopupWhenSuggestionShowing() {
		Log.info("cancel button of popup is clicked when suggestion is showing");
		isUpDateButtonClicked=false;
		currentlySelectdDatePanel.remove(currentlySelectdDatePanel.getWidgetCount()-1);
		if(currentlySelectdDatePanel.getWidgetCount()==1){
			currentlySelectdDatePanel.removeStyleName("dayCellLabel-selected");
			//Now if we have not assigned any training for selected date and clicked cancel button after just
			//opening popup so there exist label (proposed training day or proposed osce day) so setting its style.
			if(currentlySelectdDatePanel.getWidget(0) instanceof Label){
				currentlySelectdDatePanel.addStyleName("dayIsProposedDay");
			}
		}else if(currentlySelectdDatePanel.getWidgetCount()==0){
			currentlySelectdDatePanel.removeStyleName("dayCellLabel-selected");
		}
	}

	@Override
	public void showRemainingSuggestions(Date startTimeDate) {
		
		Log.info("getting result form map and showing in view");
		
		Date selectedDate = new Date(startTimeDate.getTime());
		
		selectedDate.setHours(0);
		
		selectedDate.setMinutes(0);
		
		List<TrainingSuggestionProxy> morningSuggestions= moreSuggestionForMorningMap.get(selectedDate);
		List<TrainingSuggestionProxy> afternoonSuggestions =moreSuggestionForAfterNoonMap.get(selectedDate);
		
		
		showSuggestionsOfTheDay(morningSuggestions, afternoonSuggestions, selectedDate);
		
	}
	
	private void showSuggestionsOfTheDay(List<TrainingSuggestionProxy> morningSuggestions,List<TrainingSuggestionProxy> afternoonSuggestions,Date selectedDate){
		Log.info("setting roles in view");
		
		List<StandardizedRoleProxy> morningRoleList = new ArrayList<StandardizedRoleProxy>();
		
		List<StandardizedRoleProxy> afterNoonRoleList = new ArrayList<StandardizedRoleProxy>();
		
		if(morningSuggestions.size() > 0){
			
			if(trainingSuggestionMorningMap.get(selectedDate)!=null){
				
				trainingSuggestionMorningMap.get(selectedDate).getRoleNameVerticalPanel().clear();
				
				VerticalPanel roleNamePanel =trainingSuggestionMorningMap.get(selectedDate).getRoleNameVerticalPanel();
				
			for(TrainingSuggestionProxy tsProxy : morningSuggestions){
					
					if(roleNamePanel.getWidgetCount() < OsMaConstant.MAX_ROLE_NAME_DURING_SUGGESTION){
						roleNamePanel.add(setRoleNameText(tsProxy.getStandardizedRole().getShortName()));
					
					}else if(roleNamePanel.getWidgetCount()==OsMaConstant.MAX_ROLE_NAME_DURING_SUGGESTION){
						trainingSuggestionMorningMap.get(selectedDate).getMoreButton().setVisible(true);
					}
					morningRoleList.add(tsProxy.getStandardizedRole());
				}
			}
		}else{
			trainingSuggestionMorningMap.get(selectedDate).getRoleNameVerticalPanel().clear();
		}
			if(afternoonSuggestions.size() > 0){
			
			if(trainingSuggestionAfterNoonMap.get(selectedDate)!=null){
			
				trainingSuggestionAfterNoonMap.get(selectedDate).getRoleNameVerticalPanel().clear();
				
				VerticalPanel roleNamePanel =trainingSuggestionAfterNoonMap.get(selectedDate).getRoleNameVerticalPanel();
				
				for(TrainingSuggestionProxy tsProxy : afternoonSuggestions){
					
					if(roleNamePanel.getWidgetCount() < OsMaConstant.MAX_ROLE_NAME_DURING_SUGGESTION){
						roleNamePanel.add(setRoleNameText(tsProxy.getStandardizedRole().getShortName()));
					
					}else if(roleNamePanel.getWidgetCount()==OsMaConstant.MAX_ROLE_NAME_DURING_SUGGESTION){
						trainingSuggestionAfterNoonMap.get(selectedDate).getMoreButton().setVisible(true);
					}
					afterNoonRoleList.add(tsProxy.getStandardizedRole());
				}
			}
		}else{
			trainingSuggestionAfterNoonMap.get(selectedDate).getRoleNameVerticalPanel().clear();
		}
			
			trainingSuggestionMorningMap.get(selectedDate).setMorningRoleList(morningRoleList);
			trainingSuggestionAfterNoonMap.get(selectedDate).setAfterNoonRoleList(afterNoonRoleList);
			
			if(moreSuggestionsRoleListForMorning.size()==0 && moreSuggestionsRoleListForAfternoon.size() > 0){
				((TrainingSuggestionViewImpl)trainingSuggestionAfterNoonMap.get(selectedDate)).setStyleName("afterNoonStyle");
			}
			
			Widget widget =view.getCustomCalenger().getPanelOfDate(selectedDate);
			
			VerticalPanel vpanel =((VerticalPanel)widget);
			
			if(vpanel!=null){
				vpanel.clear();
				vpanel.removeStyleName("dayIsProposedDay");
				vpanel.removeStyleName("dayCellLabel-selected");
				vpanel.removeStyleName("greenBackGround");
				
				if(trainingSuggestionMorningMap.get(selectedDate)!=null){
					vpanel.add(trainingSuggestionMorningMap.get(selectedDate));
				}
				if(trainingSuggestionAfterNoonMap.get(selectedDate)!=null){
					vpanel.add(trainingSuggestionAfterNoonMap.get(selectedDate));
				}
			}
	}
	
	public void showErrorMessageToUser(String message){
		final MessageConfirmationDialogBox confirmationDialogBox =new MessageConfirmationDialogBox(constants.warning());
		confirmationDialogBox.showConfirmationDialog(message);
	}
}