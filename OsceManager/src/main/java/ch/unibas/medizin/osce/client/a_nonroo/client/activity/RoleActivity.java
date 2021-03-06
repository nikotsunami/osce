package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.Paging;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RolePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleAddPopupView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleAddPopupViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.KeywordProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicRequest;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.style.resources.AdvanceCellTable;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.Sorting;
import ch.unibas.medizin.osce.shared.StudyYears;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
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
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

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
	private RoleTopicRequest requestAllTopic;
	// private CellTable<RoleTopicProxy> table;
	// private SingleSelectionModel<RoleTopicProxy> selectionModel;

	SpecialisationProxy specialisationProxy;

	//cell table changes start
	//private CellTable<RoleTopicProxy> table;
	private AdvanceCellTable<RoleTopicProxy> table;
	int x;
	int y;
	//cell table changes end
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
		// Highlight onViolation
		
/*//		System.out.println(specialisationProxy);
		if(topicName=="" || specialisationProxy==null)
		{
			return;
		}
		
//		ScarRequest scarReq = requests.scarRequest();
//		ScarProxy scar = scarReq.create(ScarProxy.class);
		
//		System.out.println("spec---"+specialisationProxy.getName());
*/		
		// E Highlight onViolation
		RoleTopicRequest roleTopicRequest=requests.roleTopicRequest();
		RoleTopicProxy roleTopicProxy=roleTopicRequest.create(RoleTopicProxy.class);
		
		roleTopicProxy.setName(topicName);
		roleTopicProxy.setSlotsUntilChange(Integer.parseInt(slots_till_change));
		roleTopicProxy.setStudyYear(studyYearBox);
		roleTopicProxy.setSpecialisation(specialisationProxy);
		try
		{
			
		// Highlight onViolation			
		Log.info("Map Size: " + view.getRoleTopicMap());
		roleTopicRequest.persist().using(roleTopicProxy).fire(new OSCEReceiver<Void>(view.getRoleTopicMap()) 
		{
		// E Highlight onViolation
			
			@Override
			public void onSuccess(Void response) {
				// TODO Auto-generated method stub
		//		System.out.println("INside success");
				Log.info("new topic added successfully");
			//	init2();
				Log.info("Call Init Search from onSuccess");
				setInserted(true);
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
		
		//by spec
		RecordChangeEvent.register(requests.getEventBus(), (RoleViewImpl)view);
		//by spec
		
		MenuClickEvent.register(requests.getEventBus(), (RoleViewImpl)view);
		

	//	final StandardizedPatientView systemStartView = new StandardizedPatientViewImpl();
		final RoleView systemStartView = new RoleViewImpl();
		// spec start
		
		//cell table changes start
		 //this.table = view.getTable();
		this.table = (AdvanceCellTable<RoleTopicProxy>)view.getTable();
		 //cell table chanes end
		 this.scarBox = view.getSpecialisationBoxValues();
		
		 table
			.addRangeChangeHandler(new RangeChangeEvent.Handler() {
				public void onRangeChange(RangeChangeEvent event) {
					RoleActivity.this.onRangeChanged();
				}
			});
		 
		 
		//celltable changes start
		 table.addHandler(new MouseDownHandler() {

				@Override
				public void onMouseDown(MouseDownEvent event) {
					// TODO Auto-generated method stub
					Log.info("mouse down");
					x = event.getClientX();
					y = event.getClientY();

					if(table.getRowCount()>0)
					{
					Log.info(table.getRowElement(0).getAbsoluteTop() + "--"+ event.getClientY());

					
					if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT&& event.getClientY() < table.getRowElement(0).getAbsoluteTop()) {
						
						table.getPopup().setPopupPosition(x, y);
						table.getPopup().show();

						Log.info("right event");
					}
					}
					else
					{
						if(event.getNativeButton() == NativeEvent.BUTTON_RIGHT)
						{
							table.getPopup().setPopupPosition(x, y);
							table.getPopup().show();
						}
					}
				}
			}, MouseDownEvent.getType());
			
			table.getPopup().addDomHandler(new MouseOutHandler() {

				@Override
				public void onMouseOut(MouseOutEvent event) {
					// TODO Auto-generated method stub
					//addColumnOnMouseout();
					table.getPopup().hide();
					
				}
			}, MouseOutEvent.getType());
			
		 table.addColumnSortHandler(new ColumnSortEvent.Handler() {

				@Override
				public void onColumnSort(ColumnSortEvent event) {
					// By SPEC[Start

					Column<RoleTopicProxy, String> col = (Column<RoleTopicProxy, String>) event.getColumn();
					
					
					int index = table.getColumnIndex(col);
					
					/*String[] path =	systemStartView.getPaths();	            			
					sortname = path[index];
					sortorder=(event.isSortAscending())?Sorting.ASC:Sorting.DESC;	*/
					
					if (index % 2 == 1 ) {
						
						table.getPopup().setPopupPosition(x, y);
						table.getPopup().show();

					} else {
						// path = systemStartView.getPaths();
						//String[] path =	systemStartView.getPaths();	
						Log.info("index value--"+index +systemStartView.getPaths().get(index)+"--"+systemStartView.getPaths().get(index));
						sortname = systemStartView.getPaths().get(index);
						sortorder = (event.isSortAscending()) ? Sorting.ASC: Sorting.DESC;
						// By SPEC]end
						// RoleActivity.this.init2("");
						Log.info("Call Init Search from addColumnSortHandler");
						// filter.hide();
						setInserted(false);
						initSearch();
						
					}
				}
			});
			
			/*table.addColumnSortHandler(new ColumnSortEvent.Handler() {
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
					Log.info("Call Init Search from addColumnSortHandler");
					setInserted(false);
					initSearch();
				}
			});*/
		 
		/* table.addColumnSortHandler(new ColumnSortEvent.Handler() {
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
					Log.info("Call Init Search from addColumnSortHandler");
					setInserted(false);
					initSearch();
				}
			});*/
		 /*celltable changes end*/
	 //	init2();	
		 Log.info("Call Init Search from addColumnSortHandler1");
		 setInserted(false);
		initSearch();
		this.SpecialisationListBox = view.getListBox();
		requestAllTopic = requests.roleTopicRequest();
	//	System.out.println("Query Call");

		
		 requests.roleTopicRequest().findAllAutherName().fire(new AutherNameValueReceiver());
		 requests.roleTopicRequest().findAllReviewerName().fire(new ReviewerNameValueReceiver());
		requests.specialisationRequest().findAllSpecialisations().fire(new SpecialisationBoxValuesReceiver());
        requests.keywordRequest().findAllKeywords().fire(new KeywordBoxValuesReceiver());
//		
//		System.out.println("Query Call for cell table1");
		try {
			//requests.roleTopicRequest().findAllRoleTopicAndRoles()
			/*requests.roleTopicRequest().findAllRoleTopics()
					.fire(new RoleAndRolesRecever());*/
	
		
		//	requests.roleTopicRequest().findAllRoleTopics().with("standardizedRoles").fire(new RoleAndRolesRecever());
			
		
		} catch (Exception e) {
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
				//System.out.println("======================================Selection Change==================================");
				
				RoleTopicProxy selectedObject = selectionModel.getSelectedObject();
				if (selectedObject.getId() != null) {
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
					view.setDetailPanel(true);
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					Log.debug(selectedObject.getName() + " selected!");
					showDetails(selectedObject);
				}
				else
				{
					view.setDetailPanel(false);
					//System.out.println("==============No Role Found===============");
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
		Log.info("Call Init Search from onRangeChange");
		
		setInserted(false);
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
	Log.info("Call Init Search from performSearch");
	setInserted(false);
	initSearch();
//	System.out.println("search--"+q+"---"+list);
	
}

public Sorting sortorder = Sorting.ASC;
public String sortname = "name";
  
private int totalRecords;
private boolean isInserted;



public boolean isInserted() {
	return isInserted;
}

public void setInserted(boolean isInserted) {
	this.isInserted = isInserted;
}

public Sorting getSortorder() {
	return sortorder;
}

public void setSortorder(Sorting sortorder) {
	this.sortorder = sortorder;
}

public String getSortname() {
	return sortname;
}

public void setSortname(String sortname) {
	this.sortname = sortname;
}

public void initSearch() {
	// TODO Auto-generated method stub
	
	requestAllTopic = requests.roleTopicRequest();
// (1) Text search
	List<String> searchThrough = view.getSearchFilters();
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

			if(isInserted){
					
				setSortname("id");
				setSortorder(Sorting.ASC);
				
				int start = Paging.getLastPageStart(range.getLength(), totalRecords);
				table.setPageStart(start);

				setInserted(false);				
				Log.info("sortname == "+sortname);
				Log.info("sortorder == "+sortorder);
			}
			
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
	 
	requests.roleTopicRequest().advanceSearchCount(sortname, sortorder , quickSearchTerm, 
			searchThrough,tableFilter,whereFilter ).with("standardizedRoles").fire(new TotalRecordCount());

	requests.roleTopicRequest().advanceSearch(sortname, sortorder , quickSearchTerm, 
			searchThrough,tableFilter,whereFilter,  range.getStart(), range.getLength() ).with("standardizedRoles").fire(callback);
	
	
	
	
}

private class TotalRecordCount extends  Receiver<Long> {
	@Override
	public void onSuccess(Long response) {
		
		totalRecords = response.intValue();
		view.getTable().setRowCount(response.intValue(), true);
	 
		
		
	}
}

protected void showDetails(RoleTopicProxy RoleTopic) 
{	
	//System.out.println("============================showDetails() GotoStandardizedPatientDetailsPlace=========================");	
	RoleEditActivity.roleTopic = RoleTopic;//angiv
	goTo(new RoleDetailsPlace(RoleTopic.stableId(), Operation.DETAILS));
}

	//role issue
	public void getAllSpecialisation(final RoleAddPopupView popupView, final int clientX, final int clientY)
	{
		requests.specialisationRequest().findAllSpecialisations().fire(new OSCEReceiver<List<SpecialisationProxy>>() {

			@Override
			public void onSuccess(List<SpecialisationProxy> response) {
				//System.out.println("LIST SIZE : " + response.size());
				
				((RoleAddPopupViewImpl)popupView).setSpecialisationBoxValues(response);
				((RoleAddPopupViewImpl)popupView).setPopupPosition(clientX - 100, clientY);
				((RoleAddPopupViewImpl)popupView).show();
			}
		});
	}
}
