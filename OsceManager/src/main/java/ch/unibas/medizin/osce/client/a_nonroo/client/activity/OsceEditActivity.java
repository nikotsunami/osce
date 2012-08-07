package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.OsceDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.OscePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OsceEditView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OsceEditViewImpl;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceRequest;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.TaskProxy;
import ch.unibas.medizin.osce.client.managed.request.TaskRequest;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.OsceStatus;
import ch.unibas.medizin.osce.shared.StudyYears;
import com.google.gwt.requestfactory.shared.Receiver;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
//import com.google.gwt.requestfactory.shared..OSCEReceiver;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.requestfactory.shared.Violation;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class OsceEditActivity extends AbstractActivity implements
OsceEditView.Presenter, OsceEditView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private OsceEditView view;
	private OsceDetailsPlace place;

	private RequestFactoryEditorDriver<OsceProxy, OsceEditViewImpl> editorDriver;
	private OsceProxy osce;
	private TaskProxy taskProxy;
	private boolean save;
	public static OsceActivity osceActivity;
	public static SemesterProxy semester;
	
	public OsceEditActivity(OsceDetailsPlace place,
			OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;

	}

	public OsceEditActivity(OsceDetailsPlace place,
			OsMaRequestFactory requests, PlaceController placeController,
			Operation operation) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;
		// this.operation=operation;
	}

	public void onStop() {

	}

	@Override
	public String mayStop() {
		if (!save && changed())
			return "Changes will be discarded!";
		else
			return null;
	}

	// use this to check if some value has changed since editing has started
	private boolean changed() {
		return editorDriver != null && editorDriver.flush().isChanged();
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {

		Log.info("start");
		this.widget = panel;
		this.view = new OsceEditViewImpl();
		editorDriver = view.createEditorDriver();

		System.out.println("sem found:-"+semester.getCalYear());
		view.setDelegate(this);


		view.setStudyYearPickerValues(Arrays.asList(StudyYears.values()));


		/*view.setTasksPickerValues(Collections.<TaskProxy>emptyList());
		requests.taskRequest().findTaskEntries(0, 50).with(ch.unibas.medizin.osce.client.managed.ui.TaskProxyRenderer.instance().getPaths()).fire(new Receiver<List<TaskProxy>>() {

			public void onSuccess(List<TaskProxy> response) {
				System.out.println("task set");
				List<TaskProxy> values = new ArrayList<TaskProxy>();
				values.add(null);
				values.addAll(response);
				view.setTasksPickerValues(values);
			}
		});
		*/
		
		/*requests.semesterRequest().findAllSemesters().fire(new Receiver<List<SemesterProxy>>() {

			public void onSuccess(List<SemesterProxy> response) {
				
				view.setSemsterValues(response);
			}
		});*/

		
		requests.osceRequest().findAllOsces().with("tasks").fire(new OSCEReceiver<List<OsceProxy>>() {

			public void onSuccess(List<OsceProxy> response) {
				
				view.setOsceValues(response);
			}
		});

		eventBus.addHandler(PlaceChangeEvent.TYPE,
				new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {

				// updateSelection(event.getNewPlace());
				// TODO implement
			}
		});
		// init();

		if (this.place.getOperation() == Operation.EDIT) {
			Log.info("edit");
			requests.find(place.getProxyId()).with("osce")
			.fire(new OSCEReceiver<Object>() {

				
				public void onFailure(ServerFailure error) {
					Log.error(error.getMessage());
				}

				@Override
				public void onSuccess(Object response) {
					if (response instanceof OsceProxy) {
						Log.info("edit osce with id " + ((OsceProxy) response).getId());
						// init((OsceProxy) response);
						osce = (OsceProxy) response;
						((OsceEditViewImpl)view).osceValue.setVisible(false);
						
						
						
						
						init();
						
					}

				}
			});
			
			
			
		} else {

			Log.info("new Osce");
			// oscePlace.setProxyId(osce.stableId());
			init();
		}
		// view.initialiseDriver(requests);
		widget.setWidget(view.asWidget());
		// setTable(view.getTable());

		// change {
		
		requests.roomRequestNonRoo().countTotalRooms().fire(new Receiver<Integer>() {

			@Override
			public void onSuccess(Integer response) {
				Log.info("Response Of countTotalRooms()  :" + response);
				((OsceEditViewImpl)view).numberRooms.setValue(response);
			}
		});
		
		
		// change }
	}

	private void init() {

		OsceRequest request = requests.osceRequest();

		if (osce == null) {

			OsceProxy osce = request.create(OsceProxy.class);
			this.osce = osce;
			view.setEditTitle(false);

		} else {

			view.setEditTitle(true);
		}

		Log.info("edit");

		Log.info("persist");
		request.persist().using(osce);
		editorDriver.edit(osce, request);

		Log.info("flush");
		editorDriver.flush();
		Log.debug("Create für: " + osce.getId());
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);

	}

	@Override
	public void cancelClicked() {
		if (this.place.getOperation() == Operation.EDIT)
			placeController.goTo(new OsceDetailsPlace(osce.stableId(),
					Operation.DETAILS));
		else
			placeController.goTo(new OscePlace("OscePlace!CANCEL"));

	}

	@Override
	public void saveClicked() {
		Log.info("saveClicked");
		// Highlight onViolation
			Log.info("Map Size: " + view.getOsceMap().size());
		// E Highlight onViolations
		if(this.place.getOperation() == Operation.EDIT)
		{
	
			// Highlight onViolation			
			editorDriver.flush().fire(new OSCEReceiver<Void>(view.getOsceMap()) {
				// E Highlight onViolation

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
					Log.warn(" in Osce -" + message);

					// TODO mcAppFactory.getErrorPanel().setErrorMessage(message);

				}

				@Override
				public void onSuccess(Void response) {
					Log.info("Osce successfully saved.");

					save = true;
					
					
					placeController.goTo(new OsceDetailsPlace(osce.stableId(),
							Operation.DETAILS));
					osceActivity.init();
				}

			});

	}
		else
		{
			System.out.println("create mode");
			 OsceRequest osceRequest=requests.osceRequest();
			final OsceProxy osceProxy=osceRequest.create(OsceProxy.class);
			
			osceProxy.setName(((OsceEditViewImpl)view).name.getValue());
			osceProxy.setMaxNumberStudents(((OsceEditViewImpl)view).maxNumberStudents.getValue());
			osceProxy.setIsRepeOsce(((OsceEditViewImpl)view).isRepeOsce.isChecked());
			osceProxy.setLongBreak(((OsceEditViewImpl)view).LongBreak.getValue());
			osceProxy.setShortBreak(((OsceEditViewImpl)view).shortBreak.getValue());
			osceProxy.setLunchBreak(((OsceEditViewImpl)view).lunchBreak.getValue());
			osceProxy.setMiddleBreak(((OsceEditViewImpl)view).middleBreak.getValue());
			osceProxy.setStudyYear(((OsceEditViewImpl)view).studyYear.getValue());
			//osceProxy.setSemester(((OsceEditViewImpl)view).semester.getValue());
			osceProxy.setSemester(semester);
			osceProxy.setPostLength(((OsceEditViewImpl)view).postLength.getValue());
			//osceProxy.setCopiedOsce(((OsceEditViewImpl)view).copiedOsce.getValue());
			osceProxy.setNumberCourses(((OsceEditViewImpl)view).numberCourses.getValue());
			osceProxy.setNumberRooms(((OsceEditViewImpl)view).numberRooms.getValue());
			osceProxy.setOsceStatus(OsceStatus.OSCE_NEW);
			//Set<TaskProxy> setTaskProxy = new HashSet<TaskProxy>();
			//setTaskProxy=((OsceEditViewImpl)view).osceValue.getValue().getTasks();
			//osceProxy.setTasks(setTaskProxy);
			//System.out.println("task--"+setTaskProxy.size());
			//final LifeCycleState osceDefauleState=LifeCycleState.NEW;
			
			// Highlight onViolation
			/*if(osceProxy.getIsRepeOsce()==true )
			{
				if(osceProxy.getCopiedOsce()==null)
				{
					Window.alert("Please select OSCE for Rape");
					return;
				}
				else
				{
					osceProxy.setCopiedOsce(((OsceEditViewImpl)view).copiedOsce.getValue());
				}
			}
			else if(osceProxy.getName()==""  )
			{
				Window.alert("please enter all data and proper data");
				return;
			}
			else if(osceProxy.getShortBreak()==null)
			{
				Window.alert("please enter  value in short break");
				return;
			}
			else if(osceProxy.getLongBreak()==null)
			{
				Window.alert("please enter  value in long break");
				return;
			}
			else if(osceProxy.getLunchBreak()==null)
			{
				Window.alert("please enter  value in lunch break");
				return;
			}
			else if(osceProxy.getMaxNumberStudents()==null)
			{
				Window.alert("please enter  value in max number of student");
				return;
			}
			else if(osceProxy.getPostLength()==null)
			{
				Window.alert("please enter  value in post length");
				return;
			}
			else if(osceProxy.getNumberCourses()==null)
			{
				Window.alert("please enter  value in max. parcour");
				return;
			}
			else if(osceProxy.getStudyYear()==null)
			{
				Window.alert("please select value in Study Year");
				return;
			}*/
		
			osceRequest.persist().using(osceProxy).fire(new OSCEReceiver<Void>(view.getOsceMap()) {
				// E Highlight onViolation	
				
				@Override
				public void onSuccess(Void response) {
					// TODO Auto-generated method stub
			//		System.out.println("INside success");
					Log.info(" task edited successfully");
				//	init2();
					Log.info("Call Init Search from onSuccess");
					//init();
					
					save = true;
					
					//save tasks
					//Issue # 122 : Replace pull down with autocomplete.
					/*if(((OsceEditViewImpl)view).osceValue.getValue()!=null)
					{*/
					if(((OsceEditViewImpl)view).osceValue.getSelected()!=null)
					{
						//Issue # 122 : Replace pull down with autocomplete.	
					
					requests.osceRequestNonRoo().findMaxOsce().fire(new OSCEReceiver<OsceProxy>() {

						@Override
						public void onViolation(Set<Violation> errors) {
							Iterator<Violation> iter = errors.iterator();
							String message = "";
							while (iter.hasNext()) {
								message += iter.next().getMessage() + "<br>";
							}
							Log.warn(" in Osce -" + message);

							// TODO mcAppFactory.getErrorPanel().setErrorMessage(message);

						}

						@Override
						public void onSuccess(OsceProxy response) {
							// TODO Auto-generated method stub
							System.out.println("max value:---"+response.getName());
							
							//Issue # 122 : Replace pull down with autocomplete.
							if(((OsceEditViewImpl)view).osceValue.getSelected()==null)
							{
								return;
							}
					//Issue # 122 : Replace pull down with autocomplete.
					//Issue # 122 : Replace pull down with autocomplete.
					//Iterator<TaskProxy> taskIterator=((OsceEditViewImpl)view).osceValue.getValue().getTasks().iterator();
					Iterator<TaskProxy> taskIterator=((OsceEditViewImpl)view).osceValue.getSelected().getTasks().iterator();
					//Issue # 122 : Replace pull down with autocomplete.
							while(taskIterator.hasNext())
							{
								TaskProxy tp=taskIterator.next();
								TaskRequest taskRequest=requests.taskRequest();
								
								TaskProxy addTask=taskRequest.create(TaskProxy.class);
								addTask.setDeadline(tp.getDeadline());
								addTask.setIsDone(tp.getIsDone());
								addTask.setName(tp.getName());
								addTask.setAdministrator(tp.getAdministrator());
								addTask.setOsce(response);
								
								taskRequest.persist().using(addTask).fire();
							}
							
							save = true;
							
							editorDriver.flush();
							osceActivity.init();
							placeController.goTo(new OsceDetailsPlace(osceProxy.stableId(),
									Operation.DETAILS));
						}
					});
					
					}
					else
					{
						editorDriver.flush();
						osceActivity.init();
						placeController.goTo(new OsceDetailsPlace(osceProxy.stableId(),
								Operation.DETAILS));
					}
					
				}
			});
		
		}
		
		
	

	}	
	
}
