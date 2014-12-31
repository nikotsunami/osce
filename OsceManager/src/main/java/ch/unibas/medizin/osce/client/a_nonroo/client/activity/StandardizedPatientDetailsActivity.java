package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.DMZSyncService;
import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.DMZSyncServiceAsync;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.IndividualSPDataChangedNotificationView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.IndividualSPEditRequestNotificationView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.SPDetailsReviewAnamnesisSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.SPDetailsReviewAnamnesisSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.SPDetailsReviewAnamnesisTableSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.SPDetailsReviewView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.SPDetailsReviewViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientAnamnesisSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientAnamnesisTableSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientLangSkillSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientMediaSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientScarSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.util.OSCEReceiverPopupViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.UserPlaceSettings;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueRequest;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormRequest;
import ch.unibas.medizin.osce.client.managed.request.LangSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.LangSkillRequest;
import ch.unibas.medizin.osce.client.managed.request.SPPortalPersonProxy;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.request.SpAnamnesisChecksValueProxy;
import ch.unibas.medizin.osce.client.managed.request.SpAnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.SpStandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientRequest;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import ch.unibas.medizin.osce.shared.EditRequestState;
import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.LangSkillLevel;
import ch.unibas.medizin.osce.shared.MaritalStatus;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.ResourceDownloadProps;
import ch.unibas.medizin.osce.shared.StandardizedPatientStatus;
import ch.unibas.medizin.osce.shared.WorkPermission;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.shared.i18n.OsceConstantsWithLookup;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

