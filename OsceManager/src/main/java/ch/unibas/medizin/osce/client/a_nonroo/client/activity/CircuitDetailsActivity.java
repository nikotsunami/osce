package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.CircuitDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.AccordianPanelView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.AccordianPanelViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.CircuitDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.CircuitDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.CircuitOsceSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.CircuitOsceSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.ContentView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.ContentViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.HeaderView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.HeaderViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.ListBoxPopupView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.ListBoxPopupViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OSCENewSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OSCENewSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OsceCreatePostBluePrintSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OsceCreatePostBluePrintSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OsceDayView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OsceDayViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OsceGenerateSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OsceGenerateSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OscePostSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OscePostSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OscePostView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OscePostViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.SequenceOsceSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.SequenceOsceSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.CourseRequest;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayRequest;
import ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintRequest;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRequest;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomRequest;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceRequest;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceRequest;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.RoomProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.widgets.FocusableValueListBox;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import ch.unibas.medizin.osce.domain.OscePostRoom;
import ch.unibas.medizin.osce.shared.ColorPicker;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.OsceSequences;
import ch.unibas.medizin.osce.shared.OsceStatus;
import ch.unibas.medizin.osce.shared.PostType;
import ch.unibas.medizin.osce.shared.util;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.shared.i18n.OsceConstantsWithLookup;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxy;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.requestfactory.shared.Violation;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


