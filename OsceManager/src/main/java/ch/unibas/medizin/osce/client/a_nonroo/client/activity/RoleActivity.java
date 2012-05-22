package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;

import ch.unibas.medizin.osce.client.a_nonroo.client.SearchCriteria;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RolePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleViewImpl;
import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.KeywordProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicRequest;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.Sorting;
import ch.unibas.medizin.osce.shared.StudyYears;
import ch.unibas.medizin.osce.shared.scaffold.RoleTopicRequestNonRoo;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

public class RoleActivity extends AbstractActivity implements
		RoleView.Presenter, RoleView.Delegate {

	//spec start
	private String quickSearchTerm = "";
	private List<String> searchThrough = Arrays.asList("name");
	//spec end
	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private RoleView view;
	private RoleView filterView;
	private ActivityManager activityManager;
	private RoleDetailsActivityMapper roleDetailsActivityMapper;
	//spec start
	private ListBox SpecialisationListBox;
	private RoleTopicProxy roleTopicProxy;
	private RolePlace place;
	private CellTable<AdvancedSearchCriteriaProxy> criteriaTable;
	private List<AdvancedSearchCriteriaProxy> searchCriteria = new ArrayList<AdvancedSearchCriteriaProxy>();
	//spec end
//	private List<AdvancedSearchCriteriaProxy> searchCriteria = new ArrayList<AdvancedSearchCriteriaProxy>();
	// spec start
	private RoleTopicRequestNonRoo requestAllTopic;
	// private CellTable<RoleTopicProxy> table;
	// private SingleSelectionModel<RoleTopicProxy> selectionModel;

	SpecialisationProxy specialisationProxy;

	private CellTable<RoleTopicProxy> table;
	/** holds the selection model of the standardized patient table */
	private SingleSelectionModel<RoleTopicProxy> selectionModel;
	
	
	private ValueListBox<SpecialisationProxy> scarBox;
	private HandlerRegistration rangeChangeHandler;

	private List<String> tableFilter = Arrays.asList();
	private List<String> whereFilter = Arrays.asList();
	
	
	// spec end

	@Inject
	public RoleActivity(OsMaRequestFactory requests,
			PlaceController placeController) {
		this.requests = requests;
		this.place=place;
		
		this.placeController = placeController;
		roleDetailsActivityMapper = new RoleDetailsActivityMapper(requests,
				placeController);
		this.activityManager = new ActivityManager(roleDetailsActivityMapper,
				requests.getEventBus());
		
		RoleEditActivity.roleActivity=this;
	}

	public void onStop() {
		activityManager.setDisplay(null);
	}

	// spec start
	
	private class SpecialisationBoxValuesReceiver extends Receiver<List<SpecialisationProxy>> {
		@Override
		public void onSuccess(List<SpecialisationProxy> response) {
		//	filterView.setSpecialisationBoxValues(response);
			filterView.setSpecialisationAutocompleteValue(response);
			view.setSpecialisationBoxValues(response);
		}
	}

	private class KeywordBoxValuesReceiver extends Receiver<List<KeywordProxy>> {
		@Override
		public void onSuccess(List<KeywordProxy> response) {
		//	filterView.setKeywordListBoxValues(response);
			filterView.setKeywordAutocompleteValue(response);
			
		//	view.setSpecialisationBoxValues(response);
			
			
			
		}
	}
	//spec end
//
	
	private class AutherNameValueReceiver extends Receiver<List<DoctorProxy>> {
		@Override
		public void onSuccess(List<DoctorProxy> response) {
			Log.info("set data in valuelistbox data for auther in filter popup--"+response.size());
			
		
			filterView.setAuthorAutocompleteValue(response);
			
		}
		
	}
	
	
	
	
	private class ReviewerNameValueReceiver extends Receiver<List<DoctorProxy>> {
		@Override
		public void onSuccess(List<DoctorProxy> response) {
			Log.info("set  data in valuelistbox for reviewer in filter popup");
		//	filterView.setReviewerListBoxValues(response);
			filterView.setReviewerAutocompleteValue(response);
			
			
		}
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
			if(response instanceof RoleTopicProxy){
				Log.info(((RoleTopicProxy) response).getName());
				roleTopicProxy = (RoleTopicProxy) response;
				
			}
		}
	}

