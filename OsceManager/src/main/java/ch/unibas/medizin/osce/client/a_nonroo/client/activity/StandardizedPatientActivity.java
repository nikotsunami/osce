package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchLanguagePopup;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchLanguagePopup.Delegate;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchLanguagePopupImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandartizedPatientAdvancedSearchBasicCriteriaPopUp;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandartizedPatientAdvancedSearchBasicCriteriaPopUpImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandartizedPatientAdvancedSearchSubView;
import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;

import ch.unibas.medizin.osce.client.a_nonroo.client.SearchCriteria;
import ch.unibas.medizin.osce.domain.AdvancedSearchCriteria;
import ch.unibas.medizin.osce.shared.AdvancesSearchCriteriumOld;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison2;
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

public class StandardizedPatientActivity extends AbstractActivity implements
	StandardizedPatientView.Presenter, 
	StandardizedPatientView.Delegate, 
	StandartizedPatientAdvancedSearchSubView.Delegate, 
	StandartizedPatientAdvancedSearchBasicCriteriaPopUp.Delegate, 
	StandardizedPatientAdvancedSearchLanguagePopup.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private StandardizedPatientView view;
	private CellTable<StandardizedPatientProxy> table;
	private SingleSelectionModel<StandardizedPatientProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	private ActivityManager activityManger;
	private StandardizedPatientDetailsActivityMapper StandardizedPatientDetailsActivityMapper;
	private StandartizedPatientAdvancedSearchSubView standartizedPatientAdvancedSearchSubView;
	private CellTable<AdvancedSearchCriteriaProxy> criteriaTable;


	public StandardizedPatientActivity(OsMaRequestFactory requests, PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;
		StandardizedPatientDetailsActivityMapper = new StandardizedPatientDetailsActivityMapper(requests, placeController);
		this.activityManger = new ActivityManager(StandardizedPatientDetailsActivityMapper, requests.getEventBus());
	}

	public void onStop(){
		activityManger.setDisplay(null);
	}
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		StandardizedPatientView systemStartView = new StandardizedPatientViewImpl();
		systemStartView.setPresenter(this);
		

		
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		setTable(view.getTable());
		
		standartizedPatientAdvancedSearchSubView = view.getStandartizedPatientAdvancedSearchSubViewImpl();
		standartizedPatientAdvancedSearchSubView.setDelegate(this);
		
		criteriaTable = standartizedPatientAdvancedSearchSubView.getTable();

		eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {

				if (event.getNewPlace() instanceof StandardizedPatientDetailsPlace){
					init();
				}
			}
		});
		
		requestAdvSeaCritStd = requests.standardizedPatientRequestNonRoo();

		init();

		activityManger.setDisplay(view.getDetailsPanel());

		// Inherit the view's key provider
		ProvidesKey<StandardizedPatientProxy> keyProvider = ((AbstractHasData<StandardizedPatientProxy>) table)
				.getKeyProvider();
		selectionModel = new SingleSelectionModel<StandardizedPatientProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel
		.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				StandardizedPatientProxy selectedObject = selectionModel
						.getSelectedObject();
				if (selectedObject != null) {
					Log.debug(selectedObject.getName()
							+ " selected!");
					showDetails(selectedObject);
				}
			}
		});

		view.setDelegate(this);

	}
	
	private void init() {
		init2("");
	}

	private void init2(final String q) {
		
		// (1) Text search
		
		List<String> searchThrough = view.getSearchFilters();
		
		// (2) Advanced search
		
		fireCountRequest(
				q, 
				searchThrough, 
				view.getCriteria().getFields(), 
				view.getCriteria().getComparisons(), 
				view.getCriteria().getValues(),
				new Receiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Patienten aus der Datenbank: " + response);
				view.getTable().setRowCount(response.intValue(), true);

				onRangeChanged(q);
			}

		});
		
		rangeChangeHandler = table
				.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {
						StandardizedPatientActivity.this.onRangeChanged(q);
					}
				});
		table.addColumnSortHandler(new ColumnSortEvent.Handler() {

			@Override
			public void onColumnSort(ColumnSortEvent event) {
				StandardizedPatientActivity.this.onRangeChanged(q);
				
			}
		});
	}

	protected void showDetails(StandardizedPatientProxy StandardizedPatient) {

		Log.debug(StandardizedPatient.getName());

		goTo(new StandardizedPatientDetailsPlace(StandardizedPatient.stableId(),
				StandardizedPatientDetailsPlace.Operation.DETAILS));
	}
	
	private List<AdvancedSearchCriteriaProxy> searchCriteria = new ArrayList<AdvancedSearchCriteriaProxy>();

	@SuppressWarnings({ "deprecation"})
	protected void onRangeChanged(String q) {
		final Range range = table.getVisibleRange();
		
		
		//TODO: ###david### test code
		
				List<String> fields =  Arrays.asList("weight", "height", "bmi");
				List<String> values =  Arrays.asList("80", "180", "30");
				//List<String> comparations =  Arrays.asList(Comparison2.EQUALS, Comparison2.LESS, Comparison2.MORE);
				//List<String> bindType =  Arrays.asList(BindType.AND, BindType.AND, BindType.AND);
				List<String> searchThrough  =  Arrays.asList("name", "pre_name", "comment");
				
				
				
				requestAdvSeaCritStd = requests.standardizedPatientRequestNonRoo();
//				
//				AdvancedSearchCriteriaProxy criteria = requestAdvSeaCritStd.create(AdvancedSearchCriteriaProxy.class);
//				requestAdvSeaCritStd.edit(criteria);
//				criteria.setBindType(BindType.AND);
//				criteria.setComparation(Comparison2.EQUALS);
//				criteria.setField(PossibleFields.weight);
//				criteria.setValue("80");
//				
//				searchCriteria.add(criteria);
//				
//				criteriaTable.setRowData(searchCriteria);
				/*searchCriteria.add(new AdvancesSearchCriteriumOld (PossibleFields.weight, BindType.AND,
						Comparison2.EQUALS, "80"));
				searchCriteria.add(new AdvancesSearchCriteriumOld (PossibleFields.height, BindType.OR,
						Comparison2.LESS, "180"));
				searchCriteria.add(new AdvancesSearchCriteriumOld (PossibleFields.bmi, BindType.AND,
						Comparison2.MORE, "30"));*/
				

				
				
		requestAdvSeaCritStd.countPatientsByAdvancedSearchAndSort("name",
				Sorting.ASC, q, searchThrough, searchCriteria /*
															 * fields, bindType,
															 * comparations,
															 * values
															 */).fire(
				new Receiver<Long>() {
					public void onFailure(ServerFailure error) {
						Log.error(error.getMessage());
						// onStop();
					}

					public void onViolation(Set<Violation> errors) {
						Iterator<Violation> iter = errors.iterator();
						String message = "";
						while (iter.hasNext()) {
							message += iter.next().getMessage() + "<br>";
						}
						Log.warn(" in Simpat -" + message);
						// onStop();
					}

					@Override
					public void onSuccess(Long response) {
						// table.setRowData(range.getStart(), values);

					}
				});
		
		// (1) Sorting

		Boolean asc = true;
		String sortField = "name"; // TODO: handle sort change events

		if (table.getColumnSortList().size() > 0) {

			asc = table.getColumnSortList().get(0).isAscending();

		}

		// (2) Text search

		/* List<String> */searchThrough = view.getSearchFilters();

		// (3) Advanced search

		final Receiver<List<StandardizedPatientProxy>> callback = new Receiver<List<StandardizedPatientProxy>>() {
			@Override
			public void onSuccess(List<StandardizedPatientProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
				table.setRowData(range.getStart(), values);

				// finishPendingSelection();
				if (widget != null) {
					widget.setWidget(view.asWidget());
				}
			}
		};

		fireRangeRequest(sortField, 
				asc, q, 
				new Integer(range.getStart()),
				new Integer(range.getLength()),
				searchThrough,
				view.getCriteria().getFields(), 
				view.getCriteria().getComparisons(), 
				view.getCriteria().getValues(),
				callback);
	}

	private void fireRangeRequest(String sortField, Boolean asc, String q, Integer firstResult, Integer maxResults, List<String> searchThrough, List<String> fields, List<Integer> comparisons, List<String> values, final Receiver<List<StandardizedPatientProxy>> callback) {
		createRangeRequest(sortField, asc, q, firstResult, maxResults, searchThrough, fields, comparisons, values).with(view.getPaths()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}
	
	protected Request<List<StandardizedPatientProxy>> createRangeRequest(String sortField, Boolean asc, String q, Integer firstResult, Integer maxResults, List<String> searchThrough, List<String> fields, List<Integer> comparisons, List<String> values) {
		return requests.standardizedPatientRequestNonRoo().findPatientsBySearchAndSort(sortField, asc, q, firstResult, maxResults, searchThrough, fields, comparisons, values);
		//return requests.standardizedPatientRequestNonRoo().findPatientsBySearch(q, firstResult, maxResults);
	}

	protected void fireCountRequest(String q, List<String> searchThrough, List<String> fields, List<Integer> comparisons, List<String> values, Receiver<Long> callback) {
		requests.standardizedPatientRequestNonRoo().countPatientsBySearchAndSort(q, searchThrough, fields, comparisons, values).fire(callback);
		//requests.standardizedPatientRequestNonRoo().countPatientsBySearch(q).fire(callback);
	}

	private void setTable(CellTable<StandardizedPatientProxy> table) {
		this.table = table;
	}

	@Override
	public void newClicked() {
		Log.info("create clicked");
		placeController.goTo(new StandardizedPatientDetailsPlace(StandardizedPatientDetailsPlace.Operation.CREATE));
	}
	
	@Override
	public void performSearch(String q) {
		Log.debug("Search for " + q);
		init2(q);
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public void filterTableClicked() {
		// TODO Auto-generated method stub
		
	}

	StandartizedPatientAdvancedSearchBasicCriteriaPopUp basicCriteriaPopUp; 
	
	@Override
	public void addBasicCriteriaClicked(Button addBasicData) {
		if (basicCriteriaPopUp != null && basicCriteriaPopUp.isShowing()) {
			basicCriteriaPopUp.hide();
			return;
		}
		basicCriteriaPopUp = new StandartizedPatientAdvancedSearchBasicCriteriaPopUpImpl();
		basicCriteriaPopUp.setDelegate(this);
		basicCriteriaPopUp.display(addBasicData);
	}

	@Override
	public void addScarCriteriaClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAnamnesisCriteriaClicked() {
		// TODO Auto-generated method stub
		
	}

	StandardizedPatientAdvancedSearchLanguagePopup languagePopup;
	
	@Override
	public void addLanguageCriteriaClicked(Button addLanguageButton) {
		initLanguageCriteriaSubView();
		
		if (languagePopup != null && languagePopup.isShowing()) {
			languagePopup.hide();
			return;
		}
		languagePopup = new StandardizedPatientAdvancedSearchLanguagePopupImpl();
		languagePopup.setDelegate(this);
		languagePopup.display(addLanguageButton);
	}

	@Override
	public void addBasicCriteriaPopUpClicked() {
		// TODO Auto-generated method stub
		
	}
	private StandardizedPatientRequestNonRoo requestAdvSeaCritStd;

	@Override
	public void addAdvSeaBasicButtonClicked(String string, BindType bindType, PossibleFields possibleFields, Comparison2 comparition) {
		
		requestAdvSeaCritStd = requests.standardizedPatientRequestNonRoo();
		
		AdvancedSearchCriteriaProxy criteria = requestAdvSeaCritStd.create(AdvancedSearchCriteriaProxy.class);
		criteria = requestAdvSeaCritStd.edit(criteria);
		criteria.setBindType(bindType);
		criteria.setComparation(comparition);
		criteria.setField(possibleFields);
		criteria.setValue(string);
		requestAdvSeaCritStd.fire();
		searchCriteria.add(criteria);
		
		criteriaTable.setRowData(searchCriteria);
	}
	
	@Override
	public void addLanguageButtonClicked(String language, String skill) {
		
	}
	
	private void initLanguageCriteriaSubView() {
//		requests.languageRequestNonRoo().countLanguagesByName("").fire(new Receiver<Long>() {
//			@Override
//			public void onSuccess(Long response) {
//				if (languagePopup == null) {
//					return;
//				}
//				Log.debug("Geholte Sprachen aus der Datenbank: " + response);
////				response.
////				languagePopup.getTable().setRowCount(response.intValue(), true);
//
////				onRangeChanged(q);
//			}
//		});
	}
}
