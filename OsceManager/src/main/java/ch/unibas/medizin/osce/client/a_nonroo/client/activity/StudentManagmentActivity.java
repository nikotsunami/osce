package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.StudentManagementDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StudentManagementPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StudentManagementEditPopupViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StudentManagementView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StudentManagementViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentRequest;
import ch.unibas.medizin.osce.client.style.resources.AdvanceCellTable;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.Sorting;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class StudentManagmentActivity extends AbstractActivity implements StudentManagementView.Delegate, StudentManagementView.Presenter {

	private OsMaRequestFactory requests;
	private PlaceController placeControler;
	private SemesterProxy semesterProxy;
	private HandlerManager handlerManager;
	
	private ActivityManager activityManager;
	
	private AcceptsOneWidget widget;
	private SelectChangeHandler removeHandler;
	private StudentManagementView view;
	private final OsceConstants constants = GWT.create(OsceConstants.class);

	private SingleSelectionModel<StudentProxy> selectionModel;
	
	public String columnHeader;
	public String cellValue;
	public boolean rightClick = false;
	
	private int totalRecord;
	
	private Integer firstResult;
	private Integer maxResults;
	
	public Sorting sortorder = Sorting.ASC;
	public String sortname = "name";

	public int x;
	public int y;
	public String searchWord="";
	private AdvanceCellTable<StudentProxy> table;
	public List<String> path = new ArrayList<String>();
	Map<String, String> columnName;
	private HandlerRegistration placeChangeHandlerRegistration;
	
	private StudentManagementDetailsActivityMapper studentManagementDetailsActivityMapper;
	
	public StudentManagmentActivity(OsMaRequestFactory requests, PlaceController placeController, StudentManagementPlace place) {
    	this.requests = requests;
    	this.placeControler = placeController;
    	this.semesterProxy = place.semesterProxy;
    	this.handlerManager = place.handlerManager;
    	
    	studentManagementDetailsActivityMapper = new StudentManagementDetailsActivityMapper(requests, placeController);
    	this.activityManager = new ActivityManager(studentManagementDetailsActivityMapper, requests.getEventBus());
    }

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		StudentManagementView systemStartView = new StudentManagementViewImpl();
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		this.table = (AdvanceCellTable<StudentProxy>)view.getTable();
		
		 columnName=view.getSortMap();
			
		 path = systemStartView.getPaths();
			
		addColumnOnMouseout();
		
		table.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				//StudentManagmentActivity.this.onRangeChanged(false,view.getSearchBox().getValue());
				StudentManagmentActivity.this.onRangeChanged(false,searchWord);
			}
		});
		
		table.addColumnSortHandler(new ColumnSortEvent.Handler() {

			@Override
			public void onColumnSort(ColumnSortEvent event) {
				// By SPEC[Start

				Column<StudentProxy, String> col = (Column<StudentProxy, String>) event.getColumn();
				
				
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
					
					if(index < path.size()){
						
						sortname = path.get(index);
						System.out.println("sort name is :" + sortname);
	
						sortorder = (event.isSortAscending()) ? Sorting.ASC: Sorting.DESC;
	
						// By SPEC]end
						// RoleActivity.this.init2("");
						Log.info("Call Init Search from addColumnSortHandler");
						// filter.hide();
						initSearch(false,searchWord);
					}
				}
			}
		});
		
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
		
		ProvidesKey<StudentProxy> keyProvider = ((AbstractHasData<StudentProxy>) table).getKeyProvider();
		selectionModel = new SingleSelectionModel<StudentProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		// adds a selection handler to the table so that if a valid patient is selected,
		// the corresponding details view is shown (via showDetails())
		
		table.addCellPreviewHandler(new Handler<StudentProxy>() {

			@Override
			public void onCellPreview(CellPreviewEvent<StudentProxy> event) {
				
				boolean isClicked="click".equals(event.getNativeEvent().getType());
				if(isClicked){
				//Window.alert("Column Clicked :"+ event.getColumn());
				if(event.getColumn()!=6){
					StudentProxy selectedObject = selectionModel
						.getSelectedObject();
				
				if (selectedObject != null) {
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
					view.setDetailPanel(true);
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					Log.debug(selectedObject.getName() + " selected!");
					showDetails(selectedObject);
				}
				else{
					view.setDetailPanel(false);
					System.out.println("==============No Role Found===============");
				 }
				}
				}	
			}
		});
		
       	
		/*selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				StudentProxy selectedObject = selectionModel.getSelectedObject();
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
		});*/
		
		view.setDelegate(this);
		/*this.addSelectChangeHandler(new SelectChangeHandler() {			
			@Override
			public void onSelectionChange(SelectChangeEvent event) {
				semesterProxy = event.getSemesterProxy();
				
				System.out.println("SEMESTER ID : " + semesterProxy.getId());
			}
		});*/
		
		activityManager.setDisplay(view.getDetailsPanel());
		
		placeChangeHandlerRegistration = eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			@Override
			public void onPlaceChange(PlaceChangeEvent event) {
				if (event.getNewPlace() instanceof StudentManagementDetailsPlace) {
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
					view.setDetailPanel(true);
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					StudentManagementDetailsPlace place = (StudentManagementDetailsPlace) event.getNewPlace();
					if (place.getOperation() == Operation.NEW) {
						//initSearch(true);
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					}
				}
				else{
					view.setDetailPanel(false);
				}
			}
		});
		
		initSearch(true,searchWord);
		
	}
	public void onStop(){
		activityManager.setDisplay(null);

		if (placeChangeHandlerRegistration != null) {
			placeChangeHandlerRegistration.removeHandler();
		}
	}
	protected void showDetails(StudentProxy student) {
		Log.debug(student.getName());
		
		goTo(new StudentManagementDetailsPlace(student.stableId(), Operation.DETAILS));
	}
	protected void onRangeChanged(boolean isFirst,String searchValue) {
		
		final Range range = table.getVisibleRange();
		//Log.info("search word --"+searchValue);
		requests.studentRequestNonRoo().getStudents(sortname,sortorder,range.getStart(), range.getLength(),isFirst,searchValue).fire(new OSCEReceiver<List<StudentProxy>>() {

			@Override
			public void onSuccess(List<StudentProxy> response) {
				table.setRowData(range.getStart(), response);
				
			}
		});
	}


	private void initSearch(final boolean isFirst,final String searchValue) {
		
		requests.studentRequestNonRoo().getCountOfStudent(sortname,sortorder,searchValue).fire(new OSCEReceiver<Long>() {

			@Override
			public void onSuccess(Long response) {
				
				view.getTable().setRowCount(response.intValue(), true);
				//onRangeChanged(isFirst,view.getSearchBox().getValue());
				onRangeChanged(isFirst,searchValue);
			}
		});
	
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
			
			/*if (colValue.equals("edit"))
			{
				addColumn(new ActionCell<StudentProxy>(
						OsMaConstant.EDIT_ICON, new ActionCell.Delegate<StudentProxy>() {
							public void execute(StudentProxy nation) {
								//showEditPopUp(nation);
							}
						}), "", new GetValue<StudentProxy>() {
					public StudentProxy getValue(StudentProxy nation) {
						return nation;
					}
				}, null);
			}*/
			
			if( (/*!colValue.equals("edit")) && */(selectedItems.contains(colValue) || table.getInitList().contains(colValue))))
			{
				
				if(table.getInitList().contains(colValue))
				{
					table.getInitList().remove(colValue);
				}
			columnHeader = colValue;
			String colName=(String)columnName.get(columnHeader);
				path.add(colName.toString());
				path.add(" ");
				
				
			

			table.addColumn(new TextColumn<StudentProxy>() {

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
				public String getValue(StudentProxy object) {

					

					if (tempColumnHeader == constants.name()) {
						
						return renderer.render(object.getName()!=null?object.getName():"");
					} else if (tempColumnHeader == constants.preName()) {
						
						return renderer.render(object.getPreName()!=null?object.getPreName():"");
					} else if (tempColumnHeader == constants.email()) {
						
						return renderer.render(object.getEmail()!=null?object.getEmail():"");
					} 
					else {
						return "";
					}

					// return renderer.render(cellValue);
				}
			}, columnHeader, false);
			//path.add(" ");
		}
		}
		
		
		path.add("edit");
		addColumn(new ActionCell<StudentProxy>(
				OsMaConstant.EDIT_ICON, new ActionCell.Delegate<StudentProxy>() {
					public void execute(StudentProxy studentProxy) {
						view.editPopupView(studentProxy);
						//delegate.editStudentData(studentProxy);
						
					}
				}), "", new GetValue<StudentProxy>() {
			public StudentProxy getValue(StudentProxy studentProxy) {
				return studentProxy;
			}
		}, null);
		

	}

	@Override
	public void doAnimation(boolean flag) {
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(flag));
		
	}
	
	@Override
	public void goTo(Place place) {
		placeControler.goTo(place);
	}
	
	/*public void addSelectChangeHandler(SelectChangeHandler handler) 
	{
		handlerManager.addHandler(SelectChangeEvent.getType(), handler);	
		removeHandler=handler;
	}*/

	private List<AbstractEditableCell<?, ?>> editableCells;
	
	private <C> void addColumn(Cell<C> cell, String headerText,
			final GetValue<C> getter, FieldUpdater<StudentProxy, C> fieldUpdater) {
		System.out.println("in Add Column");
		
		Column<StudentProxy, C> column = new Column<StudentProxy, C>(cell) {
			@Override
			public C getValue(StudentProxy object) {
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		if (cell instanceof AbstractEditableCell<?, ?>) {
			editableCells.add((AbstractEditableCell<?, ?>) cell);
		}
		table.addColumn(column);
	}
	
	private static interface GetValue<C> {
		C getValue(StudentProxy contact);
	}


	@Override
	public void editStudentData(StudentProxy studentProxy, String name,
			String preName, String email) {
		StudentRequest studentRequest = requests.studentRequest();
		studentProxy = studentRequest.edit(studentProxy);
		
		studentProxy.setName(name);
		studentProxy.setPreName(preName);
		studentProxy.setEmail(email);
			if(email=="")
			{
				studentProxy.setEmail(null);
			}
		studentRequest.persist().using(studentProxy).fire(new OSCEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				initSearch(true,searchWord);
				
				((StudentManagementEditPopupViewImpl)view.getStudentManagementEditPopView()).hide(true);
				
			}
		});
		
		
	}

	@Override
	public void performSearch(String value) {
		// TODO Auto-generated method stub
		searchWord=value;
		//onRangeChanged(false, searchWord);
		initSearch(false, searchWord);
		Log.info("search word--"+value);
	}
	