@SuppressWarnings("deprecation")
public class StandardizedPatientDetailsActivity extends AbstractActivity implements
StandardizedPatientDetailsView.Presenter, 
StandardizedPatientDetailsView.Delegate, 
StandardizedPatientScarSubView.Delegate,
StandardizedPatientAnamnesisSubView.Delegate,
StandardizedPatientLangSkillSubView.Delegate,
StandardizedPatientMediaSubViewImpl.Delegate,
StandardizedPatientAnamnesisTableSubView.Delegate,
IndividualSPEditRequestNotificationView.Delegate,
IndividualSPDataChangedNotificationView.Delegate,SPDetailsReviewView.Delegate,SPDetailsReviewAnamnesisSubView.Delegate,SPDetailsReviewAnamnesisTableSubView.Delegate{
	
    private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private StandardizedPatientDetailsView view;

	private StandardizedPatientDetailsPlace place;
	private StandardizedPatientProxy standardizedPatientProxy;
	
	private HashMap<AnamnesisCheckTitleProxy, StandardizedPatientAnamnesisTableSubView> anamnesisSubViews = 
			new HashMap<AnamnesisCheckTitleProxy, StandardizedPatientAnamnesisTableSubView>();

	private List<AnamnesisCheckTitleProxy> anamnesisCheckTitles = new ArrayList<AnamnesisCheckTitleProxy>();
	
	private CellTable<ScarProxy> scarTable;
	
	private ValueListBox<ScarProxy> scarBox;
	
	//Issue # 122 : Replace pull down with autocomplete.
	private DefaultSuggestBox<ScarProxy, EventHandlingValueHolderItem<ScarProxy>> newScarBox;
	
	//Issue # 122 : Replace pull down with autocomplete.
	
	private StandardizedPatientScarSubView standardizedPatientScarSubView;
	private StandardizedPatientAnamnesisSubView standardizedPatientAnamnesisSubView;
	private StandardizedPatientMediaSubViewImpl standardizedPatientMediaSubViewImpl;
	
	private StandardizedPatientLangSkillSubView standardizedPatientLangSkillSubView;
	private CellTable<LangSkillProxy> langSkillTable;

	private AnamnesisFormProxy anamnesisForm ;
	private UserPlaceSettings userSettings;
	private DMZSyncServiceAsync dmxSyncService = null;
	private OsceConstantsWithLookup messageLookup = GWT.create(OsceConstantsWithLookup.class);
	private static final OsceConstants constants = GWT.create(OsceConstants.class);
	private static final String stringForAnonymize = constants.anonymisationString();
	private int intForAnomize = 1000;

	private SPPortalPersonProxy spPersonProxy;
	
	private SpStandardizedPatientProxy spStandardizedPatientProxy;
	
	private SPDetailsReviewView  spDetailsReviewView;
	
	private SPDetailsReviewAnamnesisSubViewImpl spDetailsReviewAnamnesisSubViewImpl;
	 
	private SPDetailsReviewAnamnesisTableSubView spDetailsReviewAnamnesisTableSubView;
	
	private Map<Integer,Integer> anamnesisTabSelectedIndexMap = new HashMap<Integer, Integer>();
	
	protected List<AnamnesisCheckTitleProxy> allAnamnesisCheckTitleProxyList;
	
	private Map<AnamnesisCheckTitleProxy,SPDetailsReviewAnamnesisTableSubView>  spDetailsReviewAmnesisTableViewMap = 
			new HashMap<AnamnesisCheckTitleProxy,SPDetailsReviewAnamnesisTableSubView >();
	
	private StandardizedPatientDetailsActivity standardizedPatientDetailsActivity;
	
	public StandardizedPatientDetailsActivity(StandardizedPatientDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
    	this.requests = requests;
    	this.placeController = placeController;
    	userSettings = new UserPlaceSettings(place);
    	initLoading();
    }

	public void onStop() {
	}
	
	// By spec(Start)
		private class StandardizedPatientPdfFileReceiver extends OSCEReceiver<String> {
			@Override
			public void onFailure(ServerFailure error) {
				Log.error(error.getMessage());
				// onStop();
			}

			@Override
			public void onSuccess(String response) {
				Window.open(response, "_blank", "enabled");
			}
		}

		// By spec(Stop)
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("StandardizedPatientDetailsActivity.start()");
		dmxSyncService = DMZSyncService.ServiceFactory.instance();
		StandardizedPatientDetailsView standardizedPatientDetailsView = new StandardizedPatientDetailsViewImpl();
		standardizedPatientDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = standardizedPatientDetailsView;
		standardizedPatientScarSubView = view.getStandardizedPatientScarSubViewImpl();
		standardizedPatientAnamnesisSubView = view.getStandardizedPatientAnamnesisSubViewImpl();
		standardizedPatientLangSkillSubView = view.getStandardizedPatientLangSkillSubViewImpl();
		standardizedPatientMediaSubViewImpl = view.getStandardizedPatientMediaSubViewImpl();
		
		widget.setWidget(standardizedPatientDetailsView.asWidget());
		standardizedPatientDetailsActivity=this;
		view.setDelegate(this);
		//setting delegate sp portal related change
		((StandardizedPatientDetailsViewImpl)view).getIndividualSPEditRequestSendNotificationViewImpl().setDelegate(this);
		((StandardizedPatientDetailsViewImpl)view).getIndividualSPDataChandedNotificationViewImpl().setDelegate(this);
		
		standardizedPatientScarSubView.setDelegate(this);
		standardizedPatientAnamnesisSubView.setDelegate(this);
		standardizedPatientLangSkillSubView.setDelegate(this);
		standardizedPatientMediaSubViewImpl.setDelegate(this);
		//ScrolledTab Changes start
		//loadDisplaySettings();
		//ScrolledTab Changes start
		requests.find(place.getProxyId()).with("profession", "descriptions", "nationality", "bankAccount", "bankAccount.country", "langskills", "anamnesisForm", "anamnesisForm.scars","patientInSemester").fire(new InitializeActivityReceiver());
		
		//spportal related changes start {
		
		spDetailsReviewView = new SPDetailsReviewViewImpl();
		
		spDetailsReviewView.setDelegate(this);
		
		spDetailsReviewAnamnesisSubViewImpl = ((SPDetailsReviewViewImpl)spDetailsReviewView).getSpDetailsReviewAnamnesisSubViewImpl();
		
		spDetailsReviewAnamnesisSubViewImpl.setDelegate(this);
		
		addAnamnesisCheckTitleTabSelectionHandler();
		
		addClickHandlerOfAcceptedAndDiscardButton();
		
		//spportal related changes end }
	}
	
	/**
	 * Used as a callback for the request that gets the @StandardizedPatientProxy
	 * that is edited in this activities instance.
	 */
	
	private class InitializeActivityReceiver extends OSCEReceiver<Object> {
		@Override
		public void onFailure(ServerFailure error){
			Log.error(error.getMessage());
		}
		
		@Override
		public void onSuccess(Object response) {
			if(response instanceof StandardizedPatientProxy){
				Log.info(((StandardizedPatientProxy) response).getName());
				standardizedPatientProxy = (StandardizedPatientProxy) response;
				//SP portal related change checking whether this sp has sent edit request.
				findSPsHasSentEditRequest();
				findSPsHasChandedData();
				init();
			}
		}

	}

	/**
	 * Method that is used to check whether sp has sent edit request if yes show view to admin where admin can approve or disapprove sps edit request.
	 */
	private void findSPsHasSentEditRequest() {
		Log.info("finding sp person to check whether he has sent edit Request");
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		
		requests.sPPortalPersonRequest().findSPPersonToCheckWhetherHeHasSentEditReqOrChandedData(standardizedPatientProxy.getId()).fire(new OSCEReceiver<SPPortalPersonProxy>() {

			@Override
			public void onSuccess(SPPortalPersonProxy response) {
				Log.info("Response received from findSPPersonToCheckWhetherHeHasSentEditReq()");

				if(response!=null){
					
					spPersonProxy=response;
					
					if(spPersonProxy.getEditRequestState().ordinal()==EditRequestState.REQUEST_SEND.ordinal()){
						((StandardizedPatientDetailsViewImpl)view).showEditRequestViewToAdmin(true);
					}
				}
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
			}
		});
		
	}
	/**
	 * Method that is used to check whether sp has changed data if yes show view to admin where admin can review and import data.
	 */
	private void findSPsHasChandedData() {
		
		Log.info("finding sp person  to check whether he has changed data");
		
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));

		requests.sPPortalPersonRequest().findSPPersonToCheckWhetherHeHasSentEditReqOrChandedData(standardizedPatientProxy.getId()).fire(new OSCEReceiver<SPPortalPersonProxy>() {

			@Override
			public void onSuccess(SPPortalPersonProxy response) {
				Log.info("Response received from findSPPersonToCheckWhetherHeHasSentEditReq()");

				if(response!=null){
					spPersonProxy=response;
					if(spPersonProxy.getChanged()!=null){
						if(spPersonProxy.getChanged()){
							((StandardizedPatientDetailsViewImpl)view).showDataChandedViewToAdmin(true);
						}
					}
				}
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
			}
		});
	}
	
	  ///////////////////////
	 /////	LANGSKILL  /////
	///////////////////////
	
	/**
	 * Callback for filling the language picker with all available @SpokenLanguageProxy
	 * elements.
	 */
	
	private class SpokenLanguageReceiver extends OSCEReceiver<List<SpokenLanguageProxy>> {
		@Override
		public void onSuccess(List<SpokenLanguageProxy> response) {
			Log.debug("Geholte Sprachen aus der Datenbank: " + response.size());
			standardizedPatientLangSkillSubView.setLanguagePickerValues(response);
		}
	}
	
	/**
	 * Callback for 
	 */
	
	private class LangSkillCountReceiver extends OSCEReceiver<Long> {
		@Override
		public void onSuccess(Long count) {
			if (view == null) {
				return;
			}
			
			Log.debug(count.toString() + " language skills to load");
			langSkillTable.setRowCount(count.intValue(), true);
			onRangeChangedLanguageSkillTable();
		}
		
	}
	
	
	private class LangSkillReceiver extends  Receiver<List<LangSkillProxy>> {
		
		@Override
		public void onSuccess(List<LangSkillProxy> values) {
			Range range = langSkillTable.getVisibleRange();
			if (view == null) {
				return;
			}
			langSkillTable.setRowData(range.getStart(), values);
		}
	}
	
	// Highlight onViolation

	/*private class LangSkillUpdateReceiver extends OSCEReceiver<Void> {
		@Override
		public void onSuccess(Void response) {
			Log.debug("Langskills updated successfully");
			fillLangSkills();
		}
	}*/
	// E Highlight onViolation
	
	  ///////////////////////
	 /////	ANAMNESIS  /////
	///////////////////////
	
	private class AnamnesisCheckTitleReceiver extends OSCEReceiver<List<AnamnesisCheckTitleProxy>> {
		@Override
		public void onSuccess(List<AnamnesisCheckTitleProxy> response) {
			if (response == null) {
				// TODO: display message...
				return;
			}
			
			for (final AnamnesisCheckTitleProxy title : response) {
				StandardizedPatientAnamnesisTableSubView subView = standardizedPatientAnamnesisSubView.addAnamnesisCheckTitle(title);
				subView.setDelegate(StandardizedPatientDetailsActivity.this);
				anamnesisSubViews.put(title, subView);
				anamnesisCheckTitles.add(title);

				subView.getTable().addRangeChangeHandler(new RangeChangeEvent.Handler() {
					@Override
					public void onRangeChange(RangeChangeEvent event) {
						onRangeChangedAnamnesis(title);
					}
				});
			}
			
			standardizedPatientAnamnesisSubView.allTitlesAreLoaded();
		}
	}
	
	private class AnamnesisChecksValueFillReceiver extends OSCEReceiver<Void> {
		private AnamnesisCheckTitleProxy title;

		public AnamnesisChecksValueFillReceiver(AnamnesisCheckTitleProxy title) {
			this.title = title;
		}
		
		@Override
		public void onSuccess(Void response) {
			fireAnamnesisChecksValueCountRequest(title);
		}
	}
	
	
	private class AnamnesisChecksValueCountReceiver extends OSCEReceiver<Long> {
		private AnamnesisCheckTitleProxy title;
		
		public AnamnesisChecksValueCountReceiver(AnamnesisCheckTitleProxy title) {
			this.title = title;
		}
		
		@Override
		public void onSuccess(Long count) {
			if (view == null) {
				return;
			}
			Log.debug(count.toString() + " AnamnesisChecksValues found");
			// TODO: problem, that null is not checked on view and table? Should in any case be initialized
			CellTable<AnamnesisChecksValueProxy> table = anamnesisSubViews.get(title).getTable();
			table.setRowCount(count.intValue(), true);
			fireAnamnesisChecksValueRequest(title);
		}
	}
	
	
	private class AnamnesisChecksValueReceiver extends OSCEReceiver<List<AnamnesisChecksValueProxy>> {
		private AnamnesisCheckTitleProxy title;

		public AnamnesisChecksValueReceiver(AnamnesisCheckTitleProxy title) {
			this.title = title;
		}

		@Override
		public void onSuccess(List<AnamnesisChecksValueProxy> response) {
			CellTable<AnamnesisChecksValueProxy> table = anamnesisSubViews.get(title).getTable();
			Range range = table.getVisibleRange();
			Log.debug("response.size(): " + response.size());
			table.setRowData(range.getStart(), response);
		}
	}
	
	
	private class AnamnesisChecksValueUpdateReceiver extends OSCEReceiver<Void> {
		private AnamnesisCheckTitleProxy title;

		public AnamnesisChecksValueUpdateReceiver(AnamnesisCheckTitleProxy title) {
			this.title = title;
		}

		@Override
		public void onSuccess(Void response) {
			Log.info("AnamnesisChecksValue updated");
			onRangeChangedAnamnesis(title);
		}
	}
	
	  //////////////////
	 /////	SCAR  /////
	//////////////////
	
	private class ScarBoxReceiver extends OSCEReceiver<List<ScarProxy>> {
		@Override
		public void onSuccess(List<ScarProxy> response) {
			standardizedPatientScarSubView.setScarBoxValues(response);
		}
	}
	
	
	private class ScarCountReceiver extends OSCEReceiver<Long> {
		@Override
		public void onSuccess(Long count) {
			if (view == null) {
				// This activity is dead
				return;
			}
			
			Log.debug(count.toString() + " scars loaded");
			scarTable.setRowCount(count.intValue(), true);
			
			onRangeChangedScarTable();
		}
	}
	
	
	private class ScarReceiver extends OSCEReceiver<List<ScarProxy>> {
		
		@Override
		public void onSuccess(List<ScarProxy> values) {
			Range range = scarTable.getVisibleRange();
			if (view == null) {
				// This activity is dead
				return;
			}
			scarTable.setRowData(range.getStart(), values);

			// finishPendingSelection();
			if (widget != null) {
				widget.setWidget(view.asWidget());
			}
		}
	}
	
	// Highlight onViolation	
	/*private class ScarUpdateReceiver extends OSCEReceiver<Void>{
		@Override
		public void onSuccess(Void arg0) {
			Log.debug("scar updated...");
			fillScar();
		}
	}*/
	// E Highlight onViolation

	private void init() {
		view.setValue(standardizedPatientProxy);
		anamnesisForm =  standardizedPatientProxy.getAnamnesisForm();
		if (anamnesisForm == null) 
			Log.warn("anamnesisForm is null!");
		initScar();
		initAnamnesis();
		initLangSkills();
		initMediaView();
		
		//spec
	/*
		standardizedPatientMediaSubViewImpl.id.setValue(getIdOfStandardizedPatient().toString());
		standardizedPatientMediaSubViewImpl.name.setValue(getNameOfStandardizedPatient());
		standardizedPatientMediaSubViewImpl.vid.setValue(getIdOfStandardizedPatient().toString());
		standardizedPatientMediaSubViewImpl.vname.setValue(getNameOfStandardizedPatient());
		*/
		
		standardizedPatientMediaSubViewImpl.setStandardizedPatientProxy(standardizedPatientProxy);
		//spec
	}
	
	private void initMediaView() {
		standardizedPatientMediaSubViewImpl.setMediaContent(standardizedPatientProxy.getImmagePath());
		//spec
		standardizedPatientMediaSubViewImpl.setVideoMediaContent(standardizedPatientProxy.getVideoPath());
		//spec
		
	}

	/*******************
	 * LANGSKILL TABLE
	 ******************/
	
	protected void initLangSkills() {
		this.langSkillTable = standardizedPatientLangSkillSubView.getLangSkillTable();
		langSkillTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			@Override
			public void onRangeChange(RangeChangeEvent event) {
				Log.info("onRangeChange() ==> onRangeChangedLanguageSkillTable()");
				onRangeChangedLanguageSkillTable();
			}
		});
		fillLangSkills();
	}
	
	/**
	 * Loads languages, which the standardizedPatient does not speak and fills them into ValueListBoxe of the view.
	 * Requests the number of languages spoken by the given patient and then calls onRangeChangedLanguageSkillTable() 
	 * to fill the table.
	 */
	
	protected void fillLangSkills() {
		// Fill ValueListBoxes
		requests.spokenLanguageRequest().findLanguagesByNotStandardizedPatient(standardizedPatientProxy.getId()).fire(new SpokenLanguageReceiver());
		
		// Request number of Languages spoken by patient and call onRangeChangedLanguageSkillTable() to fill table
		requests.langSkillRequest().countLangSkillsByPatientId(standardizedPatientProxy.getId()).fire(new LangSkillCountReceiver());
	}
	
	/**
	 * Fills the Language Skill table with data from the DB-Request, by
	 * firing the database request and providing a callback method.
	 */
	protected void onRangeChangedLanguageSkillTable() {
		final Range range = langSkillTable.getVisibleRange();
		fireLangSkillRangeRequest(range, new LangSkillReceiver());
	}
	
	/**
	 * Fire database request for Language Skills
	 * @param range defines which elements to fetch ("from element x on, take n elements...");
	 * @param callback Method to call after the request is executed.
	 */
	
	private void fireLangSkillRangeRequest(final Range range, final Receiver<List<LangSkillProxy>> callback) {
		requests.langSkillRequest().findLangSkillsByPatientId(standardizedPatientProxy.getId(), range.getStart(), range.getLength()).with("spokenlanguage").fire(callback);
	}
	
	/*******************
	 * ANAMNESIS TABLE
	 ******************/

	protected void initAnamnesis() {
		requests.anamnesisCheckTitleRequest().findAllAnamnesisCheckTitles().fire(new AnamnesisCheckTitleReceiver());
	}

	private void onRangeChangedAnamnesis(AnamnesisCheckTitleProxy title) {
		// TODO Implementation mit Checkboxes korrekt machen
		if (standardizedPatientAnamnesisSubView.areUnansweredQuestionsShown()) {
			// fills the AnamnesisChecksValue table in the database with
			// NULL-values for unanswered questions
			Log.info("unanswered questions are shown (fill table)");
			requests.anamnesisChecksValueRequest().fillAnamnesisChecksValues(anamnesisForm.getId()).fire(new AnamnesisChecksValueFillReceiver(title));
		} else {
			// requests the number of rows in AnamnesisChecksValue for the
			// current patient
			fireAnamnesisChecksValueCountRequest(title);
		}
	}
	
