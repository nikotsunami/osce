package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.TopicsAndSpecDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.TopicsAndSpecView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.TopicsAndSpecViewImpl;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationRequest;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.Sorting;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
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
	private Button toolTipChange;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	//By spec to handle table change value
	private HandlerRegistration rangeChangeHandler;
	//By spec table to add data and remove
	private CellTable<SpecialisationProxy> table;
	
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

	
	public Sorting sortorder = Sorting.ASC;
	public String sortname = "name";
	public String searchFilter="";
	
	/**
	 * Initializes the corresponding views and initializes the tables as well as their
	 * corresponding handlers.
	 */
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("TopicsAndSpecDetailsActivityMapper.start()");
		//By SPEC[Start 
		//StandardizedPatientView systemStartView = new StandardizedPatientViewImpl();
		final TopicsAndSpecView topicsAndSpecView = new TopicsAndSpecViewImpl();
		//By SPEC] End
		topicsAndSpecView.setPresenter(this);
		initSearch();

		this.widget = panel;
		this.view = topicsAndSpecView;
		widget.setWidget(topicsAndSpecView.asWidget());
			
		
		setTable(view.getTable());
		table.addColumnSortHandler(new ColumnSortEvent.Handler() {
			@Override
			public void onColumnSort(ColumnSortEvent event) {
				//By SPEC[Start
				Column<SpecialisationProxy,String> col = (Column<SpecialisationProxy,String>) event.getColumn();
				int index = table.getColumnIndex(col); 
				String[] path =	topicsAndSpecView.getPaths();	            			
				sortname = path[index];
				sortorder=(event.isSortAscending())?Sorting.ASC:Sorting.DESC;				
				//By SPEC]end
				TopicsAndSpecActivity.this.onRangeChanged();
			}
		});
		
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
					Log.debug(selectedObject.getName() + " selected!");
					showDetails(selectedObject);
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
		this.table = table;
		
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
		
		specialisationReq.persist().using(special).fire(new Receiver<Void>(){
			@Override
			public void onSuccess(Void arg0) {
				Log.info("specification data saved");
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
				table.setRowData(range.getStart(), response);

				// finishPendingSelection();
				if (widget != null) {
					widget.setWidget(view.asWidget());
				}
			}

			
		};

			fireRangeRequest(range, callback);
	}
	
	private void fireRangeRequest(final Range range, final Receiver<List<SpecialisationProxy>> callback) {
		Log.info("Inside fireRangeRequest()");
		createRangeRequest(range).with(view.getPaths()).fire(callback);
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
					Window.alert("Specialisation can not be deleted if role topic is asigned");
				}
				else
				{
					requests.specialisationRequest().remove().using(response).fire(new Receiver<Void>() {
					public void onSuccess(Void ignore) 
					{
						Log.debug("Sucessfully deleted");
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
			
			toolTipChange = new Button("Save");
		 
			toolTipChange.setWidth("40px");
			toolTipChange.setHeight("25px");       
			
			
		
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
						requests.specialisationRequest().findSpecialisation(specialization.getId()).fire(new Receiver<SpecialisationProxy>(
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
								}
							);
										
										
						}
							
						});
						
					}

	@Override
	public void performSearch(String value) {
		searchFilter=value;
		Log.info("Searching specialisation");
		init2();
		
	}

	}

	















