package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.SearchCriteria;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchAnamnesisPopup;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchAnamnesisPopupImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchLanguagePopup;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchLanguagePopupImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchNationalityPopup;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchNationalityPopupImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchPopup;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchScarPopup;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchScarPopupImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandartizedPatientAdvancedSearchBasicCriteriaPopUp;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandartizedPatientAdvancedSearchBasicCriteriaPopUpImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandartizedPatientAdvancedSearchSubView;
import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.ProxySuggestOracle;
import ch.unibas.medizin.osce.domain.AdvancedSearchCriteria;

import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison;
import ch.unibas.medizin.osce.shared.LangSkillLevel;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.PossibleFields;
import ch.unibas.medizin.osce.shared.Sorting;
import ch.unibas.medizin.osce.shared.scaffold.StandardizedPatientRequestNonRoo;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.requestfactory.shared.Violation;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * Activity that handles the search of standardized patients and the display of the search results.
 * @author nikotsunami
 *
 */
public class StandardizedPatientActivity extends AbstractActivity implements StandardizedPatientView.Presenter, StandardizedPatientView.Delegate,
		StandartizedPatientAdvancedSearchSubView.Delegate, StandartizedPatientAdvancedSearchBasicCriteriaPopUp.Delegate,
		StandardizedPatientAdvancedSearchLanguagePopup.Delegate, StandardizedPatientAdvancedSearchScarPopup.Delegate,
		StandardizedPatientAdvancedSearchAnamnesisPopup.Delegate, StandardizedPatientAdvancedSearchNationalityPopup.Delegate{

	/** Holds the applications request factory */
	private OsMaRequestFactory requests;
	/** Holds the applications placeController */
	private PlaceController placeController;
	/** Holds the applications' activityManager */
	private ActivityManager activityManger;
	/** Holds the panel in which the view will be displayed */
	private AcceptsOneWidget widget;
	/** Holds the main view managed by this activity */
	private StandardizedPatientView view;
	/** Holds the table with the standardized patients */
	private CellTable<StandardizedPatientProxy> table;
	/** holds the selection model of the standardized patient table */
	private SingleSelectionModel<StandardizedPatientProxy> selectionModel;
	
	/** Holds this activities' activityMapper */
	private StandardizedPatientDetailsActivityMapper StandardizedPatientDetailsActivityMapper;
	/** Holds the SubView where the advanced search criteria can be defined by the user */
	private StandartizedPatientAdvancedSearchSubView standartizedPatientAdvancedSearchSubView;
	

	/** Holds the request used to request patient data etc. */
	private StandardizedPatientRequestNonRoo requestAdvSeaCritStd;
	
	/** Holds the table with the advanced search criteria */ 
	private CellTable<AdvancedSearchCriteriaProxy> criteriaTable;
	/** Holds the currently active advancedSearchCriteria */
	private List<AdvancedSearchCriteriaProxy> searchCriteria = new ArrayList<AdvancedSearchCriteriaProxy>();
	
	/** Holds the search string to use to find patients (quick search, looking through the fields defined in Filter panel */ 
	private String quickSearchTerm = "";
	/** List of fields that should be searched for the quickSearchTerm */
	private List<String> searchThrough = Arrays.asList("name", "preName");
	
	/** Holds a reference to the currently selected advancedSearchPopup */
	private StandardizedPatientAdvancedSearchPopup advancedSearchPopup;
	/** Holds a reference to the anamnesisPopup if open */
	private StandardizedPatientAdvancedSearchAnamnesisPopup anamnesisPopup;
	/** Holds a reference to the basicCriteriaPopUp if open */
	private StandartizedPatientAdvancedSearchBasicCriteriaPopUp basicCriteriaPopUp;
	/** Holds a reference to the scarPopup if open */
	private StandardizedPatientAdvancedSearchScarPopup scarPopup;
	/** Holds a reference to the nationalityPopup if open */
	private StandardizedPatientAdvancedSearchLanguagePopup languagePopup;
	/** Holds a reference to the nationalityPopup if open */
	private StandardizedPatientAdvancedSearchNationalityPopup nationalityPopup;
	
	/**
	 * Sets the dependencies of this activity and initializes the corresponding activity manager 
	 * @param requests The request factory to use
	 * @param placeController the place controller to use
	 */
	public StandardizedPatientActivity(OsMaRequestFactory requests, PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;
		StandardizedPatientDetailsActivityMapper = new StandardizedPatientDetailsActivityMapper(requests, placeController);
		this.activityManger = new ActivityManager(StandardizedPatientDetailsActivityMapper, requests.getEventBus());
	}

	/**
	 * Clean up activity on finish (close popups and disable display of current activities view)
	 */
	public void onStop() {
		if (advancedSearchPopup != null) {
			advancedSearchPopup.hide();
		}
		activityManger.setDisplay(null);
	}

	/**
	 * Initializes the corresponding views and initializes the tables as well as their
	 * corresponding handlers.
	 */
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		StandardizedPatientView systemStartView = new StandardizedPatientViewImpl();
		systemStartView.setPresenter(this);

		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		this.table = view.getTable();

		standartizedPatientAdvancedSearchSubView = view.getStandartizedPatientAdvancedSearchSubViewImpl();
		standartizedPatientAdvancedSearchSubView.setDelegate(this);

		criteriaTable = standartizedPatientAdvancedSearchSubView.getTable();
		
		table.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				StandardizedPatientActivity.this.onRangeChanged();
			}
		});
		
		table.addColumnSortHandler(new ColumnSortEvent.Handler() {
			@Override
			public void onColumnSort(ColumnSortEvent event) {
				StandardizedPatientActivity.this.onRangeChanged();
			}
		});
		
		initSearch();
		
		activityManger.setDisplay(view.getDetailsPanel());
		
		// Inherit the view's key provider
		ProvidesKey<StandardizedPatientProxy> keyProvider = ((AbstractHasData<StandardizedPatientProxy>) table).getKeyProvider();
		selectionModel = new SingleSelectionModel<StandardizedPatientProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		// adds a selection handler to the table so that if a valid patient is selected,
		// the corresponding details view is shown (via showDetails())
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				StandardizedPatientProxy selectedObject = selectionModel.getSelectedObject();
				if (selectedObject != null) {
					Log.debug(selectedObject.getName() + " selected!");
					showDetails(selectedObject);
				}
			}
		});
		view.setDelegate(this);
	}
	
	/**
	 * Receiver class for count request. if the request succeeds, sets 
	 * the rowCount of the standardized patient table and then executes
	 * the actual request to fill the table.
	 */
	@SuppressWarnings("deprecation")
	private class StandardizedPatientCountReceiver extends Receiver<Long> {
		@Override
		public void onSuccess(Long response) {
			if (view == null) {
				// This activity is dead
				return;
			}
			Log.debug("Geholte Patienten aus der Datenbank: " + response);
			view.getTable().setRowCount(response.intValue(), true);
			onRangeChanged();
		}
	}
	
	/**
	 * Receiver for the standardized patients that met the search criteria.
	 * If execution was successful, the table will be filled with the
	 * patients.
	 */
	 @SuppressWarnings("deprecation")
	private class StandardizedPatientReceiver extends Receiver<List<StandardizedPatientProxy>> {
		public void onFailure(ServerFailure error) {
			Log.error(error.getMessage());
			// onStop();
		}

		public void onViolation(Set<Violation> errors) {
			Iterator<Violation> iter = errors.iterator();
			String message = "";
			while (iter.hasNext()) {
				Violation it = iter.next() ; 
				message += "message "+it.getMessage() + "\n";
				message += "path "+it.getPath() + "\n";
				message += "class "+it.getClass() + "\n";
				message += "INV "+it.getInvalidProxy() + "\n";
				message += "OR "+it.getOriginalProxy() + "\n";
				message += "ID "+it.getProxyId() + "<br>";
			}
			Log.warn(" in Simpat -" + message);
			// onStop();
		}

		@Override
		public void onSuccess(List<StandardizedPatientProxy> response) {
			final Range range = table.getVisibleRange();
			table.setRowData(range.getStart(), response);
		}
	}

	/**
	 * Initializes the search for standardized patients, by first 
	 * executing a count request. Execution is then continued in 
	 * StandardizedPatientCountReceiver
	 */
	@SuppressWarnings("deprecation")
	private void initSearch() {
		//TODO: @@@SPEC when declared here, the simple search works
		requestAdvSeaCritStd = requests.standardizedPatientRequestNonRoo();
		// (1) Text search
		List<String> searchThrough = view.getSearchFilters();
		SearchCriteria criteria = view.getCriteria();
		Range range = table.getVisibleRange();

		// (2) Advanced search
		requests.standardizedPatientRequestNonRoo().countPatientsByAdvancedSearchAndSort(
	    		quickSearchTerm, searchThrough, searchCriteria).fire(new StandardizedPatientCountReceiver());	
	}

	/**
	 * Shows the details place for a given StandardizedPatientProxy
	 * @param StandardizedPatient the patient of whom details should be displayed
	 */
	protected void showDetails(StandardizedPatientProxy StandardizedPatient) {
		Log.debug(StandardizedPatient.getName());
		goTo(new StandardizedPatientDetailsPlace(StandardizedPatient.stableId(), Operation.DETAILS));
	}
	
	/**
	 * Executes the search for a standardized patient based on 
	 * advanced search criteria, quick search term and quick search
	 * filters. Control is handed to  
	 */
	@SuppressWarnings({ "deprecation" })
	protected void onRangeChanged() {
		// TODO: ###david### test code

		//List<String> fields = Arrays.asList("weight", "height", "bmi");
		//List<String> values = Arrays.asList("80", "180", "30");
		// List<String> comparations = Arrays.asList(Comparison2.EQUALS,
		// Comparison2.LESS, Comparison2.MORE);
		// List<String> bindType = Arrays.asList(BindType.AND, BindType.AND,
		// BindType.AND);
		

		//requestAdvSeaCritStd = requests.standardizedPatientRequestNonRoo();
		//
		// AdvancedSearchCriteriaProxy criteria =
		// requestAdvSeaCritStd.create(AdvancedSearchCriteriaProxy.class);
		// requestAdvSeaCritStd.edit(criteria);
		// criteria.setBindType(BindType.AND);
		// criteria.setComparation(Comparison2.EQUALS);
		// criteria.setField(PossibleFields.weight);
		// criteria.setValue("80");
		//
		// searchCriteria.add(criteria);
		//WARNING: TEST: 

		//searchCriteria.clear();
		/* AdvancedSearchCriteriaProxy criteria2 =
		 requestAdvSeaCritStd.create(AdvancedSearchCriteriaProxy.class);
		 requestAdvSeaCritStd.edit(criteria2);
		 criteria2.setBindType(BindType.AND);
		 criteria2.setComparation(Comparison2.EQUALS);
		 criteria2.setField(PossibleFields.LANGUAGE);
		 //"Deutsch: A1"
		 criteria2.setValue("Deutsch: NATIVE_SPEAKER");
		 criteria2.setObjectId(new Long(6));
		 searchCriteria.add(criteria2);*/

//		 AdvancedSearchCriteriaProxy criteria3 =
//		 requestAdvSeaCritStd.create(AdvancedSearchCriteriaProxy.class);
//		 requestAdvSeaCritStd.edit(criteria3);
//		 criteria3.setBindType(BindType.OR);
//		 criteria3.setComparation(Comparison2.NOTEQUALS);
//		 criteria3.setField(PossibleFields.ANAMNESIS);
//		 criteria3.setObjectId(new Long(7));//question ID
//		 //Nehmen Sie eines der aufgelisteten Medikamete und wenn ja, welche?
//		 criteria3.setValue("QuestionMultM: Prozac:Prozac|Ritalin|Aspirin|Ethanol");
//		
//		 searchCriteria.add(criteria3);
//
//		 AdvancedSearchCriteriaProxy criteria4 =
//		 requestAdvSeaCritStd.create(AdvancedSearchCriteriaProxy.class);
//		 requestAdvSeaCritStd.edit(criteria4);
//		 criteria4.setBindType(BindType.AND);
//		 criteria4.setComparation(Comparison2.EQUALS);
//		 criteria4.setField(PossibleFields.ANAMNESIS);
//		 criteria4.setObjectId(new Long(1));//question ID
//		 //Rauchen Sie? 
//		 criteria4.setValue("QuestionYesNo: Nein:");//no options for answers: type = 1
//		
//		 searchCriteria.add(criteria4);
//
//		 AdvancedSearchCriteriaProxy criteria5 =
//		 requestAdvSeaCritStd.create(AdvancedSearchCriteriaProxy.class);
//		 requestAdvSeaCritStd.edit(criteria5);
//		 criteria5.setBindType(BindType.AND);
//		 criteria5.setComparation(Comparison2.NOTEQUALS);
//		 criteria5.setField(PossibleFields.anamnesis);
//		 criteria5.setObjectId(new Long(2));//question ID
//		 //Wie oft rauchen Sie? 
//		 criteria5.setValue("QuestionMultS: oft:oft|mittel|selten");
//		
//		 searchCriteria.add(criteria5);
		 //
//		 criteriaTable.setRowData(searchCriteria);
		/*
		 * searchCriteria.add(new AdvancesSearchCriteriumOld
		 * (PossibleFields.weight, BindType.AND, Comparison2.EQUALS, "80"));
		 * searchCriteria.add(new AdvancesSearchCriteriumOld
		 * (PossibleFields.height, BindType.OR, Comparison2.LESS, "180"));
		 * searchCriteria.add(new AdvancesSearchCriteriumOld
		 * (PossibleFields.bmi, BindType.AND, Comparison2.MORE, "30"));
		 */
		 
		// searchThrough = new ArrayList<String>(); 
		 
		 // TODO: some bug about request
		 requestAdvSeaCritStd = requests.standardizedPatientRequestNonRoo();
		
		for (AdvancedSearchCriteriaProxy criterion : searchCriteria) {
			Log.info("Criterion: " + criterion.getField().toString() + ": " + criterion.getValue());
		}
		
		Range range = table.getVisibleRange();

		requestAdvSeaCritStd.findPatientsByAdvancedSearchAndSort("name", Sorting.ASC, quickSearchTerm, 
				searchThrough, searchCriteria, range.getStart(), range.getLength() /*fields, bindType, comparations, values */).
				fire(new StandardizedPatientReceiver());

		// OLD (1) Sorting

//		Boolean asc = true;
//		String sortField = "name"; // TODO: handle sort change events
//
//		if (table.getColumnSortList().size() > 0) {
//
//			asc = table.getColumnSortList().get(0).isAscending();
//
//		}
//
//		// (2) Text search
//		searchThrough = view.getSearchFilters();
//
//		// (3) Advanced search
//		final Receiver<List<StandardizedPatientProxy>> callback = new Receiver<List<StandardizedPatientProxy>>() {
//			@Override
//			public void onSuccess(List<StandardizedPatientProxy> values) {
//				if (view == null) {
//					// This activity is dead
//					return;
//				}
//				table.setRowData(range.getStart(), values);
//
//				// finishPendingSelection();
//				if (widget != null) {
//					widget.setWidget(view.asWidget());
//				}
//			}
//		};
//
//		fireRangeRequest(sortField, asc, q, new Integer(range.getStart()), new Integer(range.getLength()), searchThrough, view.getCriteria().getFields(), view
//				.getCriteria().getComparisons(), view.getCriteria().getValues(), callback);
	}

	/**
	 * Action to perform when the user wants to create a new patient
	 */
	@Override
	public void newClicked() {
		Log.info("create clicked");
		placeController.goTo(new StandardizedPatientDetailsPlace(Operation.CREATE));
	}

	/**
	 * Action to perfom when the value of the quicksearch field or the filters
	 * or the criteria change. sets the quickSearchTerm and initializes a search.
	 * @param q the search term
	 * @param searchThrough the list of fields to search the term in
	 */
	@Override
	public void performSearch(String q, List<String> searchTrough) {
		quickSearchTerm = q;
		this.searchThrough = searchTrough;
		Log.debug("Search for " + q);
		initSearch();
	}

	/**
	 * go to another place
	 * @param place the place to go to
	 */
	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	/**
	 * ???
	 */
	@Override
	public void filterTableClicked() {
		// TODO Auto-generated method stub

	}

	/**
	 * Shows or hides the popup to add basic search criteria to the
	 * criteria table.
	 * @param addBasicData the button clicked, used to determine the position
	 */
	@Override
	public void addBasicCriteriaClicked(Button addBasicData) {
		if (advancedSearchPopup != null && advancedSearchPopup.isShowing()) {
			advancedSearchPopup.hide();
			if (advancedSearchPopup == basicCriteriaPopUp) {
				return;
			}
		}

		basicCriteriaPopUp = new StandartizedPatientAdvancedSearchBasicCriteriaPopUpImpl();
		basicCriteriaPopUp.setDelegate(this);
		basicCriteriaPopUp.display(addBasicData);
		advancedSearchPopup = basicCriteriaPopUp;
	}

	/**
	 * Shows or hides the popup to add a scar criterion to the criteria table.
	 * Executes a request on the database for scars.
	 * @param parentButton the Button clicked, used to determine the position of the popup 
	 */
	@Override
	public void addScarCriteriaClicked(Button parentButton) {
		requests.scarRequest().findAllScars().fire(new ScarCriteriaReceiver());
		if (advancedSearchPopup != null && advancedSearchPopup.isShowing()) {
			advancedSearchPopup.hide();
			if (advancedSearchPopup == scarPopup) {
				return;
			}
		}
		scarPopup = new StandardizedPatientAdvancedSearchScarPopupImpl();
		scarPopup.setDelegate(this);
		scarPopup.display(parentButton);
		advancedSearchPopup = scarPopup;
	}

	/**
	 * Shows or hides the popup to add an anamnesis criterion to the criteria table.
	 * Executes a request on the database for anamnesisChecks.
	 * @param parentButton the Button clicked, used to determine the position of the popup 
	 */
	@Override
	public void addAnamnesisCriteriaClicked(Button parentButton) {
		requests.anamnesisCheckRequest().findAllAnamnesisChecks().fire(new AnamnesisCriteriaReceiver());
		if (advancedSearchPopup != null && advancedSearchPopup.isShowing()) {
			advancedSearchPopup.hide();
			if (advancedSearchPopup == anamnesisPopup) {
				return;
			}
		}
		anamnesisPopup = new StandardizedPatientAdvancedSearchAnamnesisPopupImpl();
		anamnesisPopup.setDelegate(this);
		anamnesisPopup.display(parentButton);
		advancedSearchPopup = anamnesisPopup;
	}

	/**
	 * Shows or hides the popup to add a language criterion to the criteria table.
	 * Executes a request on the database for languages.
	 * @param addLanguageButton the Button clicked, used to determine the position of the popup 
	 */
	@Override
	public void addLanguageCriteriaClicked(Button addLanguageButton) {
		requests.spokenLanguageRequest().findAllSpokenLanguages().fire(new LanguageCriteriaReceiver());
		if (advancedSearchPopup != null && advancedSearchPopup.isShowing()) {
			advancedSearchPopup.hide();
			if (advancedSearchPopup == languagePopup) {
				return;
			}
		}
		languagePopup = new StandardizedPatientAdvancedSearchLanguagePopupImpl();
		languagePopup.setDelegate(this);
		languagePopup.display(addLanguageButton);
		advancedSearchPopup = languagePopup;
	}

	/**
	 * Shows or hides the popup to add a nationality criterion to the criteria table.
	 * Executes a request on the database for nationalities.
	 * @param addNationalityButton the Button clicked, used to determine the position of the popup 
	 */
	@Override
	public void addNationalityCriteriaClicked(IconButton addNationalityButton) {
		requests.nationalityRequest().findAllNationalitys().fire(new NationalityCriteriaReceiver());
		if (advancedSearchPopup != null && advancedSearchPopup.isShowing()) {
			advancedSearchPopup.hide();
			if (advancedSearchPopup == nationalityPopup) {
				return;
			}
		}
		nationalityPopup = new StandardizedPatientAdvancedSearchNationalityPopupImpl();
		nationalityPopup.setDelegate(this);
		nationalityPopup.display(addNationalityButton);
		advancedSearchPopup = nationalityPopup;
	}

	/**
	 * adds a criterion selected in one of the AdvancedSearchPopups to the criteria table.
	 * @param objectId
	 * @param string
	 * @param bindType wether the criteria to add should be used with an and or or conjunction
	 * @param possibleFields 
	 * @param comparison Which type of comparison applies to the criterion (equals, greater, smaller, not equals)
	 */
	@Override
	public void addAdvSeaBasicButtonClicked(Long objectId, String string, BindType bindType, PossibleFields possibleFields, Comparison comparison) {
	//	requestAdvSeaCritStd.fire();
//		requestAdvSeaCritStd.fire();
//
//		
		StandardizedPatientRequestNonRoo req = requests.standardizedPatientRequestNonRoo();
		AdvancedSearchCriteriaProxy criteria = req.create(AdvancedSearchCriteriaProxy.class);
		criteria = req.edit(criteria);
		criteria.setBindType(bindType);
		criteria.setComparation(comparison);
		criteria.setField(possibleFields);
		criteria.setValue(string);
		criteria.setObjectId(objectId);
		req.fire();
		searchCriteria.add(criteria);

		criteriaTable.setRowData(searchCriteria);
		initSearch();
	}
	
	/**
	 * removes the given criterion from the criteria table.
	 * @param criterion the criterion to remove from the table.
	 */
	public void deleteAdvancedSearchCriteria(AdvancedSearchCriteriaProxy criterion) {
		searchCriteria.remove(criterion);
		// TODO execute search
		criteriaTable.setRowData(searchCriteria);
	}
	
	/**
	 * Receiver class that fills the Nationality Popups Pulldown with the available nationalities.
	 * Should be used in request for nationality. 
	 */
	private class NationalityCriteriaReceiver extends Receiver<List<NationalityProxy>> {
		@Override
		public void onSuccess(List<NationalityProxy> response) {
			if (nationalityPopup == null) {
				return;
			}
			List<NationalityProxy> values = new ArrayList<NationalityProxy>();
			values.addAll(response);
			if (values.size() > 0 ) {
				nationalityPopup.getNationalityBox().setValue(values.get(0));
			}
			nationalityPopup.getNationalityBox().setAcceptableValues(values);
			
		}
		
	}
	
	/**
	 * Receiver class that fills the language popups' pulldown with the available languages.
	 * Should be used in request for languages. 
	 */
	private class LanguageCriteriaReceiver extends Receiver<List<SpokenLanguageProxy>> {
		@Override
		public void onSuccess(List<SpokenLanguageProxy> response) {
			if (languagePopup == null) {
				return;
			}
			List<SpokenLanguageProxy> values = new ArrayList<SpokenLanguageProxy>();
			values.addAll(response);
			if (values.size() > 0 ) {
				languagePopup.getLanguageBox().setValue(values.get(0));
			}
			languagePopup.getLanguageBox().setAcceptableValues(values);
		}
	}
	
	/**
	 * Receiver class that fills the Scar Popups Pulldown with the available scars.
	 * Should be used in request for scars. 
	 */
	private class ScarCriteriaReceiver extends Receiver<List<ScarProxy>> {
		@Override
		public void onSuccess(List<ScarProxy> response) {
			if (scarPopup == null) {
				return;
			}

			List<ScarProxy> values = new ArrayList<ScarProxy>();
			values.addAll(response);
			if (values.size() > 0 ) {
				scarPopup.getScarBox().setValue(values.get(0));
			}
			scarPopup.getScarBox().setAcceptableValues(values);
		}
	}
	
	//TODO: @@@SPEC at the moment all values are loaded, I saw a loading on demand in your presentation, would be probably nice to implement.

	/**
	 * Receiver class that fills the anamnesis criteria popups' suggest box with possible
	 * values. Should be used in request for anamnesis values. 
	 */
	private class AnamnesisCriteriaReceiver extends Receiver<List<AnamnesisCheckProxy>> {
		public void onSuccess(List<AnamnesisCheckProxy> response) {
			if (anamnesisPopup == null) {
				return;
			}
			((ProxySuggestOracle<AnamnesisCheckProxy>) anamnesisPopup.getAnamnesisQuestionSuggestBox().getSuggestOracle()).addAll(response);
		}
	}

	/**
	 * Adds a given scar criterion to the criteria table by reformating the given data and
	 * calling addAdvSeaBasicButtonClicked()
	 * @param scarProxy the scarProxy that is relevant
	 * @param bindType the and/or conjunction for this criterion
	 * @param comparison the type of comparison that should apply (EQUALS (== "has scar") or NOT_EQUALS (== "doesn't have scar"))
	 */
	@Override
	public void addScarButtonClicked(ScarProxy scarProxy, BindType bindType, Comparison comparison) {
		Log.info("ScarType:" + scarProxy.getTraitType().toString() + ": " + scarProxy.getBodypart());
		addAdvSeaBasicButtonClicked(scarProxy.getId(), scarProxy.getTraitType().toString() + ": " + scarProxy.getBodypart(), bindType, PossibleFields.SCAR, comparison);
	}

	/**
	 * Adds a given anamnesis criterion to the criteria table by reformating the given data and
	 * calling addAdvSeaBasicButtonClicked()
	 * @param anamnesisCheck the AnamnesisCheckProxy that is relevant (contains question & possible answers)
	 * @param answer the given answer to the question.
	 * @param bindType the and/or conjunction for this criterion
	 * @param comparison the type of comparison that should apply (EQUALS (== "answered with") or NOT_EQUALS (== "didn't answer with"))
	 */
	@Override
	public void addAnamnesisValueButtonClicked(AnamnesisCheckProxy anamnesisCheck, String answer, BindType bindType, Comparison comparison) {
		// TODO Auto-generated method stub
		Log.info("Question:" + anamnesisCheck.getText() + "; options:" + anamnesisCheck.getValue() + "; answer: " + answer);
		addAdvSeaBasicButtonClicked(anamnesisCheck.getId(), anamnesisCheck.getType() + ": " + answer+":"+anamnesisCheck.getValue(), bindType, PossibleFields.ANAMNESIS, comparison);
	}
	
	//TODO: @@@SPEC implement nationality search
	
	@Override
	public void addNationalityButtonClicked(NationalityProxy nationality,
			BindType bindType, Comparison comparison) {
		// TODO implement.
	}

	/**
	 * Adds a given language criterion to the criteria table by reformating the given data and
	 * calling addAdvSeaBasicButtonClicked()
	 * @param languageProxy the SpokenLanguageProxy that is relevant (contains the relevant language)
	 * @param skill The LangSkillLevel that will be compared to
	 * @param bindType the and/or conjunction for this criterion
	 * @param comparison the type of comparison that should apply 
	 * (EQUALS (== "speaks on level") or NOT_EQUALS (== "doesn't speak on level"), GREATER("speaks better than") SMALLER ("speaks worse than")
	 */
	@Override
	public void addLanguageButtonClicked(SpokenLanguageProxy languageProxy, LangSkillLevel skill, BindType bindType, Comparison comparison) {
		addAdvSeaBasicButtonClicked(languageProxy.getId(), languageProxy.getLanguageName() + ": " + skill.toString(), bindType, PossibleFields.LANGUAGE, comparison);
	}


}