/*	public void editPopupView(final StudentProxy studentProxy) {
		
		studentManagementEditPopupView = new StudentManagementEditPopupViewImpl();
		
		((StudentManagementEditPopupViewImpl)studentManagementEditPopupView).setAnimationEnabled(true);
		
		((StudentManagementEditPopupViewImpl)studentManagementEditPopupView).setWidth("200px");
		
		studentManagementEditPopupView.getNewName().setValue(studentProxy.getName());
		studentManagementEditPopupView.getNewPreName().setValue(studentProxy.getPreName());
		studentManagementEditPopupView.getNewEmail().setValue(studentProxy.getEmail());
		
		RootPanel.get().add(((StudentManagementEditPopupViewImpl)studentManagementEditPopupView));
		
		// Highlight onViolation
		
		studentManagementMap=new HashMap<String, Widget>();
		studentManagementMap.put("name",((StudentManagementEditPopupViewImpl)studentManagementEditPopupView).getNewName());
		studentManagementMap.put("prename",((StudentManagementEditPopupViewImpl)studentManagementEditPopupView).getNewPreName());
		studentManagementMap.put("email",((StudentManagementEditPopupViewImpl)studentManagementEditPopupView).getNewEmail());
		
		
		// E Highlight onViolation
		
		studentManagementEditPopupView.getOkBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
			
					
					
					delegate.editStudentData(studentProxy, studentManagementEditPopupView.getNewName().getValue(),studentManagementEditPopupView.getNewPreName().getValue(),studentManagementEditPopupView.getNewEmail().getValue());
									
			}
		});

		studentManagementEditPopupView.getCancelBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				
				
				((StudentManagementEditPopupViewImpl)studentManagementEditPopupView).hide(true);
			}
		});
		
		((StudentManagementEditPopupViewImpl)studentManagementEditPopupView).setPopupPosition(left-450, top - 50);
		
		((StudentManagementEditPopupViewImpl)studentManagementEditPopupView).show();
	}*/
}