//	private void onRangeChangedAnamnesis() {
//		// TODO Implementation mit Checkboxes korrekt machen
//		if (standardizedPatientAnamnesisSubView.areUnansweredQuestionsShown()) {
//			// fills the AnamnesisChecksValue table in the database with
//			// NULL-values for unanswered questions
//			Log.info("unanswered questions are shown (fill table)");
//			requests.anamnesisChecksValueRequest().fillAnamnesisChecksValues(anamnesisForm.getId()).fire(new AnamnesisChecksValueFillReceiver());
//		} else {
//			// requests the number of rows in AnamnesisChecksValue for the
//			// current patient
//			fireAnamnesisChecksValueCountRequest();
//		}
//	}
	
	
	private void fireAnamnesisChecksValueCountRequest(AnamnesisCheckTitleProxy title) {
		String query = standardizedPatientAnamnesisSubView.getSearchString();
		Receiver<Long> receiver = new AnamnesisChecksValueCountReceiver(title);
		boolean answeredQuestions = standardizedPatientAnamnesisSubView.areAnsweredQuestionsShown();
		boolean unansweredQuestions = standardizedPatientAnamnesisSubView.areUnansweredQuestionsShown();
		AnamnesisChecksValueRequest request = requests.anamnesisChecksValueRequest(); 
		
		if (answeredQuestions && unansweredQuestions) {
			Log.debug("count -- show answered and unanswered");
			request.countAllAnamnesisChecksValuesByAnamnesisFormAndTitle(anamnesisForm.getId(), title.getId(), query).fire(receiver);
		} else if (answeredQuestions) {
			Log.debug("count -- show only answered");
			request.countAnsweredAnamnesisChecksValuesByAnamnesisFormAndTitle(anamnesisForm.getId(), title.getId(), query).fire(receiver);
		} else if (unansweredQuestions) {
			Log.debug("count -- show only unanswered");
			request.countUnansweredAnamnesisChecksValuesByAnamnesisFormAndTitle(anamnesisForm.getId(), title.getId(), query).fire(receiver);
		} else {
			Log.debug("count -- show none");
			receiver.onSuccess(new Long(0));
		}
	}

	
	protected void fireAnamnesisChecksValueRequest(AnamnesisCheckTitleProxy title) {
		String query = standardizedPatientAnamnesisSubView.getSearchString();
		boolean answered = standardizedPatientAnamnesisSubView.areAnsweredQuestionsShown();
		boolean unanswered = standardizedPatientAnamnesisSubView.areUnansweredQuestionsShown();
		
		String[] paths = anamnesisSubViews.get(title).getPaths();
				
		AnamnesisChecksValueRequest request = requests.anamnesisChecksValueRequest();
		AnamnesisChecksValueReceiver receiver = new AnamnesisChecksValueReceiver(title);
		
		Range range = anamnesisSubViews.get(title).getTable().getVisibleRange();
		Log.debug("range.getStart():" + range.getStart() + "; range.getLength():" + range.getLength() + ";");
		
		if (answered && unanswered) {
			Log.debug("request -- show answered and unanswered");
			request.findAnamnesisChecksValuesByAnamnesisFormAndTitle(anamnesisForm.getId(), title.getId(), query, range.getStart(), range.getLength())
					.with(paths).fire(receiver);
		} else if (answered) {
			Log.debug("request -- show only answered");
			request.findAnsweredAnamnesisChecksValuesByAnamnesisFormAndTitle(anamnesisForm.getId(), title.getId(), query, range.getStart(), range.getLength())
					.with(paths).fire(receiver);
		} else if (unanswered) {
			Log.debug("request -- show only unanswered");
			request.findUnansweredAnamnesisChecksValuesByAnamnesisFormAndTitle(anamnesisForm.getId(), title.getId(), query, range.getStart(), range.getLength())
					.with(paths).fire(receiver);
		} else {
			receiver.onSuccess(new ArrayList<AnamnesisChecksValueProxy>());
		}
	}
	
	/*******************
	 * SCAR TABLE
	 ******************/

	protected void initScar() {
		// and stuff that has to be done multiple times? (handler, the class
		// variable assignment vs. filling in values)
		this.scarTable = standardizedPatientScarSubView.getTable();
		this.scarBox = standardizedPatientScarSubView.getScarBox();
		//Issue # 122 : Replace pull down with autocomplete.
		this.newScarBox=standardizedPatientScarSubView.getNewScarBox();
		//Issue # 122 : Replace pull down with autocomplete.
		scarTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			@Override
			public void onRangeChange(RangeChangeEvent event) {
				Log.info("onRangeChange() ==> onRangeChangedScarTable()");
				onRangeChangedScarTable();
			}
		});
		fillScar();
	}
	
	/**
	 * Fills the ValueListBox and Table of the ScarSubView
	 */
	
	protected void fillScar() {
		// Finds all scars, that can still be added to the patient (i.e. the patient doesn't have them yet) 
		// and fills the corresponding ValueListBox
		requests.scarRequest().findScarEntriesByNotAnamnesisForm(anamnesisForm.getId()).fire(new ScarBoxReceiver());
		// Request number of scars the patient has and then fill the table by calling onRangeChangedScarTable()
		requests.scarRequest().countScarsByAnamnesisForm(anamnesisForm.getId()).fire(new ScarCountReceiver());
	}
	
	/**
	 * Fills the scar table of the ScarSubView with values from the database.
	 * Fires a database request and defines the callback-class for filling the
	 * database.
	 */
	protected void onRangeChangedScarTable() {
		final Range range = scarTable.getVisibleRange();
		fireScarRangeRequest(range, new ScarReceiver());
	}
	
	/**
	 * fires a request on the database to load the scars 
	 * @param range defines which elements to fetch ("from element x on, take n elements...");
	 * @param callback Class that handles the possible callbacks
	 */
	
	private void fireScarRangeRequest(final Range range, final Receiver<List<ScarProxy>> callback) {
		createScarRangeRequest(range).with(standardizedPatientScarSubView.getPaths()).fire(callback);
	}
	
	/**
	 * 
	 * @param range
	 * @return
	 */
	
	protected Request<List<ScarProxy>> createScarRangeRequest(Range range) {
		return requests.scarRequest().findScarEntriesByAnamnesisForm(anamnesisForm.getId(), range.getStart(), range.getLength());
	}
	
	public void storeDisplaySettings() {
		userSettings.setValue("anamnesisPanelOpen", view.isAnamnesisDisclosurePanelOpen());
		userSettings.setValue("panelOpen", view.isPatientDisclosurePanelOpen());
		userSettings.setValue("detailsTab", view.getSelectedDetailsTab());
		userSettings.setValue("anamnesisTab", standardizedPatientAnamnesisSubView.getSelectedTab());
		userSettings.flush();
	}
	
	private void loadDisplaySettings() {
		boolean anamnesisPanelOpen = true;
		boolean panelOpen = true;
		int detailsTab = 0;
		int anamnesisTab = 0;
		
		if (userSettings.hasSettings()) {
			anamnesisPanelOpen = userSettings.getBooleanValue("anamnesisPanelOpen");
			panelOpen = userSettings.getBooleanValue("panelOpen");
			detailsTab = userSettings.getIntValue("detailsTab");
			anamnesisTab = userSettings.getIntValue("anamnesisTab");
		}
		
		view.setAnamnesisDisclosurePanelOpen(anamnesisPanelOpen);
		view.setPatientDisclosurePanelOpen(panelOpen);
		view.setSelectedDetailsTab(detailsTab);
		standardizedPatientAnamnesisSubView.setSelectedAnamnesisTab(anamnesisTab);
	}
	
	@Override
	public void goTo(Place place) {
		placeController.goTo(place);		
	}

	
	@Override
	public void showApplicationLoading(Boolean show) {
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(show));

	}
	
	private void initLoading(){
		ApplicationLoadingScreenEvent.initialCounter();
		ApplicationLoadingScreenEvent.register(requests.getEventBus(),
				new ApplicationLoadingScreenHandler() {
					@Override
					public void onEventReceived(
							ApplicationLoadingScreenEvent event) {
//						Log.info("ApplicationLoadingScreenEvent onEventReceived Called");
						event.display();
					}
				});

	}

	//By SPEc [Start
	@Override
	public void printPatientClicked(){
		Log.info("Print clicked");
		String locale = LocaleInfo.getCurrentLocale().getLocaleName();
		StringBuilder requestData = new StringBuilder();
		
		String ordinal = URL.encodeQueryString(String.valueOf(ResourceDownloadProps.Entity.STANDARDIZED_PATIENT.ordinal()));
		requestData.append(ResourceDownloadProps.ENTITY).append("=").append(ordinal).append("&")
				.append(ResourceDownloadProps.ID).append("=").append(URL.encodeQueryString(standardizedPatientProxy.getId().toString())).append("&")
				.append(ResourceDownloadProps.LOCALE).append("=").append(URL.encodeQueryString(locale));

		String url = GWT.getHostPageBaseURL() + "downloadFile?" + requestData;
		Log.info("--> url is : " +url);
		Window.open(url, "", "");
	    
//		requests.standardizedPatientRequest()
//				.getPdfPatientsBySearch(standardizedPatientProxy.getId(),locale)
//				.fire(new StandardizedPatientPdfFileReceiver());

	}
	//	by SPEC ] End
	@Override
	public void editPatientClicked() {
		Log.info("edit clicked");
		goTo(new StandardizedPatientDetailsPlace(standardizedPatientProxy.stableId(),
				Operation.EDIT));
	}

	
	//Module 3 : Task B
	@Override
	public void statusClicked() {

		final StandardizedPatientStatus newStatus = (standardizedPatientProxy
				.getStatus() == StandardizedPatientStatus.ACTIVE) ? StandardizedPatientStatus.INACTIVE
				: StandardizedPatientStatus.ACTIVE;

		Log.info("Status is to : " + standardizedPatientProxy.getStatus());
		setStandardizedPatientStatus(newStatus);

	}
	
	private void setStandardizedPatientStatus(final StandardizedPatientStatus newStatus) {
		
		StandardizedPatientRequest standardizedPatientRequest = requests.standardizedPatientRequest();
		standardizedPatientProxy = standardizedPatientRequest.edit(standardizedPatientProxy);
		standardizedPatientProxy.setStatus(newStatus);
		standardizedPatientRequest.persist().using(standardizedPatientProxy).fire(new OSCEReceiver<Void>() {

					@Override
					public void onSuccess(Void arg0) {
						// init();
						Log.info("Patient is persist with " + newStatus.name());
						Log.info("Status is change to : " + newStatus.name());
						view.setStatusIcon(newStatus);
						placeController.goTo(new StandardizedPatientDetailsPlace(standardizedPatientProxy.stableId(), Operation.NEW));
					}

				});

	}
	
	public void onAnonymizeClicked() {
		StandardizedPatientRequest standardizedPatientRequest = requests
				.standardizedPatientRequest();
		standardizedPatientProxy = standardizedPatientRequest
				.edit(standardizedPatientProxy);
		
		//Fields which are not include : Nationality,Professional,Description,imagePath,videoPath,birthday,email,Bank.Nationality
		standardizedPatientProxy.setName(constants.anonymisationString());
		standardizedPatientProxy.setPreName(constants.anonymisationString());
		standardizedPatientProxy.setStreet(constants.anonymisationString());
		standardizedPatientProxy.setCity(constants.anonymisationString());
		standardizedPatientProxy.setPostalCode(constants.anonymisationString());
		standardizedPatientProxy.setTelephone(constants.anonymisationString());
		standardizedPatientProxy.setEmail(constants.anonymisationString() + "@" + constants.anonymisationString() + ".xxx");
		
		standardizedPatientProxy.setTelephone2(constants.anonymisationString());
		standardizedPatientProxy.setMobile(constants.anonymisationString());
		
		standardizedPatientProxy.setGender(Gender.values()[(new Random()).nextInt(Gender.values().length)]);
		standardizedPatientProxy.setWorkPermission(WorkPermission.values()[(new Random()).nextInt(WorkPermission.values().length)]);
		standardizedPatientProxy.setMaritalStatus(MaritalStatus.values()[(new Random()).nextInt(MaritalStatus.values().length)]);
		standardizedPatientProxy.setBirthday(new Date());
		
		standardizedPatientProxy.setHeight(150 + (new Random()).nextInt(51));
		standardizedPatientProxy.setWeight(50 + (new Random()).nextInt(51));
		
//		standardizedPatientProxy.setNationality(null);
//		standardizedPatientProxy.setProfession(null);
//		standardizedPatientProxy.setLangskills(null);
		
		standardizedPatientProxy.getDescriptions().setDescription(constants.anonymisationString());
		
		standardizedPatientProxy.getBankAccount().setBankName(constants.anonymisationString());
		standardizedPatientProxy.getBankAccount().setIBAN(constants.anonymisationString());
		standardizedPatientProxy.getBankAccount().setBIC(constants.anonymisationString());
		standardizedPatientProxy.getBankAccount().setOwnerName(constants.anonymisationString()) ;
		standardizedPatientProxy.getBankAccount().setPostalCode(constants.anonymisationString());
		standardizedPatientProxy.getBankAccount().setCity(constants.anonymisationString());
		
		standardizedPatientProxy.setSocialInsuranceNo(constants.anonymisationString());
		standardizedPatientProxy.setStatus(StandardizedPatientStatus.ANONYMIZED);
		
		standardizedPatientRequest.persist()
				.using(standardizedPatientProxy)
				.fire(new OSCEReceiver<Void>() {

					@Override
					public void onSuccess(Void arg0) {
						// init();
//						view.setStatusIcon(StandardizedPatientStatus.ANONYMIZED);
						placeController.goTo(new StandardizedPatientDetailsPlace(standardizedPatientProxy.stableId(), Operation.NEW));
					}
				});
	}
	
	@Override
	public void deletePatientClicked() {
		final MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.warning());

		// if patient is in a role; anonymize data; else delete SP
		
		if (standardizedPatientProxy.getPatientInSemester() != null
				&& standardizedPatientProxy.getPatientInSemester().size() > 0) {
			dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent e) {
					dialogBox.hide();
					onAnonymizeClicked();
				}
			});
			
			dialogBox.showYesNoDialog(constants.warningAnonymizeSp());
		} else {
			dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent e) {
					dialogBox.hide();
					requests.standardizedPatientRequest().remove().using(standardizedPatientProxy).fire(new OSCEReceiver<Void>() {
			            public void onSuccess(Void ignore) {
			                if (widget == null) {
			                    return;
			                }
			            	placeController.goTo(new StandardizedPatientPlace("StandardizedPatientPlace!DELETED"));
			            }
			        });
				}
			});
			
			dialogBox.showYesNoDialog(constants.warningDeleteSp());
		}
		
	}
	@Override
	public void sendClicked(){
		
		//Following code is commented as now data is push from osce to spportal mail is send to sp and req flag should be cleared. by spec-india.
		
		/*String locale = LocaleInfo.getCurrentLocale().getLocaleName();
		if(standardizedPatientProxy!=null){
			if(standardizedPatientProxy.getEmail()!=null && !standardizedPatientProxy.getEmail().equals("")){
					dmxSyncService.pushToDMZ(standardizedPatientProxy.getId(),locale,new AsyncCallback<List<String>>(){

						@Override
						public void onFailure(Throwable caught) {
							try {
					          throw caught;
					        } catch (DMZSyncException e) {
					        	Window.alert(messageLookup.serverReturndError()+ messageLookup.getString(e.getType())+e.getMessage());
					        } catch (Throwable e) {
					        	Window.alert(messageLookup.serverReturndError()+ e.getMessage());
						    }
						}

						@Override
						public void onSuccess(List<String> result) {
						
							if(result.size()<=0){
								setStandardizedPatientStatus(StandardizedPatientStatus.EXPORTED);
								Window.alert(messageLookup.exportSuccessful());
								return;
							}
							
							SimpleShowErrorDialogBox dialogBox = null;
							if(dialogBox == null){
								String dialogTitle = constants.error();
								String dialogCloseButton = constants.close();
								dialogBox = new SimpleShowErrorDialogBox(dialogTitle,result,dialogCloseButton);
							}
							dialogBox.show();
						}
						
					});	
				
			}else{
				Window.alert("the email is null");
			}
			
			
		}*/
		
		//pushing sp data to spportal sending email and resetting flags.
		approveSpsEditRequest();
	}
	
	@Override
	public void pullClicked(){

		//Following code is commented as now data is pull in osce from spportal and data is deleted from sp portal. by spec-india.
		
		/*dmxSyncService.pullFromDMZ(standardizedPatientProxy.getId(), new AsyncCallback<Void>(){

			@Override
			public void onFailure(Throwable caught) {
			   try {
		          throw caught;
		        } catch (DMZSyncException e) {
		        	Window.alert(messageLookup.serverReturndError()+messageLookup.getString(e.getType())+e.getMessage());
		        } catch (Throwable e) {
		        	Window.alert(messageLookup.serverReturndError()+e.getMessage());
		        }
				
			}

			@Override
			public void onSuccess(Void result) {
				setStandardizedPatientStatus(StandardizedPatientStatus.ACTIVE);
				Window.alert(messageLookup.importSussessful());
			}
			
		});*/
		
		final MessageConfirmationDialogBox confirmationDialogBox =new MessageConfirmationDialogBox(constants.warning());
		confirmationDialogBox.showYesNoDialog(constants.spDataPullNotificationMsg());
		confirmationDialogBox.getYesBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {

				confirmationDialogBox.hide();
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
				
				requests.sPPortalPersonRequest().findSpPortalSPBasedOnOsceSPID(standardizedPatientProxy.getId()).fire(new OSCEReceiver<SpStandardizedPatientProxy>() {

					@Override
					public void onSuccess(SpStandardizedPatientProxy response) {
						spStandardizedPatientProxy=response;
						
						requests.spStandardizedPatientRequest().moveChangedDetailsOfSPFormSPPortal(standardizedPatientProxy.getId(),spStandardizedPatientProxy.getId()).fire(new OSCEReceiver<Boolean>() {

							@Override
							public void onSuccess(Boolean response) {
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								if(response){
									((StandardizedPatientDetailsViewImpl)view).showDataChandedViewToAdmin(false);

									((StandardizedPatientDetailsViewImpl)view).getPull().setEnabled(false);
								}else{
									
									final OSCEReceiverPopupViewImpl errorMsgDisplayView =new OSCEReceiverPopupViewImpl();
									
									errorMsgDisplayView.showMessage(constants.dataSaveFailure());
									
									errorMsgDisplayView.getBtnOk().addClickHandler(new ClickHandler() {
										
										@Override
										public void onClick(ClickEvent event) {
											errorMsgDisplayView.hide();
										}
									});
								}
								
							}
						}); 
					}
				});
				
			}
		});
	}

	@Override
	public void deleteLangSkillClicked(LangSkillProxy langSkill) {
		// Highlight onViolation
		//requests.langSkillRequest().remove().using(langSkill).fire(new LangSkillUpdateReceiver());
		requests.langSkillRequest().remove().using(langSkill).fire(new OSCEReceiver<Void>() {
			@Override
			public void onSuccess(Void response) {
				Log.debug("Langskills updated successfully");
				fillLangSkills();	
			};
		});
		
		// E Highlight onViolation
	}
	
	@Override
	public void deleteScarClicked(ScarProxy scar) {
		AnamnesisFormRequest anamReq = requests.anamnesisFormRequest();
		anamnesisForm =  anamReq.edit(anamnesisForm);
		
		Log.debug("Remove scar (" + scar.getId() + ") from anamnesis-form (" + anamnesisForm.getId() + ")");
		
		Iterator<ScarProxy> i = anamnesisForm.getScars().iterator();
		while (i.hasNext()) {
			ScarProxy scarProxy = (ScarProxy) i.next();
			if (scarProxy.getId() == scar.getId() ) {
				anamnesisForm.getScars().remove(scarProxy);
				break;
			}
			// Highlight onViolation
		//}anamReq.persist().using(anamnesisForm).fire(new ScarUpdateReceiver());
		}anamReq.persist().using(anamnesisForm).fire(new OSCEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				Log.debug("scar updated...");
				fillScar();
			}
		});
		// E Highlight onViolation
	}

	@Override
	public void addScarClicked() {
		AnamnesisFormRequest anamReq = requests.anamnesisFormRequest();
		
		//Issue # 122 : Replace pull down with autocomplete.
		//ScarProxy scar = scarBox.getValue();
		ScarProxy scar = newScarBox.getSelected();
		//Issue # 122 : Replace pull down with autocomplete.
		if(scar==null || anamnesisForm==null)
		{
			Log.info("scar and anamnesisForm Null");
			return;
			
		}
		Log.debug("Add scar (" + scar.getBodypart() + " - id " + scar.getId() + ") to anamnesis-form (" + anamnesisForm.getId() + ")");
		
		anamnesisForm = anamReq.edit(anamnesisForm);
		
		anamnesisForm.getScars().add(scar);
		// Highlight onViolation		
		anamReq.persist().using(anamnesisForm).fire(new OSCEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				Log.debug("scar updated...");
				fillScar();				
			}
		});
		// E Highlight onViolation
	}
	
	@Override
	public void addLangSkillClicked(SpokenLanguageProxy spokenLanguageProxy, LangSkillLevel langSkillLevel) {
		// get requestContext and initialize new langSkillProxy
		LangSkillRequest langSkillRequest = requests.langSkillRequest();
		LangSkillProxy langSkillProxy = langSkillRequest.create(LangSkillProxy.class);
		langSkillProxy.setSkill(langSkillLevel);
		langSkillProxy.setSpokenlanguage(spokenLanguageProxy);
		langSkillProxy.setStandardizedpatient(standardizedPatientProxy);
		
		Log.debug("add skill " + langSkillLevel.toString() + " in language " + spokenLanguageProxy.getLanguageName());
		
		// write new langSkill to database and re-initialize table
		// Highlight onViolation
		Log.info("Map Size: " + standardizedPatientLangSkillSubView.getLanguageSkillMap().size());
		//langSkillRequest.persist().using(langSkillProxy).fire(new LangSkillUpdateReceiver());
		
		langSkillRequest.persist().using(langSkillProxy).fire(new OSCEReceiver<Void>(standardizedPatientLangSkillSubView.getLanguageSkillMap()) {

			@Override
			public void onSuccess(Void response) {				
				Log.debug("Langskills updated successfully");
				fillLangSkills();
			}
		});
		
		// E Highlight onViolation
	}

	@Override
	public void saveAnamnesisChecksValueProxyChanges(AnamnesisChecksValueProxy proxy, String anamnesisChecksValue, Boolean truth) {
		AnamnesisChecksValueRequest request = requests.anamnesisChecksValueRequest();
		proxy = request.edit(proxy);
		proxy.setTruth(truth);
		proxy.setAnamnesisChecksValue(anamnesisChecksValue);
		AnamnesisCheckTitleProxy title = anamnesisCheckTitles.get(standardizedPatientAnamnesisSubView.getSelectedTab());
		request.persist().using(proxy).fire(new AnamnesisChecksValueUpdateReceiver(title));
	}
	
	@Override
	public void saveAnamnesisChecksValueProxyChanges(AnamnesisChecksValueProxy proxy, String comment) {
		AnamnesisChecksValueRequest request = requests.anamnesisChecksValueRequest();
		proxy = request.edit(proxy);
		proxy.setComment(comment);
		AnamnesisCheckTitleProxy title = anamnesisCheckTitles.get(standardizedPatientAnamnesisSubView.getSelectedTab());
		request.persist().using(proxy).fire(new AnamnesisChecksValueUpdateReceiver(title));
	}
	
	@Override
	public void performAnamnesisSearch() {
		Log.debug("performAnamnesisSearch()");
		onRangeChangedAnamnesis(anamnesisCheckTitles.get(standardizedPatientAnamnesisSubView.getSelectedTab()));
	}

	@Override
	public void newClicked() {
		// TODO: ???
	}

	@Override
	public void uploadClicked() {
		// TODO: ???
	}

	@Override
	public void uploadSuccesfull(String results) {
	 StandardizedPatientRequest stdPatRequest = requests.standardizedPatientRequest();
	 standardizedPatientProxy = stdPatRequest.edit(standardizedPatientProxy);
	 standardizedPatientProxy.setImmagePath(results);
	 stdPatRequest.persist().using(standardizedPatientProxy).fire(new OSCEReceiver<Void>() {

		@Override
		public void onSuccess(Void response) {
			Log.info("uploadSuccesfull");
			
		}
	});
	}
	
	//spec start
	@Override
	public void videoUploadSuccesfull(String results) {
	 StandardizedPatientRequest stdPatRequest = requests.standardizedPatientRequest();
	 
	 standardizedPatientProxy = stdPatRequest.edit(standardizedPatientProxy);
	
	
	 standardizedPatientProxy.setVideoPath(results);
	 //spec end
	 
	 
	 
	 stdPatRequest.persist().using(standardizedPatientProxy).fire(new OSCEReceiver<Void>() {

		@Override
		public void onSuccess(Void response) {
			Log.info("videoUploadSuccesfull");
			
		}
	});
	}
	//spec end
	
	@Override
	public String getNameOfStandardizedPatient() {
		return standardizedPatientProxy.getName();
	}
	public Long getIdOfStandardizedPatient() {
		return standardizedPatientProxy.getId();
	}

	/**
	 * This method is used to send email to sp as his edit request is denies and to clear edit request flag. 
	 */
	@Override
	public void denySPsEditRequst() {
		
		Log.info("deny edit request of sp by sending mail and clearing flag");

		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));

		requests.sPPortalPersonRequest().denyEditRequestOfSP(spPersonProxy.getId()).fire(new OSCEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				((StandardizedPatientDetailsViewImpl)view).showEditRequestViewToAdmin(false);
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
				((StandardizedPatientDetailsViewImpl)view).getSend().setEnabled(false);
			}
		});
	}


	/**
	 * This method is used to send email to sp as his edit request is approved and to push his data to spportal and clear edit request flag. 
	 */
	@Override
	public void approveSpsEditRequest() {
		
		Log.info("Approve edit request of sp");
		
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));

			requests.sPPortalPersonRequest().approveEditRequestOfSP(standardizedPatientProxy.getId(),spPersonProxy.getId()).fire(new OSCEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				((StandardizedPatientDetailsViewImpl)view).showEditRequestViewToAdmin(false);
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
				((StandardizedPatientDetailsViewImpl)view).getSend().setEnabled(false);
			}
		});
	}

	private void addAnamnesisCheckTitleTabSelectionHandler() {

		spDetailsReviewAnamnesisSubViewImpl.anamnesisTabs.addSelectionHandler(new SelectionHandler<Integer>() {

				@Override
				public void onSelection(SelectionEvent<Integer> event) {
					Log.info("Selected tab index is : " + event.getSelectedItem());
					
					anamnesisTitleTabSelected(event.getSelectedItem());
				}
			});
}
	@SuppressWarnings("deprecation")
	public void anamnesisTitleTabSelected(final Integer selectedIndex) {
		
		Log.info("finding data if this index is selected first time and initializing view of it");
		
		if(anamnesisTabSelectedIndexMap.get(selectedIndex)==null){
			
			anamnesisTabSelectedIndexMap.put(selectedIndex, selectedIndex);
			
			AnamnesisFormProxy anamnesisFormProxy = standardizedPatientProxy.getAnamnesisForm();
			
			final AnamnesisCheckTitleProxy anamnesisCheckTitleProxy = allAnamnesisCheckTitleProxyList.get(selectedIndex);
			
			final SpAnamnesisFormProxy spAnamnesisFormProxy = spStandardizedPatientProxy.getAnamnesisForm();
			
			requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
			
			requests.anamnesisChecksValueRequest().findAnamnesisChecksValuesByAnamnesisFormAndCheckTitle(anamnesisFormProxy.getId(),anamnesisCheckTitleProxy.getId()).with("anamnesischeck").fire(new OSCEReceiver<List<AnamnesisChecksValueProxy>>() {

				@Override
				public void onSuccess(final List<AnamnesisChecksValueProxy> response1) {

					Log.info("Total osce standardized patient anamnesis check value proxy is : " + response1.size());
					
					Log.info("Now finding all anamnesis check value proxy is of sp portal");
					
					String anmnesisCheckText = getSPAnamnesisCheckText(response1);
					
					requests.spStandardizedPatientRequest().findAnamnesisChecksValuesByAnamnesisFormAndCheckTitleText(spAnamnesisFormProxy.getId(),anmnesisCheckText).with("anamnesischeck").fire(new OSCEReceiver<List<SpAnamnesisChecksValueProxy>>() {

						@Override
						public void onSuccess(List<SpAnamnesisChecksValueProxy> response2) {
							
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							
							Log.info("Total sp portal standardized patient anamnesis check value proxy is : " + response2.size());
							
							SPDetailsReviewAnamnesisTableSubView tableView = spDetailsReviewAmnesisTableViewMap.get(anamnesisCheckTitleProxy);
						
							if(response1.size() != response2.size()){
								//No proper data found show hiding review view and showing message to user.
								
								Log.info("No proper data found for anamnesis check value form sp portal so hiding view");
								
								((SPDetailsReviewViewImpl)spDetailsReviewView).setViewVisible(false);
								
								final OSCEReceiverPopupViewImpl errorMsgDisplayView =new OSCEReceiverPopupViewImpl();
								
								errorMsgDisplayView.showMessage(constants.osceSPPortalDataMismatch());
								
								errorMsgDisplayView.getBtnOk().addClickHandler(new ClickHandler() {
									
									@Override
									public void onClick(ClickEvent event) {
										errorMsgDisplayView.hide();
									}
								});
							}
							else{
								
								boolean isDatachanged=tableView.setValue(response1,response2);
								if(isDatachanged){
									if(selectedIndex==(spDetailsReviewAnamnesisSubViewImpl.getAnamnesisTabs().getWidgetCount()-1)){
										spDetailsReviewAnamnesisSubViewImpl.getAnamnesisTabs().getTabWidget(selectedIndex).addStyleName("lastTabChangedTabSty");
									}else{
										spDetailsReviewAnamnesisSubViewImpl.getAnamnesisTabs().getTabWidget(selectedIndex).addStyleName("chnagedTabStyle");
									}
								}else{
									if(selectedIndex==(spDetailsReviewAnamnesisSubViewImpl.getAnamnesisTabs().getWidgetCount()-1)){
										spDetailsReviewAnamnesisSubViewImpl.getAnamnesisTabs().getTabWidget(selectedIndex).removeStyleName("lastTabChangedTabSty");	
									}else{
										spDetailsReviewAnamnesisSubViewImpl.getAnamnesisTabs().getTabWidget(selectedIndex).removeStyleName("chnagedTabStyle");	
									}
								}
							}
						}
					});
				}
			});
		}
	}
	
	private void addClickHandlerOfAcceptedAndDiscardButton(){

		((SPDetailsReviewViewImpl)spDetailsReviewView).getAcceptChangesButton().addClickHandler(new ClickHandler() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(ClickEvent event) {
				Log.info("Accepted changes button clicked");
		
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
				
				requests.spStandardizedPatientRequest().moveChangedDetailsOfSPFormSPPortal(standardizedPatientProxy.getId(), spStandardizedPatientProxy.getId()).fire(new OSCEReceiver<Boolean>() {

					@Override
					public void onSuccess(Boolean response) {
						
						Log.info("Data moved successfully is" + response);
						
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));

						
						((SPDetailsReviewViewImpl)spDetailsReviewView).setViewVisible(false);
						
						((StandardizedPatientDetailsViewImpl)view).getPull().setEnabled(false);
						
						((StandardizedPatientDetailsViewImpl)view).showDataChandedViewToAdmin(false);
						
						if(!response){
						
							((SPDetailsReviewViewImpl)spDetailsReviewView).setViewVisible(false);
							
							final OSCEReceiverPopupViewImpl errorMsgDisplayView =new OSCEReceiverPopupViewImpl();
							
							errorMsgDisplayView.showMessage(constants.dataSaveFailure());
							
							errorMsgDisplayView.getBtnOk().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									errorMsgDisplayView.hide();
								}
							});
						}else{
							requests.standardizedPatientRequest().findStandardizedPatient(standardizedPatientProxy.getId()).with("profession", "descriptions", "nationality", "bankAccount", "bankAccount.country", "langskills", "anamnesisForm", "anamnesisForm.scars","patientInSemester").fire(new OSCEReceiver<StandardizedPatientProxy>(){

								@Override
								public void onSuccess(StandardizedPatientProxy response) {
									Log.info("Get refreshed sp ");
									standardizedPatientProxy=response;
									view.setValue(standardizedPatientProxy);
									
								}
								
							});
						}
					}
				});
				
				
			}
		});
		
		((SPDetailsReviewViewImpl)spDetailsReviewView).getDiscardChangesButton().addClickHandler(new ClickHandler() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(ClickEvent event) {
				Log.info("Discard changes button clicked");
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
				
				requests.spStandardizedPatientRequest().removeSPDetailsFromSPPortal(standardizedPatientProxy.getId(),spStandardizedPatientProxy.getId(),true).fire(new OSCEReceiver<Boolean>() {

					@Override
					public void onSuccess(Boolean response) {
					
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
						
						((SPDetailsReviewViewImpl)spDetailsReviewView).setViewVisible(false);
						
						((StandardizedPatientDetailsViewImpl)view).getPull().setEnabled(false);
						
						((StandardizedPatientDetailsViewImpl)view).showDataChandedViewToAdmin(false);
						
						if(!response){
							
							((SPDetailsReviewViewImpl)spDetailsReviewView).setViewVisible(false);
							
							final OSCEReceiverPopupViewImpl errorMsgDisplayView =new OSCEReceiverPopupViewImpl();
							
							errorMsgDisplayView.showMessage(constants.dataSaveFailure());
							
							errorMsgDisplayView.getBtnOk().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									errorMsgDisplayView.hide();
								}
							});
						}
						
					}
				});
				
				
			}
		});
	}
	@Override
	public void reviewButtonClicked() {
		
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		
		requests.sPPortalPersonRequest().findSpPortalSPBasedOnOsceSPID(standardizedPatientProxy.getId()).with("person","anamnesisForm","nationality","profession","bankAccount","bankAccount.country").fire(new OSCEReceiver<SpStandardizedPatientProxy>() {

			@Override
			public void onSuccess(SpStandardizedPatientProxy response) {
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
				
				spStandardizedPatientProxy=response;
		
				showOldAndNewDataOfSPToAdmin();
				
				findAllAnamnesisCheckTitle();
				
				((SPDetailsReviewViewImpl)spDetailsReviewView).setViewVisible(true);
			}
		});
		
	}
	
	private void showOldAndNewDataOfSPToAdmin() {
		Log.info("Showing old and new details of sp to admin");
		spDetailsReviewView.setValue(standardizedPatientProxy, spStandardizedPatientProxy);
	}

	@SuppressWarnings("deprecation")
	private void findAllAnamnesisCheckTitle() {
		
	requests.sPPortalPersonRequest().findAllAnamnesisThatIsSendToDMZ().fire(new OSCEReceiver<List<AnamnesisCheckTitleProxy>>() {

		@Override
		public void onSuccess(List<AnamnesisCheckTitleProxy> response) {

			if (response == null) {
				return;
			}
			Log.info("Total anamnesis check is : " + response.size());
			
			allAnamnesisCheckTitleProxyList =response;
			
			for (final AnamnesisCheckTitleProxy title : response) {
				SPDetailsReviewAnamnesisTableSubView subView = spDetailsReviewAnamnesisSubViewImpl.addAnamnesisCheckTitle(title);
				subView.setDelegate(standardizedPatientDetailsActivity);
				spDetailsReviewAmnesisTableViewMap.put(title, subView);
			}
		}
	});
}
	protected String getSPAnamnesisCheckText(List<AnamnesisChecksValueProxy> anamnesisCheckValueProxyList) {
		Log.info("Taking anamnisis check text from anmnesisValue proxy");
		
		if (anamnesisCheckValueProxyList == null|| anamnesisCheckValueProxyList.size() == 0) {
			Log.info("Return as null");
			return "";
		}
		Iterator<AnamnesisChecksValueProxy> anamnesisCheckValueIterator = anamnesisCheckValueProxyList.iterator();
		StringBuilder anamnesisCheckText = new StringBuilder();
		anamnesisCheckText.append("'',");
		while (anamnesisCheckValueIterator.hasNext()) {
			
			AnamnesisChecksValueProxy anamnesisCheckValueProxy = anamnesisCheckValueIterator.next();
			//System.out.println("Ana check value is " + anamnesisCheckValueProxy.getId() + " check text is" + anamnesisCheckValueProxy.getAnamnesischeck().getText());
			anamnesisCheckText.append("'"+anamnesisCheckValueProxy.getAnamnesischeck().getText()+"'");
			if (anamnesisCheckValueIterator.hasNext()) {
				anamnesisCheckText.append(" ,");
			}
		}
		
		return anamnesisCheckText.toString();
	}
	
}