@SuppressWarnings("deprecation")
public class CircuitDetailsActivity extends AbstractActivity implements
CircuitDetailsView.Presenter, 
CircuitDetailsView.Delegate,CircuitOsceSubView.Delegate,
OsceGenerateSubView.Delegate,OscePostSubView.Delegate,
ListBoxPopupView.Delegate,
OscePostView.Delegate
,HeaderView.Delegate,DragHandler,//5C:SPEC START
OSCENewSubView.Delegate,
OsceCreatePostBluePrintSubView.Delegate,
OsceDayView.Delegate ,
SequenceOsceSubView.Delegate,
AccordianPanelView.ParcourDelegate
{//Assignment E:Module 5

		private OsMaRequestFactory requests;
		private PlaceController placeController;
		private AcceptsOneWidget widget;
		private CircuitDetailsView view;
		private boolean isgenerated=false;
		//5C:SPEC START
		private static final OsceConstants constants = GWT.create(OsceConstants.class);
		private OsceProxy osceProxy;
		private OsceDayViewImpl osceDayViewImpl;
		private SequenceOsceSubViewImpl sequenceOsceSubViewImpl;
		private List<SequenceOsceSubViewImpl> sequenceOsceSubViewImpl1;
		
		private CircuitOsceSubViewImpl circuitOsceSubViewImpl;
		
		private OscePostBlueprintProxy oscePostBlueprintProxy;
		List<OscePostSubViewImpl>  oscePostSubViewImpl;
		HorizontalPanel newPostAddHP;
		HorizontalPanel newPostHP;
		OscePostViewImpl oscePostViewImpl;
		CircuitDetailsActivity circuitDetailsActivity;
		
		OsceConstantsWithLookup enumConstants = GWT.create(OsceConstantsWithLookup.class);
		OsceCreatePostBluePrintSubViewImpl osceCreatePostBluePrintSubViewImpl;
		private List<SpecialisationProxy> specialisationList;
		int indexGlobal;
		int maxSeq=0;
		OSCENewSubViewImpl oSCENewSubViewImpl;	
		//5C:SPEC END
		public boolean findspecialisation=false;
		// Module 5 bug Report Change
		OsceProxy osceProxyforFixedStatus;		
		// E Module 5 bug Report Change
		 public boolean successValue=false;
		//Module 5 Bug Report Solution
		OsceProxy osceToRefreshPlace;
		//E Module 5 Bug Report Solution
		
		public List<OsceDayProxy> days=new ArrayList<OsceDayProxy>();
		
		private CircuitDetailsPlace place;
		private CircuitDetailsActivity activity;
		public CircuitDetailsActivity(CircuitDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
			this.place = place;
	    	this.requests = requests;
	    	this.placeController = placeController;
	    	this.activity=this;
	    	this.circuitDetailsActivity=this;
	    }
		
		public void onStop(){

		}

		@Override
		public void start(AcceptsOneWidget panel, EventBus eventBus) {
			Log.info("CircuitDetailsActivity.start()");
			final CircuitDetailsView circuitDetailsView = new CircuitDetailsViewImpl();
			circuitDetailsView.setPresenter(this);
			this.widget = panel;
			this.view = circuitDetailsView;
			widget.setWidget(circuitDetailsView.asWidget());
			
			view.setDelegate(this);
			
			circuitDetailsView.setDelegate(this);
			
			// Module 5 Bug Test Change
			
			ApplicationLoadingScreenEvent.register(requests.getEventBus(),
					new ApplicationLoadingScreenHandler() {
						@Override
						public void onEventReceived(
								ApplicationLoadingScreenEvent event) {
							//Log.info("~~~~~~~~ApplicationLoadingScreenEvent onEventReceived Called");
							event.display();
							
						}
			});
			
			
			// E Module 5 Bug Test Change			
			
			requests.specialisationRequest().findAllSpecialisations().with("roleTopics").fire(new OSCEReceiver<List<SpecialisationProxy>>() 
					{

						public void onSuccess(List<SpecialisationProxy> response) 
						{	
							// Module 5 Bug Test Change
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
							// E Module 5 Bug Test Change
							
							specialisationList = new ArrayList<SpecialisationProxy>();				
							specialisationList.addAll(response);	
					
					//5C:SPEC START
			requests.find(place.getProxyId()).with("osces").with("oscePostBlueprints","oscePostBlueprints.specialisation","oscePostBlueprints.roleTopic").with("osce_days").with("osce_days.osceSequences").with("osce_days.osceSequences.oscePosts").with("osce_days.osceSequences.oscePosts.oscePostBlueprint").with("osce_days.osceSequences.oscePosts.standardizedRole").with("osce_days.osceSequences.oscePosts.oscePostBlueprint.roleTopic").with("osce_days.osceSequences.oscePosts.oscePostBlueprint.specialisation").with("osce_days.osceSequences.oscePosts.oscePostBlueprint.postType").with("osce_days.osceSequences.courses").with("osce_days.osceSequences.courses.oscePostRooms").with("osce_days.osceSequences.courses.oscePostRooms.oscePost").with("osce_days.osceSequences.courses.oscePostRooms.oscePost.oscePostBlueprint").with("osce_days.osceSequences.courses.oscePostRooms.oscePost.standardizedRole").with("osce_days.osceSequences.courses.oscePostRooms.room").with("osce_days.osceSequences.courses.oscePostRooms.oscePost.oscePostBlueprint.roleTopic").with("osce_days.osceSequences.courses.oscePostRooms.oscePost.oscePostBlueprint.postType").with("osce_days.osceSequences.courses.oscePostRooms.oscePost.oscePostBlueprint.specialisation").fire(new OSCEReceiver<Object>() {

				@Override
				public void onSuccess(Object response) {
					if(response instanceof OsceProxy && response != null){
						
						osceProxy=(OsceProxy)response;
						Log.info("Arrived OsceProxy At CircuitDetailActivity");
						
						circuitOsceSubViewImpl=view.getcircuitOsceSubViewImpl();
						
						OsceStatus status = ((OsceProxy) response).getOsceStatus();
						
						
						
						// Module 5 Bug Solution
						//String style = status.getOsceStatus(status);
						//setOsceStatusStyle(style);
						// E Module 5 Bug Solution
						
						//circuitOsceSubViewImpl.setStyleName(style);
						circuitOsceSubViewImpl.shortBreakTextBox.setValue(util.checkShort((((OsceProxy) response).getShortBreak())));
						circuitOsceSubViewImpl.longBreakTextBox.setValue(util.checkShort((((OsceProxy) response).getLongBreak())));
						circuitOsceSubViewImpl.launchBreakTextBox.setValue(util.checkShort((((OsceProxy) response).getLunchBreak())));
						circuitOsceSubViewImpl.maxStudentTextBox.setValue(util.checkInteger(((OsceProxy) response).getMaxNumberStudents()));
						circuitOsceSubViewImpl.maxParcourTextBox.setValue(util.checkInteger(((OsceProxy) response).getNumberCourses()));
						//Log.info("Room Value"+ ((OsceProxy) response).getNumberRooms());
						circuitOsceSubViewImpl.maxRoomsTextBox.setValue(util.checkInteger((((OsceProxy) response).getNumberRooms())));
						
						// Module 5 bug Report Change
						circuitOsceSubViewImpl.middleBreakTextBox.setValue(util.checkShort((((OsceProxy) response).getMiddleBreak())));
						circuitOsceSubViewImpl.shortBreakSimpatTextBox.setValue(util.checkShort((((OsceProxy) response).getShortBreakSimpatChange())));
						// E Module 5 bug Report Change
						
						
						
						
						circuitOsceSubViewImpl.setProxy((OsceProxy)response);
						circuitOsceSubViewImpl.setClearAllBtn(((OsceProxy)response).getOsceStatus() == OsceStatus.OSCE_GENRATED);
						circuitOsceSubViewImpl.setDelegate(activity);
						
						// Module 5 changes {
						
						setOsceFixedButtonStyle(circuitOsceSubViewImpl,osceProxy);
						
						// Module 5 changes }

						//Assignment E:Module 5[
						//5C:SPEC START		
						//view.getScrollPanel().setStylePrimaryName("Osce-Status");
						//view.getScrollPanel().setStyleName("Osce-Status");
						osceProxy=(OsceProxy)response;
						if((osceProxy.getOsceStatus() == OsceStatus.OSCE_BLUEPRINT) || (osceProxy.getOsceStatus() == OsceStatus.OSCE_NEW))
						{
							// Module 5 bug Report Change
							circuitOsceSubViewImpl.shortBreakTextBox.setEnabled(true);
							circuitOsceSubViewImpl.longBreakTextBox.setEnabled(true);
							circuitOsceSubViewImpl.launchBreakTextBox.setEnabled(true);
							circuitOsceSubViewImpl.maxStudentTextBox.setEnabled(true);
							circuitOsceSubViewImpl.maxParcourTextBox.setEnabled(true);
							circuitOsceSubViewImpl.maxRoomsTextBox.setEnabled(true);
							circuitOsceSubViewImpl.shortBreakSimpatTextBox.setEnabled(true);
							circuitOsceSubViewImpl.middleBreakTextBox.setEnabled(true);
							
							circuitOsceSubViewImpl.saveOsce.setEnabled(true);
							// E Module 5 bug Report Change
							
						// Module 5 Bug Solution
						/*if((osceProxy.getOsceStatus() == OsceStatus.OSCE_NEW))
						{
							view.getScrollPanel().addStyleDependentName("NEW");
						}
						else
						if((osceProxy.getOsceStatus() == OsceStatus.OSCE_BLUEPRINT))
						{
							view.getScrollPanel().addStyleDependentName("BluePrint");
						}*/
						// E Module 5 Bug Solution
						
						List<OscePostBlueprintProxy>listOscePostBlueprintProxy = osceProxy.getOscePostBlueprints();
						
						oSCENewSubViewImpl=new OSCENewSubViewImpl();//.getOSCENewSubViewImpl();
						 
						view.getGenerateVP().add(new Label(enumConstants.getString(osceProxy.getOsceStatus().toString())));
						view.getGenerateVP().insert(oSCENewSubViewImpl, view.getGenerateVP().getWidgetCount());
						//oSCENewSubViewImpl.
						((CircuitDetailsViewImpl)view).oSCENewSubViewImpl=oSCENewSubViewImpl;
						oSCENewSubViewImpl.setDelegate(activity);
						
						//Osce Days[
						osceDayViewImpl = oSCENewSubViewImpl.getOsceDayViewImpl();

						//Module 5 Bug Report Solution
						osceDayViewImpl.getSchedulePostponenButton().setVisible(false);
						osceDayViewImpl.getScheduleEarlierButton().setVisible(false);
						//E Module 5 Bug Report Solution
						
						//spec issue sol
						osceDayViewImpl.getBtnShiftLunchBreakNext().setVisible(false);
						osceDayViewImpl.getBtnShiftLunchBreakPrev().setVisible(false);
						
						osceDayViewImpl.setDelegate(activity);
						// Day Assignment 
						
						
						//bug solve start
						
						osceDayViewImpl.getInnerCalculationVerticalPanel().setVisible(false);
						//bug solve end
						Log.info("Before Iterator");
						
					
						
						List<OsceDayProxy> setOsceDays = ((OsceProxy) response).getOsce_days();
						if(setOsceDays.size()==0){
							Log.info("OsceDay null for proxy : " +osceProxy.getId());
							osceDayViewImpl.setOsceDayProxy(null);
						}
						else{
							Log.info("Osce Exist for OsceProxy : " + osceProxy.getId());
							//Module 5 Bug Report Solution
							//Iterator<OsceDayProxy> osceDays = setOsceDays.iterator();
							Iterator<OsceDayProxy> osceDaysIterator = setOsceDays.iterator();
							if(osceDaysIterator.hasNext())
							//E Module 5 Bug Report Solution
							{								
								//Module 5 Bug Report Solution
								//osceDayViewImpl.setOsceDayProxy(osceDays.next());
								OsceDayProxy osceDays=osceDaysIterator.next();
								osceDayViewImpl.setOsceDayProxy(osceDays);

								/*
								if(osceDays.getOsceDate()==null)
								{
									osceDayViewImpl.getDateContentValueLabel().setText("");
								}
								else
								{
									osceDayViewImpl.getDateContentValueLabel().setText(osceDays.getOsceDate().getYear()+"-"+osceDays.getOsceDate().getMonth()+"-"+osceDays.getOsceDate().getDate());	
								}
								
								if(osceDays.getTimeStart()==null)
								{
									osceDayViewImpl.getLunchBreakStartValueLabel().setText("");
								}
								else
								{
									osceDayViewImpl.getLunchBreakStartValueLabel().setText(DateTimeFormat.getFormat("HH:mm").format(osceDays.getTimeStart()).substring(0,5));	
								}
								
								if(osceDays.getLunchBreakStart()==null)
								{
									osceDayViewImpl.getLunchBreakEndTimeValueLabel().setText("");
								}
								else
								{
									osceDayViewImpl.getLunchBreakEndTimeValueLabel().setText(DateTimeFormat.getFormat("HH:mm").format(osceDays.getLunchBreakStart()).substring(0,5));	
								}
								if(osceDays.getTimeEnd()==null)
								{
									osceDayViewImpl.getLunchBreakEndTimeValueLabel().setText("");
								}
								else
								{
									osceDayViewImpl.getLunchBreakEndTimeValueLabel().setText(DateTimeFormat.getFormat("HH:mm").format(osceDays.getTimeEnd()).substring(0,5));	
								}
								
								*/
								if(osceDays.getOsceDate()==null)
								{
									osceDayViewImpl.getDateContentValueLabel().setText("  ");
								}
								else
								{
									//osceDayViewImpl.getDateContentValueLabel().setText(osceDayProxy.getOsceDate().getYear()+"-"+osceDayProxy.getOsceDate().getMonth()+"-"+osceDayProxy.getOsceDate().getDate());
									osceDayViewImpl.getDateContentValueLabel().setText(DateTimeFormat.getFormat("yyyy-MM-dd").format(osceDays.getOsceDate()));
								}
																
								if(osceDays.getTimeStart()==null)
								{
									osceDayViewImpl.getLunchBreakStartValueLabel().setText("");
								}
								else
								{
									osceDayViewImpl.getLunchBreakStartValueLabel().setText(DateTimeFormat.getFormat("HH:mm").format(osceDays.getTimeStart()).substring(0,5));	
								}
								
								if(osceDays.getLunchBreakStart()==null)
								{
									osceDayViewImpl.getLunchBreakValueLabel().setText("  ");
								}
								else
								{
									osceDayViewImpl.getLunchBreakValueLabel().setText(DateTimeFormat.getFormat("HH:mm").format(osceDays.getLunchBreakStart()).substring(0,5));	
								}
								if(osceDays.getTimeEnd()==null)
								{
									osceDayViewImpl.getLunchBreakEndTimeValueLabel().setText("  ");
								}
								else
								{
									osceDayViewImpl.getLunchBreakEndTimeValueLabel().setText(DateTimeFormat.getFormat("HH:mm").format(osceDays.getTimeEnd()).substring(0,5));	
								}
								/*if(osceDays.getOsceSequences().size()>1)
								{
									Log.info("setSize");
									osceDayViewImpl.getMainDayHP().setHeight("568px");									
									osceDayViewImpl.getCalculationVerticalPanel().getElement().getStyle().setMarginTop(30,Unit.PX);
									osceDayViewImpl.getScheduleHP().getElement().getStyle().setMarginTop(30,Unit.PX);
									osceDayViewImpl.getSaveVerticlePanel().getElement().getStyle().setMarginTop(50,Unit.PX);									
								}*/
								
								//E Module 5 Bug Report Solution
							}
						}
						
						//setDayStatusStyle(style);
						osceDayViewImpl.init();
						addTimeHendlers();
						
						//Osce Days]
						
						
						
						//Log.info("Osce Post Blue Print Size : "+ listOscePostBlueprintProxy.size());						
							
							osceCreatePostBluePrintSubViewImpl=new OsceCreatePostBluePrintSubViewImpl(specialisationList);
							osceCreatePostBluePrintSubViewImpl.setStyleName("Osce-Status-BluePrint-Create", true);
							newPostAddHP=oSCENewSubViewImpl.getOscePostBluePrintSubViewImpl().getOscePostBluePrintSubViewImplHP();
							newPostHP=oSCENewSubViewImpl.getOscePostBluePrintSubViewImpl().newPostHP;
							//newPostAddHP.setStyleName("Osce_BluePrint_Status");							
							
							if(listOscePostBlueprintProxy.size()>0)
							{
								Log.info("Osce has Osce Post Blueprint " + listOscePostBlueprintProxy.size());
								
								int index=0;
							
								if(oscePostSubViewImpl == null){
									oscePostSubViewImpl = new ArrayList<OscePostSubViewImpl>();
								}																								
								OscePostSubViewImpl tempOscePostSubViewImpl;				
								
								Iterator<OscePostBlueprintProxy> iter = listOscePostBlueprintProxy.iterator();								
								
									while (iter.hasNext()) 
									{																				
										oscePostBlueprintProxy=iter.next();
													
								
										Log.info("~OsceBluerint Id: " + oscePostBlueprintProxy.getId());
										
										tempOscePostSubViewImpl=new OscePostSubViewImpl();										
										tempOscePostSubViewImpl.enableDisableforBluePrintStatus();
										tempOscePostSubViewImpl.setDelegate(circuitDetailsActivity); // SET DELEGATE FOR SUBVIEW
										tempOscePostSubViewImpl.getRoleTopicLbl().setText(getLabelString(oscePostBlueprintProxy.getRoleTopic()==null?constants.select()+": ":oscePostBlueprintProxy.getRoleTopic().getName()));
										tempOscePostSubViewImpl.getRoleTopicLbl().setTitle(oscePostBlueprintProxy.getRoleTopic()==null?constants.select()+": ":oscePostBlueprintProxy.getRoleTopic().getName());
										tempOscePostSubViewImpl.getSpecializationLbl().setText(getLabelString(oscePostBlueprintProxy.getSpecialisation()==null?constants.select()+": ":oscePostBlueprintProxy.getSpecialisation().getName()));
										tempOscePostSubViewImpl.getSpecializationLbl().setTitle(oscePostBlueprintProxy.getSpecialisation()==null?constants.select()+": ":oscePostBlueprintProxy.getSpecialisation().getName());
                                                                                // Module 5 bug Report Change									
                                        tempOscePostSubViewImpl.oscePostBlueprintProxy=oscePostBlueprintProxy;
									        // E Module 5 bug Report Change	
									
										oscePostViewImpl=new OscePostViewImpl();
										oscePostViewImpl.setStyleName("Osce-Status-BluePrint-Save", true);
										oSCENewSubViewImpl.getOscePostBluePrintSubViewImpl().getDragController().makeDraggable(oscePostViewImpl,oscePostViewImpl.getPostTypeLbl());
										oSCENewSubViewImpl.getOscePostBluePrintSubViewImpl().getDragController().addDragHandler(activity);
										
										oscePostViewImpl.setDelegate(circuitDetailsActivity);	// SET DELEGATE FOR MAIN POST VIEW
										oscePostViewImpl.oscePostBlueprintProxy=oscePostBlueprintProxy;	
										//oscePostViewImpl.getOscePostSubViewHP().setStyleName("Osce_Genrated_Status");						
										
										oscePostViewImpl.getPostTypeLbl().setText(enumConstants.getString(oscePostBlueprintProxy.getPostType().name()));
										

										// Module 5 bug Report Change										
										//if(oscePostViewImpl.getPostTypeLbl().getText().compareToIgnoreCase("Break*")==0)
										Log.info("Break Found: " + tempOscePostSubViewImpl.oscePostBlueprintProxy.getId());
										if(tempOscePostSubViewImpl.oscePostBlueprintProxy.getPostType()==PostType.BREAK)
										{
											Log.info("~~~~Break Found");
											tempOscePostSubViewImpl.getSpecializationedit().setVisible(false);
											tempOscePostSubViewImpl.getRoleTopicEdit().setVisible(false);
											tempOscePostSubViewImpl.getRoomedit().setVisible(false);
											tempOscePostSubViewImpl.getStandardizedRoleEdit().setVisible(false);
											tempOscePostSubViewImpl.getSpecializationLbl().setVisible(false);
											tempOscePostSubViewImpl.getRoleTopicLbl().setVisible(false);
											tempOscePostSubViewImpl.getStandardizedRoleLbl().setVisible(false);
											tempOscePostSubViewImpl.getRoomLbl().setVisible(false);
										}
										// E Module 5 bug Report Change
										
										
										oscePostSubViewImpl.add(tempOscePostSubViewImpl);					
										oscePostSubViewImpl.get(index).setDelegate(circuitDetailsActivity); // SET DELEGATE FOR SUBVIEW
										oscePostSubViewImpl.get(index).oscePostBlueprintProxy=oscePostBlueprintProxy;		
										oscePostSubViewImpl.get(index).getPostNameLbl().setText(getLabelString(constants.circuitStation() + " " + oscePostBlueprintProxy.getSequenceNumber()));
										oscePostSubViewImpl.get(index).getPostNameLbl().setTitle(constants.circuitStation() + " " + oscePostBlueprintProxy.getSequenceNumber());
										maxSeq=oscePostBlueprintProxy.getSequenceNumber();
										oscePostViewImpl.getOscePostSubViewHP().add(oscePostSubViewImpl.get(index));	// ADD SUBVIEW IN POSTVIEW
										
										/*// Module 5 bug Report Change
										//Try to solve bug
										final int indexToEditSpecialisation1=index;
										oscePostSubViewImpl.get(indexToEditSpecialisation1).getSpecializationedit().addClickHandler(new ClickHandler() 
										{
											@Override
											public void onClick(ClickEvent event) {
												Log.info("Click Handler call from Circuit Detail Activity." + indexToEditSpecialisation1);
												Log.info("Widget: " + oscePostSubViewImpl.get(indexToEditSpecialisation1));
												Log.info("Osce Post Blue Print Id: " + oscePostSubViewImpl.get(indexToEditSpecialisation1).oscePostBlueprintProxy.getId());
											}
										});
										// E Module 5 bug Report Change
*/										
										if(oscePostBlueprintProxy.getPostType()==PostType.ANAMNESIS_THERAPY || oscePostBlueprintProxy.getPostType()==PostType.PREPARATION)
										{
											Log.info("~Anemnis");				
											if(iter.hasNext())
											{
												oscePostBlueprintProxy=iter.next();											
												
												tempOscePostSubViewImpl=new OscePostSubViewImpl();
												tempOscePostSubViewImpl.enableDisableforBluePrintStatus();	
												
												// Module 5 Bug Test Change
												tempOscePostSubViewImpl.oscePostBlueprintProxy=oscePostBlueprintProxy;
												// E Module 5 Bug Test Change
												
												tempOscePostSubViewImpl.setDelegate(circuitDetailsActivity);	// SET DELEGATE FOR SUBVIEW
										
												tempOscePostSubViewImpl.getSpecializationLbl().setText(getLabelString(oscePostBlueprintProxy.getSpecialisation()==null?constants.select()+": ":oscePostBlueprintProxy.getSpecialisation().getName()));
												tempOscePostSubViewImpl.getSpecializationLbl().setTitle(oscePostBlueprintProxy.getSpecialisation()==null?constants.select()+": ":oscePostBlueprintProxy.getSpecialisation().getName());
												tempOscePostSubViewImpl.getRoleTopicLbl().setText(getLabelString(oscePostBlueprintProxy.getRoleTopic()==null?constants.select()+": ":oscePostBlueprintProxy.getRoleTopic().getName()));
												tempOscePostSubViewImpl.getRoleTopicLbl().setTitle(oscePostBlueprintProxy.getRoleTopic()==null?constants.select()+": ":oscePostBlueprintProxy.getRoleTopic().getName());
												tempOscePostSubViewImpl.getPostNameLbl().setText(getLabelString(constants.circuitStation() + " " +oscePostBlueprintProxy.getSequenceNumber()));
												tempOscePostSubViewImpl.getPostNameLbl().setTitle(constants.circuitStation() + " " +oscePostBlueprintProxy.getSequenceNumber());
												Log.info("OsceBluerint Next Id: " + oscePostBlueprintProxy.getId());												
												oscePostSubViewImpl.add(tempOscePostSubViewImpl);												
												index++;																							
												oscePostViewImpl.getOscePostSubViewHP().add(oscePostSubViewImpl.get(index));	// ADD SUBVIEW IN POSTVIEW
												oscePostSubViewImpl.get(index).setDelegate(circuitDetailsActivity); // SET DELEGATE FOR SUBVIEW
												oscePostSubViewImpl.get(index).oscePostBlueprintProxy=oscePostBlueprintProxy;	
												oscePostViewImpl.oscePostBlueprintProxyNext=oscePostBlueprintProxy;
												maxSeq=oscePostBlueprintProxy.getSequenceNumber();
											}
											else
											{
												Log.info("Not Next Set");
											}
																												
										}										
										newPostAddHP.add(oscePostViewImpl);												
										index++;
									}										
								
							}
							else
							{
								Log.info("Osce has No Osce Post Blueprint");								
							}									
							newPostHP.add(osceCreatePostBluePrintSubViewImpl);
							addCreateListBoxHandler(); // Create Osce Post Blueprint Handler
							
							// Module 5 Bug Test Change
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							// E Module 5 Bug Test Change
						
						}
						// Module 5 bug Report Change
						else if(status.equals(OsceStatus.OSCE_GENRATED) || osceProxy.getOsceStatus() == OsceStatus.OSCE_FIXED || osceProxy.getOsceStatus() == OsceStatus.OSCE_CLOSED)
						// E Module 5 bug Report Change
						{
							
							// Module 5 bug Report Change
							circuitOsceSubViewImpl.shortBreakTextBox.setEnabled(false);
							circuitOsceSubViewImpl.longBreakTextBox.setEnabled(false);
							circuitOsceSubViewImpl.launchBreakTextBox.setEnabled(false);
							circuitOsceSubViewImpl.maxStudentTextBox.setEnabled(false);
							circuitOsceSubViewImpl.maxParcourTextBox.setEnabled(false);
							circuitOsceSubViewImpl.maxRoomsTextBox.setEnabled(false);
							circuitOsceSubViewImpl.shortBreakSimpatTextBox.setEnabled(false);
							circuitOsceSubViewImpl.middleBreakTextBox.setEnabled(false);
							
							circuitOsceSubViewImpl.saveOsce.setEnabled(false);
									
							// E Module 5 bug Report Change
							
							/*view.getScrollPanel().removeStyleDependentName("BluePrint");
							view.getScrollPanel().addStyleDependentName("Genrated");*/
							
							//Module 5 Bug Report Solution
							/*if((osceProxy.getOsceStatus() == OsceStatus.OSCE_FIXED))
							{
								view.getScrollPanel().getElement().getStyle().setBackgroundColor("#FAF0E6");
							}
							if((osceProxy.getOsceStatus() == OsceStatus.OSCE_CLOSED))
							{
								view.getScrollPanel().getElement().getStyle().setBackgroundColor("#FFCCCC");
							}*/
							//E Module 5 Bug Report Solution
							
							Iterator<OsceDayProxy> osceDayIterator=((OsceProxy)response).getOsce_days().iterator();
							Log.info("number of Osce Day :" + ((OsceProxy)response).getOsce_days().size());
							//changes for osce day Label
							int dayvalue=1;
							//changes for osce day Label
							while(osceDayIterator.hasNext()) {
								OsceDayProxy osceDayProxy=osceDayIterator.next();
								OsceGenerateSubView generateView=createGenerateView((OsceDayProxy)osceDayProxy);
								
								Iterator<OsceSequenceProxy> osceSeqProxyIterator=osceDayProxy.getOsceSequences().iterator();
								sequenceOsceSubViewImpl1=new ArrayList<SequenceOsceSubViewImpl>(osceDayProxy.getOsceSequences().size());
							
								while(osceSeqProxyIterator.hasNext()) {
									OsceSequenceProxy osceSeqProxy=osceSeqProxyIterator.next();
									
									
									//create All Parcor View
									//Iterator<CourseProxy> courseProxyIterator=osceSeqProxy.getCourses().iterator();
									//create Accordian
									AccordianPanelView accordianView=new AccordianPanelViewImpl(true);
									//ScrollPanel sp=new ScrollPanel(accordianView.asWidget());
									//sp.setWidth("720px");																	
									HorizontalPanel accordingHp=new HorizontalPanel();
									
									//Module 5 Bug Report Solution									
									//accordingHp.add(sp);			
									//E Module 5 Bug Report Solution
									
									//create each parcor
									//while(courseProxyIterator.hasNext())
									//{
										//CourseProxy courseProxy=courseProxyIterator.next();
                                                                                // Module 5 bug Report Change
										//createParcorView(accordianView,osceSeqProxy);
										// E Module 5 bug Report Change
									//}

                                                                         // Module 5 bug Report Change
									Iterator<CourseProxy> courseProxyIterator=osceSeqProxy.getCourses().iterator();
									int i=0;
									while(courseProxyIterator.hasNext()) {
										CourseProxy courseProxy=courseProxyIterator.next();
										createParcorView(accordianView,osceSeqProxy,courseProxy,i);
										i++;
									}
									// E Module 5 bug Report Change
									
									//create Sequence View
									
										//sequence start
									//	sequenceOsceSubViewImpl=new SequenceOsceSubViewImpl(osceSeqProxy);
										
									sequenceOsceSubViewImpl = new SequenceOsceSubViewImpl();
									//	sequenceOsceSubViewImpl=sequenceOsceSubViewImpl2;
									//	sequenceOsceSubViewImpl1.add(sequenceOsceSubViewImpl);
									sequenceOsceSubViewImpl.setDelegate(activity);
									sequenceOsceSubViewImpl.nameOfSequence.setText(getLabelString(((osceSeqProxy.getLabel()==null)? constants.circuitUnnamedSequence() : osceSeqProxy.getLabel())));
									sequenceOsceSubViewImpl.nameOfSequence.setTitle(((osceSeqProxy.getLabel()==null) 
											? constants.circuitUnnamedSequence() : osceSeqProxy.getLabel()));
									
									//Module 5 Bug Report Solution
									//sequenceOsceSubViewImpl.sequenceRotation.setText(osceSeqProxy.getNumberRotation()==null?"":osceSeqProxy.getNumberRotation().toString());
									sequenceOsceSubViewImpl.getSequenceRotationLable().setText(getLabelString(osceSeqProxy.getNumberRotation()==null?"":osceSeqProxy.getNumberRotation().toString()));
									sequenceOsceSubViewImpl.getSequenceRotationLable().setTitle(osceSeqProxy.getNumberRotation()==null?"":osceSeqProxy.getNumberRotation().toString());
									//E Module 5 Bug Report Solution

									// Module 5 bug Report Change
                                                                        //sequenceOsceSubViewImpl.sequenceRotation.setAcceptableValues(Arrays.asList(OsceSequences.values()));
									//sequenceOsceSubViewImpl.sequenceRotation.setValue(OsceSequences.OSCE_SEQUENCES_A);
									// E Module 5 bug Report Change
								
									sequenceOsceSubViewImpl.osceSequenceProxy=osceSeqProxy;
									sequenceOsceSubViewImpl.osceDayProxy=osceDayProxy;
									
									//spec issue sol
									sequenceOsceSubViewImpl1.add(sequenceOsceSubViewImpl);
									
									
									//	addClickHandler(sequenceOsceSubViewImpl);
									//sequenceOsceSubViewImpl.setStyleName(status.getOsceStatus(OsceStatus.OSCE_GENRATED));
									accordingHp.add(sequenceOsceSubViewImpl);
									//Module 5 Bug Report Solution
									accordingHp.add(accordianView.asWidget());
									//E Module 5 Bug Report Solution

									//Module 5 Bug Report Solution
									if(osceDayProxy.getOsceSequences().size()>=2) {
										sequenceOsceSubViewImpl.spliteSequence.setVisible(false);
									}
									//E Module 5 Bug Report Solution
										
									//sequence end
										
									//add accordian and sequence to vertical panel
									accordingHp.setSpacing(20);
									generateView.getAccordianVP().insert(accordingHp, generateView.getAccordianVP().getWidgetCount());
									
									//Module 5 Bug Report Solution	
									/*ScrollPanel mainSP=new ScrollPanel(generateView.getAccordianVP());
									mainSP.setWidth("720px");*/
									//E Module 5 Bug Report Solution
								}
								
									//create Day view
								//Osce Days[
								osceDayViewImpl = generateView.getOsceDayViewImpl();
								
								//spec issue sol
								osceDayViewImpl.setSequenceOsceSubViewImplList(sequenceOsceSubViewImpl1);
								
								osceDayViewImpl.setDelegate(activity);
								
								//Module 5 Bug Report Solution
								if(osceProxy.getOsceStatus() == OsceStatus.OSCE_CLOSED)
								{
									osceDayViewImpl.dateTextBox.setEnabled(false);
									osceDayViewImpl.startTimeTextBox.setEnabled(false);
									osceDayViewImpl.endTimeTextBox.setEnabled(false);
									osceDayViewImpl.getSaveOsceDayValueButton().setVisible(false);	
									
								}

								if(osceProxy.getOsceStatus() == OsceStatus.OSCE_GENRATED)
								{
									Log.info("Button Schedule Postpone/Earlier visible");
									osceDayViewImpl.getSchedulePostponenButton().setText(constants.schedulePostpone());
									osceDayViewImpl.getScheduleEarlierButton().setText(constants.scheduleEarlier());
									osceDayViewImpl.getSchedulePostponenButton().setVisible(true);
									osceDayViewImpl.getScheduleEarlierButton().setVisible(true);								
									
									//spec issue sol
									osceDayViewImpl.getBtnShiftLunchBreakNext().setVisible(true);
									osceDayViewImpl.getBtnShiftLunchBreakPrev().setVisible(true);
								}
								if(osceProxy.getOsceStatus() == OsceStatus.OSCE_FIXED || osceProxy.getOsceStatus() == OsceStatus.OSCE_CLOSED)
								{
									Log.info("Button Schedule Postpone/Earlier visible");									
									osceDayViewImpl.getSchedulePostponenButton().setVisible(false);
									osceDayViewImpl.getScheduleEarlierButton().setVisible(false);								
									
									osceDayViewImpl.getBtnShiftLunchBreakNext().setVisible(false);
									osceDayViewImpl.getBtnShiftLunchBreakPrev().setVisible(false);
								}
								//E Module 5 Bug Report Solution
								
								// Day Assignment 
								
								
								Log.info("Before Iterator");
								
								//bug solve start
								osceDayViewImpl.getSaveVerticlePanel().setVisible(false);
								osceDayViewImpl.getPresentsVerticlePanel().setVisible(false);
								//bug solve end
								
								/*Set<OsceDayProxy> setOsceDays = ((OsceProxy) response).getOsce_days();
								if(setOsceDays.size()==0){
									Log.info("OsceDay null for proxy : " +osceProxy.getId());
									osceDayViewImpl.setOsceDayProxy(null);
								}
								else{
									Log.info("Osce Exist for OsceProxy : " + osceProxy.getId());
									Iterator<OsceDayProxy> osceDays = setOsceDays.iterator();
									if(osceDays.hasNext()){
										osceDayViewImpl.setOsceDayProxy(osceDays.next());
									}
								}*/
								
								osceDayViewImpl.setOsceDayProxy(osceDayProxy);
								
								//bug solve start
								if(osceDayProxy.getOsceDate()==null)
								{
									osceDayViewImpl.getDateContentValueLabel().setText("  ");
								}
								else
								{
									//osceDayViewImpl.getDateContentValueLabel().setText(osceDayProxy.getOsceDate().getYear()+"-"+osceDayProxy.getOsceDate().getMonth()+"-"+osceDayProxy.getOsceDate().getDate());
									osceDayViewImpl.getDateContentValueLabel().setText(DateTimeFormat.getFormat("yyyy-MM-dd").format(osceDayProxy.getOsceDate()));
								}
								
								if(osceDayProxy.getTimeStart()==null)
								{
									osceDayViewImpl.getLunchBreakStartValueLabel().setText("  ");
								}
								else
								{
									osceDayViewImpl.getLunchBreakStartValueLabel().setText(DateTimeFormat.getFormat("HH:mm").format(osceDayProxy.getTimeStart()).substring(0,5));	
								}
								
								
								if(osceDayProxy.getLunchBreakStart()==null)
								{
									osceDayViewImpl.getLunchBreakValueLabel().setText("  ");
								}
								else
								{
									int hour=Integer.parseInt(DateTimeFormat.getFormat("HH").format(osceDayProxy.getLunchBreakStart()));
									int minute=Integer.parseInt(DateTimeFormat.getFormat("mm").format(osceDayProxy.getLunchBreakStart()));
									//int lunchtime=Integer.parseInt(osceProxy.getLunchBreak());
									int totalMinute=(hour*60)+minute+osceProxy.getLunchBreak();
									
									//Window.alert("minute--"+hour+"--"+minute+"--"+totalMinute+"--"+osceProxy.getLunchBreak());
									int newhr=(totalMinute/60);
									int newmin=(totalMinute%60);
									osceProxy.getLunchBreak();
									osceDayViewImpl.getLunchBreakValueLabel().setText(DateTimeFormat.getFormat("HH:mm").format(osceDayProxy.getLunchBreakStart()).substring(0,5)+"-"+newhr+":"+newmin);	
								}
								
								
								if(osceDayProxy.getTimeEnd()==null)
								{
									osceDayViewImpl.getLunchBreakEndTimeValueLabel().setText("  ");
								}
								else
								{
									osceDayViewImpl.getLunchBreakEndTimeValueLabel().setText(DateTimeFormat.getFormat("HH:mm").format(osceDayProxy.getTimeEnd()).substring(0,5));	
								}
								
								/*if(osceDayProxy.getLunchBreakStart()==null)
								{
									osceDayViewImpl.getLunchBreakStartValueLabel().setText("");
								}
								else
								{
									osceDayViewImpl.getLunchBreakStartValueLabel().setText(DateTimeFormat.getFormat("HH:mm").format(osceDayProxy.getLunchBreakStart()).substring(0,5));	
								}*/
								
								//bug solve end
								//Module 5 Bug Report Solution
								if(osceDayProxy.getOsceSequences().size()>1)
								{
									Log.info("setSize");
									osceDayViewImpl.getMainDayHP().setHeight("594px");									
									osceDayViewImpl.getCalculationVerticalPanel().getElement().getStyle().setMarginTop(30,Unit.PX);
									osceDayViewImpl.getScheduleHP().getElement().getStyle().setMarginTop(30,Unit.PX);
									osceDayViewImpl.getSaveVerticlePanel().getElement().getStyle().setMarginTop(50,Unit.PX);
								}
								//E Module 5 Bug Report Solution
								
								//setDayStatusStyle(style);
								 //Module 5 Bug Report Solution
								//osceDayViewImpl.getOsceDayLabel().setText("Day " + osceDayProxy.getId());
								//changes for osce day Label
								osceDayViewImpl.getOsceDayLabel().setText("Day " + dayvalue);
								//changes for osce day Label
								 //E Module 5 Bug Report Solution
								osceDayViewImpl.init();
								
								addTimeHendlers();
								//changes for osce day Label
								dayvalue++;
								//changes for osce day Label
								//Osce Days]
							}
							
							// Module 5 Bug Test Change
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							// E Module 5 Bug Test Change
						}
						//Assignment E:Module 5]
						
						
					}
					
				}
				
				@Override
				public void onFailure(ServerFailure error) {
					// TODO Auto-generated method stub
					super.onFailure(error);
					// Module 5 Bug Test Change
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					// E Module 5 Bug Test Change
				}
				
				@Override
				public void onViolation(Set<Violation> errors) {
					// TODO Auto-generated method stub
					super.onViolation(errors);
					
					// Module 5 Bug Test Change
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					// E Module 5 Bug Test Change
				}
			});
						
						
			
						}
						@Override						
						public void onFailure(ServerFailure error) {
							// TODO Auto-generated method stub
							super.onFailure(error);
							// Module 5 Bug Test Change
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							// E Module 5 Bug Test Change
							
						}
						@Override						
						public void onViolation(Set<Violation> errors) {
							// TODO Auto-generated method stub
							super.onViolation(errors);
							// Module 5 Bug Test Change
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
							// E Module 5 Bug Test Change
							
						}
						
					});
			
			// spec End==
			
			
	
	}
public static void setOsceFixedButtonStyle(CircuitOsceSubViewImpl circuitOsceSubViewImpl, OsceProxy osceProxy){

	/*if(osceProxy.getOsceStatus()==OsceStatus.OSCE_GENRATED || osceProxy.getOsceStatus()==OsceStatus.OSCE_CLOSED){
	 circuitOsceSubViewImpl.setFixBtnStyle(true);	
	}
	else{
		circuitOsceSubViewImpl.setFixBtnStyle(false);
	}*/
	
	if(osceProxy.getOsceStatus()==OsceStatus.OSCE_NEW){
		circuitOsceSubViewImpl.setClearAllBtn(false);
		// Module 5 bug Report Change
		circuitOsceSubViewImpl.setGenratedBtnStyle(false);
		circuitOsceSubViewImpl.setFixBtnStyle(false);
		circuitOsceSubViewImpl.setClosedBtnStyle(false);
	}
	else if(osceProxy.getOsceStatus()==OsceStatus.OSCE_BLUEPRINT){
		circuitOsceSubViewImpl.setClearAllBtn(false);
		// Module 5 bug Report Change
		circuitOsceSubViewImpl.setGenratedBtnStyle(true);
		circuitOsceSubViewImpl.setFixBtnStyle(false);
		circuitOsceSubViewImpl.setClosedBtnStyle(false);
	}
	else if(osceProxy.getOsceStatus()==OsceStatus.OSCE_GENRATED){
		circuitOsceSubViewImpl.setClearAllBtn(true);
		// Module 5 bug Report Change
		 circuitOsceSubViewImpl.setFixBtnStyle(true);
		 circuitOsceSubViewImpl.setGenratedBtnStyle(false);
		 circuitOsceSubViewImpl.setClosedBtnStyle(false);
	}
	else if(osceProxy.getOsceStatus()==OsceStatus.OSCE_FIXED){
		circuitOsceSubViewImpl.setFixBtnStyle(false);
		// Module 5 bug Report Change
		circuitOsceSubViewImpl.setClearAllBtn(true);
		circuitOsceSubViewImpl.setGenratedBtnStyle(false);
		 
		 circuitOsceSubViewImpl.setClosedBtnStyle(true);
	}
	else if(osceProxy.getOsceStatus()==OsceStatus.OSCE_CLOSED){
		
		circuitOsceSubViewImpl.setClearAllBtn(false);
		// Module 5 bug Report Change
		circuitOsceSubViewImpl.fixedBtn.setText(constants.reopenButtonString());
		circuitOsceSubViewImpl.fixedBtn.setIcon("folder-open");
		 circuitOsceSubViewImpl.setFixBtnStyle(true);
		 circuitOsceSubViewImpl.setGenratedBtnStyle(false);
		 circuitOsceSubViewImpl.setClosedBtnStyle(false);
	}
	
}
		
		
	//sequence start
		
	
		//sequence end
		
		
		//Assignment E:Module 5[

		@Override
		public void onDragEnd(DragEndEvent event) {
			Log.info("onDragEnd :");
			
			Log.info("Parent : "+((HorizontalPanel)((OscePostViewImpl)event.getSource()).getParent()).getWidgetCount());
			
			// Module 5 bug Report Change
			
			/*if(osceProxy.getOsceStatus()==OsceStatus.OSCE_GENRATED)
			{
				HorizontalPanel hp=((HorizontalPanel)((OscePostViewImpl)event.getSource()).getParent());
				int j=0;
				for(int i=0;i<hp.getWidgetCount();i++)
				{
					
					if(((OscePostView)hp.getWidget(i)).isAnemanis()==true || ((OscePostView)hp.getWidget(i)).getProxy().getOscePostBlueprint().getPostType()==PostType.PREPARATION)
					{
						
						updateSequence(((OscePostView)hp.getWidget(i)).getProxy(),i+j+1);
						j++;
						updateSequence(((OscePostView)hp.getWidget(i)).getNextOscePostProxy(),i+j+1);
					}
					else
						updateSequence(((OscePostView)hp.getWidget(i)).getProxy(),i+j+1);
					//((OscePostSubView)((OscePostView)hp.getWidget(i)).getOscePostSubViewHP().getWidget(0)).getPostNameLbl().setText("Post "+i+1);
				}
			}*/

			if(osceProxy.getOsceStatus()==OsceStatus.OSCE_BLUEPRINT)
				// E Module 5 bug Report Change
			{
				Log.info("Osce BluePrint On Drag End");
				Log.info("Event Source :"+((OscePostView)event.getSource()).getOscePostBlueprintProxy().getSequenceNumber());
				HorizontalPanel hp=((HorizontalPanel)((OscePostViewImpl)event.getSource()).getParent());
				
                                updateOscePostsSequence(hp);
                               /* int j=0;
				for(int i=0;i<hp.getWidgetCount();i++)
				{
					if(hp.getWidget(i) instanceof OsceCreatePostBluePrintSubViewImpl)
						continue;
					
					
					else if(((OscePostView)hp.getWidget(i)).getOscePostBlueprintProxy().getPostType()==PostType.ANAMNESIS_THERAPY ||  ((OscePostView)hp.getWidget(i)).getOscePostBlueprintProxy().getPostType()==PostType.PREPARATION)
					{
						
						updateBluePrintSequence(((OscePostView)hp.getWidget(i)).getOscePostBlueprintProxy(),i+j+1);
						j++;
						updateBluePrintSequence(((OscePostView)hp.getWidget(i)).getOscePostBlueprintProxyNext(),i+j+1);
					}
					else
						updateBluePrintSequence(((OscePostView)hp.getWidget(i)).getOscePostBlueprintProxy(),i+j+1);
					//((OscePostSubView)((OscePostView)hp.getWidget(i)).getOscePostSubViewHP().getWidget(0)).getPostNameLbl().setText("Post "+i+1);
				}*/
			}
			Log.info("onDragEnd");
			// TODO Auto-generated method stub
			/* hp=(HorizontalPanel)((OscePostViewImpl)event.getSource()).getParent();
			ScrollPanel sp=(ScrollPanel)hp.getParent();
			AbsolutePanel ap=(AbsolutePanel)sp.getParent();
			VerticalPanel vp=(VerticalPanel) ap.getParent();
			ContentViewImpl cv=(ContentViewImpl)vp.getParent();
			cv.isdragging=false;*/
			
		}
		
		// Module 5 bug Report Change
		public void updateSequence(OscePostProxy proxy,int seqNum,OscePostSubView view)
		// E Module 5 bug Report Change
		{
			OscePostRequest oscePostRequest=requests.oscePostRequest();
			proxy=oscePostRequest.edit(proxy);
			proxy.setSequenceNumber(seqNum);
			oscePostRequest.persist().using(proxy).fire();
				
			// Module 5 bug Report Change
			Log.info("Set Sequence... ");
			view.getPostNameLbl().setText("Post " + seqNum);
			// E Module 5 bug Report Change
		}
		
		public void updateBluePrintSequence(OscePostBlueprintProxy proxy,int seqNum)
		{
			OscePostBlueprintRequest oscePostRequest=requests.oscePostBlueprintRequest();
			proxy=oscePostRequest.edit(proxy);
			proxy.setSequenceNumber(seqNum);
			oscePostRequest.persist().using(proxy).fire();
			
		
			
		}
		
		// Module 5 bug Report Change
		/*public void updateBluePrintSequences()
		{
			Iterator<OscePostBlueprintProxy> bluePrintIterator=osceProxy.getOscePostBlueprints().iterator();
			int i=0;
			while(bluePrintIterator.hasNext())
			{
				OscePostBlueprintProxy proxy=bluePrintIterator.next();
				updateBluePrintSequence(proxy, ++i);
			}
		}*/
		// E Module 5 bug Report Change
		
		
		@Override
		public void onDragStart(DragStartEvent event) {
			Log.info("onDragStart");
			// TODO Auto-generated method stub
			/*HorizontalPanel hp=(HorizontalPanel)((OscePostViewImpl)event.getSource()).getParent();
			ScrollPanel sp=(ScrollPanel)hp.getParent();
			AbsolutePanel ap=(AbsolutePanel)sp.getParent();
			VerticalPanel vp=(VerticalPanel) ap.getParent();
			ContentViewImpl cv=(ContentViewImpl)vp.getParent();
			cv.isdragging=true;
			Log.info("dragging" + cv.isdragging);*/
		}

		@Override
		public void onPreviewDragEnd(DragEndEvent event)
				throws VetoDragException {
			Log.info("onPreviewDragEnd");
			
		}

		@Override
		public void onPreviewDragStart(DragStartEvent event)
				throws VetoDragException {
			// TODO Auto-generated method stub
			
		}
		
				
		public void deleteCourse(final HeaderView view)
		{
			Log.info("deleteCourse");
			/*CourseProxy proxy=view.getProxy();
			
			requests.courseRequest().remove().using(proxy).fire(new OSCEReceiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					Log.info("deleteCourse Success");
					((HeaderViewImpl)view).removeFromParent();
					
				}
			});
			*/
		}
		
		// Change in ParcourView
		public void colorChanged(final HeaderView view,final String color)
		// E Change in ParcourView
		{
			// Change in ParcourView
			Log.info("Color Change: " + color);
						// E Change in ParcourView			
			CourseProxy proxy=view.getProxy();
			Log.info("Proxy: " + proxy.getId());
			
			CourseRequest courseRequest=requests.courseRequest();
			proxy=courseRequest.edit(proxy);
			// Change in ParcourView
			proxy.setColor(color);
			// E Change in ParcourView
			
			courseRequest.persist().using(proxy).fire(new OSCEReceiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					Log.info("colorChanged success");
					// Change in ParcourView
					//view.changeParcourHeaderColor(color);
					view.changeHeaderColor(ColorPicker.valueOf(color));
					Log.info("Change Detail Panel Color: accordion-title-selected"+color); 
					//((ContentViewImpl)(view.getContentView())).addStyleDependentName("selected"+color);
					//((ContentViewImpl)(view.getContentView())).addStyleName("accordion-title-selected"+color);	
					changeContentViewColor(((view.getContentView())),color);
					/*((ContentViewImpl)(view.getContentView())).getContentPanel().removeStyleName("accordion-title-selected"+color);
					((ContentViewImpl)(view.getContentView())).getContentPanel().addStyleName("accordion-title-selected"+color);*/
					// E Change in ParcourView
					
				}
			});
		}
		
		public void changeContentViewColor(ContentView contentViewImpl,String color)
		{
			Log.info("Call changeContentViewColor: with Color" + color);
			
			ContentViewImpl tempView = (ContentViewImpl) contentViewImpl;
			
			ColorPicker cp[]=ColorPicker.values();
			for(int i=0;i<ColorPicker.values().length;i++)
			{
				if(!cp[i].equals(color) && color!=null)
				{
					Log.info("Remove Color: " + cp[i]);					
					tempView.removeStyleName("accordion-title-selected" +cp[i].toString());					
				}
			}
			
			if(color==null)	
			{
				Log.info("Color Null");
				contentViewImpl.getContentPanel().addStyleDependentName("selectedwhite");
			}				
			else
			{
				String c="accordion-title-selected"+color.toString();
				
				Log.info("~~~~Color Not Null : " + color +" --- : "+c);
				
				tempView.addStyleName(c);
			
			}
				
		}
		
		
		
		public void deleteOscePost(final OscePostView view)
		{
			
			Log.info("deleteOscePost");
		/*	OscePostProxy proxy=view.getProxy();
			
			requests.oscePostRequest().remove().using(proxy).fire(new OSCEReceiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					((OscePostViewImpl)view).removeFromParent();
					
				}
			});
			*/
			Log.info(osceProxy.getOsceStatus().toString());
			Log.info(OsceStatus.OSCE_BLUEPRINT.toString());
			Log.info(new Integer(OsceStatus.OSCE_BLUEPRINT.ordinal()).toString());
			if((osceProxy.getOsceStatus() == OsceStatus.OSCE_BLUEPRINT))
				deletePostClicked((OscePostViewImpl)view);
				setOsceFixedButtonStyle(circuitOsceSubViewImpl, osceProxy);
		}
		public void saveStandardizedRole(final ListBoxPopupView view)
		{
			Log.info("saveStandardizedRole ");
			OscePostProxy oscePostProxy=(OscePostProxy)view.getProxy();
		//	final StandardizedRoleProxy standardizedRoleProxy=(StandardizedRoleProxy)view.getListBox().getValue();
			
			//Issue # 122 : Replace pull down with autocomplete.
			//final StandardizedRoleProxy standardizedRoleProxy=(StandardizedRoleProxy)view.getListBox().getValue();
			final StandardizedRoleProxy standardizedRoleProxy=(StandardizedRoleProxy)view.getNewListBox().getSelected();
			//Issue # 122 : Replace pull down with autocomplete.
			OscePostRequest oscePostRequest=requests.oscePostRequest();
			oscePostProxy=oscePostRequest.edit(oscePostProxy);
			oscePostProxy.setStandardizedRole(standardizedRoleProxy);
			oscePostRequest.persist().using(oscePostProxy).fire(new OSCEReceiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					Log.info("Success saveStandardizedRole ");
					
					// Module 5 Bug Test Change
					view.getOscePostSubView().getStandardizedRoleLbl().setText(getLabelString(standardizedRoleProxy.getLongName()));
					view.getOscePostSubView().getStandardizedRoleLbl().setTitle(standardizedRoleProxy.getLongName());
					// E Module 5 Bug Test Change
					
					((ListBoxPopupViewImpl)view).hide();
				}
			});
		}
		
		public void findStandardizedRoles(final OscePostSubView view)
		{
			requests.roleTopicRequest().findRoleTopic(((OscePostSubViewImpl)view).getOscePostProxy().getOscePostBlueprint().getRoleTopic().getId()).with("standardizedRoles").fire(new OSCEReceiver<RoleTopicProxy>() {

				@Override
				public void onSuccess(RoleTopicProxy response) {
					
					Log.info("findStandardizedRoles"+response.getStandardizedRoles().get(0).getLongName());
					ArrayList list=new ArrayList();
					//list.add( (EntityProxy)response.getStandardizedRoles().get(0));
					list.addAll(response.getStandardizedRoles());
					
					((OscePostSubViewImpl)view).createOptionPopup();
					((OscePostSubViewImpl)view).popupView.setDelegate(activity);
					((OscePostSubViewImpl)view).popupView.setOscePostSubView(view);
					((OscePostSubViewImpl)view).popupView.setProxy(view.getOscePostProxy());
					((OscePostSubViewImpl)view).showPopUpView();
	
					//Issue # 122 : Replace pull down with autocomplete.
					DefaultSuggestOracle<Object> suggestOracle1 = ((DefaultSuggestOracle<Object>) (((OscePostSubViewImpl)view).popupView.getNewListBox().getSuggestOracle()));
					suggestOracle1.setPossiblilities(list);
					((OscePostSubViewImpl)view).popupView.getNewListBox().setSuggestOracle(suggestOracle1);
					
					//((OscePostSubViewImpl)view).popupView.getNewListBox().setRenderer(new StandardizedRoleProxyRenderer());
					((OscePostSubViewImpl)view).popupView.getNewListBox().setRenderer(new Renderer<Object>() {

						@Override
						public String render(Object object) {
							// TODO Auto-generated method stub
							//return object.getShortName();
							return ((StandardizedRoleProxy)object).getLongName();
						}

						@Override
						public void render(Object object,
								Appendable appendable) throws IOException {
							// TODO Auto-generated method stub
							
						}
					});


					//((OscePostSubViewImpl)view).popupView.getListBox().setAcceptableValues(list);
					
					//Issue # 122 : Replace pull down with autocomplete.
					
				}
			});
			
		}
		//create parcor view
		
		// Module 5 bug Report Change
		public void createParcorView(AccordianPanelView accordianView,EntityProxy proxy,CourseProxy courseProxy,int i)
		// E Module 5 bug Report Change
		{
			if(proxy instanceof OsceSequenceProxy)
			{
				
				OsceSequenceProxy osceSequenceProxy=(OsceSequenceProxy)proxy;
				ContentViewImpl contentView=new ContentViewImpl();
				
				// Change in ParcourView					
					Log.info("Set Initial Color: accordion-title-selected"+courseProxy.getColor());
					contentView.addStyleName("accordion-title-selected"+courseProxy.getColor());
					//bug report solve start
					contentView.setHeight("248px");
                                         //contentView.setHeight("268px");
					//bug report solve end
					contentView.getContentPanel().getElement().getStyle().setWidth(100, Unit.PCT);
					contentView.getScrollPanel().getElement().getStyle().setWidth(100, Unit.PCT);
				// E Change in ParcourView
				
				// Module 5 bug Report Change
					//contentView.getDragController().addDragHandler(this);
				// E Module 5 bug Report Change
				
				//create All Posts
				Iterator<OscePostProxy> oscePostIterator=osceSequenceProxy.getOscePosts().iterator();
				
				
				
				while(oscePostIterator.hasNext())
				{
				
					OscePostProxy oscePostProxy=oscePostIterator.next();
				
				//createPost
					if(oscePostProxy.getOscePostBlueprint().getPostType().equals(PostType.ANAMNESIS_THERAPY) || oscePostProxy.getOscePostBlueprint().getPostType().equals(PostType.PREPARATION))
					{
						//createAnamnesisTherapyPost(contentView,oscePostProxy,oscePostIterator.next());
                        createAnamnesisTherapyPost(contentView,oscePostProxy,oscePostIterator.next(),courseProxy);
					}
					else
					if(osceSequenceProxy.getOscePosts().size()==1)
						// Module 5 and TTG Bug Changes
					{					
							createUndraggablePost(contentView,oscePostProxy,courseProxy);
					}
					// E Module 5 and TTG Bug Changes
					else
					// Module 5 and TTG Bug Changes
					{
							createOscePost(contentView,oscePostProxy,courseProxy);
					}
					// E Module 5 and TTG Bug Changes
				
				}
				//create Header View
				HeaderView headerView=new HeaderViewImpl();
				
				// Module 5 bug Report Change
				//((HeaderViewImpl)headerView).setHeight("235px");
				headerView.getHeaderPanel().setHeight("248px");
                                 headerView.getColorPicker().setIcon("colorPickerIcon");
				// Module 5 Bug Report Solution
				//headerView.getDeleteBtn().setVisible(false);
				//E Module 5 Bug Report Solution
				// E Module 5 bug Report Change
				
				//Module 5 Bug Report Solution
				if(osceProxy.getOsceStatus() == OsceStatus.OSCE_CLOSED)
				{
					headerView.getColorPicker().setVisible(false);
				}
				if(osceProxy.getOsceStatus()==OsceStatus.OSCE_FIXED || osceProxy.getOsceStatus()==OsceStatus.OSCE_GENRATED)
				{
					headerView.getHeaderSimplePanel().setHeight("230px");
				}
				//E Module 5 Bug Report Solution
				
				headerView.setDelegate(this);
				headerView.setProxy(courseProxy);
			
				// Change in ParcourView
				headerView.setContentView(contentView);
				// E Change in ParcourView			
				
				accordianView.setOsceSequenceProxy(osceSequenceProxy);
				accordianView.setParcourDelegate(this);
				
				accordianView.add(headerView.asWidget(), contentView);
				
				// Module 5 bug Report Change
				if((osceSequenceProxy.getCourses().size() -1) == i)
				((AccordianPanelViewImpl)accordianView).expand((HeaderViewImpl)headerView, ((AccordianPanelViewImpl)accordianView).sp);
				// E Module 5 bug Report Change
				//CourseProxy courseProxy=osceSequenceProxy.getCourses().iterator().next();
				
				
				if(courseProxy.getColor()!=null)
				{
					//headerView.getColorPicker().setValue(ColorPicker.valueOf(courseProxy.getColor()));
					headerView.changeHeaderColor(ColorPicker.valueOf(courseProxy.getColor()));
					
				}
				
			}
			
			
		}
		
		//create Post Inside Content
		// Module 5 and TTG Bug Changes
		public void createAnamnesisTherapyPost(ContentViewImpl contentView,OscePostProxy oscePostProxy,OscePostProxy oscePostProxyNext,CourseProxy courseProxy)
		// E Module 5 and TTG Bug Changes
		{
			OscePostView oscePostView=new OscePostViewImpl();
			oscePostView.getPostTypeLbl().setText(enumConstants.getString(oscePostProxy.getOscePostBlueprint().getPostType().toString()));
			oscePostView.setDelegate(this);
			oscePostView.setProxy(oscePostProxy);
			oscePostView.setAnemanis(true);
			oscePostView.setNextOscePostProxy(oscePostProxyNext);
			//first Post
			final OscePostSubView oscePostSubView=new OscePostSubViewImpl();
			
			// Change in ParcourView
			//spec issue sol
			if(oscePostProxy.getStandardizedRole() != null)
			{
				// Module 5 Bug Test Change
				oscePostSubView.getStandardizedRoleLbl().setText(getLabelString(oscePostProxy.getStandardizedRole().getLongName()));
				oscePostSubView.getStandardizedRoleLbl().setTitle(oscePostProxy.getStandardizedRole().getLongName());				
				// E Module 5 Bug Test Change
				
			}
			else
		 	        oscePostSubView.getStandardizedRoleLbl().setText(constants.select());

			oscePostSubView.getRoomLbl().setText(constants.select());
			// E Change in ParcourView
			
				oscePostSubView.getPostNameLbl().setText(getLabelString(constants.circuitStation() + " " +oscePostProxy.getSequenceNumber()));
				oscePostSubView.getPostNameLbl().setTitle(constants.circuitStation() + " " +oscePostProxy.getSequenceNumber());
				oscePostSubView.getSpecializationLbl().setText(getLabelString(oscePostProxy.getOscePostBlueprint().getSpecialisation().getName()));				
				oscePostSubView.getSpecializationLbl().setTitle(oscePostProxy.getOscePostBlueprint().getSpecialisation().getName());
				oscePostSubView.getRoleTopicLbl().setText(getLabelString(oscePostProxy.getOscePostBlueprint().getRoleTopic().getName()));
				oscePostSubView.getRoleTopicLbl().setTitle(oscePostProxy.getOscePostBlueprint().getRoleTopic().getName());
			
			
			
			
			requests.oscePostRoomRequestNonRoo().findOscePostRoomByOscePostAndCourse(courseProxy, oscePostProxy).with("room").fire(new OSCEReceiver<OscePostRoomProxy>() 
			{

				@Override
				public void onSuccess(OscePostRoomProxy response) 
				{
					if (response != null)
					{
						Log.info("OscePostProxy: " + response.getId());
						if(response.getRoom()!=null)
						{
							oscePostSubView.getRoomLbl().setText(getLabelString(util.getEmptyIfNull(response.getRoom().getRoomNumber())));
							oscePostSubView.getRoomLbl().setTitle(util.getEmptyIfNull(response.getRoom().getRoomNumber()));
						}
					}
					
				}
			});
			
			if(oscePostProxy.getStandardizedRole()!=null)
			{
								
				// Module 5 Bug Test Change
				oscePostSubView.getStandardizedRoleLbl().setText(getLabelString(oscePostProxy.getStandardizedRole().getLongName()));
				oscePostSubView.getStandardizedRoleLbl().setTitle(oscePostProxy.getStandardizedRole().getLongName());
				// E Module 5 Bug Test Change
			}
			
			oscePostSubView.setDelegate(this);
			oscePostSubView.setOscePostProxy(oscePostProxy);
			// Module 5 and TTG Bug Changes
			oscePostSubView.setCourseProxy(courseProxy);
			// E Module 5 and TTG Bug Changes
			
			oscePostSubView.enableDisableforGeneratedStatus();
			
			// Change in ParcourView
			if(osceProxy.getOsceStatus()==OsceStatus.OSCE_CLOSED)
			{
				Log.info("Osce is in Closed Status, Role Edit Disabled.");			
				oscePostSubView.getStandardizedRoleEdit().setVisible(false);
				oscePostSubView.getRoomedit().setVisible(false);
			}
			// E Change in ParcourView
					
			
			oscePostView.getOscePostSubViewHP().insert(oscePostSubView, oscePostView.getOscePostSubViewHP().getWidgetCount());
			
			//Second Post
			final OscePostSubView oscePostSubViewNext=new OscePostSubViewImpl();
			
			// Change in ParcourView
			//spec issue sol
			if(oscePostProxy.getStandardizedRole() != null)
			{
				// Module 5 Bug Test Change
				oscePostSubViewNext.getStandardizedRoleLbl().setText(getLabelString(oscePostProxyNext.getStandardizedRole().getLongName()));
				oscePostSubViewNext.getStandardizedRoleLbl().setTitle(oscePostProxyNext.getStandardizedRole().getLongName());
				// E Module 5 Bug Test Change
			}
			else
		         	oscePostSubViewNext.getStandardizedRoleLbl().setText(constants.select());
			oscePostSubViewNext.getRoomLbl().setText(constants.select());
			// E Change in ParcourView
			
			oscePostSubViewNext.getPostNameLbl().setText(getLabelString(constants.circuitStation() + " " +oscePostProxyNext.getSequenceNumber()));
			oscePostSubViewNext.getPostNameLbl().setTitle(constants.circuitStation() + " " +oscePostProxyNext.getSequenceNumber());
		
			oscePostSubViewNext.getSpecializationLbl().setText(getLabelString(oscePostProxyNext.getOscePostBlueprint().getSpecialisation().getName()));			
			oscePostSubViewNext.getSpecializationLbl().setTitle(oscePostProxyNext.getOscePostBlueprint().getSpecialisation().getName());
			oscePostSubViewNext.getRoleTopicLbl().setText(getLabelString(oscePostProxyNext.getOscePostBlueprint().getRoleTopic().getName()));		
			oscePostSubViewNext.getRoleTopicLbl().setTitle(oscePostProxyNext.getOscePostBlueprint().getRoleTopic().getName());
			
			requests.oscePostRoomRequestNonRoo().findOscePostRoomByOscePostAndCourse(courseProxy, oscePostProxyNext).with("room").fire(new OSCEReceiver<OscePostRoomProxy>() 
					{
						@Override
						public void onSuccess(OscePostRoomProxy response) 
						{
							if(response!=null)
							{
								if(response.getRoom()!=null)
								{
									oscePostSubViewNext.getRoomLbl().setText(getLabelString(util.getEmptyIfNull(response.getRoom().getRoomNumber())));
									oscePostSubViewNext.getRoomLbl().setTitle(util.getEmptyIfNull(response.getRoom().getRoomNumber()));
								}	
							}
							
						}
					});

			
			//if(oscePostProxyNext.getStandardizedRole()!=null)
			//	oscePostSubView.getStandardizedRoleLbl().setText(oscePostProxyNext.getStandardizedRole().getLongName());
			
			oscePostSubViewNext.setDelegate(this);
			oscePostSubViewNext.setOscePostProxy(oscePostProxyNext);
				// Module 5 and TTG Bug Changes
			oscePostSubViewNext.setCourseProxy(courseProxy);
		// E Module 5 and TTG Bug Changes
			
			oscePostSubViewNext.enableDisableforGeneratedStatus();
			
			// Change in ParcourView
			if(osceProxy.getOsceStatus()==OsceStatus.OSCE_CLOSED)
			{
				Log.info("Osce is in Closed Status, Role Edit Disabled.");			
				oscePostSubViewNext.getStandardizedRoleEdit().setVisible(false);
				oscePostSubViewNext.getRoomedit().setVisible(false);
			}
			// E Change in ParcourView
								
			
			oscePostView.getOscePostSubViewHP().insert(oscePostSubViewNext, oscePostView.getOscePostSubViewHP().getWidgetCount());
			
			//make content draggable
			

			//contentView.getDragController().makeDraggable(oscePostView.asWidget(),oscePostView.getPostTypeLbl().asWidget());
			// E Module 5 bug Report Change
			// Module 5 bug Report Change			
			oscePostView.getDeletePostButton().setVisible(false);
			oscePostView.getPostTypeLbl().setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);			
			// E Module 5 bug Report Change
			contentView.getPostHP().insert(oscePostView, contentView.getPostHP().getWidgetCount());

		}
		
		// Module 5 Bug Test Change
		public String getLabelString(String text)
		{
			Log.info("Text String Size: " + text.length());
			if(text.length()>11)
			{
				Log.info("Text Length is greater than 8 Before Text is: " + text);
				text=text.substring(0, 11).concat("..");	
				Log.info("Text Length is greater than 8 So Text is: " + text);
			}			
			return text;
			
		}
		// E Module 5 Bug Test Change
		
		// Module 5 and TTG Bug Changes
		public void createUndraggablePost(ContentViewImpl contentView,OscePostProxy oscePostProxy,CourseProxy courseProxy)
		// E Module 5 and TTG Bug Changes
		{
			OscePostView oscePostView=new OscePostViewImpl();
			oscePostView.getPostTypeLbl().setText(enumConstants.getString(oscePostProxy.getOscePostBlueprint().getPostType().toString()));
			oscePostView.setDelegate(this);
			oscePostView.setProxy(oscePostProxy);
			final OscePostSubView oscePostSubView=new OscePostSubViewImpl();
			
			// Change in ParcourView
			oscePostSubView.getStandardizedRoleLbl().setText(constants.select());
			oscePostSubView.getRoomLbl().setText(constants.select());
			// E Change in ParcourView
			
			
			oscePostSubView.getPostNameLbl().setText(getLabelString(constants.circuitStation() + " " +oscePostProxy.getSequenceNumber()));
			oscePostSubView.getPostNameLbl().setTitle(constants.circuitStation() + " " +oscePostProxy.getSequenceNumber());
			// Module 5 bug Report Change
			
			oscePostSubView.getSpecializationLbl().setText(getLabelString(util.getEmptyIfNull(oscePostProxy.getOscePostBlueprint().getSpecialisation().getName())));			
			oscePostSubView.getSpecializationLbl().setTitle(util.getEmptyIfNull(oscePostProxy.getOscePostBlueprint().getSpecialisation().getName()));
			oscePostSubView.getRoleTopicLbl().setText(getLabelString(oscePostProxy.getOscePostBlueprint().getRoleTopic().getName()));
			oscePostSubView.getRoleTopicLbl().setTitle(oscePostProxy.getOscePostBlueprint().getRoleTopic().getName());
			
			requests.oscePostRoomRequestNonRoo().findOscePostRoomByOscePostAndCourse(courseProxy, oscePostProxy).with("room").fire(new OSCEReceiver<OscePostRoomProxy>() 
					{

						@Override
						public void onSuccess(OscePostRoomProxy response) 
						{
							if (response != null)
							{
								Log.info("OscePostProxy: " + response.getId());
								if(response.getRoom()!=null)
								{
									oscePostSubView.getRoomLbl().setText(util.getEmptyIfNull(response.getRoom().getRoomNumber()));	
									oscePostSubView.getRoomLbl().setTitle(getLabelString(util.getEmptyIfNull(response.getRoom().getRoomNumber())));
								}
							}							
						}
					});

			
			// E Module 5 bug Report Change
			if(oscePostProxy.getStandardizedRole()!=null)
			// Module 5 bug Report Change
			{
				oscePostSubView.getStandardizedRoleLbl().setText(getLabelString(util.getEmptyIfNull(oscePostProxy.getStandardizedRole().getLongName())));
				oscePostSubView.getStandardizedRoleLbl().setTitle(util.getEmptyIfNull(oscePostProxy.getStandardizedRole().getLongName()));
			}
			// E Module 5 bug Report Change
			
			oscePostSubView.setDelegate(this);
			oscePostSubView.setOscePostProxy(oscePostProxy);
			// Module 5 and TTG Bug Changes
			oscePostSubView.setCourseProxy(courseProxy);
			// E Module 5 and TTG Bug Changes
			
			oscePostSubView.enableDisableforGeneratedStatus();
			
			//Module 5 Bug Report Solution
			if(osceProxy.getOsceStatus()==OsceStatus.OSCE_CLOSED)
			{
				Log.info("Osce is in Closed Status, Role Edit Disabled.");			
				oscePostSubView.getStandardizedRoleEdit().setVisible(false);

				// Change in ParcourView
				oscePostSubView.getRoomedit().setVisible(false);
				// E Change in ParcourView
			}
			//E Module 5 Bug Report Solution
			
			oscePostView.getOscePostSubViewHP().insert(oscePostSubView, oscePostView.getOscePostSubViewHP().getWidgetCount());
			//contentView.getDragController().makeDraggable(oscePostView.asWidget(),oscePostView.getPostTypeLbl().asWidget());
			
			// Module 5 bug Report Change			
			oscePostView.getDeletePostButton().setVisible(false);
			oscePostView.getPostTypeLbl().setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			// E Module 5 bug Report Change
			contentView.getPostHP().insert(oscePostView, contentView.getPostHP().getWidgetCount());
		}
		// Module 5 and TTG Bug Changes
		public void createOscePost(ContentViewImpl contentView,OscePostProxy oscePostProxy,CourseProxy courseProxy)
		// E Module 5 and TTG Bug Changes
		{
			OscePostView oscePostView=new OscePostViewImpl();
			oscePostView.getPostTypeLbl().setText(enumConstants.getString(oscePostProxy.getOscePostBlueprint().getPostType().toString()));
			oscePostView.setDelegate(this);
			oscePostView.setProxy(oscePostProxy);
			final OscePostSubView oscePostSubView=new OscePostSubViewImpl();
			
			// Change in ParcourView
			oscePostSubView.getStandardizedRoleLbl().setText(constants.select());
			oscePostSubView.getRoomLbl().setText(constants.select());
			// E Change in ParcourView
			
			oscePostSubView.getPostNameLbl().setText(getLabelString(constants.circuitStation() + " " + oscePostProxy.getSequenceNumber()));												
			oscePostSubView.getPostNameLbl().setTitle(constants.circuitStation() + " " + oscePostProxy.getSequenceNumber());
			oscePostSubView.getSpecializationLbl().setText(getLabelString(oscePostProxy.getOscePostBlueprint().getSpecialisation().getName()));
			oscePostSubView.getSpecializationLbl().setTitle(oscePostProxy.getOscePostBlueprint().getSpecialisation().getName());
			oscePostSubView.getRoleTopicLbl().setText(getLabelString(oscePostProxy.getOscePostBlueprint().getRoleTopic().getName()));			
			oscePostSubView.getRoleTopicLbl().setTitle(oscePostProxy.getOscePostBlueprint().getRoleTopic().getName());
			
			requests.oscePostRoomRequestNonRoo().findOscePostRoomByOscePostAndCourse(courseProxy, oscePostProxy).with("room").fire(new OSCEReceiver<OscePostRoomProxy>() 
			{
				@Override
				public void onSuccess(OscePostRoomProxy response) 
				{
					if(response!=null)
					{
						//Log.info("OscePostProxy: " + response.getId());
						
						/*requests.oscePostRoomRequest().findOscePostRoom(response.getId()).with("room").fire(new OSCEReceiver<OscePostRoomProxy>() 
						{
							@Override
							public void onSuccess(OscePostRoomProxy response) 
							{*/
								if(response.getRoom()!=null)
								{
									oscePostSubView.getRoomLbl().setText(getLabelString(util.getEmptyIfNull(response.getRoom().getRoomNumber())));
									oscePostSubView.getRoomLbl().setTitle(util.getEmptyIfNull(response.getRoom().getRoomNumber()));
								}							
							/*}
						});*/
	
					}
										
				}
			});
			
			if(oscePostProxy.getStandardizedRole()!=null)
			{
				// Module 5 Bug Test Change
				oscePostSubView.getStandardizedRoleLbl().setText(getLabelString(oscePostProxy.getStandardizedRole().getLongName()));
				oscePostSubView.getStandardizedRoleLbl().setTitle(oscePostProxy.getStandardizedRole().getLongName());
				// E Module 5 Bug Test Change
			}
			
			oscePostSubView.setDelegate(this);
			oscePostSubView.setOscePostProxy(oscePostProxy);
			// Module 5 and TTG Bug Changes
			oscePostSubView.setCourseProxy(courseProxy);
			// E Module 5 and TTG Bug Changes			
			
			oscePostSubView.enableDisableforGeneratedStatus();
			
			//Module 5 Bug Report Solution
			if(osceProxy.getOsceStatus()==OsceStatus.OSCE_CLOSED)
			{			
				oscePostSubView.getStandardizedRoleEdit().setVisible(false);
				// Change in ParcourView
				oscePostSubView.getRoomedit().setVisible(false);
				// E Change in ParcourView
			}
			//E Module 5 Bug Report Solution
			oscePostView.getOscePostSubViewHP().insert(oscePostSubView, oscePostView.getOscePostSubViewHP().getWidgetCount());
			
			// Module 5 bug Report Change
			//contentView.getDragController().makeDraggable(oscePostView.asWidget(),oscePostView.getPostTypeLbl().asWidget());
			// E Module 5 bug Report Change
			// Module 5 bug Report Change			
			oscePostView.getDeletePostButton().setVisible(false);
			oscePostView.getPostTypeLbl().setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			// E Module 5 bug Report Change
			contentView.getPostHP().insert(oscePostView, contentView.getPostHP().getWidgetCount());
		}
		
		public OsceGenerateSubView createGenerateView(OsceDayProxy osceDayProxy)
		{
			
			OsceGenerateSubView generateView=new OsceGenerateSubViewImpl();
			view.getGenerateVP().insert(generateView, view.getGenerateVP().getWidgetCount());
			generateView.setDelegate(this);
			return generateView;
					
		}
		
		//Assignment E:Module 5]
		@Override
		public void goTo(Place place) {
			placeController.goTo(place);		
		}

		@Override
		public void saveOsceData(OsceProxy osceProxy) {
			Log.info("Call saveOsceData");
			
			CircuitOsceSubViewImpl circuitOsceSubViewImp = view.getcircuitOsceSubViewImpl();
			OsceRequest osceReq = requests.osceRequest();
			osceProxy = osceReq.edit(osceProxy);
			
			osceProxy.setShortBreak(circuitOsceSubViewImp.shortBreakTextBox.getValue());
			osceProxy.setLongBreak(circuitOsceSubViewImp.longBreakTextBox.getValue());
			osceProxy.setLunchBreak(circuitOsceSubViewImp.launchBreakTextBox.getValue());
			osceProxy.setMaxNumberStudents(circuitOsceSubViewImp.maxStudentTextBox.getValue());
			osceProxy.setNumberCourses(circuitOsceSubViewImp.maxParcourTextBox.getValue());
			osceProxy.setNumberRooms(circuitOsceSubViewImp.maxRoomsTextBox.getValue());
			// Module 5 bug Report Change
			osceProxy.setMiddleBreak(circuitOsceSubViewImp.middleBreakTextBox.getValue());
			osceProxy.setShortBreakSimpatChange(circuitOsceSubViewImp.shortBreakSimpatTextBox.getValue());
			// E Module 5 bug Report Change
			// Highlight onViolation
			Log.info("Map Size: " + circuitOsceSubViewImp.osceMap.size());
			
			
			/*
			MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
			dialog.showConfirmationDialog(constants.warningHeaderValue());
			return;*/
			try
			{
				OsceProxyVerifier.verifyOsce(osceProxy);	
			}
			catch(Exception e)
			{
				MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
				dialog.showConfirmationDialog(constants.pleaseEnterWarning() + e.getMessage());
				//Log.info(e.toString());
				return;
			}
			
			
			osceReq.persist().using(osceProxy).fire(new OSCEReceiver<Void>(circuitOsceSubViewImp.osceMap) {
			// E Highlight onViolation
				@Override
				public void onSuccess(Void response) {
					Log.info("Osce Value Updated");
					successValue=true;
					final MessageConfirmationDialogBox valueUpdateDialogBox=new MessageConfirmationDialogBox(constants.success());
					valueUpdateDialogBox.showConfirmationDialog(constants.updateOsce());
					valueUpdateDialogBox.getYesBtn().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							valueUpdateDialogBox.hide();
							
						}
					});
					
					valueUpdateDialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							valueUpdateDialogBox.hide();
							
						}
					});
					//Window.alert("Osce Data Updated sucessfully");
					
				}
				
			
			});
			
			if(successValue==true)
			{
				this.osceProxy=osceProxy;
			}
		}
		@Override
		public void clearAll(OsceProxy proxy) {
			// Highlight onViolation
			CircuitOsceSubViewImpl circuitOsceSubViewImp = view.getcircuitOsceSubViewImpl();
			// E Highlight onViolation
			
			OsceRequest osceReq = requests.osceRequest();
			proxy = osceReq.edit(proxy);			
			proxy.setOsceStatus(OsceStatus.OSCE_BLUEPRINT);		
			circuitOsceSubViewImp.setClearAllBtn(false);
			// Highlight onViolation
			osceReq.persist().using(proxy).fire(new OSCEReceiver<Void>(circuitOsceSubViewImp.osceMap) {
				// E Highlight onViolation

				@Override
				public void onSuccess(Void response) {
					Log.info("Osce Value Updated");
					setOsceFixedButtonStyle(circuitOsceSubViewImpl, osceProxy);
					// Module 5 bug Report Change
						goTo(new CircuitDetailsPlace(osceProxy.stableId(),Operation.DETAILS));
					// E Module 5 bug Report Change
					//Window.alert("Osce Data Updated sucessfully");
					
				}
			});

			
		}
		
		// 5C: SPEC START
		
		// 5C: SPEC START
		
				private void addCreateListBoxHandler() 
				{				
					osceCreatePostBluePrintSubViewImpl.getPostTypeListBox().addValueChangeHandler(new ValueChangeHandler<PostType>() {

						@Override
						public void onValueChange(ValueChangeEvent<PostType> event) 
						{
								Log.info("Osce Id: " + osceProxy.getId());	
								//if(event.getValue()==PostType.BREAK)
								//{
									Log.info("Osce Id: " + osceProxy.getId());					
									/*if (Window.confirm("Do you want to Add Specialisation?")) 
									{
										Log.info("Select Specialisation to Add..");								
									}
									else
									{*/
										changeOsceStatus();
									//}
								//}												
						}

					});
					
					osceCreatePostBluePrintSubViewImpl.getRoleTopicListBox().addValueChangeHandler(new ValueChangeHandler<RoleTopicProxy>() 
					{
						@Override
						public void onValueChange(ValueChangeEvent<RoleTopicProxy> event) 
						{
									Log.info("Osce Id: " + osceProxy.getId());							
									changeOsceStatus();						
						}
					
					});
							
					
					osceCreatePostBluePrintSubViewImpl.getSpecializationListBox().addValueChangeHandler(new ValueChangeHandler<SpecialisationProxy>() 
					{
						@Override
						public void onValueChange(ValueChangeEvent<SpecialisationProxy> event) 
						{
							Log.info("Osce Id: " + osceProxy.getId());					
							/*if (Window.confirm("Do you want to Change/Add Role Topic")) 
							{
								Log.info("Add Role Topic...");
								if(event.getValue().getRoleTopics()!=null)
								{
									osceCreatePostBluePrintSubViewImpl.getRoleTopicListBox().setValue(null);
									osceCreatePostBluePrintSubViewImpl.getRoleTopicListBox().setAcceptableValues(event.getValue().getRoleTopics());
								}
								else
								{
									osceCreatePostBluePrintSubViewImpl.getRoleTopicListBox().setValue(null);
									osceCreatePostBluePrintSubViewImpl.getRoleTopicListBox().setAcceptableValues(new ArrayList<RoleTopicProxy>());
								}
							}
							else
							{
								Log.info("Not Add Role Topic");
								if(event.getValue() != null )
								{							
									
									osceCreatePostBluePrintSubViewImpl.getRoleTopicListBox().setAcceptableValues(event.getValue().getRoleTopics());
									changeOsceStatus();
									
								}							
								else
								{						
									osceCreatePostBluePrintSubViewImpl.getRoleTopicListBox().setAcceptableValues(new ArrayList<RoleTopicProxy>());							
									changeOsceStatus();
								}
							}
							*/
							
							changeOsceStatus();
						}
						
					});	
					
				}
				
				// 5C: SPEC END
				public void changeOsceStatus()
				{

					// Module 5 bug Report Change 
					//below code is moved to in the saveOscePostBlueprint() method
					
					/*if(osceProxy.getOsceStatus()==OsceStatus.OSCE_NEW)
					{	
						OsceProxy proxy1=osceProxy;
						OsceRequest osceRequest=requests.osceRequest();
						osceProxy=osceRequest.edit(osceProxy);
						osceProxy.setOsceStatus(OsceStatus.OSCE_BLUEPRINT);
						
						osceRequest.persist().using(osceProxy).fire(new OSCEReceiver<Void>() {

							@Override
							public void onSuccess(Void response) {
								Log.info("Osce status change to blueprint");
								//view.getScrollPanel().setStyleName("Osce_BluePrint_Status");
								//view.getScrollPanel().setStyleDependentName("BluePrint", false);
								//osceProxy.setOsceStatus(OsceStatus.OSCE_BLUEPRINT);
								view.getScrollPanel().removeStyleDependentName("NEW");
								view.getScrollPanel().addStyleDependentName("BluePrint");
								
								saveOscePostBlueprint(osceProxy);
								// Module 5 bug Report Change
									goTo(new CircuitDetailsPlace(osceProxy.stableId(),Operation.DETAILS));
								// E Module 5 bug Report Change 
								setOsceFixedButtonStyle(circuitOsceSubViewImpl, osceProxy);
							}
							
						});
						
					}
					else*/
					// E Module 5 bug Report Change
						saveOscePostBlueprint(osceProxy);
				}
				public void saveOscePostBlueprint(final OsceProxy osceProxy1)
				{			
					Log.info("Call Save() : " + osceProxy.getId());
					Log.info("Total Widget is:" + newPostAddHP.getWidgetCount());
					if((osceCreatePostBluePrintSubViewImpl.getPostTypeListBox().getValue())==null)
					{
						
						// Module 5 bug Report Change
						//Window.alert("Please Select Post Type.");
						MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
						dialog.showConfirmationDialog(constants.warningPostType());
						// Module 5 bug Report Change		
						return;
					}
				/*	if((osceCreatePostBluePrintSubViewImpl.getSpecializationListBox().getValue())==null && (osceCreatePostBluePrintSubViewImpl.getPostTypeListBox().getValue())!=PostType.BREAK)
					{
						Window.alert("Please Select the Specialization.");
						return;
					}	*/		
					//else
					//{
						
						if((osceCreatePostBluePrintSubViewImpl.getRoleTopicListBox().getValue())==null)
						{
							Log.info("Role Topic is Null");				
						}
						Log.info("Not Null");
					
						Log.info("~In PERSIST OTHER");
						
							OscePostBlueprintRequest oscePostRequest= requests.oscePostBlueprintRequest();
							final OscePostBlueprintProxy oscePostBlueprintProxy1 = oscePostRequest.create(OscePostBlueprintProxy.class);
							
							//create blue print proxy
							oscePostBlueprintProxy1.setRoleTopic((osceCreatePostBluePrintSubViewImpl.getRoleTopicListBox().getValue())==null? null:(RoleTopicProxy)osceCreatePostBluePrintSubViewImpl.getRoleTopicListBox().getValue());
							oscePostBlueprintProxy1.setOsce(osceProxy);					
							oscePostBlueprintProxy1.setPostType((PostType)osceCreatePostBluePrintSubViewImpl.getPostTypeListBox().getValue());
							oscePostBlueprintProxy1.setSpecialisation((osceCreatePostBluePrintSubViewImpl.getSpecializationListBox().getValue())==null? null:(SpecialisationProxy)osceCreatePostBluePrintSubViewImpl.getSpecializationListBox().getValue());
//							oscePostBlueprintProxy1.setIsPossibleStart(null);
							
							if(osceCreatePostBluePrintSubViewImpl.getPostTypeListBox().getValue()==PostType.ANAMNESIS_THERAPY || osceCreatePostBluePrintSubViewImpl.getPostTypeListBox().getValue()==PostType.PREPARATION)
							{
								oscePostBlueprintProxy1.setIsFirstPart(true);
							}
							else
							{
								oscePostBlueprintProxy1.setIsFirstPart(false);
							}
												
							oscePostBlueprintProxy1.setSequenceNumber(++maxSeq);
							
							oscePostRequest.persist().using(oscePostBlueprintProxy1).fire(new OSCEReceiver<Void>() 
							{									
									@Override
									public void onSuccess(Void response) 
									{
										Log.info("~Success Call....");
										Log.info("~oscePostBlueprintRequest.persist()");	
										Log.info("Add New Post in Keyword Table");
										Log.info("Data Saved Successfully....");	
										setOsceFixedButtonStyle(circuitOsceSubViewImpl, osceProxy);
										requests.find(oscePostBlueprintProxy1.stableId()).with("roleTopic","specialisation").fire(new OSCEReceiver<Object>() {

											@Override
											public void onSuccess(Object response) 
											{
												// TODO Auto-generated method stub	
												final OscePostBlueprintProxy oscePostBlueprintProxy=(OscePostBlueprintProxy)response;
												Log.info("~~Total Widget Before is:" + newPostAddHP.getWidgetCount());											
												
												Log.info("~~Total Widget After is:" + newPostAddHP.getWidgetCount());
												
												if(osceCreatePostBluePrintSubViewImpl.getPostTypeListBox().getValue()==PostType.ANAMNESIS_THERAPY || osceCreatePostBluePrintSubViewImpl.getPostTypeListBox().getValue()==PostType.PREPARATION)
												{
													Log.info("~In PERSIST ANAMNESIS_THERAPY || PREPARATION");
													
													OscePostBlueprintRequest oscePostRequestNew= requests.oscePostBlueprintRequest();
													final OscePostBlueprintProxy oscePostBlueprintProxyNew = oscePostRequestNew.create(OscePostBlueprintProxy.class);
													
													oscePostBlueprintProxyNew.setRoleTopic((osceCreatePostBluePrintSubViewImpl.getRoleTopicListBox().getValue())==null? null:(RoleTopicProxy)osceCreatePostBluePrintSubViewImpl.getRoleTopicListBox().getValue());
													oscePostBlueprintProxyNew.setOsce(osceProxy);						
													oscePostBlueprintProxyNew.setPostType((PostType)osceCreatePostBluePrintSubViewImpl.getPostTypeListBox().getValue());
													oscePostBlueprintProxyNew.setSpecialisation((osceCreatePostBluePrintSubViewImpl.getSpecializationListBox().getValue())==null? null:(SpecialisationProxy)osceCreatePostBluePrintSubViewImpl.getSpecializationListBox().getValue());
													
													oscePostBlueprintProxyNew.setIsFirstPart(false);

//													oscePostBlueprintProxyNew.setIsPossibleStart(null);
													oscePostBlueprintProxyNew.setSequenceNumber(++maxSeq);
													Log.info(""+oscePostBlueprintProxyNew.getPostType().name());
													Log.info(""+oscePostBlueprintProxyNew.getOsce().getId());
																	
													oscePostRequestNew.persist().using(oscePostBlueprintProxyNew).fire(new OSCEReceiver<Void>() 
													{									
															@Override
															public void onSuccess(Void response) 
															{					
																requests.find(oscePostBlueprintProxyNew.stableId()).with("roleTopic","specialisation").fire(new OSCEReceiver<Object>()
																		{

																			@Override
																			public void onSuccess(
																					Object response) {
																				Log.info("~Success Call....");	
																				//Module 5 bug Report Change	
																				newPostAddHP.insert(setAnemniesOsceBluePrintHP(oscePostBlueprintProxy, (((OscePostBlueprintProxy)response)),""+osceCreatePostBluePrintSubViewImpl.getPostTypeListBox().getValue()),newPostAddHP.getWidgetCount());
																				//E Module 5 bug Report Change	
																			}
																	
																		});
																														
															}																		
													});
													
												}
												 // Module 5 bug Report Change
												else if(osceCreatePostBluePrintSubViewImpl.getPostTypeListBox().getValue().equals(PostType.BREAK))							
												{
														Log.info("Selected BREAK" + PostType.BREAK);																												
														newPostAddHP.insert(setNewOsceBluePrintHP(((OscePostBlueprintProxy)response),""+osceCreatePostBluePrintSubViewImpl.getPostTypeListBox().getValue()),newPostAddHP.getWidgetCount());														
												}
													
												else
												{
													newPostAddHP.insert(setNewOsceBluePrintHP(((OscePostBlueprintProxy)response),""+osceCreatePostBluePrintSubViewImpl.getPostTypeListBox().getValue()),newPostAddHP.getWidgetCount());												
												}
												// E Module 5 bug Report Change
												
												final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.success());
												dialogBox.showConfirmationDialog(constants.saveOsceBlueprint());
												
												// Module 5 bug Report Change
												/*dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
													
													@Override
													public void onClick(ClickEvent event) {
														dialogBox.hide();
													
														
														
													}
												});
												*/
												 // E Module 5 bug Report Change
												
													dialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
													
													@Override
													public void onClick(ClickEvent event) {
														dialogBox.hide();
														
													
														
													}
												});
													
												//Window.alert("Osce Post Blueprint Saved Successfully.");
												osceCreatePostBluePrintSubViewImpl.getPostTypeListBox().setValue(null);
												osceCreatePostBluePrintSubViewImpl.getRoleTopicListBox().setValue(null);
												osceCreatePostBluePrintSubViewImpl.getSpecializationListBox().setValue(null);
											}
										});
										
										// E Module 5 bug Report Change
										if(osceProxy1.getOsceStatus()==OsceStatus.OSCE_NEW)
										{
											Log.info("Osce Status is New Changed to Blueprint.");
											OsceProxy proxy1=osceProxy1;
											OsceRequest osceRequest=requests.osceRequest();
											proxy1=osceRequest.edit(proxy1);
											proxy1.setOsceStatus(OsceStatus.OSCE_BLUEPRINT);
											
											osceRequest.persist().using(proxy1).fire(new OSCEReceiver<Void>() {

												@Override
												public void onSuccess(Void response) {
													Log.info("Osce status change to blueprint");													
													/*view.getScrollPanel().removeStyleDependentName("NEW");
													view.getScrollPanel().addStyleDependentName("BluePrint");*/	
													goTo(new CircuitDetailsPlace(osceProxy.stableId(),Operation.DETAILS));												
													setOsceFixedButtonStyle(circuitOsceSubViewImpl, osceProxy);
									}																		
												
							});
												
										}
										// E Module 5 bug Report Change
							
							
					}
							});
					}
				//}
				// Module 5 bug Report Change
				public OscePostViewImpl setNewOsceBluePrintHP(OscePostBlueprintProxy oscePostBlueprintProxy,String postType)
				// E Module 5 bug Report Change
				{
					
						Log.info("~setNewOsceBluePrintHP OscePostBluePrintProxy: " + oscePostBlueprintProxy.getId() + "as" + newPostAddHP.getWidgetCount()) ;
						int innerindex=newPostAddHP.getWidgetCount();
						
						OscePostSubViewImpl tempOscePostSubViewImpl=new OscePostSubViewImpl();										
						tempOscePostSubViewImpl.enableDisableforBluePrintStatus();
						
						// Module 5 Bug Test Change
						tempOscePostSubViewImpl.oscePostBlueprintProxy=oscePostBlueprintProxy;
						// E Module 5 Bug Test Change
						
						tempOscePostSubViewImpl.getPostNameLbl().setText(getLabelString(constants.circuitStation() + " "+oscePostBlueprintProxy.getSequenceNumber()));
						tempOscePostSubViewImpl.getPostNameLbl().setTitle(constants.circuitStation() + " "+oscePostBlueprintProxy.getSequenceNumber());
						tempOscePostSubViewImpl.setDelegate(circuitDetailsActivity); // SET DELEGATE FOR SUBVIEW
						tempOscePostSubViewImpl.getRoleTopicLbl().setText(getLabelString(oscePostBlueprintProxy.getRoleTopic()==null?constants.select()+": ":oscePostBlueprintProxy.getRoleTopic().getName()));
						tempOscePostSubViewImpl.getRoleTopicLbl().setTitle(oscePostBlueprintProxy.getRoleTopic()==null?constants.select()+": ":oscePostBlueprintProxy.getRoleTopic().getName());
						tempOscePostSubViewImpl.getSpecializationLbl().setText(getLabelString(oscePostBlueprintProxy.getSpecialisation()==null?constants.select()+": ":oscePostBlueprintProxy.getSpecialisation().getName()));			
						tempOscePostSubViewImpl.getSpecializationLbl().setTitle(oscePostBlueprintProxy.getSpecialisation()==null?constants.select()+": ":oscePostBlueprintProxy.getSpecialisation().getName());
						oscePostViewImpl=new OscePostViewImpl();	
						oscePostViewImpl.setStyleName("Osce-Status-BluePrint-Save", true);
						oSCENewSubViewImpl.getOscePostBluePrintSubViewImpl().getDragController().makeDraggable(oscePostViewImpl,oscePostViewImpl.getPostTypeLbl());
						
						oscePostViewImpl.setDelegate(circuitDetailsActivity);	// SET DELEGATE FOR MAIN POST VIEW	
						
						oscePostViewImpl.getPostTypeLbl().setText(oscePostBlueprintProxy.getPostType().name());	
						if(oscePostSubViewImpl == null){
							oscePostSubViewImpl = new ArrayList<OscePostSubViewImpl>();
						}
						
						//Module 5 bug Report Change	
						Log.info("Post Type: " + postType);					
						if(postType.equals(""+PostType.BREAK))
						{
							Log.info("~~Select Break");
							tempOscePostSubViewImpl.getSpecializationedit().setVisible(false);
							tempOscePostSubViewImpl.getRoleTopicEdit().setVisible(false);
							tempOscePostSubViewImpl.getRoomedit().setVisible(false);
							tempOscePostSubViewImpl.getStandardizedRoleEdit().setVisible(false);
							tempOscePostSubViewImpl.getSpecializationLbl().setVisible(false);
							tempOscePostSubViewImpl.getRoleTopicLbl().setVisible(false);
							tempOscePostSubViewImpl.getStandardizedRoleLbl().setVisible(false);
							tempOscePostSubViewImpl.getRoomLbl().setVisible(false);
						}
						// E Module 5 bug Report Change
						oscePostSubViewImpl.add(tempOscePostSubViewImpl);					
						oscePostSubViewImpl.get(innerindex).setDelegate(circuitDetailsActivity); // SET DELEGATE FOR SUBVIEW						
						oscePostViewImpl.getOscePostSubViewHP().add(tempOscePostSubViewImpl);	// ADD SUBVIEW IN POSTVIEW
						oscePostSubViewImpl.get(innerindex).oscePostBlueprintProxy=oscePostBlueprintProxy;	
						oscePostViewImpl.oscePostBlueprintProxy=oscePostBlueprintProxy;
						
						
						
						return oscePostViewImpl;
					
				
				}
				//Module 5 bug Report Change	
				//public OscePostViewImpl setAnemniesOsceBluePrintHP(OscePostBlueprintProxy oscePostBlueprintProxy,OscePostBlueprintProxy oscePostBlueprintProxyNext)
				public OscePostViewImpl setAnemniesOsceBluePrintHP(OscePostBlueprintProxy oscePostBlueprintProxy,OscePostBlueprintProxy oscePostBlueprintProxyNext,String postType)
				//E Module 5 bug Report Change	
				{
					
					Log.info("~setNewOsceBluePrintHP OscePostBluePrintProxy: " + oscePostBlueprintProxy.getId() + "as" + newPostAddHP.getWidgetCount()) ;
					int innerindex=newPostAddHP.getWidgetCount();
					
					OscePostSubViewImpl tempOscePostSubViewImpl=new OscePostSubViewImpl();										
					tempOscePostSubViewImpl.enableDisableforBluePrintStatus();
					
					// Module 5 Bug Test Change
					tempOscePostSubViewImpl.oscePostBlueprintProxy=oscePostBlueprintProxy;
					// E Module 5 Bug Test Change
					
					tempOscePostSubViewImpl.getPostNameLbl().setText(getLabelString(constants.circuitStation() + " " + oscePostBlueprintProxy.getSequenceNumber()));
					tempOscePostSubViewImpl.getPostNameLbl().setTitle(constants.circuitStation() + " " + oscePostBlueprintProxy.getSequenceNumber());
					tempOscePostSubViewImpl.setDelegate(circuitDetailsActivity); // SET DELEGATE FOR SUBVIEW
					tempOscePostSubViewImpl.getRoleTopicLbl().setText(getLabelString(oscePostBlueprintProxy.getRoleTopic()==null?constants.select()+": ":oscePostBlueprintProxy.getRoleTopic().getName()));
					tempOscePostSubViewImpl.getRoleTopicLbl().setTitle(oscePostBlueprintProxy.getRoleTopic()==null?constants.select()+": ":oscePostBlueprintProxy.getRoleTopic().getName());
					tempOscePostSubViewImpl.getSpecializationLbl().setText(getLabelString(oscePostBlueprintProxy.getSpecialisation()==null?constants.select()+": ":oscePostBlueprintProxy.getSpecialisation().getName()));			
					tempOscePostSubViewImpl.getSpecializationLbl().setTitle(oscePostBlueprintProxy.getSpecialisation()==null?constants.select()+": ":oscePostBlueprintProxy.getSpecialisation().getName());
					oscePostViewImpl=new OscePostViewImpl();
					//oscePostViewImpl.setStylePrimaryName("Osce-Status-BluePrint");
					oscePostViewImpl.setStyleName("Osce-Status-BluePrint-Save", true);
					//oscePostViewImpl.addStyleDependentName("Save");
					//oscePostViewImpl.setStyleName("Osce-Status-BluePrint-Save");
					oSCENewSubViewImpl.getOscePostBluePrintSubViewImpl().getDragController().makeDraggable(oscePostViewImpl,oscePostViewImpl.getPostTypeLbl());
					
					oscePostViewImpl.setDelegate(circuitDetailsActivity);	// SET DELEGATE FOR MAIN POST VIEW	
					
					oscePostViewImpl.getPostTypeLbl().setText(oscePostBlueprintProxy.getPostType().name());	
					if(oscePostSubViewImpl == null){
						oscePostSubViewImpl = new ArrayList<OscePostSubViewImpl>();
					}
					oscePostSubViewImpl.add(tempOscePostSubViewImpl);	
					tempOscePostSubViewImpl.setDelegate(circuitDetailsActivity);
					tempOscePostSubViewImpl.oscePostBlueprintProxy=oscePostBlueprintProxy;
					//oscePostSubViewImpl.get(innerindex).setDelegate(circuitDetailsActivity); // SET DELEGATE FOR SUBVIEW						
					oscePostViewImpl.getOscePostSubViewHP().add(tempOscePostSubViewImpl);	// ADD SUBVIEW IN POSTVIEW
					oscePostSubViewImpl.get(innerindex).oscePostBlueprintProxy=oscePostBlueprintProxy;	
					oscePostViewImpl.oscePostBlueprintProxy=oscePostBlueprintProxy;
					
					if(oscePostBlueprintProxy.getPostType()==PostType.ANAMNESIS_THERAPY || oscePostBlueprintProxy.getPostType()==PostType.PREPARATION)
					{
						Log.info("~Anemnis");				
							
						OscePostSubViewImpl tempOscePostSubViewImplNext=new OscePostSubViewImpl();
							tempOscePostSubViewImplNext.enableDisableforBluePrintStatus();	
							tempOscePostSubViewImplNext.setDelegate(circuitDetailsActivity);	// SET DELEGATE FOR SUBVIEW
							tempOscePostSubViewImplNext.getSpecializationLbl().setText(getLabelString(oscePostBlueprintProxyNext.getSpecialisation()==null?constants.select()+": ":oscePostBlueprintProxyNext.getSpecialisation().getName()));
							tempOscePostSubViewImplNext.getSpecializationLbl().setTitle(oscePostBlueprintProxyNext.getSpecialisation()==null?constants.select()+": ":oscePostBlueprintProxyNext.getSpecialisation().getName());
							tempOscePostSubViewImplNext.getRoleTopicLbl().setText(getLabelString(oscePostBlueprintProxyNext.getRoleTopic()==null?constants.select()+": ":oscePostBlueprintProxyNext.getRoleTopic().getName()));
							tempOscePostSubViewImplNext.getRoleTopicLbl().setTitle(oscePostBlueprintProxyNext.getRoleTopic()==null?constants.select()+": ":oscePostBlueprintProxyNext.getRoleTopic().getName());
							oscePostViewImpl.oscePostBlueprintProxyNext=oscePostBlueprintProxyNext;
							tempOscePostSubViewImplNext.getPostNameLbl().setText(getLabelString(constants.circuitStation() + " " + oscePostBlueprintProxyNext.getSequenceNumber()));
							tempOscePostSubViewImplNext.getPostNameLbl().setTitle(constants.circuitStation() + " " + oscePostBlueprintProxyNext.getSequenceNumber());
							tempOscePostSubViewImplNext.oscePostBlueprintProxy=oscePostBlueprintProxyNext;
							Log.info("OsceBluerint Next Id: " + oscePostBlueprintProxyNext.getId());												
							oscePostSubViewImpl.add(tempOscePostSubViewImplNext);	
							oscePostSubViewImpl.get(innerindex).setDelegate(circuitDetailsActivity);
							innerindex++;																							
							
							//Module 5 bug Report Change	
							Log.info("Post Type: " + postType);					
							if(postType.equals(""+PostType.BREAK))
							{
								Log.info("~~Select Break");
								tempOscePostSubViewImplNext.getSpecializationedit().setVisible(false);
								tempOscePostSubViewImplNext.getRoleTopicEdit().setVisible(false);
								tempOscePostSubViewImplNext.getRoomedit().setVisible(false);
								tempOscePostSubViewImplNext.getStandardizedRoleEdit().setVisible(false);
								tempOscePostSubViewImplNext.getSpecializationLbl().setVisible(false);
								tempOscePostSubViewImplNext.getRoleTopicLbl().setVisible(false);
								tempOscePostSubViewImplNext.getStandardizedRoleLbl().setVisible(false);
								tempOscePostSubViewImplNext.getRoomLbl().setVisible(false);
							}											
							Log.info("Total Widget Before Adding: " + oscePostViewImpl.getOscePostSubViewHP().getWidgetCount());
							//E Module 5 bug Report Change
							oscePostViewImpl.getOscePostSubViewHP().add(tempOscePostSubViewImplNext);	// ADD SUBVIEW IN POSTVIEW	
							oscePostViewImpl.oscePostBlueprintProxyNext=oscePostBlueprintProxyNext;
							oscePostSubViewImpl.get(innerindex).setDelegate(circuitDetailsActivity); // SET DELEGATE FOR SUBVIEW
							oscePostSubViewImpl.get(innerindex).oscePostBlueprintProxyNext=oscePostBlueprintProxyNext;	
							
																																											
					}	
					
					return oscePostViewImpl;
					
				
				}
				
				@Override
				public void specializationEditClicked(final OscePostSubViewImpl oscePostSubViewImpledit) 
				{
					
					Log.info("~specializationEditClicked() from Activity");
					
					// Module 5 bug Report Change
						//oscePostSubViewImpledit.oscePostBlueprintProxy=this.oscePostBlueprintProxy;
					// E Module 5 bug Report Change
					
					requests.specialisationRequest().findAllSpecialisations().fire(new OSCEReceiver<List<SpecialisationProxy>>() 
					{
						public void onSuccess(List<SpecialisationProxy> response) 
						{				
							
							//Log.info("oscePostSubViewImpledit: " + oscePostSubViewImpledit.oscePostBlueprintProxy.getId());
							if(response==null)
							{
								Log.info("response null");
							}
							
							((OscePostSubViewImpl)oscePostSubViewImpledit).createOptionPopup();						
							HorizontalPanel spHorizontalPanel=((OscePostSubViewImpl)oscePostSubViewImpledit).getSpecializationHP();
							HorizontalPanel rtHorizontalPanel=((OscePostSubViewImpl)oscePostSubViewImpledit).getRoleTopicHP();
							
							
							//Issue # 122 : Replace pull down with autocomplete.
							//((ListBoxPopupViewImpl)((OscePostSubViewImpl)oscePostSubViewImpledit).popupView).setPopupPosition(spHorizontalPanel.getAbsoluteLeft()-40, rtHorizontalPanel.getAbsoluteTop()-80);					
							//Issue # 122 : Replace pull down with autocomplete.
							//((ListBoxPopupViewImpl)((OscePostSubViewImpl)oscePostSubViewImpledit).popupView).getListBox().setValue(null);
							//((ListBoxPopupViewImpl)((OscePostSubViewImpl)oscePostSubViewImpledit).popupView).getNewListBox().setSelected(null);
							//Issue # 122 : Replace pull down with autocomplete.
							ArrayList proxy=new ArrayList();
							
							//spec issue sol
							if (response != null)
								proxy.addAll(response);
							
							
							
							//Issue # 122 : Replace pull down with autocomplete.
							
							//((OscePostSubViewImpl)oscePostSubViewImpledit).popupView.getListBox().setAcceptableValues(proxy);
							//((ListBoxPopupViewImpl)((OscePostSubViewImpl)oscePostSubViewImpledit).popupView).show();										
							//((OscePostSubViewImpl)oscePostSubViewImpledit).popupView.getListBox().setValue(oscePostSubViewImpledit.oscePostBlueprintProxy.getSpecialisation());
							
							
							DefaultSuggestOracle<Object> suggestOracle1 = ((DefaultSuggestOracle<Object>)((((ListBoxPopupViewImpl)((OscePostSubViewImpl)oscePostSubViewImpledit).popupView).getNewListBox().getSuggestOracle())));
							suggestOracle1.setPossiblilities(proxy);
							((ListBoxPopupViewImpl)((OscePostSubViewImpl)oscePostSubViewImpledit).popupView).getNewListBox().setSuggestOracle(suggestOracle1);
							
							//((OscePostSubViewImpl)view).popupView.getNewListBox().setRenderer(new StandardizedRoleProxyRenderer());
							((ListBoxPopupViewImpl)((OscePostSubViewImpl)oscePostSubViewImpledit).popupView).getNewListBox().setRenderer(new Renderer<Object>() {

								@Override
								public String render(Object object) {
									// TODO Auto-generated method stub
									//return object.getShortName();
									if(((SpecialisationProxy)object)==null)
									{
										Log.info("in if");
										return "";
									}
									else
									{
										Log.info("in else");
									return ((SpecialisationProxy)object).getName();
									}
								}

								@Override
								public void render(Object object,
										Appendable appendable) throws IOException {
									// TODO Auto-generated method stub
									
								}
							});
							
							if(oscePostSubViewImpledit.oscePostBlueprintProxy != null)
							{
								((OscePostSubViewImpl)oscePostSubViewImpledit).popupView.getNewListBox().setSelected(oscePostSubViewImpledit.oscePostBlueprintProxy.getSpecialisation());
							}												
														
							// Module 5 Bug Test Change				
							/*((ListBoxPopupViewImpl)((OscePostSubViewImpl)oscePostSubViewImpledit).popupView).setPopupPosition(spHorizontalPanel.getAbsoluteLeft()-40, rtHorizontalPanel.getAbsoluteTop()-80);*/
							((ListBoxPopupViewImpl)((OscePostSubViewImpl)oscePostSubViewImpledit).popupView).setPopupPosition(spHorizontalPanel.getAbsoluteLeft()-256, rtHorizontalPanel.getAbsoluteTop()-110);
							// E Module 5 Bug Test Change
							
							((ListBoxPopupViewImpl)((OscePostSubViewImpl)oscePostSubViewImpledit).popupView).show();										
							

							//((OscePostSubViewImpl)view).popupView.getListBox().setAcceptableValues(list);
							
							//Issue # 122 : Replace pull down with autocomplete.
							
						
							}
					});	
				}

				@Override
				public void roleEditClicked(final OscePostSubViewImpl oscePostSubViewImpledit) 
				{
					if(oscePostSubViewImpledit.oscePostBlueprintProxy.getSpecialisation()==null)
					{
						// Module 5 bug Report Change
						//Window.alert("Select Specialisation.");
						MessageConfirmationDialogBox SpecialisationDialog=new MessageConfirmationDialogBox(constants.warning());
						SpecialisationDialog.showConfirmationDialog(constants.selectSpecialisation());
						return;
						// E Module 5 bug Report Change
					}
					// Module 5 bug Report Change
					/*if(oscePostSubViewImpledit.oscePostBlueprintProxy.getSpecialisation()==null)
					{
						Window.alert("Please Select Specialisation.");
						return;
					}*/
					// E Module 5 bug Report Change
					Log.info("~roleEditClicked() from Activity" + oscePostSubViewImpledit.oscePostBlueprintProxy.getSpecialisation().getId());	
											
					((OscePostSubViewImpl)oscePostSubViewImpledit).createOptionPopup();						
					HorizontalPanel spHorizontalPanel=((OscePostSubViewImpl)oscePostSubViewImpledit).getSpecializationHP();
					HorizontalPanel rtHorizontalPanel=((OscePostSubViewImpl)oscePostSubViewImpledit).getRoleTopicHP();

					// Module 5 Bug Test Change
					/*((OscePostSubViewImpl)oscePostSubViewImpledit).listBoxPopupViewImpl.setPopupPosition(spHorizontalPanel.getAbsoluteLeft()-40, rtHorizontalPanel.getAbsoluteTop()-80);*/
					((OscePostSubViewImpl)oscePostSubViewImpledit).listBoxPopupViewImpl.setPopupPosition(spHorizontalPanel.getAbsoluteLeft()-256, rtHorizontalPanel.getAbsoluteTop()-50);
					// E Module 5 Bug Test Change

					((OscePostSubViewImpl)oscePostSubViewImpledit).listBoxPopupViewImpl.show();
					
					requests.roleTopicRequestNonRoo().findRoleTopicBySpecialisation(oscePostSubViewImpledit.oscePostBlueprintProxy.getSpecialisation().getId()).fire(new OSCEReceiver<List<RoleTopicProxy>>() 
					{
						@Override
						public void onSuccess(List<RoleTopicProxy> response) 
						{
							Log.info("Find RoleTopic for Specialization Size: " + response.size());	
							//Issue # 122 : Replace pull down with autocomplete.
							//((OscePostSubViewImpl)oscePostSubViewImpledit).listBoxPopupViewImpl.getListBox().setValue(null);
							//((OscePostSubViewImpl)oscePostSubViewImpledit).listBoxPopupViewImpl.getNewListBox().setSelected(null);
							//Issue # 122 : Replace pull down with autocomplete.
							ArrayList proxy=new ArrayList();
							proxy.addAll(response);
							
							//Issue # 122 : Replace pull down with autocomplete.
							
							
							DefaultSuggestOracle<Object> suggestOracle1 = ((DefaultSuggestOracle<Object>) (((OscePostSubViewImpl)oscePostSubViewImpledit).listBoxPopupViewImpl.getNewListBox().getSuggestOracle()));
							suggestOracle1.setPossiblilities(proxy);
							((OscePostSubViewImpl)oscePostSubViewImpledit).listBoxPopupViewImpl.getNewListBox().setSuggestOracle(suggestOracle1);
							
							//((OscePostSubViewImpl)view).popupView.getNewListBox().setRenderer(new StandardizedRoleProxyRenderer());
							((OscePostSubViewImpl)oscePostSubViewImpledit).listBoxPopupViewImpl.getNewListBox().setRenderer(new Renderer<Object>() 
							{

								@Override
								public String render(Object object) {
									// TODO Auto-generated method stub
									//return object.getShortName();
									return ((RoleTopicProxy)object).getName();
								}

								@Override
								public void render(Object object,
										Appendable appendable) throws IOException {
									// TODO Auto-generated method stub
									
								}
							});
							
							//((OscePostSubViewImpl)oscePostSubViewImpledit).listBoxPopupViewImpl.getListBox().setAcceptableValues(proxy);
							
							if(oscePostSubViewImpledit.oscePostBlueprintProxy.getRoleTopic()==null)
							{
								Log.info("Null Role Topic.");
							}
							else
							{						
								
								((OscePostSubViewImpl)oscePostSubViewImpledit).listBoxPopupViewImpl.getNewListBox().setRenderer(new Renderer<Object>() {

									@Override
									public String render(Object object) {
										// TODO Auto-generated method stub
										//return object.getShortName();
										return ((RoleTopicProxy)object).getName();
							}					

									@Override
									public void render(Object object,
											Appendable appendable) throws IOException {
										// TODO Auto-generated method stub
										
									}
								});
								((OscePostSubViewImpl)oscePostSubViewImpledit).listBoxPopupViewImpl.getNewListBox().setSelected(oscePostSubViewImpledit.oscePostBlueprintProxy.getRoleTopic());
								//((OscePostSubViewImpl)oscePostSubViewImpledit).listBoxPopupViewImpl.getListBox().setValue(oscePostSubViewImpledit.oscePostBlueprintProxy.getRoleTopic());	
							}		
							
							//Issue # 122 : Replace pull down with autocomplete.
								
										
						}
					});
										
				}

				@Override
				public void saveSpecialisation(final OscePostSubViewImpl oscePostSubViewImplok) 
				{
					Log.info("~okClicked() from Activity");			
					Log.info("savSpecialisation");
					
				
					//oscePostSubViewImplok.oscePostBlueprintProxy=oscePostBlueprintProxy;
					
					Log.info("oscePostSubViewImpledit" + oscePostSubViewImplok.oscePostBlueprintProxy.getId());	
					Log.info("oscePostSubViewImpledit" + oscePostSubViewImplok.oscePostBlueprintProxy.getId());
										
					OscePostBlueprintProxy oscePostBlueprintProxy=oscePostSubViewImplok.oscePostBlueprintProxy;
				//	final SpecialisationProxy specialisationProxy=(SpecialisationProxy)oscePostSubViewImplok.getListBoxPopupViewImpl().getListBox().getValue();
					
					//Issue # 122 : Replace pull down with autocomplete.
					//final SpecialisationProxy specialisationProxy=(SpecialisationProxy)oscePostSubViewImplok.getListBoxPopupViewImpl().getListBox().getValue();
					final SpecialisationProxy specialisationProxy=(SpecialisationProxy)oscePostSubViewImplok.getListBoxPopupViewImpl().getNewListBox().getSelected();
					//Issue # 122 : Replace pull down with autocomplete.
					Log.info("Specialisation Selected: " + specialisationProxy.getName());
					
					OscePostBlueprintRequest oscePostBlueprintRequest=requests.oscePostBlueprintRequest();
					oscePostBlueprintProxy=oscePostBlueprintRequest.edit(oscePostBlueprintProxy);		
					
					oscePostBlueprintProxy.setSpecialisation(specialisationProxy);
					oscePostBlueprintProxy.setRoleTopic(null);
					
					oscePostSubViewImplok.oscePostBlueprintProxy=oscePostBlueprintProxy;
					Log.info("oscePostSubViewImplok : " + oscePostSubViewImplok.oscePostBluePrintMap.size());
					oscePostBlueprintRequest.persist().using(oscePostBlueprintProxy).fire(new OSCEReceiver<Void>(oscePostSubViewImplok.oscePostBluePrintMap)
					{
						@Override
						public void onSuccess(Void response) 
						{
							Log.info("Success saveSpecialisation ");
							oscePostSubViewImplok.getSpecializationLbl().setText(getLabelString(specialisationProxy.getName()));
							oscePostSubViewImplok.getSpecializationLbl().setTitle(specialisationProxy.getName());
							oscePostSubViewImplok.listBoxPopupViewImpl.hide();					
							//Window.alert("The Role Topic for Specialisation " + specialisationProxy.getName()+" is deleted, You need to Select thr Role.");
							oscePostSubViewImplok.getRoleTopicLbl().setText(constants.select()+":");
							oscePostSubViewImplok.listBoxPopupViewImpl.hide();	
						}
					
					});		
					
				}

				@Override
				public void saveRoleTopic(final OscePostSubViewImpl oscePostSubViewImplok) 
				{
					Log.info("~okClicked() from Activity");			
					Log.info("saveRoleTopic");
					Log.info("oscePostSubViewImpledit" + oscePostSubViewImplok.oscePostBlueprintProxy.getId());	
					
					OscePostBlueprintProxy oscePostBlueprintProxy=oscePostSubViewImplok.oscePostBlueprintProxy;
					//Issue # 122 : Replace pull down with autocomplete.
					//final RoleTopicProxy roleTopicProxy=(RoleTopicProxy)oscePostSubViewImplok.getListBoxPopupViewImpl().getListBox().getValue();
					final RoleTopicProxy roleTopicProxy=(RoleTopicProxy)oscePostSubViewImplok.getListBoxPopupViewImpl().getNewListBox().getSelected();
					//Issue # 122 : Replace pull down with autocomplete.
					Log.info("Role Topic Selected: " + roleTopicProxy.getName());
					
					OscePostBlueprintRequest oscePostBlueprintRequest=requests.oscePostBlueprintRequest();
					oscePostBlueprintProxy=oscePostBlueprintRequest.edit(oscePostBlueprintProxy);
					oscePostBlueprintProxy.setRoleTopic(roleTopicProxy);
					oscePostSubViewImplok.oscePostBlueprintProxy=oscePostBlueprintProxy;
					Log.info("oscePostSubViewImplok : " + oscePostSubViewImplok.oscePostBluePrintMap.size());
					oscePostBlueprintRequest.persist().using(oscePostBlueprintProxy).fire(new OSCEReceiver<Void>(oscePostSubViewImplok.oscePostBluePrintMap)
					{
						@Override
						public void onSuccess(Void response) 
						{
							Log.info("Success saveSpecialisation ");
							oscePostSubViewImplok.getRoleTopicLbl().setText(getLabelString(roleTopicProxy.getName()));
							oscePostSubViewImplok.getRoleTopicLbl().setTitle(roleTopicProxy.getName());
							oscePostSubViewImplok.listBoxPopupViewImpl.hide();
						}
					
					});			
				}

				// Module 5 bug Report Change
				//Update Sequence of All post inside HP
				public void updateOscePostsSequence(HorizontalPanel hp)
				{
					Log.info("updateOsceSequences");
				
					maxSeq=0;
					int j=0;
					for(int i=0;i<hp.getWidgetCount();i++)
					{
						if(hp.getWidget(i) instanceof OsceCreatePostBluePrintSubViewImpl)
							continue;
						
						else if(((OscePostView)hp.getWidget(i)).getOscePostBlueprintProxy().getPostType()==PostType.ANAMNESIS_THERAPY ||  ((OscePostView)hp.getWidget(i)).getOscePostBlueprintProxy().getPostType()==PostType.PREPARATION)
						{
							
							// Module 5 bug Report Change
							/*updateBluePrintSequence(((OscePostView)hp.getWidget(i)).getOscePostBlueprintProxy(),i+j+1);
							j++;
							updateBluePrintSequence(((OscePostView)hp.getWidget(i)).getOscePostBlueprintProxyNext(),i+j+1);*/
							Log.info("~~Set Label");
							Log.info("Lable Text: " + ((OscePostSubView)((OscePostView)hp.getWidget(i)).getOscePostSubViewHP().getWidget(0)).getPostNameLbl().getText());
						
							updateBluePrintSequence(((OscePostView)hp.getWidget(i)).getOscePostBlueprintProxy(),i+j+1);
							((OscePostSubView)((OscePostView)hp.getWidget(i)).getOscePostSubViewHP().getWidget(0)).getPostNameLbl().setText(getLabelString(constants.circuitStation() + " " + (i+j+1)));
							((OscePostSubView)((OscePostView)hp.getWidget(i)).getOscePostSubViewHP().getWidget(0)).getPostNameLbl().setTitle(constants.circuitStation() + " " + (i+j+1));
							j++;
							updateBluePrintSequence(((OscePostView)hp.getWidget(i)).getOscePostBlueprintProxyNext(),i+j+1);
							((OscePostSubView)((OscePostView)hp.getWidget(i)).getOscePostSubViewHP().getWidget(1)).getPostNameLbl().setText(getLabelString(constants.circuitStation() + " " + (i+j+1)));
							((OscePostSubView)((OscePostView)hp.getWidget(i)).getOscePostSubViewHP().getWidget(1)).getPostNameLbl().setTitle(constants.circuitStation() + " " + (i+j+1));
							
							// E Module 5 bug Report Change

							//((OscePostSubView)((OscePostView)hp.getWidget(i)).getOscePostSubViewHP().getWidget(0)).getPostNameLbl().setText("Post "+i+1);
							// E Module 5 bug Report Change
							
							maxSeq=maxSeq+2;

						}
						else
						{
							// Module 5 bug Report Change
							//updateBluePrintSequence(((OscePostView)hp.getWidget(i)).getOscePostBlueprintProxy(),i+j+1);
							updateBluePrintSequence(((OscePostView)hp.getWidget(i)).getOscePostBlueprintProxy(),i+j+1);
							((OscePostSubView)((OscePostView)hp.getWidget(i)).getOscePostSubViewHP().getWidget(0)).getPostNameLbl().setText(getLabelString(constants.circuitStation() + " " + (i+j+1)));
							((OscePostSubView)((OscePostView)hp.getWidget(i)).getOscePostSubViewHP().getWidget(0)).getPostNameLbl().setTitle(constants.circuitStation() + " " + (i+j+1));

							Log.info("~~Set Label");
							Log.info("Lable Text: " + ((OscePostSubView)((OscePostView)hp.getWidget(i)).getOscePostSubViewHP().getWidget(0)).getPostNameLbl().getText());
//							((OscePostSubView)((OscePostView)hp.getWidget(i)).getOscePostSubViewHP().getWidget(0)).getPostNameLbl().setText("Post "+i+1);
							// E Module 5 bug Report Change
							maxSeq++;

						}
						
					}
				}
				// E Module 5 bug Report Change
				
				@Override
				public void deletePostClicked(final OscePostViewImpl oscePostViewImpl) 
				{
					// Module 5 bug Report Change
					final HorizontalPanel hp=((HorizontalPanel)oscePostViewImpl.getParent());
					Log.info("Osce Proxy: " +osceProxy.getId());
					Log.info("Total Widget Before Delete: " + hp.getWidgetCount());
					// E Module 5 bug Report Change
					
					requests.oscePostBlueprintRequest().remove().using(oscePostViewImpl.oscePostBlueprintProxy).fire(new OSCEReceiver<Void>() 
					{
						public void onSuccess(Void ignore) 
						{
							
							
							if(oscePostViewImpl.oscePostBlueprintProxy.getPostType()!=null && (oscePostViewImpl.oscePostBlueprintProxy.getPostType()==PostType.ANAMNESIS_THERAPY || oscePostViewImpl.oscePostBlueprintProxy.getPostType()==PostType.PREPARATION))				
							{
								Log.info("Delete Two Records.");
								/*requests.oscePostBlueprintRequest().findOscePostBlueprint((oscePostViewImpl.oscePostBlueprintProxyNext.getId())).fire(new OSCEReceiver<OscePostBlueprintProxy>() {

									@Override
									public void onSuccess(OscePostBlueprintProxy response) 
									{*/
										requests.oscePostBlueprintRequest().remove().using(oscePostViewImpl.oscePostBlueprintProxyNext).fire(new OSCEReceiver<Void>() 
										{
											public void onSuccess(Void ignore) 
											{
													//Window.alert("TWO POST Successfully Deleted");	

												// Module 5 bug Report Change
												//HorizontalPanel hp=((HorizontalPanel)oscePostViewImpl.getParent());
													oscePostViewImpl.removeFromParent();
												updateOscePostsSequence(hp);
												Log.info("Osce Proxy Anamnesis Therapy: " +osceProxy.getId());
												Log.info("Total Widget After Delete Anamnesis Therapy: " + hp.getWidgetCount());
												if(hp.getWidgetCount()<=0)
												{
													OsceRequest osceRequest=requests.osceRequest();
													osceProxy=osceRequest.edit(osceProxy);
													osceProxy.setOsceStatus(OsceStatus.OSCE_NEW);
													
													osceRequest.persist().using(osceProxy).fire(new OSCEReceiver<Void>() 
													{

														@Override
														public void onSuccess(Void response) 
														{
															goTo(new CircuitDetailsPlace(osceProxy.stableId(),Operation.DETAILS));
															
														}
														@Override
														public void onFailure(ServerFailure error)
														{
															Log.info("Failure");
															MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
															dialog.showConfirmationDialog(constants.warningStatusNotChanged());
														}
													});
													
												}
												// E Module 5 bug Report Change
												
												//	maxSeq--;
												//	maxSeq--;
													//updateBluePrintSequences();
											}
										});		
									/*}
								
								});*/
							}
							else
							{
								//Window.alert("POST Successfully Deleted");
												
								// Module 5 bug Report Change
								//HorizontalPanel hp=((HorizontalPanel)oscePostViewImpl.getParent());
								oscePostViewImpl.removeFromParent();
								updateOscePostsSequence(hp);
							
								Log.info("Total Widget After Delete: " + hp.getWidgetCount());
								if(hp.getWidgetCount()<=0)
								{
									
									OsceRequest osceRequest=requests.osceRequest();
									osceProxy=osceRequest.edit(osceProxy);
									osceProxy.setOsceStatus(OsceStatus.OSCE_NEW);
									
									osceRequest.persist().using(osceProxy).fire(new OSCEReceiver<Void>() 
									{

										@Override
										public void onSuccess(Void response) 
										{											
											goTo(new CircuitDetailsPlace(osceProxy.stableId(),Operation.DETAILS));
							}
										@Override
										public void onFailure(ServerFailure error)
										{
											Log.info("Failure");
											MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
											dialog.showConfirmationDialog(constants.warningStatusNotChanged());
						}
					});	
												
								}
								// E Module 5 bug Report Change
								
							}

							
							
						}
						
					});	
					
				}
				
				// OSCE Day Assignment Start 
				
				public void setOsceStatusStyle(String style){
					//circuitOsceSubViewImpl.setStyleName(style);
					circuitOsceSubViewImpl.addStyleName(style);
				}
				public void setDayStatusStyle(String style){
					osceDayViewImpl.setStyleName(style);
				}
				public void addTimeHendlers(){
				
					
					osceDayViewImpl.startTimeTextBox.addBlurHandler(new BlurHandler() {
						public boolean dayStartTimeflag=true;
						@Override
						public void onBlur(BlurEvent event) {
							if(osceProxy.getOsceStatus()==OsceStatus.OSCE_GENRATED && !osceDayViewImpl.startTimeTextBox.getValue().equals("")){
							if(osceDayViewImpl.startTimeTextBox.getValue() !=null ){
								
								if(! checkStarttimeValidation()){
									return;
								}
								
							}
							
							Time startDate=new Time( osceProxy.getOsce_days().iterator().next().getTimeStart().getTime());
							Time endDate=new Time( osceProxy.getOsce_days().iterator().next().getTimeEnd().getTime());
							
							long diff=Math.abs((startDate.getTime())-endDate.getTime());
							
							Log.info("diff--"+diff);
							
						//	DateFormat dateformat = new SimpleDateFormat("HH:mm");
							
							try{
								//Date newDateTime=DateTimeFormat.getShortDateTimeFormat().parse((osceDayViewImpl.startTimeTextBox.getValue()));
								
								//Log.info("Before Date Format" + osceDayViewImpl.startTimeTextBox.getValue());
								
								Date newDateTime=new Date();
								String hrs=osceDayViewImpl.startTimeTextBox.getValue().substring(0, 2);
								newDateTime.setHours(new Integer(hrs));
								String mts=osceDayViewImpl.startTimeTextBox.getValue().substring(3, 5);
								newDateTime.setMinutes(new Integer(mts));
								
								//Date oldDate = new Date(osceDayViewImpl.startTimeTextBox.getValue());
								//Date newDateTime=DateTimeFormat.getShortDateTimeFormat().parse(oldDate.toString());
								
								
								
								//Log.info("After Date Format" + newDateTime);
							//	Date newDateTime=dateformat.parse((osceDayViewImpl.startTimeTextBox.getValue()));
								Log.info("New Date is" + newDateTime);
								
								Time newTime=new Time(newDateTime.getTime());
								newTime.setTime(newTime.getTime() + diff);
								osceDayViewImpl.endTimeTextBox.setText(newTime.toString().substring(0,5));
							}catch(Exception e){
								Log.info("Parse Exception Occured :");
								e.printStackTrace();
							}
							
						}
							else{
									if(osceDayViewImpl.startTimeTextBox.getValue() !=null && !osceDayViewImpl.startTimeTextBox.getValue().equals(""))
									if(! checkStarttimeValidation()){
										return;
									}
							}
						}
					}); 
					

					osceDayViewImpl.endTimeTextBox.addBlurHandler(new BlurHandler() {
						
						@Override
						public void onBlur(BlurEvent event) {
							if(osceProxy.getOsceStatus()==OsceStatus.OSCE_GENRATED && !osceDayViewImpl.endTimeTextBox.getValue().equals("")){
								
								if(osceDayViewImpl.endTimeTextBox.getValue() !=null ){
									
									if( ! checkEndTimeValidation())
									return;
								}
							Time startDate=new Time( osceProxy.getOsce_days().iterator().next().getTimeStart().getTime());
							Time endDate=new Time( osceProxy.getOsce_days().iterator().next().getTimeEnd().getTime());
							
							long diff=Math.abs((startDate.getTime())-endDate.getTime());
							

							System.out.println("diff--"+diff);
							
							
							//DateFormat dateformat = new SimpleDateFormat("HH:mm");
							
							try{
								Date endTime=new Date();
								String hrs=osceDayViewImpl.endTimeTextBox.getValue().substring(0, 2);
								endTime.setHours(new Integer(hrs));
								String mts=osceDayViewImpl.endTimeTextBox.getValue().substring(3, 5);
								endTime.setMinutes(new Integer(mts));
								
							//	Date newDateTime=DateTimeFormat.getShortDateTimeFormat().parse(endTime.toString());
								
								//Date newDateTime=dateformat.parse((osceDayViewImpl.endTimeTextBox.getValue()));
								Log.info("New Date is" + endTime);
								
								Time newTime=new Time(endTime.getTime());
								newTime.setTime(newTime.getTime() - diff);
								osceDayViewImpl.startTimeTextBox.setText(newTime.toString().substring(0,5));
							} catch(Exception e){
								Log.info("Parse Exception Occured :");
								e.printStackTrace();
							}

							}
							else{
								if(osceDayViewImpl.endTimeTextBox.getValue() !=null && !osceDayViewImpl.endTimeTextBox.getValue().equals("")){
									
									if(! checkEndTimeValidation())
									return;
								}
							}
						}
					});
				}
				public boolean checkStarttimeValidation(){
					boolean dayStartTimeValidflag=true;
					String sTimeValue=osceDayViewImpl.startTimeTextBox.getValue();
					if(! sTimeValue.matches("^[]0-9]{2}\\:[0-9]{2}$"))
					{
						// Module 5 bug Report Change
							//Window.alert("please Enter valid formatted Time Valid format is HH:MM");
						MessageConfirmationDialogBox sTimeValueDialog=new MessageConfirmationDialogBox(constants.warning());
						sTimeValueDialog.showConfirmationDialog(constants.warningTimeFormat());
						osceDayViewImpl.startTimeTextBox.setValue("");
						// E Module 5 bug Report Change
						
						dayStartTimeValidflag=false;
						return dayStartTimeValidflag;
					}
					
					if(new Integer(sTimeValue.substring(0,2)) >= 24)
					{						
						
						// Module 5 bug Report Change
						//Window.alert("Please Enter Valid Hour (Allowed Till 24)");
						MessageConfirmationDialogBox dialog1=new MessageConfirmationDialogBox(constants.warning());
						dialog1.showConfirmationDialog(constants.warningTimeHour());
						osceDayViewImpl.startTimeTextBox.setValue("");
						// Module 5 bug Report Change
						
						dayStartTimeValidflag=false;
						return dayStartTimeValidflag;
					}
					if(new Integer(sTimeValue.substring(3,5))> 59)
					{
						// Module 5 bug Report Change
						//Window.alert("Please Enter Valid Minutes (Allowed Till 59)");
						MessageConfirmationDialogBox dialog2=new MessageConfirmationDialogBox(constants.warning());
						dialog2.showConfirmationDialog(constants.warningTimeMinute());
						osceDayViewImpl.startTimeTextBox.setValue("");
						// E Module 5 bug Report Change
						dayStartTimeValidflag=false;
						return dayStartTimeValidflag;
					}
					return dayStartTimeValidflag;
				}
				public boolean checkEndTimeValidation(){
					boolean dayEndTimeValidFlag=true;
					String sTimeValue=osceDayViewImpl.endTimeTextBox.getValue();
					if(! sTimeValue.matches("^[]0-9]{2}\\:[0-9]{2}$")){
						// Module 5 bug Report Change
						//Window.alert("please Enter valid formatted Time Valid format is HH:MM");
						MessageConfirmationDialogBox startTimedialog=new MessageConfirmationDialogBox(constants.warning());
						startTimedialog.showConfirmationDialog(constants.warningTimeFormat());	
						osceDayViewImpl.endTimeTextBox.setValue("");
						// Module 5 bug Report Change						

						dayEndTimeValidFlag=false;
						return dayEndTimeValidFlag;
					}
					
					if(new Integer(sTimeValue.substring(0,2)) >= 24){
						
						// Module 5 bug Report Change
						//Window.alert("Please Enter Valid Hour (Allowed Till 24)");
						MessageConfirmationDialogBox startTimedialog1=new MessageConfirmationDialogBox(constants.warning());
						startTimedialog1.showConfirmationDialog(constants.warningTimeHour());
						osceDayViewImpl.endTimeTextBox.setValue("");
						// Module 5 bug Report Change						
						
						dayEndTimeValidFlag=false;
						return dayEndTimeValidFlag;
					}
					if(new Integer(sTimeValue.substring(3,5))> 59){
						
						
						// Module 5 bug Report Change
						//Window.alert("Please Enter Valid Minutes (Allowed Till 59)");
						MessageConfirmationDialogBox startTimedialog2=new MessageConfirmationDialogBox(constants.warning());
						startTimedialog2.showConfirmationDialog(constants.warningTimeMinute());
						osceDayViewImpl.endTimeTextBox.setValue("");
						// Module 5 bug Report Change		
						
						dayEndTimeValidFlag=false;
						return dayEndTimeValidFlag;
					}
					return dayEndTimeValidFlag;
				}
				
				
				
				@Override
				public void saveOsceDayValue(OsceDayProxy osceDayProxy,
						boolean insertflag) {
					Log.info("Insert Value flag status :" +insertflag);
					if(insertflag==true){
						
						
						if(osceDayViewImpl.dateTextBox.getValue()==null){
							// Module 5 bug Report Change
							//Window.alert("Data must not empty");
							//MessageConfirmationDialogBox dateDialog=new MessageConfirmationDialogBox(constants.warning());
							//dateDialog.showConfirmationDialog(constants.warningDateEmpty());
							// Module 5 bug Report Change		
							
							return;
						}
						
						Date today = new Date();
						Date selecteddate=osceDayViewImpl.dateTextBox.getValue();
						
						if(selecteddate.before(today))
							{
							MessageConfirmationDialogBox dateDialog=new MessageConfirmationDialogBox(constants.warning());
								dateDialog.showConfirmationDialog(constants.dataWarning());
							return;
						}
						
						if(osceDayViewImpl.startTimeTextBox.getValue()==null){
							// Module 5 bug Report Change
							//Window.alert("Start Time must not Empty");
							MessageConfirmationDialogBox startTimeMessageDialog=new MessageConfirmationDialogBox(constants.warning());
							startTimeMessageDialog.showConfirmationDialog(constants.warningStartTime());
							// Module 5 bug Report Change		

							return;
						}
						if(osceDayViewImpl.startTimeTextBox.getValue() !=null ){
							
							if(! checkStarttimeValidation())
							return;
						}
										
						if(osceDayViewImpl.endTimeTextBox.getValue()==null){
							// Module 5 bug Report Change
							//Window.alert("EndDate Mus Not Empty");
							MessageConfirmationDialogBox endTimeMessageDialog=new MessageConfirmationDialogBox(constants.warning());
							endTimeMessageDialog.showConfirmationDialog(constants.warningEndTime());
							// Module 5 bug Report Change		
							return;
						}
						if(osceDayViewImpl.endTimeTextBox.getValue() !=null ){
							if(! checkEndTimeValidation())
								return;
						}
						
						
						
						
						//Window.alert("minute--"+hour+"--"+minute+"--"+totalMinute+"--"+osceProxy.getLunchBreak());
						
						
						OsceDayRequest osceDayReq = requests.osceDayRequest();
						osceDayProxy = osceDayReq.create(OsceDayProxy.class);
			
						osceDayProxy.setOsceDate(osceDayViewImpl.dateTextBox.getValue());
						
						osceDayProxy.setOsce(osceProxy);
						
						//DateFormat updatetdNewStartTimeDateFormat = new SimpleDateFormat("HH:mm");
						//DateFormat updatetdNewEndTimeDateFormat = new SimpleDateFormat("HH:mm");
						
						
						
						try{
							Log.info("In side new Day persist  try block");
							
							Date newDateWithStartTime=new Date();
							
							String hrs=osceDayViewImpl.startTimeTextBox.getValue().substring(0, 2);
							newDateWithStartTime.setHours(new Integer(hrs));
							
							String mts=osceDayViewImpl.startTimeTextBox.getValue().substring(3, 5);
							newDateWithStartTime.setMinutes(new Integer(mts));
							
							Date newDateWitnEndTime=new Date();
							hrs=osceDayViewImpl.endTimeTextBox.getValue().substring(0, 2);
							newDateWitnEndTime.setHours(new Integer(hrs));
							mts=osceDayViewImpl.endTimeTextBox.getValue().substring(3, 5);
							newDateWitnEndTime.setMinutes(new Integer(mts));
							
							//bug solve start
							
							int startHour=Integer.parseInt(DateTimeFormat.getFormat("HH").format(newDateWithStartTime));
							int startminute=Integer.parseInt(DateTimeFormat.getFormat("mm").format(newDateWithStartTime));
							//int lunchtime=Integer.parseInt(osceProxy.getLunchBreak());
							int totalStartMinute=(startHour*60)+startminute;
							
							int endHour=Integer.parseInt(DateTimeFormat.getFormat("HH").format(newDateWitnEndTime));
							int endminute=Integer.parseInt(DateTimeFormat.getFormat("mm").format(newDateWitnEndTime));
							//int lunchtime=Integer.parseInt(osceProxy.getLunchBreak());
							int totalendMinute=(endHour*60)+endminute;
							if(totalStartMinute>totalendMinute)
							{
								MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
								dialog.showConfirmationDialog(constants.warningStartEndTime());
								return;
							}
							
							//bug solve end
							//Date newDateWithStartTime =updatetdNewStartTimeDateFormat.parse(osceDayViewImpl.startTimeTextBox.getValue());
							//Date newDateWitnEndTime = updatetdNewEndTimeDateFormat.parse(osceDayViewImpl.endTimeTextBox.getValue());
							
							Date oldDate = osceDayViewImpl.dateTextBox.getValue();
												
							Log.info("Updated Start Time :" +newDateWithStartTime.getTime());
							
							Date newStartTimeDate = new Date(oldDate.getYear(), oldDate.getMonth(), oldDate.getDate(), newDateWithStartTime.getHours(),newDateWithStartTime.getMinutes());
							Date newEndTimeDate = new Date(oldDate.getYear(), oldDate.getMonth(), oldDate.getDate(), newDateWitnEndTime.getHours(),newDateWitnEndTime.getMinutes());
							
										
							Log.info("Value of newDate" + newStartTimeDate);
							Log.info("Value getSuccessfully");
							
							osceDayProxy.setTimeStart(newStartTimeDate);
							osceDayProxy.setTimeEnd(newEndTimeDate);
					
						}catch(Exception e){
							Log.info("Parsing exception During new Day persist");
						}
						
						// Highlight onViolation	
						Log.info(""+osceDayViewImpl.osceDayMap.size());
						osceDayReq.persist().using(osceDayProxy).fire(new OSCEReceiver<Void>(osceDayViewImpl.osceDayMap) {
						// E Highlight onViolation
							
							@Override
							public void onSuccess(Void response) {
								final MessageConfirmationDialogBox dialogbox=new MessageConfirmationDialogBox(constants.success());								
								dialogbox.showConfirmationDialog(constants.osceDaySuccess());
								Log.info("Osce Day Saved successfully");
								
								// Module 5 bug Report Change
								/* if osce day for any osce is not defined and click on generate button give warning osce day not defined
								 * After adding osce day the day successfully committed but when click on generated the old osce is considered
								 * so following request is fired
								 */
								requests.find(place.getProxyId()).with("osces","oscePostBlueprints","oscePostBlueprints.specialisation","oscePostBlueprints.roleTopic","osce_days","osce_days.osceSequences","osce_days.osceSequences.oscePosts","osce_days.osceSequences.oscePosts.oscePostBlueprint","osce_days.osceSequences.oscePosts.standardizedRole","osce_days.osceSequences.oscePosts.oscePostBlueprint.roleTopic","osce_days.osceSequences.oscePosts.oscePostBlueprint.specialisation","osce_days.osceSequences.oscePosts.oscePostBlueprint.postType","osce_days.osceSequences.courses","osce_days.osceSequences.courses.oscePostRooms","osce_days.osceSequences.courses.oscePostRooms.oscePost","osce_days.osceSequences.courses.oscePostRooms.oscePost.oscePostBlueprint","osce_days.osceSequences.courses.oscePostRooms.oscePost.standardizedRole","osce_days.osceSequences.courses.oscePostRooms.room","osce_days.osceSequences.courses.oscePostRooms.oscePost.oscePostBlueprint.roleTopic","osce_days.osceSequences.courses.oscePostRooms.oscePost.oscePostBlueprint.postType","osce_days.osceSequences.courses.oscePostRooms.oscePost.oscePostBlueprint.specialisation").fire(new OSCEReceiver<Object>() 
								{								
								@Override
								public void onSuccess(Object response) {
									if(response instanceof OsceProxy && response != null)										
										osceProxy=(OsceProxy)response;
								}
								});
								// E Module 5 bug Report Change		
								
							}
						});
					}
					else{
						
						Log.info("Inside to Update Day");
						Log.info(""+osceDayViewImpl.dateTextBox.getValue());
						if(osceDayViewImpl.dateTextBox.getValue()==null){
							// Module 5 bug Report Change
							//Window.alert("Data must not empty");
							MessageConfirmationDialogBox dateTextDialog=new MessageConfirmationDialogBox(constants.warning());
							dateTextDialog.showConfirmationDialog(constants.warningDateEmpty());
							// Module 5 bug Report Change		
							return;
						}
						
						Date today = new Date();
						//Date selecteddate=osceDayViewImpl.dateTextBox.getValue();
						
						if(osceDayViewImpl.dateTextBox.getValue().before(today))
							{
								MessageConfirmationDialogBox dateDialog=new MessageConfirmationDialogBox(constants.warning());
								dateDialog.showConfirmationDialog(constants.dataPastDateWarning());
									return;
							}
						if(osceDayViewImpl.startTimeTextBox.getValue()==null){
							// Module 5 bug Report Change
							//Window.alert("Start Time must not Empty");
							MessageConfirmationDialogBox startTimeTextDialog=new MessageConfirmationDialogBox(constants.warning());
							startTimeTextDialog.showConfirmationDialog(constants.warningStartTime());							
							// Module 5 bug Report Change		
							return;
						}
						if(osceDayViewImpl.startTimeTextBox.getValue() !=null ){

								if(! checkStarttimeValidation())
									return;
						}
						if(osceDayViewImpl.endTimeTextBox.getValue()==null){
							// Module 5 bug Report Change
							//Window.alert("EndDate Mus Not Empty");
							MessageConfirmationDialogBox endTimeTextDialog=new MessageConfirmationDialogBox(constants.warning());
							endTimeTextDialog.showConfirmationDialog(constants.warningEndTime());
							// Module 5 bug Report Change		
							
							return;
						}
						if(osceDayViewImpl.endTimeTextBox.getValue() !=null ){

							if(! checkEndTimeValidation())
								return;
						}
						
						
						OsceDayRequest osceDayReq = requests.osceDayRequest();
						osceDayProxy = osceDayReq.edit(osceDayProxy);
						
						osceDayProxy.setOsceDate(osceDayViewImpl.dateTextBox.getValue());
						
						
						//DateFormat updatetdNewStartTimeDateFormat = new SimpleDateFormat("HH:mm");
						//DateFormat updatetdNewEndTimeDateFormat = new SimpleDateFormat("HH:mm");
						
						
						
						try{
							Log.info("In side update Day persist try block");
							
							Date newDateWithStartTime=new Date();
							String hrs=osceDayViewImpl.startTimeTextBox.getValue().substring(0, 2);
							newDateWithStartTime.setHours(new Integer(hrs));
							String mts=osceDayViewImpl.startTimeTextBox.getValue().substring(3, 5);
							newDateWithStartTime.setMinutes(new Integer(mts));
							
							Date newDateWitnEndTime=new Date();
							 hrs=osceDayViewImpl.endTimeTextBox.getValue().substring(0, 2);
							newDateWitnEndTime.setHours(new Integer(hrs));
							 mts=osceDayViewImpl.endTimeTextBox.getValue().substring(3, 5);
							newDateWitnEndTime.setMinutes(new Integer(mts));
							//Date newDateWithStartTime=DateTimeFormat.getShortDateTimeFormat().parse((osceDayViewImpl.startTimeTextBox.getValue()));
							
							//Date newDateWitnEndTime=DateTimeFormat.getShortDateTimeFormat().parse((osceDayViewImpl.endTimeTextBox.getValue()));
							//Date newDateWithStartTime =updatetdNewStartTimeDateFormat.parse(osceDayViewImpl.startTimeTextBox.getValue());
							//Date newDateWitnEndTime = updatetdNewEndTimeDateFormat.parse(osceDayViewImpl.endTimeTextBox.getValue());
							
							Date oldDate = osceDayProxy.getOsceDate();
												
							Log.info("Updated Start Time :" +newDateWithStartTime.getTime());
							
							Date newStartTimeDate = new Date(oldDate.getYear(), oldDate.getMonth(), oldDate.getDate(), newDateWithStartTime.getHours(),newDateWithStartTime.getMinutes());
							Date newEndTimeDate = new Date(oldDate.getYear(), oldDate.getMonth(), oldDate.getDate(), newDateWitnEndTime.getHours(),newDateWitnEndTime.getMinutes());
							
							//oldDate.setTime(osceDayViewImpl.startTimeTextBox.getValue());
							//Date updatedstartTime = updatestartTimeDateFormat.parse(osceDayViewImpl.startTimeTextBox.getValue());
							//bug report start
							int startHour=Integer.parseInt(DateTimeFormat.getFormat("HH").format(newStartTimeDate));
							int startminute=Integer.parseInt(DateTimeFormat.getFormat("mm").format(newStartTimeDate));
							//int lunchtime=Integer.parseInt(osceProxy.getLunchBreak());
							int totalStartMinute=(startHour*60)+startminute;
							
							int endHour=Integer.parseInt(DateTimeFormat.getFormat("HH").format(newEndTimeDate));
							int endminute=Integer.parseInt(DateTimeFormat.getFormat("mm").format(newEndTimeDate));
							//int lunchtime=Integer.parseInt(osceProxy.getLunchBreak());
							int totalendMinute=(endHour*60)+endminute;
							if(totalStartMinute>totalendMinute)
							{
								MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
								dialog.showConfirmationDialog(constants.warningStartEndTime());
								return;
							}
							
							//bug report end
							Log.info("Value of newDate" + newStartTimeDate);
							Log.info("Value getSuccessfully");
							
							osceDayProxy.setTimeStart(newStartTimeDate);
							osceDayProxy.setTimeEnd(newEndTimeDate);
					
						}catch(Exception e){
							Log.info("Parsing exception During Day persist");
						}
						//osceDayProxy.setOsce(osceProxy);
						// Highlight onViolation	
						Log.info(""+osceDayViewImpl.osceDayMap.size());
						osceDayReq.persist().using(osceDayProxy).fire(new OSCEReceiver<Void>(osceDayViewImpl.osceDayMap) {
						// E: Highlight onViolation	
							@Override
							public void onSuccess(Void response) {
								final MessageConfirmationDialogBox dialogbox=new MessageConfirmationDialogBox(constants.success());
								
								dialogbox.showConfirmationDialog(constants.osceDaySuccess());
								Log.info("Osce Day Updated successfully");
							}
						}); 

						
					}
					
				}
				
				//sequence start
				// Module 5 bug Report Change
				/*private void addClickHandler(final SequenceOsceSubViewImpl sequenceOsceSubViewImpl)
				{
					sequenceOsceSubViewImpl.ok.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub
							
							sequenceOsceSubViewImpl.chaneNameOfSequence.setVisible(false);
							sequenceOsceSubViewImpl.ok.setVisible(false);
							sequenceOsceSubViewImpl.nameOfSequence.setText(sequenceOsceSubViewImpl.chaneNameOfSequence.getValue());
						//	sequenceOsceSubViewImpl.osceSequenceProxy.setLabel(sequenceOsceSubViewImpl[i].nameOfSequence.getText());
							System.out.println(sequenceOsceSubViewImpl.osceSequenceProxy.getLabel());
							sequenceOsceSubViewImpl.ok.setVisible(false);
							sequenceOsceSubViewImpl.chaneNameOfSequence.setVisible(false);
							
							OsceSequenceRequest osceSequenceRequest=requests.osceSequenceRequest();
							OsceSequenceProxy proxy=osceSequenceRequest.edit(sequenceOsceSubViewImpl.osceSequenceProxy);
							proxy.setLabel(sequenceOsceSubViewImpl.nameOfSequence.getText());
							sequenceOsceSubViewImpl.osceSequenceProxy=proxy;
							
							osceSequenceRequest.persist().using(proxy).fire(new Receiver<Void>() {
								@Override
								public void onSuccess(Void response) {	
									// TODO Auto-generated method stub
							//		System.out.println("INside success");
									Log.info("osce sequence updated  successfully with label--"+sequenceOsceSubViewImpl.osceSequenceProxy.getLabel());
								//	init2();
								
									}
							});
							
						}
					});
				}*/
				
				
				@Override
				public void saveSequenceLabel(final SequenceOsceSubViewImpl sequenceOsceSubViewImpl,final FocusableValueListBox<OsceSequences> chaneNameOfSequence,Label nameOfSequence,final PopupPanel sequenceOscePopup) 
				{
					//E Module 5 bug Report Change
					// TODO Auto-generated method stub
					Log.info("ok button click");
					
					
					// Module 5 bug Report Change
					//sequenceOsceSubViewImpl.nameOfSequence.setText(sequenceOsceSubViewImpl.chaneNameOfSequence.getValue());					
					// E Module 5 bug Report Change
					
				//	sequenceOsceSubViewImpl.osceSequenceProxy.setLabel(sequenceOsceSubViewImpl[i].nameOfSequence.getText());

					Log.info("Osce Sqquence Proxy: "+ sequenceOsceSubViewImpl.osceSequenceProxy.getId());
					System.out.println(sequenceOsceSubViewImpl.osceSequenceProxy.getLabel());
					// Module 5 bug Report Change					
					//sequenceOsceSubViewImpl.nameOfSequence.setText(chaneNameOfSequence.getListBox().getItemText(chaneNameOfSequence.getListBox().getSelectedIndex()));
					OsceSequenceRequest osceSequenceRequest=requests.osceSequenceRequest();
					sequenceOsceSubViewImpl.osceSequenceProxy=osceSequenceRequest.edit(sequenceOsceSubViewImpl.osceSequenceProxy);
					//sequenceOsceSubViewImpl.osceSequenceProxy.setLabel(sequenceOsceSubViewImpl.nameOfSequence.getText());
					sequenceOsceSubViewImpl.osceSequenceProxy.setLabel(chaneNameOfSequence.getListBox().getItemText(chaneNameOfSequence.getListBox().getSelectedIndex()));
					sequenceOsceSubViewImpl.osceSequenceProxy=sequenceOsceSubViewImpl.osceSequenceProxy;
					
					// Highlight onViolation
					Log.info("Map Size: "+sequenceOsceSubViewImpl.osceSequenceMap.size());
					osceSequenceRequest.persist().using(sequenceOsceSubViewImpl.osceSequenceProxy).fire(new OSCEReceiver<Void>(sequenceOsceSubViewImpl.osceSequenceMap) {
					// E Highlight onViolation
						@Override
						public void onSuccess(Void response) {	
							// TODO Auto-generated method stub
					//		System.out.println("INside success");
							Log.info("osce sequence updated  successfully with label--"+sequenceOsceSubViewImpl.osceSequenceProxy.getLabel());
							// Highlight onViolation
							sequenceOsceSubViewImpl.nameOfSequence.setText(getLabelString(chaneNameOfSequence.getListBox().getItemText(chaneNameOfSequence.getListBox().getSelectedIndex())));
							sequenceOsceSubViewImpl.nameOfSequence.setTitle(chaneNameOfSequence.getListBox().getItemText(chaneNameOfSequence.getListBox().getSelectedIndex()));
							// E Highlight onViolation
							sequenceOscePopup.hide();
						//	init2();
						
							}
					});
					
				}

				@Override
				public void saveOsceDataSplit(final SequenceOsceSubViewImpl sequenceOsceSubViewImpl) 
				{
					// TODO Auto-generated method stub
				//	final OsceSequenceProxy newOsce;

					// Module 5 bug Report Change

					requests.osceDayRequest().findOsceDay(sequenceOsceSubViewImpl.osceDayProxy.getId()).with("osceSequences").fire(new OSCEReceiver<OsceDayProxy>() {

						@Override
						public void onSuccess(OsceDayProxy response) {
							sequenceOsceSubViewImpl.osceDayProxy=response;							
						}
					});
					
					
					if(sequenceOsceSubViewImpl.osceDayProxy.getOsceSequences().size()>=2)
					{
						MessageConfirmationDialogBox splittingDialog=new MessageConfirmationDialogBox(constants.warning());
						splittingDialog.showConfirmationDialog(constants.warningSplitting());
						return;
					}
					// E Module 5 bug Report Change
					
					//Module 5 Bug Report Solution
					if(sequenceOsceSubViewImpl.osceDayProxy.getOsceSequences().get(0).getNumberRotation()==1)
					{
						Log.info("Osce Sequence Has Only One Number Of Rotation.");
						MessageConfirmationDialogBox splittingDialog=new MessageConfirmationDialogBox(constants.warning());
						splittingDialog.showConfirmationDialog(constants.warningSplitting());
						return;
					}
					//E Module 5 Bug Report Solution
					
					if(sequenceOsceSubViewImpl.osceDayProxy.getTimeEnd().getHours()>13)
					{
						
						// Module 5 Bug Test Change
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
						// E Module 5 Bug Test Change
						
					requests.osceSequenceRequestNonRoo().splitSequence(sequenceOsceSubViewImpl.osceSequenceProxy.getId()).fire(new OSCEReceiver<OsceSequenceProxy>() {

						@Override
						public void onSuccess(OsceSequenceProxy osceSequenceProxy) {
							// TODO Auto-generated method stub
							Log.info("spliting of sequence done:--"+osceSequenceProxy.getId());
							// Module 5 Bug Test Change
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							// E Module 5 Bug Test Change
							goTo(new CircuitDetailsPlace(osceProxy.stableId(),Operation.DETAILS));
						}
						@Override						
						public void onFailure(ServerFailure error) {
							// TODO Auto-generated method stub
							super.onFailure(error);
							// Module 5 Bug Test Change
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							// E Module 5 Bug Test Change
						}
						@Override
						public void onViolation(Set<Violation> errors) {
							// TODO Auto-generated method stub
							super.onViolation(errors);
							// Module 5 Bug Test Change
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							// E Module 5 Bug Test Change
						}
					});
					
					}
					else
					{
						Log.info("Spliting not allowed");
						MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
						dialogBox.showConfirmationDialog(constants.splittingNotAllowWarning());
					}
						
/*						
						// TODO Auto-generated method stub
						Log.info("end time:- "+sequenceOsceSubViewImpl.osceDayProxy.getTimeEnd().getHours());
						if(sequenceOsceSubViewImpl.osceDayProxy.getTimeEnd().getHours()>13)
						{
							Log.info("perform spliting");
							requests.osceSequenceRequest().findOsceSequence(sequenceOsceSubViewImpl.osceSequenceProxy.getId()).with("osceDay","oscePosts","courses").fire(new Receiver<OsceSequenceProxy>() {

								@Override
								public void onSuccess(OsceSequenceProxy response) {
									// TODO Auto-generated method stub
									final OsceSequenceProxy globalResponse=response;
									System.out.println("success fetch--"+response.getCourses().size()+"-"+response.getOscePosts().size());
								
							
							//,"osceSequences.osceDay","osceSequences.oscePosts","osceSequences.courses"
									
							OsceSequenceRequest sequenceRequest=requests.osceSequenceRequest();
							final OsceSequenceProxy sequenceProxy=sequenceRequest.create(OsceSequenceProxy.class);
							
							sequenceProxy.setLabel(response.getLabel());
							sequenceProxy.setNumberRotation(response.getNumberRotation());
					//		
							sequenceProxy.setOsceDay(response.getOsceDay());
							System.out.println("before set course");

							sequenceRequest.persist().using(sequenceProxy).fire(new Receiver<Void>() {

								@Override
								public void onSuccess(Void response) {
									// TODO Auto-generated method stub
									System.out.println("new sequence save successfully");
									
									//requests.osceSequenceRequestNonRoo().findMaxOsceSequence().fire(new Receiver<OsceSequenceProxy>() {
									requests.find(sequenceProxy.stableId()).fire(new Receiver<Object>() {

										@Override
										public void onSuccess(Object response) {
											// TODO Auto-generated method stub
											
											System.out.println("new size--"+globalResponse.getOscePosts().size());
											Iterator<OscePostProxy> postIterator=globalResponse.getOscePosts().iterator();
											while(postIterator.hasNext())
											{
												OscePostProxy post=postIterator.next();
												OscePostRequest postRequest=requests.oscePostRequest();
												
												OscePostProxy addpost=postRequest.create(OscePostProxy.class);
												addpost.setIsPossibleStart(post.getIsPossibleStart());
												addpost.setOscePostBlueprint(post.getOscePostBlueprint());
												addpost.setOscePostRooms(post.getOscePostRooms());
												addpost.setOsceSequence((OsceSequenceProxy)response);
												addpost.setStandardizedRole(post.getStandardizedRole());
												addpost.setSequenceNumber(post.getSequenceNumber());
												
												
												postRequest.persist().using(addpost).fire();
											}
											
											Iterator<CourseProxy> courseIterator=globalResponse.getCourses().iterator();
											while(courseIterator.hasNext())
											{
												CourseProxy course=courseIterator.next();
												CourseRequest courseRequest=requests.courseRequest();
												
												CourseProxy addCourse=courseRequest.create(CourseProxy.class);
												
												addCourse.setColor(course.getColor());
												addCourse.setOsce(course.getOsce());
												addCourse.setOscePostRooms(course.getOscePostRooms());
												addCourse.setOsceSequence((OsceSequenceProxy)response);
												
												
												
												courseRequest.persist().using(addCourse).fire();
											}
											
										}
									});
								}
								
								@Override
								public void onViolation(Set<Violation> errors) {
									Iterator<Violation> iter = errors.iterator();
									String message = "";
									while (iter.hasNext()) {
										message += iter.next().getMessage() + "<br>";
									}
									Log.warn(" in sequence -" + message);
								}
								
								public void onFailure(ServerFailure error) {
									Log.error("error--"+error.getMessage());

								}
							
							});
							
							}
							});
						}
						else
						{
							Log.info("spliting not allowed");
						}
						*/
					}
		
	// Module 5 changes {
				
				
				
				@Override
				public void osceGenratedButtonClicked() {
					
					// Module 5 bug Report Change					
					/*circuitOsceSubViewImpl.setClearAllBtn(true);
					circuitOsceSubViewImpl.clearAllBtn.removeStyleName("flexTable-Button-Disabled");
					circuitOsceSubViewImpl.setGenratedBtnStyle(false);
					circuitOsceSubViewImpl.setFixBtnStyle(true);
					circuitOsceSubViewImpl.fixedBtn.setText(constants.fixedButtonString());
					circuitOsceSubViewImpl.setClosedBtnStyle(false);*/
					
					
					
					if(osceProxy.getOsce_days().size()<=0)
					{
						MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
						dialogBox.showConfirmationDialog(constants.warningNoOsceDay());
						return;
					}
					// E Module 5 bug Report Change
					
					Log.info("Genrated Button Clicked Event at Circuit Details Activity");
					Log.info("OSceProxy is :" + osceProxy.getId());
					
					try
					{
						OsceProxyVerifier.verifyOsce(osceProxy);
					}
					catch(Exception e)
					{
						Log.info("message string--"+e.getMessage());
						MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
						//dialog.showConfirmationDialog(constants.headerValueMessage());
						dialog.showConfirmationDialog(constants.pleaseEnterWarning() + e.getMessage());
						return;
					}
				/*	
					if(osceProxy.getShortBreak()<=0 || osceProxy.getLongBreak()<=0|| osceProxy.getLunchBreak()<=0|| osceProxy.getMiddleBreak()<=0 ||osceProxy.getMaxNumberStudents()<=0 || osceProxy.getNumberRooms()<=0 || osceProxy.getShortBreakSimpatChange()<=0)
					{
						MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
						dialog.showConfirmationDialog(constants.warningHeaderValue());
						return;
					}
					findspecialisation=false;*/
					
					requests.oscePostBluePrintRequestNonRoo().countOscebluePrintValue(osceProxy.getId()).fire(new OSCEReceiver<Long>() {

						@Override
						public void onSuccess(Long response) {
							
							
							
							// TODO Auto-generated method stub
							Log.info("response size--"+response);
							if(response<=0)
							{
								
						
				/*		
									int startHour=Integer.parseInt(DateTimeFormat.getFormat("HH").format(tempDay.getTimeStart()));
									int startminute=Integer.parseInt(DateTimeFormat.getFormat("mm").format(tempDay.getTimeStart()));
									//int lunchtime=Integer.parseInt(osceProxy.getLunchBreak());
									int totalStartMinute=(startHour*60)+startminute;
									
									int endHour=Integer.parseInt(DateTimeFormat.getFormat("HH").format(tempDay.getTimeEnd()));
									int endminute=Integer.parseInt(DateTimeFormat.getFormat("mm").format(tempDay.getTimeEnd()));
									//int lunchtime=Integer.parseInt(osceProxy.getLunchBreak());
									int totalendMinute=(endHour*60)+endminute;
									if(totalStartMinute>totalendMinute)
									{
										MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
										dialog.showConfirmationDialog(constants.warningStartEndTime());
										return;
									}
									
									//Window.alert("minute--"+hour+"--"+minute+"--"+totalMinute+"--"+osceProxy.getLunchBreak());
				*/		
				
						
					if(osceProxy.getOsceStatus()==OsceStatus.OSCE_BLUEPRINT)
                                        {
					//requests.oscePostBluePrintRequestNonRoo().isBluePrintHasBreakAsLast(osceProxy.getId());
					int totalOscePosts=oSCENewSubViewImpl.getOscePostBluePrintSubViewImpl().getOscePostBluePrintSubViewImplHP().getWidgetCount();
					Log.info("Total OscePost is :"+totalOscePosts);
					OscePostViewImpl lastview=(OscePostViewImpl)oSCENewSubViewImpl.getOscePostBluePrintSubViewImpl().getOscePostBluePrintSubViewImplHP().getWidget(totalOscePosts-1);
					Log.info(lastview.getPostTypeLbl().getText());
					if(lastview.getPostTypeLbl().getText().equalsIgnoreCase(PostType.BREAK.name())){
						Log.info("Break Is at Last In BluePrint");
						final MessageConfirmationDialogBox messageDialog = new MessageConfirmationDialogBox(constants.warning());
						messageDialog.showYesNoDialog(constants.warningBreakIsAtEnd());
						
						messageDialog.getYesBtn().addClickHandler(new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) {
								Log.info("Yes Button Clicked");
								messageDialog.hide();
								
								// Module 5 Bug Test Change
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
								// E Module 5 Bug Test Change
								
								requests.osceRequestNonRoo().generateOsceScaffold(osceProxy.getId()).fire(new OSCEReceiver<Boolean>() {

									@Override
									public void onSuccess(Boolean response) {
										/*// Module 5 Bug Test Change
										requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
										// E Module 5 Bug Test Change
*/										
										
										if(response==true){
											Log.info("Schedule Genrated Successfully");
											// Module 5 bug Report Change
											circuitOsceSubViewImpl.setClearAllBtn(true);
											circuitOsceSubViewImpl.setGenratedBtnStyle(false);
											circuitOsceSubViewImpl.setFixBtnStyle(true);
											circuitOsceSubViewImpl.fixedBtn.setText(constants.fixedButtonString());
											circuitOsceSubViewImpl.fixedBtn.setIcon("pin-s");
											circuitOsceSubViewImpl.setClosedBtnStyle(false);
											// E Module 5 bug Report Change
										}
	                                    
										// Module 5 Bug Test Change
										requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
										// E Module 5 Bug Test Change
										
										// Module 5 bug Report Change
										setStatusGenerated(osceProxy);
										// E Module 5 bug Report Change
										
										
										
									}
									// Module 5 bug Report Change
									@Override
									public void onFailure(ServerFailure error)
									{
										// Module 5 Bug Test Change
										requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
										// E Module 5 Bug Test Change
										
										final MessageConfirmationDialogBox messageDialog = new MessageConfirmationDialogBox(constants.warning());
										messageDialog.showConfirmationDialog(constants.warningScheduleNotGenerated());
									}
									// E Module 5 bug Report Change
									
									@Override
									public void onViolation(java.util.Set<com.google.gwt.requestfactory.shared.Violation> errors) {
										// Module 5 Bug Test Change
										requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
										// E Module 5 Bug Test Change
									};
								});
								
							}
						});
						
						messageDialog.getNoBtnl().addClickHandler(new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) {
								messageDialog.hide();
								
							}
						});
					}
					else{
						
						// Module 5 Bug Test Change
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
						// E Module 5 Bug Test Change
					
						requests.osceRequestNonRoo().generateOsceScaffold(osceProxy.getId()).fire(new OSCEReceiver<Boolean>() {

							@Override
							public void onSuccess(Boolean response) {
								
								if(response==true){
									Log.info("Schedule Genrated Successfully");
									// Module 5 bug Report Change
									circuitOsceSubViewImpl.setClearAllBtn(true);
									circuitOsceSubViewImpl.setGenratedBtnStyle(false);
									circuitOsceSubViewImpl.setFixBtnStyle(true);
									circuitOsceSubViewImpl.fixedBtn.setText(constants.fixedButtonString());
									circuitOsceSubViewImpl.fixedBtn.setIcon("pin-s");
									circuitOsceSubViewImpl.setClosedBtnStyle(false);
									// E Module 5 bug Report Change
								}
									// Module 5 bug Report Change
								setStatusGenerated(osceProxy);
								// E Module 5 bug Report Change
								
								// Module 5 Bug Test Change
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								// E Module 5 Bug Test Change
							}
							// Module 5 bug Report Change
							@Override
							public void onFailure(ServerFailure error)
							{
								final MessageConfirmationDialogBox messageDialog = new MessageConfirmationDialogBox(constants.warning());
								messageDialog.showConfirmationDialog(constants.warningScaffoldNotGenerated());
								
								// Module 5 Bug Test Change
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								// E Module 5 Bug Test Change
							}
							// E Module 5 bug Report Change
							@Override
							public void onViolation(java.util.Set<com.google.gwt.requestfactory.shared.Violation> errors) {
								// Module 5 Bug Test Change
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								// E Module 5 Bug Test Change
							};

						});

					}
				}
							}
							else
							{
									MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
									dialog.showConfirmationDialog(constants.warningGenerate());
									return;

							}	
							
							
						}
				});
					

										

				}
				
				
				
				
				// Module 5 bug Report Change			
				public void setStatusGenerated(OsceProxy osceProxy)
				{
					
					// Module 5 Bug Test Change
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
					// E Module 5 Bug Test Change
							
					OsceRequest oscerequest=requests.osceRequest();
					osceProxy=oscerequest.edit(osceProxy);
					//Window.alert("new status--"+osceProxy.getShortBreak());
					final OsceProxy tempOsceProxy=osceProxy;
					osceProxy.setOsceStatus(OsceStatus.OSCE_GENRATED);
					oscerequest.persist().using(osceProxy).fire(new OSCEReceiver<Void>() 
					{
							@Override
							public void onSuccess(Void response) 
							{																		
									circuitOsceSubViewImpl.setClearAllBtn(true);
									circuitOsceSubViewImpl.setGenratedBtnStyle(false);
									circuitOsceSubViewImpl.setFixBtnStyle(true);
									circuitOsceSubViewImpl.fixedBtn.setText(constants.fixedButtonString());
									circuitOsceSubViewImpl.fixedBtn.setIcon("pin-s");
									circuitOsceSubViewImpl.setClosedBtnStyle(false);								
									goTo(new CircuitDetailsPlace(tempOsceProxy.stableId(),Operation.DETAILS));
									
									// Module 5 Bug Test Change
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									// E Module 5 Bug Test Change
							}
							
							// Module 5 bug Report Change
							@Override
							public void onFailure(ServerFailure error)
							{
								final MessageConfirmationDialogBox messageDialog = new MessageConfirmationDialogBox(constants.warning());
								messageDialog.showConfirmationDialog(constants.warningNotGenerated());
								
								// Module 5 Bug Test Change
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								// E Module 5 Bug Test Change
							}
							// E Module 5 bug Report Change
							
							@Override
							public void onViolation(Set<Violation> errors) {
								super.onViolation(errors);
								// Module 5 Bug Test Change
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								// E Module 5 Bug Test Change
							}
					});
						
				}
				// E Module 5 bug Report Change
				
				@Override
				public void fixedButtonClicked(final OsceProxy osceProxy) 
				{
					// Module 5 bug Report Change
					
					osceProxyforFixedStatus=osceProxy;
					// E Module 5 bug Report Change
					
					
					if(osceProxy.getOsceStatus()==OsceStatus.OSCE_CLOSED){
						final MessageConfirmationDialogBox message = new MessageConfirmationDialogBox(constants.warning());
						message.showYesNoDialog(constants.confirmationWhenStatusIsChangingFormClosedToFix());
						message.getYesBtn().addClickHandler(new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) {
								
								Log.info("Yes Button Clicked So user wants to go Ahead remove all role of osce");
								// Module 5 Bug Test Change
								message.hide();
								// E Module 5 Bug Test Change
								//message.showConfirmationDialog("You Can Moov Ahead");
								
								// Module 5 Bug Test Change
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
								// E Module 5 Bug Test Change
								
								// Module 5 bug Report Change
								requests.osceRequestNonRoo().removeassignment(osceProxy).fire(new OSCEReceiver<Boolean>() {

									@Override
									public void onSuccess(Boolean response) {
										// TODO Auto-generated method stub
										Log.info("Assignment Remove Successfully");
										
										// Module 5 Bug Test Change

										// Osce Status Changed to Fixed									
										OsceRequest osceRequest=requests.osceRequest();
										osceProxyforFixedStatus=osceRequest.edit(osceProxyforFixedStatus);
										osceProxyforFixedStatus.setOsceStatus(OsceStatus.OSCE_FIXED);
										osceRequest.persist().using(osceProxyforFixedStatus).fire(new OSCEReceiver<Object>() 
										{

											@Override
											public void onSuccess(Object response) 
											{
		                                        
												circuitOsceSubViewImpl.setFixBtnStyle(false);
							                    circuitOsceSubViewImpl.setClosedBtnStyle(true);
							                    circuitOsceSubViewImpl.setGenratedBtnStyle(true);
							                    circuitOsceSubViewImpl.setClearAllBtn(false);
												Log.info("Fixed Button Clicked Event At CircuitDetails Acticity");
												Log.info("OsceProxy is :"+ osceProxy.getId());
												Log.info("Osce Status is :" + osceProxy.getOsceStatus());
							
												circuitOsceSubViewImpl.setFixBtnStyle(true);
												circuitOsceSubViewImpl.fixedBtn.setText(constants.fixedButtonString());
												circuitOsceSubViewImpl.fixedBtn.setIcon("pin-s");
										
											
												goTo(new CircuitDetailsPlace(osceProxyforFixedStatus.stableId(),Operation.DETAILS));
											
												// Module 5 Bug Test Change
												requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
												// E Module 5 Bug Test Change
												
												return;
												
											}
											@Override
											public void onFailure(ServerFailure error)
											{
												// Module 5 Bug Test Change
												requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
												// E Module 5 Bug Test Change
												MessageConfirmationDialogBox statusUpdateDialog=new MessageConfirmationDialogBox(constants.warning());
												statusUpdateDialog.showConfirmationDialog(constants.warningNotFixed());
												
											}
											@Override
											public void onViolation(
													Set<Violation> errors) {
												super.onViolation(errors);
												// Module 5 Bug Test Change
												requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
												// E Module 5 Bug Test Change
												
											}
										});
										// E Module 5 Bug Test Change
									}
								});
								// 	E Module 5 bug Report Change
								
								//To DO
								//requests.osceRequestNonRoo().deleteAllPatentInRoleForOsce(osceProxy.getId());
							}
						});
						
						message.getNoBtnl().addClickHandler(new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) {
								
								message.hide();
							}
						});
					}
					else
					{
						// Module 5 Bug Test Change
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
						// E Module 5 Bug Test Change
						
						// Module 5 bug Report Change	
						OsceRequest osceRequesttemp=requests.osceRequest();
						osceProxyforFixedStatus=osceRequesttemp.edit(osceProxyforFixedStatus);
						osceProxyforFixedStatus.setOsceStatus(OsceStatus.OSCE_FIXED);
						osceRequesttemp.persist().using(osceProxyforFixedStatus).fire(new OSCEReceiver<Object>() {

							@Override
							public void onSuccess(Object response) 
							{
								circuitOsceSubViewImpl.setFixBtnStyle(false);
								circuitOsceSubViewImpl.setClosedBtnStyle(true);
								circuitOsceSubViewImpl.setGenratedBtnStyle(true);
								circuitOsceSubViewImpl.setClearAllBtn(false);
								Log.info("Fixed Button Clicked Event At CircuitDetails Acticity");
								goTo(new CircuitDetailsPlace(osceProxyforFixedStatus.stableId(),Operation.DETAILS));	
								// Module 5 Bug Test Change
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								// E Module 5 Bug Test Change
							}
							@Override
							public void onFailure(ServerFailure error)
							{
								// Module 5 Bug Test Change
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								// E Module 5 Bug Test Change
								MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
								dialog.showConfirmationDialog(constants.warningNotFixed());
								
							}
							@Override
							public void onViolation(Set<Violation> errors) {
								// TODO Auto-generated method stub
								super.onViolation(errors);
								// Module 5 Bug Test Change
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								// E Module 5 Bug Test Change
							}
						});
						
						// E Module 5 bug Report Change
						
					}
					
					
			
					
				}
				// Module 5 changes }

				@Override
				public void closeButtonClicked(OsceProxy proxy) {
					Log.info("Closed Button Clicked");
					
					// Module 5 bug Report Change
					/*circuitOsceSubViewImpl.clearAllBtn.setStyleName("flexTable-Button-Disabled");
					circuitOsceSubViewImpl.setClearAllBtn(false);
					circuitOsceSubViewImpl.setClosedBtnStyle(false);
					circuitOsceSubViewImpl.setGenratedBtnStyle(false);
					circuitOsceSubViewImpl.setFixBtnStyle(true);
					circuitOsceSubViewImpl.fixedBtn.setText(constants.reopenButtonString());*/
					// E Module 5 bug Report Change
					
					// Module 5 Bug Test Change
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
					// E Module 5 Bug Test Change
					
					requests.oscePostRoomRequestNonRoo().findListOfOscePostRoomByOsce(proxy.getId()).fire(new OSCEReceiver<List<OscePostRoomProxy>>() 
					{
						@Override
						public void onSuccess(List<OscePostRoomProxy> response) 
						{
							if(response.size()>0)
							{																	
								// Module 5 Bug Test Change
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								// E Module 5 Bug Test Change
								MessageConfirmationDialogBox roomNullWarning=new MessageConfirmationDialogBox(constants.warning());
								roomNullWarning.showConfirmationDialog(constants.roomNotNullWarning());								
							}
							else
							{
								// dk invoke SPAllocator [
								// Module 5 Bug Test Change
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								// E Module 5 Bug Test Change
								final MessageConfirmationDialogBox message = new MessageConfirmationDialogBox(constants.warning());
								message.showYesNoDialog(constants.confirmationWhenStatusIsChangingFormClosedToFix());
								message.getYesBtn().addClickHandler(new ClickHandler() {
									
									@Override
									public void onClick(ClickEvent event) {
										
										Log.info("Yes Button Clicked - user wants to go ahead and close the osce");
										// Module 5 and TTG Bug Changes
											message.hide();
										// E Module 5 and TTG Bug Changes
										
										//message.showConfirmationDialog("You Can Moov Ahead");
			                                                        // Module 5 bug Report Change
										//circuitOsceSubViewImpl.setFixBtnStyle(true);
										//circuitOsceSubViewImpl.fixedBtn.setText(constants.fixedButtonString());
											
											// Module 5 Bug Test Change
											requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
											// E Module 5 Bug Test Change
										
										requests.osceRequestNonRoo().generateAssignments(osceProxy.getId()).fire(new  OSCEReceiver<Boolean>() 
										{
											@Override
											public void onSuccess(Boolean response) 
											{
												
												
												// TODO Auto-generated method stub
												//message.showConfirmationDialog(constants.confirmationClosed());
												// Module 5 bug Report Change	
												OsceRequest osceRequesttemp=requests.osceRequest();
												osceProxy=osceRequesttemp.edit(osceProxy);
												osceProxy.setOsceStatus(OsceStatus.OSCE_CLOSED);
												osceRequesttemp.persist().using(osceProxy).fire(new OSCEReceiver<Object>() {

													@Override
													public void onSuccess(Object response) 
													{
														
														circuitOsceSubViewImpl.setFixBtnStyle(true);
														circuitOsceSubViewImpl.setClearAllBtn(false);
														circuitOsceSubViewImpl.setClosedBtnStyle(false);
														circuitOsceSubViewImpl.setGenratedBtnStyle(false);
														circuitOsceSubViewImpl.setFixBtnStyle(true);
														circuitOsceSubViewImpl.fixedBtn.setIcon("folder-open");
														circuitOsceSubViewImpl.fixedBtn.setText(constants.reopenButtonString());											
														Log.info("Fixed Button Clicked Event At CircuitDetails Acticity");
														//message.hide();
														// Module 5 Bug Test Change
														requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
														// E Module 5 Bug Test Change
														goTo(new CircuitDetailsPlace(osceProxy.stableId(),Operation.DETAILS));									
													}
													@Override
													public void onFailure(ServerFailure error)
													{
														// Module 5 Bug Test Change
														requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
														// E Module 5 Bug Test Change
														MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
														dialog.showConfirmationDialog(constants.warningNotClosed());
														message.hide();
													}
													public void onViolation(java.util.Set<Violation> errors) {
														// Module 5 Bug Test Change
														requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
														// E Module 5 Bug Test Change
													};
												});
												
												// E Module 5 bug Report Change
												
											}
											
											// Module 5 bug Report Change
											@Override
											public void onFailure(ServerFailure error)
											{
												// Module 5 Bug Test Change
												requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
												// E Module 5 Bug Test Change
												
												MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
												dialog.showConfirmationDialog(constants.warningNoAssignment());
												message.hide();
											}
										});
										// E Module 5 bug Report Change	
									}
								});
								
								message.getNoBtnl().addClickHandler(new ClickHandler() {
									
									@Override
									public void onClick(ClickEvent event) {
										// Module 5 Bug Test Change
										requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
										// E Module 5 Bug Test Change
										message.hide();
									}
								});
								// ] dk
							}
						}
					});
					
					
				}
				//  OSCE Day Assignment END
				
				// Module 5 bug Report Change
				@Override
				public void editOsceSequence(ClickEvent event,final SequenceOsceSubViewImpl sequenceOsceSubViewImpl) 
				{
					Log.info("Call EditOsceSequence...");
					final PopupPanel popUpEditSequence=new PopupPanel();
														
					VerticalPanel hp = new VerticalPanel();	
					HorizontalPanel lable=new HorizontalPanel();
					HorizontalPanel content=new HorizontalPanel();
					
					final FocusableValueListBox<OsceSequences> chaneNameOfSequence = new FocusableValueListBox<OsceSequences>(new EnumRenderer<OsceSequences>());
					chaneNameOfSequence.setAcceptableValues(Arrays.asList(OsceSequences.values()));
					Log.info("Get Sequences by string.." + OsceSequences.getSequenceByString(sequenceOsceSubViewImpl.nameOfSequence.getText().charAt(0)));
					chaneNameOfSequence.setValue(OsceSequences.getSequenceByString(sequenceOsceSubViewImpl.nameOfSequence.getText().charAt(0)));
					Button saveOsceSequence=new Button(constants.save());
					Button closeOsceSequence=new Button(constants.close());
					final Label name=new Label(constants.osceSequence());
					
					lable.add(name);
					lable.add(chaneNameOfSequence);					
					content.add(saveOsceSequence);
					content.add(closeOsceSequence);
					hp.setSpacing(15);
					hp.add(lable);
					hp.add(content);

					popUpEditSequence.setAutoHideEnabled(true);
					popUpEditSequence.setAnimationEnabled(true);
					popUpEditSequence.setPopupPosition(event.getClientX()-11,event.getClientY()-84);
					//hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
					/*lable.setCellHorizontalAlignment(name,HasHorizontalAlignment.ALIGN_LEFT);
					lable.setCellHorizontalAlignment(chaneNameOfSequence,HasHorizontalAlignment.ALIGN_RIGHT);
					content.setCellHorizontalAlignment(saveOsceSequence,HasHorizontalAlignment.ALIGN_LEFT);
					content.setCellHorizontalAlignment(closeOsceSequence,HasHorizontalAlignment.ALIGN_RIGHT);*/
					
					//popUpEditSequence.setSize("85px","120px");	
				
					hp.setSpacing(1);
					
					popUpEditSequence.addStyleName("osceSequencePopupPanelSize");
					name.addStyleName("osceSequencePopuplable");
					//chaneNameOfSequence.addStyleName("osceSequencePopupComboBox");
					chaneNameOfSequence.setWidth("55px");
					chaneNameOfSequence.setStyleName("osceSequencePopupComboBox");
					saveOsceSequence.setWidth("70px");
					//saveOsceSequence.addStyleName("osceSequencePopupSaveButton");
					saveOsceSequence.setStyleName("osceSequencePopupSaveButton");
					closeOsceSequence.setWidth("70px");
					//saveOsceSequence.addStyleName("osceSequencePopupSaveButton");
					closeOsceSequence.setStyleName("osceSequencePopupCloseButton");
					
					popUpEditSequence.add(hp);
					popUpEditSequence.show();
					
					saveOsceSequence.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) 
						{
							if((((chaneNameOfSequence.getListBox().getItemText(chaneNameOfSequence.getListBox().getSelectedIndex())).trim()).compareToIgnoreCase(""))==0)
							{
								MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
								dialog.showConfirmationDialog(constants.warningSelectSequence());
								return;
							}
							else
							{						
							saveSequenceLabel(sequenceOsceSubViewImpl,chaneNameOfSequence,name,popUpEditSequence);
							}							
						}
					});
					closeOsceSequence.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							popUpEditSequence.hide();
						}
					});
				}
				// E Module 5 bug Report Change

				//Module 5 Bug Report Solution

				@Override
				public void schedulePostpone(final OsceDayProxy osceDayProxy) 
				{
					// Module 5 Bug Test Change
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
					// E Module 5 Bug Test Change
					
					Log.info("Call schedulePostpone for Osce Day: " + osceDayProxy.getId());										
					requests.osceDayRequestNooRoo().schedulePostpone(osceDayProxy).fire(new OSCEReceiver<String>() 
					{
						@Override
						public void onSuccess(String response) 
						{
							Log.info("Schedule Postpone Successfully.");
							Log.info("Response: " + response);
							if(response.compareToIgnoreCase("Rotation1")==0)
							{
								// Module 5 Bug Test Change
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								// E Module 5 Bug Test Change
								
								MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
								dialog.showConfirmationDialog(constants.warningRotationUnassigned());
								return;
							}
							else if(response.compareToIgnoreCase("UpdateSuccessful")==0)
							{
								// Module 5 Bug Test Change
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								// E Module 5 Bug Test Change
								
								final MessageConfirmationDialogBox updateDialog=new MessageConfirmationDialogBox(constants.success());
								//updateDialog.showConfirmationDialog(constants.confirmationAssigned());
								updateDialog.getNoBtnl().addClickHandler(new ClickHandler() {
									
									@Override
									public void onClick(ClickEvent event) 
									{
										updateDialog.hide();
									}
								});
								refreshCircuitDetailsPlace(osceDayProxy);
								return;
							}
							else if(response.compareToIgnoreCase("CreateSuccessful")==0)
							{
								// Module 5 Bug Test Change
								System.out.println("Event Stop.");
								requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
								// E Module 5 Bug Test Change
								
								final MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.success());
								//dialog.showConfirmationDialog(constants.confirmationDayCreated());
								dialog.getNoBtnl().addClickHandler(new ClickHandler() {
									
									@Override
									public void onClick(ClickEvent event) 
									{
										dialog.hide();
									}
								});
								refreshCircuitDetailsPlace(osceDayProxy);
								return;
							}
						}
						@Override
						public void onFailure(ServerFailure error)
						{
							// Module 5 Bug Test Change
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							// E Module 5 Bug Test Change
							MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
							dialog.showConfirmationDialog(constants.warningScheduleNotPostponed());
						}
						
						@Override
						public void onViolation(Set<Violation> errors) {
							// TODO Auto-generated method stub
							super.onViolation(errors);
							// Module 5 Bug Test Change
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							// E Module 5 Bug Test Change
						}
					});										
				}
				
				@Override
				public void scheduleEarlier(final OsceDayProxy osceDayProxy) 
				{
					Log.info("Call scheduleEarlier for Osce Day: " + osceDayProxy.getId());	
					// Find Osce From Osce Day to Refresh Osce If Day Removed				
					// Module 5 Bug Test Change
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
					// E Module 5 Bug Test Change
					
					requests.osceDayRequestNooRoo().findOsceIdByOsceDayId(osceDayProxy.getId()).fire(new OSCEReceiver<Long>() 
					{
						@Override
						public void onSuccess(Long response) 
						{
							Log.info("Osce: " + response);
							
							requests.osceRequest().findOsce(response).fire(new OSCEReceiver<OsceProxy>() 
							{
								@Override
								public void onSuccess(OsceProxy osceProxyResponse) 
								{
									Log.info("Osce Proxy: " + osceProxyResponse.getId());
									osceToRefreshPlace=osceProxyResponse;
									
									
									requests.osceDayRequestNooRoo().scheduleEarlier(osceDayProxy).fire(new OSCEReceiver<String>() 
											{
												@Override
												public void onSuccess(String response) 
												{
													Log.info("Schedule Earlier Successfully.");
													Log.info("Response: " + response);
													if(response.compareToIgnoreCase("FirstDay")==0)
													{
														// Module 5 Bug Test Change
														requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
														// E Module 5 Bug Test Change
														MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
														dialog.showConfirmationDialog(constants.warningNoPreviousDay());
														return;
													}
													else if(response.compareToIgnoreCase("Rotation1")==0)
													{
														// Module 5 Bug Test Change
														requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
														// E Module 5 Bug Test Change
														MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
														dialog.showConfirmationDialog(constants.warningRotationUnassignedPrevious());
														return;
													}
													else if(response.compareToIgnoreCase("SuccessfullyPreponeWithDelete")==0)
													{
														// Module 5 Bug Test Change
														requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
														// E Module 5 Bug Test Change
														final MessageConfirmationDialogBox previousAssignDialog=new MessageConfirmationDialogBox(constants.success());
														//previousAssignDialog.showConfirmationDialog(constants.confirmationAssignedPrevious());
														previousAssignDialog.getNoBtnl().addClickHandler(new ClickHandler() {
															
															@Override
															public void onClick(ClickEvent event) 
															{
																previousAssignDialog.hide();
															}
														});
														if(osceToRefreshPlace!=null)
														{
															goTo(new CircuitDetailsPlace(osceToRefreshPlace.stableId(),Operation.DETAILS));
														}
														
														return;
													}
													else if(response.compareToIgnoreCase("SuccessfulPrepond")==0)
													{
														// Module 5 Bug Test Change
														requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
														// E Module 5 Bug Test Change
														final MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.success());
														//dialog.showConfirmationDialog(constants.confirmationAssigned());
														dialog.getNoBtnl().addClickHandler(new ClickHandler() {
															
															@Override
															public void onClick(ClickEvent event) 
															{
																dialog.hide();
															}
														});
														if(osceToRefreshPlace!=null)
														{
															goTo(new CircuitDetailsPlace(osceToRefreshPlace.stableId(),Operation.DETAILS));
														}
														return;
													}
												} 
												@Override
												public void onFailure(ServerFailure error)
												{
													// Module 5 Bug Test Change
													requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
													// E Module 5 Bug Test Change
													MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
													dialog.showConfirmationDialog(constants.warningScheduleNotPostponed());
												}
												
												@Override
												public void onViolation(
														Set<Violation> errors) {
													// TODO Auto-generated method stub
													super.onViolation(errors);
													// Module 5 Bug Test Change
													requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
													// E Module 5 Bug Test Change
												}
											});
									
								}
								
								@Override
								public void onFailure(ServerFailure error) {
									// TODO Auto-generated method stub
									super.onFailure(error);
									// Module 5 Bug Test Change
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									// E Module 5 Bug Test Change
								}
								
								public void onViolation(java.util.Set<Violation> errors) {
									// Module 5 Bug Test Change
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									// E Module 5 Bug Test Change
								}
							});
						}
						
						@Override
						public void onFailure(ServerFailure error) {
							// TODO Auto-generated method stub
							super.onFailure(error);
							// Module 5 Bug Test Change
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							// E Module 5 Bug Test Change
						}
						
						@Override
						public void onViolation(Set<Violation> errors) {
							// TODO Auto-generated method stub
							super.onViolation(errors);
							// Module 5 Bug Test Change
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							// E Module 5 Bug Test Change
						}
					});
					
				}
				
				private void refreshCircuitDetailsPlace(OsceDayProxy osceDayProxy) 
				{
					Log.info("Call refreshCircuitDetailsPlace");
					Log.info("Osce Day: " + osceDayProxy.getId());
					
					// Module 5 Bug Test Change
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
					// E Module 5 Bug Test Change
                   				
					requests.osceDayRequestNooRoo().findOsceIdByOsceDayId(osceDayProxy.getId()).fire(new OSCEReceiver<Long>() 
					{
						@Override
						public void onSuccess(Long response) 
						{
							Log.info("Osce: " + response);
							requests.osceRequest().findOsce(response).fire(new OSCEReceiver<OsceProxy>() 
							{
								@Override
								public void onSuccess(OsceProxy osceProxyResponse) 
								{
									Log.info("Osce Proxy: " + osceProxyResponse.getId());
									// Module 5 Bug Test Change
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									// E Module 5 Bug Test Change
									goTo(new CircuitDetailsPlace(osceProxyResponse.stableId(),Operation.DETAILS));
								}
								
								@Override
								public void onFailure(ServerFailure error) {
									// TODO Auto-generated method stub
									
									super.onFailure(error);
									// Module 5 Bug Test Change
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									// E Module 5 Bug Test Change
								}
								
								@Override
								public void onViolation(Set<Violation> errors) {
									// TODO Auto-generated method stub
									super.onViolation(errors);
									// Module 5 Bug Test Change
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									// E Module 5 Bug Test Change
								}
							});
						}
						
						@Override
						public void onFailure(ServerFailure error) {
							// TODO Auto-generated method stub
							super.onFailure(error);
							// Module 5 Bug Test Change
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							// E Module 5 Bug Test Change
						}
						
						@Override
						public void onViolation(Set<Violation> errors) {
							// TODO Auto-generated method stub
							super.onViolation(errors);
							// Module 5 Bug Test Change
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							// E Module 5 Bug Test Change
						}
						
					});
				}
				//E Module 5 Bug Report Solution

				// Module 5 and TTG Bug Changes
				@Override
				public void roomEditClicked(final OscePostSubView view,final int left,final int top) 
				{
					Log.info("Room Edit");
					requests.roomRequest().findAllRooms().fire(new OSCEReceiver<List<RoomProxy>>() 
					{
							@Override
							public void onSuccess(List<RoomProxy> response) 
							{
								Log.info("Total Rooms: "+response.size());
								ArrayList list=new ArrayList();
								//list.add( (EntityProxy)response.getStandardizedRoles().get(0));
								list.addAll(response);
								
								((OscePostSubViewImpl)view).createOptionPopup();
								((OscePostSubViewImpl)view).popupView.setDelegate(activity);
								((OscePostSubViewImpl)view).popupView.setOscePostSubView(view);
								((OscePostSubViewImpl)view).popupView.setProxy(view.getOscePostProxy());								
								((OscePostSubViewImpl)view).showPopUpViewForRoom(left,top);
					
								//Issue # 122 : Replace pull down with autocomplete.
								DefaultSuggestOracle<Object> suggestOracle1 = ((DefaultSuggestOracle<Object>) (((OscePostSubViewImpl)view).popupView.getNewListBox().getSuggestOracle()));
								suggestOracle1.setPossiblilities(list);
								((OscePostSubViewImpl)view).popupView.getNewListBox().setSuggestOracle(suggestOracle1);
								
								//((OscePostSubViewImpl)view).popupView.getNewListBox().setRenderer(new StandardizedRoleProxyRenderer());
								((OscePostSubViewImpl)view).popupView.getNewListBox().setRenderer(new Renderer<Object>() 
								{

									@Override
									public String render(Object object) {
										// TODO Auto-generated method stub		
										if(object==null)
										{
											return null;
										}
										else
										{
											return ((RoomProxy)object).getRoomNumber();
										}
									}
									@Override
									public void render(Object object,Appendable appendable) throws IOException 
									{
											// TODO Auto-generated method stub
									}
								});																
							}
					});	
					//((OscePostSubViewImpl)view).popupView.getNewListBox().setSelected();
					
				}

				@Override
				public void saveOscePostRoom(final OscePostSubViewImpl oscePostSubViewImpl,final ListBoxPopupView view) 
				{
					Log.info("Save Osce Post Room Clicked");
					Log.info("Osce Post Proxy: " + oscePostSubViewImpl.getOscePostProxy().getId());
					Log.info("Course Proxy: " + oscePostSubViewImpl.getCourseProxy().getId());			
					
					final RoomProxy roomProxy=(RoomProxy)view.getNewListBox().getSelected();
					Log.info("Room Proxy: " + roomProxy.getId());
					
					((ListBoxPopupViewImpl)view).hide();
					
					System.out.println("SEQUENCE : " + oscePostSubViewImpl.getOscePostProxy().getOsceSequence());
					
					//spec
					
					requests.oscePostRequest().findOscePost(oscePostSubViewImpl.getOscePostProxy().getId()).with("osceSequence").fire(new OSCEReceiver<OscePostProxy>() {

						@Override
						public void onSuccess(OscePostProxy oscePostResult) {
							
									requests.oscePostRoomRequestNonRoo().findOscePostRoomByRoom(oscePostResult.getOsceSequence().getId(),roomProxy.getId()).fire(new OSCEReceiver<Integer>() {

										@Override
										public void onSuccess(Integer roomCount) {
											if (roomCount.equals(0))
											{
												requests.oscePostRoomRequestNonRoo().findOscePostRoomByOscePostAndCourse(oscePostSubViewImpl.getCourseProxy(), oscePostSubViewImpl.getOscePostProxy()).with("room").fire(new OSCEReceiver<OscePostRoomProxy>() {

													@Override
													public void onSuccess(OscePostRoomProxy response) 
													{
														if (response != null)
														{
															Log.info("Osce Post Room Proxy: " + response.getId());
															OscePostRoomRequest oscePostRoomRequest=requests.oscePostRoomRequest();
															response=oscePostRoomRequest.edit(response);
															response.setRoom(roomProxy);
															oscePostRoomRequest.persist().using(response).fire(new OSCEReceiver<Void>() {

																@Override
																public void onSuccess(Void response) 
																{
																	view.getOscePostSubView().getRoomLbl().setText(getLabelString(util.getEmptyIfNull(roomProxy.getRoomNumber())));
																	view.getOscePostSubView().getRoomLbl().setTitle(util.getEmptyIfNull(roomProxy.getRoomNumber()));
																	Log.info("Success saveOscePostRoom ");
																	/*MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.success());
																	dialog.showConfirmationDialog("Room Assign To Post Successfully.");*/
																}
															});
														}																											
													}
												});
											}
											else
											{
												Log.info("Room Already Exist");
												MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.error());
												dialog.showConfirmationDialog(constants.roomAssignedWarning());
											}
										}
									});
							}
					});
					
		/*			OscePostProxy oscePostProxy=(OscePostProxy)view.getProxy();
				//	final StandardizedRoleProxy standardizedRoleProxy=(StandardizedRoleProxy)view.getListBox().getValue();
					
					//Issue # 122 : Replace pull down with autocomplete.
					//final StandardizedRoleProxy standardizedRoleProxy=(StandardizedRoleProxy)view.getListBox().getValue();
					final StandardizedRoleProxy standardizedRoleProxy=(StandardizedRoleProxy)view.getNewListBox().getSelected();
					//Issue # 122 : Replace pull down with autocomplete.
					OscePostRequest oscePostRequest=requests.oscePostRequest();
					oscePostProxy=oscePostRequest.edit(oscePostProxy);
					oscePostProxy.setStandardizedRole(standardizedRoleProxy);
					oscePostRequest.persist().using(oscePostProxy).fire(new OSCEReceiver<Void>() {

						@Override
						public void onSuccess(Void response) {
							Log.info("Success saveStandardizedRole ");

							
							((ListBoxPopupViewImpl)view).hide();
						}
					});*/
					
				}
				// E Module 5 and TTG Bug Changes

				//spec issue sol

				@Override
				public void shiftLucnkBreakPrevClicked(final OsceDayProxy osceDayProxy, final OsceDayViewImpl osceDayViewImplTemp) {
					
					try
					{
						if (osceDayProxy.getLunchBreakAfterRotation() != null || osceDayProxy.getLunchBreakAfterRotation() != 0)
						{
							// Module 5 Bug Test Change
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
							// E Module 5 Bug Test Change
							
							requests.osceDayRequestNooRoo().updateLunchBreak(osceDayProxy.getId(), (osceDayProxy.getLunchBreakAfterRotation()-1)).fire(new OSCEReceiver<Boolean>() {

								@Override
								public void onSuccess(Boolean response1) {
									
									requests.osceDayRequest().findOsceDay(osceDayProxy.getId()).fire(new OSCEReceiver<OsceDayProxy>() {

										@Override
										public void onSuccess(
												OsceDayProxy osceDayProxyTemp) {
												osceDayViewImplTemp.getLunchBreakStartValueLabel().setText(DateTimeFormat.getFormat("HH:mm").format(osceDayProxyTemp.getLunchBreakStart()).substring(0,5));
											
												osceDayViewImplTemp.setOsceDayProxy(osceDayProxyTemp);
												
												requests.osceDayRequestNooRoo().updateRotation(osceDayProxy.getId(), -1).fire(new OSCEReceiver<Boolean>() {

													@Override
													public void onSuccess(
															Boolean response) {																
														Log.info("Done Successfully");			
														
														if (osceDayViewImplTemp.getSequenceOsceSubViewImplList().size() == 2)
														{
															SequenceOsceSubViewImpl firstSequenceOsce = osceDayViewImplTemp.getSequenceOsceSubViewImplList().get(0);
															SequenceOsceSubViewImpl secondSequenceOsce = osceDayViewImplTemp.getSequenceOsceSubViewImplList().get(1);
															
															int firstRotation = Integer.parseInt(firstSequenceOsce.getSequenceRotationLable().getText()) + 1;
															int secondRotation = Integer.parseInt(secondSequenceOsce.getSequenceRotationLable().getText()) - 1;
															firstSequenceOsce.getSequenceRotationLable().setText(String.valueOf(firstRotation));
															secondSequenceOsce.getSequenceRotationLable().setText(String.valueOf(secondRotation));
															
															
														}
														// Module 5 Bug Test Change
														requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
														// E Module 5 Bug Test Change
													}
													
													@Override
													public void onFailure(
															ServerFailure error) {
														// TODO Auto-generated method stub
														super.onFailure(error);
														// Module 5 Bug Test Change
														requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
														// E Module 5 Bug Test Change
													}
													
													@Override
													public void onViolation(
															Set<Violation> errors) {
														// TODO Auto-generated method stub
														super.onViolation(errors);
														// Module 5 Bug Test Change
														requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
														// E Module 5 Bug Test Change
													}
												});
										}
										
										@Override
										public void onFailure(
												ServerFailure error) {
											// TODO Auto-generated method stub
											super.onFailure(error);
											// Module 5 Bug Test Change
											requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
											// E Module 5 Bug Test Change
										}
										
										@Override
										public void onViolation(
												Set<Violation> errors) {
											// TODO Auto-generated method stub
											super.onViolation(errors);
											// Module 5 Bug Test Change
											requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
											// E Module 5 Bug Test Change
										}
									});
								}
								
								@Override
								public void onFailure(ServerFailure error) {
									// TODO Auto-generated method stub
									super.onFailure(error);
									// Module 5 Bug Test Change
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									// E Module 5 Bug Test Change
								}
								
								@Override
								public void onViolation(Set<Violation> errors) {
									// TODO Auto-generated method stub
									super.onViolation(errors);
									// Module 5 Bug Test Change
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									// E Module 5 Bug Test Change
								}
							});
						}
						else
						{	
							// Module 5 Bug Test Change
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
							// E Module 5 Bug Test Change
							
							requests.osceSequenceRequestNonRoo().findOsceSequenceByOsceDayId(osceDayProxy.getId()).fire(new OSCEReceiver<List<OsceSequenceProxy>>() {
								
								@Override
								public void onSuccess(List<OsceSequenceProxy> response) {
									
									if (response.size() > 0)
									{
										OsceSequenceProxy osceSequenceProxy = response.get(0);
										
										requests.osceDayRequestNooRoo().updateLunchBreak(osceDayProxy.getId(), ((osceSequenceProxy.getNumberRotation()/2)-1)).fire(new OSCEReceiver<Boolean>() {

											@Override
											public void onSuccess(Boolean response1) {
												Log.info("shiftLucnkBreakPrevClicked Response : " + response1);
												
												requests.osceDayRequest().findOsceDay(osceDayProxy.getId()).fire(new OSCEReceiver<OsceDayProxy>() {

													@Override
													public void onSuccess(
															OsceDayProxy osceDayProxyTemp) {
															osceDayViewImplTemp.getLunchBreakStartValueLabel().setText(DateTimeFormat.getFormat("HH:mm").format(osceDayProxyTemp.getLunchBreakStart()).substring(0,5));
														
															osceDayViewImplTemp.setOsceDayProxy(osceDayProxyTemp);
															
															requests.osceDayRequestNooRoo().updateRotation(osceDayProxy.getId(), -1).fire(new OSCEReceiver<Boolean>() {

																@Override
																public void onSuccess(
																		Boolean response) {																
																	Log.info("Done Successfully");
																	
																	if (osceDayViewImplTemp.getSequenceOsceSubViewImplList().size() == 2)
																	{
																		SequenceOsceSubViewImpl firstSequenceOsce = osceDayViewImplTemp.getSequenceOsceSubViewImplList().get(0);
																		SequenceOsceSubViewImpl secondSequenceOsce = osceDayViewImplTemp.getSequenceOsceSubViewImplList().get(1);
																		
																		int firstRotation = Integer.parseInt(firstSequenceOsce.getSequenceRotationLable().getText()) + 1;
																		int secondRotation = Integer.parseInt(secondSequenceOsce.getSequenceRotationLable().getText()) - 1;
																		firstSequenceOsce.getSequenceRotationLable().setText(String.valueOf(firstRotation));
																		secondSequenceOsce.getSequenceRotationLable().setText(String.valueOf(secondRotation));
																	}
																	// Module 5 Bug Test Change
																	requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
																	// E Module 5 Bug Test Change
																}
																
																public void onFailure(ServerFailure error) {
																	// Module 5 Bug Test Change
																	requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
																	// E Module 5 Bug Test Change
																};
																
																@Override
																public void onViolation(
																		Set<Violation> errors) {
																	// TODO Auto-generated method stub
																	super.onViolation(errors);
																	// Module 5 Bug Test Change
																	requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
																	// E Module 5 Bug Test Change
																}
															});
													}
													
													public void onFailure(ServerFailure error) {
														// Module 5 Bug Test Change
														requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
														// E Module 5 Bug Test Change
													}
													
													@Override
													public void onViolation(
															Set<Violation> errors) {
														// TODO Auto-generated method stub
														super.onViolation(errors);
														// Module 5 Bug Test Change
														requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
														// E Module 5 Bug Test Change
													}
												});
											}
										});
									}				
								}
								
								@Override
								public void onFailure(ServerFailure error) {
									// TODO Auto-generated method stub
									super.onFailure(error);
									// Module 5 Bug Test Change
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									// E Module 5 Bug Test Change
								}
								
								@Override
								public void onViolation(Set<Violation> errors) {
									// TODO Auto-generated method stub
									super.onViolation(errors);
									// Module 5 Bug Test Change
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									// E Module 5 Bug Test Change
								}
							});
						}
						

					}
					catch(Exception e)
					{
						System.out.println(e.getMessage());
					}
										
				}

				@Override
				public void shiftLucnkBreakNextClicked(final OsceDayProxy osceDayProxy, final OsceDayViewImpl osceDayViewImplTemp) {
					
					try
					{	
						if (osceDayProxy.getLunchBreakAfterRotation() == null || osceDayProxy.getLunchBreakAfterRotation() == 0)
						{	
							// Module 5 Bug Test Change
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
							// E Module 5 Bug Test Change
							requests.osceSequenceRequestNonRoo().findOsceSequenceByOsceDayId(osceDayProxy.getId()).fire(new OSCEReceiver<List<OsceSequenceProxy>>() {
								
								@Override
								public void onSuccess(List<OsceSequenceProxy> response) {
									
									if (response.size() > 0)
									{
										OsceSequenceProxy osceSequenceProxy = response.get(0);
										
										requests.osceDayRequestNooRoo().updateLunchBreak(osceDayProxy.getId(), ((osceSequenceProxy.getNumberRotation()/2)+1)).fire(new OSCEReceiver<Boolean>() {

											@Override
											public void onSuccess(Boolean response1) {
												
												requests.osceDayRequest().findOsceDay(osceDayProxy.getId()).fire(new OSCEReceiver<OsceDayProxy>() {

													@Override
													public void onSuccess(
															OsceDayProxy osceDayProxyTemp) {
															osceDayViewImplTemp.getLunchBreakStartValueLabel().setText(DateTimeFormat.getFormat("HH:mm").format(osceDayProxyTemp.getLunchBreakStart()).substring(0,5));
														
															osceDayViewImplTemp.setOsceDayProxy(osceDayProxyTemp);
															
															requests.osceDayRequestNooRoo().updateRotation(osceDayProxy.getId(), +1).fire(new OSCEReceiver<Boolean>() {

																@Override
																public void onSuccess(
																		Boolean response) {
																	
																	Log.info("Done Successfully");
																	
																	if (osceDayViewImplTemp.getSequenceOsceSubViewImplList().size() == 2)
																	{
																		SequenceOsceSubViewImpl firstSequenceOsce = osceDayViewImplTemp.getSequenceOsceSubViewImplList().get(0);
																		SequenceOsceSubViewImpl secondSequenceOsce = osceDayViewImplTemp.getSequenceOsceSubViewImplList().get(1);
																		
																		int firstRotation = Integer.parseInt(firstSequenceOsce.getSequenceRotationLable().getText()) - 1;
																		int secondRotation = Integer.parseInt(secondSequenceOsce.getSequenceRotationLable().getText()) + 1;
																		firstSequenceOsce.getSequenceRotationLable().setText(String.valueOf(firstRotation));
																		secondSequenceOsce.getSequenceRotationLable().setText(String.valueOf(secondRotation));
																	}
																	// Module 5 Bug Test Change
																	requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
																	// E Module 5 Bug Test Change
																}
																
																@Override
																public void onFailure(
																		ServerFailure error) {
																	// TODO Auto-generated method stub
																	super.onFailure(error);
																	// Module 5 Bug Test Change
																	requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
																	// E Module 5 Bug Test Change
																}
																
																@Override
																public void onViolation(
																		Set<Violation> errors) {
																	// TODO Auto-generated method stub
																	super.onViolation(errors);
																	// Module 5 Bug Test Change
																	requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
																	// E Module 5 Bug Test Change
																}
															});
													}
													
													@Override
													public void onFailure(
															ServerFailure error) {
														// TODO Auto-generated method stub
														super.onFailure(error);
														// Module 5 Bug Test Change
														requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
														// E Module 5 Bug Test Change
													}
													@Override
													public void onViolation(
															Set<Violation> errors) {
														// TODO Auto-generated method stub
														super.onViolation(errors);
														// Module 5 Bug Test Change
														requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
														// E Module 5 Bug Test Change
													}
												});
											}
										});

									}								
								}
							});
						}
						else
						{	
							// Module 5 Bug Test Change
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
							// E Module 5 Bug Test Change
							
							requests.osceDayRequestNooRoo().updateLunchBreak(osceDayProxy.getId(), (osceDayProxy.getLunchBreakAfterRotation()+1)).fire(new OSCEReceiver<Boolean>() {

								@Override
								public void onSuccess(Boolean response1) {
								
									requests.osceDayRequest().findOsceDay(osceDayProxy.getId()).fire(new OSCEReceiver<OsceDayProxy>() {

										@Override
										public void onSuccess(
												OsceDayProxy osceDayProxyTemp) {
												osceDayViewImplTemp.getLunchBreakStartValueLabel().setText(DateTimeFormat.getFormat("HH:mm").format(osceDayProxyTemp.getLunchBreakStart()).substring(0,5));
											
												osceDayViewImplTemp.setOsceDayProxy(osceDayProxyTemp);
												
												requests.osceDayRequestNooRoo().updateRotation(osceDayProxy.getId(), +1).fire(new OSCEReceiver<Boolean>() {

													@Override
													public void onSuccess(
															Boolean response) {
														
														Log.info("Done Successfully");
														if (osceDayViewImplTemp.getSequenceOsceSubViewImplList().size() == 2)
														{
															SequenceOsceSubViewImpl firstSequenceOsce = osceDayViewImplTemp.getSequenceOsceSubViewImplList().get(0);
															SequenceOsceSubViewImpl secondSequenceOsce = osceDayViewImplTemp.getSequenceOsceSubViewImplList().get(1);
															
															int firstRotation = Integer.parseInt(firstSequenceOsce.getSequenceRotationLable().getText()) - 1;
															int secondRotation = Integer.parseInt(secondSequenceOsce.getSequenceRotationLable().getText()) + 1;
															firstSequenceOsce.getSequenceRotationLable().setText(String.valueOf(firstRotation));
															secondSequenceOsce.getSequenceRotationLable().setText(String.valueOf(secondRotation));
															
														}
														// Module 5 Bug Test Change
														requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
														// E Module 5 Bug Test Change
													}
													
													@Override
													public void onFailure(
															ServerFailure error) {
														// TODO Auto-generated method stub
														super.onFailure(error);
														// Module 5 Bug Test Change
														requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
														// E Module 5 Bug Test Change
													}
													
													@Override
													public void onViolation(
															Set<Violation> errors) {
														// TODO Auto-generated method stub
														super.onViolation(errors);
														// Module 5 Bug Test Change
														requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
														// E Module 5 Bug Test Change
													}
													
												});
												
										}
										@Override
										public void onFailure(
												ServerFailure error) {
											// TODO Auto-generated method stub
											super.onFailure(error);
											// Module 5 Bug Test Change
											requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
											// E Module 5 Bug Test Change
										}
										
										@Override
										public void onViolation(
												Set<Violation> errors) {
											// TODO Auto-generated method stub
											super.onViolation(errors);
											// Module 5 Bug Test Change
											requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
											// E Module 5 Bug Test Change
										}
									});
								}
								
								@Override
								public void onFailure(ServerFailure error) {
									// TODO Auto-generated method stub
									super.onFailure(error);
									// Module 5 Bug Test Change
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									// E Module 5 Bug Test Change
								}
								
								@Override
								public void onViolation(Set<Violation> errors) {
									// TODO Auto-generated method stub
									super.onViolation(errors);
									// Module 5 Bug Test Change
									requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
									// E Module 5 Bug Test Change
								}
							});
						}

					}
					catch(Exception e)
					{
						System.out.println(e.getMessage());
					}
										
					
				}

          static class OsceProxyVerifier {

					public static void verifyOsce(OsceProxy osce ) throws Exception {
						if(osce.getMaxNumberStudents() == null || osce.getMaxNumberStudents() <= 0)
							throw new Exception("maximum number of students");
						
						if(osce.getNumberRooms() == null || osce.getNumberRooms() <= 0)
							throw new Exception("number of rooms available");
						
						if(osce.getPostLength() == null || osce.getPostLength() <= 0)
							throw new Exception("post length");
						
						if(osce.getShortBreak() == null || osce.getShortBreak() <= 0)
							throw new Exception("duration of short break (after a post)");
						
						if(osce.getShortBreakSimpatChange() == null || osce.getShortBreakSimpatChange() <= 0)
							throw new Exception("duration of simpat change break (when a change of simpat is needed WITHIN rotation)");
						
						if(osce.getMiddleBreak() == null || osce.getMiddleBreak() <= 0)
							throw new Exception("duration of middle break (after a rotation)");
						
						if(osce.getLongBreak() == null || osce.getLongBreak() <= 0)
							throw new Exception("duration of long break (when a change of simpat is needed AFTER rotation)");
						
						if(osce.getLunchBreak() == null || osce.getLunchBreak() <= 0)
							throw new Exception("duration of lunch break");
						
						if(osce.getNumberCourses() == null || osce.getNumberCourses() <= 0)
							throw new Exception("Number of Courses");
						
						if(osce.getNumberPosts() == null || osce.getNumberPosts() <= 0)
							throw new Exception("Number of posts");
												
						
					}
				}

	@Override
	public void refreshParcourContent(AccordianPanelViewImpl view,
			Widget header, OsceSequenceProxy osceSequenceProxy) {

		final HeaderView parHeaderView = (HeaderView) header;

		final CourseProxy courseProxy = parHeaderView.getProxy();

		final ContentViewImpl contentView = (ContentViewImpl) parHeaderView
				.getContentView();
		contentView.getPostHP().clear();
		contentView.getOscePostHP().clear();
		contentView.addStyleName("accordion-title-selected"
				+ courseProxy.getColor());
		contentView.setHeight("268px");
		contentView.getContentPanel().getElement().getStyle()
				.setWidth(100, Unit.PCT);
		contentView.getScrollPanel().getElement().getStyle()
				.setWidth(100, Unit.PCT);

		requests.osceSequenceRequest()
				.findOsceSequence(osceSequenceProxy.getId())
				.with("oscePosts", "oscePosts.oscePostBlueprint",
						"oscePosts.standardizedRole",
						"oscePosts.oscePostBlueprint.postType",
						"oscePosts.oscePostBlueprint.specialisation",
						"oscePosts.oscePostBlueprint.roleTopic")
				.fire(new OSCEReceiver<OsceSequenceProxy>() {

					@Override
					public void onSuccess(OsceSequenceProxy response) {

						Iterator<OscePostProxy> oscePostIterator = response
								.getOscePosts().iterator();

						while (oscePostIterator.hasNext()) {

							OscePostProxy oscePostProxy = oscePostIterator
									.next();

							if (oscePostProxy.getOscePostBlueprint()
									.getPostType()
									.equals(PostType.ANAMNESIS_THERAPY)
									|| oscePostProxy.getOscePostBlueprint()
											.getPostType()
											.equals(PostType.PREPARATION)) {
								createAnamnesisTherapyPost(contentView,
										oscePostProxy, oscePostIterator.next(),
										courseProxy);
							} else if (response.getOscePosts().size() == 1) {
								createUndraggablePost(contentView,
										oscePostProxy, courseProxy);
							} else {
								createOscePost(contentView, oscePostProxy,
										courseProxy);
							}
						}
					}
				});

	}                
}
