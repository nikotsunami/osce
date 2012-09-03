package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaHeaderLogic;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.ScarProxyRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchAnamnesisPopup;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchAnamnesisPopupImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchLanguagePopup;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchLanguagePopupImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchMaritialStatusPopupView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchMaritialStatusPopupViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchNationalityPopup;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchNationalityPopupImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchPopup;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchProfessionPopup;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchProfessionPopupImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchScarPopup;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchScarPopupImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchWorkPermissionPopup;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchWorkPermissionPopupImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandartizedPatientAdvancedSearchBasicCriteriaPopUp;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandartizedPatientAdvancedSearchBasicCriteriaPopUpImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandartizedPatientAdvancedSearchSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeHandler;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.managed.request.ProfessionProxy;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.ProxySuggestOracle;

import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison;
import ch.unibas.medizin.osce.shared.LangSkillLevel;
import ch.unibas.medizin.osce.shared.MaritalStatus;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.PossibleFields;
import ch.unibas.medizin.osce.shared.Sorting;
import ch.unibas.medizin.osce.shared.WorkPermission;
import ch.unibas.medizin.osce.shared.scaffold.StandardizedPatientRequestNonRoo;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.requestfactory.shared.Violation;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.Window;
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
		StandardizedPatientAdvancedSearchAnamnesisPopup.Delegate, StandardizedPatientAdvancedSearchNationalityPopup.Delegate,
		//issue 
		StandardizedPatientAdvancedSearchProfessionPopup.Delegate, StandardizedPatientAdvancedSearchWorkPermissionPopup.Delegate,
		StandardizedPatientAdvancedSearchMaritialStatusPopupView.Delegate{

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
	
	//issue
	private StandardizedPatientAdvancedSearchProfessionPopup professionPopup;
	private StandardizedPatientAdvancedSearchWorkPermissionPopup workPermissionPopup;
	private StandardizedPatientAdvancedSearchMaritialStatusPopupView maritialStausPopup;
	
	// BY SPEC v(Start)

	/** Holds a reference to the IconButton of StandardizedPatientViewImpl */
	private IconButton iconButton;
	private HandlerRegistration placeChangeHandlerRegistration;

	// private final String filePath = "StandardizedPatientList.csv";

	// BY SPEC v(End)

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
		if (placeChangeHandlerRegistration != null) {
			placeChangeHandlerRegistration.removeHandler();
		}
		activityManger.setDisplay(null);
	}
	
	public void registerLoading() {
		ApplicationLoadingScreenEvent.register(requests.getEventBus(),
				new ApplicationLoadingScreenHandler() {
					@Override
					public void onEventReceived(
							ApplicationLoadingScreenEvent event) {
						Log.info(" ApplicationLoadingScreenEvent onEventReceived Called");
					event.display();
					}
				});
	}

	// By spec V(Start)
	/**
	 * Receiver for the csv format file creation of standardized patients that
	 * met the search criteria. If execution was successful, the file will be
	 * created.
	 */
	@SuppressWarnings("deprecation")
	private class StandardizedPatientCsvFileReceiver extends OSCEReceiver<String> {
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

	// By spec V(Stop)

	/**
	 * Initializes the corresponding views and initializes the tables as well as their
	 * corresponding handlers.
	 */
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		
//		requests.osceRequestNonRoo().generateAssignments(1l).fire(new Receiver<Boolean>() {
//
//			@Override
//			public void onSuccess(Boolean response) {
//				// TODO Auto-generated method stub
////				requests.osceRequestNonRoo().generateAssignments(1l).fire();
//			}
//			
//		});
		
		//By SPEC[Start 
		//StandardizedPatientView systemStartView = new StandardizedPatientViewImpl();
		final StandardizedPatientView systemStartView = new StandardizedPatientViewImpl();
		//By SPEC] End
		systemStartView.setPresenter(this);

		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		
		//by spec
		RecordChangeEvent.register(requests.getEventBus(), (StandardizedPatientViewImpl)view);
		//by spec
		this.table = view.getTable();

		standartizedPatientAdvancedSearchSubView = view.getStandartizedPatientAdvancedSearchSubViewImpl();
		standartizedPatientAdvancedSearchSubView.setDelegate(this);

		// BY SPEC v(Start)
		this.iconButton = this.view.getExportButton();

		this.iconButton.addClickHandler(new ClickHandler() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(ClickEvent arg0) {
				Range range = table.getVisibleRange();
				requests.standardizedPatientRequestNonRoo()
						.getCSVMapperFindPatientsByAdvancedSearchAndSort(
								"name", Sorting.ASC, quickSearchTerm,
								searchThrough, searchCriteria // , filePath
								,range.getStart(),range.getLength()								
						).fire(new StandardizedPatientCsvFileReceiver());

			}
		});

		// BY SPEC v(Stop)
		criteriaTable = standartizedPatientAdvancedSearchSubView.getTable();
		
		table.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				StandardizedPatientActivity.this.onRangeChanged();
			}
		});
		
		table.addColumnSortHandler(new ColumnSortEvent.Handler() {
			@Override
			public void onColumnSort(ColumnSortEvent event) {
				//By SPEC[Start
				Column<StandardizedPatientProxy,String> col = (Column<StandardizedPatientProxy,String>) event.getColumn();
				int index = table.getColumnIndex(col); 
				String[] path =	systemStartView.getPaths();	            			
				sortname = path[index];
				sortorder=(event.isSortAscending())?Sorting.ASC:Sorting.DESC;				
				//By SPEC]end
				StandardizedPatientActivity.this.onRangeChanged();
			}
		});
		
		PlaceChangeEvent.Handler placeChangeHandler = new PlaceChangeEvent.Handler() {
			@Override
			public void onPlaceChange(PlaceChangeEvent event) {
				//requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
				Log.debug("PlaceChangeEvent: " + event.getNewPlace().toString());
				if (event.getNewPlace() instanceof StandardizedPatientDetailsPlace) {
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
					view.setDetailPanel(true);
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					StandardizedPatientDetailsPlace spdPlace = (StandardizedPatientDetailsPlace) event.getNewPlace();
					Operation op = spdPlace.getOperation();
					if (op == Operation.NEW) {
						getSearchStringByEntityProxyId((EntityProxyId<StandardizedPatientProxy>)spdPlace.getProxyId());
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					}
				} else if (event.getNewPlace() instanceof StandardizedPatientPlace) {
					view.setDetailPanel(false);
					//requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					StandardizedPatientPlace place = (StandardizedPatientPlace) event.getNewPlace();
					if (place.getToken().contains("DELETED")) {
						//requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
						initSearch();
					}
				}
			}
		};
		placeChangeHandlerRegistration = eventBus.addHandler(PlaceChangeEvent.TYPE, placeChangeHandler);
		
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
	private class StandardizedPatientCountReceiver extends OSCEReceiver<Long> {
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
	private class StandardizedPatientReceiver extends OSCEReceiver<List<StandardizedPatientProxy>> {
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
			// NOTE: If you set the Row count to the actually received responses,
			// it is impossible to access anymore than the displayed number of
			// patients! Therefore do not set the row count here! This is the
			// reason we have a "count request"!
			table.setRowData(range.getStart(), response);
			
		}
	}
	 
	 /**
	  * Used to fill table and search field after creating new entity.
	  * @param entityId
	  */
	 private void getSearchStringByEntityProxyId(EntityProxyId<StandardizedPatientProxy> entityId) {
		 requests.find(entityId).with("name", "preName").fire(new Receiver<StandardizedPatientProxy>() {

			@Override
			public void onSuccess(StandardizedPatientProxy proxy) {
				if (proxy != null) {
					List<StandardizedPatientProxy> values = new ArrayList<StandardizedPatientProxy>();
					values.add(proxy);
					view.getSearchBox().setText(proxy.getPreName() + " " + proxy.getName());
					table.setRowCount(1, true);
					table.setRowData(0, values);
				}
			}
			 
		 });
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
	
	//By SPEC[Start
	public Sorting sortorder = Sorting.ASC;
	public String sortname = "name";
	//By SPEC]end
	/**
	 * Executes the search for a standardized patient based on 
	 * advanced search criteria, quick search term and quick search
	 * filters. Control is handed to  
	 */
	@SuppressWarnings({ "deprecation" })
	protected void onRangeChanged() {
		 // TODO: some bug about request
		 requestAdvSeaCritStd = requests.standardizedPatientRequestNonRoo();
		
		for (AdvancedSearchCriteriaProxy criterion : searchCriteria) {
			Log.info("Criterion: " + criterion.getField().toString() + ": " + criterion.getValue());
		}
		
		Range range = table.getVisibleRange();

		//By SPEC[Start		
		//requestAdvSeaCritStd.findPatientsByAdvancedSearchAndSort("name", Sorting.ASC, quickSearchTerm, 
				//searchThrough, searchCriteria /*fields, bindType, comparations, values */).
				//fire(new StandardizedPatientReceiver());
		
		requestAdvSeaCritStd.findPatientsByAdvancedSearchAndSort(sortname, sortorder , quickSearchTerm, 
				searchThrough, searchCriteria, range.getStart(), range.getLength() /*fields, bindType, comparations, values */).
			   fire(new StandardizedPatientReceiver());
		//By SPEC]End
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
	 * @param value
	 * @param bindType wether the criteria to add should be used with an and or or conjunction
	 * @param possibleFields 
	 * @param comparison Which type of comparison applies to the criterion (equals, greater, smaller, not equals)
	 */
	@Override
	public void addAdvSeaBasicButtonClicked(Long objectId, String value, String shownValue, BindType bindType, PossibleFields possibleFields, Comparison comparison) {
		switch (possibleFields) {
		case BMI:
			shownValue = constants.bmi() + " "
					+ new EnumRenderer<Comparison>(EnumRenderer.Type.NUMERIC).render(comparison) + " " 
					+ value;
			break;
		case HEIGHT:
			shownValue = constants.height() + " "
					+ new EnumRenderer<Comparison>(EnumRenderer.Type.NUMERIC).render(comparison) + " " 
					+ value + "cm";
			break;
		case WEIGHT:
			shownValue = constants.weight() + " "
					+ new EnumRenderer<Comparison>(EnumRenderer.Type.NUMERIC).render(comparison) + " " 
					+ value + "kg";
			break;
		case AGE:
			shownValue = constants.age() + " " + new EnumRenderer<Comparison>(EnumRenderer.Type.NUMERIC).render(comparison) + " " 
					+ value + "years";
			break;
		case GENDER:
			shownValue = constants.gender() + " " + new EnumRenderer<Comparison>(EnumRenderer.Type.NUMERIC).render(comparison) + " " 
					+ value;
			break;
			
		}
		StandardizedPatientRequestNonRoo req = requests.standardizedPatientRequestNonRoo();
		AdvancedSearchCriteriaProxy criteria = req.create(AdvancedSearchCriteriaProxy.class);
		criteria = req.edit(criteria);
		criteria.setBindType(bindType);
		criteria.setComparation(comparison);
		criteria.setField(possibleFields);
		criteria.setValue(value);
		criteria.setObjectId(objectId);
		criteria.setShownValue(shownValue);
		req.fire();
		
		Log.debug("Added criterion: value = " + value);
		
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
		criteriaTable.setRowData(searchCriteria);
		//By SPEC[
		initSearch();
		//By SPEC]
	}
	
	/**
	 * Receiver class that fills the Nationality Popups Pulldown with the available nationalities.
	 * Should be used in request for nationality. 
	 */
	private class NationalityCriteriaReceiver extends OSCEReceiver<List<NationalityProxy>> {
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
	private class LanguageCriteriaReceiver extends OSCEReceiver<List<SpokenLanguageProxy>> {
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
	private class ScarCriteriaReceiver extends OSCEReceiver<List<ScarProxy>> {
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
	private class AnamnesisCriteriaReceiver extends OSCEReceiver<List<AnamnesisCheckProxy>> {
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
		String displayValue = new EnumRenderer<Comparison>(EnumRenderer.Type.SCAR).render(comparison) + " " 
				+ new ScarProxyRenderer().render(scarProxy);
		String value = scarProxy.getTraitType().toString() + ":" + scarProxy.getBodypart();
		addAdvSeaBasicButtonClicked(scarProxy.getId(), value, displayValue, bindType, PossibleFields.SCAR, comparison);
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
		Log.info("Question:" + anamnesisCheck.getText() + "; options:" + anamnesisCheck.getValue() + "; answer: " + answer);
		String displayValue = "\"" + anamnesisCheck.getText() + "\" "
				+ new EnumRenderer<Comparison>(EnumRenderer.Type.ANAMNESIS).render(comparison) + " "
				+ humanReadableAnamnesisAnswer(anamnesisCheck, answer);
		addAdvSeaBasicButtonClicked(anamnesisCheck.getId(), answer, displayValue, bindType, PossibleFields.ANAMNESIS, comparison);
	}
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
	
	private String humanReadableAnamnesisAnswer(AnamnesisCheckProxy proxy, String answer) {
		switch(proxy.getType()) {
		case QUESTION_OPEN:
			return answer;
		case QUESTION_YES_NO:
			if ("1".equals(answer))
				return constants.yes();
			return constants.no();
		case QUESTION_MULT_M:
		case QUESTION_MULT_S:
			String[] answerTokens = answer.split("-");
			String[] questionTokens = proxy.getValue().split("\\|");
			for (int i=0; i < answerTokens.length; i++) {
				if (answerTokens[i].equals("1"))
					return questionTokens[i];
			}
		}
		return "";
	}
	
	@Override
	public void addNationalityButtonClicked(NationalityProxy nationality, BindType bindType, Comparison comparison) {
		String displayValue = new EnumRenderer<Comparison>(EnumRenderer.Type.NATIONALITY).render(comparison) + " " 
				+ nationality.getNationality();
		addAdvSeaBasicButtonClicked(nationality.getId(), nationality.getNationality(), displayValue, bindType, PossibleFields.NATIONALITY, comparison);
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
		String displayValue = constants.patientSpeaks() + " "
				+ languageProxy.getLanguageName() + " "
				+ new EnumRenderer<Comparison>(EnumRenderer.Type.LANGSKILL).render(comparison) + " "
				+ new EnumRenderer<LangSkillLevel>().render(skill);
		String value = skill.toString();
		addAdvSeaBasicButtonClicked(languageProxy.getId(), value, displayValue, bindType, PossibleFields.LANGUAGE, comparison);
	}

	@Override
	public void addPorfessionClicked(IconButton parentButton) {
		requests.professionRequest().findAllProfessions().fire(new OSCEReceiver<List<ProfessionProxy>>() {

			@Override
			public void onSuccess(List<ProfessionProxy> response) {
				if (professionPopup == null) {
					return;
				}
				List<ProfessionProxy> values = new ArrayList<ProfessionProxy>();
				values.addAll(response);
				if (values.size() > 0 ) {
					professionPopup.getProfessionBox().setValue(values.get(0));
				}
				professionPopup.getProfessionBox().setAcceptableValues(values);
			}
		});

		if (advancedSearchPopup != null && advancedSearchPopup.isShowing()) {
			advancedSearchPopup.hide();
			if (advancedSearchPopup == professionPopup) {
				return;
			}
		}
		professionPopup = new StandardizedPatientAdvancedSearchProfessionPopupImpl();
		professionPopup.setDelegate(this);
		professionPopup.display(parentButton);
		advancedSearchPopup = professionPopup;
		
	}

	@Override
	public void addWorkPermissionClicked(IconButton parentButton) {
		if (advancedSearchPopup != null && advancedSearchPopup.isShowing()) {
			advancedSearchPopup.hide();
			if (advancedSearchPopup == workPermissionPopup) {
				return;
			}
		}
		workPermissionPopup = new StandardizedPatientAdvancedSearchWorkPermissionPopupImpl();
		workPermissionPopup.setDelegate(this);
		workPermissionPopup.display(parentButton);
		advancedSearchPopup = workPermissionPopup;		
	}

	@Override
	public void addMaritialStatusClicked(IconButton parentButton) {
		if (advancedSearchPopup != null && advancedSearchPopup.isShowing()) {
			advancedSearchPopup.hide();
			if (advancedSearchPopup == maritialStausPopup) {
				return;
			}
		}
		maritialStausPopup = new StandardizedPatientAdvancedSearchMaritialStatusPopupViewImpl();
		maritialStausPopup.setDelegate(this);
		maritialStausPopup.display(parentButton);
		advancedSearchPopup = maritialStausPopup;		
	}

	@Override
	public void addProfessionButtonClicked(ProfessionProxy profession,
			BindType bindType, Comparison comparison) {
		String displayValue = new EnumRenderer<Comparison>(EnumRenderer.Type.PROFESSION).render(comparison) + " " 
				+ profession.getProfession();
		addAdvSeaBasicButtonClicked(profession.getId(), profession.getProfession(), displayValue, bindType, PossibleFields.PROFESSION, comparison);
	}

	@Override
	public void addWokPermissionButtonClicked(WorkPermission workpermission,
			BindType bindType, Comparison comparison) {
		String displayValue = new EnumRenderer<Comparison>(EnumRenderer.Type.WORKPERMISSION).render(comparison) + " " + workpermission + " WorkPermission";
		addAdvSeaBasicButtonClicked(null, workpermission.toString(), displayValue, bindType, PossibleFields.WORKPERMISSION, comparison);
	}

	@Override
	public void addMaritialStatusButtonClicked(MaritalStatus maritialStatus,
			BindType bindType, Comparison comparison) {
		String displayValue = new EnumRenderer<Comparison>(EnumRenderer.Type.MARITIALSTATUS).render(comparison) + " " + maritialStatus; 
		addAdvSeaBasicButtonClicked(null, maritialStatus.toString(), displayValue, bindType, PossibleFields.MARITIALSTATUS, comparison);
	}

}