//spec start	
	@SuppressWarnings("deprecation")
	private class RoleAndRolesRecever extends Receiver<List<RoleTopicProxy>> {
		public void onFailure(ServerFailure error) {
		//	Log.error("  error for join");
	//		System.out.println(error);
			Log.info("Error for fetch data");
			// onStop();
		}

		@Override
		public void onSuccess(List<RoleTopicProxy> response) {

			//System.out.println("Success for join");
			Log.info("fetch data successfully");
			
		//	System.out.println("Result" + response.size());
			try{
			//	System.out.println("stan name" + response.get(0).getDescription());
			//	System.out.println("stand role1 count" + response.get(0).getStandardizedRoles());
			}
			catch(Exception e)
			{
				
			}
			try{
		//	System.out.println("stand role2 count" + response.get(1).getStandardizedRoles());
			}
			catch(Exception e)
			{
				
			}
			// final Range range = table.getVisibleRange();

			//table.setVisibleRange(0, 20);						
			//table.setRowData(range.getStart(), response);
		//aaa	Range range = table.getVisibleRange();
//			view.getTable().setRowCount(range.getLength()-1, true);
//			
//			System.out.println("visible range"+range);
//			table.setRowData(range.getStart(), response);
//			//range.getStart()
			

 		}

	}

	@Override
	public void newClicked( String topicName,  String slots_till_change,
			 SpecialisationProxy specialisationProxy,  StudyYears studyYearBox) {
		
		Log.info("call for new topic add");
//		System.out.println(specialisationProxy);
		if(topicName=="" || specialisationProxy==null)
		{
			return;
		}
		
//		ScarRequest scarReq = requests.scarRequest();
//		ScarProxy scar = scarReq.create(ScarProxy.class);
		
//		System.out.println("spec---"+specialisationProxy.getName());
		
		RoleTopicRequest roleTopicRequest=requests.roleTopicRequest();
		RoleTopicProxy roleTopicProxy=roleTopicRequest.create(RoleTopicProxy.class);
		
		roleTopicProxy.setName(topicName);
		roleTopicProxy.setSlotsUntilChange(Integer.parseInt(slots_till_change));
		roleTopicProxy.setStudyYear(studyYearBox);
		roleTopicProxy.setSpecialisation(specialisationProxy);
		try
		{
			
		
		roleTopicRequest.persist().using(roleTopicProxy).fire(new Receiver<Void>() {
			
			
			@Override
			public void onSuccess(Void response) {
				// TODO Auto-generated method stub
		//		System.out.println("INside success");
				Log.info("new topic added successfully");
			//	init2();
				initSearch();
			
				
			}
		});
		}
		catch(Exception e)
		{
	//		System.out.println("Error");
		}
		


//		System.out.println("after insert call");

		
	}

	// spec end

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("RoleActivity.start()");
		RoleView roleView = new RoleViewImpl();
		roleView.setPresenter(this);
		this.widget = panel;
		this.view = roleView;

		this.filterView=view.getFilter();
		widget.setWidget(roleView.asWidget());
	//	final StandardizedPatientView systemStartView = new StandardizedPatientViewImpl();
		final RoleView systemStartView = new RoleViewImpl();
		// spec start
		 this.table = view.getTable();
		 this.scarBox = view.getSpecialisationBoxValues();
		
		 table
			.addRangeChangeHandler(new RangeChangeEvent.Handler() {
				public void onRangeChange(RangeChangeEvent event) {
					RoleActivity.this.onRangeChanged();
				}
			});
		 
		 table.addColumnSortHandler(new ColumnSortEvent.Handler() {
				@Override
				public void onColumnSort(ColumnSortEvent event) {
					//By SPEC[Start
					Log.info("call for sort ");
					Column<RoleTopicProxy,String> col = (Column<RoleTopicProxy,String>) event.getColumn();
					int index = table.getColumnIndex(col); 
					String[] path =	systemStartView.getPaths();	            			
					sortname = path[index];
					sortorder=(event.isSortAscending())?Sorting.ASC:Sorting.DESC;				
					//By SPEC]end
					//RoleActivity.this.init2("");
					initSearch();
				}
			});
		 
	 //	init2();		 
		initSearch();
		this.SpecialisationListBox = view.getListBox();
		requestAllTopic = requests.roleTopicRequestNonRoo();
	//	System.out.println("Query Call");

		
		 requests.roleTopicRequestNonRoo().findAllAutherName().fire(new AutherNameValueReceiver());
		 requests.roleTopicRequestNonRoo().findAllReviewerName().fire(new ReviewerNameValueReceiver());
		requests.specialisationRequest().findAllSpecialisations().fire(new SpecialisationBoxValuesReceiver());
                requests.keywordRequest().findAllKeywords().fire(new KeywordBoxValuesReceiver());
