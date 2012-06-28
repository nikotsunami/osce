package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.OsceDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.OscePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OsceDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OsceDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OsceTaskPopView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OsceView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OsceViewImpl;
import ch.unibas.medizin.osce.client.managed.request.AdministratorProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTemplateRequest;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicRequest;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.TaskProxy;
import ch.unibas.medizin.osce.client.managed.request.TaskRequest;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyListPlace;
import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.server.TimetableGenerator;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
//import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.requestfactory.shared.Violation;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * @author dk
 *
 */
public class OsceDetailsActivity extends AbstractActivity implements
OsceDetailsView.Presenter, OsceDetailsView.Delegate, OsceTaskPopView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private OsceDetailsView view;
	private OsceTaskPopView osceTaskPop;
	private CellTable<OsceProxy> table;
	private SingleSelectionModel<OsceProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;

	public static OsceActivity osceActivity;
	private OsceProxy osceProxy;
	private OsceDetailsPlace place;
	private OsceDetailsActivity osceDetailsActivity;

	public OsceDetailsActivity(OsceDetailsPlace place,
			OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;
		osceDetailsActivity=this;
	}

	public void onStop() {

	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("OsceDetailsActivity.start()");
		OsceDetailsView osceDetailsView = new OsceDetailsViewImpl();
		osceDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = osceDetailsView;
		this.osceTaskPop=view.getPopView();
	//	osceActivity=new OsceActivity(requests, placeController);
		widget.setWidget(osceDetailsView.asWidget());

		
		view.setDelegate(this);
		osceTaskPop.setDelegate(this);
	//	osceTaskPop.setDelegate(this);
		
		//System.out.println("semster--"+osceActivity.getSemester());

		
		requests.administratorRequest().findAllAdministrators().fire(new OSCEReceiver<List<AdministratorProxy>>() {

			public void onSuccess(List<AdministratorProxy> response) {
				System.out.println("sem receive:-"+response);
			//	view.setAdministratorValue(response);
				osceTaskPop.setAdministratorValue(response);
				
			}
		});
		
		init();
		
		
		/*requests.find(place.getProxyId()).with("tasks").fire(new Receiver<Object>() {

			public void onFailure(ServerFailure error) {
				Log.error(error.getMessage());
			}

			@Override
			public void onSuccess(Object response) {
				if (response instanceof OsceProxy) {
					Log.info("loaded osce with id " + ((OsceProxy) response).getId());
					init((OsceProxy) response);
				}

			}
		});*/

	}

