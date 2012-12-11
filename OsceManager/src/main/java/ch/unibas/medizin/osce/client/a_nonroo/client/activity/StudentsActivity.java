package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.StudentsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StudentSubDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StudentSubDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StudentsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StudentsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentOscesProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentOscesRequest;
import ch.unibas.medizin.osce.client.style.resources.AdvanceCellTable;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.StudyYears;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
/**
 * @author dk
 *
 */
public class StudentsActivity extends AbstractActivity implements StudentsView.Presenter, StudentsView.Delegate , StudentSubDetailsView.Presenter,StudentSubDetailsView.Delegate {
	
	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private StudentsView view;
	private StudentSubDetailsView view_s = new StudentSubDetailsViewImpl();
	private StudentsActivity studentsActivity;
	private StudentsPlace place;
	private int tabIndex=0; 
	StudentSubDetailsView studentSubDetailsView;

	private ActivityManager activityManager;
	private OsceProxy osceProxy;
	private SemesterProxy semesterProxy;
	//Label l1;
	private List<OsceProxy> osceProxyList = new ArrayList<OsceProxy>();
	List<OsceProxy> response;
	//private CellTable<StudentOscesProxy> table;
	
	private AdvanceCellTable<StudentOscesProxy> table;
	int x;
	int y;
	
	private StudentOscesProxy studentOscesProxy;
	public HandlerManager handlerManager;// = new HandlerManager(this);
//	private HandlerRegistration rangeChangeHandler;
	
	public int currenttab= 0;	
	public StudentSubDetailsViewImpl[] subDetailsView;
	
	public int arrarycount = 0;
	
	public String searchWord = "";
	
	public StudentsActivity(OsMaRequestFactory requests, PlaceController placeController,StudentsPlace studentsPlace) 
	{
		Log.info("Call Activity Student");
    	this.requests = requests;
    	this.placeController = placeController;
    	this.place = studentsPlace;
    	this.handlerManager = studentsPlace.handler;
		this.semesterProxy=studentsPlace.semesterProxy;
		
		studentsActivity = this;

		this.addSelectChangeHandler(new SelectChangeHandler() 
		{			
			@Override
			public void onSelectionChange(SelectChangeEvent event) 
			{			
				semesterProxy= event.getSemesterProxy();
				currenttab = 0;
				init();
			}
		});
	}
	
	
	public void addSelectChangeHandler(SelectChangeHandler handler) {
		handlerManager.addHandler(SelectChangeEvent.getType(), handler);
		
	}
	
	public StudentsActivity(OsMaRequestFactory requests , PlaceController placeController) {
		
		this.requests = requests;
    	this.placeController = placeController;
	}

	
	public void onStop(){
		
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		
		this.widget = panel;
		init();
		
		/*view.getStudentTabPanel1().addSelectionHandler(new SelectionHandler<Integer>() {
			
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				
				Widget w = view.getStudentTabPanel1().getWidget(event.getSelectedItem());
				
				currenttab = event.getSelectedItem();
				
				System.out.println("CURRENT TAB : " + currenttab);
				
				osceProxy = osceProxyList.get(currenttab);
				
				subDetailsView[currenttab].getHidden().setValue(String.valueOf(osceProxy.getId()));
				
				if(tabIndex == event.getSelectedItem())
				{
					view_s = subDetailsView[event.getSelectedItem()];
					
					//init2("");
					onRangeChanged("");
				}
				view_s.getHidden().setValue(String.valueOf(osceProxy.getId()));
			
			}
		});*/

	}
	
