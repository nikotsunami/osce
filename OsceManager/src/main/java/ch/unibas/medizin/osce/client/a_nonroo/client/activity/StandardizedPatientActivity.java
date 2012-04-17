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
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchScarPopup.Delegate;
import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.LangSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.ProxySuggestOracle;

import ch.unibas.medizin.osce.client.a_nonroo.client.SearchCriteria;
import ch.unibas.medizin.osce.domain.AdvancedSearchCriteria;
import ch.unibas.medizin.osce.shared.AdvancesSearchCriteriumOld;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison2;
import ch.unibas.medizin.osce.shared.LangSkillLevel;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.PossibleFields;
import ch.unibas.medizin.osce.shared.Sorting;
import ch.unibas.medizin.osce.shared.TraitTypes;
import ch.unibas.medizin.osce.shared.scaffold.StandardizedPatientRequestNonRoo;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
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

public class StandardizedPatientActivity extends AbstractActivity implements StandardizedPatientView.Presenter, StandardizedPatientView.Delegate,
		StandartizedPatientAdvancedSearchSubView.Delegate, StandartizedPatientAdvancedSearchBasicCriteriaPopUp.Delegate,
		StandardizedPatientAdvancedSearchLanguagePopup.Delegate, StandardizedPatientAdvancedSearchScarPopup.Delegate,
		StandardizedPatientAdvancedSearchAnamnesisPopup.Delegate, StandardizedPatientAdvancedSearchNationalityPopup.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private StandardizedPatientView view;
	private CellTable<StandardizedPatientProxy> table;
	private SingleSelectionModel<StandardizedPatientProxy> selectionModel;
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

	public void onStop() {
		if (advancedSearchPopup != null) {
			advancedSearchPopup.hide();
		}
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

				if (event.getNewPlace() instanceof StandardizedPatientDetailsPlace) {
					init();
				}
			}
		});

		requestAdvSeaCritStd = requests.standardizedPatientRequestNonRoo();

		init();

		activityManger.setDisplay(view.getDetailsPanel());

		// Inherit the view's key provider
		ProvidesKey<StandardizedPatientProxy> keyProvider = ((AbstractHasData<StandardizedPatientProxy>) table).getKeyProvider();
		selectionModel = new SingleSelectionModel<StandardizedPatientProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

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

	private void init() {
		init2("");
	}

	private void init2(final String q) {
		// (1) Text search
		List<String> searchThrough = view.getSearchFilters();

		// (2) Advanced search
		fireCountRequest(q, searchThrough, view.getCriteria().getFields(), view.getCriteria().getComparisons(), view.getCriteria().getValues(),
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

		table.addRangeChangeHandler(new RangeChangeEvent.Handler() {
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

		goTo(new StandardizedPatientDetailsPlace(StandardizedPatient.stableId(), Operation.DETAILS));
	}

	private List<AdvancedSearchCriteriaProxy> searchCriteria = new ArrayList<AdvancedSearchCriteriaProxy>();

	@SuppressWarnings({ "deprecation" })
	protected void onRangeChanged(String q) {
		final Range range = table.getVisibleRange();

		// TODO: ###david### test code

		List<String> fields = Arrays.asList("weight", "height", "bmi");
		List<String> values = Arrays.asList("80", "180", "30");
		// List<String> comparations = Arrays.asList(Comparison2.EQUALS,
		// Comparison2.LESS, Comparison2.MORE);
		// List<String> bindType = Arrays.asList(BindType.AND, BindType.AND,
		// BindType.AND);
		List<String> searchThrough = new ArrayList<String>();//Arrays.asList("name", "preName", "comment", "BIC", "IBAN", "bankName");

		requestAdvSeaCritStd = requests.standardizedPatientRequestNonRoo();
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
//		searchCriteria.clear();
//		 AdvancedSearchCriteriaProxy criteria2 =
//		 requestAdvSeaCritStd.create(AdvancedSearchCriteriaProxy.class);
//		 requestAdvSeaCritStd.edit(criteria2);
//		 criteria2.setBindType(BindType.AND);
//		 criteria2.setComparation(Comparison2.EQUALS);
//		 criteria2.setField(PossibleFields.LANGUAGE);
//		 //"Deutsch: A1"
//		 criteria2.setValue("Deutsch: NATIVE_SPEAKER");
//		 criteria2.setObjectId(new Long(6));
//		 searchCriteria.add(criteria2);

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

		requestAdvSeaCritStd.findPatientsByAdvancedSearchAndSort("name", Sorting.ASC, q, 
				searchThrough, searchCriteria /*fields, bindType, comparations, values */).
				fire(new Receiver<List<StandardizedPatientProxy>>() {
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
				table.setRowData(range.getStart(), response);

			}
		});

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

	private void fireRangeRequest(String sortField, Boolean asc, String q, Integer firstResult, Integer maxResults, List<String> searchThrough,
			List<String> fields, List<Integer> comparisons, List<String> values, final Receiver<List<StandardizedPatientProxy>> callback) {
		createRangeRequest(sortField, asc, q, firstResult, maxResults, searchThrough, fields, comparisons, values).with(view.getPaths()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}

	protected Request<List<StandardizedPatientProxy>> createRangeRequest(String sortField, Boolean asc, String q, Integer firstResult, Integer maxResults,
			List<String> searchThrough, List<String> fields, List<Integer> comparisons, List<String> values) {
		return requests.standardizedPatientRequestNonRoo().findPatientsBySearchAndSort(sortField, asc, q, firstResult, maxResults, searchThrough, fields,
				comparisons, values);
		// return
		// requests.standardizedPatientRequestNonRoo().findPatientsBySearch(q,
		// firstResult, maxResults);
	}

	protected void fireCountRequest(String q, List<String> searchThrough, List<String> fields, List<Integer> comparisons, List<String> values,
			Receiver<Long> callback) {
		requests.standardizedPatientRequestNonRoo().countPatientsBySearchAndSort(q, searchThrough, fields, comparisons, values).fire(callback);
		// requests.standardizedPatientRequestNonRoo().countPatientsBySearch(q).fire(callback);
	}

	private void setTable(CellTable<StandardizedPatientProxy> table) {
		this.table = table;
	}

	@Override
	public void newClicked() {
		Log.info("create clicked");
		placeController.goTo(new StandardizedPatientDetailsPlace(Operation.CREATE));
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

	private StandardizedPatientAdvancedSearchPopup advancedSearchPopup;
	private StandardizedPatientAdvancedSearchAnamnesisPopup anamnesisPopup;
	private StandartizedPatientAdvancedSearchBasicCriteriaPopUp basicCriteriaPopUp;
	private StandardizedPatientAdvancedSearchScarPopup scarPopup;
	private StandardizedPatientAdvancedSearchLanguagePopup languagePopup;
	private StandardizedPatientAdvancedSearchNationalityPopup nationalityPopup;

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

	private StandardizedPatientRequestNonRoo requestAdvSeaCritStd;

	@Override
	public void addAdvSeaBasicButtonClicked(Long objectId, String string, BindType bindType, PossibleFields possibleFields, Comparison2 comparition) {

		requestAdvSeaCritStd = requests.standardizedPatientRequestNonRoo();

		AdvancedSearchCriteriaProxy criteria = requestAdvSeaCritStd.create(AdvancedSearchCriteriaProxy.class);
		criteria = requestAdvSeaCritStd.edit(criteria);
		criteria.setBindType(bindType);
		criteria.setComparation(comparition);
		criteria.setField(possibleFields);
		criteria.setValue(string);
		criteria.setObjectId(objectId);
		requestAdvSeaCritStd.fire();
		searchCriteria.add(criteria);

		criteriaTable.setRowData(searchCriteria);
		// TODO execute search.
	}
	
	public void deleteAdvancedSearchCriteria(AdvancedSearchCriteriaProxy criterium) {
		searchCriteria.remove(criterium);
		// TODO execute search
		criteriaTable.setRowData(searchCriteria);
	}
	
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

	private class AnamnesisCriteriaReceiver extends Receiver<List<AnamnesisCheckProxy>> {
		public void onSuccess(List<AnamnesisCheckProxy> response) {
			if (anamnesisPopup == null) {
				return;
			}

			((ProxySuggestOracle<AnamnesisCheckProxy>) anamnesisPopup.getAnamnesisQuestionSuggestBox().getSuggestOracle()).addAll(response);
		}
	}

	@Override
	public void addScarButtonClicked(ScarProxy scarProxy, BindType bindType, Comparison2 comparison) {
		Log.info("ScarType:" + scarProxy.getTraitType().toString() + ": " + scarProxy.getBodypart());
		addAdvSeaBasicButtonClicked(scarProxy.getId(), scarProxy.getTraitType().toString() + ": " + scarProxy.getBodypart(), bindType, PossibleFields.SCAR, comparison);
	}

	@Override
	public void addAnamnesisValueButtonClicked(AnamnesisCheckProxy anamnesisCheck, String answer, BindType bindType, Comparison2 comparison) {
		// TODO Auto-generated method stub
		Log.info("Question:" + anamnesisCheck.getText() + "; options:" + anamnesisCheck.getValue() + "; answer: " + answer);
		addAdvSeaBasicButtonClicked(anamnesisCheck.getId(), anamnesisCheck.getType() + ": " + answer+":"+anamnesisCheck.getValue(), bindType, PossibleFields.ANAMNESIS, comparison);
	}

	@Override
	public void addNationalityButtonClicked(NationalityProxy nationality,
			BindType bindType, Comparison2 comparison) {
		// TODO implement.
	}

	@Override
	public void addLanguageButtonClicked(SpokenLanguageProxy languageProxy, LangSkillLevel skill, BindType bindType, Comparison2 comparison) {
		addAdvSeaBasicButtonClicked(languageProxy.getId(), languageProxy.getLanguageName() + ": " + skill.toString(), bindType, PossibleFields.LANGUAGE, comparison);
	}
}