//		
//		System.out.println("Query Call for cell table1");
		try {
			//requests.roleTopicRequestNonRoo().findAllRoleTopicAndRoles()
			/*requests.roleTopicRequest().findAllRoleTopics()
					.fire(new RoleAndRolesRecever());*/
	
		
		//	requests.roleTopicRequest().findAllRoleTopics().with("standardizedRoles").fire(new RoleAndRolesRecever());
			
		
		} catch (Exception e) {
			System.out.println("Eoor bhargav");
			Log.error(e.getMessage());
			e.printStackTrace();
		}

	//	System.out.println("Query after Call for cell table");

		
		

		activityManager.setDisplay(view.getDetailsPanel());
		
		ProvidesKey<RoleTopicProxy> keyProvider = ((AbstractHasData<RoleTopicProxy>) table).getKeyProvider();
		selectionModel = new SingleSelectionModel<RoleTopicProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		// adds a selection handler to the table so that if a valid patient is selected,
		// the corresponding details view is shown (via showDetails())
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				System.out.println("======================================Selection Change==================================");
				RoleTopicProxy selectedObject = selectionModel.getSelectedObject();
				if (selectedObject.getId() != null) {
					Log.debug(selectedObject.getName() + " selected!");
					showDetails(selectedObject);
				}
				else
				{
					System.out.println("==============No Role Found===============");
				}
			}
		});			

		view.setDelegate(this);
		filterView.setDelegate(this);
		
		
	//	requests.find(place.getProxyId()).with("specialisation").fire(new InitializeActivityReceiver());
		
	}

/*	private void init() {
		init2("");
		// TODO implement this!
	}
*/
	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public void showSubviewClicked() {
		goTo(new RoleDetailsPlace(Operation.DETAILS));
	}
	
	
	
	protected void fireCountRequest( Receiver<Long> callback) {
		//requests.scarRequest().countScars().fire(callback);
		requests.roleTopicRequest().countRoleTopics().fire(callback);
	}

	
	
	protected void onRangeChanged() {
		final Range range = table.getVisibleRange();

		Log.info("range change for role topic ");

		initSearch();
	}
	
	
	
	protected Request<List<RoleTopicProxy>> createRangeRequest(Range range) {
		return requests.roleTopicRequest().findRoleTopicEntries(range.getStart(), range.getLength());	
	}

	


@Override
public void performSearch(String q, List<String> list,List<String> tableList,List<String> whereList ) {
	// TODO Auto-generated method stub
	quickSearchTerm = q;
	this.searchThrough = list;
	this.tableFilter=tableList;
	this.whereFilter=whereList;
//	this.searchThroughListBox=listBoxFilter;
//	Log.debug("Search for " + q);
	
	initSearch();
//	System.out.println("search--"+q+"---"+list);
	
}

public Sorting sortorder = Sorting.ASC;
public String sortname = "name";
  
public void initSearch() {
	// TODO Auto-generated method stub
	
	requestAllTopic = requests.roleTopicRequestNonRoo();
// (1) Text search
	List<String> searchThrough = view.getSearchFilters();
	SearchCriteria criteria = view.getCriteria();
	final Range range = table.getVisibleRange();
	
// (2) Advanced search
	Log.info("perform search");
//	System.out.println("before call");
	final Receiver<List<RoleTopicProxy>> callback = new Receiver<List<RoleTopicProxy>>() {
		@Override
		public void onSuccess(List<RoleTopicProxy> values) {
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

/*
	requestAllTopic.findRoleTopicsByAdvancedSearchAndSort(sortname, sortorder , quickSearchTerm, 
			searchThrough, searchCriteria, range.getStart(), range.getLength() fields, bindType, comparations, values ).with("standardizedRoles").fire(callback);
	*/
	 
	requests.roleTopicRequestNonRoo().advanceSearchCount(sortname, sortorder , quickSearchTerm, 
			searchThrough,tableFilter,whereFilter ).with("standardizedRoles").fire(new TotalRecordCount());

	requests.roleTopicRequestNonRoo().advanceSearch(sortname, sortorder , quickSearchTerm, 
			searchThrough,tableFilter,whereFilter,  range.getStart(), range.getLength() ).with("standardizedRoles").fire(callback);
	
	
	
	
}

private class TotalRecordCount extends  Receiver<Long> {
	@Override
	public void onSuccess(Long response) {
		
		
		view.getTable().setRowCount(response.intValue(), true);
	 
		
		
	}
}

protected void showDetails(RoleTopicProxy RoleTopic) 
{	
	System.out.println("============================showDetails() GotoStandardizedPatientDetailsPlace=========================");	
	RoleEditActivity.roleTopic = RoleTopic;//angiv
	goTo(new RoleDetailsPlace(RoleTopic.stableId(), Operation.DETAILS));
}
}
