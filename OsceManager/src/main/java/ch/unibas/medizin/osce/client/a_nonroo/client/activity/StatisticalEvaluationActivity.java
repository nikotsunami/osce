package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.StatisticalEvaluationDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StatisticalEvaluationPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StatisticalEvaluation.StatisticalEvaluationView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StatisticalEvaluation.StatisticalEvaluationViewImpl;
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
public class StatisticalEvaluationActivity extends AbstractActivity implements StatisticalEvaluationView.Presenter, StatisticalEvaluationView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private StatisticalEvaluationView view;
		StatisticalEvaluationPlace place;
		public HandlerManager handlerManager;
		private SemesterProxy semesterProxy;
		private OsceProxy osceProxy;
		public static SemesterProxy semesterProxyForDetail;
		StatisticalEvaluationActivity statisticalEvaluationActivity;
		private List<OsceProxy> osceProxyList = new ArrayList<OsceProxy>();
		int tabIndex=0;
		private ActivityManager activityManager;
		private SelectChangeHandler removeHandler;
		private StatisticalEvaluationDetailsActivityMapper statisticalEvaluationDetailsActivityMapper;		
		

	public StatisticalEvaluationActivity(OsMaRequestFactory requests, PlaceController placeController) {
    	this.requests = requests;
    	this.placeController = placeController;
    }

	
	// Module10 Create plans
	public StatisticalEvaluationActivity(OsMaRequestFactory requests, PlaceController placeController,StatisticalEvaluationPlace statisticalEvaluationPlace) 
	{		
		Log.info("Call IndividualSchedulesActivity Constructor");		
		this.requests = requests;
    	this.placeController = placeController;
    	this.place = statisticalEvaluationPlace;
    	this.handlerManager = statisticalEvaluationPlace.handler;
		this.semesterProxy=statisticalEvaluationPlace.semesterProxy;
		this.semesterProxyForDetail=statisticalEvaluationPlace.semesterProxy;
		statisticalEvaluationActivity = this;
		
		statisticalEvaluationDetailsActivityMapper = new StatisticalEvaluationDetailsActivityMapper(requests,placeController);
		this.activityManager = new ActivityManager(statisticalEvaluationDetailsActivityMapper,requests.getEventBus());
		
		Log.info("~~In Activity : " + semesterProxy.getId());
		Log.info("Semester Proxy : " + semesterProxy.getCalYear() + " :in Student Constructor.");
		
		this.addSelectChangeHandler(new SelectChangeHandler() 
		{			
			@Override
			public void onSelectionChange(SelectChangeEvent event) 
			{			
				Log.info("Call IndividualSchedulesActivity Activity");					
				Log.info("onSelectionChange Get Semester: " + event.getSemesterProxy().getCalYear());		
				semesterProxy= event.getSemesterProxy();
				semesterProxyForDetail= event.getSemesterProxy();
				init();
			}
		});
			
	}
	public void addSelectChangeHandler(SelectChangeHandler handler) {
		Log.info("======================================== Register SEA===================================");
		handlerManager.addHandler(SelectChangeEvent.getType(), handler);
		removeHandler=handler;
		
	}
	// E Module10 Create plans
	
	public void onStop(){
		// Module10 Create plans
		activityManager.setDisplay(null);
		Log.info("======================================== on Stop SEA===================================");
		handlerManager.removeHandler(SelectChangeEvent.getType(),removeHandler);
		// E Module10 Create plans
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		StatisticalEvaluationView systemStartView = new StatisticalEvaluationViewImpl();
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		
		init();

		view.setDelegate(this);
	}
	
	private void init() {
		// Module10 Create plans
				Log.info("Call init()");
				StatisticalEvaluationView systemStartView = new StatisticalEvaluationViewImpl();
				systemStartView.setPresenter(this);
				this.view = systemStartView;
				widget.setWidget(systemStartView.asWidget());
				view.setDelegate(this);
				initOsceTab();						
				// E Module10 Create plans
	}
	
	// Module10 Create plans
		public void initOsceTab()
		{
			Log.info("Call initOsceTab()");	
			view.getEvaluationTab().addSelectionHandler(new SelectionHandler<Integer>() {
				
				@Override
				public void onSelection(SelectionEvent<Integer> event) {				
					Log.info("Selected Tab: " + event.getSelectedItem());									
					Log.info("Get Selected Item" + event.getSelectedItem());
									
					activityManager.setDisplay((SimplePanel)view.getEvaluationTab().getWidget(event.getSelectedItem()));
					goTo(new StatisticalEvaluationDetailsPlace(osceProxyList.get(event.getSelectedItem()).stableId(),Operation.DETAILS));				
				}
			});
			Log.info("Semester Proxy For Osce: " + semesterProxy.getId());
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
					view.getEvaluationTab().insert(new SimplePanel(), osceLable,tabIndex);
					tabIndex++;
					
					}
					
					view.getEvaluationTab().selectTab(0);
					
					
					
					}
				}
			});
			
		}
		private void initTabBar(List<OsceProxy> response) 
		{
			Log.info("Call initTabBar");		
					
			tabIndex=0;		
			if(response != null && response.size() > 0)
			{
				
				Log.info("OSce Proxy Size : " + response.size());		
				Iterator<OsceProxy> osceList = response.iterator();		
			
					while(osceList.hasNext())
					{
						Log.info("OSce Proxy index : " + tabIndex);
						OsceProxy osceProxy = osceList.next();
						String osceLable = osceProxy.getStudyYear()==null?"":osceProxy.getStudyYear() + "." + osceProxy.getSemester().getSemester().name();
						view.getEvaluationTab().insert(new SimplePanel(), osceLable,tabIndex);
						tabIndex++;
					}
				view.getEvaluationTab().selectTab(0);
			}		
		}
		// E Module10 Create plans

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}
}
