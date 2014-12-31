package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.DoctorDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.DoctorView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.DoctorViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.style.resources.AdvanceCellTable;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.Sorting;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class DoctorActivity extends AbstractActivity implements
DoctorView.Presenter, DoctorView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private DoctorView view;
	//cell table changes strat
	/*private CellTable<DoctorProxy> table;*/
	private AdvanceCellTable<DoctorProxy> table;
	//cell table changes end
	private SingleSelectionModel<DoctorProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	private ActivityManager activityManger;
	private DoctorDetailsActivityMapper DoctorDetailsActivityMapper;
	private String quickSearchTerm = "";
	private HandlerRegistration placeChangeHandlerRegistration;
	int x;
	int y;
	public String columnHeader;
	public String cellValue;
	public List<String> path = new ArrayList<String>();
	Map<String, String> columnName;
	 public String sortname = "name";
	 public Sorting sortorder = Sorting.ASC;
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	public DoctorActivity(OsMaRequestFactory requests, PlaceController placeController) {
    	this.requests = requests;
    	this.placeController = placeController;
    	DoctorDetailsActivityMapper = new DoctorDetailsActivityMapper(requests, placeController);
		this.activityManger = new ActivityManager(DoctorDetailsActivityMapper, requests.getEventBus());
    }

	public void onStop(){
		activityManger.setDisplay(null);

		if (placeChangeHandlerRegistration != null) {
			placeChangeHandlerRegistration.removeHandler();
		}
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
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		DoctorView systemStartView = new DoctorViewImpl();
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		setTable(view.getTable());
		columnName=view.getSortMap();
		addColumnOnMouseout();
		
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
						addColumnOnMouseout();
						table.getPopup().hide();
						
					}
				}, MouseOutEvent.getType());
				
				table.addColumnSortHandler(new ColumnSortEvent.Handler() {

					@Override
					public void onColumnSort(ColumnSortEvent event) {
						// By SPEC[Start

						Column<DoctorProxy, String> col = (Column<DoctorProxy, String>) event.getColumn();
						
						
						int index = table.getColumnIndex(col);
						
						Log.info("column sort--"+index);
						if (index % 2 == 1 ) {
							
							table.getPopup().setPopupPosition(x, y);
							table.getPopup().show();

						} 
						 else {
								
							
								// path = systemStartView.getPaths();
								Log.info("call for sort " + path.size() + "--index--"+ index);
								sortname = path.get(index);

								sortorder = (event.isSortAscending()) ? Sorting.ASC: Sorting.DESC;
								// By SPEC]end
								// RoleActivity.this.init2("");
								Log.info("Call Init Search from addColumnSortHandler--"+sortname+"--"+sortorder);
								// filter.hide();
								initFilterTitleFill();
								initSearch();

								
							}
					}
				});
				/*celltable changes end*/
				

		RecordChangeEvent.register(requests.getEventBus(), (DoctorViewImpl)view);
		
		MenuClickEvent.register(requests.getEventBus(), (DoctorViewImpl)view);
		
		//Issue # 122 : Replace pull down with autocomplete.
		//view.getFilterTitle().clear();
		//Issue # 122 : Replace pull down with autocomplete.
		initFilterTitleFill();
		initSearch();

		activityManger.setDisplay(view.getDetailsPanel());

		// Inherit the view's key provider
		ProvidesKey<DoctorProxy> keyProvider = ((AbstractHasData<DoctorProxy>) table)
				.getKeyProvider();
		selectionModel = new SingleSelectionModel<DoctorProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						DoctorProxy selectedObject = selectionModel
								.getSelectedObject();
						if (selectedObject != null) {
							Log.debug(selectedObject.getName()
									+ " selected!");
							showDetails(selectedObject);
						}
					}
				});

		view.setDelegate(this);
		placeChangeHandlerRegistration = eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			@Override
			public void onPlaceChange(PlaceChangeEvent event) {
				if (event.getNewPlace() instanceof DoctorDetailsPlace) {
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
					view.setDetailPanel(true);
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					DoctorDetailsPlace place = (DoctorDetailsPlace) event.getNewPlace();
					if (place.getOperation() == Operation.NEW) {
						initSearch();
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					}
				}
				else{
					view.setDetailPanel(false);
				}
			}
		});
		
	}
	
	//Module 6 Start
	private void initFilterTitleFill()
	{
		requests.clinicRequest().findAllClinics().fire(new Receiver<List<ClinicProxy>>() {

			@Override
			public void onSuccess(List<ClinicProxy> resonse) {
				
				//Issue # 122 : Replace pull down with autocomplete.
				DefaultSuggestOracle<ClinicProxy> suggestOracle1 = (DefaultSuggestOracle<ClinicProxy>) view.getSuggestBox().getSuggestOracle();
				suggestOracle1.setPossiblilities(resonse);
				view.getSuggestBox().setSuggestOracle(suggestOracle1);

				view.getSuggestBox().setRenderer(new AbstractRenderer<ClinicProxy>() {

						@Override
						public String render(ClinicProxy object) {
							// TODO Auto-generated method stub
							if(object!=null)
							{
							return object.getName();
							}
							else
							{
								return "";
							}
						}
					});
				
				

				/*view.getFilterTitle().addItem("", "0");
				ClinicProxy temp = null;
				for (int i=0; i<resonse.size(); i++)
				{
					temp = resonse.get(i);
					view.getFilterTitle().addItem(temp.getName(), String.valueOf(temp.getId()));
				}
				*/
				//Issue # 122 : Replace pull down with autocomplete.
			}
			
		});
	}
	//Module 6 End
	
	private void initSearch() {
		
		if(rangeChangeHandler!=null){
			rangeChangeHandler.removeHandler();
			rangeChangeHandler=null;
		}

		fireCountRequest(quickSearchTerm, new Receiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Intitution aus der Datenbank: " + response);
				view.getTable().setRowCount(response.intValue(), true);

				onRangeChanged(quickSearchTerm);
			}

		});

		rangeChangeHandler = table
				.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {
						DoctorActivity.this.onRangeChanged(quickSearchTerm);
					}
				});
	}
	
	protected void showDetails(DoctorProxy Doctor) {
		
		Log.debug(Doctor.getName());
		
		goTo(new DoctorDetailsPlace(Doctor.stableId(),
				Operation.DETAILS));
	}
	

	protected void onRangeChanged(String q) {
		final Range range = table.getVisibleRange();

		final Receiver<List<DoctorProxy>> callback = new Receiver<List<DoctorProxy>>() {
			@Override
			public void onSuccess(List<DoctorProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
//				idToRow.clear();
//				idToProxy.clear();
//				for (int i = 0, row = range.getStart(); i < values.size(); i++, row++) {
//					DoctorProxy Doctor = values.get(i);
//					@SuppressWarnings("unchecked")
//					// Why is this cast needed?
//					EntityProxyId<DoctorProxy> proxyId = (EntityProxyId<DoctorProxy>) Doctor
//							.stableId();
//
//				}
				table.setRowData(range.getStart(), values);

				// finishPendingSelection();
				if (widget != null) {
					widget.setWidget(view.asWidget());
				}
			}
		};

		fireRangeRequest(q, range, callback);

	}
	
	private void fireRangeRequest(String q, final Range range, final Receiver<List<DoctorProxy>> callback) {
		createRangeRequest(q, range).with(view.getPaths()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}
	
	protected Request<List<DoctorProxy>> createRangeRequest(String q, Range range) {
//		return requests.doctorRequest().findDoctorEntries(range.getStart(), range.getLength());
		//final Range newRange = table.getVisibleRange();
		if(view.getSuggestBox().getSelected()==null)
		{
			//return requests.doctorRequest().findDoctorsBySearch(q, range.getStart(), range.getLength(),sortorder,sortname);
			return requests.doctorRequest().findDoctorsBySearchWithClinic(q,null, range.getStart(), range.getLength(),sortorder,sortname);
		}
		else
		{
			return requests.doctorRequest().findDoctorsBySearchWithClinic(q,view.getSuggestBox().getSelected().getId(), range.getStart(), range.getLength(),sortorder,sortname);
		}
	}

	protected void fireCountRequest(String q, Receiver<Long> callback) {
//		requests.doctorRequest().countDoctors().fire(callback);
		if(view.getSuggestBox().getSelected()==null)
		{
			//requests.doctorRequest().countDoctorsBySearch(q).fire(callback);
			requests.doctorRequest().countDoctorsBySearchWithClinic(q,null).fire(callback);
		}
		else
		{
		
			requests.doctorRequest().countDoctorsBySearchWithClinic(q,view.getSuggestBox().getSelected().getId()).fire(callback);
		}
		
	}

	private void setTable(CellTable<DoctorProxy> table) {
		//cell atble changes start
		//this.table = table;
		this.table = (AdvanceCellTable<DoctorProxy>)table;
		//cell table changes end
		
	}

	@Override
	public void newClicked() {
		Log.info("create clicked");
		placeController.goTo(new DoctorDetailsPlace(Operation.CREATE));
	}
	
	@Override
	public void performSearch(String q) {
		quickSearchTerm = q;
		Log.debug("Search for " + q);
		initSearch();
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
		
	}

	//Module 6 Start
	
	//Issue # 122 : Replace pull down with autocomplete.
	@Override
	public void changeFilterTitleShown(ClinicProxy selectedTitle) {
		initFilterTitleFill();
		initSearch();
		
		/*if (selectedTitle != null)
		{
		
			requests.doctorRequest().findDoctorByClinicID(selectedTitle.getId()).with("office").fire(new OSCEReceiver<List<DoctorProxy>>() {

				@Override
				public void onSuccess(List<DoctorProxy> response) {
					view.getTable().setRowCount(response.size());
					view.getTable().setRowData(response);
				
					table.setRowCount(response.size());
					table.setRowData(response);
				}
			});
		}
		else
		{
			requests.doctorRequest().findAllDoctors().with("office").fire(new OSCEReceiver<List<DoctorProxy>>() {

				@Override
				public void onSuccess(List<DoctorProxy> response) {
					view.getTable().setRowCount(response.size());
					view.getTable().setRowData(response);
					view.getTable().setVisibleRange(0, OsMaConstant.TABLE_PAGE_SIZE);
					table.setRowCount(response.size());
					table.setRowData(response);
					table.setVisibleRange(0, OsMaConstant.TABLE_PAGE_SIZE);
				}
			});
		}*/
		
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
			Log.info("colvalue--"+colValue);
			if(selectedItems.contains(colValue) || table.getInitList().contains(colValue))
			{
				
				if(table.getInitList().contains(colValue))
				{
					table.getInitList().remove(colValue);
				}
			
			columnHeader = colValue;
			Log.info("colheader--"+columnHeader);
			String colName=(String)columnName.get(columnHeader);
			Log.info("colname--"+colName);	
			path.add(colName.toString());
				path.add(" ");
				
				
			

			table.addColumn(new TextColumn<DoctorProxy>() {

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
				public String getValue(DoctorProxy object) {

					

					if (tempColumnHeader == constants.name()) {
						
						return renderer.render(object.getName()!=null?object.getName():"");
					} else if (tempColumnHeader == constants.preName()) {
						
						return renderer.render(object.getPreName()!=null?object.getPreName():"");
					} else if (tempColumnHeader == constants.email()) {
						
						return renderer.render(object.getEmail()!=null?object.getEmail():"");
					} else if (tempColumnHeader == constants.title()) {
						
						return renderer.render(object.getTitle()!=null?object.getTitle():"");
					} else if (tempColumnHeader == constants.telephone()) {
						
						return renderer.render(object.getTelephone()!=null?object.getTelephone():"");
						
					} else if (tempColumnHeader == constants.clinic()) {
						return renderer.render(object.getClinic()!=null?object.getClinic().getName():"");
						
					}/*else if (tempColumnHeader == constants.officeDetails()) {
						if(object.getOffice()!=null)
						{
							String officeDetail=" ";
							if(object.getOffice().getTitle()!=null)
							{
								officeDetail=officeDetail+object.getOffice().getTitle();
							}
							if(object.getOffice().getName()!=null)
							{
								officeDetail=officeDetail+" "+object.getOffice().getName();
							}
							if(object.getOffice().getPreName()!=null)
							{
								officeDetail=officeDetail+" "+object.getOffice().getPreName();
							}
							return renderer.render(officeDetail);
						}
						else
						{
							return renderer.render("");
						}
						
						//return renderer.render(object.getOffice()!=null?object.getOffice().getName():"");
						
					} */
					else {
						return "";
					}

					// return renderer.render(cellValue);
				}
			}, columnHeader, false);
			//path.add(" ");
		}
		}

	}
	
/*	@Override
	public void changeFilterTitleShown() {
	
		Log.info("change event in changeFilterTitleShown method");
		if (view.getSuggestBox().getSelected()==null)
		{
		
			requests.doctorRequest().findDoctorByClinicID(Long.parseLong("0")).with("office").fire(new OSCEReceiver<List<DoctorProxy>>() {

				@Override
				public void onSuccess(List<DoctorProxy> response) {
					view.getTable().setRowCount(response.size());
					view.getTable().setRowData(response);
				
	}
			});
		}
		else
		{
			requests.doctorRequest().findAllDoctors().with("office").fire(new OSCEReceiver<List<DoctorProxy>>() {

				@Override
				public void onSuccess(List<DoctorProxy> response) {
					view.getTable().setRowCount(response.size());
					view.getTable().setRowData(response);
				}
			});
		}
		
	}*/
	//Issue # 122 : Replace pull down with autocomplete.
	//Module 6 End

}