	private void tabSelectChangeHandler()
	{
		view.getStudentTabPanel1().addSelectionHandler(new SelectionHandler<Integer>() {
			
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				
				Widget w = view.getStudentTabPanel1().getWidget(event.getSelectedItem());
				
				currenttab = event.getSelectedItem();
				
				System.out.println("CURRENT TAB : " + currenttab);
				
				osceProxy = osceProxyList.get(currenttab);
				
				subDetailsView[currenttab].getHidden().setValue(String.valueOf(osceProxy.getId()));
				
				onRangeChanged("");
				
				if(tabIndex == event.getSelectedItem())
				{
					view_s = subDetailsView[event.getSelectedItem()];
					
					view_s.getTable().setVisibleRange(0, OsMaConstant.TABLE_PAGE_SIZE);
					//init2("");
					onRangeChanged("");
				}
				view_s.getHidden().setValue(String.valueOf(osceProxy.getId()));
			
			}
		});
	}

	private void init(){
		StudentsView systemStartView = new StudentsViewImpl();
		systemStartView.setPresenter(this);
		this.view = systemStartView;
		
		
		MenuClickEvent.register(requests.getEventBus(), (StudentsViewImpl)view);
		widget.setWidget(systemStartView.asWidget());
		view.setDelegate(this);
		
		requests.osceRequestNonRoo().findAllOsceOnSemesterId(semesterProxy.getId()).with("semester","osceStudents.student").fire(new OSCEReceiver<List<OsceProxy>>() {

			@Override
			public void onSuccess(List<OsceProxy> response) {
				
				subDetailsView = new StudentSubDetailsViewImpl[response.size()];
				
				tabIndex=0;
				
				if(response != null && response.size() > 0){
				osceProxyList=response;
		
				Iterator<OsceProxy> osceList = response.iterator();
				osceProxy = response.get(0);
				while(osceList.hasNext()){
					
					final OsceProxy tempOsceProxy = osceList.next();
					String osceLable = new EnumRenderer<StudyYears>().render(tempOsceProxy.getStudyYear()) + "." + tempOsceProxy.getSemester().getSemester().name();
				
					final StudentSubDetailsViewImpl studentSubDetailsViewImpl = new StudentSubDetailsViewImpl(tempOsceProxy);
					subDetailsView[tabIndex]=studentSubDetailsViewImpl;
					//	final StudentSubDetailsViewImpl studentSubDetailsView1 = new StudentSubDetailsViewImpl(osceProxy);
					studentSubDetailsViewImpl.setDelegate(studentsActivity);
					
					RecordChangeEvent.register(requests.getEventBus(), studentSubDetailsViewImpl);
					
					view.getStudentTabPanel1().insert(studentSubDetailsViewImpl, osceLable,tabIndex);
				//view.getStudentTabPanel1().selectTab(0);
			//	view.getStudentTabPanel1().insert(studentSubDetailsView1, "test", 0);
//				StudentTabByTabSelection(1,response);
//				if(tabIndex == 0)
//				{
//					view_s = studentSubDetailsView;
//					init2("");
//				}
					studentSubDetailsViewImpl.getTable().addRangeChangeHandler(new RangeChangeEvent.Handler() {
						
						@Override
						public void onRangeChange(RangeChangeEvent event) {
							StudentsActivity.this.onRangeChanged(searchWord);
						}
					});
					
					table=(AdvanceCellTable<StudentOscesProxy>)studentSubDetailsViewImpl.getTable();
					
					((AdvanceCellTable<StudentOscesProxy>)studentSubDetailsViewImpl.getTable()).addHandler(new MouseDownHandler() {

							@Override
							public void onMouseDown(MouseDownEvent event) {
								// TODO Auto-generated method stub
								Log.info("mouse down");
								x = event.getClientX();
								y = event.getClientY();

								if(((AdvanceCellTable<StudentOscesProxy>)studentSubDetailsViewImpl.getTable()).getRowCount()>0)
								{
								Log.info(((AdvanceCellTable<StudentOscesProxy>)studentSubDetailsViewImpl.getTable()).getRowElement(0).getAbsoluteTop() + "--"+ event.getClientY());

								
								if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT&& event.getClientY() < ((AdvanceCellTable<StudentOscesProxy>)studentSubDetailsViewImpl.getTable()).getRowElement(0).getAbsoluteTop()) {
									
									((AdvanceCellTable<StudentOscesProxy>)studentSubDetailsViewImpl.getTable()).getPopup().setPopupPosition(x, y);
									((AdvanceCellTable<StudentOscesProxy>)studentSubDetailsViewImpl.getTable()).getPopup().show();

									Log.info("right event");
								}
								}
								else
								{
									if(event.getNativeButton() == NativeEvent.BUTTON_RIGHT)
									{
										((AdvanceCellTable<StudentOscesProxy>)studentSubDetailsViewImpl.getTable()).getPopup().setPopupPosition(x, y);
										((AdvanceCellTable<StudentOscesProxy>)studentSubDetailsViewImpl.getTable()).getPopup().show();
									}
								}
							}
						}, MouseDownEvent.getType());
						
					((AdvanceCellTable<StudentOscesProxy>)studentSubDetailsViewImpl.getTable()).getPopup().addDomHandler(new MouseOutHandler() {

							@Override
							public void onMouseOut(MouseOutEvent event) {
								// TODO Auto-generated method stub
								//addColumnOnMouseout();
								((AdvanceCellTable<StudentOscesProxy>)studentSubDetailsViewImpl.getTable()).getPopup().hide();
								
							}
						}, MouseOutEvent.getType());
						
					((AdvanceCellTable<StudentOscesProxy>)studentSubDetailsViewImpl.getTable()).addColumnSortHandler(new ColumnSortEvent.Handler() {

							@Override
							public void onColumnSort(ColumnSortEvent event) {
								// By SPEC[Start

								Column<StudentOscesProxy, String> col = (Column<StudentOscesProxy, String>) event.getColumn();
								
								
								int index = ((AdvanceCellTable<StudentOscesProxy>)studentSubDetailsViewImpl.getTable()).getColumnIndex(col);
								
								/*String[] path =	systemStartView.getPaths();	            			
								sortname = path[index];
								sortorder=(event.isSortAscending())?Sorting.ASC:Sorting.DESC;	*/
								
								if (index % 2 == 1 ) {
									
									((AdvanceCellTable<StudentOscesProxy>)studentSubDetailsViewImpl.getTable()).getPopup().setPopupPosition(x, y);
									((AdvanceCellTable<StudentOscesProxy>)studentSubDetailsViewImpl.getTable()).getPopup().show();

								} 
							}
						});
					requests.studentOsceRequestNonRoo().countStudentByRange(tempOsceProxy.getId(), "").fire(new OSCEReceiver<Integer>() {

						@Override
						public void onSuccess(final Integer data) {
							studentSubDetailsViewImpl.getTable().setRowCount(data);
							
							final Range range = studentSubDetailsViewImpl.getTable().getVisibleRange();
							requests.studentOsceRequestNonRoo().findStudentByRange(range.getStart(), range.getLength(), tempOsceProxy.getId(), "").with("student").fire(new OSCEReceiver<List<StudentOscesProxy>>() {
								@Override
								public void onSuccess(List<StudentOscesProxy> value) {
									//table.setRowCount(value.size());
									studentSubDetailsViewImpl.getTable().setRowData(range.getStart(), value);
									//table.setRowCount(data);
								}
							});
						}
					});
					
				tabIndex++;
				}
				}
			}
		});
			
		tabSelectChangeHandler();
	}

	
	@Override
	public void goTo(Place place) {
		
	}
	
	@Override
	public void performSearch(String q) {
		
		//init2(q);
		StudentSubDetailsView tempdetlview = subDetailsView[currenttab];		
		view_s = tempdetlview;
		//table = view_s.getTable();
		this.table = (AdvanceCellTable<StudentOscesProxy>)view_s.getTable();
		
		searchWord = q;
		
		onRangeChanged(searchWord);
	}
	
	public void searchTest(String q)
	{
		final Range range = table.getVisibleRange();
		
		requests.studentOsceRequestNonRoo().findStudentEntriesByNameTest(q, osceProxy.getId()).with("student").fire(new OSCEReceiver<List<StudentOscesProxy>>() {

			@Override
			public void onSuccess(List<StudentOscesProxy> response) {
				table.setRowCount(response.size());
				table.setRowData(response);
			}
		});
	}

	private void init2(final String q) {
	
		StudentSubDetailsView tempdetlview = subDetailsView[currenttab];		
		view_s = tempdetlview;
		//table = view_s.getTable();
		this.table = (AdvanceCellTable<StudentOscesProxy>)view_s.getTable();
		/*fireCountRequest(q, new Receiver<Long>() {

			@Override
			public void onSuccess(Long response) {
				table.setRowCount(response.intValue(), true);
				onRangeChanged(q);
			}
		});*/
		
	}
	protected void onRangeChanged(final String name) {
		//table = subDetailsView[currenttab].getTable();
		this.table = (AdvanceCellTable<StudentOscesProxy>)subDetailsView[currenttab].getTable();
		requests.studentOsceRequestNonRoo().countStudentByRange(osceProxy.getId(), name).fire(new OSCEReceiver<Integer>() {

			@Override
			public void onSuccess(final Integer data) {
				table.setRowCount(data);
				
				final Range range = subDetailsView[currenttab].getTable().getVisibleRange();
				
				requests.studentOsceRequestNonRoo().findStudentByRange(range.getStart(), range.getLength(), osceProxy.getId(), name).with("student").fire(new OSCEReceiver<List<StudentOscesProxy>>() {
					@Override
					public void onSuccess(List<StudentOscesProxy> value) {
						//table.setRowCount(value.size());
						table.setRowData(range.getStart(), value);
						//table.setRowCount(data);
					}
				});
			}
		});
		
		
	}
	
	private void fireRangeRequest(String name, final Range range, final Receiver<List<StudentOscesProxy>> callback) {
		createRangeRequest(name, range).with(view_s.getPaths()).fire(callback);
	}
	
	protected Request<List<StudentOscesProxy>> createRangeRequest(String name, Range range) {
		return requests.studentOsceRequestNonRoo().findStudentEntriesByName(name,osceProxy.getId(), range.getStart(), range.getLength()).with("student");
	}

	protected void fireCountRequest(String name, Receiver<Long> callback) {
		requests.studentOsceRequestNonRoo().countStudentByName(name,osceProxy.getId()).fire(callback);
	}
		
		
	Boolean temp = null;

	@Override
	public Boolean onRender(StudentOscesProxy studentOscesProxy) {
		
		displayLoadingScreen(true);
		
			temp = studentOscesProxy.getIsEnrolled();			
		
			StudentOscesRequest request = requests.studentOscesRequest();
			
			studentOscesProxy = request.edit(studentOscesProxy);

			studentOscesProxy.setIsEnrolled(!studentOscesProxy.getIsEnrolled());
		
			request.persist().using(studentOscesProxy).fire(new OSCEReceiver<Void>() {

						@Override
						public void onSuccess(Void response) {
							temp = !temp;
							
							refreshdata();
							
							displayLoadingScreen(false);
						}
					});
			
			
			
			return temp;
	}

	
	public void refreshdata()
	{
		/*requests.studentOsceRequestNonRoo().findStudentOsceByOsce(osceProxy.getId()).with("student").fire(new OSCEReceiver<List<StudentOscesProxy>>() {

			@Override
			public void onSuccess(List<StudentOscesProxy> response) {
				subDetailsView[currenttab].getTable().setRowData(response);
				subDetailsView[currenttab].getTable().setRowCount(response.size());
				subDetailsView[currenttab].getTable().setVisibleRange(0, OsMaConstant.TABLE_PAGE_SIZE);
			}});*/
		
		displayLoadingScreen(true);
		
		requests.studentOsceRequestNonRoo().countStudentByRange(osceProxy.getId(), "").fire(new OSCEReceiver<Integer>() {

			@Override
			public void onSuccess(final Integer data) {
				subDetailsView[currenttab].getTable().setRowCount(data);
				
				final Range range = subDetailsView[currenttab].getTable().getVisibleRange();
				requests.studentOsceRequestNonRoo().findStudentByRange(range.getStart(), range.getLength(), osceProxy.getId(), "").with("student").fire(new OSCEReceiver<List<StudentOscesProxy>>() {
					@Override
					public void onSuccess(List<StudentOscesProxy> value) {
						//table.setRowCount(value.size());
						subDetailsView[currenttab].getTable().setRowData(range.getStart(), value);
						//table.setRowCount(data);
						
						displayLoadingScreen(false);
					}
				});
			}
		});
	}

	@Override
	public void deleteClicked(StudentOscesProxy scar) {
		
	}


	@Override
	public void importClicked() {
		arrarycount = 0;
		onRangeChanged("");
	}
	
	@Override
	public void displayLoadingScreen(boolean value) {
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(value));
	}
}
