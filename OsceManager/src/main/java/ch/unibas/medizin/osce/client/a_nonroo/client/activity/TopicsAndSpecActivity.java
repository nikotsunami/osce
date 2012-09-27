package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.Paging;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.TopicsAndSpecDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.TopicsAndSpecView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.TopicsAndSpecViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationRequest;
import ch.unibas.medizin.osce.client.style.resources.AdvanceCellTable;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.Sorting;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

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
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

public class TopicsAndSpecActivity extends  AbstractActivity implements TopicsAndSpecView.Presenter, TopicsAndSpecView.Delegate {


	/** Holds the applications request factory */
	private OsMaRequestFactory requests;
	/** Holds the applications placeController */
	private PlaceController placeController;
	/** Holds the applications' activityManager */
	private ActivityManager activityManager;
	/** Holds the panel in which the view will be displayed */
	private AcceptsOneWidget widget;
	/** Holds the main view managed by this activity */
	private TopicsAndSpecView view;
	/** Holds this activities' activityMapper */
	private TopicsAndSpecDetailsActivityMapper topicsAndSpecDetailsActivityMapper;
	
	//Tooltip components initialisation
	private PopupPanel toolTip;
	private HorizontalPanel toolTipContentPanel;
	private TextBox toolTipLabel;
	private IconButton toolTipChange;
	
	// Violation Changes Highlight
		public Map<String, Widget> viewMap;
		// E Violation Changes Highlight
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	//By spec to handle table change value
	private HandlerRegistration rangeChangeHandler;
	//By spec table to add data and remove
//	private CellTable<SpecialisationProxy> table;
	
	//cell table start
	/*private CellTable<SpecialisationProxy> table;*/
	private AdvanceCellTable<SpecialisationProxy> table;
	int x;
	int y;
	//cell table end
	/*By spec holds the selection model of the specialisation table */
	private SingleSelectionModel<SpecialisationProxy> selectionModel;
	
	/**
	 * go to another place
	 * @param place the place to go to
	 */
	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	/**
	 * Sets the dependencies of this activity and initializes the corresponding activity manager 
	 * @param requests The request factory to use
	 * @param placeController the place controller to use
	 */
	@Inject
	public TopicsAndSpecActivity(OsMaRequestFactory requests, PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;
		topicsAndSpecDetailsActivityMapper = new TopicsAndSpecDetailsActivityMapper(requests, placeController);
		this.activityManager = new ActivityManager(topicsAndSpecDetailsActivityMapper, requests.getEventBus());

	}

