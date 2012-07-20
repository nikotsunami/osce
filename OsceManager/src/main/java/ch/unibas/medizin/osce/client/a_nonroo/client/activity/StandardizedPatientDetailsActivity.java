package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.DMZSyncException;
import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.DMZSyncService;
import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.DMZSyncServiceAsync;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientAnamnesisSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientAnamnesisTableSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientMediaSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientScarSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientLangSkillSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.UserPlaceSettings;
import ch.unibas.medizin.osce.client.i18n.OsceConstantsWithLookup;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueRequest;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormRequest;
import ch.unibas.medizin.osce.client.managed.request.LangSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.LangSkillRequest;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientRequest;
import ch.unibas.medizin.osce.shared.LangSkillLevel;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.StandardizedPatientStatus;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.shared.scaffold.AnamnesisChecksValueRequestNonRoo;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;

@SuppressWarnings("deprecation")
public class StandardizedPatientDetailsActivity extends AbstractActivity implements
StandardizedPatientDetailsView.Presenter, 
StandardizedPatientDetailsView.Delegate, 
StandardizedPatientScarSubView.Delegate,
StandardizedPatientAnamnesisSubView.Delegate,
StandardizedPatientLangSkillSubView.Delegate,
StandardizedPatientMediaSubViewImpl.Delegate,
StandardizedPatientAnamnesisTableSubView.Delegate {
	
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

	public StandardizedPatientDetailsActivity(StandardizedPatientDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
    	this.requests = requests;
    	this.placeController = placeController;
    	userSettings = new UserPlaceSettings(place);
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
		
		view.setDelegate(this);
		standardizedPatientScarSubView.setDelegate(this);
		standardizedPatientAnamnesisSubView.setDelegate(this);
		standardizedPatientLangSkillSubView.setDelegate(this);
		standardizedPatientMediaSubViewImpl.setDelegate(this);
		loadDisplaySettings();
		requests.find(place.getProxyId()).with("profession", "descriptions", "nationality", "bankAccount", "bankAccount.country", "langskills", "anamnesisForm", "anamnesisForm.scars","patientInSemester").fire(new InitializeActivityReceiver());
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
				init();
			}
		}
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
	
	
	private class LangSkillUpdateReceiver extends OSCEReceiver<Void> {
		@Override
		public void onSuccess(Void response) {
			Log.debug("Langskills updated successfully");
			fillLangSkills();
		}
	}
	
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
	
	
	private class ScarUpdateReceiver extends OSCEReceiver<Void>{
		@Override
		public void onSuccess(Void arg0) {
			Log.debug("scar updated...");
			fillScar();
		}
	}

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
		standardizedPatientMediaSubViewImpl.id.setValue(getIdOfStandardizedPatient().toString());
		standardizedPatientMediaSubViewImpl.name.setValue(getNameOfStandardizedPatient());
		standardizedPatientMediaSubViewImpl.vid.setValue(getIdOfStandardizedPatient().toString());
		standardizedPatientMediaSubViewImpl.vname.setValue(getNameOfStandardizedPatient());
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
		requests.languageRequestNonRoo().findLanguagesByNotStandardizedPatient(standardizedPatientProxy.getId()).fire(new SpokenLanguageReceiver());
		
		// Request number of Languages spoken by patient and call onRangeChangedLanguageSkillTable() to fill table
		requests.langSkillRequestNonRoo().countLangSkillsByPatientId(standardizedPatientProxy.getId()).fire(new LangSkillCountReceiver());
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
		requests.langSkillRequestNonRoo().findLangSkillsByPatientId(standardizedPatientProxy.getId(), range.getStart(), range.getLength()).with("spokenlanguage").fire(callback);
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
			requests.anamnesisChecksValueRequestNonRoo().fillAnamnesisChecksValues(anamnesisForm.getId()).fire(new AnamnesisChecksValueFillReceiver(title));
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
//			requests.anamnesisChecksValueRequestNonRoo().fillAnamnesisChecksValues(anamnesisForm.getId()).fire(new AnamnesisChecksValueFillReceiver());
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
		AnamnesisChecksValueRequestNonRoo request = requests.anamnesisChecksValueRequestNonRoo(); 
		
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
				
		AnamnesisChecksValueRequestNonRoo request = requests.anamnesisChecksValueRequestNonRoo();
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
		requests.scarRequestNonRoo().findScarEntriesByNotAnamnesisForm(anamnesisForm.getId()).fire(new ScarBoxReceiver());
		// Request number of scars the patient has and then fill the table by calling onRangeChangedScarTable()
		requests.scarRequestNonRoo().countScarsByAnamnesisForm(anamnesisForm.getId()).fire(new ScarCountReceiver());
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
		return requests.scarRequestNonRoo().findScarEntriesByAnamnesisForm(anamnesisForm.getId(), range.getStart(), range.getLength());
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

	
	//By SPEc [Start
	@Override
	public void printPatientClicked(){
		Log.info("Print clicked");
		requests.standardizedPatientRequestNonRoo()
				.getPdfPatientsBySearch(standardizedPatientProxy.getId())
				.fire(new StandardizedPatientPdfFileReceiver());

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
		StandardizedPatientRequest standardizedPatientRequest = requests
				.standardizedPatientRequest();
		standardizedPatientProxy = standardizedPatientRequest
				.edit(standardizedPatientProxy);
		standardizedPatientProxy.setStatus(newStatus);
		standardizedPatientRequest.persist().using(standardizedPatientProxy)
				.fire(new OSCEReceiver<Void>() {

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
	
	@Override
	public void deletePatientClicked() {
		// TODO replace with appropriate message

		if (standardizedPatientProxy.getPatientInSemester() != null
				&& standardizedPatientProxy.getPatientInSemester().size() > 0) {

			StandardizedPatientRequest standardizedPatientRequest = requests
					.standardizedPatientRequest();
			standardizedPatientProxy = standardizedPatientRequest
					.edit(standardizedPatientProxy);
			standardizedPatientProxy
					.setStatus(StandardizedPatientStatus.ANONYMIZED);
			standardizedPatientRequest.persist()
					.using(standardizedPatientProxy)
					.fire(new OSCEReceiver<Void>() {

						@Override
						public void onSuccess(Void arg0) {
							// init();

							MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(
									"Warning");
							dialogBox.showConfirmationDialog(constants
									.onDeleteRoleAssignedToPatient());
							view.setStatusIcon(StandardizedPatientStatus.ANONYMIZED);
						}

					});
		
			//Module 3 : Task B
		} else {
		
		if (!Window.confirm("Really delete this entry? You cannot undo this change.")) {
            return;
        }
		
        requests.standardizedPatientRequest().remove().using(standardizedPatientProxy).fire(new OSCEReceiver<Void>() {
            public void onSuccess(Void ignore) {
                if (widget == null) {
                    return;
                }
            	placeController.goTo(new StandardizedPatientPlace("StandardizedPatientPlace!DELETED"));
            }
        });
		}
		
	}
	@Override
	public void sendClicked(){
		dmxSyncService.pushToDMZ(standardizedPatientProxy.getId(), new AsyncCallback<Void>(){

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
			public void onSuccess(Void result) {
				Window.alert(messageLookup.exportSuccessful());
			}
			
		});	
		
	}
	
	@Override
	public void pullClicked(){

		dmxSyncService.pullFromDMZ(standardizedPatientProxy.getId(), new AsyncCallback<Void>(){

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
				Window.alert(messageLookup.importSussessful());
			}
			
		});
		
		
	}

	@Override
	public void deleteLangSkillClicked(LangSkillProxy langSkill) {
		requests.langSkillRequest().remove().using(langSkill).fire(new LangSkillUpdateReceiver());
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
		}anamReq.persist().using(anamnesisForm).fire(new ScarUpdateReceiver());
	}

	@Override
	public void addScarClicked() {
		AnamnesisFormRequest anamReq = requests.anamnesisFormRequest();
		
		ScarProxy scar = scarBox.getValue();
		Log.debug("Add scar (" + scar.getBodypart() + " - id " + scar.getId() + ") to anamnesis-form (" + anamnesisForm.getId() + ")");
		
		anamnesisForm = anamReq.edit(anamnesisForm);
		
		anamnesisForm.getScars().add(scar);
		
		anamReq.persist().using(anamnesisForm).fire(new ScarUpdateReceiver());
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
		langSkillRequest.persist().using(langSkillProxy).fire(new LangSkillUpdateReceiver());
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
	 stdPatRequest.persist().using(standardizedPatientProxy).fire();
	}
	
	//spec start
	@Override
	public void videoUploadSuccesfull(String results) {
	 StandardizedPatientRequest stdPatRequest = requests.standardizedPatientRequest();
	 standardizedPatientProxy = stdPatRequest.edit(standardizedPatientProxy);
	
	
	 standardizedPatientProxy.setVideoPath(results);
	 //spec end
	 
	 
	 
	 stdPatRequest.persist().using(standardizedPatientProxy).fire();
	}
	//spec end
	
	@Override
	public String getNameOfStandardizedPatient() {
		return standardizedPatientProxy.getName();
	}
	public Long getIdOfStandardizedPatient() {
		return standardizedPatientProxy.getId();
	}
	

	
}
