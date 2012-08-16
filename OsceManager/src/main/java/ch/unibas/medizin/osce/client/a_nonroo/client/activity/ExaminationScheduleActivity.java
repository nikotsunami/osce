package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.ExaminationScheduleDetailPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ExaminationSchedulePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan.ExaminationScheduleView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan.ExaminationScheduleViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author dk
 *
 */
public class ExaminationScheduleActivity extends AbstractActivity implements ExaminationScheduleView.Presenter, ExaminationScheduleView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private ExaminationScheduleView view;
	private ExaminationScheduleDetailActivityMapper examinationScheduleDetailActivityMapper;
	private ActivityManager activityManager;
	private SemesterProxy semesterProxy;
	private ExaminationSchedulePlace place;
	public HandlerManager handlerManager;// = new HandlerManager(this);
	private OsceProxy osceProxy;
	private List<OsceProxy> osceProxyList = new ArrayList<OsceProxy>();
	private int tabIndex=0; 
	
	public ExaminationScheduleActivity(OsMaRequestFactory requests,PlaceController placeController, ExaminationSchedulePlace examinationSchedulePlace) 
	{			
		Log.info("~Call Constructor ExaminationScheduleActivity <Custom>");		
		this.requests = requests;		
		this.placeController = placeController;
		this.place=examinationSchedulePlace;
		this.handlerManager = ExaminationSchedulePlace.handler;
		this.semesterProxy=ExaminationSchedulePlace.semesterProxy;
		//Log.info("Semester Proxy : " + semesterProxy.getCalYear() + " :in CircuitActivity Constructor.");
		//System.out.println("Proxy: " + place.semesterProxy.getCalYear());
		
		examinationScheduleDetailActivityMapper = new ExaminationScheduleDetailActivityMapper(requests,placeController);
		this.activityManager = new ActivityManager(examinationScheduleDetailActivityMapper,requests.getEventBus());
						
		
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
	public ExaminationScheduleActivity(OsMaRequestFactory requests, PlaceController placeController) {
    	this.requests = requests;
    	this.placeController = placeController;
    	examinationScheduleDetailActivityMapper = new ExaminationScheduleDetailActivityMapper(requests,placeController);
    	this.activityManager = new ActivityManager(examinationScheduleDetailActivityMapper,requests.getEventBus());
    }

	public void onStop(){
		
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		
		this.widget = panel;
		
		
		init();

		
	}
	
	private void init() {
		ExaminationScheduleView systemStartView = new ExaminationScheduleViewImpl();
		systemStartView.setPresenter(this);
		widget.setWidget(systemStartView.asWidget());
		this.view = systemStartView;
		view.getOsceTabPanel().addSelectionHandler(new SelectionHandler<Integer>() {
			
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				Log.info("New Tab Selected");	
				//view.getCircuitTabPanel().selectTab(view.getCircuitTabPanel().getTabBar().getSelectedTab());				
				activityManager.setDisplay((SimplePanel)view.getOsceTabPanel().getWidget(event.getSelectedItem()));
				//activityManager.setDisplay(view.getCircuitDetailPanel());				
				goTo(new ExaminationScheduleDetailPlace(osceProxyList.get(event.getSelectedItem()).stableId(),Operation.DETAILS));
			}
		});
		view.setDelegate(this);
		
		requests.osceRequestNonRoo().findAllOsceOnSemesterId(semesterProxy.getId()).with("semester").fire(new OSCEReceiver<List<OsceProxy>>() {

			@Override
			public void onSuccess(List<OsceProxy> response) {
				tabIndex=0;
				if(response != null && response.size() > 0){
				osceProxyList=response;
				Log.info("OSce Proxy Size : " + response.size());
				
				Iterator<OsceProxy> osceList = response.iterator();
				
				osceProxy = response.get(0);
				while(osceList.hasNext()){
				
				Log.info("OSce Proxy index : " + tabIndex);
				OsceProxy osceProxy = osceList.next();
				String osceLable = osceProxy.getStudyYear()==null?"":osceProxy.getStudyYear() + "." + osceProxy.getSemester().getSemester().name();
				view.getOsceTabPanel().insert(new SimplePanel(), osceLable,tabIndex);
				tabIndex++;
				
				}
				
				view.getOsceTabPanel().selectTab(0);
				
				//activityManager.setDisplay((SimplePanel)view.getCircuitTabPanel().getWidget(0));	
				/*activityManager.setDisplay((SimplePanel)view.getCircuitTabPanel().getWidget(0));
				Log.info("Before Goto");
				goTo(new CircuitDetailsPlace(osceProxy.stableId(),Operation.DETAILS));*/
				
				}
			}
		});
		
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}
}
