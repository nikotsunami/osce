package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.ScarProxyRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.SPDataChangedNotificationView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.SPDataChangedNotificationViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.SPDetailsReviewAnamnesisSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.SPDetailsReviewAnamnesisSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.SPDetailsReviewAnamnesisTableSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.SPDetailsReviewView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.SPDetailsReviewViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.SPEditRequestNotificationView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.SPEditRequestNotificationViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchAnamnesisPopup;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchAnamnesisPopupImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchGenderPopupView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchGenderPopupViewImpl;
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
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.util.OSCEReceiverPopupViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.managed.request.ProfessionProxy;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.request.SpAnamnesisChecksValueProxy;
import ch.unibas.medizin.osce.client.managed.request.SpAnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.SpStandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.resources.AdvanceCellTable;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.ProxySuggestOracle;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison;
import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.LangSkillLevel;
import ch.unibas.medizin.osce.shared.MaritalStatus;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.PossibleFields;
import ch.unibas.medizin.osce.shared.ResourceDownloadProps;
import ch.unibas.medizin.osce.shared.Sorting;
import ch.unibas.medizin.osce.shared.WorkPermission;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.shared.scaffold.StandardizedPatientRequestNonRoo;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.URL;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.requestfactory.shared.Violation;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.TextColumn;
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
		StandardizedPatientAdvancedSearchAnamnesisPopup.Delegate, StandardizedPatientAdvancedSearchNationalityPopup.Delegate,StandardizedPatientAdvancedSearchProfessionPopup.Delegate
		,StandardizedPatientAdvancedSearchWorkPermissionPopup.Delegate,StandardizedPatientAdvancedSearchMaritialStatusPopupView.Delegate,
		StandardizedPatientAdvancedSearchGenderPopupView.Delegate,SPEditRequestNotificationView.Delegate,SPDataChangedNotificationView.Delegate,
		SPDetailsReviewView.Delegate,SPDetailsReviewAnamnesisTableSubView.Delegate,
		SPDetailsReviewAnamnesisSubView.Delegate{

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
	
	/** holds the selection model of the standardized patient table */
	private SingleSelectionModel<StandardizedPatientProxy> selectionModel;
	
	/** Holds this activities' activityMapper */
	private StandardizedPatientDetailsActivityMapper StandardizedPatientDetailsActivityMapper;
	/** Holds the SubView where the advanced search criteria can be defined by the user */
	private StandartizedPatientAdvancedSearchSubView standartizedPatientAdvancedSearchSubView;
	

	/** Holds the request used to request patient data etc. */
	private StandardizedPatientRequestNonRoo requestAdvSeaCritStd;
	
	/** Holds the table with the advanced search criteria */ 
	/*celltable changes start*/
	
	//private CellTable<AdvancedSearchCriteriaProxy> criteriaTable;
	private AdvanceCellTable<AdvancedSearchCriteriaProxy> criteriaTable;
	/*celltable changes end*/
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
	private StandardizedPatientAdvancedSearchGenderPopupView genderPopup;
	
	//issue
	private StandardizedPatientAdvancedSearchProfessionPopup professionPopup;
	private StandardizedPatientAdvancedSearchWorkPermissionPopup workPermissionPopup;
	private StandardizedPatientAdvancedSearchMaritialStatusPopupView maritialStausPopup;
	
	private StandardizedPatientPlace place;
	// BY SPEC v(Start)

	/** Holds a reference to the IconButton of StandardizedPatientViewImpl */
	private IconButton iconButton;
	private HandlerRegistration placeChangeHandlerRegistration;

	private StandardizedPatientActivity standardizedPatientActivity;
	
	// private final String filePath = "StandardizedPatientList.csv";

	// BY SPEC v(End)

	/*custom celltable start code*/
	
	private SPEditRequestNotificationView speditReuestView;
	private SPDataChangedNotificationView spDataChangedNotificationView;
	
	public String columnHeader;
	public String cellValue;
	public boolean rightClick = false;
	private final OsceConstants constants = GWT.create(OsceConstants.class);

	public int x;
	public int y;
	private AdvanceCellTable<StandardizedPatientProxy> table;
	public List<String> path = new ArrayList<String>();
	Map<String, String> columnName;
	/*private CellTable<StandardizedPatientProxy> table;*/
	/*custom celltable end code*/

	private Map<AnamnesisCheckTitleProxy,SPDetailsReviewAnamnesisTableSubView>  spDetailsReviewAmnesisTableViewMap = 
			new HashMap<AnamnesisCheckTitleProxy,SPDetailsReviewAnamnesisTableSubView >();
	
	private SPDetailsReviewView  spDetailsReviewView;
	
	private SPDetailsReviewAnamnesisSubViewImpl spDetailsReviewAnamnesisSubViewImpl;
	 
	private SPDetailsReviewAnamnesisTableSubView spDetailsReviewAnamnesisTableSubView;
	
	private List<SpStandardizedPatientProxy> listOfAllSpStandardizedPatientWhoEditedDataAtSPPortal;
	
	private List<StandardizedPatientProxy> listOfAllStandardizedPatientWhoseDataIsChanged;
	
	private int currentSPIndexWhoseDataIsReviewing=0;
	
	private Map<Integer,Integer> anamnesisTabSelectedIndexMap = new HashMap<Integer, Integer>();
	
	protected List<AnamnesisCheckTitleProxy> allAnamnesisCheckTitleProxyList;
	
	/**
	 * Sets the dependencies of this activity and initializes the corresponding activity manager 
	 * @param requests The request factory to use
	 * @param placeController the place controller to use
	 */
	
	
	public StandardizedPatientActivity(OsMaRequestFactory requests, PlaceController placeController) {
		
		this.place = place;

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
		/*if (placeChangeHandlerRegistration != null) {
			placeChangeHandlerRegistration.removeHandler();
		}*/
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
	
	/*custom celltable start code*/
	
	public void customFilter() {
	}
	public void addColumnOnMouseout()
	{
		Set<String> selectedItems = table.getPopup().getMultiSelectionModel().getSelectedSet();

		
		int j = table.getColumnCount();
		while (j > 0) {
			
			table.removeColumn(0);
			j--;
		}

		path.clear();

		Iterator<String> i;
		if (selectedItems.size() == 0) {

			i = table.getPopup().getDefaultValue().iterator();

		} else {
			i = selectedItems.iterator();
		}

		Set mySet = new HashSet(view.getSortMap().keySet());
		//Iterator<String> i1=mySet.iterator();
		Iterator<String> i1=view.getColumnSortSet().iterator();
		/*System.out.println("key set is--"+view.getColumnSortSet());
		System.out.println("key set is--"+selectedItems);
		*/
		while (i1.hasNext()) {
		
			
			String colValue=i1.next();
			/*System.out.println("Initlist--"+table.getInitList());*/
			if(selectedItems.contains(colValue) || table.getInitList().contains(colValue))
			{
				
				if(table.getInitList().contains(colValue))
				{
					table.getInitList().remove(colValue);
				}
			columnHeader = colValue;
			String colName=(String)columnName.get(columnHeader);
				path.add(colName.toString());
				path.add(" ");
				
				/*if(columnHeader.equals(constants.editRequest()) ){
					
					table.addColumn(new Column<SPDataProxy, SPDataProxy>(new StandardizedPatientViewImpl.EditRequestTextCell()) {
						   @Override
						   public SPDataProxy getValue(SPDataProxy object) {
						    return object;
						   }
						  }, constants.editRequest());
				}
			
				else if(columnHeader.equals(constants.dataChange())){
					table.addColumn(new Column<SPDataProxy, SPDataProxy>(new StandardizedPatientViewImpl.DataChangedTextCell()) {
						   @Override
						   public SPDataProxy getValue(SPDataProxy object) {
						    return object;
						   }
						  }, constants.dataChange());
				}*/
				/*else{*/
					
					table.addColumn(new TextColumn<StandardizedPatientProxy>() {
		
						{
							this.setSortable(true);
						}
		
						Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
		
							public String render(java.lang.String obj) {
								return obj == null ? "" : String.valueOf(obj);
							}
						};
		
						String tempColumnHeader = columnHeader;
		
						@Override
						public String getValue(StandardizedPatientProxy object) {
		
		
							if (tempColumnHeader == constants.name()) {
								
								return renderer.render(object.getName()!=null?object.getName():"");
							} else if (tempColumnHeader == constants.preName()) {
								
								return renderer.render(object.getPreName()!=null?object.getPreName():"");
							} else if (tempColumnHeader == constants.email()) {
								
								return renderer.render(object.getEmail()!=null?object.getEmail():"");
							} else if (tempColumnHeader == constants.street()) {
								
								return renderer.render(object.getStreet()!=null?object.getStreet():"");
							} else if (tempColumnHeader == constants.city()) {
								
								return renderer.render(object.getCity()!=null?object.getCity():"");
								
							} else if (tempColumnHeader == constants.telephone()) {
								
								return renderer.render(object.getTelephone()!=null?object.getTelephone():"");
		
							} else if (tempColumnHeader == constants.height()) {
								
								return renderer.render(object.getHeight()!=null?object.getHeight().toString():"");
		
							}
							else if (tempColumnHeader == constants.weight()) {
								
								return renderer.render(object.getWeight()!=null?object.getWeight().toString():"");
		
							}
		
							else {
								return "";
							}
		
							// return renderer.render(cellValue);
						}
					}, columnHeader, false);
					//path.add(" ");
				/*}*/
		}
		}

	}
	
	
/*	custom celltable end code*/

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		
		
		Log.info("SystemStartActivity.start()");
		StandardizedPatientView standardizedPatientView = new StandardizedPatientViewImpl();
		standardizedPatientView.setPresenter(this);
		this.widget = panel;
		this.view = standardizedPatientView;
		widget.setWidget(standardizedPatientView.asWidget());
		
		standardizedPatientActivity=this;
		
		RecordChangeEvent.register(requests.getEventBus(), (StandardizedPatientViewImpl)view);
		
		/*RootLayoutPanel.get().addDomHandler(new ContextMenuHandler() {

				@Override
				public void onContextMenu(ContextMenuEvent event) {
					event.preventDefault();
					event.stopPropagation();
				}
			}, ContextMenuEvent.getType());*/
		
		final StandardizedPatientView systemStartView = new StandardizedPatientViewImpl();
		this.table = (AdvanceCellTable<StandardizedPatientProxy>)view.getTable();
		//celltable changes start
				//criteriaTable = standartizedPatientAdvancedSearchSubView.getTable();
		standartizedPatientAdvancedSearchSubView = view.getStandartizedPatientAdvancedSearchSubViewImpl();
		standartizedPatientAdvancedSearchSubView.setDelegate(this);
				criteriaTable = (AdvanceCellTable<AdvancedSearchCriteriaProxy>)standartizedPatientAdvancedSearchSubView.getTable();
				//celltable changes end
				
		 columnName=view.getSortMap();
		
		path = systemStartView.getPaths();
		
		addColumnOnMouseout();
		customFilter();
		//by spec
		
		//by spec
		 
		
		
		MenuClickEvent.register(requests.getEventBus(), (StandardizedPatientViewImpl) view);
		

		table.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				StandardizedPatientActivity.this.onRangeChanged();
			}
		});
		
		/*custom celltable start code*/
		
		table.addColumnSortHandler(new ColumnSortEvent.Handler() {

			@Override
			public void onColumnSort(ColumnSortEvent event) {
				// By SPEC[Start

				Column<StandardizedPatientProxy, String> col = (Column<StandardizedPatientProxy, String>) event.getColumn();
				
				
				int index = table.getColumnIndex(col);
				
				/*String[] path =	systemStartView.getPaths();	            			
				sortname = path[index];
				sortorder=(event.isSortAscending())?Sorting.ASC:Sorting.DESC;	*/
				Log.info("call for sort " + path.size() + "--index--" + index+ "cc=" + table.getColumnCount());
				if (index % 2 == 1 || (index == (table.getColumnCount() - 1))) {
					
					table.getPopup().setPopupPosition(x, y);
					table.getPopup().show();

				} else {
					
					
					// path = systemStartView.getPaths();
					Log.info("call for sort " + path.size() + "--index--"+ index);
					sortname = path.get(index);

					sortorder = (event.isSortAscending()) ? Sorting.ASC: Sorting.DESC;
					// By SPEC]end
					// RoleActivity.this.init2("");
					Log.info("Call Init Search from addColumnSortHandler");
					// filter.hide();
					initSearch();
				}
			}
		});
		
		/* celltable start code*/
		criteriaTable.addColumnSortHandler(new ColumnSortEvent.Handler() {

			@Override
			public void onColumnSort(ColumnSortEvent event) {
				// By SPEC[Start

				Column<AdvancedSearchCriteriaProxy, String> col = (Column<AdvancedSearchCriteriaProxy, String>) event.getColumn();
				
				
				int index = criteriaTable.getColumnIndex(col);
				
				/*String[] path =	systemStartView.getPaths();	            			
				sortname = path[index];
				sortorder=(event.isSortAscending())?Sorting.ASC:Sorting.DESC;	*/
				Log.info("column sort--"+index);
				if (index % 2 == 0 ) {
					
					criteriaTable.getPopup().setPopupPosition(x, y);
					criteriaTable.getPopup().show();

				} /*else {
					
					
					// path = systemStartView.getPaths();
					Log.info("call for sort " + path.size() + "--index--"+ index);
					sortname = path.get(index);

					sortorder = (event.isSortAscending()) ? Sorting.ASC: Sorting.DESC;
					// By SPEC]end
					// RoleActivity.this.init2("");
					Log.info("Call Init Search from addColumnSortHandler");
					// filter.hide();
					initSearch();
				}*/
			}
		});
		/*table.addHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {
				// TODO Auto-generated method stub
				Log.info("mouse down");

				Log.info(table.getRowElement(0).getAbsoluteTop() + "--"+ event.getClientY());

				x = event.getClientX();
				y = event.getClientY();

				if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT&& event.getClientY() < table.getRowElement(0).getAbsoluteTop()) {
					
					table.getPopup().setPopupPosition(x, y);
					table.getPopup().show();

					Log.info("right event");
				}

			}
		}, MouseDownEvent.getType());*/
		/*celltable  start */
		
		criteriaTable.addHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {
				// TODO Auto-generated method stub
				Log.info("mouse down");
				x = event.getClientX();
				y = event.getClientY();
				if(criteriaTable.getRowCount()>0)
				{
				Log.info(criteriaTable.getRowElement(0).getAbsoluteTop() + "--"+ event.getClientY());

				
				if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT&& event.getClientY() < criteriaTable.getRowElement(0).getAbsoluteTop()) {
					
					criteriaTable.getPopup().setPopupPosition(x, y);
					criteriaTable.getPopup().show();

					Log.info("right event");
				}
				}
				else
				{
					if(event.getNativeButton() == NativeEvent.BUTTON_RIGHT)
					{
						criteriaTable.getPopup().setPopupPosition(x, y);
						criteriaTable.getPopup().show();
					}
				}
			}
		}, MouseDownEvent.getType());
		
		
		
		criteriaTable.getPopup().addDomHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				// TODO Auto-generated method stub
				//addColumnOnMouseout();
				criteriaTable.getPopup().hide();
				
			}
		}, MouseOutEvent.getType());
		/*celltable changes end*/
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
					table.getPopup().setPopupPosition(x, y);
					table.getPopup().show();
					
				}

			}
		}, MouseDownEvent.getType());
		
		
		table.getPopup().addDomHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				// TODO Auto-generated method stub
				table.getPopup().hide();
				addColumnOnMouseout();
				
			}
		}, MouseOutEvent.getType());
		/*custom celltable end code*/

		//standartizedPatientAdvancedSearchSubView = view.getStandartizedPatientAdvancedSearchSubViewImpl();
		//standartizedPatientAdvancedSearchSubView.setDelegate(this);

		// BY SPEC v(Start)
		this.iconButton = this.view.getExportButton();

		this.iconButton.addClickHandler(new ClickHandler() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(ClickEvent arg0) {
				Range range = table.getVisibleRange();
								
/*
				requests.standardizedPatientRequestNonRoo()
						.getCSVMapperFindPatientsByAdvancedSearchAndSort(
								"name", Sorting.ASC, quickSearchTerm,
								searchThrough, searchCriteria // , filePath
								,range.getStart(),range.getLength()								
						).fire(new StandardizedPatientCsvFileReceiver());
*/
/*				
				requests.standardizedPatientRequestNonRoo()
				.getCSVMapperFindPatientsByAdvancedSearchAndSortUsingSession(
						"name", Sorting.ASC, quickSearchTerm,
						searchThrough, searchCriteria // , filePath
						,range.getStart(),range.getLength()								
				).fire(new Receiver<Void>() {
					@Override
					public void onSuccess(Void response) {
						String ordinal = URL.encodeQueryString(String.valueOf(ResourceDownloadProps.Entity.STANDARDIZED_PATIENT_EXPORT.ordinal()));
						String url = GWT.getHostPageBaseURL() + "downloadFile?".concat(ResourceDownloadProps.ENTITY).concat("=").concat(ordinal);
						Log.info("--> url is : " +url);
						Window.open(url, "", "");
					}
				});
*/				
				
				List<StandardizedPatientProxy>  items = table.getVisibleItems();
				List<Long> spIdList = new ArrayList<Long>();
				for (StandardizedPatientProxy standardizedPatientProxy : items) {
					
					spIdList.add(standardizedPatientProxy.getId());
				}
				requests.standardizedPatientRequestNonRoo().getCSVMapperForStandardizedPatientUsingServlet(spIdList).fire(new Receiver<Void>() {
						@Override
						public void onSuccess(Void response) {
							String ordinal = URL.encodeQueryString(String.valueOf(ResourceDownloadProps.Entity.STANDARDIZED_PATIENT_EXPORT.ordinal()));
							String url = GWT.getHostPageBaseURL() + "downloadFile?".concat(ResourceDownloadProps.ENTITY).concat("=").concat(ordinal);
							Log.info("--> url is : " +url);
							Window.open(url, "", "");
						}
				});
						

			}

//			private String join(List<String> stringList, String separator) {
//
//			  StringBuffer buf = new StringBuffer();
//
//		        for (int i = 0; i < stringList.size(); i++) {
//		            if (i > 0) {
//		                buf.append(separator);
//		            }
//		            if (stringList.get(i) != null) {
//		                buf.append(stringList.get(i));
//		            }
//		        }
//		        return buf.toString();	
//			}
		});

		// BY SPEC v(Stop)
		//criteriaTable = standartizedPatientAdvancedSearchSubView.getTable();
		
		


		/*table.addColumnSortHandler(new ColumnSortEvent.Handler() {
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
		*/
		/*		custom celltable end code*/
		
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
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
					view.setDetailPanel(true);
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					Log.debug(selectedObject.getName() + " selected!");
					showDetails(selectedObject);
				}
				else {
					view.setDetailPanel(false);
					System.out.println("==============No Role Found===============");
				}
			}
		});
		view.setDelegate(this);
		
		//SPportal related changes start {
		speditReuestView = new SPEditRequestNotificationViewImpl();
		speditReuestView.setDelegate(this);
		
		spDataChangedNotificationView = new SPDataChangedNotificationViewImpl();
		
		spDataChangedNotificationView.setDelegate(this);
		
		
		((SPEditRequestNotificationViewImpl)speditReuestView).getTable().addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				setSPDataInEditRequestNotificationViewTable();
			}
		});
		
		((SPDataChangedNotificationViewImpl)spDataChangedNotificationView).getTable().addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				setSPDataInDataChangedNotificationViewTable();
			}
		});
	
	
		findOldAndNewDetailsOfSP();
		
		spDetailsReviewView = new SPDetailsReviewViewImpl();
		
		spDetailsReviewView.setDelegate(this);
		
		spDetailsReviewAnamnesisSubViewImpl = ((SPDetailsReviewViewImpl)spDetailsReviewView).getSpDetailsReviewAnamnesisSubViewImpl();
		
		spDetailsReviewAnamnesisSubViewImpl.setDelegate(this);
		
		addAnamnesisCheckTitleTabSelectionHandler();
		
		addClickHandlerOfAcceptedAndDiscardButton();
		
		showAllSpsWhoSentEditRequest();
		
		//findAllAnamnesisCheckTitle();
		
		//SPportal related changes end }
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
	private void findAllAnamnesisCheckTitle() {
		
	requests.spPortalPersonRequestNonRoo().findAllAnamnesisThatIsSendToDMZ().fire(new OSCEReceiver<List<AnamnesisCheckTitleProxy>>() {

		@Override
		public void onSuccess(List<AnamnesisCheckTitleProxy> response) {

			if (response == null) {
				return;
			}
			Log.info("Total anamnesis check is : " + response.size());
			
			allAnamnesisCheckTitleProxyList =response;
			
			for (final AnamnesisCheckTitleProxy title : response) {
				SPDetailsReviewAnamnesisTableSubView subView = spDetailsReviewAnamnesisSubViewImpl.addAnamnesisCheckTitle(title);
				subView.setDelegate(standardizedPatientActivity);
				spDetailsReviewAmnesisTableViewMap.put(title, subView);
			//	anamnesisCheckTitles.add(title);
			}
		}
	});
	
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
	protected void showDetails(StandardizedPatientProxy standardizedPatient) {
		Log.debug(standardizedPatient.getName());
		
		//upload image[
		copyImageAndVideo(standardizedPatient.getImmagePath(),standardizedPatient.getVideoPath(),standardizedPatient);
		//upload image]
		//goTo(new StandardizedPatientDetailsPlace(standardizedPatient.stableId(), Operation.DETAILS));
	}
	
	private class CopyReceiver extends Receiver<Boolean>{
		@Override
		public void onSuccess(Boolean response) {
			System.out.println("CopyReceiver :" + response);
		}
	}
	
	/*
	 * By SPEC
	 * 
	 */
	private void copyImageAndVideo(String imagePath,String videoPath,final StandardizedPatientProxy standardizedPatient)
	{
		/*copyService.copyImagesAndVideo(id, new AsyncCallback<Boolean>() {

            public void onFailure(Throwable caught) {
              Window.alert("RPC to sendEmail() failed.");
            }

         

			@Override
			public void onSuccess(Boolean result) {
				// TODO Auto-generated method stub
				System.out.println("Success");
			}
          });
          */
		if(imagePath ==null || videoPath == null || imagePath.equals("") || videoPath.equals(""))
		{
			goTo(new StandardizedPatientDetailsPlace(standardizedPatient.stableId(), Operation.DETAILS));
			return;
		}
		requests.standardizedPatientRequestNonRoo().copyImageAndVideo(imagePath,videoPath).fire(new OSCEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				System.out.println("CopyReceiver :" + response);
				
				goTo(new StandardizedPatientDetailsPlace(standardizedPatient.stableId(), Operation.DETAILS));
			}
		});
		
		

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
	
	/*private OsceConstants constants = GWT.create(OsceConstants.class);*/
	
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
	public void addGenderClicked(IconButton addGender) {
		if (advancedSearchPopup != null && advancedSearchPopup.isShowing()) {
			advancedSearchPopup.hide();
			if (advancedSearchPopup == genderPopup) {
				return;
			}
		}
		
		genderPopup = new StandardizedPatientAdvancedSearchGenderPopupViewImpl();
		genderPopup.setDelegate(this);
		genderPopup.display(addGender);
		advancedSearchPopup = genderPopup;
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

	@Override
	public void doAnimation(boolean flag) {
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(flag));
	}

	@Override
	public void addGenderButtonClicked(Gender gender, BindType bindType, Comparison comparison) {
		// TODO Auto-generated method stub
		String displayValue = new EnumRenderer<Comparison>(EnumRenderer.Type.MARITIALSTATUS).render(comparison) + " " + gender;
		addAdvSeaBasicButtonClicked(null, gender.toString(), displayValue, bindType, PossibleFields.GENDER, comparison);
	}

	/**
	 * This method is used to show all sps that sent edit request
	 */
	@SuppressWarnings("deprecation")
	private void showAllSpsWhoSentEditRequest() {
	
		Log.info("Finding all SPS who sent edit request");
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));

		requests.spPortalPersonRequestNonRoo().findAllSpsCountWhoSentEditReq().fire(new OSCEReceiver<Long>() {

			@Override
			public void onSuccess(Long response) {
				Log.info("Users count who sent edit requests are : " + response);
				((SPEditRequestNotificationViewImpl)speditReuestView).getTable().setRowCount(response.intValue(), true);
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
				
				if(response > 0 ){
					
					((SPEditRequestNotificationViewImpl)speditReuestView).show();
					
					setSPDataInEditRequestNotificationViewTable();
					
					((SPEditRequestNotificationViewImpl)speditReuestView).setButtonText(response.intValue());
				}
				
				if(response==0){
					showAllSpsWhoEditedData();
				}
			}
			
		});
		
	}
	/**
	 * This method set data of sp sent edit request in table.
	 */
	@SuppressWarnings("deprecation")
	private void setSPDataInEditRequestNotificationViewTable() {
		
		Range range = ((SPEditRequestNotificationViewImpl)speditReuestView).getTable().getVisibleRange();

		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
	
		requests.spPortalPersonRequestNonRoo().findAllSpsWhoSentEditRequest(range.getStart(),range.getLength()).fire(new OSCEReceiver<List<StandardizedPatientProxy>>() {

			@Override
			public void onSuccess(List<StandardizedPatientProxy> response) {
				
				Log.info("Total SP sent edit request list is : " + response);
				
				Range range = ((SPEditRequestNotificationViewImpl)speditReuestView).getTable().getVisibleRange();
			
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
				
				((SPEditRequestNotificationViewImpl)speditReuestView).getTable().setRowData(range.getStart(), response);
			}
		});
		
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void denyEditRequestButtonClicked() {
		Log.info("updating user request flag and sending email to all sps who sent edit request as their req is denied");
		
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));

		requests.spPortalPersonRequestNonRoo().denyAllSpsEditRequest().fire(new OSCEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				
				Log.info("Sent mail to all spes and their status is updated");
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
				
				((SPEditRequestNotificationViewImpl)speditReuestView).hide();
				
				showAllSpsWhoEditedData();
			}
		});
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void allowEditRequestButtonClicked() {

		Log.info("updating user request flag and sending email to all sps who sent edit request as their req is accepted");
		
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));

		requests.spPortalPersonRequestNonRoo().allSpsEditRequestIsApproved().fire(new OSCEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				
				Log.info("Sent mail to all spes and their status is updated");
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
				
				((SPEditRequestNotificationViewImpl)speditReuestView).hide();
				
				showAllSpsWhoEditedData();
			}
		});
	}
	/**
	 * This method is used to show all sps that changed their data
	 */
	@SuppressWarnings("deprecation")
	public void showAllSpsWhoEditedData() {
	
		Log.info("Finding all SPS who changed their Data");
		
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		
		requests.spStandardizedPatientRequestNonRoo().findAllSpsCountWhoEditedData().fire(new OSCEReceiver<Long>() {

			@Override
			public void onSuccess(Long response) {
				
				Log.info("Users count who edited their data are : " + response);
				
				((SPDataChangedNotificationViewImpl)spDataChangedNotificationView).getTable().setRowCount(response.intValue(), true);
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
				
				if(response > 0 ){
					
					((SPDataChangedNotificationViewImpl)spDataChangedNotificationView).show();
					
					setSPDataInDataChangedNotificationViewTable();
				}
				
			}
			
		});
	}
	/**
	 * This method is used to show all sps that changed their data in table.
	 */
	@SuppressWarnings("deprecation")
	private void setSPDataInDataChangedNotificationViewTable() {
		
		Range range = ((SPDataChangedNotificationViewImpl)spDataChangedNotificationView).getTable().getVisibleRange();

		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		
			requests.spStandardizedPatientRequestNonRoo().findAllSPWhoEditedDetails(range.getStart(),range.getLength()).fire(new OSCEReceiver<List<SpStandardizedPatientProxy>>() {

			@Override
			public void onSuccess(List<SpStandardizedPatientProxy> response) {
				
				Log.info("Total SP edited their data list is : " + response.size());
				
				Range range = ((SPDataChangedNotificationViewImpl)spDataChangedNotificationView).getTable().getVisibleRange();
			
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
				
				((SPDataChangedNotificationViewImpl)spDataChangedNotificationView).getTable().setRowData(range.getStart(), response);
			}
		});
		
	}

	/**
	 * This method is used to handle review button click handler.
	 */
	@Override
	public void reviewChangeButtonClicked() {
		
		showOldAndNewDataOfSPToAdmin();
		
		findAllAnamnesisCheckTitle();
		
		//hide changes view.
		((SPDataChangedNotificationViewImpl)spDataChangedNotificationView).showView(false);
		
		//showing review view.
		((SPDetailsReviewViewImpl)spDetailsReviewView).setViewVisible(true);
		
		//findOldAndNewDetailsOfSP();
		
		//spDetailsReviewAnamnesisSubViewImpl.anamnesisTabs.selectTab(0);
	}

	@SuppressWarnings("deprecation")
	private void findOldAndNewDetailsOfSP() {
		
		Log.info("finding all sps who changed their data at sp portal");
		
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		
		requests.spStandardizedPatientRequestNonRoo().findALlSPWhoEditedDetails().with("anamnesisForm").fire(new OSCEReceiver<List<SpStandardizedPatientProxy>>() {

			@Override
			public void onSuccess(List<SpStandardizedPatientProxy> response) {

				Log.info("total sp who edited their data at sp portal is :" + response.size());
				
				listOfAllSpStandardizedPatientWhoEditedDataAtSPPortal=response;
				
				Log.info("finding all standar dized patient whoes data is changed at sp portal to get theird old data");
				
				requests.spPortalPersonRequestNonRoo().findAllStandardizedPAtientWhoesDataIsChangedAtSPPortal(listOfAllSpStandardizedPatientWhoEditedDataAtSPPortal).with("anamnesisForm").
			
				fire(new OSCEReceiver<List<StandardizedPatientProxy>>() {

					@Override
					public void onSuccess(List<StandardizedPatientProxy> response) {
					
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
						
						Log.info("total standardized patient found is  :" + response.size());
						
						listOfAllStandardizedPatientWhoseDataIsChanged=response;
						
						//showOldAndNewDataOfSPToAdmin();
					}
					
				});
				
			}
		});
		
	}
	
	private void showOldAndNewDataOfSPToAdmin() {
		
		if(listOfAllSpStandardizedPatientWhoEditedDataAtSPPortal.size() > currentSPIndexWhoseDataIsReviewing){
			StandardizedPatientProxy standardizedPatientProxy = listOfAllStandardizedPatientWhoseDataIsChanged.get(currentSPIndexWhoseDataIsReviewing);
			SpStandardizedPatientProxy spStandardizedPatientProxy = listOfAllSpStandardizedPatientWhoEditedDataAtSPPortal.get(currentSPIndexWhoseDataIsReviewing);
			
			spDetailsReviewView.setValue(standardizedPatientProxy, spStandardizedPatientProxy);
		}
		
	}

	private void addClickHandlerOfAcceptedAndDiscardButton(){

		((SPDetailsReviewViewImpl)spDetailsReviewView).getAcceptChangesButton().addClickHandler(new ClickHandler() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(ClickEvent event) {
				Log.info("Accepted changes button clicked");
		
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
				
				StandardizedPatientProxy standardizedPatientProxy = listOfAllStandardizedPatientWhoseDataIsChanged.get(currentSPIndexWhoseDataIsReviewing);

				SpStandardizedPatientProxy spStandardizedPatientProxy = listOfAllSpStandardizedPatientWhoEditedDataAtSPPortal.get(currentSPIndexWhoseDataIsReviewing);
				
				
				requests.spStandardizedPatientRequestNonRoo().moveChangedDetailsOfSPFormSPPortal(standardizedPatientProxy.getId(), spStandardizedPatientProxy.getId()).fire(new OSCEReceiver<Boolean>() {

					@Override
					public void onSuccess(Boolean response) {
						
						Log.info("Data moved successfully is" + response);
						
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
						
						if(response){
							anamnesisTabSelectedIndexMap.clear();
							resetTabStyle();
							currentSPIndexWhoseDataIsReviewing+=1;
							
							if(spDetailsReviewAnamnesisSubViewImpl.anamnesisTabs.getWidgetCount()>=1)
							spDetailsReviewAnamnesisSubViewImpl.anamnesisTabs.selectTab(1);
							
							spDetailsReviewAnamnesisSubViewImpl.anamnesisTabs.selectTab(0);
							
							if(listOfAllStandardizedPatientWhoseDataIsChanged.size()==currentSPIndexWhoseDataIsReviewing){
								((SPDetailsReviewViewImpl)spDetailsReviewView).setViewVisible(false);
							}else{
								showOldAndNewDataOfSPToAdmin();
							}
						}else{
						
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
		
		((SPDetailsReviewViewImpl)spDetailsReviewView).getDiscardChangesButton().addClickHandler(new ClickHandler() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(ClickEvent event) {
				Log.info("Discard changes button clicked");
				
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
				
				StandardizedPatientProxy standardizedPatientProxy = listOfAllStandardizedPatientWhoseDataIsChanged.get(currentSPIndexWhoseDataIsReviewing);

				SpStandardizedPatientProxy spStandardizedPatientProxy = listOfAllSpStandardizedPatientWhoEditedDataAtSPPortal.get(currentSPIndexWhoseDataIsReviewing);
				
				
				requests.spStandardizedPatientRequestNonRoo().removeSPDetailsFromSPPortal(standardizedPatientProxy.getId(),spStandardizedPatientProxy.getId()).fire(new OSCEReceiver<Boolean>() {

					@Override
					public void onSuccess(Boolean response) {
					
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
						
						if(response){
							
							anamnesisTabSelectedIndexMap.clear();
							resetTabStyle();
							currentSPIndexWhoseDataIsReviewing+=1;
							
							if(spDetailsReviewAnamnesisSubViewImpl.anamnesisTabs.getWidgetCount()>=1)
							spDetailsReviewAnamnesisSubViewImpl.anamnesisTabs.selectTab(1);
							
							spDetailsReviewAnamnesisSubViewImpl.anamnesisTabs.selectTab(0);
							
							if(listOfAllStandardizedPatientWhoseDataIsChanged.size()==currentSPIndexWhoseDataIsReviewing){
								((SPDetailsReviewViewImpl)spDetailsReviewView).setViewVisible(false);
							}else{
								showOldAndNewDataOfSPToAdmin();
							}
						}else{
						
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
	
	private void resetTabStyle() {
		int totalTabBarCount=spDetailsReviewAnamnesisSubViewImpl.anamnesisTabs.getWidgetCount();
		Log.info("Total tabs are :" + totalTabBarCount);
		for(int count=1; count< totalTabBarCount;count++){
			if(count==(spDetailsReviewAnamnesisSubViewImpl.getAnamnesisTabs().getWidgetCount()-1)){
				spDetailsReviewAnamnesisSubViewImpl.getAnamnesisTabs().getTabWidget(count).removeStyleName("lastTabChangedTabSty");
			}else{
				spDetailsReviewAnamnesisSubViewImpl.getAnamnesisTabs().getTabWidget(count).removeStyleName("chnagedTabStyle");
			}
		}
		
	}
	@SuppressWarnings("deprecation")
	public void anamnesisTitleTabSelected(final Integer selectedIndex) {
		
		Log.info("finding data if index "+ selectedIndex +"  is selected first time.");
		
		if(anamnesisTabSelectedIndexMap.get(selectedIndex)==null){
			
			anamnesisTabSelectedIndexMap.put(selectedIndex, selectedIndex);
			
			StandardizedPatientProxy standardizedPatientProxy =listOfAllStandardizedPatientWhoseDataIsChanged.get(currentSPIndexWhoseDataIsReviewing);
			
			AnamnesisFormProxy anamnesisFormProxy = standardizedPatientProxy.getAnamnesisForm();
			
			final AnamnesisCheckTitleProxy anamnesisCheckTitleProxy = allAnamnesisCheckTitleProxyList.get(selectedIndex);
			
			SpStandardizedPatientProxy spStandardizedPatientProxy = listOfAllSpStandardizedPatientWhoEditedDataAtSPPortal.get(currentSPIndexWhoseDataIsReviewing);
			
			final SpAnamnesisFormProxy spAnamnesisFormProxy = spStandardizedPatientProxy.getAnamnesisForm();
			
			requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
			
			requests.anamnesisChecksValueRequestNonRoo().findAnamnesisChecksValuesByAnamnesisFormAndCheckTitle(anamnesisFormProxy.getId(),anamnesisCheckTitleProxy.getId()).with("anamnesischeck").fire(new OSCEReceiver<List<AnamnesisChecksValueProxy>>() {

				@Override
				public void onSuccess(final List<AnamnesisChecksValueProxy> response1) {

					Log.info("Total osce standardized patient anamnesis check value proxy is : " + response1.size());
					
					Log.info("Now finding all anamnesis check value proxy is of sp portal");
					
					String anmnesisCheckText = getSPAnamnesisCheckText(response1);
					
					requests.spStandardizedPatientRequestNonRoo().findAnamnesisChecksValuesByAnamnesisFormAndCheckTitleText(spAnamnesisFormProxy.getId(),anmnesisCheckText).with("anamnesischeck").fire(new OSCEReceiver<List<SpAnamnesisChecksValueProxy>>() {

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
