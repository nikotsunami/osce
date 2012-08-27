package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.StudentsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StudentsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StudentsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StudentSubDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StudentSubDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentOscesProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentOscesRequest;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.Range;
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
	private CellTable<StudentOscesProxy> table;
	private StudentOscesProxy studentOscesProxy;
	public HandlerManager handlerManager;// = new HandlerManager(this);
	private HandlerRegistration rangeChangeHandler;
	
	public int currenttab= 0;	
	public StudentSubDetailsViewImpl[] subDetailsView;
	
	public int arrarycount = 0;
	
	String q = "";
	
	public StudentsActivity(OsMaRequestFactory requests, PlaceController placeController,StudentsPlace studentsPlace) 
	{
    	
		Log.info("Call Activity Student");
    	this.requests = requests;
    	this.placeController = placeController;
    	this.place = studentsPlace;
    	this.handlerManager = studentsPlace.handler;
		this.semesterProxy=studentsPlace.semesterProxy;
		
		Log.info("~~In Activity : " + semesterProxy.getId());
		
		studentsActivity = this;

		Log.info("Semester Proxy : " + semesterProxy.getCalYear() + " :in Student Constructor.");
		
		
		this.addSelectChangeHandler(new SelectChangeHandler() 
		{			
			@Override
			public void onSelectionChange(SelectChangeEvent event) 
			{			
				Log.info("Call Role Activity");					
				Log.info("onSelectionChange Get Semester: " + event.getSemesterProxy().getCalYear());		
				semesterProxy= event.getSemesterProxy();
				
			
				
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
		
		view.getStudentTabPanel1().addSelectionHandler(new SelectionHandler<Integer>() {
			
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
			
				Log.info("~~ Selected Item : " + event.getSelectedItem());
				Widget w = view.getStudentTabPanel1().getWidget(event.getSelectedItem());
				
				Log.info("~~ Selected Item : " + (w instanceof StudentSubDetailsViewImpl));
			
				currenttab = event.getSelectedItem();
				
				Log.info("~~Current Tab : " + currenttab);
				
				osceProxy = osceProxyList.get(currenttab);
				
				Log.info("~~OSCEPROXY ID : " + osceProxy.getId());
				
				subDetailsView[currenttab].getHidden().setValue(String.valueOf(osceProxy.getId()));
				
				if(tabIndex == event.getSelectedItem())
				{
					view_s = subDetailsView[event.getSelectedItem()];
					
					init2("");
				}
				System.out.println("view--"+view_s);
				view_s.getHidden().setValue(String.valueOf(osceProxy.getId()));
			
				Log.info("After TabIndex");
			}
		});

}
	

	

	private void init(){
		Log.info("aa");
	
	//	init2("");
		
		StudentsView systemStartView = new StudentsViewImpl();
		systemStartView.setPresenter(this);
		this.view = systemStartView;
		Log.info("aa111");
		widget.setWidget(systemStartView.asWidget());
		view.setDelegate(this);
		Log.info("aa222");
	
		Log.info("~~Semester Proxy : " + semesterProxy.getId());
		
		
		
		
		
		requests.osceRequestNonRoo().findAllOsceOnSemesterId(semesterProxy.getId()).with("semester","osceStudents.student").fire(new OSCEReceiver<List<OsceProxy>>() {

			@Override
			public void onSuccess(List<OsceProxy> response) {
				
				Log.info("~~Inside OSCEREQUSTNONROO");
				
				subDetailsView = new StudentSubDetailsViewImpl[response.size()];
				
				tabIndex=0;
				
				if(response != null && response.size() > 0){
				osceProxyList=response;
		
				Iterator<OsceProxy> osceList = response.iterator();
				
				
				
				osceProxy = response.get(0);
				while(osceList.hasNext()){
					
				
				Log.info("OSce Proxy index : " + tabIndex);
		
				OsceProxy osceProxy = osceList.next();
				String osceLable = osceProxy.getStudyYear()==null?"":osceProxy.getStudyYear() + "." + osceProxy.getSemester().getSemester().name();
				
				final StudentSubDetailsViewImpl studentSubDetailsView = new StudentSubDetailsViewImpl(osceProxy);
				subDetailsView[tabIndex]=studentSubDetailsView;
			//	final StudentSubDetailsViewImpl studentSubDetailsView1 = new StudentSubDetailsViewImpl(osceProxy);
				studentSubDetailsView.setDelegate(studentsActivity);
		System.out.println("insert before--"+studentSubDetailsView);
				view.getStudentTabPanel1().insert(studentSubDetailsView, osceLable,tabIndex);
				//view.getStudentTabPanel1().selectTab(0);
			//	view.getStudentTabPanel1().insert(studentSubDetailsView1, "test", 0);
//				StudentTabByTabSelection(1,response);
//				if(tabIndex == 0)
//				{
//					view_s = studentSubDetailsView;
//					init2("");
//				}
		System.out.println("ARRAY COUNT--"+arrarycount);
		requests.studentOsceRequestNonRoo().findStudentOsceByOsce(osceProxy.getId()).with("student").fire(new OSCEReceiver<List<StudentOscesProxy>>() {

					@Override
					public void onSuccess(List<StudentOscesProxy> response) {
						// TODO Auto-generated method stub
						studentSubDetailsView.getTable().setRowData(response);
						Log.info("~~STATUS : " + studentSubDetailsView.getTable().getRowCount());
						subDetailsView[arrarycount] = studentSubDetailsView;
						arrarycount++;
					}
									});
		
				
		
				
		
				tabIndex++;
		

				}
			
				
				}
			}
		});
		
		
		
				
	}

	
	@Override
	public void goTo(Place place) {
		// TODO Auto-generated method stub
		
	}


	


	@Override
	public void performSearch(String q) {
		
		Log.debug("Search for " + q);
		init2(q);
	}

	private void init2(final String q) {
		System.out.println("Size--"+subDetailsView.length + "current tab--"+currenttab);
		StudentSubDetailsView tempdetlview = subDetailsView[currenttab];
		
		view_s = tempdetlview;
		System.out.println("View-s--"+view_s);
		
		table = view_s.getTable();
		/*
		// fix to avoid having multiple rangeChangeHandlers attached
		if (rangeChangeHandler!=null){
			rangeChangeHandler.removeHandler();
		}

		fireCountRequest(q, new Receiver<Long>() {
			@Override
			public void onSuccess(Long responses) {
				if (view_s == null) {
					// This activity is dead
					return;
				}
				Log.debug("Response size " + responses.intValue());				
				view_s.getTable().setRowCount(responses.intValue(), true);
				//table = view_s.getTable();
				Log.info("value of " + q);
				
				onRangeChanged(q);
			}
		});*/
	}
	protected void onRangeChanged(String q) {
		
		
		
		final Range range = table.getVisibleRange();
		
		//Log.info("~~Table Range : " + range.getLength());

		final Receiver<List<StudentOscesProxy>> callback = new Receiver<List<StudentOscesProxy>>() {
			@Override
			public void onSuccess(List<StudentOscesProxy> values) {
				if (view_s == null) {
					// This activity is dead
					return;
				}
				
				Log.info("~~Inside OnSuccess : " + values.size());				
				table.setRowData(range.getStart(),values);
				
				// finishPendingSelection();
				if (widget != null) {
					
					widget.setWidget(view.asWidget());
				}
			}
		};
		
		Log.info("call back"+ callback);
		fireRangeRequest(q, range, callback);
	}
	private void fireRangeRequest(String name, final Range range, final Receiver<List<StudentOscesProxy>> callback) {
		Log.info("~~Inside FireRangeReaquest");
		createRangeRequest(name, range).with(view_s.getPaths()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}
	
	protected Request<List<StudentOscesProxy>> createRangeRequest(String name, Range range) {
		//return requests.scarRequest().findScarEntries(range.getStart(), range.getLength());
		Log.info("~~Inside CreateRangeRequest");
		Log.info("OSCEPROXY ID : " + osceProxy.getId());
		return requests.studentOsceRequestNonRoo().findStudentEntriesByName(name,osceProxy.getId(), range.getStart(), range.getLength()).with("student");
	}

	protected void fireCountRequest(String name, Receiver<Long> callback) {
		//requests.scarRequest().countScars().fire(callback);
		Log.info("~~Inside FireCountRequest");
		requests.studentOsceRequestNonRoo().countStudentByName(name,osceProxy.getId()).fire(callback);
	}
		
		
	Boolean temp = null;

	@Override
	public Boolean onRender(StudentOscesProxy studentOscesProxy) {
		
			temp = studentOscesProxy.getIsEnrolled();
			
		
			StudentOscesRequest request = requests.studentOscesRequest();
			
			studentOscesProxy = request.edit(studentOscesProxy);

			studentOscesProxy.setIsEnrolled(!studentOscesProxy.getIsEnrolled());
		
			request.persist().using(studentOscesProxy).fire(new OSCEReceiver<Void>() {

						@Override
						public void onSuccess(Void response) {
							Log.info("~~Records Updated Successfully");
							temp = !temp;
							
							refreshdata();
						}
					});
			
			
			
			return temp;
	}

	
	public void refreshdata()
	{
		requests.studentOsceRequestNonRoo().findStudentOsceByOsce(osceProxy.getId()).with("student").fire(new OSCEReceiver<List<StudentOscesProxy>>() {

			@Override
			public void onSuccess(List<StudentOscesProxy> response) {
				// TODO Auto-generated method stub
				//studentSubDetailsView.getTable().setRowData(response);
				
				subDetailsView[currenttab].getTable().setRowData(response);
			}});
	}

	@Override
	public void deleteClicked(StudentOscesProxy scar) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void importClicked() {
		
		arrarycount = 0;
		init();
		
	}
}
