package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

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
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueRequest;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormRequest;
import ch.unibas.medizin.osce.client.managed.request.LangSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.LangSkillRequest;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.widgets.ProxySuggestOracle;
import ch.unibas.medizin.osce.domain.AnamnesisChecksValue;
import ch.unibas.medizin.osce.shared.LangSkillLevel;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
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
import com.google.gwt.view.client.SingleSelectionModel;

public class StandardizedPatientDetailsActivity extends AbstractActivity implements
StandardizedPatientDetailsView.Presenter, 
StandardizedPatientDetailsView.Delegate, 
StandardizedPatientScarSubView.Delegate,
StandardizedPatientAnamnesisSubView.Delegate,
StandardizedPatientLangSkillSubView.Delegate {
	
    private OsMaRequestFactory requestFactory;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private StandardizedPatientDetailsView view;
	private CellTable<StandardizedPatientProxy> table;
	private SingleSelectionModel<StandardizedPatientProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;

	private StandardizedPatientDetailsPlace place;
	private StandardizedPatientProxy standardizedPatientProxy;

	private StandardizedPatientScarSubView standardizedPatientScarSubView;
	private CellTable<ScarProxy> scarTable;
	private ValueListBox<ScarProxy> scarBox;
	private HandlerRegistration rangeChangeHandlerScars;
	
	private StandardizedPatientAnamnesisSubView standardizedPatientAnamnesisSubView;
	private CellTable<AnamnesisChecksValueProxy> anamnesisTable;
	
	private StandardizedPatientLangSkillSubView standardizedPatientLangSkillSubView;
	private CellTable<LangSkillProxy> langSkillTable;

	private AnamnesisFormProxy anamnesisForm ;

	public StandardizedPatientDetailsActivity(StandardizedPatientDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
    	this.requestFactory = requests;
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
		
		requestFactory.find(place.getProxyId()).with("profession", "descriptions", "nationality", "bankAccount", "langskills", "anamnesisForm", "anamnesisForm.scars").fire(new Receiver<Object>() {

			public void onFailure(ServerFailure error){
				Log.error(error.getMessage());
			}
			@Override
			public void onSuccess(Object response) {
				if(response instanceof StandardizedPatientProxy){
					Log.info(((StandardizedPatientProxy) response).getName());
					standardizedPatientProxy = (StandardizedPatientProxy) response;
					init();
					initScar();
					initAnamnesis();
					initLangSkills();
				}
			}
		});
	}

	private void init() {
		view.setValue(standardizedPatientProxy);
		anamnesisForm =  standardizedPatientProxy.getAnamnesisForm();
	}
	
	/*******************
	 * LANGSKILL TABLE
	 ******************/
	
	/**
	 * Loads languages, which the standardizedPatient does not speak and fills them into ValueListBoxe of the view.
	 * Requests the number of languages spoken by the given patient and then calls onRangeChangedLanguageSkillTable() 
	 * to fill the table.
	 */
	protected void initLangSkills() {
		// FIXME maybe it should be discerned between stuff that has to be done once
		// and stuff that has to be done multiple times? (handler, the class variable assignment vs. filling in values)
		this.langSkillTable = standardizedPatientLangSkillSubView.getLangSkillTable();
		
		// Fill ValueListBoxes
		requestFactory.languageRequestNonRoo().findLanguagesByNotStandardizedPatient(standardizedPatientProxy.getId()).fire(new Receiver<List<SpokenLanguageProxy>>() {
			@Override
			public void onSuccess(List<SpokenLanguageProxy> response) {
				Log.debug("Geholte Sprachen aus der Datenbank: " + response.size());
				standardizedPatientLangSkillSubView.setLanguagePickerValues(response);
			}
			
		});
		
		// Request number of Languages spoken by patient and call onRangeChangedLanguageSkillTable() to fill table
		requestFactory.langSkillRequestNonRoo().countLangSkillsByPatientId(standardizedPatientProxy.getId()).fire(new Receiver<Long>() {
			@Override
			public void onSuccess(Long count) {
				if (view == null) {
					return;
				}
				
				Log.debug(count.toString() + " language skills to load");
				langSkillTable.setRowCount(count.intValue(), true);
				onRangeChangedLanguageSkillTable();
			}
			
		});
		
		// FIXME: this should not be added everytime initLangSkills() is called, right?
		langSkillTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			@Override
			public void onRangeChange(RangeChangeEvent event) {
				Log.info("onRangeChangedLanguageSkillTable()");
				onRangeChangedLanguageSkillTable();
			}
		});
	}
	
	/**
	 * Fills the Language Skill table with data from the DB-Request, by
	 * firing the database request and providing a callback method.
	 */
	protected void onRangeChangedLanguageSkillTable() {
		final Range range = langSkillTable.getVisibleRange();
		
		fireLangSkillRangeRequest(range, new Receiver<List<LangSkillProxy>>() {
			@Override
			public void onSuccess(List<LangSkillProxy> values) {
				if (view == null) {
					return;
				}
				langSkillTable.setRowData(range.getStart(), values);
			}
			
		});
	}
	
	/**
	 * Fire database request for Language Skills
	 * @param range defines which elements to fetch ("from element x on, take n elements...");
	 * @param callback Method to call after the request is executed.
	 */
	private void fireLangSkillRangeRequest(final Range range, final Receiver<List<LangSkillProxy>> callback) {
		requestFactory.langSkillRequestNonRoo().findLangSkillsByPatientId(standardizedPatientProxy.getId(), range.getStart(), range.getLength()).with("spokenlanguage").fire(callback);
	}
	
	/*******************
	 * ANAMNESIS TABLE
	 ******************/

	protected void initAnamnesis() {
		// TODO implement
		
		this.anamnesisTable = standardizedPatientAnamnesisSubView.getTable();
		requestFactory.anamnesisChecksValueRequestNonRoo().fillAnamnesisChecksValues(standardizedPatientProxy.getAnamnesisForm().getId()).fire(new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				Log.info("update successful???");
			}
			
		});
		requestFactory.anamnesisChecksValueRequestNonRoo().countAnamnesisChecksValuesByAnamnesisForm(
				standardizedPatientProxy.getAnamnesisForm().getId()).fire(new Receiver<Long>() {
					@Override
					public void onSuccess(Long count) {
						if (view == null) {
							return;
						}
						
						Log.debug(count.toString() + " scars loaded");
						anamnesisTable.setRowCount(count.intValue(), true);
						
						onRangeChangedAnamnesisTable();
					}
					
//					public void onFailure(ServerFailure error) {
//						Log.error("failed on count");
//					}
				});
	}

	protected void onRangeChangedAnamnesisTable() {
//		final Range range = anamnesisTable.getVisibleRange();
//		
//		fireAnamnesisCheckRangeRequest(range, new Receiver<List<AnamnesisCheckProxy>>() {
//			@Override
//			public void onSuccess(List<AnamnesisCheckProxy> values) {
//				if (view == null) {
//					return;
//				}
//				// FIXME: ORacle should be filled with all anamnesisCheckProxyValues...
//				((ProxySuggestOracle<AnamnesisCheckProxy>) standardizedPatientAnamnesisSubView.getAnamnesisQuestionSuggestBox().getSuggestOracle()).addAll(values);
//				anamnesisTable.setRowData(range.getStart(), values);
//			}
////			
////			@Override
////			public void onFailure(ServerFailure error) {
////				Log.error("failed on exec");
////			}
//			
//		});
	}

	/**
	 * Executes the request for filling the AnamnesisCheckProxy-table in the AnamnesisSubView.
	 * @param range 
	 * @param receiver Callback (which should fill the table)
	 */
	private void fireAnamnesisCheckRangeRequest(Range range, Receiver<List<AnamnesisCheckProxy>> receiver) {
//		String[] paths = standardizedPatientAnamnesisSubView.getPaths();
//		Long anamnesisId = standardizedPatientProxy.getAnamnesisForm().getId();
//		
//		requestFactory.anamnesisCheckRequestNonRoo().findAnamnesisChecksByAnamnesisForm(anamnesisId, range.getStart(), range.getLength())
//				.with(paths).fire(receiver);
	}
	
	/*******************
	 * SCAR TABLE
	 ******************/

	/**
	 * Fills the ValueListBox and Table of the ScarSubView
	 */
	protected void initScar() {
		// FIXME maybe it should be discerned between stuff that has to be done once
		// and stuff that has to be done multiple times? (handler, the class variable assignment vs. filling in values)
		this.scarTable = standardizedPatientScarSubView.getTable();
		this.scarBox = standardizedPatientScarSubView.getScarBox();
		
		// Finds all scars, that can still be added to the patient (i.e. the patient doesn't have them yet) 
		// and fills the corresponding ValueListBox
		requestFactory.scarRequestNonRoo().findScarEntriesByNotAnamnesisForm(standardizedPatientProxy.getAnamnesisForm().getId()).fire(new Receiver<List<ScarProxy>>() {
			@Override
			public void onSuccess(List<ScarProxy> response) {
				standardizedPatientScarSubView.setScarBoxValues(response);
			}
		});

		// Request number of scars the patient has and then fill the table by calling onRangeChangedScarTable()
		requestFactory.scarRequestNonRoo().countScarsByAnamnesisForm(standardizedPatientProxy.getAnamnesisForm().getId()).fire(new Receiver<Long>(){
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
		});
		
		// FIXME: should probably only be called once?
		scarTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			@Override
			public void onRangeChange(RangeChangeEvent event) {
				Log.info("onRangeChangedScarTable()");
				onRangeChangedScarTable();
			}
		});
	}
	
	/**
	 * Fills the scar table of the ScarSubView with values from the database.
	 * Fires a database request and defines the callback-class for filling the
	 * database.
	 */
	protected void onRangeChangedScarTable() {
		final Range range = scarTable.getVisibleRange();

		final Receiver<List<ScarProxy>> callback = new Receiver<List<ScarProxy>>() {
			@Override
			public void onSuccess(List<ScarProxy> values) {
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
		};

		fireScarRangeRequest(range, callback);
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
		return requestFactory.scarRequestNonRoo().findScarEntriesByAnamnesisForm(standardizedPatientProxy.getAnamnesisForm().getId(), range.getStart(), range.getLength());
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
		if (!Window.confirm("Really delete this entry? You cannot undo this change.")) {
            return;
        }
		
        requestFactory.standardizedPatientRequest().remove().using(standardizedPatientProxy).fire(new Receiver<Void>() {

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
		requestFactory.langSkillRequest().remove().using(langSkill).fire(new Receiver<Void>() {
			@Override
			public void onSuccess(Void response) {
				Log.debug("Language removed from langSkills");
				initLangSkills();
			}
		});
	}
	
	@Override
	public void deleteScarClicked(ScarProxy scar) {
		AnamnesisFormRequest anamReq = requestFactory.anamnesisFormRequest();
		anamnesisForm =  anamReq.edit(anamnesisForm);
		
		Log.debug("Remove scar (" + scar.getId() + ") from anamnesis-form (" + standardizedPatientProxy.getAnamnesisForm().getId() + ")");
		
		Iterator<ScarProxy> i = anamnesisForm.getScars().iterator();
		while (i.hasNext()) {
			ScarProxy scarProxy = (ScarProxy) i.next();
			//Log.warn(scarProxy.stableId() + " ");
			//Log.warn(scar.stableId() + " ");
			if (scarProxy.getId() == scar.getId() ) {
				anamnesisForm.getScars().remove(scarProxy);
				break;
			}
		}
//		anamnesisForm.getScars().remove(scar);
		
		anamReq.persist().using(anamnesisForm).fire(new Receiver<Void>(){
			@Override
			public void onSuccess(Void arg0) {
				Log.debug("scar removed from anamnesisform...");
				initScar();
			}
		});
	}

	@Override
	public void addScarClicked() {
		AnamnesisFormRequest anamReq = requestFactory.anamnesisFormRequest();
		
		ScarProxy scar = scarBox.getValue();
		Log.debug("Add scar (" + scar.getBodypart() + " - id " + scar.getId() + ") to anamnesis-form (" + standardizedPatientProxy.getAnamnesisForm().getId() + ")");
		
		anamnesisForm = anamReq.edit(anamnesisForm);
		
		anamnesisForm.getScars().add(scar);
		
		anamReq.persist().using(anamnesisForm).fire(new Receiver<Void>(){
			@Override
			public void onSuccess(Void arg0) {
				Log.debug("scar added...");
				initScar();
			}
		});
	}
	
	@Override
	public void addLangSkillClicked(SpokenLanguageProxy spokenLanguageProxy, LangSkillLevel langSkillLevel) {
		// get requestContext and initialize new langSkillProxy
		LangSkillRequest langSkillRequest = requestFactory.langSkillRequest();
		LangSkillProxy langSkillProxy = langSkillRequest.create(LangSkillProxy.class);
		langSkillProxy.setSkill(langSkillLevel);
		langSkillProxy.setSpokenlanguage(spokenLanguageProxy);
		langSkillProxy.setStandardizedpatient(standardizedPatientProxy);
		
		Log.debug("add skill " + langSkillLevel.toString() + " in language " + spokenLanguageProxy.getLanguageName());
		
		// write new langSkill to database and re-initialize table
		langSkillRequest.persist().using(langSkillProxy).fire(new Receiver<Void>() {
			@Override
			public void onSuccess(Void arg0) {
				Log.debug("added language skill");
				initLangSkills();
			}
		});
	}

	@Override
	public void saveAnamnesisQuestionChanges(AnamnesisChecksValueProxy proxy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void searchAnamnesisQuestion(AnamnesisChecksValueProxy proxy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void searchAnamnesisQuestion(String needle) {
		// TODO Auto-generated method stub
		
	}
}