public void init()
{
	
	view.setDelegate(this);
	
	//requests.find(place.getProxyId()).with("tasks").fire(new OSCEReceiver<Object>() {
	requests.find(place.getProxyId()).with("tasks","tasks.administrator").fire(new OSCEReceiver<Object>() {
		public void onFailure(ServerFailure error) {
			Log.error(error.getMessage());
		}

		@Override
		public void onSuccess(Object response) {
			if (response instanceof OsceProxy) {
				Log.info("loaded osce with id " + ((OsceProxy) response).getId());
				//init((OsceProxy) response);
				osceProxy=(OsceProxy)response;
				view.setValue((OsceProxy)response);
				osceTaskPop.setValue((OsceProxy)response);
			}

		}
	});

}
	/*private void init(OsceProxy osceProxy) {
		this.osceProxy = osceProxy;

		view.setValue(osceProxy);

		view.setDelegate(this);
	}
*/
	@Override
	public void goTo(Place place) {
		placeController.goTo(place);

	}

	@Override
	public void osceEditClicked() {
		Log.info("edit clicked");
		goTo(new OsceDetailsPlace(osceProxy.stableId(),
				Operation.EDIT));

	}

	@Override
	public void osceGenerateClicked() {
		Log.info("generate clicked");
		
		requests.osceRequestNonRoo().generateOsceScaffold(osceProxy.getId()).fire(
				
		);
	}	
	
	
	@Override
	public void osceDeleteClicked() {
		if (!Window.confirm("Really delete this entry? You cannot undo this change.")) {
			return;
		}
		
		try{
			
		
		requests.osceRequest().remove().using(osceProxy).fire(new OSCEReceiver<Void>() {

			@Override
			public void onViolation(Set<Violation> errors) {
				/*Iterator<Violation> iter = errors.iterator();
				String message = "";
				while (iter.hasNext()) {
					message += iter.next().getMessage() + "<br>";
				}
				Log.warn(" in task -" + message);*/
				Window.alert("osce could not be deleted becaused it may assign to other osce or task.");
			}
			
			public void onFailure(ServerFailure error) {
		/*	Log.warn("you not able to delete record used by other ");
				Log.error(error.getMessage());
*/
				Window.alert("osce could not be deleted becaused it may assign to other osce or task.");
			}
			
			public void onSuccess(Void ignore) {
				if (widget == null) {
					return;
				}
				System.out.println("Deleted record");
				//osceActivity.init();
				
			//	placeController.goTo(new OscePlace("OscePlace!DELETED"));
				
				Log.info("return");
				osceActivity.init();
				placeController.goTo(new OscePlace("OscePlace!DELETED"));
				/*final OsceView systemStartView = new OsceViewImpl();
				 requests.osceRequestNonRoo().findAllOsce().fire(new Receiver<List<OsceProxy>>() {

						@Override
						public void onSuccess(List<OsceProxy> response) {
							// TODO Auto-generated method stub
							System.out.println("Osce size--"+response.size());
							
						//	systemStartView.getTable().setRowData(response);
							systemStartView.getTable().setRowCount(response.size(), true);
							systemStartView.getTable().setRowData(0, response);
						}
					});		*/	 
					
					
				 
				
			
			}
		});
		}
		catch(Exception e)
		{
			
		}
	}

	@Override
	public void deleteClicked(TaskProxy task) {
		System.out.println("task--"+task);
		requests.taskRequest().remove().using(task).fire(new OSCEReceiver<Void>() {
			
			public void onFailure(ServerFailure error) {
				Log.error(error.getMessage());

			}

			@Override
			public void onViolation(Set<Violation> errors) {
				Iterator<Violation> iter = errors.iterator();
				String message = "";
				while (iter.hasNext()) {
					message += iter.next().getMessage() + "<br>";
				}
				Log.warn(" in task -" + message);
			}
			
			public void onSuccess(Void ignore) {
				Log.debug("Sucessfully deleted");
				init();
			}
		});
	}

	@Override
	public void saveClicked(Boolean isedit, String innerText,
			AdministratorProxy value, Date value2, OsceProxy osce,TaskProxy task) {
		System.out.println("Save call");
		// TODO Auto-generated method stub
		Date today = new Date();
		Date futureDate=new Date();
		futureDate.setYear(today.getYear()+2);
		if(isedit==true)
		{
			System.out.println("edit mode");
			TaskRequest taskRequest=requests.taskRequest();
			task = taskRequest.edit(task);
			
			task.setAdministrator(value);
			task.setDeadline(value2);
			
			task.setName(innerText);
			task.setVersion(1);
			task.setIsDone(task.getIsDone());
			task.setOsce(osce);
			
			/*
			
			if(task.getName().length()<3)
			{
				Window.alert("please enter proper task name");
				return;
			}
			else if(task.getDeadline()==null)
			{
				Window.alert("please select deadline date");
				return;
			}
			else if(task.getDeadline().after(today) || task.getDeadline().before(futureDate))
			{
				Window.alert("please enter proper date");
			}
			else if(task.getAdministrator()==null)
			{
				Window.alert("please select administrator value");
				return;
			}
			*/
			taskRequest.persist().using(task).fire(new OSCEReceiver<Void>() {
				
				@Override
				public void onViolation(Set<Violation> errors) {
					Iterator<Violation> iter = errors.iterator();
					String message = "";
					while (iter.hasNext()) {
						message += iter.next().getMessage() + "<br>";
					}
					Log.warn(" in task -" + message);
				}
				@Override
				public void onSuccess(Void response) {
					// TODO Auto-generated method stub
			//		System.out.println("INside success");
					Log.info(" task edited successfully");
				//	init2();
					Log.info("Call Init Search from onSuccess");
					init();
				
					
				}
			});
			
		}
		else
		{
		
			System.out.println("new mode");
			TaskRequest taskRequest=requests.taskRequest();
			TaskProxy taskProxy=taskRequest.create(TaskProxy.class);
			
			taskProxy.setAdministrator(value);
			taskProxy.setDeadline(value2);
			taskProxy.setName(innerText);
			taskProxy.setVersion(1);
			taskProxy.setIsDone(false);
			taskProxy.setOsce(osce);
			System.out.println("today--"+today);
			System.out.println("future--"+futureDate);
			System.out.println("selected--"+taskProxy.getDeadline());
			
			/*
			if(taskProxy.getName().length()<3 )
			{
				Window.alert("please enter proper  name of atleast 3 charater");
				return;
			}
			
			else if(taskProxy.getDeadline()==null)
			{
				Window.alert("please select deadline date");
				return;
			}
			else if(taskProxy.getDeadline().after(futureDate) || taskProxy.getDeadline().before(today) )
			{
				Window.alert("please enter proper date");
				return;
			}
			else if(taskProxy.getAdministrator()==null)
			{
				Window.alert("please select administrator value");
				return;
			}
			*/
				System.out.println("before save");
			
			taskRequest.persist().using(taskProxy).fire(new OSCEReceiver<Void>() {
				
				
				@Override
				public void onSuccess(Void response) {
					// TODO Auto-generated method stub
			//		System.out.println("INside success");
					Log.info("new task added successfully");
				//	init2();
					Log.info("Call Init Search from onSuccess");
					init();
				
					
				}
			});
			
		
		
	}

	}

	@Override
	public void editForDone(TaskProxy task) {
		// TODO Auto-generated method stub
		System.out.println("edit mode");
		TaskRequest taskRequest=requests.taskRequest();
		task = taskRequest.edit(task);
		if(task.getIsDone()==false)
		{
			task.setIsDone(true);
		}
		
	/*	task.setAdministrator(task);
		task.setDeadline(value2);
		
		task.setName(innerText);
		task.setVersion(1);
		task.setIsDone(task.getIsDone());
		task.setOsce(osce);
	*/	
		taskRequest.persist().using(task).fire(new OSCEReceiver<Void>() {
			
			
			@Override
			public void onSuccess(Void response) {
				// TODO Auto-generated method stub
		//		System.out.println("INside success");
				Log.info(" task done successfully");
			//	init2();
				Log.info("Call Init Search from onSuccess");
				init();
			
				
			}
		});
		
	}
	
}