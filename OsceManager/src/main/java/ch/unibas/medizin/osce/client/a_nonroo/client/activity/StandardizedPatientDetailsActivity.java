package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientAnamnesisSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientScarSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientLangSkillSubView;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueRequest;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormRequest;
import ch.unibas.medizin.osce.client.managed.request.LangSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.LangSkillRequest;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.shared.LangSkillLevel;
import ch.unibas.medizin.osce.shared.scaffold.AnamnesisChecksValueRequestNonRoo;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
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
StandardizedPatientLangSkillSubView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private StandardizedPatientDetailsView view;

	private StandardizedPatientDetailsPlace place;
	private StandardizedPatientProxy standardizedPatientProxy;

	private StandardizedPatientScarSubView standardizedPatientScarSubView;
	private CellTable<ScarProxy> scarTable;
	private ValueListBox<ScarProxy> scarBox;
	
	private StandardizedPatientAnamnesisSubView standardizedPatientAnamnesisSubView;
	private CellTable<AnamnesisChecksValueProxy> anamnesisTable;
	
	private StandardizedPatientLangSkillSubView standardizedPatientLangSkillSubView;
	private CellTable<LangSkillProxy> langSkillTable;

	private AnamnesisFormProxy anamnesisForm ;

	public StandardizedPatientDetailsActivity(StandardizedPatientDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
    	this.requests = requests;
    	this.placeController = placeController;
    }

	public void onStop(){

	}
	
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("StandardizedPatientDetailsActivity.start()");
		StandardizedPatientDetailsView standardizedPatientDetailsView = new StandardizedPatientDetailsViewImpl();
		standardizedPatientDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = standardizedPatientDetailsView;
		standardizedPatientScarSubView = view.getStandardizedPatientScarSubViewImpl();
		standardizedPatientAnamnesisSubView = view.getStandardizedPatientAnamnesisSubViewImpl();
		standardizedPatientLangSkillSubView = view.getStandardizedPatientLangSkillSubViewImpl();
		
		widget.setWidget(standardizedPatientDetailsView.asWidget());
		
		view.setDelegate(this);
		standardizedPatientScarSubView.setDelegate(this);
		standardizedPatientAnamnesisSubView.setDelegate(this);
		standardizedPatientLangSkillSubView.setDelegate(this);
		
		requests.find(place.getProxyId()).with("profession", "descriptions", "nationality", "bankAccount", "langskills", "anamnesisForm", "anamnesisForm.scars").fire(new InitializeActivityReceiver());
	}
	
	/**
	 * Used as a callback for the request that gets the @StandardizedPatientProxy
	 * that is edited in this activities instance.
	 */
	
	private class InitializeActivityReceiver extends Receiver<Object> {
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
	
	/**
	 * Callback for filling the language picker with all available @SpokenLanguageProxy
	 * elements.
	 */
	
	private class SpokenLanguageReceiver extends Receiver<List<SpokenLanguageProxy>> {
		@Override
		public void onSuccess(List<SpokenLanguageProxy> response) {
			Log.debug("Geholte Sprachen aus der Datenbank: " + response.size());
			standardizedPatientLangSkillSubView.setLanguagePickerValues(response);
		}
	}
	
	/**
	 * Callback for 
	 */
	
	private class LangSkillCountReceiver extends Receiver<Long> {
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
	
	
	private class LangSkillUpdateReceiver extends Receiver<Void> {
		@Override
		public void onSuccess(Void response) {
			Log.debug("Langskills updated successfully");
			fillLangSkills();
		}
	}
	
	
	private class AnamnesisChecksValueFillReceiver extends Receiver<Void> {		
		@Override
		public void onSuccess(Void response) {
			fireAnamnesisChecksValueCountRequest();
		}
		
	}
	
	
	private class AnamnesisChecksValueCountReceiver extends Receiver<Long> {
		@Override
		public void onSuccess(Long count) {
			if (view == null) {
				return;
			}
			Log.debug(count.toString() + " AnamnesisChecksValues found");
			anamnesisTable.setRowCount(count.intValue(), true);
			fireAnamnesisChecksValueRequest();
		}
	}
	
	
	private class AnamnesisChecksValueReceiver extends Receiver<List<AnamnesisChecksValueProxy>> {
		@Override
		public void onSuccess(List<AnamnesisChecksValueProxy> response) {
			Range range = anamnesisTable.getVisibleRange();
			Log.debug("response.size(): " + response.size());
			anamnesisTable.setRowData(range.getStart(), response);
		}
	}
	
	
	private class AnamnesisChecksValueUpdateReceiver extends Receiver<Void> {

		@Override
		public void onSuccess(Void response) {
			Log.info("AnamnesisChecksValue updated");
			onRangeChangedAnamnesis();
		}
	}
	
	
	private class ScarBoxReceiver extends Receiver<List<ScarProxy>> {
		@Override
		public void onSuccess(List<ScarProxy> response) {
			standardizedPatientScarSubView.setScarBoxValues(response);
		}
	}
	
	
	private class ScarCountReceiver extends Receiver<Long> {
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
	
	
	private class ScarReceiver extends Receiver<List<ScarProxy>> {
		
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
	
	
	private class ScarUpdateReceiver extends Receiver<Void>{
		@Override
		public void onSuccess(Void arg0) {
			Log.debug("scar updated...");
			fillScar();
		}
	}

	private void init() {
		view.setValue(standardizedPatientProxy);
		anamnesisForm =  standardizedPatientProxy.getAnamnesisForm();
		initScar();
		initAnamnesis();
		initLangSkills();
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
		this.anamnesisTable = standardizedPatientAnamnesisSubView.getTable();
		this.anamnesisTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			@Override
			public void onRangeChange(RangeChangeEvent event) {
				Log.debug("onRangeChange() ==> onRangeChangedAnamnesis()");
				onRangeChangedAnamnesis();
			}
		});
		onRangeChangedAnamnesis();
	}
	
	
	private void onRangeChangedAnamnesis() {
		// TODO Implementation mit Checkboxes korrekt machen
		if (standardizedPatientAnamnesisSubView.areUnansweredQuestionsShown()) {
			// fills the AnamnesisChecksValue table in the database with
			// NULL-values for unanswered questions
			Log.info("unanswered questions are shown (fill table)");
			requests.anamnesisChecksValueRequestNonRoo().fillAnamnesisChecksValues(anamnesisForm.getId()).fire(new AnamnesisChecksValueFillReceiver());
		} else {
			// requests the number of rows in AnamnesisChecksValue for the
			// current patient
			fireAnamnesisChecksValueCountRequest();
		}
	}
	
	
	private void fireAnamnesisChecksValueCountRequest() {
		String query = standardizedPatientAnamnesisSubView.getSearchString();
		Receiver<Long> receiver = new AnamnesisChecksValueCountReceiver();
		boolean answeredQuestions = standardizedPatientAnamnesisSubView.areAnsweredQuestionsShown();
		boolean unansweredQuestions = standardizedPatientAnamnesisSubView.areUnansweredQuestionsShown();
		AnamnesisChecksValueRequestNonRoo request = requests.anamnesisChecksValueRequestNonRoo(); 
		
		if (answeredQuestions && unansweredQuestions) {
			Log.debug("count -- show answered and unanswered");
			request.countAllAnamnesisChecksValuesByAnamnesisForm(anamnesisForm.getId(), query).fire(receiver);
		} else if (answeredQuestions) {
			Log.debug("count -- show only answered");
			request.countAnsweredAnamnesisChecksValuesByAnamnesisForm(anamnesisForm.getId(), query).fire(receiver);
		} else if (unansweredQuestions) {
			Log.debug("count -- show only unanswered");
			request.countUnansweredAnamnesisChecksValuesByAnamnesisForm(anamnesisForm.getId(), query).fire(receiver);
		} else {
			Log.debug("count -- show none");
			receiver.onSuccess(new Long(0));
		}
	}

	
	protected void fireAnamnesisChecksValueRequest() {
		String query = standardizedPatientAnamnesisSubView.getSearchString();
		boolean answered = standardizedPatientAnamnesisSubView.areAnsweredQuestionsShown();
		boolean unanswered = standardizedPatientAnamnesisSubView.areUnansweredQuestionsShown();
		
		String[] paths = standardizedPatientAnamnesisSubView.getPaths();
				
		AnamnesisChecksValueRequestNonRoo request = requests.anamnesisChecksValueRequestNonRoo();
		AnamnesisChecksValueReceiver receiver = new AnamnesisChecksValueReceiver();
		
		Range range = anamnesisTable.getVisibleRange();
		Log.debug("range.getStart():" + range.getStart() + "; range.getLength():" + range.getLength() + ";");
		
		if (answered && unanswered) {
			Log.debug("request -- show answered and unanswered");
			request.findAnamnesisChecksValuesByAnamnesisForm(anamnesisForm.getId(), query, range.getStart(), range.getLength())
					.with(paths).fire(receiver);
		} else if (answered) {
			Log.debug("request -- show only answered");
			request.findAnsweredAnamnesisChecksValuesByAnamnesisForm(anamnesisForm.getId(), query, range.getStart(), range.getLength())
					.with(paths).fire(receiver);
		} else if (unanswered) {
			Log.debug("request -- show only unanswered");
			request.findUnansweredAnamnesisChecksValuesByAnamnesisForm(anamnesisForm.getId(), query, range.getStart(), range.getLength())
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
	
	@Override
	public void goTo(Place place) {
		placeController.goTo(place);		
	}

	@Override
	public void editPatientClicked() {
		Log.info("edit clicked");
		goTo(new StandardizedPatientDetailsPlace(standardizedPatientProxy.stableId(),
				StandardizedPatientDetailsPlace.Operation.EDIT));
	}

	@Override
	public void deletePatientClicked() {
		// TODO replace with appropriate message
		if (!Window.confirm("Really delete this entry? You cannot undo this change.")) {
            return;
        }
		
        requests.standardizedPatientRequest().remove().using(standardizedPatientProxy).fire(new Receiver<Void>() {
            public void onSuccess(Void ignore) {
                if (widget == null) {
                    return;
                }
            	placeController.goTo(new StandardizedPatientPlace("StandardizedPatientPlace!DELETED"));
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
		request.persist().using(proxy).fire(new AnamnesisChecksValueUpdateReceiver());
	}
	
	@Override
	public void saveAnamnesisChecksValueProxyChanges(AnamnesisChecksValueProxy proxy, String comment) {
		AnamnesisChecksValueRequest request = requests.anamnesisChecksValueRequest();
		proxy = request.edit(proxy);
		proxy.setComment(comment);
		request.persist().using(proxy).fire(new AnamnesisChecksValueUpdateReceiver());
	}
	
	@Override
	public void performAnamnesisSearch() {
		onRangeChangedAnamnesis();
	}
}