	/**
	 * Clean up activity on finish (close popups and disable display of current activities view)
	 */
	public void onStop() {

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
	
	public Sorting sortorder = Sorting.ASC;
	public String sortname = "name";
	public String searchFilter="";
	
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
	
	// Violation Changes Highlight
		TopicsAndSpecView topicsAndSpecView;
		// E Violation Changes Highlight
	
	/**
	 * Initializes the corresponding views and initializes the tables as well as their
	 * corresponding handlers.
	 */
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("TopicsAndSpecDetailsActivityMapper.start()");
		//By SPEC[Start 
		//StandardizedPatientView systemStartView = new StandardizedPatientViewImpl();
		//final TopicsAndSpecView topicsAndSpecView = new TopicsAndSpecViewImpl();
		// Violation Changes Highlight
				topicsAndSpecView= new TopicsAndSpecViewImpl();
				// E Violation Changes Highlight
		//By SPEC] End
		topicsAndSpecView.setPresenter(this);
		initSearch();

		this.widget = panel;
		this.view = topicsAndSpecView;
		widget.setWidget(topicsAndSpecView.asWidget());
			
		//by spec
		RecordChangeEvent.register(requests.getEventBus(), (TopicsAndSpecViewImpl)view);
		//by spec
		
		MenuClickEvent.register(requests.getEventBus(), (TopicsAndSpecViewImpl)view);
		
		setTable(view.getTable());
		//cell table start
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

				Column<SpecialisationProxy, String> col = (Column<SpecialisationProxy, String>) event.getColumn();
				
				
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
					Log.info("index value--"+index +view.getPaths().get(index)+"--"+view.getPaths().get(index));
					sortname = view.getPaths().get(index);
					sortorder = (event.isSortAscending()) ? Sorting.ASC: Sorting.DESC;
					setInserted(false);
					TopicsAndSpecActivity.this.onRangeChanged();
					// By SPEC]end
					// RoleActivity.this.init2("");
					Log.info("Call Init Search from addColumnSortHandler");
					// filter.hide();
					/*setInserted(false);
					initSearch();*/
					
				}
			}
		});

		
		/*table.addColumnSortHandler(new ColumnSortEvent.Handler() {
			@Override
			public void onColumnSort(ColumnSortEvent event) {
				//By SPEC[Start
				Column<SpecialisationProxy,String> col = (Column<SpecialisationProxy,String>) event.getColumn();
				int index = table.getColumnIndex(col); 
				String[] path =	topicsAndSpecView.getPaths();	            			
				sortname = path[index];
				sortorder=(event.isSortAscending())?Sorting.ASC:Sorting.DESC;				
				//By SPEC]end
				setInserted(false);
				TopicsAndSpecActivity.this.onRangeChanged();
			}
		});
		*/
		setInserted(false);
		init();
		activityManager.setDisplay(view.getDetailsPanel());
		
		// Added by spec to add selection model on the table to handle click handler.
		ProvidesKey<SpecialisationProxy> keyProvider = ((AbstractHasData<SpecialisationProxy>) table).getKeyProvider();
		selectionModel = new SingleSelectionModel<SpecialisationProxy>(keyProvider);
		table.setSelectionModel(selectionModel);
		
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				SpecialisationProxy selectedObject = selectionModel.getSelectedObject();
				if (selectedObject != null) {
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
					view.setDetailPanel(true);
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					Log.debug(selectedObject.getName() + " selected!");
					showDetails(selectedObject);
				}
				else{
					view.setDetailPanel(false);
				}
			}
		});
		
		
		view.setDelegate(this);
		
		
	}
	
	
	/**
	 * Added By spec
	 * Shows the details place for a given SpecialisationProxy
	 * @param SpecialisationProxy the specialisation of whom details should be displayed
	 */
	protected void showDetails(SpecialisationProxy specialisation) {
		Log.debug(specialisation.getName());
		goTo(new TopicsAndSpecDetailsPlace(specialisation.stableId(), Operation.DETAILS)); 
	}
	
	/**
	 * Initializes the search for standardized patients, by first 
	 * executing a count request. Execution is then continued in 
	 * StandardizedPatientCountReceiver
	 */
	@SuppressWarnings("deprecation")
	private void initSearch() {
	
	}
	private void setTable(CellTable<SpecialisationProxy> table) {
		//cell table chages
		this.table = (AdvanceCellTable<SpecialisationProxy>)table;
		//cell table chages
	}
	
	@Override
	public void showSubviewClicked() {
		goTo(new TopicsAndSpecDetailsPlace(Operation.DETAILS));
	}

	//@ By spec to handle handler to add user entered data (In TextBox) in table
	@Override
	public void newClicked(String value) {
		Log.debug("Add new Specialisation");
		SpecialisationRequest specialisationReq = requests.specialisationRequest();
		SpecialisationProxy special = specialisationReq.create(SpecialisationProxy.class);
		special.setName(value);
		//Violation Changes Highlight
		Log.info("Map Size: " + topicsAndSpecView.getMap().size());		
		specialisationReq.persist().using(special).fire(new OSCEReceiver<Void>(topicsAndSpecView.getMap()){
			// E Violation Changes Highlight
		
			@Override
			public void onSuccess(Void arg0) {
				Log.info("specification data saved");
				setInserted(true);
				init();
			}
		});
		
	}
	//By spec To add data in table
	private void init() {
		Log.info("Inside INIT()");
		init2();
	}
	//By spec To add data in table
	private void init2() {

		Log.info("Inside INIT2()");
		fireCountRequest(new Receiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Nationalitäten aus der Datenbank: " + response);
				Log.info("set specialisation table size according to count");
				totalRecords = response.intValue();
				view.getTable().setRowCount(response.intValue(), true);

				onRangeChanged();
			}

		});

		rangeChangeHandler = table
				.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {
						TopicsAndSpecActivity.this.onRangeChanged();
					}
				});
	}
	//By spec To add data in table
	protected void fireCountRequest(Receiver<Long> callback) {
//		requests.nationalityRequest().countNationalitys().fire(callback);
		Log.info("Finding total specialtation size value : ");
		requests.specialisationRequestNonRoo().countSpecializations(searchFilter).fire(callback);
	}
	
	//By spec To handle table chabge lostner
	
	protected void onRangeChanged() {
		final Range range = table.getVisibleRange();

		final Receiver<List<SpecialisationProxy>> callback = new Receiver<List<SpecialisationProxy>>() {
			
			@Override
			public void onSuccess(List<SpecialisationProxy> response) {
				if(view == null){
				return;
			}
				Log.info("Successfully specialisation result value set in table");		
				// Violation Changes Highlight				
				topicsAndSpecView.getTextBox().removeStyleName("higlight_onViolation");
				// E Violation Changes Highlight
				table.setRowData(range.getStart(), response);

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

//		if(isInserted){
//
//			sortname = "id";
//			sortorder = Sorting.ASC;
//			
//			setInserted(false);
//			
//			Log.info("sortname == "+sortname);
//			Log.info("sortorder == "+sortorder);
//		}

			fireRangeRequest(range, callback);
	}
	
	private int totalRecords;
	private boolean isInserted;
	
	public boolean isInserted() {
		return isInserted;
	}

	public void setInserted(boolean isInserted) {
		this.isInserted = isInserted;
	}
	
	
	private void fireRangeRequest(final Range range, final Receiver<List<SpecialisationProxy>> callback) {
		Log.info("Inside fireRangeRequest()");
		createRangeRequest(range).with((view.getPathsArray())).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}
	protected Request<List<SpecialisationProxy>> createRangeRequest(Range range) {
		System.out.println("Calling FindAllSpecialization with value : " );
		return requests.specialisationRequestNonRoo().findAllSpecialisation(sortname,sortorder,searchFilter, range.getStart(), range.getLength());
	}

	@Override
	public void deleteClicked(SpecialisationProxy specialization) {
		Log.info("Delete clicked");
		requests.specialisationRequest().findSpecialisation(specialization.getId()).with("roleTopics").fire(new Receiver<SpecialisationProxy>() {

			@Override
			public void onSuccess(SpecialisationProxy response) {
				if(response.getRoleTopics() != null && response.getRoleTopics().size() > 0)
				{
					//Window.alert("Specialisation can not be deleted if role topic is asigned");
					// Issue Role
					 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.success());
					 dialogBox.showConfirmationDialog("Specialisation can not be deleted if role topic is asigned");
					 
					 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							dialogBox.hide();							
							Log.info("ok click");	
							return;
								}
							});

					
					
//E: Issue Role
				}
				else
				{
					requests.specialisationRequest().remove().using(response).fire(new Receiver<Void>() {
					public void onSuccess(Void ignore) 
					{
						Log.debug("Sucessfully deleted");
						setInserted(false);
						init();
					}
					});
				}
				
			}
		});
		
	}

	@Override
	public void editClicked(final SpecialisationProxy specialization,int left,int top) {
		
		//ToolTip Function Added by spec
		Log.info("ToolTip opened");
			toolTip= new PopupPanel(true);
			
			toolTip.setWidth("180px");
			toolTip.setHeight("40px");
		    toolTip.setAnimationEnabled(true);
		    
			toolTipContentPanel=new HorizontalPanel();
			
			toolTipContentPanel.setWidth("160px");
			toolTipContentPanel.setHeight("22px");
			toolTipContentPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			toolTipContentPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			toolTipLabel=new TextBox();
			
			toolTipLabel.setWidth("120px");
			toolTipLabel.setHeight("25px");
			
			toolTipChange = new IconButton(constants.save());
			toolTipChange.setIcon("disk");
		 
			toolTipChange.setWidth("40px");
			toolTipChange.setHeight("25px");       
			
			// Violation Changes Highlight
						viewMap=new HashMap<String, Widget>();
						viewMap.put("name",toolTipLabel);
						// E Violation Changes Highlight	
		
			toolTipContentPanel.add(toolTipLabel);
			toolTipContentPanel.add(toolTipChange);
		     
			toolTipLabel.setText(specialization.getName());
		       
			    
			toolTip.add(toolTipContentPanel);   // you can add any widget here
		        
			table.getColumnIndex(table.getColumn(1));
		   
			// Issue Role
			
						//toolTip.setPopupPosition(new Integer(constants.TopicsAndSpecViewPopupXPosition()),new Integer(constants.TopicsAndSpecViewPopupYPosition()));
						toolTip.setPopupPosition(left,top);
						
						// E: Issue Role
		    
		        toolTip.show();
		        
		        toolTipChange.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						// Violation Changes Highlight

						/*						requests.specialisationRequest().findSpecialisation(specialization.getId()).fire(new Receiver<SpecialisationProxy>(
														){

															@Override
															public void onSuccess(SpecialisationProxy response) {
																SpecialisationRequest spReq = requests.specialisationRequest();										
																response = spReq.edit(response);
																response.setName(toolTipLabel.getText());
																spReq.persist().using(specialization).fire(new Receiver<Void>(){
																	
																	@Override
																	public void onFailure(ServerFailure error){
																		Log.error("onFilure");
																		Log.error(error.getMessage());				
																	}
																	
																	@Override
																	public void onSuccess(Void arg0) {
																		Log.info("Save RoleTopic values value Succesfully according to ToolTip value");
																		toolTip.clear();
																		toolTip.hide();				
																		init();
																	
																	}
																});					
															}							
														});*/
												//requests.specialisationRequest().findSpecialisation(specialization.getId()).fire(new OSCEReceiverTempTest<SpecialisationProxy>(viewMap)
												requests.specialisationRequest().findSpecialisation(specialization.getId()).fire(new OSCEReceiver<SpecialisationProxy>(viewMap)
												{
																@Override
															public void onSuccess(SpecialisationProxy response) {
																SpecialisationRequest spReq = requests.specialisationRequest();										
																response = spReq.edit(response);
																response.setName(toolTipLabel.getText());
																spReq.persist().using(specialization).fire(new OSCEReceiver<Void>(viewMap){
																	
																	@Override
																	public void onFailure(ServerFailure error){
																		Log.error("onFilure");
																		Log.error(error.getMessage());				
																	}
																	
																	@Override
																	public void onSuccess(Void arg0) {
																		Log.info("Save RoleTopic values value Succesfully according to ToolTip value");
																		toolTip.clear();
																		toolTip.hide();				
																		setInserted(false);
																		init();
																	
																	}
																});					
															}							
														});		
												// E Violation Changes Highlight
										
										
						}
							
						});
						
					}

	@Override
	public void performSearch(String value) {
		searchFilter=value;
		Log.info("Searching specialisation");
		setInserted(false);
		init2();
		
	}

	}

	















