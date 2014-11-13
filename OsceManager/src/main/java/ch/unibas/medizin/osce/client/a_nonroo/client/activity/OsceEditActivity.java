package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Rectangle;
import org.vaadin.gwtgraphics.client.shape.Text;

import ch.unibas.medizin.osce.client.a_nonroo.client.TimeTableEstimation;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.OsceDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.OscePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OsceEditPopupView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OsceEditPopupViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OsceEditView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OsceEditViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.PreCalculationPopupViewImpl;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceRequest;
import ch.unibas.medizin.osce.client.managed.request.OsceSettingsProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSettingsRequest;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.TaskProxy;
import ch.unibas.medizin.osce.client.managed.request.TaskRequest;
import ch.unibas.medizin.osce.client.style.resources.UiIcons;
import ch.unibas.medizin.osce.client.style.widgets.ScrolledTabLayoutPanel;
import ch.unibas.medizin.osce.shared.BucketInfoType;
import ch.unibas.medizin.osce.shared.OSCESecurityStatus;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.OsceSecurityType;
import ch.unibas.medizin.osce.shared.OsceStatus;
import ch.unibas.medizin.osce.shared.StudyYears;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.requestfactory.shared.Violation;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.CalendarUtil;

public class OsceEditActivity extends AbstractActivity implements
OsceEditView.Presenter, OsceEditView.Delegate, OsceEditPopupView.Delegate{

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private OsceEditView view;
	private OsceDetailsPlace place;

	//private RequestFactoryEditorDriver<OsceProxy, OsceEditViewImpl> editorDriver;
	private OsceProxy osce;
	private TaskProxy taskProxy;
	private boolean save;
	public static OsceActivity osceActivity;
	public static SemesterProxy semester;
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
	private UiIcons uiIcons = GWT.create(UiIcons.class);
	private DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd.MM.yyyy");
	private DateTimeFormat timeFormat = DateTimeFormat.getFormat("hh:mm a");
	
	int previousNoOfDays = 0;
	
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
		if (!save/* && changed()*/)
			return constants.changesDiscarded();
		else
			return null;
	}

	// use this to check if some value has changed since editing has started
	/*private boolean changed() {
		return editorDriver != null && editorDriver.flush().isChanged();
	}*/

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {

		Log.info("start");
		this.widget = panel;
		this.view = new OsceEditViewImpl();
		//editorDriver = view.createEditorDriver();

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
		
				
		requests.osceRequest().findAllOsces().with("tasks", "semester").fire(new OSCEReceiver<List<OsceProxy>>() {

			public void onSuccess(List<OsceProxy> response) {
				view.setOsceValues(response);
				
				if (osce != null && osce.getCopiedOsce() != null)
				{
					((OsceEditViewImpl)view).copiedOsce.setSelected(osce.getCopiedOsce());
				}
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
			requests.find(place.getProxyId()).with("osce","copiedOsce","copiedOsce.semester")
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
						((OsceEditViewImpl)view).labelOsceForTask.setInnerText("");
						
						
						if (OsceStatus.OSCE_NEW.equals(osce.getOsceStatus()) == false)
						{
							((OsceEditViewImpl)view).osceCreationType.removeFromParent();
							((OsceEditViewImpl)view).labelOsceCreationType.removeFromParent();
						}
						
						
						
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
		
		/*requests.roomRequestNonRoo().countTotalRooms().fire(new OSCEReceiver<Integer>() {

			@Override
			public void onSuccess(Integer response) {
				Log.info("Response Of countTotalRooms()  :" + response);
				((OsceEditViewImpl)view).numberRooms.setValue(response);
			}
		});*/
		
		
		// change }
	}

	@SuppressWarnings("deprecation")
	private void init() {

		OsceRequest request = requests.osceRequest();

		if (osce == null) {

			view.setOsceProxy(osce);
			/*OsceProxy osce = request.create(OsceProxy.class);
			this.osce = osce;
			this.osce.setOsceCreationType(OsceCreationType.Automatic);
			view.setEditTitle(false);
			*/
			view.setEditTitle(false);
			requests.roomRequestNonRoo().countTotalRooms().fire(new OSCEReceiver<Integer>() {

				@Override
				public void onSuccess(Integer response) {
					Log.info("Response Of countTotalRooms()  Is:" + response);
					((OsceEditViewImpl)view).numberRooms.setValue(response);
				}
			});

		} else {
			view.setEditTitle(true);
			view.setOsceProxy(osce);
			requests.osceSettingsRequestNonRoo().findOsceSettingsByOsce(osce.getId()).fire(new OSCEReceiver<OsceSettingsProxy>() {

				@Override
				public void onSuccess(OsceSettingsProxy response) {
					
					view.setOsceSttingsProxy(response);
					view.setValue(osce,response);
				}
			});
		}

		Log.info("edit");

		Log.info("persist");
		//osce.setCopiedOsce(((OsceEditViewImpl)view).copiedOsce.getSelected());
		//editorDriver.edit(osce, request);
		Log.info("flush");
		//editorDriver.flush();
		//Log.debug("Create für: " + osce.getId());
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

/*	TaskRequest taskRequest=requests.taskRequest();
	task = taskRequest.edit(task);
	
	taskRequest.persist().using(task).fire(new OSCEReceiver<Void>(view.getPopView().getTaskMap()) {
		// E Highlight onViolation
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
			view.getPopView().hide();
			init();
		
			
		}
	});*/
	
	@SuppressWarnings("deprecation")
	@Override
	public void saveClicked(final OsceProxy oscePrxy,final OsceSettingsProxy osceStngPrxy) {
		Log.info("saveClicked");
		// Highlight onViolation
			Log.info("Map Size: " + view.getOsceMap().size());
		// E Highlight onViolations
			//Log.info("value of osce--"+osce.getCopiedOsce());
		if(oscePrxy != null)
		{
			OsceRequest osceRequest=requests.osceRequest();
			osce= osceRequest.edit(oscePrxy);

			if (OsceStatus.OSCE_NEW.equals(osce.getOsceStatus()) == true)
			{
				osce.setOsceCreationType(((OsceEditViewImpl)view).osceCreationType.getValue());
			}
			osce.setName(((OsceEditViewImpl)view).name.getValue());
			osce.setMaxNumberStudents(((OsceEditViewImpl)view).maxNumberStudents.getValue());
			//osce.setIsRepeOsce(((OsceEditViewImpl)view).isRepeOsce.isChecked());
			osce.setLongBreak(((OsceEditViewImpl)view).LongBreak.getValue());
			osce.setShortBreak(((OsceEditViewImpl)view).shortBreak.getValue());
			osce.setLunchBreak(((OsceEditViewImpl)view).lunchBreak.getValue());
			
			osce.setLunchBreakRequiredTime(((OsceEditViewImpl)view).lunchBreakRequiredTime.getValue());
			osce.setLongBreakRequiredTime(((OsceEditViewImpl)view).longBreakRequiredTime.getValue());
			osce.setMiddleBreak(((OsceEditViewImpl)view).middleBreak.getValue());
			osce.setStudyYear(((OsceEditViewImpl)view).studyYear.getValue());
			
			osce.setShortBreakSimpatChange(((OsceEditViewImpl)view).shortBreakSimpatChange.getValue());
			
			osce.setSemester(semester);
			osce.setPostLength(((OsceEditViewImpl)view).postLength.getValue());
			if(((OsceEditViewImpl)view).copiedOsce.getSelected() != null)
			{
				Log.info("osce not null");
				osce.setCopiedOsce(((OsceEditViewImpl)view).copiedOsce.getSelected());
				osce.setIsRepeOsce(true);
			}
			else
			{
				Log.info("osce null");
				((OsceEditViewImpl)view).copiedOsce.setSelected(null);
				osce.setCopiedOsce(null);
				osce.setIsRepeOsce(false);
			}
			Log.info("osce value--"+((OsceEditViewImpl)view).copiedOsce.getSelected());
			osce.setNumberCourses(((OsceEditViewImpl)view).numberCourses.getValue());
			osce.setNumberRooms(((OsceEditViewImpl)view).numberRooms.getValue());
			//osce.setOsceStatus(OsceStatus.OSCE_NEW);
			//osce.setSecurity(OSCESecurityStatus.FEDERAL_EXAM);
			//osce.setOsceSecurityTypes(OsceSecurityType.federal);
			osce.setSpStayInPost(((OsceEditViewImpl)view).spStayInPost.isChecked());
			osce.setIsFormativeOsce(((OsceEditViewImpl)view).isFormativeOsce.getValue());
			
			osceRequest.persist().using(osce).fire(new OSCEReceiver<Void>(view.getOsceMap()) {
				// E Highlight onViolation
				@Override
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
					Log.warn(" in osce edit -" + message);
				}
				@Override
				public void onSuccess(Void response) {

					save = true;
					saveAndPersistiOsceSettings(osce, osceStngPrxy);
					Log.info("succesfull iosce settings data edit");
					placeController.goTo(new OsceDetailsPlace(osce.stableId(),
							Operation.DETAILS));
					osceActivity.init();
					Log.info("osce edit successfull");
				}
			});
			//osce.setCopiedOsce(((OsceEditViewImpl)view).copiedOsce.getSelected());
			// Highlight onViolation	
			/*if(((OsceEditViewImpl)view).isRepeOsce.isChecked())
			{
				Log.info("osce not null");
				//osce.setCopiedOsce(((OsceEditViewImpl)view).copiedOsce.getSelected());
			}
			else
			{
				Log.info("osce null");
				((OsceEditViewImpl)view).copiedOsce.setSelected(null);
				//osce.setCopiedOsce(null);
			}
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
*/
	}
		else
		{
			System.out.println("create mode");
			 OsceRequest osceRequest=requests.osceRequest();
			final OsceProxy osceProxy=osceRequest.create(OsceProxy.class);
			
			osceProxy.setName(((OsceEditViewImpl)view).name.getValue());
			osceProxy.setMaxNumberStudents(((OsceEditViewImpl)view).maxNumberStudents.getValue());
			//osceProxy.setIsRepeOsce(((OsceEditViewImpl)view).isRepeOsce.isChecked());
			osceProxy.setLongBreak(((OsceEditViewImpl)view).LongBreak.getValue());
			osceProxy.setShortBreak(((OsceEditViewImpl)view).shortBreak.getValue());
			osceProxy.setLunchBreak(((OsceEditViewImpl)view).lunchBreak.getValue());
			osceProxy.setLunchBreakRequiredTime(((OsceEditViewImpl)view).lunchBreakRequiredTime.getValue());
			osceProxy.setLongBreakRequiredTime(((OsceEditViewImpl)view).longBreakRequiredTime.getValue());
			osceProxy.setMiddleBreak(((OsceEditViewImpl)view).middleBreak.getValue());
			osceProxy.setStudyYear(((OsceEditViewImpl)view).studyYear.getValue());
			//osceProxy.setCopiedOsce(((OsceEditViewImpl)view).copiedOsce.getSelected());
			osceProxy.setShortBreakSimpatChange(((OsceEditViewImpl)view).shortBreakSimpatChange.getValue());
			//remove number post
			//osceProxy.setNumberPosts(((OsceEditViewImpl)view).numberPosts.getValue());
			//remove number post
			//osceProxy.setSemester(((OsceEditViewImpl)view).semester.getValue());
			osceProxy.setSemester(semester);
			osceProxy.setPostLength(((OsceEditViewImpl)view).postLength.getValue());
			/*if(((OsceEditViewImpl)view).isRepeOsce.isChecked())
			{
				osceProxy.setCopiedOsce(((OsceEditViewImpl)view).copiedOsce.getSelected());
			}
			else
			{
				osceProxy.setCopiedOsce(null);
			}*/
			if (((OsceEditViewImpl)view).copiedOsce.getSelected() != null)
			{
				osceProxy.setCopiedOsce(((OsceEditViewImpl)view).copiedOsce.getSelected());
				osceProxy.setIsRepeOsce(true);
			}
			else
			{
				osceProxy.setCopiedOsce(null);
				osceProxy.setIsRepeOsce(false);
			}
			
			osceProxy.setNumberCourses(((OsceEditViewImpl)view).numberCourses.getValue());
			osceProxy.setNumberRooms(((OsceEditViewImpl)view).numberRooms.getValue());
			osceProxy.setOsceStatus(OsceStatus.OSCE_NEW);
			osceProxy.setSecurity(OSCESecurityStatus.FEDERAL_EXAM);
			osceProxy.setOsceSecurityTypes(OsceSecurityType.federal);
			osceProxy.setSpStayInPost(((OsceEditViewImpl)view).spStayInPost.isChecked());
			osceProxy.setOsceCreationType(((OsceEditViewImpl)view).osceCreationType.getValue());
			osceProxy.setIsFormativeOsce(((OsceEditViewImpl)view).isFormativeOsce.getValue());
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
					Window.alert("Please select OSCE for Repe");
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
				@Override
				public void onSuccess(Void response) {
					save = true;
					
					saveAndPersistiOsceSettings(osceProxy,osceStngPrxy);
					Log.info("succesfull iosce settings data save");
					if(((OsceEditViewImpl)view).osceValue.getSelected()!=null)
					{
						requests.osceRequestNonRoo().findMaxOsce().fire(new OSCEReceiver<OsceProxy>() {
	
							@Override
							public void onViolation(Set<Violation> errors) {
								Iterator<Violation> iter = errors.iterator();
								String message = "";
								while (iter.hasNext()) {
									message += iter.next().getMessage() + "<br>";
								}
							}
	
							@Override
							public void onSuccess(OsceProxy response) {
								
								if(((OsceEditViewImpl)view).osceValue.getSelected()==null)
								{
									return;
								}
							
								Iterator<TaskProxy> taskIterator=((OsceEditViewImpl)view).osceValue.getSelected().getTasks().iterator();
								while(taskIterator.hasNext())
								{
									TaskProxy tp=taskIterator.next();
									TaskRequest taskRequest=requests.taskRequest();
									
									TaskProxy addTask=taskRequest.create(TaskProxy.class);
									addTask.setDeadline(tp.getDeadline());
									addTask.setIsDone(false);
									//addTask.setIsDone(tp.getIsDone());
									addTask.setName(tp.getName());
									addTask.setAdministrator(tp.getAdministrator());
									addTask.setOsce(response);
									
									taskRequest.persist().using(addTask).fire();
								}
								
								save = true;
								
								//editorDriver.flush();
								
								if (OsceStatus.OSCE_NEW.equals(response.getOsceStatus()))
								{
									createOsceDaySequenceAndParcour(response.getId());
								}
								
								osceActivity.init();
								placeController.goTo(new OsceDetailsPlace(osceProxy.stableId(),
										Operation.DETAILS));
							}
						});
					
					}
					else
					{
						requests.osceRequestNonRoo().findMaxOsce().fire(new OSCEReceiver<OsceProxy>() {

							@Override
							public void onSuccess(OsceProxy response) {
								if (OsceStatus.OSCE_NEW.equals(response.getOsceStatus()))
								{
									createOsceDaySequenceAndParcour(response.getId());
								}
							}
						});
						
						//editorDriver.flush();
						osceActivity.init();
						placeController.goTo(new OsceDetailsPlace(osceProxy.stableId(),
								Operation.DETAILS));
					}
					
				}
			});
		
		}

	}	
	
	@SuppressWarnings("deprecation")
	protected void saveAndPersistiOsceSettings(OsceProxy osce,
		OsceSettingsProxy osceStngPrxy) {

			if (osceStngPrxy != null) {
				//edit
				OsceSettingsProxy proxy;
				 final OsceSettingsRequest osceSettingsRequest=requests.osceSettingsRequest();
				 proxy=osceSettingsRequest.edit(osceStngPrxy);
				 proxy.setUsername(((OsceEditViewImpl)view).userName.getValue());
				 proxy.setPassword(((OsceEditViewImpl)view).password.getValue());
				 proxy.setBucketName(((OsceEditViewImpl)view).bucketName.getValue());
				 proxy.setSettingPassword(((OsceEditViewImpl)view).settingPassword.getValue());
				 if(((OsceEditViewImpl)view).backUpPeriod.getValue().equals("") == false){
						Integer backUpPeriod = Integer.parseInt(((OsceEditViewImpl)view).backUpPeriod.getValue());
						proxy.setBackupPeriod(backUpPeriod);			
				 }else{
					 proxy.setBackupPeriod(null);
				 }
				 proxy.setTimeunit(((OsceEditViewImpl)view).timeUnit.getValue());
				 proxy.setNextExaminee(((OsceEditViewImpl)view).pointNextExaminee.getValue());
				 proxy.setEncryptionType(((OsceEditViewImpl)view).encryptionType.getValue());
				 proxy.setInfotype(((OsceEditViewImpl)view).bucketInfo.getValue());
				 proxy.setSymmetricKey(((OsceEditViewImpl)view).symmetricKey.getValue());
				 proxy.setReviewMode(((OsceEditViewImpl)view).examReviewMode.getValue());
				 proxy.setAutoSelection(((OsceEditViewImpl)view).autoSelection.getValue());
				 proxy.setScreenSaverText(((OsceEditViewImpl)view).screenSaverText.getValue());
				 if(((OsceEditViewImpl)view).screenSaverTime.getValue().equals("") == false){
						Integer screenSaverTime= Integer.parseInt(((OsceEditViewImpl)view).screenSaverTime.getValue());
						proxy.setScreenSaverTime(screenSaverTime);		
				 }else{
					 proxy.setScreenSaverTime(null);
				 }
				 proxy.setOsce(osce);
			
				if(((OsceEditViewImpl)view).bucketInfo.getValue().equals(BucketInfoType.FTP)){
					proxy.setHost(((OsceEditViewImpl)view).host.getValue());
				}
				
				osceSettingsRequest.persist().using(proxy).fire(new OSCEReceiver<Void>() {

					@Override
					public void onSuccess(Void response) {

						Log.info("osce settings saved succesfully");
					}
				});
				
			} else {
				//create
					final OsceSettingsRequest osceSettingsRequest=requests.osceSettingsRequest();
					OsceSettingsProxy proxy= osceSettingsRequest.create(OsceSettingsProxy.class);
			
					proxy.setUsername(((OsceEditViewImpl)view).userName.getValue());
					proxy.setPassword(((OsceEditViewImpl)view).password.getValue());
					proxy.setBucketName(((OsceEditViewImpl)view).bucketName.getValue());
					proxy.setSettingPassword(((OsceEditViewImpl)view).settingPassword.getValue());
					if(((OsceEditViewImpl)view).backUpPeriod.getValue().equals("") == false){
						Integer backUpPeriod = Integer.parseInt(((OsceEditViewImpl)view).backUpPeriod.getValue());
						proxy.setBackupPeriod(backUpPeriod);			
					}
					proxy.setTimeunit(((OsceEditViewImpl)view).timeUnit.getValue());
					proxy.setNextExaminee(((OsceEditViewImpl)view).pointNextExaminee.getValue());
					proxy.setEncryptionType(((OsceEditViewImpl)view).encryptionType.getValue());
					proxy.setInfotype(((OsceEditViewImpl)view).bucketInfo.getValue());
					proxy.setSymmetricKey(((OsceEditViewImpl)view).symmetricKey.getValue());
					proxy.setReviewMode(((OsceEditViewImpl)view).examReviewMode.getValue());
					proxy.setScreenSaverText(((OsceEditViewImpl)view).screenSaverText.getValue());
					if(((OsceEditViewImpl)view).screenSaverTime.getValue().equals("") == false){
						Integer screenSaverTime= Integer.parseInt(((OsceEditViewImpl)view).screenSaverTime.getValue());
						proxy.setScreenSaverTime(screenSaverTime);		
					 }
					proxy.setAutoSelection(((OsceEditViewImpl)view).autoSelection.getValue());
					proxy.setOsce(osce);
				
					if(((OsceEditViewImpl)view).bucketInfo.getValue().equals(BucketInfoType.FTP)){
						proxy.setHost(((OsceEditViewImpl)view).host.getValue());
					}
					osceSettingsRequest.persist().using(proxy).fire(new OSCEReceiver<Void>() {

						@Override
						public void onSuccess(Void response) {

							Log.info("osce settings saved succesfully");
						}
					});
			}
	}

	@SuppressWarnings("deprecation")
	public void createOsceDaySequenceAndParcour(Long osceId) {
		requests.osceRequestNonRoo().createOsceDaySequeceAndCourse(osceId).fire(new OSCEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
			}
		});
	}

	@Override
	public void previewButtonClicked(int left, int top) {
		OsceEditPopupView popupView = new OsceEditPopupViewImpl();
		popupView.setDelegate(this);
		((OsceEditPopupViewImpl)popupView).setPopupPosition(left-100, top+28);
		((OsceEditPopupViewImpl)popupView).show();
	}	
	
	@Override
	public void oscePreviewButtonClicked(int noOfOscePost, Date startTime, Date endTime) {
		OsceRequest osceRequest=requests.osceRequest();
		final OsceProxy osceProxy=osceRequest.create(OsceProxy.class);		
		osceProxy.setName(((OsceEditViewImpl)view).name.getValue());
		osceProxy.setMaxNumberStudents(((OsceEditViewImpl)view).maxNumberStudents.getValue());
		//osceProxy.setIsRepeOsce(((OsceEditViewImpl)view).isRepeOsce.isChecked());
		osceProxy.setLongBreak(((OsceEditViewImpl)view).LongBreak.getValue());
		osceProxy.setShortBreak(((OsceEditViewImpl)view).shortBreak.getValue());
		osceProxy.setLunchBreak(((OsceEditViewImpl)view).lunchBreak.getValue());
		osceProxy.setLunchBreakRequiredTime(((OsceEditViewImpl)view).lunchBreakRequiredTime.getValue());
		osceProxy.setLongBreakRequiredTime(((OsceEditViewImpl)view).longBreakRequiredTime.getValue());
		osceProxy.setMiddleBreak(((OsceEditViewImpl)view).middleBreak.getValue());
		osceProxy.setStudyYear(((OsceEditViewImpl)view).studyYear.getValue());
		//osceProxy.setCopiedOsce(((OsceEditViewImpl)view).copiedOsce.getSelected());
		osceProxy.setShortBreakSimpatChange(((OsceEditViewImpl)view).shortBreakSimpatChange.getValue());
		osceProxy.setSemester(semester);
		osceProxy.setPostLength(((OsceEditViewImpl)view).postLength.getValue());
		/*if(((OsceEditViewImpl)view).isRepeOsce.isChecked())
		{
			osceProxy.setCopiedOsce(((OsceEditViewImpl)view).copiedOsce.getSelected());
		}
		else
		{
			osceProxy.setCopiedOsce(null);
		}*/
		osceProxy.setNumberCourses(((OsceEditViewImpl)view).numberCourses.getValue());
		osceProxy.setNumberRooms(((OsceEditViewImpl)view).numberRooms.getValue());
		osceProxy.setOsceStatus(OsceStatus.OSCE_NEW);
		osceProxy.setSecurity(OSCESecurityStatus.FEDERAL_EXAM);
		osceProxy.setOsceSecurityTypes(OsceSecurityType.federal);
		osceProxy.setSpStayInPost(((OsceEditViewImpl)view).spStayInPost.isChecked());
		
		try
		{
			TimeTableEstimation optimalSolution = TimeTableEstimation.getOptimalSolution(osceProxy, noOfOscePost, startTime, endTime);
			
			int numberOfDays = optimalSolution.getNumberDays();
			List<Integer>[] rotations = optimalSolution.getRotations();
			List<String> breakPerRotationByDay = optimalSolution.getBreakPerRotationByDay();
			
			createPredictionView(numberOfDays, rotations, breakPerRotationByDay, noOfOscePost, osceProxy, startTime, endTime);
		}
		catch (Exception e)
		{
			MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.messages());
			dialog.showConfirmationDialog(constants.precalculationError());
			e.printStackTrace();
		}
	}
	
	private void createPredictionView(int numberOfDays, List<Integer>[] rotations, List<String> breakPerRotationByDay, int noOfPost, OsceProxy osceProxy, Date startTime, Date endTime)
	{	
		view.getHorizontalTabPanel().clear();
		
		ImageResource icon1 = uiIcons.triangle1West(); 
		ImageResource icon2=  uiIcons.triangle1East();
		Unit u=Unit.PX;
		ScrolledTabLayoutPanel previewTabPanel = new ScrolledTabLayoutPanel(40L, u, icon1, icon2);
		view.getHorizontalTabPanel().add(previewTabPanel);
		view.getHorizontalTabPanel().addStyleName("horizontalPanelStyle");
		
		previousNoOfDays = numberOfDays;
		
		int startIndex = 0;
		int endIndex = 0;
		Date timeStart = startTime;
		Date timeEnd = endTime;
		Date displayTimeEnd = endTime;
		int canvasHeight = 0;
		
		for (int i=0; i<numberOfDays; i++)
		{
			String lunchBreakStart = "";
			
			if (i > 0)
				CalendarUtil.addDaysToDate(startTime, 1);
			
			timeStart = startTime;		
			
			String[] breakStr = breakPerRotationByDay.get(i).split("-");
			endIndex = startIndex + breakStr.length;
			
			int height = breakStr.length * 70;
			int width = 30 + (osceProxy.getNumberCourses() * 135);
						
			if (i==0)
			{
				previewTabPanel.setHeight((height + 200) + "px");
				canvasHeight = height;
			}
			
			SimplePanel simplePanel = new SimplePanel();
			
			VerticalPanel mainPanel = new VerticalPanel();
			mainPanel.setHeight("100%");
			
			previewTabPanel.insert(simplePanel, (constants.day() + (i+1)), i);
			
			width = width < 480 ? 480 : width;
			DrawingArea canvas = new DrawingArea(width, canvasHeight);
			
			simplePanel.add(mainPanel);
			
			int x = 10, y = 25;		
		
			createLegend(canvas, x, y);
			
			x = 10;
			y += 60;
		
			for(int j = 0; j < rotations.length; j++) {
				timeStart = startTime;
				createParcour(canvas, x, y, (constants.parcour() + " " + (j+1)));				
				ArrayList<Integer> rotationY = (ArrayList<Integer>) rotations[j];
				List<Integer> rotationX = rotationY.subList(startIndex, endIndex);
				
				if(rotationX.size() > 0) {
					int courseSlotsTotal = 0;
					Iterator<Integer> it = rotationX.iterator();
				
					x -= 5;
					y += 10;
					int index = 0;
					while(it.hasNext()) {
						int slots = it.next();
						Integer totalTime = ((noOfPost + slots) * osceProxy.getPostLength()) + (((noOfPost + slots) - 1) * osceProxy.getShortBreak());
						timeEnd = dateAddMin(timeStart, totalTime);
						DataObject objectData = new DataObject(dateFormat.format(timeStart), timeFormat.format(timeStart), timeFormat.format(timeEnd));
						if (slots == 1)
						{
							createRotatioWithLogicalBreak(canvas, x, y, (constants.rotation() + " " + (index+1)), objectData);
							y += 35;
						}
						else if (slots == 0)
						{
							createRotationWithoutLogicalBreak(canvas, x, y, (constants.rotation() + " " + (index+1)), objectData);
							y += 35;
						}
						
						timeStart = timeEnd;
						
						String[] temp = breakStr[index].split(":");
						
						int breakValue = 0;
						if (temp.length > 1)						
							breakValue = Integer.parseInt(temp[1]);
						timeEnd = dateAddMin(timeStart, breakValue);
						DataObject breakData = new DataObject(dateFormat.format(timeStart), timeFormat.format(timeStart), timeFormat.format(timeEnd));
						
						String colorCode = "";
						if (osceProxy.getMiddleBreak().intValue() == breakValue)
						{	
							colorCode = "#a1773b";
							createBreak(canvas, x, y, "Middle Break", breakData, colorCode);							
							y += 10;
						}
						else if (osceProxy.getLongBreak().intValue() == breakValue)
						{
							colorCode = "#65b4eb";
							createBreak(canvas, x, y, "Long Break", breakData, colorCode);
							y += 10;
						}
						else if (osceProxy.getLunchBreak().intValue() == breakValue)
						{
							lunchBreakStart = timeFormat.format(timeStart);
							colorCode = "#54ac56";
							createBreak(canvas, x, y, "Lunch Break", breakData, colorCode);
							y += 10;
						}
						
						timeStart = timeEnd;
						
						courseSlotsTotal += noOfPost + slots;
						index++;
					}
					
					x += 145;
					y = 85;
					
				}
				
				if (j==0)
					displayTimeEnd = timeEnd;
			}
			
			VerticalPanel lablePanel = createLabel(noOfPost, dateFormat.format(timeStart), timeFormat.format(startTime), timeFormat.format(displayTimeEnd), lunchBreakStart);
			mainPanel.add(lablePanel);
			mainPanel.add(canvas);
		}
		
		previewTabPanel.selectTab(0);
	}
	
	private VerticalPanel createLabel(int noOfPost, String osceDate, String startTime, String endTime, String lunchBreakStart) {
		HorizontalPanel hp1 = new HorizontalPanel();
		hp1.setSpacing(5);
		
		Label oscePostLbl = new Label(constants.oscePost() + " : ");
		Label oscePostValLbl = new Label(String.valueOf(noOfPost));
		Label osceDateLbl = new Label(constants.osceDate() + " : ");
		Label osceDateValLbl = new Label(osceDate);
		
		oscePostLbl.setWidth("90px");
		oscePostValLbl.setWidth("90px");
		osceDateLbl.setWidth("90px");
		osceDateValLbl.setWidth("90px");
		
		oscePostLbl.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		osceDateLbl.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		
		hp1.add(oscePostLbl);
		hp1.add(oscePostValLbl);
		hp1.add(osceDateLbl);
		hp1.add(osceDateValLbl);
		
		HorizontalPanel hp2 = new HorizontalPanel();
		hp2.setSpacing(5);
		
		Label startTimeLbl = new Label(constants.startTime() +  " : ");
		Label startTimeValLbl = new Label(startTime);
		Label endTimeLbl = new Label(constants.endTime() + " : ");
		Label endTimeValLbl = new Label(endTime);
		
		startTimeLbl.setWidth("90px");
		startTimeValLbl.setWidth("90px");
		endTimeLbl.setWidth("90px");
		endTimeValLbl.setWidth("90px");
		
		startTimeLbl.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		endTimeLbl.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		
		hp2.add(startTimeLbl);
		hp2.add(startTimeValLbl);
		hp2.add(endTimeLbl);
		hp2.add(endTimeValLbl);
		
		HorizontalPanel hp3 = new HorizontalPanel();
		hp3.setSpacing(5);
		
		Label lunchBreakLbl = new Label(constants.lunchBreak() + " : ");
		Label lunchBreakValLbl = new Label(lunchBreakStart.isEmpty() ? constants.notAvailable() : lunchBreakStart);
		
		lunchBreakLbl.setWidth("90px");
		lunchBreakValLbl.setWidth("90px");
		
		lunchBreakLbl.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		
		hp3.add(lunchBreakLbl);
		hp3.add(lunchBreakValLbl);
			
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setSpacing(5);
		mainPanel.add(hp1);
		mainPanel.add(hp2);
		mainPanel.add(hp3);
		
		return mainPanel;
	}

	private void createLegend(DrawingArea canvas, int x, int y) {
		Rectangle middleBreakRectangle = new Rectangle(x, y, 20, 10);		
		middleBreakRectangle.setStrokeColor("#a1773b");
		middleBreakRectangle.setFillColor("#a1773b");
		canvas.add(middleBreakRectangle);		
		
		x += 25;
		y += 10;
		
		Text middleBreakText = new Text(x, y, constants.middleBreak());
		middleBreakText.setStrokeColor("black");
		middleBreakText.setFillColor("black");
		middleBreakText.setFontFamily("Verdana");
		middleBreakText.setFontSize(12);
		middleBreakText.setStrokeOpacity(0);
		canvas.add(middleBreakText);
		canvas.bringToFront(middleBreakText);

		y -= 10;
		x += 100;
		
		Rectangle longBreakRectangle = new Rectangle(x, y, 20, 10);		
		longBreakRectangle.setStrokeColor("#65b4eb");
		longBreakRectangle.setFillColor("#65b4eb");
		canvas.add(longBreakRectangle);		
		
		x += 25;
		y += 10;
		
		Text legendBreakText = new Text(x, y, constants.longBreak());
		legendBreakText.setStrokeColor("black");
		legendBreakText.setFillColor("black");
		legendBreakText.setFontFamily("Verdana");
		legendBreakText.setFontSize(12);
		legendBreakText.setStrokeOpacity(0);
		canvas.add(legendBreakText);
		canvas.bringToFront(legendBreakText);
		
		y -= 10;
		x += 90;
		
		Rectangle lunchBreakRectangle1 = new Rectangle(x, y, 20, 10);		
		lunchBreakRectangle1.setStrokeColor("#54ac56");
		lunchBreakRectangle1.setFillColor("#54ac56");
		canvas.add(lunchBreakRectangle1);		
		
		x += 25;
		y += 10;
		
		Text lunchBreakText1 = new Text(x, y, constants.lunchBreak());
		lunchBreakText1.setStrokeColor("black");
		lunchBreakText1.setFillColor("black");
		lunchBreakText1.setFontFamily("Verdana");
		lunchBreakText1.setFontSize(12);
		lunchBreakText1.setStrokeOpacity(0);
		canvas.add(lunchBreakText1);
		canvas.bringToFront(lunchBreakText1);
		
		y -= 10;
		x += 90;
		
		Rectangle logicalBreakRectangle1 = new Rectangle(x, y, 20, 10);		
		logicalBreakRectangle1.setStrokeColor("#de8080");
		logicalBreakRectangle1.setFillColor("#de8080");
		canvas.add(logicalBreakRectangle1);		
		
		x += 25;
		y += 10;
		
		Text logicalBreakText = new Text(x, y, constants.logicalBreak());
		logicalBreakText.setStrokeColor("black");
		logicalBreakText.setFillColor("black");
		logicalBreakText.setFontFamily("Verdana");
		logicalBreakText.setFontSize(12);
		logicalBreakText.setStrokeOpacity(0);
		canvas.add(logicalBreakText);
		canvas.bringToFront(logicalBreakText);
	}

	public void createParcour(DrawingArea canvas, int x, int y, String title)
	{
		x -= 5;
		y -= 20;
		Rectangle rectangle = new Rectangle(x, y, 125, 40);
		rectangle.setStrokeColor("#999999");
		rectangle.setFillColor("#999999");
		rectangle.setRoundedCorners(10);
		canvas.add(rectangle);
		
		x += 15;
		y += 20;
		Text text = new Text(x, y, title);
		text.setStrokeColor("white");
		text.setFillColor("white");
		text.setFontFamily("Verdana");
		text.setFontSize(12);
		text.setStrokeOpacity(0);
		
		canvas.add(text);
		canvas.bringToFront(text);
		
				
	}
	
	public void createBreak(DrawingArea canvas, int x, int y, String title, DataObject breakData, String colorCode)
	{		
		final Rectangle breakRectangle = new Rectangle(x, y, 125, 5);		
		breakRectangle.setStrokeColor(colorCode);
		breakRectangle.setFillColor(colorCode);
		breakRectangle.setLayoutData(breakData);
		
		breakRectangle.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				DataObject objectData = (DataObject)breakRectangle.getLayoutData();
				openPopup(objectData, breakRectangle.getAbsoluteLeft() - 30, breakRectangle.getAbsoluteTop() - 160);
				
				/*DataObject objectData = (DataObject)breakRectangle.getLayoutData();
				PreCalculationPopupViewImpl popupView = new PreCalculationPopupViewImpl();
				popupView.getOsceDateValLbl().setText(objectData.getOsceDate());
				popupView.getStartTimeValLbl().setText(objectData.getStartTime());
				popupView.getEndTimeValLbl().setText(objectData.getEndTime());
				popupView.setPopupPosition(breakRectangle.getAbsoluteLeft() - 30, breakRectangle.getAbsoluteTop() - 160);
				popupView.show();	*/
			}
		});
		
		canvas.add(breakRectangle);
		
		x += 10;
		y += 12;
		final Text breakText = new Text(x, y, "");
		breakText.setFillColor("white");
		breakText.setFontFamily("Verdana");
		breakText.setFontSize(12);
		breakText.setLayoutData(breakData);
		breakText.setStrokeOpacity(0);
		
		canvas.add(breakText);
		canvas.bringToFront(breakText);
	}
	
	public void createRotationWithoutLogicalBreak(DrawingArea canvas, int x, int y, String title, DataObject objectData)
	{
		final Rectangle rectangle = new Rectangle(x, y, 125, 30);
		rectangle.setStrokeColor("#e5e5e5");
		rectangle.setFillColor("#e5e5e5");
		rectangle.setLayoutData(objectData);
		rectangle.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				DataObject dataObject = (DataObject)rectangle.getLayoutData();
				openPopup(dataObject, (rectangle.getAbsoluteLeft() - 30), (rectangle.getAbsoluteTop() - 160));				
			}
		});
		canvas.add(rectangle);
		
		x += 15;
		y += 20;
		final Text text = new Text(x, y, title);
		text.setFillColor("black");
		text.setFontFamily("Verdana");
		text.setFontSize(12);
		text.setStrokeOpacity(0);
		text.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				DataObject dataObject = (DataObject)rectangle.getLayoutData();
				openPopup(dataObject, (rectangle.getAbsoluteLeft() - 30), (rectangle.getAbsoluteTop() - 160));				
			}
		});
		canvas.add(text);
		canvas.bringToFront(text);
	}
	
	public void createRotatioWithLogicalBreak(DrawingArea canvas, int x, int y, String title, DataObject objectData)
	{
		int x1, y1;
		x1 = x;
		y1 = y;
		
		final Rectangle rectangle = new Rectangle(x, y, 125, 30);
		rectangle.setStrokeColor("#e5e5e5");
		rectangle.setFillColor("#e5e5e5");
		rectangle.setLayoutData(objectData);
		rectangle.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				DataObject dataObject = (DataObject)rectangle.getLayoutData();
				openPopup(dataObject, (rectangle.getAbsoluteLeft() - 30), (rectangle.getAbsoluteTop() - 160));	
			}
		});		
		canvas.add(rectangle);
				
		x += 15;
		y += 20;
		final Text text = new Text(x, y, title);
		text.setFillColor("black");
		text.setFontFamily("Verdana");
		text.setFontSize(12);		
		text.setStrokeOpacity(0);
		text.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				DataObject dataObject = (DataObject)rectangle.getLayoutData();
				openPopup(dataObject, (rectangle.getAbsoluteLeft() - 30), (rectangle.getAbsoluteTop() - 160));
			}
		});		
		canvas.add(text);
		canvas.bringToFront(text);
		
		x1 += 105;
		
		Rectangle breakRectangle = new Rectangle(x1, y1, 20, 30);
		breakRectangle.setStrokeColor("#de8080");
		breakRectangle.setFillColor("#de8080");
		breakRectangle.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				DataObject dataObject = (DataObject)rectangle.getLayoutData();
				openPopup(dataObject, (rectangle.getAbsoluteLeft() - 30), (rectangle.getAbsoluteTop() - 160));	
			}
		});		
		canvas.add(breakRectangle);
		
		x += 97;
		Text logicalBreakText = new Text(x, y, "L");
		logicalBreakText.setFillColor("black");
		logicalBreakText.setFontFamily("Verdana");
		logicalBreakText.setFontSize(12);
		logicalBreakText.setStrokeOpacity(0);
		logicalBreakText.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				DataObject dataObject = (DataObject)rectangle.getLayoutData();
				openPopup(dataObject, (rectangle.getAbsoluteLeft() - 30), (rectangle.getAbsoluteTop() - 160));	
			}
		});		
		canvas.add(logicalBreakText);
		canvas.bringToFront(logicalBreakText);		
	}
	
	private void openPopup(DataObject dataObject, int left, int top)
	{
		PreCalculationPopupViewImpl popupView = new PreCalculationPopupViewImpl();
		popupView.getOsceDateValLbl().setText(dataObject.getOsceDate());
		popupView.getStartTimeValLbl().setText(dataObject.getStartTime());
		popupView.getEndTimeValLbl().setText(dataObject.getEndTime());
		popupView.setPopupPosition(left, top);
		popupView.show();	
	}
	
	private Date dateAddMin(Date date, long minToAdd) {
		return new Date((long) (date.getTime() + minToAdd * 60 * 1000));
	}
	
	private class DataObject
	{
		private String osceDate;
		private String startTime;
		private String endTime;
		
		public DataObject(String osceDate, String startTime, String endTime) {
			this.startTime = startTime;
			this.endTime = endTime;
			this.osceDate = osceDate;
		}
		
		public String getStartTime() {
			return startTime;
		}
		
		public String getEndTime() {
			return endTime;
		}
		public String getOsceDate() {
			return osceDate;
		}
	}

	

	
}
